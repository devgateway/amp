/**
 * SectorSelected.java
 *
 * @author Priyajith
 *//*

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.SectorUtil;

public class SectorSelected extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        SelectSectorForm eaForm = (SelectSectorForm) form;

        Long sector = eaForm.getSector();
        Long subsectorLevel1 = eaForm.getSubsectorLevel1();
        Long subsectorLevel2 = eaForm.getSubsectorLevel2();

        Collection prevSelSectors = eaForm.getActivitySectors();
        Collection newSectors = new ArrayList();

        if (prevSelSectors != null) {
            newSectors.addAll(prevSelSectors);
        }

        ActivitySector actSect = new ActivitySector();
        actSect.setSectorId(sector);
        AmpSector sec = SectorUtil.getAmpSector(actSect.getSectorId());
        actSect.setSectorName(sec.getName());
        actSect.setSubsectorLevel1Id(subsectorLevel1);
        actSect.setSubsectorLevel2Id(subsectorLevel2);
        
        if (actSect.getSectorName().equalsIgnoreCase("MULTISECTOR/CROSS-CUTTING")) {
            actSect.setSubsectorLevel1Id(null);
            actSect.setSubsectorLevel2Id(null);
        } else {
            actSect.setSubsectorLevel1Id(subsectorLevel1);
            actSect.setSubsectorLevel2Id(subsectorLevel2);
        }

        if (subsectorLevel2 != null && (!subsectorLevel2.equals(new Long(-1)))) {
            actSect.setId(subsectorLevel2);
        } else if (subsectorLevel1 != null
                && (!subsectorLevel1.equals(new Long(-1)))) {
            actSect.setId(subsectorLevel1);
        } else {
            actSect.setId(sector);
        }

        boolean flag = false;

        if (prevSelSectors != null) {
            Iterator itr = prevSelSectors.iterator();
            while (itr.hasNext()) {
                ActivitySector temp = (ActivitySector) itr.next();
                if (temp.equals(actSect)) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag && actSect.getSectorId() != null
                && (!(actSect.getSectorId().equals(new Long(-1))))) {
            if (actSect.getSubsectorLevel1Id() != null
                    && (!(actSect.getSubsectorLevel1Id().equals(new Long(-1))))) {
                sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel1Id());
                actSect.setSubsectorLevel1Name(sec.getName());
            }
            if (actSect.getSubsectorLevel2Id() != null
                    && (!(actSect.getSubsectorLevel2Id().equals(new Long(-1))))) {
                sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel2Id());
                actSect.setSubsectorLevel2Name(sec.getName());
            }

            if(prevSelSectors==null){
                actSect.setSectorPercentage(new Integer(100));
            }else if(prevSelSectors.size()==0){
                    actSect.setSectorPercentage(new Integer(100));
            }else if(prevSelSectors.size()==1){
                Iterator prevItr = prevSelSectors.iterator();
                while(prevItr.hasNext()) {
                    ActivitySector oldSect = (ActivitySector) prevItr.next();
//                        if(oldSect.getSectorPercentage().equals("100")){
//                            oldSect.setSectorPercentage(null);
//                        }
                    break;
                }
            }
            newSectors.add(actSect);
        }


        eaForm.setSelActivitySectors(null);
        eaForm.setActivitySectors(newSectors);

        return mapping.findForward("forward");

    }
}
*/
