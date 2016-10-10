package model;

import java.util.List;

public class Collection {
	private List<Fragment> fragments;
	
	public Collection(List<Fragment> fragments) {
		this.fragments = fragments;
	}
	
	public List<Fragment> getFragments() {
		return fragments;
	}
	
	// Faire un alignement semi global entre toutes les paires de fragments
	
	// Imaginons que nous ayons f1, f2 faire l'aligment semi global entre
	
	// f1/f2, f1/f2ci, f2/f1, f2/f1ci, f1ci/f2, f1ci/f2ci, f2ci/f1, f2ci/f1ci
	
}
