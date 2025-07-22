<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
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
			if (trim(document.aimThemeForm.programType.value).length == 0)
			{
				alert("Please enter Program type");
				document.aimThemeForm.programType.focus();
				return false;
			}
			return true;
		}
		function addSubProgram(rutId,id,level,name)
		{
			openNewWindow(400, 300);
			<digi:context name="subProgram" property="context/module/moduleinstance/addSubPrgInd.do?event=addSubProgram"/>
			document.aimThemeForm.action = "<%= subProgram %>&themeId=" + id + "&indlevel=" + level + "&indname=" + name + "&rootId=" + rutId;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
		}
		function editProgram(id,rutId)
		{
			openNewWindow(400,300);
			<digi:context name="editTh" property="context/module/moduleinstance/editTheme.do?event=editSub"/>
			document.aimThemeForm.action = "<%= editTh %>&themeId=" + id + "&rootId=" +rutId;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
			return true;
		}

		function saveProgram(id)
		{
			var temp = validate();
			if (temp == true)
			{
				<digi:context name="addSubThm" property="context/module/moduleinstance/addSubPrgInd.do?event=save" />
				document.aimThemeForm.action = "<%= addSubThm %>&themeId=" + id;
				document.aimThemeForm.target = "_self";
				document.aimThemeForm.submit();
			}
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
		function load(){}
		function unload(){}
	-->
</script>

<digi:errors/>
<digi:instance property="aimThemeForm" />
<digi:form action="/addSubPrgInd.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">

<%--  AMP Admin Logo --%>
<jsp:include page="teamPagesHeader.jsp"  />
<%-- End of Logo --%>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMultiLevelProgramManager">Click here to goto Multi-level Program Manager</digi:trn>
						</c:set>
						<digi:link href="/themeManager.do" styleClass="comment" title="${translation}">
						<digi:trn key="aim:multilevelprogramManager">
							Multi-Level Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:subProgram">
							Sub-Programs
						</digi:trn>
					</td>
					<%-- End navigation --%>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn key="aim:multilevelprogramManager">
							Multi-Level Program Manager
						</digi:trn>
						</span>
					</td>
				</tr>

				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1" border="0">
					<tr><td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
										<table align=left valign="top" cellpadding="1" cellspacing="1" width="100%">
											<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<table bgColor=#d7eafd cellpadding="0" cellspacing="0" width="100%" valign="top">
											<tr><td bgColor=#d7eafd height="7" align="center"><h3>
													<digi:trn key="aim:subPrograms">
															Sub-Programs
													</digi:trn> of <bean:write name="aimThemeForm" property="name"/></h3>
											</td></tr>
											<tr><td>
														<logic:notEmpty name="aimThemeForm" property="subPrograms">
														<field:display name="Sub Program Level 1" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 2" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 3" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 4" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 5" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 6" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 7" feature="NPD Dashboard"> </field:display>
														<field:display name="Sub Program Level 8" feature="NPD Dashboard"> </field:display>
														<%
								                          ServletContext x=session.getServletContext();
								                          	org.dgfoundation.amp.visibility.AmpTreeVisibility atv=org.digijava.module.aim.util.FeaturesUtil.getAmpTreeVisibility(x, session);
								                          	
								                          	int i;
								                          	for(i=1;i<=8;i++)
								                          	{
								                          		org.digijava.module.aim.dbentity.AmpFieldsVisibility field=(org.digijava.module.aim.dbentity.AmpFieldsVisibility)atv.getFieldByNameFromRoot("Sub Program Level "+i);
								                          		if(field==null) break;
								                          		else
										                        	if(!field.isFieldActive(atv)) break;
								                          	}
								                          	java.lang.String visible=new String(Integer.toString(i));
								                       	%>
															<bean:define id="visibilityLevel"><%=visible%></bean:define>
															<tr bgColor=#ffffff><td>
																<table width="100%" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																	<logic:iterate name="aimThemeForm" property="subPrograms" id="subPrograms" type="org.digijava.module.aim.dbentity.AmpTheme">
																		<%------- level 1 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="1">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="100%" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																									<td width="9" height="15" bgcolor="#f4f4f2">
																											<img src= "../ampTemplate/images/arrow_right.gif" border="0">
																									</td>
																									<td bgcolor="#ffcccc" width="50">
																											<bean:write name="subPrograms" property="themeCode"/>
																									</td>
																									<td align="left" bgcolor="#ffcccc">
																											<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																											<c:set target="${urlParams1}" property="themeId">
																													<bean:write name="subPrograms" property="ampThemeId" />
																											</c:set>
																											<c:set target="${urlParams1}" property="indname">
																													<bean:write name="subPrograms" property="name" />
																											</c:set>
																											<c:set target="${urlParams1}" property="rutId">
																													<bean:write name="aimThemeForm" property="rootId" />
																											</c:set>
																											<logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>

																									</td>
                                                                                                                                                                                                        <c:set var="indicator">
                                                                                                                                                                                                        <digi:trn key="aim:indicator">Indicator</digi:trn>
                                                                                                                                                                                                        </c:set>
                                                                                                                                                                                                        <c:set var="edit">
                                                                                                                                                                                                        <digi:trn key="aim:edit">Edit</digi:trn>
                                                                                                                                                                                                        </c:set>
																									<td align="right" bgcolor="#ffcccc" width="75">

																											<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																									</td>
																									<td align="left" width="50" bgcolor="#ffcccc">
																											<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																									</td>
																									<td align="left" width="12" bgcolor="#f4f4f2">
																											<c:set var="translation">
																													<digi:trn key="aim:clickToDeleteProgram">
																															Click here to Delete Program
																													</digi:trn>
																											</c:set>
																											<digi:link href="/addSubTheme.do?event=delete" name="urlParams1" title="${translation}" onclick="return deleteProgram()">
																													<img src= "../ampTemplate/images/trash_12.gif" border="0">
																											</digi:link>
																									</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 1 ends ------------%>
																		<%------- level 2 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="2">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="99%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square1.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams2}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams2}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams2}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set>
																												<logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams2" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 2 ends ------------%>
																		<%------- level 3 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="3">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="98%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square2.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams3}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams3}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams3}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams3" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 3 ends ------------%>
																		<%------- level 4 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="4">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="97%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square3.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams4}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams4}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams4}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams4" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 4 ends ------------%>
																		<%------- level 5 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="5">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="96%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square4.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams5}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams5}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams5}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams5" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 5 ends ------------%>
																		<%------- level 6 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="6">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="95%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square5.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams6" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams6}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams6}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams6}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams6" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 6 ends ------------%>
																		<%------- level 7 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="7">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="94%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square6.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams7" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams7}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams7}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams7}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><logic:equal name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																													<bean:write name="subPrograms" property="name"/>
																											</b>
																											</logic:equal>
																											<logic:notEqual name="subPrograms" property="indlevel" value="${visibilityLevel-1}">
																											<b>
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','${subPrograms.encodeName}')" title="Click here to add Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																											</a></b>
																											</logic:notEqual>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams7" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 7 ends ------------%>
																		<%------- level 8 starts ------------%>
																		<logic:lessThan name="subPrograms" property="indlevel" value="${visibilityLevel}">
																		<logic:equal name="subPrograms" property="indlevel" value="8">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="93%" align="right" bgColor="#d7eafd" cellPadding=3 cellspacing="1">
																								<tr bgcolor="#ffffff">
																										<td width="9" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="9" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square7.gif" border="0">
																										</td>
																										<td bgcolor="#f4f4f2" width="50">
																												<bean:write name="subPrograms" property="themeCode"/>
																										</td>
																										<td align="left" bgcolor="#f4f4f2">
																												<jsp:useBean id="urlParams8" type="java.util.Map" class="java.util.HashMap"/>
																												<c:set target="${urlParams8}" property="themeId">
																														<bean:write name="subPrograms" property="ampThemeId" />
																												</c:set>
																												<c:set target="${urlParams8}" property="indname">
																														<bean:write name="subPrograms" property="name" />
																												</c:set>
																												<c:set target="${urlParams8}" property="rutId">
																														<bean:write name="aimThemeForm" property="rootId" />
																												</c:set><b>
																													<bean:write name="subPrograms" property="name"/></b>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="75">
																												<input class="dr-menu" type="button" name="addIndicator" value="${indicator}" onclick="assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />')">
																										</td>
																										<td align="left" width="50" bgcolor="#f4f4f2">
																												<input class="dr-menu" type="button" name="editButton" value="${edit}" onclick="editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','<bean:write name="aimThemeForm" property="rootId"/>')">
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<c:set var="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</c:set>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams8" title="${translation}" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border="0">
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																		</logic:equal>
																		</logic:lessThan>
																		<%------- level 8 ends ------------%>
																	</logic:iterate>
																</table>
															</td></tr>
														</logic:notEmpty>
														<logic:empty name="aimThemeForm" property="subPrograms">
																<tr align="center"  height="25" bgcolor="#ffffff">
																<td><b>
																		<digi:trn key="aim:noProgramsPresent">No Programs present</digi:trn></b>
																</td>
																</tr>
																
														</logic:empty>
														<tr>
															<td bgColor=#d7eafd height="20" align="center">
															
																	<input class="dr-menu" type="button" name="addSubTheme" value="<digi:trn key='aim:AddSubProgramLevel1'>Add SubProgram Level 1</digi:trn>" onclick=" return addSubProgram('<bean:write name="aimThemeForm" property="rootId"/>','<bean:write name="aimThemeForm" property="rootId"/>','0','${aimThemeForm.encodeName}')">
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
			<tr>
						<td class=r-dotted-lg width="20%" nowrap="nowrap">
								<img src= "../ampTemplate/images/arrow_right.gif" border="0">  Level 1,
								<img src= "../ampTemplate/images/square1.gif" border="0">  Level 2,
								<img src= "../ampTemplate/images/square2.gif" border="0">  Level 3,
								<img src= "../ampTemplate/images/square3.gif" border="0">  Level 4,
								<img src= "../ampTemplate/images/square4.gif" border="0">  Level 5,
								<img src= "../ampTemplate/images/square5.gif" border="0">  Level 6,
								<img src= "../ampTemplate/images/square6.gif" border="0">  Level 7,
								<img src= "../ampTemplate/images/square7.gif" border="0">  Level 8.
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
