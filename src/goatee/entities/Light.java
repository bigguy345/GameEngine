package goatee.entities;

import goatee.shaders.ShaderHelper;

public class Light extends Sphere {

    public Light(float x, float y, float z) {
        super(1, 36, 18);
        setColor(1, 1, 0.85f);
        setScale(1);
        setPosition(10, 5, 10);
        shaderProgram = ShaderHelper.sunShader;

    }


}
