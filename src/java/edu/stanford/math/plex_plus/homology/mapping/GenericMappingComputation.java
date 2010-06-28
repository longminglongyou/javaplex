package edu.stanford.math.plex_plus.homology.mapping;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.functional.GenericFunction;
import edu.stanford.math.plex_plus.homology.GenericPersistentHomology;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.HomProductPair;
import edu.stanford.math.plex_plus.homology.simplex_streams.HomStream;
import edu.stanford.math.plex_plus.homology.simplex_streams.SimplexStream;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.Infinity;
import edu.stanford.math.plex_plus.utility.RandomUtility;
import gnu.trove.set.hash.THashSet;

public class GenericMappingComputation<F, T extends ChainBasisElement, U extends ChainBasisElement> {
	private final GenericOrderedField<F> field;
	private final GenericFreeModule<F, HomProductPair<T, U>> chainModule;

	public GenericMappingComputation(GenericOrderedField<F> field) {
		this.field = field;
		chainModule = new GenericFreeModule<F, HomProductPair<T, U>>(this.field);
	}

	public void computeMapping(SimplexStream<T> stream1, SimplexStream<U> stream2, Comparator<T> comparator1, Comparator<U> comparator2) {
		HomStream<T, U> homStream = new HomStream<T, U>(stream1, stream2, comparator1, comparator2);
		homStream.finalizeStream();

		GenericPersistentHomology<F, HomProductPair<T, U>> homology = new GenericPersistentHomology<F, HomProductPair<T, U>>(field, homStream.getBasisComparator());
		AugmentedBarcodeCollection<GenericFormalSum<F, HomProductPair<T, U>>> barcodes = homology.computeIntervals(homStream, 1);
		System.out.println(barcodes);

		List<GenericFormalSum<F, HomProductPair<T, U>>> D_1 = homology.getBoundaryColumns(homStream, 1);
		System.out.println(D_1);

		GenericFormalSum<F, HomProductPair<T, U>> generatingCycle = new GenericFormalSum<F, HomProductPair<T, U>>();
		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycle = chainModule.add(generatingCycle, barcodes.getBarcode(0).getGeneratingCycle(i));
		}

		this.randomizedOptimization(generatingCycle, D_1, this.getImageSimpicialityPenalty(stream1), 100);
	}

	public void randomizedOptimization(GenericFormalSum<F, HomProductPair<T, U>> generatingCycle, List<GenericFormalSum<F, HomProductPair<T, U>>> chainHomotopies, GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		// make sure that we are in a field of nonzero characteristic
		//ExceptionUtility.verifyPositive(characteristic);		

		F minObjectiveValue = field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, HomProductPair<T, U>> minimizingChain = null;

		for (int repitition = 0; repitition < repititions; repitition++) {
			GenericFormalSum<F, HomProductPair<T, U>> randomizedPoint = new GenericFormalSum<F, HomProductPair<T, U>>(generatingCycle);
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(-1, 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}

			F objectiveValue = objective.evaluate(randomizedPoint);
			if (this.field.compare(objectiveValue, minObjectiveValue) < 0) {
				minObjectiveValue = objectiveValue;
				minimizingChain = randomizedPoint;
			}
		}
		
		System.out.println("chain: " + minimizingChain.toString());
		System.out.println("objective value: " + minObjectiveValue + "\n");
	}

	public void greedyOptimization(GenericFormalSum<F, HomProductPair<T, U>> generatingCycle, List<GenericFormalSum<F, HomProductPair<T, U>>> chainHomotopies, GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		int characteristic = this.field.characteristic();

		// make sure that we are in a field of nonzero characteristic
		ExceptionUtility.verifyPositive(characteristic);		

		F minObjectiveValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, HomProductPair<T, U>> minimizingChain = null;

		F metaMinimumValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, HomProductPair<T, U>> metaMinimizingChain = null;

		
		for (int repitition = 0; repitition < repititions; repitition++) {
			GenericFormalSum<F, HomProductPair<T, U>> randomizedPoint = new GenericFormalSum<F, HomProductPair<T, U>>(generatingCycle);
			// create random starting point
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(0, characteristic - 1);
				//int coefficient = this.field.valueOf(i + 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}

			F objectiveValue = objective.evaluate(randomizedPoint);
			minObjectiveValue = objectiveValue;
			minimizingChain = randomizedPoint;

			// perform greedy search
			boolean minimumFound = false;
			while (!minimumFound) {
				boolean functionValueDecreased = false;
				for (int i = 0; i < spaceDimension; i++){
					for(int coefficient = 1; coefficient < characteristic; coefficient++) {
						GenericFormalSum<F, HomProductPair<T, U>> newPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
						objectiveValue = objective.evaluate(newPoint);
						if (this.field.compare(objectiveValue, minObjectiveValue) < 0) {
							minObjectiveValue = objectiveValue;
							minimizingChain = randomizedPoint;
							functionValueDecreased = true;
						}
					}
				}
				if (!functionValueDecreased) {
					minimumFound = true;
				}
			}
			
			if (this.field.compare(minObjectiveValue, metaMinimumValue) < 0) {
				metaMinimumValue = minObjectiveValue;
				metaMinimizingChain = minimizingChain;
			}
		}
		
		System.out.println("minimizing chain: " + metaMinimizingChain.toString());
		System.out.println("minimum objective value: " + metaMinimumValue + "\n");
	}

	private GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> getObjectiveFunction() {
		GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> function = new GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>>() {

			@Override
			public F evaluate(GenericFormalSum<F, HomProductPair<T, U>> argument) {
				return MappingUtility.norm(argument, 1, field);
			}

		};

		return function;
	}

	private GenericFunction<F, GenericFormalSum<F, U>> getNormFunction(final int p) {
		GenericFunction<F, GenericFormalSum<F, U>> function = new GenericFunction<F, GenericFormalSum<F, U>>() {

			@Override
			public F evaluate(GenericFormalSum<F, U> argument) {
				return MappingUtility.norm(argument, p, field);
			}

		};
		return function;
	}

	private GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> getStreamedFunction(final SimplexStream<T> stream, final GenericFunction<F, GenericFormalSum<F, U>> baseFunction) {
		GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> function = new GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>>() {

			@Override
			public F evaluate(GenericFormalSum<F, HomProductPair<T, U>> argument) {
				return sumFunctionOverStream(stream, argument, baseFunction);
			}

		};

		return function;
	}

	private GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> getImageSimpicialityPenalty(final SimplexStream<T> stream) {
		GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>> function = new GenericFunction<F, GenericFormalSum<F, HomProductPair<T, U>>>() {

			@Override
			public F evaluate(GenericFormalSum<F, HomProductPair<T, U>> argument) {
				THashSet<U> domainMap = new THashSet<U>();
				F penalty = field.getZero();
				for (T i: stream) {
					GenericFormalSum<F, U> image = MappingUtility.computeImage(argument, i);
					for (Iterator<Entry<U, F>> iterator = image.iterator(); iterator.hasNext(); ) {
						Entry<U, F> entry = iterator.next();
						if (domainMap.contains(entry.getKey())) {
							penalty = field.add(1, penalty);
						} else {
							domainMap.add(entry.getKey());
						}
					}
				}

				return penalty;
			}

		};

		return function;
	}



	private F sumFunctionOverStream(SimplexStream<T> stream, GenericFormalSum<F, HomProductPair<T, U>> mapping, GenericFunction<F, GenericFormalSum<F, U>> functional) {
		F value = this.field.getZero();

		for (T i: stream) {
			value = field.add(functional.evaluate(MappingUtility.computeImage(mapping, i)), value);
		}

		return value;
	}
}
