package com.labros.myMonkey.Actors.SubActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.labros.myMonkey.Actors.Fruit;
import com.labros.myMonkey.View.GameView;

/**
 * Created by Labros on 3/8/2016.
 */
public class Coconut extends Fruit
{
    private float speed = 2.5f;
    private int points = 15;

    public Coconut(Body body)
    {
        this.body = body;
        startingPositionX = body.getPosition().x;
        fruitImage = new TextureRegion(new Texture(Gdx.files.internal(GameView.getCoconutImageURL())));
        positionY = (body.getPosition().y * PPM) - height / 2;
        linearVelocity = new Vector2(speed, 0);
        setSpeed(speed);
        setPoints(points);
    }
}