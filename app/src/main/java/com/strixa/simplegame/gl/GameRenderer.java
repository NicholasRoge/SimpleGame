package com.strixa.simplegame.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.strixa.gl.Entity;
import com.strixa.gl.Model;
import com.strixa.gl.ModelComponent;
import com.strixa.gl.ModelLoader;
import com.strixa.gl.Renderer;
import com.strixa.gl.Scene;
import com.strixa.gl.Shader;
import com.strixa.gl.ShaderProgram;
import com.strixa.gl.Viewport;
import com.strixa.gl.buffer.ElementBuffer;
import com.strixa.gl.buffer.VertexBuffer;
import com.strixa.simplegame.R;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer extends Renderer
{
    private float[] background_colour;
    private ShaderProgram shader_program;
    private Scene scene;
    private Viewport viewport;



    public GameRenderer(Context context)
    {
        super(context);


        this.background_colour = new float[]{48.0f/255.0f,32.0f/255.0f,96.0f/255.0f};
    }

    public void initalizeGL()
    {
        GLES20.glClearColor(this.background_colour[0], this.background_colour[1], this.background_colour[2], 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    public void initializeModels()
    {
        Model guilmon_model;


        guilmon_model = ModelLoader.Load(this.getContext(), R.raw.guilmon_model, ModelLoader.Format.OBJ);
        Model.Define("Guilmon",guilmon_model);

        Model.Define("Square", new Model() {{
            this.addComponent(new ModelComponent() {{
                this.getMesh().setVertexFormat(Mesh.VertexFormat.TriangleStrip);
                this.getMesh().setVertexBuffer(new VertexBuffer(GameRenderer.this.getContext()){{
                    this.putVertex(0, 0, 1);
                    this.putVertex(0, 0, 0);
                    this.putVertex(1, 0, 1);
                    this.putVertex(1, 0, 0);

                    this.upload();
                }});
                this.getMesh().setElementBuffer(new ElementBuffer(GameRenderer.this.getContext()) {{
                    this.putIndices(0,1,2,3);

                    this.upload();
                }});
            }});
        }});
    }

    public void initializeScene()
    {
        this.scene = new Scene(this.getContext()){{
            this.setViewport(GameRenderer.this.viewport);

            this.setFogColour(GameRenderer.this.background_colour);

            this.addEntity(new Entity() {{
                this.setModel(Model.LoadDefined("Square"));

                this.getModelMatrix().scale(100);
                this.getModelMatrix().translate(-0.5f, 0.0f, -0.5f);

                this.setShaderProgram(new ShaderProgram(GameRenderer.this.getContext()){{
                    this.attachShader(Shader.LoadShader(this.getContext(),R.raw.floor_vertex_shader,Shader.Type.VertexShader));
                    this.attachShader(Shader.LoadShader(this.getContext(),R.raw.floor_fragment_shader,Shader.Type.FragmentShader));
                    if (!this.link()) {
                        throw new RuntimeException("The shader program failed to link.  Info:  " + this.getInfo());
                    }
                }});
            }});

            this.addEntity(new Entity() {{
                this.setModel(Model.LoadDefined("Guilmon"));

                this.setShaderProgram(new ShaderProgram(GameRenderer.this.getContext()){{
                    this.attachShader(Shader.LoadShader(this.getContext(),R.raw.vertex_shader,Shader.Type.VertexShader));
                    this.attachShader(Shader.LoadShader(this.getContext(),R.raw.fragment_shader,Shader.Type.FragmentShader));
                    if(!this.link())
                    {
                        throw new RuntimeException("The shader program failed to link.  Info:  " + this.getInfo());
                    }
                }});
            }});
        }};
    }
    public void initializeViewport()
    {
        this.viewport = new Viewport();
    }

    public void onSurfaceCreated(GL10 unused,EGLConfig config)
    {
        this.initalizeGL();
        this.initializeViewport();
        this.initializeModels();
        this.initializeScene();
    }

    double theta = 0;

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        this.viewport.getCamera().setPosition(2f * (float) Math.cos(theta), 1f, 2f * (float) Math.sin(theta));
        this.viewport.getCamera().setFocus(0, 0.25f, 0);
        theta += (Math.PI/180.0)/2;

        this.scene.render();
    }

    public void onSurfaceChanged(GL10 unused,int width,int height)
    {
        this.viewport.setDimensions(width,height);
    }
}
