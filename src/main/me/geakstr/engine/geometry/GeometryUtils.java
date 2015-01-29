package main.me.geakstr.engine.geometry;

import main.me.geakstr.engine.math.Matrix;
import main.me.geakstr.engine.math.Vec3f;
import main.me.geakstr.engine.math.Vec4f;


public class GeometryUtils {
    public static Matrix v2m(Vec3f v) {
        Matrix m = new Matrix(4, 1);
        m.m[0][0] = v.x;
        m.m[1][0] = v.y;
        m.m[2][0] = v.z;
        m.m[3][0] = 1.f;
        return m;
    }
    
    public static Matrix v2m(Vec4f v) {
        Matrix m = new Matrix(4, 1);
        m.m[0][0] = v.x;
        m.m[1][0] = v.y;
        m.m[2][0] = v.z;
        m.m[3][0] = v.w;
        return m;
    }

    public static Vec3f m2v(Matrix m) {
        float x = m.m[0][0] / m.m[3][0];
        float y = m.m[1][0] / m.m[3][0];
        float z = m.m[2][0] / m.m[3][0];
        
        return new Vec3f(x, y, z);
    }
    
    public static Vec4f matrixToVec4f(Matrix m) {
        float x = m.m[0][0] / m.m[3][0];
        float y = m.m[1][0] / m.m[3][0];
        float z = m.m[2][0] / m.m[3][0];
        float w = m.m[3][0] / m.m[3][0];
        
        return new Vec4f(x, y, z, w);
    }
}
