package com.example.sainiksathimobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sainiksathimobile.utils.SmartPDFGenerator;

public class ManualEntryActivity extends AppCompatActivity {

    private EditText etName, etRank, etPpo, etDob;
    private Button btnExportPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        etName = findViewById(R.id.etName);
        etRank = findViewById(R.id.etRank);
        etPpo = findViewById(R.id.etPpo);
        etDob = findViewById(R.id.etDob);
        btnExportPdf = findViewById(R.id.btnExportPdf);

        btnExportPdf.setOnClickListener(v -> exportPdfWithManualData());
    }

    private void exportPdfWithManualData() {
        View formView = getLayoutInflater().inflate(R.layout.layout_filled_form, null);

        ((TextView) formView.findViewById(R.id.tvName)).setText("Name: " + etName.getText().toString());
        ((TextView) formView.findViewById(R.id.tvRank)).setText("Rank: " + etRank.getText().toString());
        ((TextView) formView.findViewById(R.id.tvPPO)).setText("PPO Number: " + etPpo.getText().toString());
        ((TextView) formView.findViewById(R.id.tvDOB)).setText("Date of Birth: " + etDob.getText().toString());

        Bitmap signature = BitmapFactory.decodeFile(getFilesDir() + "/signatures/user_signature.png");
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo);

        ((ImageView) formView.findViewById(R.id.signatureView)).setImageBitmap(signature);
        ((ImageView) formView.findViewById(R.id.logoView)).setImageBitmap(logo);

        SmartPDFGenerator.createPdfFromView(this, formView, "Manual_Entry_Pension_Form");

        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra(PdfViewerActivity.EXTRA_PDF_FILENAME, "Manual_Entry_Pension_Form");
        startActivity(intent);
    }
}
