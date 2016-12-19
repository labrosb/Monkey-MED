package com.labros.myMonkey;

import com.badlogic.gdx.Game;
import com.labros.myMonkey.Connection.ConnectionHandler;
import com.labros.myMonkey.Screens.ConfigurationScreen;
import com.labros.myMonkey.Screens.GameScreen;
import com.labros.myMonkey.Screens.StartupScreen;
import com.labros.myMonkey.Elements.Normalizer;

public class MyGame extends Game
{
    private ConnectionHandler connection = new ConnectionHandler();
    private Normalizer normalizer = new Normalizer();

    private int screen = 1;
    private MyGame game = this;

    @Override
    public void create ()
    {
        chooseScreen(screen);
    }
    @Override
    public void dispose ()
    {
        connection.disconnect();
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
        screen++;
        chooseScreen(screen);
    }

    public ConnectionHandler getConnection() { return connection; }

    public Normalizer getNormalizer() {
        return normalizer;
    }
}
