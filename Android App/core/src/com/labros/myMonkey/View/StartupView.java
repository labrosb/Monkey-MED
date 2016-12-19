package com.labros.myMonkey.View;

/**
 * Created by Labros on 4/8/2016.
 */
public class StartupView
{
    private static String startupBackgroundImageURL = "background_startup.jpg";
    private static String logoImageURL = "game_logo.png";
    private static String filterImageURL = "filter.png";
    private static String configFigureImageURL = "startup_monkey.png";

    private static int screenHeight = GeneralView.getScreenHeight();
    private static int screenWidth = GeneralView.getScreenWidth();

    private static float logoWidth = screenWidth * 98 / 100;
    private static float logoX = screenWidth * 1 / 100;
    private static float logoY = screenHeight * 35 / 100;

    private static float startupFontSize = screenHeight * 18 / 100;
    private static float startupTextX = screenWidth * 25f / 100f;
    private static float startupTextY = screenHeight * 30 / 100f;

    public static String getStartupBackgroundImageURL(){ return startupBackgroundImageURL; }
    public static String getLogoImageURL(){ return logoImageURL; }
    public static String getFilterImageURL(){ return filterImageURL; }
    public static String getConfigFigureImageURL(){ return configFigureImageURL; }

    public static float getLogoWidth(){ return logoWidth; }
    public static float getLogoX(){ return logoX; }
    public static float getLogoY(){ return logoY; }

    public static float getStartupFontSize() { return startupFontSize; }
    public static float getStartupTextX() { return startupTextX; }
    public static float getStartupTextY() { return startupTextY; }
}
