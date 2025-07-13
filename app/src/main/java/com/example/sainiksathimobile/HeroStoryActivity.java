// HeroStoryActivity.java - Full activity with multi-language support and voice narration

package com.example.sainiksathimobile;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HeroStoryActivity extends AppCompatActivity {

    EditText etName, etRank, etYears, etRetireYear;
    TextView storyText;
    Spinner languageSpinner;
    Button btnGenerateStory, btnDownloadPdf, btnSharePdf, btnPlayVoice, btnPauseVoice;
    String storyContent = "";
    File pdfFile;
    TextToSpeech tts;
    String selectedLanguage = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_story);

        etName = findViewById(R.id.etName);
        etRank = findViewById(R.id.etRank);
        etYears = findViewById(R.id.etYears);
        etRetireYear = findViewById(R.id.etRetireYear);
        storyText = findViewById(R.id.storyText);
        languageSpinner = findViewById(R.id.spinnerLanguage);

        btnGenerateStory = findViewById(R.id.btnGenerateStory);
        btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        btnSharePdf = findViewById(R.id.btnSharePdf);
        btnPlayVoice = findViewById(R.id.btnPlayVoice);
        btnPauseVoice = findViewById(R.id.btnPauseVoice);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"English", "Hindi", "Telugu", "Tamil"});
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(langAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = parent.getItemAtPosition(position).toString();
                setTTSLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) setTTSLanguage(selectedLanguage);
        });

        btnGenerateStory.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String rank = etRank.getText().toString().trim();
            String serviceYears = etYears.getText().toString().trim();
            String retireYear = etRetireYear.getText().toString().trim();

            if (name.isEmpty() || rank.isEmpty() || serviceYears.isEmpty() || retireYear.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {
                storyContent = HeroStoryGenerator.generateStory(name, rank, serviceYears, retireYear, selectedLanguage);
                storyText.setText(storyContent);
            }
        });

        btnDownloadPdf.setOnClickListener(v -> {
            if (!storyContent.isEmpty()) createPdf(storyContent);
        });

        btnSharePdf.setOnClickListener(v -> {
            if (pdfFile != null && pdfFile.exists()) sharePdf();
            else Toast.makeText(this, "Generate PDF first", Toast.LENGTH_SHORT).show();
        });

        btnPlayVoice.setOnClickListener(v -> {
            if (!storyContent.isEmpty()) tts.speak(storyContent, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        btnPauseVoice.setOnClickListener(v -> tts.stop());
    }

    private void setTTSLanguage(String lang) {
        switch (lang) {
            case "Hindi": tts.setLanguage(new Locale("hi", "IN")); break;
            case "Tamil": tts.setLanguage(new Locale("ta", "IN")); break;
            case "Telugu": tts.setLanguage(new Locale("te", "IN")); break;
            default: tts.setLanguage(new Locale("en", "IN"));
        }
    }

    private void createPdf(String text) {
        PdfDocument doc = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = doc.startPage(info);
        Canvas canvas = page.getCanvas();
        paint.setTextSize(14);
        int x = 40, y = 60;

        for (String line : text.split("\n")) {
            for (String part : wrapText(line, 85)) {
                canvas.drawText(part, x, y, paint);
                y += 20;
            }
            y += 10;
        }

        doc.finishPage(page);
        try {
            pdfFile = new File(getExternalCacheDir(), "HeroStory.pdf");
            doc.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "PDF Error", Toast.LENGTH_SHORT).show();
        }
        doc.close();
    }

    private void sharePdf() {
        Uri uri = FileProvider.getUriForFile(
                HeroStoryActivity.this,
                getPackageName() + ".provider",
                pdfFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share Story PDF via"));
    }

    private List<String> wrapText(String text, int maxChars) {
        List<String> lines = new ArrayList<>();
        while (text.length() > maxChars) {
            int breakPoint = text.lastIndexOf(' ', maxChars);
            if (breakPoint == -1) breakPoint = maxChars;
            lines.add(text.substring(0, breakPoint));
            text = text.substring(breakPoint).trim();
        }
        lines.add(text);
        return lines;
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
