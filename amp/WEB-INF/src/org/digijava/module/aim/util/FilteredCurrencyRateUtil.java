package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFilteredCurrencyRate;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FilteredCurrencyRateUtil {
    
    private static Logger logger = Logger.getLogger(FilteredCurrencyRateUtil.class);

    private boolean requestDbSession    ;
    
    /**
     * This set will contain Strings of the type EUR-USD 
     * (so an element in the set will be a pair of codes (toCurrencyCode-fromCurrencyCode) concatenated)
     */
    private HashSet<String> allFilteredRates;
    
    public FilteredCurrencyRateUtil() {
        this (true);
    }
    
    public FilteredCurrencyRateUtil ( boolean requestDbSession ) {
        this.requestDbSession       = requestDbSession;
        this.allFilteredRates           = null;
    }
    
    private Session getHibernateSession () throws DgException, SQLException {
        if ( requestDbSession )
            return PersistenceManager.getRequestDBSession();
        
        return PersistenceManager.getSession();
    }   
    
    public List<AmpFilteredCurrencyRate> getAllFilteredCurrencyRates() {
         Session sess = null;
         List<AmpFilteredCurrencyRate> result       = null;
        try {
            sess = this.getHibernateSession();
            String queryString  = "select f from " + AmpFilteredCurrencyRate.class.getName() +  " f order by f.fromCurrency.currencyCode" ;
            Query query             = sess.createQuery(queryString);
            
            result                              = query.list();
        }
        catch (Exception e) {
            logger.error("Could not get all AmpFilteredCurrencyRates: " + e.getMessage() );
            e.printStackTrace();
        }
        
        return result;
        
    }
    
    public void deleteFilteredCurrencyRate(Long id) {
        Session sess        = null;
         try {
             sess = this.getHibernateSession();
//beginTransaction();
             String queryString     = "select f from " + AmpFilteredCurrencyRate.class.getName() +  " f where f.id=:id" ;
             Query query                = sess.createQuery(queryString);
             query.setLong("id", id);

             AmpFilteredCurrencyRate f      =   (AmpFilteredCurrencyRate)query.uniqueResult();
             sess.delete(f);
             //tx.commit();
         }
         catch (Exception e) {
             logger.error("Could not delete AmpFilteredCurrencyRate with id " + id + ": " + e.getMessage() );
             e.printStackTrace();
         }
    }
    
    public void addFilteredCurrencyRate( AmpFilteredCurrencyRate f) {
        Session sess        = null;
         try {
             sess = this.getHibernateSession();
//beginTransaction();
             sess.save(f);
             //tx.commit();
         }
         catch (Exception e) {
             logger.error("Could not add AmpFilteredCurrencyRate: " + e.getMessage() );
             e.printStackTrace();
         }
       
    }
    
    /**
     * 
     * @param toCurrencyCode
     * @param fromCurrencyCode
     * @return if it return true than the pair already exists.
     */
    public boolean checkPairExistance(String toCurrencyCode, String fromCurrencyCode) {
         Session sess = null;
         boolean result     = true;
       try {
           sess = this.getHibernateSession();
           String queryString   = "select f from " + AmpFilteredCurrencyRate.class.getName() +  " f  " +
                "where f.fromCurrency.currencyCode=:fromCurrencyCode AND f.toCurrency.currencyCode=:toCurrencyCode" ;
           Query query              = sess.createQuery(queryString);
           query.setString("toCurrencyCode", toCurrencyCode);
           query.setString("fromCurrencyCode", fromCurrencyCode);
           
           result   = query.list().size()!=0 ;
       }
       catch (Exception e) {
            logger.error("Could not check pair existance: " + e.getMessage() );
            e.printStackTrace();
        }
       
       return result;
       
    }
    
    public boolean checkPairExistanceUsingCache(String toCurrencyCode, String fromCurrencyCode) {
        List<AmpFilteredCurrencyRate> allFilteredCurrencyRates  = this.getAllFilteredCurrencyRates();
        if ( allFilteredCurrencyRates != null && allFilteredCurrencyRates.size()>0 ) {
            this.allFilteredRates       = new HashSet<String>( allFilteredCurrencyRates.size() );
            for (AmpFilteredCurrencyRate f: allFilteredCurrencyRates ) {
                this.allFilteredRates.add( f.getToCurrency().getCurrencyCode() + "-" + f.getFromCurrency().getCurrencyCode() );
            }
            return this.allFilteredRates.contains( toCurrencyCode + "-" + fromCurrencyCode );
        }
        return false;
    }
    
    
}
