package main.me.geakstr.engine.geometry;

import java.util.Arrays;

import main.me.geakstr.engine.math.Matrix;

public class VecF {
    // Dimension
    public int dim;

    // Coords
    public float[] c;

    public VecF(float... f_c) {
        this.dim = f_c.length;
        this.c = f_c;
    }

    public VecF(double... d_c) {
        this.dim = d_c.length;
        this.c = new float[this.dim];
        for (int i = 0; i < this.dim; i++) {
            this.c[i] = (float) d_c[i];
        }
    }

    public VecF(int... i_c) {
        this.dim = i_c.length;
        this.c = new float[this.dim];
        for (int i = 0; i < this.dim; i++) {
            this.c[i] = (float) i_c[i];
        }
    }

    public VecF(VecF v) {
        this.dim = v.dim;
        this.c = new float[this.dim];
        System.arraycopy(v.c, 0, this.c, 0, this.dim);
    }

    public VecF(int dim) {
        this.dim = dim;
        this.c = new float[dim];
    }

    public VecF(Matrix m) {
        if (m.rows != 4) {
            try {
                throw new Exception("Possible construct only 3 dimensional vector from 4 rows matrix");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.dim = m.rows - 1;
        this.c = new float[this.dim];
        for (int i = 0; i < this.dim; i++) {
            c[i] = m.m[i][0] / m.m[3][0];
        }
    }

    public float x() {
        return c[0];
    }

    public float y() {
        return c[1];
    }

    public float z() {
        if (c.length < 3) {
            try {
                throw new Exception("The dimensions not exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c[2];
    }

    public float w() {
        if (c.length < 4) {
            try {
                throw new Exception("The dimensions not exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c[3];
    }

    public float x(float x) {
        c[0] = x;
        return x;
    }

    public float y(float y) {
        c[1] = y;
        return y;
    }

    public float z(float z) {
        if (c.length < 3) {
            try {
                throw new Exception("The dimensions not exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        c[2] = z;
        return z;
    }

    public float w(float w) {
        if (c.length < 4) {
            try {
                throw new Exception("The dimensions not exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        c[3] = w;
        return w;
    }

    public VecF add(VecF v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        float[] r = new float[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] + v.c[i];
        }
        return new VecF(r);
    }

    public VecF sub(VecF v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        float[] r = new float[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] - v.c[i];
        }
        return new VecF(r);
    }

    public VecF mul(float f) {
        float[] r = new float[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] * f;
        }
        return new VecF(r);
    }

    public float mul(VecF v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        float r = 0;
        for (int i = 0; i < dim; i++) {
            r += c[i] * v.c[i];
        }
        return r;
    }

    public VecF div(float f) {
        float[] r = new float[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] / f;
        }
        return new VecF(r);
    }

    public VecF cross(VecF v) {
        if (dim != v.c.length || dim != 3) {
            try {
                throw new Exception("Cross may be applied only for 3 dimensional vectors");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new VecF(c[1] * v.c[2] - c[2] * v.c[1], c[2] * v.c[0] - c[0] * v.c[2], c[0] * v.c[1] - c[1] * v.c[0]);
    }

    public float norm() {
        float r = 0;
        for (int i = 0; i < dim; i++) {
            r += c[i] * c[i];
        }
        return (float) Math.sqrt(r);
    }

    public VecF normalize() {
        return normalize(1);
    }

    public VecF normalize(int l) {
        VecF v = this.mul(l / norm());
        System.arraycopy(v.c, 0, c, 0, dim);
        return this;
    }

    public static VecF proj(VecF a, int len) {
        float[] f = new float[len];
        Arrays.fill(f, 1);
        System.arraycopy(a.c, 0, f, 0, len);
        return new VecF(f);
    }

    public static VecF embed(VecF a, int len) {
        float[] f = new float[len];
        Arrays.fill(f, 1);
        System.arraycopy(a.c, 0, f, 0, a.c.length);
        return new VecF(f);
    }

    public String toString() {
        String r = "Vec" + dim + " | ";
        for (int i = 0; i < dim; i++) {
            r += i + " : " + c[i] + "; ";
        }
        return r;
    }
}
