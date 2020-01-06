package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Guild {
	public String name;

	public int reqStrength;
	public int reqIntelligence;
	public int reqWisdom;
	public int reqConstitution;
	public int reqCharisma;
	public int reqDexterity;

	public boolean maleOnly, femaleOnly;
	public int alignments; // allowed alignments

	public int perception;
	public int fighting; // melee
	public int thieving;
	public int backstabbing;
	public int criticalHitting;
	public int swings;

	public int monsterQuestChance; // as a percentage (0-100)
	public int itemQuestChance; // as a percentage (0-100)

	public double experienceReqScaler;
	public double hitPointsScaler;

	// spells available
	// rank titles

	public ArrayList<Race> allowedRaces = new ArrayList<Race>();

	static public ArrayList<Guild> list = new ArrayList<Guild>();

	public Guild(String name) {
		this.name = name;

		// set some reasonable defaults to start new guild with
		alignments = 7;
		experienceReqScaler = 1.0;
		hitPointsScaler = 1.0;
	}

	static public Guild get(String name) {
		for (Guild g : list) {
			if (g.name.equals(name))
				return g;
		}

		return null;
	}

	static public int add(String name) {
		int index = 0;

		for (Guild guild : list) {
			if (guild.name.compareTo(name) > 0)
				break;

			index++;
		}

		if (index < list.size())
			list.add(index, new Guild(name));
		else
			list.add(new Guild(name));

		return index;
	}

	static public void loadList(DataInputStream in) throws IOException {
		boolean invalid = false;

		list.clear();

		int num = in.read();
		list.ensureCapacity(num);

		for (int i=0; i<num; i++) {
			Guild guild = list.get(add(in.readUTF()));

			guild.reqStrength = in.readInt();
			guild.reqIntelligence = in.readInt();
			guild.reqWisdom = in.readInt();
			guild.reqConstitution = in.readInt();
			guild.reqCharisma = in.readInt();
			guild.reqDexterity = in.readInt();
			
			int value = in.read();
			if ((value & 0x80) != 0)
				guild.maleOnly = true;
			if ((value & 0x40) != 0)
				guild.femaleOnly = true;
			
			guild.alignments = value & 7;

			int total = in.read();
			for (int j=0; j<total; j++) {
				String str = in.readUTF();
				Race race = Race.find(str);
				if (race == null)
					throw new IOException("Unknown race " + str);
				
				guild.allowedRaces.add(race);
			}
			
			guild.perception = in.readInt();
			guild.fighting = in.readInt();
			guild.thieving = in.readInt();
			guild.backstabbing = in.readInt();
			guild.criticalHitting = in.readInt();
			guild.swings = in.readInt();

			guild.monsterQuestChance = in.readInt();
			guild.itemQuestChance = in.readInt();

			guild.experienceReqScaler = in.readDouble();
			guild.hitPointsScaler = in.readDouble();
			
			if ((guild.alignments & ~7) != 0)
				invalid = true;
			
			if (guild.perception < 0 || guild.fighting < 0 || guild.thieving < 0)
				invalid = true;
			
			if (guild.backstabbing < 0 || guild.criticalHitting < 0 || guild.swings < 0)
				invalid = true;
			
			if (guild.monsterQuestChance < 0 || guild.monsterQuestChance > 100)
				invalid = true;

			if (guild.itemQuestChance < 0 || guild.itemQuestChance > 100)
				invalid = true;
			
			if (guild.experienceReqScaler < 0.1 || guild.experienceReqScaler > 10.0)
				invalid = true;

			if (guild.hitPointsScaler < 0.1 || guild.hitPointsScaler > 10.0)
				invalid = true;

			if (invalid)
				throw new IOException("Race \"" + guild.name + "\" data is corrupt");
		}
	}

	static public void saveList(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (Guild guild : list) {
			out.writeUTF(guild.name);

			out.writeInt(guild.reqStrength);
			out.writeInt(guild.reqIntelligence);
			out.writeInt(guild.reqWisdom);
			out.writeInt(guild.reqConstitution);
			out.writeInt(guild.reqCharisma);
			out.writeInt(guild.reqDexterity);

			int value = guild.alignments;
			if (guild.maleOnly)
				value |= 0x80;
			if (guild.femaleOnly)
				value |= 0x40;

			out.write(value);
			out.write(guild.allowedRaces.size());
			for (Race race : guild.allowedRaces)
				out.writeUTF(race.name);

			out.writeInt(guild.perception);
			out.writeInt(guild.fighting);
			out.writeInt(guild.thieving);
			out.writeInt(guild.backstabbing);
			out.writeInt(guild.criticalHitting);
			out.writeInt(guild.swings);

			out.writeInt(guild.monsterQuestChance);
			out.writeInt(guild.itemQuestChance);

			out.writeDouble(guild.experienceReqScaler);
			out.writeDouble(guild.hitPointsScaler);
		}
	}
}

// Adventurers Guilds
// Warriors Guild
// Assassins Guild
// Thieves Guild
// Explorers Guild
// Healers Guild
// Hunters Guild
// Clerics Brotherhood
// Paladins Templar
// Black Knights Order
// Charmers Sisterhood
