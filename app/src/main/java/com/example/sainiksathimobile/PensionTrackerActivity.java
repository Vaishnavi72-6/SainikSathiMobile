package com.example.sainiksathimobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PensionTrackerActivity extends AppCompatActivity {

    EditText etName, etDob, etRank, etPpo;
    Button btnGeneratePDF, btnCaptureSignature, btnViewPDF, btnSharePDF;

    File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pension_tracker);

        // Init views
        etName = findViewById(R.id.etName);
        etDob = findViewById(R.id.etDob);
        etRank = findViewById(R.id.etRank);
        etPpo = findViewById(R.id.etPpo);
        btnGeneratePDF = findViewById(R.id.btnGeneratePDF);
        btnCaptureSignature = findViewById(R.id.btnCaptureSignature);
        btnViewPDF = findViewById(R.id.btnViewPDF);
        btnSharePDF = findViewById(R.id.btnSharePDF);

        // Signature button
        btnCaptureSignature.setOnClickListener(v -> {
            Intent intent = new Intent(PensionTrackerActivity.this, SignatureActivity.class);
            startActivity(intent);
        });

        // Generate PDF
        btnGeneratePDF.setOnClickListener(v -> generatePensionCertificate());

        // View PDF
        btnViewPDF.setOnClickListener(v -> {
            pdfFile = new File(getExternalFilesDir(null), "Pension_Certificate.pdf");
            if (pdfFile.exists()) {
                Uri uri = FileProvider.getUriForFile(
                        PensionTrackerActivity.this,
                        getPackageName() + ".provider",
                        pdfFile
                );
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Certificate not found!", Toast.LENGTH_SHORT).show();
            }
        });

        // Share PDF
        btnSharePDF.setOnClickListener(v -> {
            pdfFile = new File(getExternalFilesDir(null), "Pension_Certificate.pdf");
            if (pdfFile.exists()) {
                Uri uri = FileProvider.getUriForFile(
                        PensionTrackerActivity.this,
                        getPackageName() + ".provider",
                        pdfFile
                );
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share Pension Certificate"));
            } else {
                Toast.makeText(this, "No certificate to share!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePensionCertificate() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int pageWidth = pageInfo.getPageWidth();
        int pageHeight = pageInfo.getPageHeight();

        // Paint styles
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(20);
        titlePaint.setFakeBoldText(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        Paint subTitlePaint = new Paint();
        subTitlePaint.setTextSize(16);
        subTitlePaint.setTextAlign(Paint.Align.CENTER);

        Paint labelPaint = new Paint();
        labelPaint.setTextSize(14);
        labelPaint.setFakeBoldText(true);

        Paint valuePaint = new Paint();
        valuePaint.setTextSize(14);

        Paint boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(2);
// Inside generatePensionCertificate()
        Bitmap signatureBitmap = null;
        File signatureFile = new File(getFilesDir(), "signature.png");
        if (signatureFile.exists()) {
            signatureBitmap = BitmapFactory.decodeFile(signatureFile.getAbsolutePath());
        }

        // Logo
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo);
        if (logoBitmap != null) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(logoBitmap, 80, 80, false), 40, 30, null);
        }

        // Headers
        canvas.drawText("Sainik Sathi Pro", pageWidth / 2, 60, titlePaint);
        canvas.drawText("Ministry of Defence (Unofficial)", pageWidth / 2, 90, subTitlePaint);
        canvas.drawLine(60, 105, pageWidth - 60, 105, labelPaint);
        canvas.drawText("PENSION CERTIFICATE", pageWidth / 2, 130, titlePaint);
        canvas.drawLine(60, 140, pageWidth - 60, 140, labelPaint);

        canvas.drawText("Certificate No: SSA-" + new Date().getTime(), pageWidth / 2, 160, valuePaint);

        // Input
        String name = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String rank = etRank.getText().toString().trim();
        String ppo = etPpo.getText().toString().trim();

        // Info Box
        int boxTop = 180;
        int boxBottom = boxTop + 220;
        canvas.drawRect(50, boxTop, pageWidth - 50, boxBottom, boxPaint);

        int labelX = 70;
        int valueX = 250;
        int y = boxTop + 30;

        canvas.drawText("Name:", labelX, y, labelPaint);
        canvas.drawText(name, valueX, y, valuePaint);
        y += 25;

        canvas.drawText("Rank:", labelX, y, labelPaint);
        canvas.drawText(rank, valueX, y, valuePaint);
        y += 25;

        canvas.drawText("PPO Number:", labelX, y, labelPaint);
        canvas.drawText(ppo, valueX, y, valuePaint);
        y += 25;

        canvas.drawText("Date of Birth:", labelX, y, labelPaint);
        canvas.drawText(dob, valueX, y, valuePaint);
        y += 25;

        canvas.drawText("Retirement Date:", labelX, y, labelPaint);
        canvas.drawText("01 Jan 2025", valueX, y, valuePaint);
        y += 25;

        canvas.drawText("Monthly Pension:", labelX, y, labelPaint);
        canvas.drawText("₹22,000", valueX, y, valuePaint);
        y += 40;

        // Declaration
        y = 360; // Adjusted Y position inside box

        int leftMargin = 70;
        valuePaint.setTextSize(16);
        canvas.drawText("This certifies that the above individual is a", leftMargin, y, valuePaint);
        y += 20;
        canvas.drawText("registered pensioner under the Defence Pension Scheme.", leftMargin, y, valuePaint);
        y += 50;

        // Signature
        File signFile = new File(getFilesDir(), "signature.png"); // NOT getExternalFilesDir()
        if (signFile.exists()) {
            Bitmap signBitmap = BitmapFactory.decodeFile(signFile.getAbsolutePath());
            if (signBitmap != null) {
                // ✅ Adjusted size for clearer, professional look
                Bitmap scaledSign = Bitmap.createScaledBitmap(signBitmap, 250, 100, false); // width x height
                canvas.drawBitmap(scaledSign, 70, y, null); // x = 70 (left margin), y = dynamic based on position

                // ✅ Aligned label text below signature
                valuePaint.setTextSize(16);
                canvas.drawText(" Signature", 100, y + 120, valuePaint);
            } else {
                canvas.drawText("Signature decode failed", 70, y, valuePaint);
            }
        } else {
            canvas.drawText("No Signature Found", 70, y, valuePaint);
        }

        // QR Code
        try {
            // Prepare QR content
            String qrContent = "PPO No: " + etPpo + "\nName: " + etName + "\nRank: " + rank;

            // Generate QR bitmap
            BitMatrix matrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap qrBitmap = encoder.createBitmap(matrix);

            // Start PDF generation

            // Draw QR Code
            if (qrBitmap != null) {
                // Step 1: Resize the QR code
                Bitmap scaledQr = Bitmap.createScaledBitmap(qrBitmap, 100, 100, true);

                // Step 2: Set new position - move it down inside the box
                int qrX = 420;  // right side inside box
                int qrY = 230;  // move it down (adjust as needed)

                // Step 3: Draw it
                canvas.drawBitmap(scaledQr, qrX, qrY, null);
            }
            else {
                canvas.drawText("QR Code Not Generated", 400, 100, valuePaint);
            }

            // Draw footer
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            canvas.drawText("Issued by: Sainik Sathi Pro", 70, pageHeight - 100, valuePaint);
            canvas.drawText("Issued on: " + currentDate, 70, pageHeight - 80, valuePaint);

            valuePaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Auto-generated by Sainik Sathi Pro App", pageWidth / 2, pageHeight - 40, valuePaint);

            pdfDocument.finishPage(page); // Done after drawing everything

            // Save PDF
            File pdfFile = new File(getExternalFilesDir(null), "Pension_Certificate.pdf");
            FileOutputStream out = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(out);
            out.close();
            Toast.makeText(this, "Certificate Generated successfully!", Toast.LENGTH_SHORT).show();

            pdfDocument.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "QR or PDF generation failed!", Toast.LENGTH_SHORT).show();
        }
    }
}