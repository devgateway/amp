package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.util.SectorUtil;
import javax.servlet.http.*;
import java.util.*;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.SectorClassConfigForm;

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
                configForm.setSectorClassId(config.getClassification().getAmpSecSchemeId());
                Long multiSelect = 1l;
                if (!config.isMultisector()) {
                    multiSelect = 2l;  //  multi sectors option is off
                }
                configForm.setMultiSectorSelecting(multiSelect);
            }
            return mapping.findForward("edit");
        } else {

            if (event != null && event.equals("save")) {
                Long configId = configForm.getId();
                String configName = configForm.getConfigName();
                boolean multiSector = false;
                Long sectorClassId = configForm.getSectorClassId();
                AmpSectorScheme classification = SectorUtil.getAmpSectorScheme(sectorClassId);
                // checking whether multi sectors option is selected
                if (configForm.getMultiSectorSelecting().equals(1l)) {
                    multiSector = true;
                }
                
                SectorUtil.saveClassificationConfig(configId, configName, multiSector, classification);
             

            } else {
                configForm.setClassificationConfigs(SectorUtil.getAllClassificationConfigs());

            }
        }

        return mapping.findForward("manageClassifications");

    }
}
