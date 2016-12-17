package model;

/**
 * Represente une paire de score qui est utilise pour recuperer :
 * - Le score maximum de la derniere ligne de la matrice
 * - Le score maximum de la derniere colonne de la matrice
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
