package main;

public class Main {

	public static void main(String[] args) {
		// 1. Faire l'alignement semi-global avec deux fragments tels que :
		
		String f1 = "CAGCACTTGGATTCTCG"; // F1 = ATTAGACCATGCGGC 
		String f2 = "CAGCGTGG"; // F2 = ATCGGCATTCAGT
		
		int[][] m = new int[f1.length() + 1][f2.length() + 1];
		
		// Pour obtenir la matrice de l'exemple du pdf, ce serait la première étape // OK // 15/6 ou 6/15
		
		// Initialiser la matrice semi-global avec des 0 partout
		for(int i = 0; i < f1.length() + 1; i++) {
			for(int j = 0; j < f2.length() + 1; j++) {
				m[i][j] = 0;
			}
		}
		
		// Calculer le résultat de la matrice
		for(int i = 1; i < f1.length() + 1; i++) {
			for(int j = 1; j < f2.length() + 1; j++) {
				m[i][j] = max(
							m[i-1][j] - 2,
							m[i][j-1] - 2,
							m[i-1][j-1] + match(f1.charAt(i-1), f2.charAt(j-1))
						);
			}
		}
		
		// Afficher le résultat de la matrice 
		for(int i = 0; i < f1.length() + 1; i++) {
			for(int j = 0; j < f2.length() + 1; j++) {		
				System.out.print(m[i][j]+"  ");
			}
			System.out.println("");
		}
		
		// Trouver le maximum de la dernière ligne/dernière colonne et l'afficher
		// Colonne max
		int maxColonne = 0;
		int ligne = -1;
		for(int i = 0; i < f1.length() + 1; i++) {	
			int comp = m[i][f2.length()];
			if(comp > maxColonne) {
				maxColonne = comp;
				ligne = i;
			}
		}
		// Ligne max
		int maxLigne = 0;
		int colonne = -1;
		for(int i = 0; i < f2.length() + 1; i++) {	
			int comp = m[f1.length()][i];
			if(comp > maxLigne) {
				maxLigne = comp;
				colonne = i;
			}
		}
		
		if(maxLigne > maxColonne) {
			System.out.println("\n\nLe maximum est en ["+(f1.length())+"]["+colonne+"] avec "+maxLigne);
		} else {
			System.out.println("\n\nLe maximum est en ["+ligne+"]["+(f2.length())+"] avec "+maxColonne);
		}
		
	}
	
	public static int max(int a, int b, int c) {
		if(a >= b && a >= c) {
			return a;
		} else if(b >= a && b >= c) {
			return b;
		} else if(c >= a && c >= b) {
			return c;
		}
		
		return 10000;
	}
	
	public static int match(char a, char b) {
		if(a == b) {
			return 1;
		} else {
			return -1;
		}
	}

}
