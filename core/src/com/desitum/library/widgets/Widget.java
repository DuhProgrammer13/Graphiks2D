package com.desitum.library.widgets;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.desitum.library.animation.Animator;
import com.desitum.library.animation.MovementAnimator;
import com.desitum.library.animation.ScaleAnimator;

import java.util.ArrayList;

/**
 * Created by kody on 12/11/15.
 * can be used by kody and people in [kody}]
 */
public class Widget extends Sprite {

    public static final String WIDGET = "widget";
    public static final String BUTTON = "button";
    public static final String FLOATING_BUTTON = "floating button";
    public static final String EDIT_TEXT = "edit text";
    public static final String LINEAR_LAYOUT = "linear layout";
    public static final String SLIDER = "slider";
    private Camera cam;

    private String name;
    private float myScaleX;
    private float myScaleY;
    private float myX;
    private float myY;
    private float z;
    private Texture shadow;
    private Layout parent;
    private boolean enabled;

    private boolean clickIsDown;

    private ArrayList<Animator> incomingAnimators;

    private ArrayList<Animator> animators; /* Should be used as little as possible */

    private ArrayList<Animator> outgoingAnimators;

    public Widget(Texture text, String name, float width, float height, float X, float Y, Camera camera) {
        super(text, text.getWidth(), text.getHeight());
        setSize(width, height);
        this.name = name;
        this.myScaleX = 1.0f;
        this.myScaleY = 1.0f;
        this.myX = X;
        this.myY = Y;

        enabled = true;
        clickIsDown = false;

        this.cam = camera;

        this.incomingAnimators = new ArrayList<Animator>();
        this.animators = new ArrayList<Animator>();
        this.outgoingAnimators = new ArrayList<Animator>();
    }

    public void update(float delta) {
        for (Animator anim : incomingAnimators) {
            anim.update(delta);
            updateAnim(anim);
        }
        for (Animator anim : animators) {
            anim.update(delta);
            updateAnim(anim);
        }
        for (Animator anim : outgoingAnimators) {
            anim.update(delta);
            updateAnim(anim);
        }

        // Adjust the position (x, y); the scale of width and height to fit parent
        if (parent != null) {
            setX(getMyX() + parent.getBaseX());
            setY(getMyY() + parent.getBaseY());
            setScale(
                    getMyScaleX() * parent.getScaleX(),
                    getMyScaleY() * parent.getScaleY()
            );
        } else {
            setX(getMyX());
            setY(getMyY());
            setScale(getMyScaleX(), getMyScaleY());
        }
    }

    private void updateAnim(Animator anim) {
        if (anim.isRunning()) {
            if (anim instanceof MovementAnimator && ((MovementAnimator) anim).isControllingX()) {
                setMyX(((MovementAnimator) anim).getCurrentPos());
            } else if (anim instanceof MovementAnimator && ((MovementAnimator) anim).isControllingY()) {
                setMyY(((MovementAnimator) anim).getCurrentPos());
            } else if (anim instanceof ScaleAnimator && ((ScaleAnimator) anim).isControllingX()) {
                setMyScaleX(((ScaleAnimator) anim).getScaleSize());
            } else if (anim instanceof ScaleAnimator && ((ScaleAnimator) anim).isControllingY()) {
                setMyScaleY(((ScaleAnimator) anim).getScaleSize());
            }
            //TODO need to add in Rotate Animation! Should be fun with Labels and Edit Text :/
        }
    }

    @Override
    public void draw(Batch batch) {
        if (getParent() == null) drawShadow(batch);
        super.draw(batch);
    }

    public void drawShadow(Batch spriteBatch) {
        if (shadow == null) return;
        spriteBatch.draw(shadow, getX(), getY() - getHeight() * (z / 6.0f), getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                shadow.getWidth(), shadow.getHeight(), false, false);
    }

    public void updateTouchInput(Vector3 mousePos, boolean clickDown) {
        clickIsDown = clickDown;
    }

    //region Getters and Setters
    public boolean getClickIsDown() {
        return clickIsDown;
    }

    public void setOriginToParent() {
        if (parent != null) {
            this.setOrigin(parent.getOriginX(), parent.getOriginY());
        }
    }

    public float getMyScaleX() {
        return myScaleX;
    }

    public void setMyScaleX(float myScaleX) {
        this.myScaleX = myScaleX;
    }

    public float getMyScaleY() {
        return myScaleY;
    }

    public void setMyScaleY(float myScaleY) {
        this.myScaleY = myScaleY;
    }

    public float getMyX() {
        return myX;
    }

    public void setMyX(float x) {
        myX = x;
    }

    public float getMyY() {
        return myY;
    }

    public void setMyY(float y) {
        myY = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Widget getParent() {
        return parent;
    }

    public void setParent(Layout parent) {
        this.parent = parent;
    }

    public ArrayList<Animator> getIncomingAnimators() {
        return incomingAnimators;
    }

    public void setIncomingAnimators(ArrayList<Animator> incomingAnimators) {
        this.incomingAnimators = incomingAnimators;
    }

    public ArrayList<Animator> getAnimators() {
        return animators;
    }

    public void setAnimators(ArrayList<Animator> animators) {
        this.animators = animators;
    }

    public ArrayList<Animator> getOutgoingAnimators() {
        return outgoingAnimators;
    }

    public void setOutgoingAnimators(ArrayList<Animator> outgoingAnimators) {
        this.outgoingAnimators = outgoingAnimators;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Texture getShadow() {
        return shadow;
    }

    public void setShadow(Texture shadow) {
        this.shadow = shadow;
    }

    public void addIncomingAnimator(Animator animator) {
        incomingAnimators.add(animator);
    }

    public void startIncomingAnimators() {
        for (Animator anim : incomingAnimators) {
            anim.start(false);
        }
    }

    public void addOutgoingAnimator(Animator animator) {
        outgoingAnimators.add(animator);
    }

    public void startOutgoingAnimators() {
        for (Animator anim : outgoingAnimators) {
            anim.start(false);
        }
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public Widget findByName(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }

    public ArrayList<Widget> tree(ArrayList<Widget> widgets) {
        if (widgets == null) widgets = new ArrayList<Widget>();
        widgets.add(this);
        return widgets;
    }

    //endregion
}