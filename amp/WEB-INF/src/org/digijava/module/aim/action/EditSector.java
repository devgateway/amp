package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class EditSector extends Action {

          private static Logger logger = Logger.getLogger(GetSectors.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();

        
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
                     AddSectorForm editSectorForm = (AddSectorForm) form;
                     logger.debug("In edit sector action");
//                   logger.debug("In edit sector action");
                     if (request.getParameter("id") != null) {
                            String id = (String)request.getParameter("id");
                            Long secId = new Long(Long.parseLong(request.getParameter("id")));
                            String event = request.getParameter("event");
                            String flag = request.getParameter("flag");
                            if(request.getParameter("flag")==null)
                                flag = "false";
                            //////System.out.println(flag);
                            //////System.out.println("FLAG========================"+editSectorForm.getJspFlag());
                         if(flag.equalsIgnoreCase("false"))
                            {

                             
                                if(event!=null || !event.equals("") || event.length()<=0)
                                {
                                    if(event.equalsIgnoreCase("update2LevelSector"))
                                  {
                                        AmpSector ampSector = SectorUtil.getAmpSector(secId);
                                        
                                        Collection sectors = SectorUtil.getSectorLevel1(ampSector.getAmpSecSchemeId().getAmpSecSchemeId().intValue());
                                        if(checkSectorNameCodeIsNull(editSectorForm)){                              
                                            request.setAttribute("event", "view");
                                            ActionMessages errors = new ActionMessages();
                                            errors.add("title", new ActionMessage("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.")));
                                            if (errors.size() > 0)
                                            {
                                                saveErrors(request, errors);
                                            }
                                            refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
                                            return mapping.findForward("editedSecondLevelSector");
                                        }
                                        
                                        editSectorForm.setSectorId(secId);
                                        ampSector.setDescription(editSectorForm.getDescription());
                                        ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);
                                        String curSectorCodeOfficial = ampSector.getSectorCodeOfficial();
                                        String curName = ampSector.getName();
                                        ampSector.setName(editSectorForm.getSectorName());
                                        ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
                                        String secSchemeCode = ampSector.getAmpSecSchemeId().getSecSchemeCode();
                                        String secSchemename = ampSector.getAmpSecSchemeId().getSecSchemeName();
                                        
                                        if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
                                            request.setAttribute("event", "view");
                                            ActionMessages errors = new ActionMessages();
                                            errors.add("title", new ActionMessage("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title")));
                                            if (errors.size() > 0)
                                            {
                                                saveErrors(request, errors);
                                            }
                                            refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
                                            ampSector.setName(curName);
                                            return mapping.findForward("editedSecondLevelSector");
                                        }
                                        
                                        if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
                                            request.setAttribute("event", "view");
                                            ActionMessages errors = new ActionMessages();
                                            errors.add("title", new ActionMessage("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code")));
                                            if (errors.size() > 0)
                                            {
                                                saveErrors(request, errors);
                                            }
                                            refreshFirstLevelSectors(editSectorForm, sectors, ampSector);
                                            ampSector.setSectorCodeOfficial(curSectorCodeOfficial);
                                            return mapping.findForward("editedSecondLevelSector");
                                        }
                                        
                                        logger.debug("Updating.............................................");
                                        DbUtil.saveOrUpdateObject(ampSector);
                                        Long schemeId =ampSector.getAmpSecSchemeId().getAmpSecSchemeId();
                                        Integer schemeID = new Integer(schemeId.intValue());
                                        editSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(schemeID));
                                        editSectorForm.setSecSchemeId(schemeId);
                                        editSectorForm.setSecSchemeCode(secSchemeCode);
                                        editSectorForm.setSecSchemeName(secSchemename);
                                        editSectorForm.setParentId(schemeId);
                                        logger.debug(" update sector1 Complete");
                                        return mapping.findForward("editedSecondLevelSector");
                                }
                                else if(event.equalsIgnoreCase("update3LevelSector"))
                                 {
                                    AmpSector ampSector = SectorUtil.getAmpSector(secId);
                                    Collection sectors = SectorUtil.getAllChildSectors(ampSector.getParentSectorId().getAmpSectorId());
                                    if(checkSectorNameCodeIsNull(editSectorForm)){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                            //session.setAttribute("managingSchemes",errors);
                                        }
                                        refreshSubSectors(ampSector, sectors, editSectorForm);
                                        return mapping.findForward("editedThirdLevelSector");
                                    }
                                    
                                    editSectorForm.setSectorId(secId);
                                    ampSector.setDescription(editSectorForm.getDescription());
                                    ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);
                                    String curSectorCodeOfficial = ampSector.getSectorCodeOfficial();
                                    String curName = ampSector.getName();
                                    ampSector.setName(editSectorForm.getSectorName());
                                    ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
                                    
                                    if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                        }
                                        refreshSubSectors(ampSector, sectors, editSectorForm);
                                        ampSector.setName(curName);
                                        return mapping.findForward("editedThirdLevelSector");
                                    }
                                    
                                    if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                        }
                                        refreshSubSectors(ampSector, sectors,editSectorForm);
                                        ampSector.setSectorCodeOfficial(curSectorCodeOfficial);
                                        return mapping.findForward("editedThirdLevelSector");
                                    }
                                    
                                    logger.debug("Updating.............................................");
                                    DbUtil.update(ampSector);
                                    ampSector = SectorUtil.getAmpSector(secId);
                                    Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
                                    Integer schemeID = new Integer(sectorId.intValue());
                                    editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
                                    //DbUtil.update is not flushing the session. Also everywhere flush is commented
                                    //so when retrieving sector again with SectorUtil.getAllChildSectors(sectorId)
                                    //all data is present on the recently saved sector. We update the subSectors with the current values
                                    ArrayList <AmpSector> sectorList = new ArrayList <AmpSector> (editSectorForm.getSubSectors());
                                    for (int i = 0; i <sectorList.size();i++) {
                                        if (ampSector.getAmpSectorId().longValue() == sectorList.get(i).getAmpSectorId().longValue()) {
                                            sectorList.set(i, ampSector);
                                        }
                                    }
                                    editSectorForm.setSubSectors(sectorList);
                                    
                                    editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
                                    editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
                                    editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
                                    editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
                                    editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
                                    editSectorForm.setParentSectorId(ampSector.getParentSectorId().getAmpSectorId());
                                    editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
                                    logger.debug(" update sector2 Complete");
                                    return mapping.findForward("editedThirdLevelSector");
                                }
                     
                              }
                  
                            }
                         else if(flag.equalsIgnoreCase("true"))
                            {
                             if(event.equalsIgnoreCase("update3LevelSector"))
                                {
                                    AmpSector ampSector = SectorUtil.getAmpSector(secId);
                                    Collection sectors = SectorUtil.getAllChildSectors(ampSector.getParentSectorId().getAmpSectorId());
                                    if(checkSectorNameCodeIsNull(editSectorForm)){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the sector is empty. Please enter a title and a code for the sector.")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                            //session.setAttribute("managingSchemes",errors);
                                        }
                                        refreshSubSectors1(ampSector, sectors,editSectorForm);
                                        return mapping.findForward("editedThirdLevelSectorPlusOne");
                                    }
                                    editSectorForm.setSectorId(secId);
                                    ampSector.setDescription(editSectorForm.getDescription());
                                    ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);
                                    String curSectorCodeOfficial = ampSector.getSectorCodeOfficial();
                                    String curName = ampSector.getName();
                                    ampSector.setName(editSectorForm.getSectorName());
                                    ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
                                    
                                    if(existSectorForUpdate(editSectorForm,secId, sectors) == 1){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the sector already exist in database. Please enter another title")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                        }
                                        refreshSubSectors1(ampSector, sectors,editSectorForm);
                                        ampSector.setName(curName);
                                        return mapping.findForward("editedThirdLevelSectorPlusOne");
                                    }
                                    
                                    if(existSectorForUpdate(editSectorForm, secId, sectors) == 2){
                                        request.setAttribute("event", "view");
                                        ActionMessages errors = new ActionMessages();
                                        errors.add("title", new ActionMessage("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the sector already exist in database. Please enter another code")));
                                        if (errors.size() > 0)
                                        {
                                            saveErrors(request, errors);
                                        }
                                        refreshSubSectors1(ampSector, sectors,editSectorForm);
                                        ampSector.setSectorCodeOfficial(curSectorCodeOfficial);
                                        return mapping.findForward("editedThirdLevelSectorPlusOne");
                                    }
                                    
                                    logger.debug("Updating.............................................");
                                    DbUtil.update(ampSector);
                                    ampSector = SectorUtil.getAmpSector(secId);
                                    Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
                                    Integer schemeID = new Integer(sectorId.intValue());
                                    editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
                                    //DbUtil.update is not flushing the session. Also everywhere flush is commented
                                    //so when retrieving sector again with SectorUtil.getAllChildSectors(sectorId)
                                    //all data is present on the recently saved sector. We update the subSectors with the current values
                                    ArrayList <AmpSector> sectorList = new ArrayList <AmpSector> (editSectorForm.getSubSectors());
                                    for (int i = 0; i <sectorList.size();i++) {
                                        if (ampSector.getAmpSectorId().longValue() == sectorList.get(i).getAmpSectorId().longValue()) {
                                            sectorList.set(i, ampSector);
                                        }
                                    }
                                    editSectorForm.setSubSectors(sectorList);
                            
                                    editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
                                    editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
                                    editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
                                    editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
                                    editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
                                    editSectorForm.setParentSectorId(ampSector.getParentSectorId().getAmpSectorId());
                                    editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
                                    logger.debug(" update sector3 Complete");
                                    editSectorForm.setJspFlag(false);
                                    return mapping.findForward("editedThirdLevelSectorPlusOne");
                         }
                         }
                    }
                     return null;
          }
          
          
          private void refreshSubSectors1(AmpSector ampSector,Collection sectors, AddSectorForm editSectorForm) {
            // TODO Auto-generated method stub
              editSectorForm.setSubSectors(sectors);
                editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
                editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
                editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
                editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
                editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
                editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
                logger.debug(" update sector3 Complete");
                editSectorForm.setJspFlag(false);
        }


        private void refreshSubSectors(AmpSector ampSector, Collection sectors, AddSectorForm editSectorForm) {
            // TODO Auto-generated method stub
              editSectorForm.setSubSectors(sectors);
                editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
                editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
                editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
                editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
                editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
                editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
        }


        private void refreshFirstLevelSectors(AddSectorForm editSectorForm, Collection sectors, AmpSector ampSector) {
            // TODO Auto-generated method stub
              String secSchemeCode = ampSector.getAmpSecSchemeId().getSecSchemeCode();
              String secSchemename = ampSector.getAmpSecSchemeId().getSecSchemeName();
              editSectorForm.setFormFirstLevelSectors(sectors);
              editSectorForm.setSecSchemeCode(secSchemeCode);
              editSectorForm.setSecSchemeName(secSchemename);
        }


        private boolean checkSectorNameCodeIsNull(AddSectorForm sectorsForm){
                if(sectorsForm.getSectorCodeOfficial() == null || sectorsForm.getSectorName() == null ||
                        "".equals(sectorsForm.getSectorCodeOfficial()) || "".equals(sectorsForm.getSectorName()) )
                    return true;
                return false;
            }
            
            private int existSector (AddSectorForm sectorsForm, Collection sectors){
                for (Iterator it = sectors.iterator(); it.hasNext();) {
                    AmpSector sector = (AmpSector) it.next();
                    if(sector.getName() != null && sectorsForm.getSectorName().equals(sector.getName())) return 1;
                    if(sector.getSectorCode() != null && sectorsForm.getSectorCodeOfficial().equals(sector.getSectorCode())) return 2;
                }
                return 0;
            }
            
            private int existSectorForUpdate (AddSectorForm sectorsForm, Long Id, Collection sectors){
                for (Iterator it = sectors.iterator(); it.hasNext();) {
                    AmpSector sector = (AmpSector) it.next();
                    if(!Id.equals(sector.getAmpSectorId())){
                        if( sector.getName() != null && sectorsForm.getSectorName().equals(sector.getName()) ) 
                            return 1;
                        if( sector.getSectorCodeOfficial() != null && sectorsForm.getSectorCodeOfficial().equals(sector.getSectorCodeOfficial()) ) 
                            return 2;
                    }
                }
                return 0;
            }
          
}

