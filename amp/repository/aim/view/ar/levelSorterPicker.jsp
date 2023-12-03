<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.ampModule.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="generatedReport" name="reportCD" property="generatedReport" type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>

<digi:form action="/viewNewAdvancedReport.do" ampModule="aim">
	<table width="400px" style="font-size: 11px;" cellpadding="2" cellspacing="2" align="center">
		<tr>
			<td align="right">
				<digi:trn key="aim:popsort:hierarchy:level">Hierarchy Level:</digi:trn>
			</td>
			<td>
				<html:select property="levelPicked" styleClass="dropdwn_sm">
					<logic:iterate name="reportMeta" property="hierarchies"  id="iter">
						<html:option value="${iter.levelId}">
							<digi:colNameTrn>${iter.column.columnName}</digi:colNameTrn>
						</html:option>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="right"><digi:trn key="aim:popsort:hierarchy:sortby">Sort by:</digi:trn></td>
			<td>
				<html:select property="levelSorter" styleClass="dropdwn_sm">
					<html:option value="Title">
						<digi:trn key="aim:popsort:hierarchy:title">Hierarchy Title</digi:trn>
					</html:option>
					<logic:iterate name="generatedReport" property="trailCells"  id="iter1">
						<c:if test="${not empty iter1.column.absoluteColumnName}">
						<html:option value="${iter1.column.absoluteColumnName}">
							<logic:iterate name="iter1" property="column.absoluteColumnAsList" id="col" indexId="idxId">
								<c:choose>
								  <c:when test="${col.contentCategory eq 'Year'}">
								    ${col.name}
								  </c:when>
								  <c:when test="${col.contentCategory eq 'Quarter'}">
								    ${col.name}
								  </c:when>
								  <c:otherwise>
                                    <digi:colNameTrn>${col.name}</digi:colNameTrn>
								  </c:otherwise>
								</c:choose>
								&nbsp;
							</logic:iterate>
						</html:option>
						</c:if>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="right"><digi:trn key="aim:popsort:hierarchy:sortorder">Sort Order:</digi:trn></td>
			<td>
				<html:select property="levelSortOrder" styleClass="dropdwn_sm">
					<html:option value="ascending"><digi:trn key="aim:popsort:ascending">Ascending</digi:trn></html:option>
					<html:option value="descending"><digi:trn key="aim:popsort:descending">Descending</digi:trn></html:option>				
				</html:select>
			</td>
		</tr>
		<tr>
			<logic:notEqual name="widget" scope="request" value="true">
				<td colspan="2" style="text-align: center;">
					<input type="hidden" name="reportContextId" value="${reportCD.contextId}" />
					<html:submit property="applySorter" styleClass="buttonx">
						<digi:trn key="aim:popsort:apply">Apply Sorting</digi:trn>
					</html:submit>
					&nbsp;&nbsp;
					<input type="button" class="buttonx" onclick="resetSorter(this);return false;" value="<digi:trn>Reset</digi:trn>" />
				</td>
			</logic:notEqual>
			<logic:equal name="widget" scope="request" value="true">			
				<td colspan="2" style="text-align: center;">
					<br>
					<input type="button" name="applySorter" class="buttonx"
					value='<digi:trn jsFriendly="true" key="aim:popsort:hierarchy:apply">Apply Sorting</digi:trn>' onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~applySorter=true~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~levelPicked='+levelPicked.options[levelPicked.selectedIndex].value+'~levelSorter='+levelSorter.options[levelSorter.selectedIndex].value+'~levelSortOrder='+levelSortOrder.options[levelSortOrder.selectedIndex].value);hideSorter();"/>
					&nbsp;&nbsp;
					<input type="button" class="buttonx" onclick="resetSorter(this);return false;" value="<digi:trn>Reset</digi:trn>" />
				</td>
			</logic:equal>
		</tr>
	</table>
</digi:form>
