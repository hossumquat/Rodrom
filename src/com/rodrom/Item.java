package com.rodrom;

import java.io.IOException;

public class Item {
	public ItemBase base;
	public int quantity;
	public int quality;

	public void save() throws IOException {
	}
	
	static public Item load() throws IOException {
		Item item = new Item();
		return item;
	}
}
