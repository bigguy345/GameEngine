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

float calculateDistance(in vec2 a, in vec2 b){
    float y = res.x /res.y;
    vec2 distanceVector = abs(a - b);
    return sqrt(dot(distanceVector, distanceVector));
}

void main(void){
    // vec2 uv = gl_FragCoord.xy/res;
    // vec2 center = vec2(0.5);
    // float radius = 0.3;
    // float dist = calculateDistance(uv, center);

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float length = 1 / length(toLightVector);
    float dot1 = dot(unitNormal, unitLightVector);
    float brightness = max((dot1*(length * 15)), max(0.4 * length, 0.1));
    //
    vec3 diffuse = brightness * lightColor;
    if (hasTexture > 0){
        outColor = vec4(diffuse, 1.0)* texture(textureSampler, passTextCoords); }
    else {
        outColor =  vec4(diffuse, 1.0) * vec4(objColor, 1.0); }


}
