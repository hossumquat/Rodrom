package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Character {
	final static public int MAX_ITEMS = 40;

	public String name;
	public int level;
	public long experience;
	public int age;  // measured in days
	public int hitPoints;
	public int hitMax;
	public int spellPoints;
	public int spellMax;
	public int spellLevel;
	transient public int attack;
	transient public int defense;
	public long gold;
	public long bankedGold;

	public int strength;
	public int intelligence;
	public int wisdom;
	public int constitution;
	public int charisma;
	public int dexterity;

	public Race race;
	public boolean female;
	public GuildMapping guilds;
	public Guild currentGuild;
	public int alignment;

	transient public DamageTypeMapping resistances;
	
	public ArrayList<Item> items = new ArrayList<Item>();

	static public ArrayList<Character> list = new ArrayList<Character>();

	public Character() {
	}
	
	public Character(String name) {
		this.name = name;
	}

	static public Character find(String name) {
		for (Character ch : list) {
			if (ch.name.equals(name))
				return ch;
		}
		
		return null;
	}
	
	// Generate derived data, such as resistances, etc. from primary data.
	// Derived data are flagged transient in their declaration.
	public void deriveData() {
		resistances.clear();
		resistances.add(race.resistances);
	}

	public void load(DataInputStream in) throws IOException {
		boolean invalid = false;

		name = in.readUTF();
		in.read();  // reserved for password info
		level = in.readInt();
		experience = in.readLong();
		age = in.readInt();
		hitPoints = in.readInt();
		hitMax = in.readInt();
		spellPoints = in.readInt();
		spellMax = in.readInt();
		gold = in.readLong();
		bankedGold = in.readLong();

		strength = in.read();
		intelligence = in.read();
		wisdom = in.read();
		constitution = in.read();
		charisma = in.read();
		dexterity = in.read();

		String raceName = in.readUTF();
		race = Race.find(raceName);
		female = in.readBoolean();
		guilds.load(in);

		String guildName = in.readUTF();
		currentGuild = Guild.get(guildName);

		alignment = in.read();

		items.clear();

		int num = in.read();
		items.ensureCapacity(num);

		for (int i=0; i<num; i++)
			items.add(Item.load());

		if (race == null)
			throw new IOException("Unknown race " + raceName + " for character " + name);
		
		if (currentGuild == null)
			throw new IOException("Unknown guild " + guildName + " for character " + name);
		
		if (level < 1 || experience < 0 || age < Game.MINIMUM_AGE || age >= race.maxAge)
			invalid = true;
		
		if (hitPoints > hitMax || hitPoints < 0 || hitMax < 1)
			invalid = true;

		if (spellPoints > spellMax || spellPoints < 0 || spellMax < 1)
			invalid = true;
		
		if (gold < 0 || bankedGold < 0)
			invalid = true;
		
		if (strength < 1 || intelligence < 1 || wisdom < 1 || constitution < 1 || charisma < 1 || dexterity < 1)
			invalid = true;

		if (alignment != 1 || alignment != 2 || alignment != 4)
			invalid = true;

		if (invalid)
			throw new IOException("Character \"" + name + "\" data is corrupt");

		deriveData();
	}

	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(name);
		out.write(0);  // reserved for password info
		out.writeInt(level);
		out.writeLong(experience);
		out.writeInt(age);
		out.writeInt(hitPoints);
		out.writeInt(hitMax);
		out.writeInt(spellPoints);
		out.writeInt(spellMax);
		out.writeLong(gold);
		out.writeLong(bankedGold);

		out.write(strength);
		out.write(intelligence);
		out.write(wisdom);
		out.write(constitution);
		out.write(charisma);
		out.write(dexterity);

		out.writeUTF(race.name);
		out.writeBoolean(female);
		guilds.save(out);
		out.writeUTF(currentGuild.name);
		out.write(alignment);

		out.write(items.size());
		for (Item item : items)
			item.save();
	}

	static public void loadList(DataInputStream in) throws IOException {
		list.clear();

		int num = in.read();
		list.ensureCapacity(num);

		for (int i=0; i<num; i++) {
			Character character = new Character();
			character.load(in);
			list.add(character);
		}
	}

	static public void saveList(DataOutputStream out) throws IOException {
		out.write(list.size());
		for (Character character : list)
			character.save(out);
	}
}
