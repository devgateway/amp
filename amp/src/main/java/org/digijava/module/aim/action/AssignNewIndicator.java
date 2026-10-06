package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AssignNewIndicator extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        List<IndicatorsBean> allInds = new ArrayList();
        ThemeForm allIndForm = (ThemeForm) form;
        
        Long primaryConfigClassId=SectorUtil.getPrimaryConfigClassificationId();
        Collection<AmpSector> allSectors = SectorUtil.getAllParentSectors(primaryConfigClassId);
        allIndForm.setAllSectors(allSectors);
        allIndForm.setTempNumResults(10);
        allIndForm.setSectorName("");
        allIndForm.setKeyword("");
        allIndForm.setPagedCol(null);
        allIndForm.setSelectedindicatorFromPages(1);
     
        return mapping.findForward("forward");
    }
}
