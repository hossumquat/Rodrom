package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

// A mapping set with DamageType for key and an int for a value.  For tracking damage produced or resistances, etc.
//
public class DamageTypeMapping {
	public class Element {
		DamageType type;
		int value;

		public Element(DamageType type, int value) {
			this.type = type;
			this.value = value;
		}
	}

	public ArrayList<Element> list = new ArrayList<Element>();

	private Element find(DamageType type) {
		for (Element e : list) {
			if (e.type == type)
				return e;
		}

		return null;
	}

	public void set(DamageType type, int value) {
		Element e = find(type);
		if (e != null) {
			if (value == 0) {
				if (!list.remove(e))
					System.out.println("Error: DamageType " + e.type.name + " should have been in the list");

				return;
			}

			e.value = value;
			return;
		}

		if (value != 0)
			list.add(e = new Element(type, value));
	}

	public void add(DamageType type, int value) {
		Element e = find(type);
		if (e != null) {
			e.value += value;
			return;
		}

		list.add(e = new Element(type, value));
	}

	public void add(DamageTypeMapping m) {
		for (Element e : m.list) {
			add(e.type, e.value);
		}
	}

	// zero elements rather than actually remove elements, since this is used
	// prior to tallying up so we'll just have to allocate them again anyway.
	public void clear() {
		for (Element e : list)
			e.value = 0;
	}

	public int get(DamageType type) {
		Element e = find(type);
		if (e == null)
			return 0;

		return e.value;
	}

	public void load(DataInputStream in) throws IOException {
		list.clear();

		int num = in.read();
		list.ensureCapacity(num);

		for (int i = 0; i < num; i++) {
			String text = in.readUTF();
			DamageType dt = DamageType.find(text);
			if (dt == null)
				throw new IOException("Unknown damage type " + text);

			int value = in.readInt();
			add(dt, value);
		}
	}

	public void save(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (Element e : list) {
			out.writeUTF(e.type.name);
			out.writeInt(e.value);
		}
	}
}
