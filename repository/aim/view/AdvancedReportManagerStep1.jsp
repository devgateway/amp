<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

	<script type="text/javascript">
		var idOfFolderTrees = ['dhtmlgoodies_tree'];
	</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />


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
		items=document.aimAdvancedReportForm.selectedThemes;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.selectedThemes[i].checked = document.aimAdvancedReportForm.checkall.checked;
		}
		document.aimAdvancedReportForm.root.checked= document.aimAdvancedReportForm.checkall.checked;;
}

function checkUncheckAll3() {
     var items=document.aimAdvancedReportForm.selectedColumns;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.selectedColumns[i].checked = document.aimAdvancedReportForm.root.checked;
		}
		items=document.aimAdvancedReportForm.selectedThemes;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.selectedThemes[i].checked = document.aimAdvancedReportForm.root.checked;
		}
		// document.aimAdvancedReportForm.root.checked= document.aimAdvancedReportForm.checkall.checked;;
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
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
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
																	<input type="checkbox" name="checkall" onclick="checkUncheckAll();"/>
																	</td>
																	</tr>
											<feature:display name="Identification" module="Project ID and Planning"></feature:display>
											<feature:display name="Planning" module="Project ID and Planning"></feature:display>
												<field:display name="Actual Approval Date" feature="Planning" >&nbsp;</field:display>
												<field:display name="Current Completion Date" feature="Planning">&nbsp;</field:display>
												<field:display name="Actual Start Date" feature="Planning">&nbsp;</field:display>
												<field:display name="Description" feature="Identification">&nbsp;</field:display>
												<field:display name="Objectives" feature="Identification">&nbsp;</field:display>
												<field:display name="Project Id" feature="Identification">&nbsp;</field:display>
												<field:display name="Project Title" feature="Identification">&nbsp;</field:display>
												<field:display name="Status" feature="Planning">&nbsp;</field:display>
												<field:display name="Team" feature="Identification">&nbsp;</field:display>
												<field:display name="A.C. Chapter" feature="Identification">&nbsp;</field:display>
												<field:display name="Accession Instrument" feature="Identification">&nbsp;</field:display>
												<field:display name="Draft" feature="Identification">&nbsp;</field:display>
											




