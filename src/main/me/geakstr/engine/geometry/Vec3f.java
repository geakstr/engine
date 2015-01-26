package main.me.geakstr.engine.geometry;

public class Vec3f {
    public float x, y, z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f add(Vec3f v) {
        return new Vec3f(x + v.x, y + v.y, z + v.z);
    }

    public Vec3f sub(Vec3f v) {
        return new Vec3f(x - v.x, y - v.y, z - v.z);
    }

    public Vec3f cross(Vec3f v) {
        return new Vec3f(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public Vec3f mul(float f) {
        return new Vec3f(x * f, y * f, z * f);
    }

    public float mul(Vec3f v) {
        return  (x * v.x + y * v.y + z * v.z);
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3f normalize() {
        return normalize(1);
    }

    public Vec3f normalize(int l) {
        Vec3f v = this.mul(l / norm());

        this.x = v.x;
        this.y = v.y;
        this.z = v.z;

        return this;
    }

    public String toString() {
        return String.format("Vec3 | x : %d; y : %d; z : %d", (int) x, (int) y, (int) z);
    }
}
