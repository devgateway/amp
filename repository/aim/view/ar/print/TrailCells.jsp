<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<bean:define id="reportData" name="viewable"
	type="org.dgfoundation.amp.ar.ReportData" scope="request"
	toScope="page" />

<table border="1" width="700" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<!-- generate total row -->
	<tr>
		<td align="center" colspan="2"><b><digi:trn key="rep:popup:totalsFor">TOTALS FOR</digi:trn> 
		<digi:trn key="rep:popu:${reportData.columnIdTrn}">${reportData.columnId}</digi:trn>
		<% if (!("".equals(reportData.getRepName()))){ %>
			: <digi:trn key="rep:pop:${reportData.repNameTrn}">${reportData.repName}</digi:trn>
		<% } %></b></div>
		</td>

		<logic:iterate name="reportData" property="trailCells" id="cell"
			scope="page">
			<bean:define id="viewable" name="cell"
				type="org.dgfoundation.amp.ar.cell.AmountCell" scope="page"
				toScope="request" />
			<logic:notEqual name="viewable" property="amount" value="0">
				<tr>
					<td width="75%"><b><bean:write name="viewable"
						property="column.absoluteColumnName" /></b></td>
					<td width="25%"><jsp:include page="<%=viewable.getViewerPath()%>" /></td>
				</tr>
			</logic:notEqual>
		</logic:iterate>
</table>
