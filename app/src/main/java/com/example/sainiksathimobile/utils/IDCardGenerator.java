package com.example.sainiksathimobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import com.example.sainiksathimobile.R;

import java.io.File;
import java.io.FileOutputStream;

public class IDCardGenerator {

    public static File generateIdCard(
            Context context,
            String name,
            String rank,
            String dob,
            String idNumber,
            String serviceYears,
            String address,
            Bitmap qrBitmap,
            Bitmap signatureBitmap
    )
    {
        try {
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#f1f1f1"));
            canvas.drawRect(0, 0, pageInfo.getPageWidth(), pageInfo.getPageHeight(), paint);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(14f);

            canvas.drawText("EX-SERVICEMAN ID CARD", 90, 50, textPaint);
            canvas.drawText("Name: " + name, 30, 100, textPaint);
            canvas.drawText("Rank: " + rank, 30, 130, textPaint);
            canvas.drawText("DOB: " + dob, 30, 160, textPaint);
            canvas.drawText("Service Years: " + serviceYears, 30, 190, textPaint);
            canvas.drawText("Address: " + address, 30, 220, textPaint);
            canvas.drawText("PPO Number: " + idNumber, 30, 250, textPaint);

            // Add Signature
            if (signatureBitmap != null) {
                Bitmap scaledSig = Bitmap.createScaledBitmap(signatureBitmap, 100, 50, false);
                canvas.drawBitmap(scaledSig, 250, 500, null);
                canvas.drawText("Signature", 260, 570, textPaint);
            }

            // Add QR
            if (qrBitmap != null) {
                Bitmap scaledQr = Bitmap.createScaledBitmap(qrBitmap, 100, 100, false);
                canvas.drawBitmap(scaledQr, 250, 350, null);
            }
            Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sainik_logo);
            int y = 280; // Or wherever you want to start drawing the logo
// Replace with your image name
            if (logoBitmap != null) {
                Bitmap scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 100, 100, true);
                canvas.drawBitmap(scaledLogo, 30, y, paint); // Adjust X and Y as needed
                y += 120; // Move below logo for next content
            }

            document.finishPage(page);

            File file = new File(context.getExternalFilesDir(null), "id_card.pdf");
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();

            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}