<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>



											<feature:display name="Sectors" module="Project ID and Planning"></feature:display>
											<field:display name="Sector" feature="Sectos">&nbsp;</field:display>
											<field:display name="Sub-Sector" feature="Sectors">&nbsp;</field:display>
											
											<feature:display name="Location" module="Project ID and Planning">
												<field:display name="Implementation Level" feature="Location">&nbsp;</field:display>
												<field:display name="Region" feature="Location">&nbsp;</field:display>
											</feature:display>
											<feature:display  name="Funding Organizations" module="Funding">
												<field:display name="Cumulative Commitment" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Cumulative Disbursement" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Donor Agency" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Donor Commitment Date" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Donor Group" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Financing Instrument" feature="Funding Organizations">&nbsp;</field:display>
												<field:display name="Type Of Assistance" feature="Funding Organizations">&nbsp;</field:display>
											</feature:display>
											<feature:display name="Issues" module="Issues">
												<field:display name="Actors" feature="Issues">&nbsp;</field:display>
												<field:display name="Issues" feature="Issues">&nbsp;</field:display>
												<field:display name="Measures Taken" feature="Issues">&nbsp;</field:display>
											</feature:display>
											<feature:display name="Components" module="Components"></feature:display>
												<field:display name="Component description" feature="Components">&nbsp;</field:display>
												<field:display name="Component Name" feature="Components">&nbsp;</field:display>
											
											<feature:display name="Physical Progress" module="Components">
												<field:display name="Physical Progress" feature="Physical Progress">&nbsp;</field:display>
												<field:display name="Physical progress description" feature="Physical Progress">&nbsp;</field:display>
												<field:display name="Physical progress title" feature="Physical Progress">&nbsp;</field:display>
											</feature:display>
											<feature:display name="Reports Contact Information" module="Contact Information">
												<field:display name="Contact Name" feature="Reports Contact Information">&nbsp;</field:display>
											</feature:display>
											<feature:display name="Activity" module="M & E">
												<field:display name="Indicator Base Value" feature="Activity">&nbsp;</field:display>
												<field:display name="Indicator Current Value" feature="Activity">&nbsp;</field:display>
												<field:display name="Indicator Target Value" feature="Activity">&nbsp;</field:display>
												<field:display name="Indicator Description" feature="Activity">&nbsp;</field:display>
												<field:display name="Indicator ID" feature="Activity">&nbsp;</field:display>
												<field:display name="Indicator Name" feature="Activity"></field:display>
											</feature:display>
											<feature:display name="Costing" module="Activity Costing">
												<field:display name="Costing Donor" feature="Costing"></field:display>
												<field:display name="Total Costs" feature="Costing"></field:display>
											</feature:display>
				                            <feature:display name="Program" module="Program">
				                            	<field:display name="National Planning Objectives" feature="Program">&nbsp;</field:display>
				                            	<field:display name="Primary Program" feature="Program">&nbsp;</field:display>
												<field:display name="Secondary Program" feature="Program">&nbsp;</field:display>
				                            </feature:display>																				
																	<TR>
																		<TD>
																			<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa" border=0>
																				<c:if test="${!empty aimAdvancedReportForm.ampColumns}">

																					<html:button styleClass="dr-menu" onclick="treeObj.expandAll()" property="expand">
																						<digi:trn key="btn:expand">Expand</digi:trn>
																					</html:button>
																					<html:button styleClass="dr-menu"  property="collapse" onclick="treeObj.collapseAll()">
																						<digi:trn key="btn:collapse">Collapse</digi:trn>
																					</html:button>
																					<!--
																						Because the donor report and the contribution report are now different (the donor has also the indicator columnns)
																						we have to create different c:if for each report
																						 -->
																						 <font size="3">
																							<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
																							<bean:define name="aimAdvancedReportForm" property="ampTreeColumns" id="ampTreeColumns" type="java.util.Map"  toScope="page"/>
																							<li noDrag="true">
																							<input type=checkbox id="root"
																											name="root"
																											value="root"
																											onclick="checkUncheckAll3();"

																								/>
																							<a id="1"><digi:trn key="aim:report:AMP">AMP</digi:trn></a>
																								<ul>
																							<logic:iterate name="ampTreeColumns" id="ampTreeColumn" type="java.util.Map.Entry" >
																								<bean:define id="themeColumn" name="ampTreeColumn" property="key" type="java.lang.String" scope="page"/>
																								<bean:define id="columnCollection" name="ampTreeColumn" property="value" type="java.util.ArrayList" scope="page"/>
																								<div id="limodule:<bean:write name="themeColumn"/>" noDrag="true">
																								<li id="limodule:<bean:write name="themeColumn"/>" noDrag="true">

																								<input type=checkbox id="moduleVis:<bean:write name="themeColumn"/>"
																											name="selectedThemes"
																											value="<bean:write name="themeColumn"/>"
																											onclick="toggleChildrenVisibility('limodule:<bean:write name="themeColumn"/>')"

																								/>
																								<a id="module:<bean:write name="themeColumn"/>">
																								<digi:trn key="aim:report:${themeColumn}"><bean:write name="themeColumn"/></digi:trn>
																								</a>
																								<ul>
																								<logic:iterate name="columnCollection" id="ampColumnFromTree" type="org.digijava.module.aim.dbentity.AmpColumns">
																									<li class="dhtmlgoodies_sheet.gif" noDrag="true">
																										<input type=checkbox id="fieldVis:<bean:write name="ampColumnFromTree" property="columnId"/>"
																											name="selectedColumns"
																											value="<bean:write name="ampColumnFromTree" property="columnId"/>"
																										/>
																										<a id="field:<bean:write name="ampColumnFromTree" property="columnId"/>" >
																											<digi:trn key="aim:report:${ampColumnFromTree.columnName}"><bean:write name="ampColumnFromTree" property="columnName"/></digi:trn>
																										</a>
																									</li>
																								</logic:iterate>
																								</ul>

																								</li></div>
																							</logic:iterate>
																							</ul>
																							</li></ul>
																						</font>

<!--
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

 -->
																						<!-- the contribution report doesn't have access to columns 33-38 from amp_columns -->
<!-- 																						<c:if test="${aimAdvancedReportForm.arReportType == 'contribution'}">

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
 -->

																						<!-- the regional report doesn't have access to columns 33-38 from amp_columns -->
<!--
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
-->

																						<!-- the new component report containing also indicator columns 30-35 -->
<!--
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
	-->

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
													<c:set var="message">
														<digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
													</c:set>
													<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
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
<script type="text/javascript">
function initScripts() {
treeObj = new DHTMLSuite.JSDragDropTree();
treeObj.setTreeId('dhtmlgoodies_tree');
treeObj.init();
treeObj.showHideNode(false,'dhtmlgoodies_tree');
treeObj.expandAll();
}
window.onload=initScripts;
</script>


