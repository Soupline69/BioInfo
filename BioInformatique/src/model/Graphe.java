package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represente le graphe 
 */
class Graphe {
	private List<Fragment> noeuds;
	private List<Arrete> arretes;
	
	public Graphe(List<Fragment> noeuds) {
		this.noeuds = noeuds;
		this.arretes = new ArrayList<>();
	}
	
	public synchronized void addArrete(Arrete toAdd) {
		arretes.add(toAdd);
	}
	
	public List<Arrete> greddy() {
		List<Arrete> cheminHamilton = new ArrayList<>(noeuds.size() - 1);
		List<Integer> in = new ArrayList<>();
		List<Integer> out = new ArrayList<>();
		List<List<Integer>> ensembles = makeset();
		
		int i = 0;
		while(ensembles.size() > 1) {
			Arrete f = arretes.get(i);
			
			Fragment debut = noeuds.get(f.getDebut());
			Fragment fin = noeuds.get(f.getFin());
			
			int e1 = findset(f.getDebut(), ensembles);
			int e2 = findset(f.getFin(), ensembles);
			
			if(e1 != e2 && !debut.out() && !fin.in() && fragmentsCorrects(debut.isInverse(), f.isDebutInverse(), fin.isInverse(), f.isFinInverse())) {
				cheminHamilton.add(f);
				
				in.add(f.getFin());
				isPremiereArrete(out, in, f.getFin());
				out.add(f.getDebut());
				isPremiereArrete(in, out, f.getDebut());
				
				setAttributs(debut, fin, f);

				unionset(ensembles, e1, e2);
			}
			
			i++;
		}
		
		Collections.swap(cheminHamilton, 0, cheminHamilton.indexOf(new Arrete(out.get(0))));
		
		System.out.println("Le chemin hamiltonien a ete calcule "+cheminHamilton.size()+" fleches choisies sur "+arretes.size());
		arretes.clear();
		return cheminHamilton;
	}
	
	private List<List<Integer>> makeset() {
		List<List<Integer>> ensembles = new ArrayList<>(noeuds.size());
		
		for(int i = 0; i < noeuds.size(); i++) {
			ensembles.add(Stream.of(i).collect(Collectors.toList()));
		}
		
		return ensembles;
	}
	
	private int findset(int fragment, List<List<Integer>> ensembles) {
		int pos = -1;
		int i = 0;
		
		while(pos == -1) {
			List<Integer> l = ensembles.get(i);
			
			if(l.contains(fragment)) {
				pos = i;
			}
			
			i++;
		}
		
		return pos;
	}
	
	private void unionset(List<List<Integer>> ensembles, int e1, int e2) {
		for(Integer i : ensembles.get(e2)) {
			ensembles.get(e1).add(i);
		}
		
		ensembles.remove(e2);	
	}
	
	private void setAttributs(Fragment debut, Fragment fin, Arrete f) {
		debut.setOut(true);
		debut.setIsInverse(f.isDebutInverse() ? 1 : 0);
		fin.setIn(true);
		fin.setIsInverse(f.isFinInverse() ? 1 : 0);
	}
	
	/**
	 * Determine si le fragment sera la premiere ou non.
	 * Un fragment est le premier si son attribut 'out' est vrai et son attribut 'in' est faux.
	 * Pour ne pas refaire une boucle for sur toutes les arretes du chemin hamiltonien, une verification est faite lors de l'ajout de l'arrete dans le chemin hamiltonien, 
	 * a l'aide de deux listes 'in' et 'out'. Si un fragment est dans les deux listes, il ne sera surement pas le premier.
	 */
	private void isPremiereArrete(List<Integer> e1, List<Integer> e2, int fragment) {
		if(e1.contains(fragment)) {
			e2.remove(e2.indexOf(fragment));
			e1.remove(e1.indexOf(fragment));
		}
	}
	
	/**
	 * Determine si les fragments sont de la bonnes formes :
	 * - Si le fragment n'est pas encore choisi alors il sera inverse/complemente ou non en fonction de la valeur debutInverse/finInverse
	 * - Si le fragment a deja ete choisi alors 
	 * 		- s'il est inverse/complemente la valeur de debutInverse/finInverse doit etre true
	 * 		- s'il est normal la valeur de debutInverse/finInverse doit etre false
	 */
	private boolean fragmentsCorrects(int debut, boolean debutInverse, int fin, boolean finInverse) {
		return bonneForme(debut, debutInverse) && bonneForme(fin, finInverse);
	}
	
	private boolean bonneForme(int fragment, boolean fleche) {
		return fragment == -1 || fragment == transform(fleche);
	}
	
	private int transform(boolean inverse) {
		return inverse ? 1 : 0;
	}
	
	public List<Fragment> getNoeuds() {
		return noeuds;
	}
	
	public List<Arrete> getArretes() {
		return arretes;
	}

}
