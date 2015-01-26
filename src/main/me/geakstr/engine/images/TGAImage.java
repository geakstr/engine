package main.me.geakstr.engine.images;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class TGAImage implements IImage {
    public static final int DATA_TYPE_UNCOMPRESSED_RGB = 2;
    public static final int DATA_TYPE_UNCOMPRESSED_BW = 3;
    public static final int DATA_TYPE_COMPRESSED_RGB = 10;
    public static final int DATA_TYPE_COMPRESSED_BW = 11;

    private Header header;
    private int[] pixels;

    private int width, height;
    private int bitsperpixel, datatypecode;
    private boolean hasAlpha;

    public TGAImage(String fileName) throws RuntimeException {
        final ByteBuffer byteBuffer = readFileToByteBuffer(fileName);
        readHeader(byteBuffer);
        this.width = header.width;
        this.height = header.height;
        this.bitsperpixel = header.bitsperpixel;
        this.datatypecode = header.datatypecode;
        readPixels(byteBuffer);
    }

    public TGAImage(int width, int height, int bitsperpixel) throws RuntimeException {
        this.header = new Header(width, height, bitsperpixel);
        this.width = header.width;
        this.height = header.height;
        this.bitsperpixel = header.bitsperpixel;
        this.datatypecode = header.datatypecode;
        this.pixels = new int[width * height];
    }

    public void set(int x, int y, int color) {
        set(pixels, x, y, color);
    }

    public int get(int x, int y) {
        return get(pixels, x, y);
    }

    public void set(int[] pixels, int x, int y, int color) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        pixels[x + y * width] = color;
    }

    public int get(int[] pixels, int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Color.BLACK.val;
        }
        return pixels[x + y * width];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public BufferedImage buildBufferedImage() {
        final int type = hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_BGR;
        final BufferedImage bufferedImage = new BufferedImage(width, height, type);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        return bufferedImage;
    }

    public void writeToFile(String fileName) {
        DataOutputStream out = null;

        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

            int size = this.pixels.length;
            int[] pixels = new int[size];
            System.arraycopy(this.pixels, 0, pixels, 0, size);
            flipVertically(pixels);

            out.write(0);
            out.write(0);
            out.write(2);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            out.write((width & 0x00FF));
            out.write((width & 0xFF00) / 256);
            out.write((height & 0x00FF));
            out.write((height & 0xFF00) / 256);
            out.write(32);
            out.write(0);
            for (int pixel : pixels) {
                Color color = new Color(pixel);
                out.write(color.b);
                out.write(color.g);
                out.write(color.r);
                out.write(color.a);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ByteBuffer readFileToByteBuffer(final String fileName) {
        ByteBuffer byteBuffer = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(new File(fileName));
            FileChannel fileChannel = fileInputStream.getChannel();

            byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            fileChannel.read(byteBuffer);
            byteBuffer.flip();

            fileChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return byteBuffer;
    }

    private void readHeader(final ByteBuffer byteBuffer) throws RuntimeException {
        this.header = new Header();

        header.idlength = byteBuffer.get();
        header.colourmaptype = byteBuffer.get();
        header.datatypecode = byteBuffer.get();
        header.colourmaporigin = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.colourmaplength = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.colourmapdepth = byteBuffer.get();
        header.x_origin = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.y_origin = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.width = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.height = (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8);
        header.bitsperpixel = byteBuffer.get();
        header.imagedescriptor = byteBuffer.get();

        byteBuffer.position(byteBuffer.position() + header.idlength + header.colourmaptype * header.colourmaplength);

        hasAlpha = (header.imagedescriptor & 0x0f) != 0 || header.bitsperpixel == 32;

        if (header.datatypecode != DATA_TYPE_UNCOMPRESSED_RGB &&
                header.datatypecode != DATA_TYPE_COMPRESSED_RGB &&
                header.datatypecode != DATA_TYPE_UNCOMPRESSED_BW &&
                header.datatypecode != DATA_TYPE_COMPRESSED_BW) {
            throw new RuntimeException("This datatypecode not supported : " + header.datatypecode);
        }
        if (header.bitsperpixel != 16 &&
                header.bitsperpixel != 24 &&
                header.bitsperpixel != 32) {
            throw new RuntimeException("Supported only 16, 24 and 32 bit images. This image has " + header.bitsperpixel + " bits per pixel");
        }
        if (header.colourmaptype != 0 &&
                header.colourmaptype != 1) {
            throw new RuntimeException("This colourmaptype not supported : " + header.colourmaptype);
        }

        if (header.width <= 0 || header.height <= 0) {
            throw new RuntimeException("Image must have positive width and height");
        }
    }

    private void readPixels(final ByteBuffer byteBuffer) throws RuntimeException {
        final int n = width * height;
        final int bytes = bitsperpixel / 8;
        final byte[] data = new byte[5];

        this.pixels = new int[n];

        int idx = 0;
        if (datatypecode == DATA_TYPE_UNCOMPRESSED_RGB || datatypecode == DATA_TYPE_UNCOMPRESSED_BW) {
            while (idx < n) {
                byteBuffer.get(data, 0, bytes);
                pixels[idx++] = buildPixel(data, 0);
            }
        } else if (datatypecode == DATA_TYPE_COMPRESSED_RGB || datatypecode == DATA_TYPE_COMPRESSED_BW) {
            while (idx < n) {
                byteBuffer.get(data, 0, bytes + 1);

                final int chuckSize = data[0] & 0x7f;
                pixels[idx++] = buildPixel(data, 1);

                if ((data[0] & 0x80) == 0) {
                    for (int i = 0; i < chuckSize; i++) {
                        byteBuffer.get(data, 0, bytes);
                        pixels[idx++] = buildPixel(data, 0);
                    }
                } else {
                    for (int i = 0; i < chuckSize; i++) {
                        pixels[idx++] = buildPixel(data, 1);
                    }
                }
            }
        }
        if ((header.imagedescriptor & 0x20) == 0) {
            flipVertically(pixels);
        }
        if ((header.imagedescriptor & 0x10) != 0) {
            flipHorizontally(pixels);
        }
    }

    private void flipVertically(int[] pixels) {
        int half = height >> 1;
        int[] line = new int[width];
        for (int y_top = 0, y_bot = height - 1; y_top < half; y_top++, y_bot--) {
            int top_offset = y_top * width;
            int bot_offset = y_bot * width;

            System.arraycopy(pixels, top_offset, line, 0, width);
            System.arraycopy(pixels, bot_offset, pixels, top_offset, width);
            System.arraycopy(line, 0, pixels, bot_offset, width);
        }
    }

    public void flipVertically() {
        flipVertically(pixels);
    }

    private void flipHorizontally(int[] pixels) {
        int half = width >> 1;
        for (int x = 0; x < half; x++) {
            for (int y = 0; y < height; y++) {
                int a = get(x, y);
                int b = get(width - 1 - x, y);
                set(x, y, b);
                set(width - 1 - x, y, a);
            }
        }
    }

    private int buildPixel(final byte[] data, final int offset) {
        int a = 0, r = 0, g = 0, b = 0;
        if (bitsperpixel == 32) {
            a = (data[3 + offset] & 255) << 24;
            r = (data[2 + offset] & 255) << 16;
            g = (data[1 + offset] & 255) << 8;
            b = (data[offset] & 255);
        } else if (bitsperpixel == 24) {
            a = 0xff000000;
            r = (data[2 + offset] & 255) << 16;
            g = (data[1 + offset] & 255) << 8;
            b = (data[offset] & 255);
        } else if (bitsperpixel == 16) {
            if (datatypecode == DATA_TYPE_UNCOMPRESSED_RGB || datatypecode == DATA_TYPE_COMPRESSED_RGB) {
                a = ((data[1 + offset] & 0x80) & 255) << 24;
                r = (((data[1 + offset] & 0x7c) << 1) & 255) << 16;
                g = ((((data[1 + offset] & 0x03) << 6) | ((data[offset] & 0xe0) >> 2)) & 255) << 8;
                b = (((data[offset] & 0x1f) << 3) & 255);
            } else if (datatypecode == DATA_TYPE_UNCOMPRESSED_BW || datatypecode == DATA_TYPE_COMPRESSED_BW) {
                a = (data[1 + offset] & 255) << 24;
                r = (data[offset] & 255) << 16;
                g = (data[offset] & 255) << 8;
                b = (data[offset] & 255);
            }
        }
        return Color.argb(a, r, g, b, false);
    }

    public static class Header {
        public int idlength;
        public int colourmaptype;
        public int datatypecode;
        public int colourmaporigin;
        public int colourmaplength;
        public int colourmapdepth;
        public int x_origin;
        public int y_origin;
        public int width;
        public int height;
        public int bitsperpixel;
        public int imagedescriptor;

        public Header() {}

        public Header(int width, int height, int bitsperpixel) {
            this.idlength = 0;
            this.colourmaptype = 0;
            this.datatypecode = 10;
            this.colourmaporigin = 0;
            this.colourmaplength = 0;
            this.colourmapdepth = 0;
            this.x_origin = 0;
            this.y_origin = 0;
            this.width = width;
            this.height = height;
            this.bitsperpixel = bitsperpixel;
            this.imagedescriptor = 0;
        }

        public String toString() {
            final StringBuilder ret = new StringBuilder();
            ret.append("idlength : ").append(idlength).append("\n");
            ret.append("colourmaptype : ").append(colourmaptype).append("\n");
            ret.append("datatypecode : ").append(datatypecode).append("\n");
            ret.append("colourmaporigin : ").append(colourmaporigin).append("\n");
            ret.append("colourmaplength : ").append(colourmaplength).append("\n");
            ret.append("colourmapdepth : ").append(colourmapdepth).append("\n");
            ret.append("x_origin : ").append(x_origin).append("\n");
            ret.append("y_origin : ").append(y_origin).append("\n");
            ret.append("width : ").append(width).append("\n");
            ret.append("height : ").append(height).append("\n");
            ret.append("bitsperpixel : ").append(bitsperpixel).append("\n");
            ret.append("imagedescriptor : ").append(imagedescriptor).append("\n");
            return ret.toString();
        }
    }
}