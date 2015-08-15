package com.strixa.gl.buffer;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.GLObject;

import junit.framework.Assert;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by nicho on 8/13/2015.
 */
public class DataBuffer extends GLObject
{
    private ByteBuffer buffer;
    private int        capacity; //in bytes
    private boolean    resized;
    private int        type;


    public DataBuffer(Context context,int buffer_type)
    {
        this(context,buffer_type, 1024);
    }

    public DataBuffer(Context context,int type,int initial_capacity)
    {
        super(context);


        int[] buffer = {0};


        GLES20.glGenBuffers(1,buffer,0);
        this.setNativeHandle(buffer[0]);

        this.type = type;
        this.capacity = 0;
        this.setCapacity(initial_capacity);
    }

    public void bind()
    {
        GLES20.glBindBuffer(this.getType(), this.getNativeHandle());
    }

    protected void free()
    {

    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public int getType()
    {
        return this.type;
    }

    public void putByte(byte value)
    {
        this.buffer.put(value);
    }

    public void putBytes(byte... values)
    {
        for(byte value : values)
        {
            this.putByte(value);
        }
    }

    public void putBytes(Byte[] values)
    {
        for(byte value : values)
        {
            this.putByte(value);
        }
    }

    public void putFloat(float value)
    {
        this.buffer.putFloat(value);
    }

    public void putFloats(float... values)
    {
        for(float value : values)
        {
            this.putFloat(value);
        }
    }

    public void putFloats(Float[] values)
    {
        for(float value : values)
        {
            this.putFloat(value);
        }
    }

    public void putInt(int value)
    {
        this.buffer.putInt(value);
    }

    public void putInts(int... values)
    {
        for(int value : values)
        {
            this.putInt(value);
        }
    }

    public void putInts(Integer[] values)
    {
        for(int value : values)
        {
            this.putInt(value);
        }
    }

    public void putShort(short value)
    {
        this.buffer.putShort(value);
    }

    public void putShorts(short... values)
    {
        for(short value : values)
        {
            this.putShort(value);
        }
    }

    public void putShorts(Short[] values)
    {
        for(short value : values)
        {
            this.putShort(value);
        }
    }

    public void setCapacity(int capacity)
    {
        int old_position;
        ByteBuffer resized_buffer;


        Assert.assertTrue(capacity >= 0);


        if(capacity == this.capacity)
        {
            return;
        }

        resized_buffer = ByteBuffer.allocateDirect(capacity);
        resized_buffer.order(ByteOrder.nativeOrder());

        /*if(this.capacity > 0) {
            old_position = this.buffer.position();
            this.buffer.position(0);
            resized_buffer.put(this.buffer.array(),0,old_position >= capacity ? capacity - 1 : old_position);
            resized_buffer.position(old_position);
        }*/

        this.buffer = resized_buffer;
        this.resized = true;
        this.capacity = capacity;
    }

    public void unbind()
    {
        GLES20.glBindBuffer(this.getType(),0);
    }

    public void upload()
    {
        this.bind();
        if(this.resized)
        {
            this.buffer.position(0);
            GLES20.glBufferData(this.getType(), this.capacity, this.buffer, GLES20.GL_DYNAMIC_DRAW);
        }
        else
        {
            int upload_size;


            upload_size = this.buffer.position();

            this.buffer.position(0);
            GLES20.glBufferSubData(this.getType(), 0, upload_size, this.buffer);
        }
        this.unbind();
    }
}
