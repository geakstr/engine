package main.me.geakstr.engine;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.renderer.Renderer;
import main.me.geakstr.engine.shaders.GouraudShader;
import main.me.geakstr.engine.shaders.IShader;

public class Runner {
    public Runner() throws Exception {
        if (true) {
            JFrame editorFrame = new JFrame("Image Demo");
            editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            IImage image = null;
            try {
                Model model = new Model("src/resources/model/african_head.obj", "src/resources/tga/african_head_diffuse.tga");

                image = new TGAImage(600, 600, 32);


                float[] zbuffer = new float[image.width() * image.height()];
                for (int i = 0; i < zbuffer.length; i++) {
                    zbuffer[i] = Float.MIN_VALUE;
                }

                Viewer viewer = new Viewer(new VecF(1, 1, 1), new VecF(1, 1, 3), new VecF(0, 0, 0), Viewer.viewport(image.width() / 8, image.height() / 8, image.width() * 3 / 4, image.height() * 3 / 4));

                IShader shader = new GouraudShader();
                for (int i = 0; i < model.f_size(); i++) {
                   VecF[] screen_coords = new VecF[3];
                   for (int j = 0; j < 3; j++) {
                	   screen_coords[j] = VecF.embed(shader.vertex(model, viewer, i, j), 4);
                   }
                   Renderer.triangle(model, viewer, screen_coords, shader, image, zbuffer);
                }

                image.flip_vertically();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            ImageIcon imageIcon = new ImageIcon(image.build_buffered_image());
            JLabel jLabel = new JLabel();
            jLabel.setIcon(imageIcon);
            editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

            editorFrame.pack();
            editorFrame.setLocationRelativeTo(null);
            editorFrame.setVisible(true);
        }
    }

    public static void main(String[] args) throws Exception {
        new Runner();
    }
}
