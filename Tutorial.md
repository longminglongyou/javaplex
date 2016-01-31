# Introduction #

The pdf version of this tutorial, which is the recommended format, is available at http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/javaplex_tutorial.pdf.

## javaPlex ##

javaPlex is a Java software package for computing the persistent homology of filtered chain complexes, with special emphasis on applications arising in topological data analysis. The main author is Andrew Tausz. javaPlex is a re-write of the JPlex package, which was written by Harlan Sexton and Mikael Vejdemo Johansson. The main motivation for the development of javaPlex was the need for a flexible platform that supported new directions of research in topological data analysis and computational persistent homology. The website for javaPlex is http://code.google.com/p/javaplex/ and the javadoc tree for the library is at http://javaplex.googlecode.com/svn/trunk/doc/index.html.

This tutorial is written for those using javaPlex with Matlab. However, one can run javaPlex without Matlab; see http://code.google.com/p/javaplex/wiki/Interop.

If you are interested in javaPlex, then you may also be interested in the software package Dionysus by Dmitriy Morozov, available at http://www.mrzv.org/software/dionysus.

Some of the exercises in this tutorial are borrowed from Vin de Silva's _Plexercises_, available at http://comptop.stanford.edu/u/programs/Plexercises2.pdf.

Please email Henry at `henrya@ima.umn.edu` or Andrew at `atausz@alumni.stanford.edu` if you have questions about this tutorial.


## License ##

javaPlex is an open source software package under the Open BSD License. The source code can be found at http://code.google.com/p/javaplex/. If you are interested in contributing to the project, we invite you to contact either of the authors.


## Installation for Matlab ##

Open Matlab and check which version of Java is being used. In this tutorial, the symbol `>>` precedes commands to enter into your Matlab window.
```
>> version -java 
ans = Java 1.5.0_13 with Apple Inc.   Java Hotspot(TM) Client VM mixed mode, sharing
```

javaPlex requires version number `1.5` or higher.

To install javaPlex for Matlab, go the the website http://code.google.com/p/javaplex/downloads/list. Download the zip file containing the Matlab examples. It should be called something like `matlab-examples-4.1.0.tar.gz`.
Extract the zip file. The resulting folder should be called `matlab-examples`.

Change directories in Matlab  to `matlab-examples`. Run the `load_javaplex.m` file.
```
>> load_javaplex
```
Installation is complete. Confirm that javaPlex is working properly with the following command.
```
>> api.Plex4.createExplicitSimplexStream() 
ans = edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream@513fd4
```
Your output should be the same except for the last several characters. Each time upon starting a new Matlab session, you will need to run `load_javaplex.m`.


## Java heap size ##

Depending on the size of your javaPlex computations, you may need to increase the maximum Java heap size. This should not be necessary for the examples in this tutorial.

The following command returns your maximum heap size in bytes.
```
>> java.lang.Runtime.getRuntime.maxMemory
ans = 130875392
```
My Matlab installation sets the limit to approximately 128 megabytes by default. To increase your limit to, say, 1.5 gigabytes, create a file named `java.opts` in your Matlab directory which contains the text `-Xmx1500m` and then restart Matlab. For more information, please see this link: http://www.mathworks.com/support/solutions/en/data/1-18I2C/.


## Accompanying files ##

The following Matlab scripts containing the commands in this tutorial are available in the folder `matlab_examples/tutorial_examples`. This means that you don't need to type in each command individually.

  * `core_subsets_example.m`
  * `euler_characteristic_example.m`
  * `explicit_metric_space.m`
  * `explicit_simplex_example.m`
  * `house_example.m`
  * `image_patch_examples.m`
  * `landmark_example.m`
  * `lazy_witness_example.m`
  * `pointcloud_examples.m`
  * `rips_example.m`
  * `witness_example.m`

The folder `matlab_examples/tutorial_examples` also contains the following Matlab functions

  * `coreSubset.m`
  * `dct.m`
  * `eulerCharacteristic.m`

and the following Matlab data files

  * `pointsRange.mat`
  * `pointsTorusGrid.mat`

which are used in this tutorial.

The folder `matlab_examples/tutorial_solutions` contains the following solution scripts to tutorial exercises.

  * `exercise_1.m`
  * `exercise_2.m`
  * `exercise_3.m`
  * `exercise_4.m`
  * `exercise_5.m`
  * `exercise_6.m`
  * `exercise_8.m`
  * `exercise_9.m`
  * `exercise_16.m`
  * `exercise_17.m`
  * `exercise_18.m`
  * `exercise_19.m`
  * `flatKleinDistanceMatrix.m`
  * `flatTorusDistanceMatrix.m`
  * `getDoubleTorusPoints.m`
  * `projPlaneDistanceMatrix.m`

