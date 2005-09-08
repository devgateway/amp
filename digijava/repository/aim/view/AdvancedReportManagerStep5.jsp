<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">

function moveUp(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the column Up ");
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
		alert("Cannot move the column Down");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=Step2MoveDown" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}	
}

function gotoStep() {

		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectMeasures" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}

function addColumn()
{
	<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=Step2AddRows" />
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
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</bean:define>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;

								<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="<%=translation%>" >
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
					Report Builder : Select Rows
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
														<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do/check=forward"   styleClass="sub-nav" title="<%=translation%>" >
														1 :   Select Columns
													</digi:link>
												</td>											
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToselectrows/hierarchies">Click here to select rows/hierarchies</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do"  styleClass="sub-nav3" title="<%=translation%>" >
														2 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
													</digi:link>
												</td>										
												<td noWrap align=left>
													<bean:define id="translation">
														<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav" title="<%=translation%>" > 
													3 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
													</digi:link>
												</td>											
												<td noWrap align=left> 
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
													</bean:define>
													<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >
														4 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
													</digi:link>
												</td>
											</tr>
										</table>	
									</td>
								</tr>
								<TR>
									<td noWrap valign=top align=left>
									 <table cellpadding=0 cellspacing=1 valign=top align=left>	<tr>	<td valign=top>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=5"  styleClass="sub-nav" title="<%=translation%>" onclick="javascript:gotoStep()">
										5 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
										</digi:link>
										</td>	
										<td noWrap valign=top align=left>
										<bean:define id="translation">
											<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
										</bean:define>
										<digi:link href="/advancedReportManager.do?check=charts"  styleClass="sub-nav" title="<%=translation%>" >
										6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
										</digi:link>
										</td>	
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
																<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="left" bgcolor="#eeeeee" >
																	<tr height=10>	<td>	</td>	</tr>												
																	<TR bgcolor="#eeeeee">
																		<td align="center" class=box-title>
																			Selected Columns
																		</td>	
																	</tr>
																	<tr height=10>	<td>	</td>	</tr>
																	
																	<TR>
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
																					<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
																						<TR bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																								<c:out value="${addedColumns.columnName}"/>
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							  <c:out value="${addedColumns.columnId}"/>
					  																	    </html:multibox>
																							</td>
																						</tr>
																					</logic:iterate>
																					
																				</c:if>
																			</TABLE>
																		</TD>
																	</TR>												
		
																	<tr>
																		<c:if test="${empty aimAdvancedReportForm.addedColumns}">
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">	
																						<td class=box-title align=center>
																							Columns to be selected before creating hierarchie
																							<br><br><br><br><br><br>
																								Please return to Step 1 : Select Column																						</td>
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
																			Column Hierarchie
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
																							<c:out value="${columnHierarchie.columnName}"/>
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
																		
																		<c:if test="${empty aimAdvancedReportForm.columnHierarchie}">
																			<td >
																				<TABLE width="100%" height="200" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
																					<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																					No Columns Hierarchie Selected 
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
													<input type=button name=back value="<< Previous"   class="dr-menu" onclick="javascript:history.back()">												
													<input type="button" name="Cancel" value="Cancel" class="dr-menu" bgcolor="#ffffff">
													<input type=button value="  Next  " class="dr-menu" onclick="javascript:gotoStep()" >															
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