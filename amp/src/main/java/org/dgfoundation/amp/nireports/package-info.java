/**
 * this package contains the NiReports core classes and interfaces. The central class and entrypoint is {@link org.dgfoundation.amp.nireports.NiReportsEngine} <br />
 * Unless client code needs to access internals of the reporting engine after the report is done with 
 * (like the state of the engine, the fetched cells etc), one would normally access the engine via {@link org.dgfoundation.amp.nireports.output.NiReportExecutor}, which allows one to specify an "output builder". <br />
 * An <strong>output builder</strong> is an instance of {@link org.dgfoundation.amp.nireports.output.NiReportOutputBuilder} which allows one to extract and transform whichever part of the output the client code is interested in.
 * No schema-specific (e.g. AMP-specific) code should go in this package., 
 * as it might be extracted for other projects
 */
package org.dgfoundation.amp.nireports;
