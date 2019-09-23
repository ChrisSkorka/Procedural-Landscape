#version 330 core

layout(location = 0) in vec3 position;

out vec3 vColor;

void main(){

    gl_Position = vec4(position, 1.0);
    vColor = vec3(position.z, position.z, position.z);

}