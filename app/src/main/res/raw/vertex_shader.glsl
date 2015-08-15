#include [transform_lib]


attribute vec4 vertex_position;
attribute vec3 vertex_texture_coordinates;

varying vec3 texture_coordinates;


void main()
{
    gl_Position = ProjectionSpaceTransform(vertex_position);
    texture_coordinates = vertex_texture_coordinates;
}