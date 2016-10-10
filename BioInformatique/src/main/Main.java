package main;

import model.Reader;

public class Main {

	public static void main(String[] args) {
		System.out.println(Reader.getCollection("fragments/10000/collection1.fasta").getFragments().size());
	}

}
