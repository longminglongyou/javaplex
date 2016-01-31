# GTS 2012 Participants #
  * Please see the [instructions page](http://code.google.com/p/javaplex/wiki/GTS2012) for information on setup and the tutorial


# Introduction #

`javaPlex` can be called from any Java-based system. Below we show some examples.

# Matlab #

As in the load\_javaplex.m file in the matlab examples, one may use the following:

```
javaaddpath('/path/to/javaplex.jar');
import edu.stanford.math.plex4.*;
```

# Jython #

Similarly, from Jython one may use the following:

Invocation:
```
CLASSPATH=$CLASSPATH:/path/to/javaplex.jar jython
```

Loading javaPlex:
```
from edu.stanford.math.plex4 import *
```

# Java #

The following example shows how to run from Java.

In a folder of your choice, download the file javaplex-4.1.0.jar. In the same folder, create a file named `WitnessComplexDemo.java` with the following contents:

```
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;

public class WitnessComplexDemo {

	public static void main(String[] args) {
		
		int n = 10000;
		int d = 2;
		int numLandmarkPoints = 50;
		double maxDistance = 0.1;
		
		double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);
		EuclideanMetricSpace metricSpace = new EuclideanMetricSpace(points);
		
		LandmarkSelector<double[]> landmarkSelector = new MaxMinLandmarkSelector<double[]>(metricSpace, numLandmarkPoints);
		
		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(metricSpace, landmarkSelector, d + 1, maxDistance, 20);
		stream.finalizeStream();
		
		System.out.println("Number of simpleces in complex: " + stream.getSize());
		
		AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getDefaultSimplicialAlgorithm(d + 1);
		
		BarcodeCollection<Double> intervals = algorithm.computeIntervals(stream);
		
		System.out.println("\nBarcodes for " + d + "-sphere:");
		System.out.println(intervals);
	}
}
```

To compile the program, execute:

```
javac -classpath javaplex-4.1.0.jar WitnessComplexDemo.java
```

To run it, execute:

```
java -classpath .:javaplex-4.1.0.jar WitnessComplexDemo
```

Alternatively, one may use an IDE such as [Eclipse](http://www.eclipse.org) and import the necessary jar files.

# Processing #

The Processing IDE and language, focused for producing programmatic art and graphics, works on a Java basis and can access arbitrary Java libraries. With a thin wrapper with some metadata, Java libraries can be packaged as plugins for Processing, making them even easier to use.

As of version 4.1.0, javaPlex includes a Processing library plugin. Install it where Processing expects its plugin files, and you can import and use javaPlex primitives - as demonstrated in the Java section above - from your Processing sketch. We also include an example sketch in the javaPlex distribution demonstrating how to use javaPlex form Processing.

![http://javaplex.googlecode.com/svn/trunk/reports/processing-examples/Processing-Screenshot.png](http://javaplex.googlecode.com/svn/trunk/reports/processing-examples/Processing-Screenshot.png)