package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.helper.ActivitySector;
import javax.servlet.http.HttpSession;

public class AddSelectedSectors
    extends Action {

  private static Logger logger = Logger.getLogger(AddSelectedLocations.class);

  SelectSectorForm eaForm;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response) throws
      Exception {

    eaForm = (SelectSectorForm) form;
    boolean isDuplicated = false;
    Collection<ActivitySector> sectr = new ArrayList();

    if (eaForm.getCols() == null) {
      eaForm.setCols(new ArrayList());
    }

    Long selsearchedSector[] = eaForm.getSelSectors();
    logger.info("size off selected searched sectors: " +
                selsearchedSector.length);
    Iterator itr = eaForm.getSearchedSectors().iterator();
    int count = 0;
    ActivitySector sctr = null;
    Long sectorId = null;

    while (itr.hasNext()) {
      sctr = (ActivitySector) itr.next();
      for (int i = 0; i < selsearchedSector.length; i++) {
        
        if(sctr.getSubsectorLevel2Id() == -1 && sctr.getSubsectorLevel1Id() != -1){
        	sectorId = sctr.getSubsectorLevel1Id();
        }else if(sctr.getSubsectorLevel1Id() == -1){
        	sectorId = sctr.getSectorId();
        }else{
        	sectorId = sctr.getSubsectorLevel2Id();
        }
        
        logger.info("getsectorid: " + sectorId +
                " selsearchedSector: " + selsearchedSector[i]);
 
        if (sectorId.equals(selsearchedSector[i])) {
          isDuplicated = checkDuplicate(sctr);
          if (!isDuplicated) {
            logger.info("adding now...");
            if (eaForm.getCols() == null) {
              if (selsearchedSector.length == 1) {
                sctr.setSectorPercentage(new Integer(100));
              }
            }
            else if (eaForm.getCols().size() == 0) {
              if (selsearchedSector.length == 1) {
                sctr.setSectorPercentage(new Integer(100));
              }
            }
            else if (eaForm.getCols().size() == 1) {
              Iterator sectItr = eaForm.getCols().iterator();
              while (sectItr.hasNext()) {
                ActivitySector oldSect = (ActivitySector) sectItr.next();
//                                    if(oldSect.getSectorPercentage().equals("100")){
//                                        oldSect.setSectorPercentage(null);
//                                    }
                break;
              }
            }
            eaForm.getCols().add(sctr);
            sectr.add(sctr);
            count++;
          }

        }

      }
      if (count == selsearchedSector.length)
        break;
    }

    //checking duplicates
    //	Sector dup = null;

    if (request.getParameter("addButton") != null && !isDuplicated) {
      HttpSession session = request.getSession();
      session.setAttribute("sectorSelected", sectr);
      request.setAttribute("addButton", "true");
    }

    return mapping.findForward("forward");
  }

  public boolean checkDuplicate(ActivitySector dup) {
    Iterator itr = eaForm.getCols().iterator();
    ActivitySector sector;
    boolean flag = false;
    while (itr.hasNext()) {
      sector = (ActivitySector) itr.next();
      if (sector.equals(dup)) {
        flag = true;
        logger.info("duplicate sector found....");
        break;
      }
      else
        flag = false;
    }
    return flag;
  }
}
