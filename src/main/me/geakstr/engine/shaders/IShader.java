package main.me.geakstr.engine.shaders;

import java.util.List;

import main.me.geakstr.engine.Viewer;
import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.model.Model;


public interface IShader {
    VecF vertex(Model model, Viewer viewer, int f_i, int v_i);
    boolean fragment(VecF bar, List<Color> color);
}
