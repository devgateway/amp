package org.digijava.module.aim.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.store.Directory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.LuceneUtil;

/**
 * Test class for lucene
 * 
 * @author alexandru
 * @deprecated
 */
public class LuceneIndex extends Action {

	  private static Logger logger = Logger.getLogger(LuceneIndex.class);

	  public ActionForward execute(ActionMapping mapping,
			  				ActionForm form,
							HttpServletRequest request,
							HttpServletResponse response) throws java.lang.Exception {

		  HttpSession session = request.getSession();

		  ServletContext ampContext = session.getServletContext();
		  
		  logger.info("lucene:" + request.getParameter("action"));
		  if (request.getParameter("action") != null)
		  if ("create".compareTo(request.getParameter("action"))== 0){
			  LuceneUtil.checkIndex(request.getSession().getServletContext());
		  }
		  else{
			  if ("view".compareTo(request.getParameter("action")) == 0){
				  logger.info("VIEW!");
				  String field = request.getParameter("field");
				  String search = request.getParameter("search");

				  Hits hits = LuceneUtil.search(LuceneUtil.activityIndexDirectory, field, search);
				  for(int i = 0; i < hits.length(); i++) {
					   Document doc = hits.doc(i);
					   AmpActivity act = ActivityUtil.getAmpActivity(Long.parseLong(doc.get("id")));
					   logger.info(doc.get("id") + "[" + act.getAmpId() + "] " + act.getName());
				  }
				  
			  }
			  else
				  if ("delete".compareTo(request.getParameter("action")) == 0){
					  logger.info("DELETE!");
					  String field = request.getParameter("field");
					  String search = request.getParameter("search");

					  LuceneUtil.deleteActivity(request.getSession().getServletContext().getRealPath("/") + "/" + LuceneUtil.activityIndexDirectory, field, search);
				  }
		  }
		  return mapping.findForward("forward");
	  }
}

