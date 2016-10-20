package model;

public class Controller { // Au lieu du model on peut avoir la collection direct dans le controller
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
		go();
	}
	
	public void go() {
		for(int i = 0; i < model.getCollection().getFragments().size(); i++) {
			String f1 = model.getCollection().getFragments().get(i);
			for(int j = i + 1; j < model.getCollection().getFragments().size(); j++) {
				String f2 = model.getCollection().getFragments().get(j);
				Alignement alignement = new Alignement(f1, f2);

				System.out.println("Le fragment "+(i+1)+" avec le fragment "+(j+1)+" pour un score de "+alignement.getScore());
			}
		}
		
		/*String f1 = model.getCollection().getFragments().get(0);
		String f2 = model.getCollection().getFragments().get(62); // 7, 39, 62
		
		Alignement f1f2 = new Alignement(f1, f2);
		System.out.println(f1f2.getF1());
		System.out.println(f1f2.getF2());
		System.out.println(f1f2.getScore());*/
	}

}
