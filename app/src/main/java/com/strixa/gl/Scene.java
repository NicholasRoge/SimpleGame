package com.strixa.gl;

import android.content.Context;

import com.strixa.gl.buffer.FrameBuffer;
import com.strixa.simplegame.R;
import com.strixa.simplegame.gl.GameRenderer;

import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scene
{
    private float[] fog_colour = new float[3];
    private float fog_density;
    private List<Entity> entities = new ArrayList<>();
    private FrameBuffer frame_buffer;
    private ShaderProgram shader_program;
    private Viewport viewport;


    public Scene(final Context context)
    {
        this.setViewport(new Viewport());
        this.setFogColour(1.0f, 1.0f, 1.0f);
        this.setFogDensity(1.0f);
        /*this.setShaderProgram(new ShaderProgram() {{
            this.attachShader(new Shader.VertexShader() {{
                try {
                    this.setSource(context, R.raw.scene_vertex_shader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (!this.compile()) {
                    throw new RuntimeException("A shader failed to compile.  Info:  " + this.getInfo());
                }
            }});
            this.attachShader(new Shader.FragmentShader() {{
                try {
                    this.setSource(context, R.raw.scene_fragment_shader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (!this.compile()) {
                    throw new RuntimeException("A shader failed to compile.  Info:  " + this.getInfo());
                }
            }});
            if (!this.link()) {
                throw new RuntimeException("The shader program failed to link.  Info:  " + this.getInfo());
            }
        }});*/
    }

    public void addEntity(Entity entity)
    {
        this.entities.add(entity);
    }

    public Viewport getViewport()
    {
        return this.viewport;
    }

    public List<Entity> getEntities()
    {
        return this.entities;
    }

    public float[] getFogColour()
    {
        return this.fog_colour;
    }

    public float getFogDensity()
    {
        return this.fog_density;
    }

    protected ShaderProgram getShaderProgram()
    {
        return this.shader_program;
    }

    public void removeEntity(Entity entity)
    {
        this.entities.remove(entity);
    }

    public void render()
    {
        ShaderProgram program;


        for(Entity entity : this.getEntities())
        {
            if(entity.isRenderable())
            {
                program = entity.getShaderProgram();
                program.bind();

                entity.render(this.getViewport(), program);

                program.unbind();
            }
        }

        /*program = this.getShaderProgram();
        program.bind();
        program.setUniformVector("fog_colour",fog_colour);
        program.setUniformValue("fog_density",fog_density);
        for(Entity entity : this.getEntities())
        {
            if(entity.isRenderable())
            {
                entity.render(this.getViewport(),program);
            }
        }
        program.unbind();*/
    }

    protected void setShaderProgram(ShaderProgram program)
    {
        Assert.assertTrue(program != null);


        this.shader_program = program;
    }

    public void setViewport(Viewport viewport)
    {
        Assert.assertTrue(viewport != null);


        this.viewport = viewport;
    }

    public void setFogColour(float[] colour)
    {
        Assert.assertTrue(colour != null);
        Assert.assertTrue(colour.length == 3);


        this.setFogColour(colour[0],colour[1],colour[2]);
    }

    public void setFogColour(float red,float green,float blue)
    {
        this.fog_colour[0] = Math.max(Math.min(red,1.0f),0.0f);
        this.fog_colour[1] = Math.max(Math.min(green, 1.0f),0.0f);
        this.fog_colour[2] = Math.max(Math.min(blue, 1.0f), 0.0f);
    }

    public void setFogDensity(float density)
    {
        this.fog_density = density;
    }
}
