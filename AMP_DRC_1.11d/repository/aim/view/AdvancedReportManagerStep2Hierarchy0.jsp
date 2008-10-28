<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAdvancedReportForm" />
	<%boolean typeAssist=false;%>
	<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
	<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
	<c:if test="${addedColumns.columnName == 'Type Of Assistance'}">
		<%typeAssist=true;%>
	</c:if>
	</logic:iterate>
	</c:if>

	<%request.setAttribute("typeAssist",new Boolean(typeAssist));%>


	<tr><td bgColor=#f4f4f2>
	<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">

	<logic:equal name="aimAdvancedReportForm" property="option" value="A">	
	<tr bgcolor="#cccccc">
	<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
	<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
		<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
		<c:if test="${addedColumns.columnName != 'Type Of Assistance'}">
		<td align="center"  class=box-title rowspan="2">
		<c:out value="${addedColumns.columnName}"/>	
		</td>
		</c:if>
		</logic:equal>
		
		<logic:notEqual name="aimAdvancedReportForm" property="reportType" value="donor">	
		<td align="center" class="box-title" rowspan="2">
		<c:out value="${addedColumns.columnName}"/>
		</td>
		</logic:notEqual>
		
	</logic:iterate>
	</c:if>
																	
	<bean:size name="aimAdvancedReportForm" property="addedMeasures" id="measureSize"/>
	<%
		int fcnt = measureSize.intValue();
		int flag = 0;
	%>              
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">				
	<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
		<%fcnt = fcnt-1;
		flag = 1;
		%>
		</c:if>
	</logic:iterate>
	
	<%
	if(flag == 0 || fcnt > 0)
	{
	%>
	<logic:equal name="typeAssist" value="true">
	<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
	<td height="21" width="89" align="center" rowspan="2">
	<strong>Type of Assistance</strong>
	</td>
	</logic:equal>
	</logic:equal>
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="89" colspan=<%=fcnt%> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="89" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align=center >
	<b> Total 	</b>
	</td>
    </tr> 	
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="option" value="Q">	
	<tr bgcolor="#cccccc">
	<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
	<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >

		<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
		<c:if test="${addedColumns.columnName != 'Type Of Assistance'}">
		<td align="center"  class=box-title rowspan="3">
		<c:out value="${addedColumns.columnName}"/>	
		</td>
		</c:if>
		</logic:equal>
		
		<logic:notEqual name="aimAdvancedReportForm" property="reportType" value="donor">	
		<td align="center" class="box-title" rowspan="3">
		<c:out value="${addedColumns.columnName}"/>
		</td>
		</logic:notEqual>
		
	</logic:iterate>
	</c:if>
																	
	<bean:size name="aimAdvancedReportForm" property="addedMeasures" id="measureSize"/>
	<%
		int fcnt = measureSize.intValue();
		int flag = 0;
	%>              
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">				
	<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
		<%fcnt = fcnt-1;
		flag = 1;
		%>
		</c:if>
	</logic:iterate>
	
	<%
	if(flag == 0 || fcnt > 0)
	{
	%>
	<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
	<logic:equal name="typeAssist" value="true">
	<td height="21" width="89" rowspan="3" align="center">
	<strong>Type of Assistance</strong>
	</td>
	</logic:equal>
	</logic:equal>
	
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="89" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="89" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
	<b> Total 	</b>
	</td>
    </tr> 
	<tr bgcolor="#F4F4F2">
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q1</strong></td>
		<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q2</strong></td>
		<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q3</strong></td>
		<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q4</strong></td>
	</logic:iterate>
	</tr>
	</logic:equal>


	<tr> <!-- heading start -->
	
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
	<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">

	<jsp:include page="fundColView.jsp"/>

	</logic:iterate>
	</logic:iterate>


	<jsp:include page="fundColView.jsp"/>


	</tr> <!-- Heading end -->

	<logic:notEmpty name="aimAdvancedReportForm" property="report"> 
	<logic:iterate name="aimAdvancedReportForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
	
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
						
			<bean:define id="records" name="records" type="org.digijava.module.aim.helper.AdvancedReport" toScope="request"/>
			<jsp:include page="columnView.jsp">
				<jsp:param name="rowspan" value="<%=report.getReportRowSpan(typeAssist)%>" />
			</jsp:include>

			<jsp:include page="fundView.jsp"/>


		</logic:iterate>
		</tr>

		<jsp:include page="termView.jsp"/>
		
	</logic:iterate>

<!-- for colspan -->
	<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>	 					
	<tr background="#F4F4F2"><td colspan=<bean:write name="cols"/> align="right"> <b> Total :</b></td>

	<logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
			
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="89" >
		<logic:notEqual name="totFund" property="commAmount" value="0">
		<b><bean:write name="totFund" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="89">
		<logic:notEqual name="totFund" property="disbAmount" value="0">
		<b><bean:write name="totFund" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="89">
		<logic:notEqual name="totFund" property="expAmount" value="0">
		<b><bean:write name="totFund" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="89">
		<logic:notEqual name="totFund" property="plCommAmount" value="0">
		<b><bean:write name="totFund" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="89">
		<logic:notEqual name="totFund" property="plDisbAmount" value="0">
		<b><bean:write name="totFund" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="89">
		<logic:notEqual name="totFund" property="plExpAmount" value="0">
		<b><bean:write name="totFund" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<logic:notEmpty name="totFund" property="unDisbAmount">
		<td align="right" height="21" width="89" >
		<logic:notEqual name="totFund" property="unDisbAmount" value="0">
		<b><bean:write name="totFund" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:notEmpty>
		</logic:equal>
	</logic:iterate>
	</tr>																
	</logic:notEmpty>
	</TABLE></td></tr>