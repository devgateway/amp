<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
  <!-- Dependencies --> 
  
        <script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="script/yui/dom-min.js"/>"></script>        
		<script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script> 

		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>
		

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />


		<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>



<style>
.link{
	text-decoration: none;
	font-size: 8pt; font-family: Tahoma;
}

.reportname{
	background:#bbe0e3; 
	width: 100px;
	margin-top: 20px;
	margin-left: 5px;
}
</style>
<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>
<script language="JavaScript">
function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings} &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings} &lt;&lt;";
	}
}
	
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>



<div id="mySorter" style="display: none">
	<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
        <!--
		<a href='#' onclick='hideSorter();return false'>
			<b>
				<digi:trn key="rep:pop:Close">Close</digi:trn>
			</b>
		</a>
		 -->
	</div>
<%
Integer counter = (Integer)session.getAttribute("progressValue");
counter++;
session.setAttribute("progressValue", counter);
%>

<logic:notEqual name="viewFormat" scope="request" value="print">
<div id="myFilterWrapper" style="display: none;" >
	<div id="myFilter" style="display: none; height: 100%; overflow: hidden;" >
		<jsp:include page="/aim/reportsFilterPicker.do">
		 <jsp:param name="init" value=""/>
		</jsp:include>
	</div>
	<div id="myRange" style="display: none">
	          <jsp:include page="/repository/aim/view/ar/RangePicker.jsp" />
	</div>
	<div id="customFormat" style="display: none">
	          <jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
	</div>
</div>
</logic:notEqual>

<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />
<%
counter++;
session.setAttribute("progressValue", counter);
%>

<table width="100%">
<tr>
	<td>
<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<jsp:include page="/repository/aim/view/ar/toolBar.jsp" />
		
		<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>
		<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page" />
		
		<logic:notEqual name="widget" scope="request" value="true">
			<div class="reportname" style="background: #222E5D">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  			<tr>
		    			<td align="left">
		    				<img src="images/tableftcorner.gif"/>
		    			</td>
		    			<td align="center" nowrap="nowrap" style="background:#222E5D; font-family: Arial;color:white;font-weight: bold;">
		    				<bean:write scope="session" name="reportMeta" property="name" />
		    			</td>
		    			<td align="right">
		    				<img src="images/tabrightcorner.gif" />
		    			</td>
		  			</tr>
				</table>
			</div>
		</logic:notEqual>
		
		<table width="100%" cellpadding="3" cellspacing="1" rules="rows" frame="box" style="margin-left: 5px;border-color:#999999;">
			<logic:notEmpty property="reportDescription" name="reportMeta">
			<tr style="border-top:#222E5D 3px solid;">
				<td style="padding-left: 5px;padding-left: 5px;">
					<digi:trn key="rep:pop:Description">Description:</digi:trn><br>
						<span style="font-weight: bold;font-size: 13px;margin-left: 5px;margin-top: 3px; font-family: Arial">
							<bean:write scope="session" name="reportMeta" property="reportDescription" />
						</span>
				</td>
			</tr>
			</logic:notEmpty>
			<logic:empty property="reportDescription" name="reportMeta">
				<tr style="border-top:#222E5D 3px solid;">
			</logic:empty>
			<logic:notEmpty property="reportDescription" name="reportMeta">
			<tr>
			</logic:notEmpty> 
				<td align="left" height="20px" style="padding-left: 5px;padding-left: 5px;">
					<span  style="color: red;font-family: Arial;font-size: 10px;">
						<%
	                	AmpARFilter af = (AmpARFilter) session.getAttribute("ReportsFilter");
	                	if (af.getAmountinthousand()!=null && af.getAmountinthousand()==true){%>
	               			<digi:trn key="rep:pop:AllAmount">
								Amounts are in thousands (000)
							</digi:trn>
	           			<%}%>
	           			
	           			<%	                	
	                	if (af.getAmountinmillion()!=null && af.getAmountinmillion()==true){%>
	               			<digi:trn key="rep:pop:AllAmount">
								Amounts are in millions (000 000)
							</digi:trn>
	           			<%}%>
						
						<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY%>">
							<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
							<digi:trn key="<%=\"aim:currency:\" + ((String)selCurrency).toLowerCase().replaceAll(\" \", \"\") %>"><%=selCurrency %></digi:trn>
						</logic:present>
					</span>
				</td>
			</tr>
			</table>
	</logic:notEqual>
