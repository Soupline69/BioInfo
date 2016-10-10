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

}
