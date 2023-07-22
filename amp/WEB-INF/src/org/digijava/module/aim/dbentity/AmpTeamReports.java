
/**
 * @author Priyajith
 * 21/10/04
 */

package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_TEAM_REPORTS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpTeamReports implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_TEAM_REPORTS_seq")
    @SequenceGenerator(name = "AMP_TEAM_REPORTS_seq", sequenceName = "AMP_TEAM_REPORTS_seq", allocationSize = 1)
    @Column(name = "amp_team_reports_id")
    private Long ampTeamReportsId;

    @Column(name = "team_view")
    private Boolean teamView;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team")
    private AmpTeam team;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "report")
    private AmpReports report;




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
