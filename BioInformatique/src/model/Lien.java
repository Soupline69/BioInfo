package model;

/**
 * Représente un 'lien' qui peut être un gap (-) ou un none (_) qui est un gap de début ou de fin mais qui n'est pas pénalisé contrairement au gap
 */
public enum Lien implements ADN {
    GAP, NONE;

    @Override
    public char toChar() {
        switch (this) {
	        case NONE:
	            return '_';
	        default:
	            return '-';
        }
    }

    @Override
    public String toString() {
        return String.valueOf(toChar());
    }
}