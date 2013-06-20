/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.util.*;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {

    public static final Logger logger = Logger.getLogger(AmpLocationSearchModel.class);
    public static final String PARENT_DELIMITER="\\] \\[";
	
	public enum PARAM implements AmpAutoCompleteModelParam {
		LAYER, LEVEL
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

		if (!CategoryManagerUtil.equalsCategoryValue(cvLevel,
				CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL)
				&& CategoryManagerUtil.equalsCategoryValue(cvLayer,
						CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY)) {
			// then we can only return the current default country of the system
			try {
				AmpCategoryValueLocations defCountry = DynLocationManagerUtil
						.getLocationByIso(
								FeaturesUtil.getDefaultCountryIso(),
								CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
				ret = new ArrayList<AmpCategoryValueLocations>();
				ret.add(defCountry);
				return ret;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		
		Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

		Session dbSession = null;
		try {
			dbSession = PersistenceManager.getRequestDBSession();

			Criteria criteria = dbSession
					.createCriteria(AmpCategoryValueLocations.class);
			criteria.setCacheable(true);
			Junction junction = Restrictions.conjunction().add(
					Restrictions.eq("parentCategoryValue", cvLayer));
			if (input.trim().length() > 0) {
				if(isExactMatch()) {
					String[] strings = input.split(PARENT_DELIMITER);
					if(strings.length>1) {
						String locName = strings[strings.length-1].substring(0,strings[strings.length-1].length()-2);
						junction.add( getTextCriterion("name", locName));
						String parentName = null;
						if(strings.length==2)
							parentName = strings[0].substring(1);
						else
							parentName=strings[strings.length-2];
						criteria.createCriteria("parentLocation").add(getTextCriterion("name", parentName));
					} else {
						junction.add(getTextCriterion("name",  input.substring(1, input.length()-2)));
					}


				} else junction.add(getTextCriterion("name", input));
			}
			criteria.add(junction);
			criteria.addOrder(Order.asc("name"));
			if (maxResults != null && maxResults != 0)
				criteria.setMaxResults(maxResults);			
			List<AmpCategoryValueLocations> tempList = criteria.list();

            if (assignedRegion != null){
                Iterator<AmpCategoryValueLocations> it = tempList.iterator();
                while (it.hasNext()) {
                    AmpCategoryValueLocations location = it.next();
                    AmpCategoryValueLocations locationRegion = DynLocationManagerUtil.getAncestorByLayer(location, CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
                    if (!assignedRegion.getId().equals(locationRegion.getId()))
                        it.remove();
                }
            }

			ret.addAll(tempList);
		} catch (DgException e) {
			logger.error(e);
		}
		return ret;
	}

}
