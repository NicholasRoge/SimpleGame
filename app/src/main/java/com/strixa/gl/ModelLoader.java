package com.strixa.gl;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import com.strixa.gl.buffer.ElementBuffer;
import com.strixa.gl.buffer.VertexBuffer;
import com.strixa.util.ResourceUtil;

import junit.framework.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by nicho on 8/13/2015.
 */
public class ModelLoader
{
    private static class MTLLoader
    {
        Context context;
        int current_line;
        String source;


        public MTLLoader(Context context,String source) {
            Assert.assertTrue(source != null);
            Assert.assertTrue(context != null);


            this.context = context;
            this.source = source;
        }

        public Map<String,ModelComponent.Material> parse()
        {
            ModelComponent.Material active_material;
            Map<String,ModelComponent.Material> material_map;


            material_map = new HashMap<>();
            active_material = null;

            this.current_line = 0;
            for (String line : this.source.split("\n")) {
                String command[];


                ++this.current_line;

                command = line.trim().split(" ");
                if (command[0].length() == 0 || command[0].charAt(0) == '#' ) {
                    continue;
                }

                switch (command[0])
                {
                    case "d":
                    case "Tr":
                        this.parseTransparency(command,active_material);
                        break;

                    case "illum":
                        this.parseIlluminationModel(command,active_material);
                        break;

                    case "Ka":
                        this.parseAmbientColour(command, active_material);
                        break;

                    case "Kd":
                        this.parseDiffuseColour(command, active_material);
                        break;

                    case "Ks":
                        this.parseSpecularColour(command, active_material);
                        break;

                    case "map_Ka":
                        this.parseAmbientTexture(command, active_material);
                        break;

                    case "map_Kd":
                        this.parseDiffuseTexture(command,active_material);
                        break;

                    case "map_Ks":
                        this.parseSpecularTexture(command,active_material);
                        break;

                    case "newmtl":
                        active_material = this.parseNewMaterial(command,material_map);
                        break;

                    case "Ns":
                        this.parseSpecularIntensity(command,active_material);
                        break;

                    default:
                        throw new RuntimeException("Unsupported command found while parsing OBJ.  Command:  " + command[0]);
                }
            }

            return material_map;
        }

        private ModelComponent.Material parseNewMaterial(String[] command,Map<String,ModelComponent.Material> material_map)
        {
            Assert.assertTrue(command.length == 2);


            ModelComponent.Material material;


            material = new ModelComponent.Material();
            material_map.put(command[1],material);
            return material;
        }
        
        private void parseAmbientColour(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 4);


            material.setAmbientColour(Float.parseFloat(command[1]), Float.parseFloat(command[2]), Float.parseFloat(command[3]));
        }

        private void parseAmbientTexture(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            Texture2D texture;
            int       texture_resource_id;


            texture_resource_id = this.context.getResources().getIdentifier(command[1],null,null);
            if(texture_resource_id == 0)
            {
                throw new RuntimeException("Failed to obtain texture resource handle.  Cause:  Identifier \"" + command[1] + "\" could not be found.");
            }

            texture = new Texture2D(this.context);
            texture.bind();
            texture.setMinFilter(Texture2D.Filter.Linear);
            texture.setMagFilter(Texture2D.Filter.Linear);
            texture.setData(0,texture_resource_id);
            texture.unbind();
            material.setAmbientTexture(texture);
        }

        private void parseDiffuseColour(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 4);


