package com.labros.myMonkey.Actors.SubActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.labros.myMonkey.Actors.Player;
import com.labros.myMonkey.View.GameView;

/**
 * Created by Labros on 3/8/2016.
 */
public class Monkey extends Player
{
    public Monkey(Body catcher, Body body)
    {
        this.body = body;
        this.catcher = catcher;

        bodyX = (body.getPosition().x * PPM) - (playerWidth/2);
        catcherX = (body.getPosition().x * PPM);
        playerImage = new TextureRegion(new Texture(Gdx.files.internal(GameView.getMonkeyImageURL())));
    }
}