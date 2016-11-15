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
	 * synchronised permet de s�curiser le fait que plusieurs thread peuvent acc�der en m�me temps � cette m�thode
	 * A chaque ajout de fl�che, on trie la liste pour que la plus forte soit au dessus.
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
