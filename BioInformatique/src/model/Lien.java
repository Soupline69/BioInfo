package model;

public enum Lien implements ADN {
    GAP, NONE;

    @Override
    public char toChar() {
        switch (this) {
        case GAP:
            return '-';
        case NONE:
            return '_';
        default:
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(toChar());
    }
}