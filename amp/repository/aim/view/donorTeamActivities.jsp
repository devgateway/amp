<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${bcparams}" property="tId" value="-1"/>

<c:set target="${bcparams}" property="dest" value="teamLead"/>			





<script type="text/javascript">



<!--



	function checkall() {

		var selectbox = document.aimTeamActivitiesForm.checkAll;

		var items = document.aimTeamActivitiesForm.selActivities;

		if (document.aimTeamActivitiesForm.selActivities.checked == true || 

							 document.aimTeamActivitiesForm.selActivities.checked == false) {

				  document.aimTeamActivitiesForm.selActivities.checked = selectbox.checked;

		} else {

			for(i=0; i<items.length; i++){

				document.aimTeamActivitiesForm.selActivities[i].checked = selectbox.checked;

			}

		}

	}



	function validate() {

		if (document.aimTeamActivitiesForm.selActivities.checked != null) {

			if (document.aimTeamActivitiesForm.selActivities.checked == false) {

				alert("Please choose an activity to remove");

				return false;

			}				  

		} else {

			var length = document.aimTeamActivitiesForm.selActivities.length;	  

			var flag = 0;

			for (i = 0;i < length;i ++) {

				if (document.aimTeamActivitiesForm.selActivities[i].checked == true) {

					flag = 1;

					break;

				}

			}		



			if (flag == 0) {

				alert("Please choose an activity to remove");

				return false;					  

			}				  

		}

		return true;			  

	}



	function confirmDelete() {

		var valid = validate();

		document.aimTeamActivitiesForm.actionEvent.value="delete";

		

		if (valid == true) {

			var flag = confirm("<digi:trn>Are you sure you want to remove the selected activities</digi:trn>");

			if(flag == false)

				return false;

			else 

				return true;

		} else {

			return valid;

		}		  



	}

	



	function assignActivity() {

		dnrTeamId = document.aimTeamActivitiesForm.dnrTeamId.value;

		teamId = document.aimTeamActivitiesForm.teamId.value;

		<digi:context name="sel" property="context/module/moduleinstance/getDonorActivityList.do" />

		url = "<%= sel %>~dnrTeamId=" + dnrTeamId + "~teamId=" + teamId + "~type=U" ;

		document.aimTeamActivitiesForm.action = url;

		document.aimTeamActivitiesForm.submit();

	}



-->



</script>



<digi:instance property="aimTeamActivitiesForm" />

<digi:form action="/updateDonorActivities.do" method="post">



<html:hidden property="sort" />

<html:hidden property="sortOrder" />

<html:hidden property="page" />

<html:hidden property="dnrTeamId" />

<html:hidden property="teamId" />

<input type="hidden" name="actionEvent">



<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">

<tr><td width="100%" valign="top" align="left">

<jsp:include page="teamPagesHeader.jsp"  />

</td></tr>

