varying vec4 position;


/* StrixaLibFS Library Begin */
/*varying float eye_distance;
uniform vec4 fog_colour;
uniform float fog_density;


vec4 AddAtmosphere(vec4 colour);


vec4 AddAtmosphere(vec4 colour)
{
    return mix(colour,fog_colour,log(eye_distance)/log( 50.0f / pow(10.0f,fog_density - 1.0f)));
}*/
/* StrixaLibFS Libary End */


void main()
{
    vec4 fractional_rp = abs(fract(position));
    vec4 fragment_colour;


    if(fractional_rp.x <= 0.5f)
    {
        if(fractional_rp.z <= 0.5f)
        {
            fragment_colour = vec4(0.25f,0.25f,0.25f,1.0f);
        }
        else
        {
            fragment_colour = vec4(0.75f,0.75f,0.75f,1.0f);
        }
    }
    else
    {
        if(fractional_rp.z <= 0.5f)
        {
            fragment_colour = vec4(0.75f,0.75f,0.75f,1.0f);
        }
        else
        {
            fragment_colour = vec4(0.25f,0.25f,0.25f,1.0f);
        }
    }

    gl_FragColor = fragment_colour;//AddAtmosphere(fragment_colour);
}