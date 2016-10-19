package model;

public class Alignement {
	private final int GAP = -2;
	private String f1;
	private String f2;
	private int score = 0;
		
	public Alignement(String f1, String f2) {		
		String[] f = check(f1, f2).split("&");
		String[] alignement = alignementSemiGlobal(f[0], f[1]).split("&");
		this.f1 = alignement[0];
		this.f2 = alignement[1];
	}
	
	/** 
	 * Aligne sur base de l'alignement semi-global les fragments f1 et f2
	 */
	public String alignementSemiGlobal(String f1, String f2) {
		Matrice m = new Matrice(f1, f2);
		Position maxPosition = m.calcul(f1, f2, GAP);
		
		if(maxPosition.getX() == m.getCountLigne() - 1) { // Si le maximum est sur la ligne
			System.out.println("Le maximum est en ["+(m.getCountLigne() - 1)+"]["+maxPosition.getY()+"] (ligne)");
			return alignementLigne(f1, f2, m.getCountLigne() - 1, maxPosition.getY(), m.getMatrice());
		} else { // Sinon le maximum est sur la colonne
			System.out.println("Le maximum est en ["+maxPosition.getX()+"]["+(m.getCountColonne() - 1)+"] (colonne)");
			return alignementColonne(f1, f2, maxPosition.getX(), m.getCountColonne() - 1, m.getMatrice());
		}
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière colonne de la matrice
	 */
	public String alignementColonne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String[] pre = preTraitement(f1, f2, ligne).split("&");
		String temp = pre[0];
		f2 = pre[1];
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				temp = f1.charAt(ligne - 1) + temp;
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				temp = "-" + temp;
				score += GAP;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un grap du haut
				String[] trai = traitement(temp, f1, f2, ligne, colonne).split("&");
				temp = trai[0];
				f2 = trai[1];
				score += GAP;
				ligne--;
				colonne++;
			}
			
			colonne--;
		}
	
		String[] post = postTraitement(temp, f1, f2, ligne, colonne).split("&");
		
		return post[0] + "&" + post[1];
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière ligne de la matrice
	 */
	public String alignementLigne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String[] pre = preTraitement(f2, f1, colonne).split("&");
		String temp = pre[0];
		f1 = pre[1];
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				temp = match(f1.charAt(ligne - 1), f2.charAt(colonne - 1)) == 1 ? f1.charAt(ligne - 1) + temp : f2.charAt(colonne - 1) + temp;
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				colonne--;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un gap du haut
				temp = "-" + temp;
				score += GAP;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				String[] trai = traitement(temp, f2, f1, colonne - 1, ligne).split("&");
				temp = trai[0];
				f1 = trai[1];
				score += GAP;
				colonne--;
				ligne++;
			}
			
			ligne--;
		}
			
		String[] post = postTraitement(temp, f2, f1, colonne, ligne).split("&");

		return post[0] + "&" + post[1];
	}
	
	/**
	 * Effectue le pré-traitement sur les deux fragments en fonction d'où se trouve le maximum dans la matrice
	 */
	public String preTraitement(String f1, String f2, int limite) {
		String temp = f1.substring(limite);
		
		for(int i = 0; i < temp.length(); i++) {
			f2 = f2.concat("-");
		} 
		
		return temp + "&" + f2;
	}
	
	/**
	 * Effectue le traitement sur les deux fragments en fonction d'où se trouve le maximum dans la matrice
	 */
	public String traitement(String temp, String f1, String f2, int ligne, int colonne) {
		temp = f1.charAt(ligne) + temp;
		String localBegin = f2.substring(0, colonne);
		String localEnd = f2.substring(colonne);
		f2 = localBegin + "-" + localEnd;
		
		return temp + "&" + f2;
	}
	
	/**
	 * Effectue le post-traitement sur les deux fragments en fonction d'où se trouve le maximum dans la matrice
	 */
	public String postTraitement(String temp, String f1, String f2, int ligne, int colonne) {
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				temp = "-" + temp;
			}
		}
		
		if(colonne == 0 && ligne != 0) {
			temp = f1.substring(0, ligne) + temp;
	
			for(int i = 0; i < ligne; i++) {
				f2 = "-" + f2;
			}
		}
		
		return temp + "&" + f2;
	}
	
	/**
	 * Permet de toujours avoir le fragment le plus long sur les lignes (f1 sera toujours plus long que f2)
	 */
	public String check(String f1, String f2) {
		if(f1.length() < f2.length()) { 
			String tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
		
		return f1 + "&" + f2;
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	public int match(char a, char b) {
		return a == b ? 1 : -1;
	}
	
	public String getF1() {
		return f1;
	}
	
	public String getF2() {
		return f2;
	}
	
	public int getScore() {
		return score;
	}

}
