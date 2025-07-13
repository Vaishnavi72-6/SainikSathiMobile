package com.example.sainiksathimobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignatureStorage {

    // Save bitmap and return its Uri
    public static Uri saveSignature(Context context, Bitmap signatureBitmap) {
        File signatureDir = new File(context.getFilesDir(), "signatures");
        if (!signatureDir.exists()) {
            signatureDir.mkdirs();
        }

        File signatureFile = new File(signatureDir, "signature.png");
        try {
            FileOutputStream fos = new FileOutputStream(signatureFile);
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            return Uri.fromFile(signatureFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Load saved signature as Bitmap
    public static File getSignatureFile(Context context) {
        return new File(context.getFilesDir(), "signatures/signature.png");
    }
}
