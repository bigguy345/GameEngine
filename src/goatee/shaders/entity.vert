#version 400 core

in vec3 vertexPosition;
in vec2 textureCoords;
in vec3 vertexNormals;

out vec2 passTextCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 fragPosition;

uniform mat4 mvp;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

uniform vec2 res;
uniform vec3 lightPosition;


//void main(void){
//
//    vec4 worldPos = model * vec4(position, 1.0);
//    gl_Position = mvp * vec4(position, 1.0);
//    passTextCoords = textureCoords;
//
//    surfaceNormal = vec4(normal, 1.0).xyz;
//    toLightVector =  lightPosition - worldPos.xyz;
//
//}

void main(void) {
    //    fragmentPosition = position;
    //
    //    // Calculate time-based distortion factor
    //    float distortion = sin(gl_FragCoord.x * 0.1 + gl_FragCoord.y * 0.1 + 0.1 * 5.0 * sin(0.1 * 3.141592 * mod(10.0 * 0.1 * gl_FragCoord.x + 20.0 * 0.1 * gl_FragCoord.y, 1.0)));
    //    // Displace the vertex position
    //    vec4 displacedPosition = model * vec4(position, 1.0) + vec4(distortion * 0.1, distortion * 0.1, 0.0, 0.0);
    //
    //    gl_Position = projection * view * displacedPosition;

    gl_Position = projection * view  * model* vec4(vertexPosition, 1.0);
    fragPosition = vertexPosition;// Pass vertex position to fragment shader

}