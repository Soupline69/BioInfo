package model;

/**
 * Repr�sente une paire de score qui est utilis� pour r�cup�rer :
 * - Le score maximum de la derni�re ligne de la matrice
 * - Le score maximum de la derni�re colonne de la matrice
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
