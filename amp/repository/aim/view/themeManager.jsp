<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
		function validate() 
		{
			if (trim(document.aimThemeForm.programName.value).length == 0) 
			{
				alert("Please enter Program name");
				document.aimThemeForm.programName.focus();
				return false;
			}	
			if (trim(document.aimThemeForm.programCode.value).length == 0) 
			{
				alert("Please enter Program code");
				document.aimThemeForm.programCode.focus();
				return false;
			}
			if (document.aimThemeForm.programType.value == -1) 
			{
				alert("Please Select a  Program type");
				document.aimThemeForm.programType.focus();
				return false;
			}			
			if (trim(document.aimThemeForm.programType.value).length == 0) 
			{
				alert("Please enter Program type");
				document.aimThemeForm.programType.focus();
				return false;
			}
			return true;
		}
		function saveProgram()
		{
			var temp = validate();
			if (temp == true) 
			{
				<digi:context name="addThm" property="context/module/moduleinstance/addTheme.do"/>
				document.aimThemeForm.action = "<%=addThm%>";
				document.aimThemeForm.target = "_self";
				document.aimThemeForm.submit();
			}
			return true;
		}
		function addSubProgram(id,name)
		{
			<digi:context name="subProgram" property="context/module/moduleinstance/addSubTheme.do?event="/>
			document.aimThemeForm.action = "<%= subProgram %>&themeId=" + id + "&indname=" + name;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;
		}
		function editProgram(id)
		{
			openNewWindow(400,300);
			<digi:context name="editTh" property="context/module/moduleinstance/editTheme.do?event=edit"/>
			document.aimThemeForm.action = "<%= editTh %>&themeId=" + id;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
			return true;
		}
		function assignIndicators(id)
		{
			openNewWindow(650, 500);
			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do"/>
			document.aimThemeForm.action = "<%= indAssign %>?resetIndicatorId=true&themeId=" + id;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
			return true;
		}
		function deleteProgram()
		{
			return confirm("Do you want to delete the Program ?");
		}
		function load()
		{
			document.aimThemeForm.programName.value = "";
			document.aimThemeForm.programCode.value = "";
			document.aimThemeForm.programType.value = "";
			document.aimThemeForm.programDescription.value = "";		
		}

        function setOverImg(index){
          document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-righthover1.gif"
        }
        function setOutImg(index){
          document.getElementById("img"+index).src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
        }
	-->
</script>

<digi:errors/>
<digi:instance property="aimThemeForm" />
<digi:form action="/themeManager.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">

