#version 330 core

layout(location = 0) in vec3 position;

out float depth;
out float height;
out vec2 noiseTextureCoord;

uniform mat4 transformation;

void main(){

    height = position.z;
    noiseTextureCoord = vec2(position.x, position.y);

    vec4 p = transformation * vec4(position, 1.0);
    gl_Position = p;
    depth = p.z / 100 + 1;

}