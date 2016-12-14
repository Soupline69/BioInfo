package model;

/**
 * Repr�sente un score maximum (de la derni�re ligne ou la derni�re colonne) de la matrice d'alignement
 * Afin de maximiser le chevauchement entre deux fragments, un attribut 'chevauchement' a �t� ajout� afin de d�partager deux arr�tes de m�me score
 */
public class Score implements Comparable<Score> {
	private int score;
	private int chevauchement;
	
	public Score(int score, int chevauchement) {
		this.score = score;
		this.chevauchement = chevauchement;
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
		if (score != other.score)
			return false;
		return true;
	}

	@Override
	public int compareTo(Score toCompare) {
		return score > toCompare.getScore() ? -1 : score < toCompare.getScore() ? 1 : chevauchement > toCompare.getChevauchement() ? 1 : chevauchement < toCompare.getChevauchement() ? -1 : 0;
	}

}
