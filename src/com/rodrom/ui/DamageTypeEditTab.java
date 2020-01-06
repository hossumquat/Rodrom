package com.rodrom.ui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.rodrom.DamageType;

import net.miginfocom.swing.MigLayout;

public class DamageTypeEditTab extends JPanel implements ListSelectionListener, ActionListener, DocumentListener {
	private static final long serialVersionUID = 1L;

	public class DamageTypeListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String getElementAt(int index) {
			return DamageType.list.get(index).name;
		}

		@Override
		public int getSize() {
			return DamageType.list.size();
		}
		
		public void add() {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || DamageType.find(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			int index = DamageType.add(name);
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);

			nameField.setText("");
			nameField.requestFocusInWindow();

			update();
		}
		
		public void remove() {
			int index = list.getSelectedIndex();
			if (index < 0 || index >= DamageType.list.size()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			DamageType.list.remove(index);
			list.setSelectedIndex(-1);
			update();
		}
		
		public void update() {
			fireContentsChanged(this, 0, DamageType.list.size());
		}
	}

	public JList<String> list;
	public DamageTypeListModel listModel;
	public JTextField nameField;
	public JButton addButton, removeButton;

	public DamageTypeEditTab() {
		super(new MigLayout("filly, align 50% 50%"));

		listModel = new DamageTypeListModel();

		list = new JList<String>();
		list.setModel(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);
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

		JScrollPane scroller = new JScrollPane(list);
		add(scroller, "grow, pushy");
		add(removeButton, "bottom, wrap");

		add(nameField, "growx");
		add(addButton);
		
		int index = list.getSelectedIndex();
		if (index < 0 || index > DamageType.list.size())
			removeButton.setEnabled(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() == -1) {
				removeButton.setEnabled(false);

			} else {
				removeButton.setEnabled(true);
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		addButton.setEnabled(e.getDocument().getLength() > 0);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		addButton.setEnabled(true);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		addButton.setEnabled(e.getDocument().getLength() > 0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("add")) {
			String name = Utility.capitalize(nameField.getText());
			if (name.length() < 1 || DamageType.find(name) != null) {
				Toolkit.getDefaultToolkit().beep();
				nameField.requestFocusInWindow();
				nameField.selectAll();
				return;
			}

			int index = DamageType.add(name);
			nameField.setText("");
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
			listModel.update();

		} else if (command.equals("remove")) {
			listModel.remove();

		} else {
			System.out.println("Unhandled command " + command);
		}
	}
}
