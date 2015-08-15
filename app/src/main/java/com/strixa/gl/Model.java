package com.strixa.gl;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicho on 8/8/2015.
 */
public class Model
{
    private static Map<String,Model> defined_types = new HashMap<>();


    static public void Define(String name,Model model)
    {
        Assert.assertTrue(name != null);
        Assert.assertTrue(model != null);


        if(Model.defined_types.containsKey(name))
        {
            throw new RuntimeException("A model type with the name \""+name+"\" already exists.  Consider removing the existing type.");
        }
        else
        {
            Model.defined_types.put(name,model);
        }
    }

    static public Model LoadDefined(String name)
    {
        Assert.assertTrue(name != null);


        if(Model.defined_types.containsKey(name))
        {
            return Model.defined_types.get(name);
        }
        else
        {
            throw new RuntimeException("Model type \"" + name + "\" is undefined.");
        }
    }

    static public void Undefine(String name)
    {
        Assert.assertTrue(name != null);


        Model.defined_types.remove(name);
    }


    private List<ModelComponent> components = new ArrayList<>();
    private Viewport.ModelMatrix model_matrix;


    public Model()
    {
        this.setModelMatrix(new Viewport.ModelMatrix());
    }

    public void addComponent(ModelComponent component)
    {
        this.components.add(component);
    }

    public List<ModelComponent> getComponents()
    {
        return this.components;
    }

    public Viewport.ModelMatrix getModelMatrix()
    {
        return this.model_matrix;
    }

    public void setModelMatrix(Viewport.ModelMatrix matrix)
    {
        Assert.assertTrue(matrix != null);


        this.model_matrix = matrix;
    }
}
