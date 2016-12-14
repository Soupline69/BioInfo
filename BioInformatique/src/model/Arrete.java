package model;

/**
 * Représente une arrête du graphe
 */
public class Arrete implements Comparable<Arrete> {
	private int debut;
	private int fin;
	private Score score;
	private boolean debutInverse;
	private boolean finInverse;
	
	/**
	 * Constructeur spécial (lié à la méthode 'equals') permettant de trouver la position d'une arrête dans une liste d'arrête en ne connaissant que le fragment de début
	 */
	public Arrete(int debut) {
		this.debut = debut;
	}
	
	public Arrete(int debut, int fin, Score score, boolean debutInverse, boolean finInverse) {
		this.debut = debut;
		this.fin = fin;
		this.score = score;
		this.debutInverse = debutInverse;
		this.finInverse = finInverse;
	}

	public int getDebut() {
		return debut;
	}

	public int getFin() {
		return fin;
	}

	public Score getScore() {
		return score;
	}
	
	public boolean isDebutInverse() {
		return debutInverse;
	}

	public boolean isFinInverse() {
		return finInverse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + debut;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arrete other = (Arrete) obj;
		if (debut != other.debut)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Arrete toCompare) {
		return score.compareTo(toCompare.getScore());
	}

}
