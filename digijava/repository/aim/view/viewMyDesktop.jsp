<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<digi:context name="digiContext" property="context" />

<script type="text/javascript">

function addLinks(id) {

	window.name = "opener" + new Date().getTime();		  
	var t = ((screen.width)-420)/2;
	var l = ((screen.height)-110)/2;
	
	addLinksWindow = window.open("","",'resizable=no,width=420,height=110,top='+l+',left='+t);
	addLinksWindow.document.open();
	addLinksWindow.document.write(getAddLinksWindowString(id));
	addLinksWindow.document.close();
}

function openNewWindow(wndWidth, wndHeight){
		window.name = "opener" + new Date().getTime();
		if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
			wndWidth = window.screen.availWidth/2;
			wndHeight = window.screen.availHeight/2;
		}
		popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no,scrollbars=yes");
}
function newWin(val) {
		if (document.getElementById("currUrl").value == "") {
			<digi:context name="selectLoc" property="context/module/moduleinstance/viewOrganisation.do" />
				url = "<%= selectLoc %>?ampOrgId=" + val;
				document.getElementById("currUrl").value = "<%= selectLoc %>";
				openNewWindow(635, 600);
				//obj.target = popupPointer.name;
				popupPointer.document.location.href = url;
				//obj.href = url;
		} else
			popupPointer.focus();
}
function myTask() {
	<digi:context name="mytask" property="context/module/moduleinstance/viewMyTask.do" />
    document.aimMyDesktopForm.action = "<%=mytask%>";
    document.aimMyDesktopForm.showTask.value = "showTask";
    document.aimMyDesktopForm.submit();
}
function open_win()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewMyDesktop.do" />
    document.aimMyDesktopForm.action = "<%=addUrl%>?view=reset";
    document.aimMyDesktopForm.submit();
}

function logout()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/logout.do" />
    document.aimMyDesktopForm.action = "<%=addUrl%>";
    document.aimMyDesktopForm.submit();
}

function fnAddProject()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/addAmpProjectDetails.do" />
    document.aimMyDesktopForm.action = "<%=addUrl%>?project=add";
    document.aimMyDesktopForm.submit();
}

function fnSearchActivity()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewMyDesktop.do" />
    document.aimMyDesktopForm.action = "<%=addUrl%>?view=search";
    document.aimMyDesktopForm.submit();
}
function showtip()
{
	how.style.color = "yellow"
}	
</script>

<digi:errors/>
<digi:instance property="aimMyDesktopForm"/>
<input type="hidden" id="currUrl" value="">

<TABLE width="100%" cellPadding=0 cellSpacing=0 valign="top" align="left">
<TR><TD width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</TD></TR>
<TR><TD width="100%" valign="top" align="left">

<digi:form action="/viewMyDesktop.do" method="post">
<html:hidden property="teamMemberId" />

<input type="hidden" name="showTask" value="">

<TABLE border=0 bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" valign="top" align="left">
<TBODY>
	<TR>
	   <TD class=r-dotted-lg width=9>&nbsp;</TD>
	   <TD align=left class=r-dotted-lg vAlign=top width="80%">
<!-- Contains the Portfolio part of data-->
			<TABLE border=0 cellPadding=2 cellSpacing=0 width="100%" height="100%">
       		<TBODY>
       			<TR>
         			<TD height=31 width=9>&nbsp;</TD>
         			<TD class=subtitle-blue vAlign=center width="100%">
						<digi:trn key="aim:portfolio">Portfolio</digi:trn></TD>
					<TD>&nbsp;</TD>
				</TR>	
<%
	int i=0;
