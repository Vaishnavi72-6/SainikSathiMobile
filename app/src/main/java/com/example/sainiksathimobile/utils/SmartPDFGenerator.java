package com.example.sainiksathimobile.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.sainiksathimobile.PdfViewerActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SmartPDFGenerator {

    // ✅ Enhanced OCR PDF with Signature + Logo + QR
    public static void generateEnhancedPDF(Context context, String ocrText, String fileName,
                                           Bitmap signatureBitmap, Bitmap logoBitmap,
                                           String name, String rank, String ppoNumber) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        paint.setTextSize(14);
        paint.setColor(Color.BLACK);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 40;
        int y = 60;

        if (logoBitmap != null) {
            Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 80, 80, false);
            canvas.drawBitmap(scaledLogo, 480, 30, null);
        }

        for (String line : ocrText.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent() + 6;
            if (y > 700) break;
        }

        // Generate and draw QR Code
        String qrData = "Name: " + name + "\nPPO: " + ppoNumber + "\nRank: " + rank;
        try {
            Bitmap qrBitmap = generateQRCode(qrData);
            if (qrBitmap != null) {
                Bitmap scaledQR = Bitmap.createScaledBitmap(qrBitmap, 100, 100, false);
                canvas.drawBitmap(scaledQR, 440, 700, null);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Signature
        if (signatureBitmap != null) {
            Bitmap scaledSign = Bitmap.createScaledBitmap(signatureBitmap, 200, 80, false);
            canvas.drawText("Signature:", x, 760, paint);
            canvas.drawBitmap(scaledSign, x + 100, 730, null);
        }

        pdfDocument.finishPage(page);

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");
            FileOutputStream out = new FileOutputStream(file);
            pdfDocument.writeTo(out);
            pdfDocument.close();
            Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "PDF Save Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // ✅ PDF from a View (Auto-Filled Form)
    public static void createPdfFromView(Context context, View view, String filename) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(792, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1120, View.MeasureSpec.EXACTLY)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        PdfDocument pdfDoc = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDoc.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDoc.finishPage(page);

        File file = new File(context.getExternalFilesDir(null), filename + ".pdf");
        try {
            pdfDoc.writeTo(new FileOutputStream(file));
            pdfDoc.close();

            Toast.makeText(context, "Form PDF saved", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra(PdfViewerActivity.EXTRA_PDF_FILENAME, filename);
            context.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Full Packet Generator with Attachments
    public static void generateMultiPagePensionPacket(Context context, View mainFormView, List<Bitmap> attachedBitmaps, String filename) {
        PdfDocument pdfDocument = new PdfDocument();

        // Page 1 from View
        mainFormView.measure(
                View.MeasureSpec.makeMeasureSpec(792, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1120, View.MeasureSpec.EXACTLY)
        );
        mainFormView.layout(0, 0, mainFormView.getMeasuredWidth(), mainFormView.getMeasuredHeight());
        Bitmap mainBitmap = Bitmap.createBitmap(mainFormView.getWidth(), mainFormView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mainCanvas = new Canvas(mainBitmap);
        mainFormView.draw(mainCanvas);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(mainBitmap.getWidth(), mainBitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        page.getCanvas().drawBitmap(mainBitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        // Add each supporting image as a new page
        int pageNumber = 2;
        for (Bitmap bitmap : attachedBitmaps) {
            PdfDocument.PageInfo imagePageInfo = new PdfDocument.PageInfo.Builder(792, 1120, pageNumber++).create();
            PdfDocument.Page imagePage = pdfDocument.startPage(imagePageInfo);

            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 792, 1120, false);
            imagePage.getCanvas().drawBitmap(scaled, 0, 0, null);
            pdfDocument.finishPage(imagePage);
        }

        try {
            File file = new File(context.getExternalFilesDir(null), filename + ".pdf");
            FileOutputStream out = new FileOutputStream(file);
            pdfDocument.writeTo(out);
            pdfDocument.close();

            Toast.makeText(context, "Multi-page Pension Packet Saved", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra(PdfViewerActivity.EXTRA_PDF_FILENAME, filename);
            context.startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(context, "Error saving packet: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // ✅ QR Code Generator (ZXing)
    private static Bitmap generateQRCode(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        int size = 200;
        com.google.zxing.common.BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);
        Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
