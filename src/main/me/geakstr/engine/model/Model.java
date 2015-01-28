package main.me.geakstr.engine.model;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.geometry.VecI;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.images.TGAImage;
import main.me.geakstr.engine.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<VecF> v;
    private List<int[]> f;
    private List<VecF> uv;
    private List<VecF> n;

    private IImage diffuse_map;

    public Model(String model_file_name, String texture_file_name) {
        this.v = new ArrayList<>();
        this.f = new ArrayList<>();
        this.uv = new ArrayList<>();
        this.n = new ArrayList<>();
        read_model(model_file_name);
        read_texture(texture_file_name);
    }

    public VecF v(int idx) {
        return v.get(idx);
    }

    public int[] f(int idx) {
        return f.get(idx);
    }

    public VecI uv(int f_i, int v_i) {
        int idx = f.get(f_i)[v_i + 3];
        return new VecI(uv.get(idx).c[0] * diffuse_map.width(), uv.get(idx).c[1] * diffuse_map.height());
    }

    public int diffuse(VecF uv) {
        return diffuse_map.get((int) uv.c[0], (int) uv.c[1]);
    }

    public VecF n(int f_i, int v_i) throws Exception {
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
                v.add(new VecF(x, y, z));
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
                uv.add(new VecF(u, v));
            } else if ("vn".equals(tokens[0])) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                n.add(new VecF(x, y, z));
            }
        }

        reader.close();
    }
}
