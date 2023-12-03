<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script type="text/javascript" src="<digi:file src="ampModule/aim/scripts/separateFiles/dhtmlSuite-tableWidget.js"/>"></script>

<script type="text/javascript">
<!--
function confirmDeletion(message)
{
	var msg= message;
	var temp = confirm(msg);
	return(temp);
}
//-->
</script>

<c:set var="message">
<digi:trn key="gateperm:permDeleteConfirm">Do you really want to delete this Permission object?</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
<table id="permissionsList">
<thead>
<tr><td><digi:trn>Name</digi:trn></td><td><digi:trn>Permissibles</digi:trn></td><td><digi:trn>Permission Type</digi:trn></td><td><digi:trn>Contents</digi:trn></td><td><digi:trn>Linked With</digi:trn></td><td><digi:trn>Change Permission</digi:trn></td></tr>
</thead>
<logic:iterate id="perm" name="allPermissions" scope="request">
<tr>
<td><bean:write name="perm" property="name"/></td>
<td><bean:write name="perm" property="permissibleObjects"/></td>
<td><bean:write name="perm" property="class.simpleName"/></td>
<td>
<logic:equal name="perm" property="class.simpleName" value="GatePermission">
<b><digi:trn>Actions</digi:trn>:</b>
<bean:write name="perm" property="actions"/>
<br/>
<b><digi:trn>Gate Init</digi:trn>:</b>
<bean:write name="perm" property="gateTypeName"/>
<br/>
<b><digi:trn>Gate Parameters</digi:trn>:	</b>
<bean:write name="perm" property="gateParameters"/>
</logic:equal>
<logic:equal name="perm" property="class.simpleName" value="CompositePermission">
<b><digi:trn>Permissions</digi:trn>:</b>
<bean:write name="perm" property="permissions"/>
</logic:equal>
</td>
<td>
<bean:write name="perm" property="compositeLinkedPermissions"/>
</td>

<%--
<td> 
<bean:define id="listable" name="perm" scope="page" toScope="request"/>
<jsp:include page="${listable.jspFile}"/>
</td>
 --%>
<td>
<digi:link href="/managePerm.do?edit" paramId="permissionId" paramName="perm" paramProperty="id" title="EDIT">
<digi:img src="ampModule/gateperm/images/edit.gif" border="0" />
</digi:link>
<digi:link href="/managePerm.do?delete" paramId="permissionId" onclick="return confirmDeletion('${msg}')" paramName="perm" paramProperty="id" title="DELETE">
<digi:img src="ampModule/gateperm/images/delete.gif" border="0" />
</digi:link>
</td>
</tr>
</logic:iterate>
</table>
<div align="left">&nbsp;&nbsp;
<digi:link href="/managePerm.do?new" title="NEW">
<digi:img src="ampModule/gateperm/images/add.gif" border="0" /><digi:trn>Add New Permission</digi:trn>
</digi:link>

</div>
<br/>
