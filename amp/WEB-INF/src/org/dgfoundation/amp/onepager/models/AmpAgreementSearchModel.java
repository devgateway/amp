/**
 * Copyright (c) 2012 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * @author aartimon@developmentgateway.org
 * @since Nov 13, 2012
 */
public class AmpAgreementSearchModel extends
        AbstractAmpAutoCompleteModel<AmpAgreement> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AmpAgreementSearchModel.class);
    private static final AmpAgreement NEW_AGREEMENT = new AmpAgreement(-1L, TranslatorUtil.getTranslation("Add new agreement"));
    
    public AmpAgreementSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
    }

    @Override
    protected Collection<AmpAgreement> load() {
        Collection<AmpAgreement> ret = null;
        try {
            ret = new ArrayList<>();
            Session session = PersistenceManager.getRequestDBSession();
            Integer maxResults = (Integer) getParams().get(
                    AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

            Criteria crit = session.createCriteria(AmpAgreement.class);
            crit.setCacheable(true);
            Junction junction = Restrictions.disjunction().add(
                    getTextCriterion("title", input));
            junction.add(getTextCriterion("code", input));
            crit.add(junction);
            crit.addOrder(Order.asc("code"));
            if (maxResults != null && maxResults != 0)
                crit.setMaxResults(maxResults);
            List<AmpAgreement> list = crit.list();
            
            org.apache.wicket.Session wSession = org.apache.wicket.Session.get();
            HashSet<AmpAgreement> agItems = wSession.getMetaData(OnePagerConst.AGREEMENT_ITEMS);
            if (agItems == null){
                agItems = new HashSet<AmpAgreement>();
                wSession.setMetaData(OnePagerConst.AGREEMENT_ITEMS, agItems);
            }

            //Remove from list the agreements that have been edited and are now in the NEW_AGREEMENT metadata set
            Iterator<AmpAgreement> listIt = list.iterator();
            while (listIt.hasNext()) {
                AmpAgreement tmpAg = listIt.next();
                for (AmpAgreement tmpAg2 : agItems) {
                    if (tmpAg.getId().equals(tmpAg2.getId())) {
                        listIt.remove();
                        break;
                    }
                }
            }

            if (!isExactMatch()){
                list.addAll(agItems);
                list.add(0,NEW_AGREEMENT);
            }
            else{
                Iterator<AmpAgreement> it = agItems.iterator();
                while (it.hasNext()) {
                    AmpAgreement tmpAgg = (AmpAgreement) it.next();
                    if (tmpAgg.getTitle().equals(input))
                        list.add(tmpAgg);
                }
                
                if (NEW_AGREEMENT.getTitle().equals(input))
                    list.add(NEW_AGREEMENT);
            }
            ret = list;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

}