<tr><td width="100%" valign="top" align="left">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg valign="top" width=750>



			<table cellPadding=5 cellspacing="0" width="100%">

				<tr>

					<td height=33><span class=crumb>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

						</c:set>

						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:portfolio">Portfolio</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;

						<c:set var="translation">

							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>

						</c:set>

						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;						

						<digi:trn key="aim:donorActivityList">Donor Activity List</digi:trn>

					</td>

				</tr>

				<tr>

					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>

					</td>

				</tr>

				<tr>

					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">

							<tr bgColor=#3754a1>

								<td vAlign="top" width="100%">

									<jsp:include page="teamSetupMenu.jsp"  />								

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td>&nbsp;

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">

									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="98%">	

										<tr><td>

											<digi:errors />

										</td></tr>

										<tr bgColor=#f4f4f2>

											<td bgColor=#f4f4f2>

												<table border="0" cellPadding=3 cellspacing="0" width="100%">

													<tr bgColor=#f4f4f2>

														<td bgColor=#c9c9c7 class=box-title>

															<digi:trn key="aim:activityListforTeam">

															Activity List for Team

															</digi:trn>

															<c:out value="${aimTeamActivitiesForm.teamName}" />

														</td>

													</tr>

												</table>

											</td>

										</tr>

										<tr>

											<td bgColor=#ffffff class=box-border valign="top">

												<table border="0" cellpadding="0" cellspacing="1" class=box-border-nopadding width="100%">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"

															bgcolor="#ffffff">

																<tr><td width=3 bgcolor="#dddddd">

																	<input type="checkbox" name="checkAll" onclick="checkall()">

																</td>

																<td width="20%" bgcolor="#dddddd">

																	<b><digi:trn key="aim:ampId">AMP ID</digi:trn></b>

																</td>																

																<td valign="center" align="center" bgcolor="#dddddd">

																	<b><digi:trn key="aim:activityList">Activity List</digi:trn></b>

																</td>

																<td bgColor=#dddddb align="center" width="20%" bgcolor="#dddddd">

																	<b><digi:trn key="aim:donors">Donors</digi:trn></b>

																</td></tr>

															</table>

														</td>

													</tr>

													<logic:empty name="aimTeamActivitiesForm" property="activities">

													<tr>

														<td align="center">

															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"

															bgcolor="#f4f4f2">

																<tr><td bgcolor="#f4f4f2" align="center">

																	<digi:trn key="aim:noActivitiesPresent">

																		No activities present

																	</digi:trn>

																</td></tr>

															</table>														

														</td>

													</tr>	

													<tr>

														<td align="center">

															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"

															bgcolor="#ffffff">

																<tr><td bgcolor="#ffffff" align="center">

																		<html:button  styleClass="dr-menu" property="submitButton" onclick="assignActivity()">
																			<digi:trn key="btn:addActivityToWorkspace">Add Activity To Workspace</digi:trn> 
																		</html:button>
																		

																</td></tr>

															</table>

														</td>

													</tr>	

													</logic:empty>

							

													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"

															bgcolor="#dddddd">

															<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 

															>

																<tr><td width=3 bgcolor="#f4f4f2">

																	<html:multibox property="selActivities">

																		<bean:write name="activities" property="ampActivityId" />

																	</html:multibox>

																</td>

																<td width="20%" bgcolor="#f4f4f2">

																	<bean:write name="activities" property="ampId" />

																</td>																

																<td bgcolor="#f4f4f2">

																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

																	<c:set target="${urlParams}" property="activityId">

																		<bean:write name="activities" property="ampActivityId" />

																	</c:set>

																	<c:set target="${urlParams}" property="pageId" value="3"/>

																	<c:set var="translation">

																		<digi:trn key="aim:clickToViewActivityDetails">

																		Click here to view Activity Details</digi:trn>

																	</c:set>

																	<digi:link href="/viewActivityPreview.do" name="urlParams" 

																	title="${translation}">

																		<bean:write name="activities" property="name" />

																	</digi:link>

																</td>

																<td align="left" width="20%" bgcolor="#f4f4f2">

																	<bean:write name="activities" property="donors" />

																</td></tr>

															</logic:iterate>

															</table>

														</td>

													</tr>													

													<tr>

														<td align="center" colspan=2>

															<table cellspacing="5">

																<tr>

																	<td>

																		<html:button  styleClass="dr-menu" property="submitButton" onclick="assignActivity()">
																			<digi:trn key="btn:addActivityToWorkspace">Add Activity To Workspace</digi:trn> 
																		</html:button>

																	</td>

																	<td>	

																		<html:submit  styleClass="dr-menu" property="submitButton"  onclick="return confirmDelete()">
																			<digi:trn key="btn:removeSelectedActivities">Remove selected activities</digi:trn> 
																		</html:submit>
																		

																	</td>

																</tr>

															</table>

														</td>

													</tr>

													</logic:notEmpty>

												</table>

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>

								&nbsp;

							</td></tr>

						</table>			

					</td>

				</tr>

			</table>

		</td>

	</tr>

</table>

</td></tr>

</table>

</digi:form>




