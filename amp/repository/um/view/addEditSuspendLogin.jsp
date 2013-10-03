<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<jsp:include page="/repository/aim/view/scripts/newCalendar.jsp"  />

<digi:instance property="suspendLoginManagerForm"/>
	
<digi:form action="/suspendLoginManager.do?action=save" method="post">
	<html:hidden name="suspendLoginManagerForm" property="currentObj.id"/>
	<digi:errors/>
	<table>
		<tr>
			<td>Name</td>
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
			<td colspan="2"><html:submit value="Save"/></td>
		</tr>
	</table>
</digi:form>