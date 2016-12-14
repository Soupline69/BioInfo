package model;

import java.util.List;

/**
 * Représente un alignement semi-global entre un fragment f1 et un fragment f2
 */
public class Alignement {
	private List<ADN> f1;
	private int sizeF1;
	private List<ADN> f2;
	private int sizeF2;
	private int debutAlignementF1;
	private int debutAlignementF2;
	private int finAlignementF1;
	private int finAlignementF2;
	private int debutChevauchement;
	private int finChevauchement;
		
	public Alignement(List<ADN> f1, List<ADN> f2) {
		this.f1 = f1;
		sizeF1 = f1.size(); 
		this.f2 = f2;
		sizeF2 = f2.size();
	}
	
	/**
	 * Retourne la Pair de score maximum (sur la dernière ligne et sur la dernière colonne) de la matrice m
	 */
	public Pair score() {		
		int[][] m = getMatrice();
		
		Score ligne = maxLigne(m);
		Score colonne = maxColonne(m);
				
		return new Pair(ligne, colonne);
	}
	
	/**
	 * Calcule la matrice d'alignement semi-global
	 */
	private int[][] getMatrice() {
		int[][] m = new int[sizeF1 + 1][sizeF2 + 1];
		
		for(int i = 1; i < sizeF1 + 1; i++) {
			for(int j = 1; j < sizeF2 + 1; j++) {
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
	 * Retourne le Score maximum sur la dernière ligne de la matrice m
	 */
	private Score maxLigne(int[][] m) {
		Score ligne = new Score(0, 0);

		for(int i = 1; i < sizeF2 + 1; i++) {
			if(m[sizeF1][i] >= ligne.getScore()) {
				ligne.setScore(m[sizeF1][i]);
				ligne.setChevauchement(i);
			}
		}
		
		return ligne;
	}
	
	/**
	 * Retourne le Score maximum sur la dernière colonne de la matrice m
	 */
	private Score maxColonne(int[][] m) {
		Score colonne = new Score(0, 0);
		
		for(int i = 1; i < sizeF1 + 1; i++) {
			if(m[i][sizeF2] >= colonne.getScore()) {
				colonne.setScore(m[i][sizeF2]);
				colonne.setChevauchement(i);
			}
		}
		
		return colonne;	
	}
	
	/** 
	 * Effectue l'alignement semi-global entre deux fragments
	 */
	public void alignementSemiGlobal() {
		int[][] m = getMatrice();
		Pair score = new Pair(maxLigne(m), maxColonne(m));

		if(score.getLigne().getScore() >= score.getColonne().getScore()) {
			alignementLigne(sizeF1, score.getLigne().getChevauchement(), m);
		} else { 
			alignementColonne(score.getColonne().getChevauchement(), sizeF2, m);
		}
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice est sur la dernière ligne de la matrice
	 * 1. Ajouter des gaps de fin sur le fragment f1 (ligne)
	 * 2. Tant que l'on est pas arrivé sur la première ligne ou la première colonne de la matrice
	 * 		2.1. On regarde si on a un match ou un mismatch 
	 * 		2.2. On regarde s'il ne faut pas mettre un gap sur le fragment f1 (ligne)
	 * 		2.3. On regarde s'il ne faut pas mettre un gap sur le fragment f2 (colonne)
	 * 3. Si on est arrivé sur la première ligne de la matrice 
	 * 		3.1. On ajoute des gaps de début au fragment f1 (ligne) 
	 * 	  Si on est arrivé sur la première colonne de la matrice
	 * 		3.2. On ajoute des gaps de début au fragment f2 (colonne)
	 */
	private void alignementLigne(int ligne, int colonne, int[][] m) {	
		finAlignementF2 = pretraitement(sizeF2, colonne, f1, finAlignementF2);
						
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.get(ligne - 1), f2.get(colonne - 1))) { 
				colonne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + ADN.GAP) {
				f1.add(ligne, Lien.GAP);
				sizeF1++;
				colonne--;
				ligne++;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + ADN.GAP) { 
				f2.add(colonne, Lien.GAP);
				sizeF2++;
			} 
			
			finAlignementF1++;
			finAlignementF2++;
			ligne--;
		}
			
		postTraitement(ligne, colonne);
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice est sur la dernière colonne de la matrice
	 *  1. Ajouter des gaps de fin sur le fragment f2 (colonne)
	 *  2. Tant que l'on est pas arrivé sur la première ligne ou la première colonne de la matrice
	 * 		2.1. On regarde si on a un match ou un mismatch 
	 * 		2.2. On regarde s'il ne faut pas mettre un gap sur le fragment f1 (ligne)
	 * 		2.3. On regarde s'il ne faut pas mettre un gap sur le fragment f2 (colonne)
	 * 3. Si on est arrivé sur la première ligne de la matrice 
	 * 		3.1. On ajoute des gaps de début au fragment f1 (ligne) 
	 * 	  Si on est arrivé sur la première colonne de la matrice
	 * 		3.2. On ajoute des gaps de début au fragment f2 (colonne)
	 */
	private void alignementColonne(int ligne, int colonne, int[][] m) {		
		finAlignementF1 = pretraitement(sizeF1, ligne, f2, finAlignementF1);
					
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.get(ligne - 1), f2.get(colonne - 1))) { // Si c'est un match ou un mismatch 
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + ADN.GAP) { // Si c'est un gap de gauche
				f1.add(ligne, Lien.GAP); 
				sizeF1++;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + ADN.GAP) { // Si c'est un gap du haut
				f2.add(colonne, Lien.GAP);
				sizeF2++;
				ligne--;
				colonne++;
			}
			
			finAlignementF1++;
			finAlignementF2++;
			colonne--;
		}
	
