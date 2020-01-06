package com.rodrom.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import com.rodrom.Race;
import com.rodrom.ui.Utility.DoubleVerifier;
import com.rodrom.ui.Utility.IntVerifier;

import net.miginfocom.swing.MigLayout;

public class RaceEditTab extends JPanel implements ListSelectionListener, ActionListener, DocumentListener {
	private static final long serialVersionUID = 1L;

	public JList<String> list;
	public RaceListModel listModel;
	public JTextField nameField;
	public JButton addButton, removeButton;

	public StatsTable stats;
	public DamageTypeMappingTable resistances;
	public JCheckBox alignmentsGoodCB;
	public JCheckBox alignmentsNeutralCB;
	public JCheckBox alignmentsEvilCB;
	public JComboBox<String> size;
	public JTextField bonusField;
	public JTextField maxAgeField;
	public JTextField expScalerField;
	
	public IntVerifier bonusVerifier;
	public IntVerifier maxAgeVerifier;
	public DoubleVerifier expScalerVerifier;

	public class RaceListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String getElementAt(int index) {
			return Race.list.get(index).name;
		}

		@Override
		public int getSize() {
			return Race.list.size();
		}

		public void add() {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || Race.find(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			int index = Race.add(name);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);

			nameField.setText("");
			nameField.requestFocusInWindow();

			update();
		}

		public void remove() {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Race.list.size()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			Race.list.remove(index);
			list.setSelectedIndex(-1);
			update();
		}

