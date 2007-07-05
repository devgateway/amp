<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="text/javascript" language="JavaScript">
function menuA(val){
    if (!check())
    	return false;
	<digi:context name="base" property="context/module/moduleinstance/advancedReportManager.do?check=" />
	href = "<%= base %>";
	switch (val){
    	case 0:
    		href = href + "forward";
    		break;
    	case 1:
    		href = href + "SelectCols";
    		break;
    	case 2:
    		href = href + "SelectRows";
    		break;
    	case 3:
    		href = href + "SelectMeasures";
    		break;
    	case 4:
    		href = href + "4";
    		break;
    	case 5:
    		href = href + "SelectCols";
    		break;
    }
	document.aimAdvancedReportForm.action = href;
	document.aimAdvancedReportForm.target = "_self";
	document.aimAdvancedReportForm.submit();
	
	return true;
}
function nope(){
	alert("Please do not skip steps!");
	return true;
}
</script>

<digi:instance property="aimAdvancedReportForm" />
								<tr width="100%" valign="top">
									<td height="20">
										<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
											<tr>
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectReportType">Click here to Select Report Type</digi:trn>
													</bean:define>
													<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(0)">
													1 :   <digi:trn key="aim:SelectReportType2">Select Report Type</digi:trn>
													</a>
												</td>
											<!--ends here-->
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</bean:define>
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="0">
													<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(1)">
													2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
													</a>
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="0">
													<a class="sub-nav" style="cursor:move;" title="<%=translation%>"  onclick="return nope()">
													2 :   <digi:trn key="aim:SelectColumns2">Select Columns</digi:trn>
													</a>
													</logic:lessThan>
												</td>											
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies" >Click here to select rows/hierarchies</digi:trn>
													</bean:define>
													
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="1">
													<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(2)">
													3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</a>
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="1">
													<a class="sub-nav" style="cursor:move;" title="<%=translation%>"  onclick="return nope()">
													3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</a>
													</logic:lessThan>
													
													
												</td>										
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</bean:define>
													
													<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="2">
													<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(3)">
													4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</a>
													</logic:greaterEqual>
													<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="2">
													<a class="sub-nav" style="cursor:move;" title="<%=translation%>"  onclick="return nope()">
													4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</a>
													</logic:lessThan>
													
												</td>											
												
											</tr>
										</table>	
									</td>
								</tr>
								<TR>
									<td noWrap valign=top align=left>
									 <table cellpadding=0 cellspacing=1 valign=top align=left>	<tr>	
									 <td noWrap align=left> 
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
										</bean:define>
										<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="3">
										<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(4)">
										5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
										</a>
										</logic:greaterEqual>
										<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="3">
										<a class="sub-nav" style="cursor:move;" title="<%=translation%>"  onclick="return nope()">
										5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
										</a>
										</logic:lessThan>
									</td>
   								    <td valign=top>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
										</bean:define>
										<logic:greaterEqual name="aimAdvancedReportForm" property="maxStep" value="4">
										<a class="sub-nav" style="cursor:pointer;" title="<%=translation%>"  onclick="return menuA(5)">
										6 : <digi:trn key="aim:SaveReport">Save Report</digi:trn>
										</a>
										</logic:greaterEqual>
										<logic:lessThan name="aimAdvancedReportForm" property="maxStep" value="4">
										<a class="sub-nav" style="cursor:move;" title="<%=translation%>"  onclick="return nope()">
										6 : <digi:trn key="aim:SaveReport">Save Report</digi:trn>
										</a>
										</logic:lessThan>
									</td>
								</tr>	
									</table>
									</td>	
								</tr>
								