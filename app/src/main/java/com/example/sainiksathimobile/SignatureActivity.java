package com.example.sainiksathimobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;

public class SignatureActivity extends Activity {

    private SignaturePad signaturePad;
    private Button btnClear, btnSave;
    private boolean hasSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        signaturePad = findViewById(R.id.signaturePad);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);

        // Detect if user has started signing
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                hasSigned = true;
            }

            @Override
            public void onSigned() {
                hasSigned = true;
            }

            @Override
            public void onClear() {
                hasSigned = false;
            }
        });

        btnClear.setOnClickListener(v -> signaturePad.clear());

        btnSave.setOnClickListener(v -> {
            if (!hasSigned) {
                Toast.makeText(SignatureActivity.this, "Please sign before saving", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
            try {
                File signatureFile = new File(getExternalFilesDir(null), "signature.png");

                FileOutputStream out = new FileOutputStream(signatureFile);
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                setResult(RESULT_OK);  // ✅ Notify parent
                finish();
                Toast.makeText(SignatureActivity.this, "Signature saved successfully", Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Toast.makeText(SignatureActivity.this, "Failed to save signature", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
