package com.example.sainiksathimobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.sainiksathimobile.utils.QRCodeGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PensionCertificateActivity extends AppCompatActivity {

    private File pdfFile;
    private Bitmap userSignatureBitmap;
    private ImageView pdfPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pension_certificate);

        // Load signature if exists
        File signatureFile = new File(getFilesDir(), "signature.png");
        if (signatureFile.exists()) {
            userSignatureBitmap = BitmapFactory.decodeFile(signatureFile.getAbsolutePath());
        }

        pdfPreview = findViewById(R.id.pdfPreview);
        Button btnGenerate = findViewById(R.id.btnGeneratePDF);
        Button btnView = findViewById(R.id.btnViewPDF);
        Button btnDownload = findViewById(R.id.btnDownloadCertificate);
        Button btnShare = findViewById(R.id.btnShareCertificate);

        btnGenerate.setOnClickListener(v -> generateCertificate());
        btnView.setOnClickListener(v -> {
            if (pdfFile != null) viewPDF(pdfFile);
        });
        btnDownload.setOnClickListener(v -> {
            if (pdfFile != null) {
                Toast.makeText(this, "Certificate downloaded to internal storage", Toast.LENGTH_SHORT).show();
            }
        });
        btnShare.setOnClickListener(v -> {
            if (pdfFile != null) sharePDF(pdfFile);
        });
    }

    private void generateCertificate() {
        String name = "Subedar Major Rakesh Singh";
        String rank = "Subedar Major";
        String pensionId = "PEN123456789";
        String pensionAmount = "₹ 48,500";
        String issuedDate = "July 03, 2025";

        Bitmap qrBitmap = QRCodeGenerator.generateQRCode(pensionId, 100, 100);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);

        int y = 80;
        canvas.drawText("Sainik Sathi – Pension Certificate", 140, y, paint);
        y += 40;
        canvas.drawText("Name: " + name, 40, y, paint);
        y += 30;
        canvas.drawText("Rank: " + rank, 40, y, paint);
        y += 30;
        canvas.drawText("Pension ID: " + pensionId, 40, y, paint);
        y += 30;
        canvas.drawText("Monthly Pension: " + pensionAmount, 40, y, paint);
        y += 30;
        canvas.drawText("Issued Date: " + issuedDate, 40, y, paint);

        y += 40;
        canvas.drawText("QR Code:", 40, y, paint);
        if (qrBitmap != null) {
            canvas.drawBitmap(qrBitmap, 250, y - 40, null);
        }

        y += 140;
        canvas.drawText("Authorized Signature:", 40, y, paint);

        // Draw signature box
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(250, y - 60, 450, y + 20, paint);
        paint.setStyle(Paint.Style.FILL);

        if (userSignatureBitmap != null) {
            Bitmap scaled = Bitmap.createScaledBitmap(userSignatureBitmap, 180, 60, false);
            canvas.drawBitmap(scaled, 255, y - 50, null);
        } else {
            canvas.drawText("No signature found", 260, y - 10, paint);
        }

        document.finishPage(page);

        try {
            File dir = new File(getFilesDir(), "certificates");
            if (!dir.exists()) dir.mkdirs();
            pdfFile = new File(dir, "Pension_Certificate.pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewPDF(File file) {
        try {
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(fileDescriptor);
            if (renderer.getPageCount() > 0) {
                PdfRenderer.Page page = renderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                pdfPreview.setImageBitmap(bitmap);
                page.close();
            }
            renderer.close();
            fileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error viewing PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePDF(File file) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Pension Certificate"));
    }
}
