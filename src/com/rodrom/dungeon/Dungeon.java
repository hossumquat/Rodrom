package com.rodrom.dungeon;

public class Dungeon {
	final public int WALL = 1;
	final public int DOOR = 2;
	final public int HIDDEN = 3;
	
	final public int ROCK = (1 << 4);
	final public int STAIRS_UP = (1 << 5);
	final public int STAIRS_DOWN = (1 << 6);
	final public int PIT = (1 << 7);
	final public int CHUTE = (1 << 8);
	final public int TELEPORTER = (1 << 9);
	final public int FACE_NORTH = (1 << 10);
	final public int FACE_EAST = (1 << 11);
	final public int FACE_SOUTH = (1 << 12);
	final public int FACE_WEST = (1 << 13);
	final public int WATER = (1 << 14);
	final public int FOG = (1 << 15);
	final public int QUICKSAND = (1 << 16);
	final public int ROTATOR = (1 << 17);
	final public int ANTIMAGIC = (1 << 18);
	final public int EXTINGUISHER = (1 << 19);
	final public int STUD = (1 << 20);

	final public int EXPLORED = (1 << 31);
}
