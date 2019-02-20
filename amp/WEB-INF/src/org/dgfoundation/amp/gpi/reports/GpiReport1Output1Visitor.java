package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.DateCell;
import org.dgfoundation.amp.newreports.IdentifiedReportCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportVisitor;

/**
 * An implementation of ReportVisitor for fetching items used in GPI report 1 output 2
 * 
 * @author Viorel Chihai
 *
 */
public class GpiReport1Output1Visitor implements ReportVisitor {

    private Map<Long, List<GPIOutput1Item>> donorMap = new HashMap<>();
    private List<GPIOutput1Item> gpiItems = new ArrayList<>();
    private List<GPIOutput1Item> donorItems = new ArrayList<>();
    private GPIOutput1Item gpiItem;

    @Override
    public void visit(ReportArea area) {
        if (area.getChildren() == null) {
            if (!gpiItem.isEmpty()) {
                donorItems.add(gpiItem);
            }
        } else {
            if (area.getOwner() != null) {
                if (area.getOwner().columnName.equals(ColumnConstants.DONOR_AGENCY) && !donorItems.isEmpty()) {
                    donorItems.forEach(g -> {
                        g.setDonorAgency(gpiItem.getDonorAgency());
                        g.setDonorId(gpiItem.getDonorId());
                    });
                    donorMap.put(area.getOwner().id, donorItems);
                    donorItems = new ArrayList<>();
                } else if (area.getOwner().columnName.equals(ColumnConstants.PROJECT_TITLE) && !donorMap.isEmpty()) {
                    List<GPIOutput1Item> tmpDonorItems = donorMap.values().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                    
                    tmpDonorItems.forEach(g -> {
                        g.setProjectTitle(gpiItem.getProjectTitle());
                        g.setActivityId(gpiItem.getActivityId());
                        g.setActCommitments(gpiItem.getActCommitments());
                    });
                    gpiItems.addAll(tmpDonorItems);
                    donorMap.clear();
                }
            }
        }
    }

    @Override
    public void visit(Map<ReportOutputColumn, ReportCell> contents) {
        gpiItem = new GPIOutput1Item();
        contents.entrySet().stream().filter(e -> StringUtils.isNotBlank(e.getValue().displayedValue)).forEach(e -> {

            if (e.getKey().originalColumnName.equals(ColumnConstants.PROJECT_TITLE)) {
                gpiItem.setProjectTitle(e.getValue().displayedValue);
                gpiItem.setActivityId((((IdentifiedReportCell) e.getValue()).entityId));
            }
            
            if (e.getKey().originalColumnName.equals(ColumnConstants.DONOR_AGENCY)) {
                gpiItem.setDonorAgency(e.getValue().displayedValue);
                gpiItem.setDonorId((((IdentifiedReportCell) e.getValue()).entityId));
            }
            
            if (e.getKey().originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)) {
                gpiItem.setActCommitments(((AmountCell) e.getValue()).extractValue());
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.IMPLEMENTING_AGENCY)) {
                gpiItem.setImplementingAgencies(((IdentifiedReportCell) e.getValue()).entitiesIdsValues);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.PRIMARY_SECTOR)) {
                gpiItem.setPrimarySectors(((IdentifiedReportCell) e.getValue()).entitiesIdsValues);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.FINANCING_INSTRUMENT)) {
                gpiItem.setFinancingInstruments(((IdentifiedReportCell) e.getValue()).entitiesIdsValues);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q6)) {
                Boolean q6 = e.getValue().displayedValue.equals("1") ? true : false;
                gpiItem.setQ6(q6);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q6_DESCRIPTION)) {
                gpiItem.setQ6Description(e.getValue().displayedValue);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q7)) {
                gpiItem.setQ7(new BigDecimal(Long.parseLong(e.getValue().displayedValue)));
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q8)) {
                gpiItem.setQ8(new BigDecimal(Long.parseLong(e.getValue().displayedValue)));
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q9)) {
                gpiItem.setQ9(new BigDecimal(Long.parseLong(e.getValue().displayedValue)));
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q10)) {
                Boolean q10 = e.getValue().displayedValue.equals("1") ? true : false;
                gpiItem.setQ10(q10);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q10_DESCRIPTION)) {
                gpiItem.setQ10Description(e.getValue().displayedValue);
            }

            if (e.getKey().originalColumnName.equals(ColumnConstants.ACTUAL_APPROVAL_DATE)) {
                DateCell dateCell = (DateCell) e.getValue();
                if (dateCell.entityId != 0) {
                    gpiItem.setApprovalDate(dateCell.entityId);
                    gpiItem.setApprovalDateAsString(dateCell.displayedValue);
                }
            }
        });
    }

    public List<GPIOutput1Item> getGpiItems() {
        return gpiItems;
    }

    public void setGpiItems(List<GPIOutput1Item> gpiItems) {
        this.gpiItems = gpiItems;
    }
}

