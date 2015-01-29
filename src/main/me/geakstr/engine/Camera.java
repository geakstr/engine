package main.me.geakstr.engine;

import main.me.geakstr.engine.math.Matrix;
import main.me.geakstr.engine.math.Vec3f;

public class Camera {
	private Vec3f light_dir;
    private Vec3f eye;
    private Vec3f center;

    private Matrix modelview;
    private Matrix projection;
    private Matrix viewport;
}
