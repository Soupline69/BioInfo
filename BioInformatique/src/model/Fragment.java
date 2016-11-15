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
