package main.me.geakstr.engine.shaders;

import main.me.geakstr.engine.Viewer;
import main.me.geakstr.engine.geometry.GeometryUtils;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.math.Vec3f;
import main.me.geakstr.engine.math.Vec4f;
import main.me.geakstr.engine.model.Model;

public class GouraudShader {
    private Vec3f varying_intensity;

    public Vec4f vertex(Model model, Viewer viewer, int f_i, int v_i) {
        varying_intensity.set_coord(v_i, Math.max(0.f, model.vn(f_i, v_i).mul(viewer.light_dir())));
        
        Vec4f gl_Vertex = new Vec4f(model.v(f_i, v_i));
        return GeometryUtils.matrixToVec4f(viewer.viewport().mul(viewer.projection().mul(viewer.modelview().mul(gl_Vertex))));
    }

    public boolean fragment(Vec3f bar, Color color) {
        float intensity = varying_intensity.mul(bar);
        color = Color.WHITE.mul(intensity);
        return false;
    }
}
