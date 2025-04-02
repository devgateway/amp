package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.DateCell;
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
public class GpiReport1Output2Visitor implements ReportVisitor {

    private List<GPIOutput2Item> gpiItems = new ArrayList<>();
    private GPIOutput2Item gpiItem;

    @Override
    public void visit(ReportArea area) {
        if (area.getChildren() == null) {
            if (!gpiItem.isEmpty() && gpiItem.getApprovalDate() != 0) {
                gpiItems.add(gpiItem);
            }
        }
    }

    @Override
    public void visit(Map<ReportOutputColumn, ReportCell> contents) {
        gpiItem = new GPIOutput2Item();
        contents.entrySet().stream().filter(e -> StringUtils.isNotBlank(e.getValue().displayedValue)).forEach(e -> {
            if (e.getKey().originalColumnName.equals(ColumnConstants.GPI_1_Q6)) {
                Boolean q6 = e.getValue().displayedValue.equals("1") ? true : false;
                gpiItem.setQ6(q6);
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

            if (e.getKey().originalColumnName.equals(ColumnConstants.ACTUAL_APPROVAL_DATE)) {
                DateCell dateCell = (DateCell) e.getValue();
                if (dateCell.entityId != 0) {
                    gpiItem.setApprovalDate(dateCell.entityId);
                }
            }
        });
    }

    public List<GPIOutput2Item> getGpiItems() {
        return gpiItems;
    }

    public void setGpiItems(List<GPIOutput2Item> gpiItems) {
        this.gpiItems = gpiItems;
    }
}

class GPIOutput2Item {

    private long approvalDate = 0;
    private Boolean q6;
    private BigDecimal q7 = BigDecimal.ZERO;
    private BigDecimal q8 = BigDecimal.ZERO;
    private BigDecimal q9 = BigDecimal.ZERO;
    private Boolean q10;

    public GPIOutput2Item() {
    }

    public long getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(long approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Boolean getQ6() {
        return q6;
    }

    public void setQ6(Boolean q6) {
        this.q6 = q6;
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

    public boolean isEmpty() {
        return q7.equals(BigDecimal.ZERO) || q8.equals(BigDecimal.ZERO) || q9.equals(BigDecimal.ZERO) || q6 == null
                || q10 == null;
    }

    @Override
    public String toString() {
        return "GPIIndicatator1Item [approvalDate=" + getApprovalDate() + ", q6=" + q6 + ", q7=" + q7 + ", q8=" + q8
                + ", q9=" + q9 + ", q10=" + q10 + "]";
    }
}
