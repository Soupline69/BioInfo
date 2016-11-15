package model;

public class Controller { // Au lieu du model on peut avoir la collection direct dans le controller
	private Model model;
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public void go() {
		Graphe graphe = model.getGraphe(); // On a le graphe qui possède des fragments // Le graphe a fragments sommets
				
		graphe.getNoeuds().parallelStream().forEach(f -> { // pour chaque noeud du graphe (k)
			int indexDebut = graphe.getNoeuds().indexOf(f);
			graphe.getNoeuds().subList(indexDebut + 1, graphe.getNoeuds().size()).parallelStream().forEach(j -> { // Pour chaque noeud suivant le noeud courant (k + 1 jusque la fin)
				int indexFin = graphe.getNoeuds().indexOf(j);
				Position score = new Alignement(f.getValue(), j.getValue()).score();
				Position scoreInverse = new Alignement(f.getValue(), j.getInverse()).score();
				
				if(score.getY() == 1) { // Si le score maximum est sur la dernière ligne de la matrice
					graphe.addFleche(new Arrete(indexDebut, indexFin, score.getX(), false, false));
					graphe.addFleche(new Arrete(indexFin, indexDebut, score.getX(), true, true));
					graphe.addFleche(new Arrete(indexDebut, indexFin, scoreInverse.getX(), false, true));
					graphe.addFleche(new Arrete(indexFin, indexDebut, scoreInverse.getX(), false, true));
				} else { // Si il est sur la dernière colonne de la matrice
					graphe.addFleche(new Arrete(indexFin, indexDebut, score.getX(), false, false));
					graphe.addFleche(new Arrete(indexDebut, indexFin, score.getX(), true, true));
					graphe.addFleche(new Arrete(indexFin, indexDebut, scoreInverse.getX(), true, false));
					graphe.addFleche(new Arrete(indexDebut, indexFin, scoreInverse.getX(), true, false));
				}
			});
		});  
		
		graphe.trie();

		// On a créé le graphe ! Les sommets + les noeuds 
		/*for(Arrete f : graphe.getArretes()) {
			if(f.getScore() > 100)
			System.out.println("Score de "+f.getDebut()+" avec "+f.getFin()+" : "+f.getScore()+" "+f.isDebutInverse()+" "+f.isFinInverse());
		}*/
		
		System.out.println(graphe.getNoeuds().size());
		
		
		/*Fragment f1 = new Fragment("acgta"); // actgaatgccgat // atcggcattcagt
		Fragment f2 = new Fragment("gcat"); // gccgcatggtctaat // attagaccatgcggc

		System.out.println("Le score de l'alignement = "+graphe.score(f1.getValue(), f2.getValue()).getX()+" et est sur la ligne ? "+graphe.score(f1.getValue(), f2.getValue()).getY()); // f g - f/ g/ - g f - g/ f/
		//System.out.println("Le score de l'alignement = "+f2.score(f1.getInverse()).getX()+" et est sur la ligne ? "+f2.score(f1.getInverse()).getY()); // f g/ - f/ g - g/ f - g f/*/
	}
	
}
