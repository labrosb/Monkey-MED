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
import com.labros.myMonkey.Elements.Normalizer;
import com.labros.myMonkey.View.ConfigurationView;
import com.labros.myMonkey.View.GeneralView;

public class ConfigurationLayout extends Stage
{
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont progressFont;

    private Sprite backgroundSprite, figureSprite, bubbleSprite, blackBubbleSprite;

    private String backgroundImagePath = ConfigurationView.getConfigBackgroundImageURL();
    private String figureImagePath = ConfigurationView.getConfigFigureImageURL();
    private String bubblePath = ConfigurationView.getWhiteBubbleImageURL();
    private String blackBubblePath = ConfigurationView.getBlackBubbleImageURL();

    private float backgroundHeight = GeneralView.getScreenHeight() ;
    private float figureHeight = ConfigurationView.getConfigurationFigureHeight() ;
    private float bubbleHeight = ConfigurationView.getSpeechBubbleHeight();

    private float figureX = ConfigurationView.getConfigurationFigureX();
    private float figureY = ConfigurationView.getConfigurationFigureY();

    private float bubbleX = ConfigurationView.getSpeechBubbleX();
    private float bubbleY = ConfigurationView.getSpeechBubbleY();

    private static float textFontSize =
            ConfigurationView.getConfigurationFontSize() / GeneralView.getPPM();

    private static float textPositionX = ConfigurationView.getConfigurationTextX();
    private static float textPositionY = ConfigurationView.getConfigurationTextY();

    private MyGame game;
    private ConnectionHandler connection;
    private Normalizer normalizer;

    private int connectionStatus = -1;
    private int subscriptionStatus = -1;
    private boolean connectionLost;
    private boolean minMaxstarted;
    private boolean settingMax;
    private boolean settingMin;
    private int timeForMin = 4;
    private int timeForMax = 4;

    public ConfigurationLayout(MyGame game)
    {
        batch = new SpriteBatch();

        backgroundSprite = createElement(backgroundImagePath, backgroundHeight, -330, 0);
        figureSprite = createElement(figureImagePath, figureHeight, figureX, figureY);
        bubbleSprite = createElement(bubblePath, bubbleHeight, bubbleX, bubbleY);
        blackBubbleSprite = createElement(blackBubblePath, bubbleHeight, bubbleX, bubbleY);
        createText();

        this.game = game;
        connection = game.getConnection();
        normalizer = game.getNormalizer();

        connectionStatus = connection.getConnectionStatus();

        subscriptionStatus = connection.getSubscriptionStatus();

        if ( connectionStatus == 1 && subscriptionStatus == 1 ) { getMinMaxStart(); }

        connection.serverBroadcastStart();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        connectionStatus = connection.getConnectionStatus();

        if ( connectionStatus == 1 && subscriptionStatus == 1 )
        {
            if (settingMin)
            {
                double signalVal = connection.getValue();
                normalizer.setMin(signalVal);
            }
            else
            if (settingMax)
            {
                double signalVal = connection.getValue();
                normalizer.setMax(signalVal);
            }
            else
            if(minMaxstarted)
            {
                game.goToNextScreen();
            }
        }
        else if(connectionStatus == 0)
        {
            connectionLost = connection.isConnectionLost();
        }
    }

    @Override
    public void draw()
    {
        super.draw();

        batch.begin();

        backgroundSprite.draw(batch);
        figureSprite.draw(batch);

        if (connectionStatus == 1 && subscriptionStatus == 1)
        {
            if (settingMin)
            {
                bubbleSprite.draw(batch);
                progressFont.draw(batch, "Configuring...\nKeep your muscle \nrelaxed !",
                        textPositionX, textPositionY);
            }
            else if (settingMax)
            {
                blackBubbleSprite.draw(batch);
                progressFont.setColor(Color.GREEN );
                progressFont.draw(batch, "Configuring...\nStretch your muscle \nto begin !",
                        textPositionX, textPositionY);
            }
        }
        else if (connectionStatus == 1 && subscriptionStatus == 0)
        {
            progressFont.draw(batch, "Connected to broker! \nError: Subscription failed !! ",
                    textPositionX, textPositionY);
        }
        else if (connectionStatus == 0)
        {
            if (connectionLost)
            {
                progressFont.draw(batch, "Error: Connection Lost !!",
                        textPositionX, textPositionY);
            }
            else {
                progressFont.draw(batch, "Error: Connection to broker failed !!",
                        textPositionX, textPositionY);
            }
        }
        else if (connectionStatus == -1 )
        {
            progressFont.draw(batch, "Connecting...", textPositionX, textPositionY);
        }
        batch.end();
    }

    public Sprite createElement(String imagePath, float height, float x, float y)
    {
        Texture texture = new Texture(Gdx.files.internal(imagePath));
        float width = GeneralView.getWidthFromHeight(new TextureRegion(texture), height);
        Sprite sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        return sprite;
    }

    public void createText()
    {
        progressFont = new BitmapFont();
        progressFont.setColor(Color.DARK_GRAY  );
        progressFont.getRegion().getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        progressFont.getData().setScale(textFontSize);
    }

    public void getMinMaxStart()
    {
        minMaxstarted = true;
        settingMin = true;

        Timer Max = new Timer();
        Max.schedule(new Timer.Task() {
            @Override
            public void run()
            {
                settingMin = false;
                settingMax = true;
                getMinMaxStop();
                //Timer.instance().clear();
            }
        },timeForMin);
    }

    public void getMinMaxStop()
    {
        Timer MaxStop = new Timer();
        MaxStop.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                settingMax = false;
                Timer.instance().clear();
            }
        },timeForMax);
    }
}
