package com.labros.myMonkey.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

/**
 * Created by Lampros on 4/12/2016.
 */

public class ConnectionSettings
{
    private XmlReader.Element root;
    private String protocol;
    private String TCP_port;
    private String SSL_port;
    private String server;
    private String broker;
    private String subject;
    private int qos;
    private String client_id;
    private boolean SSL;
    private String authentication_method;
    private String token;

    public ConnectionSettings()
    {
        XmlReader reader = new XmlReader();
        try {
            root = reader.parse(Gdx.files.internal("settings/connection.xml"));
        }
        catch (Throwable e) {
            System.out.println("----------->Read error  " + e);
        }
        XmlReader.Element choice=null;
        choice = root.getChildByName("default");
        protocol = choice.getChildByName("primary_protocol").getText();
        XmlReader.Element bluetooth = choice.getChildByName("bluetooth");
        XmlReader.Element MQTT = choice.getChildByName("MQTT");
        server = bluetooth.getChildByName("server").getText();
        broker = MQTT.getChildByName("broker").getText();
        client_id = MQTT.getChildByName("client_id").getText();
        authentication_method = MQTT.getChildByName("authentication_method").getText();
        token = MQTT.getChildByName("token").getText();
        SSL = MQTT.getBoolean("SSL");
        subject = MQTT.getChildByName("subject").getText();
        qos = MQTT.getInt("qos");
        TCP_port = MQTT.getChildByName("TCP_port").getText();
        SSL_port = MQTT.getChildByName("SSL_port").getText();
    }
    public String getPrimaryProtocol(){
        return protocol;
    }
    public String getBluetoothServer(){
        return server;
    }
    public String getMQTT_broker(){
        return broker;
    }
    public String getMQTT_client_id(){
        return client_id;
    }
    public String getMQTT_authentication_method(){
        return authentication_method;
    }
    public String getMQTT_token()
    {
        if (token == null ){ token = ""; }
        return token;
    }
    public boolean MQTT_under_SSL() { return SSL; }
    public int getMQTT_QoS() { return qos; }
    public String getMQTT_TCP_port(){
        return TCP_port;
    }
    public String getMQTT_SSL_port(){
        return SSL_port;
    }
    public String getSubject(){
        return subject;
    }
}