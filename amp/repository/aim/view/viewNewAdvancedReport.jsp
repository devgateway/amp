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

       
 
  
<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css">
<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" /> 
<style>
.paging {font-size:11px; color:#CCCCCC; margin-top:10px; margin-bottom:10px; font-family:Arial, Helvetica, sans-serif;}
.paging_sel {color:#FFFFFF; background-color:#FF6000; padding:2px 2px 2px 4px;text-align:center;}
.l_sm {font-size:11px; color:#376091;}
.tab_opt {background-color:#F2F2F2;}
.tab_opt_box {border:1px solid #EBEBEB;}
.tab_opt_box_cont {padding:5px; font-size:11px; background-color:#FAFAFA;}
.tab_opt_cont {padding:5px; font-size:11px; color:#CCCCCC;}
.show_hide_setting {
    float: right;
    font-size: 11px;
    padding: 5px;
    width: 200px;
    font-family:Arial, Helvetica, sans-serif;
}
</style>
<!-- Individual YUI JS files --> 


<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
  		  
		
	<!-- Individual YUI JS files --> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

<script type="text/javascript">
var msgwait0="<digi:trn> Please wait...</digi:trn>";
var msgwait1="<digi:trn> Loading Report </digi:trn>";
var loadingreport = new YAHOO.widget.Panel("wait",   
        { width:"240px",  
          fixedcenter:true,  
          close:false,  
          draggable:false,  
          zindex:99, 
          modal:true, 
          visible:false,
          underlay:"shadow"
        }  
    ); 

	loadingreport.setHeader(msgwait0); 
	loadingreport.setBody("<div align='center'>"+msgwait1+"<br>"+ '<img src="/TEMPLATE/ampTemplate/img_2/rel_interstitial_loading.gif" />'+"</div>"); 
	loadingreport.render(document.body);
	loadingreport.show();
</script>

		<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>



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
		displaySettingsButton.innerHTML = "${showCurrSettings}";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings}";
	}
}

</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<div id="mySorter" class="dialog" style="padding:10px 5px;overflow: auto; display: none;">
	<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
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
	<div id="myRange" class="invisible-item">
		<jsp:include page="/repository/aim/view/ar/RangePicker.jsp" />
	</div>
	<div id="customFormat" style="display: none;height: 372px;width: auto;">
		<jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
	</div>
</div>
</logic:notEqual>
<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>
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
			<div class="reportname" style="font-size:18px; padding-left:5px; font-family:Arial, Helvetica, sans-serif; margin-bottom:10px; font-weight:bold; color:#000000;">
			<bean:write scope="session" name="reportMeta" property="name" />
				<!--<table width="100%" border="0" cellpadding="0" cellspacing="0">
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
				</table>-->
			</div>
		</logic:notEqual>
		
		<table width="100%" cellpadding="3" cellspacing="1" rules="rows" frame="box" style="margin-left: 5px;border-color:#cccccc;">
			<logic:notEmpty property="reportDescription" name="reportMeta">
			<tr>
				<td style="padding-left: 5px;padding-left: 5px;">
					<digi:trn key="rep:pop:Description">Description:</digi:trn><br>
						<span style="font-weight: bold;font-size: 13px;margin-left: 5px;margin-top: 3px; font-family: Arial">
							<bean:write scope="session" name="reportMeta" property="reportDescription" />
						</span>
				</td>
			</tr>
			</logic:notEmpty>
			<logic:empty property="reportDescription" name="reportMeta">
				<tr>
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
		<div class="tab_opt_box">
				<div class="show_hide_setting">
	        		<b>
	        			<a style="cursor:pointer;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings}</a>
	        		</b>
	        	</div>
	        	<div class="tab_opt">
		        	<div class="tab_opt_cont">
            <logic:notEmpty name="reportMeta" property="hierarchies">
            	<c:if test="${!ReportsFilter.publicView}">
	                <a class="settingsLink" style="color:#376091;" onClick="showSorter();">
	                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
	                </a> | 
	            </c:if>
            </logic:notEmpty> 
                <a class="settingsLink"  style="color:#376091;" onClick="showFilter(); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <%
               	 	AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
                	if (arf.isPublicView()==false){%>
                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
	          	 	|
	          	 	<a class="settingsLink"  style="color:#376091;" onClick="initSaveReportEngine(false);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}" >
	                	${saveFilters}
	                </a>
                </feature:display>
               <%}%>
               
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  	<%if (arf.isPublicView()==false){%>
           	  	|
				<a  id="frezzlinkreport" class="settingsLinkDisable" style="color:#376091; cursor: default;" onclick="javascript:frezzreport()">
               		<script language="javascript">
						document.write(msg1);
					</script>
                </a>
                <%} %>  	
                      	
           	</logic:notEqual>
                
                |<a  class="settingsLink"  style="color:#376091;" onClick="showFormat(); " >
                <digi:trn>Report Settings</digi:trn>
                </a>
           
             &nbsp;<br>
             <div class="tab_opt_box_cont" style="display:none;" id="currentDisplaySettings" >
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
			<div class="tab_opt_box">
				<div class="show_hide_setting">
	        		<b>
	        			<a style="cursor:pointer;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings}</a>
	        		</b>
	        	</div>
	        	<div class="tab_opt">
		        	<div class="tab_opt_cont">
		        		<logic:notEmpty name="reportMeta" property="hierarchies">
	                		<a class="l_sm" onClick="showSorter();" style="cursor: pointer;text-decoration: underline;">
	                			<digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
	                		</a> 
	                		&nbsp;|&nbsp; 
	            		</logic:notEmpty> 
	                	<a class="l_sm" onClick="showFilter();" style="cursor: pointer;text-decoration: underline;" >
	                		<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
	                	</a>
		                <%
		                AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
		                if (arf.isPublicView()==false){%>
		                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
			                &nbsp;|&nbsp;
			          	 	<a style="cursor: pointer;text-decoration: underline;" class="l_sm" onClick="initSaveReportEngine(true);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}">
			                	${saveFilters}
			                </a>
		           		</feature:display>
		           		<%}%>
		           	  <logic:notEqual name="viewFormat" value="foldable">
		           	 	 &nbsp;|&nbsp;
		           	  	<a style="cursor: pointer;text-decoration: underline;" id="frezzlink" class="l_sm">
		               		<script language="	">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
           	  
              </logic:notEqual>
                 &nbsp;|&nbsp;
                 <a class="l_sm" onClick="showFormat(); " style="text-decoration: underline;cursor: pointer;">
                	<digi:trn>Tab Settings</digi:trn>
                </a>
           
            </span>
             &nbsp;<br>
             <div class="tab_opt_box_cont" style="display:none;" id="currentDisplaySettings" >
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
		
		<!-- DESKTOP TAB SETTINGS BOX-->
		<digi:secure authenticated="true">
			<div class="tab_opt_box">
				<div class="show_hide_setting">
	        		<b>
	        			<a style="cursor:pointer;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings}</a>
	        		</b>
	        	</div>
	        	<div class="tab_opt">
		        	<div class="tab_opt_cont">
		        		<logic:notEmpty name="reportMeta" property="hierarchies">
	                		<a class="l_sm" onClick="showSorter();" style="cursor: pointer;text-decoration: underline;">
	                			<digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
	                		</a> 
	                		&nbsp;|&nbsp; 
	            		</logic:notEmpty> 
	            		<c:if test="${param.queryEngine!='true' }">
		                	<a class="l_sm" onClick="showFilter();" style="cursor: pointer;text-decoration: underline;" >
		                		<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
		                	</a>
	                	</c:if>
		                <%
		                AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");
		                if (arf.isPublicView()==false){%>
		                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
			                &nbsp;|&nbsp;
			          	 	<a style="cursor: pointer;text-decoration: underline;" class="l_sm" onClick="initSaveReportEngine(true);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}">
			                	${saveFilters}
			                </a>
		           		</feature:display>
		           		<%}%>
		           	  <logic:notEqual name="viewFormat" value="foldable">
		           	 	 &nbsp;|&nbsp;
		           	  	<a style="cursor: pointer;text-decoration: underline;" id="frezzlink" class="l_sm">
		               		<script language="	">
						document.write((scrolling)?msg2:msg1);
					</script>
                </a>
           	  
              </logic:notEqual>
              <c:if test="${param.queryEngine!='true' }">
                 &nbsp;|&nbsp;
                 <a class="l_sm" onClick="showFormat(); " style="text-decoration: underline;cursor: pointer;">
                	<digi:trn>Tab Settings</digi:trn>
                </a>
               </c:if>
           
            </span>
             &nbsp;<br>
             <div class="tab_opt_box_cont" style="display:none;" id="currentDisplaySettings" >
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
			<div style="font-family: Arial,sans-serif;font-size: 11px">
				<logic:notEmpty name="reportMeta" property="filterDataSet">
				<span style="cursor:pointer;cursor:pointer;font-style: italic;">
		        	<digi:trn>Please note: Filter(s) have been applied. Click on "Show current settings" to see list of applied filters</digi:trn>
				</span>
				</logic:notEmpty>
			</div>
			<div class="show_legend">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td style="font-size: 11px;font-family: Arial,sans-serif">
						<%
				            AmpARFilter af = (AmpARFilter) session.getAttribute("ReportsFilter");
				            if (af.getAmountinthousand()!=null && af.getAmountinthousand()==true){%>
				            <digi:trn key="rep:pop:AllAmount">
								Amounts are in thousands (000)
							</digi:trn>&nbsp;
		           		<%}%>
						</td>
						<td style="font-size: 11px;font-family: Arial,sans-serif">
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
							&nbsp;|&nbsp;
						</td>
						<td style="font-size: 11px;font-family: Arial,sans-serif">
							<logic:notEqual name="viewFormat" value="print">
								<logic:present name="isUserLogged" scope="session">
	          						<jsp:include page="legendPopup.jsp"/>
	         					</logic:present>
	          				</logic:notEqual>
	          			</td>
	          		</tr>
	          	</table>
			</div>	
			
			<div class="paging">
	   			<c:if test="${report.visibleRows/recordsPerPage>1}">
				<c:set var="max_value"><%=Integer.MAX_VALUE%></c:set>
				<c:if test="${recordsPerPage ne max_value}">
	           	<logic:notEqual name="viewFormat" value="print">
		               	<logic:equal name="viewFormat" value="foldable">
						<c:if test="${report.startRow != 0}">
		                  		<!-- Go to FIRST PAGE -->
		                  	<c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
			                  				&lt;&lt;
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
			  		            		<digi:trn key="aim:previous">Previous</digi:trn>
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  	</c:when>
			                  	<c:otherwise>
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=0~endRow=${recordsPerPage-1}~queryEngine=true');">	
			                  				&lt;&lt;
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow-recordsPerPage}~endRow=${report.startRow-1}~queryEngine=true');">	
			  		            		<digi:trn key="aim:previous">Previous</digi:trn>
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  	</c:otherwise>
			                </c:choose>
		                  	</c:if>
					</logic:equal>
					<c:set var="lastPage">0</c:set>
		            <c:forEach var="i" begin="0" end="${report.visibleRows}" step="${recordsPerPage}">
						<logic:equal name="viewFormat" value="html2">
							<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html2~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
						</logic:equal>
		                  <logic:equal name="viewFormat" value="html">
		                      <a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
		                  </logic:equal>
		                  <logic:equal name="viewFormat" value="foldable">
		                  	<c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
			                      <a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">
			                    </c:when>
			                    <c:otherwise>
			                    	<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${i}~endRow=${i+recordsPerPage-1}~queryEngine=true');">	
			                    </c:otherwise>
		                    </c:choose>	
		                  </logic:equal>
		                  <c:choose>							
		                      <c:when  test="${i eq report.startRow}">
		                          <b class="paging_sel">
		                          	<fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/>
		                          </b>
		                      </c:when>
		                      <c:otherwise>
		                          <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
		                      </c:otherwise>								
		                  </c:choose>
		                  </a>
		                  &nbsp;|&nbsp;
		              	<c:set var="lastPage">
		                  	${lastPage+1}
		                  </c:set>
					</c:forEach>
					<logic:equal name="viewFormat" value="foldable">
						<c:if test="${(report.startRow+recordsPerPage+1) <= report.visibleRows}">
							<c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
									<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
				                    	<digi:trn key="aim:next">Next</digi:trn>
				                    </a>
				                    &nbsp;|&nbsp;
				                   	<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
				                      &gt;&gt;
									</a>
								</c:when>
								<c:otherwise>
									<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow+recordsPerPage}~endRow=${report.startRow+(recordsPerPage*2)-1}~queryEngine=true');">	
				                    	<digi:trn key="aim:next">Next</digi:trn>
				                    </a>
				                    &nbsp;|&nbsp;
				                   	<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${((lastPage-1)*recordsPerPage)}~endRow=${(lastPage*recordsPerPage)}~queryEngine=true');">	
				                      &gt;&gt;
									</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</logic:equal>
			</logic:notEqual>
			</c:if>
	        </c:if>
		</div>
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
			<table id='reportTable' cellSpacing="0" width="900px" style="overflow:hidden">
				<bean:define id="viewable" name="report" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>
		</logic:equal>
		<logic:notEqual name="viewFormat" value="print">
		<table id='reportTable' class="html2ReportTable inside" width="100%" cellpadding="0" cellspacing="0">
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
			<!-- PAGINATION -->	 
				<div class="paging">
		   			<c:if test="${report.visibleRows/recordsPerPage>1}">
					<c:set var="max_value"><%=Integer.MAX_VALUE%></c:set>
					<c:if test="${recordsPerPage ne max_value}">
		           	<logic:notEqual name="viewFormat" value="print">
			               	<logic:equal name="viewFormat" value="foldable">
							<c:if test="${report.startRow != 0}">
			                  	<c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=0~endRow=<c:out value="${recordsPerPage-1}"/>');">	
			                  				&lt;&lt;
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow-recordsPerPage}"/>~endRow=<c:out value="${report.startRow-1}"/>');">	
			  		            		<digi:trn key="aim:previous">Previous</digi:trn>
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  	</c:when>
			                  	<c:otherwise>
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=0~endRow=${recordsPerPage-1}~queryEngine=true');">	
			                  				&lt;&lt;
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  		<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow-recordsPerPage}~endRow=${report.startRow-1}~queryEngine=true');">	
			  		            		<digi:trn key="aim:previous">Previous</digi:trn>
			                  		</a>
			                  		&nbsp;|&nbsp;
			                  	</c:otherwise>
			                </c:choose>
			                  	</c:if>
						</logic:equal>
						<c:set var="lastPage">0</c:set>
			            <c:forEach var="i" begin="0" end="${report.visibleRows}" step="${recordsPerPage}">
			            	<logic:equal name="viewFormat" value="html2">
								<a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html2~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=false~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
							</logic:equal>
			                  <logic:equal name="viewFormat" value="html">
			                      <a class="l_sm" style="cursor:pointer" onclick="window.location.href='/aim/viewNewAdvancedReport.do~viewFormat=html~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=false~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+(recordsPerPage-1)}"/>';">
			                  </logic:equal>
			                  <logic:equal name="viewFormat" value="foldable">
			                  <c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
			                      <a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${i}"/>~endRow=<c:out value="${i+recordsPerPage-1}"/>');">
			                    </c:when>
			                    <c:otherwise>
			                    	<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${i}~endRow=${i+recordsPerPage-1}~queryEngine=true');">	
			                    </c:otherwise>
		                    </c:choose>	
			                  </logic:equal>
			                  <c:choose>							
			                      <c:when  test="${i eq report.startRow}">
			                          <b class="paging_sel">
			                          	<fmt:formatNumber value="${(i)/recordsPerPage + 1}" maxFractionDigits="0"/>
			                          </b>
			                      </c:when>
			                      <c:otherwise>
			                          <fmt:formatNumber value="${(i/recordsPerPage) + 1}" maxFractionDigits="0"/>
			                      </c:otherwise>								
			                  </c:choose>
			                  </a>
			                  &nbsp;|&nbsp;
			              	<c:set var="lastPage">
			                  	${lastPage+1}
			                  </c:set>
						</c:forEach>
						<logic:equal name="viewFormat" value="foldable">
							<c:if test="${(report.startRow+recordsPerPage+1) <= report.visibleRows}">
								<c:choose>
			                  	<c:when test="${param.queryEngine!='true' }">
									<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${report.startRow+recordsPerPage}"/>~endRow=<c:out value="${report.startRow+(recordsPerPage*2)-1}"/>');">	
				                    	<digi:trn key="aim:next">Next</digi:trn>
				                    </a>
				                    &nbsp;|&nbsp;
				                   	<a class="l_sm" style="cursor:pointer" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="nametrimed"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~cached=true~startRow=<c:out value="${((lastPage-1)*recordsPerPage)}"/>~endRow=<c:out value="${(lastPage*recordsPerPage)}"/>');">	
				                      &gt;&gt;
									</a>
								</c:when>
								<c:otherwise>
									<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${report.startRow+recordsPerPage}~endRow=${report.startRow+(recordsPerPage*2)-1}~queryEngine=true');">	
				                    	<digi:trn key="aim:next">Next</digi:trn>
				                    </a>
				                    &nbsp;|&nbsp;
				                   	<a class="l_sm" style="cursor:pointer" onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~cached=true~startRow=${((lastPage-1)*recordsPerPage)}~endRow=${(lastPage*recordsPerPage)}~queryEngine=true');">	
				                      &gt;&gt;
									</a>
								</c:otherwise>
							</c:choose>
							</c:if>
						</logic:equal>
				</logic:notEqual>
				</c:if>
		        </c:if>
			</div>
           <!-- END PAGINATION -->
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
<script language="JavaScript">
	loadingreport.hide();
</script>
<%
	session.setAttribute(" ", null);
	session.setAttribute("progressValue", -1);
%>
