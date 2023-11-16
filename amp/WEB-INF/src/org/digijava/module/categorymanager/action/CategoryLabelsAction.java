package org.digijava.module.categorymanager.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.exceptions.UsedCategoryException;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryLabelsUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoryLabelsAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        return this.addingUsedCategories(mapping, form, request, response);
    }
    public ActionForward addingUsedCategories (ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        
        CategoryManagerForm myForm      = (CategoryManagerForm) form;
        
        if ( "addCategory".equals( myForm.getUseAction() ) && myForm.getUsedCategoryId() != null ) {
            if ( myForm.getUsedCategories() == null )
                myForm.setUsedCategories( new ArrayList<AmpCategoryClass>() );
            
            AmpCategoryClass category   = CategoryManagerUtil.loadAmpCategoryClass(myForm.getUsedCategoryId());
            category.setUsedByCategorySingleSelect(myForm.getUsedCatIsSingleSelect());
            myForm.getUsedCategories().add( category );
            
            CategoryLabelsUtil.removeCategoryFromKeyValueList(myForm.getUsedCategoryId(), myForm.getAvailableCategories() );
            CategoryLabelsUtil.addCategoryToPossibleVals(category, myForm);
            
        }
        
        if ( "delCategory".equals( myForm.getUseAction() ) && myForm.getDelUsedCategoryId() != null ) {
            if ( myForm.getUsedCategories() == null || myForm.getUsedCategories().size() == 0 )
                throw new UsedCategoryException("myForm.usedCategories cannot be null since we need to remove an element from it");
            Iterator<AmpCategoryClass> iter     = myForm.getUsedCategories().iterator();
            int index                           = 0;
            while ( iter.hasNext() ) {
                AmpCategoryClass category       = iter.next();
                if ( category.getId().equals( myForm.getDelUsedCategoryId() ) ){
                    iter.remove();
                    String translatedName   = CategoryManagerUtil.translate( 
                                CategoryManagerUtil.getTranslationKeyForCategoryName(category.getKeyName()), category.getName()   );
                    KeyValue kv             = new KeyValue(category.getId().toString(), translatedName);
                    myForm.getAvailableCategories().add(kv);
                    break;
                }
                index++ ;
            }
            
            Iterator<PossibleValue> iterPV  = myForm.getPossibleVals().iterator();
            while ( iterPV.hasNext() ) {
                PossibleValue pv        = iterPV.next();
                pv.getLabelCategories().remove(index);
            }
            
        }
        myForm.setUsedCategoryId(null);
        
        return mapping.findForward("createOrEditCategory");
    }

}
