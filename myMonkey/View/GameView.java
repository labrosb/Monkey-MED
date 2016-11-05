package com.labros.myMonkey.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Labros on 4/8/2016.
 */
public class GameView
{
    private static String progressBarImageURL = "progressBar.png";
    private static String backgroundImageURL = "background.jpg";
    private static String monkeyImageURL = "monkey_right.png";
    private static String bananaImageURL = "banana.png";
    private static String coconutImageURL = "coconut.png";
    private static String grapeImageURL = "grape.png";

    private static int screenHeight = GeneralView.getScreenHeight();
    private static int screenWidth = GeneralView.getScreenWidth();
    private static float PPM = GeneralView.getPPM();

    private static int headerHeight = screenHeight * 12/100;
    private static int contentHeight = screenHeight - headerHeight;

    private static float progressFontSize = screenHeight * 22/100;
    private static float progressBarHeight = screenHeight * 2.3f/100;
    private static float progressBarWidth = screenWidth * 33/100;

    private static float progressBarColorWidthMin = 0;
    private static float progressBarColorWidthMax = progressBarWidth - 12;
    private static float progressBarColorHeight = progressBarHeight;

    private static float progressBarPositionX = (screenWidth/2.155f) - (progressBarWidth/2);
    private static float progressBarPositionY = screenHeight - (headerHeight/2) - (progressBarHeight/2);

    private static float progressBarColorPositionX = progressBarPositionX + progressBarPositionX * 0.025f;
    private static float progressBarColorPositionY = progressBarPositionY;

    private static float scorePositionX = screenWidth * 6.85f/10;
    private static float scorePositionY = screenHeight - (headerHeight/2) + (progressFontSize/PPM * 4.5f);

    private static float levelPositionX = screenWidth * 0.5f/10;
    private static float levelPositionY = screenHeight - (headerHeight/2) + (progressFontSize/PPM * 4.5f);

    private static float playerHeight = screenHeight * 17.5f/100;
    private static float playerWidth = GeneralView.getWidthFromHeight
            (new TextureRegion(new Texture(Gdx.files.internal(monkeyImageURL))), playerHeight);

    private static float playerPositionY = (screenHeight - headerHeight) / 2 - (playerHeight / 2);

    private static float fruitHeight = screenHeight * 5.5f/100;
    private static float fruitWidth = GeneralView.getWidthFromHeight
            (new TextureRegion(new Texture(Gdx.files.internal(bananaImageURL))), fruitHeight);

    private static float fruitPositionX = -fruitWidth;


    public static int getHeaderHeight() { return headerHeight; }
    public static int getContentHeight() { return contentHeight; }

    public static float getProgressBarHeight() { return progressBarHeight; }
    public static float getProgressBarWidth() { return progressBarWidth; }
    public static float getProgressBarColorWidthMin() { return progressBarColorWidthMin; }
    public static float getProgressBarColorWidthMax() { return progressBarColorWidthMax; }
    public static float getProgressBarColorHeight() { return progressBarColorHeight; }

    public static float getProgressBarPositionX() { return progressBarPositionX; }
    public static float getProgressBarPositionY() { return progressBarPositionY; }
    public static float getProgressBarColorPositionX() { return progressBarColorPositionX; }
    public static float getProgressBarColorPositionY() { return progressBarColorPositionY; }

    public static float getScorePositionX() { return scorePositionX; }
    public static float getScorePositionY() { return scorePositionY; }

    public static float getLevelPositionX() { return levelPositionX; }
    public static float getLevelPositionY() { return levelPositionY; }

    public static float getProgressFontSize() { return progressFontSize; }

    public static String getProgressBarImageURL() { return progressBarImageURL; }
    public static String getBackgroundImageURL() { return backgroundImageURL; }
    public static String getMonkeyImageURL() { return monkeyImageURL; }
    public static String getBananaImageURL() { return bananaImageURL; }
    public static String getCoconutImageURL() { return coconutImageURL; }
    public static String getGrapeImageURL() { return grapeImageURL; }

    public static float getPlayerHeight() { return playerHeight; }
    public static float getPlayerWidth() { return playerWidth; }
    public static float getPlayerPositionY() { return playerPositionY; }

    public static float getFruitHeight() { return fruitHeight; }
    public static float getFruitWidth() { return fruitWidth; }

    public static float getFruitPositionX() { return fruitPositionX; }
}
