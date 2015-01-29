package main.me.geakstr.engine.shaders;

import main.me.geakstr.engine.math.Vec3f;
import main.me.geakstr.engine.math.Vec4f;
import main.me.geakstr.engine.model.Model;


public interface IShader {
    Vec4f vertex(Model model, int f_i, int v_i);
    boolean fragment(Vec3f bar, int color);
}
