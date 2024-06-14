/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.dgfoundation.amp.onepager.components.features.tables.AmpSectorsFormTableFeature;
import org.dgfoundation.amp.onepager.interfaces.ISectorTableDeleteListener;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.SectorUtil;

import org.dgfoundation.amp.onepager.interfaces.ISectorTableUpdateListener;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpSectorsFormSectionFeature extends AmpFormSectionFeaturePanel
        implements ISectorTableUpdateListener, ISectorTableDeleteListener {

    private static final long serialVersionUID = -5601918041949098629L;

    private AmpSectorsFormTableFeature primarySectorsTable;
    private AmpSectorsFormTableFeature secondarySectorsTable;
    AmpClassificationConfiguration primaryConf = new AmpClassificationConfiguration();
    AmpClassificationConfiguration secondaryConf = new AmpClassificationConfiguration();

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpSectorsFormSectionFeature(String id, String fmName,final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        RepeatingView view = new RepeatingView("allSectorsTables");
        view.setOutputMarkupId(true);
        add(view);

        List<AmpClassificationConfiguration> allClassificationConfigs = SectorUtil.getAllClassificationConfigsOrdered();


        for (AmpClassificationConfiguration conf : allClassificationConfigs) {
            if (conf.getName().equals(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                primaryConf = conf;
            } else if (conf.getName().equals(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                secondaryConf = conf;
            }
        }

        primarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                primaryConf.getName() + " Sectors", am, primaryConf);
        primarySectorsTable.setUpdateListener(this);
        primarySectorsTable.setDeleteListener(this);

        secondarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                secondaryConf.getName() + " Sectors", am, secondaryConf);


        view.add(primarySectorsTable);
        view.add(secondarySectorsTable);
    }


    /**
     * Updates the user interface based on the given data. This method is called when the trigger is fired.
     * Used to filter secondary sectors based on the primary sector selection when exist mapping between them.
     *
     * @param data the list of AmpSector objects to update the UI with
     */
    @Override
    public void onUpdate(List<AmpSector> data) {
        if (secondarySectorsTable != null) {
            // Update user interface
             AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
             if (target != null) {
                 secondarySectorsTable.updateBasedOnData(data);
                 target.add(secondarySectorsTable.getSearchSectors());
             }
             if (this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.ACTION)=="add" || this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.ACTION)=="add") {
                 populateSecondarySectorsFor1Choice( target, secondaryConf);
             }

        }




    }

    private void populateSecondarySectorsFor1Choice(AjaxRequestTarget target, AmpClassificationConfiguration sectorClassification)
    {

        AmpSector selectedSector =(AmpSector) this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.CURRENT_SRC_SECTOR_SELECTED);
        logger.info("Selected sector: " + selectedSector);
        try {
            if (selectedSector!=null) {
                List<AmpSector> choices = (List<AmpSector>)load();
                List<AmpSector> sectorChoices = searchSectorsDstFromMapping(selectedSector);


                logger.info("Choices found: " + choices);
                logger.info("Sector Choices found: " + sectorChoices);
                if (sectorChoices.size() == 1) {
                    for (AmpSector secondarySector : choices) {
                        AmpActivitySector newSector = new AmpActivitySector();
                        newSector.setSectorId(secondarySector);
                        newSector.setActivityId(secondarySectorsTable.getSetModel().getObject().iterator().next().getActivityId()); // Assuming activityId is the same
                        newSector.setClassificationConfig(sectorClassification);
                        this.secondarySectorsTable.getSearchSectors().getModelParams().computeIfAbsent(AmpSectorSearchModel.PARAM.DST_SECTOR_SELECTED,
                                k -> new ArrayList<>());
                        ((List<AmpActivitySector>)secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.DST_SECTOR_SELECTED)).add(newSector);

                        secondarySectorsTable.getSetModel().getObject().add(newSector);
                    }
                    target.add(secondarySectorsTable.getList().getParent());

                }
                List<AmpSector> srcSectorSelected = (List<AmpSector>) this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SRC_SECTOR_SELECTED);

//                secondarySectorsTable.updateBasedOnData(srcSectorSelected);

