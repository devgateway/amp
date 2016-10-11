<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<digi:instance property="aimTeamActivitiesForm" />
<c:set var="translation">
	<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
</c:set>
<digi:link href="/admin.do" title="${translation}" >
	Admin Home Page
</digi:link><br><br>
<jsp:include page="workspaceManagerMenu.jsp"  />

<digi:form action="/removeTeamActivity.do" method="post">
<html:hidden property="activityId" />
<html:hidden property="teamId" />
<table width="100%" border="0">
	<tr>
		<td colspan="5">
			<table bgcolor="#006699" width ="100%">
				<tr>
					<td>
					<font color="#FFFFFF">
					<b>
						<digi:trn key="aim:removeActivityFrom">
						Remove Activity from </digi:trn>
						<bean:write name="aimTeamActivitiesForm" property="teamName" />	
					</b>
					</font>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		</td>
			<table width="100%" cellspacing="10" border="0">
				<tr>
					<td align="right">
						<digi:trn key="aim:activity">
						Activity 
						</digi:trn>
					</td>
					<td align="left">
						</b><bean:write name="aimTeamActivitiesForm" property="activityName" /></b>
					</td>
				</tr>
				<tr>
					<td align="right">
						<digi:trn key="aim:team">
						Team
						</digi:trn>
					</td>
					<td align="left">
						</b><bean:write name="aimTeamActivitiesForm" property="teamName" /></b>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<b>
						<digi:trn key="aim:confirmActivityRemovalFromTeam">
						Are you sure you want to remove this activity from the team ? 
						</digi:trn>
						</b>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<table cellspacing="5">
							<tr>
								<td>
									<html:submit value="Remove" />
								</td>
								<td>
									<html:reset value="Cancel"  onclick="javascript:history.go(-1)"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>	
		</td>
	</tr>
</table>
</digi:form>



