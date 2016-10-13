package main;

import model.Matrice;
import model.Position;

public class Main2 {

	public static void main(String[] args) {
		// 1. Faire l'alignement semi-global avec deux fragments tels que :
		
		String f1 = "CCAAGTCAAGTCGG"; //AGCTTC // ATTAGACCATGCGGC // CAGCACTTGGATTCTCGG
		String f2 = "GTTCAAATCGGGCTT"; // AGTCAGTGCGTGC // ATCGGCATTCAGT // CAGCGTGG
		
		System.out.println("f1 : "+f1.length()+" f2 : "+f2.length());
		if(f1.length() < f2.length()) { // Permet de toujours avoir le fragment le plus long sur les lignes
			String tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
		System.out.println("f1 : "+f1.length()+" f2 : "+f2.length());
		
		
		Matrice m = new Matrice(f1, f2);
		Position maxPosition = m.maxMatrice();
		
		if(maxPosition.getX() == m.getCountLigne() - 1) {
			System.out.println("\n\nLe maximum est en ["+(m.getCountLigne() - 1)+"]["+maxPosition.getY()+"] (ligne)");
			alignementLigne(f1, f2, f1.length(), maxPosition.getY(), m.getMatrice());
		} else {
			System.out.println("\n\nLe maximum est en ["+maxPosition.getX()+"]["+(m.getCountColonne() - 1)+"] (colonne)");
			alignementColonne(f1, f2, maxPosition.getX(), f2.length(), m.getMatrice());
		}
	}

	public static void alignementColonne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String temp = f1.substring(ligne); // OK
		
		for(int i = 0; i < temp.length(); i++) { // OK
			f2 = f2.concat("-");
		} 
		
		int col = colonne;
		int li = ligne ;
		
		while(col > 0 && li > 0) {
			if(m[li][col] == m[li-1][col-1] + match(f1.charAt(li-1), f2.charAt(col-1))) { // provient de la diagonale 
				temp = f1.charAt(li-1) + temp;
				li--;
			} else if(m[li][col] == m[li][col-1] - 2) { // Gap
				temp = "-" + temp;
			} else if(m[li][col] == m[li-1][col] - 2) { // Gap
				temp = f1.charAt(li) + temp;
				String localBegin = f2.substring(0, col);
				String localEnd = f2.substring(col);
				f2 = localBegin + "-" + localEnd;
				li--;
			}
			
			col--;
		}

		if(col == 0 && li != 0) {
			temp = f1.substring(0, li) + temp;
			for(int i = 0; i < li; i++) {
				f2 = "-" + f2;
			}
		}
		
		if(li == 0 && col != 0) {
			for(int i = 0; i < col+1; i++) {
				temp = "-" + temp;
			}
		}
		
		f1 = temp; 
		
		System.out.println("\n\n");
		System.out.println(f1 + "    " + f1.length());
		System.out.println(f2 + "    " + f2.length());
	}
	
	public static void alignementLigne(String f1, String f2, int ligne, int colonne, int[][] m) {
		String temp = f2.substring(colonne);
		int col = colonne;
		int li = ligne;
		
		for(int i = 0; i < f2.length() - col; i++) {
			f1 = f1.concat("-");
		}
				
		while(col > 0 && li > 0) {
			if(m[li][col] == m[li-1][col-1] + match(f1.charAt(li-1), f2.charAt(col-1))) { // provient de la diagonale 
				temp = f1.charAt(li-1) + temp;
				col--;
			} else if(m[li][col] == m[li-1][col] - 2) { // Gap
				temp = "-" + temp;
			} else if(m[li][col] == m[li][col-1] - 2) { // Gap // VERIFIER ICI QUAND TU VAS SUR LE COTE
				temp = f2.charAt(col) + temp;
				String localBegin = f1.substring(0, li);
				String localEnd = f1.substring(li);
				f1 = localBegin + "-" + localEnd;
				col--;
			}
			
			li--;
		}
		
		if(col == 0 && li != 0) {
			for(int i = 0; i < li; i++) {
				temp = "-" + temp;
			}
		} 
		
		f2 = temp;
		
		System.out.println("\n\n");
		System.out.println(f1 + "    " + f1.length());
		System.out.println(f2 + "    " + f2.length());
	}
	
	public static int match(char a, char b) {
		if(a == b) {
			return 1;
		} else {
			return -1;
		}
	}
	

}
