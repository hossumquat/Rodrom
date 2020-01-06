package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

// bits 0-1 for north wall, bits 2-3 for west wall
public class Map {
	// 2 bit field values (for bits 0-1 and 2-3)
	final static public int WALL = 1;
	final static public int DOOR = 2;
	final static public int HIDDEN = 3;

	final static public int NORTH_WALL = 3;
	final static public int WEST_WALL = (3 << 2);
	final static public int ROCK = (1 << 4);
	final static public int STAIRS_UP = (1 << 5);
	final static public int STAIRS_DOWN = (1 << 6);
	final static public int PIT = (1 << 7);
	final static public int CHUTE = (1 << 8);
	final static public int ROTATOR = (1 << 9);
	final static public int TELEPORTER = (1 << 10);
	final static public int ANTIMAGIC = (1 << 11);
	final static public int EXTINGUISHER = (1 << 12);
	final static public int STUD = (1 << 13);
	final static public int WATER = (1 << 14);
	final static public int FOG = (1 << 15);
	final static public int DARK = (1 << 16);
	final static public int QUICKSAND = (1 << 17);
	final static public int FACE_DIR = (1 << 18);

	// 2 bit field values if FACE_DIR is set
	final static public int FACE_NORTH = 0;
	final static public int FACE_EAST = (1 << 19);
	final static public int FACE_SOUTH = (2 << 19);
	final static public int FACE_WEST = (3 << 19);
	final static public int FACE_MASK = (3 << 19);

	final static public int SEMI_EXPLORED = (1 << 26);

	// special case hacks
	final static public int FOG_UNKNOWN_BIT_POS = 16;  // 8 bit range, used only when unexplored
	final static public int SOUTH_WALL_BIT_POS = 27;
	final static public int EAST_WALL_BIT_POS = 29;
	
	final static public int NONWALL_MASK = 0x7ffffff0;
	
	final static public int OUT_OF_BOUNDS = WALL | (WALL << 2) | ROCK | (1 << 31);

	final static public int MAX_SIZE = 256;
	
	public int xPos, yPos, facing, orient;
	public int width, height;
	public int[] tiles;

	public int exploredMarginSize;
	public int exploredSpan;  // distance going from y to y+1 in explored array
	public byte[] explored;

	public Map() {
		width = height = 0;
		tiles = null;
		exploredMarginSize = 2;
		explored = null;
		init();
	}

	public Map(int size) {
		this(size, size);
	}

	public Map(int width, int height) {
		this.width = width;
		this.height = height;

		tiles = new int[width * height];

		// make map initially all rock
		Arrays.fill(tiles, WALL | (WALL << 2) | ROCK);

		exploredMarginSize = 2;
		explored = null;
		init();
	}
	
	public void derive(Map master) {
		width = master.width;
		height = master.height;
		tiles = new int[width * height];
		
		xPos = master.xPos;
		yPos = master.yPos;
		facing = master.facing;
		orient = master.orient;
		
		explored = null;
		init();
	}

	public void init() {
		exploredSpan = width + exploredMarginSize * 2;
	}

	public void findStart() {
		for (yPos=0; yPos<height; yPos++) {
			for (xPos=0; xPos<width; xPos++) {
				if ((tiles[xPos + yPos * width] & STAIRS_UP) != 0)
					return;
			}
		}
		
		xPos = yPos = 0;
	}
	
	public int getWall(int x, int y, int dir) {
		if (dir == 1)
			x++;
		else if (dir == 2)
			y++;

		if (x < 0 || y < 0 || x >= width || y >= height)
			return WALL;

		if ((dir & 1) == 0)
			return tiles[x + y * width] & NORTH_WALL;
		else
			return (tiles[x + y * width] & WEST_WALL) >> 2;
	}

	public boolean isNorthWall() {
		return getWall(xPos, yPos, 0) == WALL;
	}

	public boolean isEastWall() {
		return getWall(xPos, yPos, 1) == WALL;
	}

	public boolean isSouthWall() {
		return getWall(xPos, yPos, 2) == WALL;
	}

	public boolean isWestWall() {
		return getWall(xPos, yPos, 3) == WALL;
	}

	public int get(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return OUT_OF_BOUNDS;

		return tiles[x + y * width];
	}

	public int get() {
		return get(xPos, yPos);
	}
	
	public void set(int mask, boolean state) {
		if (xPos < 0 || yPos < 0 || xPos >= width || yPos >= height)
			return;

		int index = xPos + yPos * width;
		if (state)
			tiles[index] |= mask;
		else
			tiles[index] &= ~mask;
	}

