clear all;
import edu.stanford.math.plex4.*;
tic;
load Distances.out;
m_space = metric.impl.ExplicitMetricSpace(Distances);
diam = metric.utility.MetricUtility.estimateDiameter(m_space);
r_max = diam/2
landmark_selector = api.Plex4.createMaxMinSelector(m_space,100);
stream = api.Plex4.createWitnessStream(landmark_selector,3,1.5);
stream.getSize()
persistence = api.Plex4.getDefaultSimplicialAlgorithm(3);
fii = persistence.computeIntervals(stream);
fvi = stream.transform(fii);
api.Plex4.createBarcodePlot(fvi,'witness',1.5);
toc