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
		
	<logic:empty name="aimAdvancedReportForm" property="multiReport"> 
	<tr bgcolor="#F4F4F2">
	<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
	<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center" height="21" >
	<b>
		<digi:trn key="aim:noRecords">No Records</digi:trn>
	</b>
	</td>
	</tr>
	</logic:empty>

	<logic:notEmpty name="aimAdvancedReportForm" property="multiReport"> 
	<tr>
	<td bgColor=#f4f4f2>
	<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">
	<logic:iterate name="aimAdvancedReportForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
		<logic:iterate name="multiReport"  property="hierarchy" id="hierarchy" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
		<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">

    	<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="hierarchy" property="label" /> :
		<b><u><bean:write name="hierarchy" property="name" /></u></b>
		</td>
		</tr>
	
		<logic:notEmpty name="hierarchy"  property="project">
		
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
		<td height="21" width="69" colspan=<%=fcnt%> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align=center >
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
	<logic:equal name="typeAssist" value="true">
	<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
	<td height="21" width="89" align="center" rowspan="2">
	<strong>Type of Assistance</strong>
	</td>
	</logic:equal>
	</logic:equal>


	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
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
		
		<tr>
		
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">
		
			<jsp:include page="fundColView.jsp"/>
			
		</logic:iterate>
		</logic:iterate>

			<jsp:include page="fundColView.jsp"/>

		
		</tr> <!-- Heading end -->
				
		<logic:iterate name="hierarchy"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">

			<bean:define id="records" name="records" type="org.digijava.module.aim.helper.AdvancedReport" toScope="request"/>
			<jsp:include page="columnView.jsp">
				<jsp:param name="rowspan" value="<%=project.getReportRowSpan(typeAssist)%>" />
			</jsp:include>

			<jsp:include page="fundView.jsp"/>
			</logic:iterate>
			</tr>

			<jsp:include page="termView.jsp"/>
			
		<!-- end of by terms rows for hierarchy 0 -->
			

			
		</logic:iterate>
		
		<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>

		<logic:empty name="aimAdvancedReportForm" property="addedColumns">
		<tr><td colspan=<bean:write name="cols"/> align="right"> <b> <bean:write name="hierarchy" property="name" />&nbsp; Total :</b>
		</td></tr></logic:empty>

		<tr background="#F4F4F2">
		<logic:notEmpty name="aimAdvancedReportForm" property="addedColumns">
		<td align="right" colspan=<bean:write name="cols"/> > <b> <bean:write name="hierarchy" property="name" />&nbsp; Total :</b></td>
		</td></logic:notEmpty>
		
		<logic:present name="hierarchy" property="fundSubTotal">
		<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
				
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<b><bean:write name="fundSubTotal" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
	
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<b><bean:write name="fundSubTotal" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:notEmpty>
		</logic:equal>
	</logic:iterate>
	</logic:present>
	</td>
	</tr>																
	</logic:notEmpty>

	<!-- end of hierarchy 0 -->

	<logic:notEmpty name="hierarchy"  property="levels">
	<logic:iterate name="hierarchy"  property="levels" id="levels" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
		<!-- table for iterating levels > 0 -->
		<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">
		<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="levels" property="label" /> :
		<b><u><bean:write name="levels" property="name" /></u></b>
		</td>
		</tr>
		
		<logic:notEmpty name="levels" property="project">
		
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
		<td height="21" width="69" colspan=<%=fcnt%> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan='<bean:write name="aimAdvancedReportForm" property="fundColumns" />' align='center'>
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

	<logic:equal name="typeAssist" value="true">
		<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">	
	<td height="21" width="89" align="center" rowspan="2">
	<strong>Type of Assistance</strong>
	</td>
	</logic:equal>
	</logic:equal>

	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
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
		<tr>
		
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">

			<jsp:include page="fundColView.jsp"/>

			</logic:iterate>
		</logic:iterate>

			<jsp:include page="fundColView.jsp"/>


	   	</tr>
		<logic:iterate name="levels"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">

			<bean:define id="records" name="records" type="org.digijava.module.aim.helper.AdvancedReport" toScope="request"/>
			<jsp:include page="columnView.jsp">
				<jsp:param name="rowspan" value="<%=project.getReportRowSpan(typeAssist)%>" />
			</jsp:include>

	
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

			<jsp:include page="fundView.jsp"/>



			</logic:iterate>
			</tr>
			<jsp:include page="termView.jsp"/>

			

			
		</logic:iterate>	
		</logic:notEmpty>
		
		<logic:notEmpty name="levels" property="levels">
		<logic:iterate name="levels"  property="levels" id="level" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
		<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="level" property="label" /> :
		<b><u><bean:write name="level" property="name" /></u></b>
		</td>
		</tr>
		
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
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="69" colspan=<%=fcnt%> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align=center >
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
		<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>


	<%
	}
	%>

	<td width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
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
		<tr>
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">

			<jsp:include page="fundColView.jsp"/>

			</logic:iterate>
		</logic:iterate>
		
			<jsp:include page="fundColView.jsp"/>
		
	   	</tr>
		<logic:iterate name="level"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
			
			<bean:define id="records" name="records" type="org.digijava.module.aim.helper.AdvancedReport" toScope="request"/>
			<jsp:include page="columnView.jsp">
				<jsp:param name="rowspan" value="<%=project.getReportRowSpan(typeAssist)%>" />
			</jsp:include>
	
				
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


	<jsp:include page="fundView.jsp"/>

			</logic:iterate>
			</tr>
			
			<!-- terms breakdown for hierarchy > 0 -->
			<jsp:include page="termView.jsp"/>
			
			
	</logic:iterate>
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

		<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>
		<logic:empty name="aimAdvancedReportForm" property="addedColumns">
		<tr><td colspan=<bean:write name="cols"/> align="right"> <b> <bean:write name="level" property="name" />&nbsp; Total :</b>
		</td></tr></logic:empty>
		<tr background="#F4F4F2">
		<logic:notEmpty name="aimAdvancedReportForm" property="addedColumns">
		<td align="right" colspan=<bean:write name="cols"/> > <b> <bean:write name="level" property="name" />&nbsp; Total :</b></td></logic:notEmpty>
		<logic:present name="level" property="fundSubTotal">
		<logic:iterate name="level"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
		
			<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td align="right" height="21" width="69" >
			<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
			<b><bean:write name="fundSubTotal" property="commAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
			<b><bean:write name="fundSubTotal" property="expAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
							<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
							<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
							</logic:notEqual>
							</td>
							</logic:notEmpty>
							</logic:equal>
							
						</c:if>

			
		</logic:iterate>
		</logic:iterate>
		</logic:present>
		</tr>
		</logic:iterate>
	
		
		</logic:notEmpty>
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

		<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>
		<logic:empty name="aimAdvancedReportForm" property="addedColumns">
		<tr><td colspan=<bean:write name="cols"/> align="right"> <b> <bean:write name="levels" property="name" />&nbsp; Total :</b>
		</td></tr></logic:empty>
		<tr background="#F4F4F2">
		<logic:notEmpty name="aimAdvancedReportForm" property="addedColumns"><td align="right" colspan=<bean:write name="cols"/> > <b> <bean:write name="levels" property="name" />&nbsp; 
		<!-- h1 total -->
		Total :
		</b></td></logic:notEmpty>
		<logic:present name="levels" property="fundSubTotal">
		<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
			<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td align="right" height="21" width="69" >
			<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
			<b><bean:write name="fundSubTotal" property="commAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
			<b><bean:write name="fundSubTotal" property="expAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			</c:if>

						<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
							<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
							<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
							</logic:notEqual>
							</td>
							</logic:notEmpty>
							</logic:equal>
							
						</c:if>

			
		</logic:iterate>
		</logic:iterate>
		</logic:present>
		</tr>

	</logic:iterate>
	
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

	<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>
	<logic:empty name="aimAdvancedReportForm" property="addedColumns">
		<tr><td colspan=<bean:write name="cols"/> align="right"> <b> <bean:write name="hierarchy" property="name" />&nbsp; 
	 Total :</b>
		</td></tr></logic:empty>
	 <tr background="#F4F4F2">
<!--	 <td colspan=<bean:write name="aimAdvancedReportForm" property="addedColumns" /> >aa
	 </td>-->
	 <logic:notEmpty name="aimAdvancedReportForm" property="addedColumns">
	 <td align="right" colspan=<bean:write name="cols"/> > <b> <bean:write name="hierarchy" property="name" />&nbsp; 
	<!-- h2 total -->
	 Total :</b>
	 </td></logic:notEmpty>
	 <logic:present name="hierarchy" property="fundSubTotal">
	 <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
	 	<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">			
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<b><bean:write name="fundSubTotal" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>

		<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<b><bean:write name="fundSubTotal" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:notEmpty>
		</logic:equal>
		</c:if>
		

		</logic:iterate>
		</logic:iterate>
	</logic:present>
	</tr>			
	</logic:notEmpty>
				
 </logic:iterate>

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

<bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>
<logic:empty name="aimAdvancedReportForm" property="addedColumns">
		<tr><td colspan=<bean:write name="cols"/> align="right"> <b> Grand Total :</b>
		</td></tr></logic:empty>

 <tr background="#F4F4F2">
 <logic:notEmpty name="aimAdvancedReportForm" property="addedColumns"><td align="right" colspan=<bean:write name="cols"/> > <b> Grand Total :</b></td>
 </logic:notEmpty>
 <logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
	
	<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">			
	<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
	<td align="right" height="21" width="69" >
	<logic:notEqual name="fundTotal" property="commAmount" value="0">
	<b><bean:write name="fundTotal" property="commAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
	<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="disbAmount" value="0">
	<b><bean:write name="fundTotal" property="disbAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
	<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="expAmount" value="0">
	<b><bean:write name="fundTotal" property="expAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
	<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
	<b><bean:write name="fundTotal" property="plCommAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
	<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
	<b><bean:write name="fundTotal" property="plDisbAmount" />	</b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>
	
	<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
	<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
	<b><bean:write name="fundTotal" property="plExpAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>
	</c:if>
	<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
	<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
	<logic:notEmpty name="fundTotal" property="unDisbAmount">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
	<b><bean:write name="fundTotal" property="unDisbAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:notEmpty>
	</logic:equal>
	</c:if>

</logic:iterate>
</logic:iterate>
</tr>
</logic:iterate>
</tr>			
</td>
</tr>
</logic:notEmpty>
