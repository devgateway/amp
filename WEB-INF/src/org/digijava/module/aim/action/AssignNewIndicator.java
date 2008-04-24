

package org.digijava.module.aim.action;

import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.digijava.module.aim.form.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.aim.dbentity.AmpActivity;

public class AssignNewIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        List<IndicatorsBean> allInds = new ArrayList();
        ThemeForm allIndForm = (ThemeForm) form;
        
        Long primaryConfigClassId=SectorUtil.getPrimaryConfigClassificationId();
        Collection allSectors = SectorUtil.getAllParentSectors(primaryConfigClassId);
        allIndForm.setAllSectors(allSectors);
        allIndForm.setTempNumResults(10);
        allIndForm.setSectorName("");
        allIndForm.setKeyword("");
        allIndForm.setPagedCol(null);
        
        
        
     
        return mapping.findForward("forward");
    }
}