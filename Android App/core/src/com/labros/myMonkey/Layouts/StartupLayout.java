package com.labros.myMonkey.Layouts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.labros.myMonkey.Connection.ConnectionHandler;
import com.labros.myMonkey.MyGame;
import com.labros.myMonkey.View.GeneralView;
import com.labros.myMonkey.View.StartupView;

/**
 * Created by Labros on 28/7/2016.
 */
public class StartupLayout extends Stage
{
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont progressFont;

    private Sprite backgroundSprite, logoSprite, filterSprite;

    private String backgroundImagePath = StartupView.getStartupBackgroundImageURL();
    private String logoImagePath = StartupView.getLogoImageURL();
    private String filterImagePath = StartupView.getFilterImageURL();

    private float backgroundHeight = GeneralView.getScreenHeight() ;

    private float logoWidth = StartupView.getLogoWidth();
    private float logoX = StartupView.getLogoX();
    private float logoY = StartupView.getLogoY();

    private float startupFontSize = StartupView.getStartupFontSize() / GeneralView.getPPM();
    private float textPositionX = StartupView.getStartupTextX();
    private float textPositionY = StartupView.getStartupTextY();

    private MyGame game;

    private ConnectionHandler connection;
    private String currentProtocol = "";
    private int connectionStatus = -1;
    private int subscriptionStatus = -1;
    private boolean connectionLost;

    private boolean timeout;
    private int timoutTime = 5;

                                                                                        private String message;

    public StartupLayout(MyGame game)
    {
        this.game = game;

        connection = game.getConnection();
        connection.getOptions();
        batch = new SpriteBatch();

        backgroundSprite = createElement(backgroundImagePath, backgroundHeight, 0, 0, 0);
        filterSprite = createElement(filterImagePath, backgroundHeight, 0, 0, 0);
        logoSprite = createElement(logoImagePath, 0, logoWidth, logoX, logoY);
        createText();
        timeout();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        connection.connect();
        connectionStatus = connection.getConnectionStatus();
        subscriptionStatus = connection.getSubscriptionStatus();
        if (connectionStatus == 0 || connectionStatus < -1)
        {
            connectionLost = connection.isConnectionLost();
            connection.secondaryProtocolConnect();
        }
        else if (connectionStatus == 1 && subscriptionStatus == 1 && timeout)
        {
            game.goToNextScreen();
        }
    }

    @Override
    public void draw()
    {
        super.draw();

        currentProtocol = connection.getChosenProtocol();

        batch.begin();

        backgroundSprite.draw(batch);
        filterSprite.draw(batch);
        logoSprite.draw(batch);

        if (connectionStatus == 1 && subscriptionStatus == 1)
        {
            progressFont.draw(batch, currentProtocol+" connected!! Please wait...",
                    textPositionX, textPositionY);
        }
        else if (connectionStatus == 1 && subscriptionStatus == 0)
        {
            if(currentProtocol.equals("MQTT"))
            {
                progressFont.draw(batch, currentProtocol + " Error: Subscription failed !! ",
                        textPositionX, textPositionY);
            }
        }
        else if (connectionStatus == 1 && subscriptionStatus == -1)
        {
            if(currentProtocol.equals("MQTT"))
            {
                progressFont.draw(batch, currentProtocol + " subscribing.. ",
                        textPositionX, textPositionY);
            }
        }
        else if (connectionStatus == 0)
        {
            if(currentProtocol.equals("Bluetooth"))
            {
                if (subscriptionStatus == 0)
                {
                    progressFont.draw(batch, "Bluetooth Error: Devices are not Paired !!",
                            textPositionX, textPositionY);
                }
                else if (subscriptionStatus == -2)
                {
                    progressFont.draw(batch, "Bluetooth Error: Failed to connect !!",
                            textPositionX, textPositionY);
                }
            }
            if(currentProtocol.equals("MQTT"))
            {
                if (connectionLost)
                {
                    progressFont.draw(batch, currentProtocol+" Error: Connection Lost !!",
                            textPositionX, textPositionY);
                }
                else {
                    progressFont.draw(batch, currentProtocol + " Error: Connection to broker failed !!",
                            textPositionX, textPositionY);
                    }
            }
        }
        else if (connectionStatus == -1 )
        {
            progressFont.draw(batch, "Connecting via "+currentProtocol+". \n Please wait...",
                    textPositionX, textPositionY);
        }
        else if (connectionStatus == -2 )
        {
            progressFont.draw(batch, "Error: No bluetooth adapter found !!",
                    textPositionX, textPositionY);
        }
        batch.end();
    }

    public Sprite createElement(String imagePath, float height, float width, float x, float y)
    {
        Texture texture = new Texture(Gdx.files.internal(imagePath));
        if (width  == 0)
        {
            width = GeneralView.getWidthFromHeight(new TextureRegion(texture), height);
        }
        else if (height  == 0)
        {
            height = GeneralView.getHeightFromWidth(new TextureRegion(texture), width);
        }
        Sprite sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        return sprite;
    }

    public void createText()
    {
        progressFont = new BitmapFont();
        progressFont.setColor(Color.GREEN);
        progressFont.getRegion().getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        progressFont.getData().setScale(startupFontSize);
    }

    public void timeout()
    {
        Timer Max = new Timer();
        Max.schedule(new Timer.Task() {
            @Override
            public void run()
            {
            timeout = true;
                //Timer.instance().clear();
            }
        },timoutTime);
    }
}