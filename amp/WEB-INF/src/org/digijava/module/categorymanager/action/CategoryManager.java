/**
 * 
 */
package org.digijava.module.categorymanager.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.visibility.data.DataVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.dbentity.AmpLinkedCategoriesState;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryLabelsUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;

/**
 * @author Alex Gartner
 *
 */
public class CategoryManager extends Action {
    
    private static Logger logger    = Logger.getLogger(CategoryManager.class);
    ActionMessages  errors;

    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        errors                      = new ActionMessages();
        CategoryManagerForm myForm  = (CategoryManagerForm) form;
        HttpSession session         = request.getSession();
        
        /**
         * Test if user is administrator
         */
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                    return mapping.findForward("index");
                }
            }
        /**
         * If the user wants to create a new category
         */
        if (request.getParameter("new") != null) {
            myForm.setNumOfPossibleValues(new Integer(0));
            myForm.setCategoryName(null);
            myForm.setDescription(null);
            myForm.setKeyName(null);
            myForm.setIsMultiselect(false);
            myForm.setIsOrdered(false);
            List<PossibleValue> possibleVals = new ArrayList();
            for (int i = 0; i < 3; i++) {
                possibleVals.add(new PossibleValue());
            }
            myForm.setEditedCategoryId(null);
            myForm.setPossibleVals(possibleVals);
            myForm.setAdvancedMode(true);
            return mapping.findForward("createOrEditCategory");
        }
        /**
         * If the user wants add a new value to existing category we need to show the appropriate text boxes 
         * and prepare the ActionForm
         */
        if (request.getParameter("addValue") != null) {
            int position    = myForm.getPossibleVals().size();
            if ( request.getParameter("position") != null )
                position    = Integer.parseInt(request.getParameter("position"))-1;
            if(myForm.getPossibleVals()==null){
                myForm.setPossibleVals(new ArrayList<PossibleValue>());
            }
            boolean flagEmptyField = false;
            for (int i = 0; i<myForm.getPossibleVals().size();i++){
                if( myForm.getPossibleVals().get(i).getValue().isEmpty() && myForm.getPossibleVals().get(i).isDisable() == false){
                    flagEmptyField = true;
                    break;
                }
            }
            if(!flagEmptyField){
                for (int i=0; i<myForm.getNumOfAdditionalFields(); i++) {
                    PossibleValue value=new PossibleValue();
                    value.setValue("");
                    value.setId(null);
                    value.setDisable(false);
                    CategoryLabelsUtil.addUsedCategoriesToPossibleValue(myForm, value);
                    myForm.getPossibleVals().add(position, value);
                }
            }
            return mapping.findForward("createOrEditCategory");
        }
        /**
         * If the user wants to edit an existing category
         */
        if (request.getParameter("edit") != null) {
            Collection categoryCollection       = this.loadCategories(new Long(request.getParameter("edit")) );
            AmpCategoryClass ampCategoryClass   = (AmpCategoryClass)(categoryCollection.toArray())[0];
            boolean advancedMode                = false;
            if ( request.getParameter("advancedMode") != null)
                advancedMode                    = true;
            this.populateForm(ampCategoryClass, myForm, advancedMode, request);
            return mapping.findForward("createOrEditCategory");
        }
        if (request.getParameter("delete") != null ) {
            this.deleteCategory(new Long( request.getParameter("delete") ));
            // and also notify about changes the dependent modules
            DataVisibility.notifyVisibilityChanged();
        }
                else{
                    /**
                     * Adding a new category to the database
                     */
                    if (myForm.getSubmitPressed() != null && myForm.getSubmitPressed().booleanValue()) {
                        myForm.setSubmitPressed(false);
                        boolean saved   = this.saveCategoryToDatabase(myForm, errors);
                        if (!saved) {
                            /*try{
                                AmpCategoryClass ampCategoryClass   = CategoryManagerUtil.loadAmpCategoryClass( myForm.getEditedCategoryId() );
                                this.populateForm(ampCategoryClass, myForm);
                            }
                            catch (Exception e) {
                                // TODO: handle exception
                                logger.info(e.getMessage());
                                e.printStackTrace();
                            }*/
                            this.saveErrors(request, errors);
                            return mapping.findForward("createOrEditCategory");
                        }else{
                            //if the save process went ok, we should delete from the cache the given category
                            CategoryManagerUtil.removeAmpCategryBykey(myForm.getKeyName());
                            // and also notify about changes the dependent modules
                            DataVisibility.notifyVisibilityChanged();
                        }
                    }
                }
        /**
         * loading existing categories
         */
        myForm.setCategories( this.loadCategories(null) );
        /* Ordering the values alphabetically if necessary */
        if (myForm.getCategories() != null) {
            myForm.setAllCategoryValues( new HashSet<AmpCategoryValue>() );
            for(AmpCategoryClass ampCategoryClass:myForm.getCategories()) {
                myForm.getAllCategoryValues().addAll( ampCategoryClass.getPossibleValues() );
                myForm.getAllCategoryValues().remove(null);
            }
        }
                
        /* END- Ordering the values alphabetically if necessary */      
        String errorString      = CategoryManagerUtil.checkImplementationLocationCategory();
        if ( errorString != null ) {
            ActionMessage error = (ActionMessage) new ActionMessage("error.aim.categoryManager.implLocProblem", errorString);
            errors.add("title", error);
        }
        this.saveErrors(request, errors);
        return mapping.findForward("forward");
    }
    /**
     * 
     * @param ampCategoryClass
     * @param myForm
     */
    private void populateForm(AmpCategoryClass ampCategoryClass, CategoryManagerForm myForm, boolean advancedMode, HttpServletRequest request) {
        if (ampCategoryClass != null) {
            myForm.setCategoryName( ampCategoryClass.getName() );
            myForm.setDescription( ampCategoryClass.getDescription() );
            myForm.setNumOfPossibleValues( new Integer(ampCategoryClass.getPossibleValues().size()) );
            myForm.setEditedCategoryId( ampCategoryClass.getId() ); 
            myForm.setIsMultiselect( ampCategoryClass.isMultiselect() );
            myForm.setIsOrdered( ampCategoryClass.isOrdered() );
            myForm.setKeyName( ampCategoryClass.getKeyName() );
            
            myForm.setAdvancedMode(advancedMode);
            
            
            ArrayList<AmpCategoryClass> availableCategories = new ArrayList<AmpCategoryClass>( myForm.getCategories() );
            availableCategories.remove( ampCategoryClass );
            myForm.setAvailableCategories( createKVList(availableCategories, request) );
            myForm.setUsedCategories(null);
            
            
            Iterator iterator                   = ampCategoryClass.getPossibleValues().iterator();
            //String[] possibleValues               = new String [ampCategoryClass.getPossibleValues().size()];
            List<PossibleValue> possibleVals    = new ArrayList<PossibleValue>();
            
            while (iterator.hasNext()) {
                AmpCategoryValue ampCategoryValue   = (AmpCategoryValue) iterator.next();
                if (ampCategoryValue!=null){
                    //possibleValues[k++]                   = ampCategoryValue.getValue();
                    PossibleValue value                 = new PossibleValue();
                
                    value.setValue(ampCategoryValue.getValue());
                    value.setId(ampCategoryValue.getId());
                    
                    value.setDeleted(!ampCategoryValue.isVisible());
//                  boolean isDeleted = true;
//                  if ((ampCategoryValue.getDeleted() == null) || (ampCategoryValue.getDeleted().equals(false)))
//                      isDeleted = false;
//                  value.setDeleted(isDeleted);
                    value.setDisable(!ampCategoryValue.isVisible());
                    possibleVals.add(value);
                }
            }
            
            //myForm.setPossibleValues( possibleValues );
            myForm.setPossibleVals(possibleVals);
            
            CategoryLabelsUtil.populateFormWithLabels(ampCategoryClass, myForm);
        }
        else{
            if ( myForm.getPossibleValues() != null && myForm.getPossibleValues().length > 0 ) {
                    int count   = 0;
                    for (int i=0; i< myForm.getPossibleValues().length; i++) {
                        if ( myForm.getPossibleValues()[i].length() > 0  )
                                    count++;
                    }
                    myForm.setNumOfPossibleValues( new Integer(count) );
            }
            else
                myForm.setNumOfPossibleValues( new Integer(0) );
        }
        
    }
    
    
    /**
     * @param categoryId set to null if you want to load all categories
     * @return all the categories from the database or just one if categoryId is not null
     */
    public Collection<AmpCategoryClass> loadCategories(Long categoryId) {
        logger.info("Getting category with id " + categoryId);
        Collection<AmpCategoryClass> returnCollection = null;
        try {
            String queryString;
            Query qry;
            if (categoryId != null) {
                queryString = "select c from " + AmpCategoryClass.class.getName() + " c where c.id=:id";
                qry = PersistenceManager.getSession().createQuery(queryString).setParameter("id", categoryId, LongType.INSTANCE);
            }
            else {
                queryString = "select c from " + AmpCategoryClass.class.getName() + " c ";
                qry = PersistenceManager.getSession().createQuery(queryString);
            }
            returnCollection = qry.list();
            
            for(AmpCategoryClass acc:returnCollection)
                acc.getUsedCategories().size();
            return returnCollection;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCategory (Long categoryId) {
        Session dbSession = PersistenceManager.openNewSession();
        Transaction transaction = null;
        try{
//beginTransaction();
            transaction = dbSession.beginTransaction();
            dbSession.createQuery("delete from " + AmpLinkedCategoriesState.class.getName() +" s where s.mainCategory.id=:categoryId")
                .setLong("categoryId", categoryId).executeUpdate();
            //transaction.commit();
            
            String queryString  = "select c from "  + AmpCategoryClass.class.getName()+ " c where c.id=:id";
            Query qry           = dbSession.createQuery(queryString);
            qry.setParameter("id", categoryId, LongType.INSTANCE);
            dbSession.delete( qry.uniqueResult() ); 
            transaction.commit();
        } 
        catch (Exception ex) {
            ActionMessage error = (ActionMessage) new ActionMessage("error.aim.categoryManager.cannotDeleteCategory");
            errors.add("title", error);
            logger.error("Unable to delete Category: " + ex.getMessage());
            
            try {
                transaction.rollback();
            } catch (HibernateException e) {
                logger.error("Failed to rollback transaction when deleting category with id " + categoryId + ":"+ ex.getMessage());
                e.printStackTrace();
            }
        } 
        finally {
            PersistenceManager.closeSession(dbSession);
        }
    }

    private boolean saveCategoryToDatabase(CategoryManagerForm myForm, ActionMessages errors) throws Exception {
        
        Session dbSession = PersistenceManager.openNewSession();
        Transaction tx = dbSession.beginTransaction();
        String undeletableCategoryValues = null;
        boolean retValue = true;
        
        try {
            /* Testing if entered category key is not already used */
            if (myForm.getEditedCategoryId() == null && CategoryManagerUtil.isCategoryKeyInUse( myForm.getKeyName())) {
                ActionMessage duplicateKeyError = new ActionMessage("error.aim.categoryManager.duplicateKey");
                errors.add("title", duplicateKeyError);
                return false;
            }
    
            AmpCategoryClass dbCategory = new AmpCategoryClass();
            dbCategory.setPossibleValues(new ArrayList<AmpCategoryValue>());
            if (myForm.getEditedCategoryId() != null) {
                AmpCategoryClass cc = (AmpCategoryClass) dbSession.get(AmpCategoryClass.class, myForm.getEditedCategoryId());
                if (cc != null)
                    dbCategory = cc;
            }

            dbCategory.setName( myForm.getCategoryName() );
            dbCategory.setDescription( myForm.getDescription() );
            dbCategory.setIsMultiselect( myForm.getIsMultiselect() );
            dbCategory.setIsOrdered( myForm.getIsOrdered() );
            dbCategory.setKeyName( myForm.getKeyName() );
            
            //dbCategory.getPossibleValues().clear();
            
            //String[] possibleValues       = myForm.getPossibleValues();
            List <PossibleValue> possibleVals=myForm.getPossibleVals();
            
            /**
             * Eliminate empty values from the new values
             */
            Iterator<PossibleValue> iter            = possibleVals.iterator();
            while ( iter.hasNext() ) {
                if ("".equals(iter.next().getValue()))
                    iter.remove();
            }
            /**
             * Add new values
             */
            String unaddableCategoryValues = null;

            for ( int i=0; i<possibleVals.size(); i++ ) {
                PossibleValue pVal = possibleVals.get(i);
                if ( !pVal.isDisable() && (pVal.getId() == null || pVal.getId() == 0L)) {
                    AmpCategoryValue newVal         = new AmpCategoryValue();
                    newVal.setValue( pVal.getValue() );
                    newVal.setAmpCategoryClass( dbCategory );
                    newVal.setIndex(i);
                    dbCategory.getPossibleValues().add(i, newVal);
                    
                    try {
                        //trying to add 
                        dbSession.flush();
                    }
                    catch (Exception e) {
                        unaddableCategoryValues = (unaddableCategoryValues == null ? "" : (unaddableCategoryValues + ", ")) + newVal.getValue();
                    }
                }
            }
            
            /**
             * Save modifications to existing values only if we are in advanced mode
             */
            if ( myForm.isAdvancedMode() ) {
                for ( int i = 0; i < possibleVals.size(); i++ ) {
                    dbCategory.getPossibleValues().get(i).setValue( possibleVals.get(i).getValue() );
                }
            }
            
            /**
             * Remove deleted values from database
             */
            iter                        = possibleVals.iterator();
            while ( iter.hasNext() ) {
                PossibleValue pVal      = iter.next();
                if ( pVal.isDisable() ) {
                    if ( pVal.getId() == null ) {
                        throw new Exception ("Received id paramater is null");
                    }
                    Iterator<AmpCategoryValue> iterCV   = dbCategory.getPossibleValues().iterator();
                    while ( iterCV.hasNext() ) {
                        
                        AmpCategoryValue ampCategoryValue   = iterCV.next();
                        
                        // hack
                        if (ampCategoryValue == null)
                            continue;
                        
                        if ( pVal.getId().equals(ampCategoryValue.getId()) ) {
                            if (!pVal.isDeleted()) {
                                try{
                                    ampCategoryValue.setDeleted(true);
                                    dbSession.flush();
                                    //removing it, since softdelete is only for limiting user input
    //                              if ( CategoryManagerUtil.verifyDeletionProtectionForCategoryValue( dbCategory.getKeyName(), 
    //                                                                      ampCategoryValue.getValue()) )
    //                                      throw new Exception("This value is in CategoryConstants.java and used by the system");
                                }
                                catch (Exception e) {
                                    if (undeletableCategoryValues ==  null) 
                                        undeletableCategoryValues = ampCategoryValue.getValue();
                                    else
                                        undeletableCategoryValues += ", " + ampCategoryValue.getValue(); 
                                }
                            }
                        }
                    }
                }
                
                //restore undeleted values
                if (pVal.isDeleted() && !pVal.isDisable()) {
                    if ( pVal.getId() == null ) {
                        throw new Exception ("Received id paramater is null");
                    }
                    Collection <AmpCategoryValue> acvColl = dbCategory.getPossibleValues();
                    for (AmpCategoryValue acv: acvColl) {
                        if (acv == null)
                            continue;
                        if ( pVal.getId().equals(acv.getId()) ) {
                            try{
                                acv.setDeleted(false);
                                dbSession.flush();
                            }
                            catch (Exception e) {
                                logger.error("could not mark ACV as deleted", e);
                            }
                        }
                    }
                }
            }
            
            this.reindexAmpCategoryValueList( dbCategory.getPossibleValues() );
            
            CategoryLabelsUtil.populateCategoryWithLabels(myForm, dbCategory);
            
            String dupValue     = checkDuplicateValues(dbCategory.getPossibleValues()); 
            
            if ( dupValue != null ) {
                if (tx != null)
                    tx.rollback();
                ActionMessage error = new ActionMessage("error.aim.categoryManager.duplicateValue", dupValue);
                errors.add("title",error);
                retValue = false;
            }
            else {
            
                if (undeletableCategoryValues != null) {
                    if (tx != null)
                        tx.rollback();
                    ActionMessage error2    = new ActionMessage("error.aim.categoryManager.cannotDeleteValues", undeletableCategoryValues);
                    errors.add("title",error2);
                    retValue = false;
                }
                
                else {
                    if (myForm.getEditedCategoryId() == null) {
                        // AMP-29782
                        if (dbCategory.getPossibleValues() != null && dbCategory.getPossibleValues().size() > 0) {
                            dbCategory.getPossibleValues().forEach(value -> {
                                dbSession.saveOrUpdate(value);
                            });
                        }

                        dbSession.saveOrUpdate(dbCategory);
                    }
                    else{
                        dbSession.flush();
                        //label category states updates
                        String queryString  = "delete from " + AmpLinkedCategoriesState.class.getName() + " s where s.mainCategory=:dbCategory";
                        dbSession.createQuery(queryString).setEntity("dbCategory", dbCategory).executeUpdate();
                        Set<AmpCategoryClass> usedCategories = dbCategory.getUsedCategories();
                        for (AmpCategoryClass ampCategoryClass : usedCategories) {
                            AmpLinkedCategoriesState newState = new AmpLinkedCategoriesState ();
                            newState.setMainCategory(dbCategory);
                            newState.setLinkedCategory(ampCategoryClass);
                            newState.setSingleChoice(ampCategoryClass.getUsedByCategorySingleSelect());
                            dbSession.saveOrUpdate( newState );
                        }                       

                    }
                    tx.commit();
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to save or update the AmpCategoryClass: ", ex);
            ActionMessage error1    = new ActionMessage("error.aim.categoryManager.cannotSaveOrUpdate");
            errors.add("title",error1);
            if (tx != null)
                tx.rollback();
            return false;
        } finally {
            PersistenceManager.closeSession(dbSession);
        }
        return retValue;
    }
    
    private void reindexAmpCategoryValueList(List<AmpCategoryValue> values) {
        for (int i = 0; i < values.size(); i++) 
            if (values.get(i) != null)
                values.get(i).setIndex(i);
    }
    
    public static String checkDuplicateValues( List<AmpCategoryValue>  values) {
        HashSet<String> set = new HashSet<String>( values.size());      
        for (AmpCategoryValue val: values) {
            if (val != null && !set.add(val.getValue())) {
                return val.getValue();
            }
        }
        return null;
    }
    
    public static Set<KeyValue> createKVList(Collection<AmpCategoryClass> categories, HttpServletRequest request) {
        TreeSet<KeyValue> kvCategories  = new TreeSet<KeyValue>( KeyValue.valueComparator );
        Iterator<AmpCategoryClass> iter     = categories.iterator();
        while (iter.hasNext()) {
            AmpCategoryClass cat    = (AmpCategoryClass) iter.next();
            String translatedName   = CategoryManagerUtil.translate( 
                            CategoryManagerUtil.getTranslationKeyForCategoryName(cat.getKeyName()), cat.getName()   );
            
            KeyValue kv             = new KeyValue(cat.getId().toString(), translatedName);
            kvCategories.add(kv);
        }
        return kvCategories;
    }
    
}
