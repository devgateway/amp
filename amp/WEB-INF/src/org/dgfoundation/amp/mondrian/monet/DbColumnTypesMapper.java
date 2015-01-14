package org.dgfoundation.amp.mondrian.monet;

/**
 * a class which maps a Database table type (java.sql.Types) to a OLAP column type name string
 * @author simple
 *
 */
public interface DbColumnTypesMapper {
	public String mapSqlTypeToName(int rsType, int maxWidth);
}
