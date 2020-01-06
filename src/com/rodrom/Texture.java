package com.rodrom;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	public String filename;
	public BufferedImage texture;
	public int[] texels;

	static public Texture load(String filename) {
		Texture tex = null;
		BufferedImage image = null;

		try {
			BufferedImage temp = ImageIO.read(new File(filename));
			image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.drawImage(temp, 0, 0, null);
			g.dispose();

			tex = new Texture();
			tex.filename = filename;
			tex.texture = image;
			tex.texels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			tex.checkValid();

		} catch (IOException e) {
			tex = null;
			e.printStackTrace();
		}

		return tex;
	}

	public void checkValid() {
		int width = texture.getWidth();
		int height = texture.getHeight();

		if ((0x7fffffff & (width - 1)) + 1 != width)
			System.out.println("Texture " + filename + " width is " + width + " but should be a power of 2");
		if ((0x7fffffff & (height - 1)) + 1 != height)
			System.out.println("Texture " + filename + " height is " + height + " but should be a power of 2");
	}
}
