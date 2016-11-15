package main;

import model.Controller;
import model.Model;

public class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new Controller(new Model("fragments/10000/collection1.fasta")).go();
		long finish = System.currentTimeMillis();
		System.out.println("Méthode exécutée en " + Long.toString(finish - start) + " millisecondes");
	}

}