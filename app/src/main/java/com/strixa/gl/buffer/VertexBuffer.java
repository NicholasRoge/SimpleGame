package com.strixa.gl.buffer;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.GLObject;

import junit.framework.Assert;

import java.util.Vector;

public class VertexBuffer extends GLObject
{
    public static final int BYTES_PER_GEOMETRIC_VERTEX = VertexBuffer.FLOATS_PER_GEOMETRIC_VERTEX * (Float.SIZE / 8);
    public static final int BYTES_PER_NORMAL_VERTEX = VertexBuffer.FLOATS_PER_NORMAL_VERTEX * (Float.SIZE / 8);
    public static final int BYTES_PER_TEXTURE_VERTEX = VertexBuffer.FLOATS_PER_TEXTURE_VERTEX * (Float.SIZE / 8);
    public static final int FLOATS_PER_GEOMETRIC_VERTEX = 4;
    public static final int FLOATS_PER_NORMAL_VERTEX = 3;
    public static final int FLOATS_PER_TEXTURE_VERTEX = 3;
    public static final int STRIDE = BYTES_PER_GEOMETRIC_VERTEX + BYTES_PER_TEXTURE_VERTEX + BYTES_PER_NORMAL_VERTEX;


    public DataBuffer buffer;
    public int vertex_count;


    public VertexBuffer(Context context)
    {
        this(context,100);
    }

    public VertexBuffer(Context context,int initial_capacity)
    {
        super(context);


        this.buffer = new DataBuffer(context,GLES20.GL_ARRAY_BUFFER,initial_capacity * VertexBuffer.STRIDE);
    }

    public void bind()
    {
        this.buffer.bind();
    }

    public void free()
    {
        this.buffer.destroy();
    }

    @Override
    public int getNativeHandle()
    {
        return this.buffer.getNativeHandle();
    }

    public int getVertexCount()
    {
        return this.vertex_count;
    }

    @Override
    public boolean isValid()
    {
        return this.buffer.isValid();
    }

    public void putVertex(float[] vertex)
    {
        Assert.assertTrue(vertex != null);
        Assert.assertTrue(vertex.length == 10);


        this.putVertex(
                vertex[0],vertex[1],vertex[2],vertex[3],
                vertex[4],vertex[5],vertex[6],
                vertex[7],vertex[8],vertex[9]
        );
    }

    public void putVertex(Float[] vertex)
    {
        Assert.assertTrue(vertex != null);
        Assert.assertTrue(vertex.length == 10);


        this.putVertex(
                vertex[0], vertex[1], vertex[2], vertex[3],
                vertex[4], vertex[5], vertex[6],
                vertex[7], vertex[8], vertex[9]
        );
    }

    public void putVertex(float x,float y,float z)
    {
        this.putVertex(x,y,z,1.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f);
    }

    public void putVertex(float x,float y,float z,float w,float tx,float ty,float tz,float nx,float ny,float nz)
    {
        ++this.vertex_count;

        this.buffer.putFloats(x,y,z,w,tx,ty,tz,nx,ny,nz);
    }

    public void putVertices(float[][] vertices)
    {
        Assert.assertTrue(vertices != null);


        for(float[] vertex : vertices)
        {
            this.putVertex(vertex);
        }
    }

    public void putVertices(Float[][] vertices)
    {
        Assert.assertTrue(vertices != null);


        for(Float[] vertex : vertices)
        {
            this.putVertex(vertex);
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
