package model;

public class Controller { // Au lieu du model on peut avoir la collection direct dans le controller
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public void go() {
	/*	Graphe graphe = model.getGraphe(); // On a le graphe qui possède des fragments // Le graphe a fragments sommets
		
		graphe.getNoeuds().parallelStream().forEach(f -> { // pour chaque noeud du graphe (k)
			int indexDebut = graphe.getNoeuds().indexOf(f);
			graphe.getNoeuds().subList(indexDebut + 1, graphe.getNoeuds().size()).parallelStream().forEach(j -> { // Pour chaque noeud suivant le noeud courant (k + 1 jusque la fin)
				 int indexFin = graphe.getNoeuds().indexOf(j);
				 int score = f.score(j.getValue());
				 int scoreInverse = f.score(j.getInverse());
				 
				 graphe.addFleche(new Arrete(indexDebut, indexFin, score, false, false));
				 //graphe.addFleche(new Fleche(indexDebut, indexFin, score, true, true));
				 //graphe.addFleche(new Fleche(indexDebut, indexFin, scoreInverse, true, false));
				 graphe.addFleche(new Arrete(indexDebut, indexFin, scoreInverse, false, true));
			});
		});  
		
		graphe.trie();

		// On a créé le graphe ! Les sommets + les noeuds 
		for(Arrete f : graphe.getArretes()) {
			if(f.getScore() > 50)
			System.out.println("Score de "+f.getDebut()+" avec "+f.getFin()+" : "+f.getScore());
		}
		*/
		
		Fragment f1 = new Fragment("attagaccatgcggc");
		Fragment f2 = new Fragment("atcggcattcagt");

		int[][] matrice = f2.getMatrice(f1.getValue());
		
		for(int i = 0; i < f2.getValue().size() + 1; i++) {
			for(int j = 0; j < f1.getValue().size() + 1; j++) {
				System.out.print(matrice[i][j]+" ");
			}
			System.out.println();
		}
		
		System.out.println("Le score de l'alignement = "+f2.score(f1.getValue()));
		
	}
	
	/*Fragment f1 = new Fragment("accgtgc");
	Fragment f2 = new Fragment("ttac");
	
	Alignement f1f2 = new Alignement(f1.getNucleobases(), f2.getNucleobases());
	System.out.println(f1f2.getScore());
	f1f2.alignementSemiGlobal();
	System.out.println(f1f2.getF1());
	System.out.println(f1f2.getF2());*/
	
	/*	for(int i = 0; i < graphe.getNodes().size(); i++) {
	System.out.println("Le fragment "+(i));
	for(int j = 0; j < graphe.getNodes().get(i).getAlignements().size(); j++) {
		System.out.println("Score : "+graphe.getNodes().get(i).getAlignements().get(j).getScore());
	}
}

System.out.println(new Alignement(graphe.getNodes().get(2).getValue(), graphe.getNodes().get(3).getValue()).getScore());*/

}
