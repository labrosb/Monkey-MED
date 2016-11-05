package com.labros.myMonkey.Layouts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.labros.myMonkey.Actors.Background;
import com.labros.myMonkey.Actors.Fruit;
import com.labros.myMonkey.Actors.Player;
import com.labros.myMonkey.Actors.ProgressBar;
import com.labros.myMonkey.Actors.SubActors.Monkey;
import com.labros.myMonkey.Connection.MQTT;
import com.labros.myMonkey.Logic.GameLogic;
import com.labros.myMonkey.Elements.FruitsGenerator;
import com.labros.myMonkey.Elements.Normalizer;
import com.labros.myMonkey.MyGame;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;
import com.labros.myMonkey.World.Border;
import com.labros.myMonkey.World.GameWorld;

import java.util.ArrayList;


public class GameLayout extends Stage
{
    private static float PPM = GeneralView.getPPM();

    private static final float VIEWPORT_WIDTH = Gdx.graphics.getWidth() / PPM;
    private static final float VIEWPORT_HEIGHT = Gdx.graphics.getHeight() / PPM;

    private World world;

    private ProgressBar progressBar;
    private Background background;

    private Border ground;
    private Border ceiling;
    private static Player player;

    private GameLogic gameLogic;
    private FruitsGenerator generator;
    private ArrayList<Fruit> fruits = new ArrayList<Fruit>();
    private int fruitCnt = 0;

    private Boolean stop = false;

    private final float TIME_STEP = 1 / 50f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Rectangle screenLeftSide;
    private Rectangle screenRightSide;
    private Vector3 touchPoint;

    private MQTT mqtt;
    private Normalizer normalizer;

    private float screenMax = GameView.getContentHeight() - GameView.getPlayerHeight()/2;
    private float screenMin = GameView.getPlayerHeight()/2;

    private float prevSignal = 50f;
    private float signal = 0;
    private float testSignal = 0;


    public GameLayout(MyGame game)
    {
        mqtt = game.getMQTT();
        normalizer = game.getNormalizer();

        System.out.println("Min--> "+normalizer.min);
        System.out.println("Max--> "+normalizer.max);

        world = GameWorld.createWorld();
        //world.setContactListener(this);

        progressBar = new ProgressBar();

        background = new Background();
        ground = new Border(GameWorld.createGround(world));
        ceiling = new Border(GameWorld.createCeiling(world));
        player = new Monkey(GameWorld.createPlayerCatcher(world), GameWorld.createPlayerBody(world));

        addActor(progressBar);
        addActor(background);
        addActor(player);

        generator = new FruitsGenerator();
        fruits = generator.generateFruits(world, 50);
        gameLogic = new GameLogic(ground, ceiling, player, fruits, progressBar);
        world.setContactListener(gameLogic);
        setupCamera();
        setupTouchControlAreas();
        renderer = new Box2DDebugRenderer();
    }

    private void setupCamera()
    {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);
        camera.update();
    }

    private void setupTouchControlAreas()
    {
        touchPoint = new Vector3();
        screenLeftSide =
                new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        screenRightSide =
                new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }
