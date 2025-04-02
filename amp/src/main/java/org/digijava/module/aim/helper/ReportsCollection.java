
package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpReports;

public class ReportsCollection implements Comparable {

          private AmpReports report;
          private boolean teamView;

          public ReportsCollection() {
                     report = null;
                     teamView = false;
          }
          
          public ReportsCollection(AmpReports report,boolean teamView) 
          {
              this.report = report;
              this.teamView =teamView;
          }


          public void setReport(AmpReports report) {
                     this.report = report;
          }

          public void setTeamView(boolean teamView) {
                     this.teamView = teamView;
          }

          public AmpReports getReport() { return report; }

          public boolean getTeamView() { return teamView; }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object obj) {
            ReportsCollection rc = (ReportsCollection) obj;
            return report.compareTo(rc.getReport());
        }

}
