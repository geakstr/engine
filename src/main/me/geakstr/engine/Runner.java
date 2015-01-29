package main.me.geakstr.engine;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.me.geakstr.engine.geometry.*;
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
                Model model = new Model("../src/resources/model/african_head.obj", "../src/resources/tga/african_head_diffuse.tga");

                image = new TGAImage(600, 600, 32);

                int[] zbuffer = new int[image.width() * image.height()];
                for (int i = 0; i < zbuffer.length; i++) {
                    zbuffer[i] = Integer.MIN_VALUE;
                }

                Viewer viewer = new Viewer(new VecF(1, -3, 1), new VecF(0, 0, 3), new VecF(0, 0, 0), Viewer.viewport(image.width() / 8, image.height() / 8, image.width() * 3 / 4, image.height() * 3 / 4));


                Matrix modelview = Viewer.lookat(viewer.eye(), viewer.center(), new VecF(0, 1, 0));
                Matrix projection = Matrix.identity(4);
                Matrix viewport = viewer.viewport(image.width() / 8, image.height() / 8, image.width() * 3 / 4, image.height() * 3 / 4);
                projection.m()[3][2] = -1.f / (viewer.eye().sub(viewer.center())).norm();

                for (int i = 0; i < model.f_size(); i++) {
                    int[] f = model.f(i);
                    VecI screen_coords[] = new VecI[3];
                    VecF world_coords[] = new VecF[3];
                    VecI ut[] = new VecI[3];
                    for (int j = 0; j < 3; j++) {
                        VecF v = model.v(f[j]);
                        screen_coords[j] = new VecI(viewport.mul(projection.mul(modelview.mul(v))));
                        ut[j] = model.vt(i, j);
                        world_coords[j] = v;
                    }
                    VecF n = (world_coords[2].sub(world_coords[0])).cross(world_coords[1].sub(world_coords[0])).normalize();
                    float intensity = n.mul(viewer.light_dir());
                    Renderer.triangle(screen_coords[0],
                            screen_coords[1],
                            screen_coords[2],
                            ut[0],
                            ut[1],
                            ut[2],
                            image,
                            model,
                            intensity,
                            zbuffer);

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
