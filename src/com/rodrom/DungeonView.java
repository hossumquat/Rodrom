package com.rodrom;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class DungeonView {
	final static public int WALL = 1;
	final static public int DOOR = 2;
	final static public int HIDDEN = 3;

	final static public int FRONT_WALL = 3;
	final static public int SIDE_WALL = (3 << 2);
	
	final static public double MARGIN = 0.0;
	static public double CORRECTION = 0.5;
	
	static public final int FORWARD_SIZE = 9;
	static public final int SIDE_SIZE = 5;
	static int[][] rightLayout = new int[FORWARD_SIZE][SIDE_SIZE];
	static int[][] leftLayout = new int[FORWARD_SIZE][SIDE_SIZE];
	static public int left, right;

	static public boolean litFlag = false;
	static public boolean texFlag = true;
	static public double rot = 0.0;
	static public double viewPlane = 0.66;
	
	static public int cosTable[] = { 1, 0, -1, 0 };
	static public int sinTable[] = { 0, 1, 0, -1 };

	static public int[] values = new int[10];
	static public int[] lookupTable = {
			-1, 0, 17, 16, 6, 0, 15, 16,
			19, 1, 17, 16, 7, 1, 15, 16,
			12, 5, 17, 16, 11, 5, 15, 16,
			21, 2, 17, 16, 8, 2, 15, 16,
			14, 3, 18, 16, 9, 3, 15, 16,
			20, 1, 18, 16, 7, 1, 15, 16,
			13, 4, 18, 16, 10, 4, 15, 16,
			22, 2, 18, 16, 8, 2, 15, 16,
			};
	
	static public double[] perspectiveCorrectionX, perspectiveCorrectionY;
	
	static public Texture wallTex = null;
	static public Texture doorTex = null;
	static public Texture floorTex = null;
	static public Texture ceilingTex = null;

	static public void init() {
		wallTex = Texture.load("images/wall.jpg");
		doorTex = Texture.load("images/door.jpg");
		floorTex = Texture.load("images/rocky_floor.png");
//		floorTex = Texture.load("images/aneli3.jpg");
//		floorTex = Texture.load("images/test1.png");
		ceilingTex = Texture.load("images/ceiling.png");
//		ceilingTex = Texture.load("images/aneli3.jpg");
//		ceilingTex = Texture.load("images/test2.png");
	}
	
	static public int calcView(Map map) {
		int x = map.xPos;
		int y = map.yPos;

		if (map.facing == 0) {
			values[0] = (map.get(x, y) & Map.WEST_WALL) >> 2;
			values[1] = (map.get(x+1, y) & Map.WEST_WALL) >> 2;
			values[2] = map.get(x-1, y) & Map.NORTH_WALL;
			values[3] = map.get(x, y) & Map.NORTH_WALL;
			values[4] = map.get(x+1, y) & Map.NORTH_WALL;
			values[5] = (map.get(x, y-1) & Map.WEST_WALL) >> 2;
			values[6] = (map.get(x+1, y-1) & Map.WEST_WALL) >> 2;
			values[7] = map.get(x-1, y-1) & Map.NORTH_WALL;
			values[8] = map.get(x, y-1) & Map.NORTH_WALL;
			values[9] = map.get(x+1, y-1) & Map.NORTH_WALL;

		} else if (map.facing == 1) {
			values[0] = map.get(x, y) & Map.NORTH_WALL;
			values[1] = map.get(x, y+1) & Map.NORTH_WALL;
			values[2] = (map.get(x+1, y-1) & Map.WEST_WALL) >> 2;
			values[3] = (map.get(x+1, y) & Map.WEST_WALL) >> 2;
			values[4] = (map.get(x+1, y+1) & Map.WEST_WALL) >> 2;
			values[5] = map.get(x+1, y) & Map.NORTH_WALL;
			values[6] = map.get(x+1, y+1) & Map.NORTH_WALL;
			values[7] = (map.get(x+2, y-1) & Map.WEST_WALL) >> 2;
			values[8] = (map.get(x+2, y) & Map.WEST_WALL) >> 2;
			values[9] = (map.get(x+2, y+1) & Map.WEST_WALL) >> 2;

		} else if (map.facing == 2) {
			values[0] = (map.get(x+1, y) & Map.WEST_WALL) >> 2;
			values[1] = (map.get(x, y) & Map.WEST_WALL) >> 2;
			values[2] = map.get(x+1, y+1) & Map.NORTH_WALL;
			values[3] = map.get(x, y+1) & Map.NORTH_WALL;
			values[4] = map.get(x-1, y+1) & Map.NORTH_WALL;
			values[5] = (map.get(x+1, y+1) & Map.WEST_WALL) >> 2;
			values[6] = (map.get(x, y+1) & Map.WEST_WALL) >> 2;
			values[7] = map.get(x+1, y+2) & Map.NORTH_WALL;
			values[8] = map.get(x, y+2) & Map.NORTH_WALL;
			values[9] = map.get(x-1, y+2) & Map.NORTH_WALL;

		} else {
			values[0] = map.get(x, y+1) & Map.NORTH_WALL;
			values[1] = map.get(x, y) & Map.NORTH_WALL;
			values[2] = (map.get(x, y+1) & Map.WEST_WALL) >> 2;
			values[3] = (map.get(x, y) & Map.WEST_WALL) >> 2;
			values[4] = (map.get(x, y-1) & Map.WEST_WALL) >> 2;
			values[5] = map.get(x-1, y+1) & Map.NORTH_WALL;
			values[6] = map.get(x-1, y) & Map.NORTH_WALL;
			values[7] = (map.get(x-1, y+1) & Map.WEST_WALL) >> 2;
			values[8] = (map.get(x-1, y) & Map.WEST_WALL) >> 2;
			values[9] = (map.get(x-1, y-1) & Map.WEST_WALL) >> 2;
		}

		left = 0;
		if ((values[0] & Map.NORTH_WALL) != 0)
			left |= 1;
		if ((values[3] & Map.NORTH_WALL) != 0)
			left |= 2;
		if ((values[2] & Map.NORTH_WALL) != 0)
			left |= 4;
		if ((values[5] & Map.NORTH_WALL) != 0)
			left |= 8;
		if ((values[8] & Map.NORTH_WALL) != 0)
			left |= 16;
		if ((values[7] & Map.NORTH_WALL) != 0)
			left |= 32;

		right = 0;
		if ((values[1] & Map.NORTH_WALL) != 0)
			right |= 1;
		if ((values[3] & Map.NORTH_WALL) != 0)
			right |= 2;
		if ((values[4] & Map.NORTH_WALL) != 0)
			right |= 4;
		if ((values[6] & Map.NORTH_WALL) != 0)
			right |= 8;
		if ((values[8] & Map.NORTH_WALL) != 0)
			right |= 16;
		if ((values[9] & Map.NORTH_WALL) != 0)
			right |= 32;

		left = lookupTable[left];
		right = lookupTable[right];
		return 0;
	}

	static public int constructCell(Map map, int x, int y, int dir1, int dir2) {
		int value = map.get(x, y) & Map.NONWALL_MASK;
		value |= map.getWall(x, y, dir1);
		value |= map.getWall(x, y, dir2) << 2;
		return value;
	}

	static public void constructLayouts(Map map) {
		boolean prevWall;
		double xx, yy;

		int x = map.xPos;
		int y = map.yPos;

		for (int f=0; f<FORWARD_SIZE; f++) {
			for (int s=0; s<SIDE_SIZE; s++) {
				if (map.facing == 0) {
					leftLayout[f][s] = constructCell(map, x - s, y - f, 0, 3);
					rightLayout[f][s] = constructCell(map, x + s, y - f, 0, 1);

				} else if (map.facing == 1) {
					leftLayout[f][s] = constructCell(map, x + f, y - s, 1, 0);
					rightLayout[f][s] = constructCell(map, x + f, y + s, 1, 2);
		
				} else if (map.facing == 2) {
					leftLayout[f][s] = constructCell(map, x + s, y + f, 2, 1);
					rightLayout[f][s] = constructCell(map, x - s, y + f, 2, 3);
		
				} else {
					leftLayout[f][s] = constructCell(map, x - f, y + s, 3, 2);
					rightLayout[f][s] = constructCell(map, x - f, y - s, 3, 0);
				}
			}
		}

		for (int f=0; f<FORWARD_SIZE; f++) {
			int s = 1 - SIDE_SIZE;
			prevWall = false;
			yy = f + 0.5 - MARGIN + CORRECTION;
			
			while (s < SIDE_SIZE) {
				int value = (s < 0) ? leftLayout[f][-s] : rightLayout[f][s];
				if ((value & FRONT_WALL) != 0) {
					xx = s - 0.5;
					if (!prevWall)
						xx -= MARGIN;

					int t = ((value & FRONT_WALL) == DOOR) ? DOOR : WALL; 
					Wall3D.add(xx, yy, s + 0.5, yy, t, false);
					prevWall = true;

				} else {
					if (prevWall)
						Wall3D.adjustLast(MARGIN, 0.0);

					prevWall = false;
				}

				s++;
			}
		}

		for (int s=0; s<SIDE_SIZE; s++) {
			prevWall = false;
			xx = -s - 0.5 + MARGIN;

			for (int f=0; f<FORWARD_SIZE; f++) {
				int value = leftLayout[f][s];
				if ((value & SIDE_WALL) != 0) {
					yy = f - 0.5 + CORRECTION;
					if (!prevWall)
						yy -= MARGIN;

					int t = (((value & SIDE_WALL) >> 2) == DOOR) ? DOOR : WALL; 
					Wall3D.add(xx, yy, xx, f + 0.5 + CORRECTION, t, false);
					prevWall = true;

				} else {
					if (prevWall)
						Wall3D.adjustLast(0.0, MARGIN);
				}
			}

			prevWall = false;
			xx = s + 0.5 - MARGIN;

			for (int f=0; f<FORWARD_SIZE; f++) {
				int value = rightLayout[f][s];
				if ((value & SIDE_WALL) != 0) {
					yy = f - 0.5 + CORRECTION;
					if (!prevWall)
						yy -= MARGIN;

					int t = (((value & SIDE_WALL) >> 2) == DOOR) ? DOOR : WALL; 
					Wall3D.add(xx, yy, xx, f + 0.5 + CORRECTION, t, true);
					prevWall = true;

				} else {
					if (prevWall)
						Wall3D.adjustLast(0.0, MARGIN);
				}
			}
		}

		left = 0;
		if ((leftLayout[0][0] & SIDE_WALL) != 0)
			left |= 1;
		if ((leftLayout[0][0] & FRONT_WALL) != 0)
			left |= 2;
		if ((leftLayout[0][1] & FRONT_WALL) != 0)
			left |= 4;
		if ((leftLayout[1][0] & SIDE_WALL) != 0)
			left |= 8;
		if ((leftLayout[1][0] & FRONT_WALL) != 0)
			left |= 16;
		if ((leftLayout[1][1] & FRONT_WALL) != 0)
			left |= 32;

		right = 0;
		if ((rightLayout[0][0] & SIDE_WALL) != 0)
			right |= 1;
		if ((rightLayout[0][0] & FRONT_WALL) != 0)
			right |= 2;
		if ((rightLayout[0][1] & FRONT_WALL) != 0)
			right |= 4;
		if ((rightLayout[1][0] & SIDE_WALL) != 0)
			right |= 8;
		if ((rightLayout[1][0] & FRONT_WALL) != 0)
			right |= 16;
		if ((rightLayout[1][1] & FRONT_WALL) != 0)
			right |= 32;

		left = lookupTable[left];
		right = lookupTable[right];
	}

	static public BufferedImage loadTexture(String name) {
		BufferedImage tex = null;

		try {
			BufferedImage temp = ImageIO.read(new File(name));
			tex = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = tex.getGraphics();
			g.drawImage(temp, 0, 0, null);
			g.dispose();

		} catch (IOException e) {
			tex = null;
			e.printStackTrace();
		}

		return tex;
	}

	static public double light(double s) {
/*		if (!litFlag) {
			s = s * 5.0 - 2.6;
		}*/

		s = 1.0 / (s * s);
		if (s < 0.2) {
			s = s / 0.2;
			s = s * s * 0.2;
			if (s < 0.15) {
				s = s / 0.15;
				s = s * s * s * 0.15;
			}
		}

		return s;
	}

	static public void preCalc(int width, int height) {
		perspectiveCorrectionX = new double[width];
		perspectiveCorrectionY = new double[height];

		double baseAngleY = Math.atan(viewPlane / 2.0);
		double angleStepY = baseAngleY * 2.0 / (height - 1);
		
		double baseAngleX = Math.atan(viewPlane * (double) width / (double) height / 2.0);
		double angleStepX = baseAngleX * 2.0 / (width - 1);

		for (int x=0; x<width; x++)
			perspectiveCorrectionX[x] = 1.0 / Math.cos(x * angleStepX - baseAngleX);

		for (int y=0; y<height; y++)
			perspectiveCorrectionY[y] = 1.0 / Math.cos(y * angleStepY - baseAngleY);
	}

	static public void render(BufferedImage image, Map map) {
		if (litFlag) {
			oldRender(image, map);
			return;
		}

		long start = System.currentTimeMillis();

		Wall3D.reset();
		constructLayouts(map);

		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println("size " + width + " x " + height);

		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		Arrays.fill(pixels, 0);
//		System.out.println("FOV = " + (Math.atan(viewPlane / 2.0) * 360.0 / Math.PI) + " viewPlane = " + viewPlane);

		if (wallTex == null)
			init();

		int tw = floorTex.texture.getWidth();
		int th = floorTex.texture.getHeight();

		double rayX, rayY;
		double rayZ = 1.0;

		Plane plane = new Plane();
		plane.set(0.0, -0.5, 0.0, 0.0, 1.0, 0.0);

		// render floor and ceiling
		for (int y=0; y<height/2; y++) {
			int y2 = height - y - 1;
			double scaler = 1.0 - 2.0 * (double) y / (double) (height - 1);  // 1.0 to 0.0
			rayY = -viewPlane * scaler;

			for (int x=0; x<width; x++) {
				scaler = 2.0 * (double) x / (double) (width - 1) - 1.0;  // -1 to 1
				rayX = viewPlane * scaler * (double) width / (double) height;
				if (Math.abs(rayX) >= viewPlane * 2.0)
					continue;  // reasonably limit the distortion

				double s = plane.distToPlane(rayX, rayY, rayZ);
				int tx = (int) (rayX * s / plane.mag * tw * 256) + tw * 128;
				int ty = (int) ((rayZ * s / plane.mag - CORRECTION) * th * 256) + th * 128;

				double intensity = light(s);
				if (x == 200 && y == 200)
					System.out.println(s + " " + intensity);
				if (intensity < 0.0)
					intensity = 0.0;
				if (intensity > 1.0)
					intensity = 1.0;

				int texelIndex = ((tx >> 8) & (tw - 1)) + ((ty >> 8) & (th - 1)) * th;
				int color = texFlag ? ceilingTex.texels[texelIndex] : 0xffffff;
				int red = (int) (((color >> 16) & 0xff) * intensity);
				int green = (int) (((color >> 8) & 0xff) * intensity);
				int blue = (int) (((color) & 0xff) * intensity);
				pixels[x + y * width] = (red << 16) | (green << 8) | blue;

				color = texFlag ? floorTex.texels[texelIndex] : 0xffffff;
				red = (int) (((color >> 16) & 0xff) * intensity);
				green = (int) (((color >> 8) & 0xff) * intensity);
				blue = (int) (((color) & 0xff) * intensity);
				pixels[x + y2 * width] = (red << 16) | (green << 8) | blue;
			}
		}

		// render walls
		for (int x=0; x<width; x++) {
			double scaler = 2.0 * (double) x / (double) (width - 1) - 1.0;  // -1 to 1
			rayX = viewPlane * scaler * (double) width / (double) height;
			if (Math.abs(rayX) >= viewPlane * 2.0)
				continue;  // reasonably limit the distortion

			double xx = rayX * Math.cos(rot) - rayZ * Math.sin(rot);
			double zz = rayZ * Math.cos(rot) + rayX * Math.sin(rot);

			Wall3D wall = Wall3D.find(rayX, rayZ);
			if (wall != null) {
				tw = wallTex.texture.getWidth();
				th = wallTex.texture.getHeight();
				int[] texels = wallTex.texels;

				if (wall.texture == DOOR) {
					tw = doorTex.texture.getWidth();
					th = doorTex.texture.getHeight();
					texels = doorTex.texels;
				}

				int size = (int) ((double) height / (2.0 * viewPlane * wall.s));
				int y = height / 2 - size / 2;
				if (y < 0)
					y = 0;

				int yEnd = y + size;
				if (yEnd > height)
					yEnd = height;

				wall.set(plane);

				double intensity = (double) size / height;
				if (intensity > 1.0)
					intensity = 1.0;
				else if (intensity < 0.0)
					intensity = 0.0;

				int tx = (int) (wall.t * tw) & (tw - 1);
				if (wall.flip)
					tx = (int) ((1.0 - wall.t) * tw) & (tw - 1);

				int ty = 0;
				while (y < yEnd) {
					scaler = 2.0 * (double) y / (double) (height - 1) - 1.0;  // -1 to 1
					rayY = -viewPlane * scaler;

					ty = y * 256 - height * 128 + size * 128;
					ty = (ty * th / size / 256) & (th - 1);

					double s = plane.distToPlane(rayX, rayY, rayZ);
//					tx = (int) (rayX * s / plane.mag * tw * 256) + tw * 128;
//					ty = (int) ((rayZ * s / plane.mag - CORRECTION) * th * 256) + th * 128;

					intensity = light(s);
					if (intensity < 0.0)
						intensity = 0.0;
					if (intensity > 1.0)
						intensity = 1.0;

					int color = texFlag ? texels[(tx & (tw - 1)) + (ty & (th - 1)) * th] : 0xffffff;
					int red = (int) (((color >> 16) & 0xff) * intensity);
					int green = (int) (((color >> 8) & 0xff) * intensity);
					int blue = (int) (((color) & 0xff) * intensity);
					pixels[x + y * width] = (red << 16) | (green << 8) | blue;
					y++;
				}
			}
		}

		System.out.println("Render took " + (System.currentTimeMillis() - start) + " ms");
	}

	static public void oldRender(BufferedImage image, Map map) {
		long start = System.currentTimeMillis();

		Wall3D.reset();
		constructLayouts(map);

		int width = image.getWidth();
		int height = image.getHeight();
		double dw = viewPlane / width;
		double dh = viewPlane / height;

		if (perspectiveCorrectionX == null)
			preCalc(width, height);
		
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		Arrays.fill(pixels, 0);
//		System.out.println("FOV = " + (Math.atan(viewPlane) * 360.0 / Math.PI) + " viewPlane = " + viewPlane);

		if (wallTex == null)
			init();

		double rayZ = 1.0;
		for (int y=0; y<height/2; y++) {
			int y2 = height - y - 1;
			double scaler = 1.0 - 2.0 * (double) y / (double) height;
			double rayY = -viewPlane * scaler;
			double s = 1.0 / rayY;

			int tw = floorTex.texture.getWidth();
			int th = floorTex.texture.getHeight();
//			int stepY = (int) (th * 256 / s);
			int ty = (int) (s * th * 256);

			double intensity = 1.2 - (s * 0.1);
			if (intensity > 1.0)
				intensity = 1.0;
			else if (intensity < 0.0)
				intensity = 0.0;
			intensity *= intensity;

			int stepX = (int) -(dw * tw / rayY * 256);
			int tx = -width * stepX / 2;

			for (int x=0; x<width; x++) {
				int texelIndex = ((tx >> 8) & (tw - 1)) + ((ty >> 8) & (th - 1)) * th;

				int color = texFlag ? ceilingTex.texels[texelIndex] : 0xffffff;
				int red = (int) (((color >> 16) & 0xff) * intensity);
				int green = (int) (((color >> 8) & 0xff) * intensity);
				int blue = (int) (((color) & 0xff) * intensity);
				pixels[x + y * width] = (red << 16) | (green << 8) | blue;

				color = texFlag ? floorTex.texels[texelIndex] : 0xffffff;
				red = (int) (((color >> 16) & 0xff) * intensity);
				green = (int) (((color >> 8) & 0xff) * intensity);
				blue = (int) (((color) & 0xff) * intensity);
				pixels[x + y2 * width] = (red << 16) | (green << 8) | blue;
				
				tx += stepX;
			}
		}
		
		for (int x=0; x<width; x++) {
			double scaler = 2.0 * (double) x / (double) width - 1.0;  // -1 to 1
			double rayX = viewPlane * scaler;
			rayZ = 1.0;
			double xx = rayX;
			double yy = rayZ;
			rayX = xx * Math.cos(rot) - yy * Math.sin(rot);
			rayZ = yy * Math.cos(rot) + xx * Math.sin(rot);

			Wall3D wall = Wall3D.find(rayX, rayZ);
			if (wall != null) {
				int tw = wallTex.texture.getWidth();
				int th = wallTex.texture.getHeight();
				int[] texels = wallTex.texels;

				if (wall.texture == DOOR) {
					tw = doorTex.texture.getWidth();
					th = doorTex.texture.getHeight();
					texels = doorTex.texels;
				}

				int size = (int) ((double) height / (2.0 * viewPlane * wall.s));
				int y = height / 2 - size / 2;
				if (y < 0)
					y = 0;

				int yEnd = height / 2 + size / 2;
				if (yEnd > height)
					yEnd = height;

				double intensity = (double) size / height;
//				intensity = 1.0 / (2.0 * viewPlane * wall.s);
				intensity = 1.2 - (wall.s * 0.2);
				if (intensity > 1.0)
					intensity = 1.0;
				else if (intensity < 0.0)
					intensity = 0.0;
//				intensity = 1.0 - intensity;
				intensity *= intensity;
//				intensity = 1.0 - intensity;
//				System.out.println(intensity + " " + wall.s);

				int tx = (int) (wall.t * tw) & (tw - 1);
				if (wall.flip)
					tx = (int) ((1.0 - wall.t) * tw) & (tw - 1);

				int ty = 0;
				while (y < yEnd) {
					ty = y * 256 - height * 128 + size * 128;
					ty = (ty * th / size / 256) & (th - 1);

					int color = texFlag ? texels[tx + ty * th] : 0xffffff;
					int red = (int) (((color >> 16) & 0xff) * intensity);
					int green = (int) (((color >> 8) & 0xff) * intensity);
					int blue = (int) (((color) & 0xff) * intensity);
					pixels[x + y * width] = (red << 16) | (green << 8) | blue;
					y++;
				}
			}
		}

		System.out.println("Render took " + (System.currentTimeMillis() - start) + " ms");
	}
}
