package org.dgfoundation.amp.ar.dbentity;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.*;

@Entity
@Table
public class AmpTeamFilterData extends AmpFilterData {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(AmpTeamFilterData.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_team_filter_data_seq_generator")
    @SequenceGenerator(name = "amp_team_filter_data_seq_generator", sequenceName = "AMP_TEAM_FILTER_DATA_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_class_name")
    private String propertyClassName;

    @Column(name = "element_class_name")
    private String elementClassName;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "amp_team_id", nullable = false)
    private AmpTeam filterRelObj;
    
    public AmpTeamFilterData() {
        super();
    }
    
    public AmpTeamFilterData(FilterDataSetInterface filterRelObj,
            String propertyName, String propertyClassName,
            String elementClassName, String value) {
        super(filterRelObj, propertyName, propertyClassName, elementClassName, value);
    }

    public static void deleteOldFilterData ( Long ampReportId ) {
        Session sess    = null;
        Transaction tx  = null;
        try {
            sess    = PersistenceManager.openNewSession();
            tx      = sess.beginTransaction();
            String qryStr   = "select a from "
                + AmpTeamFilterData.class.getName() + " a "
                + "where (a.filterRelObj=:report)";
            Query query     = sess.createQuery(qryStr);
            query.setLong("report", ampReportId);
            List results    = query.list();
            
            if ( results != null ) {
                Iterator iter   = results.iterator();
                boolean cleared = false;
                while (iter.hasNext()) {
                    AmpTeamFilterData afd   = (AmpTeamFilterData)iter.next();
                    if ( !cleared ) {
                        cleared = true;
                        afd.getFilterRelObj().getFilterDataSet().clear();
                    } 
                    afd.setFilterRelObj(null);
                    sess.delete(afd);
                }
            }
            tx.commit();
            
        } catch (Exception e) {
            logger.error("Rolling back transaction of deleting exising filters before saving new ones", e);
            tx.rollback();
        }
        finally{
            PersistenceManager.closeSession(sess);
        }
        
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        AmpTeamFilterData.logger = logger;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyClassName() {
        return propertyClassName;
    }

    @Override
    public void setPropertyClassName(String propertyClassName) {
        this.propertyClassName = propertyClassName;
    }

    @Override
    public String getElementClassName() {
        return elementClassName;
    }

    @Override
    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public AmpTeam getFilterRelObj() {
        return filterRelObj;
    }

    public void setFilterRelObj(AmpTeam filterRelObj) {
        this.filterRelObj = filterRelObj;
    }
}
