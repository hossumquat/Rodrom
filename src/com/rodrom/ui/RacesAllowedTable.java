package com.rodrom.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.rodrom.Race;

public class RacesAllowedTable extends JTable {
	private static final long serialVersionUID = 1L;

	// list of models so we can structureUpdate() them all if needed
	static public ArrayList<TableModel> modelList = new ArrayList<TableModel>();
	
	class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		public ArrayList<Race> list;
		
		public TableModel() {
			list = null;
			RacesAllowedTable.modelList.add(this);
		}

		// structure changed, i.e. DamageType list was modified in editor
		public void structureUpdate() {
			fireTableDataChanged();
		}

		public int getColumnCount() {
			return 2;
		}

		public int getRowCount() {
			return Race.list.size();
		}

		public String getColumnName(int column) {
			if (column == 0)
				return "Race";
			
			return "Allowed";
		}

		public Object getValueAt(int row, int column) {
			Race race = Race.list.get(row);
			if (column == 0)
				return race.name;
			
			if (list == null)
				return false;

			return list.contains(race);
		}

		public Class<?> getColumnClass(int column) {
			if (column == 0)
				return String.class;
			
			return Boolean.class;
		}

		public boolean isCellEditable(int row, int column) {
			return column > 0;
		}

		public void setValueAt(Object value, int row, int column) {
			Race race = Race.list.get(row);
			if (column == 1 && race != null && list != null) {
				if ((boolean) value) {
					if (!list.contains(race))
						list.add(race);

				} else {
					if (list.contains(race))
						list.remove(race);
				}
			}

			fireTableCellUpdated(row, column);
		}
	}

	public TableColumnAdjuster tableColumnAdjuster;
	public TableModel tableModel;
	
	public RacesAllowedTable() {
		super();
		setModel(tableModel = new TableModel());
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableColumnAdjuster = new TableColumnAdjuster(this);
		tableColumnAdjuster.adjustColumns();
	}
	
	public void set(ArrayList<Race> list) {
		tableModel.list = list;
		reload();
	}

	public void structureUpdate() {
		for (TableModel model : modelList)
			model.structureUpdate();
	}

	public void reload() {
		tableModel.fireTableDataChanged();
	}

	@Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(tableColumnAdjuster.getTotalWidth(), getRowHeight() * getModel().getRowCount());
    }
}
