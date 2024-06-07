#version 400 core
in vec4 color1;

out vec4 outColor;

uniform vec3 color;
uniform vec2 res;
uniform float time;

void main(void) {

    vec2 uv = gl_FragCoord.xy/res;

    vec2 circleCenter =  vec2(0.5);
    vec2 distanceVector = abs(uv - circleCenter);
    float radius = 0.3;

    float dist = sqrt(dot(distanceVector, distanceVector));
    float threshold = 100;
    float luminance = dot(vec3(outColor), vec3(0.2126, 0.7152, 0.0722));
    float gradient = fwidth(luminance);
    bool isEdge = gradient > threshold;
    outColor = vec4(uv.x, 1, uv.y, 1.0);
    // vec3 c = vec3(uv.xy, 0.5+0.5*sin(time *1));


    // outColor = vec4(mix(c.x,color1.x),mix(c.y,color1.y),mix(c.z,color1.z),1);
    // outColor = vec4(c, 1.0);


}
