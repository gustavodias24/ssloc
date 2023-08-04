package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityEncerrarContratoBinding;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EncerrarContratoActivity extends AppCompatActivity {

    private Dialog dialog_encerrado;
    private ActivityEncerrarContratoBinding vb;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityEncerrarContratoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Encerrar contrato");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.termoencerracao).into(vb.encerramento);

        criar_dialog_encerrado();

        vb.encerrarBtn.setOnClickListener( encerrarView -> {
            SignaturePad signaturePad = vb.assinaturaPad;
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT); // enviar sapoha pro back

            if ( vb.checkTermos.isChecked() ){
                dialog_encerrado.show();
                vb.encerrarBtn.setText("Encerrado");
                vb.encerrarBtn.setTextColor(Color.parseColor("#FEFE01"));
            }else{
                Toast.makeText(this, "Aceite os termos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void criar_dialog_encerrado(){
        AlertDialog.Builder b = new AlertDialog.Builder(EncerrarContratoActivity.this);
        b.setTitle("Importante");
        b.setMessage("SEU CONTRATO DE ALUGUEL COM A SSLOC\n" +
                "FOI ENCERRADO DIRIJA-SE A UNIDADE\n" +
                "PARA VISTORIA E ENTREGA DO VEÍCULO. ");
        b.setPositiveButton("ok", null);
        b.setCancelable(false);
        dialog_encerrado = b.create();
    }
}