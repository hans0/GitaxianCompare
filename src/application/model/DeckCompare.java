package application.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A Comparator to sort by name after I've inserted the number into the String
class SortByName implements Comparator<String> {
	public int compare(String s1, String s2) {
		int index1 = s1.indexOf(' ');
		int index2 = s2.indexOf(' ');
		return s1.substring(index1).trim().compareTo(s2.substring(index2).trim());
	}
}


public class DeckCompare {
	private HashMap<String, Integer> oldDeckHashmap;
	private HashMap<String, Integer> newDeckHashmap;

	String oldDeckFilepath;
	String newDeckFilepath;
	
	public boolean old;
	
	public DeckCompare() {
		oldDeckHashmap = new HashMap<String, Integer>();
		newDeckHashmap = new HashMap<String, Integer>();
		// should really fix this
		oldDeckFilepath = System.getProperty("user.home") + "/decks/oldDeck.txt";
		newDeckFilepath = System.getProperty("user.home") + "/decks/newDeck.txt";
		old = true;
	}
	
	
	public boolean isWindows() {
		return System.getProperty("os.name").indexOf("Windows") != -1;
	}
	
	public boolean isMacOS() {
		return System.getProperty("os.name").indexOf("Mac") != -1;
	}
	
	public boolean isLinux() {
		return false;
	}
	
	public HashMap<String, Integer> getOldDeckHashmap() {
		return this.oldDeckHashmap;
	}
	
	public HashMap<String, Integer> getNewDeckHashmap() {
		return this.newDeckHashmap;
	}
	
	
	// Need to add methods to flush the Hashmap
	
	/*
	public void switchFile() {
		this.old = !this.old;
	}
	*/
	
	public File downloadDeck(String s, boolean old) {
		File result = null;
		URL website = null;
		try {
			website = new URL(s);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos;
			if (old) fos = new FileOutputStream(this.oldDeckFilepath);
			else fos = new FileOutputStream(this.newDeckFilepath);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		if (old)
			result = new File(this.oldDeckFilepath);
		else 
			result = new File(this.newDeckFilepath);
		
		return result;
	}
	
	public boolean loadDeck(File f, boolean old) {
		Scanner sc;
		
		// This is a regex pattern to extract the numbers
		Pattern p = Pattern.compile("\\d+");
		Matcher m;
		HashMap<String, Integer> current;
		
		if (old) current = oldDeckHashmap;
		else current = newDeckHashmap;
		
		current.clear();

		
		String currentLine, currentCard;
		Integer i;
		int x;
		
		// Try-catch block because we're not sure that the file exists
		try {
			sc = new Scanner(f);
			// standard loop for a Scanner object iterating through a file
			while (sc.hasNextLine()) {
				i = 0;
				currentLine = sc.nextLine();
				// To deal with MTGO .dec files, which append SB: to every card 
				if (currentLine.indexOf("SB:") == 0) {
					currentLine = currentLine.substring(3).trim();
				}
				// We're going to hit blank lines
				if (currentLine.equals("") || currentLine.equals("\n") || currentLine.indexOf("//") != -1){
					continue;
				}
				// This is how we regex
				m = p.matcher(currentLine);

				if (m.find()) {
					// This is where we get the number.
					i = Integer.parseInt(m.group());
				}
				
				currentCard = currentLine.substring(2).trim();
				if (currentCard.indexOf(']') != -1) {
					currentCard = currentCard.substring(1+currentCard.indexOf(']')).trim();
				}
				
				// Storing the first deck in a HashMap
				if (i != 0) {
					if (current.containsKey(currentCard)) {
						x = current.get(currentCard);
						x += i;
						current.put(currentCard, x);
					} else {
						current.put(currentCard, i);
					}
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String compare() {
		StringBuilder result = new StringBuilder();
		int n, o;
		ArrayList<String> additions = new ArrayList<String>();
		ArrayList<String> deletions = new ArrayList<String>();
		Set<String> set = newDeckHashmap.keySet();
		for (String s: set) {
			
			if (!oldDeckHashmap.containsKey(s)) {
				additions.add(newDeckHashmap.get(s) + " " + s);
			} else if (oldDeckHashmap.containsKey(s)) {
				o = oldDeckHashmap.get(s);
				n = newDeckHashmap.get(s);
				if (o != n) {
					if (o > n) {
						//System.out.println("||" + s + "||");

						deletions.add((n - o) + " " + s);
					} else {
						additions.add((n - o) + " " + s);
					}
				}
			}
		}
		
		set = oldDeckHashmap.keySet();

		for (String s: set) {
			if (!newDeckHashmap.containsKey(s)) {
				deletions.add(0 - oldDeckHashmap.get(s) + " " + s);
			}
		}
		Collections.sort(additions, new SortByName());
		Collections.sort(deletions, new SortByName());
				
		for (String s: additions) result.append(s + '\n');
		for (String s: deletions) result.append(s + '\n');
		
		return result.toString();
	}
}
