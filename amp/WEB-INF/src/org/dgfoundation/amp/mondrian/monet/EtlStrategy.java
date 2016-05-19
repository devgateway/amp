package org.dgfoundation.amp.mondrian.monet;

public interface EtlStrategy {
	public OlapDbConnection getOlapConnection();
	public String getDataSourceString();
}
