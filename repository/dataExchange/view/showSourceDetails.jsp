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

<digi:instance property="manageSourceForm" />
<bean:define id="myForm" name="manageSourceForm" toScope="session" type="org.digijava.module.dataExchange.form.ManageSourceForm" />

<table border="1">
	<thead>
		<tr>
			<th style="background-color:#006699; color: white" colspan="2"><digi:trn>Source Details</digi:trn></th>		
		</tr>
	</thead>
	<tr>
		<td>
			<strong><digi:trn>Name</digi:trn> </strong>
		</td>
		<td>
			${myForm.name}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Source</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.source}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Strategy</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.strategy}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Language</digi:trn> </strong>
		</td>
		<td>
			${myForm.language}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Unique Identifier</digi:trn> </strong>
		</td>
		<td>
			${myForm.uniqueIdentifier}
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Approval Status</digi:trn> </strong>
		</td>
		<td>
			<digi:trn>${myForm.approvalStatus}</digi:trn>
		</td>
	</tr>
	<tr>
		<td>
			<strong><digi:trn>Fields</digi:trn> </strong>
		</td>
		<td>
			<logic:notEmpty name="myForm" property="fields">
				<ul>
					<logic:iterate id="field" name="myForm" property="fields">
						<li>${field}</li>
					</logic:iterate>
				</ul>
			</logic:notEmpty>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="text-align: center">
			<a onclick="executeSource(${myForm.dbId},'${myForm.source }')" style="cursor:pointer; text-decoration:underline; color: blue"><digi:trn>Execute</digi:trn></a> &nbsp; | &nbsp;
			<a href="/dataExchange/showLogs.do?htmlView=true&selectedSourceId=${myForm.dbId}" style="cursor:pointer; text-decoration:underline; color: blue"><digi:trn>Show Logs</digi:trn></a> &nbsp; | &nbsp;
			<a onclick="deleteSource(${myForm.dbId});" style="cursor:pointer; text-decoration:underline; color: blue"><digi:trn>Delete</digi:trn></a> &nbsp; 
		</td>
	</tr>

</table>
