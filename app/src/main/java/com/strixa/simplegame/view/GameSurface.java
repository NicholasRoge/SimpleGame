package com.strixa.simplegame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.strixa.simplegame.R;
import com.strixa.simplegame.gl.GameRenderer;

import java.lang.reflect.InvocationTargetException;

public class GameSurface extends GLSurfaceView
{
    private class FramerateTicker implements Runnable
    {
        public void run()
        {
            while(GameSurface.this.frame_duration > -1) {
                while (GameSurface.this.frame_duration > 0) {
                    GameSurface.this.requestRender();

                    try {
                        Thread.currentThread().sleep(GameSurface.this.frame_duration);
                    } catch (InterruptedException exception) {
                        //TODO:  Do I actually need to do anything here?
                    }
                }

                Thread.currentThread().yield();
            }
        }
    }


    long frame_duration;
    Thread frameticker_thread;
    int framerate;


    public GameSurface(Context context) {
        super(context);

        this.initialize();
    }

    public GameSurface(Context context,AttributeSet attribute_set) {
        super(context, attribute_set);

        this.initialize();

        TypedArray attributes;
        attributes = context.getTheme().obtainStyledAttributes(attribute_set,R.styleable.GameSurface,0,0);
        {
            this.setFramerateFromAttributes(attributes);
        }
        attributes.recycle();
    }

    public int getFramerate()
    {
        return this.framerate;
    }

    private void initialize()
    {
        this.setEGLContextClientVersion(2);
        this.setRenderer(new GameRenderer(this.getContext()));
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);

        this.setFramerate(0);
        this.frameticker_thread = new Thread(new FramerateTicker());
        this.frameticker_thread.start();
    }

    public void setFramerate(int framerate)
    {
        if(framerate == 0)
        {
            this.framerate = 0;
            this.frame_duration = 0;
        }
        else if(framerate > 0)
        {
            this.framerate = framerate;
            this.frame_duration = 1000 / framerate;
        }
        else
        {
            throw new IllegalArgumentException("Invalid framerate value.  Value must not be negative.");
        }
    }

    private void setFramerateFromAttributes(TypedArray attributes)
    {
        this.setFramerate(attributes.getInt(R.styleable.GameSurface_framerate,0));
    }
}
