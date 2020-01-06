package com.rodrom.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class PartyTable extends JPanel {
	private static final long serialVersionUID = 1L;

	public JTable table;
	public PartyTableModel model;
	public TableColumnAdjuster tableColumnAdjuster;

	public PartyTable() {
		super();

		model = new PartyTableModel();
		table = new JTable(model);

		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		table.getTableHeader().setFocusable(false);
		add(table, BorderLayout.CENTER);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setFocusable(false);
		table.setRowSelectionInterval(0, 0);

		// TODO: get first column to take up excess space in the table
		TableColumnModel columnModel = table.getColumnModel();
		for (int i=0; i<model.getColumnCount(); i++)
			System.out.println("Column " + i + " = " + columnModel.getColumn(i).getWidth());

		tableColumnAdjuster = new TableColumnAdjuster(table);
		tableColumnAdjuster.adjustColumns();
/*		TableColumnModel tcm = table.getColumnModel();
		for (int i = 1; i < tcm.getColumnCount(); i++)
			tableColumnAdjuster.adjustColumn(i);*/
//		table.getTableHeader().setResizingColumn(aColumn);

		// set all columns except the first (name) column to centered text
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int i = 1; i < model.getColumnCount(); i++)
			columnModel.getColumn(i).setCellRenderer(centerRenderer);
	}

	public Dimension getMaximumSize() {
		Dimension hsize = table.getTableHeader().getPreferredSize();
		Dimension tsize = table.getPreferredSize();

		return new Dimension(Short.MAX_VALUE, hsize.height + tsize.height);
	}
}
