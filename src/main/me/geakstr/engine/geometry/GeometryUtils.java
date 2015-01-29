package main.me.geakstr.engine.geometry;

import main.me.geakstr.engine.math.Matrix;

public class GeometryUtils {
    public static Matrix v2m(VecF v) {
        Matrix m = new Matrix(4, 1);
        m.m[0][0] = v.x();
        m.m[1][0] = v.y();
        m.m[2][0] = v.z();
        m.m[3][0] = v.dim == 4 ? v.w() : 1.f;
        return m;
    }

    public static VecF m2v(Matrix m) {
        float x = m.m[0][0] / m.m[3][0];
        float y = m.m[1][0] / m.m[3][0];
        float z = m.m[2][0] / m.m[3][0];
        
        return new VecF(x, y, z);
    }
    
    public static VecF matrixToVec4f(Matrix m) {
        float x = m.m[0][0] / m.m[3][0];
        float y = m.m[1][0] / m.m[3][0];
        float z = m.m[2][0] / m.m[3][0];
        float w = m.m[3][0] / m.m[3][0];
        
        return new VecF(x, y, z, w);
    }
}
