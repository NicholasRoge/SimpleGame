package com.strixa.gl;

import com.strixa.gl.buffer.ElementBuffer;
import com.strixa.gl.buffer.VertexBuffer;

import junit.framework.Assert;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicho on 8/9/2015.
 */
public class ModelComponent
{
    public static class Material
    {
        public static Map<String,Material> named_materials = new HashMap<>();


        public static void Add(String name,Material material)
        {
            Assert.assertTrue(name != null);
            Assert.assertTrue(material != null);


            if(Material.named_materials.containsKey(name))
            {
                throw new RuntimeException("A material with the name \"" + name + "\" already exists.");
            }
            Material.named_materials.put(name,material);
        }

        public static Material Get(String name)
        {
            if(!Material.named_materials.containsKey(name))
            {
                throw new RuntimeException("No such material with the name \"" + name + "\" exists.");
            }
            return Material.named_materials.get(name);
        }

        public static void Remove(String name)
        {
            Assert.assertTrue(name != null);


            Material.named_materials.remove(name);
        }


        private float alpha;
        private float[] ambient_colour = new float[3];
        private Texture2D ambient_texture;
        private float[] diffuse_colour = new float[3];
        private Texture2D diffuse_texture;
        private int illumination_model;
        private float[] specular_colour = new float[3];
        private float specular_intensity;
        private Texture2D specular_texture;


        public Material()
        {
            this.setAlpha(1.0f);
            this.setAmbientColour(0.8f,0.8f,0.8f);
            this.setDiffuseColour(0.8f,0.8f,0.8f);
            this.setSpecularColour(0.0f,0.0f,0.0f);
            this.setSpecularIntensity(0.0f);
            this.setIlluminationModel(2);
        }

        public float getAlpha()
        {
            return this.alpha;
        }

        public float[] getAmbientColour()
        {
            return this.ambient_colour;
        }

        public Texture2D getAmbientTexture()
        {
            return this.ambient_texture;
        }

        public float[] getDiffuseColour()
        {
            return this.diffuse_colour;
        }

        public Texture2D getDiffuseTexture()
        {
            return this.diffuse_texture;
        }

        public int getIlluminationModel()
        {
            return this.illumination_model;
        }

        public float[] getSpecularColour()
        {
            return this.specular_colour;
        }

        public float getSpecularIntensity()
        {
            return this.specular_intensity;
        }

        public Texture2D getSpecularTexture()
        {
            return this.specular_texture;
        }

        public boolean hasAmbientTexture()
        {
            return this.ambient_texture != null;
        }

        public boolean hasDiffuseTexture()
        {
            return this.diffuse_texture != null;
        }

        public boolean hasSpecularTexture()
        {
            return this.specular_texture != null;
        }

        public void setAlpha(float value)
        {
            this.alpha = Math.max(Math.min(value,1.0f),0.0f);
        }

        public void setAmbientColour(float[] colour)
        {
            Assert.assertTrue(colour != null);
            Assert.assertTrue(colour.length >= 3);


            this.setAmbientColour(colour[0],colour[1],colour[2]);
        }

        public void setAmbientColour(float red,float green,float blue)
        {
            this.ambient_colour[0] = red;
            this.ambient_colour[1] = green;
            this.ambient_colour[2] = blue;
        }

        public void setAmbientTexture(Texture2D texture)
        {
            this.ambient_texture = texture;
        }

        public void setDiffuseColour(float[] colour)
        {
            Assert.assertTrue(colour != null);
            Assert.assertTrue(colour.length >= 3);


            this.setDiffuseColour(colour[0],colour[1],colour[2]);
        }

        public void setDiffuseColour(float red,float green,float blue)
        {
            this.diffuse_colour[0] = red;
            this.diffuse_colour[1] = green;
            this.diffuse_colour[2] = blue;
        }

        public void setDiffuseTexture(Texture2D texture)
        {
            this.diffuse_texture = texture;
        }

        public void setIlluminationModel(int model)
        {
            this.illumination_model = model;
        }

        public void setSpecularColour(float[] colour)
        {
            Assert.assertTrue(colour != null);
            Assert.assertTrue(colour.length >= 3);


            this.setSpecularColour(colour[0],colour[1],colour[2]);
        }

        public void setSpecularColour(float red,float green,float blue)
        {
            this.specular_colour[0] = red;
            this.specular_colour[1] = green;
            this.specular_colour[2] = blue;
        }

        public void setSpecularIntensity(float intensity)
        {
            this.specular_intensity = Math.max(Math.min(intensity,1000.0f),0.0f);
        }

        public void setSpecularTexture(Texture2D texture)
        {
            this.specular_texture = texture;
        }
    }

    public static class Mesh
    {
        public enum VertexFormat {
            Triangles,
            TriangleFan,
            TriangleStrip,
            Unspecified
        }


        private ElementBuffer element_buffer;
        private VertexBuffer vertex_buffer;
        private VertexFormat vertex_format;


        public Mesh()
        {
            this.setVertexFormat(VertexFormat.Unspecified);
        }

        public ElementBuffer getElementBuffer()
        {
            return this.element_buffer;
        }

        public int getFaceCount()
        {
            switch(this.vertex_format)
            {
                case TriangleFan:
                    if(this.element_buffer.getIndexCount() > 2)
                    {
                        return this.element_buffer.getIndexCount() - 2;
                    }
                    else
                    {
                        return 0;
                    }

                case Triangles:
                    return this.element_buffer.getIndexCount() / 3;

                case TriangleStrip:
                    if(this.element_buffer.getIndexCount() > 2)
                    {
                        return this.element_buffer.getIndexCount() - 2;
                    }
                    else
                    {
                        return 0;
                    }

                case Unspecified:
                    throw new RuntimeException("Vertex format must be specified before a face count may be retrieved.");
            }

            return -1;
        }

        public VertexBuffer getVertexBuffer()
        {
            return this.vertex_buffer;
        }

        public VertexFormat getVertexFormat()
        {
            return this.vertex_format;
        }

        public void setElementBuffer(ElementBuffer buffer)
        {
            this.element_buffer = buffer;
        }

        public void setVertexBuffer(VertexBuffer buffer)
        {
            this.vertex_buffer = buffer;
        }

        public void setVertexFormat(VertexFormat format)
        {
            this.vertex_format = format;
        }
    }


    private Mesh mesh = new Mesh();
    private Material material;
    private Viewport.ModelMatrix model_matrix;


    public ModelComponent()
    {
        this.setModelMatrix(new Viewport.ModelMatrix());
        this.setMaterial(new Material());
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public Mesh getMesh()
    {
        return this.mesh;
    }

    public Viewport.ModelMatrix getModelMatrix()
    {
        return this.model_matrix;
    }

    public void setModelMatrix(Viewport.ModelMatrix model_matrix)
    {
        Assert.assertTrue(model_matrix != null);


        this.model_matrix = model_matrix;
    }

    public void setMaterial(Material material)
    {
        Assert.assertTrue(material != null);


        this.material = material;
    }

    public void setMesh(Mesh mesh)
    {
        Assert.assertTrue(mesh != null);


        this.mesh = mesh;
    }
}
