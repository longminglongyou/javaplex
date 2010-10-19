package edu.stanford.math.plex4.homology.streams.impl;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;

public class ExplicitSimplexStream extends ExplicitStream<Simplex> {

	public ExplicitSimplexStream(Comparator<Simplex> comparator) {
		super(comparator);
		// TODO Auto-generated constructor stub
	}

	public void addVertex(int index) {
		this.addElement(new Simplex(new int[]{index}), 0);
	}
	
	public void addElement(int[] vertices) {
		this.addElement(new Simplex(vertices), 0);
	}
	
	public void addElement(int[] vertices, int filtrationIndex) {
		this.addElement(new Simplex(vertices), filtrationIndex);
	}
}