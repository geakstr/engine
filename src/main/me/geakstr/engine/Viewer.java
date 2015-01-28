package main.me.geakstr.engine;

import main.me.geakstr.engine.geometry.Matrix;
import main.me.geakstr.engine.geometry.Vec3f;

public class Viewer {
	private Vec3f light_dir;
	private Vec3f eye;
	private Vec3f center;
	private int depth;
	
	public Viewer(Vec3f light_dir, Vec3f eye, Vec3f center, int depth) {
		this.light_dir = light_dir;
		this.eye = eye;
		this.center = center;
		this.depth = depth;
	}
	
	public Viewer(Vec3f light_dir, Vec3f eye, int depth) {
		this(light_dir, eye, new Vec3f(0, 0, 0), depth);
	}
	
	public Viewer(int depth) {
		this(new Vec3f(1, -3, 1), new Vec3f(1, 1, 3), new Vec3f(0, 0, 0), depth);
	}
	public Viewer() {
		this(255);
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
	
	public static Matrix lookat(Vec3f eye, Vec3f center, Vec3f up) {
	    Vec3f z = eye.sub(center).normalize();
	    Vec3f x = up.cross(z).normalize();
	    Vec3f y = z.cross(x).normalize();
	    Matrix res = Matrix.identity(4);

	    res.m()[0][0] = x.x;
        res.m()[1][0] = y.x;
        res.m()[2][0] = z.x;
        res.m()[0][3] = -center.x;
        
        res.m()[0][1] = x.y;
        res.m()[1][1] = y.y;
        res.m()[2][1] = z.y;
        res.m()[1][3] = -center.y;
        
        res.m()[0][2] = x.z;
        res.m()[1][2] = y.z;
        res.m()[2][2] = z.z;
        res.m()[2][3] = -center.z;
        
	    return res;
	}
}
