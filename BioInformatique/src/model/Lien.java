package model;

/**
 * Represente un 'lien' qui peut etre un gap (-) ou un none (_) qui est un gap de debut ou de fin mais qui n'est pas penalise contrairement au gap
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