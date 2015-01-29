package main.me.geakstr.engine;

import main.me.geakstr.engine.math.Matrix;
import main.me.geakstr.engine.math.Vec3f;

public class Viewer {
    private Vec3f light_dir;
    private Vec3f eye;
    private Vec3f center;

    private Matrix modelview;
    private Matrix projection;
    private Matrix viewport;

    public Viewer(Vec3f light_dir, Vec3f eye, Vec3f center, Matrix viewport) {
        this.light_dir = light_dir;
        this.eye = eye;
        this.center = center;

        this.modelview = lookat(eye, center, new Vec3f(0, 1, 0));
        this.projection = Matrix.identity(4);
        this.viewport = viewport;
        this.projection.m[3][2] = -1.f / (eye.sub(center)).norm();
    }
    public Vec3f light_dir() {
        return light_dir;
    }

    public Vec3f eye() {
        return eye;
    }

    public Vec3f center() {
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

    public static Matrix lookat(Vec3f eye, Vec3f center, Vec3f up) {
    	Vec3f z = eye.sub(center).normalize();
    	Vec3f x = up.cross(z).normalize();
    	Vec3f y = z.cross(x).normalize();
        Matrix res = Matrix.identity(4);

        res.m[0][0] = x.x;
        res.m[1][0] = y.x;
        res.m[2][0] = z.x;
        res.m[0][3] = -center.x;

        res.m[0][1] = x.y;
        res.m[1][1] = y.y;
        res.m[2][1] = z.y;
        res.m[1][3] = -center.y;

        res.m[0][2] = x.z;
        res.m[1][2] = y.z;
        res.m[2][2] = z.z;
        res.m[2][3] = -center.z;

        return res;
    }
}
