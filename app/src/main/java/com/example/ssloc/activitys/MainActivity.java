package com.example.ssloc.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.ssloc.R;
import com.example.ssloc.activitys.LoginOuCadastroActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        PendingIntent pIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_MUTABLE
        );

        @SuppressLint("ServiceCast") NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "not";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel n = new NotificationChannel(channelId, "notificacao", NotificationManager.IMPORTANCE_HIGH);
            n.setDescription("Sempre verifique os prazos dos planos e as manutenções dos automóveis.");
            notificationManager.createNotificationChannel(n);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.baseline_back_hand_24);
        long[] vibrationPattern = {0, 300, 100, 300, 100, 1000, 100, 1000, 100, 300, 100, 300, 100, 1000, 100, 1000, 100, 300, 100, 300, 100, 2000};
        builder.setVibrate(vibrationPattern);

        builder.setContentTitle("Atenção!!");
        builder.setContentText("Sempre verifique os prazos dos planos e as manutenções dos automóveis.");
        builder.setContentIntent(pIntent);

        notificationManager.notify(1, builder.build());
        new Handler().postDelayed(() -> {
            if (preferences.getString("DadosUsuario", null) != null) {
                if (preferences.getString("DadosUsuario", null).equals("admin233") ){
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                }
            } else {
                startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            }
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }

}