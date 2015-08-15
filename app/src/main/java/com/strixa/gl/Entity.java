package com.strixa.gl;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.strixa.gl.buffer.VertexBuffer;

import junit.framework.Assert;

/**
 * Created by nicho on 8/8/2015.
 */
public class Entity
{
    Model model;
    Viewport.ModelMatrix model_matrix;
    ShaderProgram shader_program;


    public Entity()
    {
        this.setModelMatrix(new Viewport.ModelMatrix());
    }

    public Model getModel()
    {
        return this.model;
    }

    public Viewport.ModelMatrix getModelMatrix()
    {
        return this.model_matrix;
    }

    public ShaderProgram getShaderProgram()
    {
        return this.shader_program;
    }

    public boolean hasModel()
    {
        return this.getModel() != null;
    }

    public boolean hasShaderProgram()
    {
        return this.getShaderProgram() != null;
    }

    public boolean isRenderable()
    {
        return this.hasModel() && this.hasShaderProgram();
    }

    public void render(Viewport viewport,ShaderProgram program)
    {
        for(ModelComponent component : this.model.getComponents())
        {
            ModelComponent.Material material;
            ModelComponent.Mesh mesh;


            mesh = component.getMesh();
            mesh.getVertexBuffer().bind();

            material = component.getMaterial();
            if(program.hasUniform("ambient_colour"))
            {
                program.setUniformVector("ambient_colour", material.getAmbientColour());
            }
            if(program.hasUniform("diffuse_colour"))
            {
                program.setUniformVector("diffuse_colour", material.getDiffuseColour());
            }
            if(program.hasUniform("specular_colour"))
            {
                program.setUniformVector("specular_colour", material.getSpecularColour());
            }
            if(program.hasUniform("specular_intensity"))
            {
                program.setUniformValue("specular_intensity", material.getSpecularIntensity());
            }
            if(material.hasAmbientTexture())
            {
                material.getAmbientTexture().bind();
            }
            if(material.hasDiffuseTexture())
            {
                material.getDiffuseTexture().bind();
            }
            if(material.hasSpecularTexture())
            {
                material.getSpecularTexture().bind();
            }

            if(program.hasAttribute("vertex_position"))
            {
                program.enableVertexAttribute("vertex_position");
                program.setAttributeFormat("vertex_position", VertexBuffer.FLOATS_PER_GEOMETRIC_VERTEX, GLES20.GL_FLOAT, VertexBuffer.STRIDE, 0, false);
            }
            if(program.hasAttribute("vertex_texture_coordinates"))
            {
                program.enableVertexAttribute("vertex_texture_coordinates");
                program.setAttributeFormat("vertex_texture_coordinates", VertexBuffer.FLOATS_PER_TEXTURE_VERTEX, GLES20.GL_FLOAT, VertexBuffer.STRIDE, VertexBuffer.BYTES_PER_GEOMETRIC_VERTEX, false);
            }
            if(program.hasAttribute("vertex_normal"))
            {
                program.enableVertexAttribute("vertex_normal");
                program.setAttributeFormat("vertex_normal", VertexBuffer.FLOATS_PER_NORMAL_VERTEX, GLES20.GL_FLOAT, VertexBuffer.STRIDE, VertexBuffer.BYTES_PER_GEOMETRIC_VERTEX + VertexBuffer.BYTES_PER_TEXTURE_VERTEX, false);
            }

            if(program.hasUniform("projection_matrix"))
            {
                program.setUniformMatrix("projection_matrix", viewport.getProjectionMatrix(), false);
            }
            if(program.hasUniform("view_matrix"))
            {
                program.setUniformMatrix("view_matrix", viewport.getCamera().getMatrix(), false);
            }
            if(program.hasUniform("model_matrix"))
            {
                float[] matrix;


                matrix = new float[16];
                Matrix.multiplyMM(matrix,0,this.getModelMatrix().getMatrix(),0,this.model.getModelMatrix().getMatrix(),0);
                Matrix.multiplyMM(matrix,0,matrix,0,component.getModelMatrix().getMatrix(),0);
                program.setUniformMatrix("model_matrix", matrix, false);
            }

            mesh.getElementBuffer().bind();
            switch(mesh.getVertexFormat())
            {
                case Triangles:
                    GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getFaceCount() * 3,GLES20.GL_UNSIGNED_INT,0);
                    break;

                case TriangleFan:
                    GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, mesh.getFaceCount() + 2, GLES20.GL_UNSIGNED_INT, 0);
                    break;

                case TriangleStrip:
                    GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, mesh.getFaceCount() + 2, GLES20.GL_UNSIGNED_INT, 0);
                    break;
            }
            mesh.getElementBuffer().unbind();

            if(material.hasAmbientTexture())
            {
                material.getAmbientTexture().unbind();
            }
            if(material.hasDiffuseTexture())
            {
                material.getDiffuseTexture().unbind();
            }
            if(material.hasSpecularTexture())
            {
                material.getSpecularTexture().unbind();
            }

            if(program.hasAttribute("vertex_position"))
            {
                program.disableVertexAttribute("vertex_position");
            }
            if(program.hasAttribute("vertex_texture_coordinates"))
            {
                program.disableVertexAttribute("vertex_texture_coordinates");
            }
            if(program.hasAttribute("vertex_normal"))
            {
                program.disableVertexAttribute("vertex_normal");
            }

            mesh.getVertexBuffer().unbind();
        }
    }

    public void setModel(Model model)
    {
        this.model = model;
    }

    public void setModelMatrix(Viewport.ModelMatrix model_matrix)
    {
        Assert.assertTrue(model_matrix != null);


        this.model_matrix = model_matrix;
    }

    public void setShaderProgram(ShaderProgram program)
    {
        this.shader_program = program;
    }
}

