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
			
			<digi:form action="/suspendLoginManager.do~action=saveUsers" method="post">	
				<html:hidden name="suspendLoginManagerForm" property="objId"/>
					<table border="0" width="800"  class="inside">
						<tr style="background-color:#c7d4db">
							<td nowrap="nowrap" class="inside" width="20%">First name</td>
							<td nowrap="nowrap" class="inside" width="20%">Last name</td>
							<td nowrap="nowrap" class="inside" width="60%">Email</td>
							<td nowrap="nowrap" class="inside" width="10"><input type="checkbox" id="selAll"></td>
						</tr>
						
						<logic:iterate name="suspendLoginManagerForm" property="allUsers" id="user">
						<tr>
							<td nowrap="nowrap" class="inside"><bean:write name="user" property="firstNames"/></td>
							<td nowrap="nowrap" class="inside"><bean:write name="user" property="lastName"/></td>
							<td nowrap="nowrap" class="inside"><bean:write name="user" property="email"/></td>
							<td nowrap="nowrap" class="inside">
								<html:multibox name="suspendLoginManagerForm" property="suspendedUserIDs">
									<bean:write name="user" property="id"/>
								</html:multibox>
							</td>
						</tr>
						</logic:iterate>
					</table>
				<html:submit value="Apply"/>
			</digi:form>
		</td>
	</tr>
</table>

	<script language="javascript">
	
	$("input[name='suspendedUserIDs']").bind( "change", function() {
		if ($("input[name='suspendedUserIDs']:not(:checked)").size() > 0) {
			$("#selAll").prop("checked", false);
		} else {
			$("#selAll").prop("checked", true);
		}
		
	});
	
	$("#selAll").bind( "change", function() {
		$("input[name='suspendedUserIDs']").prop("checked", $("#selAll").prop("checked"));
	});
</script>