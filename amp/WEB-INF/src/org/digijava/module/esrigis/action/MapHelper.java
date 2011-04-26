package org.digijava.module.esrigis.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.ecs.xhtml.applet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.NewAddLocationForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.form.MapHelpForm;
import org.digijava.module.esrigis.helpers.ActivityPoint;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.QueryUtil;
import org.digijava.module.esrigis.helpers.SimpleLocation;
import org.digijava.module.visualization.form.VisualizationForm;

public class MapHelper extends MultiAction{
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
			MapHelpForm maphelperform = (MapHelpForm)form;
			MapFilter filter = maphelperform.getFilter(); 
			if (filter == null || !filter.isIsinitialized()){
				maphelperform.setFilter(QueryUtil.getNewFilter());
				maphelperform.getFilter().setWorkspaceOnly(true);
			}
			response.setContentType("text/json");
		return modeSelect(mapping, maphelperform, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
			if (request.getParameter("showactivities") != null){
				return modeShowActivities(mapping, form, request, response);
			}
		return null;
	}
	
	public ActionForward modeShowActivities(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		MapHelpForm maphelperform = (MapHelpForm)form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		
		JSONArray jsonArray = new JSONArray();
		 List<AmpActivity> list = new ArrayList<AmpActivity>();
		 list = DbHelper.getActivities(maphelperform.getFilter());
		 for (Iterator<AmpActivity> iterator = list.iterator(); iterator.hasNext();) {
			 ActivityPoint ap = new ActivityPoint();
			 AmpActivity aA = (AmpActivity) iterator.next();
			 ap.setActivityname(aA.getName());
			 
			ArrayList<SimpleLocation> sla = new ArrayList<SimpleLocation>();
			for (Iterator iterator2 =aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				SimpleLocation sl = new SimpleLocation();
				sl.setName(alocation.getLocation().getLocation().getName());
				sl.setGeoId(alocation.getLocation().getLocation().getCode());
				sl.setLat(alocation.getLocation().getLocation().getGsLat());
				sl.setLon(alocation.getLocation().getLocation().getGsLong());
				sl.setImplementation_location(alocation.getLocation().getLocation().getParentCategoryValue().getValue());
				//This has to be manage by the filter
				if (sl.getImplementation_location() != CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey()){
					sla.add(sl);
				}
			}
			
			ap.setLocations(sla);
			jsonArray.add(ap);
		}
	
		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();
		return null;
	}
}



