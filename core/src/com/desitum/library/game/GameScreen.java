package com.desitum.library.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by kody on 12/27/15.
 * can be used by kody and people in [kody}]
 */
public class GameScreen implements Screen {

    private OrthographicCamera cam;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private World world;
    private WorldRenderer worldRenderer;

    private Vector3 touchPos;
    private Color clearColor;

    public GameScreen(float viewportWidth, float viewportHeight, Constructor<World> worldClass) {
        spriteBatch = new SpriteBatch();
        cam = new OrthographicCamera(viewportWidth, viewportHeight);
        cam.position.set(viewportWidth / 2, viewportHeight / 2, 0);
        viewport = new FitViewport(viewportWidth, viewportHeight, cam);

        touchPos = new Vector3(0, 0, 0);

        world = new World(cam); // Hmmm annoying to create it and then recreate it later
        try {
            world = worldClass.newInstance(cam);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        worldRenderer = new WorldRenderer(world);
        clearColor = new Color(0, 0, 0, 1);
    }

    public GameScreen(float viewportWidth, float viewportHeight) {
        spriteBatch = new SpriteBatch();
        cam = new OrthographicCamera(viewportWidth, viewportHeight);
        cam.position.set(viewportWidth / 2, viewportHeight / 2, 0);
        viewport = new FitViewport(viewportWidth, viewportHeight, cam);

        touchPos = new Vector3(0, 0, 0);

        world = new World(cam); // Hmmm annoying to create it and then recreate it later, use above to only create one
        worldRenderer = new WorldRenderer(world);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    public void update(float delta) {
        world.update(delta);
        touchPos = viewport.unproject(touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        world.updateTouchInput(touchPos, Gdx.input.isTouched());
    }

    public void draw() {
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);

        spriteBatch.begin();

        worldRenderer.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        this.worldRenderer = new WorldRenderer(world);
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }

    public Vector3 getTouchPos() {
        return touchPos;
    }

    public void setTouchPos(Vector3 touchPos) {
        this.touchPos = touchPos;
    }

    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }
}
