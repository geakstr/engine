package main.me.geakstr.engine.shaders;


import main.me.geakstr.engine.Viewer;
import main.me.geakstr.engine.geometry.GeometryUtils;
import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.model.Model;

public class GouraudShader implements IShader {
    private VecF[] varying_uv = new VecF[3];
    private VecF varying_intensity = new VecF(3);

    public VecF vertex(Model model, Viewer viewer, int f_i, int v_i) {
        varying_intensity.c[v_i] = Math.max(0.f, model.vn(f_i, v_i).mul(viewer.light_dir()));
        varying_uv[v_i] = model.vt(f_i, v_i);
        return GeometryUtils.m2v(viewer.viewport().mul(viewer.projection().mul(viewer.modelview().mul(GeometryUtils.v2m(model.v(f_i, v_i))))));
    }

    public boolean fragment(Model model, Viewer viewer, VecF bar) {
        VecF uv = varying_uv[0].mul(bar.x()).add(varying_uv[1].mul(bar.y()).add(varying_uv[2].mul(bar.z())));
        float intensity = varying_intensity.mul(bar);
        ShaderBuffer.color = new Color(model.diffuse(uv)).mul(intensity);
        return false;
    }
}
