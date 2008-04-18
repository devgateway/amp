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





-->



</script>



<digi:instance property="aimTeamActivitiesForm" />



<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">

<tr><td width="100%" valign="top" align="left">

<jsp:include page="teamPagesHeader.jsp" flush="true" />

</td></tr>

<tr><td width="100%" valign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg vAlign=top width=750>



			<table cellPadding=5 cellSpacing=0 width="100%">

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

						<digi:trn key="aim:activityList">Activity List</digi:trn>

					</td>

				</tr>

				<tr>

					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>

					</td>

				</tr>

				<tr>

					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellPadding=0 cellSpacing=0  width="100%">

							<tr >

								<td vAlign="top" width="100%">

									<c:set var="selectedTab" value="6" scope="request"/>

									<jsp:include page="teamSetupMenu.jsp" flush="true" />								

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td>&nbsp;

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">

									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	

										<tr><td>

											<digi:errors />

										</td></tr>

										<tr bgColor=#f4f4f2>

											<td bgColor=#f4f4f2>

												<table border="0" cellPadding=0 cellSpacing=0 width=237>

													<tr bgColor=#f4f4f2>

														<td bgColor=#c9c9c7 class=box-title width=220>

															<digi:trn key="aim:monitoringAndEvaluation">

																Monitoring & Evaluation	

															</digi:trn>

														</td>

														<td background="module/aim/images/corner-r.gif" height="17" width=17>

														</td>

													</tr>

												</table>

											</td>

										</tr>

										<tr>

											<td bgColor=#ffffff class=box-border valign="top">

												<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"

															bgcolor="#ffffff">

																<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">

																	<b><digi:trn key="aim:activityList">Activity List</digi:trn></b>

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

													</logic:empty>

							

													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=1 cellPadding=6 vAlign="top" align="left" bgcolor="#f4f4f2">

															<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 

															type="org.digijava.module.aim.helper.Activity">

																<tr bgcolor="#f4f4f2">

																<td width="7" valign="center">

																	<img src= "../ampTemplate/images/arrow_dark.gif" border=0>

																</td>

																<td>

																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

																	<c:set target="${urlParams}" property="activityId">

																		<bean:write name="activities" property="activityId" />

																	</c:set>

																	<digi:link href="/getActivityIndicators.do" name="urlParams">

																		<bean:write name="activities" property="name" />

																	</digi:link>

																</td></tr>

															</logic:iterate>

															</table>

														</td>

													</tr>													

													</logic:notEmpty>

												</table>

											</td>

										</tr>

										<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>

										<logic:notEmpty name="aimTeamActivitiesForm" property="pages">

											<tr>

												<td>

													<digi:trn key="aim:pages">

														Pages :

													</digi:trn>

														<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" type="java.lang.Integer">

													  	<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />

														

														<% if (currPage.equals(pages)) { %>

																<%=pages%>

														<%	} else { %>

															<c:set var="translation">

																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>

															</c:set>

															<c:set target="${urlParams1}" property="page">

																<%=pages%>

															</c:set>

															<digi:link href="/getTeamActivities.do" name="urlParams1">

															<%=pages%></digi:link>

														<% } %>

														|&nbsp; 

													</logic:iterate>

												</td>

											</tr>

										</logic:notEmpty>											

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




