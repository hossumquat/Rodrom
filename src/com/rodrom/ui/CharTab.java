package com.rodrom.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CharTab extends JPanel {
	private static final long serialVersionUID = 1L;

	public CharTab() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(createPane(3, "One ", Color.red));
        this.add(createPane(3, "Two ", Color.green));
        this.add(createPane(4, "Three ", Color.blue));
    }

    private JPanel createPane(int n, String s, Color c) {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(BorderFactory.createLineBorder(c, 2));

        for (int i = 0; i < n; i++) {
//            JTextField tf = new JTextField("Stackoverflow!", 32);

            JLabel label = new JLabel(s + i + ":", JLabel.RIGHT);
            label.setPreferredSize(new Dimension(80, 32));

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
            inner.add(label);
//            inner.add(tf);

            outer.add(inner);
        }
        return outer;
    }
}
