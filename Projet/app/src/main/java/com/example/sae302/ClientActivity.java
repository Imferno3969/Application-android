package com.example.sae302;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private EditText editTextPort;
    private EditText editTextIPAddress;
    private Button buttonConnect;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private TextView textViewStatus;

    private Socket clientSocket;
    private BufferedReader inputReader;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        editTextPort = findViewById(R.id.editTextPort);
        editTextIPAddress = findViewById(R.id.editTextIPAddress);
        buttonConnect = findViewById(R.id.buttonConnect);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        textViewStatus = findViewById(R.id.textViewStatus);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToServer();
            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // Ajouter le bouton pour revenir à la page principale
        Button buttonBackToMain = findViewById(R.id.buttonBackToMain);
        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Ferme l'activité actuelle pour revenir à la principale
            }
        });
    }

    private void connectToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean connectionEstablished = false;

                try {
                    int port = Integer.parseInt(editTextPort.getText().toString());
                    String ipAddress = editTextIPAddress.getText().toString();

                    clientSocket = new Socket(ipAddress, port);
                    inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outputStream = clientSocket.getOutputStream();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Connected to server");
                        }
                    });

                    connectionEstablished = true;

                    while (connectionEstablished) {
                        try {
                            final String receivedMessage = inputReader.readLine();
                            if (receivedMessage != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewStatus.setText("Received message: " + receivedMessage);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Gérer la déconnexion du client ici
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewStatus.setText("Connection lost");
                                }
                            });
                            connectionEstablished = false; // Terminer la boucle en cas d'erreur
                        }
                    }

                } catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Connection failed: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }



    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = editTextMessage.getText().toString();
                    outputStream.write((message + "\n").getBytes());
                    outputStream.flush();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Message sent");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Failed to send message");
                        }
                    });
                }
            }
        }).start();
    }
}