package model;

public class Controller { // Au lieu du model on peut avoir la collection direct dans le controller
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
		go();
	}
	
	public void go() {
		
		String f1 = model.getCollection().getFragments().get(1);
		String f2 = model.getCollection().getFragments().get(2);
		
		Alignement f1f2 = new Alignement(f1, f2);
		System.out.println(f1f2.getF1());
		System.out.println(f1f2.getF2());
		System.out.println(f1f2.getScore());
	}

}
