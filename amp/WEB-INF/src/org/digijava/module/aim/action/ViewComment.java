/*
 * Created on 28/04/2005
 * @author akashs
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ViewComment extends Action {

		  private static Logger logger = Logger.getLogger(ViewComment.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 logger.debug("In view comment");
					 ActionMessages errors = new ActionMessages();

					 HttpSession session = request.getSession();

					 // if user is not logged in, forward him to the home page
					 if (session.getAttribute("currentMember") == null)
					 	return mapping.findForward("index");
					 HashMap commentColInSession;
					 if(session.getAttribute("commentColInSession")==null)
					 {
						 commentColInSession=new HashMap();
						 session.setAttribute("commentColInSession",commentColInSession);
					 }
					 else commentColInSession=(HashMap)session.getAttribute("commentColInSession");

					 EditActivityForm editForm = (EditActivityForm) form;
					 TeamMember member = (TeamMember) request.getSession().getAttribute("currentMember");
					 AmpTeam activityTeam= editForm.getIdentification().getTeam();
					 AmpTeam currentTeam=TeamUtil.getAmpTeam(member.getTeamId());
					 
					 if (currentTeam.getComputation() != null && !currentTeam.getComputation()){
							if (activityTeam != null && !currentTeam.getAmpTeamId().equals(activityTeam.getAmpTeamId())){
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.editActivity.noWritePermissionForUser"));
								saveErrors(request, errors);
								String url = "/aim/viewChannelOverview.do?ampActivityId="+ editForm.getActivityId() + "&tabIndex=0";
								RequestDispatcher rd = getServlet().getServletContext()
										.getRequestDispatcher(url);
								rd.forward(request, response);
								return null;	
							}
						}
					 
					 
					 if (editForm.getComments().getCommentsCol() == null) {
						 editForm.getComments().setCommentsCol( new ArrayList() );
					}
					 
					 
					 String action = editForm.getComments().getActionFlag();
					 if(action==null ||action.equals("") )
					 {
						 action="create";
						 editForm.getComments().setActionFlag("create");
					 }
					 String comment = request.getParameter("comment");
					 
					
					 logger.debug("CommentFlag[before IF] : " + editForm.getComments().isCommentFlag());
					 if (comment != null && comment.trim().length() != 0) {
					 	AmpField field = null;
						ArrayList col = new ArrayList();
						//if (editForm.getComments().isCommentFlag() == false) {
						if (comment.equals("ccd") || comment.equals("viewccd")){
							editForm.getComments().setFieldName("current completion date");
							field = DbUtil.getAmpFieldByName("current completion date");
							}
						else
							if (comment.equals("fdd") || comment.equals("viewfdd")){
								editForm.getComments().setFieldName("Final Date for Disbursements");
								field = DbUtil.getAmpFieldByName("Final Date for Disbursements");
							}
							else
								if (comment.equals("objAssumption")||comment.equals("viewobjAssumption")){
									editForm.getComments().setFieldName("Objective Assumption");
									field = DbUtil.getAmpFieldByName("Objective Assumption");
								}
								else
									if (comment.equals("objVerification")||comment.equals("viewobjVerification")){
										editForm.getComments().setFieldName("Objective Verification");
										field = DbUtil.getAmpFieldByName("Objective Verification");
									}
									else
										if (comment.equals("purpAssumption")||comment.equals("viewpurpAssumption")){
											editForm.getComments().setFieldName("Purpose Assumption");
											field = DbUtil.getAmpFieldByName("Purpose Assumption");
										}
										else
											if (comment.equals("purpVerification")||comment.equals("viewpurpVerification")){
												editForm.getComments().setFieldName("Purpose Verification");
												field = DbUtil.getAmpFieldByName("Purpose Verification");
											}else
												if (comment.equals("resAssumption")||comment.equals("viewresAssumption")){
													editForm.getComments().setFieldName("Results Assumption");
													field = DbUtil.getAmpFieldByName("Results Assumption");
												}
												else
													if (comment.equals("resVerification")||comment.equals("viewresVerification")){
														editForm.getComments().setFieldName("Results Verification");
														field = DbUtil.getAmpFieldByName("Results Verification");
													}
													else
														if (comment.equals("resObjVerIndicators")||comment.equals("viewresObjVerIndicators")){
															editForm.getComments().setFieldName("Results Objectively Verifiable Indicators");
															field = DbUtil.getAmpFieldByName("Results Objectively Verifiable Indicators");
														}
														else
															if (comment.equals("objObjVerIndicators")||comment.equals("viewObjObjVerIndicators")){
																editForm.getComments().setFieldName("Objective Objectively Verifiable Indicators");
																field = DbUtil.getAmpFieldByName("Objective Objectively Verifiable Indicators");
															}
															else
																if (comment.equals("purpObjVerIndicators")||comment.equals("viewpurpObjVerIndicators")){
																	editForm.getComments().setFieldName("Purpose Objectively Verifiable Indicators");
																	field = DbUtil.getAmpFieldByName("Purpose Objectively Verifiable Indicators");
																}
						editForm.getComments().setField(field);
							if (request.getParameter("previus")!=null && request.getParameter("previus").equalsIgnoreCase("vco")){
								editForm.getComments().setField(field);
								long actid = Long.parseLong(request.getParameter("actId"));
								editForm.getComments().setCommentsCol(DbUtil.getAllCommentsByField(editForm.getComments().getField().getAmpFieldId(), actid));
								return mapping.findForward("overview");
							}
						//}
						
						//logger.debug("editForm.getCommentsCol().size() [At Start-I]: " + editForm.getCommentsCol().size());
/*						if (editForm.isEditAct()) {
							if (comment.equals("viewccd")||comment.equals("ccd")||comment.equals("fdd")
									||comment.equals("viewobjAssumption")||comment.equals("objAssumption")||comment.equals("viewobjVerification")||comment.equals("objVerification")||comment.equals("viewobjObjVerIndicators")||comment.equals("objObjVerIndicators")
									||comment.equals("viewpurpAssumption")||comment.equals("purpAssumption")||comment.equals("viewpurpVerification")||comment.equals("purpVerification")||comment.equals("viewresObjVerIndicators")||comment.equals("resObjVerIndicators")
									||comment.equals("viewresAssumption")||comment.equals("resAssumption")||comment.equals("viewresVerification")||comment.equals("resVerification")||comment.equals("viewpurpObjVerIndicators")||comment.equals("purpObjVerIndicators")) {
								String activityId = request.getParameter("actId");
								Long id = null;
								if (activityId != null && activityId.length() != 0)
									id = new Long(Integer.parseInt(activityId));
								if (id == null)
									id = editForm.getActivityId();
								
							if ( !editForm.getCommentsLoadedFromDb().contains( editForm.getField().getAmpFieldId() ) ) {
								editForm.getCommentsLoadedFromDb().add( editForm.getField().getAmpFieldId() );
								col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(),id);
								if(!commentColInSession.isEmpty())	//if there was some comments added before in this session...
								{
									ArrayList colAux=new ArrayList();
									colAux.addAll(col);
									ArrayList colFromSession=(ArrayList)commentColInSession.get(editForm.getField().getAmpFieldId());
									if(colFromSession!=null)
										if(!colFromSession.isEmpty())
											for(Iterator it=colFromSession.iterator();it.hasNext();)
											{
												AmpComments ampCom=(AmpComments)it.next();
												boolean found=false;
												for(Iterator jt=colAux.iterator();jt.hasNext();)
												{
													AmpComments ampComAux=(AmpComments)jt.next();
													if( ampCom.getComment().equals(ampComAux.getComment()) && ampCom.getCommentDate().compareTo(ampComAux.getCommentDate())==0 )
													{
														found=true;
														continue;
													}
												}
												if(!found) col.add(ampCom);
											}
								}
							}
							else{
								col	= (ArrayList)commentColInSession.get( editForm.getField().getAmpFieldId() );
							}
								editForm.setCommentsCol(col);
								editForm.setCommentFlag(false);
								return mapping.findForward("forward");
							}
							else
								if (editForm.isCommentFlag() == false) {
                                    if(editForm.getField()!=null){
                                        col = DbUtil.getAllCommentsByField(editForm.getField().getAmpFieldId(), editForm.getActivityId());
                                        editForm.setCommentsCol(col);
                                    }
								}
						}*/
						logger.debug("editForm.getCommentsCol().size() [At Start-II]: " + editForm.getComments().getCommentsCol().size());

						editForm.getComments().setActionFlag("create");
						editForm.getComments().setAmpCommentId(null);			 // Clearing the ampCommentId property
						if (editForm.getComments().getCommentText() != null)	// Clearing the commentText property
							editForm.getComments().setCommentText(null);
						//editForm.setSerializeFlag(false);  //To make sure comment(s) not added to database without user's knowledge
						if (comment.equals("ccd")||comment.equals("fdd")||comment.equals("objAssumption")||comment.equals("objVerification")||comment.equals("purpAssumption")
								||comment.equals("purpVerification")||comment.equals("resAssumption")||comment.equals("resVerification"))
							editForm.getComments().setCommentFlag(true);
						logger.debug("CommentFlag[forwarding] : " + editForm.getComments().isCommentFlag());
						return mapping.findForward("forward");
					}

					 if ("create".equals(action)) {
					 	AmpComments com = new AmpComments();
						com.setAmpFieldId(editForm.getComments().getField());
						if (editForm.getComments().getCommentText().trim().equals("") || editForm.getComments().getCommentText() == null)
							com.setComment(" ");
						else
						com.setComment(editForm.getComments().getCommentText());
						com.setCommentDate(new Date());
                        AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(member.getMemberId());
						com.setMemberId(teamMember);
                        com.setMemberName(teamMember.getUser().getName());

						editForm.getComments().getCommentsCol().add(com);  // for setting activityId in saveAvtivity.java
					 	//commentColInSession.put(editForm.getField().getAmpFieldId(),editForm.getCommentsCol());
						putObjectInListInMap(commentColInSession, editForm.getComments().getField().getAmpFieldId(), com);
					 	session.setAttribute("commentColInSession",commentColInSession);
						logger.debug("editForm.getComments().getCommentsCol().size() [After Create]: " + editForm.getComments().getCommentsCol().size());
						editForm.getComments().setCommentText("");  	// Clear the commentText
						logger.debug("Comment added");
						return mapping.findForward("forward");
					}
					 else if ("edit".equals(action)){
						AmpComments com = (AmpComments) editForm.getComments().getCommentsCol().get(Integer.parseInt(request.getParameter("comments.ampCommentId")));
							if (editForm.getComments().getCommentText() == null || editForm.getComments().getCommentText().trim().length() == 0) {
								logger.debug("Inside IF [EDIT]");
								editForm.getComments().setCommentText(com.getComment());
								return mapping.findForward("forward");
							 }
							 else {
							 	if (editForm.getComments().getCommentText().trim().equals("") || editForm.getComments().getCommentText() == null)
							 		com.setComment(" ");
							 	else
							 		com.setComment(editForm.getComments().getCommentText());
							 	

							 	 AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(member.getMemberId());
							 	com.setMemberId(teamMember);
		                        com.setMemberName(teamMember.getUser().getName());
		                        com.setCommentDate(new Date());
							 	AmpComments replacedComment	= (AmpComments)editForm.getComments().getCommentsCol().set(
							 							Integer.parseInt(request.getParameter("comments.ampCommentId")),com
							 							);  // for setting activityId in saveAvtivity.java
							 	//commentColInSession.put(editForm.getComments().getField().getAmpFieldId(),editForm.getComments().getCommentsCol());
							 	List tempList				= (List)commentColInSession.get( editForm.getComments().getField().getAmpFieldId() );
							 	if (tempList != null && replacedComment != null){
				 	 				int index	= tempList.indexOf(replacedComment);
				 	 				if (index >= 0) {
				 	 					tempList.set(index, com);
				 	 				}
							 	}
							 	
							 	session.setAttribute("commentColInSession",commentColInSession);
							 	logger.debug("editForm.getComments().getCommentsCol().size() [After Edit]: " + editForm.getComments().getCommentsCol().size());
							 	editForm.getComments().setCommentText("");  	// Clear the commentText
							 	editForm.getComments().setActionFlag("create");  // Clear the actionFlag
							 	logger.debug("Comment updated");
								return mapping.findForward("forward");
							 }
					    }
					 	 else if ("delete".equals(action)){
					 	 		AmpComments removedComment	= (AmpComments)editForm.getComments().getCommentsCol().remove(
					 	 								editForm.getComments().getAmpCommentId().intValue());
							 	//commentColInSession.put(editForm.getComments().getField().getAmpFieldId(),editForm.getComments().getCommentsCol());
				 	 			List tempList				= (List)commentColInSession.get(editForm.getComments().getField().getAmpFieldId());
				 	 			if (tempList != null && removedComment != null)
					 	 				tempList.remove(removedComment);
							 	session.setAttribute("commentColInSession",commentColInSession);
					 	 		logger.debug("editForm.getComments().getCommentsCol().size() [After Delete]: " + editForm.getComments().getCommentsCol().size());
					 	 		editForm.getComments().setCommentText("");      // Clear the commentText
					 	 		editForm.getComments().setActionFlag("create");  // Clear the actionFlag
								logger.debug("Comment deleted");
								return mapping.findForward("forward");
							}
					return mapping.findForward("forward");
				}
		  
		@SuppressWarnings("unchecked")
		public static void putObjectInListInMap (Map map, Object key, Object value) {
			  List tempList	= (List)map.get(key);
			  if (tempList != null) {
				  tempList.add(value);
			  }
			  else {
				 tempList	= new ArrayList();
				 tempList.add(value);
				 map.put(key, tempList);
			  }
				  
		  }
}
