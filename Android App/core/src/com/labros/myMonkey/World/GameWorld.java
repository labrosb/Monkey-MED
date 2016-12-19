package com.labros.myMonkey.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;

/**
 * Created by Labros on 23/5/2016.
 */
public class GameWorld
{
    private static float screenWidth = GeneralView.getScreenWidth();
    private static float PPM = GeneralView.getPPM();
    private static float contentHeight = GameView.getContentHeight();
    private static float playerHeight = GameView.getPlayerHeight();
    private static float playerWidth = GameView.getPlayerWidth();
    private static float playerPositionY = GameView.getPlayerPositionY();
    private static float fruitHeight = GameView.getFruitHeight();
    private static float fruitWidth = GameView.getFruitWidth();
    private static float fruitPositionY = GameView.getFruitPositionX();

    private static float GRAVITY = -10f;

    private static float GROUND_WIDTH = (screenWidth/2) / PPM;
    private static float GROUND_HEIGHT = 0f;
    private static float GROUND_X = (screenWidth/2) / PPM;
    private static float GROUND_Y = 0f;
    private static float GROUND_DENSITY = 0f;

    private static float CEILING_WIDTH = (screenWidth/2)/ PPM;
    private static float CEILING_HEIGHT = 0f;
    private static float CEILING_X = (screenWidth/2) / PPM;
    private static float CEILING_Y = contentHeight / PPM;
    private static float CEILING_DENSITY = 0f;

    private static float PLAYER_HEIGHT = (playerHeight/2) / PPM;
    private static float PLAYER_WIDTH = (playerWidth/2) / PPM;
    private static float CATCHER_HEIGHT = (playerHeight/3) / PPM;

    private static float PLAYER_X = (screenWidth - (playerWidth / 2)) / PPM;
    private static float PLAYER_Y = playerPositionY / PPM;
    private static float PLAYER_DENSITY = 0f;

    private static float FRUIT_HEIGHT = (fruitHeight/2) / PPM;
    private static float FRUIT_WIDTH = (fruitWidth/2) / PPM;

    //private static float FRUIT_X = -FRUIT_WIDTH;
    private static float FRUIT_X = (fruitPositionY/2) / PPM;
    private static float FRUIT_DENSITY = 0f;

    private static Vector2 WORLD_GRAVITY = new Vector2(0, 0);


    public static World createWorld()
    {
        return new World(WORLD_GRAVITY, true);
    }

    public static Body createGround(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GROUND_X, GROUND_Y);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GROUND_WIDTH, GROUND_HEIGHT);
        body.createFixture(shape, GROUND_DENSITY);
        shape.dispose();

        return body;
    }

    public static Body createCeiling(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(CEILING_X, CEILING_Y);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(CEILING_WIDTH, CEILING_HEIGHT);
        body.createFixture(shape, CEILING_DENSITY);
        shape.dispose();

        return body;
    }

    public static Body createPlayerCatcher(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(PLAYER_X , PLAYER_Y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.1f, CATCHER_HEIGHT);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, PLAYER_DENSITY);
        body.resetMassData();
        shape.dispose();

        return body;
    }

    public static Body createPlayerBody(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(PLAYER_X , PLAYER_Y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PLAYER_WIDTH, PLAYER_HEIGHT);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, PLAYER_DENSITY);
        body.resetMassData();
        shape.dispose();

        return body;
    }

    public static Body createFruit(World world, float position_Y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(FRUIT_X, position_Y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FRUIT_WIDTH, FRUIT_HEIGHT);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, FRUIT_DENSITY);
        body.resetMassData();
        shape.dispose();

        return body;
    }

}