See Appendix [Exercise Solutions](Tutorial#Exercise_solutions.md) for exercise solutions.




# Math review #

Below is a brief math review. For more details, see Armstrong (1983), Edelsbrunner and Harer (2010), Edelsbrunner et al. (2002), Hatcher (2002), and Zomorodian and Carlsson (2005).

## Simplicial complexes ##

An abstract simplicial complex is given by the following data.

  * A set _Z_ of vertices or 0-simplices.
  * For each _k≥1_, a set of _k_-simplices _s=[z<sub>0</sub>,z<sub>1</sub>,…,z<sub>k</sub>]_, where _z<sub>i</sub>_ is in _Z_.
  * Each _k_-simplex has _k+1_ faces obtained by deleting one of the vertices. The following membership property must be satisfied: if _s_ is in the simplicial complex, then all faces of _s_ must be in the simplicial complex.

We think of 0-simplices as vertices, 1-simplices as edges, 2-simplices as triangular faces, and 3-simplices as tetrahedrons.


## Homology ##

Betti numbers help describe the homology of a simplicial complex _X_. The value _Betti<sub>k</sub>_, where _k_ is a natural number, is equal to the rank of the _k_-th homology group of _X_. Roughly speaking, _Betti<sub>k</sub>_ gives the number of _k_-dimensional holes. In particular, _Betti<sub>0</sub>_ is the number of connected components. For instance, a _k_-dimensional sphere has all Betti numbers equal to zero except for _Betti<sub>0</sub> = Betti<sub>k</sub> = 1_.


## Filtered simplicial complexes ##

A filtration on a simplicial complex _X_ is a collection of subcomplexes _X(t)_ of _X_ for real values of _t_ such that _X(t)_ is a subcomplx of _X(s)_ whenever _t ≤ s_. The filtration time of a simplex _s_ in _X_ is the smallest _t_ such that _s_ is in _X(t)_. In javaPlex, filtered simplicial complexes (or more generally filtered chain complexes) are called streams.

## Persistent homology ##

Betti intervals help describe how the homology of _X(t)_ changes with _t_. A _k_-dimensional Betti interval, with endpoints [_t<sub>start</sub>_, _t<sub>end</sub>_), corresponds roughly to a _k_-dimensional hole that appears at filtration time _t<sub>start</sub>_, remains open for _t<sub>start</sub> ≤ t < t<sub>end</sub>_, and closes at time _t<sub>end</sub>_. We are often interested in Betti intervals that persist for a long filtration range.

Persistent homology depends heavily on functoriality: for _t ≤ s_, the inclusion _i:X(t) -> X(s)_ of simplicial complexes induces a map _i<sub>*</sub>:H<sub>k</sub>(X(t)) -> H<sub>k</sub>(X(s))_ between homology groups.




# Explicit simplex streams #

In javaPlex, filtered simplicial complexes (or more generally filtered chain complexes) are called streams. The class `ExplicitSimplexStream` allows one to build a simplicial complex from scratch. In [Streams from point cloud data](Tutorial#Streams_from_point_cloud_data.md) we will learn about other automated methods of generating simplicial complexes; namely the Vietoris-Rips, witness, and lazy witness constructions.


## Computing homology ##

You should change your current Matlab directory to `tutorial_examples`, perhaps using the following command.
```
>> cd tutorial_examples
```
The Matlab script corresponding to this section is `explicit_simplex_example.m`, which is in the folder `tutorial_examples`. You may copy and paste commands from this script into the Matlab window, or you may run the entire script at once with the following command.
```
>> explicit_simplex_example
```



_Circle example._ Let's build a simplicial complex homeomorphic to a circle. We have three 0-simplices: `[0]`, `[1]`, `[2]`, and three 1-simplices: `[0,1]`, `[0,2]`, `[1,2]`.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/s1.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/s1.png)

To build a simplicial complex in javaPlex we simply build a stream in which all filtration times are zero. First we create an empty explicit simplex stream. Many command lines in this tutorial will end with a semicolon to supress unwanted output.
```
>> stream = api.Plex4.createExplicitSimplexStream();
```

Next we add simplicies using the methods `addVertex` and `addElement`. The first creates a vertex with a specified index, and the second creates a _k_-simplex (for _k >0_) with the specified array of vertices. Since we don't specify any filtration times, by default all added simplices will have filtration time zero.

```
>> stream.addVertex(0);
>> stream.addVertex(1);
>> stream.addVertex(2);
>> stream.addElement([0, 1]);
>> stream.addElement([0, 2]);
>> stream.addElement([1, 2]);
>> stream.finalizeStream();
```

We print the total number of simplices in the complex.
```
>> num_simplices = stream.getSize()
num_simplices = 6
```

We create an object that will compute the homology of our complex. The first input parameter 3 indicates that homology will be computed in dimensions 0, 1, and 2 — that is, in all dimensions strictly less than 3. The second input 2 means that we will compute homology with _Z/2Z_ coefficients, and this input can be any prime number.
```
>> persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);
```
We compute and print the intervals.
```
>> circle_intervals = persistence.computeIntervals(stream) 
circle_intervals = 

Dimension: 1 
[0.0, infinity) 
Dimension: 0 
[0.0, infinity)
```
This gives us the expected Betti numbers _Betti<sub>0</sub>=1_ and _Betti<sub>1</sub>=1_.

The persistence algorithm computing intervals can also find a representative cycle for each interval. However, there is no guarantee that the produced representative will be geometrically nice.
```
>> circle_intervals = persistence.computeAnnotatedIntervals(stream)
circle_intervals =

Dimension: 1
[0.0, infinity): [1,2] + [0,2] + [0,1]
Dimension: 0
[0.0, infinity): [0]
```

A representative cycle generating the single 0-dimensional homology class is `[0]`, and a representative cycle generating the single 1-dimensional homology class is `[1,2] + [0,2] + [0,1]`.

_9-sphere example._ Let's build a 9-sphere, which is homeomorphic to the boundary of a 10-simplex. First we add a single 10-simplex to an empty explicit simplex stream. The result is not a simplicial complex because it does not contain the faces of the 10-simplex. We add all faces using the method `ensureAllFaces`. Then, we remove the 10-simplex using the method `removeElementIfPresent`. What remains is the boundary of a 10-simplex, that is, a 9-sphere.

```
>> dimension = 9; 
>> stream = api.Plex4.createExplicitSimplexStream(); 
>> stream.addElement(0:(dimension + 1)); 
>> stream.ensureAllFaces(); 
>> stream.removeElementIfPresent(0:(dimension + 1)); 
>> stream.finalizeStream(); 
```

In the above, the `finalizeStream` function is used to ensure that the stream has been fully constructed and is ready for consumption by a persistence algorithm. Note that it can be omitted in the case where the simplex additions are done in increasing order. However, it should be used in general.

We print the total number of simplices in the complex.
```
>> num_simplices = stream.getSize()
num_simplices = 2046
```

We get the persistence algorithm
```
persistence = api.Plex4.getModularSimplicialAlgorithm(dimension + 1, 2);
```
and compute and print the intervals.
```
>> n_sphere_intervals = persistence.computeIntervals(stream) 
n_sphere_intervals = 

Dimension: 9
[0.0, infinity)
Dimension: 0
[0.0, infinity)
```
This gives us the expected Betti numbers _Betti<sub>0</sub>=1_ and _Betti<sub>9</sub>=1_.

Try computing a representative cycle for each barcode.
```
>> n_sphere_intervals = persistence.computeAnnotatedIntervals(stream)
```
We don't display the output from this command in the tutorial, because the representative 9-cycle is very long and contains all eleven 9-simplices.

See Appendix [Exercise Solutions](Tutorial#Exercise_solutions.md) for exercise solutions.

_Exercise_:
Build a simplicial complex homeomorphic to the torus. Compute its Betti numbers. _Hint: You will need at least 7 vertices_ (Hatcher 2002, page 107). _We recommend using a_3x3_grid of 9 vertices._

_Exercise_:
Build a simplicial complex homeomorphic to the Klein bottle. Check that it has the same Betti numbers as the torus over _Z/2Z_ coefficients but different Betti numbers over _Z/3Z_ coefficients.

_Exercise_:
Build a simplicial complex homeomorphic to the projective plane. Find its Betti numbers over _Z/2Z_ and _Z/3Z_ coefficients.



## Computing persistent homology ##

Let's build a stream with nontrivial filtration times.

_House example._ The Matlab script corresponding to this section is `house_example.m`.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/houseFig.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/houseFig.png)

We build a house, with the vertices and edges on the square appearing at time 0, with the top vertex appearing at time 1, with the roof edges appearing at times 2 and 3, and with the roof 2-simplex appearing at time 7.
```
>> stream = api.Plex4.createExplicitSimplexStream(); 
>> stream.addVertex(1, 0); 
>> stream.addVertex(2, 0); 
>> stream.addVertex(3, 0); 
>> stream.addVertex(4, 0); 
>> stream.addVertex(5, 1); 
>> stream.addElement([1, 2], 0); 
>> stream.addElement([2, 3], 0); 
>> stream.addElement([3, 4], 0); 
>> stream.addElement([4, 1], 0); 
>> stream.addElement([3, 5], 2); 
>> stream.addElement([4, 5], 3); 
>> stream.addElement([3, 4, 5], 7);
>> stream.finalizeStream();
```
We get the persistence algorithm with _Z/2Z_ coefficients
```
>> persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);
```
and compute the intervals.
```
>> intervals = persistence.computeIntervals(stream)
intervals =

Dimension: 1
[3.0, 7.0)
[0.0, infinity)
Dimension: 0
[1.0, 2.0)
[0.0, infinity)
```

There are four intervals. The first is a _Betti<sub>1</sub>_ interval, starting at filtration time 3 and ending at 7. This 1-dimensional hole is formed by the three edges of the roof. It forms when edge `[4,5]` appears at filtration time 3 and closes when 2-simplex `[3,4,5]` appears at filtration time 7.

We can also store the intervals as Matlab matrices.
```
>> intervals_dim0 = edu.stanford.math.plex4.homology.barcodes.BarcodeUtility.getEndpoints(intervals, 0, 0)
intervals_dim0 =

0     Inf
1     2

>> intervals_dim1 = edu.stanford.math.plex4.homology.barcodes.BarcodeUtility.getEndpoints(intervals, 1, 0)
intervals_dim1 =

0     Inf
3     7
```

The second input of this command is the dimension of the intervals, and the third input is a Boolean flag: 0 to include infinite intervals, and 1 to exclude infinite intervals.

We compute a representative cycle for each barcode.
```
>> intervals = persistence.computeAnnotatedIntervals(stream)
intervals =

Dimension: 1
[3.0, 7.0): [4,5] + [3,4] + -[3,5]
[0.0, infinity): [1,4] + [2,3] + [1,2] + [3,4]
Dimension: 0
[1.0, 2.0): -[3] + [5]
[0.0, infinity): [1]
```

One _Betti<sub>0</sub>_ interval and one _Betti<sub>1</sub>_ interval are semi-infinite.
```
>> infinite_barcodes = intervals.getInfiniteIntervals();
```
We can print the Betti numbers (at the largest filtration time 7) as an array
```
>> betti_numbers_array = infinite_barcodes.getBettiSequence()
betti_numbers_array = 

1
1
```

or as a list with entries of the form _k: Betti<sub>k</sub>_.
```
>> betti_numbers_string = infinite_barcodes.getBettiNumbers()
betti_numbers_string = {0: 1, 1: 1}
```

The Matlab function `plot_barcodes.m` lets us display the intervals as Betti barcodes. The Matlab structure array `options` contains different options for the plot. We choose the filename `house` and we choose the maximum filtration time for the plot to be eight.
```
>> options.filename = 'house';
>> options.max_filtration_value = 8;
>> plot_barcodes(intervals, options);
```
The file `house.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/house.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/house.png)

The filtration times are on the horizontal axis. The _Betti\_k_ number of the stream at filtration time _t_ is the number of intervals in the dimension _k_ plot that intersect a vertical line through _t_. Check that the displayed intervals agree with the filtration times we built into the house stream. At time 0, a connected component and a 1-dimensional hole form. At time 1, a second connected component appears, which joins to the first at time 2. A second 1-dimensional hole forms at time 3, and closes at time 7.

_Remark._ The methods `addElement` and `removeElementIfPresent` do not necessarily enforce the definition of a stream. They allow us to build inconsistent complexes in which some simplex _s_ in _X(t)_ contains a subsimplex _s'_ not in _X(t)_, meaning that _X(t)_ is not a simplicial complex. The method `validateVerbose` returns `1` if our stream is consistent and returns `0` with explanation if not.
```
>> stream.validateVerbose() 
ans = 1 
>> stream.addElement([1, 4, 5], 0); 
>> stream.validateVerbose() 
Filtration index of face [4,5] exceeds that of element [1,4,5] (3 > 0) 
Stream does not contain face [1,5] of element [1,4,5] 
ans = 0
```

_Remark._ If you want to use filtration times that are not integers, then you first need to specify an upper bound on the filtration times in your complex. This is demonstrated below, where the non-integer filtration time is 17.23 and the upper bound is 100.

```
>> stream = api.Plex4.createExplicitSimplexStream(100);
>> stream.addVertex(1, 17.23);
>> stream.finalizeStream();
```



# Point cloud data #

A point cloud is a finite metric space, that is, a finite set of points equipped with a notion of distance. One can create a Euclidean metric space by specifying the coordinates of points in Euclidean space, or one can create an explicit metric space by specifying all pairwise distances between points. In [Streams from point cloud data](Tutorial#Streams_from_point_cloud_data.md) we will learn how to build streams from point cloud data.

## Euclidean metric spaces ##

> The Matlab script corresponding to this section is `pointcloud_examples.m`.

_House example._ Let's give Euclidean coordinates to the points of our house.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/houseCoord.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/houseCoord.png)

You can enter these coordinates manually.
```
>> point_cloud = [-1,0; 1,0; 1,2; -1,2; 0,3]
point_cloud = 

-1     0
 1     0
 1     2
-1     2
 0     3
```

Or, these coordinates are stored as a javaPlex example.
```
>> point_cloud = examples.PointCloudExamples.getHouseExample();
```

We create a metric space using these coordinates. The input to the `EuclideanMetricSpace` method is a matrix whose _i-th row lists the coordinates of the_i_-th point.
```
>> m_space = metric.impl.EuclideanMetricSpace(point_cloud);
```
We can return the coordinates of a specific point. Note the points are indexed starting at 0.
```
>> m_space.getPoint(0)
ans =

  -1
   0
```_

```
>> m_space.getPoint(2)
ans =

   1
   2
```

A metric space can return the distance between any two points.
```
>> m_space.distance(m_space.getPoint(0), m_space.getPoint(2))
ans = 2.8284
```

_Figure 8 example._ We select 1,000 points randomly from a figure eight, that is, the union of unit circles centered at _(0,1)_ and _(0,-1)_.
```
>> point_cloud = examples.PointCloudExamples.getRandomFigure8Points(1000);
```
We plot the points.
```
>> figure
>> scatter(point_cloud(:,1), point_cloud(:,2), '.')
>> axis equal
```

_Torus example._ We select 2,000 points randomly from a torus in _R<sup>3</sup>_ with inner radius 1 and outer radius 2. The first input is the number of points, the second input is the inner radius, and the third input is the outer radius
```
>> point_cloud = examples.PointCloudExamples.getRandomTorusPoints(2000, 1, 2);
```
We plot the points.
```
>> figure
>> scatter3(point_cloud(:,1), point_cloud(:,2), point_cloud(:,3), '.')
>> axis equal
```

_Sphere product example._ We select 1,000 points randomly from the unit torus _S<sup>1</sup> x S<sup>1</sup>_ in _R<sup>4</sup>_. The first input is the number of points, the second input is the dimension of each sphere, and the third input is the number of sphere factors.
```
>> point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(1000, 1, 2);
```
Plotting the third and fourth coordinates of each point shows a circle _S<sup>1</sup>_.
```
>> figure
>> scatter(point_cloud(:,3), point_cloud(:,4), '.')
>> axis equal
```


## Explicit metric spaces ##

We can also create a metric space from a distance matrix using the method `ExplicitMetricSpace`. For a point cloud in Euclidean space, this method is generally less convenient than the command `EuclideanMetricSpace`. However, method `ExplicitMetricSpace` can be used for a point cloud in an arbitrary (perhaps non-Euclidean) metric space.

The Matlab script corresponding to this section is `explicit_metric_space.m`.

_House example._ The matrix `distances` summarizes the metric for our house points above: entry _(i,j)_ is the distance from point _i_ to point _j_.

```
>> distances = [0,2,sqrt(8),2,sqrt(10);
                2,0,2,sqrt(8),sqrt(10);
                sqrt(8),2,0,2,sqrt(2);
                2,sqrt(8),2,0,sqrt(2);
                sqrt(10),sqrt(10),sqrt(2),sqrt(2),0]

distances =

0  2.0000  2.8284  2.0000  3.1623
2.0000  0  2.0000  2.8284  3.1623
2.8284  2.0000  0  2.0000  1.4142
2.0000  2.8482  2.0000  0  1.4142
3.1623  3.1623  1.4142  1.4142  0
```

We create a metric space from this distance matrix.
```
>> m_space = metric.impl.ExplicitMetricSpace(distances);
```
We return the distance between points 0 and 2.
```
>> m_space.distance(m_space.getPoint(0), m_space.getPoint(2)) 
ans = 2.8284 
```

_Exercise_:
One way to produce a torus is to take a square `[0, 1] x [0, 1]` and then identify opposite sides. This is called a flat torus. More explicitly, the quotient space (`[0, 1] x [0, 1]`) / ~, where _(0, y) ~ (1, y)_ for all _y_ in `[0, 1]` and where _(x, 0) ~ (x, 1)_ for all _x_ in `[0, 1]`, is a flat torus. The Euclidean metric on `[0, 1] x [0, 1]` induces a metric on the flat torus. For example, in the induced metric on the flat torus, the distance between _(0, 1/2)_ and _(1, 1/2)_ is zero, since these two points are identified. The distance between _(1/10, 1/2)_ and _(9/10, 1/2)_ is _2/10_, by passing through the point _(0, 1/2) ~ (1, 1/2)_.

Write a Matlab script or function that selects 1,000 random points from the square `[0, 1] x [0, 1]` and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the flat torus. Create an explicit metric space from this distance matrix.

This exercise is continued by an exercise in [Lazy witness streams](Tutorial#Lazy_witness_streams.md).

_Exercise_:
One way to produce a Klein bottle is to take a square `[0, 1] x [0, 1]` and then identify opposite edges, with the left and right sides identified with a twist. This is called a flat Klein bottle. More explicitly, the quotient space (`[0, 1] x [0, 1]`) / ~, where _(0, y) ~ (1, 1 - y)_ for all _y_ in `[0, 1]` and where _(x, 0) ~ (x, 1)_ for all _x_ in `[0, 1]`, is a flat Klein bottle. The Euclidean metric on `[0, 1] x [0, 1]` induces a metric on the flat Klein bottle. For example, in the induced metric on the flat Klein bottle, the distance between _(0, 4/10)_ and _(1, 6/10)_ is zero, since these two points are identified. The distance between _(1/10, 4/10)_ and _(9/10, 6/10)_ is _2/10_, by passing through the point _(0, 4/10) ~ (1, 6/10)_.

Write a Matlab script or function that selects 1,000 random points from the square `[0, 1] x [0, 1]` and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the flat Klein bottle. Create an explicit metric space from this distance matrix.

This exercise is continued by an exercise in [Lazy witness streams](Tutorial#Lazy_witness_streams.md).

_Exercise_:
One way to produce a projective plane is to take the unit sphere _S<sup>2</sup>_ in _R<sup>3</sup>_ and then identify antipodal points. More explicitly, the quotient space (_S<sup>2</sup>_ / (_x_ ~ _-x_) is a projective plane. The Euclidean metric on _S<sup>2</sup>_ induces a metric on the projective plane.

Write a Matlab script or function that selects 1,000 random points from the unit sphere _S<sup>2</sup>_ in _R<sup>3</sup>_ and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the projective plane. Create an explicit metric space from this distance matrix.

This exercise is continued by an exercise in [Lazy witness streams](Tutorial#Lazy_witness_streams.md).




# Streams from point cloud data #


In [Explicit simplex streams](Tutorial#Explicit_simplex_streams.md) we built streams explicitly, or by hand. In this section we construct streams from a point cloud _Z_. We build Vietoris-Rips, witness, and lazy witness streams. See de Silva and Carlsson (2004) for additional information.

The Vietoris-Rips, witness, and lazy witness streams all take three of the same inputs: the maximum dimension _d<sub>max</sub>_, the maximum filtration time _t<sub>max</sub>_, and the number of divisions _N_. These inputs allow the user to limit the size of the constructed stream, for computational efficiency. No simplices above dimension _d<sub>max</sub>_ are included. The persistent homology of the resulting stream can be calculated only up to dimension _d<sub>max</sub> - 1_ (do you see why?). Also, instead of computing complex _X(t)_ for all _t ≥ 0_, we only compute _X(t)_ for _t_ in _{0, t<sub>max</sub>/(N-1), 2t<sub>max</sub>/(N-1), 3t<sub>max</sub>/(N-1), ..., (N-2)t<sub>max</sub>/(N-1), t<sub>max</sub> }_.

The number of divisions _N_ is an optional input. If this input parameter is not specified, then the default value _N = 20_ is used.

When working with a new dataset, don't choose _d<sub>max</sub>_ and _t<sub>max</sub>_ too large initially. First get a feel for how fast the simplicial complexes are growing, and then raise _d<sub>max</sub>_ and _t<sub>max</sub>_ nearer to the computational limits. If you ever choose _d<sub>max</sub>_ or _t<sub>max</sub>_ too large and Matlab seems to be running forever, pressing the `control` and `c` buttons simultaneously may halt the computation.


## Vietoris-Rips streams ##

Let _d(-,-)_ denote the distance between two points in metric space _Z_. A natural stream to build is the Vietoris-Rips stream. The complex _VR(Z,t)_ is defined as follows:

  * the vertex set is _Z_.
  * for vertices _a_ and _b_, edge `[a,b]` is in _VR(Z,t)_ if _d(a,b) ≤ t_.
  * a higher dimensional simplex is in _VR(Z,t)_ if all of its edges are.

Note that _VR(Z,t)_ is a subcomplex of _VR(Z,s)_ whenever _t ≤ s_, so the Vietoris-Rips stream is a filtered simplicial complex. Since a Vietoris-Rips complex is the maximal simplicial complex that can be built on top of its 1-skeleton, it is a _flag complex_.

The Matlab script corresponding to this section is `rips_example.m`.

_House example._ Let's build a Vietoris-Rips stream from the house point cloud in [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md). Note this stream is different than the explicit house stream we built in [Computing persistent homology](Tutorial#Computing_persistent_homology.md).
```
>> max_dimension = 3; 
>> max_filtration_value = 4; 
>> num_divisions = 100; 

>> point_cloud = examples.PointCloudExamples.getHouseExample(); 
>> stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);
```
The order of the inputs is `createVietorisRipsStream`(_Z_,_d<sub>max_</sub>_,_t<sub>max</sub>_,_N_). For a Vietoris-Rips stream, the parameter _t<sub>max</sub>_ is the maximum possible edge length. Since _t<sub>max</sub> = 4_ is greater than the diameter (_√10_) of our point cloud, all edges will eventually form.

Since _d<sub>max</sub> = 3_ we can compute up to second dimensional persistent homology.
```
>> persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);
>> intervals = persistence.computeIntervals(stream);
```
We display the Betti intervals. Parameter `options.max_filtration_value` is the largest filtration time to be displayed. Typically `options.max_filtration_value` is chosen to be `max_filtration_value`. Parameter `options.max_dimension` is the largest persistent homology dimension to be displayed. Typically `options.max_dimension` is chosen to be `max_dimension - 1` because in a stream with simplices computed up to dimension _d<sub>max</sub>_ we can only compute persistent homology up to dimension _d<sub>max</sub> - 1_.
```
>> options.filename = 'ripsHouse';
>> options.max_filtration_value = max_filtration_value;
>> options.max_dimension = max_dimension - 1;
>> plot_barcodes(intervals, options);
```
The file `ripsHouse.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/ripsHouse.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/ripsHouse.png)

