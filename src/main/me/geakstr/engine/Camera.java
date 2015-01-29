package main.me.geakstr.engine;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.math.Matrix;

public class Camera {
	private VecF light_dir;
    private VecF eye;
    private VecF center;

    private Matrix modelview;
    private Matrix projection;
    private Matrix viewport;
}
