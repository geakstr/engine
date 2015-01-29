package main.me.geakstr.engine.math;

public class MathHelpers {
    public static Matrix v2m(VecF v) {
        Matrix m = new Matrix(4, 1);
        m.m[0][0] = v.x();
        m.m[1][0] = v.y();
        m.m[2][0] = v.z();
        m.m[3][0] = v.dim == 4 ? v.w() : 1.f;
        return m;
    }

    public static VecF m2v(Matrix m) {
        float[] r = new float[m.rows];
        for (int i = 0; i < m.rows; i++) {
            r[i] = m.m[i][0] / m.m[m.rows - 1][0];
        }
        return new VecF(r);
    }
}
