package main.me.geakstr.engine.shaders;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.geometry.VecI;
import main.me.geakstr.engine.model.Model;

public interface IShader {
    VecI vertex(Model model, int f_i, int v_i);
    boolean fragment(VecF bar, int color);
}
