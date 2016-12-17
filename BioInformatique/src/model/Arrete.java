package model;

/**
 * Represente une arrete du graphe
 */
class Arrete implements Comparable<Arrete> {
	private int debut;
	private int fin;
	private Score score;
	private boolean debutInverse;
	private boolean finInverse;
	
	/**
	 * Constructeur special (lie a la methode 'equals') permettant de trouver la position d'une arrete dans une liste d'arrete en ne connaissant que le fragment de debut
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

	private Score getScore() {
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
		return debut == other.debut;
	}
	
	@Override
	public int compareTo(Arrete toCompare) {
		return score.compareTo(toCompare.getScore());
	}

}
