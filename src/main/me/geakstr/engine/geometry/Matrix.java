package main.me.geakstr.engine.geometry;

public class Matrix {
	public static final int DEFAULT_ALLOC = 4;
	
	private float[][] m;
	private int rows, cols;
	
	public Matrix() {
		this(DEFAULT_ALLOC, DEFAULT_ALLOC);
	}
	
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.m = new float[rows][cols];
	}
	
	public int rows() {
		return rows;
	}
	
	public int cols() {
		return cols;
	}
	
	public float[][] m() {
		return m;
	}
	
	public static Matrix identity(int d) {
		Matrix ret = new Matrix(d, d);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < d; j++) {
				ret.m[i][j] = i == j ? 1f : 0f;
			}
		}
		return ret;
	}
	
	public Matrix mul(Matrix a) {
		Matrix ret = new Matrix(rows, a.cols);
		for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < a.cols; j++) {
	            ret.m[i][j] = 0.f;
	            for (int k = 0; k < cols; k++) {
	                ret.m[i][j] += m[i][k] * a.m[k][j];
	            }
	        }
	    }
	    return ret;
	}

	public Matrix mul(VecF v) {
		return mul(GeometryUtils.v2m(v));
	}
	
	public Matrix transpose() {
		Matrix ret = new Matrix(cols, rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				ret.m[j][i] = m[i][j];
			}
		}
		return ret;
	}
	
	public Matrix inverse() {
		Matrix result = new Matrix(rows, cols * 2);
		for (int i = 0; i < rows; i++) {
	        for(int j = 0; j < cols; j++) {
	            result.m[i][j] = m[i][j];
	        }
		}
	    for(int i = 0; i < rows; i++) {
	        result.m[i][i + cols] = 1;
	    }
	    for (int i = 0; i < rows - 1; i++) {
	        for(int j = result.cols - 1; j >= 0; j--) {
	            result.m[i][j] /= result.m[i][i];
	        }
	        for (int k = i + 1; k < rows; k++) {
	            float coeff = result.m[k][i];
	            for (int j = 0; j < result.cols; j++) {
	                result.m[k][j] -= result.m[i][j] * coeff;
	            }
	        }
	    }
	    for (int j = result.cols - 1; j >= rows - 1; j--) {
	        result.m[rows - 1][j] /= result.m[rows - 1][rows-1];
	    }
	    for (int i = rows - 1; i > 0; i--) {
	        for (int k = i - 1; k >= 0; k--) {
	            float coeff = result.m[k][i];
	            for (int j = 0; j < result.cols; j++) {
	                result.m[k][j] -= result.m[i][j] * coeff;
	            }
	        }
	    }
	    
	    Matrix truncate = new Matrix(rows, cols);
	    for(int i = 0; i < rows; i++) {
	        for(int j = 0; j < cols; j++) {
	            truncate.m[i][j] = result.m[i][j + cols];
	        }
	    }
	    return truncate;
	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i < rows; i++)  {
	        for (int j = 0; j < cols; j++) {
	            System.out.print(m[i][j]);
	            if (j < cols - 1) {
	            	System.out.println("\t");
	            }
	        }
	        System.out.println();
	    }
	    return ret;
	}
}
