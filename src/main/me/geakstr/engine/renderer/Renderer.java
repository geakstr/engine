package main.me.geakstr.engine.renderer;

import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;

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
}
