/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * @author aartimon@dginternational.org
 * @since Mar 31, 2011
 */
public class AmpPIOrganisationSearchModel extends
        AbstractAmpAutoCompleteModel<AmpOrganisation> {

    public AmpPIOrganisationSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
    }

    private static final long serialVersionUID = 8211300754918658812L;
    private Session session;

    @Override
    protected Collection<AmpOrganisation> load() {
        List<AmpOrganisation> ret = null;
        try {
            session = PersistenceManager.getSession();
            Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
            Criteria crit = session.createCriteria(AmpOrganisation.class);
            crit.setCacheable(true);

            Junction junction = Restrictions
                    .conjunction()
                    .add(Restrictions.disjunction()
                            .add(getTextCriterion("name", input))
                            .add(getTextCriterion("acronym", input)))
                    .add(Restrictions
                            .disjunction()
                            .add(Restrictions.like(
                                    "orgGrpId.orgType.orgTypeCode", "BIL"))
                            .add(Restrictions.like(
                                    "orgGrpId.orgType.orgTypeCode", "MUL")));

            crit.add(junction);
            if (maxResults != null)
                crit.setMaxResults(maxResults);

            ret = crit.list();

        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

}