Check that these plots are consistent with the Vietoris-Rips definition: edges `[3,5]` and `[4,5]` appear at filtration time _t = √2_; the square appears at _t = 2_; the square closes at _t = √8_.

_Torus example._ Try the following sequence of commands. We start with 400 points from a _20 x 20_ grid on the unit torus _S<sup>1</sup> x S<sup>1</sup>_ in _R<sup>4</sup>_, and add a small amount of noise to each point.

We build the Vietoris-Rips stream.
```
>> max_dimension = 3; 
>> max_filtration_value = 0.9; 
>> num_divisions = 100;
```
Load the file `pointsTorusGrid.mat`. The matrix `pointsTorusGrid` appears in your Matlab workspace.
```
>> load pointsTorusGrid.mat 
>> point_cloud = pointsTorusGrid; 
>> size(point_cloud) 
ans = 400    4          # 400 points in dimension 4

>> stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);  
>> num_simplices = stream.getSize() 
num_simplices = 82479

>> persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);
>> intervals = persistence.computeIntervals(stream);

>> options.filename = 'ripsTorus';
>> options.max_filtration_value = max_filtration_value;
>> options.max_dimension = max_dimension - 1;
>> options.side_by_side = true;
>> plot_barcodes(intervals, options);
```
Setting the parameter `options.side_by_side` equal to `true` makes it such that the Betti barcodes of different dimensions are plotted side by side instead of above and below each other. The file `ripsTorus.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/ripsTorus.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/ripsTorus.png)

The diameter of this torus (before adding noise) is _√8_, so choosing _t<sub>max</sub> = 0.9_ likely will not show all homological activity. However, the torus will be reasonably connected by this time. Note the semi-infinite intervals match the correct numbers _Betti<sub>0</sub> = 1_, _Betti<sub>1</sub> = 2_, _Betti<sub>2</sub> = 1_ for a torus.

```
>> infinite_barcodes = intervals.getInfiniteIntervals(); 
>> betti_numbers_array = infinite_barcodes.getBettiSequence()
betti_numbers_array =

   1
   2
   1
