/* StrixaLibFS Library Begin */
varying float eye_distance;
uniform vec3 fog_colour;
uniform float fog_density;


vec4 AddAtmosphere(vec4 colour);


vec4 AddAtmosphere(vec4 colour)
{
    return mix(colour,vec4(fog_colour,1.0f),log(eye_distance)/log( 50.0f / pow(10.0f,fog_density - 1.0f)));
}
/* StrixaLibFS Libary End */


void main()
{
    AddAtmosphere(vec4(1.0f,1.0f,1.0f,1.0f));
    gl_FragColor = vec4(1.0f,0.0f,0.0f,1.0f);
}