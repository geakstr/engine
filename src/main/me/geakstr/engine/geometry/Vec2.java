package main.me.geakstr.engine.geometry;

public class Vec2 {
    public float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    public Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    public Vec2 mul(float f) {
        return new Vec2(x * f, y * f);
    }

    public String toString() {
        return String.format("Vec2 | x : %d; y : %d", (int) x, (int) y);
    }
}
