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
		
	
	<p>Template Name: <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/></p>
	<ul id="dhtmlgoodies_tree" class="dhtmlgoodies_tree">
	<bean:define name="aimVisibilityManagerForm" property="ampTreeVisibility" id="template" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
	<bean:define name="template" property="items" id="modules" type="java.util.Map"  toScope="page"/>
	<bean:define name="template" property="root" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page"/>
	<font size="2">
		<li><a href="#" id="<bean:write name="template" property="root.id"/>"><bean:write name="template" property="root.name"/></a>
			<ul>
				
				<logic:iterate name="modules" id="module" type="java.util.Map.Entry" >
				
				<bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
				<bean:define id="moduleAux2" name="moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
				
					<li id="limodule:<bean:write name="moduleAux" property="root.id"/>">
					<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
						<input onclick="toggleChildrenVisibility('limodule:<bean:write name="moduleAux" property="root.id"/>')"
						 type=checkbox id="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
						 name="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
						 value="moduleVis:<bean:write name="moduleAux" property="root.id"/>"
						/>
					</logic:equal>
					<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
						<input onclick="toggleChildrenVisibility('limodule:<bean:write name="moduleAux" property="root.id"/>')" 
						type=checkbox id="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
						name="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
						value="moduleVis:<bean:write name="moduleAux" property="root.id"/>" 
						<%= moduleAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
						 />
					</logic:equal>
							<a href="#" id="module:<bean:write name="moduleAux" property="root.id"/>">
								<digi:trn key="<%="viz:"+moduleAux.getRoot().getNameTrimmed() %>"><bean:write name="moduleAux" property="root.name"/></digi:trn>
							</a>
						<ul>
						<logic:iterate name="moduleAux" property="items" id="feature" type="java.util.Map.Entry" >
						<bean:define id="featureAux" name="feature" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
						<bean:define id="featureAux2" name="featureAux" property="root" type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility" scope="page"/>
							<li id="lifeature:<bean:write name="featureAux" property="root.id"/>">
								<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
									<input onclick="toggleChildrenVisibility('lifeature:<bean:write name="featureAux" property="root.id"/>')" 
									type=checkbox id="featureVis:<bean:write name="featureAux" property="root.id"/>" 
									name="featureVis:<bean:write name="featureAux" property="root.id"/>" 
									value="featureVis:<bean:write name="featureAux" property="root.id"/>"
								/>
								</logic:equal>

								<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
									<input onclick="toggleChildrenVisibility('lifeature:<bean:write name="featureAux" property="root.id"/>')" 
									type=checkbox id="featureVis:<bean:write name="featureAux" property="root.id"/>" 
									name="featureVis:<bean:write name="featureAux" property="root.id"/>" 
									value="featureVis:<bean:write name="featureAux" property="root.id"/>"
									<%= featureAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
								/>
								</logic:equal>
									<a href="#" id="feature:<bean:write name="featureAux" property="root.id"/>">
										<digi:trn key="<%="viz:"+featureAux.getRoot().getNameTrimmed() %>"><bean:write name="featureAux" property="root.name"/></digi:trn>
									</a>
								<ul>
									<logic:iterate name="featureAux" property="items" id="field" type="java.util.Map.Entry" >
										<bean:define id="fieldAux" name="field" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
										<bean:define id="fieldAux2" name="fieldAux" property="root" type="org.digijava.module.aim.dbentity.AmpFieldsVisibility" scope="page"/>
										<li class="dhtmlgoodies_sheet.gif">
											<logic:equal name="aimVisibilityManagerForm" property="mode" value="addNew">
												<input type=checkbox id="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
												name="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
												value="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
											/>
											</logic:equal>
											<logic:equal name="aimVisibilityManagerForm" property="mode" value="editTemplateTree">
												<input type=checkbox id="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
												name="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
												value="fieldVis:<bean:write name="fieldAux" property="root.id"/>" 
												<%= fieldAux2.isVisibleTemplateObj(currentTemplate)?"checked":"" %>
											/>
											</logic:equal>
											<a id="field:<bean:write name="fieldAux" property="root.id"/>">
												<digi:trn key="<%="viz:"+fieldAux.getRoot().getNameTrimmed() %>"><bean:write name="fieldAux" property="root.name"/></digi:trn>
											</a>
										</li>	
									</logic:iterate>
									</ul>
							</li>
						</logic:iterate>
						</ul>
					</li>
				</logic:iterate>
				
			</ul>
		</li>
		</font>
	</ul>
		
	<html:submit style="dr-menu" value="Save Tree Visibility" property="saveTreeVisibility"/>
		</td>
	</tr>
</table>
</digi:form>



