package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.DesktopForm;
import java.util.Collection;
import org.digijava.module.aim.dbentity.AmpModality;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.AmpSectors;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PreviewPrintDesktop
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        DesktopForm eaform=(DesktopForm)form;
        ArrayList newColl=new ArrayList();
        ArrayList oldColl=new ArrayList();

        ListIterator itr = null;
        if(eaform.getFltrDonor() != null) {
            oldColl=(ArrayList)eaform.getDonors();
            itr = oldColl.listIterator();
            while(itr.hasNext()) {
                for(int i = 0; i < eaform.getFltrSector().length; i++) {
                    if(itr.nextIndex()==eaform.getFltrDonor()[i]) {
                        AmpOrganisation org = (AmpOrganisation) itr.next();
                        newColl.add(org);
                    }
                }
            }
        }
        eaform.setSelDonors(newColl);
        newColl.clear();

        if(eaform.getFltrSector() != null) {
            oldColl=(ArrayList)eaform.getDonors();
            itr = oldColl.listIterator();
            while(itr.hasNext()) {
                for(int i = 0; i < eaform.getFltrSector().length; i++) {
                    if(itr.nextIndex()==eaform.getFltrSector()[i]) {
                        AmpSector sec = (AmpSector) itr.next();
                        newColl.add(sec);
                    }
                }
            }
        }
        eaform.setSelSectors(newColl);
        newColl.clear();

        if(eaform.getFltrStatus() != null) {
           oldColl=(ArrayList)eaform.getStatus();
           itr = oldColl.listIterator();
           while(itr.hasNext()) {
               for(int i = 0; i < eaform.getFltrStatus().length; i++) {
                   if(itr.nextIndex()==eaform.getFltrStatus()[i]) {
                       AmpStatus st = (AmpStatus) itr.next();
                       newColl.add(st);
                   }
               }
           }
       }
       eaform.setSelStatus(newColl);
       newColl.clear();

       return mapping.findForward("forward");
    }
}
