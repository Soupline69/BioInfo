package model;

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
	        default:
	        	return 0;
		}
	}
    
    @Override
    public String toString() {
    	return String.valueOf(toChar());
    }

}