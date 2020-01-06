package com.rodrom.dll;

import java.io.IOException;

public class TypeInfo {
	public int typeId;
	public int count;
	public int reserved1;
	public int reserved2;
	
	public String name;

	public NameInfo[] nameInfos;

	public TypeInfo(int typeId, Reader in, int shift) throws IOException {
		this.typeId = typeId;
		count = in.int16();
		reserved1 = in.int16();
		reserved2 = in.int16();
		
		nameInfos = new NameInfo[count];
		for (int i=0; i<count; i++ ) {
			nameInfos[i] = new NameInfo(in, shift);
		}
	}
	
	public void readName(Reader in) throws IOException {
		name = in.readName(typeId);
		for (NameInfo ni : nameInfos) {
			ni.readName(in);
		}
	}
	
	public void readIcons(Reader in) throws IOException {
		for (NameInfo ni : nameInfos) {
			ni.readIcons(in);
		}
	}
	
	public void readImages() {
		for (NameInfo ni : nameInfos) {
			ni.readImage();
		}
	}
}
