package com.strixa.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.buffer.VertexBuffer;

import junit.framework.Assert;

public class ShaderProgram extends GLObject
{
    public ShaderProgram(Context context)
    {
        super(context);


        this.setNativeHandle(GLES20.glCreateProgram());
    }

    public void attachShader(Shader shader)
    {
        Assert.assertTrue(this.isValid());


        GLES20.glAttachShader(this.getNativeHandle(), shader.getNativeHandle());
    }

    public void bind()
    {
        Assert.assertTrue(this.isValid());

        GLES20.glUseProgram(this.getNativeHandle());
    }

    public void disableVertexAttribute(String name)
    {
        Assert.assertTrue(this.isValid());

        int location;


        location = this.getAttributeLocation(name);
        if(location < 0)
        {
            throw new RuntimeException("Vertex attribute \"" + name + "\" could not be found.");
        }
        GLES20.glEnableVertexAttribArray(location);
    }

    protected void free()
    {
        GLES20.glDeleteProgram(this.getNativeHandle());
    }

    public void detachShader(Shader shader)
    {
        Assert.assertTrue(this.isValid());

        GLES20.glDetachShader(this.getNativeHandle(), shader.getNativeHandle());
    }

    public void enableVertexAttribute(String name)
    {
        Assert.assertTrue(this.isValid());

        int location;


        location = this.getAttributeLocation(name);
        if(location < 0)
        {
            throw new RuntimeException("Vertex attribute \"" + name + "\" could not be found.");
        }
        GLES20.glEnableVertexAttribArray(location);
    }

    public int getAttributeLocation(String name)
    {
        return GLES20.glGetAttribLocation(this.getNativeHandle(), name);
    }

    public String getInfo()
    {
        Assert.assertTrue(this.isValid());

        return GLES20.glGetProgramInfoLog(this.getNativeHandle());
    }

    public int getUniformLocation(String name)
    {
        return GLES20.glGetUniformLocation(this.getNativeHandle(), name);
    }

    public boolean hasAttribute(String name)
    {
        return this.isValid() && this.isLinked() && this.getAttributeLocation(name) != -1;
    }

    public boolean hasUniform(String name)
    {
        return this.isValid() && this.isLinked() && this.getUniformLocation(name) != -1;
    }

    public boolean isBound()
    {
        int buffer[] = {0};


        Assert.assertTrue(this.isValid());

        GLES20.glGetIntegerv(GLES20.GL_CURRENT_PROGRAM, buffer, 0);
        return this.getNativeHandle() == buffer[0];
    }

    public boolean isLinked()
    {
        int buffer[] = {0};


        Assert.assertTrue(this.isValid());

        GLES20.glGetProgramiv(this.getNativeHandle(), GLES20.GL_LINK_STATUS, buffer, 0);
        return buffer[0] != GLES20.GL_FALSE;
    }

    public boolean link()
    {
        Assert.assertTrue(this.isValid());

        GLES20.glLinkProgram(this.getNativeHandle());
        return this.isLinked();
    }

    public void setUniformMatrix(int location,float value[],boolean transpose)
    {
        Assert.assertTrue(location >= 0);
        Assert.assertTrue(value != null);
        Assert.assertTrue(ShaderProgram.this.isBound());


        switch(value.length)
        {
            case 4:
                GLES20.glUniformMatrix2fv(location,1,transpose,value,0);
                break;

            case 9:
                GLES20.glUniformMatrix3fv(location,1,transpose,value,0);
                break;

            case 16:
                GLES20.glUniformMatrix4fv(location,1,transpose,value,0);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public void setUniformMatrix(String name,float value[],boolean transpose)
    {
        this.setUniformMatrix(this.getUniformLocation(name), value, transpose);
    }

    public void setUniformValue(int location,float value)
    {
        Assert.assertTrue(location >= 0);
        Assert.assertTrue(ShaderProgram.this.isBound());

        GLES20.glUniform1f(location, value);
    }

    public void setUniformValue(String name,float value)
    {
        this.setUniformValue(this.getUniformLocation(name),value);
    }

    public void setUniformVector(int location,float[] vector)
    {
        Assert.assertTrue(location >= 0);
        Assert.assertTrue(ShaderProgram.this.isBound());

        switch(vector.length)
        {
            case 1:
                GLES20.glUniform1f(location, vector[0]);
                break;

            case 2:
                GLES20.glUniform2f(location, vector[0], vector[1]);
                break;

            case 3:
                GLES20.glUniform3f(location, vector[0], vector[1], vector[2]);
                break;

            case 4:
                GLES20.glUniform4f(location, vector[0], vector[1], vector[2], vector[3]);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public void setUniformVector(String name,float[] vector)
    {
        this.setUniformVector(this.getUniformLocation(name),vector);
    }

    public void setAttributeFormat(String name, int size, int type, int stride, int offset, boolean normalize)
    {
        Assert.assertTrue(this.isValid());

        int location;


        location = this.getAttributeLocation(name);
        if(location < 0)
        {
            throw new RuntimeException("Vertex attribute \"" + name + "\" could not be found.");
        }
        GLES20.glVertexAttribPointer(location, size, type, normalize, stride, offset);
    }

    @Override
    public String toString()
    {
        return "[ShaderProgram]NativeHandle = " + this.getNativeHandle() + ";Status = " + (this.isValid() ? "Valid" : "Invalid") + ";IsBound = " + this.isBound() + ";IsLinked = " + this.isLinked();
    }

    public void unbind()
    {
        Assert.assertTrue(this.isValid());

        GLES20.glUseProgram(0);
    }
}
