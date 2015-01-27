package main.me.geakstr.engine.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileUtil {

    public static class Reader {
        private BufferedReader in;
        private StringTokenizer st;

        public Reader(String fileName) {
            try {
                this.in = new BufferedReader(new FileReader(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean ready() {
            boolean ready = false;
            try {
                ready = in.ready();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ready;
        }

        public String ns() {
            try {
                while (st == null || !st.hasMoreTokens()) {
                    st = new StringTokenizer(in.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return st.nextToken();
        }

        public int ni() {
            return Integer.parseInt(ns());
        }

        public long nl() {
            return Long.parseLong(ns());
        }

        public double nd() {
            return Double.parseDouble(ns());
        }

        public float nf() {
            return Float.parseFloat(ns());
        }

        public String line() {
            String line = "";
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
        }
        
        public String[] tokens() {
        	return line().trim().split("\\s+");
        }

        public void close() {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
