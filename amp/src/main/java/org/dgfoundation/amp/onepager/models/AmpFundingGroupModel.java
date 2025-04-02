package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;

public class AmpFundingGroupModel implements IModel<Set<AmpOrgRole>> {
    private static final long serialVersionUID = 1L;
    private IModel<Set<AmpFunding>> model;
    private AmpDonorFundingFormSectionFeature parent;

    public AmpFundingGroupModel(IModel<Set<AmpFunding>> model, final AmpDonorFundingFormSectionFeature parent) {
        this.model = model;
        this.parent = parent;
    }

    @Override
    public void detach() {
        model.detach();
    }

    @Override
    public Set<AmpOrgRole> getObject() {
        Set<AmpOrgRole> orgRoles = new LinkedHashSet<AmpOrgRole>();
        Set<AmpFunding> fundingSet = model.getObject();
        if (fundingSet == null)
            return orgRoles;

        List<AmpFunding> auxAmpFunding = new ArrayList<AmpFunding>(fundingSet);
        Collections.sort(auxAmpFunding, new Comparator<AmpFunding>() {

            @Override
            public int compare(AmpFunding o1, AmpFunding o2) {

                if (o1.getOrderNumber() == null ^ o2.getOrderNumber() == null) {
                    return (o1.getOrderNumber() == null) ? -1 : 1;
                }

                if (o1.getOrderNumber() == null && o2.getOrderNumber() == null) {
                    
                    //only old fundings won't have and order. Old Fundings will have an id
                    if (o1.getAmpFundingId()!=null && o2.getAmpFundingId()!=null) {
                        return o1.getAmpFundingId().compareTo(o2.getAmpFundingId());
                    }
                    else {
                        return 0;
                    }
                }
                return o1.getOrderNumber().compareTo(o2.getOrderNumber());

            }

        });

        Iterator<AmpFunding> fundingSetIterator = auxAmpFunding.iterator();
        while (fundingSetIterator.hasNext()) {
            AmpFunding funding = (AmpFunding) fundingSetIterator.next();
            AmpOrgRole ampOrgRole = parent.findAmpOrgRole(funding.getAmpDonorOrgId(), funding.getSourceRole());
            if(ampOrgRole != null) {
                orgRoles.add(ampOrgRole);
            }
        }
        return orgRoles;
    }

    @Override
    public void setObject(Set<AmpOrgRole> object) {
        // do nothing
    }

}
