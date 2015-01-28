package main.me.geakstr.engine.geometry;

public class GeometryUtils {
	public static Matrix v2m(Vec3f v) {
    	Matrix m = new Matrix(4, 1);
    	m.m()[0][0] = v.x;
        m.m()[1][0] = v.y;
        m.m()[2][0] = v.z;
        m.m()[3][0] = 1.f;
    	return m;
    }
    
    public static Vec3f m2v(Matrix m) {
        return new Vec3f(m.m()[0][0] / m.m()[3][0], m.m()[1][0] / m.m()[3][0], m.m()[2][0] / m.m()[3][0]);
    }
}
