package model;

public class Model {
	private Collection collection;
	
	public Model(String file) {
		this.collection = Reader.getCollection(file);
	}
	
	public Collection getCollection() {
		return collection;
	}

}
