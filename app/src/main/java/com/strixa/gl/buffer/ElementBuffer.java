package com.strixa.gl.buffer;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.GLObject;

import junit.framework.Assert;

public class ElementBuffer extends GLObject
{
    private DataBuffer buffer;
    private int index_count;


    public ElementBuffer(Context context)
    {
        this(context,100);
    }

    public ElementBuffer(Context context,int initial_capacity)
    {
        super(context);


        this.index_count = 0;
        this.buffer = new DataBuffer(context,GLES20.GL_ELEMENT_ARRAY_BUFFER,initial_capacity * (Integer.SIZE / 8));
    }

    public void bind()
    {
        this.buffer.bind();
    }

    protected void free()
    {
        this.buffer.destroy();
    }

    public int getIndexCount()
    {
        return this.index_count;
    }

    @Override
    public int getNativeHandle()
    {
        return this.buffer.getNativeHandle();
    }

    @Override
    public boolean isValid()
    {
        return this.buffer.isValid();
    }

    public void putIndex(int index)
    {
        this.buffer.putInt(index);

        ++this.index_count;
    }

    public void putIndices(int... indices)
    {
        for(int index : indices)
        {
            this.putIndex(index);
        }
    }

    public void putIndices(Integer[] indices)
    {
        Assert.assertTrue(indices != null);


        for(Integer index : indices)
        {
            this.putIndex(index);
        }
    }

    public void unbind()
    {
        this.buffer.unbind();
    }

    public void upload()
    {
        this.buffer.upload();
    }
}
