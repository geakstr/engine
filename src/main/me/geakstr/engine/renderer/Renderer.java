package main.me.geakstr.engine.renderer;

import static main.me.geakstr.engine.utils.Swapper.swap;
import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.geometry.VecI;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.model.Model;

public class Renderer {
	public static VecF barycentric(VecF A, VecF B, VecF C, VecF P) {
	    VecF[] s = new VecF[2];
	    
	    s[0].x(C.x() - A.x());
        s[0].y(B.x() - A.x());
        s[0].z(A.x() - P.x());
        
        s[1].x(C.y() - A.y());
        s[1].y(B.y() - A.y());
        s[1].z(A.y() - P.y());
        
        
	    VecF u = s[0].cross(s[1]);
	    
	    if (Math.abs(u.z()) > 0.01) {
	        return new VecF(1.f - (u.x() + u.y()) / u.z(), u.y() / u.z(), u.x() / u.z());
	    }
	    return new VecF(-1, 1, 1);
	}
	
//	public static void triangle(VecF[] pts, IImage image, IImage zbuffer) {
//	    VecF bboxmin = new Vec2f(Float.MAX_VALUE,  Float.MAX_VALUE);
//	    Vec2f bboxmax = new Vec2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
//	    
//	    for (int i = 0; i < 3; i++) {	        
//	        bboxmin.x = Math.min(bboxmin.x, pts[i].x / pts[i].w);
//            bboxmax.x = Math.min(bboxmax.x, pts[i].x / pts[i].w);
//            
//            bboxmin.y = Math.min(bboxmin.y, pts[i].y / pts[i].w);
//            bboxmax.y = Math.min(bboxmax.y, pts[i].y / pts[i].w);
//	    }
//	    Vec2i P;
//	    Color color;
//	    for (P.x = (int) bboxmin.x; P.x <= bboxmax.x; P.x++) {
//	        for (P.y = (int) bboxmin.y; P.y <= bboxmax.y; P.y++) {
//	            Vec3f c = barycentric(proj<2>(pts[0]/pts[0][3]), proj<2>(pts[1]/pts[1][3]), proj<2>(pts[2]/pts[2][3]), proj<2>(P));
//	            float z = pts[0][2]*c.x + pts[1][2]*c.y + pts[2][2]*c.z;
//	            float w = pts[0][3]*c.x + pts[1][3]*c.y + pts[2][3]*c.z;
//	            int frag_depth = std::max(0, std::min(255, int(z/w+.5)));
//	            if (c.x<0 || c.y<0 || c.z<0 || zbuffer.get(P.x, P.y)[0]>frag_depth) continue;
//	            bool discard = shader.fragment(c, color);
//	            if (!discard) {
//	                zbuffer.set(P.x, P.y, TGAColor(frag_depth));
//	                image.set(P.x, P.y, color);
//	            }
//	        }
//	    }
//	}
	
    public static void triangle(VecI t0, VecI t1, VecI t2,
                                VecI ut0, VecI ut1, VecI ut2,
                                IImage image, Model model,
                                float intensity, int[] zbuffer) {
        if (t0.c[1] == t1.c[1] && t0.c[1] == t2.c[1]) {
            return;
        }
        if (t0.c[1] > t1.c[1]) {
            t1 = swap(t0, t0 = t1);
            ut1 = swap(ut0, ut0 = ut1);
        }
        if (t0.c[1] > t2.c[1]) {
            t2 = swap(t0, t0 = t2);
            ut2 = swap(ut0, ut0 = ut2);
        }
        if (t1.c[1] > t2.c[1]) {
            t2 = swap(t1, t1 = t2);
            ut2 = swap(ut1, ut1 = ut2);
        }
        int total_height = t2.c[1] - t0.c[1];
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.c[1] - t0.c[1] || t1.c[1] == t0.c[1];
            int segment_height = second_half ? t2.c[1] - t1.c[1] : t1.c[1] - t0.c[1];
            float alpha = (float) i / (float) total_height;
            float beta = (float) (i - (second_half ? t1.c[1] - t0.c[1] : 0)) / segment_height;
            VecI A = t0.add(t2.sub(t0).mul(alpha));
            VecI B = second_half ? t1.add(t2.sub(t1).mul(beta)) : t0.add(t1.sub(t0).mul(beta));
            VecI uvA = ut0.add(ut2.sub(ut0).mul(alpha));
            VecI uvB = second_half ? ut1.add(ut2.sub(ut1).mul(beta)) : ut0.add(ut1.sub(ut0).mul(beta));
            if (A.c[0] > B.c[0]) {
                B = swap(A, A = B);
                uvB = swap(uvA, uvA = uvB);
            }
            for (int j = A.c[0]; j <= B.c[0]; j++) {
                float phi = (float) (B.c[0] == A.c[0] ? 1. : (float) (j - A.c[0]) / (float) (B.c[0] - A.c[0]));
                VecI P = A.add(B.sub(A).mul(phi));
                VecI uvP = uvA.add(uvB.sub(uvA).mul(phi));
                P.c[0] = j;
                P.c[1] = t0.c[1] + i;
                int idx = j + (t0.c[1] + i) * image.width();
                if (P.c[0] >= image.width() || P.c[1] >= image.height() || P.c[0] < 0 || P.c[1] < 0) {
                    continue;
                }
                if (idx >= 0 && idx < zbuffer.length && zbuffer[idx] < P.c[2]) {
                    zbuffer[idx] = P.c[2];
                    Color color = new Color(model.diffuse(uvP));
                    image.set(P.c[0], P.c[1], color.val);
                }
            }
        }
    }
}
