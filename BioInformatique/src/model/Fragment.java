package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un noeud du graphe
 */
public class Fragment {  
	private List<ADN> value;
	private List<ADN> inverse;
	private int isInverse;
	private boolean in;
	private boolean out;
	
	public Fragment(CharSequence nucleobases) {
		value = new ArrayList<>(nucleobases.length());
		inverse = new ArrayList<>(nucleobases.length());

		for(int i = 0; i < nucleobases.length(); i++) {
	        value.add(ADN.fromChar(nucleobases.charAt(i)));
	        inverse.add(0, ADN.complementaire(nucleobases.charAt(i)));
	    }
	    
	    isInverse = -1;
	}
	
	public List<ADN> getValue() {
		return value;
	}
	
	public List<ADN> getInverse() {
		return inverse;
	}
	
	public int isInverse() {
		return isInverse;
	}
	
	/**
	 * Si un fragment est inversé/complémenté alors sa valeur devient la valeur inverse/complémenté (ce qui nous permet de juste utilisé la méthode 'getValue()' pour les alignements)
	 */
	public void setIsInverse(int isInverse) {
		this.isInverse = isInverse;
		
		if(isInverse == 1) {
			value = inverse;
		}
	}
	
	public boolean in() {
		return in;
	}
	
	public void setIn(boolean in) {
		this.in = in;
	}
	
	public boolean out() {
		return out;
	}
	
	public void setOut(boolean out) {
		this.out = out;
	}
	
}
