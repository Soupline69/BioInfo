package model;

import java.util.Random;

/**
 * Represente un nucleobase qui peut etre A, T, C, G
 */
public enum Nucleobase implements ADN {
    A, C, G, T;

    @Override
	public char toChar() {
		switch (this) {
	        case A:
	            return 'a';
	        case C:
	            return 'c';
	        case G:
	        	return 'g';
	        case T:
	        	return 't';
	        default :
	        	return 0;
		}
	}
    
    /**
     * Retourne un nucleobase au hasard
     */
    public static char random() {
    	int i = new Random().nextInt(4);
    	
    	switch (i) {
	    	case 0 : 
	    		return 'a';
	    	case 1 : 
	    		return 't';
	    	case 2 : 
	    		return 'c';
	    	default :
	    		return 'g';
    	}
    }
    
    @Override
    public String toString() {
    	return String.valueOf(toChar());
    }

}