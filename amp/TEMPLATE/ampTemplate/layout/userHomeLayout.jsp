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
		<META HTTP-EQUIV="Cache-Control" CONTENT="private"
		
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
	<jsp:include page="headerTop_2.jsp"/>
	 <digi:insert attribute="headerMiddle"/>	

	<!-- BREADCRUMP START -->
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
				<span class="sec_name">My Desktop</span>
			</div>
		</div>
	</div>
	<!-- BREADCRUMP END -->
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<tbody>
			<tr>
				<td width=900px valign=top>
					<digi:insert attribute="body"/>
				</td>
				<td width=20px align=center background="img_2/close_panel_bg.gif" valign=top>
					<a style="cursor: pointer;" ><img src="img_2/close_panel.gif" width="9" height="96" border=0 id="closepanel"></a>
				</td>
				<td valign="top">
				<div id="rightpanel">
					<div class="right_menu">
						<div class="right_menu_header"><div class="right_menu_header_cont">Search</div></div>
							<div class="right_menu_box"><div class="right_menu_cont">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  								<tr>
	    								<td class="tbl_spacing"><div class="search_label">Keyword:</div></td>
	    								<td align=right class="tbl_spacing"><input name="" type="text" class="inputx" style="width:147px;"></td>
	  								</tr>
									<tr>
	    								<td>
	    									<div class="search_label">Type:</div></td>
	    								<td align=right>
	    									<select name="select" class=dropdwn_sm style="width:150px;">
	  											<option value="Na">All</option>
											</select>
	    								</td>
	    							</tr>
								</table>
							</div>
						</div>
					</div>
					<logic:present name="currentMember">
						<digi:insert attribute="myLinks" />
						<digi:insert attribute="myReports"/>
						<digi:insert attribute="myMessages"/>
					</logic:present>
					<!-- 
					<div class="right_menu">
						<div class="right_menu_header"><div class="right_menu_header_cont">Latest created / updated projects</div></div>
							<div class="right_menu_box">
								<div class="right_menu_cont">
									<li class="tri"><a href=#>Essential Services for Health</a></li>
									<li class="tri"><a href=#>WTO Capacity Building Program</a></li>
									<li class="tri"><a href=#>Polio Vaccination Program</a></li>
									<li class="tri"><a href=#>Water Sector Support Project</a></li>
									<li class="tri"><a href=#>Forest Protection andReforestation Project</a></li>
									
									<li class="tri"><a href=#>Tax Administration Project</a></li>
									<li class="tri"><a href=#>Initiative to End Hunger</a></li>
									<li class="tri"><a href=#>Rural Productivity Project</a></li>
									<li class="tri"><a href=#>Nutritional Survey</a></li>
									<li class="tri"><a href=#>National Voter Registration</a></li>
								</div>
							</div>
						</div>
					</div>
					 -->
					</td>
					
					</tr>
			</tbody>
		</table>		
		<digi:insert attribute="footer" />
    </body>
</html>
<script language=javascript>
		$(document).ready(function(){
	   		$("#closepanel").click(function(event){
	     	$("#rightpanel").toggle('slow');
		});
	 });
</script>

