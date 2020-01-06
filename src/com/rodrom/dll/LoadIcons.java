package com.rodrom.dll;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.LinkedList;

public class LoadIcons {
	final static String fileName = "MG11.DLL";

	public static int shift;
	public static LinkedList<TypeInfo> typeInfoList = new LinkedList<TypeInfo> ();
	
	public static void load() {
		Reader in = null;
		
		try {
			in = new Reader(new BufferedInputStream(new FileInputStream(fileName)));
			
			in.skip(208);
			shift = in.int16();
			
			while (true) {
				int typeId = in.int16();
				if (typeId == 0)
					break;
				
				typeInfoList.add(new TypeInfo(typeId, in, shift));
			}
			
			for (TypeInfo ti : typeInfoList) {
				ti.readName(in);
			}

			for (TypeInfo ti : typeInfoList) {
				ti.readImages();
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (typeInfoList != null)
			System.out.println("blah");
	}
	
	public static BufferedImage getImage(int n) {
		TypeInfo ti = typeInfoList.getFirst();
		return ti.nameInfos[n].image;
	}
}
