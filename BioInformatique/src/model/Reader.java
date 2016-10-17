package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reader {
	
	public static Collection getCollection(String file) {
		return new Collection(getFragments(getBuffer(file)));
	}
	
	private static List<String> getFragments(BufferedReader buffer) {
		List<String> fragments = new ArrayList<>();
		
		Iterator<String> it = buffer.lines().iterator();
		it.next();
		while(it.hasNext()) {
			String fragment = "";
			String current = it.next();
			
			while(!current.startsWith(">") && it.hasNext()) {
				fragment = fragment.concat(current);
				current = it.next();
			}
				
			fragments.add(fragment);
		}
		
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
