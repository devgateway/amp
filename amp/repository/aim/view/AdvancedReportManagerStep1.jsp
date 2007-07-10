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
function check(){
	if(document.aimAdvancedReportForm.removeColumns == null){
		alert(" Please select columns to generate report ");
		return false;
	}
	return true;
}

function gotoStep() {
	if (check()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectRows" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*added here*/
function backStep() {
	if (check()){	
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=forward" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
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
						<digi:trn key="aim:report:selectColumn">
							Report Builder : Select Column
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
																<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="left" bgcolor="#eeeeee" border=0>
																	<tr height=10>	<td>	</td>	</tr>												
																	<TR bgcolor="#eeeeee">
																		<td align="center" class=box-title>
																			<digi:trn key="aim:report:AvailableColumns">Available Columns </digi:trn>
																			
																		</td>	
																	</tr>

																	<tr height=10>	<td align="right">
																	<digi:trn key="aim:report:SelectAll">Select All</digi:trn>
																	<input type="checkbox" name="checkall" onclick="checkUncheckAll();">
																	</input>
																	</td>
																	</tr>
																	
																	<TR>
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.ampColumns}">
																					
																					<!-- 
																						Because the donor report and the contribution report are now different (the donor has also the indicator columnns)
																						we have to create different c:if for each report								
																						 -->
																					
																						<c:if test="${aimAdvancedReportForm.arReportType == 'donor'}">
																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<tr bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																							<digi:trn key="aim:report:${ampColumns.columnName}"><c:out value="${ampColumns.columnName}"/></digi:trn>
																								
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							<c:out value="${ampColumns.columnId}"/>
																							</html:multibox>
																							</td>
																						</tr>
																						</logic:iterate>
																						</c:if>
																						
																						
																						<!-- the contribution report doesn't have access to columns 33-38 from amp_columns -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'contribution'}">

																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<c:if test="${ampColumns.columnId<'33' || ampColumns.columnId>'38'}">
																						<tr bgcolor="#ffffff">
																						
																							<td align="left" width="98%" valign=top>
																							<digi:trn key="aim:report:${ampColumns.columnName}"><c:out value="${ampColumns.columnName}"/></digi:trn>
																								
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
																						
																						
																						<!-- the regional report doesn't have access to columns 33-38 from amp_columns -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'regional'}">
																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<logic:notEqual name="ampColumns" property="columnId" value="5">
																						<c:if test="${ampColumns.columnId<'33' || ampColumns.columnId>'38'}">
																						<tr bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																							<digi:trn key="aim:report:${ampColumns.columnName}"><c:out value="${ampColumns.columnName}"/></digi:trn>
																								
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							<c:out value="${ampColumns.columnId}"/>	
					  																	    </html:multibox>
																							</td>
																						</tr>
																						</c:if>
																						</logic:notEqual>
																						</logic:iterate>
																						</c:if>
																					
																						<!-- the new component report containing also indicator columns 30-35 -->
																						<c:if test="${aimAdvancedReportForm.arReportType == 'component'}">
																						<logic:iterate name="aimAdvancedReportForm" id="ampColumns"	property="ampColumns" >
																						<logic:notEqual name="ampColumns" property="columnId" value="5">

																						<tr bgcolor="#ffffff">
																							<td align="left" width="98%" valign=top>
																							<digi:trn key="aim:report:${ampColumns.columnName}"><c:out value="${ampColumns.columnName}"/></digi:trn>
																								
																							</td>
																							<td align="right">
																							<html:multibox property="selectedColumns">
																							<c:out value="${ampColumns.columnId}"/>	
					  																	    </html:multibox>
																							</td>
																						</tr>
																						
																						</logic:notEqual>
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
																	<digi:trn key="aim:report:Nocolumns">No columns available to add</digi:trn>
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
																		<digi:trn key="aim:report:Selectedcolumns">Selected Columns</digi:trn>
																			
																		</td>
																	</tr>
																	<tr height=10>	<td align="right"><digi:trn key="aim:report:SelectAll">Select All</digi:trn>
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
																					
																					 <digi:trn key="aim:report:${addedColumns.columnName}"><c:out value="${addedColumns.columnName}"/></digi:trn>		
																						
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
																					<digi:trn key="aim:report:NoColumns Selected">No Columns Selected</digi:trn>
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
														<digi:trn key="btn:add">  Add </digi:trn> >>&nbsp;&nbsp;&nbsp;
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
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr()">
														<digi:trn key="btn:cancel">Cancel</digi:trn> 
													</html:button>					
													<html:button  styleClass="dr-menu" property="submitButton" onclick="javascript:gotoStep()">
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