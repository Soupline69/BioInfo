package model;

/**
 * Représente une paire de score qui est utilisé pour récupérer :
 * - Le score maximum de la dernière ligne de la matrice
 * - Le score maximum de la dernière colonne de la matrice
 */
public class Pair {
	  private Score ligne;
	  private Score colonne;
	  
	  public Pair(Score ligne, Score colonne) {
	    this.ligne = ligne; 
	    this.colonne = colonne;
	  }
	  
	  public Score getLigne() {
		  return ligne;
	  }
	  
	  public Score getColonne() {
		  return colonne;
	  }
	  
}
