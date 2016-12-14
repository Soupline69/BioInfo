package main;

import model.Controller;

public class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new Controller(args[0], args[1], args[2]);
		long finish = System.currentTimeMillis();
		System.out.println("Méthode exécutée en " + Long.toString(finish - start) + " millisecondes");
	}

}