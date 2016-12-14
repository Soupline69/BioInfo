package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Controller {
	private Graphe graphe;
	
	public Controller(String fileCollection, String fileNormal, String fileIC) {
		this.graphe = new Graphe(Parser.getFragments(fileCollection));
		
		init(fileCollection, fileNormal, fileIC);
	}
	
	/**
	 * Dirige le programme en appelant les différentes méthodes correspondantes aux différentes parties du projet :
	 * 1. Calcul des scores d'alignement semi global pour toutes les paires de fragments
	 * 2. Trier les flèches du graphe de la plus "forte" à la plus "faible"
	 * 3. Appliquer l'algorithme greedy pour trouver un chemin hamiltonien dans le graphe
	 * 4. Aligner deux à deux les fragments du chemin hamiltonien
	 */
	private void init(String fileCollection, String fileNormal, String fileIC) {				
		graphe.getNoeuds().parallelStream().forEach(this::calcul); 
		System.out.println("Toutes les flèches ont été crées");
		Collections.sort(graphe.getArretes());
		System.out.println("Toutes les flèches ont été triées");
		Parser.write(fileCollection, fileNormal, fileIC, consensus(alignements(graphe.greddy())));
		System.out.println("Les fichiers cibles ont été créés");
	}
	
	/**
	 * Calcul toutes les matrices d'alignement semi global avec 
	 * - f comme fragment de début
	 * - tous les fragments qui suivent f comme fragment de fin 
	 */
	private void calcul(Fragment f) {
		int indexDebut = graphe.getNoeuds().indexOf(f);
		graphe.getNoeuds().subList(indexDebut + 1, graphe.getNoeuds().size()).parallelStream().forEach(g -> {
			calculScore(f, g, indexDebut, graphe.getNoeuds().indexOf(g));
		});
	}
	
	/**
	 * Calcul le score d'alignement semi global entre f et g (en prenant le maximum de la dernière ligne de la matrice et le maximum de la dernière colonne de la matrice)
	 */
	private void calculScore(Fragment f, Fragment g, int indexDebut, int indexFin) {
		addArretes(new Alignement(f.getValue(), g.getValue()).score(), indexDebut, indexFin, false, false);
		addArretes(new Alignement(f.getInverse(), g.getInverse()).score(), indexDebut, indexFin, true, true);
		addArretes(new Alignement(f.getValue(), g.getInverse()).score(), indexDebut, indexFin, false, true);
		addArretes(new Alignement(f.getInverse(), g.getValue()).score(), indexDebut, indexFin, true, false);
	}
	
	/**
	 * Ajoute deux arrêtes dans le graphe :
	 * - une première telle que le sens est de f à g avec le score maximum de la dernière ligne de la matrice
	 * - une deuxième telle que le sens est de g à f avec le score amximum de la dernière colonne de la matrice
	 * @param debut indique si le fragment f a été inversé/complémenté ou non
	 * @param fin indique si le fragment g a été inversé/complémenté ou non
	 */
	private void addArretes(Pair score, int indexDebut, int indexFin, boolean debut, boolean fin) {
		graphe.addArrete(new Arrete(indexDebut, indexFin, score.getLigne(), debut, fin));
		graphe.addArrete(new Arrete(indexFin, indexDebut, score.getColonne(), fin, debut));
	}
	
	/**
	 * Aligne tous les fragments du chemin hamiltonien :
	 * - en démarrant avec le début de la première flèche
	 * - en continuant avec la flèche correspondante à la fin de la précédente
	 * - en propageant si besoin des gaps dans les fragments précédents / dans le fragment suivant
	 */
	private List<Alignement> alignements(List<Arrete> cheminHamilton) {
		List<Alignement> alignements = new ArrayList<>(cheminHamilton.size());
		
		Arrete courante = cheminHamilton.get(0);
		Alignement a = alignePaire(courante);
		alignements.add(a);
		int debutAlignement = Math.min(alignements.get(0).getDebutAlignementF1(), alignements.get(0).getDebutAlignementF2());
		
		int i = 1;
		while(i < cheminHamilton.size()) { 
			courante = cheminHamilton.get(cheminHamilton.indexOf(new Arrete(courante.getFin())));
			a = alignePaire(courante);
			alignements.add(a);
			
			gapBas(a, alignements.get(i-1), debutAlignement);		
			debutAlignement = updateDebutAlignement(a) + alignements.get(i - 1).getDebutAlignementF2();
			gapHaut(a, alignements, debutAlignement, i);
			a.update(debutAlignement);
			
			System.out.println(i+" sur "+cheminHamilton.size());
			
			i++;
		}
		
		System.out.println("Tous les fragments du chemin hamiltonien ont été alignés");
		
		return alignements;
	}
	
	/**
	 * Compte le nombre de 'A', 'T', 'C', 'G', '-' par colonne
	 */
	private Fragment consensus(List<Alignement> alignements) { 		
		int min = Collections.min(alignements.parallelStream().map(a -> Math.min(a.getDebutAlignementF1(), a.getDebutAlignementF2())).collect(Collectors.toList()));
		int max = Collections.max(alignements.parallelStream().map(a -> Math.max(a.getFinAlignementF1(), a.getFinAlignementF2())).collect(Collectors.toList()));
		
		List<int[]> compteurs = new ArrayList<>(
				Arrays.asList(new int[max + Math.abs(min) + 1], new int[max + Math.abs(min) + 1], new int[max + Math.abs(min) + 1], new int[max + Math.abs(min) + 1], new int[max + Math.abs(min) + 1])
		);
		
		alignements.parallelStream().forEach(a -> {
			int debut = a.getDebutAlignementF1() <= a.getDebutAlignementF2() ? 0 : a.getDebutAlignementF1() - a.getDebutAlignementF2();
			int fin = debut + a.getFinAlignementF1() - a.getDebutAlignementF1();
						
			for(int j = 0; j <= fin - debut; j++) {
				updateCompteurs(compteurs, a.getF1().get(j + debut).toChar(), a.getDebutAlignementF1() + Math.abs(min) + j);
			}
			
			if(alignements.indexOf(a) == alignements.size() - 1) {
				debut = a.getDebutAlignementF1() <= a.getDebutAlignementF2() ? a.getDebutAlignementF2() - a.getDebutAlignementF1() : 0;
				fin = debut + a.getFinAlignementF2() - a.getDebutAlignementF2();
								
				for(int j = 0; j <= fin - debut; j++) {
					updateCompteurs(compteurs, a.getF2().get(j + debut).toChar(), a.getDebutAlignementF2() + Math.abs(min) + j);
				}
			}
			
			a.getF1().clear();
			a.getF2().clear();
		});
		
		System.out.println("Le consensus est terminé");
		
		return supersequence(max, min, compteurs);
	}
	
	/**
	 * Détermine la lettre prédominante par colonne :
	 * - Soit il y a une majorité (dans le cas où c'est une majorité de 'gap', un nucléobase est choisi au hasard)
	 * - Soit il y a une égalité et le choix est fait au hasard entre les différentes égalités
	 */
	private Fragment supersequence(int max, int min, List<int[]> compteurs) {
		String supersequence = "";
		
		Map<ADN, Integer> map = new HashMap<>();
		for(int i = 0; i <= max + Math.abs(min); i++) {	
			map.put(Nucleobase.A, compteurs.get(0)[i]);
			map.put(Nucleobase.T, compteurs.get(1)[i]);
			map.put(Nucleobase.C, compteurs.get(2)[i]);
			map.put(Nucleobase.G, compteurs.get(3)[i]);
			map.put(Lien.GAP, compteurs.get(4)[i]);
			
			int maxOccurence = Collections.max(map.values());
			
			List<ADN> choix = new ArrayList<>();
			map.keySet().stream().forEach(k -> {
				if(map.get(k).intValue() == maxOccurence) {
					choix.add(k);
				}
			});
			
			supersequence += Character.toString(determineNucleobaseSuivant(choix));
			
			map.clear();
		}

		System.out.println("La superséquence a été calculée");
		
		return new Fragment(supersequence);
	}
	
	/**
	 * Aligne les fragments présents dans l'arrête où :
	 * - f1 est le début de la flèche
	 * - f2 est la fin de la flèche
	 */
	private Alignement alignePaire(Arrete courante) {
		Alignement a = new Alignement(new ArrayList<>(graphe.getNoeuds().get(courante.getDebut()).getValue()), new ArrayList<>(graphe.getNoeuds().get(courante.getFin()).getValue()));
		a.alignementSemiGlobal();
		
		graphe.getNoeuds().get(courante.getDebut()).getValue().clear();

		return a;
	}
	
	/**
	 * Vérifier qu'il n'y a pas un gap dans le f2 de l'alignement précédent qu'il faut propager dans l'alignement courant
	 */
	private void gapBas(Alignement courant, Alignement precedent, int debutAlignement) {
		int position = courant.getDebutAlignementF1() - 1;
		for(int j = 0; j < precedent.getChevauchement(); j++) {		
			position++;
			
			if(precedent.getF2().get(j + precedent.getDebutChevauchement() - debutAlignement) == Lien.GAP && courant.getF1().get(position) != Lien.GAP) {
				addGap(courant, position, j);
			}
		}
	}
	
	/**
	 * Ajoute un gap dans le fragment f1 et f2 de l'alignement a en
	 */
	private void addGap(Alignement a, int position, int j) {
		a.getF1().add(position, a.getF1().get(position) == Lien.NONE ? Lien.NONE : Lien.GAP);
		a.getF2().add(position, a.getF2().get(position) == Lien.NONE ? Lien.NONE : Lien.GAP);
		a.increment(j);
	}
	
	/**
	 * Permet de mettre à jour la position du début de chaque alignement
	 */
	private int updateDebutAlignement(Alignement courant) {
		if(courant.getDebutAlignementF1() <= courant.getDebutAlignementF2()) {
			return Math.min(courant.getDebutAlignementF1(), courant.getDebutAlignementF2());
		} else {
			return Math.max(courant.getDebutAlignementF1(), courant.getDebutAlignementF2());
		}
	}
	
	/**
	 * Vérifier qu'il n'y a pas un gap dans le f1 de l'alignement courant qu'il faut propager dans les alignements précédents
	 */
	private void gapHaut(Alignement a, List<Alignement> alignements, int debutAlignement, int i) {		
		int positionGap;
		int positionAlignement;
		int debutAlignementMinimum;
		
		for(int j = 0; j < a.getChevauchement(); j++) {	
			positionGap = debutAlignement;
			
			if(a.getDebutAlignementF1() <= a.getDebutAlignementF2()) {
				if(a.getF1().get(j + a.getDebutChevauchement()) == Lien.GAP) {
					positionGap += j + a.getDebutChevauchement();
				}
			} else {
				if(a.getF1().get(j + Math.abs(a.getDebutAlignementF2())) == Lien.GAP) {
					positionGap += j + Math.abs(a.getDebutAlignementF2());
				}
			} 
			
			if(positionGap != debutAlignement) {
				positionAlignement = i - 1;
				debutAlignementMinimum = Math.min(alignements.get(positionAlignement).getDebutAlignementF1(), alignements.get(positionAlignement).getDebutAlignementF2());
				
				while(Math.max(a.getFinAlignementF1(), a.getFinAlignementF2()) >= positionGap && isNucleobase(a.getF2().get(positionGap - debutAlignementMinimum))) {
					addGap(a, positionGap - debutAlignementMinimum, positionGap - debutAlignementMinimum);
					positionAlignement--;
				}
			}
		}
	}
	
	/**
	 * Met à jour les compteurs en fonction de la lettre reçue
	 */
	private void updateCompteurs(List<int[]> compteurs, char adn, int position) {
		switch(adn) {
			case 'a' : 
				compteurs.get(0)[position]++;
				break;
			case 't' : 
				compteurs.get(1)[position]++;
				break;
			case 'c' : 
				compteurs.get(2)[position]++;
				break;
			case 'g' : 
				compteurs.get(3)[position]++;	
				break;
			default : 
				compteurs.get(4)[position]++;
				break;
		}
	}
	
	/**
	 * Détermine le prochain nucléobase à inscrire dans la superséquence
	 */
	private char determineNucleobaseSuivant(List<ADN> choix) {		
		if(choix.contains(Lien.GAP)) {
			choix.remove(choix.indexOf(Lien.GAP));
		}
		
		if(choix.isEmpty()) {
			return Nucleobase.random();
		} else {
			return choix.get(new Random().nextInt(choix.size())).toChar();
		}
	}
	
	/**
	 * Retourne si oui ou non l'ADN est un nucléobase
	 */
	private boolean isNucleobase(ADN adn) {
		return adn != Lien.GAP && adn != Lien.NONE;
	}
		
}
