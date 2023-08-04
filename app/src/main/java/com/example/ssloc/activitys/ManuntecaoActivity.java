package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityManuntecaoBinding;
import com.squareup.picasso.Picasso;

public class ManuntecaoActivity extends AppCompatActivity {
    private static final int REQUEST_TROCA_IMAGE_SELECT = 1;
    private static final int REQUEST_RECIBO_IMAGE_SELECT = 2;
    private ActivityManuntecaoBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityManuntecaoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Manutenção");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.atencaooleo).into(vb.atencao);
        Picasso.get().load(R.raw.dicaoleo).into(vb.dica);

        vb.trocaBtn.setOnClickListener(trocaView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_TROCA_IMAGE_SELECT);
        });

        vb.reciboBtn.setOnClickListener( recibo -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_RECIBO_IMAGE_SELECT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TROCA_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);
            vb.trocaText.setText(imageName);


        }
        else if (requestCode == REQUEST_RECIBO_IMAGE_SELECT && resultCode == RESULT_OK && data != null){
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);

            // Exibir o nome da imagem no TextView
            vb.rebicoText.setText(imageName);

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}