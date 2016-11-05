package com.labros.myMonkey.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Labros on 4/8/2016.
 */
public class GeneralView
{
    private static int screenHeight = Gdx.graphics.getHeight();
    private static int screenWidth = Gdx.graphics.getWidth();;
    private static int PPM = 100;;


    public GeneralView()
    {
        //screenHeight = Gdx.graphics.getHeight();
        //screenWidth = Gdx.graphics.getWidth();
        //PPM = 100;
    }

    public static int getScreenHeight() { return screenHeight; }
    public static int getScreenWidth() { return screenWidth; }
    public static float getPPM() { return PPM; }

    public static float getWidthFromHeight(TextureRegion image, float height)
    {
        int originalHeight = image.getRegionHeight();
        int originalWidth = image.getRegionWidth();
        float width = originalWidth * height / originalHeight;
        return width;
    }

    public static float getHeightFromWidth(TextureRegion image, float width)
    {
        float originalHeight = image.getRegionHeight();
        float originalWidth = image.getRegionWidth();
        float height = width * originalHeight / originalWidth;
        return height;
    }
}