```

This example makes it clear that the computed ``semi-infinite'' intervals do not necessarily persist until _t = ∞_: in a Vietoris-Rips stream, once _t_ is greater than the diameter of the point cloud, the Betti numbers for _VR(Z,t)_ will be _Betti<sub>0</sub> = 1_, _Betti<sub>1</sub> = Betti<sub>2</sub> = ... = 0_. The computed semi-infinite intervals are merely those that persist until _t = t<sub>max</sub>_.

_Remark._ We can build Vietoris-Rips streams not only on top of Euclidean point clouds, but also on top of explicit metric spaces. For example, if `m_space` were an explicit metric space, then we could call a command such as
```
>> stream = api.Plex4.createVietorisRipsStream(m_space, max_dimension, max_filtration_value, num_divisions);  
```

_Exercise_:
Slowly increase the values for _t<sub>max</sub>_, _d<sub>max</sub>_ and note how quickly the size of the Vietoris-Rips stream and the time of computation grow. Either increasing _t<sub>max</sub>_ from 0.9 to 1 or increasing _d<sub>max</sub>_ from 3 to 4 roughly doubles the size of the Vietoris-Rips stream.

_Exercise_:
Find a planar dataset _Z_ and a filtration value _t_ such that _VR(Z,t)_ has nonzero _Betti<sub>2</sub>_. Build a Vietoris-Rips stream to confirm your answer.

_Exercise_:
Find a planar dataset _Z_ and a filtration value _t_ such that _VR(Z,t)_ has nonzero _Betti<sub>6</sub>_. When building a Vietoris-Rips stream to confirm your answer, don't forget to choose _d<sub>max</sub> = 7_.



## Landmark selection ##

For larger datasets, if we include every data point as a vertex, as in the Vietoris-Rips construction, our streams will quickly contain too many simplices for efficient computation. The witness stream and the lazy witness stream address this problem. In building these streams, we select a subset _L_ of _Z_, called landmark points, as the only vertices. All data points in _Z_ help serve as witnesses for the inclusion of higher dimensional simplices.

There are two common methods for selecting landmark points. The first is to choose the landmarks _L_ randomly from point cloud _Z_. The second is a greedy inductive selection process called sequential maxmin. In sequential maxmin, the first landmark is picked randomly from _Z_. Inductively, if _L<sub>i-1</sub>_ is the set of the first _i-1_ landmarks, then let the _i_-th landmark be the point of _Z_ which maximizes the function _z -> d(z, L<sub>i-1</sub>)_, where _d(z, L<sub>i-1</sub>)_ is the distance between the point _z_ and the set _L<sub>i-1</sub>_.

Landmarks chosen using sequential maxmin tend to cover the dataset and to be spread apart from each other. A disadvantage is that outlier points tend to be selected. However, outlier points are less of an issue if one first takes dense core subsets as in Appendix [Dense core subsets](Tutorial#Dense_core_subsets.md). Sequential maxmin landmarks are used by Adams and Carlsson (2009) and Carlsson et al. (2008).

The Matlab script corresponding to this section is `landmark_example.m`.

_Figure 8 example._ We create a point cloud of 1,000 points from a figure eight.
```
>> point_cloud = examples.PointCloudExamples.getRandomFigure8Points(1000);
```

We create both a random landmark selector and a sequential maxmin landmark selector. These selectors will pick 100 landmarks each.
```
>> num_landmark_points = 100; 
>> random_selector = api.Plex4.createRandomSelector(point_cloud, num_landmark_points); 
>> maxmin_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
```

We select 100 random landmarks and 100 landmarks via sequential maxmin. Note we need to increment the indices by 1 since Java uses 0-based arrays.
```
>> random_points = point_cloud(random_selector.getLandmarkPoints() + 1, :); 
>> maxmin_points = point_cloud(maxmin_selector.getLandmarkPoints() + 1, :);
```

We plot the two sets of landmark points to see the difference between random and sequential maxmin landmark selection.

```
>> subplot(1, 2, 1); 
>> scatter(random_points(:,1), random_points(:, 2));
>> title('Random landmark selection'); 
>> subplot(1, 2, 2); 
>> scatter(maxmin_points(:,1), maxmin_points(:, 2)); 
>> title('Maxmin landmark selection');
```

http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/fig8lands.PNG

Sequential maxmin seems to do a better job of choosing landmarks that cover the figure eight and that are spread apart.

_Remark._ We can select landmark points not only from Euclidean point clouds but also from explicit metric spaces. For example, if _m\_space_ is an explicit metric space, then we may select landmarks using a command such as the following.
```
>> maxmin_selector = api.Plex4.createMaxMinSelector(m_space, num_landmark_points); 
```

Given point cloud _Z_ and landmark subset _L_, we define `R` = _max<sub>z</sub>_ in Z{d(z,L)}_. Number_R_reflects how finely the landmarks cover the dataset. We often use it as a guide for selecting the maximum filtration value_t<sub>max</sub>_for a witness or lazy witness stream._

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Suppose we are using sequential maxmin to select a set _L_ of 3 landmarks, and the first (randomly selected) landmark is _(1,0)_. Find by hand the other two landmarks in _L_.

_Exercise_:
Let _Z_ be a point cloud and _L_ a landmark subset. Show that if _L_ is chosen via sequential maxmin, then for any _l<sub>i</sub> ,l<sub>j</sub>_ in _L_, we have _d(l<sub>i</sub> ,l<sub>j</sub>)_ ≥ `R`.



## Witness streams ##

Suppose we are given a point cloud _Z_ and landmark subset _L_. Let _m<sub>k</sub>(z)_ be the distance from a point _z_ in _Z_ to its _(k+1)_-th closest landmark point. The witness stream complex _W(Z,L,t)_ is defined as follows.

  * the vertex set is _L_.
  * for _k>0_ and vertices _l<sub>i</sub>_, the _k_-simplex `[l,,0,, l,,1,, ... l,,k,,]` is in _W(Z,L,t)_ if all of its faces are, and if there exists a witness point _z_ in _Z_ such that _max {d(l<sub>0</sub> ,z), d(l<sub>1</sub> ,z), ..., d(l<sub>k</sub> ,z)} ≤ t+m<sub>k</sub>(z)_.

Note that _W(Z,L,t)_ is a subcomplex of _W(Z,L,s)_ whenever _t ≤ s_. Note that a landmark point can serve as a witness point.

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Let _L = {(1,0),(0,3),(-1,0)}_ be the landmark subset. Find by hand the filtration time for the edge between vertices _(1,0)_ and _(0,3)_. Which point or points witness this edge? What is the filtration time for the lone 2-simplex `[(1,0),(0,3),(-1,0)]`?


The Matlab script corresponding to this section is `witness_example.m`.

_Torus example._ Let's build a witness stream instance for 10,000 random points from the unit torus _S<sup>1</sup> x S<sup>1</sup>_ in _R<sup>4</sup>_, with 50 sequential maxmin landmarks.
```
>> num_points = 10000; 
>> num_landmark_points = 50; 
>> max_dimension = 3; 
>> num_divisions = 100; 

