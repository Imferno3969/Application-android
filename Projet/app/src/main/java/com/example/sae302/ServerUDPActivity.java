package com.example.sae302;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerUDPActivity extends AppCompatActivity {

    private TextView textViewServerStatus;
    private TextView textViewUDPPort;
    private TextView textViewUDPIP;
    private TextView textViewReceivedMessagesUDP;

    private DatagramSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_udp);

        textViewServerStatus = findViewById(R.id.textViewServerStatus);
        textViewUDPPort = findViewById(R.id.textViewUDPPort);
        textViewUDPIP = findViewById(R.id.textViewUDPIP);
        textViewReceivedMessagesUDP = findViewById(R.id.textViewReceivedMessagesUDP);

        Button buttonStartUDPServer = findViewById(R.id.buttonStartUDPServer);
        buttonStartUDPServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUDPServer();
            }
        });

        Button buttonBackToMainUDPServer = findViewById(R.id.buttonBackToMainUDPServer);
        buttonBackToMainUDPServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startUDPServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new DatagramSocket(0); // Utilise un port aléatoire
                    byte[] receiveData = new byte[1024];

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewServerStatus.setText("Server is running...");
                            textViewUDPPort.setText("Port: " + serverSocket.getLocalPort());
                            textViewUDPIP.setText("IP Address: " + getLocalIpAddress());
                        }
                    });

                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        final String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewReceivedMessagesUDP.append(message + "\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}