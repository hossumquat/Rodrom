package com.rodrom;

// TODO: random map generation: First divide map area up into biomes.  Then generate features based on the biome for
// each area.  Maybe biomes could span more than just one level?

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class MapArray {
	private static final int MAX_MAPS = 999;
	
	public ArrayList<Map> maps = new ArrayList<Map>();
	public int level = 0;
	public String name;

	public void add(Map map) {
		if (level >= maps.size()) {
			maps.add(map);
			level = maps.size() - 1;
			return;
		}
		
		if (level < 0)
			level = 0;

		maps.add(level, map);
	}
	
	public void add() {
		maps.add(level, new Map());
	}

	public Map get(int index) {
		return maps.get(index);
	}
	
	public Map get() {
		return maps.get(level);
	}

	public void remove() {
		if (level < 0 || level >= maps.size())
			return;

		maps.remove(level);
		if (level == maps.size())
			level--;
	}
	
	public void removeAll() {
		maps.clear();
		level = 0;
	}

	public int size() {
		return maps.size();
	}

	public MapArray newCopy() {
		MapArray ma = new MapArray();
		ma.maps.ensureCapacity(maps.size());
		for (int i=0; i<maps.size(); i++)
			ma.maps.add(new Map().copy(get(i)));

		return ma;
	}

	public void save() {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(name))) {
			out.writeInt(maps.size());
			for (Map map : maps)
				map.save(out);

		} catch (IOException e) {
			String text = "Failed to save " + e.getMessage();
			JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean load(String name) {
		maps.clear();
		this.name = name;

		try (DataInputStream in = new DataInputStream(new FileInputStream(name))) {
			int size = in.readInt();
			if (size < 1 || size > MAX_MAPS)
				throw new IOException("Invalid map file");
			
			maps.ensureCapacity(size);
			for (int i=0; i<size; i++)
				maps.add(new Map().load(in));

		} catch (IOException e) {
			String message = e.getMessage();
			if (!message.substring(0, name.length()).equals(name))
				message = name + " (" + message + ")";
			
			String text = "Failed to load " + message;
			JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
			maps.clear();
			return false;
		}
		
		return true;
	}
}