	public void set(int mask, int state) {
		if (xPos < 0 || yPos < 0 || xPos >= width || yPos >= height)
			return;

		int index = xPos + yPos * width;
		tiles[index] &= ~mask;
		tiles[index] |= state;
	}

	public boolean isSemiExplored(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			if ((tiles[x + y * width] & SEMI_EXPLORED) != 0)
				return true;
		}
		
		return isExplored(x, y);
	}

	public boolean isExplored(int x, int y) {
		if (explored == null)
			return false;

		x += exploredMarginSize;
		y += exploredMarginSize;

		if (x < 0 || y < 0 || x >= exploredSpan || y >= height + exploredMarginSize * 2)
			return false;

		int pos = x + y * exploredSpan;
		int index = pos / 8;
		pos &= 7;

		return (explored[index] & (1 << pos)) != 0;
	}

	public boolean checkAndRevealWall(int x, int y, int dir, Map map) {
		dir = (facing + dir) & 3;

		if (facing == 0) {
			x = xPos + x;
			y = yPos + y;

		} else if (facing == 1) {
			int xx = xPos - y;
			y = yPos + x;
			x = xx;

		} else if (facing == 2) {
			x = xPos - x;
			y = yPos - y;

		} else {
			int xx = xPos + y;
			y = yPos - x;
			x = xx;
		}

		if (dir == 1)
			x++;
		else if (dir == 2)
			y++;

		if ((dir & 1) == 0 && y == height && x >= 0 && x < width) {
			map.tiles[x + y * width - width] |= (1 << SOUTH_WALL_BIT_POS);
			return false;
		}
		
		if ((dir & 1) != 0 && x == width && y >= 0 && y < height) {
			map.tiles[x + y * width - 1] |= (1 << EAST_WALL_BIT_POS);
			return false;
		}

		if (x < 0 || y < 0 || x >= width || y >= height)
			return false;

		int wall, index = x + y * width;
		if ((dir & 1) == 0)
			wall = tiles[index] & NORTH_WALL;
		else
			wall = tiles[index] & WEST_WALL;

		map.tiles[index] |= wall;
		return wall == 0;
	}

	public void explore(int x, int y, Map map, int code) {
		if (facing == 0) {
			x = xPos + x;
			y = yPos + y;

		} else if (facing == 1) {
			int xx = xPos - y;
			y = yPos + x;
			x = xx;

		} else if (facing == 2) {
			x = xPos - x;
			y = yPos - y;

		} else {
			int xx = xPos + y;
			y = yPos - x;
			x = xx;
		}

		if (code == 0)
			map.setExplored(x, y, this);

		else {
			if (x >= 0 && y >= 0 && x < width && y < height) {
				int index = x + y * width;
				map.tiles[index] |= SEMI_EXPLORED;
			}
		}
	}

	// Reveal what can be seen from current position and facing
	public void explore(Map map) {
		int r1, r2, l1, l2;
		r1 = r2 = l1 = l2 = 0;
		
		int x = xPos = map.xPos;
		int y = yPos = map.yPos;
		facing = map.facing;

		map.setExplored(x, y, this);
		if (checkAndRevealWall(0, 0, 0, map)) {
			explore(0, -1, map, 0);
			if (checkAndRevealWall(0, -1, 0, map)) {
				explore(0, -2, map, 4);
				if (checkAndRevealWall(0, -2, 1, map))
					r2 = 32;
				if (checkAndRevealWall(0, -2, 3, map))
					l2 = 64;
			}

			if (checkAndRevealWall(0, -1, 1, map))
				r1 = 8;
			if (checkAndRevealWall(0, -1, 3, map))
				l1 = 2;
		}

		if (checkAndRevealWall(0, 0, 1, map)) {
			explore(1, 0, map, 8);
			if (checkAndRevealWall(1, 0, 0, map))
				r1 = (r1 == 0) ? 4 : 12;
		}

		if (checkAndRevealWall(0, 0, 3, map)) {
			explore(-1, 0, map, 2);
			if (checkAndRevealWall(-1, 0, 0, map))
				l1 = (l1 == 0) ? 4 : 6;
		}

		if (r1 > 0) {
			explore(1, -1, map, r1);
			if (checkAndRevealWall(1, -1, 0, map))
				r2 = 32;
		}
		
		if (r2 > 0)
			explore(1, -2, map, r2);

		if (l1 > 0) {
			explore(-1, -1, map, l1);
			if (checkAndRevealWall(-1, -1, 0, map))
				l2 = 64;
		}

		if (l2 > 0)
			explore(-1, -2, map, l2);

/*		if (getWall(x, y, facing) == 0) {
			if (facing == 0) {
				map.setExplored(x, y - 1, this);
				if (getWall(x, y, 1) == 0 && getWall(x, y - 1, 1) == 0 && getWall(x + 1, y, 0) == 0)
					map.setExplored(x + 1, y - 1, this);
				if (getWall(x, y, 3) == 0 && getWall(x, y - 1, 3) == 0 && getWall(x - 1, y, 0) == 0)
					map.setExplored(x - 1, y - 1, this);
	
			} else if (facing == 1) {
				map.setExplored(x + 1, y, this);
				if (getWall(x, y, 2) == 0 && getWall(x + 1, y, 2) == 0 && getWall(x, y + 1, 1) == 0)
					map.setExplored(x + 1, y + 1, this);
				if (getWall(x, y, 0) == 0 && getWall(x + 1, y, 0) == 0 && getWall(x, y - 1, 1) == 0)
					map.setExplored(x + 1, y - 1, this);
	
			} else if (facing == 2) {
				map.setExplored(x, y + 1, this);
				if (getWall(x, y, 1) == 0 && getWall(x, y + 1, 1) == 0 && getWall(x + 1, y, 2) == 0)
					map.setExplored(x + 1, y + 1, this);
				if (getWall(x, y, 3) == 0 && getWall(x, y + 1, 3) == 0 && getWall(x - 1, y, 2) == 0)
					map.setExplored(x - 1, y + 1, this);
	
			} else {
				map.setExplored(x - 1, y, this);
				if (getWall(x, y, 0) == 0 && getWall(x - 1, y, 0) == 0 && getWall(x, y - 1, 3) == 0)
					map.setExplored(x - 1, y - 1, this);
				if (getWall(x, y, 2) == 0 && getWall(x - 1, y, 2) == 0 && getWall(x, y + 1, 3) == 0)
					map.setExplored(x - 1, y + 1, this);
			}
		}*/
	}

	public void setExplored(int x, int y, Map master) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			int index = x + y * width;
			tiles[index] |= master.tiles[index] & ~(NORTH_WALL | WEST_WALL | SEMI_EXPLORED);
		}

		x += exploredMarginSize;
		y += exploredMarginSize;

		if (x < 0 || y < 0 || x >= exploredSpan || y >= height + exploredMarginSize * 2)
			return;

		if (explored == null) {
			int size = ((width + exploredMarginSize * 2) * (height + exploredMarginSize * 2) + 7) / 8;
			explored = new byte[size];
		}

		int pos = x + y * exploredSpan;
		int index = pos / 8;
		pos &= 7;

		explored[index] |= (1 << pos);
	}

	// attempt to create a wall, door or hidden door
	public void setWall(int x, int y, int value, int mask) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		
		int index = x + y * width;
		if ((tiles[index] & ROCK) != 0)
			return;

		if (mask == NORTH_WALL) {
			if (y == 0)
				return;

			if ((tiles[index - width] & ROCK) != 0)
				return;
		}
		
		if (mask == WEST_WALL) {
			if (x == 0)
				return;

			if ((tiles[index - 1] & ROCK) != 0)
				return;
		}

		if (mask == WEST_WALL)
			value <<= 2;

		tiles[index] &= ~mask;
		tiles[index] |= value;
	}

	// set position in map to match position in another map
	public void setPosFrom(Map source) {
		xPos = source.xPos;
		yPos = source.yPos;
		facing = source.facing;
		orient = source.orient;
	}

	// make this a copy of another map
	public Map copy(Map source) {
		width = source.width;
		height = source.height;

		int size = width * height;
		tiles = new int[size];

		for (int i = 0; i < size; i++)
			tiles[i] = source.tiles[i];

		init();
		return this;
	}

	public Map load(DataInputStream in) throws IOException {
		width = in.readInt();
		height = in.readInt();

		if (width < 1 || height < 1 || width > MAX_SIZE || height > MAX_SIZE)
			throw new IOException("Invalid map file");

		int size = width * height;
		tiles = new int[size];

		for (int i = 0; i < size; i++)
			tiles[i] = in.readInt();

		init();
		return this;
	}

	public Map save(DataOutputStream out) throws IOException {
		out.writeInt(width);
		out.writeInt(height);

		int size = width * height;
		for (int i = 0; i < size; i++)
			out.writeInt(tiles[i]);

		return this;
	}
}
