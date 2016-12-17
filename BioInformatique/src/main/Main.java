package main;

import model.Controller;

class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new Controller(args[0], args[1], args[2]);
		long finish = System.currentTimeMillis();
		System.out.println("Methode executee en " + Long.toString(finish - start) + " millisecondes");
	}

}