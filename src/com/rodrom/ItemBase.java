package com.rodrom;

public class ItemBase {
	public class Wieldables {
		String type;
		int allowed;  // number of items of this type one can wield at once
		
		Wieldables(String type, int allowed) {
			this.type = type;
			this.allowed = allowed;
		}
	}
	
	public Wieldables wieldables[] = {
		new Wieldables("Weapon", 2),
		new Wieldables("Helmet", 1),
	};
	
	public String name;
	public String type;
	public String subType;
	
	public int attack;
	public int defense;
	public boolean restricted;  // guild (or class in mordor) restricted
	public int swings;
	public int damage;  // power in mordor
	public boolean twoHanded;
	public boolean dualWield;  // can effectively dual wield
	public boolean cursed;
	public int weight;  // 0-1 light, 2-5 medium, 6+ heavy

	public int level;
	public int chance;  // chance to find
	public int basePrice;
	
	// spell to cast
	public int spellLevel;  // what spell level item casts at, 0 = player level, <0 = player level+
	public int charges, maxCharges;
	public int rechargeRate;
	
	public int requiredStrength;
	public int requiredIntelligence;
	public int requiredWisdom;
	public int requiredConstitution;
	public int requiredCharisma;
	public int requiredDexterity;

	public int modifyStrength;
	public int modifyIntelligence;
	public int modifyWisdom;
	public int modifyConstitution;
	public int modifyCharisma;
	public int modifyDexterity;

	public DamageTypeMapping damageTypes;
	public DamageTypeMapping resistances;
}
