package model;

public class Alignement {
	private String f1;
	private String f2;
	private int score;
		
	public Alignement(String f1, String f2) {		
		String[] f = check(f1, f2).split("&");
		alignementSemiGlobal(f[0], f[1]);
	}
	
	public void alignementSemiGlobal(String f1, String f2) {
		Matrice m = construireMatrice(f1, f2);
		Position maxPosition = m.maxMatrice();
		
		if(maxPosition.getX() == m.getCountLigne() - 1) { // Si le maximum est sur la ligne
			System.out.println("\n\nLe maximum est en ["+(m.getCountLigne() - 1)+"]["+maxPosition.getY()+"] (ligne)");
			alignementLigne(f1, f2, m.getCountLigne() - 1, maxPosition.getY(), m.getMatrice());
		} else { // Sinon le maximum est sur la colonne
			System.out.println("\n\nLe maximum est en ["+maxPosition.getX()+"]["+(m.getCountColonne() - 1)+"] (colonne)");
			alignementColonne(f1, f2, maxPosition.getX(), m.getCountColonne() - 1, m.getMatrice());
		}
	}
	
	public void alignementColonne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String[] pre = preTraitement(f1, f2, ligne).split("&");
		String temp = pre[0];
		f2 = pre[1];
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne-1][colonne-1] + match(f1.charAt(ligne-1), f2.charAt(colonne-1))) { // provient de la diagonale 
				temp = f1.charAt(ligne-1) + temp;
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne-1] - 2) { // Gap
				temp = "-" + temp;
			} else if(m[ligne][colonne] == m[ligne-1][colonne] - 2) { // Gap
				String[] trai = traitement(temp, f1, f2, ligne, colonne).split("&");
				temp = trai[0];
				f2 = trai[1];
				ligne--;
				colonne++;
			}
			
			colonne--;
		}
	
		String[] post = postTraitement(temp, f1, f2, ligne, colonne).split("&");
		f1 = post[0]; 
		f2 = post[1];
		
		System.out.println("\n\n");
		System.out.println(f1 + "    " + f1.length());
		System.out.println(f2 + "    " + f2.length());
	}
	
	public void alignementLigne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String[] pre = preTraitement(f2, f1, colonne).split("&");
		String temp = pre[0];
		f1 = pre[1];
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne-1][colonne-1] + match(f1.charAt(ligne-1), f2.charAt(colonne-1))) { // provient de la diagonale 
				temp = match(f1.charAt(ligne-1), f2.charAt(colonne-1)) == 1 ? f1.charAt(ligne-1) + temp : f2.charAt(colonne-1) + temp;
				colonne--;
			} else if(m[ligne][colonne] == m[ligne-1][colonne] - 2) { // Gap
				temp = "-" + temp;
			} else if(m[ligne][colonne] == m[ligne][colonne-1] - 2) { // Gap
				System.out.println("on passe ici");
				String[] trai = traitement(temp, f2, f1, colonne - 1, ligne).split("&");
				temp = trai[0];
				f1 = trai[1];
				colonne--;
				ligne++;
			}
			
			ligne--;
		}
			
		String[] post = postTraitement(temp, f2, f1, colonne, ligne).split("&");
		f2 = post[0];
		f1 = post[1];
		
		System.out.println("\n\n");
		System.out.println(f1 + "    " + f1.length());
		System.out.println(f2 + "    " + f2.length());
	}
	
	public String preTraitement(String f1, String f2, int limite) {
		String temp = f1.substring(limite); // OK
		
		for(int i = 0; i < temp.length(); i++) { // OK
			f2 = f2.concat("-");
		} 
		
		return temp + "&" + f2;
	}
	
	public String traitement(String temp, String f1, String f2, int ligne, int colonne) {
		temp = f1.charAt(ligne) + temp;
		String localBegin = f2.substring(0, colonne);
		String localEnd = f2.substring(colonne);
		f2 = localBegin + "-" + localEnd;
		
		return temp + "&" + f2;
	}
	
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
	
	public int match(char a, char b) {
		return a == b ? 1 : -1;
	}
	
	public Matrice construireMatrice(String f1, String f2) {
		return new Matrice(f1, f2);
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
