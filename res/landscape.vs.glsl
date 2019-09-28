#version 330 core

layout(location = 0) in vec3 position;

out float depth;
out float height;

uniform mat4 transformation;

void main(){

    height = position.z;

    vec4 p = transformation * vec4(position, 1.0);
    gl_Position = p;
//    depth = p.z * -4.0;
    depth = p.z / 100 + 1;
//    depth = p.z / 2 + 0.5;
//    depth =  1;

}