package main.me.geakstr.engine.model;

import main.me.geakstr.engine.geometry.Vec2f;
import main.me.geakstr.engine.geometry.Vec3f;
import main.me.geakstr.engine.utils.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private List<Vec3f> v;
    private List<int[]> f;
    private List<Vec2f> uv;

    public Model(String fileName) {
        this.v = new ArrayList<>();
        this.f = new ArrayList<>();
        this.uv = new ArrayList<>();
        readFile(fileName);
    }

    public Vec3f v(int idx) {
        return v.get(idx);
    }

    public int[] f(int idx) {
        return f.get(idx);
    }

    public int vertsSize() {
        return v.size();
    }

    public int facesSize() {
        return f.size();
    }

    private void readFile(String fileName) {
        FileUtil.Reader reader = new FileUtil.Reader(fileName);

        while (reader.ready()) {
            String[] tokens = reader.tokens();

            if ("v".equals(tokens[0])) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                v.add(new Vec3f(x, y, z));
            } else if ("f".equals(tokens[0])) {
            	String[] t1 = tokens[1].split("/");
            	String[] t2 = tokens[2].split("/");
            	String[] t3 = tokens[3].split("/");
            	
            	
                int v1 = Integer.parseInt(t1[0]) - 1;
                int v2 = Integer.parseInt(t2[0]) - 1;
                int v3 = Integer.parseInt(t3[0]) - 1;
                
                int u1 = Integer.parseInt(t1[1]) - 1;
                int u2 = Integer.parseInt(t2[1]) - 1;
                int u3 = Integer.parseInt(t3[1]) - 1;
                
                f.add(new int[] { v1, v2, v3, u1, u2, u3 });
            } else if ("vt".equals(tokens[0])) {
            	float u = Float.parseFloat(tokens[1]);
                float v = Float.parseFloat(tokens[2]);
                uv.add(new Vec2f(u, v));
            }
        }

        reader.close();
    }
}
