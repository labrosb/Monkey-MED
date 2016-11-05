package com.labros.myMonkey.World;

import com.badlogic.gdx.physics.box2d.Body;

public class Border
{
    protected Body body;

    public Border(Body body)
    {
        this.body = body;
    }

    public Body getBody(){return body;}
}