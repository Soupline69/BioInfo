package model;

public class Matrice {
	private int[][] m;
	private int countLigne;
	private int countColonne;
	
	public Matrice(String f1, String f2) {
		countLigne = f1.length() + 1;
		countColonne = f2.length() + 1;
		m = new int[countLigne][countColonne];
		calcul(f1, f2);
	}
	
	private void calcul(String f1, String f2) {
		for(int i = 1; i < countLigne; i++) {
			for(int j = 1; j < countColonne; j++) {
				m[i][j] = max(
							m[i-1][j] - 2, // Faire une constante avec le score d'un GAP
							m[i][j-1] - 2,
							m[i-1][j-1] + match(f1.charAt(i-1), f2.charAt(j-1))
						);
			}
		}
	}
	
	private int max(int a, int b, int c) {
		if(a >= b && a >= c) {
			return a;
		} else if(b >= a && b >= c) {
			return b;
		} else {
			return c;
		}
	}
	
	public int match(char a, char b) {
		if(a == b) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public void display() {
		for(int i = 0; i < countLigne; i++) {
			for(int j = 0; j < countColonne; j++) {		
				System.out.print(m[i][j]+"  ");
			}
			System.out.println("");
		}
	}
	
	public Position maxMatrice() {
		Position maxColonne = maxColonne();
		Position maxLigne = maxLigne();

		return maxLigne.getX() > maxColonne.getX() ? new Position(countLigne - 1, maxLigne.getY()) : new Position(maxColonne.getY(), countColonne - 1);
	}
	
	public Position maxColonne() {
		int maxColonne = 0;
		int ligne = -1;
		
		for(int i = 0; i < countLigne; i++) {	
			int comp = m[i][countColonne - 1];
			if(comp > maxColonne) {
				maxColonne = comp;
				ligne = i;
			}
		}
		
		return new Position(maxColonne, ligne);
	}
	
	public Position maxLigne() {
		int maxLigne = 0;
		int colonne = -1;
		
		for(int i = 0; i < countColonne; i++) {	
			int comp = m[countLigne - 1][i];
			if(comp > maxLigne) {
				maxLigne = comp;
				colonne = i;
			}
		}
		
		return new Position(maxLigne, colonne);
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