>> point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(num_points, 1, 2);
>> landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
```
The next command returns the landmark covering measure `R` from [Landmark selection](Tutorial#Landmark_selection.md).  Often the value for _t<sub>max</sub>_ is chosen in proportion to `R`.
```
>> R = landmark_selector.getMaxDistanceFromPointsToLandmarks() 
R = 0.7033          # Generally close to 0.7 
>> max_filtration_value = R / 8; 
```

We create the witness stream.
```
>> stream = api.Plex4.createWitnessStream(landmark_selector, max_dimension,  max_filtration_value, num_divisions);  
>> num_simplices = stream.getSize() 
num_simplices = 1164          # Generally close to 1200
```
We plot the Betti intervals.
```
>> persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2); 
>> intervals = persistence.computeIntervals(stream); 

>> options.filename = 'witnessTorus';
>> options.max_filtration_value = max_filtration_value;
>> options.max_dimension = max_dimension - 1;
>> plot_barcodes(intervals, options);
```
The file `witnessTorus.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/witnessTorus.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/witnessTorus.png)

The idea of persistent homology is that long intervals should correspond to real topological features, whereas short intervals are considered to be noise. The plot above shows that for a long range, the torus numbers _Betti<sub>0</sub> = 1_, _Betti<sub>1</sub> = 2_, _Betti<sub>2</sub> = 1_ are obtained. Your plot should contain a similar range.

