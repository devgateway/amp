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
		alert('<digi:trn key="aim:reportBuilder:ReportColumnMoveUpValidation">Cannot move the column Up!</digi:trn>');
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=Step2MoveUp" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function moveDown(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert('<digi:trn key="aim:reportBuilder:ReportColumnMoveDownValidation">Cannot move the column Down!</digi:trn>');
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=Step2MoveDown" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
function check(){
	return true;
}
function gotoStep() {
	if (check()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectMeasures" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

/*added here*/
function backStep() {
	if (check()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectCols" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*ended here*/

function addColumn()
{

 var count=0;
	for (var i=0;i<document.aimAdvancedReportForm.selectedColumns.length;i++)
	{
		if(document.aimAdvancedReportForm.selectedColumns[i].checked)
		{
		count++;
		}
	}
	if(count>3 )
	{
		alert('<digi:trn key="aim:reportBuilder:ReportSelHierarchiesValidation">Please select three or less hierarchies</digi:trn>');
	}
	else
	{
	<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=Step2AddRows" />
	document.aimAdvancedReportForm.action = "<%= advReport %>";
	document.aimAdvancedReportForm.target = "_self";
	document.aimAdvancedReportForm.submit();
	}
}

function deleteColumn()
{
	if(document.aimAdvancedReportForm.removeColumns == null)
		alert('<digi:trn key="aim:reportBuilder:ReportSelRemoveValidation">Please select columns to remove</digi:trn>');
	else
	{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=Step2DeleteRows" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
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
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;

								<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectcolumn">
									Report Builder : Select Column
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:trn key="aim:reportBuilder:selectrows">
									Report Builder : Select Rows
								</digi:trn>

							</td>
						</tr>
					</table>
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					<digi:trn key="aim:reportBuilder:selectrows">
									Report Builder : Select Rows
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
																<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="left" bgcolor="#eeeeee" >
																	<tr height=10>	<td>	</td>	</tr>
																	<TR bgcolor="#eeeeee">
																		<td align="center" class=box-title>
																			<digi:trn key="aim:report:SelectedColumn">Selected Columns</digi:trn>
																		</td>
																	</tr>
																	<tr height=10>	<td>	</td>	</tr>

																	<TR>
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.notHierarchyColumns}">

																						<!-- Donor Funding -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'donor' }">
																						<logic:iterate name="aimAdvancedReportForm" id="notHierarchyColumns"	property="notHierarchyColumns" >

																							<c:if test="${notHierarchyColumns.columnName == 'A.C. Chapter' || notHierarchyColumns.columnName == 'Sub-Sector' || notHierarchyColumns.columnName == 'Implementation Level' || notHierarchyColumns.columnName == 'Accession Instrument' || 
																							notHierarchyColumns.columnName == 'Executing Agency' || notHierarchyColumns.columnName == 'Donor' || notHierarchyColumns.columnName == 'Donor Group' || 
																							notHierarchyColumns.columnName == 'Donor Agency' || notHierarchyColumns.columnName == 'Sector' || notHierarchyColumns.columnName == 'Status' || notHierarchyColumns.columnName == 'Region' || 
																							notHierarchyColumns.columnName == 'Type Of Assistance' || notHierarchyColumns.columnName == 'Financing Instrument' || notHierarchyColumns.columnName == 'Project Title' || 
																							notHierarchyColumns.columnName == 'National Planning Objectives' || notHierarchyColumns.columnName == 'Primary Program' || notHierarchyColumns.columnName == 'Componente' || 
																							notHierarchyColumns.columnName == 'Secondary Program' || notHierarchyColumns.columnName == 'Donor Type' || notHierarchyColumns.columnName == 'Credit/Donation' ||notHierarchyColumns.columnName == 'Beneficiary Agency' 
																							||notHierarchyColumns.columnName == 'Implementing Agency'
																							||notHierarchyColumns.columnName == 'Component Name'
																							}">
																								<TR bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																									<digi:trn key="aim:report:${notHierarchyColumns.columnName}"><c:out value="${notHierarchyColumns.columnName}"/></digi:trn>

																									</td>
																									<td align="right">
																									<html:multibox property="selectedColumns">

																									<c:out value="${notHierarchyColumns.columnId}"/>

																									  </html:multibox>
																									</td>
																								</tr>
																							</c:if>

																						</logic:iterate>
																						</c:if>

																						<!-- Regional Funding -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'regional' }">
																						<logic:iterate name="aimAdvancedReportForm" id="notHierarchyColumns"	property="notHierarchyColumns" >
																							<c:if test="${notHierarchyColumns.columnName == 'Status' || 
																							notHierarchyColumns.columnName == 'A.C. Chapter' || notHierarchyColumns.columnName == 'Accession Instrument' || notHierarchyColumns.columnName == 'Financing Instrument' || 
																							notHierarchyColumns.columnName == 'Implementation Level' || notHierarchyColumns.columnName == 'National Planning Objectives' || notHierarchyColumns.columnName == 'Primary Program' || 
																							notHierarchyColumns.columnName == 'Secondary Program' || notHierarchyColumns.columnName == 'Sub-Sector' || notHierarchyColumns.columnName == 'Type Of Assistance' || 
																							notHierarchyColumns.columnName == 'Sector' || notHierarchyColumns.columnName == 'Region' || 
																							notHierarchyColumns.columnName == 'Project Title' ||notHierarchyColumns.columnName == 'Beneficiary Agency' ||notHierarchyColumns.columnName == 'Implementing Agency'}">
																								<tr bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																									<digi:trn key="aim:report:${notHierarchyColumns.columnName}"><c:out value="${notHierarchyColumns.columnName}"/></digi:trn>

																									</td>
																									<td align="right">
																									<html:multibox property="selectedColumns">
																									<c:out value="${notHierarchyColumns.columnId}"/>
																										</html:multibox>
																									</td>
																								</tr>
																							</c:if>
																						</logic:iterate>
																						</c:if>

																						<!-- Component Funding -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'component' }">
																						<logic:iterate name="aimAdvancedReportForm" id="notHierarchyColumns"	property="notHierarchyColumns" >
																							<c:if test="${notHierarchyColumns.columnName == 'Status' ||
																							notHierarchyColumns.columnName == 'A.C. Chapter' || notHierarchyColumns.columnName == 'Accession Instrument' || notHierarchyColumns.columnName == 'Financing Instrument' || 
																							notHierarchyColumns.columnName == 'Implementation Level' || notHierarchyColumns.columnName == 'National Planning Objectives' || notHierarchyColumns.columnName == 'Primary Program' || 
																							notHierarchyColumns.columnName == 'Secondary Program' || notHierarchyColumns.columnName == 'Sub-Sector' || notHierarchyColumns.columnName == 'Type Of Assistance' || 
																							notHierarchyColumns.columnName == 'Sector' || notHierarchyColumns.columnName == 'Component Name' || notHierarchyColumns.columnName == 'Project Title'|| notHierarchyColumns.columnName == 'Beneficiary Agency' || notHierarchyColumns.columnName == 'Implementing Agency'}">
																								<tr bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																									  	<digi:trn key="aim:report:${notHierarchyColumns.columnName}"><c:out value="${notHierarchyColumns.columnName}"/></digi:trn>
																								  	</td>
																									<td align="right">
																									<html:multibox property="selectedColumns">
																									  	<c:out value="${notHierarchyColumns.columnId}"/>
																									 </html:multibox>
																									</td>
																								</tr>
																							</c:if>
																						</logic:iterate>
																						</c:if>


																							<!-- Contribution Funding -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'contribution' }">
																						<logic:iterate name="aimAdvancedReportForm" id="notHierarchyColumns"	property="notHierarchyColumns" >
																							<c:if test="${notHierarchyColumns.columnName == 'Costing Donor' || notHierarchyColumns.columnName == 'National Planning Objectives' || notHierarchyColumns.columnName == 'Primary Program' || 
																							notHierarchyColumns.columnName == 'Project Title' || notHierarchyColumns.columnName == 'Secondary Program' || notHierarchyColumns.columnName == 'Sub-Sector' || 
																							 notHierarchyColumns.columnName == 'Status' || notHierarchyColumns.columnName == 'Sector' || notHierarchyColumns.columnName == 'A.C. Chapter' || 
																							 notHierarchyColumns.columnName == 'Accession Instrument' || notHierarchyColumns.columnName == 'Donor'}">
																								<tr bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																										<digi:trn key="aim:report:${notHierarchyColumns.columnName}"> <c:out value="${notHierarchyColumns.columnId}"/></digi:trn>
																									</td>
																									<td align="right">
																									<html:multibox property="selectedColumns">
																									<c:out value="${notHierarchyColumns.columnId}"/>
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
																		<c:if test="${empty aimAdvancedReportForm.notHierarchyColumns}">
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">
																						<td class=box-title align=center>
																						<digi:trn key="aim:report:Columnstobeselected">Columns to be selected before creating hierarchie</digi:trn>
																							<br><br><br><br><br><br>
																							<digi:trn key="aim:report:Pleasereturn">Please return to Step 1 : Select Column</digi:trn>
																						</td>
																					</tr>
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
																			<digi:trn key="aim:report:ColumnHierarchy">Column Hierarchy</digi:trn>
																		</td>
																	</tr>
																	<tr height=10>	<td>	</td>	</tr>
																	<TR>
																		<c:if test="${!empty aimAdvancedReportForm.columnHierarchie}">
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.columnHierarchie}">
																				<% int i =0; %>
																					<logic:iterate name="aimAdvancedReportForm" id="columnHierarchie"	property="columnHierarchie" >
																					<% i = i + 1; %>
																						<tr bgcolor=#ffffff>
																						<td align="left" width="98%">
																							<% if(i > 1 )	{ 	%>
																								<% for(int j=0; j<i; j++)	{  %>
																							   		&nbsp;
																							   	<% }	%>
																									<IMG src="../ampTemplate/images/link_out_bot.gif">
																							<%	}	%>
																							<digi:trn key="aim:report:${columnHierarchie.columnName}"><c:out value="${columnHierarchie.columnName}"/></digi:trn>

																						</td>
																						<td align="right">
																							<html:multibox property="removeColumns" >
																								<c:out value="${columnHierarchie.columnId}"/>
																						    </html:multibox>
																						</td>
																							<td align="right">
																								<IMG alt="Move Up"  height=10 src="../ampTemplate/images/up-arrow.jpg" width=10 onclick="moveUp(<c:out value='${columnHierarchie.columnId}' />)">
																								<IMG alt="Move Down" styleClass="test" height=10 src="../ampTemplate/images/down-arrow.jpg" width=10 onclick="moveDown(<c:out value='${columnHierarchie.columnId}' />)">
																							</td>
																						</tr>
																					</logic:iterate>
																				</c:if>
																			</TABLE>
																		</TD>
																		</c:if>

																		<c:if test="${empty aimAdvancedReportForm.columnHierarchie}">Column Hierarchy
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																					<digi:trn key="aim:report:NoColumnsHierarchie">No Columns Hierarchie Selected</digi:trn>
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
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="addColumn()">
														 <digi:trn key="btn:add">Add</digi:trn> >>
													</html:button>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="deleteColumn()">
														<< <digi:trn key="btn:remove">Remove</digi:trn>
													</html:button>
												</td>
											</tr>	<!-- Add and Remove Button-->
											<tr>
												<td align="right" colspan="2">
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:backStep()">
														<< <digi:trn key="btn:previous">Previous</digi:trn>
													</html:button>

													<c:set var="message">
														<digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
													</c:set>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
														<digi:trn key="btn:cancel">Cancel</digi:trn>
													</html:button>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:gotoStep()">
														<digi:trn key="btn:next">Next</digi:trn> >>
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



