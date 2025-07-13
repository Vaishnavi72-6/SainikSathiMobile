package com.example.sainiksathimobile;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class PdfViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PDF_PATH = "pdf_path";
    public static final String EXTRA_PDF_FILENAME = "fileName";

    private ImageView pdfImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfImageView = findViewById(R.id.imageViewPdf);

        String pdfPath = getIntent().getStringExtra(EXTRA_PDF_PATH);

        if (pdfPath == null || pdfPath.isEmpty()) {
            Toast.makeText(this, "Invalid or missing file path", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        File file = new File(pdfPath);
        if (!file.exists()) {
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPdfFromFile(file);
    }

    private void loadPdfFromFile(File pdfFile) {
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(fd);

            if (renderer.getPageCount() > 0) {
                PdfRenderer.Page page = renderer.openPage(0);

                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                pdfImageView.setImageBitmap(bitmap);

                page.close();
            }

            renderer.close();
            fd.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