%>
       			<TR >
         			<TD>&nbsp;</TD>
         		    <TD noWrap width="100%" align="left">
						<logic:notEqual name="aimMyDesktopForm" property="filterFlag" value="false">
           				<DIV class=f-names>
							<bean:write name="aimMyDesktopForm" property="perspective"/>&nbsp;<digi:trn key="aim:view">View</digi:trn>:
							</DIV>
						</logic:notEqual>
						<%--
						<logic:notEmpty name="aimMyDesktopForm" property="perspective">
									
						<html:select styleClass="dr-menu" property="perspective">
								<html:optionsCollection name="aimMyDesktopForm" property="perspective" value="code" label="name"/> 
						</html:select>
						
						</logic:notEmpty>
						--%>

						<logic:notEmpty name="aimMyDesktopForm" property="fiscalYears">
						<% i++; %>
							<html:select property="ampCalType" style="font-family: Arial; font-size: 8pt">
								<html:optionsCollection name="aimMyDesktopForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%> <br>  <%	}%>
						<logic:notEmpty name="aimMyDesktopForm" property="currency">
							<% i++; %>
							<html:select property="ampCurrencyCode" style="font-family: Arial; font-size: 8pt" >
								<html:optionsCollection name="aimMyDesktopForm" property="currency" value="currencyCode" label="currencyName"/> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%> <br>  <%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="donor">
						<% i++; %>
							<html:select property="ampOrgId" style="font-family: Arial; font-size: 8pt">
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimMyDesktopForm" property="donor" value="ampOrgId" label="acronym"/> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%>	<br>	<%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="region">
						<% i++; %>
							<html:select property="ampLocationId" style="font-family: Arial; font-size: 8pt" >
								<option value="All">All Regions</option>
								<html:optionsCollection name="aimMyDesktopForm" property="region" value="name" label="name"  /> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%>	<br>	<%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="sector">
						<% i++; %>
							<html:select property="ampSectorId" style="font-family: Arial; font-size: 8pt" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimMyDesktopForm" property="sector" value="ampSectorId" label="name" /> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%>	<br> <%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="status">
						<% i++; %>
							<html:select property="ampStatusId" style="font-family: Arial; font-size: 8pt" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimMyDesktopForm" property="status" value="ampStatusId" 
								label="name"  /> 
							</html:select>
						</logic:notEmpty>
						<% if(i==4){ i=0;%>	<br>	<%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="ampFromYears">
						<% i++; %>
							<html:select property="ampFromYear" style="font-family: Arial; font-size: 8pt" >
								<option value="0">From Year</option>
								<html:options name="aimMyDesktopForm" property="ampFromYears" /> 
							</html:select> 
						</logic:notEmpty>
						<% if(i==4){ i=0;%>	<br>	<%	}%>

						<logic:notEmpty name="aimMyDesktopForm" property="ampToYears">
						<% i++; %>
							<html:select property="ampToYear" style="font-family: Arial; font-size: 8pt" >
								<option value="0">To Year</option>
								<html:options name="aimMyDesktopForm" property="ampToYears"  /> 
							</html:select> 
						</logic:notEmpty>
					
						<logic:notEqual name="aimMyDesktopForm" property="filterFlag" value="false">
							<html:submit value=" GO "  style="font-family: Arial; font-size: 7pt" />
			  				<html:submit style="font-family: Arial; font-size: 7pt" value="Reset" onclick="open_win()" />
							</logic:notEqual>
			  		</TD>
					<TD>&nbsp;</TD>
				</TR>

	        	<TR>
          			<TD>&nbsp;</TD>
          			<TD vAlign="top" class="box-border-nopadding"> 
            			<TABLE border=0 cellPadding=5 cellSpacing=1 width="100%" vAlign="top">
              			<TBODY>
              				<TR bgColor=#dddddb height="25">
                				<TD class=box-title  width="40%">
                  					<DIV align=center>
									<a title="<digi:trn key="aim:ProjectNames">Complete List of Projects for Team</digi:trn>">
									<digi:trn key="aim:project">Project</digi:trn>
									</a>
									(<jsp:useBean id="urlProjectAsc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlProjectAsc}" property="sortField" value="1"/>
									<c:set target="${urlProjectAsc}" property="sortOrder" value="1"/>
									<digi:link href="/viewMyDesktop.do" name="urlProjectAsc" style="TEXT-DECORATION: NONE">+</digi:link>/<jsp:useBean id="urlProjectDesc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlProjectDesc}" property="sortField" value="1"/>
									<c:set target="${urlProjectDesc}" property="sortOrder" value="0"/>
									<digi:link href="/viewMyDesktop.do" name="urlProjectDesc" style="TEXT-DECORATION: NONE">-</digi:link>)</DIV></TD>
	                			<TD class=box-title width="20%">
					       			<DIV align=center>
									<a title="<digi:trn key="aim:IdforAMP">System Generated Project ID</digi:trn>">
									<digi:trn key="aim:ampId">AMP ID</digi:trn>
									</a>(<jsp:useBean id="urlIdAsc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlIdAsc}" property="sortField" value="2"/>
									<c:set target="${urlIdAsc}" property="sortOrder" value="1"/>
									<digi:link href="/viewMyDesktop.do" name="urlIdAsc" style="TEXT-DECORATION: NONE">+</digi:link>/<jsp:useBean id="urlIdDesc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlIdDesc}" property="sortField" value="2"/>
									<c:set target="${urlIdDesc}" property="sortOrder" value="0"/>
									<digi:link href="/viewMyDesktop.do" name="urlIdDesc" style="TEXT-DECORATION: NONE">-</digi:link>)</DIV></TD>
                				<TD class=box-title width="20%">
                  					<DIV align=center>
									<a title="<digi:trn key="aim:FundingDonor">Funding Donor for Project</digi:trn>">
									<digi:trn key="aim:donor">Donor(s)</digi:trn>
									</a>
									(<jsp:useBean id="urlDonorAsc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlDonorAsc}" property="sortField" value="3"/>
									<c:set target="${urlDonorAsc}" property="sortOrder" value="1"/>
									<digi:link href="/viewMyDesktop.do" name="urlDonorAsc" style="TEXT-DECORATION: NONE">+</digi:link>/<jsp:useBean id="urlDonorDesc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlDonorDesc}" property="sortField" value="3"/>
									<c:set target="${urlDonorDesc}" property="sortOrder" value="0"/>
									<digi:link href="/viewMyDesktop.do" name="urlDonorDesc" style="TEXT-DECORATION: NONE">-</digi:link>)</DIV></TD>
                				<TD class=box-title width="20%">
                  					<DIV align=center>
									<a title="<digi:trn key="aim:TotalCommitMade">Total Committed Amount of Project</digi:trn>">
									<FONT color="blue">*</FONT>
									<digi:trn key="aim:totalCommitments">Total Commitments</digi:trn>
									</a>
									(<jsp:useBean id="urlTotalAsc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlTotalAsc}" property="sortField" value="4"/>
									<c:set target="${urlTotalAsc}" property="sortOrder" value="1"/>
									<digi:link href="/viewMyDesktop.do" name="urlTotalAsc" style="TEXT-DECORATION: NONE">+</digi:link>/<jsp:useBean id="urlTotalDesc" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlTotalDesc}" property="sortField" value="4"/>
									<c:set target="${urlTotalDesc}" property="sortOrder" value="0"/>
									<digi:link href="/viewMyDesktop.do" name="urlTotalDesc" style="TEXT-DECORATION: NONE">-</digi:link>)</DIV></TD>
							</TR>
					
				            <logic:empty name="aimMyDesktopForm" property="ampProjects"> 
		<tr><td colspan="4" align="center">
		<b>No Search Results</b></td></tr>
		</logic:empty>
		<logic:notEmpty name="aimMyDesktopForm" property="ampProjects"> 
							<logic:iterate name="aimMyDesktopForm" property="ampProjects"  id="ampProjects"
							type="org.digijava.module.aim.helper.AmpProject">
							<TR bgColor="#f8f8f5" vAlign="top">
								<TD>
									<jsp:useBean id="urlChannelOverview" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlChannelOverview}" property="ampActivityId">
										<bean:write name="ampProjects" property="ampActivityId"/>
									</c:set>
									<c:set target="${urlChannelOverview}" property="tabIndex" value="0"/>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
									</bean:define>
									<digi:link href="/viewChannelOverview.do" name="urlChannelOverview" title="<%=translation%>" >
										<bean:write name="ampProjects" property="name"/>
									</digi:link>
									
									<c:if test='${ampProjects.approvalStatus == "started"}'>
										<font size="2" color="#FF0000">*</font>
									</c:if>
									<c:if test='${ampProjects.approvalStatus == "created"}'>
										<font size="2" color="#008000">*</font>
									</c:if>
									<c:if test='${ampProjects.approvalStatus == "edited"}'>
										<font size="2" color="#008000">**</font>
									</c:if>
									
								</TD>
								<TD>
									<bean:write name="ampProjects"  property="ampId"/>
								</TD>
								<TD>
									<logic:empty name="ampProjects" property="donor">Unspecified
									</logic:empty>
									<logic:notEmpty name="ampProjects" property="donor">
										<logic:iterate name="ampProjects" id="donors" property="donor" type="org.digijava.module.aim.helper.AmpProjectDonor"> 
											<a href="javascript:newWin(<c:out value='${donors.ampDonorId}' />)">
												<bean:write name="donors"  property="donorName"/></a>
										<br>
										</logic:iterate>
									</logic:notEmpty>
								</TD>
								
								<TD>
									<logic:notEqual name="ampProjects" property="totalCommited" value="0"><DIV align="right"><bean:write name="ampProjects" property="totalCommited"/>&nbsp;</DIV></logic:notEqual>
								</TD>
							</TR>
							</logic:iterate>
							<TR valign="top">
								<TD>&nbsp;
								</TD>
								<TD><strong>
                			<bean:write name="aimMyDesktopForm" property="ampCurrencyCode"/></strong>
							</TD>
							
                			<TD>&nbsp;
                			<STRONG><digi:trn key="aim:totals"> Totals</digi:trn></STRONG>
							</TD>
                			<TD align="right">
                			<strong><bean:write name="aimMyDesktopForm" property="grandTotal"/></strong>
							</TD>
							</TR>		
					</logic:notEmpty>	
							<!-- modified by Priyajith -->
							<!-- start -->
							<logic:notEmpty name="aimMyDesktopForm" property="pages">
							<tr>
								<td>
