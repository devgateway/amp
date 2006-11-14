<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
		function addSubProgram(id)
		{
			<digi:context name="subProgram" property="context/module/moduleinstance/addSubTheme.do?event=addSubProgram"/>
			document.aimThemeForm.action = "<%= subProgram %>&themeId=" + id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;
		}
		function saveProgram(id)
		{
			<digi:context name="addSubThm" property="context/module/moduleinstance/addSubTheme.do?event=save" />
			document.aimThemeForm.action = "<%= addSubThm %>&themeId=" + id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;
		}
		function assignIndicators(id)
		{
			openNewWindow(650, 500);
			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do"/>
			document.aimThemeForm.action = "<%= indAssign %>?themeId=" + id;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
			return true;
		}
		function deleteProgram()
		{
			return confirm("Do you want to delete the Program ?");
		}
		function load(){}
		function unload(){}
	-->
</script>

<digi:errors/>
<digi:instance property="aimThemeForm" />
<digi:form action="/addSubTheme.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">

<%--  AMP Admin Logo --%>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<%-- End of Logo --%>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewProgramManager">Click here to goto Program Manager</digi:trn>
						</bean:define>
						<digi:link href="/themeManager.do" styleClass="comment" title="<%=translation%>">
						<digi:trn key="aim:programManager">
							Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:subProgram">
							Sub-Programs
						</digi:trn>
					</td>
					<%-- End navigation --%>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:programManager">
						Program Manager
						</digi:trn>
						</span>
					</td>
				</tr>

				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
										<table align=left valign=top cellPadding=1 cellSpacing=1 width="100%">
											<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
													<tr bgColor=#ffffff>
													<td bgColor=#d7eafd height="10" align="center" colspan="2"><h3>
													<digi:trn key="aim:CreatingNewSubProgram">
															Create a New Sub-Program
													</digi:trn></h3>
													</td>
													</tr>
													<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
													<tr bgColor=#ffffff>
													<td height="10" align="left">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<digi:trn key="aim:programName">
																	Program Name</digi:trn>
															<font color="red">*</font>
													</td>
													<td height="10" align="left">
															<html:text property="programName" size="20"/>
													</td>
													</tr>
													<tr bgcolor=#ffffff><td height="5"></td></tr>
													<tr bgColor=#ffffff>
													<td height="20" align="left">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<digi:trn key="aim:programDescription">
																	Description</digi:trn>
													</td>
													<td align="left">
															<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
													</td>
													</tr>	
													<tr bgColor=#ffffff>
													<td height="20" align="left">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<digi:trn key="aim:programCode">
																	Program Code</digi:trn>
															<font color="red">*</font>
													</td>
													<td align="left">
															<html:text property="programCode" size="20" styleClass="inp-text"/>
													</td>
													</tr>
													<tr bgColor=#ffffff>
													<td height="20" align="left">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<digi:trn key="aim:programType">
																	Program Type</digi:trn>
															<font color="red">*</font>
													</td>
													<td align="left">
															<html:text property="programType" size="20" styleClass="inp-text"/>
													</td>
													</tr>	
													<tr bgcolor=#ffffff><td height="5"></td></tr>	
													<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
													<tr bgColor=#d7eafd>
													<td bgColor=#d7eafd height="25" align="center" colspan="2">
															<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="prgParentThemeId"/>')">
													</td>
													</tr>	
											</table>
											</td></tr>
											<tr bgColor=#ffffff><td height="7" colspan="2"></td></tr>
											<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
											<tr><td bgColor=#d7eafd height="7" align="center"><h3>
													<digi:trn key="aim:subPrograms">
															Sub-Programs
													</digi:trn></h3>
											</td></tr>
											<tr><td>
														<logic:notEmpty name="aimThemeForm" property="subPrograms">
															<tr bgColor=#ffffff><td>
																<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																	<logic:iterate name="aimThemeForm" property="subPrograms" id="subPrograms" 
																	type="org.digijava.module.aim.dbentity.AmpTheme">
																		<tr bgcolor="#ffffff">
																			<td width="9" height="15" bgcolor="#f4f4f2">
																					<img src= "../ampTemplate/images/arrow_right.gif" border=0>
																			</td>
																			<td bgcolor="#f4f4f2" width="50">
																					<bean:write name="subPrograms" property="themeCode"/>
																			</td>
																			<td align="left" bgcolor="#f4f4f2">
																					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="themeId">
																							<bean:write name="subPrograms" property="ampThemeId" />
																					</c:set><b>
																					<a href="javascript:addSubProgram('<bean:write name="subPrograms" property="ampThemeId"/>')" title="Click here to add Sub-Programs">
																							<bean:write name="subPrograms" property="name"/>
																					</a></b>
																			</td>
																			<td align="right" bgcolor="#f4f4f2" width="75">
																					<input class="buton" type="button" name="addIndicator" 
																					value="Indicator" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																			</td>
																			<td align="left" width="50" bgcolor="#f4f4f2">
																					<bean:define id="translation">
																							<digi:trn key="aim:clickToEditProgram">Click here to Edit Program</digi:trn>
																					</bean:define>
																					[ <digi:link href="/editTheme.do?event=edit" name="urlParams" title="<%=translation%>" >
																							<digi:trn key="aim:programManagerEdit">Edit</digi:trn>
																					</digi:link> ]						
																			</td>
																			<td align="left" width="12" bgcolor="#f4f4f2">
																					<bean:define id="translation">
																							<digi:trn key="aim:clickToDeleteProgram">
																									Click here to Delete Program
																							</digi:trn>
																					</bean:define>
																					<digi:link href="/editTheme.do?event=delete" name="urlParams" title="<%=translation%>" onclick="return deleteProgram()">
																							<img src= "../ampTemplate/images/trash_12.gif" border=0>
																					</digi:link>
																			</td>
																		</tr>
																	</logic:iterate>
																</table>
															</td></tr>
														</logic:notEmpty>
														<logic:empty name="aimThemeForm" property="subPrograms">
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
