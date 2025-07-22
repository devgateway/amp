/**
 * This package contains the AMP non-trivial dimensions' definitions. The distinction between a trivial and non-trivial dimension is up to personal tastes,
 * the one I used is: <i>a view is considered trivial IFF it has a depth of 1 AND its backing store is a database table</i>. The trivial dimensions are defined inline in {@link org.dgfoundation.amp.nireports.amp.AmpReportsSchema}. <br />
 * All the AMP dimensions are singletons for obvious reasons. <br />
 * One can read more about them <a href='https://wiki.dgfoundation.org/display/AMPDOC/2.+NiReports+Configuration%3A+the+schema#id-2.NiReportsConfiguration:theschema-3.4.3.Dimensions,DimensionUsages,LevelColumns,Coordinates'>here</a> (generic NiReports dimensions)
 * and <a href=''>also here</a> (the AMP database-backed cached dimensions) https://wiki.dgfoundation.org/display/AMPDOC/4.+The+AMP+NiReports+Schema#id-4.TheAMPNiReportsSchema-4.2.3.Thedimensions
 */
package org.dgfoundation.amp.nireports.amp.dimensions;
