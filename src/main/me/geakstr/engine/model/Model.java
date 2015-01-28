package main.me.geakstr.engine.model;

import main.me.geakstr.engine.geometry.Vec2f;
import main.me.geakstr.engine.geometry.Vec2i;
import main.me.geakstr.engine.geometry.Vec3f;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Vec3f> v;
    private List<int[]> f;
    private List<Vec2f> uv;
    private List<Vec3f> n;


    private IImage diffuse_map;

    public Model(String model_file_name, String texture_file_name) {
        this.v = new ArrayList<>();
        this.f = new ArrayList<>();
        this.uv = new ArrayList<>();
        this.n = new ArrayList<>();
        read_model(model_file_name);
        read_texture(texture_file_name);
    }

    public Vec3f v(int idx) {
        return v.get(idx);
    }

    public int[] f(int idx) {
        return f.get(idx);
    }

    public Vec2i uv(int f_i, int v_i) {
        int idx = f.get(f_i)[v_i + 3];
        return new Vec2i(uv.get(idx).x * diffuse_map.width(), uv.get(idx).y * diffuse_map.height());
    }

    public int diffuse(Vec2i uv) {
        return diffuse_map.get(uv.x, uv.y);
    }
    
    public Vec3f n(int f_i, int v_i) {
        int idx = f.get(f_i)[v_i + 6];
        return n.get(idx).normalize();
    }

    public int v_size() {
        return v.size();
    }

    public int f_size() {
        return f.size();
    }

    private void read_texture(String file_name) {
        diffuse_map = new TGAImage(file_name);
        diffuse_map.flip_vertically();
    }

    private void read_model(String file_name) {
        FileUtil.Reader reader = new FileUtil.Reader(file_name);

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
                
                int n1 = Integer.parseInt(t1[2]) - 1;
                int n2 = Integer.parseInt(t2[2]) - 1;
                int n3 = Integer.parseInt(t3[2]) - 1;

                f.add(new int[]{v1, v2, v3, u1, u2, u3, n1, n2, n3});
            } else if ("vt".equals(tokens[0])) {
                float u = Float.parseFloat(tokens[1]);
                float v = Float.parseFloat(tokens[2]);
                uv.add(new Vec2f(u, v));
            } else if ("vn".equals(tokens[0])) {
            	float x = Float.parseFloat(tokens[1]);
            	float y = Float.parseFloat(tokens[2]);
            	float z = Float.parseFloat(tokens[3]);
            	n.add(new Vec3f(x, y, z));
            }
        }

        reader.close();
    }
}
