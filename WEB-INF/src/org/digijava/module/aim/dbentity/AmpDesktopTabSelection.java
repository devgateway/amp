package org.digijava.module.aim.dbentity;	

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.ar.impexp.data.Reports;
import org.digijava.module.aim.util.AdvancedReportUtil;

public class AmpDesktopTabSelection {
	private Long id;
	private AmpTeamMember owner;
	private AmpReports report;
	private Integer index;
	
	private static Logger logger = Logger.getLogger( AmpDesktopTabSelection.class );
	
	public static Comparator<AmpDesktopTabSelection> tabOrderComparator	= new Comparator<AmpDesktopTabSelection>() {

		public int compare(AmpDesktopTabSelection o1, AmpDesktopTabSelection o2) {
			return o1.getIndex().compareTo( o2.getIndex() );
		}
	};
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public AmpTeamMember getOwner() {
		return owner;
	}
	public void setOwner(AmpTeamMember owner) {
		this.owner = owner;
	}
	public AmpReports getReport() {
		return report;
	}
	public void setReport(AmpReports report) {
		this.report = report;
	}
	
	@Override
	public int hashCode() {
		return (owner.getAmpTeamMemId() + " || " + report.getAmpReportId()).hashCode();
	}
	
	@Override
	public boolean equals (Object o) {
		if (o == null)
			return false;
		try {
			AmpDesktopTabSelection a	= (AmpDesktopTabSelection) o;
			if ( a.owner == null || a.report == null)
				return false;
			if ( a.owner.getAmpTeamMemId().equals(this.owner.getAmpTeamMemId()) &&  
					a.report.getAmpReportId().equals(this.report.getAmpReportId()) ) {
				return true;
			}
		}
		catch (ClassCastException e) {
			logger.info ( e.getMessage() );
		}
		return false;
	}
  
}
