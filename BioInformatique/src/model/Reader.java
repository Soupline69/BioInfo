package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reader {
	
	public static Graphe getGraphe(String file) {
		return new Graphe(getFragments(getBuffer(file)));
	}
	
	private static List<Fragment> getFragments(BufferedReader buffer) {
		List<Fragment> fragments = new ArrayList<>();
		
		Iterator<String> it = buffer.lines().iterator();
		it.next();
		String fragment = "";
		while(it.hasNext()) {
			String current = it.next();
			
			if(!current.startsWith(">")) {
				fragment += current;
			} else {
				fragments.add(new Fragment(fragment));
				fragment = "";
			}
		}
		
		fragments.add(new Fragment(fragment));
		
		return fragments;
	}
	
	private static BufferedReader getBuffer(String file) {
		BufferedReader buffer = null;
		
        try {
			buffer = new BufferedReader(new FileReader(file));
        } catch(Exception e) {}
        
        return buffer;
    }

}
