package main.me.geakstr.engine.model;

import main.me.geakstr.engine.geometry.Vec3;
import main.me.geakstr.engine.utils.FileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Model {
    private List<Vec3> verts;
    private List<List<Integer>> faces;

    public Model(String fileName) {
        this.verts = new ArrayList<>();
        this.faces = new ArrayList<>();
        readFile(fileName);
    }

    public Vec3 vert(int idx) {
        return verts.get(idx);
    }

    public List<Integer> face(int idx) {
        return faces.get(idx);
    }

    public int vertsSize() {
        return verts.size();
    }

    public int facesSize() {
        return faces.size();
    }

    private void readFile(String fileName) {
        FileUtil.Reader reader = new FileUtil.Reader(fileName);

        while (reader.ready()) {
            String line = reader.line().trim();

            if (line.startsWith("v ")) {
                String[] tokens = line.split(" ");
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                verts.add(new Vec3(x, y, z));
            } else if (line.startsWith("f ")) {
                String[] tokens = line.split(" ");
                int v1 = Integer.parseInt(tokens[1].split("/")[0]) - 1;
                int v2 = Integer.parseInt(tokens[2].split("/")[0]) - 1;
                int v3 = Integer.parseInt(tokens[3].split("/")[0]) - 1;
                faces.add(Arrays.asList(v1, v2, v3));
            }
        }

        reader.close();
    }
}
