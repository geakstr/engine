package main.me.geakstr.engine;

import main.me.geakstr.engine.math.VecF;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.renderer.Renderer;
import main.me.geakstr.engine.shaders.GouraudShader;
import main.me.geakstr.engine.shaders.IShader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class Runner extends Component {
    private VolatileImage volatile_image = null;
    private static BufferedImage buffered_image;

    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    public void paint(Graphics g) {
        if (volatile_image == null) {
            volatile_image = createVolatileImage(WIDTH, HEIGHT);
        }
        do {
            int code = volatile_image.validate(getGraphicsConfiguration());
            if (code != VolatileImage.IMAGE_RESTORED && code == VolatileImage.IMAGE_INCOMPATIBLE) {
                if (volatile_image != null) {
                    volatile_image.flush();
                    volatile_image = null;
                }
                volatile_image = createVolatileImage(WIDTH, HEIGHT);
            }
            volatile_image.getGraphics().drawImage(buffered_image, 0, 0, this);
            g.drawImage(volatile_image, 0, 0, this);
        } while (volatile_image.contentsLost());
    }

    public static void main(String[] args) {
        try {
            Model model = new Model("src/resources/models/african_head");
            IImage image = new TGAImage(WIDTH, HEIGHT, 32);

            float[] zbuffer = new float[WIDTH * HEIGHT];
            for (int i = 0; i < zbuffer.length; i++) {
                zbuffer[i] = Float.MIN_VALUE;
            }

            Viewer viewer = new Viewer(new VecF(1, 1, 1),
                    new VecF(1, 1, 3),
                    new VecF(0, 0, 0),
                    Viewer.viewport(image.width() / 8, image.height() / 8, image.width() * 3 / 4, image.height() * 3 / 4));

            IShader shader = new GouraudShader();
            for (int i = 0; i < model.f_size(); i++) {
                VecF[] screen_coords = new VecF[3];
                for (int j = 0; j < 3; j++) {
                    screen_coords[j] = VecF.embed(shader.vertex(model, viewer, i, j), 4);
                }
                Renderer.triangle(model, viewer, screen_coords, shader, image, zbuffer);
            }

            image.flip_vertically();
            buffered_image = image.build_buffered_image();

            JFrame jFrame = new JFrame("Image Demo");
            jFrame.setSize(WIDTH, HEIGHT);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.getContentPane().add(new Runner());
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

