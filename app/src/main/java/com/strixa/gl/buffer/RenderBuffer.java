package com.strixa.gl.buffer;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.GLObject;

/**
 * Created by nicho on 8/14/2015.
 */
public class RenderBuffer extends GLObject
{
    public RenderBuffer(Context context)
    {
        super(context);


        int[] buffer = {0};


        GLES20.glGenRenderbuffers(1, buffer, 0);
        this.setNativeHandle(buffer[0]);

        this.setDimensions(0,0);
    }

    public void bind()
    {
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, this.getNativeHandle());
    }

    protected void free()
    {
        GLES20.glDeleteRenderbuffers(1, new int[]{this.getNativeHandle()}, 0);
    }

    public int getHeight()
    {
        int[] buffer = {0};


        GLES20.glGetRenderbufferParameteriv(GLES20.GL_RENDERBUFFER,GLES20.GL_RENDERBUFFER_HEIGHT,buffer,0);
        return buffer[0];
    }

    public int getWidth()
    {
        int[] buffer = {0};


        GLES20.glGetRenderbufferParameteriv(GLES20.GL_RENDERBUFFER,GLES20.GL_RENDERBUFFER_WIDTH,buffer,0);
        return buffer[0];
    }

    public boolean isValid()
    {
        return this.getNativeHandle() != -1;
    }

    public void setDimensions(int width,int height)
    {
        this.bind();
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT, width, height);
        this.unbind();
    }

    public void unbind()
    {
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
    }
}
