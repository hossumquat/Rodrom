package com.rodrom.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import com.rodrom.DamageType;
import com.rodrom.Guild;
import com.rodrom.ui.Utility.DoubleVerifier;
import com.rodrom.ui.Utility.IntVerifier;

import net.miginfocom.swing.MigLayout;

public class GuildEditTab extends JPanel implements ListSelectionListener, ActionListener, DocumentListener {
	private static final long serialVersionUID = 1L;

	public JList<String> list;
	public GuildListModel listModel;
	public JTextField nameField;
	public JButton addButton, removeButton;

	public StatsTable stats;
	public JCheckBox alignmentsGoodCB;
	public JCheckBox alignmentsNeutralCB;
	public JCheckBox alignmentsEvilCB;
	public JCheckBox maleOnlyCB;
	public JCheckBox femaleOnlyCB;
	
	public RacesAllowedTable racesAllowed;

	public JTextField perceptionField;
	public JTextField fightingField;
	public JTextField thievingField;
	public JTextField backstabbingField;
	public JTextField critField;
	public JTextField swingsField;
	public JTextField expScalerField;
	public JTextField hpScalerField;
	public JTextField monQuestField;
	public JTextField itemQuestField;
	
	public IntVerifier perceptionVerifier;
	public IntVerifier fightingVerifier;
	public IntVerifier thievingVerifier;
	public IntVerifier backstabbingVerifier;
	public IntVerifier critVerifier;
	public IntVerifier swingsVerifier;
	public DoubleVerifier expScalerVerifier;
	public DoubleVerifier hpScalerVerifier;
	public IntVerifier monQuestVerifier;
	public IntVerifier itemQuestVerifier;

	public class GuildListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String getElementAt(int index) {
			return Guild.list.get(index).name;
		}

		@Override
		public int getSize() {
			return Guild.list.size();
		}

		public void add() {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || Guild.get(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			int index = Guild.add(name);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);

			nameField.setText("");
			nameField.requestFocusInWindow();

			update();
		}

		public void remove() {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Guild.list.size()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			Guild.list.remove(index);
			list.setSelectedIndex(-1);
			update();
		}

