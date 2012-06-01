package org.digijava.module.aim.form ;


public class ChannelOverviewObjectiveForm extends MainProjectDetailsForm
{
	private String name ;
	private String objective ;
	private boolean validLogin;

	public String getName() 
	{
		return name;
	}

	public String getObjective() 
	{
		return objective;
	}

	public boolean getValidLogin() 
	{
		return validLogin;
	}

	public void setObjective(String objective) 
	{
		this.objective = objective ;
	}

	public void setName(String name) 
	{
		this.name = name ;
	}

	public void setValidLogin(boolean bool) 
	{
		this.validLogin = bool ;
	}

}
