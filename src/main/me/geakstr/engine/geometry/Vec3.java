package main.me.geakstr.engine.geometry;

public class Vec3 {
    public float x, y, z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    public Vec3 sub(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    public Vec3 mul(float f) {
        return new Vec3(x * f, y * f, z * f);
    }

    public float mul(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalize() {
        return normalize(1);
    }

    public Vec3 normalize(float l) {
        Vec3 v = this.mul(l / norm());

        this.x = v.x;
        this.y = v.y;
        this.z = v.z;

        return this;
    }

    public String toString() {
        return String.format("Vec3 | x : %d; y : %d; z : %d", (int) x, (int) y, (int) z);
    }
}
