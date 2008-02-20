<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="javascript">

</script>

<digi:instance property="aHarManagerForm" />

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="center" border="0">
	<tr>
		<th width="50%"><digi:trn key="ie:import">Import</digi:trn></th>
		<th width="50%"><digi:trn key="ie:export">Export</digi:trn></th>
	</tr>
<c:if test="${not empty aHarManagerForm.errorLog}">
	<tr align="center" bgcolor="#FFBBBB">
		<td colspan="2" >
			<digi:form action="/ieManager.do?actionType=error" method="post" enctype="multipart/form-data">
				<digi:trn key="amp:export:import:page:error">while operation there produced some error</digi:trn>
				<html:submit style="dr-menu" value="view errors"/>
				&nbsp;
			</digi:form>	
		</td>
	</tr>
</c:if>	
	<tr>
		<td align="center">
			<digi:form action="/ieManager.do?actionType=upload" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td><digi:trn key="ie:selectteam">Select Team</digi:trn></td>
						<td>
							<html:select name="aHarManagerForm" property="selectedAmpTeamId" >
								<c:forEach var="vTeam" items="${aHarManagerForm.teamList}" varStatus="lStatus">
									<option value="${vTeam.ampTeamId}">${vTeam.name}</option>
								</c:forEach>
							</html:select>
						</td>
					</tr>
					<tr>
						<td><digi:trn key="ie:selectfile">Select File</digi:trn>  </td>
						<td>
							<html:file property="uploadFile"/>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>
							<html:submit style="dr-menu" value="Upload"/>				
						</td>
					</tr>
				</table>
			</digi:form>	
		</td>
		<td align="center">
			<digi:form action="/ieManager.do?actionType=export" method="post" enctype="multipart/form-data">
				<html:submit style="dr-menu" value="Export"/>
			</digi:form>	
		</td>
	</tr>
</table>

