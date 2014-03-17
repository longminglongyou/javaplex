% This script demonstrates how to obtain the dimension, vertices, and filtration time of each simplex in a filtered simplicial complex.

clc; clear; close all;

max_dimension = 2;
max_filtration_value = 4;
num_divisions = 100;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample();

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);

%% Dump Example:

iterator = stream.iterator();

while (iterator.hasNext())
  % the next line will print the current simplex
  simplex = iterator.next()
  % here you can do whatever is needed with the simplex
  dimension = simplex.getDimension
  vertices = simplex.getVertices()
  filtration_value = stream.getFiltrationValue(simplex)
end

