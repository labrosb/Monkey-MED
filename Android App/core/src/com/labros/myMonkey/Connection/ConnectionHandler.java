package com.labros.myMonkey.Connection;

import com.labros.myMonkey.Elements.Normalizer;
import com.labros.myMonkey.Settings.ConnectionSettings;

/**
 * Created by Lampros on 4/12/2016.
 */

public class ConnectionHandler
{
    private String chosenProtocol;
    private boolean secondaryActivated = false;
    private MQTT mqtt;
    private iBluetooth bluetooth;
    private final String startServerMsg = "Start Broadcasting";
    private int connectionStatus = -1;
    private int subscriptionStatus = -1;
    private boolean connectionLost;
    private ConnectionSettings options;
    private boolean BluetoothEnableAttempted = false;
    private boolean connectionAttemted = false;
    private long startTime = System.currentTimeMillis();//

    public void getOptions()
    {
        options = new ConnectionSettings();
        chosenProtocol = options.getPrimaryProtocol();
    }
    public void connect()
    {
        if(!connectionAttemted)
        {
            if (chosenProtocol.equals("Bluetooth"))
            {
                bluetooth = BluetoothSingleton.getInstance().bluetoothManager;
                if (!bluetooth.isEnabled() && !BluetoothEnableAttempted)
                {
                    bluetooth.bluetoothEnable();
                    BluetoothEnableAttempted = true;
                }
                else if (!bluetooth.isEnabled() && BluetoothEnableAttempted)
                {
                    long timeout = System.currentTimeMillis() - startTime;
                    if (timeout > 5000) { connectionStatus = 0; }
                }
                else if (bluetooth.isEnabled())
                {
                    BluetoothConnect();
                    connectionAttemted = true;
                }
            }
            else if (chosenProtocol.equals("MQTT"))
            {
                MQTTConnect();
                connectionAttemted = true;
            }
        }
    }

    public void serverBroadcastStart()
    {
        sendMessage(startServerMsg);
    }

    private void sendMessage(String Value)
    {
        if (chosenProtocol.equals("Bluetooth")) { bluetooth.sendMessage(Value); }
        else if (chosenProtocol.equals("MQTT")){  }
    }

    private void BluetoothConnect()
    {
        String address = options.getBluetoothServer();
        bluetooth.setConnection(address);
    }

    private void MQTTConnect()
    {
        String broker = options.getMQTT_broker();
        String clientId = options.getMQTT_client_id();
        String auth = options.getMQTT_authentication_method();
        String token = options.getMQTT_token();
        boolean SSL = options.MQTT_under_SSL();
        String TCP_Port = options.getMQTT_TCP_port();
        String SSL_Port = options.getMQTT_SSL_port();
        String subject = options.getSubject();
        int Qos = options.getMQTT_QoS();

        mqtt = new MQTT();
        mqtt.connect(broker, clientId, auth, token, SSL, TCP_Port, SSL_Port);
        mqtt.subscribe(subject, Qos);
    }

    public void secondaryProtocolConnect()
    {
        startTime = System.currentTimeMillis();
        if(!secondaryActivated)
        {
            secondaryActivated = true;
            connectionAttemted = false;
            if (chosenProtocol.equals("Bluetooth")) { chosenProtocol = "MQTT"; }
            else if (chosenProtocol.equals("MQTT")) { chosenProtocol = "Bluetooth"; }
            connect();
        }
    }

    public void disconnect()
    {
        if (chosenProtocol.equals("Bluetooth")) { bluetooth.disconnect(); }
        else if (chosenProtocol.equals("MQTT")) { mqtt.disconnect(); }
    }

    public String getChosenProtocol(){
        return chosenProtocol;
    }

    public int getConnectionStatus()
    {
        if(chosenProtocol.equals("Bluetooth")) { connectionStatus =  bluetooth.getConnectionStatus();}
        else if(chosenProtocol.equals("MQTT")) { connectionStatus = mqtt.getConnectionStatus(); }
        return connectionStatus;
    }

    public int getSubscriptionStatus()
    {
        if(chosenProtocol.equals("Bluetooth")) { subscriptionStatus = bluetooth.getPairingStatus();}
        else if(chosenProtocol.equals("MQTT")) { subscriptionStatus = mqtt.getSubscriptionStatus();}
        return subscriptionStatus;
    }

    public boolean isConnectionLost()
    {
        connectionLost = false;
        if(chosenProtocol.equals("MQTT")) { connectionLost = mqtt.isConnectionLost(); }
        return connectionLost;
    }

    public double getValue()
    {
        double value = 0.0;
        if(chosenProtocol.equals("Bluetooth")) { value = bluetooth.getIncomingValue(); }
        else if(chosenProtocol.equals("MQTT")) { value = mqtt.getIncomingValue(); }
        //System.out.println(value);
        return value;
    }
}