		public void update() {
			fireContentsChanged(this, 0, Race.list.size());
		}
	}

	public class StatsUpdate implements StatsTable.Update {
		public void update(int row, int column, int value) {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Race.list.size())
				return;

			Race race = Race.list.get(index);
			if (column == 1) {
				if (row == 0) race.minStrength = value;
				else if (row == 1) race.minIntelligence = value;
				else if (row == 2) race.minWisdom = value;
				else if (row == 3) race.minConstitution = value;
				else if (row == 4) race.minCharisma = value;
				else race.minDexterity = value;

			} else {
				if (row == 0) race.maxStrength = value;
				else if (row == 1) race.maxIntelligence = value;
				else if (row == 2) race.maxWisdom = value;
				else if (row == 3) race.maxConstitution = value;
				else if (row == 4) race.maxCharisma = value;
				else race.maxDexterity = value;
			}
		}

		public int get(int row, int column) {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= Race.list.size())
				return 0;

			Race race = Race.list.get(index);
			if (column == 1) {
				switch (row) {
				case 0: return race.minStrength;
				case 1: return race.minIntelligence;
				case 2: return race.minWisdom;
				case 3: return race.minConstitution;
				case 4: return race.minCharisma;
				default: return race.minDexterity;
				}
			}
			
			switch (row) {
			case 0: return race.maxStrength;
			case 1: return race.maxIntelligence;
			case 2: return race.maxWisdom;
			case 3: return race.maxConstitution;
			case 4: return race.maxCharisma;
			default: return race.maxDexterity;
			}
		}
	};

	public void update() {
		boolean enabled;
		int index = list.getSelectedIndex();
		if (index < 0 || index >= Race.list.size()) {
			enabled = false;
			resistances.tableModel.mapping = null;

		} else {
			enabled = true;
			Race race = Race.list.get(index);
			
			stats.reload();
			resistances.tableModel.mapping = race.resistances;
			resistances.reload();
			alignmentsGoodCB.setSelected((race.alignments & 1) != 0);
			alignmentsNeutralCB.setSelected((race.alignments & 2) != 0);
			alignmentsEvilCB.setSelected((race.alignments & 4) != 0);
			size.setSelectedIndex(race.size);
			bonusField.setText(String.valueOf(race.bonus));
			maxAgeField.setText(String.valueOf(race.maxAge));
			expScalerField.setText(String.valueOf(race.expScaler));
		}
		
		removeButton.setEnabled(enabled);

		stats.setEnabled(enabled);
		resistances.setEnabled(enabled);
		alignmentsGoodCB.setEnabled(enabled);
		alignmentsNeutralCB.setEnabled(enabled);
		alignmentsEvilCB.setEnabled(enabled);
		size.setEnabled(enabled);
		bonusField.setEnabled(enabled);
		maxAgeField.setEnabled(enabled);
		expScalerField.setEnabled(enabled);
	}
	
	public RaceEditTab() {
		super(new MigLayout("align 50% 50%"));

		listModel = new RaceListModel();

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

		container.add(new JLabel("<html><b>Stat Ranges"), "wrap");
		stats = new StatsTable("Min", "Max", new StatsUpdate());
		scroller = new JScrollPane(stats);
		container2 = new JPanel(new BorderLayout());
		container2.add(scroller);
		container.add(container2, "wrap unrel");

		container.add(new JLabel("<html><b>Natural Resistances"), "wrap");
		resistances = new DamageTypeMappingTable(null);
		scroller = new JScrollPane(resistances);
		container2 = new JPanel(new BorderLayout());
		container2.add(scroller);
		container.add(container2, "wrap unrel");
		main.add(container);

		container = new JPanel(new MigLayout());
		container2 = new JPanel(new MigLayout());
		
		JLabel label = new JLabel("<html><b>Extra Points");
		label.setToolTipText("Extra stat points race gets to distribute at creation");
		container2.add(label);
		bonusField = new JTextField(3);
		bonusField.setInputVerifier(bonusVerifier = new IntVerifier(-20, 20, bonusField));
		bonusField.getDocument().addDocumentListener(this);
		container2.add(bonusField, "wrap 1px");

		container2.add(new JLabel("<html><b>Max Age"));
		maxAgeField = new JTextField(3);
		maxAgeField.setInputVerifier(maxAgeVerifier = new IntVerifier(50, 2000, maxAgeField));
		maxAgeField.getDocument().addDocumentListener(this);
		container2.add(maxAgeField, "wrap 1px");

		label = new JLabel("<html><b>Experience Scaler");
		label.setToolTipText("How much actual experience race gets");
		container2.add(label);
		expScalerField = new JTextField(3);
		expScalerField.setInputVerifier(expScalerVerifier = new DoubleVerifier(0.2, 5.0, expScalerField));
		expScalerField.getDocument().addDocumentListener(this);
		container2.add(expScalerField, "wrap");
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

		container.add(new JLabel("<html><b>Size"), "wrap");
		size = new JComboBox<String>(Utility.sizeText);
		size.addActionListener(this);
		container.add(size, "wrap");

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
		Race race = null;
		int index = list.getSelectedIndex();
		if (index >= 0 && index < Race.list.size())
			race = Race.list.get(index);

		if (document == nameField.getDocument()) {
			addButton.setEnabled(e.getDocument().getLength() > 0);

		} else if (document == bonusField.getDocument()) {
			if (bonusVerifier.verify(bonusField))
				race.bonus = bonusVerifier.value;

		} else if (document == maxAgeField.getDocument()) {
			if (maxAgeVerifier.verify(maxAgeField))
				race.maxAge = maxAgeVerifier.value;

		} else if (document == expScalerField.getDocument()) {
			if (expScalerVerifier.verify(expScalerField))
				race.expScaler = expScalerVerifier.value;
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
			System.out.println("index is " + index);
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
		Race race = null;
		int index = list.getSelectedIndex();
		if (index >= 0 && index < Race.list.size())
			race = Race.list.get(index);

		if (e.getSource() == addButton) {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || Race.find(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			index = Race.add(name);
			nameField.setText("");
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
			listModel.update();

		} else if (e.getSource() == removeButton) {
			listModel.remove();

		} else if (e.getSource() == alignmentsGoodCB) {
			if (race != null) {
				race.alignments &= ~1;
				if (alignmentsGoodCB.isSelected())
					race.alignments |= 1;
			}
			
		} else if (e.getSource() == alignmentsNeutralCB) {
			if (race != null) {
				race.alignments &= ~2;
				if (alignmentsNeutralCB.isSelected())
					race.alignments |= 2;
			}
			
		} else if (e.getSource() == alignmentsEvilCB) {
			if (race != null) {
				race.alignments &= ~4;
				if (alignmentsEvilCB.isSelected())
					race.alignments |= 4;
			}
			
		} else if (e.getSource() == size) {
			if (race != null)
				race.size = size.getSelectedIndex();

		} else {
			System.out.println("Unhandled ActionEvent " + e + " from " + e.getSource());
		}
	}
}
