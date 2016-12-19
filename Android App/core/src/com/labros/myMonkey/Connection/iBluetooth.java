package com.labros.myMonkey.Connection;

/**
 * Created by Labros on 25/8/2016.
 */
public interface iBluetooth
{

    void bluetoothEnable();
    void setConnection(String DeviceMAC);
    void disconnect();
    double getIncomingValue();
    void sendMessage(String value);
    boolean isEnabled();
    int getConnectionStatus();
    int getPairingStatus();

}