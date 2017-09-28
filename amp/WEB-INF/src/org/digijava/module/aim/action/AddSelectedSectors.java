package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.SectorUtil;

public class AddSelectedSectors
    extends Action {

  private static Logger logger = Logger.getLogger(AddSelectedSectors.class);

  SelectSectorForm eaForm;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response) throws
      Exception {

      HttpSession session = request.getSession();  
      
    eaForm = (SelectSectorForm) form;
    List sectors=new ArrayList();
    ActionMessages errors = new ActionMessages();
    if(session.getAttribute("selectedSectorsForActivity")!=null){
        sectors=(List)session.getAttribute("selectedSectorsForActivity");
    }
  
    boolean isDuplicated = false;
    Collection<ActivitySector> sectr = new ArrayList();
    Long[] removedsectorId = (Long[]) session.getAttribute("removedSector"); 
   
    if(removedsectorId != null && eaForm.getCols() != null ){
     for(int i= 0;  i < removedsectorId.length; i++){
           checkDuplicateremove(removedsectorId[i]);
         }
    }
 
      if (eaForm.getCols() == null || eaForm.getCols().size() == 0) {
          eaForm.setCols(new ArrayList());
          eaForm.getCols().addAll(sectors);
      }
    Long configId=eaForm.getConfigId();
    AmpClassificationConfiguration config=null;
    Long selsearchedSector[] = eaForm.getSelSectors();
    logger.info("size off selected searched sectors: " +
                selsearchedSector.length);
    eaForm.setSomeError(false);
    if(configId!=null&&configId!=-1){
        config=SectorUtil.getClassificationConfigById(configId);
        if(!config.isMultisector()&&selsearchedSector.length>1){
           errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.aim.addActivity.sectorMultipleSelectionIsOff"));
          saveErrors(request, errors);
          eaForm.setSomeError(true);
          return mapping.findForward("forward");  
     }
    }    
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
                sctr.setSectorPercentage(FormatHelper.formatPercentage(new Float(100)));
              }
            }
            else if (eaForm.getCols().size() == 0) {
              if (selsearchedSector.length == 1) {
                sctr.setSectorPercentage(FormatHelper.formatPercentage(new Float(100)));
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
            sctr.setConfigId(configId);
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
    //  Sector dup = null;

    if (request.getParameter("addButton") != null && !isDuplicated) {
      session.setAttribute("sectorSelected", sectr);
      request.setAttribute("addButton", "true");
      session.setAttribute("add", "true");
    }
    else{
       
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.aim.sectorAlreadyAddedToActivity"));
          saveErrors(request, errors);
          eaForm.getCols().retainAll(sectors);
    }

    return mapping.findForward("forward");
  }

  public boolean checkDuplicateremove(Long id){
      Iterator itr = eaForm.getCols().iterator();
      ActivitySector sector;
       boolean flag = false;
      while (itr.hasNext()) {
          sector = (ActivitySector) itr.next();
          if(sector.getSubsectorLevel2Id().equals(id) || sector.getSubsectorLevel1Id().equals(id) || sector.getSectorId().equals(id)){
              eaForm.getCols().remove(sector);
              break;
          }
      }
      return true;
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
