package model;

public class Model {
	private Graphe graphe;
	
	public Model(String file) {
		this.graphe = Reader.getGraphe(file);
	}
	
	public Graphe getGraphe() {
		return graphe;
	}

}
