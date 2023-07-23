package org.digijava.module.categorymanager.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.dbentity.AmpLinkedCategoriesState;
import org.digijava.module.categorymanager.form.CategoryManagerForm;

public class CategoryLabelsUtil {
    private static Logger logger = Logger.getLogger(CategoryLabelsUtil.class);
    public static void populateCategoryWithLabels(CategoryManagerForm form, AmpCategoryClass category) {
    }    public static void populateFormWithLabels(AmpCategoryClass category, CategoryManagerForm form) {
        HashMap<Long, List<Long>> labelsMap     = new HashMap<Long, List<Long>>(); 
        if ( category.getUsedCategories() == null || category.getUsedCategories().size() == 0 )
                return;

        for (AmpCategoryClass lCat : category.getUsedCategories()) {
            removeCategoryFromKeyValueList(lCat.getId(), form.getAvailableCategories());
            if (form.getUsedCategories() == null)
                form.setUsedCategories(new ArrayList<AmpCategoryClass>());
            form.getUsedCategories().add(lCat);
            addCategoryToPossibleVals(lCat, form);
            labelsMap.put(lCat.getId(), new ArrayList<Long>());

            //get state
            if (category.getId() != null) {
                AmpLinkedCategoriesState stateForUsedCat = CategoryManagerUtil.getState(category, lCat);
                if (stateForUsedCat != null) {
                    lCat.setUsedByCategorySingleSelect(stateForUsedCat.getSingleChoice());
                }

            }


        }
        
        if ( category.getPossibleValues() == null || category.getPossibleValues().size() == 0 )
            return;
        
        /**
         * Iterating thorugh the values in the MAIN CATEGORY
         */
        Iterator<AmpCategoryValue> iterVal      = category.getPossibleValues().iterator();
        while (iterVal.hasNext()) {
            AmpCategoryValue value  = iterVal.next();
            PossibleValue pv        = getPossibleValueFromList( form.getPossibleVals(), value.getId() );
            if ( value.getUsedValues() == null || value.getUsedValues().size() == 0 )
                continue;
            /**
             * For each label value
             */
            Iterator<AmpCategoryValue> iterLabelVal = value.getUsedValues().iterator();
            while ( iterLabelVal.hasNext() ) {
                AmpCategoryValue label  = iterLabelVal.next();
                List<Long> labelList    = labelsMap.get( label.getAmpCategoryClass().getId() );
                if ( labelList != null )
                    labelList.add( label.getId() );
                else
                    logger.error("labelList is null. This should never happen as all labels should have corresponding categories in AmpCategoryClass.usedCategories");
            }
            
            Iterator<LabelCategory> iterLC          = pv.getLabelCategories().iterator();
            while ( iterLC.hasNext() ) {
                LabelCategory lc        = iterLC.next();
                List<Long> labelList    = labelsMap.get( lc.getCategoryId() );
                Long  longArray[]       = new Long[0];
                if ( labelList.size() > 0 )
                    lc.setLabelsId( labelList.toArray(longArray) );
                labelsMap.put(lc.getCategoryId(), new ArrayList<Long>() );
            }
            
            
            
        }
    }
    
    public static void removeCategoryFromKeyValueList(Long categoryId, Collection<KeyValue> kvCol) {
        Iterator<KeyValue> iter     = kvCol.iterator();
        while ( iter.hasNext() ) 
            if ( iter.next().getKey().equals( categoryId.toString() ) )
                iter.remove();
    }
    public static void addCategoryToPossibleVals(AmpCategoryClass category, CategoryManagerForm myForm) {
        Iterator<PossibleValue> iterPV  = myForm.getPossibleVals().iterator();
        while ( iterPV.hasNext() ) {
            PossibleValue pv        = iterPV.next();
            if ( pv.getLabelCategories() == null )
                pv.setLabelCategories( new ArrayList<LabelCategory>() );
            LabelCategory lc        = new LabelCategory();
            lc.setCategoryId( category.getId() );
            pv.getLabelCategories().add( lc );
        }
    }
    
    public static PossibleValue getPossibleValueFromList( Collection<PossibleValue> list, Long valueId) {
        Iterator<PossibleValue> iter    = list.iterator();
        while ( iter.hasNext() ) {
            PossibleValue pv            = iter.next();
            if ( pv.getId().equals(valueId) )
                return pv;
        }
        return null;
    }
    
    public static void addUsedCategoriesToPossibleValue( CategoryManagerForm myForm, PossibleValue pv ) {
        if ( myForm.getUsedCategories() == null || myForm.getUsedCategories().size() == 0 )
            return;
        if ( pv.getLabelCategories() == null )
            pv.setLabelCategories( new ArrayList<LabelCategory>() );
        
        Iterator<AmpCategoryClass> iter     = myForm.getUsedCategories().iterator();
        while ( iter.hasNext() ) {
            LabelCategory lc        = new LabelCategory();
            lc.setCategoryId( iter.next().getId() );
            pv.getLabelCategories().add( lc );
        }
        
    }
}
