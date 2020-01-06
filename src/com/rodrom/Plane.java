package com.rodrom;

public class Plane {
	double x, y, z;  // origin point on plane
	double nx, ny, nz;  // normal vector
	double mag;

	public void set(double x, double y, double z, double nx, double ny, double nz) {
		this.x = x; 
		this.y = y; 
		this.z = z; 
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}

	// dot product with the plane normal
	double dot(double x, double y, double z) {
		return x * nx + y * ny + z * nz;
	}

	double distToPlane(double dx, double dy, double dz) {
		mag = Math.sqrt(dx * dx + dy * dy + dz * dz);
		dx /= mag;
		dy /= mag;
		dz /= mag;

		double top = dot(x, y, z);
		double bottom = dot(dx, dy, dz);
		if (bottom == 0.0)
			return Double.MAX_VALUE;

		return top / bottom;
	}
}
