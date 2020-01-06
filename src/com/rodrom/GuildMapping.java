package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

// A mapping set with Guild for key and an int for a value.  For tracking guild levels for characters
//
public class GuildMapping {
	public class Element {
		public Guild guild;
		public int value;

		public Element(Guild guild, int value) {
			this.guild = guild;
			this.value = value;
		}
	}

	public ArrayList<Element> list = new ArrayList<Element>();

	private Element find(Guild guild) {
		for (Element e : list) {
			if (e.guild == guild)
				return e;
		}

		return null;
	}

	public void set(Guild guild, int value) {
		Element e = find(guild);
		if (e != null) {
			if (value == 0) {
				if (!list.remove(e))
					System.out.println("Error: Guild " + e.guild.name + " should have been in the list");

				return;
			}

			e.value = value;
			return;
		}

		if (value != 0)
			list.add(e = new Element(guild, value));
	}

	public void add(Guild guild, int value) {
		Element e = find(guild);
		if (e != null) {
			e.value += value;
			return;
		}

		list.add(e = new Element(guild, value));
	}

	public int get(Guild guild) {
		Element e = find(guild);
		if (e == null)
			return 0;

		return e.value;
	}

	public void load(DataInputStream in) throws IOException {
		list.clear();
		int size = in.read();
		list.ensureCapacity(size);

		for (int i = 0; i < size; i++) {
			String name = in.readUTF();
			int value = in.readInt();
			if (value < 0)
				throw new IOException("Data is corrupt");

			Guild guild = Guild.get(name);
			if (guild == null)
				throw new IOException("Invalid guild " + name);

			add(guild, value);
		}
	}

	public void save(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (Element e : list) {
			out.writeUTF(e.guild.name);
			out.writeInt(e.value);
		}
	}
}
