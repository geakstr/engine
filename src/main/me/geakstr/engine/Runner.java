package main.me.geakstr.engine;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.me.geakstr.engine.geometry.Vec2i;
import main.me.geakstr.engine.geometry.Vec3f;
import main.me.geakstr.engine.geometry.Vec3i;
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

                Vec3f light_dir = new Vec3f(0, 0, -1);

                for (int i = 0; i < model.f_size(); i++) {
                    int[] f = model.f(i);
                    Vec3i screen_coords[] = new Vec3i[3];
                    Vec3f world_coords[] = new Vec3f[3];
                    for (int j = 0; j < 3; j++) {
                        Vec3f v = model.v(f[j]);
                        screen_coords[j] = new Vec3i((v.x + 1.) * image.width() / 2., (v.y + 1.) * image.height() / 2., (v.z + 1.) * 255 / 2.);
                        world_coords[j] = v;
                    }
                    Vec3f n = (world_coords[2].sub(world_coords[0])).cross(world_coords[1].sub(world_coords[0])).normalize();
                    float intensity = n.mul(light_dir);
                    if (intensity > 0) {
                        Vec2i[] uv = new Vec2i[3];
                        for (int k = 0; k < 3; k++) {
                            uv[k] = model.uv(i, k);
                        }
                        Renderer.triangle(screen_coords[0],
                                screen_coords[1],
                                screen_coords[2],
                                uv[0],
                                uv[1],
                                uv[2],
                                image,
                                model,
                                intensity,
                                zbuffer);
                    }
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
