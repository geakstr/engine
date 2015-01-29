package main.me.geakstr.engine.images;

import java.awt.image.BufferedImage;

public interface IImage {
    void set(int x, int y, int color);
    int get(int x, int y);
    int width();
    int height();
    void flip_vertically();
    BufferedImage build_buffered_image();
}
