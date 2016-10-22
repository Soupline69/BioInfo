package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReaderBuffer {
	
	public static CollectionBuffer getCollection(String file) {
		return new CollectionBuffer(getFragments(getBuffer(file)));
	}
	
	private static List<StringBuffer> getFragments(BufferedReader buffer) {
		List<StringBuffer> fragments = new ArrayList<>();
		
		Iterator<String> it = buffer.lines().iterator();
		it.next();
		while(it.hasNext()) {
			StringBuffer fragment = new StringBuffer();
			String current = it.next();
			
			while(!current.startsWith(">") && it.hasNext()) {
				fragment.append(current);
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
