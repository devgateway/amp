<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.form.ReportsForm"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page language="java" import="org.digijava.module.aim.helper.TeamMember" %>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.PUBLIC_VIEW %>" compareWith="On" onTrueEvalBody="true">

<!-- this is for the nice tooltip widgets -->

<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>">
</script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>">
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>">
</script>
<script type="text/javascript">
<!--
function popup(mylink, windowname)
{
	if (!window.focus)
		return true;

	var href;
	if (typeof(mylink) == 'string')
		href = mylink;
	else
		href = mylink.href;
	
	window.open(href, windowname, 'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
	return false;
}
//-->
</script>
<style>
.publicReportsTable THEAD TD {
	font-family: Arial;
	font-size:10px;
	background-color:#27415f;
	color:#FFFFFF;
	border-bottom:1px solid black;
	border-right:1px solid black;
}
.publicReportsTable TBODY TD {
	font-family: Arial;
	font-size:10px;
	color:black;
	border-right:1px solid #666666;
}
.publicReportsTable TBODY TD A {
	font-family: Arial;
	font-size:10px;
}
</style>

<digi:instance property="aimTeamReportsForm" />
<digi:form action="/viewTeamReports.do" method="post">

<c:if test="${!aimTeamReportsForm.showTabs}">
  <c:set var="pageTitle">
    <digi:trn> List of Reports </digi:trn>
  </c:set>
  <c:set var="breadCrumb">
    <digi:trn> List of Reports </digi:trn>
  </c:set>
  <c:set var="titleColumn">
    <digi:trn> Report Title </digi:trn>
  </c:set>
</c:if>
<div align="center" style="padding-top:10px">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
  <tr>
    <td width=14>&nbsp;</td>
    <td align=left vAlign=top width=1000>
    <table cellPadding=5 cellSpacing=0 width="100%">
        <tr>
          <td valign="bottom" class="crumb" >
	        <c:set var="translation">
            	<digi:trn>Home Page</digi:trn>
            </c:set>
            <digi:link href="/" styleClass="comment" title="${translation}">
	            <digi:trn>Home Page</digi:trn>
            </digi:link>
            &gt; ${breadCrumb}</td>
        </tr>
        <tr>
          <td height=16 align="left" vAlign="center"><digi:errors/>
            <span class=subtitle-blue> ${pageTitle} </span> </td>
        </tr>
        <tr>
          <td><c:if test="${aimTeamReportsForm.showTabs}"> <a style="cursor:pointer; text-decoration:underline; color: #05528b" onclick="initializeTabManager()"> <b>
              <digi:trn>Manage Active Tabs</digi:trn>
              </b> </a> </c:if>
          </td>
        </tr>
         <tr>
                <td><digi:trn>Report Title</digi:trn>: <html:text property="keyword"/> <html:submit property="action" value="search"><digi:trn>Search</digi:trn></html:submit><html:submit property="action" value="clear"><digi:trn>Clear</digi:trn></html:submit></td>
            </tr>
        <tr>
          <td noWrap width=1000 vAlign="top"><table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
              <tr>
                <td>
					<%
                      ReportsForm aimTeamReportsForm = (ReportsForm) pageContext.getAttribute("aimTeamReportsForm");
                      java.util.List pagelist = new java.util.ArrayList();
                      for(int i = 0; i < aimTeamReportsForm.getTotalPages(); i++)
                        pagelist.add(new Integer(i + 1));
                      pageContext.setAttribute("pagelist",pagelist);
                      pageContext.setAttribute("maxpages", new Integer(aimTeamReportsForm.getTotalPages()));
                      pageContext.setAttribute("actualPage", new Integer(aimTeamReportsForm.getPage()));
                    %>
                  <c:set var="reportNumber" value="${fn:length(aimTeamReportsForm.reports)}"> </c:set>
                  <c:if test="${reportNumber != 0}">
                    <c:if test="${aimTeamReportsForm.totalPages ne '1'}">
		                <table cellpadding="3" cellspacing="3" border="0" width="100%" height="20">
                      <tr>
                        <td>
                          <jsp:useBean id="urlParamsPagination" type="java.util.Map" class="java.util.HashMap"/>
                          <c:set target="${urlParamsPagination}" property="action" value="getPage"/>
                          <c:if test="${aimTeamReportsForm.currentPage >0}">
                            <jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
                            <c:set target="${urlParamsFirst}" property="page" value="0"/>
                            <c:set target="${urlParamsFirst}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsFirst}" property="tabs" value="true"/>
                            </c:if>
                            <c:set var="translation">
                              <digi:trn key="aim:firstpage">First Page</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  > &lt;&lt; </digi:link>
                            <jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
                            <c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}"/>
                            <c:set target="${urlParamsPrevious}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsPrevious}" property="tabs" value="true"/>
                            </c:if>
                            |
                            <c:set var="translation">
                              <digi:trn key="aim:previous">Previous</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
                              <digi:trn key="aim:previous">Previous</digi:trn>
                            </digi:link>
                            | </c:if>
                          <c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
                          <c:set var="start" value="${aimTeamReportsForm.offset}"/>
                          <c:if test="${maxpages > 1}">
                            <logic:iterate name="pagelist" id="pageidx" type="java.lang.Integer" offset="${start}" length="${length}">
                              <c:set target="${urlParamsPagination}" property="page" value="${pageidx - 1}"/>
                              <c:if test="${(pageidx - 1) eq actualPage}">
                                <bean:write name="pageidx"/>
                              </c:if>
                              <c:if test="${(pageidx - 1) ne actualPage}">
                                <c:if test="${aimTeamReportsForm.showTabs}">
                                  <c:set target="${urlParamsPagination}" property="tabs" value="true"/>
                                </c:if>
                                <digi:link href="/viewTeamReports.do"  name="urlParamsPagination" >
                                  <bean:write name="pageidx"/>
                                </digi:link>
                              </c:if>
                              <c:if test="${pageidx < maxpages}"> | </c:if>
                            </logic:iterate>
                          </c:if>
                          <c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
                            <jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
                            
                            <c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
                              <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}"/>
                            </c:if>
                            <c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
                              <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}"/>
                            </c:if>
                            <c:set target="${urlParamsNext}" property="action" value="getPage"/>
                            <c:set var="translation">
                              <digi:trn key="aim:nextpage">Next Page</digi:trn>
                            </c:set>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsNext}" property="tabs" value="true"/>
                            </c:if>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
                              <digi:trn key="aim:next">Next</digi:trn>
                            </digi:link>
                            |
                            <jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
                            
                            <c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}"/>
                            <c:set target="${urlParamsLast}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsLast}" property="tabs" value="true"/>
                            </c:if>
                            <c:set var="translation">
                              <digi:trn key="aim:lastpage">Last Page</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  > &gt;&gt; </digi:link>
                            &nbsp;&nbsp; </c:if>
                          <c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>
                          &nbsp;
                          <digi:trn key="aim:of">of</digi:trn>
                          &nbsp;
                          <c:out value="${aimTeamReportsForm.totalPages}"></c:out>
                        </td>
                      </tr>
                    </table>
					</c:if>
                  </c:if>
                </td>
              </tr>
              <tr bgColor=#f4f4f2>
                <td valign="top"><table align=center cellPadding=0 cellSpacing=0 width="100%">
                    <tr>
                      <td><table border=0 cellPadding=4 cellSpacing=3 width="100%" class="publicReportsTable">
	                      <thead>
                          <tr>
                            <td align="center" height="20" style="text-transform:capitalize;">
                                  <c:if test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=1}">
                                      <digi:link href="/viewTeamReports.do?sortBy=1" style="color:#FFF;">
                                        <b><digi:trn>${titleColumn}</digi:trn></b>
                                    </digi:link>
                                   <c:if test="${aimTeamReportsForm.sortBy==2}"><img src="/repository/aim/images/down.gif" alt="down"/></c:if>
                                </c:if>
                                <c:if test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==1}">
                                    <digi:link href="/viewTeamReports.do?sortBy=2" style="color:#FFF;">
                                        <b><digi:trn key="aim:organizationName">${titleColumn}</digi:trn></b>
                                    </digi:link>
                                      <img  src="/repository/aim/images/up.gif" alt="up"/>
                                </c:if>
                             </td>
                            <td align="center" height="20" style="text-transform:capitalize;"><b>
                              <digi:trn>Type</digi:trn>
                              </b> </td>
                            <td align="center" height="20" style="text-transform:capitalize;"><b>
                              <digi:trn>Filtered</digi:trn>
                              </b> </td>
                            <td align="center" height="20" style="text-transform:capitalize;"><b>
                              <digi:trn>Hierarchies</digi:trn>
                              </b> </td>
                            <td align="center" height="20" style="text-transform:capitalize;"><b>
                              <digi:trn>Columns</digi:trn>
                              </b> </td>
                            <td align="center" height="20" style="text-transform:capitalize;"><b>
                              <digi:trn>Measures</digi:trn>
                              </b> </td>
                            <td align="center" height="20" style="text-transform:capitalize;">
                            	<strong>
                            		<digi:trn>Export Options</digi:trn> 
                            	</strong>
                            </td>
                          </tr>
                          </thead>
                          <tbody>
                          <c:if test="${reportNumber == 0}">
                          <tr>
                            <td colspan="7"><digi:trn key="aim:noreportspresent"> No reports present </digi:trn>
                            </td>
                          </tr>
                          </c:if>
                          <logic:iterate name="aimTeamReportsForm"  property="reportsList" id="report" indexId="idx"
                            type="org.digijava.module.aim.dbentity.AmpReports">
                            <tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');">
                              <td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>">
                                  <digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');" title="<digi:trn>Click here to view the Report</digi:trn>"> <b>
                                    <p style="max-width: 400px;white-space: normal" title="${report.name}">
                                      <c:if test="${fn:length(report.name) > 120}" >
                                        <c:out value="${fn:substring(report.name, 0, 120)}" />
                                        ... </c:if>
                                      <c:if test="${fn:length(report.name) < 120}" >
                                        <c:out value="${report.name}" />
                                      </c:if>
                                    </p>
                                    </b> </digi:link>
                                <logic:present name="report" property="reportDescription" >
                                  <p style="max-width: 400px;white-space: normal" title="${report.reportDescription}">
                                    <c:if test="${fn:length(report.reportDescription) > 120}" >
                                      <c:out value="${fn:substring(report.reportDescription, 0, 120)}" />
                                      ... </c:if>
                                    <c:if test="${fn:length(report.reportDescription) < 120}" >
                                      <c:out value="${report.reportDescription}" />
                                    </c:if>
                                  </p>
                                </logic:present>
                              </td>
                              <td style="text-transform:capitalize">
                                  <%-- Report Types --%>
                                  <%-- Donor:1 --%>
                                  <%-- Regional:2 --%>
                                  <%-- Component:3 --%>
                                  <%-- Contribution:4 --%>
                                  <%-- Pledge:5 --%>
                                  <c:choose>
                                    <c:when test="${report.type == 1}">
                                      <digi:trn>donor</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.type == 2}">
                                      <digi:trn>component</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.type == 3}">
                                      <digi:trn>regional</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.type == 4}">
                                      <digi:trn>contribution</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.type == 5}">
                                      <digi:trn>pledge</digi:trn><br />
                                    </c:when>
                                  </c:choose>
                                  <%-- Report Options --%>
                                  <%-- Monthly:M --%>
                                  <%-- Quarterly:Q --%>
                                  <%-- Annual:A --%>
                                  <c:choose>
                                    <c:when test="${report.options eq 'M'}">
                                        <digi:trn key="aim:monthlyreport">Monthly</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.options eq 'Q'}">
                                        <digi:trn key="aim:quarterlyreport">Quarterly</digi:trn><br />
                                    </c:when>
                                    <c:when test="${report.options eq 'A'}">
                                        <digi:trn key="aim:annualreport">Annual</digi:trn><br />
                                    </c:when>
                                  </c:choose>
                                  <c:if test="${report.hideActivities}">
                                    <digi:trn>Summary Report</digi:trn>
                                  </c:if>
                              </td>
                              <td align="center">
                              	<c:choose>
                                	<c:when test="${empty report.filterDataSet}">
	                                    <img src="/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
                                    </c:when>
                                    <c:otherwise>
	                                    <img src="/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
                                    </c:otherwise>
                                </c:choose>
                              </td>
                              <td style="text-transform:capitalize;">
                                  <c:forEach var="hierarchy" items="${report.hierarchies}">
                                    <digi:trn key="aim:report:${hierarchy.column.columnName}">${hierarchy.column.columnName}</digi:trn><br />
                                  </c:forEach>&nbsp;
                              </td>
                              <td width="200" style="text-transform:capitalize;">
                                  <c:forEach var="column" items="${report.columns}">
                                      <digi:trn key="aim:report:${column.column.columnName}">${column.column.columnName}</digi:trn><br />
                                  </c:forEach>
                                </td>
                              <td style="text-transform:capitalize;">
                                  <c:forEach var="measure" items="${report.measures}">
                                      <digi:trn key="aim:reportBuilder:${measure.measure.aliasName}"> ${measure.measure.aliasName} </digi:trn> <br />
                                  </c:forEach>
							  </td>
                              <td align="center" style="border:0px none;"><p style="white-space: nowrap">
                                  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
                                  
                                  <c:set target="${urlParams}" property="rid">
                                    <bean:write name="report" property="ampReportId" />
                                  </c:set>
                                  <c:set target="${urlParams}" property="event" value="edit" />
                                    <c:set var="translation">
                                        <digi:trn>Get report in Excel format&nbsp;</digi:trn>
                                    </c:set>
                                    <digi:link href="/xlsExport.do?ampReportId=${report.ampReportId}" title="${translation}"><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/icons/xls.gif" border="0"/></digi:link>
                                    &nbsp;
                                    <c:set var="translation">
                                        <digi:trn>Get report in PDF format&nbsp;</digi:trn>
                                    </c:set>
                                    <digi:link href="/pdfExport.do?ampReportId=${report.ampReportId}" title="${translation}"><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/icons/pdf.gif" border="0"/></digi:link>
                                    <feature:display name="Show Printer Friendly option" module="Public Reports">
	                                    &nbsp;
	                                    <c:set var="translation">
	                                        <digi:trn>Get report in printer friendly version&nbsp;</digi:trn>
	                                    </c:set>
	                                    <digi:link href="/viewNewAdvancedReport.do?viewFormat=print&widget=false"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');" title="${translation}"><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0"/></digi:link>
                                    </feature:display>
                                </p>
                                </td>
                            </tr>
                          </logic:iterate>
                          </tbody>
                        </table></td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td valign="top">
                  <c:if test="${reportNumber != 0}">
                    <c:if test="${aimTeamReportsForm.totalPages ne '1'}">
  	                  <table cellpadding="3" cellspacing="3" border="0" width="100%" height="20">
                      <tr>
                        <td><c:set target="${urlParamsPagination}" property="action" value="getPage"/>
                          <c:if test="${aimTeamReportsForm.currentPage >0}">
                            <c:set target="${urlParamsFirst}" property="page" value="0"/>
                            <c:set target="${urlParamsFirst}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsFirst}" property="tabs" value="true"/>
                            </c:if>
                            <c:set var="translation">
                              <digi:trn key="aim:firstpage">First Page</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  > &lt;&lt; </digi:link>
                            <c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}"/>
                            <c:set target="${urlParamsPrevious}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsPrevious}" property="tabs" value="true"/>
                            </c:if>
                            |
                            <c:set var="translation">
                              <digi:trn key="aim:previous">Previous</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
                              <digi:trn key="aim:previous">Previous</digi:trn>
                            </digi:link>
                            | </c:if>
                          <c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
                          <c:set var="start" value="${aimTeamReportsForm.offset}"/>
                          <c:if test="${maxpages > 1}">
                            <logic:iterate name="pagelist" id="pageidx" type="java.lang.Integer" offset="${start}" length="${length}">
                              <c:set target="${urlParamsPagination}" property="page" value="${pageidx - 1}"/>
                              <c:if test="${(pageidx - 1) eq actualPage}">
                                <bean:write name="pageidx"/>
                              </c:if>
                              <c:if test="${(pageidx - 1) ne actualPage}">
                                <c:if test="${aimTeamReportsForm.showTabs}">
                                  <c:set target="${urlParamsPagination}" property="tabs" value="true"/>
                                </c:if>
                                <digi:link href="/viewTeamReports.do"  name="urlParamsPagination" >
                                  <bean:write name="pageidx"/>
                                </digi:link>
                              </c:if>
                              <c:if test="${pageidx < maxpages}"> | </c:if>
                            </logic:iterate>
                          </c:if>
                          <c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
                            <c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
                              <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}"/>
                            </c:if>
                            <c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
                              <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}"/>
                            </c:if>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsNext}" property="tabs" value="true"/>
                            </c:if>
                            <c:set target="${urlParamsNext}" property="action" value="getPage"/>
                            <c:set var="translation">
                              <digi:trn key="aim:nextpage">Next Page</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
                              <digi:trn key="aim:next">Next</digi:trn>
                            </digi:link>
                            |
                            <c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}"/>
                            <c:set target="${urlParamsLast}" property="action" value="getPage"/>
                            <c:if test="${aimTeamReportsForm.showTabs}">
                              <c:set target="${urlParamsLast}" property="tabs" value="true"/>
                            </c:if>
                            <c:set var="translation">
                              <digi:trn key="aim:lastpage">Last Page</digi:trn>
                            </c:set>
                            <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  > &gt;&gt; </digi:link>
                            &nbsp;&nbsp; </c:if>
                          <c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>
                          &nbsp;
                          <digi:trn key="aim:of">of</digi:trn>
                          &nbsp;
                          <c:out value="${aimTeamReportsForm.totalPages}"></c:out>
                        </td>
                      </tr>
                    </table>
                    </c:if>
                  </c:if>
                </td>
              </tr>
            </table>
             </digi:form>
            <br />
            <br />
            <br />
			<div align="center">
            <TABLE cellpadding="3" cellspacing="2" style="border:2px solid #CCCCCC; background-color:#EEEEEE;">
            <TR>
              <TD COLSPAN="2">
              	<strong>
                <digi:trn>Icons Reference</digi:trn>
                </strong>
              </TD>
            </TR>
              <TR>
                <TD><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/icons/xls.gif" border="0"/>
                  <digi:trn>Click on this icon to get report in Excel format&nbsp;</digi:trn>
                  <br />
                </TD>
                <TD><img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
                  <digi:trn>Filtered Report&nbsp;</digi:trn>
                  <br />
                </TD>
              </TR>
              <TR>
                <TD><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/icons/pdf.gif" border="0"/>
                  <digi:trn>Click on this icon to get report in PDF format&nbsp;</digi:trn>
                  <br />
                </TD>
                <TD><img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
                  <digi:trn>Not filtered Report&nbsp;</digi:trn>
                  <br />
                </TD>
              </TR>
              <feature:display name="Show Printer Friendly option" module="Public Reports">
	              <TR>
	                <TD><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0"/>
	                  <digi:trn>Click on this icon to get report in printer friendly format&nbsp;</digi:trn>
	                  <br />
	                </TD>
	                <TD>&nbsp;</TD>
	              </TR>
			 </feature:display>
          </TABLE>
            </div>
            <br />
            <br />
          </td>
        </tr>
      </table></td>
  </tr>
</table>

</div>
</gs:test>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.PUBLIC_VIEW %>" compareWith="Off" onTrueEvalBody="true">
<br/>
</gs:test>
