package org.digijava.module.mondrian.action;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.exception.reportwizard.DuplicateReportNameException;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.digijava.module.mondrian.form.SaveHtmlForm;
import org.digijava.module.mondrian.form.ShowReportForm;
import org.digijava.module.mondrian.query.MoConstants;
import org.digijava.module.mondrian.query.QueryThread;

import com.tonbeller.jpivot.mondrian.MondrianMdxQuery;
import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.jpivot.tags.OlapModelProxy;

public class SaveHtml extends Action {

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		SaveHtmlForm sf = (SaveHtmlForm) form;
		
		if (sf.getAction()!= null && sf.getAction().equalsIgnoreCase("save")){
			HttpSession session = request.getSession();
			String action = sf.getAction();
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			String currentmdx = null;
			String measures = "";
			String dimensions = "";
			
			 OlapModelProxy omp = (OlapModelProxy) session.getAttribute("query01");
			if (omp != null) {
				MondrianModel mm  = (MondrianModel) omp.getDelegate().getRootModel();
				MondrianMdxQuery mdxQueryExt = (MondrianMdxQuery) omp.getExtension("mdxQuery");
				currentmdx = mdxQueryExt.getMdxQuery();
				
				String result = currentmdx;
				
				for (int i = 0; i < mm.getDimensions().length; i++) {
					Pattern p = Pattern.compile("\\["+mm.getDimensions()[i].getLabel()+"\\]");
					Matcher m = p.matcher(result);
					if (m.find()){
						if (dimensions.length()>0){
							dimensions = dimensions + "," + mm.getDimensions()[i].getLabel();
						}else{
							dimensions = mm.getDimensions()[i].getLabel();
						}
					}
				}
				for (int x = 0; x < mm.getMeasures().length; x++) {
					Pattern p = Pattern.compile("\\["+mm.getMeasures()[x].getLabel()+"\\]");
					Matcher m = p.matcher(result);
					if (m.find()){
						if (measures.length()>0){
							measures = measures + "," + mm.getMeasures()[x].getLabel();
						}else{
							measures = mm.getMeasures()[x].getLabel();
						}
					}
				}
			}
			if (EntityHelper.isDuplicated(sf.getReportname())){
				OffLineReports newreport = new OffLineReports();
				newreport.setName(sf.getReportname());
				newreport.setQuery(currentmdx);
				newreport.setTeamid(tm.getTeamId());
				newreport.setMeasures(measures);
				newreport.setColumns(dimensions);
				newreport.setCreationdate(new Timestamp(new java.util.Date().getTime()));
				newreport.setOwnerId(TeamUtil.getAmpTeamMember(tm.getMemberId()));
				EntityHelper.SaveReport(newreport);
			}else{
				session.setAttribute("DuplicateName", true);
				return mapping.findForward("report");
			}
			
			return mapping.findForward("mainreport");
			}
			else{
				return mapping.findForward("forward");
			}
	}
}
