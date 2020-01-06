package com.rodrom.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import com.rodrom.Game;
import com.rodrom.Input;
import com.rodrom.MainWindow;

public class GameLayoutPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static int layout = 2;

	public class LineBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;
		final static private int thickness = 1;

		// Paints a black line on the right side of the component
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			if (thickness > 0) {
				Color oldColor = g.getColor();
				g.setColor(Color.black);
				x += width - thickness;
				g.drawRect(x, y, thickness, height);
				g.setColor(oldColor);
			}
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.set(0, 0, 0, thickness);
			return insets;
		}
	}

	public class PaneScroller extends JScrollPane {
		private static final long serialVersionUID = 1L;

		public PaneScroller(Component view) {
			super(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
	}

	public GameLayoutPanel() {
		super(new GridLayout(1, 1));
		setBackground(MainWindow.backgroundColor);

		JTabbedPane tabbedPane1 = new JTabbedPane();
		tabbedPane1.setFocusable(false);
//		tabbedPane1.setMinimumSize(new Dimension(350, 400));
//		tabbedPane1.setPreferredSize(new Dimension(350, 400));
//		tabbedPane1.setMaximumSize(new Dimension(350, Short.MAX_VALUE));

		tabbedPane1.addTab("Character", new PaneScroller(new StatsTab()));
		tabbedPane1.addTab("Items", new ItemTab());
		tabbedPane1.addTab("Spells", new SpellTab());
//		tabbedPane1.addTab("Guild", new GuildTab());
//		tabbedPane1.addTab("Buffs", new BufferTab());
		tabbedPane1.addTab("Look", new LookTab());
		tabbedPane1.addTab("Info", new InfoTab());

/*		JTabbedPane tabbedPane2 = new JTabbedPane();
		tabbedPane2.setFocusable(false);
		tabbedPane2.setMaximumSize(new Dimension(350, Short.MAX_VALUE));

		tabbedPane2.addTab("Items", new ItemTab());
		tabbedPane2.addTab("Spells", new SpellTab());
		tabbedPane2.addTab("Companions", new CompanionsTab());*/

		JScrollPane scrollPane1 = new JScrollPane(Game.combatLog = new MessageText());
		JScrollPane scrollPane2 = new JScrollPane(Game.messageLog = new MessageText());
		JSplitPane logs = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane1, scrollPane2);
		logs.setOneTouchExpandable(true);
		removeArrowKeyBindings(logs);

		Game.dungeonView = new DungeonViewPanel();
		Game.creaturesView = new CreaturesViewPanel();
		Game.mapView = new MapViewPanel(Game.map);

		/*
		 * JPanel container = new JPanel(new GridLayout(1, 1));
		 * container.add(Game.creaturesView);
		 * 
		 * Box box2b2 = Box.createVerticalBox(); if (layout == 1) box2b2.add(new
		 * PartyTable());
		 * 
		 * box2b2.add(scrollPane);
		 */

//		Box box2a = Box.createHorizontalBox();
		JSplitPane box2a = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Game.dungeonView, Game.mapView);
		removeArrowKeyBindings(box2a);
//		box2a.add(Game.dungeonView);
//		box2a.add(container);
//		box2a.add(Game.mapView);

//		JPanel box2b = new JPanel(new SquareMapLayout());
//		box2b.add(Game.mapView);
//		box2b.add(box2b2);

		Box box1 = Box.createVerticalBox();
		box1.setPreferredSize(new Dimension(350, Short.MAX_VALUE));
		box1.setMaximumSize(new Dimension(350, Short.MAX_VALUE));
		box1.setBorder(new LineBorder());
		PartyTable pt = new PartyTable();
		JScrollPane scrollPane = new JScrollPane(pt);
		Border border = scrollPane.getBorder();
		pt.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				border));
		
		scrollPane.remove(pt);  // got the border, now get rid of the scrollPane.
		box1.add(pt);
		box1.add(tabbedPane1);
//		box1.add(tabbedPane2);

//		Box split1 = Box.createVerticalBox();
//		split1.add(box2a);
//		split1.add(scrollPane);
		JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, box2a, logs);
		removeArrowKeyBindings(split1);

		Box box2 = Box.createVerticalBox();
//		JPanel box2 = new JPanel(new VerticalLayout());
		box2.add(split1);
		System.out.println(split1.getInsets());
		System.out.println(box2a.getInsets());
		/*
		 * box2.add(box2a); box2.add(box2b);
		 */

		Box box = Box.createHorizontalBox();
		box.add(box1);
		box.add(box2);
//		box.add(split1);

		add(box);
		Input.addGameActions(this);
	}

	public void removeArrowKeyBindings(JComponent component) {
		InputMap im = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "none");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "none");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "none");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "none");
	}
}