            material.setDiffuseColour(Float.parseFloat(command[1]), Float.parseFloat(command[2]), Float.parseFloat(command[3]));
        }

        private void parseDiffuseTexture(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            Texture2D texture;
            int       texture_resource_id;


            texture_resource_id = this.context.getResources().getIdentifier(command[1],null,null);
            if(texture_resource_id == 0)
            {
                throw new RuntimeException("Failed to obtain texture resource handle.  Cause:  Identifier \"" + command[1] + "\" could not be found.");
            }

            texture = new Texture2D(this.context);
            texture.bind();
            texture.setMinFilter(Texture2D.Filter.Linear);
            texture.setMagFilter(Texture2D.Filter.Linear);
            texture.setData(0,texture_resource_id);
            texture.unbind();
            material.setDiffuseTexture(texture);
        }

        private void parseIlluminationModel(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            material.setIlluminationModel(Integer.parseInt(command[1]));
        }

        private void parseSpecularColour(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 4);


            material.setSpecularColour(Float.parseFloat(command[1]), Float.parseFloat(command[2]), Float.parseFloat(command[3]));
        }

        private void parseSpecularIntensity(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            material.setSpecularIntensity(Float.parseFloat(command[1]));
        }

        private void parseSpecularTexture(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            Texture2D texture;
            int       texture_resource_id;


            texture_resource_id = this.context.getResources().getIdentifier(command[1],null,null);
            if(texture_resource_id == 0)
            {
                throw new RuntimeException("Failed to obtain texture resource handle.  Cause:  Identifier \"" + command[1] + "\" could not be found.");
            }

            texture = new Texture2D(this.context);
            texture.bind();
            texture.setMinFilter(Texture2D.Filter.Linear);
            texture.setMagFilter(Texture2D.Filter.Linear);
            texture.setData(0,texture_resource_id);
            texture.unbind();
            material.setSpecularTexture(texture);
        }

        private void parseTransparency(String[] command,ModelComponent.Material material)
        {
            Assert.assertTrue(command.length == 2);


            material.setAlpha(Float.parseFloat(command[1]));
        }
    }

    private static class OBJLoader
    {
        Context context;
        int     current_line;
        String  source;


        public OBJLoader(Context context,String source)
        {
            Assert.assertTrue(source != null);
            Assert.assertTrue(context != null);


            this.context = context;
            this.source = source;
        }

        private ModelComponent createComponent(List<Float[]> vertices,List<Integer> face_indices,ModelComponent.Material material)
        {
            ModelComponent component;
            ElementBuffer element_buffer;
            VertexBuffer vertex_buffer;


            vertex_buffer = new VertexBuffer(this.context,vertices.size());
            element_buffer = new ElementBuffer(this.context,face_indices.size());

            component = new ModelComponent();
            if(material != null)
            {
                component.setMaterial(material);
            }
            component.getMesh().setVertexBuffer(vertex_buffer);
            component.getMesh().setElementBuffer(element_buffer);
            component.getMesh().setVertexFormat(ModelComponent.Mesh.VertexFormat.Triangles);  //This is only temporary.  It will change eventually.

            element_buffer.putIndices(face_indices.toArray(new Integer[face_indices.size()]));
            element_buffer.upload();

            vertex_buffer.putVertices(vertices.toArray(new Float[vertices.size()][10]));
            vertex_buffer.upload();

            return component;
        }

        public Model parse()
        {
            List<Integer> face_indices = new Vector<>();
            Model model = new Model();
            ModelComponent.Material material = null;
            List<Float[]> normal_vertices = new Vector<>();
            List<Float[]> texture_vertices = new Vector<>();
            List<Float[]> vertices = new Vector<>();


            for(String line : source.split("\n"))
            {
                String command[];


                ++this.current_line;

                command  = line.trim().split(" ");
                if(command[0].length() == 0 || command[0].charAt(0) == '#')
                {
                    continue;
                }

                switch(command[0])
                {
                    case "f":
                        this.parseFace(command, face_indices, vertices, texture_vertices, normal_vertices);

                        break;

                    case "mtllib":
                        this.parseMaterialLibrary(command);

                        break;

                    case "usemtl":
                        material = this.parseUseMaterial(command);

                        break;

                    case "v":
                        this.parseGeometricVertex(command, vertices);

                        break;

                    case "vn":
                        this.parseNormalVertex(command, normal_vertices);

                        break;

                    case "vt":
                        this.parseTextureVertex(command, texture_vertices);

                        break;

                    default:
                        //throw new RuntimeException("Unsupported command found while parsing OBJ.  Command:  " + command[0]);
                }
            }

            model.addComponent(this.createComponent(vertices, face_indices,material));

            return model;
        }

        private void parseFace(String[] command,List<Integer> face_indices,List<Float[]> vertices,List<Float[]> texture_vertices,List<Float[]> normal_vertices)
        {
            int[] vertex_indices;


            if(command.length < 2)
            {
                throw new RuntimeException("Unexpected number of arguments on line " + this.current_line + ".");
            }

            for(int offset = 1;offset < command.length;++offset)
            {
                Float[] vertex;
                int     vertex_index;
                Float[] texture_vertex;
                Float[] normal_vertex;
                String[] vertex_info;


                vertex_info = command[offset].split("/");

                vertex_index = Integer.parseInt(vertex_info[0]) - 1;
                vertex = vertices.get(vertex_index);
                if(vertex_info.length >= 2)
                {
                    texture_vertex = texture_vertices.get(Integer.parseInt(vertex_info[1]) - 1);
                    vertex[4] = texture_vertex[0];
                    vertex[5] = texture_vertex[1];
                    vertex[6] = texture_vertex[2];

                    if(vertex_info.length >= 3)
                    {
                        normal_vertex = normal_vertices.get(Integer.parseInt(vertex_info[2]) - 1);
                        vertex[4] = normal_vertex[0];
                        vertex[5] = normal_vertex[1];
                        vertex[6] = normal_vertex[2];
                    }
                }

                face_indices.add(vertex_index);
            }
        }

        private void parseGeometricVertex(String[] command,List<Float[]> vertices)
        {
            Float[] vertex;


            if(command.length < 4 || command.length > 5)
            {
                throw new RuntimeException("Unexpected number of arguments on line " + this.current_line + ".");
            }

            vertex = new Float[10];
            vertex[0] = Float.parseFloat(command[1]);
            vertex[1] = Float.parseFloat(command[2]);
            vertex[2] = Float.parseFloat(command[3]);
            vertex[3] = (command.length == 5 ? Float.parseFloat(command[4]) : 1.0f);

            vertex[4] = 0.0f;
            vertex[5] = 0.0f;
            vertex[6] = 0.0f;

            vertex[7] = 0.0f;
            vertex[8] = 0.0f;
            vertex[9] = 0.0f;

            vertices.add(vertex);
        }

        private void parseMaterialLibrary(String[] command)
        {
            Assert.assertTrue(command.length == 2);


            MTLLoader mtllib_loader;
            String    mtllib_source;


            mtllib_source = ResourceUtil.ReadRawResourceAsString(this.context,command[1]);
            mtllib_loader = new MTLLoader(this.context,mtllib_source);

            for(Map.Entry<String,ModelComponent.Material> material : mtllib_loader.parse().entrySet())
            {
                ModelComponent.Material.Add(material.getKey(),material.getValue());
            }
        }

        private void parseNormalVertex(String[] command,List<Float[]> normal_vertices)
        {
            Float[] vertex;


            if(command.length != 3)
            {
                throw new RuntimeException("Unexpected number of arguments on line " + this.current_line + ".");
            }

            vertex = new Float[3];
            vertex[0] = Float.parseFloat(command[1]);
            vertex[1] = Float.parseFloat(command[2]);
            vertex[2] = Float.parseFloat(command[3]);

            normal_vertices.add(vertex);
        }

        private void parseTextureVertex(String[] command,List<Float[]> texture_vertices)
        {
            Float[] vertex;


            if(command.length < 2 || command.length > 4)
            {
                throw new RuntimeException("Unexpected number of arguments on line " + this.current_line + ".");
            }

            vertex = new Float[3];
            vertex[0] = Float.parseFloat(command[1]);
            if(command.length >= 3)
            {
                vertex[1] = Float.parseFloat(command[2]);
                if(command.length >= 4)
                {
                    vertex[2] = Float.parseFloat(command[3]);
                }
                else
                {
                    vertex[2] = 0.0f;
                }
            }
            else
            {
                vertex[1] = 0.0f;
            }

            texture_vertices.add(vertex);
        }

        private ModelComponent.Material parseUseMaterial(String[] command)
        {
            Assert.assertTrue(command.length == 2);


            return ModelComponent.Material.Get(command[1]);  //Well that was easy
        }
    }

    public enum Format
    {
        OBJ
    }


    public static Model Load(Context context,int resource_id,Format format)
    {
        return Load(context,ResourceUtil.ReadRawResourceAsString(context,resource_id),format);
    }

    public static Model Load(Context context,String source,Format format)
    {
        switch(format)
        {
            case OBJ:
                return ModelLoader.LoadOBJ(context,source);

            default:
                throw new RuntimeException("Unsupported source format.");
        }
    }

    private static Model LoadOBJ(Context context,String source)
    {
        return new OBJLoader(context,source).parse();
    }
}
