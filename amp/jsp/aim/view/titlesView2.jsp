<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<digi:instance property="aimAdvancedReportForm" />

<%int rowspan = 1;%>
<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
	<%rowspan = 2;%>
</logic:equal>


<logic:iterate name="aimAdvancedReportForm" property="titles"
	id="titles" type="org.digijava.module.aim.helper.Column">

	<logic:equal name="titles" property="columnName"
		value="Type Of Assistance">
		<logic:notEqual name="aimAdvancedReportForm" property="reportType"
			value="donor">
			<td align="center" height="21" width="42" rowspan="<%=rowspan%>">
			<div align="center"><strong> <bean:write name="titles"
				property="columnName" /> </strong></div>
			</td>
		</logic:notEqual>
	</logic:equal>


	<logic:notEqual name="titles" property="columnName"
		value="Type Of Assistance">
		<td align="center" height="21" width="42" rowspan="<%=rowspan%>">
		<div align="center"><strong> <bean:write name="titles"
			property="columnName" /> </strong></div>
		</td>
	</logic:notEqual>
</logic:iterate>

<logic:equal name="aimAdvancedReportForm" property="reportType"
	value="donor">
	<logic:equal name="typeAssist" value="true">
		<td height="21" width="89" align="center" rowspan="<%=rowspan%>"><strong>Type
		of Assistance</strong></td>
	</logic:equal>
</logic:equal>