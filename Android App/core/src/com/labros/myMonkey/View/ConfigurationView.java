package com.labros.myMonkey.View;

/**
 * Created by Labros on 4/8/2016.
 */
public class ConfigurationView
{
    private static String configBackgroundImageURL = "background_startup.jpg";
    private static String configFigureImageURL = "startup_monkey.png";
    private static String whiteBubbleImageURL = "speech_bubble.png";
    private static String blackBubbleImageURL = "speech_bubble_black.png";

    private static int screenHeight = GeneralView.getScreenHeight();
    private static int screenWidth = GeneralView.getScreenWidth();

    private static float configurationFigureHeight = screenHeight * 50/100;
    private static float configurationFigureX = screenWidth * 38/100f;
    private static float configurationFigureY = screenHeight * 5/100f;

    private static float speechBubbleHeight = screenHeight * 25/100;
    private static float speechBubbleX = screenWidth * 5/100f;
    private static float speechBubbleY = screenHeight * 50/100f;

    private static float configurationFontSize = screenHeight * 22/100;
    private static float configurationFontSizeSmall = screenHeight * 15/100;
    private static float configurationTextX = screenWidth * 14f/100f;
    private static float configurationTextY = screenHeight * 71/100f;

    public static String getConfigBackgroundImageURL(){ return configBackgroundImageURL; }
    public static String getConfigFigureImageURL(){ return configFigureImageURL; }
    public static String getWhiteBubbleImageURL(){ return whiteBubbleImageURL; }
    public static String getBlackBubbleImageURL(){ return blackBubbleImageURL; }

    public static float getConfigurationFigureHeight() { return configurationFigureHeight; }
    public static float getConfigurationFigureX(){ return configurationFigureX; }
    public static float getConfigurationFigureY(){ return configurationFigureY; }

    public static float getSpeechBubbleHeight() { return speechBubbleHeight; }
    public static float getSpeechBubbleX() { return speechBubbleX; }
    public static float getSpeechBubbleY() { return speechBubbleY; }

    public static float getConfigurationFontSize() { return configurationFontSize; }
    public static float getConfigurationTextX() { return configurationTextX; }
    public static float getConfigurationTextY() { return configurationTextY; }
}
