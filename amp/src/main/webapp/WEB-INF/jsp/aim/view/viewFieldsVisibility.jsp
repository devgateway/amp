<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	function onDeleteField() {
		var flag = confirm("Delete this Field?");
		return flag;
	}

	function onDeleteFeature() {
		var flag = confirm("Delete this Feature?");
		return flag;
	}

	function onDeleteModule() {
		var flag = confirm("Delete this Module?");
		return flag;
	}

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left style="font-size:12px;">	

		<tr><td bgColor=#c7d4db height="25" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatures">
		<b>AMP Features</b>
	</digi:trn>
	<!-- end table title -->										
	</td></tr>

	<tr><td>
	
	<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left border="0" style="font-size:12px;" class="inside">
		<tr>
			<td class="inside" height=20 bgcolor=#F2F2f2 align=center width=85%><digi:trn key="aim:ampFeature"><b>Feature</b></digi:trn></th>
			<td class="inside" height=20 bgcolor=#F2F2f2 align=center width=15%><digi:trn key="aim:ampAction"><b>Action</b></digi:trn></th>
		</tr>
		<jsp:useBean id="urlParamsDelModule" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allModules" id="module"
			type="org.digijava.module.aim.dbentity.AmpModulesVisibility">
			<tr>
				<td align="left" class="inside"><digi:trn key="<%=\"fm:\" + module.getNameTrimmed() %>"><bean:write name="module" property="name"/></digi:trn></td>
				<c:set target="${urlParamsDelModule}" property="action" value="deleteFFM"/>
				<c:set target="${urlParamsDelModule}" property="moduleId" value="<%=module.getId()%>"/>
				<td class="inside" align=center>
					<c:set var="translation">
						<digi:trn key="aim:clickToDeleteModule">Click here to Delete Module</digi:trn>
					</c:set>
					<c:set var="deletetext">
						<digi:trn key="aim:delete">Delete</digi:trn>
					</c:set>
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelModule" 
						title="${translation}" onclick="return onDeleteModule()">${deletetext}</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	
	
	<tr><td>&nbsp;</td></tr>
	<tr><td bgColor=#c7d4db height="25" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampSections">
		<b>AMP Sections</b>
	</digi:trn>
	<!-- end table title -->										
	</td></tr>

	<tr><td>
	
	<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left class="inside" style="font-size:12px;">
		<tr>
			<td height=20 bgcolor=#F2F2f2 align=center width=80% class="inside"><digi:trn key="aim:ampSection"><b>Section</b></digi:trn></th>
			<td height=20 bgcolor=#F2F2f2 align=center width=10% class="inside"><digi:trn key="aim:ampFeature"><b>Feature</b></digi:trn></th>
			<td height=20 bgcolor=#F2F2f2 align=center width=10% class="inside"><digi:trn key="aim:ampAction"><b>Action</b></digi:trn></th>
		</tr>
		<jsp:useBean id="urlParamsDelFeature" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allFeatures" id="feature"
			type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility">
			<tr>
				<td align="left" class="inside">
				<digi:trn key='<%="fm:" + feature.getNameTrimmed()%>'>
					<bean:write name="feature" property="name"/>
				</digi:trn>
				</td>
				<td class="inside" align=center>
					<i>
					<%if(feature.getParent() != null){%>
						<digi:trn key='<%="fm:"+feature.getParent().getNameTrimmed()%>'>
						<bean:write name="module" property="name"/> 
						</digi:trn>
					<%}%>
					</i>
				</td>
				<c:set target="${urlParamsDelFeature}" property="action" value="deleteFFM"/>
				<c:set target="${urlParamsDelFeature}" property="featureId" value="<%=feature.getId()%>"/>
				<td class="inside" align=center>
					<c:set var="translation">
						<digi:trn key="aim:clickToDeleteFeature">Click here to Delete Feature</digi:trn>
					</c:set>
					<c:set var="translation">
						<digi:trn key="aim:clickToDeleteModule">Click here to Delete Module</digi:trn>
					</c:set>
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelFeature" 
						title="${translation}" onclick="return onDeleteFeature()">${deletetext}</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	<tr><td>&nbsp;</td></tr>
	
	<tr><td bgColor=#c7d4db height="25" align="center" colspan="3">
	<!-- Table title -->
	<b><digi:trn key="aim:ampFields">AMP Fields</digi:trn></b>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<tr><td>
	
	<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left border="0" style="font-size:12px;" class="inside">
		<tr>
			<td height=20 bgcolor=#F2F2f2 align=center width=70% class="inside"><b><digi:trn key="aim:ampField">Field</digi:trn></b>
			</th>
			<td height=20 bgcolor=#F2F2f2 align=center width=10% class="inside"><b><digi:trn key="aim:ampSection">Section</digi:trn></b>
			</th>
			<td height=20 bgcolor=#F2F2f2 align=center width=10% class="inside"><b><digi:trn key="aim:ampFeature">Feature</digi:trn></b>
			</th>
			<td height=20 bgcolor=#F2F2f2 align=center width=10% class="inside"><b><digi:trn key="aim:ampAction">Action</digi:trn></b>
			</th>
		</tr>
		<jsp:useBean id="urlParamsDelField" type="java.util.Map" class="java.util.HashMap"/>
		<logic:iterate name="aimVisibilityManagerForm" property="allFields" id="field"
			type="org.digijava.module.aim.dbentity.AmpFieldsVisibility">
			<tr>
				<bean:define id="feature" name="field" property="parent" type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility"/>
				<bean:define id="module" name="feature" property="parent" type="org.digijava.module.aim.dbentity.AmpModulesVisibility"/>
				<td align="left" class="inside">
					<digi:trn key='<%="fm:"+field.getNameTrimmed() %>'>
						<bean:write name="field" property="name"/>
					</digi:trn></td>
				<td class="inside" align=center>
					<i>
					<digi:trn key='<%="fm:" + feature.getNameTrimmed()%>'>
						<bean:write name="feature" property="name"/>
					</digi:trn>
					</i>
				</td>
				<td class="inside" align=center>
					<i>
					<digi:trn key='<%="fm:"+module.getNameTrimmed()%>'>
						<bean:write name="module" property="name"/>
					</digi:trn>
					</i>
				</td>
				<c:set target="${urlParamsDelField}" property="action" value="deleteFFM"/>
				<c:set target="${urlParamsDelField}" property="fieldId" value="<%=field.getId()%>"/>
				<td class="inside" align=center>
					<c:set var="translation">
						<digi:trn key="aim:clickToDeleteField">Click here to Delete Field</digi:trn>
					</c:set>
					
					[ <digi:link href="/visibilityManager.do" name="urlParamsDelField" 
						title="${translation}" onclick="return onDeleteField()">${deletetext}</digi:link> ]
				</td>
				
			</tr>
		</logic:iterate>
	</table>
	</td></tr>
	
	</digi:form>
</table>
