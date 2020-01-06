package com.rodrom;

public class ItemSpecial {
	final static public int LEVITATE = 0;
	final static public int SPEED = 1; // increase attack and defense
	final static public int PROTECTION = 2; // reduces damage received
	final static public int INVISIBLE = 3;
	final static public int SEE_INVISIBLE = 4;
	final static public int CRITICAL_HIT = 5;
	final static public int BACKSTAB = 6;
	final static public int BONUS_VS = 7; // bonus damage vs a monster type
	final static public int PEACE = 8; // monsters more likely to be peaceful
	final static public int STRIFE = 9; // monsters more likely to be aggressive
	final static public int REGENERATION = 10;
	final static public int PERCEPTION = 11;  // adjust perception

	public int type;
	public int adjust;
	
	public ItemSpecial(int type, int adjust) {
		this.type = type;
		this.adjust = adjust;
	}
	
	public String toString() {
		switch (type) {
		case LEVITATE: return "Levitation";
		case SPEED: return "Speed " + adjuster();
		case PROTECTION: return "Protection " + adjuster();
		case INVISIBLE: return "Invisibility";
		case SEE_INVISIBLE: return "See Invisible";
		case CRITICAL_HIT: return "Critical Hit " + adjuster();
		case BACKSTAB: return "Backstab " + adjuster();
		case BONUS_VS: return adjuster() + " Bonus vs " + monsterType();
		case PEACE: return "Peace " + adjuster();
		case STRIFE: return "Strife " + adjuster();
		case REGENERATION: return "Regeneration " + adjuster();
		case PERCEPTION: return "Perception " + adjuster();
		}
		
		return "?";
	}
	
	public String monsterType() {
		return "Dragon";
	}
	
	public String adjuster() {
		if (adjust > 0)
			return "+" + adjust;
		
		return String.valueOf(adjust);
	}
}

// Tetrahedron: 4 sides
// Cube: 6 sides
// Octohedron: 8 sides
// Dodecahedron: 12 sides
// Icosahedron: 20 sides
// Sphere: Inf sides