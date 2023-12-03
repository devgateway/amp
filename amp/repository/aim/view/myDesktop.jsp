<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>


<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<digi:context name="digiContext" property="context" />



<script type="text/javascript">



	function newWin(val) {

		<digi:context name="selectLoc" property="context/ampModule/moduleinstance/viewOrganisation.do" />

		var url = "<%= selectLoc %>?ampOrgId=" + val;

		openNewWindow(635, 600);

		//obj.target = popupPointer.name;

		popupPointer.document.location.href = url;

		//obj.href = url;

	}



    function perviewPrint(val) {

        <digi:context name="printLoc" property="context/ampModule/moduleinstance/previewPrintMyDesktop.do" />

        openNewWindowWithMenubar(screen.width,screen.height);

        document.aimDesktopForm.target=popupPointer.name;

        document.aimDesktopForm.action ="<%=printLoc%>";

		document.aimDesktopForm.submit();

	}



	function searchActivity() {

		<digi:context name="url" property="context/ampModule/moduleinstance/searchDesktopActivities.do" />

		document.aimDesktopForm.action = "<%= url %>";

		document.aimDesktopForm.submit();

	}



	function resetFilters() {

		document.aimDesktopForm.resetFliters.value = "true";

	}



</script>



<digi:errors/>



<digi:form action="/filterDesktopActivities.do" method="POST">



<html:hidden name="aimDesktopForm" property="resetFliters" value="false"/>



