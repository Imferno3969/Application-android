package com.example.sae302;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDPActivity extends AppCompatActivity {

    // Déclaration des éléments de l'interface utilisateur
    private EditText editTextUDPPort;
    private EditText editTextUDPIP;
    private EditText editTextUDPMessage;
    private Button buttonConnectUDP;
    private Button buttonSendUDPMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_udp);

        // Liaison des éléments de l'interface utilisateur avec les vues XML
        editTextUDPPort = findViewById(R.id.editTextUDPPort);
        editTextUDPIP = findViewById(R.id.editTextUDPIP);
        editTextUDPMessage = findViewById(R.id.editTextUDPMessage);
        buttonConnectUDP = findViewById(R.id.buttonConnectUDP);
        buttonSendUDPMessage = findViewById(R.id.buttonSendUDPMessage);

        // Définition des écouteurs pour les boutons
        buttonConnectUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToUDPServer(); // Fonction de connexion au serveur UDP
            }
        });

        buttonSendUDPMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToUDPServer(); // Fonction d'envoi de message au serveur UDP
            }
        });

        // Bouton pour revenir à la page principale
        Button buttonBackToMainUDP = findViewById(R.id.buttonBackToMainUDP);
        buttonBackToMainUDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Fonction pour se connecter au serveur UDP
    private void connectToUDPServer() {
        final String serverIP = editTextUDPIP.getText().toString();
        final int serverPort = Integer.parseInt(editTextUDPPort.getText().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    byte[] sendData = new byte[1024];

                    InetAddress serverAddress = InetAddress.getByName(serverIP);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

                    socket.send(sendPacket);
                    socket.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClientUDPActivity.this, "Connecté au serveur UDP", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Fonction pour envoyer un message au serveur UDP
    private void sendMessageToUDPServer() {
        final String serverIP = editTextUDPIP.getText().toString();
        final int serverPort = Integer.parseInt(editTextUDPPort.getText().toString());
        final String message = editTextUDPMessage.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    byte[] sendData = message.getBytes();

                    InetAddress serverAddress = InetAddress.getByName(serverIP);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

                    socket.send(sendPacket);
                    socket.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClientUDPActivity.this, "Message envoyé au serveur UDP", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
