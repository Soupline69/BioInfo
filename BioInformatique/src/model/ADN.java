package model;

/**
 * Represente un nucleobase ou un lien
 */
public interface ADN {
	int GAP = -2;
	int MATCH = 1;
	int MISMATCH = -1;
	
    char toChar();
	
	static ADN fromChar(char c) {
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
            throw new IllegalArgumentException("Representation d'ADN inconnue : " + c);
        }
    }
	
	static ADN complementaire(char c) {
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
