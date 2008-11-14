/**
 * 
 */
package org.digijava.module.categorymanager.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Alex Gartner
 *
 */
public class CategoryManager extends Action {
	
	private static Logger logger	= Logger.getLogger(CategoryManager.class);
	ActionErrors	errors;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		errors						= new ActionErrors();
		CategoryManagerForm myForm	= (CategoryManagerForm) form;
		HttpSession session 		= request.getSession();
		
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
			return mapping.findForward("createOrEditCategory");
		}
		if (request.getParameter("addValue") != null) {
			if(myForm.getPossibleVals()==null){
				myForm.setPossibleVals(new ArrayList<PossibleValue>());
			}
			for (int i=0; i<myForm.getNumOfAdditionalFields(); i++) {
				PossibleValue value=new PossibleValue();
				value.setValue("");
				myForm.getPossibleVals().add(value);
			}
			return mapping.findForward("createOrEditCategory");
		}
		/**
		 * If the user wants to edit an existing category
		 */
		if (request.getParameter("edit") != null) {
			Collection categoryCollection		= this.loadCategories(new Long(request.getParameter("edit")) );
			AmpCategoryClass ampCategoryClass	= (AmpCategoryClass)(categoryCollection.toArray())[0];
			this.populateForm(ampCategoryClass, myForm);
			return mapping.findForward("createOrEditCategory");
		}
		if (request.getParameter("delete") != null ) {
			this.deleteCategory(new Long( request.getParameter("delete") ));
		}
                else{
		/**
		 * Adding a new category to the database
		 */
		if (myForm.getAddNewCategory() != null && myForm.getAddNewCategory().booleanValue()) {
			boolean saved	= this.saveCategoryToDatabase(myForm, errors);
			if (!saved) {
				try{
					AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClass( myForm.getEditedCategoryId() );
					this.populateForm(ampCategoryClass, myForm);
				}
				catch (Exception e) {
					// TODO: handle exception
					logger.info(e.getMessage());
					e.printStackTrace();
				}
				this.saveErrors(request, errors);
				return mapping.findForward("createOrEditCategory");
			}
		}
                }
		/**
		 * loading existing categories
		 */
		myForm.setCategories( this.loadCategories(null) );
		/* Ordering the values alphabetically if necessary */
		if (myForm.getCategories() != null) {
			Iterator iterator	= myForm.getCategories().iterator();
			while(iterator.hasNext()) {
				AmpCategoryClass ampCategoryClass	= (AmpCategoryClass) iterator.next();
				if ( ampCategoryClass.getIsOrdered() && ampCategoryClass.getPossibleValues() != null ) {
					TreeSet treeSet	= new TreeSet( new CategoryManagerUtil().new CategoryComparator() );
					treeSet.addAll( ampCategoryClass.getPossibleValues() );
					ampCategoryClass.setPossibleValues( new ArrayList(treeSet) );
				}
			}
		}
                
		/* END- Ordering the values alphabetically if necessary */
		this.saveErrors(request, errors);
		return mapping.findForward("forward");
	}
	/**
	 * 
	 * @param ampCategoryClass
	 * @param myForm
	 */
	private void populateForm(AmpCategoryClass ampCategoryClass, CategoryManagerForm myForm) {
		if (ampCategoryClass != null) {
			myForm.setCategoryName( ampCategoryClass.getName() );
			myForm.setDescription( ampCategoryClass.getDescription() );
			myForm.setNumOfPossibleValues( new Integer(ampCategoryClass.getPossibleValues().size()) );
			myForm.setEditedCategoryId( ampCategoryClass.getId() );
			myForm.setIsMultiselect( ampCategoryClass.isMultiselect() );
			myForm.setIsOrdered( ampCategoryClass.isOrdered() );
			myForm.setKeyName( ampCategoryClass.getKeyName() );
			
			
			Iterator iterator			= ampCategoryClass.getPossibleValues().iterator();
			String[] possibleValues		= new String [ampCategoryClass.getPossibleValues().size()];
                        List<PossibleValue> possibleVals=new ArrayList();
			int k 						= 0;
			while (iterator.hasNext()) {
				AmpCategoryValue ampCategoryValue	= (AmpCategoryValue) iterator.next();
				possibleValues[k++]					= ampCategoryValue.getValue();
				PossibleValue value					= new PossibleValue();
				
				value.setValue(ampCategoryValue.getValue());
				possibleVals.add(value);
			}
			
			myForm.setPossibleValues( possibleValues );
                        myForm.setPossibleVals(possibleVals);
		}
		else{
			if ( myForm.getPossibleValues() != null && myForm.getPossibleValues().length > 0 ) {
					int count	= 0;
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
	private Collection loadCategories(Long categoryId) {
		logger.info("Getting category with id " + categoryId);
		
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;
			if ( categoryId != null ){
				queryString = "select c from "
					+ AmpCategoryClass.class.getName()
					+ " c where c.id=:id";
				qry			= dbSession.createQuery(queryString);
				qry.setParameter("id", categoryId, Hibernate.LONG);
			}
			else {
				queryString = "select c from "
					+ AmpCategoryClass.class.getName()
					+ " c ";
				qry 		= dbSession.createQuery(queryString);
			}
			
			returnCollection	= qry.list();
			
		} catch (Exception ex) {
			logger.error("Unable to get Categories: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return returnCollection;
	}
	private void deleteCategory (Long categoryId) {
		Session dbSession			= null;
		Transaction transaction		= null;
		try{
			dbSession		= PersistenceManager.getSession();
			transaction		= dbSession.beginTransaction();
			
			String queryString 	= "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.id=:id";
			Query qry			= dbSession.createQuery(queryString);
			qry.setParameter("id", categoryId, Hibernate.LONG);
			dbSession.delete( qry.uniqueResult() );
			transaction.commit();
		} 
		catch (Exception ex) {
			ActionError error	= (ActionError) new ActionError("error.aim.categoryManager.cannotDeleteCategory");
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
			try {
				PersistenceManager.releaseSession(dbSession);
				
			} 
			catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
	}
	/**
	 * 
	 * @param myForm
	 * @param errors
	 * @throws Exception
	 */


		
	
	private boolean saveCategoryToDatabase(CategoryManagerForm myForm, ActionErrors errors) throws Exception {
		
		Session dbSession					= null;	
		Transaction tx						= null;
		String undeletableCategoryValues	= null; 
		
		boolean retValue					= true;
		
		
//		Transaction transaction		= dbSession.beginTransaction();
		
		
		try {
			/* Testing if entered category key is not already used */
			if ( myForm.getEditedCategoryId() == null && 
					CategoryManagerUtil.isCategoryKeyInUse( myForm.getKeyName() )  ) 
			{
				ActionError duplicateKeyError	= new ActionError("error.aim.categoryManager.duplicateKey");
				errors.add("title",duplicateKeyError);
				return false;
			}
			
			dbSession						= PersistenceManager.getSession();
			tx								= dbSession.beginTransaction();
			AmpCategoryClass dbCategory		= new AmpCategoryClass();
			dbCategory.setPossibleValues( new Vector() );
			if (myForm.getEditedCategoryId() != null) {
				String queryString	= "select c from " + AmpCategoryClass.class.getName() + " c where c.id=:id";
				Query query			= dbSession.createQuery(queryString);
				query.setParameter("id", myForm.getEditedCategoryId(), Hibernate.LONG);
				Collection col		= query.list();
				if (col !=null && col.size() > 0)
					dbCategory			= (AmpCategoryClass)col.toArray()[0];
			}

			dbCategory.setName( myForm.getCategoryName() );
			dbCategory.setDescription( myForm.getDescription() );
			dbCategory.setIsMultiselect( myForm.getIsMultiselect() );
			dbCategory.setIsOrdered( myForm.getIsOrdered() );
			dbCategory.setKeyName( myForm.getKeyName() );
			
			//dbCategory.getPossibleValues().clear();
			
			//String[] possibleValues		= myForm.getPossibleValues();
                        List <PossibleValue> possibleVals=myForm.getPossibleVals();
			
			boolean addToPossibleValues	= false;
                       
			int k	= 0; //Index for going through the exisiting values in the database of the AmpCategoryClass
			for (int index=0;index<possibleVals.size(); index++  ) {
				if ( k < dbCategory.getPossibleValues().size() && !addToPossibleValues ) {
					AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)dbCategory.getPossibleValues().get( k );
                                        PossibleValue value=possibleVals.get(index);
					
					if (value.isDisable()||value.getValue().equals("")) {// In this block we are surely editing an existing category (not creating a new one)
						AmpCategoryValue removedValue			= (AmpCategoryValue)dbCategory.getPossibleValues().remove( k );
						try{
							dbSession.flush();
						}
						catch(Exception E) {
							if (undeletableCategoryValues ==  null) 
								undeletableCategoryValues = new String() + removedValue;
							else
								undeletableCategoryValues += ", " + removedValue; 
						}
					}
					else {
						ampCategoryValue.setValue( possibleVals.get(index).getValue() );
						ampCategoryValue.setIndex( k++ );
					}
				}
				else{
					addToPossibleValues	= true;
					if ( !possibleVals.get(index).isDisable() && !possibleVals.get(index).getValue().equals("") ) {
						AmpCategoryValue dbValue	= new AmpCategoryValue();
						dbValue.setValue( possibleVals.get(index).getValue() );
						dbValue.setIndex( dbCategory.getPossibleValues().size() );
						dbValue.setAmpCategoryClass( dbCategory );
						dbCategory.getPossibleValues().add(dbValue);
					}
				}			
			} 
			
			if (undeletableCategoryValues != null) {
				if (tx != null)
					tx.rollback();
				ActionError error2	= new ActionError("error.aim.categoryManager.cannotDeleteValues", undeletableCategoryValues);
				errors.add("title",error2);
				retValue			= false;
			}
			
			else {
				if (myForm.getEditedCategoryId() == null) {
					dbSession.saveOrUpdate( dbCategory );
				}
				else{
					dbSession.flush();
				}
				
				tx.commit();
			}
			
		} catch (Exception ex) {
			logger.error("Unable to save or update the AmpCategoryClass: " + ex);
			ActionError error1	= new ActionError("error.aim.categoryManager.cannotSaveOrUpdate");
			errors.add("title",error1);
			if (tx != null)
					tx.rollback();
			retValue			= false;
			
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return retValue;
	}
	
}
