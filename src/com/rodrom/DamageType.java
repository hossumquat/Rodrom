package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

// A class to represent valid damage types, used to associate resistances as well
//
public class DamageType {
	static public ArrayList<DamageType> list = new ArrayList<DamageType>();

	public String name;

	public DamageType(String name) {
		this.name = name;
	}

	static public DamageType get(int index) {
		return list.get(index);
	}

	static public DamageType find(String name) {
		for (DamageType dt : list) {
			if (dt.name.equals(name))
				return dt;
		}

		return null;
	}

	static public int add(String name) {
		int index = 0;

		for (DamageType dt : list) {
			if (dt.name.compareTo(name) > 0)
				break;

			index++;
		}

		if (index < list.size())
			list.add(index, new DamageType(name));
		else
			list.add(new DamageType(name));

		return index;
	}

	public void load(DataInputStream in) throws IOException {
		name = in.readUTF();
	}

	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(name);
	}

	static public void loadList(DataInputStream in) throws IOException {
		list.clear();

		int num = in.read();
		list.ensureCapacity(num);

		for (int i=0; i<num; i++)
			add(in.readUTF());
	}

	static public void saveList(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (DamageType dt : list)
			out.writeUTF(dt.name);
	}
}
