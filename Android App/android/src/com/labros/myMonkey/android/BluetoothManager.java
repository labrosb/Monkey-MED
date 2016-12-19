package com.labros.myMonkey.android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.labros.myMonkey.Connection.iBluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Lampros on 1/12/2016.
 */

public class BluetoothManager implements iBluetooth
{
    public BluetoothAdapter mBluetoothAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice pairedDevice;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket mSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String incomingValue="0.0";

    private Activity activity;
    private Handler mHandler;
    private int mState;
    private boolean isConnected = false;
    private int connectionStatus = -1;
    private int pairingStatus = -1;
                                                                                            public String msg;
                                                                                            public String getMsg(){
                                                                                                return msg;
                                                                                            }
    public BluetoothManager(Activity activity, Handler handler)
    {
        this.activity = activity;
        mHandler = handler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void bluetoothEnable()
    {
        if (mBluetoothAdapter == null)
        {
            connectionStatus = -2;
        }
        else if (!isEnabled())
        {
            mBluetoothAdapter.enable();
        }
    }

    public boolean isEnabled()
    {   Boolean enabled = false;
        if (mBluetoothAdapter != null){
            enabled = mBluetoothAdapter.isEnabled();
        }
        return enabled;
    }

    private void setPairedDevices()
    {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
    }

    private boolean devicePaired(String DeviceAddress)
    {
        boolean devicePaired = false;
                                                                                         msg="";
        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                                                                                        msg=msg+"\n"+device.getAddress();
                if(DeviceAddress.equals(device.getAddress()))
                {
                    pairedDevice = device;
                    pairingStatus = 1;
                    devicePaired = true;
                    break;
                }
            }
            if( pairingStatus == -1)
            {
                pairingStatus = 0;
                connectionStatus = 0;
            }
        }
        else{
                                                                                            msg="No device found";
            pairingStatus = 0;
            connectionStatus = 0;
        }
        return devicePaired;
    }

    public void setConnection(String DeviceAddress)
    {
        if (mBluetoothAdapter != null)
        {
            if (mBluetoothAdapter.isEnabled())
            {
                setPairedDevices();
                if(devicePaired(DeviceAddress))
                {
                    try
                    {
                        msg = msg+"\n Creates socket with UUID: "+MY_UUID+"...";
                        mSocket = pairedDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    }
                    catch (IOException e1)
                    {
                        msg = msg + "\n Create Socket Error: " + e1;
                        pairingStatus = -2;
                        connectionStatus = 0;
                        Log.d(TAG, "socket not created");
                        e1.printStackTrace();
                    }
                    try
                    {
                        msg = msg+"\n Connecting..";
                        mSocket.connect();
                        msg = msg+"\n Connected!!";
                        connectionStatus = 1;
                        mmOutputStream = mSocket.getOutputStream();
                        mmInputStream = mSocket.getInputStream();
                        listenForInput();
                    }
                    catch (IOException e) {
                        msg = msg + "\n Connection Error: " + e;
                        pairingStatus = -2;
                        connectionStatus = 0;
                        try
                        {
                            msg = msg+"\n Closing Socket..";
                            mSocket.close();
                            msg = msg+"\n Socket Closed..";
                            Log.d(TAG, "Cannot connect");
                        }
                        catch (IOException e1)
                        {
                            msg = msg+"\n Error Closing Socket..";
                            Log.d(TAG, "Socket not closed");
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void disconnect()
    {
        try
        {
            msg = msg+"\n Closing Socket..";
            mSocket.close();
            msg = msg+"\n Socket Closed..";
            Log.d(TAG, "Cannot connect");
        }
        catch (IOException e1)
        {
            msg = msg+"\n Error Closing Socket..";
            Log.d(TAG, "Socket not closed");
            e1.printStackTrace();
        }
    }

    public void sendMessage(String value)
    {
        byte[] msgBuffer = value.getBytes();
        try
        {
            mmOutputStream.write(msgBuffer);
        }
        catch (IOException e)
        {
            msg = msg+"\n "+ e;
            e.printStackTrace();
        }
    }

    private void setIncomingValue(String value)
    {
        incomingValue = value;
    }
    public double getIncomingValue()
    {

        double value = Double.parseDouble(incomingValue);
        // Log.i(TAG, stringValue);

        return value;
    }

    public boolean isConnected() { return isConnected; }
    public int getConnectionStatus() { return connectionStatus; }
    public int getPairingStatus() { return pairingStatus; }

    public void listenForInput()
    {
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[256];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                int fistPacketBytes=0;
                boolean firstPacketReceived = false;
                boolean monitor = true;
                int max=0;
                int flag=0;
                long CurrentTime = System.currentTimeMillis();
                while(monitor)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();

                        if(!firstPacketReceived && bytesAvailable > 0)
                        {
                            fistPacketBytes = bytesAvailable;
                            firstPacketReceived = true;
                            System.out.println("FIRST---------->"+fistPacketBytes);
                        }

                        if (bytesAvailable > 0)
                        {
                            //System.out.println("bytesAvailable->>" + bytesAvailable);
                            int bytes = mmInputStream.read(readBuffer);
                            //System.out.println("bytes->>" + bytes);
                            if(bytes < fistPacketBytes*2 && firstPacketReceived)
                            {
                                String readMessage = new String(readBuffer, 0, bytes);
                                setIncomingValue(readMessage);
                                if (flag == 0)
                                {
                                    CurrentTime = System.currentTimeMillis();
                                    flag++;
                                }
                                else{
                                    long time = System.currentTimeMillis()-CurrentTime;
                                    System.out.println("TIME----------->"+time);
                                    flag=0;
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        System.out.println("ERROR:" + e);
                    }

                }
            }
        });

        workerThread.start();
    }
}