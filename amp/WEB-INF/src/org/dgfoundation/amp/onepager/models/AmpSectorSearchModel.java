/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorMapping;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpSectorSearchModel extends
        AbstractAmpAutoCompleteModel<AmpSector> {

    public enum PARAM implements AmpAutoCompleteModelParam {
        SECTOR_SCHEME,
        SRC_SECTOR_SELECTED // used in case of sector mapping exists
    };

    public AmpSectorSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 8211300754918658832L;
    private Session session;

    @Override
    protected Collection<AmpSector> load() {
        Collection<AmpSector> ret = null;
        try {
            ret = new ArrayList<AmpSector>();
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