</logic:notEqual>
	<logic:equal name="viewFormat" scope="request" value="print">
	<script language="JavaScript">
		function load()
		{
			window.print();
		}
		function unload() {}
	</script>
	</logic:equal>
	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
	<tr>
		<td>
		<div style="margin-left:5px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
	        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &gt;&gt;</span>
	        
            <span style="cursor:pointer;float:left;">
            <logic:notEmpty name="reportMeta" property="hierarchies">
            	<c:if test="${!ReportsFilter.publicView}">
	                <a class="settingsLink" onClick="showSorter();">
	                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
	                </a> | 
	            </c:if>
            </logic:notEmpty> 
                <a class="settingsLink" onClick="showFilter(); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <%
               	 	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                	if (arf.isPublicView()==false){%>
                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
	          	 	|
	          	 	<a class="settingsLink" onClick="initSaveReportEngine(false);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}" >
	                	${saveFilters}
	                </a>
                </feature:display>
               <%}%>
               
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  	<%if (arf.isPublicView()==false){%>
           	  	|
				<a  id="frezzlink" class="settingsLinkDisable" style="cursor: default;">
               		<script language="javascript">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
                <%} %>  	
                      	
           	</logic:notEqual>
                
                |<a  class="settingsLink" onClick="showFormat(); " >
                <digi:trn>Report Settings</digi:trn>
                </a>
           
            </span>
             &nbsp;<br>
             <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
             <table cellpadding="0" cellspacing="0" border="0" width="80%">
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
				<strong><digi:trn key="rep:pop:SelectedRange">Selected Range:</digi:trn></strong>
				<c:set var="all" scope="page">
                	<digi:trn key="rep:pop:SelectedRangeAll">All:</digi:trn>
                </c:set>
                
                <digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn> <%=(arf.getRenderStartYear() > 0)?arf.getRenderStartYear():pageContext.getAttribute("all")%> |
                <digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn> <%=(arf.getRenderEndYear() > 0)?arf.getRenderEndYear():pageContext.getAttribute("all")%> |
             </td>
             </tr>
             <tr>
           </table>
           </div>
    	</div>
	</td>
	</tr>
	</logic:notEqual>
	</logic:notEqual>

	<logic:equal name="widget" scope="request" value="true">

	<table width="100%"> 
		<tr>
		<td style="padding-left:-2px;">
		<digi:secure authenticated="false">
			<module:display name="Public Reports and Tabs" parentModule="PUBLIC VIEW">
				<feature:display name="Filters" module="Public Reports and Tabs">
				<div style="width:99%;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
			        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &gt;&gt;</span>
		
		            <span style="cursor:pointer;float:left;">
		            <logic:notEmpty name="reportMeta" property="hierarchies">
		                <a class="settingsLink" onClick="showSorter();">
		                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
		                </a> | 
		            </logic:notEmpty> 
		                <a class="settingsLink" onClick="showFilter(); " >
		                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
		                </a>
		                <%
		                AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
		                if (arf.isPublicView()==false){%>
		                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
			                |
			          	 	<a class="settingsLink" onClick="initSaveReportEngine(true);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}">
			                	${saveFilters}
			                </a>
		           		</feature:display>
		           		<%}%>
		           	  <logic:notEqual name="viewFormat" value="foldable">
		           	  |
		           	  	<a  id="frezzlink" class="settingsLinkDisable">
		               		<script language="	">
						document.write((scrolling)?msg2:msg1);
					</script>
		                </a>
		           	  
		              </logic:notEqual>
		                
		                |<a  class="settingsLink" onClick="showFormat(); " >
		                <digi:trn>Tab Settings</digi:trn>
		                </a>
		           
		            </span>
		             &nbsp;<br>
		             <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
		             <table cellpadding="0" cellspacing="0" border="0" width="80%">
		             <tr>
		             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
					<strong>
					<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
		                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
		                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
		                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
		                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
		                    <jsp:include page="${listable.jspFile}" />
		                </logic:present>
		             </td>
		             </tr>
		             <tr>
		             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
						<strong><digi:trn key="rep:pop:SelectedRange">Selected Range:</digi:trn></strong>
		                    <c:set var="all" scope="page">
		                	<digi:trn key="rep:pop:SelectedRangeAll">All</digi:trn>
		                </c:set>
		                
		            	<i><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn></i> <%=(arf.getRenderStartYear() > 0)?arf.getRenderStartYear():pageContext.getAttribute("all")%> |
		                <i><digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn></i> <%=(arf.getRenderEndYear() > 0)?arf.getRenderEndYear():pageContext.getAttribute("all")%> |
		              </td>
		             </tr>
		           </table>
		           </div>
		    	</div>
				</feature:display>
			</module:display>
		</digi:secure>
		<digi:secure authenticated="true">
			<div style="width:99%;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
	        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &gt;&gt;</span>

            <span style="cursor:pointer;float:left;">
            <logic:notEmpty name="reportMeta" property="hierarchies">
                <a class="settingsLink" onClick="showSorter();">
                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
                </a> | 
            </logic:notEmpty> 
                <a class="settingsLink" onClick="showFilter(); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <%
                AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                if (arf.isPublicView()==false){%>
                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
	                |
	          	 	<a class="settingsLink" onClick="initSaveReportEngine(true);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}">
	                	${saveFilters}
	                </a>
           		</feature:display>
           		<%}%>
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  |
           	  	<a  id="frezzlink" class="settingsLinkDisable">
               		<script language="	">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
           	  
              </logic:notEqual>
                
                |<a  class="settingsLink" onClick="showFormat(); " >
                <digi:trn>Tab Settings</digi:trn>
                </a>
           
            </span>
             &nbsp;<br>
             <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
             <table cellpadding="0" cellspacing="0" border="0" width="80%">
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
				<strong><digi:trn key="rep:pop:SelectedRange">Selected Range:</digi:trn></strong>
                    <c:set var="all" scope="page">
                	<digi:trn key="rep:pop:SelectedRangeAll">All</digi:trn>
                </c:set>
                
            	<i><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn></i> <%=(arf.getRenderStartYear() > 0)?arf.getRenderStartYear():pageContext.getAttribute("all")%> |
                <i><digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn></i> <%=(arf.getRenderEndYear() > 0)?arf.getRenderEndYear():pageContext.getAttribute("all")%> |
              </td>
             </tr>
           </table>
           </div>
    	</div>
		</digi:secure>
	</td>
	</tr>
	<tr>
		<td>
		<table width="100%">
			<tr>
			
			<td align="right">
			<logic:notEmpty name="reportMeta" property="filterDataSet">
			<span style="cursor:pointer;float:left;cursor:pointer;font-style: italic;">
	        	<digi:trn>Please note: Filter(s) have been applied. Click on "Show current settings" to see list of applied filters</digi:trn>
			</span>
			</logic:notEmpty>
			<span style="color: red;font-family: Arial;padding-right: 5px">
				<%
	            AmpARFilter af = (AmpARFilter) session.getAttribute("ReportsFilter");
	            if (af.getAmountinthousand()!=null && af.getAmountinthousand()==true){%>
	            <digi:trn key="rep:pop:AllAmount">
					Amounts are in thousands (000)
				</digi:trn>
	           	<%}%>
				
	           <%	                	
	            if (af.getAmountinmillion()!=null && af.getAmountinmillion()==true){%>
	               			<digi:trn key="rep:pop:AllAmount">
								Amounts are in millions (000 000)
							</digi:trn>
   			   <%}%>				
				
				<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY%>">
					<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
					<digi:trn key="<%=\"aim:currency:\" + ((String)selCurrency).toLowerCase().replaceAll(\" \", \"\") %>"><%=selCurrency %></digi:trn>
				</logic:present>
			</span>
			</td>
			</tr>
		</table>
		</td>
	</tr>
		<tr>
		<td style="padding-left: 5px;padding-right: 5px" align="left">
		<table width="100%">
			<tr>
			<td>
    <c:if test="${report.visibleRows/recordsPerPage>1}">
