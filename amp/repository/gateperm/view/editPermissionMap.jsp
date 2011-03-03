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
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=1000 align=center>
		<tr>
			<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33 bgcolor=#F2F2f2><span class=crumb> <c:set var="translation">
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
			<hr />
			
			
			
			<table border=0 style="font-size:12px;" cellpadding="0" cellspacing="0">
			<tr>
						<td valign=top>
			<table class="inside">
			<tr>
			<td colspan="2" class="inside_header" align=center><div><b><digi:trn key="aim:changeexistingperms">Change Existing Permissions</digi:trn></b></div>
</td>
			</tr>
				<tr>
					<td align="right" class="inside"><digi:trn key="aim:permisiblecategory">Permissible Category</digi:trn></td>
					<td class="inside"><html:select property="permissibleCategory"
						onchange="submitForm('permissibleCategoryPicked')">
						<html:option value="select"><digi:trn key="aim:comboSelect">--Select--</digi:trn></html:option>
						<html:optionsCollection property="_availablePermissibleCategories"
							value="simpleName" label="simpleName" />
					</html:select></td>
				</tr>
				<logic:notEmpty name="permissionMapForm"
					property="permissibleCategory">
					
					<tr>
						<td align="right" class="inside">Assign a global permission to the
						entire class:</td>
						<td class="inside"><html:select property="permissionId">
							<html:option value="0">--None--</html:option>
							<html:optionsCollection property="_availablePermissions"
								value="id" label="name" />
						</html:select> <html:button property="saveGlobal"
							onclick="submitForm('saveGlobal')">Assign Global</html:button> <digi:link
							href="/managePerm.do?list" title="EDIT PERMISSIONS">
							<digi:img src="module/gateperm/images/edit.gif" border="0" />&nbsp;Edit Permissions</digi:link></td>
					
				</logic:notEmpty>
	</table>
</digi:form>
<td width=15>&nbsp;</td>
			
			</td>

			<td valign=top width=150><div><digi:trn key="aim:globalperms"></digi:trn></div>
			
			<digi:context name="exportperm" property="context/module/moduleinstance/exchangePermission.do?export" />
			<digi:context name="importperm" property="context/module/moduleinstance/exchangePermission.do?import" />
			<input type="button" name="export" value='<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>'
			onclick="javascript:window.location.href='<%=exportperm%>'" /> <input
			type="button" name="import" value='<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>'
			onclick="javascript:window.location.href='<%=importperm%>'" />
		
			<p/>
			<hr/>
</td>
			</tr>
			</table>
			
			
			
			
