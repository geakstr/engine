package main.me.geakstr.engine.renderer;

import main.me.geakstr.engine.Viewer;
import main.me.geakstr.engine.math.VecF;
import main.me.geakstr.engine.math.VecI;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;

import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.shaders.IShader;

public class Renderer {
    public static VecF barycentric(VecF A, VecF B, VecF C, VecI P) {
        VecF[] s = new VecF[2];
        for (int i = 0; i < 2; i++) {
            s[i] = new VecF(3);
        }

        s[0].x(C.x() - A.x());
        s[0].y(B.x() - A.x());
        s[0].z(A.x() - P.x());

        s[1].x(C.y() - A.y());
        s[1].y(B.y() - A.y());
        s[1].z(A.y() - P.y());


        VecF u = s[0].cross(s[1]);

        if (Math.abs(u.z()) > 0.01) {
            return new VecF(1.f - (u.x() + u.y()) / u.z(), u.y() / u.z(), u.x() / u.z());
        }
        return new VecF(-1, 1, 1);
    }

    public static void triangle(Model model, Viewer viewer, VecF[] pts, IShader shader, IImage image, float[] zbuffer) {
        VecI bboxmin = new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE);
        VecI bboxmax = new VecI(-Integer.MAX_VALUE, -Integer.MAX_VALUE);

        for (int i = 0; i < 3; i++) {
            bboxmin.x(Math.min(bboxmin.x(), (int) (pts[i].x() / pts[i].w())));
            bboxmax.x(Math.max(bboxmax.x(), (int) (pts[i].x() / pts[i].w())));

            bboxmin.y(Math.min(bboxmin.y(), (int) (pts[i].y() / pts[i].w())));
            bboxmax.y(Math.max(bboxmax.y(), (int) (pts[i].y() / pts[i].w())));
        }

        VecI P = new VecI(2);
        Color color = Color.BLACK;
        for (P.x(bboxmin.x()); P.x() <= bboxmax.x(); P.x(P.x() + 1)) {
            for (P.y(bboxmin.y()); P.y() <= bboxmax.y(); P.y(P.y() + 1)) {
                VecF bar = barycentric(
                        VecF.proj(pts[0].div(pts[0].w()), 2),
                        VecF.proj(pts[1].div(pts[1].w()), 2),
                        VecF.proj(pts[2].div(pts[2].w()), 2),
                        VecI.proj(P, 2));

                float z = pts[0].z() * bar.x() + pts[1].z() * bar.y() + pts[2].z() * bar.z();
                float w = pts[0].w() * bar.x() + pts[1].w() * bar.y() + pts[2].w() * bar.z();
                float frag_depth = Math.max(0, Math.min(255, z / w + 0.5f));

                int idx = P.x() + P.y() * image.width();
                if (bar.x() < 0 || bar.y() < 0 || bar.z() < 0 || zbuffer[idx] > frag_depth) {
                    continue;
                }
                if (idx >= 0 && idx < zbuffer.length) {
                    boolean discard = shader.fragment(model, viewer, bar, color);
                    if (!discard) {
                        zbuffer[idx] = frag_depth;
                        image.set(P.x(), P.y(), color.val);
                    }
                }
            }
        }
    }
}