//=================================================================================================
    //-============================================================================================

    @Override
    public void act(float delta)
    {
        super.act(delta);
        //float signal = (float) Math.random();
        float signal = normalizer.normalizeSignal(mqtt.getIncomingValue());
        //System.out.println("signal-->"+signal);
        //signalExploit(signal);     //setLinearVelocity
        //signalExploit1_1(signal); //setLinearVelocity after sleep
        signalExploit2(signal);  //Apply force
        //signalExploit2(signal);  //Apply force after sleep
        if (!stop)
        {
            //gameLogic.objectsLoop();
            if (System.currentTimeMillis() >= gameLogic.getNextThrowFruitTime() )
                try {
                    Fruit thisFruit = fruits.get(fruitCnt);
                    if(thisFruit.getStage() == null) {
                        addActor(thisFruit);
                    }
                    gameLogic.throwObject(thisFruit);
                    gameLogic.updateThrowFruitTime();
                    fruitCnt++;
                }
                catch (IndexOutOfBoundsException e)
                {
                    fruitCnt = 0;
                }

        }
        accumulator += delta;
        while (accumulator >= delta)
        {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public void draw()
    {
        super.draw();
        //renderer.render(world, camera.combined);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button)
    {
        translateScreenToWorldCoordinates(x, y);
        if (leftSideTouched(touchPoint.x, touchPoint.y))
        {
            //signal+=0.2;
            //signalExploit(signal);
        }
        else
        if (rightSideTouched(touchPoint.x, touchPoint.y))
        {
            //signal-=0.2;
            //signalExploit(signal);
        }

        return super.touchDown(x, y, pointer, button);
    }

    private void signalExploit(float signal) // setLinearVelocity
    {
        float position = normalizer.deNormalize(signal,screenMin,screenMax);
        float newY = position / PPM;
        if (player.getPositionY()+0.1 > newY  &&  player.getPositionY()-0.1 < newY)
        {
            player.getBody().setAwake(false);
        }
        else{
            float distance;
            if (player.getPositionY() > newY)
            {
                distance = player.getPositionY() - newY;
            }
            else {
                distance = newY - player.getPositionY();
            }
            float speed = distance * 3;

            player.moveTowardsY(newY, speed); // setLinearVelocity
            //player.moveUpDown(signal);
        }
    }

    private void signalExploit1_1(float signal) // setLinearVelocity after sleep
    {
        float position = normalizer.deNormalize(signal,screenMin,screenMax);
        float newY = position / PPM;
        if (player.getPositionY()+0.1 > newY  &&  player.getPositionY()-0.1 < newY)
        {
            player.getBody().setAwake(false);
        }
        else{
            float distance;
            if (player.getPositionY() > newY)
            {
                distance = player.getPositionY() - newY;
            }
            else {
                distance = newY - player.getPositionY();
            }
            float speed = distance * 3;

            player.getBody().setAwake(false);
            player.moveTowardsY(newY, speed); // setLinearVelocity
            //player.moveUpDown(signal);
        }
    }

    private void signalExploit2(float signal) //Apply force
    {
        float position = normalizer.deNormalize(signal,screenMin,screenMax);
        float newY = position / PPM;
        if (player.getPositionY()+0.1 > newY  &&  player.getPositionY()-0.1 < newY)
        {
            player.getBody().setAwake(false);
        }
        else{
            float distance = newY - player.getPositionY();;
            float speed = distance * 3;

            //player.getBody().setAwake(false);
            player.getBody().applyForce(new Vector2(0, speed), new Vector2(0, newY), true);
        }
    }

    private void signalExploit3(float signal) //Apply force after sleep
    {
        float position = normalizer.deNormalize(signal,screenMin,screenMax);
        float newY = position / PPM;
        if (player.getPositionY()+0.1 > newY  &&  player.getPositionY()-0.1 < newY)
        {
            //player.getBody().setLinearVelocity(0,0);
            player.getBody().setAwake(false);
        }
        else{
            float distance = newY - player.getPositionY();
            float speed = distance * 300;

            player.getBody().setAwake(false);
            player.getBody().applyForce(new Vector2(0, speed), new Vector2(0, newY), true);
        }
    }
    private void signalExploit4(float signal) // set linear impulse
    {
        float position = normalizer.deNormalize(signal,screenMin,screenMax);
        if (position >= player.getPositionY()* PPM)
        {
            player.jump2(signal);
        }
        else{
            player.getBody().setLinearVelocity(0,-10);
        }
    }

    private void translateScreenToWorldCoordinates(int x, int y)
    {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    private boolean rightSideTouched(float x, float y)
    {
        return screenRightSide.contains(x, y);
    }

    private boolean leftSideTouched(float x, float y)
    {
        return screenLeftSide.contains(x, y);
    }

    public void makeJump()
    {
        player.jump();
    }

    public void setFruit(Fruit newFruit){ addActor(newFruit);}

}

