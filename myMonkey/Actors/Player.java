package com.labros.myMonkey.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;

public abstract class Player extends Actor
{
    protected Body body;
    protected Body catcher;
    protected float bodyX;
    protected float bodyY;
    protected float catcherX;
    protected TextureRegion playerImage;
    protected float playerHeight = GameView.getPlayerHeight();
    protected float playerWidth = GameView.getPlayerWidth();

    protected float PPM = GeneralView.getPPM();

    private boolean jumping;
    private Vector2 jumpingLinearImpulse;


    public Body getBody()
    {
        return body;
    }
    public Body getCatcher() {return catcher;}

    public void jump()
    {
        body.applyLinearImpulse(getJumpingLinearImpulse(), body.getWorldCenter(), true);
        jumping = true;
    }

    public void jump2(float signal)
    {
        body.applyLinearImpulse(new Vector2(0, signal), body.getWorldCenter(), true);
        jumping = true;
    }

    public void moveUpDown(float speed)
    {
        body.setLinearVelocity(0, speed);
    }

    public void moveTowardsY(float y, float speed)
    {
        bodyY = y;
        Vector2 direction = new Vector2(body.getPosition().x, y );
        direction.sub(body.getPosition());
        direction.nor();
        body.setLinearVelocity(direction.scl(speed));
    }

    public void landed()
    {
        jumping = false;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse)
    {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }

    public Vector2 getJumpingLinearImpulse()
    {
        return jumpingLinearImpulse;
    }

    public float getPositionY()
    {
        return body.getPosition().y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
        batch.draw(playerImage,
                bodyX, (body.getPosition().y * PPM)- playerHeight/2, playerWidth, playerHeight);

        catcher.setTransform(catcherX / PPM, ((body.getPosition().y * PPM)) / PPM, 0);
    }
}