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

<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
	<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
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
  
  <c:set var="titleColumn">
    <digi:trn> Report Title </digi:trn>
  </c:set>
</c:if>

<hr />
<!-- MAIN CONTENT PART START -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
    	<td width="740" valign="top">	
    		<div class="wlcm_txt_menu">
    			<a href="/reportsPublicView.do" style="text-decoration: underline;cursor: pointer;">
    				<digi:trn>
    					Public Tab's
    				</digi:trn>
    			</a>
    			<span class="wlcm_txt_menu_spc">|</span>
    			<a onClick="showAbout(); return false;" style="text-decoration: underline;cursor: pointer;">
    				<digi:trn>
    					About AMP
    				</digi:trn>
    			</a>
    			<span class="wlcm_txt_menu_spc">|</span>
    			<b class="sel_mid">
    				<digi:trn>
    					Reports
    				</digi:trn>
    			</b>
    			<span class="wlcm_txt_menu_spc">|</span>
    			<a href="/contentrepository/publicDocTabManager.do?action=publicShow" module="contentrepository" onclick="return quitRnot()">
    				<digi:trn>
    					Resources
    				</digi:trn>
    			</a><span class="wlcm_txt_menu_spc">|</span>
    			<a href=#>
    				<digi:trn>
    					Donor Profiles
    				</digi:trn>
    			</a>
    			<span class="wlcm_txt_menu_spc">|</span>
    				<a href=#>
    					<digi:trn>
    						Aid Map
    					</digi:trn>
    					</a>
    			<span class="wlcm_txt_menu_spc">|</span>
    				<a href=#>
    					<digi:trn>
    						Contact us
    					</digi:trn>		
    				</a>
    		</div>
		<table class="inside" width="100%" cellpadding="0" cellspacing="0">
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
			                <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}">
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
  <tr>
    <td valign="top">
    	<table class="inside" width="100%" cellpadding="0" cellspacing="0">
    		<thead>
	              <tr>
	              	<td background="img_2/ins_bg.gif" class="inside" width="450px">
	              		<b class="ins_title">${titleColumn}</b>
	              	</td>
	                <td background="img_2/ins_bg.gif" class="inside" align="center">
	                 	<b class="ins_title"><digi:trn>Type</digi:trn></b>
	                 </td>
	                <td background="img_2/ins_bg.gif" class="inside" align="center">
	                	<b class="ins_title"><digi:trn>Filtered</digi:trn></b> 
	                </td>
	                <td background="img_2/ins_bg.gif" class="inside" align="center">
	                	<b class="ins_title"><digi:trn>Hierarchies</digi:trn></b> 
	                </td>
	                 <td background="img_2/ins_bg.gif" class="inside" align="center">
	                	<b class="ins_title"><digi:trn>Columns</digi:trn></b> 
	                </td>
	                 <td background="img_2/ins_bg.gif" class="inside" align="center">
	                	<b class="ins_title"><digi:trn>Measures</digi:trn></b> 
	                </td>
	                 <td background="img_2/ins_bg.gif" class="inside" align="center">
	                	<b class="ins_title">Export Options</b>
	                </td>
	              </tr>
              </thead>
              <tbody>
              <c:if test="${reportNumber == 0}">
              <tr>
                <td colspan="7">
                	<b class="ins_title"><digi:trn key="aim:noreportspresent"> No reports present </digi:trn></b>
                </td>
              </tr>
              </c:if>
              <logic:iterate name="aimTeamReportsForm"  property="reportsList" id="report" indexId="idx"
                type="org.digijava.module.aim.dbentity.AmpReports">
                <tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');">
                  <td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="inside" style="font-size: 11px">
                      <digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');" title="<digi:trn>Click here to view the Report</digi:trn>"> <b>
                        <div class="t_sm" title="${report.name}">
                          <c:if test="${fn:length(report.name) > 120}" >
                            <c:out value="${fn:substring(report.name, 0, 120)}"/>... 
                           </c:if>
                          <c:if test="${fn:length(report.name) < 120}" >
                            <c:out value="${report.name}" />
                          </c:if>
                        </b> 
                     </digi:link>
                     <br>
                    <logic:present name="report" property="reportDescription" >
                      <c:if test="${fn:length(report.reportDescription) > 120}" >
                          <c:out value="${fn:substring(report.reportDescription, 0, 120)}" />... </c:if>
                        <c:if test="${fn:length(report.reportDescription) < 120}" >
                          <c:out value="${report.reportDescription}" />
                        </c:if>
                    </logic:present>
                    </div>
                  </td>
                  <td class="inside" align="center" style="font-size: 11px">
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
                  <td align="center" class="inside" style="font-size: 11px">
                  	<c:choose>
                    	<c:when test="${empty report.filterDataSet}">
                         <img src="/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
                        </c:when>
                        <c:otherwise>
                         <img src="/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
                        </c:otherwise>
                    </c:choose>
                  </td>
                  <td align="center" class="inside" style="font-size: 11px">
                      <c:forEach var="hierarchy" items="${report.hierarchies}">
                        <digi:trn key="aim:report:${hierarchy.column.columnName}">${hierarchy.column.columnName}</digi:trn><br />
                      </c:forEach>&nbsp;
                  </td>
                  <td align="center" class="inside" style="font-size: 11px">
                      <c:forEach var="column" items="${report.columns}">
                          <digi:trn key="aim:report:${column.column.columnName}">${column.column.columnName}</digi:trn><br />
                      </c:forEach>
                    </td>
                  <td align="center" class="inside" style="font-size: 11px">
                      <c:forEach var="measure" items="${report.measures}">
                          <digi:trn key="aim:reportBuilder:${measure.measure.aliasName}"> ${measure.measure.aliasName} </digi:trn> <br />
                      </c:forEach>
					</td>
                     <td align="center" class="inside" style="font-size: 11px">
                     <p style="white-space: nowrap">
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
                         <digi:link href="/viewNewAdvancedReport.do?viewFormat=print&widget=false"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');" title="${translation}"><digi:img  hspace="0" vspace="0" height="16" width="16" src="/TEMPLATE/ampTemplate/img_2/ico-print.png" border="0"/></digi:link>
                        </feature:display>
                    </p>
                    </td>
                </tr>
              </logic:iterate>
              </tbody>
            </table>

     </td>
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
<br/>
	<hr />
	<div class="t_sm">
		<b>Icons Reference</b>
		<br/>
		<img src="img_2/ico_exc.gif">&nbsp;&nbsp;Click on this icon to get report in Excel format &nbsp;&nbsp;|&nbsp;&nbsp;
		<img src="img_2/ico_pdf.gif">&nbsp;&nbsp;Click on this icon to get report in PDF format
	</div>
	</td>
</tr>
</table>

            
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.PUBLIC_VIEW %>" compareWith="Off" onTrueEvalBody="true">
<br/>
</gs:test>
