/*
 *   SaveCategory.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 7, 2004
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.cms.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.digijava.module.cms.util.CMSManager;
import org.digijava.module.cms.exception.*;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class SaveCategory
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    CMSForm cmsForm = (CMSForm) form;
    ActionErrors errors = new ActionErrors();
    String forward = "showTaxonomyManager";

    //Get cms
    ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
        request);
    String siteId;
    String instanceId;
    if (moduleInstance.getRealInstance() == null) {
      siteId = moduleInstance.getSite().getSiteId();
      instanceId = moduleInstance.getInstanceName();
    }
    else {
      siteId = moduleInstance.getRealInstance().getSite().getSiteId();
      instanceId = moduleInstance.getRealInstance().getInstanceName();
    }
    CMS cms = null;
    try {
      cms = DbUtil.getCMSItem(siteId, instanceId);
    }
    catch (CMSException ex1) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.saveCategory"));
    }
    //Get cms


    //Create new category
    if (cmsForm.getProcessingMode() == CMSForm.MODE_NEW) {
      long primaryParentId = cmsForm.getPrimaryParentCategoryId();

      CMSCategory newCategory = null;
      try {

        if (primaryParentId > 0) {
          CMSCategory primaryParent = null;
          primaryParent = DbUtil.getCategoryItem(primaryParentId);
          newCategory = new CMSCategory(primaryParent);

        }
        else {
          newCategory = new CMSCategory(null);
          newCategory.setOwner(cms);
        }

        String[] selCatIds = cmsForm.getPropertyArray();
        if (selCatIds != null && selCatIds.length > 0) {
          for (int catIndex = 0;
               catIndex < selCatIds.length;
               catIndex++) {
            if (!selCatIds[catIndex].equals(String.valueOf(primaryParentId))) {
              CMSCategory newParent = DbUtil.getCategoryItem(Long.parseLong(
                  selCatIds[catIndex]));
              newCategory.getParentCategories().add(newParent);
            }
          }
        }

        cmsForm.fillCategoryProperties(newCategory);

        DbUtil.createCategory(newCategory);
      }
      catch (Exception ex) {
      }
    } else if (cmsForm.getProcessingMode() == CMSForm.MODE_EDIT){ //Update edited category
      long categoryId = cmsForm.getCategoryId();
      CMSCategory category = null;
      try {

        long primaryParentId = cmsForm.getPrimaryParentCategoryId();
        CMSCategory tmpCategory = DbUtil.getCategoryItem(categoryId);


        //Change primary parent if recuired
        if ((tmpCategory.getPrimaryParent()==null && primaryParentId>0) ||
            (tmpCategory.getPrimaryParent()!=null &&
             tmpCategory.getPrimaryParent().getId() != primaryParentId)) {
          DbUtil.changeCategoryPrimaryParent(categoryId,
                                             primaryParentId,
                                             cms);
          category = DbUtil.getCategoryItem(categoryId);
        } else {
          category = tmpCategory;
        }

        //Set parent categories
          String [] selCatIds = cmsForm.getPropertyArray();
          List toAddIds = new ArrayList();
          if (!category.getParentCategories().isEmpty()) {
            for (int propIndex = 0;
                 propIndex < selCatIds.length;
                 propIndex++) {
              boolean contains = false;
              Iterator it = category.getParentCategories().iterator();
              while (it.hasNext()) {
                CMSCategory parCategory = (CMSCategory) it.next();
                if (selCatIds[propIndex].equals(String.valueOf(parCategory.getId()))) {
                  contains = true;
                  break;
                }
              }
              if (!contains && (category.getPrimaryParent() == null ||
                                !selCatIds[propIndex].equals(String.
                  valueOf(category.getPrimaryParent().getId())))) {
                CMSCategory newParent = DbUtil.getCategoryItem(Long.parseLong(
                    selCatIds[propIndex]));
                category.getParentCategories().add(newParent);
              }
            }

            //Remove parent category from the CMSCategory if removed during edit
            Iterator it = category.getParentCategories().iterator();
            while (it.hasNext()) {
              CMSCategory parCategory = (CMSCategory) it.next();
              boolean contains = false;
              for (int propIndex = 0;
                   propIndex < selCatIds.length;
                   propIndex++) {
                if (selCatIds[propIndex].equals(String.valueOf(parCategory.getId()))) {
                  contains = true;
                  break;
                }
              }
              if (!contains) {
                it.remove();
              }
            }

          } else {
            for (int propIndex = 0;
                 propIndex < selCatIds.length;
                 propIndex++) {
              if (category.getPrimaryParent() == null ||
                  !selCatIds[propIndex].equals(String.valueOf(category.
                  getPrimaryParent().getId()))) {
                CMSCategory newParent = DbUtil.getCategoryItem(Long.parseLong(
                    selCatIds[propIndex]));
                category.getParentCategories().add(newParent);
              }
            }
          }



          //Set related categories
          String relCategories[] = cmsForm.getRelatedIdArray();
          if (relCategories != null && relCategories.length > 0){
            for (int relCatIndex = 0;
                 relCatIndex < relCategories.length;
                 relCatIndex ++ ){
              Iterator relCatIt = category.getRelatedCategories().iterator();
              boolean contains = false;
              while (relCatIt.hasNext()) {
                CMSCategory relCat = (CMSCategory) relCatIt.next();
                if (relCategories[relCatIndex].equals(String.valueOf(relCat.getId()))) {
                  contains = true;
                  break;
                }
              }

              if (!contains) {
                CMSCategory newRelCat = DbUtil.getCategoryItem(Long.parseLong(
                    relCategories[relCatIndex]));
                category.getRelatedCategories().add(newRelCat);
              }
            }

            //Remove related category from the CMSCategory if removed during edit
              Iterator it = category.getRelatedCategories().iterator();
              while (it.hasNext()) {
                CMSCategory relCategory = (CMSCategory) it.next();
                boolean contains = false;
                for (int propIndex = 0;
                     propIndex < relCategories.length;
                     propIndex++) {
                  if (relCategories[propIndex].equals(String.valueOf(relCategory.getId()))) {
                    contains = true;
                    break;
                  }
                }
                if (!contains) {
                  it.remove();
                }
              }


          }

        cmsForm.fillCategoryProperties(category);
        DbUtil.updateCategory(category);
      }
      catch (Exception ex) {
      }
    }
    return mapping.findForward(forward);
  }

}