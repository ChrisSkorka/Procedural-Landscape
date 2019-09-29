#version 430 core

//flat smooth noperspective
smooth in float depth;
smooth in float height;
smooth in vec2 noiseTextureCoord;

layout(binding=0) uniform sampler1D heightTexture;
layout(binding=1) uniform sampler2D noiseTexture;

void main(){

    vec4 color = texture(heightTexture, height);
    vec4 noise = texture2D(noiseTexture, noiseTextureCoord);
    float ratio = 0.97;

    gl_FragColor = color * ratio + noise * (1 - ratio);
    gl_FragDepth = depth;

}