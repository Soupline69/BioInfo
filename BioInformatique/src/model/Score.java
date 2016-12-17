package model;

/**
 * Represente un score maximum (de la derniere ligne ou la derniere colonne) de la matrice d'alignement
 * Afin de maximiser le chevauchement entre deux fragments, un attribut 'chevauchement' a ete ajoute afin de departager deux arretes de meme score
 */
public class Score implements Comparable<Score> {
	private int score;
	private int chevauchement;
	
	public Score() {
		this.score = 0;
		this.chevauchement = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getChevauchement() {
		return chevauchement;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setChevauchement(int chevauchement) {
		this.chevauchement = chevauchement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chevauchement;
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
		Score other = (Score) obj;
		if (chevauchement != other.chevauchement)
			return false;
		return score == other.score;
	}

	@Override
	public int compareTo(Score toCompare) {
		return score > toCompare.getScore() ? -1 : score < toCompare.getScore() ? 1 : chevauchement > toCompare.getChevauchement() ? 1 : chevauchement < toCompare.getChevauchement() ? -1 : 0;
	}

}
