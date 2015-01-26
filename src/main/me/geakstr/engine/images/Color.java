package main.me.geakstr.engine.images;

public class Color {
    public int a;
    public int r;
    public int g;
    public int b;
    public int val;

    public static final Color BLACK = new Color();
    public static final Color WHITE = new Color(255, 255, 255, 255);

    public Color() {
        this(0, 0, 0, 0);
    }

    public Color(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.val = argb(a, r, g, b);
    }

    public Color(int val) {
        this.a = ((val >> 24) & 255);
        this.r = ((val >> 16) & 255);
        this.g = ((val >> 8) & 255);
        this.b = (val & 255);
        this.val = val;
    }

    public Color(Color color) {
        this.a = color.a;
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.val = color.val;
    }

    public static int argb(int a, int r, int g, int b, boolean shift) {
        int argb = 0;
        if (shift) {
            argb |= (a & 255) << 24;
            argb |= (r & 255) << 16;
            argb |= (g & 255) << 8;
            argb |= (b & 255);
        } else {
            argb |= a;
            argb |= r;
            argb |= g;
            argb |= b;
        }
        return argb;
    }

    public static int argb(int a, int r, int g, int b) {
        return argb(a, r, g, b, true);
    }

    public static int rgba(int r, int g, int b, int a) {
        return rgba(r, g, b, a, true);
    }

    public static int rgba(int r, int g, int b, int a, boolean shift) {
        return argb(a, r, g, b, shift);
    }

    public static int rgb(int r, int g, int b) {
        return rgb(r, g, b, true);
    }

    public static int rgb(int r, int g, int b, boolean shift) {
        return rgba(r, g, b, 0xff000000, shift);
    }
}
