#include [light_manipulation]


uniform vec3 ambient_colour;
uniform vec3 diffuse_colour;
uniform vec3 specular_colour;
uniform sampler2D diffuse_texture;

varying vec3 texture_coordinates;


void main()
{
    vec4 combined_ambient_colour;
    vec4 combined_diffuse_colour;
    Light light;


    light.ambient_colour = vec3(1.0f,1.0f,1.0f);
    light.diffuse_colour = vec3(1.0f,1.0f,1.0f);
    light.direction = vec3(0.0f,-1.0f,0.0f);

    combined_ambient_colour = vec4(ambient_colour,1.0f);
    combined_diffuse_colour = texture2D(diffuse_texture,texture_coordinates.xy);//CalculateDiffuseReflection(diffuse_colour,vec3(0.0f,1.0f,0.0f),light);

    gl_FragColor = combined_ambient_colour + combined_diffuse_colour;//AddAtmosphere(fragment_colour);
}