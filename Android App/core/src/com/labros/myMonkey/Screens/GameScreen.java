package com.labros.myMonkey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.labros.myMonkey.Layouts.GameLayout;
import com.labros.myMonkey.MyGame;

/**
 * Created by Labros on 23/5/2016.
 */
public class GameScreen implements Screen
{
    //private Rectangle gameHeader;
    //private Rectangle gameContent;

    private GameLayout stage;

    public GameScreen(MyGame game)
    {
        //gameHeader = new  Rectangle(0, contentHeight, headerHeight, screenWidth);
        //gameContent = new Rectangle(0, 0, contentHeight, screenWidth);

        stage = new GameLayout(game);
    }

    @Override
    public void render(float delta)
    {        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the stage
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}

