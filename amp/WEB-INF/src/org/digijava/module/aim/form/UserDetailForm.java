package org.digijava.module.aim.form;

import java.util.Collection;

import org.digijava.module.um.form.UserProfileForm;

public class UserDetailForm extends UserProfileForm
{
	
	private Collection teamMemberDetails;
	private String info[];
	private String teamName;
	private String roleName;
	private String address;
	private int count;

	
	
	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return Returns the info.
	 */
	public String[] getInfo() {
		return info;
	}
	/**
	 * @param info The info to set.
	 */
	public void setInfo(String[] info) {
		this.info = info;
	}
	
	/**
	 * @return Returns the teamMemberDetails.
	 */
	public Collection getTeamMemberDetails() {
		return teamMemberDetails;
	}
	/**
	 * @param teamMemberDetails The teamMemberDetails to set.
	 */
	public void setTeamMemberDetails(Collection teamMemberDetails) {
		this.teamMemberDetails = teamMemberDetails;
	}
	
	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return Returns the teamName.
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * @param teamName The teamName to set.
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	/**
	 * @return Returns the teamMemberDetails.
	 */
}