package main.me.geakstr.engine.images;

public interface IImage {
    void set(int x, int y, int color);
    int get(int x, int y);
    int width();
    int height();
}
