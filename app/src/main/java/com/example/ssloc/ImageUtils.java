package com.example.ssloc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {


    public static String imageToBase64(Uri imageUri, Context c) {
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String imageToBase64Comprimida(Uri imageUri, Context c) {
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Redimensionar a imagem para um tamanho menor
            int targetWidth = 800; // Largura desejada
            int targetHeight = (int) ((float) bitmap.getHeight() * targetWidth / bitmap.getWidth());
            bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

            // Converter a imagem para o formato WebP com compressão ajustável
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 70, outputStream); // 70 é a qualidade (0-100)

            // Converter para base64
            byte[] webpBytes = outputStream.toByteArray();
            return Base64.encodeToString(webpBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
