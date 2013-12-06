package org.dgfoundation.amp.ar.moldovamigration;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

public class MyHibernateWork implements Work 
{
	protected int result;
	protected String query;
	
	public MyHibernateWork(String query)
	{
		this.query = query;
	}
	
	@Override
	public void execute(Connection conn) throws SQLException
	{
		result = conn.createStatement().executeUpdate(query);					
	}
	
	public int getResult()
	{
		return result;
	}
}
