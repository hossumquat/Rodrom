package com.rodrom.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.rodrom.Game;
import com.rodrom.Map;
import com.rodrom.Symbols;

public class MapViewPanel extends JPanel implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String path = "images/map symbols.png";
//	private static final String path = "images/msym.png";
	private static final int maxTileChoices = 20;
	private static int[] tileChoices = new int[maxTileChoices];

	private static int symbolSize;

	public static int FOU = 0;

	public Map map;
	public static BufferedImage image;
	public boolean editMode = false;
	public MapEditTab editor = null;

	public int counter = 0;
	private Point coords = new Point();

	public MapViewPanel(Map map) {
		super(false);

		this.map = map;

		if (image == null) {
			File file = new File(path);

			try {
				image = ImageIO.read(file);

			} catch (IOException e) {
				image = null;
				e.printStackTrace();
			}

			// symbols spritesheet is always 10 tiles wide so we can calculate tile size
			symbolSize = (image.getWidth() - 9) / 10;
		}

		setBorder(BorderFactory.createLineBorder(Color.black, 1));

		setFocusable(true);
		addMouseListener(this);

		Timer timer = new Timer(500, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		counter++;
		repaint();
	}

	public void draw(Graphics g, int x, int y, int index) {
		int sx = (index % 10) * (symbolSize + 1);
		int sy = (index / 10) * (symbolSize + 1);
		if (sy >= image.getHeight())
			return;

		g.drawImage(image, x, y, x + symbolSize, y + symbolSize, sx, sy, sx + symbolSize, sy + symbolSize, null);
	}

	public void drawUnexplored(int x, int y, int xx, int yy, Graphics g) {
		int index = 0;

		if (FOU == 0) {
			if ((map.get(x, y) & Map.SEMI_EXPLORED) != 0)
				draw(g, xx, yy, 30);
			else
				draw(g, xx, yy, 118);
			
			return;
		}

		if (FOU == 1) {
			if (map.isExplored(x, y - 1) && map.getWall(x, y, 0) == 0)
				index |= 1;
			if (map.isExplored(x + 1, y) && map.getWall(x, y, 1) == 0)
				index |= 2;
			if (map.isExplored(x, y + 1) && map.getWall(x, y, 2) == 0)
				index |= 4;
			if (map.isExplored(x - 1, y) && map.getWall(x, y, 3) == 0)
				index |= 8;

		} else if (FOU == 2) {
			if (map.isExplored(x, y - 1))
				index |= 1;
			if (map.isExplored(x + 1, y))
				index |= 2;
			if (map.isExplored(x, y + 1))
				index |= 4;
			if (map.isExplored(x - 1, y))
				index |= 8;
			if (map.isExplored(x + 1, y - 1))
				index |= 16;
			if (map.isExplored(x + 1, y + 1))
				index |= 32;
			if (map.isExplored(x - 1, y + 1))
				index |= 64;
			if (map.isExplored(x - 1, y - 1))
				index |= 128;
		}
		
		index = simplifyIndex(index);
		index = translateIndex(index);
		draw(g, xx, yy, index);
	}
	
	public void draw(int x, int y, int value, Graphics g) {
		int index = Symbols.BACKGROUND;
		if ((value == Map.OUT_OF_BOUNDS) && editMode)
			index = Symbols.OUT_OF_BOUNDS;
		else if ((value & Map.ROCK) != 0)
			index = Symbols.ROCK;
		else if ((value & Map.DARK) != 0)
			index = Symbols.DARK;
		else if ((value & Map.FOG) != 0)
			index = Symbols.FOG;

		draw(g, x, y, index);

		index = 0;
		if ((value & Map.STAIRS_UP) != 0)
			tileChoices[index++] = Symbols.STAIRS_UP;

		if ((value & Map.STAIRS_DOWN) != 0)
			tileChoices[index++] = Symbols.STAIRS_DOWN;

		if ((value & Map.PIT) != 0)
			tileChoices[index++] = Symbols.PIT;

		if ((value & Map.CHUTE) != 0)
			tileChoices[index++] = Symbols.CHUTE;

		if ((value & Map.ROTATOR) != 0)
			tileChoices[index++] = Symbols.ROTATOR;

		if ((value & Map.TELEPORTER) != 0)
			tileChoices[index++] = Symbols.TELEPORTER;

		if ((value & Map.ANTIMAGIC) != 0)
			tileChoices[index++] = Symbols.ANTIMAGIC;

		if ((value & Map.EXTINGUISHER) != 0)
			tileChoices[index++] = Symbols.EXTINGUISHER;

		if ((value & Map.STUD) != 0)
			tileChoices[index++] = Symbols.STUD;

		if ((value & Map.WATER) != 0)
			tileChoices[index++] = Symbols.WATER;

		if ((value & Map.QUICKSAND) != 0)
			tileChoices[index++] = Symbols.QUICKSAND;

		if ((value & Map.FACE_DIR) != 0) {
			int dir = value & Map.FACE_WEST;

			if (dir == Map.FACE_NORTH)
				tileChoices[index++] = Symbols.FACE_NORTH;

			else if (dir == Map.FACE_EAST)
				tileChoices[index++] = Symbols.FACE_EAST;

			else if (dir == Map.FACE_SOUTH)
				tileChoices[index++] = Symbols.FACE_SOUTH;

			else if (dir == Map.FACE_WEST)
				tileChoices[index++] = Symbols.FACE_WEST;
		}
		
		if (index == 0)
			return;

		index = (counter / 2) % index;

		draw(g, x, y, tileChoices[index]);
	}

	public void getCoords(int xo, int yo) {
		int x = map.xPos;
		int y = map.yPos;

		if (map.orient == 0) {
			x -= xo;
			y -= yo;

		} else if (map.orient == 1) {
			x -= yo;
			y += xo;

		} else if (map.orient == 2) {
			x += xo;
			y += yo;

		} else if (map.orient == 3) {
			x += yo;
			y -= xo;
		}
		
		coords.x = x;
		coords.y = y;
	}
	
	public boolean isExplored(int dx, int dy) {
		if (map.orient == 0)
			return map.isSemiExplored(coords.x + dx, coords.y + dy);

		if (map.orient == 1)
			return map.isSemiExplored(coords.x - dy, coords.y + dx);

		if (map.orient == 2)
			return map.isSemiExplored(coords.x - dx, coords.y - dy);

		return map.isSemiExplored(coords.x + dy, coords.y - dx);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // paint background

		if (!editMode)
			map = Game.map;

		if (image == null)
			return;

		int width = getWidth();
		int height = getHeight();
		int size = symbolSize - 1;
		int wallSymbolOffset = (symbolSize / 2) - 7;

		int xOffset = width / 2 - symbolSize / 2;
		int xo = 0;
		while (xOffset > 0) {
			xOffset -= size;
			xo++;
		}

		int yOffset = height / 2 - symbolSize / 2;
		int yo = 0;
		while (yOffset > 0) {
			yOffset -= size;
			yo++;
		}

		int xEnd = (width - xOffset + size) / size;
		int yEnd = (height - yOffset + size) / size;

		// draw the cells but not the walls/doors
		for (int y = 0; y < yEnd; y++) {
			for (int x = 0; x < xEnd; x++) {
				int sx = xOffset + x * size;
				int sy = yOffset + y * size;
				getCoords(xo - x, yo - y);

				if (map.isExplored(coords.x, coords.y) || editMode)
					draw(sx, sy, map.get(coords.x, coords.y), g);
				else
					drawUnexplored(coords.x, coords.y, sx, sy, g);

				if ((counter & 1) == 0) {
					if (coords.x == map.xPos && coords.y == map.yPos) {
						int symbol = Symbols.CROSSHAIR;
						if (!editMode)
							symbol = Symbols.FACING_NORTH + ((map.facing + map.orient) & 3);

						draw(g, sx, sy, symbol);
					}
				}
			}
		}

		// draw in horizontal walls on map
		for (int x = 0; x < xEnd; x++) {
			getCoords(xo - x, yo);

			boolean e1, e2;
			if (map.orient == 0) {
				e1 = isExplored(0, -1);

			} else if (map.orient == 1) {
				e1 = isExplored(1, 0);

			} else if (map.orient == 2) {
				coords.y++;
				e1 = isExplored(0, 0);

			} else {
				coords.x++;
				e1 = isExplored(0, 0);
			}

			int v;
			for (int y = 0; y < yEnd; y++) {
				if ((map.orient & 1) == 0) {
					if (coords.y == map.height)
						v = (map.get(coords.x, coords.y - 1) >> 27) & 3;
					else
						v = map.get(coords.x, coords.y) & 3;

				} else {
					if (coords.x == map.width)
						v = (map.get(coords.x - 1, coords.y) >> 29) & 3;
					else
						v = (map.get(coords.x, coords.y) >> 2) & 3;
				}

				if (map.orient == 2)
					e2 = isExplored(0, 1);
				else if (map.orient == 3)
					e2 = isExplored(0, -1);
				else
					e2 = isExplored(0, 0);

				if (v != 0 && (editMode || e1 || e2)) {
					int xx = symbolSize + 1;
					int yy = (v - 1) * 5 + wallSymbolOffset;
					int sx = xOffset + x * size;
					int sy = yOffset + y * size;
					g.drawImage(image, sx, sy - 2, sx + symbolSize, sy + 3, xx, yy, xx + symbolSize, yy + 5, null);
				}

				if (map.orient == 0)
					coords.y++;
				else if (map.orient == 1)
					coords.x++;
				else if (map.orient == 2)
					coords.y--;
				else if (map.orient == 3)
					coords.x--;

				e1 = e2;
			}
		}

		// draw in vertical walls on map
		for (int y = 0; y < yEnd; y++) {
			getCoords(xo, yo - y);

			boolean e1, e2;
			if (map.orient == 0) {
				e1 = isExplored(-1, 0);

			} else if (map.orient == 1) {
				e1 = isExplored(0, 0);
				coords.y++;

			} else if (map.orient == 2) {
				e1 = isExplored(0, 0);
				coords.x++;

			} else {
				e1 = isExplored(1, 0);
			}

			int v;
			for (int x = 0; x < xEnd; x++) {
				if ((map.orient & 1) == 0) {
					if (coords.x == map.width)
						v = (map.get(coords.x - 1, coords.y) >> 29) & 3;
					else
						v = (map.get(coords.x, coords.y) >> 2) & 3;

				} else {
					if (coords.y == map.height)
						v = (map.get(coords.x, coords.y - 1) >> 27) & 3;
					else
						v = map.get(coords.x, coords.y) & 3;
				}

				if (map.orient == 1)
					e2 = isExplored(-1, 0);
				else if (map.orient == 2)
					e2 = isExplored(1, 0);
				else
					e2 = isExplored(0, 0);

				if (v != 0 && (editMode || e1 || e2)) {
					int xx = (symbolSize + 1) * 2 + (v - 1) * 5 + wallSymbolOffset;
					int sx = xOffset + x * size;
					int sy = yOffset + y * size;
					g.drawImage(image, sx - 2, sy, sx + 3, sy + symbolSize, xx, 0, xx + 5, symbolSize, null);
				}

				if (map.orient == 0)
					coords.x++;
				else if (map.orient == 1)
					coords.y--;
				else if (map.orient == 2)
					coords.x--;
				else if (map.orient == 3)
					coords.y++;

				e1 = e2;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		this.requestFocusInWindow();
		if (editMode) {
			int width = getWidth();
			int height = getHeight();
			int size = symbolSize - 1;

			int xOffset = width / 2 - symbolSize / 2;
			map.xPos += event.getX() / size - xOffset / size;
			int yOffset = height / 2 - symbolSize / 2;
			map.yPos += event.getY() / size - yOffset / size;
			repaint();
			if (editor != null)
				editor.update();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	public BufferedImage subImage(int index) {
		int sx = (index % 10) * (symbolSize + 1);
		int sy = (index / 10) * (symbolSize + 1);
		if (sy >= image.getHeight())
			return null;

		BufferedImage newImage = new BufferedImage(symbolSize, symbolSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, symbolSize, symbolSize, 0, 0, symbolSize, symbolSize, null);
		g.drawImage(image, 0, 0, symbolSize, symbolSize, sx, sy, sx + symbolSize, sy + symbolSize, null);
		g.dispose();
		return newImage;
	}

	public static final int NORTH_BIT = 1;
	public static final int EAST_BIT = 2;
	public static final int SOUTH_BIT = 4;
	public static final int WEST_BIT = 8;
	public static final int NE_BIT = 16;
	public static final int SE_BIT = 32;
	public static final int SW_BIT = 64;
	public static final int NW_BIT = 128;

	public static int simplifyIndex(int index) {
		if ((index & NORTH_BIT) != 0)
			index &= ~(NW_BIT | NE_BIT);
		if ((index & EAST_BIT) != 0)
			index &= ~(NE_BIT | SE_BIT);
		if ((index & SOUTH_BIT) != 0)
			index &= ~(SW_BIT | SE_BIT);
		if ((index & WEST_BIT) != 0)
			index &= ~(NW_BIT | SW_BIT);
		
		return index;
	}
	
	public static int translateIndex(int index) {
		switch (index) {
		case 0: return 118;
		case 1: return 108;
		case 2: return 119;
		case 3: return 109;
		case 4: return 128;
		case 5: return 87;
		case 6: return 129;
		case 7: return 111;
		case 8: return 117;
		case 9: return 107;

		case 10: return 97;
		case 11: return 122;
		case 12: return 127;
		case 13: return 113;
		case 14: return 102;
		case 15: return 112;
		case 16: return 124;
		case 20: return 83;
		case 24: return 96;
		case 28: return 81;

		case 32: return 104;
		case 33: return 93;
		case 40: return 86;
		case 41: return 91;
		case 48: return 114;
		case 56: return 99;
		case 64: return 106;
		case 65: return 94;
		case 66: return 85;
		case 67: return 90;

		case 80: return 82;
		case 96: return 105;
		case 97: return 98;
		case 112: return 101;
		case 128: return 126;
		case 130: return 95;
		case 132: return 84;
		case 134: return 80;
		case 144: return 125;
		case 148: return 89;

		case 160: return 92;
		case 176: return 121;
		case 192: return 116;
		case 194: return 88;
		case 208: return 123;
		case 224: return 103;
		case 240: return 115;
		}
		
		return 120;
	}
	
	public void dumpCombos() {
		boolean[] valid = new boolean[256];

		for (int i=0; i<256; i++)
			valid[simplifyIndex(i)] = true;
		
		for (int i=0; i<256; i++)
			if (valid[i])
				System.out.println("valid " + i);
	}
}
