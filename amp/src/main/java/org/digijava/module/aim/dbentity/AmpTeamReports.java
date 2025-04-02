
/**
 * @author Priyajith
 * 21/10/04
 */

package org.digijava.module.aim.dbentity;


public class AmpTeamReports implements Comparable {

          private Long ampTeamReportsId;
          private AmpTeam team;
          private AmpReports report;
          private boolean teamView;

          public void setAmpTeamReportsId(Long value) {
                     ampTeamReportsId = value;
          }

          public void setTeam(AmpTeam team) {
                     this.team = team;
          }

          public void setReport(AmpReports report) {
                     this.report = report;
          }

          public void setTeamView(boolean teamView) {
                     this.teamView = teamView;
          }

          public Long getAmpTeamReportsId() { return ampTeamReportsId; }
            
          public AmpTeam getTeam() { return team; }

          public AmpReports getReport()  { return report; }

          public boolean getTeamView() { return teamView; }

        public int compareTo(Object o) {
            
             if (!(o instanceof AmpTeamReports)) throw new ClassCastException();
             AmpTeamReports ampRep = (AmpTeamReports) o;
             return (this.report.getName().compareTo(ampRep.report.getName()));
            // TODO Auto-generated method stub
        }
} 
