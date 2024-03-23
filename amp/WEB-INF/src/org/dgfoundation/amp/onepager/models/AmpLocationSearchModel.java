/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

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

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.*;

import static org.digijava.module.aim.util.LocationConstants.MULTI_COUNTRY_ISO_CODE;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel extends
        AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {

    public static final Logger logger = Logger.getLogger(AmpLocationSearchModel.class);
    public static final String PARENT_DELIMITER = "\\] \\[";

    public enum PARAM implements AmpAutoCompleteModelParam {
        LAYER, LEVEL, ALL_SETUP_COUNTRIES
    }

    ;

    public AmpLocationSearchModel(String input, String language,
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
        AmpCategoryValueLocations assignedRegionT = null;
        if (currentMember != null) {
            User user = currentMember.getUser();
            if (user != null) {
                assignedRegionT = user.getRegion();
            }
        }

        if (layerModel == null || layerModel.getObject().size() < 1 || levelModel == null || levelModel.getObject().size() < 1)
            return ret;
        AmpCategoryValue cvLayer = layerModel.getObject().iterator().next();
        AmpCategoryValue cvLevel = levelModel.getObject().iterator().next();

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

                ret = new ArrayList<>();
                ret.addAll(filterCountries);
                return ret;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
        Session dbSession = PersistenceManager.getSession();

        CriteriaBuilder builder = dbSession.getCriteriaBuilder();

        CriteriaQuery<AmpCategoryValueLocations> criteriaQuery = builder.createQuery(AmpCategoryValueLocations.class);

        Root<AmpCategoryValueLocations> root = criteriaQuery.from(AmpCategoryValueLocations.class);

        List<Predicate> predicates = new ArrayList<>();

        if (!CategoryConstants.IMPLEMENTATION_LOCATION_ALL.equalsCategoryValue(cvLayer)) {
            predicates.add(builder.equal(root.get("parentCategoryValue"), cvLayer));
        }


        predicates.add(builder.equal(root.get("deleted"), false));
        //check fro locations with children if location = admin_0

        if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(cvLayer) &&
                DynLocationManagerUtil.getDefaultCountry().getIso().equals(MULTI_COUNTRY_ISO_CODE)) {
            Subquery<AmpCategoryValueLocations> subquery = criteriaQuery.subquery(AmpCategoryValueLocations.class);
            Root<AmpCategoryValueLocations> subRoot = subquery.from(AmpCategoryValueLocations.class);
            subquery.select(subRoot);

            Predicate hasChildren = builder.equal(subRoot.get("parentLocation"), root.get("id"));
            subquery.where(hasChildren);

            predicates.add(builder.exists(subquery));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(builder.asc(root.get("name")));

        TypedQuery<AmpCategoryValueLocations> query = dbSession.createQuery(criteriaQuery);

        if (maxResults != null && maxResults != 0) {
            query.setMaxResults(maxResults);
        }

        ret.addAll(getAmpCategoryValueLocations(query, assignedRegionT));
        return ret;
    }

    private static List<AmpCategoryValueLocations> getAmpCategoryValueLocations(TypedQuery<AmpCategoryValueLocations> query, AmpCategoryValueLocations assignedRegionT) {
        List<AmpCategoryValueLocations> tempList = query.getResultList();

        // Additional filtering based on 'assignedRegion'
        final AmpCategoryValueLocations assignedRegion = assignedRegionT;
        if (assignedRegion != null) {
            tempList.removeIf(location -> {
                AmpCategoryValueLocations locationRegion = DynLocationManagerUtil.getAncestorByLayer(
                        location, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
                return locationRegion == null || !assignedRegion.getId().equals(locationRegion.getId());
            });
        }
        return tempList;
    }

}
