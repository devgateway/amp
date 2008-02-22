package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;


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
			  Directory idx = LuceneUtil.createIndex();
			  ampContext.setAttribute(Constants.LUCENE_INDEX, idx);
		  }
		  else{
			  Directory idx = (Directory) ampContext.getAttribute(Constants.LUCENE_INDEX);
			  if ("view".compareTo(request.getParameter("action")) == 0){
				  logger.info("VIEW!");
				  String field = request.getParameter("field");
				  String search = request.getParameter("search");

				  Hits hits = LuceneUtil.search(idx, field, search);
				  
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

					  LuceneUtil.deleteActivity(idx, field, search);
				  }
		  }
		  return mapping.findForward("forward");
	  }
}

