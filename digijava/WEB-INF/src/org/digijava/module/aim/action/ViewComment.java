/*
 * Created on 28/04/2005
 * @author akashs
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.EditActivityForm;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.Date;

public class ViewComment extends Action {

		  private static Logger logger = Logger.getLogger(ViewComment.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 logger.debug("In view comment");
					 
					 HttpSession session = request.getSession();

					 // if user is not logged in, forward him to the home page
					 if (session.getAttribute("currentMember") == null)
					 	return mapping.findForward("index");

					 EditActivityForm editForm = (EditActivityForm) form;
					 String action = editForm.getActionFlag();
					 String comment = request.getParameter("comment");
					 
					 TeamMember member = (TeamMember) request.getSession().getAttribute("currentMember");
					 
					 logger.debug("CommentFlag[before IF] : " + editForm.isCommentFlag());
					 if (comment != null && comment.trim().length() != 0) {
					 	AmpField field = null;
						ArrayList col = new ArrayList();
						if (editForm.isCommentFlag() == false) {
							editForm.setFieldName("current completion date");
							if (comment.equals("ccd") || comment.equals("viewccd"))
								field = DbUtil.getAmpFieldByName("current completion date");
							editForm.setField(field);
						}
						 	
						logger.debug("editForm.getCommentsCol().size() [At Start-I]: " + editForm.getCommentsCol().size());
						if (editForm.isEditAct() || comment.equals("viewccd")) {
							if (comment.equals("viewccd")) {
								String activityId = request.getParameter("actId");
								Long id = null;
								if (activityId != null && activityId.length() != 0)
									id = new Long(Integer.parseInt(activityId));
								col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(),id);
								editForm.setCommentsCol(col);
								editForm.setCommentFlag(false);
								//editForm.setSerializeFlag(true);
								return mapping.findForward("overview");
							}
							else if (editForm.isCommentFlag() == false) {
								col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(),editForm.getActivityId());
								editForm.setCommentsCol(col);
							}	
						}
						logger.debug("editForm.getCommentsCol().size() [At Start-II]: " + editForm.getCommentsCol().size());
						 	
						editForm.setActionFlag("create");
						editForm.setAmpCommentId(null);			 // Clearing the ampCommentId property
						if (editForm.getCommentText() != null)	// Clearing the commentText property
							editForm.setCommentText(null);
						//editForm.setSerializeFlag(false);  //To make sure comment(s) not added to database without user's knowledge
						if (comment.equals("ccd"))
							editForm.setCommentFlag(true);
						logger.debug("CommentFlag[forwarding] : " + editForm.isCommentFlag());
						return mapping.findForward("forward");
					}
					 
					 if ("create".equals(action)) {
					 	AmpComments com = new AmpComments();
						com.setAmpFieldId(editForm.getField());
						if (editForm.getCommentText().trim().equals("") || editForm.getCommentText() == null)
							com.setComment(" ");
						else
							com.setComment(editForm.getCommentText());
						com.setCommentDate(new Date());
						com.setMemberId(DbUtil.getAmpTeamMember(member.getMemberId()));
						 	
						editForm.getCommentsCol().add(com);  // for setting activityId in saveAvtivity.java
						logger.debug("editForm.getCommentsCol().size() [After Create]: " + editForm.getCommentsCol().size());
						editForm.setCommentText("");  	// Clear the commentText
						logger.debug("Comment added");
						return mapping.findForward("forward");
					} 
					 else if ("edit".equals(action)){						
						AmpComments com = (AmpComments) editForm.getCommentsCol().get(Integer.parseInt(request.getParameter("ampCommentId")));
							if (editForm.getCommentText() == null || editForm.getCommentText().trim().length() == 0) {
								logger.debug("Inside IF [EDIT]");
								editForm.setCommentText(com.getComment());
								return mapping.findForward("forward");
							 }
							 else {
							 	if (editForm.getCommentText().trim().equals("") || editForm.getCommentText() == null)
							 		com.setComment(" ");
							 	else
							 		com.setComment(editForm.getCommentText());
							 	
							 	editForm.getCommentsCol().set(Integer.parseInt(request.getParameter("ampCommentId")),com);  // for setting activityId in saveAvtivity.java
							 	logger.debug("editForm.getCommentsCol().size() [After Edit]: " + editForm.getCommentsCol().size());
							 	editForm.setCommentText("");  	// Clear the commentText
							 	editForm.setActionFlag("create");  // Clear the actionFlag
							 	logger.debug("Comment updated");
								return mapping.findForward("forward");
							 }
					    } 
					 	 else if ("delete".equals(action)){	
					 	 		editForm.getCommentsCol().remove(Integer.parseInt(request.getParameter("ampCommentId")));
					 	 		logger.debug("editForm.getCommentsCol().size() [After Delete]: " + editForm.getCommentsCol().size());
								editForm.setCommentText("");      // Clear the commentText
								editForm.setActionFlag("create");  // Clear the actionFlag
								logger.debug("Comment deleted");
								return mapping.findForward("forward");
							}
					 	
					 return mapping.findForward("index");
				}
}