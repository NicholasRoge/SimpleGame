package com.strixa.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import junit.framework.Assert;

import java.nio.Buffer;

public class Texture2D extends GLObject
{
    public static enum Filter
    {
        Linear(GLES20.GL_LINEAR),
        LinearMipmapLinear(GLES20.GL_LINEAR_MIPMAP_LINEAR),
        LinearMipmapNearest(GLES20.GL_LINEAR_MIPMAP_NEAREST),
        Nearest(GLES20.GL_NEAREST),
        NearestMipmapLinear(GLES20.GL_NEAREST_MIPMAP_LINEAR),
        NearestMipmapNearest(GLES20.GL_NEAREST_MIPMAP_NEAREST);


        private int gl_enum;


        Filter(int gl_enum)
        {
            this.gl_enum = gl_enum;
        }

        public static Filter FromGLEnum(int gl_enum)
        {
            switch(gl_enum)
            {
                case GLES20.GL_LINEAR:
                    return Linear;

                case GLES20.GL_LINEAR_MIPMAP_LINEAR:
                    return LinearMipmapLinear;

                case GLES20.GL_LINEAR_MIPMAP_NEAREST:
                    return LinearMipmapNearest;

                case GLES20.GL_NEAREST:
                    return Nearest;

                case GLES20.GL_NEAREST_MIPMAP_LINEAR:
                    return NearestMipmapLinear;

                case GLES20.GL_NEAREST_MIPMAP_NEAREST:
                    return NearestMipmapNearest;
            }

            return null;
        }

        public int toGLEnum()
        {
            return this.gl_enum;
        }
    }

    public static enum PixelFormat
    {
        Alpha(GLES20.GL_ALPHA),
        Luminance(GLES20.GL_LUMINANCE),
        LuminanceAlpha(GLES20.GL_LUMINANCE_ALPHA),
        RGB(GLES20.GL_RGB),
        RGBA(GLES20.GL_RGBA);


        private int gl_enum;


        PixelFormat(int gl_enum)
        {
            this.gl_enum = gl_enum;
        }

        public static PixelFormat FromGLEnum(int gl_enum)
        {
            switch(gl_enum)
            {
                case GLES20.GL_ALPHA:
                    return Alpha;

                case GLES20.GL_LUMINANCE:
                    return Luminance;

                case GLES20.GL_LUMINANCE_ALPHA:
                    return LuminanceAlpha;

                case GLES20.GL_RGB:
                    return RGB;

                case GLES20.GL_RGBA:
                    return RGBA;
            }

            return null;
        }

        public int toGLEnum()
        {
            return this.gl_enum;
        }
    }

    public static enum WrapMode
    {
        ClampToEdge(GLES20.GL_CLAMP_TO_EDGE),
        MirroredRepeat(GLES20.GL_MIRRORED_REPEAT),
        Repeat(GLES20.GL_REPEAT);


        private int gl_enum;


        WrapMode(int gl_enum)
        {
            this.gl_enum = gl_enum;
        }

        public static WrapMode FromGLEnum(int gl_enum)
        {
            switch(gl_enum)
            {
                case GLES20.GL_CLAMP_TO_EDGE:
                    return ClampToEdge;

                case GLES20.GL_MIRRORED_REPEAT:
                    return MirroredRepeat;

                case GLES20.GL_REPEAT:
                    return Repeat;
            }

            return null;
        }

        public int toGLEnum()
        {
            return this.gl_enum;
        }
    }


    private int height;
    private int width;


    public Texture2D(Context context)
    {
        super(context);


        int buffer[] = {0};


        this.height = 0;
        this.width = 0;

        GLES20.glGenTextures(1,buffer,0);
        this.setNativeHandle(buffer[0]);

        this.bind();
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        this.unbind();
    }

    public void bind()
    {
        Assert.assertTrue(this.isValid());


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getNativeHandle());
    }

    public void free()
    {
        GLES20.glDeleteTextures(1,new int[]{this.getNativeHandle()},0);
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public Filter getMagFilter()
    {
        Assert.assertTrue(this.isBound());


        int[] buffer = {0};


        GLES20.glGetTexParameteriv(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,buffer,0);

        return Filter.FromGLEnum(buffer[0]);
    }

    public Filter getMinFilter()
    {
        Assert.assertTrue(this.isBound());


        int[] buffer = {0};


        GLES20.glGetTexParameteriv(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,buffer,0);

        return Filter.FromGLEnum(buffer[0]);
    }

    public WrapMode getSWrapMode()
    {
        Assert.assertTrue(this.isBound());


        int[] buffer = {0};


        GLES20.glGetTexParameteriv(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, buffer, 0);

        return WrapMode.FromGLEnum(buffer[0]);
    }

    public WrapMode getTWrapMode()
    {
        Assert.assertTrue(this.isBound());


        int[] buffer = {0};


        GLES20.glGetTexParameteriv(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, buffer, 0);

        return WrapMode.FromGLEnum(buffer[0]);
    }

    public boolean isBound()
    {
        int[] buffer = {0};


        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, buffer, 0);

        return this.getNativeHandle() == buffer[0];
    }

    public void setData(int level,PixelFormat format,int width,int height,Buffer pixels)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,level,format.toGLEnum(),width,height,0,format.toGLEnum(),GLES20.GL_UNSIGNED_BYTE,pixels);
    }

    public void setData(int level,Bitmap bitmap)
    {
        Assert.assertTrue(this.isBound());


        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,level,bitmap,0);

        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void setData(int level,int resource_id)
    {
        Bitmap                bitmap;
        BitmapFactory.Options options;


        options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(this.getContext().getResources(),resource_id,options);

        //TODO:  Currently, for some reason I have to flip the image vertically in order for the texture to be correct.  Look into teh cause of this.
        {
            Matrix matrix;


            matrix = new Matrix();
            matrix.preScale(1.0f,-1.0f);

            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        }

        this.setData(level, bitmap);
        bitmap.recycle();
    }

    public void setMagFilter(Filter function)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,function.toGLEnum());
    }

    public void setMinFilter(Filter function)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,function.toGLEnum());
    }

    public void setSWrapMode(WrapMode mode)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,mode.toGLEnum());
    }

    public void setTWrapMode(WrapMode mode)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,mode.toGLEnum());
    }

    public void unbind()
    {
        Assert.assertTrue(this.isValid());


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    public void updateData(int level,PixelFormat format,int x,int y,int width,int height,Buffer pixels)
    {
        Assert.assertTrue(this.isBound());


        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, level, x, y, width, height, format.toGLEnum(), GLES20.GL_UNSIGNED_BYTE, pixels);
    }

    public void updateData(int level,int x,int y,Bitmap bitmap)
    {
        Assert.assertTrue(this.isBound());


        GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D,level,x,y,bitmap);
    }

    public void updateData(int level,int x,int y,int resource_id)
    {
        Bitmap                bitmap;
        BitmapFactory.Options options;


        options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(this.getContext().getResources(),resource_id,options);
        this.updateData(level, x, y, bitmap);
        bitmap.recycle();
    }
}
