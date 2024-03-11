<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${bcparams}" property="tId" value="-1"/>

<c:set target="${bcparams}" property="dest" value="teamLead"/>



<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>



<digi:instance property="aimDonorTeamsForm" />



<table cellspacing="0" cellpadding="0" vAlign="top" align="left" width="100%">

<tr><td width="100%">

<jsp:include page="teamPagesHeader.jsp"  />

</td></tr>

<tr><td>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=780>

	<tr>

		<td width=14>&nbsp;</td>

		<td align=left valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">

				<tr>

					<td height=33><span class=crumb>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

						</c:set>

						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

						<digi:trn key="aim:portfolio">

						Portfolio

						</digi:trn>

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

					<td height=16 valign="center" width=571>

						<span class=subtitle-blue>

							<digi:trn key="aim:teamWorkspaceSetup">

								Team Workspace Setup 

							</digi:trn>

						</span>

					</td>

				</tr>

				<tr>

					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" 

						valign="top" align="left">

							<tr >

								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="2" scope="request"/>
									<c:set var="selectedSubTab" value="1" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp"  />
								</td>

							</tr>


							<tr bgColor=#f4f4f2>

								<td valign="top">
                                	<div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<div align="center">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="98%">	
										<tr>

											<td bgColor=#ffffff >

												<table border="0" cellPadding=3 cellspacing="0"  width="100%">

													<tr bgColor=#dddddb>

														<td bgColor=#999999 colspan="2" align="center" style="color:black">

															<b><digi:trn key="aim:donorTeam">Donor Team</digi:trn></b>

														</td>

														<td bgColor=#999999 colspan="2" align="center" style="color:black">

															<b><digi:trn key="aim:donorTeamLeader">Team Leader</digi:trn></b>

														</td>														

													</tr>

													<c:if test="${aimDonorTeamsForm.donorTeams != null}">

													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

														<c:forEach var="donorTeams" items="${aimDonorTeamsForm.donorTeams}">	

															<tr bgColor=#f4f4f2>

																<td colspan="2" align="left">

																	<c:set target="${urlParams}" property="dnrTeamId">

																		<c:out value="${donorTeams.teamId}" />

																	</c:set>

																	<c:set target="${urlParams}" property="teamId">

																		<c:out value="${aimDonorTeamsForm.teamId}" />

																	</c:set>

																	<c:set target="${urlParams}" property="type" value="A"/>

																	<digi:link href="/getDonorActivityList.do" name="urlParams">

																	<c:out value="${donorTeams.teamName}" /></digi:link>

																</td>

																<td colspan="2" align="left">

																	<c:out value="${donorTeams.teamMemberName}" />

																</td>

															</tr>

														</c:forEach>

													</c:if>

												</table>

											</td>

										</tr>

									</table>
									</div>
                                    </div>
								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>&nbsp;

								

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




