/*
 *   CMSManager.java
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
package org.digijava.module.cms.util;

import org.digijava.module.cms.dbentity.CMS;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.List;
import java.util.Iterator;
import org.digijava.module.cms.dbentity.CMSCategory;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import org.digijava.module.cms.exception.CMSException;

public class CMSManager {
  public static CMS getCMS(ModuleInstance moduleInstance) throws CMSException {
    CMS retVal = null;
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
    if (siteId != null && siteId.length() > 0 &&
        instanceId != null && instanceId.length() > 0) {
      retVal = DbUtil.getCMSItem(siteId, instanceId);
    }
    return retVal;
  }

  public static String getCategoryTreeAsXml (List rootNodes){
    StringBuffer retVal = new StringBuffer ();
    retVal.append("<root>");
    retVal.append(processTree (rootNodes, null));
    retVal.append("</root>");

    return retVal.toString();
  }

  private static String processTree (List rootNodes, CMSCategory parent){
    sortCategoryList(rootNodes);
    StringBuffer src = new StringBuffer();
    if (rootNodes != null && !rootNodes.isEmpty()) {
      Iterator it = rootNodes.iterator();
      while (it.hasNext()) {
        CMSCategory category = (CMSCategory) it.next();

        if (category.getPrimaryParent()==null && parent != null) {
          src.append("<top-copy");
        } else if (category.getPrimaryParent()==null){
                    src.append("<top-level");
        } else if (parent != null && category.getPrimaryParent()!=null &&
                  parent.getId() == category.getPrimaryParent().getId()) {
          src.append("<primary");
        } else {
          src.append("<copy");
        }

        src.append(" name=\"");
        src.append(category.getName());
        src.append("\"");

        src.append(" id=\"");
        src.append(category.getId());
        src.append("\"");

        src.append(" parId=\"");
        if (parent != null){
          src.append(parent.getId());
        } else {
          src.append("0");
        }
        src.append("\"");


        if (category.getChildCategories().isEmpty() &&
            category.getPrimaryChildCategories().isEmpty()) {
            src.append("/");

        }
        src.append(">");


        if (!category.getChildCategories().isEmpty()) {
          src.append(processTree (new ArrayList(category.getChildCategories()), category));
        }
        if (!category.getPrimaryChildCategories().isEmpty()) {
          src.append(processTree (new ArrayList(category.getPrimaryChildCategories()), category));
        }


        if (!category.getChildCategories().isEmpty() ||
            !category.getPrimaryChildCategories().isEmpty()) {
          if (category.getPrimaryParent()==null) {
            src.append("</top-level>");
          }
          else if (parent != null && category.getPrimaryParent() != null &&
                   parent.getId() == category.getPrimaryParent().getId()) {
            src.append("</primary>");
          }
          else {
            src.append("</copy>");
          }

        }

      }
    }
    return src.toString();
  }

  public static void sortCategoryList (List categories) {
    Comparator categoryComparator = new Comparator(){
      public int compare (Object o1, Object o2) {
        CMSCategory cat1 = (CMSCategory) o1;
        CMSCategory cat2 = (CMSCategory) o2;
        return cat1.getName().compareTo(cat2.getName());
      }
    };
    Collections.sort(categories, categoryComparator);
  }

  public static boolean removeCategoryFromSet (Set categorySet,
                                         CMSCategory category){
    boolean result = false;
    Iterator it = categorySet.iterator();
    while (it.hasNext()) {
      CMSCategory categoryIt = (CMSCategory) it.next();
      if (categoryIt.getId() == category.getId()) {
        it.remove();
        result = true;
        break;
      }
    }

    return result;
  }

  public static String[] getListAsStringArray (List list) {
    String[] retVal = null;
    if (list != null) {
      retVal = new String[list.size()];
      for (int index = 0; index < list.size(); index ++) {
        retVal[index] = (String) list.get(index);
      }
    }
    return retVal;
  }

  public static List getCategoryBradcrump (CMSCategory category) {
    List retVal = new ArrayList();
    do {
      CMSBradcrampItem item = new CMSBradcrampItem();
      item.setItemName(category.getName());
      item.setItemId(category.getId());
      retVal.add(item);
      if (category.getPrimaryParent() != null) {
        category = category.getPrimaryParent();
      } else {
        break;
      }
    } while (category != null);
    Collections.reverse(retVal);
    return retVal;
  }
}