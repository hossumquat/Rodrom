package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.rodrom.ui.Utility;

public class Race {
	public String name;

	public int minStrength;
	public int maxStrength;
	public int minIntelligence;
	public int maxIntelligence;
	public int minWisdom;
	public int maxWisdom;
	public int minConstitution;
	public int maxConstitution;
	public int minCharisma;
	public int maxCharisma;
	public int minDexterity;
	public int maxDexterity;

	public DamageTypeMapping resistances;
	public int alignments;
	public int size;
	public int bonus;  // bonus points
	public int maxAge;
	public double expScaler;

	static public ArrayList<Race> list = new ArrayList<Race>();

	public Race(String name) {
		this.name = name;
		resistances = new DamageTypeMapping();

		// set some reasonable defaults to start new races with
		minStrength = 5;
		minIntelligence = 5;
		minWisdom = 5;
		minConstitution = 5;
		minCharisma = 5;
		minDexterity = 5;

		maxStrength = 18;
		maxIntelligence = 18;
		maxWisdom = 18;
		maxConstitution = 18;
		maxCharisma = 18;
		maxDexterity = 18;
		
		alignments = 7;
		size = 3;
		maxAge = 120;
		expScaler = 1.0;
	}
	
	static public Race find(String name) {
		for (Race race : list) {
			if (race.name.equals(name))
				return race;
		}

		return null;
	}

	static public int add(String name) {
		int index = 0;

		for (Race race : list) {
			if (race.name.compareTo(name) > 0)
				break;

			index++;
		}

		if (index < list.size())
			list.add(index, new Race(name));
		else
			list.add(new Race(name));

		return index;
	}
	
	static public void loadList(DataInputStream in) throws IOException {
		boolean invalid = false;

		list.clear();

		int num = in.read();
		list.ensureCapacity(num);

		for (int i=0; i<num; i++) {
			Race race = list.get(add(in.readUTF()));

			race.minStrength = in.read();
			race.maxStrength = in.read();
			race.minIntelligence = in.read();
			race.maxIntelligence = in.read();
			race.minWisdom = in.read();
			race.maxWisdom = in.read();
			race.minConstitution = in.read();
			race.maxConstitution = in.read();
			race.minCharisma = in.read();
			race.maxCharisma = in.read();
			race.minDexterity = in.read();
			race.maxDexterity = in.read();
			
			race.resistances.load(in);
			race.alignments = in.read();
			race.size = in.read();
			race.bonus = in.read();
			race.maxAge = in.readInt();
			race.expScaler = in.readDouble();
			
			if ((race.alignments & ~7) != 0 || race.size < 0 || race.size >= Utility.sizeText.length)
				invalid = true;

			if (invalid)
				throw new IOException("Race \"" + race.name + "\" data is corrupt");
		}
	}

	static public void saveList(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (Race race : list) {
			out.writeUTF(race.name);
			out.write(race.minStrength);
			out.write(race.maxStrength);
			out.write(race.minIntelligence);
			out.write(race.maxIntelligence);
			out.write(race.minWisdom);
			out.write(race.maxWisdom);
			out.write(race.minConstitution);
			out.write(race.maxConstitution);
			out.write(race.minCharisma);
			out.write(race.maxCharisma);
			out.write(race.minDexterity);
			out.write(race.maxDexterity);
			race.resistances.save(out);
			out.write(race.alignments);
			out.write(race.size);
			out.write(race.bonus);
			out.writeInt(race.maxAge);
			out.writeDouble(race.expScaler);
		}
	}

	public String toString() {
		return name;
	}
}
