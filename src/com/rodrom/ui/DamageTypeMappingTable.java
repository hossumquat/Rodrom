package com.rodrom.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.rodrom.DamageType;
import com.rodrom.DamageTypeMapping;

// Specialized table for handling viewing and editing a DamageTypeMapping instance in the editor
public class DamageTypeMappingTable extends JTable {
	private static final long serialVersionUID = 1L;

	// list of models so we can structureUpdate() them all if needed
	static public ArrayList<TableModel> modelList = new ArrayList<TableModel>();
	
	class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		public DamageTypeMapping mapping;

		public TableModel(DamageTypeMapping mapping) {
			this.mapping = mapping;
			DamageTypeMappingTable.modelList.add(this);
		}

		// structure changed, i.e. DamageType list was modified in editor
		public void structureUpdate() {
			fireTableDataChanged();
		}

		public int getColumnCount() {
			return 2;
		}

		public int getRowCount() {
			return DamageType.list.size();
		}

		public String getColumnName(int column) {
			if (column == 0)
				return "Damage Type";
			
			return "Value";
		}

		public Object getValueAt(int row, int column) {
			DamageType dt = DamageType.get(row);
			if (column == 0)
				return dt.name;
			
			if (mapping == null)
				return 0;

			return mapping.get(dt);
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
			DamageType dt = DamageType.get(row);
			if (column == 1 && mapping != null)
				mapping.set(dt, (int) value);

			fireTableCellUpdated(row, column);
		}
	}

	public TableColumnAdjuster tableColumnAdjuster;
	public TableModel tableModel;
	
	public DamageTypeMappingTable(DamageTypeMapping mapping) {
		super();
		setModel(tableModel = new TableModel(mapping));
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableColumnAdjuster = new TableColumnAdjuster(this);
		tableColumnAdjuster.adjustColumns();
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
