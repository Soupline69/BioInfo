package model;

import java.util.ArrayList;
import java.util.List;
// Un noeud du graphe overlap
public class Fragment {  
	private List<ADN> value;
	private List<ADN> inverse;
	private boolean isInverse;
	
	public Fragment(CharSequence nucleobases) {
		this.value = new ArrayList<>(nucleobases.length());
		inverse = new ArrayList<>(nucleobases.length());
		
	    for(int i = 0; i < nucleobases.length(); i++) {
	        this.value.add(ADN.fromChar(nucleobases.charAt(i)));
	        this.inverse.add(0, ADN.complementaire(nucleobases.charAt(i)));
	    }
	    
	    isInverse = false;
	}
	
	public int score(List<ADN> f) {
		int score = 0;
		int[][] m = getMatrice(f);
		
		for(int i = 1; i < f.size() + 1; i++) {
			if(m[value.size()][i] > score) {
				score = m[value.size()][i];
			}
		}
		
		for(int j = 1; j < value.size() + 1; j++) {
			if(m[j][f.size()] > score) {
				score = m[j][f.size()];
			}
		}
		
		return score;
	}
	
	public int[][] getMatrice(List<ADN> f) {
		int[][] m = new int[value.size() + 1][f.size() + 1];
		
		for(int i = 1; i < value.size() + 1; i++) {
			for(int j = 1; j < f.size() + 1; j++) {
				m[i][j] = max(
							m[i-1][j] + ADN.GAP,
							m[i][j-1] + ADN.GAP,
							m[i-1][j-1] + match(value.get(i-1), f.get(j-1))
				);
			}
		}
		
		return m;
	}
	
	/**
	 * Retourne le maximum entre a, b ou c
	 */
	private int max(int a, int b, int c) {
		if(a >= b && a >= c) {
			return a;
		} else if(b >= a && b >= c) {
			return b;
		} else {
			return c;
		}
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	private int match(ADN a, ADN b) {
		return a == b ? ADN.MATCH : ADN.MISMATCH;
	}
	
	public List<ADN> getValue() {
		return value;
	}
	
	public List<ADN> getInverse() {
		return inverse;
	}
	
	public boolean getIsInverse() {
		return isInverse;
	}
}
