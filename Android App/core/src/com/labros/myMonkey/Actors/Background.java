package com.labros.myMonkey.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;

/**
 * Created by Labros on 30/5/2016.
 */
public class Background extends Actor
{

    private final TextureRegion background;
    private Rectangle backgroundBounds1;
    private Rectangle backgroundBounds2;
    private int speed = 100;
    private float gameHeight = GameView.getContentHeight();
    private float gameWidth = GeneralView.getScreenWidth();
    private float backgroundWidth;

    public Background()
    {
        background = new TextureRegion(new Texture(Gdx.files.internal(GameView.getBackgroundImageURL())));
        backgroundWidth = GeneralView.getWidthFromHeight(background, gameHeight);
        backgroundBounds1 = new Rectangle(0 - backgroundWidth/2, 0, backgroundWidth, gameHeight);
        backgroundBounds2 = new Rectangle(backgroundWidth/2, 0, backgroundWidth, gameHeight);
    }

    @Override
    public void act(float delta)
    {
        if (leftBoundsReached(delta)) {
            resetBounds();
        } else {
            updateXBounds(-delta);
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
        batch.draw(background, 0,0, backgroundWidth, gameHeight);

        batch.draw(background, backgroundBounds1.x, backgroundBounds1.y, backgroundWidth, gameHeight);
        batch.draw(background, backgroundBounds2.x, backgroundBounds2.y, backgroundWidth, gameHeight);
    }

    private boolean leftBoundsReached(float delta)
    {
        return (backgroundBounds2.x - (delta * speed)) <= 0;
    }

    private void updateXBounds(float delta)
    {
        backgroundBounds1.x += delta * speed;
        backgroundBounds2.x += delta * speed;
    }

    private void resetBounds()
    {
        float bounds1_X = backgroundBounds1.x;
        backgroundBounds1 = backgroundBounds2;
        backgroundBounds2 = new Rectangle(bounds1_X + backgroundWidth , 0, backgroundWidth, gameHeight);
    }
}



