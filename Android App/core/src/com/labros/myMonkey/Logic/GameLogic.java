package com.labros.myMonkey.Logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.labros.myMonkey.Actors.Fruit;
import com.labros.myMonkey.Actors.Player;
import com.labros.myMonkey.Actors.ProgressBar;
import com.labros.myMonkey.Elements.GameLevel;
import com.labros.myMonkey.Layouts.GameLayout;
import com.labros.myMonkey.World.Border;

import java.util.ArrayList;

/**
 * Created by Labros on 8/8/2016.
 */
public class GameLogic implements ContactListener
{
    private Border ground;
    private Border ceiling;
    private Player player;
    private ProgressBar progressBar;
    private ArrayList<Fruit> fruits;

    //private float throwFruitTime = 2;
    private double nextThrowFruitTime;
    private float fruitAcceleration;
    //private int fruitCnt = 0;

    private GameLevel gameLevel;
    //private int level;
    private int scoreToAccomplish;

    private long startTime;
    private Timer timer;

    GameLayout layout; //-

    public GameLogic(Border ground, Border ceiling,
                     Player player, ArrayList<Fruit> fruits, ProgressBar progressBar)
    {
        this.ground = ground;
        this.ceiling = ceiling;
        this.player = player;
        this.fruits = fruits;
        this.progressBar = progressBar;

        gameLevel = new GameLevel();
        //level = gameLevel.getLevel();
        nextThrowFruitTime = System.currentTimeMillis() + 2000;
        //nextThrowFruitTime =  gameLevel.getNextTime();

        fruitAcceleration = gameLevel.getFruitAcceleration();
        scoreToAccomplish = gameLevel.getScoreToAccomplish();
        progressBar.standardScoreBounds(0,scoreToAccomplish);

        //this.layout = layout; //-
    }
    @Override
    public void beginContact(Contact contact)
    {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        boolean a_isGround = (ground.getBody() == a);
        boolean b_isGround = (ground.getBody() == b);

        boolean a_isPlayerBody = (player.getBody() == a);
        boolean b_isPlayerBody = (player.getBody() == b);

        boolean a_isPlayerCatcher = (player.getCatcher() == a);
        boolean b_isPlayerCatcher = (player.getCatcher() == b);

        if ((a_isPlayerBody && b_isGround) || (a_isGround && b_isPlayerBody))
        {
            player.landed();
        }
        else
        {
            if(a_isPlayerCatcher || b_isPlayerCatcher)
            {
                for (Fruit fruit: fruits)
                {
                    if((fruit.getBody() == a  || fruit.getBody() == b) && !fruit.isCaught())
                    {
                        fruit.setCaught();
                        fruit.stop();
                        int points = fruit.getPoints();
                        progressBar.setNewScore(points);
                        levelUpdate();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        boolean a_isGround = (ground.getBody() == a);
        boolean b_isGround = (ground.getBody() == b);

        boolean a_isCeiling = (ceiling.getBody() == a);
        boolean b_isCeiling = (ceiling.getBody() == b);

        boolean a_isPlayerBody = (player.getBody() == a);
        boolean b_isPlayerBody = (player.getBody() == b);


        if ((a_isPlayerBody  && b_isGround) || (a_isGround && b_isPlayerBody ))
        {
            player.landed();

        }
        else if (!a_isCeiling && !b_isCeiling && !a_isGround && !b_isGround)
        {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    public void throwObject(Fruit thisFruit)
    {
        thisFruit.activate();
        Vector2 velocity = thisFruit.getLinearVelocity();
        float speed = thisFruit.getSpeed() * fruitAcceleration;;
        velocity.x = speed;
        thisFruit.setLinearVelocity(velocity);
    }

    public double getNextThrowFruitTime() {
        return nextThrowFruitTime;
    }

    public void updateThrowFruitTime() {
        nextThrowFruitTime = System.currentTimeMillis() + (int)gameLevel.getNextTime();;
    }

    public void levelUpdate()
    {
        int currentScore = progressBar.getScore();
        if (currentScore >= scoreToAccomplish)
        {
            gameLevel.increaseLevel();
            nextThrowFruitTime = gameLevel.getNextTime();
            fruitAcceleration = gameLevel.getFruitAcceleration();
            scoreToAccomplish = gameLevel.getScoreToAccomplish();
            int newLevel = gameLevel.getLevel();
            progressBar.updateLevel(newLevel, scoreToAccomplish);
        }
    }
}
