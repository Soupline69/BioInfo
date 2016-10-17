package model;

public class Controller { // Au lieu du model on peut avoir la collection direct dans le controller
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
		go();
	}
	
	public void go() {
		
		String f1 = model.getCollection().getFragments().get(50);
		String f2 = model.getCollection().getFragments().get(90);
		
		Alignement f1f2 = new Alignement(f1, f2);
	}

}
