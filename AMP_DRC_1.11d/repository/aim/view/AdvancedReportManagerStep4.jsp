<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function saveReport()
{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SaveReport" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}

function check(){
	var temp = document.aimAdvancedReportForm.reportTitle.value;
    var iChars = "\"*|,?_:.<>[]{}';()@&$#%!~`^-+=\\/";
	var j = "0123456789";
	var flag=0;
	var desc = document.aimAdvancedReportForm.reportDescription.value;
	var stat=0;
    var a = temp.charAt(0);

	/*if (isNaN(a)) {
		alert("Not Numeric");
	} else {
		alert("Numeric");
		flag=1;
	}*/

	if(trim(temp) == "")
	{
		alert('<digi:trn key="aim:reportBuilder:ReportSavingValidation">Your report is being saved</digi:trn>');
		document.aimAdvancedReportForm.reportTitle.value = "";
		flag = 1;
	}
	else if(!(isNaN(a)))
	{
		alert('<digi:trn key="aim:reportBuilder:ReportNumericTittleValidation">Report Title cannot start with a numeric value</digi:trn>');
		flag=1;
	}
	else
	{
		for (var i = 0; i < temp.length; i++)
		{
			if (iChars.indexOf(temp.charAt(i)) != -1)
			{
				alert('<digi:trn key="aim:reportBuilder:ReportSpecialChrTittleValidation">Please do not enter special characters in the Report Title</digi:trn>');
				flag=1;
				break;
			}
		}
	}
	if(temp.length > 30)
	{
		alert('<digi:trn key="aim:reportBuilder:ReportMaxTittleValidation">Report title should not be greater than 30 characters including spaces.</digi:trn>');
		flag=1;
	}
	if (flag == 0)
		return true;
	else
		return false;
}

function gotoStep()
{
	if(check())
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=5" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.reportTitle.value = temp;
		document.aimAdvancedReportForm.reportDescription.value = desc;
		document.aimAdvancedReportForm.submit();
	}

}

function checkForBack(){
return true;
}

/*added here*/
function backStep() {
	if (checkForBack()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectMeasures" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*ended here*/
</script>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">
<input type="hidden" name="isAdd" >

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<tr>
	<td>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td>
</tr>

<tr>

<td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
	<tr>

	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
		<table>
			<tr>
				<td>
					<table cellPadding=5 cellSpacing=0 width="100%">
						<tr>
							<td height=33><span class=crumb>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;

								<c:set var="translation">
									<digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn>
								</c:set>
								<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectcolumn">
									Report Builder : Select Column
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=4" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectrows">
									Report Builder : Select Rows
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;


								<digi:link href="/advancedReportManager.do?check=SelectMeasures" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectmeasures">
									Report Builder : Select Measure
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:trn key="aim:reportBuilder:reportDetails">
									Report Builder: Report Details
								</digi:trn>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					<digi:trn key="aim:reportBuilder:reportDetails">
					Report Builder : Report Details
					</digi:trn>
					</span>
				</td>
			</tr>
			<tr colspan="2">
				<td class=box-title align="right" valign="top">
					<img src="module/aim/images/arrow-014E86.gif"><digi:trn key="aim:report:Type">Report Type :</digi:trn>
					<bean:write name="aimAdvancedReportForm" property="arReportType"/>
				<td>
			</tr>
			<TR>
			<TD vAlign="top" align="center">

				<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4">
							<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
								<jsp:include page="AdvancedReportManagerMenu.jsp" flush="true"/>
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<tr>
												<td>
													<digi:errors/>
													<font color="red">
														<UL>
															<logic:equal name="aimAdvancedReportForm" property="duplicatedReportName" value="true">
																<LI><digi:trn key="aim:reportBuilder:error:duplicatedReportName">A report with the same name already exists. Please choose another report name.</digi:trn></LI>
															</logic:equal>
															<logic:equal name="aimAdvancedReportForm" property="blankReportName" value="true">
																<LI><digi:trn key="aim:reportBuilder:error:blankReportName">You need to put a title.</digi:trn></LI>
															</logic:equal>
															<logic:present name="aimAdvancedReportForm" property="duplicatedReportOwner">
																<LI><digi:trn key="aim:reportBuilder:error:duplicatedReportOwner">The report with this name is owned by </digi:trn>&nbsp;<bean:write name="aimAdvancedReportForm" property="duplicatedReportOwner"/>.</LI>
															</logic:present>
														</UL>
													</font>
												</td>
											</tr>
											<TR>
												<TD width="100%" bgcolor="pink" align="center"  valign=top>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
													<tr>
														<td bgColor=#f4f4f2 height=50>
															<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 border=1
															style="border-collapse: collapse" >
															<tr>
																<td align="left" height=20  bgColor=#f4f4f2 width=150 class=box-title>
																	<FONT color=red><BIG>*</BIG> </font><digi:trn key="aim:reportBuilder:ReportTitle">Report Title</digi:trn>

																</td>
															</tr>
															<tr>
																<td align="left" height=20 width=200 >
																	<html:textarea property="reportTitle" cols="60" rows="2" styleClass="inp-text" />
																</td>
															</tr>
															<tr>

																<td align="left"  bgColor=#f4f4f2 height=20 width=150 class=box-title >
																<digi:trn key="aim:reportBuilder:ReporDescription">Report Description</digi:trn>

																</td>
															</tr>
															<tr>
																<td align="left" height=60 width=200 >
																	<html:textarea property="reportDescription" cols="60" rows="10" styleClass="inp-text" />
																</td>
															</tr>
															</TABLE>
														</td>
													</tr>
													</TABLE>
												</TD>
											</TR>
											<tr bgcolor="#f4f4f2">
												<td align="center" colspan="2" bgcolor="#f4f4f2">
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:backStep()">
															<< <digi:trn key="btn:previous">Previous</digi:trn>
													</html:button>
													<c:set var="message">
														<digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
													</c:set>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
															<digi:trn key="btn:cancel">Cancel</digi:trn>
													</html:button>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:saveReport()">
															<digi:trn key="btn:saveReport">Save Report</digi:trn>
													</html:button>
												</td>
											</tr>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
			</TR>
		</table>
	</td>
	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
</tr>
</table>
</td>
</TR>
</TABLE>


</digi:form>



