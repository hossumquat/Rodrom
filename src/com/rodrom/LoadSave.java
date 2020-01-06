package com.rodrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rodrom.ui.DungeonViewPanel;

// The master file (master.dat) stores all static data, which is data that doesn't
// change from normal gameplay.  It is only changed via the editor.  Therefore,
// this file lives in a jar file.
//
// The data file (save.dat) stores all dynamic data, which is data that changes
// to reflect the current state of the game.  On first run this file will not
// exist yet.  This means you could just delete this file to reset back to
// a fresh install state.

public class LoadSave {
	final static private String masterPathname = "data/master.dat";
	final static private String masterBackupPathname = "data/master.bak";
	final static private String dataPathname = "data/save.dat";
	final static private String dataBackupPathname = "data/save.bak";

	// core of the load, minus the overhead
	static private void simplifiedLoadMaster(DataInputStream in) throws IOException {
		DamageType.loadList(in);
		Race.loadList(in);
		Guild.loadList(in);
	}

	static private void simplifiedLoadData(DataInputStream in) throws IOException {
		Character.loadList(in);
	}
	
	// core of the save, minus the overhead
	static private void simplifiedSaveMaster(DataOutputStream out) throws IOException {
		DamageType.saveList(out);
		Race.saveList(out);
		Guild.saveList(out);
	}

	static private void simplifiedSaveData(DataOutputStream out) throws IOException {
		Character.saveList(out);
	}

	static public void loadMaster() {
		try (DataInputStream in = new DataInputStream(new FileInputStream(masterPathname))) {
			simplifiedLoadMaster(in);

		} catch (IOException e) {
			String message = e.getMessage();
			if (!message.substring(0, masterPathname.length()).equals(masterPathname))
				message = masterPathname + " (" + message + ")";
			
			System.out.println("Failed to load " + message);
			System.out.println("Trying " + masterBackupPathname);

			try (DataInputStream in = new DataInputStream(new FileInputStream(masterBackupPathname))) {
				simplifiedLoadMaster(in);

			} catch (IOException e2) {
				message = e2.getMessage();
				if (!message.substring(0, masterBackupPathname.length()).equals(masterBackupPathname))
					message = masterBackupPathname + " (" + message + ")";
				
				System.out.println("Failed to load " + message);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	static public void saveMaster() {
		File file = null;
		Path src = null, dest = null;
		DataOutputStream out = null;
		
		try {
			file = File.createTempFile("master", null);
			out = new DataOutputStream(new FileOutputStream(file));
			simplifiedSaveMaster(out);
			out.close();
			System.out.println("Saved to " + file.getAbsolutePath());

			src = Paths.get(masterPathname);
			if (Files.exists(src)) {
				dest = Paths.get(masterBackupPathname);
				Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Copy " + src + " to " + dest + " successful");
			}
			
			dest = src;
			src = Paths.get(file.getAbsolutePath());
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Copy " + src + " to " + dest + " successful");

		} catch (IOException e) {
			String text = "Failed to save " + e.getMessage();
			JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (out != null) {
				try {
					out.close();

				} catch (IOException e) {
					System.out.println("Failed to close " + file.getAbsolutePath() + " in LoadSave.save()");
				}
			}
			
			if (file != null) {
				try {
					Files.deleteIfExists(Paths.get(file.getAbsolutePath()));

				} catch (IOException e) {
					System.out.println("Failed to delete tempfile " + file.getAbsolutePath());
				}
			}
		}
	}

	static public void loadData() {
		try (DataInputStream in = new DataInputStream(new FileInputStream(dataPathname))) {
			simplifiedLoadData(in);

		} catch (IOException e) {
			String message = e.getMessage();
			if (!message.substring(0, dataPathname.length()).equals(dataPathname))
				message = dataPathname + " (" + message + ")";
			
			System.out.println("Failed to load " + message);
			System.out.println("Trying " + dataBackupPathname);

			try (DataInputStream in = new DataInputStream(new FileInputStream(dataBackupPathname))) {
				simplifiedLoadMaster(in);

			} catch (IOException e2) {
				message = e2.getMessage();
				if (!message.substring(0, dataBackupPathname.length()).equals(dataBackupPathname))
					message = dataBackupPathname + " (" + message + ")";
				
				System.out.println("Failed to load " + message);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	static public void saveData() {
		File file = null;
		Path src = null, dest = null;
		DataOutputStream out = null;
		
		try {
			file = File.createTempFile("save", null);
			out = new DataOutputStream(new FileOutputStream(file));
			simplifiedSaveData(out);
			out.close();
			System.out.println("Saved to " + file.getAbsolutePath());

			src = Paths.get(dataPathname);
			if (Files.exists(src)) {
				dest = Paths.get(dataBackupPathname);
				Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Copy " + src + " to " + dest + " successful");
			}
			
			dest = src;
			src = Paths.get(file.getAbsolutePath());
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Copy " + src + " to " + dest + " successful");

		} catch (IOException e) {
			String text = "Failed to save " + e.getMessage();
			JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (out != null) {
				try {
					out.close();

				} catch (IOException e) {
					System.out.println("Failed to close " + file.getAbsolutePath() + " in LoadSave.save()");
				}
			}
			
			if (file != null) {
				try {
					Files.deleteIfExists(Paths.get(file.getAbsolutePath()));

				} catch (IOException e) {
					System.out.println("Failed to delete tempfile " + file.getAbsolutePath());
				}
			}
		}
	}

	public static void loadConfig() {
		FileReader reader = null;

		try {
			reader = new FileReader("config.json");
			JSONObject json = (JSONObject) new JSONParser().parse(reader);
			Game.lookAndFeel = (String) json.get("look and feel");
			DungeonViewPanel.setSize((int)(long) json.get("dungeon view scale"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (reader != null) {
			try {
				reader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveConfig() {
		PrintWriter writer = null;

		JSONObject json = new JSONObject();

		json.put("look and feel", Game.lookAndFeel);
		json.put("dungeon view scale", DungeonViewPanel.size);

		try {
			writer = new PrintWriter("config.json");
			writer.write(json.toJSONString());
			writer.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (writer != null)
			writer.close();
	}
}
