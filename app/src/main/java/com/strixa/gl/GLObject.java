package com.strixa.gl;

import android.content.Context;

/**
 * Created by nicho_000 on 2/13/2015.
 */
abstract public class GLObject
{
    /* Properties */
    private Context context;
    private int native_handle;


    public GLObject(Context context)
    {
        this.context = context;
        this.native_handle = -1;
    }

    public void destroy()
    {
        if(!this.isValid())
        {
            this.free();

            this.native_handle = -1;
        }
    }

    abstract protected void free();

    public Context getContext()
    {
        return this.context;
    }

    public int getNativeHandle()
    {
        return this.native_handle;
    }

    public boolean isValid()
    {
        return this.native_handle != -1;
    }

    protected void setNativeHandle(int native_handle)
    {
        this.native_handle = native_handle;
    }
}
