package main;

public class Main {

	public static void main(String[] args) {
		// 1. Faire l'alignement semi-global avec deux fragments tels que :
		
		String f1 = "ccacggttaggcccgtggccgccacgcccgcctcccgtcaggatccgccccctcccggagggctcccctgggggaggaca"
				+ "agcgccaggagttgtccaggctcaagccctcgggaactaccaacctcccctcgggcctcccaaaacccggttgtcagggc"
				+ "gggaaaacccagcccccaagtggcgctgaggtcctgatccggtccagcctttcggtacagagcgccttcacgtcgccgcg"
				+ "tgggacaccgccgggtgaggacgaccccgaacgctagccccgcgcgtgaccaactcgcctggtctccccctccagcacgg"
				+ "gggcttatcgcgccagcccctcgggatcttccccctacgagccggcgtttcgtcacccctacgggcttctcccgtcatcc"
				+ "gggcccccacccgtcagggagctgccgcctggaggttcccccgttagtctgcccctacaacccgggccgcctgacccagc"
				+ "ggcccagcgcggtttccgcaaaccggatccccccaccctacgaggttccaagagctcggttagggggggccgcgacgggc"
				+ "gtggcgtccatcggcaccggtacttccccgcagggggtttctcaccccca"; //AGCTTC // F1 = ATTAGACCATGCGGC // CAGCACTTGGATTCTCGG
		String f2 = "ttctcggggtactccgtcacggtgccggggagcgcaaggatcccctcaggtcttcccaagtgggcacagctggacctctg"
				+ "gccggtgaaactcaccacccagaacgggaagaacatctacaacgcctggatctcccaggccctgaacggcaagcaggcag"
				+ "gactggaccaggcggtcctgcccaaggactacgccccgggggctaaccgctggctcgcggacaacggcctgccggtgcgg"
				+ "gtgacgggggcctacctgcgcgacttcgacacctccttcctcctgaacccctacatacccatcaccggccccatcgcctg"
				+ "ggtgagctacacgcagtacacctggaccaacaacgggctgctcacggacaccatcgggaatagggatcttccggcggtgg"
				+ "ttctgtacgacagcgcacagatacgtagcgggtcgtcctactacctccacaaggccccggcctaccgctaccgggtggtg"
				+ "gagtactgggagtggtccggcaacttcgccacggtggacaacgggacggagcgtctttccccccgtccttttggtcccat"
				+ "tgagatttaactcagggactactacgctctctccaacggggtgtggcgcatcagccgctagagggtggtga"; // AGTCAGTGCGTGC // F2 = ATCGGCATTCAGT // CAGCGTGG
		
		System.out.println("f1 : "+f1.length()+" f2 : "+f2.length());
		if(f1.length() < f2.length()) { // Permet de toujours avoir le fragment le plus long sur les lignes
			String tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
		System.out.println("f1 : "+f1.length()+" f2 : "+f2.length());
		
		int[][] m = new int[f1.length() + 1][f2.length() + 1];
		
		// Pour obtenir la matrice de l'exemple du pdf, ce serait la première étape // OK // 15/6 ou 6/15
		
		// Calculer le résultat de la matrice
		for(int i = 1; i < f1.length() + 1; i++) {
			for(int j = 1; j < f2.length() + 1; j++) {
				m[i][j] = max(
							m[i-1][j] - 2, // Faire une constante avec le score d'un GAP
							m[i][j-1] - 2,
							m[i-1][j-1] + match(f1.charAt(i-1), f2.charAt(j-1))
						);
			}
		}
		
		// Afficher le résultat de la matrice 
	/*	for(int i = 0; i < f1.length() + 1; i++) {
			for(int j = 0; j < f2.length() + 1; j++) {		
				System.out.print(m[i][j]+"  ");
			}
			System.out.println("");
		}*/
		
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
			System.out.println("\n\nLe maximum est en ["+(f1.length())+"]["+colonne+"] avec "+maxLigne+" (ligne)");
			alignementLigne(f1, f2, f1.length(), colonne, m);
		} else {
			System.out.println("\n\nLe maximum est en ["+ligne+"]["+(f2.length())+"] avec "+maxColonne+" (colonne)");
			alignementColonne(f1, f2, ligne, f2.length(), m);
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
				temp = "-" + temp;
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
			for(int i = 0; i < col; i++) {
				temp = "-" + temp;
			}
		}
		
		
		
		System.out.println("colonne : "+col + " ligne : "+li);

		
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
			} else if(m[li][col] == m[li][col-1] - 2) { // Gap
				temp = "-" + temp;
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
	
	public static int max(int a, int b, int c) {
		if(a >= b && a >= c) {
			return a;
		} else if(b >= a && b >= c) {
			return b;
		} else {
			return c;
		}
	}
	
	public static int match(char a, char b) {
		if(a == b) {
			return 1;
		} else {
			return -1;
		}
	}

}
