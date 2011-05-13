<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

</script>
<digi:form action="/managePermMap.do">
	<script type="text/javascript">
function submitForm(mode) {
	permissionMapForm.mode.value=mode;
	permissionMapForm.submit();
}
</script>
	<html:hidden property="mode" />
	<digi:errors />
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> <c:set var="translation">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> <a href="/aim/admin.do" styleClass="comment"
						title="${translation}">
						<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
						<c:set var="translation">
							<digi:trn key="aim:clickToGlobalPerm">Click here to goto Global Permission Manager</digi:trn>
						</c:set>
					</a>&nbsp;&gt;&nbsp; <digi:link href="/managePermMap.do"
						styleClass="comment" title="${translation}">
						<digi:trn key="aim:globalperms">Global Permission Manager</digi:trn>
						</digi:link>
						</span>
						</td>
						<!-- End navigation -->
				</tr>
			</table>
			<h2><digi:trn>Global Permissions</digi:trn></h2>
			
			<digi:context name="exportperm" property="context/module/moduleinstance/exchangePermission.do?export" />
			<digi:context name="importperm" property="context/module/moduleinstance/exchangePermission.do?import" />
			<input type="button" name="export" value='<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>'
			onclick="javascript:window.location.href='<%=exportperm%>'" /> <input
			type="button" name="import" value='<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>'
			onclick="javascript:window.location.href='<%=importperm%>'" />
		
			<p/>
			<hr/>
			<h3><digi:trn key="aim:changeexistingperms">Change Existing Permissions</digi:trn></h3>
			<table>
				<tr>
					<td align="right"><digi:trn key="aim:permisiblecategory">Permissible Category</digi:trn></td>
					<td><html:select property="permissibleCategory"
						onchange="submitForm('permissibleCategoryPicked')">
						<html:option value="select"><digi:trn key="aim:comboSelect">--Select--</digi:trn></html:option>
						<html:optionsCollection property="_availablePermissibleCategories"
							value="simpleName" label="simpleName" />
					</html:select></td>
				</tr>
				<logic:notEmpty name="permissionMapForm"
					property="permissibleCategory">
					
					<tr>
						<td align="right"><b>Assign a global permission to the
						entire class:</b></td>
						<td><html:select property="permissionId">
							<html:option value="0">--None--</html:option>
							<html:optionsCollection property="_availablePermissions"
								value="id" label="name" />
						</html:select> <html:button property="saveGlobal"
							onclick="submitForm('saveGlobal')">Assign Global</html:button> <digi:link
							href="/managePerm.do?list" title="EDIT PERMISSIONS">
							<digi:img src="module/gateperm/images/edit.gif" border="0" />&nbsp;Edit Permissions</digi:link></td>
					
				</logic:notEmpty>
				<tr>
						<td align="right">&nbsp;</td>
						
	</table>
</digi:form>
