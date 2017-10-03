package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.SectorClassConfigForm;
import org.digijava.module.aim.util.SectorUtil;

public class ManageClassificationConfig extends Action {

    private static Logger logger = Logger.getLogger(UpdateSectorSchemes.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {


        SectorClassConfigForm configForm = (SectorClassConfigForm) form;
        configForm.setClassifications(new ArrayList(SectorUtil.getSectorSchemes()));
        String event = configForm.getEvent();
        if (event != null && (event.equals("edit") || event.equals("add"))) {
            Long id = configForm.getId();
            if (id != null) {
                AmpClassificationConfiguration config = SectorUtil.getClassificationConfigById(id);
                configForm.setConfigName(config.getName());
                
                if (config.getClassification() != null) {
                    configForm.setSectorClassId(config.getClassification().getAmpSecSchemeId());
                }
                
                Long multiSelect = 1l;
                if (!config.isMultisector()) {
                    multiSelect = 2l;  //  multi sectors option is off
                }
                configForm.setMultiSectorSelecting(multiSelect);
            }
            return mapping.findForward("edit");
        }
        
        if (event != null && event.equals("save")) {
                Long configId = configForm.getId();
                String configName = configForm.getConfigName();
                String configDescription = configForm.getConfigDescription();
                //check for duplication
                int calCount = SectorUtil.getClassificationConfigCount(configName, configId);
                if(calCount>0){
                    ActionMessages errors= new ActionMessages();
                    errors.add("classification config not unique", new ActionMessage("admin.clasConfig.calExists",TranslatorWorker.translateText("Classification Config with the given email already exists") ));
                    saveErrors(request, errors);
                    return mapping.findForward("edit");
                }
                
                
                boolean multiSector = false;
                Long sectorClassId = configForm.getSectorClassId();
                AmpSectorScheme classification = SectorUtil.getAmpSectorScheme(sectorClassId);
                // checking whether multi sectors option is selected
                if (configForm.getMultiSectorSelecting().equals(1l)) {
                    multiSector = true;
                }
                
                SectorUtil.saveClassificationConfig(configId, configName, configDescription, multiSector, classification);
             

            } else {
                configForm.setClassificationConfigs(SectorUtil.getAllClassificationConfigs());

            }
        if (event != null && event.equals("delete")) {
            SectorUtil.deleteClassification(configForm.getId());
        }

        return mapping.findForward("manageClassifications");

    }
}