<TABLE width="99%" cellspacing="1" cellpadding="4" valign="top" align="center">



	<TR><TD>

		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" >

      	<TR><TD>

           	<TABLE border="0" cellpadding="0" cellspacing="0" >

           		<TR>

              		<TD bgColor=#c9c9c7 class=box-title>

							&nbsp;<digi:trn key="aim:portfolio">Portfolio</digi:trn>

						</TD>

                 	<TD background="ampModule/aim/images/corner-r.gif"

						height=17 width=17></TD>

					</TR>

				</TABLE>

			</TD></TR>

			<TR><TD bgColor=#ffffff class=box-border align=left>

				<TABLE border="0" cellPadding=4 cellspacing="1" width="100%" >

				<c:if test="${aimDesktopForm.filtersPresent == true}">

					<TR><TD>

					<!-- Filters -->



						<c:if test="${!empty aimDesktopForm.sectors}">

							<html:select property="fltrSector" styleClass="inp-text" multiple="true" size="3">

								<html:option value="-1">--All--</html:option>

								<html:optionsCollection property="sectors" value="ampSectorId" label="name" />

							</html:select>

						</c:if>



						<c:if test="${!empty aimDesktopForm.donors}">

							<html:select property="fltrDonor" styleClass="inp-text" multiple="true" size="3">

								<html:option value="-1">--All--</html:option>

								<html:optionsCollection property="donors" value="ampOrgId" label="acronym"/>

							</html:select>

						</c:if>

						<c:if test="${!empty aimDesktopForm.status}">

							<html:select property="fltrStatus" styleClass="inp-text" multiple="true" size="3">

								<html:option value="-1">--All--</html:option>

								<html:optionsCollection property="status" value="ampStatusId" label="name" />

							</html:select>

						</c:if>	<br>



						<c:if test="${!empty aimDesktopForm.calendars}">

							<html:select property="fltrCalendar" styleClass="inp-text">

								<html:optionsCollection property="calendars" value="ampFiscalCalId" label="name"/>

							</html:select>

							<html:select property="fltrFrmYear" styleClass="inp-text">

								<html:option value="0">From Year</html:option>

								<c:forEach var="yrs" items="${aimDesktopForm.yearRange}">

									<bean:define id="yr">

										<c:out value="${yrs}"/>

									</bean:define>

									<html:option value="<%=yr%>"><c:out value="${yrs}"/></html:option>

								</c:forEach>

							</html:select>

							<html:select property="fltrToYear" styleClass="inp-text">

								<html:option value="0">To Year</html:option>

								<c:forEach var="yrs" items="${aimDesktopForm.yearRange}">

									<bean:define id="yr">

										<c:out value="${yrs}"/>

									</bean:define>

									<html:option value="<%=yr%>"><c:out value="${yrs}"/></html:option>

								</c:forEach>

							</html:select>

						</c:if>



						<c:if test="${!empty aimDesktopForm.currencies}">

							<html:select property="fltrCurrency" styleClass="inp-text">

								<html:optionsCollection property="currencies" value="currencyCode" label="currencyName"/>

							</html:select>

						</c:if>



						<c:if test="${!empty aimDesktopForm.activityRisks}">

							<html:select property="fltrActivityRisks" styleClass="inp-text">

								<html:option value="0">All Risks</html:option>

								<html:optionsCollection property="activityRisks" value="ratingValue" label="ratingName" />

							</html:select>

						</c:if>



						<html:select name="aimDesktopForm" property="lineMinRank" styleClass="inp-text">

							<html:option value="-1">LMR</html:option>

							<c:forEach var="lmr" items="${aimDesktopForm.actRankColl}" >

								<c:choose>

									<c:when test="${lmr == aimDesktopForm.lineMinRank}">

										<option value='<c:out value="${lmr}" />' selected><c:out value="${lmr}"/></option>

									</c:when>

									<c:otherwise>

										<option value='<c:out value="${lmr}" />'><c:out value="${lmr}"/></option>

									</c:otherwise>

								</c:choose>

							</c:forEach>

							<%--

							<html:option value="-2">LMR+</html:option>

							<html:option value="-3">LMR-</html:option>	--%>

						</html:select>

						<input type="submit" class="dr-menu" value="<digi:trn key="aim:desktopGoBtn">Go</digi:trn>">

						<input type="submit" class="dr-menu" onclick="resetFilters()" value="<digi:trn key="aim:desktopResetBtn">Reset</digi:trn>">

                        <input type="button" class="dr-menu" onclick="perviewPrint()" value="<digi:trn key="aim:desktopPrintBtn">Print</digi:trn>">

					</TD></TR>

				</c:if>

				<!-- Project List -->

				<TR><TD>

					<TABLE width="100%" cellpadding="4" cellSpacing="1" bgcolor="#ffffff" valign="top" align="left">

						<TR bgcolor="#DDDDDD" height="30">

							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"

							onMouseOut="this.className='colHeaderLink'" width="40%"

							onClick="window.document.location='/aim/viewMyDesktop.do?view=sorted&srt=1'"

							title='<digi:trn key="aim:ProjectNames" jsFriendly="true">Complete List of Projects for Team</digi:trn>'>

								<digi:trn key="aim:project">Project</digi:trn>

								<c:if test="${aimDesktopForm.srtFld == 1}">

									<c:if test="${aimDesktopForm.srtAsc == true}">

										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">

									</c:if>

									<c:if test="${aimDesktopForm.srtAsc == false}">

										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">

									</c:if>

								</c:if>

							</TD>

							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"

							onMouseOut="this.className='colHeaderLink'"  width="14%"

							onClick="window.document.location='/aim/viewMyDesktop.do?view=sorted&srt=2'"

							title='<digi:trn key="aim:IdforAMP" jsFriendly="true">System Generated Project ID</digi:trn>'>

								<digi:trn key="aim:ampId">AMP ID</digi:trn>

								<c:if test="${aimDesktopForm.srtFld == 2}">

									<c:if test="${aimDesktopForm.srtAsc == true}">

										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">

									</c:if>

									<c:if test="${aimDesktopForm.srtAsc == false}">

										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">

									</c:if>

								</c:if>

							</TD>

							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"

							onMouseOut="this.className='colHeaderLink'"  width="28%"

							onClick="window.document.location='/aim/viewMyDesktop.do?view=sorted&srt=3'"

							title='<digi:trn key="aim:FundingDonor" jsFriendly="true">Funding Donor for Project</digi:trn>'>

								<digi:trn key="aim:donor">Donor(s)</digi:trn>

								<c:if test="${aimDesktopForm.srtFld == 3}">

									<c:if test="${aimDesktopForm.srtAsc == true}">

										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">

									</c:if>

									<c:if test="${aimDesktopForm.srtAsc == false}">

										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">

									</c:if>

								</c:if>

							</TD>

							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"

							onMouseOut="this.className='colHeaderLink'"  width="18%"

							onClick="window.document.location='/aim/viewMyDesktop.do?view=sorted&srt=4'"

							title='<digi:trn key="aim:TotalCommitMade" jsFriendly="true">Total Committed Amount of Project</digi:trn>'>

								<FONT color="blue">*</FONT>

								<digi:trn key="aim:totalCommitments">Total Commitments</digi:trn>

								<c:if test="${aimDesktopForm.srtFld == 4}">

									<c:if test="${aimDesktopForm.srtAsc == true}">

										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">

									</c:if>

									<c:if test="${aimDesktopForm.srtAsc == false}">

										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">

									</c:if>

								</c:if>

							</TD>

						</TR>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>

						</c:set>

						<logic:notEmpty name="aimDesktopForm" property="activities">

							<c:forEach var="project" items="${aimDesktopForm.activities}"

							begin="${aimDesktopForm.stIndex}" end="${aimDesktopForm.edIndex - 1}">

								<TR bgcolor="#f4f4f2">

									<TD>

										<jsp:useBean id="urlChannelOverview" type="java.util.Map" class="java.util.HashMap"/>

										<c:set target="${urlChannelOverview}" property="ampActivityId">

											<bean:write name="project" property="ampActivityId"/>

										</c:set>

										<c:set target="${urlChannelOverview}" property="tabIndex" value="0"/>

										<div title='${translation}'>

										<digi:link href="/viewChannelOverview.do" name="urlChannelOverview">

											<c:out value="${project.name}" />

										</digi:link></div>

										<c:if test='${project.approvalStatus == "started"}'>

											<FONT size="2" color="#FF0000">*</FONT>

										</c:if>

										<c:if test='${project.approvalStatus == "created"}'>

											<FONT size="2" color="#008000">*</FONT>

										</c:if>

										<c:if test='${project.approvalStatus == "edited"}'>

											<FONT size="2" color="#008000">**</FONT>

										</c:if>

									</TD>

									<TD>

										<c:out value="${project.ampId}" />

									</TD>

									<TD>

										<c:if test="${!empty project.donor}">

											<TABLE cellspacing="1" cellpadding="1">

												<c:forEach var="dnr" items="${project.donor}">

													<TR><TD>

														<a href="javascript:newWin(<c:out value='${dnr.ampDonorId}' />)">

														<c:out value="${dnr.donorName}" /></a>

													</TD></TR>

												</c:forEach>

											</TABLE>

										</c:if>

										<c:if test="${empty project.donor}">

											<digi:trn key="aim:unspecified">Unspecified</digi:trn>

										</c:if>

									</TD>

									<TD align="right">

										<c:out value="${project.totalCommited}" />

									</TD>

								</TR>

							</c:forEach>

						</logic:notEmpty>



						<TR bgcolor="#FFFFFF">

							<TD><b>
							<font class="home-bg-header">
							<c:out value="${aimDesktopForm.activityCount}" />&nbsp;<digi:trn key="aim:activityCount">activities in your portfolio.</digi:trn>
							</font>
							</b></TD>

							<TD>

								<b><c:out value="${aimDesktopForm.defCurrency}" /></b>

							</TD>

							<TD align="right">

								<b><digi:trn key="aim:total">Total</digi:trn></b>

							</TD>

							<TD align="right">

								<b><c:out value="${aimDesktopForm.totalCommitments}" /></b>

							</TD>

						</TR>

						<TR bgcolor="#FFFFFF">

							<TD colspan="4">

								<bean:size id="totPages" name="aimDesktopForm" property="pages" />



								<c:if test="${totPages > 1}">



								<jsp:useBean id="pageUrl" type="java.util.Map" class="java.util.HashMap"/>

								<c:set target="${pageUrl}" property="view" value="page" />

								<c:if test="${aimDesktopForm.currentPage == 1}">

									<digi:trn key="aim:previousPage">Previous</digi:trn>

								</c:if>

								<c:if test="${aimDesktopForm.currentPage != 1}">

									<c:set target="${pageUrl}" property="page">

										<c:out value="${aimDesktopForm.currentPage - 1}" />

									</c:set>

									<digi:link href="/viewMyDesktop.do" name="pageUrl">

										<digi:trn key="aim:previousPage">Previous</digi:trn>

									</digi:link>

								</c:if>



								<c:forEach var="pg" items="${aimDesktopForm.pages}">

									|

									<c:if test="${aimDesktopForm.currentPage == pg}">

										<c:out value="${pg}" />

									</c:if>

									<c:if test="${aimDesktopForm.currentPage != pg}">

										<c:set target="${pageUrl}" property="page">

											<c:out value="${pg}" />

										</c:set>

										<digi:link href="/viewMyDesktop.do" name="pageUrl">

											<c:out value="${pg}" />

										</digi:link>

									</c:if>

								</c:forEach> |



								<c:if test="${aimDesktopForm.currentPage == totPages}">

									<digi:trn key="aim:nextPage">Next</digi:trn>

								</c:if>

								<c:if test="${aimDesktopForm.currentPage != totPages}">

									<c:set target="${pageUrl}" property="page">

										<c:out value="${aimDesktopForm.currentPage + 1}" />

									</c:set>

									<digi:link href="/viewMyDesktop.do" name="pageUrl">

										<digi:trn key="aim:nextPage">Next</digi:trn>

									</digi:link>

								</c:if>



								</c:if>

							</TD>

						</TR>

					</TABLE>

				</TD></TR>



				</TABLE>

			</TD></TR>

		</TABLE>

	</TD></TR>



	<TR><TD>

		<TABLE width="100%" cellPadding=2 cellspacing="0" vAlign="top" align="left">

			<TR>

				<TD>
					<FONT color=blue>
						<jsp:include page="utils/amountUnitsUnformatted.jsp">
							<jsp:param value="<BIG>* </BIG>" name="amount_prefix"/>
						</jsp:include>
						<bean:define id="selCurrency" name="reportCD" property="selectedCurrency" />
						<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>"> 
							<%=selCurrency %>
						</digi:trn>
						<digi:trn key="aim:perspectiveWarning1">Current workspace perspective is:</digi:trn>

						<bean:write name="currentPerspective" scope="session"/>.
						<digi:trn key="aim:perspectiveWarning2">Totals column only show funding items  having the same perspective as the current workspace.</digi:trn>
					</FONT>

				</TD>

				<c:if test="${aimDesktopForm.showAddActivityLink == true}">

				<TD width="300" align="right">

					<c:set var="translation">

						<digi:trn key="aim:clickToAddNewActivity">Click here to Add New Activity</digi:trn>

					</c:set>

					<div title='${translation}'>

					<digi:link href="/addActivity.do~pageId=1~reset=true~action=create">

					<digi:trn key="aim:addActivity">

					Add Activity</digi:trn></digi:link></div>

				</TD>

				</c:if>

			</TR>

		</TABLE>

	</TD></TR>



	<TR><TD>

		<TABLE width="100%" cellPadding=2 cellspacing="0" vAlign="top" align="left">

			<TR>

				<TD>

					<B><digi:trn key="aim:activityKeywordSearch">Activity Keyword 	Search</digi:trn></B>

					<html:text property="searchKey" styleClass="inp-text"/>

					<input type="button" value="GO" class="dr-menu" onclick="searchActivity()">

				</TD>

				<TD align="right">

					<logic:equal name="aimDesktopForm" property="teamHead" value="true">

						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

						<c:set target="${urlParams}" property="tId">

							<bean:write name="aimDesktopForm" property="teamId"/>

						</c:set>

						<c:set target="${urlParams}" property="dest" value="teamLead"/>

						<c:set var="translation">

							<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Workspace</digi:trn>

						</c:set>

						<div title='${translation}'>

						<digi:link href="/workspaceOverview.do" name="urlParams">

							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>

						</digi:link></div><br />

                        <div title='${translation}'>

					</logic:equal>

				</TD>

			</TR>

		</TABLE>

	</TD></TR>



</TABLE>



</digi:form>



<script type="text/javascript">
messageObj = new DHTMLSuite.modalMessage();	// We only create one object of this class
messageObj.setWaitMessage('Loading...');
messageObj.setShadowOffset(5);	// Large shadow

DHTMLSuite.commonObj.setCssCacheStatus(false);

function displayMessage(url)
{
	messageObj.setSource(url);
	messageObj.setCssClassMessageBox(false);
	messageObj.setSize(400,200);
	messageObj.setShadowDivVisible(true);	// Enable shadow for these boxes
	messageObj.display();
}

function displayStaticMessage(messageContent,cssClass)
{
	messageObj.setHtmlContent(messageContent);
	messageObj.setSize(400,150);
	messageObj.setCssClassMessageBox(cssClass);
	messageObj.setSource(false);	// no html source since we want to use a static message here.
	messageObj.setShadowDivVisible(false);	// Disable shadow for these boxes
	messageObj.display();
}

function closeMessage()
{
	messageObj.close();
}
</script>
