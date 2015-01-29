package main.me.geakstr.engine.renderer;

import static main.me.geakstr.engine.utils.Swapper.swap;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;
import main.me.geakstr.engine.math.Vec2f;
import main.me.geakstr.engine.math.Vec2i;
import main.me.geakstr.engine.math.Vec3f;
import main.me.geakstr.engine.math.Vec3i;
import main.me.geakstr.engine.math.Vec4f;
import main.me.geakstr.engine.model.Model;
import main.me.geakstr.engine.shaders.IShader;

public class Renderer {
	
	public static Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {
	    Vec3f[] s = new Vec3f[2];
	    
	    s[0].x = C.x - A.x;
        s[0].y = B.x - A.x;
        s[0].z = A.x - P.x;
        
        s[1].x = C.y - A.y;
        s[1].y = B.y - A.y;
        s[1].z = A.y - P.y;
        
        
	    Vec3f u = s[0].cross(s[1]);
	    
	    if (Math.abs(u.z) > 0.01) {
	        return new Vec3f(1.f - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z);
	    }
	    return new Vec3f(-1, 1, 1);
	}
	
	public static void triangle(Vec4f[] pts, IShader shader, IImage image, IImage zbuffer) {
	    Vec2f bboxmin = new Vec2f(Float.MAX_VALUE,  Float.MAX_VALUE);
	    Vec2f bboxmax = new Vec2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
	    
	    for (int i = 0; i < 3; i++) {	        
	        bboxmin.x = Math.min(bboxmin.x, pts[i].x / pts[i].w);
            bboxmax.x = Math.min(bboxmax.x, pts[i].x / pts[i].w);
            
            bboxmin.y = Math.min(bboxmin.y, pts[i].y / pts[i].w);
            bboxmax.y = Math.min(bboxmax.y, pts[i].y / pts[i].w);
	    }
	    Vec2i P;
	    Color color;
	    for (P.x = (int) bboxmin.x; P.x <= bboxmax.x; P.x++) {
	        for (P.y = (int) bboxmin.y; P.y <= bboxmax.y; P.y++) {
	            Vec3f c = barycentric(proj<2>(pts[0]/pts[0][3]), proj<2>(pts[1]/pts[1][3]), proj<2>(pts[2]/pts[2][3]), proj<2>(P));
	            float z = pts[0][2]*c.x + pts[1][2]*c.y + pts[2][2]*c.z;
	            float w = pts[0][3]*c.x + pts[1][3]*c.y + pts[2][3]*c.z;
	            int frag_depth = std::max(0, std::min(255, int(z/w+.5)));
	            if (c.x<0 || c.y<0 || c.z<0 || zbuffer.get(P.x, P.y)[0]>frag_depth) continue;
	            bool discard = shader.fragment(c, color);
	            if (!discard) {
	                zbuffer.set(P.x, P.y, TGAColor(frag_depth));
	                image.set(P.x, P.y, color);
	            }
	        }
	    }
	}
	
    public static void triangle(Vec3i t0, Vec3i t1, Vec3i t2,
                                Vec2i ut0, Vec2i ut1, Vec2i ut2,
                                IImage image, Model model,
                                float intensity, int[] zbuffer) {
        if (t0.y == t1.y && t0.y == t2.y) {
            return;
        }
        if (t0.y > t1.y) {
            t1 = swap(t0, t0 = t1);
            ut1 = swap(ut0, ut0 = ut1);
        }
        if (t0.y > t2.y) {
            t2 = swap(t0, t0 = t2);
            ut2 = swap(ut0, ut0 = ut2);
        }
        if (t1.y > t2.y) {
            t2 = swap(t1, t1 = t2);
            ut2 = swap(ut1, ut1 = ut2);
        }
        int total_height = t2.y - t0.y;
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.y - t0.y || t1.y == t0.y;
            int segment_height = second_half ? t2.y - t1.y : t1.y - t0.y;
            float alpha = (float) i / (float) total_height;
            float beta = (float) (i - (second_half ? t1.y - t0.y : 0)) / segment_height;
            Vec3i A = t0.add(t2.sub(t0).mul(alpha));
            Vec3i B = second_half ? t1.add(t2.sub(t1).mul(beta)) : t0.add(t1.sub(t0).mul(beta));
            Vec2i uvA = ut0.add(ut2.sub(ut0).mul(alpha));
            Vec2i uvB = second_half ? ut1.add(ut2.sub(ut1).mul(beta)) : ut0.add(ut1.sub(ut0).mul(beta));
            if (A.x > B.x) {
                B = swap(A, A = B);
                uvB = swap(uvA, uvA = uvB);
            }
            for (int j = A.x; j <= B.x; j++) {
                float phi = (float) (B.x == A.x ? 1. : (float) (j - A.x) / (float) (B.x - A.x));
                Vec3i P = A.add(B.sub(A).mul(phi));
                Vec2i uvP = uvA.add(uvB.sub(uvA).mul(phi));
                P.x = j;
                P.y = t0.y + i;
                int idx = j + (t0.y + i) * image.width();
                if (P.x >= image.width() || P.y >= image.height() || P.x < 0 || P.y < 0) {
                    continue;
                }
                if (idx >= 0 && idx < zbuffer.length && zbuffer[idx] < P.z) {
                    zbuffer[idx] = P.z;
                    Color color = new Color(model.diffuse(uvP));
                    image.set(P.x, P.y, color.val);
                }
            }
        }
    }
}
