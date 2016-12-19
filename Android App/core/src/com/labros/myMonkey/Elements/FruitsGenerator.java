package com.labros.myMonkey.Elements;

import com.badlogic.gdx.physics.box2d.World;
import com.labros.myMonkey.Actors.Fruit;
import com.labros.myMonkey.Actors.SubActors.Banana;
import com.labros.myMonkey.Actors.SubActors.Coconut;
import com.labros.myMonkey.Actors.SubActors.Grape;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;
import com.labros.myMonkey.World.GameWorld;

import java.util.ArrayList;
import java.util.Random;

public class FruitsGenerator
{
    private String[] fruitsList = {"Banana", "Coconut", "Grape"};

    private float fruitHeight = (GameView.getFruitHeight()) / GeneralView.getPPM();
    //private static float fruitWidth = (GameView.getFruitWidth()/2) / GeneralView.getPPM();
    private float contentHeight = GameView.getContentHeight() / GeneralView.getPPM();
    private float minBound = fruitHeight;
    private float maxBound = contentHeight - fruitHeight;
    private Fruit newFruit;

    public ArrayList generateFruits(World world, int fruitsNum)
    {
        ArrayList<Fruit> fruits = new ArrayList<Fruit>();
        for (int i=0; i<fruitsNum; i++)
        {
            Fruit myFruit = newFruit(world);
            fruits.add(myFruit);
        }
        return fruits;
    }

    private Fruit newFruit(World world)
    {
        int randomNum =  randomNumber(1, fruitsList.length);
        int max = Math.round(maxBound);
        int min = Math.round(minBound);
        float y = (randomNumber(min, max));
        switch (randomNum) {
            case 1:
            {
                newFruit = new Banana(GameWorld.createFruit(world, y));
                break;
            }
            case 2:
            {
                newFruit = new Coconut(GameWorld.createFruit(world, y));
                break;
            }
            case 3:
            {
                newFruit = new Grape(GameWorld.createFruit(world, y));
                break;
            }
            default: {
                newFruit = new Banana(GameWorld.createFruit(world, y));
            }
        }
        return newFruit;
    }

    private int randomNumber(int min, int max)
    {
      Random rand = new Random();
      int randomNum = rand.nextInt((max - min) + 1) + min;
      return randomNum;
    }
}
