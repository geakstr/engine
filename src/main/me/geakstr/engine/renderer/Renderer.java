package main.me.geakstr.engine.renderer;

import main.me.geakstr.engine.geometry.Vec2i;
import main.me.geakstr.engine.geometry.Vec3f;
import main.me.geakstr.engine.geometry.Vec3i;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.model.Model;
import static main.me.geakstr.engine.utils.Swapper.swap;

public class Renderer {
    public static void line(int x0, int y0, int x1, int y1, IImage image, int color) {
        boolean steep = false;

        int tmp;
        if (Math.abs(x0 - x1) < Math.abs(y0 - y1)) {
            tmp = x0;
            x0 = y0;
            y0 = tmp;

            tmp = x1;
            x1 = y1;
            y1 = tmp;
            steep = true;
        }
        if (x0 > x1) {
            tmp = x0;
            x0 = x1;
            x1 = tmp;

            tmp = y0;
            y0 = y1;
            y1 = tmp;
        }

        int dx = x1 - x0, dy = y1 - y0;
        int d_error = Math.abs(dy) * 2, error = 0;
        int y = y0;

        for (int x = x0; x <= x1; x++) {
            if (steep) {
                image.set(y, x, color);
            } else {
                image.set(x, y, color);
            }
            error += d_error;
            if (error > dx) {
                y += y1 > y0 ? 1 : -1;
                error -= dx * 2;
            }
        }
    }

    public static void line(int x0, int y0, int x1, int y1, IImage image, Color color) {
        line(x0, y0, x1, y1, image, color.val);
    }

    public static void line(Vec2i v0, Vec2i v1, IImage image, int color) {
        line(v0.x, v0.y, v1.x, v1.y, image, color);
    }

    public static void line(Vec2i v0, Vec2i v1, IImage image, Color color) {
        line(v0, v1, image, color.val);
    }

    public static void triangle(Vec3i t0, Vec3i t1, Vec3i t2,
    							float ity0, float ity1, float ity2,
                                IImage image, Model model,
                                int[] zbuffer) {
        if (t0.y == t1.y && t0.y == t2.y) {
            return;
        }
        if (t0.y > t1.y) {
            t1 = swap(t0, t0 = t1);
            ity1 = swap(ity0, ity0 = ity1);
        }
        if (t0.y > t2.y) {
            t2 = swap(t0, t0 = t2);
            ity2 = swap(ity0, ity0 = ity2);
        }
        if (t1.y > t2.y) {
            t2 = swap(t1, t1 = t2);
            ity2 = swap(ity1, ity1 = ity2);
        }
        int total_height = t2.y - t0.y;
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.y - t0.y || t1.y == t0.y;
            int segment_height = second_half ? t2.y - t1.y : t1.y - t0.y;
            float alpha = (float) i / (float) total_height;
            float beta = (float) (i - (second_half ? t1.y - t0.y : 0)) / segment_height;
            Vec3i A = t0.add(t2.sub(t0).mul(alpha));
            Vec3i B = second_half ? t1.add(t2.sub(t1).mul(beta)) : t0.add(t1.sub(t0).mul(beta));
            float ityA = ity0 + (ity2 - ity0) * alpha;
            float ityB = second_half ? ity1 + (ity2 - ity1) * beta : ity0 + (ity1 - ity0) * beta;
            if (A.x > B.x) {
                B = swap(A, A = B);
                ityB = swap(ityA, ityA = ityB);
            }
            for (int j = A.x; j <= B.x; j++) {
                float phi = (float) (B.x == A.x ? 1. : (float) (j - A.x) / (float) (B.x - A.x));
                Vec3i P = A.add(B.sub(A).mul(phi));
                float ityP = ityA + (ityB - ityA) * phi;
                P.x = j;
                P.y = t0.y + i;
                int idx = j + (t0.y + i) * image.width();
                if (P.x >= image.width() || P.y >= image.height() || P.x<0 || P.y < 0) {
                	continue;
                }
                if (idx >= 0 && idx < zbuffer.length && zbuffer[idx] < P.z) {
                    zbuffer[idx] = P.z;
                    image.set(P.x, P.y, Color.WHITE.mul(ityP).val);
                }
            }
        }
    }
}
