package main.me.geakstr.engine.renderer;


import java.util.ArrayList;
import java.util.List;

import main.me.geakstr.engine.geometry.VecF;
import main.me.geakstr.engine.geometry.VecI;
import main.me.geakstr.engine.images.Color;
import main.me.geakstr.engine.images.IImage;

import main.me.geakstr.engine.shaders.IShader;

public class Renderer {
	public static VecF barycentric(VecF A, VecF B, VecF C, VecI P) {
	    VecF[] s = new VecF[2];
	    for (int i = 0; i < 2; i++) {
	    	s[i] = new VecF(3);
	    }
	    
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
	
	public static void triangle(VecF[] pts, IShader shader, IImage image, IImage zbuffer) {
	    VecF bboxmin = new VecF(Float.MAX_VALUE,  Float.MAX_VALUE);
	    VecF bboxmax = new VecF(-Float.MAX_VALUE, -Float.MAX_VALUE);
	    
	    for (int i = 0; i < 3; i++) {	        
	        bboxmin.x(Math.min(bboxmin.x(), pts[i].x() / pts[i].w()));
            bboxmax.x(Math.max(bboxmax.x(), pts[i].x() / pts[i].w()));
            
            bboxmin.y(Math.min(bboxmin.y(), pts[i].y() / pts[i].w()));
            bboxmax.y(Math.max(bboxmax.y(), pts[i].y() / pts[i].w()));
	    }
	    VecI P = new VecI(2);
	    List<Color> color = new ArrayList<>();
	    color.add(Color.WHITE);
	    
	    for (P.x((int) bboxmin.x()); P.x() <= bboxmax.x(); P.x(P.x() + 1)) {
	    	for (P.y((int) bboxmin.y()); P.y() <= bboxmax.y(); P.y(P.y() + 1)) {
	            VecF c = barycentric(VecF.proj(pts[0].div(pts[0].w()), 2), VecF.proj(pts[1].div(pts[1].w()), 2), VecF.proj(pts[2].div(pts[2].w()), 2), VecI.proj(P, 2));
	            float z = pts[0].z() * c.x() + pts[1].z() * c.y() + pts[2].z() * c.z();
	            float w = pts[0].w() * c.x() + pts[1].w() * c.y() + pts[2].w() * c.z();
	            int frag_depth = (int) Math.max(0, Math.min(255, z / w + 0.5)); 
	            if (c.x() < 0 || c.y() < 0 || c.z() < 0 || zbuffer.get(P.x(), P.y()) > frag_depth) {
	            	continue;
	            }
	            boolean discard = shader.fragment(c, color);
	            if (!discard) {
	                zbuffer.set(P.x(), P.y(), new Color(frag_depth).val);
	                image.set(P.x(), P.y(), color.get(0).val);
	            }
	        }
	    }
	}
}
