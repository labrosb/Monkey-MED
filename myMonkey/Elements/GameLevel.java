package com.labros.myMonkey.Elements;

import com.labros.myMonkey.View.GeneralView;

/**
 * Created by Labros on 8/6/2016.
 */
public class GameLevel
{
    private int level;
    private int scoreToAccomplish;
    private float nextThrowFruitTime;
    private float fruitAcceleration;
    private float speedCoefficient;
    private float throwCoefficient;
    private float PPM;

    public GameLevel()
    {
        PPM = GeneralView.getPPM();
        level = 1;
        fruitAcceleration = 1;
        scoreToAccomplish = 200;
        speedCoefficient = 0.02f;
        throwCoefficient = 4;
        nextThrowFruitTime = ((GeneralView.getScreenWidth()/PPM ) / throwCoefficient) * 1000;
    }

    public void increaseLevel()
    {
        level++;
        switch(level)
        {
            case 1: {
                fruitAcceleration = 1;
                scoreToAccomplish = 200;
                break;
            }
            case 2: {
                throwCoefficient += 3;
                fruitAcceleration = 1.1f;
                scoreToAccomplish = 500;
                break;
            }
            case 3: {
                fruitAcceleration = 1.3f;
                scoreToAccomplish = 900;
                break;
            }
            case 4: {
                throwCoefficient += 2;
                fruitAcceleration = 1.4f;
                scoreToAccomplish = 1300;
                break;
            }
            case 5: {
                fruitAcceleration = 1.6f;
                scoreToAccomplish = 1800;
                break;
            }
        }
        if (level > 5)
        {
            throwCoefficient += 2;
            fruitAcceleration = fruitAcceleration + 0.1f;
            float myScoreToAccomplish = scoreToAccomplish + scoreToAccomplish * speedCoefficient * 20;
            scoreToAccomplish = Math.round(myScoreToAccomplish);
        }
        nextThrowFruitTime = ((GeneralView.getScreenWidth()/PPM ) / throwCoefficient) * 1000;
    }

    public int getLevel(){return level;}

    public int getScoreToAccomplish(){return scoreToAccomplish;}

    public float getNextTime(){ return nextThrowFruitTime;}

    public float getFruitAcceleration(){return fruitAcceleration;}
}
