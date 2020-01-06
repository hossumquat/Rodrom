package com.rodrom.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.rodrom.Guild;
import com.rodrom.MainWindow;
import com.rodrom.Race;
import com.rodrom.Character;
import com.rodrom.Game;

import net.miginfocom.swing.MigLayout;

public class CreateCharacter extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	public JTextField name;
	public JComboBox<Race> raceBox;
	public JComboBox<String> alignmentBox;
	public JRadioButton male;
	public JRadioButton female;

	public JLabel strLabel;
	public JLabel intLabel;
	public JLabel wisLabel;
	public JLabel conLabel;
	public JLabel chrLabel;
	public JLabel dexLabel;
	// show "Strength - [] + (range) where -/+ are buttons.
	public JLabel strRange;
	public JLabel intRange;
	public JLabel wisRange;
	public JLabel conRange;
	public JLabel chrRange;
	public JLabel dexRange;
	public JLabel pointsLeftLabel;

	public JLabel guilds;
	// list of all guilds, gray if unavailable, plain if available but stats are too
	// low
	// bold if stats are high enough to allow joining. Can select a guild in the
	// list
	// to highlight which stats are too low, or highlight race/alignment/sex that is
	// making the guild unavailable.

	public JButton[] buttons = new JButton[12];
	public JButton save, cancel;

	public Race race;
	public int alignment;
	public int strength;
	public int intelligence;
	public int wisdom;
	public int constitution;
	public int charisma;
	public int dexterity;
	public int pointsLeft;

	public CreateCharacter() {
		super(MainWindow.frame, "Create a new character", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout());

		JPanel container = new JPanel(new MigLayout());
		JLabel label = new JLabel("<html><b>Character Name");
		container.add(label, "wrap");
		name = new JTextField(12);
		container.add(name, "gapbottom 10, wrap");

		label = new JLabel("<html><b>Race");
		container.add(label, "wrap");
		raceBox = new JComboBox<Race>(Race.list.toArray(new Race[Race.list.size()]));
		raceBox.addActionListener(this);
		container.add(raceBox, "gapbottom 10, wrap");

		label = new JLabel("<html><b>Alignment");
		container.add(label, "wrap");
		alignmentBox = new JComboBox<String>();
		alignmentBox.addActionListener(this);
		container.add(alignmentBox, "gapbottom 10, wrap");

		label = new JLabel("<html><b>Gender");
		container.add(label, "wrap");

		JPanel subContainer = new JPanel(new MigLayout());
		male = new JRadioButton("Male");
		male.addActionListener(this);
		subContainer.add(male, "gapright 8");
		female = new JRadioButton("Female");
		female.addActionListener(this);
		subContainer.add(female);
		container.add(subContainer);
		add(container, "aligny top");

		ButtonGroup group = new ButtonGroup();
		group.add(male);
		group.add(female);
		male.setSelected(true);

		container = new JPanel(new MigLayout());
		container.setBorder(BorderFactory.createTitledBorder("Stats"));

		label = new JLabel("Strength");
		container.add(label);
		buttons[0] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[0], "gap unrel");
		strLabel = new JLabel("-");
		container.add(strLabel, "alignx 50%");
		buttons[6] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[6]);
		strRange = new JLabel();
		container.add(strRange, "gap unrel, wrap");

		label = new JLabel("Intelligence");
		container.add(label);
		buttons[1] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[1], "gap unrel");
		intLabel = new JLabel("-");
		container.add(intLabel, "alignx 50%");
		buttons[7] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[7]);
		intRange = new JLabel();
		container.add(intRange, "gap unrel, wrap");

		label = new JLabel("Wisdom");
		container.add(label);
		buttons[2] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[2], "gap unrel");
		wisLabel = new JLabel("-");
		container.add(wisLabel, "alignx 50%");
		buttons[8] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[8]);
		wisRange = new JLabel();
		container.add(wisRange, "gap unrel, wrap");

		label = new JLabel("Constitution");
		container.add(label);
		buttons[3] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[3], "gap unrel");
		conLabel = new JLabel("-");
		container.add(conLabel, "alignx 50%");
		buttons[9] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[9]);
		conRange = new JLabel();
		container.add(conRange, "gap unrel, wrap");

		label = new JLabel("Charisma");
		container.add(label);
		buttons[4] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[4], "gap unrel");
		chrLabel = new JLabel("-");
		container.add(chrLabel, "alignx 50%");
		buttons[10] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[10]);
		chrRange = new JLabel();
		container.add(chrRange, "gap unrel, wrap");

		label = new JLabel("Dexterity");
		container.add(label);
		buttons[5] = Utility.iconButton("images/dec.png", "small");
		container.add(buttons[5], "gap unrel");
		dexLabel = new JLabel("-");
		container.add(dexLabel, "alignx 50%");
		buttons[11] = Utility.iconButton("images/inc.png", "small");
		container.add(buttons[11]);
		dexRange = new JLabel();
		container.add(dexRange, "gap unrel, wrap unrel");

		for (JButton button : buttons)
			button.addActionListener(this);
		
		subContainer = new JPanel(new MigLayout());
		pointsLeftLabel = new JLabel("-");
		subContainer.add(pointsLeftLabel);
		label = new JLabel("Stat points left");
		subContainer.add(label);
		container.add(subContainer, "span, alignx 50%");
		add(container, "aligny top, sgy box");

		container = new JPanel(new MigLayout());
		container.setBorder(BorderFactory.createTitledBorder("Available Guilds"));

		guilds = new JLabel();
		container.add(guilds);
		add(container, "aligny top, sgy box, wrap");

		save = new JButton("Save Character");
		save.addActionListener(this);
		add(save, "span, split, alignx right");
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		add(cancel);
		
		newRaceUpdate();
	}

	public void newRaceUpdate() {
		race = (Race) raceBox.getSelectedItem();

		alignmentBox.removeAllItems();
		if ((race.alignments & 1) != 0)
			alignmentBox.addItem(Utility.alignmentText(1));
		if ((race.alignments & 2) != 0)
			alignmentBox.addItem(Utility.alignmentText(2));
		if ((race.alignments & 4) != 0)
			alignmentBox.addItem(Utility.alignmentText(4));

		strength = race.minStrength + 5;
		intelligence = race.minIntelligence + 5;
		wisdom = race.minWisdom + 5;
		constitution = race.minConstitution + 5;
		charisma = race.minCharisma + 5;
		dexterity = race.minDexterity + 5;
		pointsLeft = race.bonus;

		strRange.setText("(" + race.minStrength + "-" + race.maxStrength + ")");
		intRange.setText("(" + race.minIntelligence + "-" + race.maxIntelligence + ")");
		wisRange.setText("(" + race.minWisdom + "-" + race.maxWisdom + ")");
		conRange.setText("(" + race.minConstitution + "-" + race.maxConstitution + ")");
		chrRange.setText("(" + race.minCharisma + "-" + race.maxCharisma + ")");
		dexRange.setText("(" + race.minDexterity + "-" + race.maxDexterity + ")");

		update();
	}

	public void update() {
		boolean allowed = true, first = true;
		race = (Race) raceBox.getSelectedItem();

		String text = (String) alignmentBox.getSelectedItem();
		if (text == null)
			alignment = 0;
		else if (text.equals(Utility.alignmentText(1)))
			alignment = 1;
		else if (text.equals(Utility.alignmentText(2)))
			alignment = 2;
		else if (text.equals(Utility.alignmentText(4)))
			alignment = 4;
		else
			alignment = 0;

		text = "<html>";
		for (Guild guild : Guild.list) {
			if (!first)
				text += "<br>";
			else
				first = false;

			if (female.isSelected()) {
				if (guild.maleOnly)
					allowed = false;

			} else {
				if (guild.femaleOnly)
					allowed = false;
			}

			if (!guild.allowedRaces.contains(race))
				allowed = false;

			if ((guild.alignments & alignment) == 0)
				allowed = false;

			if (!allowed) {
				text += "<font color=gray>" + guild.name + "</font>";

			} else {
				if (strength < guild.reqStrength)
					allowed = false;
				else if (intelligence < guild.reqIntelligence)
					allowed = false;
				else if (wisdom < guild.reqWisdom)
					allowed = false;
				else if (constitution < guild.reqConstitution)
					allowed = false;
				else if (charisma < guild.reqCharisma)
					allowed = false;
				else if (dexterity < guild.reqDexterity)
					allowed = false;

				if (allowed)
					text += "<b>" + guild.name + "</b>";
				else
					text += guild.name;
			}
		}

		System.out.println(text);
		guilds.setText(text);
		
		strLabel.setText("<html><font size=4>" + strength);
		intLabel.setText("<html><font size=4>" + intelligence);
		wisLabel.setText("<html><font size=4>" + wisdom);
		conLabel.setText("<html><font size=4>" + constitution);
		chrLabel.setText("<html><font size=4>" + charisma);
		dexLabel.setText("<html><font size=4>" + dexterity);
		pointsLeftLabel.setText("<html><font size=4>" + pointsLeft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == cancel) {
			setVisible(false);
			dispose();
			return;
		}
		
		if (source == save) {
			if (save()) {
				setVisible(false);
				dispose();
			}
			
			return;
		}

		if (source == raceBox)
			newRaceUpdate();

		else if (source == buttons[0] && strength > race.minStrength) {
			strength--;
			pointsLeft++;

		} else if (source == buttons[1] && intelligence > race.minIntelligence) {
			intelligence--;
			pointsLeft++;

		} else if (source == buttons[2] && wisdom > race.minWisdom) {
			wisdom--;
			pointsLeft++;

		} else if (source == buttons[3] && constitution > race.minConstitution) {
			constitution--;
			pointsLeft++;

		} else if (source == buttons[4] && charisma > race.minCharisma) {
			charisma--;
			pointsLeft++;

		} else if (source == buttons[5] && dexterity > race.minDexterity) {
			dexterity--;
			pointsLeft++;

		} else if (source == buttons[6] && strength < race.maxStrength && pointsLeft > 0) {
			strength++;
			pointsLeft--;

		} else if (source == buttons[7] && intelligence < race.maxIntelligence && pointsLeft > 0) {
			intelligence++;
			pointsLeft--;

		} else if (source == buttons[8] && wisdom < race.maxWisdom && pointsLeft > 0) {
			wisdom++;
			pointsLeft--;

		} else if (source == buttons[9] && constitution < race.maxConstitution && pointsLeft > 0) {
			constitution++;
			pointsLeft--;

		} else if (source == buttons[10] && charisma < race.maxCharisma && pointsLeft > 0) {
			charisma++;
			pointsLeft--;

		} else if (source == buttons[11] && dexterity < race.maxDexterity && pointsLeft > 0) {
			dexterity++;
			pointsLeft--;
		}

		update();
	}

	public boolean save() {
		String name = this.name.getText();
		if (name == null || name.length() == 0) {
			JOptionPane.showMessageDialog(null, "Character must have a name.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if (Character.find(name) != null) {
			JOptionPane.showMessageDialog(null, "That character already exists.  Try another name.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (pointsLeft > 0) {
			int r = JOptionPane.showConfirmDialog(null, "You still have stat points left to distribute.  Forfeit and save anyway?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (r != JOptionPane.YES_OPTION)
				return false;
		}

		Character character = new Character(name);
		Character.list.add(character);
		
		character.age = Game.MINIMUM_AGE;
		character.level = 1;
		character.experience = 0;
		character.hitPoints = character.hitMax = 15;
		character.spellPoints = character.spellMax = 100;
		character.spellLevel = 1;
		character.gold = 100;
		character.bankedGold = 0;
		
		character.strength = strength;
		character.intelligence = intelligence;
		character.wisdom = wisdom;
		character.constitution = constitution;
		character.charisma = charisma;
		character.dexterity = dexterity;
		
		character.race = race;
		character.female = female.isSelected();
		character.alignment = alignment;
		
		Guild nomad = Guild.get("Nomad");
		character.guilds.add(nomad, 1);
		character.currentGuild = nomad; 
		
		return true;
	}
}
