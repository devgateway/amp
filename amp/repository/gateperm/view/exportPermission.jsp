<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<SCRIPT LANGUAGE="JavaScript">
	var checkflag = "false";
	function check() {
		field=document.getElementsByName("permissions");
		if (checkflag == "false") {
		for (i = 0; i < field.length; i++) {
		field[i].checked = true;}
		checkflag = "true";
		return "Uncheck All"; }
		else {
		for (i = 0; i < field.length; i++) {
		field[i].checked = false; }
		checkflag = "false";
		return "Check All"; }
	}
</script>
<DIV id="TipLayer"
	style="visibility: hidden; position: absolute; z-index: 1000; top: -100;"></DIV>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<digi:form action="/exchangePermission.do">
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
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
			<input type=button value="Check All" onClick="this.value=check()"/>
			<html:submit property="exportPerform">Export</html:submit>			
			<p/>
			<table border="1px" width="95%" align="center">
				<thead>
					<tr>
						<td bgcolor="#006699" class="textalb" align="center">Select</td>
						<td bgcolor="#006699" class="textalb" align="center">Name</td>
						<td bgcolor="#006699" class="textalb" align="center">Description</td>
						<td bgcolor="#006699" class="textalb" align="center">Details</td>
					</tr>
				</thead>
				<logic:iterate id="perm" name="allPermissions" scope="request">
					<tr>
						<td><html:multibox property="permissions">
							<bean:write name="perm" property="id" />
						</html:multibox></td>
						<td><bean:write name="perm" property="name" /></td>
						<td><bean:write name="perm" property="description" />&nbsp;</td>
						<td>
						<div style='position: relative; display: none;'
							id='detailsPerm-<bean:write name="perm" property="id"/>'><logic:equal
							name="perm" property="class.simpleName" value="GatePermission">
							<b>Actions:</b>
							<bean:write name="perm" property="actions" />
							<br />
							<b>Gate Init:</b>
							<bean:write name="perm" property="gateSimpleName" />
							<br />
							<b>Gate Parameters: </b>
							<bean:write name="perm" property="gateParameters" />
						</logic:equal> <logic:equal name="perm" property="class.simpleName"
							value="CompositePermission">
							<b>Children Permissions:</b>
							<bean:write name="perm" property="permissions" />
						</logic:equal></div>
						<div align="center"
							onMouseOver="stm(['Details',document.getElementById('detailsPerm-<bean:write name="perm" property="id"/>').innerHTML],Style[1])"
							onMouseOut="htm()">[<u><digi:trn
							key="aim:reportbuilder:list">list...</digi:trn></u>]</div>
						</td>
				</logic:iterate>
			</table>
			</td>
		</tr>
	</table>
</digi:form>