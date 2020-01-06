package com.rodrom.ui;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class StatsTable extends JTable {
	private static final long serialVersionUID = 1L;

	interface Update {
		public void update(int row, int column, int value);
		public int get(int row, int column);
	}
	
	class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		public String column1Name, column2Name;
		public int[] values;
		public Update update;

		public TableModel(String column1Name, String column2Name, Update update) {
			this.update = update;
			this.column1Name = column1Name;
			this.column2Name = column2Name;
		}

		public int getColumnCount() {
			return (column2Name == null) ? 2 : 3;
		}

		public int getRowCount() {
			return 6;
		}

		public String getColumnName(int column) {
			if (column == 0)
				return "Stat";

			if (column == 1)
				return column1Name;

			return column2Name;
		}

		public Object getValueAt(int row, int column) {
			if (column == 0) {
				switch (row) {
				case 0: return "Strength";
				case 1: return "Intelligence";
				case 2: return "Wisdom";
				case 3: return "Constitution";
				case 4: return "Charisma";
				case 5: return "Dexterity";
				}

				return null;
			}

			if (update == null)
				return 0;
			
			return update.get(row, column);
		}

		public Class<?> getColumnClass(int column) {
			if (column == 0)
				return String.class;
			
			return Integer.class;
		}

		public boolean isCellEditable(int row, int column) {
			return column > 0;
		}

		public void setValueAt(Object value, int row, int column) {
			System.out.println("Setting value at " + row + "," + column + " to " + value + " (an instance of "
					+ value.getClass() + ")");

			if (column > 0 && update != null)
				update.update(row, column, (int) value);

			fireTableCellUpdated(row, column);
		}
	}

	public TableColumnAdjuster tableColumnAdjuster;
	public TableModel tableModel;
	
	public StatsTable(String column1Name, String column2Name, Update update) {
		super();
		setModel(tableModel = new TableModel(column1Name, column2Name, update));
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableColumnAdjuster = new TableColumnAdjuster(this);
		tableColumnAdjuster.adjustColumns();
	}

	public void reload() {
		tableModel.fireTableDataChanged();
	}

	@Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(tableColumnAdjuster.getTotalWidth(), getRowHeight() * getModel().getRowCount());
    }
}
