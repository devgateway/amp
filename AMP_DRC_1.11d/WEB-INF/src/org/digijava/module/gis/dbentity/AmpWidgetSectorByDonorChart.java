package org.digijava.module.gis.dbentity;

import java.util.Date;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.widget.dbentity.AmpWidget;

/**
 * Widget of pie chart - Sector by donors. 
 * @author Irakli Kobiashvili
 *
 */
public class AmpWidgetSectorByDonorChart extends AmpWidget {

	private static final long serialVersionUID = 1L;
	
	private AmpOrganisation donorOrg;
	private Date date;
	
	public AmpOrganisation getDonorOrg() {
		return donorOrg;
	}
	public void setDonorOrg(AmpOrganisation donorOrg) {
		this.donorOrg = donorOrg;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
