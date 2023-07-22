package org.digijava.module.aim.dbentity;   

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.*;

@Entity
@Table(name = "AMP_DESKTOP_TAB_SELECTION")
public class AmpDesktopTabSelection implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_DESKTOP_TAB_SELECTION_seq_generator")
    @SequenceGenerator(name = "AMP_DESKTOP_TAB_SELECTION_seq_generator", sequenceName = "AMP_DESKTOP_TAB_SELECTION_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "array_index", nullable = false)
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_report_id")
    private AmpReports report;

    
    public static Comparator<AmpDesktopTabSelection> tabOrderComparator = new Comparator<AmpDesktopTabSelection>() {

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
  
}
