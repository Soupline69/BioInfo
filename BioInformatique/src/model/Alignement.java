package model;

import java.util.List;

public class Alignement {
	private List<ADN> f1;
	private List<ADN> f2;
	private Position maxPosition;
	private int score = 0;
		
	public Alignement(List<ADN> f1, List<ADN> f2) {
		this.f1 = f1;
		this.f2 = f2;
		maxPosition = new Position(0, 0);
	}
	
	public Position score() {
		int[][] m = getMatrice();
		
		for(int i = 1; i < f2.size() + 1; i++) {
			if(m[f1.size()][i] >= score) {
				score = m[f1.size()][i];
				maxPosition.setX(f1.size());
				maxPosition.setY(i);
			}
		}
		
		for(int j = 1; j < f1.size() + 1; j++) {
			if(m[j][f2.size()] >= score) {
				score = m[j][f2.size()];
				maxPosition.setX(j);
				maxPosition.setY(f2.size());
			}
		}
		
		return /*score;*/ maxPosition.getX() == f1.size() ? new Position(score, 1) : new Position(score, 0);
	}
	
	public int[][] getMatrice() {
		int[][] m = new int[f1.size() + 1][f2.size() + 1];
		
		for(int i = 1; i < f1.size() + 1; i++) {
			for(int j = 1; j < f2.size() + 1; j++) {
				m[i][j] = max(
							m[i-1][j] + ADN.GAP,
							m[i][j-1] + ADN.GAP,
							m[i-1][j-1] + match(f1.get(i-1), f2.get(j-1))
				);
			}
		}
		
		return m;
	}
	
	/** 
	 * Crée la matrice d'alignement semi global,
	 * Donne le score d'alignement semi global (sans faire l'alignement vu que l'on en aura peut etre pas besoin),
	 * Donne la position où se trouve le maximum dans la matrice (pour faire l'alignement plus tard si besoin).
	 */
	
	public void alignementSemiGlobal() {
		if(maxPosition.getX() == f1.size()) { // Si le maximum est sur la ligne
			alignementLigne(f1.size(), maxPosition.getY(), getMatrice());
		} else { // Sinon le maximum est sur la colonne
			alignementColonne(maxPosition.getX(), f2.size(), getMatrice());
		}
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière colonne de la matrice
	 */
	private void alignementColonne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f1.size() - ligne; i++) {
			f2.add(Lien.NONE);
		} 
			
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.get(ligne - 1), f2.get(colonne - 1))) { // Si c'est un match ou un mismatch 
				//score += match(f1.get(ligne - 1), f2.get(colonne - 1));
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + ADN.GAP) { // Si c'est un gap de gauche
				f1.add(ligne, Lien.GAP); 
				//score += ADN.GAP;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + ADN.GAP) { // Si c'est un grap du haut
				f2.add(colonne, Lien.GAP);
				//score += ADN.GAP;
				ligne--;
				colonne++;
			}
			
			colonne--;
		}
	
		postTraitement(ligne, colonne);
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la dernière ligne de la matrice
	 */
	private void alignementLigne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f2.size() - colonne; i++) {
			f1.add(Lien.NONE);
		} 
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.get(ligne - 1), f2.get(colonne - 1))) { // Si c'est un match ou un mismatch 
				//score += match(f1.get(ligne - 1), f2.get(colonne - 1));
				colonne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + ADN.GAP) { // Si c'est un gap de gauche
				f1.add(ligne, Lien.GAP);
				//score += ADN.GAP;
				colonne--;
				ligne++;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + ADN.GAP) { // Si c'est un gap du haut
				f2.add(colonne, Lien.GAP);
				//score += ADN.GAP;
			} 
			
			ligne--;
		}
			
		postTraitement(ligne, colonne);
	}
	
	private void postTraitement(int ligne, int colonne) {
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1.add(0, Lien.NONE);
			}
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2.add(0, Lien.NONE);
			}
		}	
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
	 * Permet de toujours avoir le fragment le plus long sur les lignes (f1 sera toujours plus long que f2)
	 */
	private void check() {
		if(f1.size() < f2.size()) { 
			List<ADN> tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	private int match(ADN a, ADN b) {
		return a == b ? ADN.MATCH : ADN.MISMATCH;
	}
	
	public List<ADN> getF1() {
		return f1;
	}
	
	public List<ADN> getF2() {
		return f2;
	}
	
	public int getScore() {
		return score;
	}

}
