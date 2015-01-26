package main.me.geakstr.engine;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.me.geakstr.engine.geometry.Vec3;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.renderer.Renderer;

public class Runner {
    public Runner() throws Exception {
        if (true) {
            JFrame editorFrame = new JFrame("Image Demo");
            editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            TGAImage image = null;
            try {
                Model model = new Model("src/resources/model/african_head.obj");

                image = new TGAImage(640, 640, 32);

                for (int i = 0; i < model.facesSize(); i++) {
                    List<Integer> face = model.face(i);
                    for (int j = 0; j < 3; j++) {
                        Vec3 v0 = model.vert(face.get(j));
                        Vec3 v1 = model.vert(face.get((j + 1) % 3));
                        int x0 = (int) ((v0.x + 1.) * image.width() / 2.);
                        int y0 = (int) ((v0.y + 1.) * image.height() / 2.);
                        int x1 = (int) ((v1.x + 1.) * image.width() / 2.);
                        int y1 = (int) ((v1.y + 1.) * image.height() / 2.);
                        Renderer.line(x0, y0, x1, y1, image, Color.WHITE);
                    }
                }
                image.flipVertically();

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            ImageIcon imageIcon = new ImageIcon(image.buildBufferedImage());
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
