package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Permet de lire un fichier .fasta et d'écrire les résultats dans deux fichiers .fasta
 */
public class Parser {
	
	public static List<Fragment> getFragments(String file) {
		BufferedReader buffer = getBufferReader(file);
		
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
	
	public static void write(String fileCollection, String fileNormal, String fileIC, Fragment supersequence) {
		String nomCollection = getNomCollection(fileCollection);
		BufferedWriter bufferNormal = getBufferWriter(fileNormal);
		BufferedWriter bufferInverse = getBufferWriter(fileIC);
		
		try {
			bufferNormal.write("> Groupe7 Collection "+nomCollection+" Longueur "+supersequence.getValue().size());
			bufferInverse.write("> Groupe7 Collection "+nomCollection+" Longueur "+supersequence.getValue().size());

			for(int i = 0; i < supersequence.getValue().size(); i++) {
				if(i % 60 == 0) {
					bufferNormal.write("\n");
					bufferInverse.write("\n");
				}
				
				bufferNormal.write(String.valueOf(supersequence.getValue().get(i).toChar()));
				bufferInverse.write(String.valueOf(supersequence.getInverse().get(i).toChar()));
			}
			
			bufferNormal.close();
			bufferInverse.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getNomCollection(String file) {
		String[] split = file.split("/");
		return split[split.length - 1];
	}
	
	private static BufferedReader getBufferReader(String file) {
		BufferedReader buffer = null;
		
        try {
			buffer = new BufferedReader(new FileReader(file));
        } catch(Exception e) {}
        
        return buffer;
    }
	
	private static BufferedWriter getBufferWriter(String file) {
		BufferedWriter buffer = null;
		
        try {
			buffer = new BufferedWriter(new FileWriter(file));
        } catch(Exception e) {}
        
        return buffer;
	}

}
