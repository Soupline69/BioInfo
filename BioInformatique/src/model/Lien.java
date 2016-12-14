package model;

/**
 * Repr�sente un 'lien' qui peut �tre un gap (-) ou un none (_) qui est un gap de d�but ou de fin mais qui n'est pas p�nalis� contrairement au gap
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