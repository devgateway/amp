<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<digi:context name="exportperm"
	property="context/module/moduleinstance/exchangePermission.do?export" />
<digi:context name="importperm"
	property="context/module/moduleinstance/exchangePermission.do?import" />
	

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
		<table cellPadding=5 cellspacing="0" width="100%" border="0">
			<tr>
				<!-- Start Navigation -->
				<td height=33><span class=crumb> <c:set
					var="translation">
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
				</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:exchangePermission">Permission Exchange Management</digi:trn></td>
				<!-- End navigation -->
			</tr>
		</table>
		
		<h2><digi:trn key="aim:exchangePermission">Permission Exchange Management</digi:trn></h2>
		<input type="button" name="export" value="EXPORT"
			onclick="javascript:window.location.href='<%=exportperm%>'" /> <input
			type="button" name="import" value="IMPORT"
			onclick="javascript:window.location.href='<%=importperm%>'" />
		<h2>Import Results</h2>
		<h3>Updated Permissions:</h3>
		<logic:iterate name="updatedPermissions" id="permission">
		<bean:write name="permission"/><p/>
		</logic:iterate>
		<h3>Added Permissions:</h3>
		<logic:iterate name="addedPermissions"  id="permission">
		<bean:write name="permission"/><p/>
		</logic:iterate>
		</td>
	</tr>
</table>
