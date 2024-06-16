/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorMapping;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpSectorSearchModel extends
        AbstractAmpAutoCompleteModel<AmpSector> {

    public enum PARAM implements AmpAutoCompleteModelParam {
        SECTOR_SCHEME,
        ACTION,
        CURRENT_SRC_SECTOR_SELECTED,
        SRC_SECTOR_SELECTED, // used in case of sector mapping exists
        DST_SECTOR_SELECTED,
        NEW_CHOICES, DST_SECTORS_FOUND
    }
    Logger logger = LoggerFactory.getLogger(AmpSectorSearchModel.class);

    public AmpSectorSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }


    private static final long serialVersionUID = 8211300754918658832L;
    private Session session;

    @Override
    protected Collection<AmpSector> load() {
        Collection<AmpSector> ret= new ArrayList<>();
        try {
            session = PersistenceManager.getSession();
            session.enableFilter("isDeletedFilter").setParameter("deleted", Boolean.FALSE);

            Integer maxResults = (Integer) getParams().get(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
            AmpSectorScheme scheme = (AmpSectorScheme) getParams().get(PARAM.SECTOR_SCHEME);
            Criteria crit = session.createCriteria(AmpSector.class);
            crit.setCacheable(true);
            Junction junction = Restrictions.conjunction().add(
                    Restrictions.and(
                            Restrictions.eq("ampSecSchemeId", scheme),
                            Restrictions.or(
                                    Restrictions.isNull("deleted"),
                                    Restrictions.eq( "deleted", Boolean.FALSE)
                            )));

            List<AmpSector> srcSectorSelected = (List<AmpSector>) getParams().get(PARAM.SRC_SECTOR_SELECTED);
            Junction junction2 = null;
            if (srcSectorSelected != null && !srcSectorSelected.isEmpty()) {
                List<Long> ids = searchSectorsDstFromMapping(srcSectorSelected);
                if (!ids.isEmpty()) {
                    junction2 = Restrictions.conjunction().add(
                            Restrictions.in("ampSectorId", ids));
                }
            }

            crit.add(junction);
            if (junction2 != null) crit.add(junction2);
            crit.addOrder(Order.asc("name"));
            if (maxResults != null && maxResults != 0) crit.setMaxResults(maxResults);
            List<AmpSector> list = crit.list();


            ret = (Collection<AmpSector>) createTreeView(list);
            getParams().put(PARAM.DST_SECTORS_FOUND, ret);
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } finally {
            session.disableFilter("isDeletedFilter");
        }
        return ret;
    }

    /*
    * Search for sectors that are mapped to the given sector
    * */
    private List<Long> searchSectorsDstFromMapping(List<AmpSector> srcSectors) {
        List<Long> ids = new ArrayList<Long>();
        try {
            session = PersistenceManager.getSession();
            Criteria crit = session.createCriteria(AmpSectorMapping.class);
            crit.setCacheable(true);
            Query query;
            for (AmpSector sector : srcSectors) {
                 query =session.createQuery("FROM " + AmpSectorMapping.class.getName() + " am WHERE am.srcSector.ampSectorId = :sectorId");
                 query.setParameter("sectorId", sector.getAmpSectorId());
                 List<AmpSectorMapping> list = query.list();
                 if (list== null || list.isEmpty()) {
                    return Collections.emptyList();
                 }

            }


            if (srcSectors != null && !srcSectors.isEmpty()) {
                Junction junction = Restrictions.conjunction().add(
                        Restrictions.in("srcSector", srcSectors)
                );

                crit.add(junction);
                List<AmpSectorMapping> list = crit.list();
                if (list != null && !list.isEmpty()) {
                    for (AmpSectorMapping mapping : list) {
                        ids.add(mapping.getDstSector().getAmpSectorId());
                    }
                }
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } finally {
            session.disableFilter("isDeletedFilter");
        }
        return ids;
    }

}
