<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<jsp:include page="utils/closePopup.jsp"/>


<digi:form action="/assignFieldPermissions.do">
	<html:hidden property="fieldId"  />
			<table width="100%" cellpadding="1" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td colspan="3" width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						<digi:trn key="aim:gateperm:editFieldPermissionsTitle">Edit Field Permissions</digi:trn>
					</td>
				</tr>
		<tr>
			<td colspan="3" align="center">
				<bean:define id="nameTrimmed" name="aimFieldPermissionsForm" property="fieldNameTrimmed" />			
				<b><digi:trn key="aim:gateperm:fieldName">Field Name:</digi:trn></b>&nbsp;<i>
				<digi:trn key="<%=\"fm:\"+nameTrimmed %>">
				<bean:write name="aimFieldPermissionsForm" property="fieldName"/></digi:trn></i>
			</td>
		</tr>
		<tr>
			<th><digi:trn key="aim:gateperm:name">Name</digi:trn></th>
			<th><digi:trn key="aim:gateperm:read">Read</digi:trn></th>
			<th><digi:trn key="aim:gateperm:edit">Edit</digi:trn></th>
		</tr>
		<tr>
			<td><digi:trn key="aim:gateperm:everyone">Everyone</digi:trn></td>
			<td align="center"><html:checkbox property="evRead" /></td>
			<td align="center"><html:checkbox property="evEdit" /></td>
		</tr>
		<tr>
			<td><digi:trn key="aim:gateperm:guest">Guest</digi:trn></td>
			<td align="center"><html:checkbox property="guRead" /></td>
			<td align="center"><html:checkbox property="guEdit" /></td>
		</tr>
		<tr>
			<td><digi:trn key="aim:gateperm:owner">Owner</digi:trn></td>
			<td align="center"><html:checkbox property="owRead" /></td>
			<td align="center"><html:checkbox property="owEdit" /></td>
		</tr>
		<tr>
			<td><digi:trn key="aim:gateperm:beneficiaryAgency">Beneficiary Agency</digi:trn></td>
			<td align="center"><html:checkbox property="baRead" /></td>
			<td align="center"><html:checkbox property="baEdit" /></td>
		</tr>			
		<tr>
			<td><digi:trn key="aim:gateperm:contractingAgency">Contracting Agency</digi:trn></td>
			<td align="center"><html:checkbox property="caRead" /></td>
			<td align="center"><html:checkbox property="caEdit" /></td>
		</tr>			
		<tr>
			<td><digi:trn key="aim:gateperm:executingAgency">Executing Agency</digi:trn></td>
			<td align="center"><html:checkbox property="eaRead" /></td>
			<td align="center"><html:checkbox property="eaEdit" /></td>
		</tr>			
		<tr>
			<td><digi:trn key="aim:gateperm:fundingAgency">Funding Agency</digi:trn></td>
			<td align="center"><html:checkbox property="faRead" /></td>
			<td align="center"><html:checkbox property="faEdit" /></td>
		</tr>			
		<tr>
			<td><digi:trn key="aim:gateperm:implementingAgency">Implementing Agency</digi:trn></td>
			<td align="center"><html:checkbox property="iaRead" /></td>
			<td align="center"><html:checkbox property="iaEdit" /></td>
		</tr>
		<tr>
			<td><digi:trn key="aim:gateperm:responsibleAgency">Responsible Agency</digi:trn></td>
			<td align="center"><html:checkbox property="roRead" /></td>
			<td align="center"><html:checkbox property="roEdit" /></td>
		</tr>	
		<tr>
			<td><digi:trn key="aim:gateperm:regionalGroup">Regional Group</digi:trn></td>
			<td align="center"><html:checkbox property="rgRead" /></td>
			<td align="center"><html:checkbox property="rgEdit" /></td>
		</tr>			
		<tr>
			<td><digi:trn key="aim:gateperm:sectorGroup">Sector Group</digi:trn></td>
			<td align="center"><html:checkbox property="sgRead" /></td>
			<td align="center"><html:checkbox property="sgEdit" /></td>
		</tr>			
		<tr>
			<td align="center" colspan="3">
			<html:submit styleClass="dr-menu" property="save" onclick="javascript:savePermissions();"><digi:trn key="aim:gateperm:save">Save</digi:trn></html:submit>&nbsp;&nbsp;
			<html:button property="cancel" styleClass="dr-menu" onclick="javascript:window.close()"><digi:trn key="aim:gateperm:cancel">Cancel</digi:trn></html:button>
			</td>			
		</tr>			
	</table>
</digi:form>
