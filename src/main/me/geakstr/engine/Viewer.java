package main.me.geakstr.engine;

import main.me.geakstr.engine.geometry.Matrix;
import main.me.geakstr.engine.geometry.VecF;

public class Viewer {
    private VecF light_dir;
    private VecF eye;
    private VecF center;

    private Matrix modelview;
    private Matrix projection;
    private Matrix viewport;

    public Viewer(VecF light_dir, VecF eye, VecF center, Matrix viewport) {
        this.light_dir = light_dir;
        this.eye = eye;
        this.center = center;

        this.modelview = lookat(eye, center, new VecF(0, 1, 0));
        this.projection = Matrix.identity(4);
        this.viewport = viewport;
        this.projection.m()[3][2] = -1.f / (eye.sub(center)).norm();
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

    public Matrix modelview() {
        return modelview;
    }

    public Matrix projection() {
        return projection;
    }

    public Matrix viewport() {
        return viewport;
    }

    public static Matrix viewport(int x, int y, int w, int h) {
        Matrix m = Matrix.identity(4);
        m.m()[0][3] = x + w / 2.f;
        m.m()[1][3] = y + h / 2.f;
        m.m()[2][3] = 255 / 2.f;

        m.m()[0][0] = w / 2.f;
        m.m()[1][1] = h / 2.f;
        m.m()[2][2] = 25 / 2.f;
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
