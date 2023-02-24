<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="javascript">

	function load() {}

	function unload() {}

</script>

<%
	Long actId = (Long) request.getAttribute("actId");
	
	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),600,450,"",false,request );

	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}

%>

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="center" border="0">
	<tr>
		<td colspan="2">
			<img src="<%= actPerfChartUrl %>" width="600" height="450" border="0" usemap="#<%= actPerfChartFileName %>"><br><br>
		</td>
	</tr>

	<tr>
		<td width="50%" align="right">
			<a href="javascript:window.close()">
			<digi:trn key="um:closeThisWindow">Close this window</digi:trn></a> |
		</td>
		<td width="50%" align="left">
			<a href="javascript:window.print()">
			<digi:trn key="aim:print">Print</digi:trn></a>
		</td>
	</tr>	
	</tr>
</table>


