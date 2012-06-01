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
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">
		
        <!-- Dependencies --> 
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/tabview-min.js"/>"></script>
        
        <link type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/css_2/tabs.css"/>" rel="stylesheet" />
        
	</head>
     	
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<digi:secure authenticated="false">
		<logic:notPresent name="currentMember" scope="session">
			<digi:insert attribute="headerTop" />	
		</logic:notPresent>
	</digi:secure>
	<digi:secure authenticated="true">
		<jsp:include page="headerTop_2.jsp"/>
	</digi:secure>
<center>	
	<div class="main_menu">
	  	<table cellpadding="0"cellspacing="0" width="1000">
        	<tr>
            	<td style="width:1000px;" valign="top"><digi:insert attribute="headerMiddle"/></td>
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

	<!-- BREADCRUMP START -->
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
				<span class="sec_name">Messages</span>
			</div>
		</div>
	</div>
	</center>
	<!-- BREADCRUMP END -->
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
		<tbody>
			<tr>
				<td width=900px valign="top">
					<digi:insert attribute="body"/>
				</td>
				</tr>
			</tbody>
		</table>		
		<digi:insert attribute="footer" />
	</body>
</html>


