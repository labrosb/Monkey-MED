package com.labros.myMonkey.Connection;
/**
 * Created by Labros on 7/7/2016.
 */
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT implements MqttCallback
{
    private final static String DEFAULT_TCP_PORT = "1883";
    private final static String DEFAULT_SSL_PORT = "8883";

    MqttClient client;
    private int connectionStatus = -1;
    private int subscriptionStatus = -1;
    private boolean connectionLost;
    private double incomingValue;

    int counter = 0;
    long timers = 0;

    public int getConnectionStatus() { return connectionStatus; }

    public int getSubscriptionStatus() { return subscriptionStatus; }

    public boolean isConnectionLost() { return connectionLost; }

    public double getIncomingValue()
    {
        return incomingValue;
    }


    public void connect(String serverHost, String clientId, String authmethod,
                                        String authtoken, boolean isSSL)
    {
        // check if client is already connected
        if (!isConnected())
        {
            String connectionUri = null;

            if (isSSL)
            {
                connectionUri = "ssl://" + serverHost + ":" + DEFAULT_SSL_PORT;
            } else {
                connectionUri = "tcp://" + serverHost + ":" + DEFAULT_TCP_PORT;
            }

            if (client != null)
            {
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                client = null;
            }

            try {

                MqttConnectOptions options;
                options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setUserName(authmethod);
                options.setPassword(authtoken.toCharArray());
                //options.setKeepAliveInterval(30);

                if (isSSL) {
                    java.util.Properties sslClientProps = new java.util.Properties();
                    sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
                    options.setSSLProperties(sslClientProps);
                }

                MemoryPersistence persistence = new MemoryPersistence();
                client = new MqttClient(connectionUri, clientId, persistence);
                client.connect(options);
                client.setCallback(this);

                connectionLost = false;
                connectionStatus = 1;

            } catch (MqttException e) {
                connectionStatus = 0;
                e.printStackTrace();
            }
        }
        else{
            connectionStatus = 1;
        }
    }

    public void disconnect()
    {
        // check if client is actually connected
        if (isConnected()) {
            try {
                // disconnect
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe(String topic, int qos) {

        // check if client is connected
        if (isConnected()) {
            try {
                client.subscribe(topic, qos);
                subscriptionStatus = 1;
            } catch (MqttException e) {
                subscriptionStatus = 0;
                e.printStackTrace();
            }
        } else {
            connectionLost(null);
        }
    }

    public void unsubscribe(String topic) {
        // check if client is connected
        if (isConnected()) {
            try {
                subscriptionStatus = -1;
                client.unsubscribe(topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            connectionLost(null);
        }
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        subscriptionStatus = -1;
        connectionStatus = -1;
        connectionLost = true;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        try
        {
            //System.out.println("--------->>> "+message);
            //GameLayout.makeJump();
            //long CurrentTime = new Long((new Date().getTime()));
         //long CurrentTime = System.currentTimeMillis();
            //System.out.println(CurrentTime);
            String startTimeS = String.valueOf(message);
            incomingValue = Double.parseDouble(startTimeS);
            //long startTime = Long.parseLong(startTimeS);
            //long time = CurrentTime - startTime;
            //System.out.println(time);
            //System.out.println(incomingValue);
            //if (incomingValue == 5000 || incomingValue == 10000 || incomingValue == 15000 )
            //{
            //System.out.println(incomingValue);
            // }
            //timers +=time;
            //counter++;
            //System.out.println("Average time---> "+ timers/counter);
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("Not a number--> "+ message);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

    public boolean isConnected()
    {
        boolean connected = false;
        try {
            if ((client != null) && (client.isConnected()))
            {
                connectionStatus = 1;
                connected = true;
            }
        } catch (Exception e) {
            connectionStatus = 0;
            connected = false;
        }
        return connected;
    }
}