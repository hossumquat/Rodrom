package com.rodrom.dll;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Reader extends DataInputStream {
	public int position = 0;
	
	public Reader(InputStream in) {
		super(in);
	}

	public int int16() throws IOException {
		int a = read();
		int b = read();
		
		if (a < 0 || b < 0)
			return 0;

		position += 2;
		return (b << 8) | a;
	}

	public long skip(long n) throws IOException {
		n = super.skip(n);
		position += n;
		return n;
	}

	public void seek(int n) throws IOException {
		int len = 208 + n - position;
		if (len < 0)
			throw new IOException();

		if (len > 0)
			skip(len);
	}

	public String readName(int id) throws IOException {
		seek(id);

		int len = read();
		if (len < 0)
			return null;
		
		position++;
		char[] data = new char[len];
		for (int i=0; i<len; i++) {
			int b = read();
			if (b < 0)
				break;
			
			position++;
			data[i] = (char) b;
		}

		return new String(data);
	}
}
