/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel extends
        AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {

    public static final Logger logger = Logger.getLogger(AmpLocationSearchModel.class);
    public static final String PARENT_DELIMITER="\\] \\[";
    
    public enum PARAM implements AmpAutoCompleteModelParam {
        LAYER, LEVEL, ALL_SETUP_COUNTRIES
    };

    public AmpLocationSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
    }

    private static final long serialVersionUID = -1967371789152747599L;

    @Override
    protected Collection<AmpCategoryValueLocations> load() {
        Collection<AmpCategoryValueLocations> ret = new TreeSet<AmpCategoryValueLocations>(new AmpAutoCompleteDisplayable.AmpAutoCompleteComparator());
        IModel<Set<AmpCategoryValue>> layerModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LAYER);
        IModel<Set<AmpCategoryValue>> levelModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LEVEL);
        Boolean allSetupCountries = (Boolean) getParam(PARAM.ALL_SETUP_COUNTRIES);
        AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
        AmpTeamMember currentMember = wicketSession.getAmpCurrentMember();
        AmpCategoryValueLocations assignedRegion = null;
        if (currentMember != null){
            User user = currentMember.getUser();
            if (user != null){
                assignedRegion = user.getRegion();
            }
        }

        if (layerModel == null || layerModel.getObject().size() < 1 || levelModel==null || levelModel.getObject().size()<1)
            return ret;
        AmpCategoryValue cvLayer = layerModel.getObject().iterator().next();
        AmpCategoryValue cvLevel= levelModel.getObject().iterator().next();

        if (!CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(cvLevel)
                && CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(cvLayer)) {
            // then we can only return the current default country of the system
            try {
                Set<AmpCategoryValueLocations> filterCountries = new HashSet<AmpCategoryValueLocations>();
                if (!allSetupCountries) {
                    filterCountries.add(DynLocationManagerUtil.getDefaultCountry());
                } else {//AMP-16857
                    Set<AmpCategoryValueLocations> countries = DynLocationManagerUtil.getLocationsByLayer(cvLayer);
                    for (AmpCategoryValueLocations loc : countries) {
                        if (loc.getChildLocations() != null && !loc.getChildLocations().isEmpty()) {
                            filterCountries.add(loc);
                        }
                    }
                }

                ret = new ArrayList<AmpCategoryValueLocations>();
                ret.addAll(filterCountries);
                return ret;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        
        Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

        Session dbSession = PersistenceManager.getSession();

        Criteria criteria = dbSession.createCriteria(AmpCategoryValueLocations.class);
        criteria.setCacheable(true);

        if (!CategoryConstants.IMPLEMENTATION_LOCATION_ALL.equalsCategoryValue(cvLayer)) {
            criteria.add(Restrictions.eq("parentCategoryValue", cvLayer));
        }
        
        criteria.add(Restrictions.eqOrIsNull("deleted", false));

        criteria.addOrder(Order.asc("name"));
        if (maxResults != null && maxResults != 0)
            criteria.setMaxResults(maxResults);         
        List<AmpCategoryValueLocations> tempList = criteria.list();

        if (assignedRegion != null) {
            Iterator<AmpCategoryValueLocations> it = tempList.iterator();
            while (it.hasNext()) {
                AmpCategoryValueLocations location = it.next();
                AmpCategoryValueLocations locationRegion = DynLocationManagerUtil.getAncestorByLayer(
                        location, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
                if (locationRegion == null || !assignedRegion.getId().equals(locationRegion.getId()))
                    it.remove();
            }
        }
        ret.addAll(tempList);
        return ret;
    }

}
