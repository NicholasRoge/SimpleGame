package com.strixa.gl.buffer;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.GLObject;
import com.strixa.gl.Texture2D;

import junit.framework.Assert;

/**
 * Created by nicho on 8/14/2015.
 */
public class FrameBuffer extends GLObject
{
    RenderBuffer render_buffer;
    Texture2D texture;


    public FrameBuffer(Context context)
    {
        super(context);


        int[] buffer = {0};


        GLES20.glGenFramebuffers(1,buffer,0);
        this.setNativeHandle(buffer[0]);
    }

    public void bind()
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.getNativeHandle());
    }

    protected void free()
    {
        GLES20.glDeleteFramebuffers(1, new int[]{this.getNativeHandle()}, 0);
        this.setNativeHandle(-1);
    }

    public RenderBuffer getRenderBuffer()
    {
        return this.render_buffer;
    }

    public Texture2D getTexture()
    {
        return this.texture;
    }

    public boolean isValid()
    {
        return this.getNativeHandle() != -1 && GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) == GLES20.GL_FRAMEBUFFER_COMPLETE;
    }

    public void setRenderBuffer(RenderBuffer buffer)
    {
        Assert.assertTrue(buffer != null);


        this.render_buffer = buffer;

        this.bind();
        this.render_buffer.bind();
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,GLES20.GL_DEPTH_ATTACHMENT,GLES20.GL_RENDERBUFFER,this.render_buffer.getNativeHandle());
        if(!this.isValid())
        {
            throw new RuntimeException("Call to glFramebufferRenderbuffer failed.");
        }
        this.render_buffer.unbind();
        this.unbind();
    }

    public void setTexture(Texture2D texture)
    {
        Assert.assertTrue(texture != null);


        this.texture = texture;

        this.bind();
        texture.bind();
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,this.texture.getNativeHandle(),0);
        if(!this.isValid())
        {
            throw new RuntimeException("Call to glFramebufferTexture2D failed.");
        }
        texture.unbind();
        this.unbind();


    }

    public void unbind()
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
    }
}
