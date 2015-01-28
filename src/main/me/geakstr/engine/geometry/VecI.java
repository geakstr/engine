package main.me.geakstr.engine.geometry;

public class VecI {
    // Dimension
    public int dim;

    // Coords
    public int[] c;

    public VecI(int... f_c) {
        this.dim = f_c.length;
        this.c = f_c;
    }

    public VecI(float... d_c) {
        this.dim = d_c.length;
        this.c = new int[this.dim];
        for (int i = 0; i < this.dim; i++) {
            this.c[i] = (int) d_c[i];
        }
    }

    public VecI(double... i_c) {
        this.dim = i_c.length;
        this.c = new int[this.dim];
        for (int i = 0; i < this.dim; i++) {
            this.c[i] = (int) i_c[i];
        }
    }

    public VecI(VecI v) {
        this.dim = v.dim;
        this.c = new int[this.dim];
        System.arraycopy(v.c, 0, this.c, 0, this.dim);
    }

    public VecI(Matrix m) {
        if (m.rows() != 4) {
            try {
                throw new Exception("Possible construct only 3 dimensional vector from 4 rows matrix");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.dim = m.rows() - 1;
        this.c = new int[this.dim];
        for (int i = 0; i < this.dim; i++) {
            c[i] = (int) (m.m()[i][0] / m.m()[3][0]);
        }
    }

    public VecI add(VecI v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] r = new int[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] + v.c[i];
        }
        return new VecI(r);
    }

    public VecI sub(VecI v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] r = new int[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = c[i] - v.c[i];
        }
        return new VecI(r);
    }

    public VecI mul(float f) {
        int[] r = new int[dim];
        for (int i = 0; i < dim; i++) {
            r[i] = (int) (c[i] * f);
        }
        return new VecI(r);
    }

    public float mul(VecI v) {
        if (dim != v.c.length) {
            try {
                throw new Exception("The dimensions of the vectors do not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int r = 0;
        for (int i = 0; i < dim; i++) {
            r += c[i] * v.c[i];
        }
        return r;
    }

    public VecI cross(VecI v) {
        if (dim != v.c.length || dim != 3) {
            try {
                throw new Exception("Cross may be applied only for 3 dimensional vectors");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new VecI(c[1] * v.c[2] - c[2] * v.c[1], c[2] * v.c[0] - c[0] * v.c[2], c[0] * v.c[1] - c[1] * v.c[0]);
    }

    public float norm() {
        int r = 0;
        for (int i = 0; i < dim; i++) {
            r += c[i] * c[i];
        }
        return (float) Math.sqrt(r);
    }

    public VecI normalize() {
        return normalize(1);
    }

    public VecI normalize(int l) {
        VecI v = this.mul(l / norm());
        System.arraycopy(v.c, 0, c, 0, dim);
        return this;
    }

    public String toString() {
        String r = "Vec" + dim + " | ";
        for (int i = 0; i < dim; i++) {
            r += i + " : " + c[i] + "; ";
        }
        return r;
    }
}
