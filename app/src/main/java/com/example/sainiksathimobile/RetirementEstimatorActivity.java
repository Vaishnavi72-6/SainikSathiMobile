package com.example.sainiksathimobile;
import static com.example.sainiksathimobile.utils.IDCardGenerator.generateIdCard;

import com.example.sainiksathimobile.utils.QRCodeGenerator;
import com.example.sainiksathimobile.utils.IDCardGenerator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.sainiksathimobile.utils.QRCodeGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class RetirementEstimatorActivity extends AppCompatActivity {
    EditText nameE, rankE, yearsE, basicE, groupE, yearE, leaveE;
    TextView resultT;
    Button  btnAddSignature, btnGenerateBoth, btnViewPpo, btnSharePpo, btnViewEpfo, btnShareEpfo;
    Bitmap logoBitmap, signatureBitmap, qrBitmap;
    File ppoPdfFile, epfoPdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retirement_estimator);

        nameE = findViewById(R.id.editName);
        rankE = findViewById(R.id.editRank);
        yearsE = findViewById(R.id.editServiceYears);
        basicE = findViewById(R.id.editBasicPay);
        groupE = findViewById(R.id.editGroup);
        yearE = findViewById(R.id.editRetirementYear);
        leaveE = findViewById(R.id.editLeaveDays);
        resultT = findViewById(R.id.textResult);


        btnAddSignature = findViewById(R.id.btnAddSignature);
        btnViewPpo = findViewById(R.id.btnViewPpo);
        btnSharePpo = findViewById(R.id.btnSharePpo);
        btnViewEpfo = findViewById(R.id.btnViewEpfo);
        btnShareEpfo = findViewById(R.id.btnShareEpfo);
        btnGenerateBoth = findViewById(R.id.btnGenerateBoth);
        Button btnGenerateIdCard = findViewById(R.id.btnGenerateIdCard);

        loadAssets();

        btnAddSignature.setOnClickListener(v -> {
            promptSignatureOption(
                    () -> Toast.makeText(this, "Saved signature will be used.", Toast.LENGTH_SHORT).show(),
                    () -> startActivityForResult(new Intent(this, SignatureActivity.class), 101)
            );
        });


        btnGenerateBoth.setOnClickListener(v -> calculateAndGenerate());

        btnViewPpo.setOnClickListener(v -> {
            if (ppoPdfFile != null && ppoPdfFile.exists()) openGeneratedPdf(ppoPdfFile);
            else
                Toast.makeText(this, "Please generate PPO certificate first", Toast.LENGTH_SHORT).show();
        });

        btnSharePpo.setOnClickListener(v -> {
            if (ppoPdfFile != null && ppoPdfFile.exists()) sharePdf(ppoPdfFile);
            else
                Toast.makeText(this, "Please generate PPO certificate first", Toast.LENGTH_SHORT).show();
        });

        btnViewEpfo.setOnClickListener(v -> {
            if (epfoPdfFile != null && epfoPdfFile.exists()) openGeneratedPdf(epfoPdfFile);
            else
                Toast.makeText(this, "Please generate EPFO certificate first", Toast.LENGTH_SHORT).show();
        });

        btnShareEpfo.setOnClickListener(v -> {
            if (epfoPdfFile != null && epfoPdfFile.exists()) sharePdf(epfoPdfFile);
            else
                Toast.makeText(this, "Please generate EPFO certificate first", Toast.LENGTH_SHORT).show();
        });
        btnGenerateIdCard.setOnClickListener(v -> {
            String name = nameE.getText().toString().trim();
            String rank = rankE.getText().toString().trim();
            String dob = "01-01-1970";
            String idNumber = "ESM" + System.currentTimeMillis();
            String serviceYears = "1990 - 2020";
            String address = "Hyderabad, Telangana";

            File signatureFile = new File(getExternalFilesDir(null), "signature.png");
            signatureBitmap = BitmapFactory.decodeFile(signatureFile.getAbsolutePath());
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo);

            Bitmap qrBitmap = QRCodeGenerator.generateQRCode(name + " | ID: " + idNumber, 300, 300);

            File idCardFile = generateIdCard(
                    this,
                    name,
                    rank,
                    dob,
                    idNumber,
                    serviceYears,
                    address,
                    qrBitmap,
                    signatureBitmap
            );

            if (idCardFile != null && idCardFile.exists()) {
                Intent intent = new Intent(this, PdfViewerActivity.class);
                intent.putExtra(PdfViewerActivity.EXTRA_PDF_PATH, idCardFile.getAbsolutePath());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to generate ID card", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void promptSignatureOption(Runnable onAutoSign, Runnable onReSign) {
        new AlertDialog.Builder(this)
                .setTitle("Choose Signature Option")
                .setMessage("Use saved signature or re-sign?")
                .setPositiveButton("Use Saved", (d, w) -> onAutoSign.run())
                .setNegativeButton("Re-Sign", (d, w) -> onReSign.run())
                .show();
    }

    private void calculateAndGenerate() {
        try {
            String name = nameE.getText().toString().trim();
            String rank = rankE.getText().toString().trim();
            int years = Integer.parseInt(yearsE.getText().toString());
            double basicPay = Double.parseDouble(basicE.getText().toString());
            String group = groupE.getText().toString().toUpperCase();
            int leaveDays = Integer.parseInt(leaveE.getText().toString());
            int retireYear = Integer.parseInt(yearE.getText().toString());

            double basePension = basicPay * 0.5;
            double gratuity = Math.min(basicPay * 15 * years / 26, 2000000);
            double commutation = basePension * 0.4 * 148.33;
            double leaveEnc = basicPay / 30 * leaveDays;
            double pensionWithDA = basePension * 1.46;
            double totalSettlement = gratuity + commutation + leaveEnc;

            resultT.setText(String.format(Locale.getDefault(),
                    "Name: %s\nMonthly Pension: ₹%.0f\nGratuity: ₹%.0f\nCommutation: ₹%.0f\nLeave Encashment: ₹%.0f\n\nTotal: ₹%.0f",
                    name, pensionWithDA, gratuity, commutation, leaveEnc, totalSettlement));

            generatePpoPdf(name, rank, years, basicPay, group, retireYear, pensionWithDA, gratuity, commutation, leaveEnc);
            generateEpfoPdf(name, rank, retireYear);

        } catch (Exception e) {
            Toast.makeText(this, "Fill all fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void generatePpoPdf(String name, String rank, int yrs, double basic, String grp, int ry,
                                double pension, double gratuity, double commutation, double leaveEnc) {
        int width = 595, height = 842;
        Bitmap ppoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ppoBitmap);
        Paint paint = new Paint();
        canvas.drawColor(0xFFFFFFFF);
        int y = 60;

        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo); // Replace with your image name
        if (logoBitmap != null) {
            Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 100, 100, true);
            canvas.drawBitmap(scaledLogo, 30, y, paint); // Adjust X and Y as needed
            y += 120; // Move below logo for next content
        }

        paint.setColor(0xFF000000);
        paint.setTextSize(18f);
        paint.setFakeBoldText(true);
        canvas.drawText("PPO (Pension Payment Order) Certificate", 100, y, paint);
        y += 30;

        paint.setFakeBoldText(false);
        paint.setTextSize(14f);
        canvas.drawText("Name: " + name, 40, y += 30, paint);
        canvas.drawText("Rank: " + rank, 40, y += 30, paint);
        canvas.drawText("Service Years: " + yrs, 40, y += 25, paint);
        canvas.drawText("Basic Pay: ₹" + basic, 40, y += 25, paint);
        canvas.drawText("Service Group: " + grp, 40, y += 25, paint);
        canvas.drawText("Retirement Year: " + ry, 40, y += 25, paint);

        canvas.drawText("Calculated Pension Details:", 40, y += 30, paint);
        canvas.drawText("Monthly Pension with DA: ₹" + (int) pension, 40, y += 25, paint);
        canvas.drawText("Gratuity: ₹" + (int) gratuity, 40, y += 25, paint);
        canvas.drawText("Commutation: ₹" + (int) commutation, 40, y += 25, paint);
        canvas.drawText("Leave Encashment: ₹" + (int) leaveEnc, 40, y += 25, paint);

        String qrData = "Name: " + name + "\nRank: " + rank + "\nRetirement Year: " + ry;
        qrBitmap = QRCodeGenerator.generateQRCode(qrData, 250, 250);
        if (qrBitmap != null) {
            canvas.drawText("QR:", 40, y += 30, paint);
            Bitmap scaledQr = Bitmap.createScaledBitmap(qrBitmap, 100, 100, false);
            canvas.drawBitmap(scaledQr, 40, y += 10, paint);
        }

        if (signatureBitmap != null) {
            Bitmap scaledSig = Bitmap.createScaledBitmap(signatureBitmap, 150, 80, false);
            canvas.drawBitmap(scaledSig, width - 180, height - 140, paint);
            paint.setTextSize(12f);
            canvas.drawText("Authorized Signature", width - 180, height - 50, paint);
        }

        ppoPdfFile = createPdfFromBitmap(ppoBitmap, "ppo_certificate.pdf");
    }

    private void generateEpfoPdf(String name, String rank, int retireYear) {
        int width = 595, height = 842;
        Bitmap epfoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(epfoBitmap);
        Paint paint = new Paint();
        canvas.drawColor(0xFFFFFFFF);
        int y = 60;

        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo); // Replace with your image name
        if (logoBitmap != null) {
            Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 100, 100, true);
            canvas.drawBitmap(scaledLogo, 30, y, paint); // Adjust X and Y as needed
            y += 120; // Move below logo for next content
        }


        paint.setColor(0xFF000000);
        paint.setTextSize(18f);
        paint.setFakeBoldText(true);
        canvas.drawText("EPFO Retirement Benefits Certificate", 70, y, paint);
        y += 30;

        paint.setFakeBoldText(false);
        paint.setTextSize(14f);
        canvas.drawText("Name: " + name, 40, y += 30, paint);
        canvas.drawText("Rank: " + rank, 40, y += 30, paint);
        canvas.drawText("Retirement Year: " + retireYear, 40, y += 25, paint);
        canvas.drawText("EPFO settlement details processed successfully.", 40, y += 25, paint);

        String qrData = "Name: " + name + "\nRank: " + rank + "\nRetirement Year: " + retireYear;
        Bitmap qrBitmap = QRCodeGenerator.generateQRCode(qrData, 250, 250);
        if (qrBitmap != null) {
            canvas.drawText("QR:", 40, y += 30, paint);
            Bitmap scaledQr = Bitmap.createScaledBitmap(qrBitmap, 100, 100, false);
            canvas.drawBitmap(scaledQr, 40, y += 10, paint);
        }

        if (signatureBitmap != null) {
            Bitmap scaledSig = Bitmap.createScaledBitmap(signatureBitmap, 150, 80, false);
            canvas.drawBitmap(scaledSig, width - 180, height - 140, paint);
            paint.setTextSize(12f);
            canvas.drawText("Authorized Signature", width - 180, height - 50, paint);
        }

        epfoPdfFile = createPdfFromBitmap(epfoBitmap, "epfo_certificate.pdf");
    }

    private File createPdfFromBitmap(Bitmap bmp, String filename) {
        PdfDocument doc = new PdfDocument();
        PdfDocument.Page page = doc.startPage(new PdfDocument.PageInfo.Builder(bmp.getWidth(), bmp.getHeight(), 1).create());
        page.getCanvas().drawBitmap(bmp, 0, 0, null);
        doc.finishPage(page);

        File file = new File(getExternalFilesDir(null), filename);
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.writeTo(out);
            Toast.makeText(this, filename + " generated", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.close();
        return file;
    }

    private void openGeneratedPdf(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open Certificate"));
    }

    private void sharePdf(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Certificate via"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadAssets();
            calculateAndGenerate();
        }
    }

    private void loadAssets() {
        try {
            File sigFile = new File(getExternalFilesDir(null), "signature.png");
            File qrFile = new File(getExternalFilesDir(null), "qr_saved.png");
            File logoFile = new File(getExternalFilesDir(null), "sainik_logo.png");

            if (sigFile.exists()) signatureBitmap = BitmapFactory.decodeStream(new FileInputStream(sigFile));
            if (qrFile.exists()) qrBitmap = BitmapFactory.decodeStream(new FileInputStream(qrFile));
            if (logoFile.exists()) logoBitmap = BitmapFactory.decodeStream(new FileInputStream(logoFile));
            Log.d("Assets", "Signature: " + (signatureBitmap != null));
            Log.d("Assets", "QR: " + (qrBitmap != null));
            Log.d("Assets", "Logo: " + (logoBitmap != null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
