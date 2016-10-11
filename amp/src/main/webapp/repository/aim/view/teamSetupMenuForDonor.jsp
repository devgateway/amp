<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlParams}" property="tId" value="-1"/>

<c:set target="${urlParams}" property="dest" value="teamLead"/>			



<table width="750" cellpadding="0" cellspacing="1" align="left" valign="top">

	<tr><td height="20">

		<table bgColor=#ffffff align="left" valign="bottom" cellpadding="0" cellspacing="1" height="20">

			<tr height="20">

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>

					</c:set>

					<digi:link href="/workspaceOverview.do" name="urlParams" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:workspaceOverview">Workspace Overview</digi:trn>

					</digi:link>

				</td>											

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>

					</c:set>

					<digi:link href="/teamActivityList.do" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:activityList">Activity List</digi:trn>

					</digi:link>

				</td>										

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>

					</c:set>

					<digi:link href="/teamReportList.do" styleClass="sub-nav" title="${translation}" > 

						:: <digi:trn key="aim:reportList">Report List</digi:trn>

					</digi:link>

				</td>											

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>

					</c:set>

					<digi:link href="/relatedLinksList.do" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:relatedLinksList">Related Links List</digi:trn>

					</digi:link>

				</td>

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>

					</c:set>

					<digi:link href="/teamMemberList.do" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:members">Members</digi:trn>

					</digi:link>

				</td>

				<td class="sub-nav" width="112">

					&nbsp;

				</td>				

			</tr>

		</table>	

	</td></tr>

	<tr><td>

		<table bgcolor="#f4f4f2" align="left" valign="top" cellpadding="0" cellspacing="1">

			<tr>

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToCustomizeTeamSettings">Click here to Customize Team Settings</digi:trn>

					</c:set>

					<digi:link href="/defaultSettings.do" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:customizedTeamSettings">Customized Team Settings</digi:trn>

					</digi:link>

				</td>	

				<td noWrap>

					<c:set var="translation">

						<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Pages</digi:trn>

					</c:set>

					<digi:link href="/configureTeamPage.do" styleClass="sub-nav" title="${translation}" >

						:: <digi:trn key="aim:configureTeamPages">Configure Team Pages</digi:trn>

					</digi:link>

				</td>

				<td class="sub-nav" width="192">

					&nbsp;

				</td>

			</tr>

		</table>													

	</td></tr>

</table>