<c:set var="max_value"><%=Integer.MAX_VALUE%></c:set>
<c:if test="${recordsPerPage ne max_value}">
            <logic:notEqual name="viewFormat" value="print">
                <logic:equal name="viewFormat" value="foldable">
					<c:if test="${report.startRow != 0}">
                    	<!-- Go to FIRST PAGE -->
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
                    	&lt;&lt;
                    	</a>
                    	|
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
    		            	<digi:trn key="aim:previous">Previous</digi:trn>
                    	</a>
                    	|
                    </c:if>
                </logic:equal>
				<c:set var="lastPage">
                	0
                </c:set>
                <c:forEach var="i" begin="0" end="${report.visibleRows}" step="${recordsPerPage}">
                    <logic:equal name="viewFormat" value="html">
                        <a style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
                    </logic:equal>
                    <logic:equal name="viewFormat" value="foldable">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">	
                    </logic:equal>
                    <c:choose>							
                        <c:when  test="${i eq report.startRow}">
                            <font color="#FF0000"><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
                        </c:otherwise>								
                    </c:choose>
                    </a>
                    |
                	<c:set var="lastPage">
                    	${lastPage+1}
                    </c:set>
				</c:forEach>


                <logic:equal name="viewFormat" value="foldable">
					<c:if test="${(report.startRow+recordsPerPage+1) <= report.visibleRows}">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
                            <digi:trn key="aim:next">Next</digi:trn>
                        </a>
                        |
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
                        &gt;&gt;
                        </a>
                    </c:if>
                </logic:equal>
				</logic:notEqual>