The witness stream above contains approximately 2,000 simplices, fewer than the approximately 80,000 simplices in the Vietoris-Rips stream from the torus example in [Vietoris-Rips streams](Tutorial#Vietoris-Rips_streams.md). This is despite the fact that we started with a point cloud of 100,000 points in the witness case, but of only 400 points in the Vietoris-Rips case. This supports our belief that the witness stream returns good results at lower computational expense.


## Lazy witness streams ##
A lazy witness stream is similar to a witness stream. However, there is an extra parameter _v_, typically chosen to be 0, 1, or 2, which helps determine how the lazy witness complexes _LW<sub>v</sub>(Z,L,t)_ are constructed. See de Silva and Carlsson (2004) for more information.

Suppose we are given a point cloud _Z_, landmark subset _L_, and natural number parameter _v_. If _v = 0_, let _m(z) = 0_ for all _z_ in _Z_. If _v > 0_, let _m(z)_ be the distance from _z_ to the _v_-th closest landmark point. The lazy witness complex _LW<sub>v</sub>(Z,L,t)_ is defined as follows.

  * the vertex set is _L_.
  * for vertices _a_ and _b_, edge `[a,b]` is in _LW<sub>v</sub>(Z,L,t)_ if there exists a witness _z_ in _Z_ such that _max{d(a,z), d(b,z)} ≤ t+m(z)_.
  * a higher dimensional simplex is in _LW<sub>v</sub>(Z,L,t)_ if all of its edges are.

Note that _LW<sub>v</sub>(Z,L,t)_ is a subcomplex of _LW<sub>v</sub>(Z,L,s)_ whenever _t ≤ s_. The adjective _lazy_ refers to the fact that the lazy witness complex is a flag complex: since the 1-skeleton determines all higher dimensional simplices, less computation is involved.

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Let _L = {(1,0),(0,3),(-1,0)}_ be the landmark subset. Let _v = 1_. Find by hand the filtration time for the edge between vertices _(1,0)_ and _(0,3)_. Which point or points witness this edge? What is the filtration time for the lone 2-simplex `[(1,0),(0,3),(-1,0)]`?

_Exercise_:
Repeat the above exercise with _v = 0_ and with _v = 2_.

_Exercise_:
Check that the 1-skeleton of a witness complex _W(Z,L,t)_ is the same as the 1-skeleton of a lazy witness complex _LW<sub>2</sub>(Z,L,t)_. As a consequence, _LW<sub>2</sub>(Z,L,t)_ is the flag complex of _W(Z,L,t)_.


_2-sphere example._ The Matlab script corresponding to this example is `lazy_witness_example.m`.

We use parameter _v = 1_.
```
>> max_dimension = 3; 
>> num_points = 1000; 
>> num_landmark_points = 50; 
>> nu = 1; 
>> num_divisions = 100; 

>> point_cloud = examples.PointCloudExamples.getRandomSpherePoints(num_points, max_dimension - 1);
>> landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points); 
```
Often _t<sub>max</sub>_ is chosen in proportion to `R`.
```
>> R = landmark_selector.getMaxDistanceFromPointsToLandmarks() 
R = 0.3841          # Generally close to 0.38 
>> max_filtration_value = 2 * R;
>> stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions); 
>> stream.finalizeStream() 
>> num_simplices = stream.getSize() 
num_simplices = 56518          # Generally close to 50000
>> persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);
>> intervals = persistence.computeIntervals(stream);

>> options.filename = 'lazySphere';
>> options.max_filtration_value = max_filtration_value;
>> options.max_dimension = max_dimension - 1;
>> plot_barcodes(intervals, options);
```
The file `lazySphere.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/lazySphere.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/lazySphere.png)

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a flat torus. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a torus.

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a flat Klein bottle. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a Klein bottle, over _Z/2Z_ and _Z/3Z_ coefficients.

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a projective plane. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a projective plane, over _Z/2Z_ and _Z/3Z_ coefficients.

_Exercise_:
Sample points from an embedding of a double torus, that is, a surface of genus two, in _R<sup>3</sup>_. Build a lazy witness stream on this Euclidean metric space. Confirm the barcodes match the homology of a double torus. Choosing suitable parameters will not be easy.




# Example with real data #

We now do an example with real data. The corresponding Matlab script is `image_patch_examples.m`, and it relies on the files `pointsRange.mat` and `dct.m`.

In _On the nonlinear statistics of range image patches_ (Adams and Carlsson 2009), we study a space of range image patches drawn from the Brown database (Lee et al. 2003). A range image is like an optical image, except that each pixel contains a distance instead of a grayscale value. Our space contains high-contrast, normalized, _5 x 5_ pixel patches. We write each _5 x 5_ patch as a vector with 25 coordinates and think of our patches as point cloud data in _R<sup>25</sup>_. We select from this space the 30% densest vectors, based on a density estimator called _p<sub>300</sub>_ (see Appendix [Dense core subsets](Tutorial#Dense_core_subsets.md)). In Adams and Carlsson (2009) this dense core subset is denoted _X<sup>5</sup>(300,30)_, and it contains 15,000 points. In the next example we verify a result from Adams and Carlsson (2009): _X<sup>5</sup>(300,30)_ has the topology of a circle.

Load the file `pointsRange.mat`. The matrix `pointsRange` appears in your Matlab workspace.
```
>> load pointsRange.mat 
>> size(pointsRange)  
ans = 15000     25          # 15000 points in dimension 25
```
Matrix `pointsRange` is in fact _X<sup>5</sup>(300,30)_: each of its rows is a vector in _R<sup>25</sup>_. Display some of the coordinates of `pointsRange`. It is not easy to visualize a circle by looking at these coordinates!

We pick 50 sequential maxmin landmark points, we find the value of `R`, and we build the lazy witness stream with parameter _v = 1_.

```
>> max_dimension = 3; 
>> num_landmark_points = 50; 
>> nu = 1; 
>> num_divisions = 500; 

>> landmark_selector = api.Plex4.createMaxMinSelector(pointsRange, num_landmark_points); 
>> R = landmark_selector.getMaxDistanceFromPointsToLandmarks()  
R = 0.7759          # Generally close to 0.75 
>> max_filtration_value = R / 3; 
>> stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions);  
>> stream.finalizeStream() 
>> num_simplices = stream.getSize() 
num_simplices = 12036          # Generally between 10000 and 25000

>> persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);
>> intervals = persistence.computeIntervals(stream); 

