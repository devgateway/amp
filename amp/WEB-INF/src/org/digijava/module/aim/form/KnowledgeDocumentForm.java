package org.digijava.module.aim.form ;


public class KnowledgeDocumentForm extends MainProjectDetailsForm
{
	private String name ;
	private String description ;
	

	public String getName() 
	{
		return name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description ;
	}

	public void setName(String name) 
	{
		this.name = name ;
	}

}
