package main;

import model.Controller;
import model.Matrice;
import model.Model;
import model.Position;

public class Main2 {

	public static void main(String[] args) {
		new Controller(new Model("fragments/10000/collection1.fasta"));
	}

}
