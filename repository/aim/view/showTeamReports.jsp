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

<%@ page language="java" import="org.digijava.module.aim.helper.TeamMember" %>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script language="JavaScript1.2" type="text/javascript"
  src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
  
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<digi:instance property="aimTeamReportsForm" />

<c:if test="${aimTeamReportsForm.showTabs}">

	<c:set var="translation">
		<digi:trn key="aim:confirmDeleteTab">
			Are you sure you want to delete the selected desktop tab ?
		</digi:trn>
	</c:set>
	<c:set var="pageTitle">
		<digi:trn key="aim:teamTabs">
			Team Tabs
		</digi:trn>
	</c:set>
	<c:set var="breadCrumb">
		<digi:trn key="aim:AllTabs">All Tabs</digi:trn>
	</c:set>
	<c:set var="titleColumn">
		<digi:trn key="aim:tabTitle">
			Tab Title
		</digi:trn>
	</c:set>
	<c:set var="generator">
		<digi:trn key="aim:tabGenerator">Tab Generator</digi:trn>
	</c:set>
	
	<jsp:include page="tabManager/tabManager.jsp" />
	
</c:if>
<digi:form action="/viewTeamReports.do" method="post">

<c:if test="${!aimTeamReportsForm.showTabs}">
	<c:set var="translation">
		<digi:trn>
			Are you sure you want to delete the selected report ?
		</digi:trn>
	</c:set>
	<c:set var="pageTitle">
		<digi:trn>
			Report Manager
		</digi:trn>
	</c:set>
	<c:set var="breadCrumb">
		<digi:trn>
			Report Manager
		</digi:trn>
   	</c:set>
	<c:set var="titleColumn">
		<digi:trn>
			Report Title
		</digi:trn>
	</c:set>
	<c:set var="generator">
		<digi:trn>Report Generator</digi:trn>
	</c:set>
</c:if>

<SCRIPT TYPE="text/javascript">
function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;

  	myWindow	= window.open('',windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
  	myWindow.document.write("<html>");
	myWindow.document.write("<div style='height: 20px; left: 45%; position: absolute; text-align: center; top: 0%;width: 230px;padding: 5px;background-color:#27415F;font-family: arial; font-size: 14px;text-align: center;font-weight:bold;color: white;'>");
	myWindow.document.write("Loading step 1/3. Please wait ...");
	myWindow.document.write("<div><html>");
	myWindow.focus();
	myWindow.location	= href;
	

return false;
}

function confirmFunc() {
  return confirm("${translation}");
}
</SCRIPT>

