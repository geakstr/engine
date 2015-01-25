package main.me.geakstr.engine;

import main.me.geakstr.engine.images.TGAImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Runner {

    private BufferedImage image;

    public Runner() throws Exception {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

        if (true) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame editorFrame = new JFrame("Image Demo");
                    editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                    TGAImage image = null;
                    try {
                        image = new TGAImage("image.tga");
                        image.writeTiFile("output.tga");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    ImageIcon imageIcon = new ImageIcon(image.buildBufferedImage(true));
                    JLabel jLabel = new JLabel();
                    jLabel.setIcon(imageIcon);
                    editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

                    editorFrame.pack();
                    editorFrame.setLocationRelativeTo(null);
                    editorFrame.setVisible(true);
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new Runner();
    }
}
