<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@page trimDirectiveWhitespaces="true"%>
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
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
  <!-- Dependencies --> 

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
	pageContext.setAttribute("currentFilter", ReportContextData.getFromRequest().getFilter());
%>

<bean:define id="generatedReport" name="reportCD" property="generatedReport" type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>
 
  
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
.tab_opt_box {border:1px solid #EBEBEB;max-height:100px;overflow:auto;}
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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
  		  
		
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
/*---snippet rafy---*/
function css_browser_selector(u) {
    var ua=u.toLowerCase(),is=function(t){
        return ua.indexOf(t)>-1
        },g='gecko',w='webkit',s='safari',o='opera',m='mobile',h=document.documentElement,b=[(!(/opera|webtv/i.test(ua))&&/msie\s(\d)/.test(ua))?('ie ie'+RegExp.$1):is('firefox/2')?g+' ff2':is('firefox/3.5')?g+' ff3 ff3_5':is('firefox/3.6')?g+' ff3 ff3_6':is('firefox/3')?g+' ff3':is('gecko/')?g:is('opera')?o+(/version\/(\d+)/.test(ua)?' '+o+RegExp.$1:(/opera(\s|\/)(\d+)/.test(ua)?' '+o+RegExp.$2:'')):is('konqueror')?'konqueror':is('blackberry')?m+' blackberry':is('android')?m+' android':is('chrome')?w+' chrome':is('iron')?w+' iron':is('applewebkit/')?w+' '+s+(/version\/(\d+)/.test(ua)?' '+s+RegExp.$1:''):is('mozilla/')?g:'',is('j2me')?m+' j2me':is('iphone')?m+' iphone':is('ipod')?m+' ipod':is('ipad')?m+' ipad':is('mac')?'mac':is('darwin')?'mac':is('webtv')?'webtv':is('win')?'win'+(is('windows nt 6.0')?' vista':''):is('freebsd')?'freebsd':(is('x11')||is('linux'))?'linux':'','js'];
    c = b.join(' ');
    h.className += ' '+c;
    return c;
}
css_browser_selector(navigator.userAgent);


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
	int counter = ReportContextData.getFromRequest().getProgressValue();
	counter ++;
	ReportContextData.getFromRequest().setProgressValue(counter);
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
<%-- <jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>  this file is already included up --%>
<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />
<%	
	counter ++;
	ReportContextData.getFromRequest().setProgressValue(counter);
%>

<table width="100%">
<tr>
	<td>
<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<bean:define id="viewable" name="generatedReport" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
		<jsp:include page="/repository/aim/view/ar/toolBar.jsp" />
		
		<c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>
		
		<logic:notEqual name="widget" scope="request" value="true">
			<div class="reportname" style="font-size:18px; padding-left:5px; font-family:Arial, Helvetica, sans-serif; margin-bottom:10px; font-weight:bold; color:#000000;">
				<bean:write name="reportMeta" property="name" />
			</div>
		</logic:notEqual>
		
		<table width="100%" cellpadding="3" cellspacing="1" rules="rows" frame="box" style="margin-left: 5px;border-color:#cccccc;">
			<logic:notEmpty property="reportDescription" name="reportMeta">
			<tr>
				<td style="padding-left: 5px;padding-left: 5px;">
					<digi:trn key="rep:pop:Description">Description:</digi:trn><br>
						<span style="font-weight: bold;font-size: 13px;margin-left: 5px;margin-top: 3px; font-family: Arial">
							<bean:write name="reportMeta" property="reportDescription" />
						</span>
				</td>
			</tr>
			</logic:notEmpty>
			<tr>
				<td align="left" height="20px" style="padding-left: 5px;padding-left: 5px;">
					<span  style="color: red;font-family: Arial;font-size: 10px;">
						<%
						AmpARFilter af = ReportContextData.getFromRequest().getFilter();
	                	if (af.computeEffectiveAmountInThousand() == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS){%>
	               			<digi:trn key="rep:pop:AllAmount">
								Amounts are in thousands (000)
							</digi:trn>
	           			<%}%>
						
	           			<%	                	
	                	if (af.computeEffectiveAmountInThousand() == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS){%>
	               			<digi:trn key="rep:pop:AllAmountMillions">
								Amounts are in millions (000 000)
							</digi:trn>
	           			<%}%>
						
						<bean:define id="selCurrency" name="reportCD" property="selectedCurrency" />
						<digi:trn key="<%=\"aim:currency:\" + ((String)selCurrency).toLowerCase().replaceAll(\" \", \"\") %>"><%=selCurrency %></digi:trn>
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
	                <a class="settingsLink" style="cursor: pointer;color:#376091;" onClick="showSorter();">
	                <digi:trn key="rep:pop:ChangeSorting">Change Sorting</digi:trn>
	                </a> | 
	            </c:if>
            </logic:notEmpty> 
                <a class="settingsLink"  style="cursor: pointer;color:#376091;" onClick="showFilter('<%=ReportContextData.getCurrentReportContextId(request, true)%>'); " >
                <digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
                </a>
                <%
                	AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
                	if (arf.isPublicView()==false){%>
                <feature:display name="Save Report/Tab with Filters" module="Report and Tab Options">
	          	 	|
	          	 	<a class="settingsLink"  style="cursor: pointer;color:#376091;" onClick="initSaveReportEngine(false);saveReportEngine.showPanel(); " title="${saveFiltersTooltip}" >
	                	${saveFilters}
	                </a>
                </feature:display>
               <%}%>
               
           	  <logic:notEqual name="viewFormat" value="foldable">
           	  	<%if (arf.isPublicView()==false){%>
           	  	|
				<a  id="frezzlinkreport" reportContextId='<%=ReportContextData.getCurrentReportContextId(request, true) %>' class="settingsLinkDisable" style="cursor: pointer;color:#376091; " onclick="javascript:frezzreport(<%=ReportContextData.getCurrentReportContextId(request, true) %>)">
               		<script language="javascript">
						document.write(msg1);
					</script>
                </a>
                <%} %>  	
                      	
           	</logic:notEqual>
                
                |<a  class="settingsLink"  style="cursor: pointer;color:#376091;" onClick="showFormat(); " >
                <digi:trn>Report Settings</digi:trn>
                </a>
           
             &nbsp;<br>
             <div class="tab_opt_box_cont" style="display:none;" id="currentDisplaySettings" >
             <table cellpadding="0" cellspacing="0" border="0" width="80%">
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
				<digi:trn key="rep:pop:WorkspaceFilters">Workspace Filters:</digi:trn>
			</strong>
            <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
             </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                <c:if test="${reportCD.filter != null}">
                	<bean:define id="listable" name="reportCD" property="filter" toScope="request" />
                	<bean:define id="listableStyle" value="settingsList" toScope="request"/>
                	<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
                </c:if>
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
	                	<a class="l_sm" onClick="showFilter('<%=ReportContextData.getCurrentReportContextId(request, true)%>');" style="cursor: pointer;text-decoration: underline;" >
	                		<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
	                	</a>
		                <%
		                AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
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
		           	  	<a style="cursor: pointer;text-decoration: underline;" id="frezzlink" reportContextId='<%=ReportContextData.getCurrentReportContextId(request, true) %>' class="l_sm">
		               		<script language="javascript">
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
			<digi:trn key="rep:pop:WorkspaceFilters">Workspace Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
                 <c:if test="${reportCD.filter != null}">
                	<bean:define id="listable" name="reportCD" property="filter" toScope="request" />
                	<bean:define id="listableStyle" value="settingsList" toScope="request"/>
                	<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
		             <jsp:include page="${listable.jspFile}" />
                </c:if>
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
		                	<a class="l_sm" onClick="showFilter('<%=ReportContextData.getCurrentReportContextId(request, true)%>');" style="cursor: pointer;text-decoration: underline;" >
		                		<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
		                	</a>
	                	</c:if>
		                <%
		                AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
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
		           	  	<a style="cursor: pointer;text-decoration: underline;" id="frezzlink" reportContextId='<%=ReportContextData.getCurrentReportContextId(request, true) %>' class="l_sm">
		               		<script language="javascript">
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
			<digi:trn key="rep:pop:WorkspaceFilters">Workspace Filters:</digi:trn></strong>
                <logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" scope="session">
                <bean:define id="listable" name="<%=org.dgfoundation.amp.ar.ArConstants.TEAM_FILTER%>" toScope="request"/>
                <bean:define id="listableStyle" value="settingsList" toScope="request"/>
                <bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
                    <jsp:include page="${listable.jspFile}" />
                </logic:present>
             </td>
             </tr>
             <tr>
             <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
			<strong>
			<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn></strong>
               <c:if test="${reportCD.filter != null}">
                	<bean:define id="listable" name="reportCD" property="filter" toScope="request" />
					<bean:define id="listableStyle" value="settingsList" toScope="request"/>
					<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
					<jsp:include page="${listable.jspFile}" />
                </c:if>
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
				<logic:equal name="currentFilter" property="changed" value="true">
					<span style="cursor:pointer;cursor:pointer;font-style: italic;">
		        		<digi:trn>Please note: Filter(s) have been applied. Click on "Show current settings" to see list of applied filters</digi:trn>
					</span>
				</logic:equal>
			</div>
			<div class="show_legend">
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td style="font-size: 11px;font-family: Arial,sans-serif">
						<%
							AmpARFilter af = ReportContextData.getFromRequest().getFilter();
	            			if (af.getAmountinthousand()!=null && af.getAmountinthousand()==1){%>
				            	<digi:trn key="rep:pop:AllAmount">
									Amounts are in thousands (000)
								</digi:trn>&nbsp;
		           			<%}%>
						</td>
						<td style="font-size: 11px;font-family: Arial,sans-serif">
	           <%	                	
	            if (af.getAmountinthousand()!=null && af.getAmountinthousand()==2){%>
	               			<digi:trn key="rep:pop:AllAmountMillions">
								Amounts are in millions (000 000)
							</digi:trn>
   			   <%}%>				
				
							<bean:define id="selCurrency" name="reportCD" property="selectedCurrency" />
							<digi:trn key="<%=\"aim:currency:\" + ((String)selCurrency).toLowerCase().replaceAll(\" \", \"\") %>"><%=selCurrency %></digi:trn>
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
		</td>
	</tr>
	<tr>
		<td>
			<jsp:include page="viewNewAdvancedReportPaginator.jsp"></jsp:include>
		</td>
	</tr>
	</logic:equal>
<!--<span>Level Sorters</span>-->
	<logic:notEmpty name="reportMeta" property="hierarchies">
		<logic:notEmpty name="generatedReport" property="levelSorters">
			<bean:define id="hierarchies" name="reportMeta" property="hierarchiesArray" /> 
			<tr>
				<td align="left">
				<logic:iterate name="generatedReport" property="levelSorters" id="sorter" indexId="levelId">
					<div id="level-sorter">
					<logic:present name="sorter">
						<digi:trn>${hierarchies[levelId].column.columnName}</digi:trn>&nbsp;<digi:trn key="rep:pop:sortedBy">sorted by</digi:trn>&nbsp;<bean:write name="sorter"/>
						<br />		
					</logic:present>
				</div>
				</logic:iterate>
				</td>
			</tr>
				</logic:notEmpty>
	</logic:notEmpty>
<!--<span>Total Unique Rows</span>	-->
	<logic:notEqual name="generatedReport" property="totalUniqueRows" value="0">
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
					<bean:define id="viewable" name="generatedReport" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
					<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
				</table>
			</logic:equal>
			<logic:notEqual name="viewFormat" value="print">
				<div id="fixAutomaticDivWithHeight100">
				<table id='reportTable' class="html2ReportTable inside" width="100%" cellpadding="0" cellspacing="0">
				      <bean:define id="viewable" name="generatedReport" type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				      <jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
				</table>
				</div>
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
					<bean:write name="reportMeta" property="updatedDate"/>
				</c:if>
				&nbsp;
				<u><digi:trn key="rep:print:user">User :</digi:trn></u>
				<c:if test="${reportMeta.user != null}">
					<bean:write name="reportMeta" property="user"/>
				</c:if>
				<BR>
			</td>
		</tr>
		</logic:equal>
		<tr>
			<td style="padding-left: 5px;padding-right: 5px">
			<jsp:include page="viewNewAdvancedReportPaginator.jsp"></jsp:include>
		</td>
	</tr>
		</logic:notEqual>
	<logic:equal name="generatedReport" property="totalUniqueRows" value="0">
		<tr>
			<td style="font-family: Arial;font-style: italic;font-size: 10x">
				<c:choose>
					<c:when test="${param.queryEngine =='true'}">
						<digi:trn key="rep:pop:filteredSearch">The specified filters does not hold any data. Pick a different filter criteria.	
						</digi:trn>
					</c:when>
					<c:otherwise>
						<digi:trn key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
							pick a different filter criteria or use another report.	
						</digi:trn>
					</c:otherwise>
				</c:choose>
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
	//session.setAttribute(" ", null); to be deleted later
	ReportContextData.getFromRequest().setProgressValue(-1);
%>
