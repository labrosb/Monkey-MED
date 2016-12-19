package com.labros.myMonkey.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.labros.myMonkey.Layouts.ConfigurationLayout;
import com.labros.myMonkey.MyGame;

/**
 * Created by Labros on 23/5/2016.
 */
public class ConfigurationScreen implements Screen
{
    private ConfigurationLayout stage;

    public ConfigurationScreen(MyGame game)
    {
        stage = new ConfigurationLayout(game);
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

