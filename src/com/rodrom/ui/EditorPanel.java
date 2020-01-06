package com.rodrom.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.rodrom.LoadSave;
import com.rodrom.MainWindow;

import net.miginfocom.swing.MigLayout;

public class EditorPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	public EditorPanel() {
		super(new MigLayout("fill"));
		
		JButton button = new JButton("Save Master.dat");
		button.setActionCommand("save.master");
		button.addActionListener(this);
		
		JPanel panel = new JPanel(new MigLayout("fillx, insets n n 0 n"));
		panel.setBackground(MainWindow.backgroundColor);
		
		JLabel title = new JLabel("Welcome to the Rodrom editor suite");
		title.setFont(title.getFont().deriveFont(Font.BOLD));

		panel.add(title);
		panel.add(new Gap());
		panel.add(button, "right");
		add(panel, "north");

		setBackground(MainWindow.backgroundColor);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);

		tabbedPane.addTab("Map", new MapEditTab());
		tabbedPane.addTab("Items", new JScrollPane(new ItemEditTab()));
		tabbedPane.addTab("Item Bases", new ItemBaseEditTab());
		tabbedPane.addTab("Loot Lists", new LootListEditTab());
		tabbedPane.addTab("Creatures", new CreatureEditTab());
		tabbedPane.addTab("Damage Types", new DamageTypeEditTab());
		tabbedPane.addTab("Races", new RaceEditTab());
		tabbedPane.addTab("Guilds", new GuildEditTab());
		
		add(tabbedPane, "grow");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LoadSave.saveMaster();
	}
}

// A character has skills with individual trap types.  As a character encounters new
// trap types, he becomes more experienced with that type and is thus able to
// better identify them and disarm them.  A character tends to learn more from
// them going off than from successfully disarming them.  The thieves guild trains
// their members, so members will have have a minimum baseline that rises as they
// rank up.
