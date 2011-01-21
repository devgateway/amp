package org.digijava.module.aim.form ;


public class PhysicalProgressDescriptionForm extends MainProjectDetailsForm
{
	private String pid;
	private String title ;
	private String description ;
	

	public String getTitle() 
	{
		return title;
	}

	public String getPid() 
	{
		return pid;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description ;
	}

	public void setTitle(String title) 
	{
		this.title = title ;
	}

	public void setPid(String n) 
	{
		this.pid = n ;
	}

}
