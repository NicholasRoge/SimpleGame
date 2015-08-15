struct Light
{
    vec3 ambient_colour;
    vec3 diffuse_colour;
    vec3 direction;
};


vec4 CalculateDiffuseReflection(vec3 surface_diffuse_colour,vec3 surface_normal,Light light)
{
    float light_intensity;


    light_intensity = dot(surface_normal,-light.direction);
    if(light_intensity > 0.0f)
    {
        return vec4(light_intensity * surface_diffuse_colour.rgb,1.0f);
    }
    else
    {
        return vec4(0.0f,0.0f,0.0f,0.0f);
    }
}