>> options.filename = 'lazyRange';
>> options.max_filtration_value = max_filtration_value;
>> options.max_dimension = max_dimension - 1;
>> plot_barcodes(intervals, options);
```
The file `lazyRange.png` is saved to your current directory.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/lazyRange.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/lazyRange.png)
Betti intervals for the lazy witness complex built from _X<sup>5</sup>(300,30)_

The plots above show that for a long range, the circle Betti numbers _Betti<sub>0</sub> = Betti<sub>1</sub> = 1_ are obtained. Your plot should contain a similar range. This is good evidence that the core subset _X<sup>5</sup>(300,30)_ is well-approximated by a circle.

Our _5 x 5_ normalized patches are currently in the pixel basis: every coordinate corresponds to the range value at one of the 25 pixels. The Discrete Cosine Transform (DCT) basis is a useful basis for our patches (Adams and Carlsson 2009; Lee et al. 2003). We change to this basis in order to plot a projection of the loop evidenced by the barcode above. The method `dct.m` returns the DCT change-of-basis matrix for square patches of size specified by the input parameter.
```
>> pointsRangeDct = pointsRange * dct(5);
```

Two of the DCT basis vectors are horizontal and linear gradients.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/linearGrad.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/linearGrad.png)
We plot the projection of `pointsRangeDct` onto the linear gradient DCT basis vectors.
```
>> scatter(pointsRangeDct(:,1), pointsRangeDct(:,5), '.') 
axis square
```


![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/r5k300c30.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/r5k300c30.png) Projection of _X<sup>5</sup>(300,30)_

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/primaryCircle.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/primaryCircle.png)
Range primary circle


The projection of _X<sup>5</sup>(300,30)_ above shows a circle. It is called the range primary circle and is parameterized as shown above.




# Remarks #


## Matlab functions with javaPlex commands ##

Writing Matlab functions is very useful. In order to include javaPlex commands in an m-file function, include the command `import edu.stanford.math.plex4.*;` as the second line of the function --  that is, as the first line underneath the function header. We include the m-file `eulerCharacteristic.m` as an example Matlab function.

_Euler characteristic example._
The corresponding Matlab script is `euler_characteristic_example.m`, and it relies on the file `eulerCharacteristic.m`.

First we create a 6-dimensional sphere.
```
>> dimension = 6; 
>> stream = api.Plex4.createExplicitSimplexStream(); 
>> stream.addElement(0:(dimension + 1)); 
>> stream.ensureAllFaces(); 
>> stream.removeElementIfPresent(0:(dimension + 1)); 
>> stream.finalizeStream();
```
The function `eulerCharacteristic.m` accepts an explicit simplex stream and its dimension as input. The function demonstrates two different methods for computing the Euler characteristic.
```
>> eulerCharacteristic(stream, dimension) 
The Euler characteristic is 2 = 8 - 28 + 56 - 70 + 56 - 28 + 8, using the alternating sum of cells.
The Euler characteristic is 2 = 1 - 0 + 0 - 0 + 0 - 0 + 1, using the alternating sum of Betti numbers.
```


## Displaying the simplices in a stream ##

It is possible to display the simplices in a stream, along with their filtration times. You can also obtain the vertices of each simplex as a Matlab matrix. For an example of how to do this, please see the file http://code.google.com/p/javaplex/source/browse/trunk/src/matlab/for_distribution/basic_examples/dump_example.m in the folder basic\_examples.


# Dense core subsets #

A core subset of a dataset is a collection of the densest points, such as _X<sup>5</sup>(300,30)_ in [Example with real data](Tutorial#Example_with_real_data.md). Since there are many density estimators, and since we can choose any number of the densest points, a dataset has a variety of core subsets. In this appendix we discuss how to create core subsets.

Real datasets can be very noisy, and outlier points can significantly alter the computed topology. Therefore, instead of trying to approximate the topology of an entire dataset, we often proceed as follows. We create a family of core subsets and identify their topologies. Looking at a variety of core subsets can give a good picture of the entire dataset.

See Carlsson et al. (2008) and de Silva and Carlsson (2004) for an example using multiple core subsets. The dataset contains high-contrast patches from natural images. The authors use three density estimators. As they change from the most global to the most local density estimate, the topologies of the core subsets change from a circle, to three intersecting circles, to a Klein bottle.

One way to estimate the density of a point _z_ in a point cloud _Z_ is as follows. Let _p<sub>k</sub>(z)_ be the distance from _z_ to its _k_-th closest neighbor. Let the density estimate at _z_ be _1/p<sub>k</sub>(z)_. Varying parameter _k_ gives a family of density estimates. Using a small value for _k_ gives a local density estimate, and using a larger value for _k_ gives a more global estimate.

For Euclidean datasets, one can use the m-file `kDensitySlow.m` to produce density estimates _1/p<sub>k</sub>_. The following command is typical.
```
>> densities = kDensitySlow(points, _k_); 
```
Input `points` is an _N x n_ matrix of _N_ points in _R<sup>n</sup>_. Input _k_ is the density estimate parameter. Output `densities` is a vertical vertex of length _N_ containing the density estimate at each point.

M-file `coreSubset.m` builds a core subset. The following command is typical.
```
>> core = coreSubset(points, densities, numPoints); 
```
Inputs `points` and `densities` are as above. Output `core` is a _`numPoints` x n_ matrix representing the `numPoints` densest points.

_Prime numbers example._ The Matlab script corresponding to this example is `core_subsets_example.m`.

The command `primes(3571)` returns a vector listing all prime numbers less than or equal to 3571, which is the 500-th prime. We think of these primes as points on the real line and build the core subset of the 10 densest points with density parameter _k = 1_.
```
>> p = primes(3571)'; 
>> length(p) 
ans = 500 
>> densities1 = kDensitySlow(p, 1); 
>> core1 = coreSubset(p, densities1, 10) 
core1 =

   2
   3
   5
   7
  11
  13
  17
  19
  29
  31 
```

We get a bunch of twin primes, which makes sense since _k = 1_. Let's repeat with _k = 50_.
```
>> densities50 = kDensitySlow(p, 50); 
>> core50 = coreSubset(p, densities50, 10) 
core50 =

  113
  127
  109
  131
  107
  137
  139
  157
  149
  151 
