package main.me.geakstr.engine.geometry;

public class Vec3i {
    public int x, y, z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public Vec3i(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public Vec3i add(Vec3i v) {
        return new Vec3i(x + v.x, y + v.y, z + v.z);
    }

    public Vec3i sub(Vec3i v) {
        return new Vec3i(x - v.x, y - v.y, z - v.z);
    }

    public Vec3i cross(Vec3i v) {
        return new Vec3i(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public Vec3i mul(float f) {
        return new Vec3i((int) (x * f), (int) (y * f), (int) (z * f));
    }

    public int mul(Vec3i v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3i normalize() {
        return normalize(1);
    }

    public Vec3i normalize(int l) {
        Vec3i v = this.mul(l / norm());

        this.x = v.x;
        this.y = v.y;
        this.z = v.z;

        return this;
    }

    public String toString() {
        return String.format("Vec3i | x : %d; y : %d; z : %d", x, y, z);
    }
}
