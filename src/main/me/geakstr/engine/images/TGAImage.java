package main.me.geakstr.engine.images;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TGAImage {
    private Header header;
    private int[] pixels;

    public TGAImage(String fileName) {
        final ByteBuffer byteBuffer = readFileToByteBuffer(fileName);
        this.header = readHeader(byteBuffer);
        this.pixels = readPixels(header, byteBuffer);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void set(int x, int y, int color) {
        pixels[x + y * header.width] = color;
    }

    public int get(int x, int y) {
        return pixels[x + y * header.width];
    }

    public BufferedImage buildBufferedImage(final boolean flipVertically) {
        BufferedImage bufferedImage = buildBufferedImage();

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -bufferedImage.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferedImage = op.filter(bufferedImage, null);

        return bufferedImage;
    }

    public BufferedImage buildBufferedImage() {
        final BufferedImage bufferedImage = new BufferedImage(header.width, header.height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, header.width, header.height, pixels, 0, header.width);
        return bufferedImage;
    }

    public void writeToFile(String fileName) {
        DataOutputStream out = null;

        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

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
            out.write((header.width & 0x00FF));
            out.write((header.width & 0xFF00) / 256);
            out.write((header.height & 0x00FF));
            out.write((header.height & 0xFF00) / 256);
            out.write(32);
            out.write(0);
            for (int pixel : pixels) {
                out.write(pixel & 255);
                out.write((pixel >> 8) & 255);
                out.write((pixel >> 16) & 255);
                out.write((pixel >> 24) & 255);
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

    private Header readHeader(final ByteBuffer byteBuffer) {
        final Header header = new Header();

        header.idlength = byteBuffer.get();
        header.colourmaptype = byteBuffer.get();
        header.datatypecode = byteBuffer.get();
        header.colourmaporigin = byteBuffer.get() + (byteBuffer.get() << 8);
        header.colourmaplength = byteBuffer.get() + (byteBuffer.get() << 8);
        header.colourmapdepth = byteBuffer.get();
        header.x_origin = byteBuffer.get() + (byteBuffer.get() << 8);
        header.y_origin = byteBuffer.get() + (byteBuffer.get() << 8);
        header.width = byteBuffer.get() + (byteBuffer.get() << 8);
        header.height = byteBuffer.get() + (byteBuffer.get() << 8);
        header.bitsperpixel = byteBuffer.get();
        header.imagedescriptor = byteBuffer.get();

        assert header.datatypecode == 2 || header.datatypecode == 10;
        assert header.bitsperpixel == 16 || header.bitsperpixel == 24 || header.bitsperpixel == 32;
        assert header.colourmaptype == 0 || header.colourmaptype == 1;

        return header;
    }

    private int[] readPixels(final Header header, final ByteBuffer byteBuffer) {
        final int n = header.width * header.height;

        assert n > 0;

        final int[] pixels = new int[n];

        final int skip = byteBuffer.position() + header.idlength + header.colourmaptype * header.colourmaplength;
        byteBuffer.position(skip);

        final byte[] data = new byte[5];
        final int bytes = header.bitsperpixel / 8;

        assert bytes >= 2;

        int idx = 0;
        if (header.datatypecode == 2) {
            while (idx < n) {
                byteBuffer.get(data, 0, bytes);
                pixels[idx++] = buildPixel(data, bytes, 0);
            }
        } else if (header.datatypecode == 10) {
            while (idx < n) {
                byteBuffer.get(data, 0, bytes + 1);

                final int chuckSize = data[0] & 0x7f;
                pixels[idx++] = buildPixel(data, bytes, 1);

                if ((data[0] & 0x80) == 0) {
                    for (int i = 0; i < chuckSize; i++) {
                        byteBuffer.get(data, 0, bytes);
                        pixels[idx++] = buildPixel(data, bytes, 0);
                    }
                } else {
                    // RLE
                    for (int i = 0; i < chuckSize; i++) {
                        pixels[idx++] = buildPixel(data, bytes, 1);
                    }
                }
            }
        }

        return pixels;
    }

    private int buildPixel(final byte[] data, final int bytes, final int offset) {
        int argb = 0;
        if (bytes == 4) {
            argb |= (data[3 + offset] & 255) << 24;
            argb |= (data[2 + offset] & 255) << 16;
            argb |= (data[1 + offset] & 255) << 8;
            argb |= (data[offset] & 255);
        } else if (bytes == 3) {
            argb |= 0xff000000;
            argb |= (data[2 + offset] & 255) << 16;
            argb |= (data[1 + offset] & 255) << 8;
            argb |= (data[0] & 255);
        } else if (bytes == 2) {
            argb |= (((data[1 + offset] & 0x80)) & 255) << 24;
            argb |= (((data[1 + offset] & 0x7c) << 1) & 255) << 16;
            argb |= ((((data[1 + offset] & 0x03) << 6) | ((data[offset] & 0xe0) >> 2)) & 255) << 8;
            argb |= (((data[offset] & 0x1f) << 3) & 255);
        }
        return argb;
    }

    public static class Color {
        public int r;
        public int g;
        public int b;
        public int a;
        public int val;

        public Color(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public Color(int color) {

        }

        public static Color RGBAToColor(int r, int g, int b, int a) {
            int argb = 0;
            argb |= a;
            argb |= r;
            argb |= g;
            argb |= b;
            return new Color(argb);
        }
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