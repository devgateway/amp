<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@page import="org.dgfoundation.amp.ar.CellColumn"%>
<%@page import="org.dgfoundation.amp.ar.cell.Cell"%>
<bean:define id="metadata" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />
<bean:define id="columnReport" name="viewable"
	type="org.dgfoundation.amp.ar.ColumnReportData" scope="request"
	toScope="page" />
<!-- 
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
 -->
<tr>

	<td
		style="font-size:12pt;font-weight:bold; padding-left:15px;padding-bottom: 4px;color: #C00000"
		colspan="3"><digi:trn key="rep:popu:${columnReport.columnIdTrn}">${columnReport.columnId}</digi:trn>
	<% if (!("".equals(columnReport.getRepName()))){ %> : <digi:trn
		key="rep:pop:${columnReport.repNameTrn}">${columnReport.repName}</digi:trn>
	<% } %>
	</td>
</tr>
<!-- generate report data -->
<logic:notEqual name="reportMeta" property="hideActivities" value="true">

	<logic:iterate name="columnReport" property="ownerIds" id="ownerId"
		scope="page">
		<bean:define id="ownerId" name="ownerId" type="java.lang.Long"
			scope="page" toScope="request" />
		<logic:iterate name="columnReport" property="items" id="column"
			scope="page" type="org.dgfoundation.amp.ar.Viewable">

			<%//for no funding columns%>
			<logic:notEqual name="column" property="name" value="Funding">

				<tr>
					<logic:notEqual name="column" property="name" value="Total Costs">
						<bean:define id="viewable" name="column"
							type="org.dgfoundation.amp.ar.Viewable" scope="page"
							toScope="request" />
						<bean:define id="isGroup" type="java.lang.String" value="false"
							toScope="request" />
						<jsp:include page="<%=viewable.getViewerPath()%>" />
					</logic:notEqual>
				</tr>

				<logic:equal name="column" property="name" value="Total Costs">
					<tr>
						<td></td>
						<td
							style="font-size: 8pt; font-weight: bold;text-align: left;border-bottom:1px solid;text-transform: uppercase;"><c:set
							var="key">
								aim:reportBuilder:${column.name}
							</c:set> <digi:trn key="${key}">
					 				${column.name}
							 </digi:trn>:
						</td>
						<td valign="bottom" style="font-size: 8pt;text-align: left;border-bottom:1px solid;text-transform: uppercase;">
						<table border="0" cellpadding="2" cellspacing="1" style="margin:3px ">
								
								<bean:define id="isGroup" type="java.lang.String" value="true" toScope="request" />
								<logic:iterate id="cell" name="column" property="items" type="org.dgfoundation.amp.ar.Viewable">
								<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request" />
									
								<%
								CellColumn cellColumn=(CellColumn)viewable;
								Cell c=cellColumn.getByOwner((Long)ownerId);
								if(c!=null) {
								%> 
								<tr>
									<td nowrap="nowrap" style="font-size: 8pt;text-align: right;text-transform: uppercase;font-style: italic">
									
									<c:set var="key">
										aim:reportBuilder:<%=cellColumn.getName()%>
									</c:set>
								 	<digi:trn key="${key}">
								 		<%=cellColumn.getName()%>
									 </digi:trn>:&nbsp;
									</td>
									<td style="font-size: 8pt;text-align: left;text-transform: uppercase" align="right">
										<jsp:include page="<%=viewable.getViewerPath()%>" />
									</td>		
								</tr>
								<%} %>
								</logic:iterate>
						
						</table>
						
						
						</td>
					</tr>
				</logic:equal>

			</logic:notEqual>
		</logic:iterate>
		<%//for funding columns%>
		
	<bean:define id="isGroup" type="java.lang.String" value="true" toScope="request" />
		<!--//iterate  all columns  -->
		<logic:iterate name="columnReport" property="items" id="column"
			scope="page">

			<!--//if columns is funfing -->
			<logic:equal name="column" property="name" value="Funding">
				<tr>
					<td>&nbsp;</td>
					<td colspan="2" align="right"><!--//iterate measurememts-->
					<table width="90%" cellpadding="0" cellspacing="1"
						style="margin: 3px;">
						<!--//iterate year, or quarters ..-->
						<c:set var="showHeader" value="true" scope="page" />
						<logic:iterate name="column" property="items" id="col"
							type="org.dgfoundation.amp.ar.GroupColumn">
							<% if(col.getVisibleCellCount((java.lang.Long) ownerId)!=0){ %>
							<!--//if year has data .-->
							<c:if test="${showHeader}">
								<logic:iterate name="col" property="items" id="cell"
									type="org.dgfoundation.amp.ar.Viewable" length="1">
									<tr>
										<td height="20"
											style="font-size: 8pt; font-weight: bold;text-align: center;border-bottom:1px dotted;text-transform: uppercase;"
											width="50">&nbsp;</td>
										<logic:iterate id="cellHeader" name="col" property="items"
											type="org.dgfoundation.amp.ar.Viewable">
											<td width="25%"
												style="font-size: 8pt; font-weight: bold;text-align: center;border-bottom:1px dotted;text-transform: uppercase;padding:4px;border-left:1px dotted #CCCCCC;">
											
												<c:set var="key" value="aim:reportBuilder:${cellHeader.name}">
													</c:set>
												<digi:trn key="${key}">
														<c:out value="${cellHeader.name}"></c:out>
												</digi:trn>
											</td>
										</logic:iterate>
									</tr>
								</logic:iterate>
								<c:set var="showHeader" value="false" scope="page" />
							</c:if>
							<tr>
								<td height="18" width="20%"
									style="font-size: 8pt; font-weight: bold;text-align: center;border-bottom:1px dotted;text-transform: uppercase;">
										<c:out value="${col.name}"></c:out></td>
								<logic:iterate id="cell" name="col" property="items"
									type="org.dgfoundation.amp.ar.Viewable">
									<td width="25%" nowrap="nowrap"
										style="font-size:8pt;text-align: right;border-bottom:1px dotted;border-left:1px dotted #CCCCCC; padding: 2px"
										valign="middle"><bean:define id="viewable" name="cell"
										type="org.dgfoundation.amp.ar.Viewable" scope="page"
										toScope="request" /> <jsp:include
										page="<%=viewable.getViewerPath()%>" /></td>
								</logic:iterate>


							</tr>
							<%}%>
						</logic:iterate>

					</table>

					</td>
				</tr>
				<tr>
					<td colspan="3" height="18" style="">&nbsp;</td>
				</tr>
			</logic:equal>
		</logic:iterate>

	</logic:iterate>

</logic:notEqual>
<tr>
	<td></td>

	<!-- generate total row -->
	<td colspan="2" style="border-bottom: 1px solid #000000"><bean:define
		id="viewable" name="columnReport"
		type="org.dgfoundation.amp.ar.ColumnReportData" scope="page"
		toScope="request" /> <jsp:include page="TrailCells.jsp" /></td>
</tr>
<tr>
	<td colspan="2" height="20">&nbsp;</td>
</tr>
