#version 330 core

//flat smooth noperspective
smooth in float depth;
smooth in float height;

uniform sampler1D tex;

void main(){

    // gl_FragColor = vec4(color, 1.0);
    gl_FragColor = texture(tex, height);
    gl_FragDepth = depth ;

}