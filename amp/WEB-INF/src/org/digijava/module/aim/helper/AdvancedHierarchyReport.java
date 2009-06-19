package org.digijava.module.aim.helper ;
import java.util.Collection;

public class AdvancedHierarchyReport
{
	private Long id;
	private String name ;
	private String label;
	private Collection levels ;
	private Collection project ;
	private Collection activities ;
	private Collection fundSubTotal;
	
	public Collection getActivities() 
	{
		return activities;
	}

	public void setActivities(Collection c) 
	{
		activities = c;
	}

	public Collection getLevels() 
	{
		return levels;
	}

	public void setLevels(Collection c) 
	{
		levels = c;
	}

	public Collection getProject() 
	{
		return project;
	}

	public void setProject(Collection c) 
	{
		project = c;
	}

	public Long getId() 
	{
		return id;
	}

	public void setId(Long l) 
	{
		id = l;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setName(String string) 
	{
		name = string;
	}

	public String getLabel() 
	{
		return label;
	}
	
	public void setLabel(String string) 
	{
		label = string;
	}

	public Collection getFundSubTotal() 
	{
		return fundSubTotal;
	}

	public void setFundSubTotal(Collection c) 
	{
		fundSubTotal = c;
	}

	

}