<!--								
									Page : <bean:write name="aimMyDesktopForm" property="page" /> of 
									<digi:trn key="aim:themeManagerPages">
									Pages :
									</digi:trn>
-->								
<!-- -------------------------------  Prevoius Links     ---------------------------       -->
									<bean:define id="currPage" name="aimMyDesktopForm" property="page" type="java.lang.Integer" /> 
									<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams2}" property="page">
										<%=(currPage.intValue()-1)%>
									</c:set>
									<logic:notEqual name="aimMyDesktopForm" property="page"
									value="1">
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewPreviousPage">Click here to goto Previous page</digi:trn>
										</bean:define>
										<digi:link href="/viewMyDesktop.do" name="urlParams2" title="<%=translation%>" >
											Previous 
										</digi:link>
										&nbsp
									</logic:notEqual>
									
									<logic:equal name="aimMyDesktopForm" property="page"
									value="1">
										Previous &nbsp
									</logic:equal>
<!----------------------------------END   -----------------------------------------------     -->									
									
									<logic:iterate name="aimMyDesktopForm" property="pages" id="pages" type="java.lang.Integer">
									<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams1}" property="page">
										<%=pages%>
									</c:set>
									
									<%  int curr = currPage.intValue();
										int cnt = pages.intValue();
										//System.out.println(curr + " Comparison : " + cnt);
									%>
									<% if( curr != cnt ) { %>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAllPages">Click here to view All pages</digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" name="urlParams1" title="<%=translation%>" >
										<%=pages%>
									</digi:link>
									<% } else { %>
									<%=pages%>
									<% } %>
												
									|&nbsp; 
									</logic:iterate>
									
