package main.me.geakstr.engine;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.me.geakstr.engine.geometry.*;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.renderer.Renderer;
import main.me.geakstr.engine.shaders.GouraudShader;
import main.me.geakstr.engine.shaders.IShader;

public class Runner extends Component {

	VolatileImage backBuffer = null;
	static BufferedImage img;

	public void paint(Graphics g) {
		long startTime = System.currentTimeMillis();
		int w = getWidth();
		int h = getHeight();
		if (backBuffer == null) {
			backBuffer = createVolatileImage(w, h);
		}
		do {
			int valCode = backBuffer.validate(getGraphicsConfiguration());
			if (valCode != VolatileImage.IMAGE_RESTORED
					&& valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				if (backBuffer != null) {
					backBuffer.flush();
					backBuffer = null;
				}
				backBuffer = createVolatileImage(w, h);
			}
			backBuffer.getGraphics().drawImage(img, 0, 0, this);
			g.drawImage(backBuffer, 0, 0, this);
		} while (backBuffer.contentsLost());
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
	}

	public static void main(String[] args) throws Exception {
		IImage image = null;
	    try {
	        Model model = new Model("src/resources/model/african_head.obj", "src/resources/tga/african_head_diffuse.tga");
	        image = new TGAImage(600, 600, 32);
	        IImage zbuffer = new TGAImage(600, 600, 8);
	        Viewer viewer = new Viewer(new VecF(1, 1, 1), new VecF(0, 0.5, 3), new VecF(0, 0, 0), Viewer.viewport(image.width() / 8, image.height() / 8, image.width() * 3 / 4, image.height() * 3 / 4));
	
	        IShader shader = new GouraudShader();
	        for (int i = 0; i < model.f_size(); i++) {
	           VecF[] screen_coords = new VecF[3];
	           for (int j = 0; j < 3; j++) {
	             screen_coords[j] = VecF.embed(shader.vertex(model, viewer, i, j), 4);
	           }
	           Renderer.triangle(screen_coords, shader, image, zbuffer);
	        }
	
	        image.flip_vertically();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
		img = image.build_buffered_image();

		JFrame editorFrame = new JFrame("Image Demo");
		editorFrame.setSize(600, 600);
		editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		editorFrame.getContentPane().add(new Runner());
		editorFrame.setLocationRelativeTo(null);
		editorFrame.setVisible(true);

	}
}