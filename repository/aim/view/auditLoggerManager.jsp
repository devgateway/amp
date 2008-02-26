<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
 
<script language="JavaScript">


</script>

<digi:instance property="aimAuditLoggerManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=800>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top>
	
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:AuditLoggerManager">
							Audit Logger Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:AuditLoggerManager">
							Audit Logger Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>

								
				<tr>
					<td>
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 border="1px">
							<tr>
								<th><digi:trn key="aim:name">Name</digi:trn></th>
								<th><digi:trn key="aim:objectType">Object Type</digi:trn></th>																
								<th><digi:trn key="aim:teamName">Team Name</digi:trn></th>
								<th><digi:trn key="aim:authorName">Author Name</digi:trn></th>
								<th><digi:trn key="aim:authorEmail">Author Email</digi:trn></th>
								<th><digi:trn key="aim:creationDateLogger">Creation Date</digi:trn></th>
								<th><digi:trn key="aim:editorName">Editor Name</digi:trn></th>
								<th><digi:trn key="aim:editorEmail">Editor Email</digi:trn></th>
								<th><digi:trn key="aim:changeDate">Change Date</digi:trn></th>
								<th><digi:trn key="aim:action">Action</digi:trn></th>											
								
							</tr>
							<logic:iterate name="aimAuditLoggerManagerForm" property="logs" id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
								<tr>
									<td align="center">&nbsp;<bean:write name="log" property="objectName"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="objectTypeTrimmed"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="teamName"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="authorName"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="authorEmail"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="loggedDate"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="editorName"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="editorEmail"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="modifyDate"/>&nbsp;</td>
									<td align="center">
										<logic:equal value="delete" property="action" name="log">
											<digi:trn key="admin:delete">Delete</digi:trn>
										</logic:equal>
										<logic:equal value="add" property="action" name="log">
											<digi:trn key="admin:add">Add</digi:trn>
										</logic:equal>
										<logic:equal value="update" property="action" name="log">
											<digi:trn key="admin:update">Update</digi:trn>
										</logic:equal>
									</td>
								</tr>
							</logic:iterate>
						</table>
					</td>
				</tr>
</table>



