package com.rodrom;

public class Wall3D {
	final static public int MAX_SIZE = 256;
	static public Wall3D[] walls;

	static public int left, front, right;
	static public int total;

	public double x1, y1, x2, y2, s, t;
	public int color;
	public int texture;
	boolean flip;

	static public void init() {
		walls = new Wall3D[MAX_SIZE];
		for (int i = 0; i < MAX_SIZE; i++) {
			walls[i] = new Wall3D();
		}
	}

	static public void reset() {
		if (walls == null)
			init();

		total = front = 0;
	}

	static public void add(double x1, double y1, double x2, double y2, int texture, boolean flip) {
		if (total >= MAX_SIZE) {
			System.out.println("Wall3D[] is full, add failed.");
			return;
		}

		Wall3D element = walls[total++];
		element.x1 = x1;
		element.y1 = y1;
		element.x2 = x2;
		element.y2 = y2;
		element.texture = texture;
		element.flip = flip;
	}
	
	static public void adjustLast(double dx, double dy) {
		Wall3D element = walls[total - 1];

		element.x2 += dx;
		element.y2 += dy;
	}

	static public Wall3D find(double dx, double dy) {
		Wall3D best = null;

		for (int i = 0; i < total; i++) {
			Wall3D element = walls[i];
			if (element.testIntersect(dx, dy) > 0.0) {
				if (best == null || element.s < best.s)
					best = element;
			}
		}

		return best;
	}

	// check if cast ray intersect line or not. Ray starts at (0,0) and points
	// (dx,dy).
	// return scaler distance to the intersection (i.e. s * (dx,dy)) or negative if
	// no intersect.
	public double testIntersect(double dx, double dy) {
		double dx1 = x2 - x1;
		double dy1 = y2 - y1;

		double det = dx1 * dy - dx * dy1; // determinants
		double det1 = dx1 * y1 - x1 * dy1;
		double det2 = dx * y1 - x1 * dy;

		if (det == 0.0) // parallel
			return -1.0;

		s = det1 / det;
		t = det2 / det;
		if (t < 0.0 || t > 1.0)
			return -1.0;

		return s;
	}
	
	public void set(Plane plane) {
		double nx = flip ? (y2 - y1) : (y1 - y2);
		double nz = flip ? (x1 - x2) : (x2 - x1);
		plane.set(x1, 0.0, y1, nx, 0.0, nz);
	}
}
