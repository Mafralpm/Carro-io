package com.example.labm4.unifor_obdreaderii.Services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.androidannotations.annotations.EService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

@EService
public class ObdBluetooth extends Service {

    private static final String myUUID = "00001101-0000-1000-8000-00805F9B34FB";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket sock;
    private BluetoothDevice dev;
    private String deviceAdress;

    private Handler handler;
    private Thread serviceThread;
    private ObdDados obdDados;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        obdDados = new ObdDados();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAdress = (String) intent.getSerializableExtra("deviceAdress");

        try {
            startConnection(deviceAdress);
        } catch (IOException e) {

        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startConnection(String remoteDevice) throws IOException {
        dev = mBluetoothAdapter.getRemoteDevice(remoteDevice);
        if (dev == null)
            conectando();
        startObdConnection();
    }

    private void startObdConnection() throws IOException {
        serviceThread = new Thread(new Runnable() {

            @Override
            public void run() {

                mBluetoothAdapter.cancelDiscovery();

                try {
                    Method m = dev.getClass().getMethod("createRfcommSocket", int.class);
                    sock = (BluetoothSocket) m.invoke(dev, 1);
                    sock.connect();
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                    try {
                        sock.close();
                    } catch (IOException e1) {

                    }
                }

                try {
                    obdDados.permissoes(sock);
                    obdDados.setDados(sock);
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Dispositivo invalido.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        serviceThread.start();
    }

    public void conectando() {

        try {

            BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(deviceAdress);
            sock = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));

            if (sock != null)
                sock.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
