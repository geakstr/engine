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
                                Vec2i uv0, Vec2i uv1, Vec2i uv2,
                                IImage image, Model model,
                                float intensity, int[] zbuffer) {
        if (t0.y == t1.y && t0.y == t2.y) {
            return;
        }
        if (t0.y > t1.y) {
            t1 = swap(t0, t0 = t1);
            uv1 = swap(uv0, uv0 = uv1);
        }
        if (t0.y > t2.y) {
            t2 = swap(t0, t0 = t2);
            uv2 = swap(uv0, uv0 = uv2);
        }
        if (t1.y > t2.y) {
            t2 = swap(t1, t1 = t2);
            uv2 = swap(uv1, uv1 = uv2);
        }
        int total_height = t2.y - t0.y;
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.y - t0.y || t1.y == t0.y;
            int segment_height = second_half ? t2.y - t1.y : t1.y - t0.y;
            float alpha = (float) i / (float) total_height;
            float beta = (float) (i - (second_half ? t1.y - t0.y : 0)) / segment_height;
            Vec3i A = t0.add(t2.sub(t0).mul(alpha));
            Vec3i B = second_half ? t1.add(t2.sub(t1).mul(beta)) : t0.add(t1.sub(t0).mul(beta));
            Vec2i uvA = uv0.add(uv2.sub(uv0).mul(alpha));
            Vec2i uvB = second_half ? uv1.add(uv2.sub(uv1).mul(beta)) : uv0.add(uv1.sub(uv0).mul(beta));
            if (A.x > B.x) {
                B = swap(A, A = B);
                uvB = swap(uvA, uvA = uvB);
            }
            for (int j = A.x; j <= B.x; j++) {
                float phi = (float) (B.x == A.x ? 1. : (float) (j - A.x) / (float) (B.x - A.x));
                Vec3i P = A.add(B.sub(A).mul(phi));
                Vec2i uvP = uvA.add(uvB.sub(uvA).mul(phi));
                P.x = j;
                P.y = t0.y + i;
                int idx = j + (t0.y + i) * image.width();
                if (zbuffer[idx] < P.z) {
                    zbuffer[idx] = P.z;
                    Color color = new Color(model.diffuse(uvP));
                    image.set(P.x, P.y, Color.rgb((int) (color.r * intensity), (int) (color.g * intensity), (int) (color.b * intensity)));
                }
            }
        }
    }
}
