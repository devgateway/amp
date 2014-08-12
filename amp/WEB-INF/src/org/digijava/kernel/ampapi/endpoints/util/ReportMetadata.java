package org.digijava.kernel.ampapi.endpoints.util;

import org.dgfoundation.amp.newreports.ReportSpecificationImpl;

public class ReportMetadata {
	private String name = "";
	private String connection = "";
	private String cube = "";
	private String catalog = "";
	private String schema = "";
	private String queryName = "";
	private ReportSpecificationImpl reportSpec;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	public String getCube() {
		return cube;
	}
	public void setCube(String cube) {
		this.cube = cube;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public ReportSpecificationImpl getReportSpec() {
		return reportSpec;
	}
	public void setReportSpec(ReportSpecificationImpl report) {
		this.reportSpec = report;
	}
	
}