```

With _k = 50_, we expect the densest points to be slightly larger than the 25-th prime, which is 97.

_Note:_ As its name suggests, the m-file `kDensitySlow.m` is not the most efficient way to calculate _p<sub>k</sub>_ for large datasets. There is a faster file `kDensity.m` for this purpose, which uses the kd-tree data structure. It is not included in the tutorial because it requires one to download a kd-tree package for Matlab, available at http://www.mathworks.com/matlabcentral/fileexchange/21512-kd-tree-for-matlab. Please email Henry at `henrya@ima.umn.edu` if you're interested in using `kDensity.m`.




# Exercise solutions #

Several exercise solutions are accompanied by Matlab scripts, which are available in the folder `tutorial_solutions`.

_Exercise_:
Build a simplicial complex homeomorphic to the torus. Compute its Betti numbers. _Hint: You will need at least 7 vertices_ (Hatcher 2002, page 107). _We recommend using a_3x3_grid of 9 vertices._

_Solution_:
See the Matlab script `exercise_1.m` in folder `tutorial_solutions` for a solution. We use 9 vertices, which we think of as a _3x3_ grid numbered as a telephone keypad. We identify opposite sides.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/torus.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/torus.png)

_Exercise_:
Build a simplicial complex homeomorphic to the Klein bottle. Check that it has the same Betti numbers as the torus over _Z/2Z_ coefficients but different Betti numbers over _Z/3Z_ coefficients.

_Solution_:
See the Matlab script `exercise_2.m` for a solution. We use 9 vertices, which we think of as a _3x3_ grid numbered as a telephone keypad. We identify opposite sides, with left and right sides identified with a twist.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/klein.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/klein.png)

_Exercise_:
Build a simplicial complex homeomorphic to the projective plane. Find its Betti numbers over _Z/2Z_ and _Z/3Z_ coefficients.

_Solution_:
See the Matlab script `exercise_3.m` for a solution. We use the minimal triangulation for the projective plane, which contains 6 vertices.

![http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/projPlane.png](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/projPlane.png)

_Exercise_:
Write a Matlab script or function that selects 1,000 random points from the square `[0, 1] x [0, 1]` and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the flat torus. Create an explicit metric space from this distance matrix.

_Solution_:
See the Matlab script `exercise_4.m` and the Matlab function `flatTorusDistanceMatrix.m` for a solution.

_Exercise_:
Write a Matlab script or function that selects 1,000 random points from the square `[0, 1] x [0, 1]` and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the flat Klein bottle. Create an explicit metric space from this distance matrix.

_Solution_:
See the Matlab script `exercise_5.m` and the Matlab function `flatKleinDistanceMatrix.m` for a solution.

_Exercise_:
Write a Matlab script or function that selects 1,000 random points from the unit sphere _S<sup>2</sup>_ in _R<sup>3</sup>_ and then computes the 1,000 x 1,000 distance matrix for these points under the induced metric on the projective plane. Create an explicit metric space from this distance matrix.

_Solution_:
See the Matlab script `exercise_6.m` and the Matlab function `projPlaneDistanceMatrix.m` for a solution.

_Exercise_:
Slowly increase the values for _t<sub>max</sub>_, _d<sub>max</sub>_ and note how quickly the size of the Vietoris-Rips stream and the time of computation grow. Either increasing _t<sub>max</sub>_ from 0.9 to 1 or increasing _d<sub>max</sub>_ from 3 to 4 roughly doubles the size of the Vietoris-Rips stream.

_Solution_:
No solution included.

_Exercise_:
Find a planar dataset _Z_ and a filtration value _t_ such that _VR(Z,t)_ has nonzero _Betti<sub>2</sub>_. Build a Vietoris-Rips stream to confirm your answer.

_Solution_:
See the Matlab script `exercise_8.m` for a solution. Our planar dataset is 6 evenly spaced points on the unit circle. We build a Vietoris-Rips stream which, at the correct filtration value, is an octahedron.

_Exercise_:
Find a planar dataset _Z_ and a filtration value _t_ such that _VR(Z,t)_ has nonzero _Betti<sub>6</sub>_. When building a Vietoris-Rips stream to confirm your answer, don't forget to choose _d<sub>max</sub> = 7_.

_Solution_:
See the Matlab script `exercise_9.m` for a solution. Our planar dataset is 14 evenly spaced points on the unit circle. We build a Vietoris-Rips stream which, at the correct filtration value, is homeomorphic to the 6-sphere. It has 14 vertices because it is obtained by suspending the 0-sphere six times, for a total of _2 + (6 x 2) = 14_ vertices.

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Suppose we are using sequential maxmin to select a set _L_ of 3 landmarks, and the first (randomly selected) landmark is _(1,0)_. Find by hand the other two landmarks in _L_.

_Solution_:
_L_ is the set _{(1, 0), (0, 3), (-1, 0)}_.

_Exercise_:
Let _Z_ be a point cloud and _L_ a landmark subset. Show that if _L_ is chosen via sequential maxmin, then for any _l<sub>i</sub> , l<sub>j</sub>_ in _L_, we have _d(l<sub>i</sub> , l<sub>j</sub>)_ ≥ `R`.

_Solution_:
Without loss of generality, assume that _i < j_ and that landmarks _l<sub>i</sub>_ and _l<sub>j</sub>_ were the _i_-th and _j_-th landmarks selected by the inductive sequential maxmin process. Let _L<sub>j-1</sub>_ be the first _j - 1_ landmarks chosen.

We proceed using a proof by contradiction. Suppose that _d(l<sub>i</sub> , l<sub>j</sub>)_ < `R`. By definition of `R`, there exists a _z_ in _Z_ such that _d(z, L) =_ `R`. Note that
_d(l<sub>j</sub> , L<sub>j-1</sub>)_ ≤ _d(l<sub>j</sub> , l<sub>i</sub>)_ = _d(l<sub>i</sub> , l<sub>j</sub>)_ < `R` = _d(z, L)_ ≤ _d(z, L_<sub>j-1</sub>)_.
This contradicts the fact that landmark_l<sub>j</sub>_was chosen at the_j_-th step of sequential maxmin. Hence, it must be the case that_d(l<sub>i</sub> , l<sub>j</sub>)_≥ `R`._

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Let _L = {(1,0),(0,3),(-1,0)}_ be the landmark subset. Find by hand the filtration time for the edge between vertices _(1,0)_ and _(0,3)_. Which point or points witness this edge? What is the filtration time for the lone 2-simplex `[(1,0),(0,3),(-1,0)]`?

_Solution_:
The edge between _(1, 0)_ and _(0, 3)_ has filtration time zero. Points _(1, 2)_ or _(0, 3)_ witness this edge. The lone 2-simplex has filtration time zero.

_Exercise_:
Let _Z_ be the point cloud from [Euclidean metric spaces](Tutorial#Euclidean_metric_spaces.md), corresponding to the house point cloud. Let _L = {(1,0),(0,3),(-1,0)}_ be the landmark subset. Let _v = 1_. Find by hand the filtration time for the edge between vertices _(1,0)_ and _(0,3)_. Which point or points witness this edge? What is the filtration time for the lone 2-simplex `[(1,0),(0,3),(-1,0)]`?

_Solution_:
The edge between _(1, 0)_ and _(0, 3)_ has filtration time _2-√2_. Point _(1, 2)_ witnesses this edge. The lone 2-simplex has filtration time _√2_, which is when the edge between _(1, 0)_ and _(-1, 0)_ appears.

_Exercise_:
Repeat the above exercise with _v = 0_ and with _v = 2_.

_Solution_:
First we do the case when _v = 0_. The edge between _(1, 0)_ and _(0, 3)_ has filtration time 2. Point _(1, 2)_ witnesses this edge. The lone 2-simplex has filtration time 2.

Next we do the case when _v = 2_. The edge between _(1, 0)_ and _(0, 3)_ has filtration time zero. Points _(1, 2)_ or _(0, 3)_ witness this edge. The lone 2-simplex has filtration time zero.

_Exercise_:
Check that the 1-skeleton of a witness complex _W(Z,L,t)_ is the same as the 1-skeleton of a lazy witness complex _LW<sub>2</sub>(Z,L,t)_. As a consequence, _LW<sub>2</sub>(Z,L,t)_ is the flag complex of _W(Z,L,t)_.

_Solution_:
This follows from the definition of the witness stream and the definition of the lazy witness stream for _v = 2_.

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a flat torus. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a torus.

_Solution_:
See the Matlab script `exercise_16.m` and the Matlab function `flatTorusDistanceMatrix.m` for a solution.

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a flat Klein bottle. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a Klein bottle, over _Z/2Z_ and _Z/3Z_ coefficients.

_Solution_:
See the Matlab script `exercise_17.m` and the Matlab function `flatKleinDistanceMatrix.m` for a solution.

_Exercise_:
In an exercise in [Explicit metric spaces](Tutorial#Explicit_metric_spaces.md) you created an explicit metric space for 1,000 random points on a projective plane. Build a lazy witness stream on this explicit metric space with 50 landmarks chosen via sequential maxmin and with _v = 1_.  Confirm the barcodes match the homology of a projective plane, over _Z/2Z_ and _Z/3Z_ coefficients.

_Solution_:
See the Matlab script `exercise_18.m` and the Matlab function `projPlaneDistanceMatrix.m` for a solution.

_Exercise_:
Sample points from an embedding of a double torus, that is, a surface of genus two, in _R<sup>3</sup>_. Build a lazy witness stream on this Euclidean metric space. Confirm the barcodes match the homology of a double torus. Choosing suitable parameters will not be easy.

_Solution_:
See the Matlab script `exercise_19.m` and the Matlab function `getDoubleTorusPoints.m`. Thanks to Ulrich Bauer for this solution.




# Bibliography #

H. Adams and G. Carlsson. On the nonlinear statistics of range image patches. _SIAM J. Img. Sci._, 2: 110-117, 2009.

M. A. Armstrong. _Basic Topology_. Springer, New York, Berlin, 1983.

G. Carlson, T. Ishkhanov, V. de Silva, and A. Zomorodian. _On the local behavior of spaces of natural images._Int. J. Computer Vision_, 76:1-12, 2008._

V. de Silva and G. Carlsson. Topological estimation using witness complexes. In _Eurographics Symposium on Point-Based Graphics_, June 2004.

H. Edelsbrunner and J. Harer. _Computational Topology: An Introduction_. American Mathematical Society, Providence, 2010.

H. Edelsbrunner, D. Letscher, and A. Zomorodian. Topological persistence and simplification. _Discrete Comput. Geom._, 28:511-533, 2002.

A. Hatcher. _Algebraic Topology_. Cambridge University Press, Cambridge, 2002.

A. B. Lee, K. S. Pedersen, and D. Mumford. The nonlinear statistics of high-contrast patches in natural images. _Int. J. Comput. Vision_, 54:83-103, 2003.

A. Zomorodian and G. Carlsson. Computing persistent homology. _Discrete Comput. Geom_., 33:249-274, 2005.