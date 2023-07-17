package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import javax.persistence.*;
@Entity
@Table(name = "AMP_ORG_RECIPIENT")
@Cacheable
public class AmpOrgRecipient implements Serializable,OrgProfileValue {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_RECIPIENT_seq")
    @SequenceGenerator(name = "AMP_ORG_RECIPIENT_seq", sequenceName = "AMP_ORG_RECIPIENT_seq", allocationSize = 1)    @Column(name = "amp_org_rec_id")
    private Long ampOrgRecipientId;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "amp_org_id")
    private AmpOrganisation organization;

    @ManyToOne
    @JoinColumn(name = "parent_org_id")
    private AmpOrganisation parentOrganization;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmpOrgRecipientId() {
        return ampOrgRecipientId;
    }

    public void setAmpOrgRecipientId(Long id) {
        this.ampOrgRecipientId = id;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }
 

    public AmpOrganisation getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(AmpOrganisation parentOrganization) {
        this.parentOrganization = parentOrganization;
    }
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{getOrganization().getName()}),false));
        values.add(new ValueTranslatabePair(Arrays.asList(new String[]{(getDescription()==null)?"":getDescription()}),false));
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        String[] subHeaders = { "Name", "Description" };
        return subHeaders;
    }

}