<!-- -------------------------------  Next Links -------------------------------       -->									
									<bean:define id="currPage" name="aimMyDesktopForm" property="page" type="java.lang.Integer" /> 
									<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams3}" property="page">
										<%=(currPage.intValue()+1)%>
									</c:set>
									
									<bean:define name="aimMyDesktopForm" id="allPages" property="pages" 
									type="java.util.Collection" />

									<% if(allPages.size() == currPage.intValue()) { %>	
										&nbsp Next 
									<% } else { %>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGoToNext">Click here to go to Next page</digi:trn>
										</bean:define>
										<digi:link href="/viewMyDesktop.do" name="urlParams3" title="<%=translation%>" >
										Next
										</digi:link>
										&nbsp
									<% } %>
<!-- ------------------------------------------------------------------------------  -->									
								</td>
							</tr>
							</logic:notEmpty>							
							<!-- end -->
				  		</TBODY>
						</TABLE>
					</TD>
					<TD>&nbsp;</TD>
				</TR>
				<TR>
					<TD>&nbsp;</TD>
          		<TD align=left width="100%">
						<table width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="left">
							<tr>
								<td>
									<FONT color=blue>*
									<digi:trn key="aim:allTheAmountsInThousands">	
									All the amounts are in thousands (000)</digi:trn></FONT>								
								</td>
								<c:if test="${aimMyDesktopForm.donorFlag == false}">
								<td width="300" align="right">
									<c:if test="${aimMyDesktopForm.write == true}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToAddNewActivity">Click here to Add New Activity</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do~pageId=1~reset=true~action=create" title="<%=translation%>">
										<digi:trn key="aim:addActivity">
										Add Activity</digi:trn></digi:link>
									</c:if>								
								</td>
								</c:if>
							</tr>
						</table>

					</TD>
					<TD>&nbsp;</TD>				
				</TR>
        		<TR>
          			<TD>&nbsp;</TD>
          			<TD align=right width="100%">
            			<DIV align=left>
							<B><digi:trn key="aim:activityKeywordSearch">Activity Keyword 	Search</digi:trn></B>
							<html:text name="aimMyDesktopForm"  property="searchCriteria"/>
							<html:submit value=" GO "  styleClass="dr-menu" 
							onclick="fnSearchActivity()" />
						</DIV></TD>
					<TD>&nbsp;</TD>
				</TR>
        		<TR>
          			<TD>&nbsp;</TD>
          			<TD width="100%">
            			<DIV align=right>&nbsp;
							</DIV></TD>
					<TD>&nbsp;</TD>						
				</TR>
				<TR>
          			<TD>&nbsp;</TD>
		 	 		<TD align=right width="100%">
            			<DIV align=left>

						<!-- modified by Priyajith -->
						<!-- start -->
							 <TABLE width="100%">
							 <TR><TD>
							 		<%--
									<B><digi:trn key="aim:applicationSettings">Application Settings</digi:trn></B>
									<br>
									<bean:define id="translation">
										<digi:trn key="aim:clickToCustomizePersonalSettings">Click here to Customize Personal Settings</digi:trn>
									</bean:define>
									<digi:link href="/customizeSettings.do" title="<%=translation%>">
									<digi:trn key="aim:customizedPersonalSettings">Customized Personal Settings</digi:trn>
									</digi:link>
									--%>
									</TD>
									<TD align="right">
										<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlParams}" property="tId">
											<bean:write name="aimMyDesktopForm" property="teamId"/>
										</c:set>
										<c:set target="${urlParams}" property="dest" value="teamLead"/>
										<logic:equal name="aimMyDesktopForm" property="teamLeadFlag" value="true">
											<bean:define id="translation">
												<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Workspace</digi:trn>
											</bean:define>
											<digi:link href="/workspaceOverview.do" name="urlParams" title="<%=translation%>">
												<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
											</digi:link>
										</logic:equal>
									</TD>
								</TR>
								</TABLE>

							<!-- end -->						
						
						</DIV></TD>
					<TD>&nbsp;</TD>						
				</TR>	  
		  	</TBODY>
			</TABLE>		
		</TD>
		<TD bgColor=#f7f7f4 class=r-dotted-lg vAlign=top>
			<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%">
     		<TBODY>
				<logic:notEmpty name="aimMyDesktopForm"  property="ampReports" >
				<TR>
					<TD class=r-dotted-lg-buttom vAlign=top>
						<TABLE border=0 cellPadding=0 cellSpacing=0 width="198" >
  		           		<TBODY>
           					<TR>
              					<TD>
                 					<TABLE border=0 cellPadding=0 cellSpacing=0 >
                 						<TR bgColor=#f4f4f2>
                    						<TD bgColor=#c9c9c7 class=box-title width=70>
											<a title="<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>">
											Reports
											</a>
											</TD>
                      						<TD background="module/aim/images/corner-r.gif" 
											height=17 width=17></TD>
											</TR>

										</TABLE>
									</TD>
								</TR>
								<TR>
							     <TD bgColor=#ffffff class=box-border align=left>
									<TABLE border=0 cellPadding=0 cellSpacing=1 width="80%" >
											<logic:iterate name="aimMyDesktopForm"  property="ampReports" id="ampReports"
											type="org.digijava.module.aim.dbentity.AmpReports"> 
											
											<TR><TD>
				                 	 			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" 
												width=10>
												<bean:define name="ampReports" id="link"  property="description" type="java.lang.String"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewReport">Click here to view Report</digi:trn>
												</bean:define>
												<digi:link href="<%=link%>" styleClass="h-box" title="<%=translation%>" >
												<bean:write name="ampReports" property="name"/>
												</digi:link><br>
											</TD>
										</TR>
										
										
										</logic:iterate>
										<TR>
											<TD>
												<TABLE width="100%">
													<TR><TD width="50%" align="left">
													<logic:greaterThan name="aimMyDesktopForm" property="reportCount" value="5">
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>
														</bean:define>
														<digi:link href="/viewTeamReports.do" title="<%=translation%>" ><digi:trn key="aim:more">..more</digi:trn>
														</digi:link>
													</logic:greaterThan>
													</TD></TR>
												</TABLE>
											</TD>
										</TR>

										<tr><td >	&nbsp;	</td></tr>
										<tr>
											<td height= 20 >
												<bean:define id="translation">
													<digi:trn key="aim:createAdvancedReport">Create Advanced Report </digi:trn>
							  				 	</bean:define>
												<digi:link href="/advancedReportManager.do?clear=true" title="<%=translation%>">
													<b> Advanced Report Manager	</b>
												</digi:link>
											</td>
										</tr>
									</TABLE>	
								</TD>
							</TR>
						</TBODY>
						</TABLE>
					</TD>
				</TR>
				</logic:notEmpty>
				
				<TR>
					<TD class=r-dotted-lg-buttom vAlign=top>
						<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
  		           		<TBODY>
           					<TR>
              					<TD>
                 					<TABLE border=0 cellPadding=0 cellSpacing=0>
                 					<TBODY>
                 						<TR bgColor=#f4f4f2>
                    						<TD bgColor=#c9c9c7 class=box-title width=100>
													<a title="<digi:trn key="aim:ListofRelatedLinks">Frequently Used Links for Desktop</digi:trn>">
														<digi:trn key="aim:relatedLinks">Related Links</digi:trn>
													</a>
												</TD>
                    						<TD background="module/aim/images/corner-r.gif" height=17 width=17></TD>
											</TR>
										</TBODY>
										</TABLE>
									</TD>
								</TR>
								<TR>
	   					   	<TD bgColor=#ffffff class=box-border width="20%">
										<TABLE border=0 cellPadding=0 cellSpacing=3 width="90%">	
										<logic:notEmpty name="aimMyDesktopForm"  property="documents" >
										<%--
										<jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>			
										--%>
										<% int linkCnt = 0; %>
										<logic:iterate name="aimMyDesktopForm"  property="documents" 
									 	id="document" type="org.digijava.module.aim.helper.Documents"> 							     
										<% if (linkCnt < 5) { linkCnt ++; %>
										<TR>
											<TD><IMG alt=Link height=10 width=10 src="../ampTemplate/images/arrow-gr.gif"></TD>
											<TD width="91%">
												<%--
												<c:set target="${docParams}" property="docId">
													<c:out value="${document.docId}"/>
												</c:set>											
												<c:set target="${docParams}" property="actId">
													<c:out value="${document.activityId}"/>
												</c:set>																							
												<c:set target="${docParams}" property="pageId" value="0"/>
												<c:set target="${docParams}" property="reset" value="true"/>
												--%>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewLinkDetails">Click here to view Link Details</digi:trn>
												</bean:define>
												<digi:link href="/viewRelatedLinks.do" styleClass="h-box" title="<%=translation%>" >
												<bean:write name="document" property="title"/>
												</digi:link>
											</TD>
										</TR>
										<% } %>
										</logic:iterate>
										</logic:notEmpty>
										<TR><TD colspan=2>
											<table width="100%" cellpadding=0 cellspacing=0 valign=top align=left>
												<tr>
													<td>
														<a href="javascript:addLinks(<c:out value="${aimMyDesktopForm.teamMemberId}"/>)">
														<digi:trn key="aim:addLinks">Add Links</digi:trn></a>													
													</td>
													<td>
														<logic:greaterThan name="aimMyDesktopForm" property="documentCount" value="5">
															<digi:link href="/viewRelatedLinks.do">
															<digi:trn key="aim:more">more</digi:trn></digi:link>
														</logic:greaterThan>													
													</td>
												</tr>
											</table>
										</TD></TR>									
										</TABLE> 
									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</TD>
				</TR>


				<%-- My Task begins here --%>
				
   				<logic:equal name="aimMyDesktopForm"  property="workingTeamFlag" value="yes">
				<TR>
					<TD class=r-dotted-lg-buttom vAlign=top>
						<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
  		           		<TBODY>
           					<TR>
              					<TD>
                 					<TABLE border=0 cellPadding=0 cellSpacing=0 width="50%">
                 					<TBODY>
                 						<TR bgColor=#f4f4f2>
                    						<TD bgColor=#c9c9c7 class=box-title width=100%>
											<a title="<digi:trn key="aim:myTaskListOfTeamLeader">List of activities pending for approval</digi:trn>">
														<digi:trn key="aim:myTaskList">My Tasks</digi:trn>
											</a>
											</TD>
											<TD background="module/aim/images/corner-r.gif" height=17 width=17>&nbsp;</TD>
										</TR>
									</TBODY>
									</TABLE></TD>
							</TR>
							<TR>
							     <TD bgColor=#ffffff class=box-border width="100%">
									<TABLE border=0 cellPadding=2 cellSpacing=3 width="80%">
									<logic:empty name="aimMyDesktopForm"  property="myTasksColl" >
										<digi:trn key="aim:emptyMyTaskList">No pending task</digi:trn>
									</logic:empty>
									<logic:notEmpty name="aimMyDesktopForm"  property="myTasksColl" >
										<a href="javascript:myTask()">
											<bean:write name="aimMyDesktopForm" property="myTaskSize" />&nbsp;
											<c:choose>
												<c:when test="${aimMyDesktopForm.myTaskSize == 1}">
													<digi:trn key="aim:myPendingTask">Task pending for approval</digi:trn>
												</c:when>
												<c:otherwise>
													<digi:trn key="aim:myPendingTasks">Tasks pending for approval</digi:trn>
												</c:otherwise>
											</c:choose>
										</a>
									</logic:notEmpty>
								   </TABLE>
								</TD>
							</TR>
							
						</TBODY>
						
						</TABLE>
					</TD>
				</TR>
				</logic:equal>

				<%-- My Task ends here --%>
				
				
				<logic:notEmpty name="aimMyDesktopForm"  property="ampTeamMembers" >
				<TR>
					<TD class=r-dotted-lg-buttom vAlign=top>
						<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
  		           		<TBODY>
           					<TR>
              					<TD>
                 					<TABLE border=0 cellPadding=0 cellSpacing=0 width="50%">
                 					<TBODY>
                 						<TR bgColor=#f4f4f2>
                    						<TD bgColor=#c9c9c7 class=box-title width=100%>
											<a title="<digi:trn key="aim:TeamMemberListNActivities">List of Team Members associated with Activities</digi:trn>">			<digi:trn key="aim:teamMembers">Team Members</digi:trn>
											</a>
											</TD>
											<TD background="module/aim/images/corner-r.gif" height=17 width=17></TD>
										</TR>
									</TBODY>
									</TABLE></TD>
							</TR>
							<TR>
							     <TD bgColor=#ffffff class=box-border width="100%">
									<TABLE border=0 cellPadding=2 cellSpacing=3 width="80%">
									<logic:iterate name="aimMyDesktopForm"  property="ampTeamMembers" id="ampTeamMembers" 
									type="org.digijava.module.aim.helper.TeamMember"> 
										<TR>
											<TD>
												<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewMemberDetails">Click here to View Member Details</digi:trn>
													</bean:define>
													<a href="javascript:showUserProfile(<c:out value="${ampTeamMembers.memberId}"/>)" title="<%=translation%>">	
														<bean:write name="ampTeamMembers" property="memberName" />
													</a>											
												<logic:equal name="ampTeamMembers" property="teamHead" value="true">
													*
												</logic:equal>
											</TD>
										</TR>
										</logic:iterate>
										<TR>
											<TD>
												<TABLE width="100%">
													<TR><TD width="50%" align="left">
															*Team Lead
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
							
						</TBODY>
						
						</TABLE>
					</TD>
				</TR>
				</logic:notEmpty>
			</TBODY>
			</TABLE>
		</TD>
	</TR>
</TBODY>
</TABLE>
</digi:form>
</TD></TR>
</TABLE>
