package model;

import java.util.List;

public class Matrice {
	private static int[][] m;
	private static Position maxPosition;
	private static int score;
	private static int sizeF1;
	private static int sizeF2;
	
	public static void updateMatrice(List<ADN> f1, List<ADN> f2) {
		update(f1, f2);
		calcul(f1, f2);
		getMaxScore(f1, f2);
	}
	
	public static void getMaxScore(List<ADN> f1, List<ADN> f2) {		
		score = 0;
		maxPosition = new Position(sizeF1 - 1, 0);
		
		for(int i = 1; i < sizeF1; i++) {
			for(int j = 1; j < sizeF2; j++) {
				if((i == f1.size() || j == f2.size()) && m[i][j] > score) { // si c'est la dernière ligne ou la dernière colonne, on peut déjà regardé où se trouve le maximum
					score = m[i][j];
					maxPosition.setX(i);
					maxPosition.setY(j);
				}
			}
		}
	}
	
	public static void calcul(List<ADN> f1, List<ADN> f2) {
		m = new int[sizeF1][sizeF2];

		for(int i = 1; i < sizeF1; i++) {
			for(int j = 1; j < sizeF2; j++) {
				m[i][j] = max(
							m[i-1][j] + ADN.GAP,
							m[i][j-1] + ADN.GAP,
							m[i-1][j-1] + match(f1.get(i-1), f2.get(j-1))
				);
			}
		}	
	}
	
	public static void update(List<ADN> f1, List<ADN> f2) {
		sizeF1 = f1.size() + 1;
		sizeF2 = f2.size() + 1;
	}
	
	/**
	 * Retourne le maximum entre a, b ou c
	 */
	private static int max(int a, int b, int c) {
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
	private static int match(ADN a, ADN b) {
		return a == b ? ADN.MATCH : ADN.MISMATCH;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static int[][] getMatrice() {
		return m;
	}
	
	public static Position getMaxPosition() {
		return maxPosition;
	}
}
