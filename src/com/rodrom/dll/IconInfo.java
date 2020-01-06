package com.rodrom.dll;

import java.io.IOException;

public class IconInfo {
	public class IconSubInfo {
		public int width;
		public int height;
		public int colors;
		public int reserved;
		public int planes;
		public int bits;
		public int bytesInRes;
		public int id;
		
	    IconSubInfo(Reader in) throws IOException {
	    	read(in);
	    }
	    
	    public void read(Reader in) throws IOException {
			width = in.read();
			height = in.read();
			colors = in.read();
			reserved = in.read();
			
			planes = in.int16();
			bits = in.int16();
			bytesInRes = in.int16() | (in.int16() << 16);
			id = in.int16();
	    }
	}

	public int reserved;
	public int type;
	public int count;

	public IconSubInfo[] icon; 

	public IconInfo(Reader in) throws IOException {
		read(in);
	}

	public void read(Reader in) throws IOException {
		reserved = in.int16();
		type = in.int16();
		count = in.int16();
		
		icon = new IconSubInfo[count];
		for (int i=0; i<count; i++)
			icon[i] = new IconSubInfo(in);
	}
}
