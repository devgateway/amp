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
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveUp" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	
}

function moveDown(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the column Down ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveDown" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	
}

function gotoStep() {

	if(document.aimAdvancedReportForm.removeColumns == null)
		alert(" Please select columns to generate report ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectRows" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*added here*/
function backStep() {
	
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=forward" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	
}
/*ended here*/

function addColumn()
{
	<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=add" />
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
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=delete" />
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
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</bean:define>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:trn key="aim:reportBuilder:selectcolumn">
									Report Builder : Select Column
								</digi:trn>					
								&gt;		
							</td>
						</tr>
					</table>	
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					Report Builder : Select Column
					</span>
				</td>
			</tr>
			<tr colspan="2">
				<td class=box-title align="center" valign="top">

				<td>
			</tr>
			<TR>
			<TD vAlign="top" align="center">
				<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4">
							<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
								<tr width="100%" valign="top">
									<td height="20">
										<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
											<tr>
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectReportType">Click here to Select Report Type</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do~check=forward" styleClass="sub-nav" title="<%=translation%>"  >
														1 :   Select Report Type
													</digi:link>
												</td>
											<!--ends here-->
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectCols" styleClass="sub-nav3" title="<%=translation%>" >
														2 :   Select Columns
													</digi:link>
												</td>											
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies" >Click here to select rows/hierarchies</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectRows"  styleClass="sub-nav" title="<%=translation%>" >
														3 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</digi:link>
												</td>										
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav" title="<%=translation%>" > 
													4 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</digi:link>
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
													<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >
														5 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
													</digi:link>
												</td>
									 <td valign=top>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=SelectCols"  styleClass="sub-nav" title="<%=translation%>" onclick="javascript:gotoStep()">
										6 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
										</digi:link>
										</td>
									<!--	<td noWrap valign=top align=left>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=forward"  styleClass="sub-nav" title="<%=translation%>" onclick="javascript:alert('Charts Coming Soon...');">
										6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
										</digi:link>
										</td> -->	
										</tr>	</table>
									</td>	
								</tr>

								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="100%">
									</TD>
								</TR>				

								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<TR>
												<TD width="100%" bgcolor="pink" align="center"  valign=top>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
														<TR>
				
															<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="left" bgcolor="#eeeeee" border=0>
																	<tr height=10>	<td>	</td>	</tr>												
																	<TR bgcolor="#eeeeee">
																		<td align="center" class=box-title>
																			Available Columns 
																		</td>	
																	</tr>

																	<tr height=10>	<td align="right">Select All
																	<input type="checkbox" name="checkall" onclick="checkUncheckAll();">
																	</input>
																	</td>
																	</tr>
																	
																	<TR>
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.ampColumns}">
																					
																						<c:if test="${aimAdvancedReportForm.arReportType == 'donor' }">
																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<tr bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																								<c:out value="${ampColumns.columnName}"/>
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							  <c:out value="${ampColumns.columnId}"/>
					  																	    </html:multibox>
																							</td>
																						</tr>
																						</logic:iterate>
																						</c:if>

																						<c:if test="${aimAdvancedReportForm.arReportType == 'regional' || aimAdvancedReportForm.arReportType == 'component'}">
																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<c:if test="${ampColumns.columnName != 'Type Of Assistance'}">
																						<tr bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																								<c:out value="${ampColumns.columnName}"/>
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							  <c:out value="${ampColumns.columnId}"/>
					  																	    </html:multibox>
																							</td>
																						</tr>
																						</c:if>
																						</logic:iterate>
																						</c:if>
																					
																					
																				</c:if>
																			</TABLE>
																		</TD>
																	</TR>												
		
																	<tr>
																		<c:if test="${empty aimAdvancedReportForm.ampColumns}">
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																					No columns available to add 
																					</td></tr>
																				</table>
																			</td>
																		</c:if>
																	</tr>
																</TABLE>
															</TD>
				
															<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" align="top" bgcolor="#eeeeee" border=0>
																	<tr height=10>	<td>	</td>	</tr>												
																	<TR bgcolor="#eeeeee">
																		<td align="center" class=box-title>
																			Selected Columns
																		</td>
																	</tr>
																	<tr height=10>	<td align="right">Select All
																	<input type="checkbox" name="checkall2" onclick="checkUncheckAll2(this);">
																	</input>
																	</td>
																	</tr>																	
																	<tr height=10>	<td>	</td>	</tr>
																	<TR>
																		<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
																					<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
																						<tr bgcolor=#ffffff>
																						<td align="left" width="98%">
																							<c:out value="${addedColumns.columnName}"/>
																						</td>
																						<td align="right">
																							<html:multibox property="removeColumns" >
																							  <c:out value="${addedColumns.columnId}"/>
					  																	    </html:multibox>
																						</td>
																							<td align="right">
																								<IMG alt="Move Up"  height=10 src="../ampTemplate/images/up-arrow.jpg" width=10 onclick="moveUp(<c:out value='${addedColumns.columnId}' />)">
																								<IMG alt="Move Down" styleClass="test" height=10 src="../ampTemplate/images/down-arrow.jpg" width=10 onclick="moveDown(<c:out value='${addedColumns.columnId}' />)">
																							</td>
																						</tr>
																					</logic:iterate>
																				</c:if>
																			</TABLE>
																		</TD>
																		</c:if>														
																		
																		<c:if test="${empty aimAdvancedReportForm.addedColumns}">
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																					No Columns Selected 
																					</td></tr>
																				</table>
																			</td>
																		</c:if>
																	</TR>												
																</TABLE>
															</TD>
														</TR>
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
												<td align="right" colspan="2">
												    <input type=button name=back value="<< Previous"   class="dr-menu" onclick="javascript:backStep()">	
													<input type="button" name="Cancel" value=" Cancel " class="dr-menu" onclick="return quitAdvRptMngr()" >
													<input type=button value="  Next >>  " class="dr-menu" onclick="javascript:gotoStep()" >															
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