package com.strixa.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by nicho on 8/2/2015.
 */
abstract public class Renderer implements GLSurfaceView.Renderer {
    Context context;


    public Renderer(Context context)
    {
        if(context == null)
        {
            throw new NullPointerException();
        }
        this.context = context;
    }

    public Context getContext()
    {
        return this.context;
    }
}
