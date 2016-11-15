package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graphe {
	private List<Fragment> noeuds;
	private List<Arrete> arretes;
	
	public Graphe(List<Fragment> fragments) {
		this.noeuds = fragments;
		this.arretes = new ArrayList<>();
	}
	
	/**
	 * synchronised permet de sécuriser le fait que plusieurs thread peuvent accèder en même temps à cette méthode
	 * A chaque ajout de flèche, on trie la liste pour que la plus forte soit au dessus.
	 */
	public synchronized void addFleche(Arrete toAdd) {
		arretes.add(toAdd);
	}
	
	public List<Arrete> getCheminHamilton() {
		return null;
	}
	
	public List<Fragment> getNoeuds() {
		return noeuds;
	}
	
	public List<Arrete> getArretes() {
		return arretes;
	}
	
	public void trie() {
		Collections.sort(arretes);
	}

}