		postTraitement(ligne, colonne);
	}
	
	/**
	 * Ajoute des gaps à la fin du fragment f
	 */
	private int pretraitement(int size, int soustraction, List<ADN> f, int finAlignement) {
		for(int i = 0; i < size - soustraction; i++) {
			f.add(Lien.NONE);
			finAlignement++;
		} 
		
		return finAlignement;
	}
	
	/**
	 * Ajoute des gaps au début des fragments f1 et f2 si les conditions sont satisfaites
	 */
	private void postTraitement(int ligne, int colonne) {
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1.add(0, Lien.NONE);
				finAlignementF2++;
			}
			
			changeValeur(-colonne, -ligne);
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2.add(0, Lien.NONE);
				finAlignementF1++;
			}
			
			changeValeur(ligne, colonne);
		}
		
		finAlignementF1--;
		finAlignementF2--;
	}
	
	/**
	 * Change les valeurs des attributs : debutAlignementF1, finAlignementF1, debutAlignementF2, finAlignementF2
	 */
	private void changeValeur(int v1, int v2) {
		debutAlignementF1 = v2;
		finAlignementF1 += v2;
		debutAlignementF2 = v1;
		finAlignementF2 += v1;
	}
	
	/**
	 * Met à jour les attributs : debutAlignementF1, finAlignementF1, debutAlignementF2, finAlignementF2, debutChevauchement, finChevauchement en fonction du paramètre
	 * update
	 * @param update correspond à la position où commence l'alignement
	 */
	public void update(int update) {
		debutAlignementF1 += update;
		finAlignementF1 += update;
		debutAlignementF2 += update;
		finAlignementF2 += update;
		debutChevauchement += update;
		finChevauchement += update;
	}
	
	/**
	 * Lorsque les fragments sont alignés deux à deux, il est possible de devoir propager un gap vers le haut ou le bas
	 * Il faut donc mettre à jour les valeurs des différents attributs en fonction de la position du gap
	 * @param gap correspond à la position du gap qui a été propagé
	 */
	public void increment(int gap) {
		if(gap <= debutAlignementF1) {
			debutAlignementF1++;
			finAlignementF1++;
		} else if(gap <= finAlignementF1) {
			finAlignementF1++;
		}
		
		if(gap <= debutAlignementF2) {
			debutAlignementF2++;
			finAlignementF2++;
		} else if(gap <= finAlignementF2) {
			finAlignementF2++;
		}
		
		if(gap <= debutChevauchement) {
			debutChevauchement++;
			finChevauchement++;
		} else if(gap <= finChevauchement) {
			finChevauchement++;
		}
	}
	
	/**
	 * Retourne le maximum entre a, b et c
	 */
	private int max(int a, int b, int c) {
		return Math.max(a, Math.max(b, c));
	}
	
	/**
	 * Retourne 1 si le caractère a est égal au caractère b (match)
	 * Retourne - 1 sinon (mismatch)
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
	
	public int getDebutChevauchement() {
		return Math.max(debutAlignementF1, debutAlignementF2);
	}
	
	public int getFinChevauchement() {
		return Math.min(finAlignementF1, finAlignementF2);
	}
	
	public int getChevauchement() {
		return getFinChevauchement() - getDebutChevauchement() + 1;
	}
	
	public int getDebutAlignementF1() {
		return debutAlignementF1;
	}
	
	public int getDebutAlignementF2() {
		return debutAlignementF2;
	}
	
	public int getFinAlignementF1() {
		return finAlignementF1;
	}
	
	public int getFinAlignementF2() {
		return finAlignementF2;
	}

}
