package com.example.sae302;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonOpenURL = findViewById(R.id.buttonOpenURL);
        buttonOpenURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://bastenier-edouard.fr";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        // Récupérer le bouton Client
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonClient = findViewById(R.id.buttonClient);
        // Ajouter un écouteur de clic au bouton Client
        buttonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lorsque le bouton Client est cliqué, ouvrir l'activité Client
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });

        // Récupérer le bouton Server
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonServer = findViewById(R.id.buttonServer);
        // Ajouter un écouteur de clic au bouton Server
        buttonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lorsque le bouton Server est cliqué, ouvrir l'activité Server
                Intent intent = new Intent(MainActivity.this, ServerActivity.class);
                startActivity(intent);
            }
        });

        // Récupérer le bouton Client UDP
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonClientUDP = findViewById(R.id.buttonClientUDP);
        buttonClientUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClientUDPActivity.class);
                startActivity(intent);
            }
        });

        // Récupérer le bouton Server UDP
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonServerUDP = findViewById(R.id.buttonServerUDP);
        buttonServerUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ServerUDPActivity.class);
                startActivity(intent);
            }
        });

        // Bouton pour aller sur le serveur Tetris
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonTetrisServer = findViewById(R.id.buttonTetrisServer);
        buttonTetrisServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JeuActivity.class);
                startActivity(intent);
            }
        });

    }
}
