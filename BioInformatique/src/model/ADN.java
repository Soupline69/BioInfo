package model;

/**
 * Représente un nucléobase ou un lien
 */
public interface ADN {
	public final int GAP = -2;
	public final int MATCH = 1;
	public final int MISMATCH = -1;
	
    char toChar();
	
	public static ADN fromChar(char c) {
        switch (c) {
        case 'a':
            return Nucleobase.A;
        case 't':
        	return Nucleobase.T;
        case 'c':
        	return Nucleobase.C;
        case 'g': 
        	return Nucleobase.G;
        case '-':
            return Lien.GAP;
        case '_':
            return Lien.NONE;
        default:
            throw new IllegalArgumentException("Représentation d'ADN inconnue : " + c);
        }
    }
	
	public static ADN complementaire(char c) {
		switch(c) {
		case 'a':
			return Nucleobase.T;
		case 't':
			return Nucleobase.A;
		case 'c':
			return Nucleobase.G;
		case 'g':
			return Nucleobase.C;
		default:
			return null;
		}
	}
}
