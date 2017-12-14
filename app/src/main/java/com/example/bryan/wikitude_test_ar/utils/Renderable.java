package com.example.bryan.wikitude_test_ar.utils;

/**
 * Created by bryan on 12/14/2017.
 */

public abstract class Renderable {
    public float[] projectionMatrix = null;
    public float[] viewMatrix = null;

    public abstract void onSurfaceCreated();
    public abstract void onDrawFrame();
}
