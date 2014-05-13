<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<jsp:include page="/repository/aim/view/scripts/newCalendar.jsp"  />

<digi:instance property="suspendLoginManagerForm"/>
<script type="text/javascript">
function validate () {
	var name =document.getElementsByName('currentObj.name')[0].value;
	//for IE 7 and lower
	if(!String.prototype.trim){  
		  String.prototype.trim = function(){  
		    return this.replace(/^\s+|\s+$/g,'');  
	};
	}
	if (name.trim() == ""){
		errorMsg='<digi:trn jsFriendly="true" >Name field is blank</digi:trn>';
		alert(errorMsg);
		return;
	
	}
	document.suspendLoginManagerForm.submit();
  	
}
</script>	
<digi:form action="/suspendLoginManager.do?action=save" method="post">
	<html:hidden name="suspendLoginManagerForm" property="currentObj.id"/>
	<digi:errors/>
	<table>
		<tr>
			<td>Name <font color="red">*</font></td>
			<td><html:text name="suspendLoginManagerForm" property="currentObj.name"/></td>
		</tr>
		<tr>
			<td>Text</td>
			<td><html:text name="suspendLoginManagerForm" property="currentObj.reasonText"/></td>
		</tr>
		<tr>
			<td>Expires</td>
			<td><html:checkbox name="suspendLoginManagerForm" property="currentObj.expires"/></td>
		</tr>
		<tr>
			<td>Date</td>
			<td><html:text name="suspendLoginManagerForm" property="currentObj.formatedDate" styleId="txtExpireDate"/>
				<!--
				<a id="clear1" href='javascript:clearDate(document.getElementById("txtExpireDate"), "clear1")'>remove</a>
				-->
				<a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("txtExpireDate"),"clear1")'>show</a>
				</td>
		</tr>
		<tr>
			<td>Active</td>
			<td><html:checkbox name="suspendLoginManagerForm" property="currentObj.active"/></td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" value="Save" onclick="validate()"/></td>
		</tr>
	</table>
</digi:form>