package model;

public class ControllerBuffer { // Au lieu du model on peut avoir la collection direct dans le controller
	private ModelBuffer model;
	
	public ControllerBuffer(ModelBuffer model) {
		this.model = model;
	}
	
	public void go() {
		for(int i = 0; i < model.getCollection().getFragments().size(); i++) {
			String f1 = model.getCollection().getFragments().get(i).toString();
			for(int j = i + 1; j < model.getCollection().getFragments().size(); j++) {
				String f2 = model.getCollection().getFragments().get(j).toString();
				AlignementBuffer alignement = new AlignementBuffer(new StringBuffer(f1), new StringBuffer(f2));

				System.out.println("Le fragment "+(i+1)+" avec le fragment "+(j+1)+" pour un score de "+alignement.getScore());
			}
		}
		
		/*StringBuffer f1 = model.getCollection().getFragments().get(0);
		StringBuffer f2 = model.getCollection().getFragments().get(4); // 7, 39, 62
		
		AlignementBuffer f1f2 = new AlignementBuffer(f1, f2);
		System.out.println(f1f2.getF1());
		System.out.println(f1f2.getF2());
		System.out.println("\n"+f1f2.getScore());*/
	}

}