<jsp:include page="teamPagesHeader.jsp"  />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
  <tr>
    <td width=14>&nbsp;</td>

    <td align=left vAlign=top width=750>

      <table cellPadding=5 cellSpacing=0 width="100%">
        <tr>
        <td valign="bottom" class="crumb" >
        <c:set var="translation">
          <digi:trn>Click here to view MyDesktop</digi:trn>
        </c:set>
          <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
            <digi:trn>My Desktop</digi:trn>
          </digi:link> &gt; ${breadCrumb}</td></tr>
        <tr>
          <td height=16 align="left" vAlign="center">
          	<digi:errors/>
          	<span class=subtitle-blue>
            	${pageTitle}
            </span>
          </td>
        </tr>
        <tr>
        	<td>
        		<c:if test="${aimTeamReportsForm.showTabs}">
					<a style="cursor:pointer; text-decoration:underline; color: #05528b" onclick="initializeTabManager()">
						<b><digi:trn>Manage Active Tabs</digi:trn></b>
					</a>
				</c:if>
        	</td>
        </tr>
         <tr>
         	<td>
          	<table cellpadding="6" cellspacing="6">
               <tr>
               <td><digi:trn>Report Title</digi:trn>: <html:text property="keyword"/> </td> <td> <html:submit property="action"><digi:trn>Search</digi:trn></html:submit></td> <td><html:submit property="action"><digi:trn>Clear</digi:trn></html:submit></td>
               </tr>
              </table>
             </td>
         </tr>
         <tr>
          <td noWrap width=1000 vAlign="top">
            <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
              <tr>
                <td>
                <c:set var="reportNumber" value="${fn:length(aimTeamReportsForm.reports)}">
                  </c:set>
                  <c:if test="${reportNumber != 0}">
                  <table cellpadding="3" cellspacing="3" border="0" width="100%" height="20">
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
                          <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
                            &lt;&lt;
                          </digi:link>
                          
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
                          | 
                        </c:if>
                        
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
                          <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  >
                            &gt;&gt; 
                          </digi:link>
                          &nbsp;&nbsp; 
                        </c:if>
                        <c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>
                      </td>
                    </tr>
                  </table>
                  </c:if>
                </td>
              </tr>
              <tr bgColor=#f4f4f2>
                <td valign="top">
                  <table align=center cellPadding=0 cellSpacing=0 width="100%">
                    <tr>                    
                      <td>
                        <table border=0 cellPadding=3 cellSpacing=3 width="100%" >
                          <tr bgColor=#999999>
                            <td bgColor=#999999 align="center" height="20">
                                <c:if test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=1}">
                                    <digi:link href="/viewTeamReports.do?sortBy=1">
                                        <b><digi:trn>${titleColumn}</digi:trn></b>
                                    </digi:link>
                                   <c:if test="${aimTeamReportsForm.sortBy==2}"><img src="/repository/aim/images/down.gif" alt="down"/></c:if>
                                </c:if>
                                <c:if test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==1}">
                                    <digi:link href="/viewTeamReports.do?sortBy=2">
                                        <b><digi:trn key="aim:organizationName">${titleColumn}</digi:trn></b>
                                    </digi:link>
                                      <img  src="/repository/aim/images/up.gif" alt="up"/>
                                </c:if>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <c:if test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=3}">
                                  <digi:link href="/viewTeamReports.do?sortBy=3">
                                      <digi:trn key="aim:reportOwnerName">
                                          Owner
                                      </digi:trn>
                                  </digi:link>
                                   <c:if test="${aimTeamReportsForm.sortBy==4}"><img src="/repository/aim/images/down.gif" alt="down"/></c:if>
                                </c:if>
                                <c:if test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==3}">
                                    <digi:link href="/viewTeamReports.do?sortBy=4">
                                       <digi:trn key="aim:reportOwnerName">
                                          Owner
                                      </digi:trn>
                                    </digi:link>
                                      <img  src="/repository/aim/images/up.gif" alt="up"/>
                                </c:if>
                            
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                                    <c:if test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=5}">
                                  <digi:link href="/viewTeamReports.do?sortBy=5">
                                      <digi:trn key="aim:reportCreationDate">
                                          Creation Date
                                      </digi:trn>
                                  </digi:link>
                                   <c:if test="${aimTeamReportsForm.sortBy==6}"><img src="/repository/aim/images/down.gif" alt="down"/></c:if>
                                </c:if>
                                <c:if test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==5}">
                                    <digi:link href="/viewTeamReports.do?sortBy=6">
                                        <digi:trn key="aim:reportCreationDate">
                                            Creation Date
                                        </digi:trn>
                                    </digi:link>
                                      <img  src="/repository/aim/images/up.gif" alt="up"/>
                                </c:if>
                             
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn key="aim:reportType">
                                Type 
                              </digi:trn>
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn>
                              Filtered
                              </digi:trn>
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn key="aim:hierarchies">
                              Hierarchies
                              </digi:trn>
                              </b>
                            </td>
                            <% String s = (String)session.getAttribute("teamLeadFlag");
                               TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                             if(tm!=null)
                              {
                            %>
                              <td bgColor=#999999 align="center" height="20">&nbsp;
                              
                              </td>
                              <td bgColor=#999999 align="center" height="20">
                                <b>
                                <digi:trn key="aim:reportAction">
                                  Action
                                </digi:trn>
                                </b>
                              </td>
                            <% } %>
                          </tr>                          
                          <c:if test="${reportNumber == 0}">
								<c:if test="${!aimTeamReportsForm.showTabs}">
		                          <tr>
		                            <td colspan="4">
		                            <digi:trn key="aim:noreportspresent">
		                            	No reports present
		                            </digi:trn>
		                            </td>
		                          </tr>
								</c:if>
								<c:if test="${aimTeamReportsForm.showTabs}">
		                          <tr>
		                            <td colspan="4">
		                            <digi:trn key="aim:notabspresent">
		                            	No tabs present
		                            </digi:trn>
		                            </td>
		                          </tr>
								</c:if>

                          </c:if>
                          <logic:iterate name="aimTeamReportsForm"  property="reportsList" id="report" indexId="idx"
                            type="org.digijava.module.aim.dbentity.AmpReports">
                              <tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
                              onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
                              <td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                              <c:if test="${!aimTeamReportsForm.showTabs}">
	                              <digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" styleClass="h-box" onclick="return popup(this,'');" title="Click here to view the Report">
	                              <b>
	                                <p style="max-width: 400px;white-space: normal" title="${report.name}">
									<c:if test="${fn:length(report.name) > 120}" >
										<c:out value="${fn:substring(report.name, 0, 120)}" />...
									</c:if>
									<c:if test="${fn:length(report.name) < 120}" >
										<c:out value="${report.name}" />
									</c:if>
	                                </p>  
	                              </b>
	                              </digi:link>
	                          </c:if>
	                          <c:if test="${aimTeamReportsForm.showTabs}">
	                          	<b>
	                                <p style="max-width: 400px;white-space: normal" title="${report.name}">
									<c:if test="${fn:length(report.name) > 120}" >
										<c:out value="${fn:substring(report.name, 0, 120)}" />...
									</c:if>
									<c:if test="${fn:length(report.name) < 120}" >
										<c:out value="${report.name}" />
									</c:if>
	                                </p>  
	                              </b>
	                          </c:if>
                             
                              <logic:present name="report" property="reportDescription" >
                                <p style="max-width: 400px;white-space: normal" title="${report.reportDescription}">
								<c:if test="${fn:length(report.reportDescription) > 120}" >
									<c:out value="${fn:substring(report.reportDescription, 0, 120)}" />...
								</c:if>
								<c:if test="${fn:length(report.reportDescription) < 120}" >
									<c:out value="${report.reportDescription}" />
								</c:if>
                                </p>
                              </logic:present>
                              </td>

                              <td align="center">
                                <p style="white-space: nowrap">
                                <logic:present name="report" property="ownerId">
                                   <i><bean:write name="report" property="ownerId.user.name" /></i>
                                </logic:present>
                                </p>
                              </td>
                              <td align="center">
                                <p style="white-space: nowrap">
                                  <logic:present name="report" property="updatedDate">
                                      <bean:write name="report" property="formatedUpdatedDate" />
                                  </logic:present>
                                </p>
                              </td>
                              <td>
                                <p style="white-space: nowrap">
                                  <li>
                                      <%
                                        if (report.getType()!=null && report.getType().equals(new Long(1))) {
                                      %>
                                          <digi:trn key="aim:donorType">donor</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long (3))){
	                                  %>
                                          <digi:trn key="aim:regionalType">regional</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long(2))){
	                                  %>
                                          <digi:trn key="aim:componentType">component</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long(4))){
	                                  %>
                                          <digi:trn key="aim:contributionType">contribution</digi:trn>
                                       <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long(5))){
	                                  %>
	                                  	 <digi:trn>pledge</digi:trn>
	                                  <%}%>
	                              </li>
                                  <logic:equal name="report" property="drilldownTab" value="true">
                                    <li>
                                      <digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="publicReport" value="true">
                                    <li>
                                      <digi:trn key="aim:typePublicReport">Public Report</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="hideActivities" value="true">
                                    <li>
                                      <digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
                                    </li>
                                  </logic:equal>                                  
                                  <logic:equal name="report" property="options" value="A">
                                    <li>
                                    	<digi:trn key="aim:annualreport">Annual</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="options" value="Q">
                                    <li>
                                    	<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="options" value="M">
                                    <li>
                                    	<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
                                    </li>
                                  </logic:equal>
                                </p>
                              </td>
							<td align="center">
	                              <logic:notEmpty name="report" property="filterDataSet">
	                                  <img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
	                              </logic:notEmpty>
	                              <logic:empty name="report" property="filterDataSet">
	                                   <img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
	                              </logic:empty>
                              </td>
                              <td>
                                <logic:iterate name="report" property="hierarchies" id="hierarchy" >
                                  <%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
                                  <li>
                                  
	                                      	<digi:trn key="aim:report:${hierarchy.column.columnName}">
	                                        	<bean:write name="hierarchy" property="column.columnName" />
	                                      	</digi:trn>
                                  
                                  </li>
                                </logic:iterate>
                              </td>
                              
                              <%
                                if(tm!=null){
                              %>

                              <td width="200">  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="ampReportId"/>'> 
	                                  <logic:iterate name="report" property="columns" id="column" indexId="index"  >
	                                    <%if (index.intValue()%2==0){ %>
	                                      <li>                                      
	                                      	<digi:trn key="aim:report:${column.column.columnName}">
	                                        	<bean:write name="column" property="column.columnName" />
	                                      	</digi:trn>
	                                    <% } else {%>
	                                      ,
	                                      	<digi:trn key="aim:report:${column.column.columnName}">
	                                        	<bean:write name="column" property="column.columnName" />
	                                      	</digi:trn>
	                                      </li>
	                                    <%} %>
	                                  </logic:iterate>
	                                </div>
	                                                                
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn>Columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn>Columns</digi:trn></u> ]&nbsp;
	                                </span>
                                
	                                <div style='position:relative;display:none;' id='measure-<bean:write name="report" property="ampReportId"/>'> 
	                                  <logic:iterate name="report" property="measures" id="measure" indexId="index"  >
	                                    <li>
	                                    	<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">                                      
	                                      		${measure.measure.aliasName}
	                                      	</digi:trn>
	                                    </li>
	                                  </logic:iterate>
	                                </div>
	                                
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
                                </td>
                                <td align="center">
	                                <p style="white-space: nowrap">
	                                <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
	                                <c:set target="${urlParams}" property="rid">
	                                  <bean:write name="report" property="ampReportId" />
	                                </c:set>
	                                <c:set target="${urlParams}" property="event" value="edit" />
	                                <logic:equal name="teamLeadFlag" scope="session" value="true"> 
                                      	<c:set var="translation">
	                                      	<c:if test="${aimTeamReportsForm.showTabs}">
		                                        <digi:trn key="aim:ClickEditTab">Click on this icon to edit tab&nbsp;</digi:trn>
	                                      	</c:if>
	                                      	<c:if test="${!aimTeamReportsForm.showTabs}">
	    	                                    <digi:trn key="aim:ClickEditReport">Click on this icon to edit report&nbsp;</digi:trn>
	                                      	</c:if>
                                        </c:set>
	                                    <digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" title="${translation}">
	                                      <img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
	                                    </digi:link> 
                                      &nbsp;
                                      <c:set var="translation">
                                      	<c:if test="${aimTeamReportsForm.showTabs}">
	                                        <digi:trn key="aim:ClickDeleteTab">Click on this icon to delete tab&nbsp;</digi:trn>
											<c:set target="${urlParams}" property="isTab" value="1" />
                                      	</c:if>
                                      	<c:if test="${!aimTeamReportsForm.showTabs}">
	                                        <digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>
											<c:set target="${urlParams}" property="isTab" value="0" />
                                      	</c:if>
                                       </c:set>
                                      <digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" title="${translation}">
										<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
	                                  </digi:link> 
	                                  
	                                </logic:equal>                            
	                                <logic:equal name="teamLeadFlag" scope="session" value="false">
	                                  <logic:present name="report" property="ownerId">
	                                    <logic:equal  name="report" property="ownerId.ampTeamMemId" value="${aimTeamReportsForm.currentMemberId}"> 
	                                        <c:set var="translation">
	                                      		<c:if test="${aimTeamReportsForm.showTabs}">
		                                       		<digi:trn key="aim:ClickEditTab">Click on this icon to edit tab&nbsp;</digi:trn>
	                                      		</c:if>
	                                      		<c:if test="${!aimTeamReportsForm.showTabs}">
	    	                                    	<digi:trn key="aim:ClickEditReport">Click on this icon to edit report&nbsp;</digi:trn>
	                                      		</c:if>
                                        	</c:set>
	                                    	<digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" title="${translation}">
	                                      		<img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
	                                    	</digi:link>

		                                    <c:set var="translation">
	                                      		<c:if test="${aimTeamReportsForm.showTabs}">
		                                        	<digi:trn key="aim:ClickDeleteTab">Click on this icon to delete tab&nbsp;</digi:trn>
													<c:set target="${urlParams}" property="isTab" value="1" />
	                                      		</c:if>
	                                      		<c:if test="${!aimTeamReportsForm.showTabs}">
		                                        	<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>
													<c:set target="${urlParams}" property="isTab" value="0" />
	                                      		</c:if>
	                                       	</c:set>
	                                       	<digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" title="${translation}">
												<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
		                                  	</digi:link>
	                                    </logic:equal>    
	                                  </logic:present>                                                                                                
	                                </logic:equal>                              
	                                </p>
                                
                              </td>
                              <% } %>   
                              
                          </tr>
                          </logic:iterate>
                        </table>
                      </td>
                    </tr>      
                  </table>
                </td>
              </tr>
              <tr><td valign="top">
              <c:if test="${reportNumber != 0}">
                  <table cellpadding="3" cellspacing="3" border="0" width="100%" height="20">
                    <tr>
                      <td>                                     
                        <c:set target="${urlParamsPagination}" property="action" value="getPage"/>
                        <c:if test="${aimTeamReportsForm.currentPage >0}">
                          <c:set target="${urlParamsFirst}" property="page" value="0"/>
                          <c:set target="${urlParamsFirst}" property="action" value="getPage"/>
                          <c:if test="${aimTeamReportsForm.showTabs}">
                          	<c:set target="${urlParamsFirst}" property="tabs" value="true"/>
                          </c:if>
                          <c:set var="translation">
                            <digi:trn key="aim:firstpage">First Page</digi:trn>
                          </c:set>
                          <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
                            &lt;&lt;
                          </digi:link>
                          
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
                          | 
                        </c:if>
                        
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
                          <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  >
                            &gt;&gt; 
                          </digi:link>
                          &nbsp;&nbsp; 
                        </c:if>
                        <c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>
                      </td>
                    </tr>
                  </table>
                  </c:if>
              </td></tr>
            </table>
             </digi:form>
			<logic:present name="isUserLogged" scope="session">          
            <TABLE>
               <TR>
                   <TD COLSPAN="2">
                   <strong><digi:trn key="aim:IconReference">Icons Reference</digi:trn></strong>
               </TD>
               </TR>
<c:if test="${!aimTeamReportsForm.showTabs}">
               <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn key="aim:ClickEditReport">Click on this icon to edit report&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
                <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
				<TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn>Filtered Report&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
                <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn>Not filtered Report&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
</c:if>
<c:if test="${aimTeamReportsForm.showTabs}">
               <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn key="aim:ClickEditTab">Click on this icon to edit tab&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
                <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn key="aim:ClickDeleteTab">Click on this icon to delete tab&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
				<TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn>Filtered tab&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
                <TR>
                   <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="absmiddle" />
                       <digi:trn>Not filtered tab&nbsp;</digi:trn>
                       <br />
               </TD>
               </TR>
</c:if>
           </TABLE>
           </logic:present>

          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<img src='/TEMPLATE/ampTemplate/images/ajax-loader.gif' style="display: none;"/>


