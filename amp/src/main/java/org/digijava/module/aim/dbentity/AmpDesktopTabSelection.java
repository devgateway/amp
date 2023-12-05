package org.digijava.module.aim.dbentity;   

import java.io.Serializable;
import java.util.Comparator;

public class AmpDesktopTabSelection implements Serializable{
    private Long id;
    private AmpTeamMember owner;
    private AmpReports report;
    private Integer index;
    
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
