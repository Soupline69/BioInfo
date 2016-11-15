package model;

public class Arrete implements Comparable<Arrete> {
	private int debut;
	private int fin;
	private int score;
	private boolean debutInverse;
	private boolean finInverse;
	
	public Arrete(int debut, int fin, int score, boolean debutInverse, boolean finInverse) {
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

	public int getScore() {
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
		result = prime * result + score;
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
		if (score != other.score)
			return false;
		return true;
	}

	@Override
	public int compareTo(Arrete toCompare) {
		return score > toCompare.getScore() ? -1 : score < toCompare.getScore() ? 1 : 0; 
	}

}
