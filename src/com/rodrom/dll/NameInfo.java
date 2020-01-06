package com.rodrom.dll;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.imageio.ImageIO;

public class NameInfo {
	public int offset;
	public int length;
	public int flags;
	public int id;
	public int handle;
	public int usage;

	public String name;
	public IconInfo iconInfo;
	public BufferedImage image;

	public NameInfo() {
	}
	
	public NameInfo(Reader in, int shift) throws IOException {
		read(in, shift);
	}
	
	public void read(Reader in, int shift) throws IOException {
		offset = in.int16() << shift;
		length = in.int16() << shift;
		flags = in.int16();
		id = in.int16();
		handle = in.int16();
		usage = in.int16();
	}

	public void readName(Reader in) throws IOException {
		name = in.readName(id);
	}
	
	public void readIcons(Reader in) throws IOException {
		in.seek(offset);
		iconInfo = new IconInfo(in);
	}
	
	public void readImage() {
		try (FileChannel fch = FileChannel.open(Paths.get(LoadIcons.fileName), StandardOpenOption.READ)) {
		    InputStream is = Channels.newInputStream(fch.position(offset));
		    image = ImageIO.read(is);
		    System.out.println(name + ": " + image.getWidth() + "," + image.getHeight());

		} catch (Exception e) {
			System.out.println("Failed to load " + name);
			e.printStackTrace();
		}
	}
}
