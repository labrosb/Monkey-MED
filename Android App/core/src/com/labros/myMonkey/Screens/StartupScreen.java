package com.labros.myMonkey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.labros.myMonkey.Layouts.StartupLayout;
import com.labros.myMonkey.MyGame;

/**
 * Created by Labros on 28/7/2016.
 */
public class StartupScreen implements Screen
{
    private StartupLayout stage;

    public StartupScreen(MyGame game)
    {
        stage = new StartupLayout(game);
    }

    @Override
    public void render(float delta)
    {
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the stage
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

}