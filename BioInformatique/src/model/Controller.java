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

		int i = 1;
		while(i < cheminHamilton.size()) { 
			courante = cheminHamilton.get(cheminHamilton.indexOf(new Arrete(courante.getFin())));
			a = alignePaire(courante);
			alignements.add(a);
			gapBas(a, alignements.get(i-1));		
			int debutAlignement = alignements.get(i - 1).getDebutAlignementF2();
			gapHaut(a, alignements, i);
			a.update(debutAlignement);
			
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
			int debut = a.f1AvantF2() ? 0 : a.getDebutAlignementF1() - a.getDebutAlignementF2();
			int fin = debut + a.getFinAlignementF1() - a.getDebutAlignementF1();
						
			for(int j = 0; j <= fin - debut; j++) {
				updateCompteurs(compteurs, a.getF1().get(j + debut).toChar(), a.getDebutAlignementF1() + Math.abs(min) + j);
			}
			
			if(alignements.indexOf(a) == alignements.size() - 1) {
				debut = a.f1AvantF2() ? a.getDebutAlignementF2() - a.getDebutAlignementF1() : 0;
				fin = debut + a.getFinAlignementF2() - a.getDebutAlignementF2();
								
				for(int j = 0; j <= fin - debut; j++) {
					updateCompteurs(compteurs, a.getF2().get(j + debut).toChar(), a.getDebutAlignementF2() + Math.abs(min) + j);
				}
			}
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
	private void gapBas(Alignement courant, Alignement precedent) {	
		int positionPrecedentF2 = Math.abs(precedent.getDebutAlignementF2() - precedent.getDebutAlignementF1());
		
		for(int j = 0; j < precedent.getChevauchement(); j++) {	
			if(precedent.getF2().get(j + positionPrecedentF2) == Lien.GAP) {
				if(courant.f1AvantF2()) {
					if(precedent.f1AvantF2() && courant.getF1().get(j) != Lien.GAP) 
						addGap(courant, j); 
				    else if(courant.getF1().get(j + positionPrecedentF2) != Lien.GAP) 
						addGap(courant, j + positionPrecedentF2); 
				} else {
					if(!precedent.f1AvantF2() && courant.getF1().get(j + positionPrecedentF2 + Math.abs(courant.getDebutAlignementF2() - courant.getDebutAlignementF1())) != Lien.GAP)
							addGap(courant, j + positionPrecedentF2 + Math.abs(courant.getDebutAlignementF2() - courant.getDebutAlignementF1())); 
					else if(courant.getF1().get(j + Math.abs(courant.getDebutAlignementF2() - courant.getDebutAlignementF1())) != Lien.GAP) 
							addGap(courant, j + Math.abs(courant.getDebutAlignementF2() - courant.getDebutAlignementF1())); 
				}
			}
		}			
	}
	
	/**
	 * Ajoute un gap dans le fragment f1 et f2 de l'alignement a en
	 */
	private void addGap(Alignement a, int position) {
		a.getF1().add(position, a.getF1().get(position) == Lien.NONE || position == a.getDebutAlignementF1() ? Lien.NONE : Lien.GAP);
		a.getF2().add(position, a.getF2().get(position) == Lien.NONE || position == a.getDebutAlignementF2() ? Lien.NONE : Lien.GAP);
		a.increment(position);
	}
	
	/**
	 * Vérifier qu'il n'y a pas un gap dans le f1 de l'alignement courant qu'il faut propager dans les alignements précédents
	 */
	private void gapHaut(Alignement a, List<Alignement> alignements, int i) {	
		int positionGap = alignements.get(i-1).getDebutAlignementF2();

		for(int j = 0; j < a.getChevauchement(); j++) {				
			if(a.f1AvantF2()) {
				if(a.getF1().get(j + a.getDebutChevauchement() - a.getDebutAlignementF1()) == Lien.GAP) {
					positionGap += j + a.getDebutChevauchement();
				}
			} else {
				if(a.getF1().get(j + a.getDebutAlignementF1() - a.getDebutAlignementF2()) == Lien.GAP) {
					positionGap += j + Math.abs(a.getDebutAlignementF2());
				}
			} 
		}
			
		checkGapHaut(positionGap, alignements, i - 1);
	}
	
	/**
	 * Vérifie si un gap peut être propagé vers le haut en positionGap
	 */
	private void checkGapHaut(int positionGap, List<Alignement> alignements, int i) {
		if(positionGap != alignements.get(i).getDebutAlignementF2()) {
			int positionAlignement = i;
			Alignement precedent = alignements.get(positionAlignement);
			
			if(precedent.getFinAlignementF1() >= positionGap) {	
				int position;

				if(precedent.f1AvantF2()) {
					position = positionGap - precedent.getDebutAlignementF1();
				} else {
					position = positionGap - precedent.getDebutAlignementF2();
				}
				
				if(positionAlignement - 1 >= 0 && precedent.getF2().get(position) != Lien.GAP && isNucleobase(precedent.getF1().get(position))) {
					addGap(precedent, position);
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