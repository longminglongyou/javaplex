# Introduction #

There are many reasons one might be interested in cohomology rather than homology. With the functionality provided in javaPlex, this is easily accessible.

# Prepare a cosimplex stream #

The simplex stream needs to be dualized. This can be accomplished with `edu.stanford.math.plex4.streams.derived.DualStream`.

# Prepare a cohomological simplex order #

Cohomology proceeds backwards in simplex order. This is easily accomplished by reversing the simplex ordering, e.g. using `java.util.Collections.reverseOrder`.

# Example in Java #

```
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.*;
import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.streams.derived.DualStream;
import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import java.util.*;
import java.lang.*;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.*;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;

public class CohomologyExample {

    public static void main(String[] args) {
        int prime=13;
        int d = 2;
        ExplicitSimplexStream stream = new ExplicitSimplexStream();
        stream.addVertex(0);
        stream.addVertex(1);
        stream.addVertex(2);
        int[][] edges = {{0,1},{0,2},{1,2}};
        for(int i = 0; i != edges.length; ++i){
            stream.addElement(edges[i]);
        }
        stream.finalizeStream();

	AbstractPersistenceBasisAlgorithm<Simplex, int[]> annotated_coalgorithm = new IntAbsoluteHomology(ModularIntField.getInstance(prime), Collections.reverseOrder(SimplexComparator.getInstance()), 0, d+1);
	DualStream costream = new DualStream(stream);
        AnnotatedBarcodeCollection<Double, int[]> annotated_cointervals = annotated_algorithm.computeAnnotatedIntervals(costream);
        System.out.println("\Cohomology basis for " + d + "-sphere:");
        System.out.println(annotated_cointervals);
    }
}
```






Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages