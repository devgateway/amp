<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<html>
<digi:base />
<%-- <script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script> --%>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
<head>

		<%@include file="title.jsp"%>
			
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
		<script type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
		
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>
	
		<!-- Stylesheet of AMP -->
		<!--[if IE 6]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_6.css' rel='stylesheet' type='text/css'><![endif]-->
		<!--[if IE 7]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_7.css' rel='stylesheet' type='text/css'><![endif]-->
		<!--[if IE 8]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_8.css' rel='stylesheet' type='text/css'><![endif]-->
		<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
		<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" type="text/css" rel="stylesheet" />
		<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" type="text/css" rel="stylesheet" />
		<digi:ref href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 
			 
	    <!-- Individual YUI CSS files --> 
		<link rel="stylesheet" type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"/>"> 
		<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" type="text/css" rel="stylesheet" />
		 
		<!-- Individual YUI JS files --> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js"/>"></script>
		
		<!-- Jquery Base Library -->
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
	
		<script type="text/javascript">
			var YAHOOAmp 	= YAHOO;
		</script>  
        
		<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
		
		<logic:present name="currentMember" scope="session">
			<script type="text/javascript">
				function showUserProfile(id){
					<digi:context name="information" property="/aim/userProfile.do" />
					//openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
					var param = "~edit=true~id="+id;
					previewWorkspaceframe('/aim/default/userProfile.do',param);
				}
				function help(){
					 <digi:context name="rev" property="/help/help.do~blankPage=true" />
						openURLinWindow("<%=rev%>",1024,768);
					}
				function adminHelp(){
						 <digi:context name="admin" property="/help/admin/help.do~blankPage=true" />
						openURLinWindow("<%=admin%>",1024,768);
				}
				
				function canExit(){
				    if(typeof quitRnot1 == 'function') {
				        return quitRnot1('${msg}');
				    }
				    else{
				        return true;
				    }
				
				}	
			</script>
		</logic:present>

	<style type="text/css">
     table.inside {border-color: #CCC; border-style: solid; font-size:11px;border-width: 0 0 1px 1px; border-spacing: 0; border-collapse: collapse;}
     .selector_type_cont {padding: 5px;}
	.buttonx,.dr-menu {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
	.buttonx_sm {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:10px; color:#FFFFFF; font-weight:bold; padding-left:2px; padding-right:2px; padding-top:2px; padding-bottom:2px;}
	.invisible-item{display: none;}
.white-item{color: white;}
.marginal-five{margin-right:5px;}

	</style>
</head>
<body bgcolor="#ffffff">
<digi:insert attribute="body" />
</body>
</html>
