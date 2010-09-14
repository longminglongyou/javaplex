package edu.stanford.math.plex4.homology;

import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.PersistenceInterval;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.RipsStream;
import edu.stanford.math.plex4.algebraic_structures.impl.ModularIntField;
import edu.stanford.math.plex4.algebraic_structures.impl.ModularIntegerField;
import edu.stanford.math.plex4.array_utility.IntArrayMath;
import edu.stanford.math.plex4.homology.PersistenceCalculationData.PersistenceAlgorithmType;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.StreamUtility;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.test_utility.Timing;

public class PersistenceAlgorithmTester {
	
	public static PersistenceAlgorithmResult testVietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3Vietoris(points, maxDimension, maxFiltrationValue, numDivisions);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			SearchableFiniteMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points);
			VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension + 1, numDivisions);
			stream.finalizeStream();
			BarcodeCollection barcodeCollection = computePlex4Barcodes(stream, maxDimension, type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(maxFiltrationValue);
			result.setNumPoints(points.length);
			result.setNumSimplices(StreamUtility.getSize(stream));
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}
	
	public static PersistenceAlgorithmResult testLazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3LazyWitness(selector, maxDimension, maxFiltrationValue, numDivisions);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(selector.getUnderlyingMetricSpace(), selector, maxDimension + 1, maxFiltrationValue, numDivisions);
			stream.finalizeStream();
			BarcodeCollection barcodeCollection = computePlex4Barcodes(stream, maxDimension, type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(maxFiltrationValue);
			result.setNumPoints(StreamUtility.getSkeletonSize(stream, 0));
			result.setNumSimplices(StreamUtility.getSize(stream));
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}

	public static PersistenceAlgorithmResult testExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream, int maxDimension, PersistenceAlgorithmType type) {
		if (type == PersistenceAlgorithmType.Plex3Homology) {
			return testPlex3ExplicitStream(plex4Stream);
		} else {
			PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
			
			Timing.restart();
			
			BarcodeCollection barcodeCollection = computePlex4Barcodes(plex4Stream, maxDimension, type);
			
			Timing.stop();
			
			result.setBarcodeCollection(barcodeCollection);
			result.setMaxDimension(maxDimension);
			result.setMaxFiltrationValue(0);
			result.setNumPoints(StreamUtility.getSkeletonSize(plex4Stream, 0));
			result.setNumSimplices(StreamUtility.getSize(plex4Stream));
			result.setSeconds(Timing.seconds());
			result.setType(type);
			
			return result;
		}
	}

	private static PersistenceAlgorithmResult testPlex3ExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		edu.stanford.math.plex.ExplicitStream plex3Stream = convertToPlex3ExplicitStream(plex4Stream);
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(plex3Stream);
		BarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(plex3Stream.maxDimension());
		result.setMaxFiltrationValue(0);
		result.setNumPoints(StreamUtility.getSkeletonSize(plex4Stream, 0));
		result.setNumSimplices(plex3Stream.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}
	
	private static BarcodeCollection testPlex3ExplicitStream(edu.stanford.math.plex.ExplicitStream plex3Stream) {
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(plex3Stream);
		return convertFromPlex3PersistenceIntervals(intervals);
	}
	
	private static PersistenceAlgorithmResult testPlex3Vietoris(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);		
		RipsStream stream = Plex.RipsStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, pData);
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(stream);
		BarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(maxDimension);
		result.setMaxFiltrationValue(maxFiltrationValue);
		result.setNumPoints(pData.count());
		result.setNumSimplices(stream.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}

	private static PersistenceAlgorithmResult testPlex3LazyWitness(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		PersistenceAlgorithmResult result = new PersistenceAlgorithmResult();
		
		Timing.restart();
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(selector.getUnderlyingMetricSpace().getPoints());
		edu.stanford.math.plex.LazyWitnessStream wit = Plex.LazyWitnessStream(maxFiltrationValue / numDivisions, maxDimension + 1, maxFiltrationValue, 2, convertTo1Based(selector.getLandmarkPoints()), pData);
		PersistenceInterval[] intervals = Plex.Persistence().computeIntervals(wit);
		BarcodeCollection barcodeCollection = convertFromPlex3PersistenceIntervals(intervals);
		
		Timing.stop();
		
		result.setBarcodeCollection(barcodeCollection);
		result.setMaxDimension(maxDimension);
		result.setMaxFiltrationValue(maxFiltrationValue);
		result.setNumPoints(pData.count());
		result.setNumSimplices(wit.size());
		result.setSeconds(Timing.seconds());
		result.setType(PersistenceAlgorithmType.Plex3Homology);
		
		return result;
	}
	
	private static BarcodeCollection computePlex4Barcodes(AbstractFilteredStream<Simplex> stream, int d, PersistenceAlgorithmType type) {
		switch (type) {
		case GenericClassicalHomology:
			return computeClassicalHomology(stream, d);
		case GenericDualityHomology:
			return computeDualityHomology(stream, d);
		case GenericDualityCohomology:
			return computeDualityCohomology(stream, d);
		default:
			return null;
		}
	}

	private static BarcodeCollection computeClassicalHomology(AbstractFilteredStream<Simplex> stream, int d) {
		ClassicalPersistentHomology<Simplex> classicalHomology = new ClassicalPersistentHomology<Simplex>(ModularIntField.getInstance(13), SimplexComparator.getInstance());
		BarcodeCollection barcodes = classicalHomology.computeIntervals(stream, d + 1);
		return barcodes;
	}
	
	private static BarcodeCollection computeDualityHomology(AbstractFilteredStream<Simplex> stream, int d) {
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteHomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		return barcodes;
	}
	
	private static BarcodeCollection computeDualityCohomology(AbstractFilteredStream<Simplex> stream, int d) {
		GenericPersistenceAlgorithm<Integer, Simplex> homology = new GenericAbsoluteCohomology<Integer, Simplex>(ModularIntegerField.getInstance(13), SimplexComparator.getInstance(), d);
		BarcodeCollection barcodes = homology.computeIntervals(stream);
		return barcodes;
	}

	private static BarcodeCollection convertFromPlex3PersistenceIntervals(PersistenceInterval[] intervals) {
		BarcodeCollection barcodes = new BarcodeCollection();

		for (PersistenceInterval interval: intervals) {
			if (interval.infiniteExtent()) {
				barcodes.addRightInfiniteInterval(interval.dimension, interval.toDouble()[0]);
			} else {
				barcodes.addInterval(interval.dimension, interval.toDouble()[0], interval.toDouble()[1]);
			}
		}

		return barcodes;
	}

	private static edu.stanford.math.plex.ExplicitStream convertToPlex3ExplicitStream(edu.stanford.math.plex4.homology.streams.impl.ExplicitStream<Simplex> plex4Stream) {
		edu.stanford.math.plex.ExplicitStream plex3Stream = new edu.stanford.math.plex.ExplicitStream();

		for (Simplex simplex: plex4Stream) {
			plex3Stream.add(IntArrayMath.scalarAdd(simplex.getVertices(), 1), plex4Stream.getFiltrationValue(simplex));
		}
		
		plex3Stream.close();

		return plex3Stream;
	}
	
	private static int[] convertTo1Based(int[] array) {
		int[] result = new int[array.length + 1];
		result[0] = 0;
		for (int i = 0; i < array.length; i++) {
			result[i + 1] = array[i] + 1;
		}
		return result;
	}
	
}
