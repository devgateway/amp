<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-folder-tree-static.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-context-menu.js"/>"></script>

	<script type="text/javascript">
		var idOfFolderTrees = ['dhtmlgoodies_tree'];
	</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />



<digi:instance property="aimVisibilityManagerForm" />
<digi:form action="/visibilityManager.do" method="post" >

<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampTemplatesVisibilityManager">
		AMP Templates Visibility Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	
	<tr>
		<td><br/>
		</td>
	</tr>
	<tr>
		<td>
		
	
	<p><digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/></p>
	<ul id="dhtmlgoodies_tree" class="dhtmlgoodies_tree">
	<bean:define name="aimVisibilityManagerForm" property="ampTreeVisibility" id="template" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
	<bean:define name="template" property="items" id="modules" type="java.util.Map"  toScope="page"/>
	<bean:define name="template" property="root" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="request"/>
	<font size="3">
		<li><a class="alinkblue" href="#" id="<bean:write name="template" property="root.id"/>"><bean:write name="template" property="root.name"/></a>
			<ul>
				<logic:iterate name="modules" id="module" type="java.util.Map.Entry" >
					<bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
					<bean:define id="_moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility"/>
					<bean:define id="_moduleAux2" name="_moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
					<bean:define id="size" name="_moduleAux2" property="submodules"/>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="false">
						<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="true">
							<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
				</logic:iterate>
				
			</ul>
		</li>
		</font>
	</ul>
	<c:set var="translation">
		<digi:trn key="aim:treeVisibilitiSaveChanges">Save Changes</digi:trn>
	</c:set>
	<html:submit style="dr-menu" value="${translation}" property="saveTreeVisibility"/>
		</td>
	</tr>
</table>
</digi:form>



