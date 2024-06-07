#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextCoords;
out vec3 surfaceNormal;

uniform mat4 mvp;
uniform mat4 model;
uniform vec2 res;
uniform vec3 lightPosition;
void main() {
    vec4 worldPos = model * vec4(position, 1.0);
    gl_Position = mvp * vec4(position, 1.0);
    passTextCoords = textureCoords;

    // surfaceNormal = vec4(normal, 1.0).xyz;
    // toLightVector =  lightPosition - worldPos.xyz;
}
