package com.labros.myMonkey.Connection;

/**
 * Created by Lampros on 1/12/2016.
 */

public class BluetoothSingleton {

    public static volatile BluetoothSingleton coreBluetooth = null;
    public iBluetooth bluetoothManager;

    /* METHODS */
    public static BluetoothSingleton getInstance()
    {
        if (coreBluetooth == null)
        {
            synchronized (BluetoothSingleton.class)
            {
                if (coreBluetooth == null) { coreBluetooth = new BluetoothSingleton(); }
            }
        }
        return coreBluetooth;
    }
}