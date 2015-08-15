package com.strixa.gl;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import com.strixa.util.ResourceUtil;

import junit.framework.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by nicho_000 on 2/12/2015.
 */
public class Shader extends GLObject
{
    /* Types */
    public static class FragmentShader extends Shader
    {
        public FragmentShader(Context context)
        {
            super(context,Type.FragmentShader);
        }
    }

    public enum Type
    {
        FragmentShader(GLES20.GL_FRAGMENT_SHADER),
        VertexShader(GLES20.GL_VERTEX_SHADER);


        private int gl_enum;


        Type(int gl_enum)
        {
            this.gl_enum = gl_enum;
        }

        public static Type FromGLEnum(int gl_enum)
        {
            switch(gl_enum)
            {
                case GLES20.GL_FRAGMENT_SHADER:
                    return FragmentShader;

                case GLES20.GL_VERTEX_SHADER:
                    return VertexShader;
            }

            return null;
        }

        public int toGLEnum()
        {
            return this.gl_enum;
        }
    }

    public static class VertexShader extends Shader
    {
        public VertexShader(Context context)
        {
            super(context,Type.VertexShader);
        }
    }


    /* Methods */
    public Shader(Context context,Type type)
    {
        super(context);


        this.setNativeHandle(GLES20.glCreateShader(type.toGLEnum()));
    }

    public boolean compile()
    {
        Assert.assertTrue(this.isValid());

        GLES20.glCompileShader(this.getNativeHandle());

        return this.isCompiled();
    }

    protected void free()
    {
        GLES20.glDeleteShader(this.getNativeHandle());
    }

    public String getInfo()
    {
        Assert.assertTrue(this.isValid());

        return GLES20.glGetShaderInfoLog(this.getNativeHandle());
    }

    public String getSource()
    {
        Assert.assertTrue(this.isValid());

        /*
            Note:  The following implementation can be used if the API level is moved to 17.
            return GLES20.glGetShaderSource(this.gl_handle);
         */
        // TODO:  Find a workaround for this.
        throw new UnsupportedOperationException("This method is currently unsupported pending API level increase.");
    }

    public Type getType()
    {
        int buffer[] = {0};


        Assert.assertTrue(this.isValid());

        GLES20.glGetShaderiv(this.getNativeHandle(),GLES20.GL_SHADER_TYPE,buffer,0);
        return Type.FromGLEnum(buffer[0]);
    }

    public boolean isCompiled()
    {
        int buffer[] = {0};


        Assert.assertTrue(this.isValid());

        GLES20.glGetShaderiv(this.getNativeHandle(),GLES20.GL_COMPILE_STATUS,buffer,0);
        return buffer[0] != GLES20.GL_FALSE;
    }

    @Override
    public boolean isValid()
    {
        if(this.getNativeHandle() > 0)
        {
            return super.isValid();
        }
        else
        {
            return false;
        }
    }

    public static Shader LoadShader(Context context,int resource_id,Type type)
    {
        Shader shader;


        switch(type)
        {
            case FragmentShader:
                shader = new FragmentShader(context);
                break;

            case VertexShader:
                shader = new VertexShader(context);
                break;

            default:
                throw new RuntimeException("Unsupported shader type.");
        }

        shader.setSource(resource_id);
        if(!shader.compile())
        {
            throw new RuntimeException("A shader failed to compile.  Info:  " + shader.getInfo());
        }

        return shader;
    }

    private static String PreprocessSource(Context context,String source)
    {
        String preprocessed_source;


        preprocessed_source = "";
        for(String line : source.split("\n"))
        {
            line = line.trim();


            if(line.startsWith("#include "))
            {
                String include_file;


                include_file = line.substring(9,line.length()).trim();
                switch(include_file.charAt(0))
                {
                    case '[':
                        include_file = include_file.substring(1,include_file.length() - 1).trim();
                        preprocessed_source += Shader.PreprocessSource(context,ResourceUtil.ReadRawResourceAsString(context,include_file)) + "\n";
                        break;

                    case '<':
                    case '"':
                        throw new RuntimeException("Including from that source requires implementation.");

                    default:
                        throw new RuntimeException("Unsupported include source.");
                }
            }
            else
            {
                preprocessed_source += line + "\n";
            }
        }

        return preprocessed_source;
    }

    public void setSource(String source)
    {
        Assert.assertTrue(this.isValid());
        Assert.assertTrue(source != null);


        GLES20.glShaderSource(this.getNativeHandle(),Shader.PreprocessSource(this.getContext(),source));
    }

    public void setSource(int resource_id)
    {
        String resource_type;
        String source;


        resource_type = this.getContext().getResources().getResourceTypeName(resource_id);
        switch(resource_type)
        {
            case "string":
                source = this.getContext().getResources().getString(resource_id);
                break;

            case "raw":
                source = ResourceUtil.ReadRawResourceAsString(this.getContext(),resource_id);
                break;

            default:
                throw new RuntimeException("Invalid resource type.");
        }

        this.setSource(source);
    }

    @Override
    public String toString()
    {
        return "[Shader]NativeHandle = " + this.getNativeHandle() + ";Type = " + this.getType() + ";Status = " + (this.isValid() ? "Valid" + (this.isCompiled() ? " and Compiled" : "") : "Invalid");
    }
}
