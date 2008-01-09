<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>

<!-- generate total row -->
<tr class=newClsTableL1SubTotalEndSection>
	<td colspan='<bean:write name="reportData" property="sourceColsCount"/>'>
	
	<c:if test="${reportData.levelDepth <= 2}">
		<b>
	</c:if>
	<!--<logic:present name="grandTotal" scope="request">
		<b>
	</logic:present> -->
	<i>
	<digi:trn key="rep:popup:totalsFor">TOTALS FOR</digi:trn>
	<digi:trn key="rep:popu:${reportData.columnIdTrn}">${reportData.columnId}</digi:trn>
	<% if (!("".equals(reportData.getRepName()))){ %>
		: <digi:trn key="rep:pop:${reportData.repNameTrn}">${reportData.repName}</digi:trn>
	<% } %>
	</i>
	<c:if test="${reportData.levelDepth <= 2}">
		</b>
	</c:if>
	<!-- 
	<logic:present name="grandTotal" scope="request">
		</b>
	</logic:present> -->
	</td>
	<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<td>  
		<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
		<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />			
		<jsp:include page="<%=viewable.getViewerPath()%>"/>	
		</td>
	</logic:iterate>
</tr>