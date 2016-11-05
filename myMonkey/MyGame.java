package com.labros.myMonkey;



import com.badlogic.gdx.Game;
import com.labros.myMonkey.Connection.MQTT;
import com.labros.myMonkey.Screens.ConfigurationScreen;
import com.labros.myMonkey.Screens.GameScreen;
import com.labros.myMonkey.Screens.StartupScreen;
import com.labros.myMonkey.Elements.Normalizer;

public class MyGame extends Game
{
    private MQTT mqtt = new MQTT();
    private Normalizer normalizer = new Normalizer();

    private int screen = 1;
    private MyGame game = this;
/*
    public MyGame(){

        bluetooth.enableAdapter();
    }
*/
    @Override
    public void create ()
    {
        chooseScreen(screen);
    }

    @Override
    public void render()
    {
        super.render();
    }

    public void chooseScreen(int screen)
    {
        switch (screen)
        {
            case 1:
                setScreen(new StartupScreen(game));
                break;
            case 2:
                setScreen(new ConfigurationScreen(game));
                break;
            case 3:
                setScreen(new GameScreen(game));
                break;
        }
    }

    public void goToNextScreen()
    {
        //BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        screen++;
        chooseScreen(screen);
    }

    public MQTT getMQTT() { return mqtt; }

    public Normalizer getNormalizer() {
        return normalizer;
    }
}
