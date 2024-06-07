#version 400 core
in vec3 position;
in vec2 textureCoords;
in vec3 color;

out vec4 color1;

uniform mat4 mvp;
void main(void) {
     //utcolor =  vec4(color, 1.0);
    float f = (gl_VertexID/7) * 1;
    // color1 = vec4(f,f*0.5,0,1);
    gl_Position = mvp * vec4(position,1.0);
    color1 = vec4(position,1.0);

}
