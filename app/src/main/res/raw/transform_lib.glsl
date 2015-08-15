uniform mat4 model_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;


vec4 ProjectionSpaceTransform(vec3 point);

vec4 ProjectionSpaceTransform(vec4 point);

vec4 ViewSpaceTransform(vec3 point);

vec4 ViewSpaceTransform(vec4 point);

vec4 WorldSpaceTransform(vec3 point);

vec4 WorldSpaceTransform(vec4 point);


vec4 ProjectionSpaceTransform(vec3 point)
{
    return ProjectionSpaceTransform(vec4(point,1.0f));
}

vec4 ProjectionSpaceTransform(vec4 point)
{
    return projection_matrix * view_matrix * model_matrix * point;
}

vec4 ViewSpaceTransform(vec3 point)
{
    return ViewSpaceTransform(vec4(point,1.0f));
}

vec4 ViewSpaceTransform(vec4 point)
{
    return view_matrix * model_matrix * point;
}

vec4 WorldSpaceTransform(vec3 point)
{
    return WorldSpaceTransform(vec4(point,1.0f));
}

vec4 WorldSpaceTransform(vec4 point)
{
    return model_matrix * point;
}