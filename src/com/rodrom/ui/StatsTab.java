package com.rodrom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import com.rodrom.Character;
import com.rodrom.DamageType;
import com.rodrom.GuildMapping;

import net.miginfocom.swing.MigLayout;

public class StatsTab extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;

	public JLabel name, line2, curGuildInfo;
	public JLabel strength, intelligence, wisdom, constitution, charisma, dexterity;
	public JLabel age, hitPoints, spellPoints, spellLevel, attackDefense, statsAD, guildAD, itemsAD, experience, gold, bank;
	public JLabel[] resistances;
	public JPanel guildsPanel;

	public Font font, bold;

	// set all the swing components to show data for the given character
	public void setInfo(Character character) {
		String gender = character.female ? "Female" : "Male";
		String race = character.race.name;
		String alignment = Utility.alignmentText(character.alignment);
		
		name.setText(character.name);
		line2.setText(gender + " " + race + " (" + alignment + ")");
//		curGuildInfo.setText(text);

		strength.setText(String.valueOf(character.strength));
		intelligence.setText(String.valueOf(character.intelligence));
		wisdom.setText(String.valueOf(character.wisdom));
		constitution.setText(String.valueOf(character.constitution));
		charisma.setText(String.valueOf(character.charisma));
		dexterity.setText(String.valueOf(character.dexterity));

		age.setText(String.valueOf(character.age));
		hitPoints.setText(character.hitPoints + " / " + character.hitMax);
		spellPoints.setText(character.spellPoints + " / " + character.spellMax);
		spellLevel.setText(String.valueOf(character.spellLevel));
		attackDefense.setText(character.attack + " / " + character.defense);
//		statsAD.setText(String.valueOf(character.age));
//		guildAD.setText(String.valueOf(character.age));
//		itemsAD.setText(String.valueOf(character.age));
		experience.setText(String.valueOf(character.experience));
		gold.setText(String.valueOf(character.gold));
		bank.setText(String.valueOf(character.bankedGold));
		
		synchronized(getTreeLock()) {
			int i = 0, total = guildsPanel.getComponentCount() - 1;
			JLabel label;

			for (GuildMapping.Element e : character.guilds.list) {
				if (i < total)
					label = (JLabel) guildsPanel.getComponent(i + 1);
				else
					guildsPanel.add(label = new JLabel(), "wrap");

				label.setText("        " + e.guild.name + " (" + e.value + ")");
				i++;
			}

			while (i < total) {
				guildsPanel.remove(i);
				total--;
			}
		}

		int total = DamageType.list.size();
		for (int i=0; i<total; i++) {
			int value = character.resistances.get(DamageType.get(i)); 
			resistances[i].setText((value == 0) ? " - " : String.valueOf(value));
		}
	}
	
	public StatsTab() {
		super(false);
		setMaximumSize(new Dimension(350, Short.MAX_VALUE));

		setLayout(new MigLayout());

		name = new JLabel("Throg");

		font = name.getFont().deriveFont(Font.PLAIN);
		bold = font.deriveFont(Font.BOLD);

		name.setFont(bold.deriveFont(bold.getSize2D() + 2.0f));
		name.setForeground(Color.blue);
		add(name, "align center, wrap");

		line2 = boldLabel("Male Giant (Neutral)");
		line2.setToolTipText("Sex, race and (alignment)");
		add(line2, "align center, wrap");

		curGuildInfo = new JLabel("Vagrant (1) in Nomads Guild");
		curGuildInfo.setToolTipText("Current Guild, rank and level");
		add(curGuildInfo, "align center, wrap");

//		position = new JLabel("You are alone");
//		position.setHorizontalAlignment(SwingConstants.CENTER);
		addSeperator();

//		JPanel positionPanel = new JPanel(new GridLayout(1, 1));
//		positionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//		positionPanel.add(position);
//		add(positionPanel, "width :100%:, wrap");

		add(createStats1(), "width :100%:, wrap");
		addSeperator();
		add(createStats2(), "width :100%:, wrap");
		addSeperator();
		add(createStats3(), "width :100%:, wrap");
		addSeperator();
		add(createStats4(), "width :100%:, wrap");
	}

	public void addSeperator() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createRaisedBevelBorder());
		add(p, "width :100%:, height 4:4:4, wrap");
	}

	public JPanel createStats1() {
		JPanel panel = new JPanel(new MigLayout("fillx"));

		panel.add(boldLabel("Strength"));
		panel.add(new JLabel("18"), "align right, wrap");

		panel.add(boldLabel("Intelligence"));
		panel.add(new JLabel("16"), "align right, wrap");

		panel.add(boldLabel("Wisdom"));
		panel.add(new JLabel("14"), "align right, wrap");

		panel.add(boldLabel("Constitution"));
		panel.add(new JLabel("16"), "align right, wrap");

		panel.add(boldLabel("Charisma"));
		panel.add(new JLabel("3"), "align right, wrap");

		panel.add(boldLabel("Dexterity"));
		panel.add(new JLabel("12"), "align right, wrap");
		
		return panel;
	}

	public JPanel createStats2() {
		JPanel panel = new JPanel(new MigLayout("fillx"));

		panel.add(boldLabel("Age"));
		panel.add(new JLabel("16"), "align right, wrap");

		panel.add(boldLabel("Hit Points"));
		panel.add(new JLabel("100 / 120"), "align right, wrap");

		panel.add(boldLabel("Spell Points"));
		panel.add(new JLabel("215 / 215"), "align right, wrap");

		panel.add(boldLabel("Spell Level"));
		panel.add(new JLabel("7"), "align right, wrap");

		panel.add(boldLabel("Attack / Defense"));
		panel.add(new JLabel("50 / 59"), "align right, wrap");

		panel.add(new JLabel("        From Stats"));
		panel.add(grayLabel("10 / 10        "), "align right, wrap");

		panel.add(new JLabel("        From Guilds"));
		panel.add(grayLabel("31 / 31        "), "align right, wrap");

		panel.add(new JLabel("        From Items"));
		panel.add(grayLabel("9 / 18        "), "align right, wrap");

		panel.add(boldLabel("Experience"));
		panel.add(new JLabel("19,701"), "align right, wrap");

		panel.add(boldLabel("Gold"));
		panel.add(new JLabel("0"), "align right, wrap");

		panel.add(boldLabel("In Bank"));
		panel.add(new JLabel("141,920"), "align right, wrap");

		return panel;
	}

//	public JLabel 
	public JPanel createStats3() {
		guildsPanel = new JPanel(new MigLayout("fillx"));

		guildsPanel.add(boldLabel("Guild Memberships"), "wrap");
		guildsPanel.add(new JLabel("        Nomad (30)"), "wrap");

		guildsPanel.add(new JLabel("        Warrior (28)"), "wrap");

		guildsPanel.add(new JLabel("        Healer (19)"), "wrap");

		return guildsPanel;
	}

	public JPanel createStats4() {
		int total = DamageType.list.size();
		resistances = new JLabel[total];
		
		JPanel panel = new JPanel(new MigLayout("fillx"));

		panel.add(boldLabel("Resistances"), "wrap");
		
		for (int i=0; i<total; i++) {
			DamageType dt = DamageType.get(i);
			panel.add(new JLabel("        " + dt.name));
			resistances[i] = new JLabel(" - ");
			panel.add(resistances[i], "align right, wrap");
		}

		return panel;
	}
	
	public JLabel boldLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(bold);

		return label;
	}
	
	public JLabel grayLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.GRAY);

		return label;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return 20;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 20;
	}
}
