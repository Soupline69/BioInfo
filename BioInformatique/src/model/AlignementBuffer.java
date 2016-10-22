package model;

public class AlignementBuffer {
	private final int GAP = -2;
	private final int MATCH = 1;
	private final int MISMATCH = -1;
	private StringBuffer f1;
	private StringBuffer f2;
	private int score = 0;
		
	public AlignementBuffer(StringBuffer f1, StringBuffer f2) {	
		this.f1 = f1;
		this.f2 = f2;
		
		check();
		alignementSemiGlobal();
	}
	
	/** 
	 * Aligne sur base de l'alignement semi-global les fragments f1 et f2
	 */
	public void alignementSemiGlobal() {
		MatriceBuffer m = new MatriceBuffer(f1, f2);
		Position maxPosition = m.calcul(f1, f2, GAP);

		if(maxPosition.getX() == m.getCountLigne() - 1) { // Si le maximum est sur la ligne
			//System.out.println("Le maximum est en ["+(m.getCountLigne() - 1)+"]["+maxPosition.getY()+"] (ligne)");
			alignementLigne(m.getCountLigne() - 1, maxPosition.getY(), m.getMatrice());
		} else { // Sinon le maximum est sur la colonne
			//System.out.println("Le maximum est en ["+maxPosition.getX()+"]["+(m.getCountColonne() - 1)+"] (colonne)");
			alignementColonne(maxPosition.getX(), m.getCountColonne() - 1, m.getMatrice());
		}
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière colonne de la matrice
	 */
	public void alignementColonne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f1.substring(ligne).length(); i++) {
			f2.append("_");
		} 
		
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				f1.insert(ligne, "-");
				score += GAP;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un grap du haut
				f2.insert(colonne, "-");
				score += GAP;
				ligne--;
				colonne++;
			}
			
			colonne--;
		}
			
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1.insert(0, "_");
			}
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2.insert(0, "_");
			}
		}		
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière ligne de la matrice
	 */
	public void alignementLigne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f2.substring(colonne).length(); i++) {
			f1.append("_");
		} 
						
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				colonne--;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un gap du haut
				f2.insert(colonne, "-");
				score += GAP;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				f1.insert(ligne, "-");
				score += GAP;
				colonne--;
				ligne++;
			}
			
			ligne--;
		}
			
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1.insert(0, "_");
			}
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2.insert(0, "_");
			}
		}	
	}
	
	/**
	 * Effectue le pré-traitement sur les deux fragments en fonction d'où se trouve le maximum dans la matrice
	 */
	public String preTraitement(StringBuffer f, int limite) {
		String temp = f.substring(limite);
		
		StringBuffer pre = new StringBuffer();
		for(int i = 0; i < temp.length(); i++) {
			pre.append("_");
		} 
		
		return temp + "&" + pre;
	}
	
	/**
	 * Effectue le post-traitement sur les deux fragments en fonction d'où se trouve le maximum dans la matrice
	 */
	public String postTraitement(StringBuffer f1, StringBuffer f2, int ligne, int colonne) {
		StringBuffer post = new StringBuffer();
		
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				post.append("_");
			}
		}
		
		StringBuffer postF2 = new StringBuffer();
		if(colonne == 0 && ligne != 0) {
			post.append(f1.substring(0, ligne));
	
			for(int i = 0; i < ligne; i++) {
				postF2.append("_");
			}
		}
		
		return postF2.length() == 0 ? post.toString() : post + "&" + postF2;
	}
	
	/**
	 * Permet de toujours avoir le fragment le plus long sur les lignes (f1 sera toujours plus long que f2)
	 */
	public void check() {
		if(f1.length() < f2.length()) { 
			StringBuffer tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	public int match(char a, char b) {
		return a == b ? MATCH : MISMATCH;
	}
	
	public StringBuffer getF1() {
		return f1;
	}
	
	public StringBuffer getF2() {
		return f2;
	}
	
	public int getScore() {
		return score;
	}

}
