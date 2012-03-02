package org.digijava.module.aim.util;

/*
 * @author Govind G Dalwani
 */
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpComponentsIndicators;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ComponentsUtil {

    private static Logger logger = Logger.getLogger(ComponentsUtil.class);

    public static Collection<AmpComponent> getAmpComponents() {
        logger.debug(" starting to get all the components....");
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select distinct co from " + AmpComponent.class.getName() + " co ";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Components  from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    

    public static Collection<AmpComponent> getAmpComponentsByType(Long id) {
        logger.debug(" starting to get all the components....");
        Collection col = null;	
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select distinct co from " + AmpComponent.class.getName() + " co where co.type.type_id=:id";
            qry = session.createQuery(queryString);
            qry.setLong("id", id);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Components  from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

  
    @SuppressWarnings("unchecked")
	public static Collection<AmpComponentType> getAmpComponentTypes() {
        Collection<AmpComponentType> col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponentType.class.getName() + " co ";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get AmpComponentType  from database " + ex.getMessage());
        }
        return col;
    }
    
	public static AmpComponentType getAmpComponentTypeByName(String name) {
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponentType.class.getName() + " co where (TRIM(co.name)=:name)";
            qry = session.createQuery(queryString);
            qry.setParameter("name",name.trim(),Hibernate.STRING);
            col = qry.list();
            if (col.size() > 0){
            	return new ArrayList<AmpComponentType>(col).get(0);
            }
        } catch (Exception ex) {
            logger.error("Unable to get AmpComponentType  from database " + ex.getMessage());
        }
        return null;
    }
    public static AmpComponentType getComponentTypeById(Long id) {
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponentType.class.getName() + " co where co.type_id=:id";
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);

            col = qry.list();
            if (col.size() > 0){
            	return new ArrayList<AmpComponentType>(col).get(0);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return null;
    }
    
    public static Collection getComponentForEditing(Long id) {
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponent.class.getName() + " co where co.ampComponentId=:id";
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);

            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }
    
    

    /*
     * update component details
     */
    public static void updateComponentType(AmpComponentType type) {
        DbUtil.update(type);
    }

    /*
     * add a new Component
     */
    public static void addNewComponentType(AmpComponentType type) {
        DbUtil.add(type);

    }
    
    /*
     * delete a Component
     */
    public static void deleteComponentType(Long compId) {

		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();

			// Now delete AmpComponent
			AmpComponentType type = (AmpComponentType) session.load(AmpComponentType.class, compId);
			session.delete(type);
			//tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteComponentType() : " + e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
				}
			}
			e.printStackTrace();
		}
	}
    
    
    /*
	 * update component details
	 */
    public static void updateComponents(AmpComponent ampComp) {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpComponent oldCompo=(AmpComponent) session.load(AmpComponent.class,ampComp.getAmpComponentId());

			oldCompo.setCode(ampComp.getCode());
			oldCompo.setDescription(ampComp.getDescription());
			oldCompo.setTitle(ampComp.getTitle());
			oldCompo.setType(ampComp.getType());
			session.update(oldCompo);
			//tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteComponentType() : " + e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
				}
			}
			e.printStackTrace();
		}
    }

    /*
	 * add a new Component
	 */
    public static void addNewComponent(AmpComponent ampComp) {
        DbUtil.add(ampComp);

    }

    /*
     * delete a Component
     */
    public static void deleteComponent(Long compId) {

        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();

            // Now delete AmpComponent
            AmpComponent ampComp = (AmpComponent) session.load(
                AmpComponent.class, compId);

            /*
             * Delete AmpComponentFunding items
             * This operation should be done on casade :(
             */
            String queryString = "select co from "
                + AmpComponentFunding.class.getName()
                + " co where amp_component_id='" + compId + "'";
            Query qry = session.createQuery(queryString);
            Collection ampComCol = qry.list();
            if (ampComCol != null) {
                Iterator componentFundingIterator = ampComCol.iterator();
                while (componentFundingIterator.hasNext()) {
                    session.delete(componentFundingIterator.next());
                }
            }

            session.delete(ampComp);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Exception from deleteComponent() : " + e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception trbf) {
                    logger.error("Transaction roll back failed ");
                }
            }
            e.printStackTrace();
        }
    }

    
    public static AmpComponentFunding getComponentFundingById(Long id) {
    	AmpComponentFunding ret = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            ret=(AmpComponentFunding) session.get(AmpComponentFunding.class, id);
        } catch (Exception ex) {
            logger.error("Unable to get Component Funding for editing from database " + ex.getMessage());
            ex.printStackTrace();
        }
        return ret;
    }
    
    
    /*
     * To get the Component Fundings from the ampComponentFundings Table
     * parameter passed is the component id
     */
    public static Collection getComponentFunding(Long id) {
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponentFunding.class.getName() + " co where co.component=:id";
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);

            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    /*
     * To get the physical progress for a component from the physical progress table...
     * parameter passed is the amp component id
     */
    public static Collection getComponentPhysicalProgress(Long id) {
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpPhysicalPerformance.class.getName() + " co where co.component=:id";
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    public static AmpComponent getComponentById(Long id) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            AmpComponent comp = (AmpComponent) session.get(AmpComponent.class, id);
            return comp;
        } catch (Exception ex) {
        	logger.error("Unable to get Component:", ex);
        }
        return null;
    }

    public static Collection getComponent(String title) {
        logger.info(" in here ");
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponent.class.getName() + " co where co.title=:title";
            qry = session.createQuery(queryString);
            qry.setParameter("title", title, Hibernate.STRING);

            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        logger.info(" returning the collection");
        return col;
    }

    /*
     * this is to check whether a component with the same name already exists in the AMP Components Table.returns true if present and false if not present
     */
    public static boolean checkComponentNameExists(String title) {
        logger.info(" in the checking for components existence through title ");
        boolean flag = false;
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponent.class.getName() + " co where co.title=:title";
            qry = session.createQuery(queryString);
            qry.setParameter("title", title, Hibernate.STRING);

            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        logger.info(" returning the collection");
        if (col.isEmpty()) {
            return false;
        } else
            return true;
    }

    /*
     * this is to check whether a component with the same code already exists in the AMP Components Table.returns true if present and false if not present
     */
    public static boolean checkComponentCodeExists(String code) {
        logger.info(" in the checking for component existence through code ");
        Collection col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select co from " + AmpComponent.class.getName() + " co where co.code=:code";
            qry = session.createQuery(queryString);
            qry.setParameter("code", code, Hibernate.STRING);

            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Component for editing from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        logger.info(" returning the collection");
        if (col.isEmpty()) {
            return false;
        } else
            return true;
    }

    public static Collection getAllComponentIndicators() {
        Session session = null;
        Query qry = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select i from " + AmpComponentsIndicators.class.getName() + " i";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get component indicators");
            logger.debug("Exception : " + e);
        }
        return col;
    }

    public static void saveComponentIndicator(AmpComponentsIndicators newIndicator) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(newIndicator);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Exception from saveComponentIndicator() :" + e.getMessage());
            e.printStackTrace(System.out);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
        }
    }

    public static Collection getComponentIndicator(Long id) {
        Session session = null;
        Query query = null;
        Collection ampCoInd = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ami from " + AmpComponentsIndicators.class.getName() + " ami where (ami.ampCompIndId=:id)";
            query = session.createQuery(queryString);
            query.setParameter("id", id, Hibernate.LONG);
            ampCoInd = query.list();
        } catch (Exception ex) {
            logger.error("Unable to retrieve Indicator/s for a give Component");
            logger.debug("Exception:=" + ex);
        }
        return ampCoInd;
    }

    public static void delComponentIndicator(Long indId) {

        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            AmpComponentsIndicators ampCompInd = (AmpComponentsIndicators) session.load(
                AmpComponentsIndicators.class, indId);
//beginTransaction();
            session.delete(ampCompInd);
            //tx.commit();
        } catch (Exception ex) {
            logger.error("Exception from delComponentIndicators() :" + ex.getMessage());
            ex.printStackTrace(System.out);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception trbf) {
                    logger.error("Transaction roll back failed ");
                    trbf.printStackTrace(System.out);
                }
            }
        }
    }

    public static boolean checkDuplicateNameCode(String name, String code, Long id) {
        Session session = null;
        Query qry = null;
        boolean duplicatesExist = false;
        String queryString = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            if (id != null && id.longValue() > 0) {
                queryString = "select count(*) from "
                    + AmpComponentsIndicators.class.getName() + " ami "
                    + "where ( name=:name"
                    + " or code=:code) and " +
                    "(ami.ampCompIndId !=:id)";
                qry = session.createQuery(queryString);
                qry.setParameter("id", id, Hibernate.LONG);
                qry.setParameter("code", code.trim(), Hibernate.STRING);
                qry.setParameter("name", name.trim(), Hibernate.STRING);
            } else {
                queryString = "select count(*) from "
                    + AmpComponentsIndicators.class.getName() + " ami "
                    + "where ( name=:name"
                    + " or code=:code)";
                qry = session.createQuery(queryString);
                qry.setParameter("code", code.trim(), Hibernate.STRING);
                qry.setParameter("name", name.trim(), Hibernate.STRING);

            }
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                Integer cnt = (Integer) itr.next();
                if (cnt.intValue() > 0)
                    duplicatesExist = true;
            }
        } catch (Exception ex) {
            logger.error("UNABLE to find Indicators with duplicate name.", ex);
            ex.printStackTrace(System.out);
        }
        return duplicatesExist;
    }

    public static void calculateFinanceByYearInfo(Components<FundingDetail> tempComp, Collection<AmpComponentFunding> fundingComponentActivity) {
        SortedMap<Integer, Map<String, Double>> fbyi = new TreeMap<Integer, Map<String, Double>> ();
        Iterator<AmpComponentFunding> fundingDetailIterator = fundingComponentActivity.iterator();
        while (fundingDetailIterator.hasNext()) {
            AmpComponentFunding fundingDetail = fundingDetailIterator.next();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fundingDetail.getTransactionDate());
            int year = cal.get(Calendar.YEAR);
            if (!fbyi.containsKey(year)) {
                fbyi.put(year, new HashMap<String, Double> ());
            }
            Map<String, Double> yearInfo = fbyi.get(year);
            double montoProgramado = yearInfo.get("MontoProgramado") != null ? yearInfo.get("MontoProgramado") : 0;
            double montoReprogramado = yearInfo.get("MontoReprogramado") != null ? yearInfo.get("MontoReprogramado") : 0;
            double montoEjecutado = yearInfo.get("MontoEjecutado") != null ? yearInfo.get("MontoEjecutado") : 0;
            double exchangeRate = 1;
            if (fundingDetail.getExchangeRate() != null && fundingDetail.getExchangeRate() != 0) {
                exchangeRate = fundingDetail.getExchangeRate();
            }
            if (fundingDetail.getTransactionType() == 0 && fundingDetail.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()) ) {
                montoProgramado += fundingDetail.getTransactionAmount() / exchangeRate;
            } else if (fundingDetail.getTransactionType() == 0 && fundingDetail.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                montoReprogramado += fundingDetail.getTransactionAmount() / exchangeRate;
            } else if (fundingDetail.getTransactionType() == 2 && fundingDetail.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                montoEjecutado += fundingDetail.getTransactionAmount() / exchangeRate;
            }
            yearInfo.put("MontoProgramado", montoProgramado);
            yearInfo.put("MontoReprogramado", montoReprogramado);
            yearInfo.put("MontoEjecutado", montoEjecutado);
        }
        tempComp.setFinanceByYearInfo(fbyi);
    }
    
    /**
     * @author Diego Dimunzio
     * Compare components by Tittle
     */
    public static class HelperComponetTitleComparator implements Comparator<AmpComponent> {
    	Locale locale;
        Collator collator;

        public HelperComponetTitleComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperComponetTitleComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

		public int compare(AmpComponent o1, AmpComponent o2) {
			collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getTitle()==null || o2.getTitle()==null)?0:collator.compare(o1.getTitle().toLowerCase(), o2.getTitle().toLowerCase());
            return result;
		}
    }
    
    /**
     * @author Diego Dimunzio
     * Compare components by code
     */
    public static class HelperComponetCodeComparator implements Comparator<AmpComponent> {
    	Locale locale;
        Collator collator;

        public HelperComponetCodeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperComponetCodeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

		public int compare(AmpComponent o1, AmpComponent o2) {
			collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getCode()==null || o2.getCode()==null)?0:collator.compare(o1.getCode().toLowerCase(), o2.getCode().toLowerCase());
            return result;
		}
    }
    
    /**
     * @author Diego Dimunzio
     * Compare components by type
     */
    public static class HelperComponetTypeComparator implements Comparator<AmpComponent> {
    	Locale locale;
        Collator collator;

        public HelperComponetTypeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperComponetTypeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

		public int compare(AmpComponent o1, AmpComponent o2) {
			collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getType().getName()==null || o2.getType().getName()==null)?0:collator.compare(o1.getType().getName(), o2.getType().getName());
            return result;
		}
    }
    
    /**
     * @author Diego Dimunzio
     * Compare components by date 
     */
    public static class HelperComponetDateComparator implements Comparator<AmpComponent> {
		public int compare(AmpComponent o1, AmpComponent o2) {
			int result = (o1.getCreationdate()==null || o2.getCreationdate()==null)?0:o1.getCreationdate().compareTo(o2.getCreationdate());
            return result;
		}
    }
}