</c:if>
                 </c:if>
            </td>
            <logic:notEqual name="viewFormat" value="print">
             <logic:present name="isUserLogged" scope="session">
            	<td align="right">
            		<jsp:include page="legendPopup.jsp"/>
            	</td>
             </logic:present>
            </logic:notEqual>
            </tr>
            </table>
			</td>
		</tr>
	</logic:equal>
<!--<span>Level Sorters</span>-->
	<logic:notEmpty name="reportMeta" property="hierarchies">
		<logic:notEmpty name="report" property="levelSorters">
			<tr>
				<td align="left">
				<logic:iterate name="report" property="levelSorters" id="sorter" indexId="levelId">
					<span style="font-style: italic;font-size: 9px;font-family: Arial;margin-left: 3px; margin-top: 3px;margin-left: 3px">
					<logic:present name="sorter">
						<digi:trn key="rep:pop:Level">Level</digi:trn> 
							<bean:write name="levelId"/> 
							<digi:trn key="rep:pop:sortedBy">sorted by</digi:trn> 
							<bean:write name="sorter"/>
						<br>		
					</logic:present>
				</span>
				</logic:iterate>
				</td>
			</tr>
				</logic:notEmpty>
	</logic:notEmpty>
<!--<span>Total Unique Rows</span>	-->
	<logic:notEqual name="report" property="totalUniqueRows" value="0">
	<tr>
		<td  style="padding-left: 5px;padding-left: 5px;">
		<table style="width: 100%">
		<tr>
		<td>
			<!-- begin big report table -->
			<c:set var="pageNumber" value="<%=new Integer(0)%>" scope="request"/>
			<c:set var="paginar" value="<%=new Boolean(true)%>" scope="request"/>
			<c:if test="${not empty param.pageNumber }">
				<c:set var="pageNumber" value="<%=Integer.valueOf(request.getParameter(\"pageNumber\"))%>" scope="request"/>
			</c:if>
		<logic:equal name="viewFormat" value="print">
			<table id='reportTable'  cellSpacing="0" width="900" style="overflow:hidden">
				<bean:define id="viewable" name="report" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>
		</logic:equal>
		<logic:notEqual name="viewFormat" value="print">
	<table id='reportTable'  cellSpacing="0" cellPadding="1" width="100%" class="reportsBorderTable">
				
			<bean:define id="viewable" name="report" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			
	</tr>
		
			</tbody>
	</table>

		



	</logic:notEqual>
		</td>
		</tr>
		</table>
		<!-- end of big report table -->
		</td>
		</tr>
		<logic:equal name="viewFormat" value="print">
		<tr>
			<td>
				<u><digi:trn key="rep:print:lastupdate">Last Update :</digi:trn></u>
				&nbsp;
				<c:if test="${reportMeta.updatedDate != null}">
					<bean:write scope="session" name="reportMeta" property="updatedDate"/>
				</c:if>
				&nbsp;
				<u><digi:trn key="rep:print:user">User :</digi:trn></u>
				<c:if test="${reportMeta.user != null}">
					<bean:write scope="session" name="reportMeta" property="user"/>
				</c:if>
				<BR>
			</td>
		</tr>
		</logic:equal>
		<tr>
			 <td style="padding-left: 5px;padding-right: 5px">
			 <table width="100%">
			 <tr>
			 <td>
 <c:if test="${report.visibleRows/recordsPerPage>1}">
