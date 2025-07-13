package com.example.sainiksathimobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class SmartPDFExporterActivity extends AppCompatActivity {

    private static final String TAG = "SmartPDFExporter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View content = inflater.inflate(R.layout.form_pdf_layout, null);

        // Get data from intent
        Intent intent = getIntent();
        Map<String, String> formFields = (Map<String, String>) intent.getSerializableExtra("formFields");

        // Set data into TextViews
        ((TextView) content.findViewById(R.id.tvName)).setText("Name: " + formFields.get("name"));
        ((TextView) content.findViewById(R.id.tvDob)).setText("Date of Birth: " + formFields.get("dob"));
        ((TextView) content.findViewById(R.id.tvRank)).setText("Rank: " + formFields.get("rank"));
        ((TextView) content.findViewById(R.id.tvPpo)).setText("PPO Number: " + formFields.get("ppo"));

        // Create PDF
        content.measure(
                View.MeasureSpec.makeMeasureSpec(595, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(842, View.MeasureSpec.EXACTLY));
        content.layout(0, 0, 595, 842);

        Bitmap bitmap = Bitmap.createBitmap(595, 842, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        content.draw(canvas);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, 0f, 0f, null);
        document.finishPage(page);

        // Save to file
        File pdfDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "GeneratedPDFs");
        if (!pdfDir.exists()) pdfDir.mkdirs();

        File pdfFile = new File(pdfDir, "PensionForm_" + System.currentTimeMillis() + ".pdf");

        try (FileOutputStream out = new FileOutputStream(pdfFile)) {
            document.writeTo(out);
            Toast.makeText(this, "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Error writing PDF", e);
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}
