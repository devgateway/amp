<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<html>
	<meta http-equiv="X-UA-Compatible" content="chrome=1; IE=edge" />
	<digi:base />
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
	<digi:context name="digiContext" property="context"/>
	<head>
		<title>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn neverShowLinks = "true">Aid Management Platform </digi:trn> 
				<digi:trn key="${key}" neverShowLinks = "true">
					<%=title%>
				</digi:trn>
		</title>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">
		
		<!-- FIX ANNOYING ISSUE WITH IE 8-9 -->
		<script type="text/javascript"> if (!window.console) console = {log: function() {}, warn: function() {}, error: function() {}, info: function(){}}; </script>
        
        <link type="text/css" href="css_2/tabs.css" rel="stylesheet" />
        
        <!-- START LOADING NEW TABS NECESSARY FILES -->
        
        <!-- Open Sans -->
		<link href='tabs/fonts/open-sans.css' rel='stylesheet' type='text/css'>
		<!-- Icon Font - Font Awesome -->
		<link href="tabs/fonts/font-awesome.min.css" rel="stylesheet">
        
		<link rel="stylesheet" href="tabs/css/bootstrap.css">
		<link rel="stylesheet" href="tabs/css/bootstrap-theme.css">
		<link rel="stylesheet" href="tabs/css/jquery-ui.css">
		<link rel="stylesheet" href="tabs/js/lib/jqgrid-4.6.0/css/ui.jqgrid.css">		
		<link rel="stylesheet" href="tabs/css/settings.css">
		<link rel="stylesheet" href="node_modules/amp-filter/dist/amp-filter.css">
		<link rel="stylesheet" href="node_modules/amp-settings/dist/amp-settings.css">
		<link rel="stylesheet" href="tabs/css/tabs.css">
		<script>
		    // define waitSeconds above require script tag
		    // to override the default, until main.js loads
		    window.require = {
		        waitSeconds: 0
		    };
		    window.currentLocale = '<%= request.getAttribute("currentLocale") %>';
		    //alert('currentLocale: ' + currentLocale);
		</script>		
		
		<!-- END LOADING NEW TABS NECESSARY FILES -->
		<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/common/lib/object_hash.js"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate//tabs/js/lib/jquery.min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/tabs/js/lib/jquery-ui.min.js"/>"></script>	
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/script/common/CommonFilterUtils.js"/>"></script>
        			
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter.js"/>"></script>		
        <script type="text/javascript">
         $.noConflict(true);
        </script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-settings/dist/amp-settings.js"/>"></script> 
		
        
	</head>
     	
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<div id="amp-header"></div>
<%-- 	 <digi:secure authenticated="false"> --%>
<%-- 		<logic:notPresent name="currentMember" scope="session"> --%>
<%-- 			<digi:insert attribute="headerTop" />	 --%>
<%-- 		</logic:notPresent> --%>
<%-- 	</digi:secure> --%>
	<digi:secure authenticated="true">
		<jsp:include page="headerForDesktopTabs.jsp"/>
	</digi:secure>
	<digi:insert attribute="headerMiddle"/>
	<div class="breadcrump_1">
	</div>
	<div class="content-dir">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center" id="main-desktop-container">
		<tbody>
			<tr>
				<td width="100%" valign="top" id="maintd">
					<div id="tabs-container" data-tab-id="<%=request.getParameter("ampReportId")%>"></div>
 					<%-- <digi:insert attribute="body"/> --%>
				</td>
				<td width="20px" align="center" background="img_2/close_panel_bg.gif" valign="top" id="center-column">
					<a style="cursor: pointer;">
						<digi:secure authenticated="true">
							<img src="img_2/close_panel_notxt.gif" width="9" height="96" border="0" id="closepanel" style="padding: 6px;-webkit-box-sizing:content-box;box-sizing:content-box;">
						</digi:secure>
					</a>
					<img src="img_2/t.gif" width="20" height="1"> 
				</td>
				<td valign="top" width="1px" id="right-column">
					<digi:secure authenticated="true">
					<div id="rightpanel">
						<feature:display name="Desktop Search form" module="Tools">
							<jsp:include page="/repository/search/view/desktopsearch.jsp" flush="true"/>
						</feature:display>
						<logic:present name="currentMember">
							<digi:insert attribute="myLastVersions"/>
							<digi:insert attribute="myReports"/>
							
							<%-- <digi:insert attribute="myLinks" /> --%>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewMoreResources">Click here to view more resources</digi:trn>
							</c:set>
							<module:display name="Content Repository" parentModule="Resources">
								<div id='resources-widget-container'></div>
							</module:display>							
							
							<digi:insert attribute="myMessages"/>																		
						</logic:present>
					</div>
					</digi:secure>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
		<digi:insert attribute="footer" />		
		
		
    </body>
</html>	
<script data-main="tabs/js/main.js" src="tabs/js/lib/require_2.1.14.js"></script>
<script type="text/javascript">

//TODO: Refactor all of this. Defer method added to wait for jQuery to be loaded before using it
function defer(method) {
    if (window.$)
        method();
    else
        setTimeout(function() { defer(method) }, 1000);
}

var attachClosePanelEvent = function() {
	var originalWidth = $("#maintd #tabs-body-section .dynamic-content-table").width();
	console.log(originalWidth);
	$("#closepanel").click(function(){
		$('#rightpanel').toggle('slow', function() {
			if($("#closepanel").attr("src") == 'img_2/close_panel_notxt.gif') {
	    		$("#closepanel").attr('src','img_2/open_panel_notxt.gif');
	    		app.TabsApp.resizePanel(originalWidth, true);
	    		return false;    
	 	    }
			if($("#closepanel").attr("src") == 'img_2/open_panel_notxt.gif') {
				$("#closepanel").attr('src','img_2/close_panel_notxt.gif');
				app.TabsApp.resizePanel(originalWidth, false);
				return false;    
	 	    }			
		});
    });
};

YAHOO.util.Event.addListener(window, 'load', function(){
	defer(attachClosePanelEvent);
});


	function showlegend() {
		var contentId = document.getElementById("show_legend_pop_box");
  		contentId.style.display == "block" ? contentId.style.display = "none" : contentId.style.display = "block"; 
	}
</script>
