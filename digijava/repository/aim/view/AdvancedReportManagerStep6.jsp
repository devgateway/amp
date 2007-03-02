<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript">

function moveUp(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the column Up ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveUpMeasure" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	

}

function moveDown(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the  column Down ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveDownMeasure" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	
}

function moveUpAdjType(val)
{

	if(document.aimAdvancedReportForm.removeAdjustType.length == undefined)
		alert("Cannot move the column Up ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveUpAdjustType" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	

}
function moveDownAdjType(val)
{
	if(document.aimAdvancedReportForm.removeAdjustType.length == undefined)
		alert("Cannot move the  column Down ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveDownAdjustType" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	

}
function gotoStep() {
		/*changed from here*/
		if(document.aimAdvancedReportForm.removeColumns == null)
		alert(" Please select a Measure to generate report ");
		/*change complted*/
		
		else
		{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=4" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
		}
}
/*added here*/
function backStep() {

		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectRows" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}
/*ended here*/

function addColumn()
{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=AddMeasure" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}

function deleteColumn()
{
	if(document.aimAdvancedReportForm.removeColumns == null)
		alert(" Please select columns to remove");
	else
	{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=DeleteMeasure" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function addAdjustType()
{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=AddAdjustType" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}
function deleteAdjustType()
{
	if(document.aimAdvancedReportForm.removeAdjustType == null)
		alert(" Please select columns to remove");
	else
	{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=DeleteAdjustType" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function checkUncheckAll() {
     var items=document.aimAdvancedReportForm.selectedColumns;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.selectedColumns[i].checked = document.aimAdvancedReportForm.checkall.checked;
		}
}

function checkUncheckAll2() {
     var items=document.aimAdvancedReportForm.removeColumns;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.removeColumns[i].checked = document.aimAdvancedReportForm.checkall2.checked;
		}
}

</script>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">


<html:hidden property="moveColumn"/>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</TD>
</TR>
<TR>
	<TD width="100%" vAlign="top" align="left">
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
											<bean:define id="translation">
											<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
											</bean:define>
											<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
											<digi:trn key="aim:portfolio">
												Portfolio
											</digi:trn>
											</digi:link>&nbsp;&gt;&nbsp;

											<bean:define id="translation">
											<digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn>
											</bean:define>
											<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="<%=translation%>" >
											<digi:trn key="aim:reportBuilder:selectcolumn">
												Report Builder : Select Column
											</digi:trn>					
											&gt;&gt;		
											</digi:link>&nbsp;&nbsp;
								
											<digi:link href="/advancedReportManager.do?check=SelectMeasures" styleClass="comment" title="<%=translation%>" >
											<digi:trn key="aim:reportBuilder:selectrows">
												Report Builder : Select Rows
											</digi:trn>					
											&gt;&gt;		
											</digi:link>&nbsp;&nbsp;

											<digi:trn key="aim:reportBuilder:selectmeasures">
												Report Builder : Select Measure
											</digi:trn>					
										</td>
									</tr>
								</table>	
							</td>
						</tr>
		 				<tr>

							<td height=16 vAlign=right align=center>
								<span class=subtitle-blue>
									Report Builder : Select Measures
								</span>
							</td>
						</tr>
						<tr colspan="2">
							<td class=box-title align="right" valign="top">
								<img src="module/aim/images/arrow-014E86.gif">Report Type :
								<bean:write name="aimAdvancedReportForm" property="arReportType"/>
							<td>
						</tr>
						<TR>
							<TD vAlign="top" align="center">
								<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" 
								class="box-border-nopadding">
									<TR>
										<TD bgcolor="#f4f4f4">
											<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
												<tr width="100%" valign="top">
													<td height="20">
														<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
															<tr>
															<!--this one-->
																<td noWrap align=left> 
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToSelectReportType">Click here to Select Report Type</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do~check=forward" styleClass="sub-nav" 
																	title="<%=translation%>"  >
																		1 :   Select Report Type
																	</digi:link>
																</td>
																<!--ends here-->
																<td noWrap align=left> 
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do?check=SelectCols" styleClass="sub-nav" 
																	title="<%=translation%>"  >
																		2 :   Select Columns
																	</digi:link>
																</td>											
																<td noWrap align=left>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToselectrows/hierarchies">
																			Click here to select rows/hierarchies</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do?check=SelectRows"  styleClass="sub-nav" 
																	title="<%=translation%>" >
																		3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
																	</digi:link>
																</td>										
																<td noWrap align=left>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav3" 
																	title="<%=translation%>" > 
																		4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
																	</digi:link>
																</td>											
															</tr>
														</table>	
													</td>
												</tr>
												<TR>
													<td noWrap valign=top align=left>
														<table cellpadding=0 cellspacing=1 valign=top align=left>	
															<tr>	
																<td noWrap align=left> 
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" 
																	title="<%=translation%>" >
																		5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
																	</digi:link>
																</td>
																<td valign=top>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
																	</bean:define>
																	<digi:link href="/advancedReportManager.do?check=SelectMeasures"  
																	styleClass="sub-nav" title="<%=translation%>" onclick="javascript:gotoStep()">
																		6 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
																	</digi:link>
																</td>	
																<%--<td noWrap valign=top align=left>
																<bean:define id="translation">
																<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
																</bean:define>
																<digi:link href="/advancedReportManager.do?check=SelectMeasures"  
																styleClass="sub-nav" title="<%=translation%>" onclick="javascript:alert('Charts Coming Soon...');">
																	6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
																</digi:link>
																</td>--%>
															</tr>	
														</table>
													</td>	
												</tr>
												<TR bgColor=#f4f4f2>
													<TD vAlign="top" align="left" width="100%"></TD>
												</TR>				
												<TR bgColor=#f4f4f2>
													<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
														<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
															<TR>
																<TD width="100%" bgcolor="#f4f4f2" align="center"  valign=top>
																	<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" 
																	align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
																		<TR>
																			<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																				<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" 
																				align="left" bgcolor="#eeeeee" >
																					<tr height=10>	<td>	</td>	</tr>												
																					<TR bgcolor="#eeeeee">
																						<td align="center" class=box-title>
																							Available Measures
																						</td>	
																					</tr>
																					<tr height="10">
																						<td align="right">Select All
																							<input type="checkbox" name="checkall" onclick="checkUncheckAll();">
																							</input>
																						</td>
																					</tr>
																					<TR>
																						<TD>
																							<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" 
																							align="top" bgcolor="#aaaaaa" border=0>
																								<c:if test="${!empty aimAdvancedReportForm.ampMeasures}">
																								<logic:iterate name="aimAdvancedReportForm" id="ampMeasures"	
																								property="ampMeasures" >
																								<TR bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																										<c:out value="${ampMeasures.aliasName}"/>
																									</td>
																									<td align="right">
																										<html:multibox property="selectedColumns">
																										  <c:out value="${ampMeasures.measureId}"/>
						  																			    </html:multibox>
																									</td>
																								</tr>
																								</logic:iterate>
																								</c:if>
																							</TABLE>
																						</TD>
																					</TR>
																					<tr>
																						<c:if test="${empty aimAdvancedReportForm.ampMeasures}">
																						<td>
																							<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0 
																							vAlign="top" align="center" bgcolor="#f4f4f2">
																								<tr bgcolor="#eeeeee">
																									<td class=box-title align=center>
																										No measures available to add
																									</td>
																								</tr>
																							</table>
																						</td>
																						</c:if>
																					</tr>
																				</TABLE>
																			</TD>
																			<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																				<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" 
																				align="top" bgcolor="#eeeeee" border=0>
																					<tr height=10><td></td></tr>												
																					<TR bgcolor="#eeeeee">
																						<td align="center" class=box-title>
																							Selected Measures
																						</td>
																					</tr>
																					<tr height=10>
																						<td align="right">Select All
																							<input type="checkbox" name="checkall2" onclick="checkUncheckAll2(this);">
																							</input>
																						</td>
																					</tr>																	
																					<TR>
																						<c:if test="${!empty aimAdvancedReportForm.addedMeasures}">
																						<TD>
																						<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" 
																						align="top" bgcolor="#aaaaaa" border=0>
																							<c:if test="${!empty aimAdvancedReportForm.addedMeasures}">
																							<logic:iterate name="aimAdvancedReportForm" id="addedMeasures"	
																							property="addedMeasures" >
																							<tr bgcolor=#ffffff>
																								<td align="left" width="98%">
																									<c:out value="${addedMeasures.aliasName}"/>
																								</td>
																								<td align="right">
																									<html:multibox property="removeColumns" >
																										<c:out value="${addedMeasures.measureId}"/>
																									</html:multibox>
																								</td>
																								<td align="right">
																									<IMG alt="Move Up"  height=10 
																									src="../ampTemplate/images/up-arrow.jpg" width=10 
																									onclick="moveUp(<c:out value='${addedMeasures.measureId}' />)">
																									<IMG alt="Move Down" styleClass="test" height=10 
																									src="../ampTemplate/images/down-arrow.jpg" width=10 
																									onclick="moveDown(<c:out value='${addedMeasures.measureId}' />)">
																								</td>
																							</tr>
																							</logic:iterate>
																							</c:if>
																						</TABLE>
																						</TD>
																						</c:if>														
																		
																						<c:if test="${empty aimAdvancedReportForm.addedMeasures}">
																						<td >
																							<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0 
																							vAlign="top" align="center" bgcolor="#f4f4f2">
																								<tr bgcolor="#eeeeee">	
																									<td class=box-title align=center>
																										No measures selected 
																									</td>
																								</tr>
																							</table>
																						</td>
																						</c:if>
																					</TR>
																				</TABLE>
																			</TD>
																		</TR>
																		<TR><TD colspan="2">
																			<table width="638" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
																				<tr align="center">
																					<td align="center" class="box-title" rowspan="2" width="300">		
																						&nbsp;&nbsp;&nbsp;&nbsp;Kind of report													
																					</td>
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:radio property="reportOption" value="A">
																							<digi:trn key="aim:AnnualReport">
																								Annual
																							</digi:trn>
																						</html:radio>
																					</td>
																				<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:radio property="reportOption" value="Q"> 
																							<digi:trn key="aim:QuarterlyReport">
																								Quarterly
																							</digi:trn>
																						</html:radio>
																					</td>
																				</tr>
																				<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="hideActivities"> 
																							<digi:trn key="aim:summaryReport">
																								Summary Report
																							</digi:trn>
																						</html:checkbox>
																					</td>
																				</tr>
																	
																			<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="drilldownTab"> 
																							<digi:trn key="aim:drilldownTab">
																							Show as a drilldown in My Tabs
																							</digi:trn>
																						</html:checkbox>
																					</td>
																				</tr>
																	
																		<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="publicReport"> 
																							<b><digi:trn key="aim:publicReport">
																							Public Report
																							</digi:trn>
																							</b>
																						</html:checkbox>
																					</td>
																				</tr>
																	
																			</table>
																		</TD></TR>
																	</TABLE>
																</TD>
															</TR>
															<tr align="center">
																<td align="center">
																	<input type=button value="  Add >>   " class="dr-menu" onclick="addColumn()" >						
																	<input type=button value="<< Remove " class="dr-menu" onclick="deleteColumn()" >						
																</td>
															</tr>	<!-- Add and Remove Button-->
															<tr>	
																<td height=30>	&nbsp;</td>	</tr>
															<tr>
																<td align="right" colspan="2">
																	<input type=button name=back value="<< Previous" class="dr-menu"
																	onclick="javascript:backStep()">
																	<input type="button" name="Cancel" value=" Cancel " class="dr-menu"
																	onclick="return quitAdvRptMngr()">
																	<input type=button value="  Next >>" class="dr-menu" onclick="javascript:gotoStep()">
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
				<td class=r-dotted-lg align=left vAlign=top >&nbsp;
				</td>
			</tr>
		</table>
	</td>	
</TR>
</TABLE>
</digi:form>
