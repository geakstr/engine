package main.me.geakstr.engine.renderer;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.geometry.VecI;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.model.Model;

import static main.me.geakstr.engine.utils.Swapper.swap;

public class Renderer {
    public static void triangle(VecI t0, VecI t1, VecI t2,
                                VecI uv0, VecI uv1, VecI uv2,
                                IImage image, Model model,
                                float intensity, int[] zbuffer) {
        if (t0.c[1] == t1.c[1] && t0.c[1] == t2.c[1]) {
            return;
        }
        if (t0.c[1] > t1.c[1]) {
            t1 = swap(t0, t0 = t1);
            uv1 = swap(uv0, uv0 = uv1);
        }
        if (t0.c[1] > t2.c[1]) {
            t2 = swap(t0, t0 = t2);
            uv2 = swap(uv0, uv0 = uv2);
        }
        if (t1.c[1] > t2.c[1]) {
            t2 = swap(t1, t1 = t2);
            uv2 = swap(uv1, uv1 = uv2);
        }
        int total_height = t2.c[1] - t0.c[1];
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.c[1] - t0.c[1] || t1.c[1] == t0.c[1];
            int segment_height = second_half ? t2.c[1] - t1.c[1] : t1.c[1] - t0.c[1];
            float alpha = (float) i / (float) total_height;
            float beta = (float) (i - (second_half ? t1.c[1] - t0.c[1] : 0)) / segment_height;
            VecI A = t0.add(t2.sub(t0).mul(alpha));
            VecI B = second_half ? t1.add(t2.sub(t1).mul(beta)) : t0.add(t1.sub(t0).mul(beta));
            VecI uvA = uv0.add(uv2.sub(uv0).mul(alpha));
            VecI uvB = second_half ? uv1.add(uv2.sub(uv1).mul(beta)) : uv0.add(uv1.sub(uv0).mul(beta));
            if (A.c[0] > B.c[0]) {
                B = swap(A, A = B);
                uvB = swap(uvA, uvA = uvB);
            }
            for (int j = A.c[0]; j <= B.c[0]; j++) {
                float phi = (float) (B.c[0] == A.c[0] ? 1. : (float) (j - A.c[0]) / (float) (B.c[0] - A.c[0]));
                VecI P = A.add(B.sub(A).mul(phi));
                VecI uvP = uvA.add(uvB.sub(uvA).mul(phi));
                P.c[0] = j;
                P.c[1] = t0.c[1] + i;
                int idx = j + (t0.c[1] + i) * image.width();
                if (P.c[0] >= image.width() || P.c[1] >= image.height() || P.c[0] < 0 || P.c[1] < 0) {
                    continue;
                }
                if (idx >= 0 && idx < zbuffer.length && zbuffer[idx] < P.c[2]) {
                    zbuffer[idx] = P.c[2];
                    Color color = new Color(model.diffuse(uvP));
                    image.set(P.c[0], P.c[1], color.val);
                }
            }
        }
    }
}
