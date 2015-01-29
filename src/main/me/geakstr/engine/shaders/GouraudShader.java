package main.me.geakstr.engine.shaders;


import java.util.List;

import main.me.geakstr.engine.Viewer;
import main.me.geakstr.engine.geometry.GeometryUtils;
import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.model.Model;

public class GouraudShader implements IShader {
    private VecF varying_intensity = new VecF(3);

    public VecF vertex(Model model, Viewer viewer, int f_i, int v_i) {
        varying_intensity.c[v_i] = Math.max(0.f, model.vn(f_i, v_i).mul(viewer.light_dir()));
        VecF gl_Vertex = VecF.embed(model.v(f_i, v_i), 4);
        return GeometryUtils.m2v(viewer.viewport().mul(viewer.projection().mul(viewer.modelview().mul(gl_Vertex))));
    }

    public boolean fragment(VecF bar, List<Color> color) {
        float intensity = varying_intensity.mul(bar);
        color.set(0, Color.WHITE.mul(intensity));
        return false;
    }
}
