package main.me.geakstr.engine.math;

public class Vec4f {
	public float x, y, z, w;

	public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
	public Vec4f(double x, double y, double z, double w) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
        this.w = (float) w;
    }
    
	public Vec4f(Vec4f v) {
    	this(v.x, v.y, v.z, v.w);
    }
    
	public Vec4f(Matrix m) {
    	this.x = m.m[0][0] / m.m[3][0];
    	this.y = m.m[1][0] / m.m[3][0];
    	this.z = m.m[2][0] / m.m[3][0];
    	this.w = m.m[3][0] / m.m[3][0];
    }

	public Vec4f add(Vec4f v) {
        return new Vec4f(x + v.x, y + v.y, z + v.z, w + v.w);
    }

	public Vec4f sub(Vec4f v) {
        return new Vec4f(x - v.x, y - v.y, z - v.z, z - v.w);
    }

	public Vec4f mul(float f) {
        return new Vec4f(x * f, y * f, z * f, w * f);
    }

	public float mul(Vec4f v) {
        return  (x * v.x + y * v.y + z * v.z + w * v.w);
    }

	public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

	public Vec4f normalize() {
        return normalize(1);
    }

	public Vec4f normalize(int l) {
        Vec4f v = this.mul(l / norm());

        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;

        return this;
    }

    public String toString() {
        return String.format("Vec4f | x : %f; y : %f; z : %f; w : %f;", x, y, z, w);
    }
}