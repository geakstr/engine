package main.me.geakstr.engine.math;

public class Vec2i {
	public int x, y;

	public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public Vec2i(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    
	public Vec2i(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

	public Vec2i add(Vec2i v) {
        return new Vec2i(x + v.x, y + v.y);
    }

	public Vec2i sub(Vec2i v) {
        return new Vec2i(x - v.x, y - v.y);
    }

	public Vec2i mul(float f) {
        return new Vec2i((int) (x * f), (int) (y * f));
    }

    public String toString() {
        return String.format("Vec2i | x : %d; y : %d", x, y);
    }
}