#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
uniform mat4 mvp;
uniform mat4 transMat;
uniform vec2 res;
uniform vec3 lightPosition;



void main(void){

    vec4 worldPos = transMat * vec4(position, 1.0);
    gl_Position = mvp * vec4(position, 1.0);
    passTextCoords = textureCoords;

    surfaceNormal = vec4(normal, 1.0).xyz;
    toLightVector =  lightPosition - worldPos.xyz;

}