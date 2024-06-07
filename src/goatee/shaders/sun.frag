#version 400 core

in vec2 passTextCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in mat4 tm;
out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec2 res;
uniform vec3 lightColor;
uniform float hasTexture;
uniform vec3 objColor;

void main() {

    outColor =  vec4(lightColor, 1.0);

}
