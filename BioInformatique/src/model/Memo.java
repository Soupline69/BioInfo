package model;

public class Memo {

	/* 
	 * Un fragment = un sommet du graphe
	 * 
	 * Cas où le max de f g est sur la ligne :
	 * 		1) f g = ligne =   f g
	 * 		2) g f = colonne = f g
	 * 		3) g/ f/ = ligne = g/ f/
	 * 		4) f/ g/ = colonne = g/ f/
	 * 		5) f g/ = ligne = f g/
	 * 		6) g/ f = colonne = f g/
	 * 		7) g f/ = ligne = g f/
	 * 		8) f/ g = colonne = g f/
	 * 
	 * Cas où le max de f g est sur la colonne :
	 * 	 	1) f g = colonne =  g f
	 * 		2) g f = ligne = g f
	 * 		3) g/ f/ = colonne = f/ g/
	 * 		4) f/ g/ = ligne = f/ g/
	 * 		5) f g/ = colonne = g/ f
	 * 		6) g/ f = ligne = g/ f
	 * 		7) g f/ = colonne = f/ g
	 * 		8) f/ g = ligne = f/ g
	 * 
	 * Cas où le max de f g est sur la colonne et la ligne :
	 */
	
}
