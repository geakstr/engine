package main.me.geakstr.engine.math;

public class Vec2f {
	public float x, y;

	public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
	public Vec2f(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public Vec2f(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }
    
	public Vec2f add(Vec2f v) {
        return new Vec2f(x + v.x, y + v.y);
    }

	public Vec2f sub(Vec2f v) {
        return new Vec2f(x - v.x, y - v.y);
    }

	public Vec2f mul(float f) {
        return new Vec2f(x * f, y * f);
    }

    public String toString() {
        return String.format("Vec2f | x : %f; y : %f", x, y);
    }
}