//                secondarySectorsTable.triggerUpdateEvent(secondarySectorsTable.getSetModel().getObject(), sectorClassification);
                secondarySectorsTable.getSearchSectors().getModelParams().put(AmpSectorSearchModel.PARAM.NEW_CHOICES, choices);

                target.add(secondarySectorsTable.getSearchSectors());


            }
        }catch (Exception e) {
            logger.error("Error",e);
        }




    }


    protected void addToRootTree(Collection<AmpAutoCompleteDisplayable> tree, AmpAutoCompleteDisplayable obj,
                                 boolean searchHit) {
        if (obj.getParent() == null)
            tree.add(obj);
        else {
            if (searchHit)
                obj.getVisibleSiblings().addAll(obj.getNonDeletedChildren());
            obj.getParent().getVisibleSiblings().add(obj);
            addToRootTree(tree, obj.getParent(), false);
        }
    }

    protected void addToRootList(Collection<AmpAutoCompleteDisplayable> list, AmpAutoCompleteDisplayable obj) {
        list.add(obj);
        Collection<? extends AmpAutoCompleteDisplayable> children = obj.getVisibleSiblings();
        for (AmpAutoCompleteDisplayable ampSector : children) {
            addToRootList(list, ampSector);
        }
    }

    protected Collection<? extends AmpAutoCompleteDisplayable> createTreeView(Collection l) {
        Boolean b = (Boolean) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AbstractAmpAutoCompleteModel.PARAM.EXACT_MATCH);
        if (b != null && b)
            return l;
        Collection<AmpAutoCompleteDisplayable> ret = new ArrayList<AmpAutoCompleteDisplayable>();

        Set<AmpAutoCompleteDisplayable> root = new TreeSet<AmpAutoCompleteDisplayable>(new AmpAutoCompleteDisplayable.AmpAutoCompleteComparator());
        for (Object o : l)
            addToRootTree(root, (AmpAutoCompleteDisplayable) o, true);
        for (Object o : root) {
            addToRootList(ret, (AmpAutoCompleteDisplayable) o);
        }

        return ret;
    }





    private List<AmpSector> searchSectorsDstFromMapping(AmpSector srcSector) {
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSector> dstSectorIds = new ArrayList<>();
        String hql = "SELECT sm.dstSector FROM AmpSectorMapping sm WHERE sm.srcSector.ampSectorId = :srcSectorId";

        try {
            Query query = session.createQuery(hql);
            query.setParameter("srcSectorId", srcSector.getAmpSectorId(), LongType.INSTANCE);
            dstSectorIds = query.list();
            if (dstSectorIds.isEmpty())
            {
                AmpSectorScheme scheme = (AmpSectorScheme) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SECTOR_SCHEME);

                session = PersistenceManager.getSession();
                String sql = "select pi from "
                        + AmpSector.class.getName()
                        + " pi where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null and (pi.deleted is null or pi.deleted = false)";
                query = session.createQuery(sql);
                query.setParameter("schemeId", scheme.getAmpSecSchemeId(), LongType.INSTANCE);
                dstSectorIds = query.list();
            }

        } catch (HibernateException e) {
            // Handle the exception
            e.printStackTrace();
        }

        return dstSectorIds;
    }


    protected Collection<AmpSector> load() {
        Collection<AmpSector> ret= new ArrayList<>();
        Session session = PersistenceManager.getSession();
        try {
            session.enableFilter("isDeletedFilter").setParameter("deleted", Boolean.FALSE);

            Integer maxResults = (Integer) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
            AmpSectorScheme scheme = (AmpSectorScheme) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SECTOR_SCHEME);
            Criteria crit = session.createCriteria(AmpSector.class);
            crit.setCacheable(true);
            Junction junction = Restrictions.conjunction().add(
                    Restrictions.and(
                            Restrictions.eq("ampSecSchemeId", scheme),
                            Restrictions.or(
                                    Restrictions.isNull("deleted"),
                                    Restrictions.eq( "deleted", Boolean.FALSE)
                            )));

            List<AmpSector> srcSectorSelected = (List<AmpSector>) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SRC_SECTOR_SELECTED);
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
            this.secondarySectorsTable.getSearchSectors().getModelParams().put(AmpSectorSearchModel.PARAM.DST_SECTORS_FOUND, ret);
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
        Session session = PersistenceManager.getRequestDBSession();
        try {
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

    /**
     * Updates the user interface when a deletion event is triggered.
     * This method is called when the onDelete method is invoked in the containing class.
     *
     * @param data the AmpSector object to be deleted
     */
    @Override
    public void onDelete(AmpSector data) {
        if (secondarySectorsTable != null) {
            // Update user interface
            AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
            if (target != null) {
                secondarySectorsTable.deleteBasedOnData(data);
                secondarySectorsTable.refreshTable(target);
                target.add(secondarySectorsTable);
            }
        }
    }
}
