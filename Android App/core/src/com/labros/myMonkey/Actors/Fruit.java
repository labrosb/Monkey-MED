package com.labros.myMonkey.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;

public abstract class Fruit extends Actor
{
    protected Body body;
    protected TextureRegion fruitImage;
    protected float startingPositionX;
    protected float positionX;
    protected float positionY;
    protected Vector2 linearVelocity;

    protected float width = GameView.getFruitWidth();
    protected float height = GameView.getFruitHeight();

    private float newWidth = GameView.getFruitWidth();
    private float newHeight = GameView.getFruitHeight();

    private int points;
    private float speed;
    private boolean outOfBounds = false;
    private boolean caught = false;
    private boolean active;

    protected float PPM = GeneralView.getPPM();

    public Body getBody() {
        return body;
    }

    public void setLinearVelocity(Vector2 linearVelocity)
    {
        this.linearVelocity = linearVelocity;
        body.setLinearVelocity(linearVelocity);
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void removeVelocity() {
        body.setLinearVelocity(new Vector2(0, 0));
    }

    public void stop(){ body.setAwake(false); }

    public void activate() { active = true; }

    public void deactivate()
    {
        active = false;
        stop();
    }

    public void reset()
    {
        active = false;
        caught = false;
        outOfBounds = false;
        body.setTransform((startingPositionX - width/2)/100,
                body.getPosition().y, 0);
        newWidth = width;
        newHeight = height;
    }

    public boolean isCaught() {
        return caught;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public void setOutOfBounds()
    {
        outOfBounds = true;
    }

    public void setCaught() {
        caught = true;
    }

    public void setSpeed(float speed){ this.speed = speed; }

    public void setPoints(int points){ this.points = points; }

    public float getSpeed()
    {
        return speed;
    }

    public int getPoints()
    {
        return points;
    }

    @Override
    public void act(float delta)
    {
        if (isCaught() && isActive())
        {
            if(newHeight < height/10)
            {
                reset();
            }
        }
        else if (isActive() && !isOutOfBounds())
        {
            if((body.getPosition().x * PPM) - width / 2 > GeneralView.getScreenWidth() + width/2)
            {
                setOutOfBounds();
                deactivate();
                reset();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
            if(!isCaught())
            {
                batch.draw(fruitImage,
                        (body.getPosition().x * PPM) - width / 2, positionY, width, height);
                positionX = (body.getPosition().x * PPM) - height / 2;
            }else {
                positionX = positionX + 0.1f + (newWidth/6) / 2;
                newWidth = newWidth - newWidth / 6;
                newHeight = newHeight - newHeight / 6;
                batch.draw(fruitImage, positionX, positionY, newWidth, newHeight);
            }
    }
}