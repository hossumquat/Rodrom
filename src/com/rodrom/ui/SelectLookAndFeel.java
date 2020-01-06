/*
 *  This basically implements a JComboBox for selecting a LookAndFeel for
 *  everything.  Create an instance and then add the public ComboBox that
 *  is created to the parent JComponent.
 */

package com.rodrom.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.rodrom.Game;
import com.rodrom.MainWindow;

public class SelectLookAndFeel implements ActionListener {
	public JComboBox<String> comboBox;
	public UIManager.LookAndFeelInfo[] infoList;
	
	public SelectLookAndFeel() {
		int select = -1;
		
		infoList = UIManager.getInstalledLookAndFeels();
		String[] list = new String[infoList.length];

		LookAndFeel current = UIManager.getLookAndFeel();
		String name = current.getName();

		for (int i=0; i<infoList.length; i++) {
			list[i] = infoList[i].getName();
			if (list[i].equals(name))
				select = i;
		}

		comboBox = new JComboBox<String> (list);
		comboBox.addActionListener(this);
		if (select > 0) {
			comboBox.setSelectedIndex(select);
			Game.lookAndFeel = infoList[select].getClassName();
		}
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		int index = comboBox.getSelectedIndex();
		if (index < 0)
			return;
		
		String name = infoList[index].getClassName();

		try {
			UIManager.setLookAndFeel(name);
			Game.lookAndFeel = name;

		} catch (ClassNotFoundException ex) {
			System.out.println(ex);
		} catch (InstantiationException ex) {
			System.out.println(ex);
		} catch (IllegalAccessException ex) {
			System.out.println(ex);
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println(ex);
		}
		
		SwingUtilities.updateComponentTreeUI(MainWindow.frame);
	}
}
