package com.rodrom.ui;

import java.awt.Font;

import javax.swing.JTextArea;

import com.rodrom.MainWindow;

public class MessageText extends JTextArea {
	private static final long serialVersionUID = 1L;

	public MessageText() {
		super(5, 30);
		setBackground(MainWindow.backgroundColor);
		setEditable(false);
		setFont(getFont().deriveFont(Font.BOLD));
	}

	public void addMessage(String msg) {
		append(msg + "\n");
	}
	// textArea = new JTextArea(5, 30);
	// JScrollPane scrollPane = new JScrollPane(textArea);
	// add(scrollPane, BorderLayout.CENTER);
	// textArea.setEditable(false);
}
