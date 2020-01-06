package com.rodrom;

public class CreatureBase {
	final static public String[] creatureTypes = {
			"Humanoid", "Animal", "Beast", "Reptile", "Dragon", "Insect", "Water-Dweller",
			"Slime", "Demon", "Devil", "Elemental", "Undead", "Mythical", "Lycanthrope"
	};

	String name;
	String plural;
	int size;
	String type;  // humanoid, animal, slime, demon, devil, elemental, reptile, dragon, insect, undead, water-dweller, giant, mythical, lycanthrope, thief, mage, warrior, indigini
	int level;
	int maxHitPoints;
	int attack;
	int defense;
	int damage;
	int image;
	int chance;  // chance of encountering
	int groups;
	int amount;
	// Generally seen as part of # groups of monsters with # in each group.
	// alignment?  Thinking no..
	int loyalty;
	
	int strength;
	int intelligence;
	int wisdom;
	int constitution;
	int charisma;
	int dexterity;
	
	DamageTypeMapping resistances;
	// attack types, including message texts i.e. spits, bites, claws
	// special abilities/skills
	// see invisible
	// invisible
	// resistant to all magic
	// charm resistant
	// weapon resistant
	// complete weapon resistant
	// poison
	// disease
	// paralyze
	// breath fire
	// breath cold
	// acid
	// electrocute
	// drain
	// stone
	// age
	// critical hit
	// backstab
	// destroy item
	// steal

	int minimumGold;
	int maximumGold;
	int experienceScaler;
	int boxChance;
	int chestChance;
	// items/loot lists & chances
}

// Basilisk
// Cockatrice
// Chimera
// Gryphon
// Hippogriff
// Barking Beast
// Shadow Hound
// Banshee
// Gorgon
// Margon
// Cyclops
// Scylla
// Nephilim
// Etten
// Yangan
// Wyvern
// Kelpie
// Kitsune
// Unicorn
// Hydra
// Sidhe
// Grungling
// Stone Nymph
// Kinghorn
// Copper Serpent
// Gold Speckled Serpent
// Silver Striped Snake
// Stone Snake
// Viper
// Cobra
// Blue Snake
// Slitherling
// Poochling
