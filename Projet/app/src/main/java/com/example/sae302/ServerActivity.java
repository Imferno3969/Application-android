package com.example.sae302;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class ServerActivity extends AppCompatActivity {

    private Button buttonStartServer;
    private TextView textViewIPAddress;
    private TextView textViewPort;
    private Button buttonBackToMain;
    private TextView textViewServerStatus;
    // Déclaration de la TextView pour afficher les messages reçus
    private TextView textViewReceivedMessages;

    private boolean serverRunning = false;
    private ServerSocket serverSocket;
    private boolean firstConnection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        buttonStartServer = findViewById(R.id.buttonStartServer);
        textViewIPAddress = findViewById(R.id.textViewIPAddress);
        textViewPort = findViewById(R.id.textViewPort);
        buttonBackToMain = findViewById(R.id.buttonBackToMain);
        textViewServerStatus = findViewById(R.id.textViewServerStatus);
        // Initialisation de la TextView pour afficher les messages reçus
        textViewReceivedMessages = findViewById(R.id.textViewReceivedMessages);

        buttonStartServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!serverRunning) {
                    startServer();
                }
            }
        });

        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(0);
                    serverRunning = true;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewIPAddress.setText("Adresse IP serveur : " + getLocalIpAddress());
                            textViewPort.setText("Port serveur : " + serverSocket.getLocalPort());
                            textViewServerStatus.setText("Status du serveur : en ligne...");
                        }
                    });

                    while (serverRunning) {
                        Socket clientSocket = serverSocket.accept();
                        // Create a new thread to handle each client
                        Thread clientThread = new Thread(new ClientHandler(clientSocket));
                        clientThread.start();

                        // Afficher une notification lorsque le client se connecte
                        final String clientAddress = clientSocket.getInetAddress().getHostAddress();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewReceivedMessages.append("Client connecté: " + clientAddress + "\n");
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeServerSocket();
                }
            }
        }).start();
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLocalIpAddress() {
        try {
            InetAddress inetAddress = getWifiInetAddress();
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InetAddress getWifiInetAddress() {
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        android.net.DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        if (dhcpInfo != null) {
            int ip = dhcpInfo.ipAddress;
            try {
                return InetAddress.getByAddress(new byte[]{(byte) (ip & 0xff), (byte) (ip >> 8 & 0xff), (byte) (ip >> 16 & 0xff), (byte) (ip >> 24 & 0xff)});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Inner class to handle each client in a separate thread
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            BufferedReader inputReader = null;
            try {
                inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                final String clientAddress = clientSocket.getInetAddress().getHostAddress();

                while (serverRunning) {
                    final String receivedMessage = inputReader.readLine();
                    if (receivedMessage != null) {
                        // Check if the message starts with '[' or '{'
                        if (receivedMessage.trim().startsWith("[") || receivedMessage.trim().startsWith("{")) {
                            // Skip processing this message, as it's in a special format
                            continue;
                        } else if (receivedMessage.trim().startsWith("\"")) {
                            // Remove all '"' characters from the message
                            final String modifiedMessage = receivedMessage.replaceAll("\"", "");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewReceivedMessages.append(clientAddress + " : " + modifiedMessage + "\n");
                                }
                            });
                        } else {
                            // Remove all '{', '}', ';', and ',' characters from the message
                            final String modifiedMessage = receivedMessage.replaceAll("[{};,]", "");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewReceivedMessages.append(clientAddress + " : " + modifiedMessage + "\n");
                                }
                            });
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputReader != null) {
                        inputReader.close();
                    }
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
