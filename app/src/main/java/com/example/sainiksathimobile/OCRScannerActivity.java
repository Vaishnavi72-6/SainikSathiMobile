package com.example.sainiksathimobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.sainiksathimobile.utils.SmartPDFGenerator;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OCRScannerActivity extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST_CODE = 101;
    private static final int CAMERA_CAPTURE_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private TextView tvExtractedText;
    private EditText etName, etDob, etRank, etPpo;
    private Button btnCaptureText;

    private ExecutorService cameraExecutor;
    private String lastExtractedText = "";
    private List<Bitmap> supportingBitmaps = new ArrayList<>();

    private boolean fromSmartScan = false;

    private Uri photoURI;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_scanner);

        fromSmartScan = getIntent().getBooleanExtra("fromSmartScan", false);

        tvExtractedText = findViewById(R.id.tvExtractedText);
        etName = findViewById(R.id.etName);
        etDob = findViewById(R.id.etDob);
        etRank = findViewById(R.id.etRank);
        etPpo = findViewById(R.id.etPpo);

        btnCaptureText = findViewById(R.id.btnCaptureText);
        btnCaptureText.setOnClickListener(v -> checkAndRequestCameraPermission());

        findViewById(R.id.btnAttachDocs).setOnClickListener(v -> openImagePicker());
        findViewById(R.id.btnExportAutoPdf).setOnClickListener(v -> exportAutoFilledFormAsPdf());

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void checkAndRequestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                openCameraForTextCapture();
            }
        } else {
            openCameraForTextCapture();
        }
    }

    private void openCameraForTextCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_CAPTURE_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraForTextCapture();
            } else {
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            if (bitmap != null) {
                runTextRecognition(bitmap);
            } else {
                Toast.makeText(this, "Failed to load captured image", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        supportingBitmaps.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    supportingBitmaps.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            generateFullPacketPdf();
        }
    }

    private void runTextRecognition(Bitmap bitmap) {
        try {
            InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(inputImage)
                    .addOnSuccessListener(result -> {
                        lastExtractedText = result.getText();
                        tvExtractedText.setText(lastExtractedText);
                        extractAndFillFields(lastExtractedText);

                        if (fromSmartScan) {
                            // Handle any PDF generation or navigation here safely
                        }
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(this, "OCR Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error during OCR processing", Toast.LENGTH_SHORT).show();
        }
    }

    private void extractAndFillFields(String text) {
        if (text == null || text.isEmpty()) return;

        String lowerText = text.toLowerCase();

        if (etName.getText().toString().isEmpty()) {
            String name = extractValueAfterKeyword(lowerText, "name");
            if (!name.isEmpty()) etName.setText(name);
        }

        if (etRank.getText().toString().isEmpty()) {
            String rank = extractValueAfterKeyword(lowerText, "rank");
            if (!rank.isEmpty()) etRank.setText(rank);
        }

        if (etPpo.getText().toString().isEmpty()) {
            String ppo = extractValueAfterKeyword(lowerText, "ppo");
            if (!ppo.isEmpty()) etPpo.setText(ppo);
        }

        if (etDob.getText().toString().isEmpty()) {
            String dob = extractValueAfterKeyword(lowerText, "dob");
            if (!dob.isEmpty()) etDob.setText(dob);
        }
    }

    private String extractValueAfterKeyword(String text, String keyword) {
        String pattern = keyword + "\\s*[:\\-]?\\s*(.+)";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = r.matcher(text);
        if (m.find()) {
            String value = m.group(1);
            if (value.contains("\n")) {
                value = value.substring(0, value.indexOf("\n")).trim();
            }
            if (value.contains(",")) {
                value = value.substring(0, value.indexOf(",")).trim();
            }
            return value.trim();
        }
        return "";
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Documents"), PICK_IMAGES_REQUEST_CODE);
    }

    private void exportAutoFilledFormAsPdf() {
        View formView = getLayoutInflater().inflate(R.layout.layout_filled_form, null);

        ((TextView) formView.findViewById(R.id.tvName)).setText("Name: " + etName.getText().toString());
        ((TextView) formView.findViewById(R.id.tvRank)).setText("Rank: " + etRank.getText().toString());
        ((TextView) formView.findViewById(R.id.tvPPO)).setText("PPO Number: " + etPpo.getText().toString());
        ((TextView) formView.findViewById(R.id.tvDOB)).setText("Date of Birth: " + etDob.getText().toString());

        Bitmap signature = BitmapFactory.decodeFile(getFilesDir() + "/signatures/user_signature.png");
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo);

        ((ImageView) formView.findViewById(R.id.signatureView)).setImageBitmap(signature);
        ((ImageView) formView.findViewById(R.id.logoView)).setImageBitmap(logo);

        SmartPDFGenerator.createPdfFromView(this, formView, "AutoFilled_Pension_Form");
    }

    private void generateFullPacketPdf() {
        View pageOne = getLayoutInflater().inflate(R.layout.layout_filled_form, null);

        ((TextView) pageOne.findViewById(R.id.tvName)).setText("Name: " + etName.getText().toString());
        ((TextView) pageOne.findViewById(R.id.tvRank)).setText("Rank: " + etRank.getText().toString());
        ((TextView) pageOne.findViewById(R.id.tvPPO)).setText("PPO: " + etPpo.getText().toString());
        ((TextView) pageOne.findViewById(R.id.tvDOB)).setText("Date of Birth: " + etDob.getText().toString());

        Bitmap signature = BitmapFactory.decodeFile(getFilesDir() + "/signatures/user_signature.png");
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.sainik_logo);

        ((ImageView) pageOne.findViewById(R.id.signatureView)).setImageBitmap(signature);
        ((ImageView) pageOne.findViewById(R.id.logoView)).setImageBitmap(logo);

        SmartPDFGenerator.generateMultiPagePensionPacket(
                this,
                pageOne,
                supportingBitmaps,
                "Full_Pension_Packet"
        );
    }
}