		public void update() {
			fireContentsChanged(this, 0, Guild.list.size());
		}
	}

	public class StatsUpdate implements StatsTable.Update {
		public void update(int row, int column, int value) {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Guild.list.size())
				return;

			Guild guild = Guild.list.get(index);
			if (column == 1) {
				if (row == 0) guild.reqStrength = value;
				else if (row == 1) guild.reqIntelligence = value;
				else if (row == 2) guild.reqWisdom = value;
				else if (row == 3) guild.reqConstitution = value;
				else if (row == 4) guild.reqCharisma = value;
				else guild.reqDexterity = value;
			}
		}

		public int get(int row, int column) {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Guild.list.size())
				return 0;

			Guild guild = Guild.list.get(index);
			switch (row) {
			case 0: return guild.reqStrength;
			case 1: return guild.reqIntelligence;
			case 2: return guild.reqWisdom;
			case 3: return guild.reqConstitution;
			case 4: return guild.reqCharisma;
			default: return guild.reqDexterity;
			}
		}
	};

	public void update() {
		boolean enabled;
		int index = list.getSelectedIndex();
		if (index < 0 || index >= Guild.list.size()) {
			enabled = false;

		} else {
			enabled = true;
			Guild guild = Guild.list.get(index);
			
			stats.reload();
			alignmentsGoodCB.setSelected((guild.alignments & 1) != 0);
			alignmentsNeutralCB.setSelected((guild.alignments & 2) != 0);
			alignmentsEvilCB.setSelected((guild.alignments & 4) != 0);
			maleOnlyCB.setSelected(guild.maleOnly);
			femaleOnlyCB.setSelected(guild.femaleOnly);
			racesAllowed.set(guild.allowedRaces);
			perceptionField.setText(String.valueOf(guild.perception));
			fightingField.setText(String.valueOf(guild.fighting));
			thievingField.setText(String.valueOf(guild.thieving));
			backstabbingField.setText(String.valueOf(guild.backstabbing));
			critField.setText(String.valueOf(guild.criticalHitting));
			swingsField.setText(String.valueOf(guild.swings));
			expScalerField.setText(String.valueOf(guild.experienceReqScaler));
			hpScalerField.setText(String.valueOf(guild.hitPointsScaler));
			monQuestField.setText(String.valueOf(guild.monsterQuestChance));
			itemQuestField.setText(String.valueOf(guild.itemQuestChance));
		}
		
		removeButton.setEnabled(enabled);

		stats.setEnabled(enabled);
		alignmentsGoodCB.setEnabled(enabled);
		alignmentsNeutralCB.setEnabled(enabled);
		alignmentsEvilCB.setEnabled(enabled);
		maleOnlyCB.setEnabled(enabled);
		femaleOnlyCB.setEnabled(enabled);
		racesAllowed.setEnabled(enabled);
		perceptionField.setEnabled(enabled);
		fightingField.setEnabled(enabled);
		thievingField.setEnabled(enabled);
		backstabbingField.setEnabled(enabled);
		critField.setEnabled(enabled);
		swingsField.setEnabled(enabled);
		expScalerField.setEnabled(enabled);
		hpScalerField.setEnabled(enabled);
		monQuestField.setEnabled(enabled);
		itemQuestField.setEnabled(enabled);
	}
	
	public GuildEditTab() {
		super(new MigLayout("align 50% 50%"));

		listModel = new GuildListModel();

		list = new JList<String>();
		list.setModel(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(15);
		list.addListSelectionListener(this);

		addButton = new JButton("Add");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		addButton.setEnabled(false);

		removeButton = new JButton("Remove");
		removeButton.setActionCommand("remove");
		removeButton.setEnabled(DamageType.list.size() != 0);
		removeButton.addActionListener(this);

		nameField = new JTextField(10);
		nameField.getDocument().addDocumentListener(this);
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.add();
			}
		});

		JPanel container = new JPanel(new MigLayout());
		JPanel container2 = new JPanel(new MigLayout());

		JScrollPane scroller = new JScrollPane(list);
		container.add(scroller, "grow, pushy");
		container.add(removeButton, "bottom, wrap");

		container.add(nameField, "growx");
		container.add(addButton);
		add(container, "growy, pushy");

		JPanel main = new JPanel(new MigLayout());
		container = new JPanel(new MigLayout());

		container.add(new JLabel("<html><b>Stats Required"), "wrap");
		stats = new StatsTable("Min", null, new StatsUpdate());
		scroller = new JScrollPane(stats);
		container2 = new JPanel(new BorderLayout());
		container2.add(scroller);
		container.add(container2, "wrap unrel");

		container.add(new JLabel("<html><b>Allowed Alignments"), "wrap");

		alignmentsGoodCB = new JCheckBox(Utility.alignmentText(1));
		alignmentsGoodCB.addActionListener(this);
		container.add(alignmentsGoodCB, "wrap 1px");

		alignmentsNeutralCB = new JCheckBox(Utility.alignmentText(2));
		alignmentsNeutralCB.addActionListener(this);
		container.add(alignmentsNeutralCB, "wrap 1px");

		alignmentsEvilCB = new JCheckBox(Utility.alignmentText(4));
		alignmentsEvilCB.addActionListener(this);
		container.add(alignmentsEvilCB, "wrap unrel");

		container.add(new JLabel("<html><b>Allowed Genders"), "wrap");

		maleOnlyCB = new JCheckBox("Males Only");
		maleOnlyCB.addActionListener(this);
		container.add(maleOnlyCB, "wrap 1px");
		
		femaleOnlyCB = new JCheckBox("Females Only");
		femaleOnlyCB.addActionListener(this);
		container.add(femaleOnlyCB);
		main.add(container);

		container = new JPanel(new MigLayout());
		container.add(new JLabel("<html><b>Allowed Races"), "wrap");
		racesAllowed = new RacesAllowedTable();
		scroller = new JScrollPane(racesAllowed);
		container2 = new JPanel(new BorderLayout());
		container2.add(scroller);
		container.add(container2);
		main.add(container);
		
		container = new JPanel(new MigLayout());
		container2 = new JPanel(new MigLayout());
		
		container2.add(new JLabel("<html><b>Perception"));
		perceptionField = new JTextField(3);
		perceptionField.setInputVerifier(perceptionVerifier = new IntVerifier(0, 20, perceptionField));
		perceptionField.getDocument().addDocumentListener(this);
		container2.add(perceptionField, "wrap 1px");

		container2.add(new JLabel("<html><b>Fighting"));
		fightingField = new JTextField(3);
		fightingField.setInputVerifier(fightingVerifier = new IntVerifier(1, 25, fightingField));
		fightingField.getDocument().addDocumentListener(this);
		container2.add(fightingField, "wrap 1px");

		container2.add(new JLabel("<html><b>Thieving"));
		thievingField = new JTextField(3);
		thievingField.setInputVerifier(thievingVerifier = new IntVerifier(1, 25, thievingField));
		thievingField.getDocument().addDocumentListener(this);
		container2.add(thievingField, "wrap 1px");

		container2.add(new JLabel("<html><b>Backstabbing"));
		backstabbingField = new JTextField(3);
		backstabbingField.setInputVerifier(backstabbingVerifier = new IntVerifier(1, 25, backstabbingField));
		backstabbingField.getDocument().addDocumentListener(this);
		container2.add(backstabbingField, "wrap 1px");

		container2.add(new JLabel("<html><b>Critical Hitting"));
		critField = new JTextField(3);
		critField.setInputVerifier(critVerifier = new IntVerifier(1, 25, critField));
		critField.getDocument().addDocumentListener(this);
		container2.add(critField, "wrap 1px");

		container2.add(new JLabel("<html><b>Extra Swings"));
		swingsField = new JTextField(3);
		swingsField.setInputVerifier(swingsVerifier = new IntVerifier(0, 25, swingsField));
		swingsField.getDocument().addDocumentListener(this);
		container2.add(swingsField, "wrap unrel");

		container2.add(new JLabel("<html><b>Exp. Req. Scaler"));
		expScalerField = new JTextField(3);
		expScalerField.setInputVerifier(expScalerVerifier = new DoubleVerifier(0.2, 5.0, expScalerField));
		expScalerField.getDocument().addDocumentListener(this);
		container2.add(expScalerField, "wrap 1px");

		container2.add(new JLabel("<html><b>HP Scaler"));
		hpScalerField = new JTextField(3);
		hpScalerField.setInputVerifier(hpScalerVerifier = new DoubleVerifier(0.2, 5.0, hpScalerField));
		hpScalerField.getDocument().addDocumentListener(this);
		container2.add(hpScalerField, "wrap 1px");

		container2.add(new JLabel("<html><b>Mon. Quest Chance"));
		monQuestField = new JTextField(3);
		monQuestField.setInputVerifier(monQuestVerifier = new IntVerifier(0, 100, monQuestField));
		monQuestField.getDocument().addDocumentListener(this);
		container2.add(monQuestField, "wrap 1px");

		container2.add(new JLabel("<html><b>Item Quest Chance"));
		itemQuestField = new JTextField(3);
		itemQuestField.setInputVerifier(itemQuestVerifier = new IntVerifier(0, 100, itemQuestField));
		itemQuestField.getDocument().addDocumentListener(this);
		container2.add(itemQuestField, "wrap");
		container.add(container2, "wrap unrel");

		main.add(container);
		JScrollPane scroll = new JScrollPane(main) {
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize() {
				Dimension dim = super.getPreferredSize();
				JScrollBar bar = getVerticalScrollBar();
				if (bar.isShowing())
					dim.width += bar.getWidth();

				bar = getHorizontalScrollBar();
				if (bar.isShowing())
					dim.height += bar.getHeight();

				return dim;
			}
		};
		add(scroll);
		
		update();
	}

	public void commonDocumentUpdate(DocumentEvent e) {
		Document document = e.getDocument();
		Guild guild = null;
		int index = list.getSelectedIndex();
		if (index >= 0 && index < Guild.list.size())
			guild = Guild.list.get(index);

		if (document == nameField.getDocument()) {
			addButton.setEnabled(e.getDocument().getLength() > 0);

		} else if (document == perceptionField.getDocument()) {
			if (perceptionVerifier.verify(perceptionField))
				guild.perception = perceptionVerifier.value;

		} else if (document == fightingField.getDocument()) {
			if (fightingVerifier.verify(fightingField))
				guild.fighting = fightingVerifier.value;

		} else if (document == thievingField.getDocument()) {
			if (thievingVerifier.verify(thievingField))
				guild.thieving = thievingVerifier.value;

		} else if (document == backstabbingField.getDocument()) {
			if (backstabbingVerifier.verify(backstabbingField))
				guild.backstabbing = backstabbingVerifier.value;

		} else if (document == critField.getDocument()) {
			if (critVerifier.verify(critField))
				guild.criticalHitting = critVerifier.value;

		} else if (document == swingsField.getDocument()) {
			if (swingsVerifier.verify(swingsField))
				guild.swings = swingsVerifier.value;

		} else if (document == expScalerField.getDocument()) {
			if (expScalerVerifier.verify(expScalerField))
				guild.experienceReqScaler = expScalerVerifier.value;

		} else if (document == hpScalerField.getDocument()) {
			if (hpScalerVerifier.verify(hpScalerField))
				guild.hitPointsScaler = hpScalerVerifier.value;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		commonDocumentUpdate(e);
		System.out.println("changedUpdate() finally called (with " + e + ")");
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		commonDocumentUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		commonDocumentUpdate(e);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			int index = list.getSelectedIndex();
			if (index == -1) {
				removeButton.setEnabled(false);

			} else {
				removeButton.setEnabled(true);
			}
		}
		
		update();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Guild guild = null;
		int index = list.getSelectedIndex();
		if (index >= 0 && index < Guild.list.size())
			guild = Guild.list.get(index);

		if (e.getSource() == addButton) {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || Guild.get(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			index = Guild.add(name);
			nameField.setText("");
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
			listModel.update();

		} else if (e.getSource() == removeButton) {
			listModel.remove();

		} else if (e.getSource() == alignmentsGoodCB) {
			if (guild != null) {
				guild.alignments &= ~1;
				if (alignmentsGoodCB.isSelected())
					guild.alignments |= 1;
			}
			
		} else if (e.getSource() == alignmentsNeutralCB) {
			if (guild != null) {
				guild.alignments &= ~2;
				if (alignmentsNeutralCB.isSelected())
					guild.alignments |= 2;
			}
			
		} else if (e.getSource() == alignmentsEvilCB) {
			if (guild != null) {
				guild.alignments &= ~4;
				if (alignmentsEvilCB.isSelected())
					guild.alignments |= 4;
			}
			
		} else if (e.getSource() == maleOnlyCB) {
			if (guild != null) {
				guild.maleOnly = maleOnlyCB.isSelected();
				if (guild.maleOnly && guild.femaleOnly) {
					guild.femaleOnly = false;
					femaleOnlyCB.setSelected(false);
				}
			}
			
		} else if (e.getSource() == femaleOnlyCB) {
			if (guild != null) {
				guild.femaleOnly = femaleOnlyCB.isSelected();
				if (guild.maleOnly && guild.femaleOnly) {
					guild.maleOnly = false;
					maleOnlyCB.setSelected(false);
				}
			}
			
		} else {
			System.out.println("Unhandled ActionEvent " + e + " from " + e.getSource());
		}
	}
}
