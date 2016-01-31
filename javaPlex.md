# Welcome to javaPlex #

javaPlex is the newest member of the family of persistent homology computation libraries developed at Stanford. The impetus for developing javaPlex was a perceived lack of flexibility in the predecessor, jPlex. While jPlex is (and remains) a really fast library, the optimizations used made it hard to extend the library to new tasks.

In addition to keeping extensibility as a watchword through the development, javaPlex is released under a BSD license, and we encourage other researchers and hobbyists in persistent homology and computational topology to submit suggestions and patches to extend javaPlex further.

# Where to go from here? #

  * [Download](http://code.google.com/p/javaplex/downloads/list) the software
  * Read the [tutorial](http://javaplex.googlecode.com/svn/trunk/reports/javaplex_tutorial/javaplex_tutorial.pdf) and the [documentation](http://javaplex.googlecode.com/svn/trunk/doc/index.html)
  * Compute your own barcodes
  * Create your own extensions

# Objectives #

There are two main functionalities of javaPlex:

  * the computation of the persistent homology of filtered chain complexes of vector spaces
  * the automated construction of filtered complexes from geometric data

# Capabilities #

  * Computation of persistent homology of filtered complexes over the fields Z/pZ and Q
  * Automated construction of Vietoris-Rips, witness, and lazy-witness complexes from finite metric spaces
  * Construction of CW-complexes and the computation of their persistent homology
  * Computation of zigzag persistence of "incremental" zigzag complexes
  * Homological operations such as tensor and hom of complexes

# Naming #

The current version of the plex family of software is known as javaPlex, whereas the previous version we refer to as jPlex. However, they may also be refered to as versions 4 and 3 of plex, respectively. The table below describes the evolution of the plex family.

| **Version** | **Name** | **Notes** |
|:------------|:---------|:----------|
| Plex 1      | N/A      | Written in Matlab |
| Plex 2      | N/A      | Written in C++ |
| Plex 3      | jPlex    | Written in Java |
| Plex 4      | javaPlex |Written in Java |

# Other Goodies #

Along with javaPlex, there is a tool for viewing geometric simplicial complexes, known as plex-viewer. The google code page for it can be found [here](http://http://code.google.com/p/plex-viewer/). In the downloads, one can find a file named plex-viewer-examples---.tar.gz which contains the jar file as well as various examples.

It has two modes of operation: direct drawing on screen, and pov script file output. The first mode simply draws the complex on the screen using OpenGL. The second mode produces a script file which can then be read by the Pov ray-tracer to produce a ray-traced image of the simplicial complex.

# More information #

JavaPlex is created and maintained by members of the [computational topology research group](http://comptop.stanford.edu) at Stanford University.