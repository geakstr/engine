package main.me.geakstr.engine.geometry;

public class GeometryUtils {
    public static Matrix v2m(VecF v) {
        Matrix m = new Matrix(4, 1);
        m.m()[0][0] = v.c[0];
        m.m()[1][0] = v.c[1];
        m.m()[2][0] = v.c[2];
        m.m()[3][0] = 1.f;
        return m;
    }

    public static VecF m2v(Matrix m) throws Exception {
        return new VecF(m.m()[0][0] / m.m()[3][0], m.m()[1][0] / m.m()[3][0], m.m()[2][0] / m.m()[3][0]);
    }
}
