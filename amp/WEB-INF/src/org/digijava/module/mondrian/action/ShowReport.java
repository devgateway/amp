package org.digijava.module.mondrian.action;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import mondrian.olap.MondrianProperties;

import org.apache.ecs.xhtml.object;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.digijava.module.mondrian.form.ShowReportForm;
import org.eigenbase.util.property.BooleanProperty;
import org.exolab.castor.xml.Marshaller;

import com.tonbeller.jpivot.mondrian.MondrianMdxQuery;
import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.jpivot.tags.MondrianModelFactory;
import com.tonbeller.jpivot.tags.OlapModelProxy;
import com.tonbeller.wcf.bookmarks.BookmarkManager;
import com.tonbeller.wcf.bookmarks.Bookmarkable;

public class ShowReport extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		ShowReportForm tf = (ShowReportForm) form;
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		String currentmdx = null;
		String id = request.getParameter("id");
		if (id!=null){
			OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			session.setAttribute("querystring", report.getQuery());
		}
		//request.getParameterMap().put("pagename", "query");
		
		/*
		 OlapModelProxy omp = (OlapModelProxy) session.getAttribute("query01");
		if (omp != null) {
			MondrianModel mm  = (MondrianModel) omp.getDelegate().getRootModel();
			MondrianMdxQuery mdxQueryExt = (MondrianMdxQuery) omp.getExtension("mdxQuery");
			currentmdx = mdxQueryExt.getMdxQuery();
			}
		else{
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream("storedQuery.xml")));
			HashMap stater = (HashMap) d.readObject();
			d.close();  
			Bookmarkable attrquery = null;
			Bookmarkable attrnavi = null;
			
			session.setAttribute("query01", attrquery);
			session.setAttribute("navi01", attrnavi);
			BookmarkManager.instance(session).restoreSessionState(stater);
		}
		if (action != null && action.equalsIgnoreCase("save")) {
			Object state = BookmarkManager.instance(session).collectSessionState(Bookmarkable.EXTENSIONAL);
			JAXBContext context = JAXBContext.newInstance(state.getClass());
			javax.xml.bind.Marshaller m = context.createMarshaller();
			m.marshal(state,System.out);			
			
			XMLEncoder e = new XMLEncoder(new FileOutputStream("storedQuery.xml"));
			e.writeObject(state);
			e.close();
			
			OffLineReports newreport = new OffLineReports();
			newreport.setName(tf.getReportname());
			newreport.setQuery(currentmdx);
			newreport.setTeamid(tm.getTeamId());
			newreport.setOwnerId(TeamUtil.getAmpTeamMember(tm.getMemberId()));
			EntityHelper.SaveReport(newreport);
			
		}
		*/
		return mapping.findForward("forward");
	}
}
