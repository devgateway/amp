<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="showLogsForm" />
<bean:define id="myForm" name="showLogsForm" toScope="session" type="org.digijava.module.dataExchange.form.ShowLogsForm" />

<div style="width: 100%;" >

<table border="1" style="margin-left: auto; margin-right: auto;">
	
	<tr>
		<td>
			<strong><digi:trn>Name</digi:trn> </strong>
		</td>
		<td>
			${myForm.lpi.name}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Database ID</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.lpi.id}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Log Level</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.lpi.logType}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Date</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.lpi.dateAsString}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Time</digi:trn> </strong>
		</td>
		<td>
			${myForm.lpi.timeAsString}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Description</digi:trn> </strong>
		</td>
		<td>
			${myForm.lpi.description}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Item Type</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.lpi.itemType}</digi:trn>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="text-align: center">
			<c:if test="${myForm.lpi.logType=='ERROR'}">
			<button class="buton" onclick="deleteSource();">
				<digi:trn>Retry execution</digi:trn>
			</button> &nbsp;
			</c:if> 
			<button class="buton" onclick="new MyPanelWrapper('','/dataExchange/showLogs.do?selectedLogPerItemId=${myForm.lpi.id}').hide();">
				<digi:trn>Close</digi:trn>
			</button>
		</td>
	</tr>
</table>

</div>