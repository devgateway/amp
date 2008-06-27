package org.digijava.module.gis.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.gis.form.GisDemoForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.SectorRefCount;


public class ShowDemo extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        GisDemoForm GisDemoForm=(GisDemoForm)form;

        //List<AmpActivity> actList = ActivityUtil.getAllActivitiesList();


        List secData = DbUtil.getUsedSectors();

        List usedSectors = new ArrayList();
        Iterator it = secData.iterator();
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            SectorRefCount src = new SectorRefCount((AmpSector) obj[0], ((Integer)obj[1]).intValue());
            usedSectors.add(src);
        }

        GisDemoForm.setSectorCollection(usedSectors);

        return mapping.findForward("forward");
    }

}
