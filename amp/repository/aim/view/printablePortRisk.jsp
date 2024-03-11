<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script language="javascript">
	
	function load() {}
	function unload() {}

</script>

<%
	String actRiskChartFileName = ChartGenerator.getPortfolioRiskChartFileName(
						 session,new PrintWriter(out),600,400,"");

	String actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;

%>

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border="0">
	<tr>
		<td width="100%" colspan="2">
			<img src="<%= actRiskChartUrl %>" width=600 height=400 border="0" usemap="#<%= actRiskChartFileName %>">		
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
</table>
