package com.labros.myMonkey.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.labros.myMonkey.View.GameView;
import com.labros.myMonkey.View.GeneralView;

/**
 * Created by Labros on 1/6/2016.
 */
public class ProgressBar extends Actor
{
    private float barHeight = GameView.getHeaderHeight();
    private float barWidth = GeneralView.getScreenWidth();
    private float barPositionX = 0;
    private float barPositionY = GameView.getContentHeight();
    private float scorePositionX = GameView.getScorePositionX();
    private float scorePositionY = GameView.getScorePositionY();
    private float levelPositionX = GameView.getLevelPositionX();
    private float levelPositionY = GameView.getLevelPositionY();
    private float progressFontSize = GameView.getProgressFontSize() / GeneralView.getPPM();
    private float progressBarWidth = GameView.getProgressBarWidth();
    private float progressBarHeight = GameView.getProgressBarHeight();
    private float progressBarPositionX = GameView.getProgressBarPositionX();
    private float progressBarPositionY = GameView.getProgressBarPositionY();
    private float progressBarColorWidth = GameView.getProgressBarColorWidthMin();
    private float progressBarColorWidthMax = GameView.getProgressBarColorWidthMax();
    private float progressBarColorHeight = GameView.getProgressBarColorHeight();
    private float progressBarColorPositionX = GameView.getProgressBarColorPositionX();
    private float progressBarColorPositionY = GameView.getProgressBarColorPositionY();

    private Texture texture;
    private Texture progressTexture;
    private TextureRegion progressBarImage =
            new TextureRegion(new Texture(Gdx.files.internal(GameView.getProgressBarImageURL())));
    private BitmapFont progressFont;

    private int score = 0;
    private int newScore = 0;
    private int level = 1;
    private int minLevelScore;
    private int maxLevelScore;

    private boolean updating = false;
    //long startTime = 0;
    //double timeToUpdate = 1; //milliseconds

    public ProgressBar()
    {
        createScoreBar();
        createProgressColor();
        addLabels();
    }

    public void standardScoreBounds(int min, int max)
    {
        minLevelScore = min;
        maxLevelScore = max;
    }

    public void setNewScore(int scoreToAdd)
    {
        newScore = newScore + scoreToAdd;
        updating = true;
    }

    public int updateScore(int tempScore)
    {
        tempScore = tempScore + 1;
        return tempScore;
    }

    public void updateLevel(int level, int maxScore)
    {
        this.level = level;
        updateProgressBounds(maxScore);
        resetProgressBar();
    }

    public void updateProgressBounds(int maxScore)
    {
        minLevelScore = maxLevelScore + 1;
        maxLevelScore = maxScore;
    }

    public float updateProgressBarWidth(int currentScore)
    {
        float currentWidth = ((currentScore-minLevelScore) * progressBarColorWidthMax)
                / (maxLevelScore - minLevelScore);
        if (currentWidth < 0)
        {
            currentWidth = 0;
        }
        return currentWidth;
    }

    public void resetProgressBar()
    {
        progressBarColorWidth = 0;
    }

    public int getScore()
    {
        return newScore;
    }

    public int getLevel()
    {
        return level;
    }

    @Override
    public void act(float delta)
    {
        if (updating)
        {
            int tempScore = updateScore(score);
            progressBarColorWidth = updateProgressBarWidth(tempScore);

            if  (tempScore > score)
            {
                score = tempScore;
            }

            if (score >= newScore)
            {
                updating = false;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);

        batch.draw(texture, barPositionX, barPositionY);
        batch.draw(progressTexture, progressBarColorPositionX, progressBarColorPositionY,
                progressBarColorWidth, progressBarColorHeight);

        batch.draw(progressBarImage, progressBarPositionX , progressBarPositionY,
                progressBarWidth, progressBarHeight);
        progressFont.draw(batch, "Level: "+level, levelPositionX, levelPositionY);
        progressFont.draw(batch, " " +score+ "/"+maxLevelScore, scorePositionX, scorePositionY);
        //progressFont.draw(batch, "Score: "+score, scorePositionX, scorePositionY);
    }

    public void createScoreBar()
    {
        int width = Math.round(barWidth);
        int height = Math.round(barHeight);
        Color color = new Color(0.065f, 0.058f, 0.058f, 1);
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle( 0, 0, width, height);
        texture = new Texture(pixmap);
        texture.bind();
    }

    public void createProgressColor()
    {
        int width = Math.round(barWidth);
        int height = Math.round(barHeight);
        Color color = new Color(0.05f, 2.21f, 0, 1);
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        progressTexture = new Texture(pixmap);
        texture.bind();
    }

    public void addLabels()
    {
        progressFont = new BitmapFont();
        //progressFont.setColor(0.212f, 0.237f, 0.193f, 1);
        progressFont.setColor(Color.GREEN );
        progressFont.getRegion().getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        progressFont.getData().setScale(progressFontSize);
    }

    public void changeLevel(double val)
    {
        level = (int)val;
    }
}
