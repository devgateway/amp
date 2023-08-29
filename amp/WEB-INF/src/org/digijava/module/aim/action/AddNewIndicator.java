package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class AddNewIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        NewIndicatorForm newIndForm = (NewIndicatorForm) form;

        String action=request.getParameter("action");

        if(action ==null ){
            newIndForm.reset();
            newIndForm.setActivitySectors(null);
        }
            
        if(action!=null && action.equalsIgnoreCase("add") && newIndForm.getName() != null &&  
             newIndForm.getCode() != null){
            
//          if(newIndForm.getIndicatorType() == 0 || newIndForm.getIndType() == 2){
                 AmpPrgIndicator newInd = new AmpPrgIndicator();
                 newInd.setCategory(newIndForm.getCategory());
                 newInd.setCode(newIndForm.getCode());
                 newInd.setCreationDate(newIndForm.getDate());
                 newInd.setDescription(newIndForm.getDescription());
                 newInd.setName(newIndForm.getName());                  
                           
                 newInd.setSector(newIndForm.getSelActivitySector());
                 
                 newInd.setIndSectores(newIndForm.getActivitySectors());
                    
                 if(newIndForm.getSelectedActivityId() == null && 
                        newIndForm.getActivitySectors() == null ){
                           newInd.setDefaultInd(true);
                           newInd.setType("global");
                          }
                     
                     
                 newInd.setSelectedActivity(newIndForm.getSelectedActivities());
                 //TODO INDIC IndicatorUtil.saveIndicators(newInd);
//               ProgramUtil.saveThemeIndicators(newInd, newIndForm.getSelectedProgramId());
                 //newIndForm.reset();
                 
                 //TODO INDIC  all "add" action above is depricated!!! because it is awful!!!!
                 AmpIndicator indicator = new AmpIndicator();
                 indicator.setName(newIndForm.getName());
                 indicator.setDescription(newIndForm.getDescription());
                 indicator.setCreationDate(new Date());
                 indicator.setCode(newIndForm.getCode());
                 indicator.setType(newIndForm.getType());
                 if (newIndForm.getActivitySectors()!=null && newIndForm.getActivitySectors().size()>0){
                     indicator.setSectors(new HashSet<AmpSector>());
                     for (Iterator sectorIt = newIndForm.getActivitySectors().iterator(); sectorIt.hasNext();) {
                         //This is awful! why is here AmpActivitySector???!!
                        ActivitySector actSector = (ActivitySector) sectorIt.next();
                        AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
                        sector.setAmpSectorId(actSector.getSectorId());
                        indicator.getSectors().add(sector);
                    }
                 }
                 
                 if (indicator.getSectors() == null
                    || indicator.getSectors() != null && indicator.getSectors().size() == 0) {
                     ActionMessages errors=new ActionMessages();
                     errors.add("noSector", new ActionMessage("error.aim.addIndicator.noSector"));
                     saveErrors(request, errors);
                     return mapping.findForward("forward");
                 } 
                 
               if(!IndicatorUtil.validateIndicatorName(indicator)){
                    IndicatorUtil.saveIndicator(indicator);
                    newIndForm.setAction("added");
                }
                else{
                    ActionMessages errors=new ActionMessages();
                    errors.add("title", new ActionMessage("error.aim.addIndicator.duplicateName"));
                    saveErrors(request, errors);
                    return mapping.findForward("forward");
                }                
                 
            }
            
        // AMP-2828 by mouhamad
        String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
        SimpleDateFormat sdf = null;
        //temporary workaround
        if (dateFormat!=null){
            //?????
            dateFormat = dateFormat.replace("m", "M");
            sdf = new SimpleDateFormat(dateFormat);
        }else{
            sdf= new SimpleDateFormat();
        }
        
        newIndForm.setDate(sdf.format(new Date()).toString());
      
        return mapping.findForward("forward");
    }
}
