package main;

import model.Controller;
import model.ControllerBuffer;
import model.Matrice;
import model.Model;
import model.ModelBuffer;
import model.Position;

public class Main2Buffer {

	public static void main(String[] args) {
		long debut = System.currentTimeMillis();
		new ControllerBuffer(new ModelBuffer("fragments/10000/collection1.fasta")).go();
		long fin = System.currentTimeMillis();
		System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");
	}

}
