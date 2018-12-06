package org.dgfoundation.amp.newreports;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ReportRenderWarning implements Comparable<ReportRenderWarning> {
    
    public final NumberedTypedEntity subject; // entity (e.g. activity) which is in error
    public final String field; // field which is in error. Name = FactTableColumn.columnName
    public final Long fieldId; // id which identifies the cart-entity, e.g. for example "primarySectorId". Might be ALL (e.g. undefined)
    public final ReportRenderWarningType warningType;
    
    public final int hashcode;
    
    public ReportRenderWarning(NumberedTypedEntity subject, String field, Long fieldId, ReportRenderWarningType warningType) {
        this.subject = subject;
        this.field = field;
        this.fieldId = fieldId;
        this.warningType = warningType;
        
        if (this.subject == null)
            throw new NullPointerException("subject cannot be null");
        
        if (this.field == null)
            throw new NullPointerException("field cannot be null");
        
        if (this.warningType == null)
            throw new NullPointerException("warningType cannot be null");
        
        this.hashcode = new HashCodeBuilder().append(subject).append(field).append(fieldId).append(warningType).toHashCode();
    }
    
    @Override public int hashCode() {
        return this.hashcode;
    }
    
    @Override public boolean equals(Object other) {
        ReportRenderWarning oth = (ReportRenderWarning) other;
        return new EqualsBuilder().append(subject, oth.subject).append(field, oth.field).append(fieldId, oth.fieldId).append(warningType, oth.warningType).isEquals();
    }
    
    @Override public int compareTo(ReportRenderWarning oth) {
        return new CompareToBuilder().append(subject, oth.subject).append(field, oth.field).append(fieldId, oth.fieldId).append(warningType, oth.warningType).toComparison();
    }
    
    @Override public String toString() {
        return String.format("%s on (%s, %s) of %s", this.warningType, this.field, this.fieldId, this.subject);
    }
}
