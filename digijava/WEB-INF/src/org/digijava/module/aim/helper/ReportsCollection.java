
package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpReports;

public class ReportsCollection {

		  private AmpReports report;
		  private boolean teamView;

		  public ReportsCollection() {
					 report = null;
					 teamView = false;
		  }

		  public void setReport(AmpReports report) {
					 this.report = report;
		  }

		  public void setTeamView(boolean teamView) {
					 this.teamView = teamView;
		  }

		  public AmpReports getReport() { return report; }

		  public boolean getTeamView() { return teamView; }
}
