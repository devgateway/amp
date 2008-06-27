package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.GisDashboardForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.SectorRefCount;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpSector;

/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowGisDashboard extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GisDashboardForm gisForm=(GisDashboardForm)form;

    List secData = DbUtil.getUsedSectors();

        List usedSectors = new ArrayList();
        Iterator it = secData.iterator();
        while (it.hasNext()) {
            Object[] obj = (Object[])it.next();
            SectorRefCount src = new SectorRefCount((AmpSector) obj[0], ((Integer)obj[1]).intValue());
            usedSectors.add(src);
        }

        gisForm.setSectorCollection(usedSectors);


		return mapping.findForward("forward");
	}

}
