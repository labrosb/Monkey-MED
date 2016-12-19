package com.labros.myMonkey.Elements;

/**
 * Created by labros_b on 22/7/2016.
 */
public class Normalizer
{
    public double min = 10000;
    public double max = -1;

    int counter = 0;
    double sumMin = 0;
    //double min;

    public void setMin(double signal)
    {
        if (signal < min)
        {
            if (signal > 0.0)
            {
                min = signal;
                System.out.println("signal min-->"+signal);
            }
        }
    }

    public void setMax(double signal)
    {
        if (signal > max)
        {
            max = signal;
            System.out.println("signal-->"+signal);
            System.out.println("signal max-->"+signal);
        }

    }

    /*
    public float normalizeSignal(double signal)
    {
        //System.out.println("min--> "+min);
        //System.out.println("max--> "+max);
        //System.out.println("signal--> "+signal);
        float normalized = (float) ((signal-min)/(max-min));

        if (normalized <= 0.5)
        {
            normalized = -normalized * 2;
        }
        else{
            normalized = normalized * 3;
        }

        //if (signal >= max/2)
        /{
           // normalized = (float) ((signal-min)/(max-min)) * 3;
       // }
       // else{
       //     normalized = (float) -((signal-min)/(max-min)) * 3;
      //  }

        return normalized;
    }
    */

    public float normalizeSignal(double signal)
    {
        return (float) ((signal-min)/(max-min));

    }

    public float deNormalize(float value, float min, float max)
    {
        return (value*(max-min)+min);
    }
}
