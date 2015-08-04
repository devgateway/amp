package org.dgfoundation.amp.reports.mondrian;

/**
 * statistics regarding time it took to generate a report - for production diagnostic reasons
 * @author Dolghier Constantin
 *
 */
public class ReportGenerationStats {
	public long lock_wait_time;
	public long mdx_time;
	public long total_time;	
	public String mdx_query;
	public int width;
	public int height;
	public long postproc_time;
	boolean crashed;
	/**
	 * number of reports which have been run parallelly
	 */
	public int reportDepth;
}