<c:set var="max_value"><%=Integer.MAX_VALUE%></c:set>
<c:if test="${recordsPerPage ne max_value}">
            <logic:notEqual name="viewFormat" value="print">
                <logic:equal name="viewFormat" value="foldable">
					<c:if test="${report.startRow != 0}">
                    	<!-- Go to FIRST PAGE -->
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
                    	&lt;&lt;
                    	</a>
                    	|
                    	<a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
    		            	<digi:trn key="aim:previous">Previous</digi:trn>
                    	</a>
                    	|
                    </c:if>
                </logic:equal>
				<c:set var="lastPage">
                	0
                </c:set>
                <c:forEach var="i" begin="0" end="${report.visibleRows}" step="${recordsPerPage}">
                    <logic:equal name="viewFormat" value="html">
                        <a style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=false~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
                    </logic:equal>
                    <logic:equal name="viewFormat" value="foldable">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">	
                    </logic:equal>
                    <c:choose>							
                        <c:when  test="${i eq report.startRow}">
                            <font color="#FF0000"><fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/></font>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
                        </c:otherwise>								
                    </c:choose>
                    </a>
                    |
                	<c:set var="lastPage">
                    	${lastPage+1}
                    </c:set>
				</c:forEach>

                <logic:equal name="viewFormat" value="foldable">
					<c:if  test="${(report.startRow+recordsPerPage+1) <= report.visibleRows}">
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
                            <digi:trn key="aim:next">Next</digi:trn>
                        </a>
                        |
                        <a style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
                        &gt;&gt;
                        </a>
                    </c:if>
                </logic:equal>
				</logic:notEqual>
</c:if>
                </c:if>
            </td>
            <logic:notEqual name="viewFormat" value="print">
             <logic:present name="isUserLogged" scope="session">
     		<td align="right">
            	<jsp:include page="legendPopup.jsp" />
            </td>
             </logic:present>
            </logic:notEqual>
            </tr>
            <tr>
            	<td align="left">
            	<%
                	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                %>
				<c:set var="accessTypeLocal"><%=arf.getAccessType().toString() %></c:set>
				<c:if test="${accessTypeLocal == 'Management'}">
                <digi:trn key="aim:noNewActivitiesManagement">Number of activities that do not show up in the list (these include draft and new activities that are pending approvals)</digi:trn>:
                &nbsp;<%= arf.getActivitiesRejectedByFilter().toString() %>
                </c:if>&nbsp;
			</td>
            </tr>
            </table>
			</td>
			</tr>
		</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td style="font-family: Arial;font-style: italic;font-size: 10x"> 
				<digi:trn key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
					pick a different filter criteria or use another report.	
				</digi:trn>
			</td>
		</tr>
	</logic:equal>


</table>
	</td>
</tr>
</table>

<%
	session.setAttribute(" ", null);
	session.setAttribute("progressValue", -1);
%>
