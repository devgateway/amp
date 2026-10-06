package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.AMPUncheckedException;
import org.dgfoundation.amp.error.ExceptionFactory;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.LuceneUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Test class for lucene
 * 
 * @author alexandru
 * @deprecated
 */
public class LuceneIndex extends Action {

      private static Logger logger = Logger.getLogger(LuceneIndex.class);

      private void testUnchecked3(){
          try {
              testUnchecked2();         
          } catch (Exception e) {
              AMPUncheckedException au = ExceptionFactory.newAMPUncheckedException(e);
              au.addTag("test");
              throw au;
          }
      }
      private void testUnchecked2(){
          testUnchecked();
      }
      private void testUnchecked() throws AMPUncheckedException{
          AMPUncheckedException aue = new AMPUncheckedException();
          aue.addTag("activity");
          throw aue;
      }
      
      public ActionForward execute(ActionMapping mapping,
                            ActionForm form,
                            HttpServletRequest request,
                            HttpServletResponse response) throws java.lang.Exception {

          HttpSession session = request.getSession();

          ServletContext ampContext = session.getServletContext();
          
          String action = request.getParameter("action");
          
          logger.info("lucene:" + action);
          if (action != null)
          if ("create".compareTo(action)== 0){
              LuceneUtil.checkActivityIndex(request.getSession().getServletContext());
          }
          else{
              if ("view".compareTo(action) == 0){
                  logger.info("VIEW!");
                  String field = request.getParameter("field");
                  String search = request.getParameter("search");

                  Document[] docs = LuceneUtil.search(LuceneUtil.ACTIVITY_INDEX_DIRECTORY, field, search);
                  for (Document doc : docs) {
                      AmpActivityVersion act = ActivityUtil.loadActivity(Long.parseLong(doc.get("id")));
                      logger.info(doc.get("id") + "[" + act.getAmpId() + "] " + act.getName());
                  }
                  
              } else if ("delete".compareTo(action) == 0) {
                      logger.info("DELETE!");
                      String field = request.getParameter("field");
                      String search = request.getParameter("search");

                      LuceneUtil.deleteEntry(request.getSession().getServletContext().getRealPath("/") + "/" + LuceneUtil.ACTIVITY_INDEX_DIRECTORY, field, search);
                  } else {
                      if ("checked".compareTo(action) == 0){
                          //first occurence of the error ... get's wrapped
                          AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, new Exception("simulated save activity error"));
                          ae.addTag("save");
                          
                          //wrap it on the activity level
                          AMPException a2 = new AMPException(ae);
                          a2.addTag("activity");
                          
                          //wrap again with a unexistang tag
                          AMPException a3 = new AMPException(a2);
                          a3.addTag("categories");
                          
                          throw a3;
                      }
                      else{
                          if ("unchecked".compareTo(action) == 0){
                              try {
                                  testUnchecked3();
                              } catch (Exception e) {
                                  throw e;
                              }
                              //throw new RuntimeException("manual test of unchecked exception");
                          }
                          else{
                              if ("random".compareTo(action) == 0){
                                  AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, false, new Exception("simulated random activity error " + System.currentTimeMillis()));
                                  throw ae;
                              }
                              else
                                  if ("confluence".compareTo(action) == 0){
                                  }
                          }
                              
                      }
                  }
          }
          return mapping.findForward("forward");
      }
}

