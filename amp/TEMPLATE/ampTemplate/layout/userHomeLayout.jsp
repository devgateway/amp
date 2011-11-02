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
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>
<html>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<head>
		<title>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn>Aid Management Platform </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</title>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">
		
        <!-- Dependencies --> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="js_2/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="js_2/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/tabview-min.js"/>"></script>
        
        <link type="text/css" href="css_2/tabs.css" rel="stylesheet" />
        
	</head>
     	
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<digi:secure authenticated="false">
		<logic:notPresent name="currentMember" scope="session">
			<digi:insert attribute="headerTop" />	
		</logic:notPresent>
	</digi:secure>
	<digi:secure authenticated="true">
		<jsp:include page="headerTop_2.jsp"/>
	</digi:secure><center>
	<div class="main_menu" id="userHomeMenu" >
    	<table cellpadding="0"cellspacing="0" width="1000">
        	<tr>
            	<td style="width:900px;" valign="top"><digi:insert attribute="headerMiddle"/></td>
                <td><digi:secure authenticated="true">
         <div class="workspace_info"> <!-- I think this class should be renamed to correspong the logout item -->   						
   			<digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
				<digi:trn key="aim:logout">LOGOUT</digi:trn>
			</digi:link>
		</div>	
		</digi:secure></td>
            </tr>
        </table>
    	
		
	
	</div>
   
	<div class="breadcrump_1">
	</div></center>
	<%AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");%>
	<!-- BREADCRUMP START -->
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">

				<%if (arf != null && arf.isPublicView()==true){%>
				<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
    					<td width="740" valign="top">	
		    				<div class="wlcm_txt_menu">
				    			<b class="sel_mid">
				    				<digi:trn>
				    					Public Tab's
				    				</digi:trn>
				    			</b>
				    			<span class="wlcm_txt_menu_spc">|</span>
				    			<a onClick="showAbout(); return false;" style="text-decoration: underline;cursor: pointer;">
				    				<digi:trn>
				    					About AMP
				    				</digi:trn>
				    			</a>
				    			<span class="wlcm_txt_menu_spc">|</span>
				    			<a href="/viewTeamReports.do?tabs=false">
				    				<digi:trn>
				    					Reports
				    				</digi:trn>
				    			</a>
				    			<span class="wlcm_txt_menu_spc">|</span>
				    			<a href="/contentrepository/publicDocTabManager.do?action=publicShow" module="contentrepository" onClick="return quitRnot()">
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
    					</td>
    				</tr>
    			</table>
    		<%}%>
			</div>
		</div>
	</div>	
    <center>
    
			
            </center>
	<!-- BREADCRUMP END -->
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
		<tbody>
			<tr>
				<td width="100%" valign="top" id="maintd">
					<digi:insert attribute="body"/>
				</td>
				<td width="20px" align="center" background="img_2/close_panel_bg.gif" valign="top">
					<a style="cursor: pointer;">
						<%if (arf != null && arf.isPublicView()==false){%>
							<img src="img_2/close_panel_notxt.gif" width="9" height="96" border="0" id="closepanel" style="padding: 6px">
						<%}%>
					</a>
				</td>
				<td valign="top" width="1px">
					<%
               	 	if (arf != null && arf.isPublicView()==false){%>
					<div id="rightpanel">
						<feature:display name="Desktop Search form" module="Tools">
							<jsp:include page="/repository/search/view/desktopsearch.jsp" flush="true"/>
						</feature:display>
						<logic:present name="currentMember">
							<digi:insert attribute="myLinks" />
							<digi:insert attribute="myReports"/>
							<digi:insert attribute="myMessages"/>
							<digi:insert attribute="myLastVersions"/>
						</logic:present>
					</div>
					<%}%>
				</td>
			</tr>
			</tbody>
		</table>
		<digi:insert attribute="footer" />		
		
		
    </body>
</html>	
<script language=javascript>
	$("#closepanel").click(function(){
		$('#rightpanel').toggle('slow', function() {
			if($("#closepanel").attr("src") == 'img_2/close_panel_notxt.gif') {
	    		$("#closepanel").attr('src','img_2/open_panel_notxt.gif');
	    		return false;    
	 	    }
			if($("#closepanel").attr("src") == 'img_2/open_panel_notxt.gif') {
				$("#closepanel").attr('src','img_2/close_panel_notxt.gif');
				return false;    
	 	    }
		});
    });
	
	function showlegend() {
		var contentId = document.getElementById("show_legend_pop_box");
  		contentId.style.display == "block" ? contentId.style.display = "none" : contentId.style.display = "block"; 
	}
</script>