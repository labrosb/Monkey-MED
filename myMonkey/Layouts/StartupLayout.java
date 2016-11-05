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
import com.labros.myMonkey.Connection.MQTT;
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
    ;
    private float backgroundHeight = GeneralView.getScreenHeight() ;

    private float logoWidth = StartupView.getLogoWidth();
    private float logoX = StartupView.getLogoX();
    private float logoY = StartupView.getLogoY();

    private float startupFontSize = StartupView.getStartupFontSize() / GeneralView.getPPM();
    private float textPositionX = StartupView.getStartupTextX();
    private float textPositionY = StartupView.getStartupTextY();

    private MyGame game;

    private MQTT mqtt;

    private int MQTTConnectionStatus = -1;
    private int MQTTSubscriptionStatus = -1;
    private boolean MQTTConnectionLost;

    private boolean timeout;
    private int timoutTime = 5;

    public StartupLayout(MyGame game)
    {
        this.game = game;
        mqtt = game.getMQTT();

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

        MQTTConnectionStatus = mqtt.getConnectionStatus();
        MQTTSubscriptionStatus = mqtt.getSubscriptionStatus();

        if (MQTTConnectionStatus == -1)
        {
            mqtt.connect("test.mosquitto.org", "qqw", null, "", false);
            mqtt.subscribe("EMG-DataX",0);
        }
        else if (MQTTConnectionStatus == 0)
        {
            MQTTConnectionLost = mqtt.isConnectionLost();
        }
        else if (MQTTConnectionStatus == 1 && timeout)
        {
            game.goToNextScreen();
        }
    }

    @Override
    public void draw()
    {
        super.draw();

        batch.begin();

        backgroundSprite.draw(batch);
        filterSprite.draw(batch);
        logoSprite.draw(batch);

        if (MQTTConnectionStatus == 1 && MQTTSubscriptionStatus == 1) {
            progressFont.draw(batch, "Connected!! Please wait...", textPositionX, textPositionY);
        }
        else if (MQTTConnectionStatus == 1 && MQTTSubscriptionStatus == 0)
        {
            progressFont.draw(batch, "Error: Subscription failed !! ",
                    textPositionX, textPositionY);
        }
        else if (MQTTConnectionStatus == 0)
        {
            if (MQTTConnectionLost)
            {
                progressFont.draw(batch, "Error: Connection Lost !!",
                        textPositionX, textPositionY);
            }
            else {
                progressFont.draw(batch, "Error: Connection to broker failed !!",
                        textPositionX, textPositionY);
            }
        }
        else if (MQTTConnectionStatus == -1 )
        {
            progressFont.draw(batch, "Connecting. Please wait...", textPositionX, textPositionY);
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