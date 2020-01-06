package com.rodrom.ui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class Utility {
	static public String[] sizeText = { "Tiny", "Very Small", "Small", "Normal", "Large", "Very Large", "Huge" };

	static public ImageIcon loadIcon(String path) {
		ImageIcon icon = null;

		try {
			File file = new File(path);
			icon = new ImageIcon(ImageIO.read(file));

		} catch (IOException e) {
			e.printStackTrace();
			icon = null;
		}

		return icon;
	}

	static public JButton iconButton(String path, String size) {
		JButton button = new JButton("", loadIcon(path));
		button.putClientProperty("JComponent.sizeVariant", size);
		button.setFocusable(false);
		return button;
	}

	// Capitalize first letter of a string, return it
	static public String capitalize(String src) {
		if (src == null || src.length() < 1)
			return src;

		if (src.length() == 1)
			return src.substring(0, 1).toUpperCase();

		return src.substring(0, 1).toUpperCase() + src.substring(1);
	}

	static public String alignmentText(int alignment) {
		if ((alignment & 1) != 0)
			return "Righteous"; // or Virtuous

		if ((alignment & 2) != 0)
			return "Neutral";

		return "Selfish";
	}

	static class IntVerifier extends InputVerifier {
		public int value, min, max;

		public IntVerifier(int min, int max, JComponent comp) {
			this.min = min;
			this.max = max;
			if (comp != null)
				comp.setToolTipText("Valid range is " + min + " to " + max);
		}

		public boolean verify(JComponent comp) {
			boolean valid = false;
			JTextField textField = (JTextField) comp;

			try {
				value = Integer.parseInt(textField.getText());
				if (value >= min && value <= max)
					valid = true;

			} catch (NumberFormatException e) {
				valid = false;
			}

			return valid;
		}
	}

	static class DoubleVerifier extends InputVerifier {
		public double value, min, max;

		public DoubleVerifier(double min, double max, JComponent comp) {
			this.min = min;
			this.max = max;
			if (comp != null)
				comp.setToolTipText("Valid range is " + min + " to " + max);
		}

		public boolean verify(JComponent comp) {
			boolean valid = false;
			JTextField textField = (JTextField) comp;

			try {
				value = Double.parseDouble(textField.getText());
				if (value >= min && value <= max)
					valid = true;

			} catch (NumberFormatException e) {
				valid = false;
			}

			return valid;
		}
	}
}
