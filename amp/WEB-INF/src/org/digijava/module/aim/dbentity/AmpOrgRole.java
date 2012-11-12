package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.util.Output;


public class AmpOrgRole implements Serializable, Versionable, Cloneable
{
    private Long ampOrgRoleId;
    private AmpActivityVersion activity;
	private AmpOrganisation organisation;
	private AmpRole role;
	private Double 	percentage;
	private Long budgetCode;
	private String additionalInfo;
	
    public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	/**
     * @return Returns the activity.
     */
    public AmpActivityVersion getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the ampOrgRoleId.
     */
    public Long getAmpOrgRoleId() {
        return ampOrgRoleId;
    }
    /**
     * @param ampOrgRoleId The ampOrgRoleId to set.
     */
    public void setAmpOrgRoleId(Long ampOrgRoleId) {
        this.ampOrgRoleId = ampOrgRoleId;
    }
    /**
     * @return Returns the organisation.
     */
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    /**
     * @param organisation The organisation to set.
     */
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
    /**
     * @return Returns the role.
     */
    public AmpRole getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(AmpRole role) {
        this.role = role;
    }
    
        
	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public Long getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(Long budgetCode) {
		this.budgetCode = budgetCode;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpOrgRole aux = (AmpOrgRole) obj;
		String original = "" + this.organisation.getAmpOrgId() + "-" + this.role.getAmpRoleId();
		String copy = "" + aux.organisation.getAmpOrgId() + "-" + aux.role.getAmpRoleId();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "Organization: " }, new Object[] { this.organisation.getName() }));
		out.getOutputs().add(new Output(null, new String[] { " Role: " }, new Object[] { this.role.getName() }));
		if (this.percentage != null) {
			out.getOutputs().add(new Output(null, new String[] { " Percentage: " }, new Object[] { this.percentage }));
		}
		if (this.additionalInfo != null && this.additionalInfo.trim().length() > 0)
			out.getOutputs().add(new Output(null, new String[] {" Department/Division:"}, new Object[] {this.additionalInfo}));
		if (this.budgetCode != null)
			out.getOutputs().add(new Output(null, new String[] {" Budget Code:"}, new Object[] {this.budgetCode}));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.percentage + "" + this.additionalInfo + "" + this.budgetCode;
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpOrgRole aux = (AmpOrgRole) clone();
		aux.activity = newActivity;
		aux.ampOrgRoleId = null;
		return aux;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}	
