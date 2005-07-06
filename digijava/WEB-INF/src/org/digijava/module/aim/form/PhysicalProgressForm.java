package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm ;
import java.util.Collection;

public class PhysicalProgressForm extends ActionForm
{
	private String name ;
	private String title ;
	private String description ;
	private Collection physicalProgress;
	private Collection selectComponent;
	private int flag;
	private Long pid;
	private String display;
	private String dd;
	private String mm;
	private String yyyy;
	private Long ampActivityId;
	private boolean validLogin;
	private String perspective;
	
	public String getDescription() 
	{
		return description;
	}

	public String getDisplay() 
	{
		return display;
	}

	public Long getAmpActivityId() 
	{
		return ampActivityId;
	}

	public Long getPid()
	{
		return pid ;	
	}

	public String getDd() 
	{
		return dd;
	}
	public String getMm() 
	{
		return mm;
	}
	public String getYyyy() 
	{
		return yyyy;
	}

	public String getPerspective() 
	{
		return perspective;
	}

	public void setPid(Long pid) 
	{
		this.pid = pid ;
	}

	public String getName() 
	{
		return name;
	}

	public String getTitle() 
	{
		return title;
	}

	public Collection getPhysicalProgress() 
	{
		return physicalProgress;
	}
	
	public Collection getSelectComponent() 
	{
		return selectComponent;
	}

	public int getFlag() 
	{
		return flag;
	}

	public boolean getValidLogin() 
	{
		return validLogin;
	}

	public void setDescription(String description) 
	{
		this.description = description ;
	}

	public void setDisplay(String display) 
	{
		this.display = display;
	}

	public void setAmpActivityId(Long ampActivityId) 
	{
		this.ampActivityId = ampActivityId;
	}
	
	public void setName(String name) 
	{
		this.name = name ;
	}

	public void setTitle(String title) 
	{
		this.title = title ;
	}

	public void setDd(String dd) 
	{
		this.dd = dd ;
	}

	public void setMm(String mm) 
	{
		this.mm = mm ;
	}

	public void setYyyy(String yyyy) 
	{
		this.yyyy = yyyy ;
	}

	public void setPhysicalProgress(Collection collection) 
	{
		physicalProgress = collection;
	}
	
	public void setSelectComponent(Collection selectComponent) 
	{
		this.selectComponent = selectComponent;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	public void setValidLogin(boolean bool) 
	{
		this.validLogin = bool ;
	}

	public void setPerspective(String perspective) 
	{
		this.perspective = perspective ;
	}

}
