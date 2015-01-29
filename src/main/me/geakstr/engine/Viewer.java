package main.me.geakstr.engine;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.math.Matrix;

public class Viewer {
    private VecF light_dir;
    private VecF eye;
    private VecF center;

    private Matrix modelview;
    private Matrix projection;
    private Matrix viewport;

    public Viewer(VecF vecF, VecF vecF2, VecF vecF3, Matrix viewport) {
        this.light_dir = vecF;
        this.eye = vecF2;
        this.center = vecF3;

        this.modelview = lookat(vecF2, vecF3, new VecF(0, 1, 0));
        this.projection = Matrix.identity(4);
        this.viewport = viewport;
        this.projection.m[3][2] = -1.f / (vecF2.sub(vecF3)).norm();
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
        m.m[0][3] = x + w / 2.f;
        m.m[1][3] = y + h / 2.f;
        m.m[2][3] = 255 / 2.f;

        m.m[0][0] = w / 2.f;
        m.m[1][1] = h / 2.f;
        m.m[2][2] = 25 / 2.f;
        return m;
    }

    public static Matrix lookat(VecF eye, VecF center, VecF up) {
    	VecF z = eye.sub(center).normalize();
    	VecF x = up.cross(z).normalize();
    	VecF y = z.cross(x).normalize();
        Matrix res = Matrix.identity(4);

        res.m[0][0] = x.x();
        res.m[1][0] = y.x();
        res.m[2][0] = z.x();
        res.m[0][3] = -center.x();

        res.m[0][1] = x.y();
        res.m[1][1] = y.y();
        res.m[2][1] = z.y();
        res.m[1][3] = -center.y();

        res.m[0][2] = x.z();
        res.m[1][2] = y.z();
        res.m[2][2] = z.z();
        res.m[2][3] = -center.z();

        return res;
    }
}
