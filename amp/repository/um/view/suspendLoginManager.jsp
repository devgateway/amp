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