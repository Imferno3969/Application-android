package com.example.sae302;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class JeuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        Button buttonBackToMain = findViewById(R.id.buttonBackToMain);
        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Récupérer le bouton Client UDP
        Button buttonClientUDP = findViewById(R.id.buttonTetrisClient);
        buttonClientUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JeuActivity.this, TetrisClientActivity.class);
                startActivity(intent);
            }
        });

        // Récupérer le bouton Server UDP
        Button buttonServerUDP = findViewById(R.id.buttonTetrisServer);
        buttonServerUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JeuActivity.this, ServerTetrisActivity.class);
                startActivity(intent);
            }
        });
    }
}
