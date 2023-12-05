package org.digijava.module.contentrepository.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.contentrepository.dbentity.template.PossibleValue;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.helper.template.PossibleValueHelper;
import org.digijava.module.contentrepository.helper.template.SubmittedValueHolder;
import org.digijava.module.contentrepository.helper.template.TemplateDocumentHelper;
import org.digijava.module.contentrepository.helper.template.TemplateFieldHelper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
/**
 * @author Dare
 *
 */
public class TemplateDocsUtil {
    
    private static Logger logger = Logger.getLogger(TemplateDocsUtil.class);
    
    public static List<TemplateDoc> getTemplateDocs(){
        Session session=null;
        String queryString =null;
        Query query=null;
        List<TemplateDoc> returnValue=null;
        try {
            session=PersistenceManager.getRequestDBSession();           
            queryString= "select a from " + TemplateDoc.class.getName()+ " a";
            query=session.createQuery(queryString);
            returnValue=query.list();
        }catch(Exception ex) {
            logger.error("couldn't load Templates" + ex.getMessage());
        }
        return returnValue;
    }
    
    public static TemplateDoc getTemplateDoc(Long id){
        TemplateDoc tempDoc=null;
        Session session=null;
        String queryString =null;
        Query query=null;
        try {
            session=PersistenceManager.getRequestDBSession();           
            queryString= "select t from " + TemplateDoc.class.getName()+ " t where t.id="+id;
            query=session.createQuery(queryString);
            tempDoc=(TemplateDoc)query.uniqueResult();
        } catch (Exception e) {
            logger.error("couldn't load Template" + e.getMessage());
        }
        return tempDoc;
    }
    
    public static TemplateDoc getTemplateDocByName(String name){
        TemplateDoc tempDoc=null;
        Session session=null;
        String queryString =null;
        Query query=null;
        try {
            session=PersistenceManager.getRequestDBSession();           
            queryString= "select t from " + TemplateDoc.class.getName()+ " t where t.name='"+name+"'";
            query=session.createQuery(queryString);
            tempDoc=(TemplateDoc)query.uniqueResult();
        } catch (Exception e) {
            logger.error("couldn't load Template" + e.getMessage());
        }
        return tempDoc;
    }
    
    public static void saveTemplateDoc(TemplateDocumentHelper tempDocHelper) throws Exception{
        Session session=null;
        Transaction tx=null;
        try {
            session=PersistenceManager.getRequestDBSession();
//beginTransaction();
            if(tempDocHelper.getId()!=null){
                TemplateDoc oldTemplateDoc= (TemplateDoc)session.get(TemplateDoc.class, tempDocHelper.getId());
                session.delete(oldTemplateDoc);
            }
            TemplateDoc newDoc=new TemplateDoc(tempDocHelper.getName());
            session.save(newDoc);
            if(tempDocHelper.getFields()!=null && tempDocHelper.getFields().size()>0){
                //save fields
                for (TemplateFieldHelper pendingField : tempDocHelper.getFields()) {
                    Class clazz = Class.forName(pendingField.getFieldType());
                    TemplateField templateField=(TemplateField) clazz.newInstance();
                    templateField.setOrdinalNumber(pendingField.getOrdinalNumber());
                    templateField.setTemplateDoc(newDoc);
                    session.save(templateField);
                    //save field values
                    List<PossibleValueHelper> possibleValues=pendingField.getValues();
                    if(possibleValues!=null && possibleValues.size()>0){;
                        for (PossibleValueHelper possibleValueHelper : possibleValues) {
                            PossibleValue posVal=new PossibleValue();
                            posVal.setValue(possibleValueHelper.getPreDefinedValue());
                            posVal.setField(templateField);
                            session.save(posVal);
                        }
                    }
                }
            }           
            //tx.commit();
        }catch(Exception ex) {
            if(tx!=null) {
                try {
                    tx.rollback();                  
                }catch(Exception e ) {
                    logger.error("...Rollback failed");
                    throw new AimException("Can't rollback", e);
                }           
            }
            throw new AimException("update failed",ex);
        }
    }
    
    public static class HelperTempDocNameComparator implements Comparator<TemplateDoc> {
        Locale locale;
        Collator collator;

        public HelperTempDocNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperTempDocNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(TemplateDoc o1, TemplateDoc o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = collator.compare(o1.getName(), o2.getName());
            return result;
        }
    }
    
    public static class HelperTempDocFieldOrdinaryNumberComparator implements Comparator<TemplateFieldHelper> {
        public int compare(TemplateFieldHelper o1, TemplateFieldHelper o2) {
            int result = o1.getOrdinalNumber().compareTo(o2.getOrdinalNumber());
            return result;
        }
    }
    
    public static class TempDocFieldOrdinaryNumberComparator implements Comparator<TemplateField> {
        public int compare(TemplateField o1, TemplateField o2) {
            int result = o1.getOrdinalNumber().compareTo(o2.getOrdinalNumber());
            return result;
        }
    }
    
    /**
     * used to sort submitted values (from create document using template) with their ordinal number in the template
     * @author skvisha
     *
     */
    public static class SubmittedValuesOrdinaryNumberComparator implements Comparator<SubmittedValueHolder> {
        public int compare(SubmittedValueHolder o1, SubmittedValueHolder o2) {
            int result = o1.getFieldOrdinalValue().compareTo(o2.getFieldOrdinalValue());
            return result;
        }
    }
    
    public static class PossibleValuesValueComparator implements Comparator<PossibleValue> {
        @Override
        public int compare(PossibleValue o1, PossibleValue o2) {
            int result = o1.getValue().compareTo(o2.getValue());
            return result;
        }
        
    }
}
