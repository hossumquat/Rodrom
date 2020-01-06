package com.rodrom.ui;

import javax.swing.table.AbstractTableModel;

public class PartyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	final public String[] headers = new String[] { "Name", "Hits", "Spells", "Status", "Action" };

	final Class<?>[] columnClass = new Class<?>[] { String.class, String.class, Integer.class, String.class,
			String.class };

	Object[][] data = new Object[][] { { "Throg", "100/120", 215, "OK", "None" }, { "Bob", "90/90", 178, "OK", "None" },
			{ "Doug", "28/98", 12, "OK", "None" } };

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data[row][column];
	}

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}

	@Override
	public Class<? extends Object> getColumnClass(int column) {
		return columnClass[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void update() {
		fireTableDataChanged();
		fireTableStructureChanged();
	}
}
