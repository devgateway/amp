package org.dgfoundation.amp.testmodels;

import edu.emory.mathcs.backport.java.util.Arrays;

public abstract class ReportModel implements Comparable<ReportModel>{
	
	protected String name;
	
	protected ReportModel(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ReportModel[] sort(ReportModel[] src)
	{
		ReportModel[] dest = new ReportModel[src.length];
		for(int i = 0; i < src.length; i++)
			dest[i] = src[i];
		Arrays.sort(dest);
		return dest;
	}
	
	@Override
	public int compareTo(ReportModel other)
	{
		int bla = this.getName().compareTo(other.getName()); 
		
		if (bla != 0)
			return bla;
		
		return this.getClass().getName().compareTo(other.getClass().getName());
	}
	
	@Override
	public String toString()
	{
		return String.format("[%s] %s", this.getClass().getSimpleName(), this.getName());
	}
}