<%--  AMP Admin Logo--%> 
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<%-- End of Logo--%>

	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr><%-- Start Navigation --%>
					<td height=33><span class=crumb>
					<c:set var="ToViewAdmin">
					<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> 
						<digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:multiProgramManager">
							Strategy/Plan Manager
						</digi:trn>
					</td>
				</tr><%-- End navigation --%>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:multiProgramManager">
							Strategy/Plan Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=0 cellSpacing=0 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left>	
										<tr><td>
												<table cellspacing=0 cellpadding=0>
														<tr>
															<td noWrap height=17>
																<c:set var="viewIndicators">
																<digi:trn key="aim:viewIndicators">Click here to View Indicators</digi:trn>
																</c:set>
																<digi:link href="/themeManager.do?view=indicators"  styleClass="sub-navGovSelected" title="${viewIndicators}" onmouseover="setOverImg(1)" onmouseout="setOutImg(1)"><font color="ffffff">
																<digi:trn key="aim:programIndicatorList">
																		Program Indicator List
																</digi:trn></font>
																</digi:link>
												 			</td>
                                                            <td>
                                                              <img id="img1" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif" width="20" height="19" />
                                                            </td>
															<td noWrap height=17>
															<c:set var="MultiProgramIndicators">
																	<digi:trn key="aim:viewMultiProgramIndicators" >Click here to view Multi Program Indicators</digi:trn>
															</c:set>
																<digi:link href="/themeManager.do?view=multiprogram"  styleClass="sub-navGov" title="${MultiProgramIndicators}" ><font color="ffffff">
															<digi:trn key="aim:multiProgramManager">
																Strategy/Plan Manager
															</digi:trn></font>
																</digi:link>
															</td>
                                                            <td>
                                                              <img id="img2" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-right1.gif" width="20" height="19" />
                                                            </td>
															<td noWrap height=17>
															<c:set var="MEProjectIndicators">
																	<digi:trn key="aim:viewM&EProjectIndicators" >Click here to view M & E Project Indicators</digi:trn>
															</c:set>
															<digi:link href="/themeManager.do?view=meindicators"  styleClass="sub-navGovSelected" title="${MEProjectIndicators}" onmouseover="setOverImg(3)" onmouseout="setOutImg(3)"><font color="ffffff">
																<digi:trn key="aim:projectIndicatorList">
																		Project Indicator List
																</digi:trn></font>
																</digi:link>
															</td>		
                                                            <td>
                                                              <img id="img3" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif" width="20" height="19" />
                                                            </td>
														</tr>
													</table>
												</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
										<table align=left valign=top cellPadding=1 cellSpacing=1 width="100%">
												<tr><td>
														<table width="100%" cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff">
																<tr bgColor=#ffffff>
																<td bgColor=#d7eafd class=box-title height="20" align="center" colspan="2">
																		<digi:trn key="aim:CreatingNewProgram">
																				Create a New Program
																		</digi:trn>
																</td>
																</tr>
																<tr bgcolor=#ffffff><td height="5"></td></tr>
																<tr bgColor=#ffffff>
																<td height="10" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<digi:trn key="aim:programName">
																				Program Name
																		</digi:trn>
																		<font color="red">*</font>
																</td>
																<td height="10" align="left">
																		<html:text property="programName" size="20"/>
																</td>
																</tr>
																<tr bgcolor=#ffffff><td height="5"></td></tr>
																<tr bgColor=#ffffff>
																<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<digi:trn key="aim:programDescription">
																				Description
																		</digi:trn>
																</td>
																<td align="left">
																		<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
																</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programLeadAgency">
																					Lead Agency
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programLeadAgency" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>	
																<tr bgColor=#ffffff>
																<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<digi:trn key="aim:programCode">
																				Program Code
																		</digi:trn>
																		<font color="red">*</font>
																</td>
																<td align="left">
																		<html:text property="programCode" size="20" styleClass="inp-text"/>
																</td>
																</tr>
																<tr bgColor=#ffffff>
																<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<digi:trn key="aim:programType">
																				Program Type
																		</digi:trn>
																		<font color="red">*</font>
																</td>
																<td align="left">
																	<html:select property="programType" styleClass="inp-text">
																	<html:option value="-1">Select Progarm Type</html:option>
																		<html:optionsCollection name="aimThemeForm" property="programTypeNames"
													 						value="title" label="title" />
																		</html:select>
																		<%--<html:text property="programType" size="20" styleClass="inp-text"/>--%>
																</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programTargetGroups">
																					Target Groups
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programTargetGroups" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programBackground">
																					Background
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programBackground" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>	
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programObjectives">
																					Objectives
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programObjectives" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programOutputs">
																					Outputs
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programOutputs" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programBeneficiaries">
																					Beneficiaries
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programBeneficiaries" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>
																<tr bgColor=#ffffff>
																	<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<digi:trn key="aim:programEnvironmentConsiderations">
																					Environment Considerations
																			</digi:trn>
																	</td>
																	<td align="left">
																			<html:textarea property="programEnvironmentConsiderations" cols="35" rows="2" styleClass="inp-text"/>
																	</td>
																</tr>
																<tr bgcolor=#ffffff><td height="5"></td></tr>	
																<tr bgColor=#dddddb>
																<td align="center" bgcolor="#f4f4f2" width="75" colspan="2">
																		<input class="button" type="button" name="addBtn" value="<digi:trn key="aim:addNewProgram">Save</digi:trn>" onclick="return saveProgram()">&nbsp;&nbsp;
																		<input class="button" type="reset" value="<digi:trn key="aim:resetBtn">Cancel</digi:trn>">
																</td>
																</tr>	
														</table>
												</td></tr>
												<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
														<digi:trn key="aim:listofPrograms">
																List of Programs
														</digi:trn>
												</td></tr>
												<c:if test="${aimThemeForm.flag == 'activityReferences'}">
																	<tr>
																		<td colspan="2" align="center">
																			<font color="red"><b><digi:trn key="aim:cannotDeleteThemeMsg1">
																			Cannot delete the theme since some 
																			activities references it.
																			</digi:trn></b></font>
																		</td>
																	</tr>				
												</c:if>
												<c:if test="${aimThemeForm.flag == 'indicatorsNotEmpty'}">
																	<tr>
																		<td colspan="2" align="center">
																			<font color="red"><b><digi:trn key="aim:cannotDeleteThemeMsg2">
																			Cannot delete this program, one or more indicators are attached to it. 
																			Delete the indicator(s) before deleting the program.
																			</digi:trn></b></font>
																		</td>
																	</tr>				
												</c:if>	

											<tr><td>
													<table width="100%" cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff">
														<logic:notEmpty name="aimThemeForm" property="themes">
															<tr><td>
																<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																	<logic:iterate name="aimThemeForm" property="themes" id="themes" 
																	type="org.digijava.module.aim.dbentity.AmpTheme">
																		<tr bgcolor="#ffffff">
																			<td width="9" height="15" bgcolor="#f4f4f2">
																					<img src= "../ampTemplate/images/arrow_right.gif" border=0>
																			</td>
																			<td bgcolor="#f4f4f2" width="50">
																					<bean:write name="themes" property="themeCode"/>
																			</td>
																			<td align="left" bgcolor="#f4f4f2">
																					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="themeId">
																							<bean:write name="themes" property="ampThemeId" />
																					</c:set><b>
																					<a href="javascript:addSubProgram('<bean:write name="themes" property="ampThemeId" />','<bean:write name="themes" property="name"/>')" title="Click here to add Sub-Programs">
																							<bean:write name="themes" property="name"/>
																					</a></b>
																			</td>
																			<td align="right" bgcolor="#f4f4f2" width="75">
																					<input class="buton" type="button" name="addIndicator" value="<digi:trn key="aim:indicator">Indicator</digi:trn>" onclick="assignIndicators('<bean:write name="themes" property="ampThemeId" />')">
																			</td>
																			<td align="left" width="40" bgcolor="#f4f4f2">
																					<input class="buton" type="button" name="editButton" value="<digi:trn key="aim:edit">Edit</digi:trn>" onclick="editProgram('<bean:write name="themes" property="ampThemeId"/>')">
																			</td>
																			<td align="left" width="12" bgcolor="#f4f4f2">
																					
																					<c:set var="ToDeleteProgram">
																					<digi:trn key="aim:clickToDeleteProgram">
																									Click here to Delete Program
																							</digi:trn>
																					
																					</c:set>
																					<digi:link href="/themeManager.do?event=delete" name="urlParams" title="${ToDeleteProgram}" onclick="return deleteProgram()">
																							<img src= "../ampTemplate/images/trash_12.gif" border=0>
																					</digi:link>
																			</td>
																		</tr>
																	</logic:iterate>
																</table>
															</td></tr>
														</logic:notEmpty>
														<logic:empty name="aimThemeForm" property="themes">
																<tr align="center" bgcolor="#ffffff"><td><b>
																		<digi:trn key="aim:noProgramsPresent">No Programs present</digi:trn></b></td>
																</tr>
														</logic:empty>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>
</digi:form>
