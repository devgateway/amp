package org.digijava.module.fundingpledges.form;

import lombok.Data;

/**
 * Contact data for a contact in the Pledges Form
 * @author Dolghier Constantin
 *
 */
@Data
public class PledgeFormContact {
	
	private String name;
	private String title;
	private String orgName;
	private String orgId;
	private String ministry;
	private String address;
	private String telephone;
	private String email;
	private String fax;
	private String alternateName;
	private String alternateEmail;
	private String alternateTelephone;
	
	public void reset()
	{
		name = title = orgName = orgId = ministry = address = telephone = email = fax = alternateEmail = alternateName = alternateTelephone = null;
	}
}
