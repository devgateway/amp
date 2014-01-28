<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>



<digi:instance property="suspendLoginManagerForm"/>


<table width="100%" border="0" cellpadding="0">
	<tr>
		<td align="center">	
	
			<table border="0" width="800"  class="inside">
				<tr style="background-color:#c7d4db">
					<td nowrap="nowrap" class="inside">
						name
					</td>
					<td nowrap="nowrap" class="inside">
						text
					</td>
					<td nowrap="nowrap" class="inside">
						active
					</td>
					<td nowrap="nowrap" class="inside">
						expires
					</td>
					<td nowrap="nowrap" class="inside">
						date
					</td>
					<td nowrap="nowrap" class="inside">
						actions
					</td>
				</tr>
				<logic:iterate name="suspendLoginManagerForm" property="suspendLoginObjects" id="suspendLoginObject">
					<tr>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="name"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="reasonText"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="active"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="expires"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="formatedDate"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<a href="/um/suspendLoginManager.do~action=users~objId=<bean:write name="suspendLoginObject" property="id"/>">Users</a>&nbsp;|&nbsp;
							<a href="/um/suspendLoginManager.do~action=edit~objId=<bean:write name="suspendLoginObject" property="id"/>">Edit</a>&nbsp;|&nbsp;
							<a href="/um/suspendLoginManager.do~action=delete~objId=<bean:write name="suspendLoginObject" property="id"/>">Delete</a>
						</td>
					</tr>
				</logic:iterate>
			</table>
			
		</td>
	</tr>
	<tr><td><a href="/um/suspendLoginManager.do~action=add">Add</a></td></tr>
</table>
<td width=20>&nbsp;</td>
    <td width=300 valign=top>
		<table align="center" cellpadding="0" cellspacing="0"
			width="300" border="0">
			<tr>
				<td>
					<!-- Other Links -->
					<table cellpadding="0" cellspacing="0" width="100">
						<tr>
							<td bgColor=#c9c9c7 class=box-title><digi:trn
									key="aim:otherLinks">
									<b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;"><digi:trn>Other links</digi:trn></b>
								</digi:trn></td>
							<td background="module/aim/images/corner-r.gif"
								height="17" width=17>&nbsp;</td>
						</tr>
					</table></td>
			</tr>
			<tr>
				<td bgColor=#ffffff>
					<table cellPadding=0 cellspacing="0" width="100%" class="inside">
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim" href="/admin.do">
									<digi:trn key="aim:AmpAdminHome">
								Admin Home
								</digi:trn>
								</digi:link></td>
						</tr>
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim"
									href="/workspaceManager.do~page=1">
									<digi:trn key="aim:WorkspaceManager">
								Workspace Manager
								</digi:trn>
								</digi:link></td>
						</tr>
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim"
									href="/../um/viewAllUsers.do~reset=true">
									<digi:trn>
										User Manager
									</digi:trn>
								</digi:link></td>
						</tr>
						<!-- end of other links -->
					</table></td>
			</tr>
	</table>
	</td>