class GPIOutput1Item {

    private long approvalDate = 0;
    private long activityId;
    private String approvalDateAsString;
    private String projectTitle;
    private String donorAgency;
    private Long donorId;
    private BigDecimal actCommitments = BigDecimal.ZERO;
    private Map<Long, String> financingInstruments = new HashMap<>();
    private Map<Long, String> implementingAgencies = new HashMap<>();
    private Map<Long, String> primarySectors = new HashMap<>();
    private Boolean q6;
    private String q6Description;
    private BigDecimal q7 = BigDecimal.ZERO;
    private BigDecimal q8 = BigDecimal.ZERO;
    private BigDecimal q9 = BigDecimal.ZERO;
    
    private Boolean q10;
    private String q10Description;

    public GPIOutput1Item() {
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public long getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(long approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalDateAsString() {
        return approvalDateAsString;
    }

    public void setApprovalDateAsString(String approvalDateAsString) {
        this.approvalDateAsString = approvalDateAsString;
    }

    public BigDecimal getActCommitments() {
        return actCommitments;
    }

    public void setActCommitments(BigDecimal actCommitments) {
        this.actCommitments = actCommitments;
    }

    public Map<Long, String> getFinancingInstruments() {
        return financingInstruments;
    }

    public void setFinancingInstruments(Map<Long, String> financingInstruments) {
        this.financingInstruments = financingInstruments;
    }

    public Map<Long, String> getImplementingAgencies() {
        return implementingAgencies;
    }

    public void setImplementingAgencies(Map<Long, String> implementingAgencies) {
        this.implementingAgencies = implementingAgencies;
    }

    public Map<Long, String> getPrimarySectors() {
        return primarySectors;
    }

    public void setPrimarySectors(Map<Long, String> primarySectors) {
        this.primarySectors = primarySectors;
    }

    public Boolean getQ6() {
        return q6;
    }

    public void setQ6(Boolean q6) {
        this.q6 = q6;
    }

    public String getQ6Description() {
        return q6Description;
    }

    public void setQ6Description(String q6Description) {
        this.q6Description = q6Description;
    }

    public BigDecimal getQ7() {
        return q7;
    }

    public void setQ7(BigDecimal q7) {
        this.q7 = q7;
    }

    public BigDecimal getQ8() {
        return q8;
    }

    public void setQ8(BigDecimal q8) {
        this.q8 = q8;
    }

    public BigDecimal getQ9() {
        return q9;
    }

    public void setQ9(BigDecimal q9) {
        this.q9 = q9;
    }

    public Boolean getQ10() {
        return q10;
    }

    public void setQ10(Boolean q10) {
        this.q10 = q10;
    }
    
    public String getQ10Description() {
        return q10Description;
    }

    public void setQ10Description(String q10Description) {
        this.q10Description = q10Description;
    }

    public boolean isEmpty() {
        return q7.equals(BigDecimal.ZERO) || q8.equals(BigDecimal.ZERO) || q9.equals(BigDecimal.ZERO) || q6 == null
                || q10 == null;
    }

    @Override
    public String toString() {
        return "GPIIndicatator1Item [approvalDate=" + getApprovalDateAsString() + ", q6=" + q6 + ", q7=" + q7 + ", q8="
                + q8 + ", q9=" + q9 + ", q10=" + q10 + "]";
    }
}
