package model;

public class MatriceBuffer {
	private int[][] m;
	private int countLigne;
	private int countColonne;
	
	public MatriceBuffer(StringBuffer f1, StringBuffer f2) {
		countLigne = f1.length() + 1;
		countColonne = f2.length() + 1;
		m = new int[countLigne][countColonne];
	}
	
	/** 
	 * Calcul la matrice d'alignement semi-global entre les fragments f1 et f2 
	 * Renvoie la position (ligne, colonne) du maximum sur la derniere ligne/colonne de la matrice
	 */
	public Position calcul(StringBuffer f1, StringBuffer f2, int gap) {
		Position posMax = new Position(-1, -1);
		int max = 0; 
		
		for(int i = 1; i < countLigne; i++) {
			for(int j = 1; j < countColonne; j++) {
				m[i][j] = max(
							m[i-1][j] + gap,
							m[i][j-1] + gap,
							m[i-1][j-1] + match(f1.charAt(i-1), f2.charAt(j-1))
						);
				
				if(i == countLigne - 1 || j == countColonne - 1) { // si c'est la dernière ligne ou la dernière colonne, on peut déjà regardé où se trouve le maximum
					if(m[i][j] > max) {
						max = m[i][j];
						posMax.setX(i);
						posMax.setY(j);
					}
				}
			}
		}
		
		return max == 0 ? new Position(countLigne - 1, 0) : posMax;
	}
	
	/**
	 * Retourne le maximum entre a, b ou c
	 */
	private int max(int a, int b, int c) {
		if(a >= b && a >= c) {
			return a;
		} else if(b >= a && b >= c) {
			return b;
		} else {
			return c;
		}
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	private int match(char a, char b) {
		return a == b ? 1 : -1;
	}
	
	public void display() {
		for(int i = 0; i < countLigne; i++) {
			for(int j = 0; j < countColonne; j++) {		
				System.out.print(m[i][j]+"  ");
			}
			System.out.println("");
		}
	}
	
	public int getCountLigne() {
		return countLigne;
	}
	
	public int getCountColonne() {
		return countColonne;
	}
	
	public int[][] getMatrice() {
		return m;
	}
	
}