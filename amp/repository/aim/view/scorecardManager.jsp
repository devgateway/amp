<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>

<digi:instance property="aimScorecardManagerForm" />
<h1 class="admintitle">
	<digi:trn>Scorecard Settings</digi:trn>
</h1>
<digi:form action="/scorecardManager.do" method="post">
	<html:hidden property="action" value="UPDATE"/>
	<table cellPadding="5" cellspacing="1">
		<tr>
			<td><digi:trn>Validation Period</digi:trn></td>
			<td><html:checkbox property="validationPeriod" value="true" /></td>
		</tr>
		<tr>
			<td><digi:trn>Validation Time</digi:trn></td>
			<td><html:select property="validationTime">
					<html:option value="1">1 <digi:trn>Week</digi:trn>
					</html:option>
					<html:option value="2">2 <digi:trn>Weeks</digi:trn>
					</html:option>
					<html:option value="3">3 <digi:trn>Weeks</digi:trn>
					</html:option>
					<html:option value="4">4 <digi:trn>Weeks</digi:trn>
					</html:option>
				</html:select></td>
		</tr>
		<tr>
		
		<tr>
			<td>&nbsp;</td>
			<td align="right" class="addUserButContainer"><c:set
					var="btnSubmit">
					<digi:trn key="btn:submit">Submit</digi:trn>
				</c:set> <html:submit value="${btnSubmit}" styleClass="dr-menu" /></td>
			<td align="left" class="addUserButContainer"><c:set
					var="btnCancel">
					<digi:trn key="btn:cancel">Cancel</digi:trn>
				</c:set> <html:submit value="${btnCancel}" styleClass="dr-menu"
					onclick="return cancel()" /></td>

		</tr>


	</table>
</digi:form>