package main.me.geakstr.engine;

import main.me.geakstr.engine.geometry.Matrix;
import main.me.geakstr.engine.geometry.VecF;

public class Viewer {
    private VecF light_dir;
    private VecF eye;
    private VecF center;
    private int depth;

    public Viewer(VecF light_dir, VecF eye, VecF center, int depth) {
        this.light_dir = light_dir;
        this.eye = eye;
        this.center = center;
        this.depth = depth;
    }

    public Viewer(VecF light_dir, VecF eye, int depth) {
        this(light_dir, eye, new VecF(0, 0, 0), depth);
    }

    public Viewer(int depth) {
        this(new VecF(1, -3, 1), new VecF(1, 1, 3), new VecF(0, 0, 0), depth);
    }

    public Viewer() {
        this(255);
    }

    public VecF light_dir() {
        return light_dir;
    }

    public VecF eye() {
        return eye;
    }

    public VecF center() {
        return center;
    }

    public int depth() {
        return depth;
    }

    public Matrix viewport(int x, int y, int w, int h) {
        Matrix m = Matrix.identity(4);
        m.m()[0][3] = x + w / 2.f;
        m.m()[1][3] = y + h / 2.f;
        m.m()[2][3] = depth / 2.f;

        m.m()[0][0] = w / 2.f;
        m.m()[1][1] = h / 2.f;
        m.m()[2][2] = depth / 2.f;
        return m;
    }

    public static Matrix lookat(VecF eye, VecF center, VecF up) {
        VecF z = eye.sub(center).normalize();
        VecF x = up.cross(z).normalize();
        VecF y = z.cross(x).normalize();
        Matrix res = Matrix.identity(4);

        res.m()[0][0] = x.c[0];
        res.m()[1][0] = y.c[0];
        res.m()[2][0] = z.c[0];
        res.m()[0][3] = -center.c[0];

        res.m()[0][1] = x.c[1];
        res.m()[1][1] = y.c[1];
        res.m()[2][1] = z.c[1];
        res.m()[1][3] = -center.c[1];

        res.m()[0][2] = x.c[2];
        res.m()[1][2] = y.c[2];
        res.m()[2][2] = z.c[2];
        res.m()[2][3] = -center.c[2];

        return res;
    }
}
