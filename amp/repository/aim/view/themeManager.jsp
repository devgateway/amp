<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

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
				document.aimThemeForm.target = "context/module/moduleinstance/addNewTheme.do";
				document.aimThemeForm.submit();
			}
			return true;
		}

		function addProgram()
		{
			openNewWindow(400,300);
			<digi:context name="addNewTh" property="context/module/moduleinstance/addNewTheme.do"/>
			document.aimThemeForm.action = "<%=addNewTh%>";
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();
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

		
		function editProgram(id)
		{
			openNewWindow(400,300);
			<digi:context name="editTh" property="context/module/moduleinstance/editTheme.do?event=edit"/>
			document.aimThemeForm.action = "<%= editTh %>&themeId=" + id;
			document.aimThemeForm.target = popupPointer.name;
			document.aimThemeForm.submit();

		}
		function assignIndicators(id,name)
		{

			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do"/>
			document.aimThemeForm.action = "<%= indAssign %>?resetIndicatorId=true&themeId=" + id + "&themeName="+name;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();

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
       
  var Open = ""
  var Closed = ""

  function preload(){

    if(document.images){

      Open = new Image(9,9)

      Closed = new Image(6,6)

      Open.src = "../ampTemplate/images/arrow_down.gif"

      Closed.src = "../ampTemplate/images/arrow_right.gif"

    }

  }
  function showhide(what,what2){

    if (what.style.display=='none'){

      what.style.display='';

      what2.src=Open.src

    }
    else{

      what.style.display='none'

      what2.src=Closed.src

    }

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

	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772 border="1">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750 border="0">
			<table cellPadding=5 cellSpacing=0 width="100%" border="0">
				<tr><%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:NationalPlanManager">
							National Plan Manager
						</digi:trn>
					</td>
				</tr><%-- End navigation --%>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:NationalPlanManager">
							National Plan Manager
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
					<body onload="preload()">
					<table width="100%" cellspacing=0 cellSpacing=0 border="0">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left>
										<!-- AMP-1655
										<tr>
											<td>
											 	<table cellspacing=0 cellpadding=0 border="1" height="20">
														<tr>

															<td noWrap height=17>
																<bean:define id="translation">
																	<digi:trn key="aim:viewMultiProgramIndicators" >Click here to view Multi Program Indicators</digi:trn>
																</bean:define>
																<digi:link href="/themeManager.do?view=multiprogram"  styleClass="sub-navGov" title="<%=translation%>" ><font color="ffffff">
															<digi:trn key="aim:multiProgramManager">
																Strategy/Plan Manager
															</digi:trn></font>
																</digi:link>
															</td>
                                                            <td>
                                                              <img id="img2" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-right1.gif" width="20" height="19" />
                                                            </td>

															<td noWrap height=17>
																<bean:define id="translation">
																	<digi:trn key="aim:viewAllIndicators" >Click here to view Indicators</digi:trn>
																</bean:define>
																<digi:link href="/viewIndicators.do"  styleClass="sub-navGovSelected" title="<%=translation%>" onmouseover="setOverImg(3)" onmouseout="setOutImg(3)"><font color="ffffff">
																<digi:trn key="aim:ViewIndicatorManager">
																		Indicator Manager
																</digi:trn></font>
																</digi:link>
															</td>
                                                            <td>
                                                              <img id="img3" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif" width="20" height="19" />
                                                            </td>

														</tr>
													</table>
												</tr>
												-->
				<tr>
					<td noWrap width=100% vAlign="top">

					<table width="100%" cellspacing=1 cellSpacing=1 border="0" class="r-dotted-lg">
					<tr><td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
										<table align=left valign=top cellPadding=1 cellSpacing=1 width="100%">
												<tr><td>
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
											
													<table width="100%" cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff" border="0">
														<logic:notEmpty name="aimThemeForm" property="themes">
															<tr><td>
																<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1 border="0">
																	<logic:iterate name="aimThemeForm" property="themes" id="themes" type="org.digijava.module.aim.dbentity.AmpTheme">
																		<tr bgcolor="#ffffff">
																		
																	<td width="9" height="15" bgcolor="#f4f4f2" id="menu1" onClick="showhide(menu1outline${themes.ampThemeId},menu1sign${themes.ampThemeId})" >
																		<img id="menu1sign${themes.ampThemeId}" src= "../ampTemplate/images/arrow_right.gif" valign="bottom">
																	</td>		
																			
																			<td bgcolor="#f4f4f2" width="50">
																			${themes.themeCode}
																			</td>
																			<td align="left" bgcolor="#f4f4f2" width="60%" nowrap="nowrap">
																					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="themeId">
																							<bean:write name="themes" property="ampThemeId" />
																					</c:set><b>
																					<a href="javascript:editProgram('<bean:write name="themes" property="ampThemeId"/>')" title="Edit Program">
																						${themes.name}
																					</a> &nbsp;&nbsp;&nbsp;
																					</b>
																			</td>
																			
																			<td align="left" width="10%" bgcolor="#f4f4f2" nowrap="nowrap">
	                                                    			<a href="javascript:addSubProgram('${themes.ampThemeId}','${themes.ampThemeId}','0','<bean:write name="aimThemeForm" property="name"/>')" title="Click here to Edit Sub-Programs">
	                                                    				<digi:trn key="aim:addSubProgram">
																			<b>Add sub program</b>
	                                                    				</digi:trn>
																	</a>
																</td>
																			
																			<td align="right" bgcolor="#f4f4f2" width="10%" nowrap="nowrap">

																					<a href="javascript:assignIndicators('<bean:write name="themes" property="ampThemeId" />','<bean:write name="themes" property="name" />' )" title="Add/Edit indicators">
																						<digi:trn key="aim:ThemeManager:manageIndicators">
																									Manage Indicators
																						</digi:trn>
																					</a>
																					&nbsp;|
																			</td>

																			<td align="left" width="12" bgcolor="#f4f4f2">
																					<bean:define id="translation">
																							<digi:trn key="aim:clickToDeleteProgram">
																								 Click here to Delete Program
																							</digi:trn>
																					</bean:define>
																					<digi:link href="/themeManager.do?event=delete" name="urlParams" title="<%=translation%>" onclick="return deleteProgram()">
																							<img src= "../ampTemplate/images/trash_12.gif" border=0>
																					</digi:link>
	
																			</td>
																		</tr>
														<td width="25" height="15" bgcolor="#f4f4f2" align="Center" colspan="7" id="menu1outline${themes.ampThemeId}" style="display:none">
														 <table width="100%" border="0">
																
																<logic:iterate name="aimThemeForm" property="subPrograms" id="subPrograms" type="org.digijava.module.aim.dbentity.AmpTheme">
																	<logic:equal name="subPrograms" property="indlevel" value="1">
																				 <c:if test="${subPrograms.parentThemeId.ampThemeId == themes.ampThemeId }">
																						<tr bgcolor="#ffffff">
																							<td height="15" colspan="6">
																								<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1 border="0">
																								<tr bgcolor="#ffffff">
																								
																									<td width="9" height="15" bgcolor="#f4f4f2" id="menu2" onClick="showhide(menu${subPrograms.ampThemeId}outline${themes.ampThemeId},menu${subPrograms.ampThemeId}sign${themes.ampThemeId})" >
																									<img id="menu${subPrograms.ampThemeId}sign${themes.ampThemeId}" src= "../ampTemplate/images/arrow_right.gif" valign="bottom">
																								</td>		
																											<td bgcolor="#ffcccc" nowrap="nowrap">
																									<b>
																									<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																									   	 ${subPrograms.name}
																									   	 
																									</a>
																									</b>
																									</td>
																									<td bgcolor="#ffcccc" nowrap="nowrap"  width="50%" align="left">
																										(<bean:write name="subPrograms" property="themeCode"/>)
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
																													${themes.ampThemeId}
																											</c:set>
																									</td>
																									<td align="left" bgcolor="#ffcccc" nowrap="nowrap" width="10%" >
																											<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
                                                                                                                  <digi:trn key="aim:addSubProgram">
                                                                                                                        Add Sub Program
                                                                                                                  </digi:trn>
																											</a>
																										&nbsp;|
																									</td>
																									<td align="left" bgcolor="#ffcccc" nowrap="nowrap" width="10%">
																										<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																										</a>
																									</td>
																									<td align="left" width="12" bgcolor="#f4f4f2">
																											<bean:define id="translation">
																													<digi:trn key="aim:clickToDeleteProgram">
																															Click here to Delete Program
																													</digi:trn>
																											</bean:define>
																											<digi:link href="/addSubTheme.do?event=delete" name="urlParams1" title="<%=translation%>" onclick="return deleteProgram()">
																													<img src= "../ampTemplate/images/trash_12.gif" border=0>
																											</digi:link>
																									`	</td>
																								</tr>
																						</table>
																						</td>
																					</tr>
																				</c:if>
																	</logic:equal>
																	<%------- level 1 ends ------------%>
																	<%------- level 2 starts ------------%>
																	<td width="25" height="15" bgcolor="#f4f4f2" align="Center" colspan="7" id="menu${subPrograms.ampThemeId}outline${themes.ampThemeId}" style="display:none">
																		<table width="100%" border="0">
																			<logic:iterate name="aimThemeForm" property="subPrograms" id="subPrograms" type="org.digijava.module.aim.dbentity.AmpTheme">
															 	  	<logic:equal name="subPrograms" property="indlevel" value="2">
																 	  	<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams1.themeId}">
																		  <tr bgcolor="#ffffff">
																		 		<td height="15" colspan="6">
																							<table width="99%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1 border="0">
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square1.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2">
																										  <b>
																										<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																													<bean:write name="subPrograms" property="name" />
																										</a>
																										</b>
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																														${themes.ampThemeId}
																												</c:set>
																										</td>
																											<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																												<digi:trn key="aim:addSubProgram">
                                                                                                                         Add Sub Program
                                                                                                                </digi:trn>
																												</a>
																												&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2" nowrap="nowrap" width="10%">
																											<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Edit Sub-Programs">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																											</a>
																										</b>
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams2" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																									</tr>
																							</table>
																						</td>
																					</tr>
																				</c:if>
																		</logic:equal>
																
																		<%------- level 2 ends ------------%>
																		<%------- level 3 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="3">
																			<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams2.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="98%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square2.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2" >
																										   <b>
																										   <a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																											<bean:write name="subPrograms" property="name"/>
																											</a>
																							
																										  </b>
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																														${themes.ampThemeId}
																												</c:set>
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																												<digi:trn key="aim:addSubProgram">
                                                                                                                       Add Sub Program
                                                                                                                </digi:trn>
																												</a>
																										&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																											<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																											</a>
																										</td>
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams3" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																				</c:if>
																		</logic:equal>
																		<%------- level 3 ends ------------%>
																		<%------- level 4 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="4">
																		<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams3.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="97%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2" >
																												<img src= "../ampTemplate/images/square3.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2" >
																										<b>
																										<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																														<bean:write name="subPrograms" property="name"/>
																										</a>
																										</b>
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																														${themes.ampThemeId}
																												</c:set>
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																												<digi:trn key="aim:addSubProgram">
                                                                                                                Add Sub Program
                                                                                                                  </digi:trn>
																												</a>
																												&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																											<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																											</a>

																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams4" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																			</c:if>
																		</logic:equal>
																		<%------- level 4 ends ------------%>
																		
																		<%------- level 5 starts ------------%>
																		
																		<logic:equal name="subPrograms" property="indlevel" value="5">
																		<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams4.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="96%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square4.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2">
																										<b>
																										<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																												<bean:write name="subPrograms" property="name"/>
																										</a>
																										</b>
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																														${themes.ampThemeId}
																												</c:set>
																									</td>
																										<td align="right" bgcolor="#f4f4f2" nowrap="nowrap" width="10%">
																												<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																												<digi:trn key="aim:addSubProgram">
                                                                                                                               Add Sub Program
                                                                                                                           </digi:trn>
																												</a>
																												&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																											<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																														<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																														</digi:trn>
																											</a>

																										</td>

																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams5" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																			</c:if>
																		</logic:equal>
																		<%------- level 5 ends ------------%>
																		<%------- level 6 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="6">
																			<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams5.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="95%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square5.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2">
																										<b>
																										<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<bean:write name="subPrograms" property="name"/>
																											</a>
																										
																												
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																														${themes.ampThemeId}
																												</c:set>
																										</td>
																											<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:addSubProgram('${themes.ampThemeId}','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																												<digi:trn key="aim:addSubProgram">
                                                                                                                           Add Sub Program
                                                                                                                  </digi:trn>
																												</a>
																												&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																											</a>
																										</td>
																										
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams6" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																			</c:if>
																		</logic:equal>
																		<%------- level 6 ends ------------%>
																		<%------- level 7 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="7">
																			<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams6.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="94%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square6.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2">
																										<b>
																										
																										<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																											<bean:write name="subPrograms" property="name"/>
																										</a>
																										</b>
																										</td>
																										<td bgcolor="#f4f4f2" width="35%" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																												</c:set>
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																											<a href="javascript:addSubProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="indlevel"/>','<bean:write name="subPrograms" property="name"/>')" title="Click here to add Sub-Programs">
																											<digi:trn key="aim:addSubProgram">
                                                                                                                    Add Sub Program
                                                                                                                </digi:trn>
																											</a>
																										</b>
																													&nbsp;|
																										</td>
																										<td align="right" bgcolor="#f4f4f2"  nowrap="nowrap" width="10%">
																												<a href="javascript:assignIndicators('<bean:write name="subPrograms" property="ampThemeId" />','<bean:write name="subPrograms" property="name" />')" title="Click here to Add/Edit Indicators">
																													<digi:trn key="aim:ThemeManager:manageIndicators">
																														Manage Indicators
																													</digi:trn>
																											</a>
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams7" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																				</c:if>
																		</logic:equal>
																		<%------- level 7 ends ------------%>
																		<%------- level 8 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="8">
																			<c:if test="${subPrograms.parentThemeId.ampThemeId == urlParams7.themeId}">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan="6">
																						<table width="93%" align="right" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
																								<tr bgcolor="#ffffff">
																										<td width="2%" height="15">
																												<img src="../ampTemplate/images/link_out_bot.gif">
																										</td>
																										<td width="2%" height="15" bgcolor="#f4f4f2">
																												<img src= "../ampTemplate/images/square7.gif" border=0>
																										</td>
																										<td bgcolor="#f4f4f2"   width="35%" >
																										<b>
																										<a href="javascript:editProgram('<bean:write name="subPrograms" property="ampThemeId"/>','${themes.ampThemeId}')" title="Click here to Edit Sub-Programs">
																													<bean:write name="subPrograms" property="name"/>
																										</a>
																										</b>
																										</td>
																										<td bgcolor="#f4f4f2" width="50" nowrap="nowrap">
																												(<bean:write name="subPrograms" property="themeCode"/>)
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
																												</c:set>
																										</td>
																										<td align="right" bgcolor="#f4f4f2" width="17%" nowrap="nowrap">
																												
																										</td>
																										<td align="left" width="12" bgcolor="#f4f4f2">
																												<bean:define id="translation">
																														<digi:trn key="aim:clickToDeleteProgram">
																																Click here to Delete Program
																														</digi:trn>
																												</bean:define>
																												<digi:link href="/addSubTheme.do?event=delete" name="urlParams8" title="<%=translation%>" onclick="return deleteProgram()">
																														<img src= "../ampTemplate/images/trash_12.gif" border=0>
																												</digi:link>
																										</td>
																								</tr>
																						</table>
																					</td>
																				</tr>
																				</c:if>
																		</logic:equal>
																	</logic:iterate>
																	</table>
															      </td>
																		
																		<%------- level 8 ends ------------%>
																	</logic:iterate>
																</table>
																</td>
														</logic:iterate>
													</table>
													</td>
												</tr>
											<td>
										</td>
						</logic:notEmpty>
<tr align="center" bgcolor="#ffffff">
<td>

<input class="button" type="button" name="addBtn" value="<digi:trn key="aim:addnewprogram">Add Program</digi:trn>" onclick="addProgram()" style="font-family:verdana;font-size:11px;">
														</tr>
														<tr>
					<td  width="20%" nowrap="nowrap">
					<img src= "../ampTemplate/images/arrow_right.gif" border=0>  Level 1,
					<img src= "../ampTemplate/images/square1.gif" border=0>  Level 2,
					<img src= "../ampTemplate/images/square2.gif" border=0>  Level 3,
					<img src= "../ampTemplate/images/square3.gif" border=0>  Level 4,
					<img src= "../ampTemplate/images/square4.gif" border=0>  Level 5,
					<img src= "../ampTemplate/images/square5.gif" border=0>  Level 6,
					<img src= "../ampTemplate/images/square6.gif" border=0>  Level 7,
					<img src= "../ampTemplate/images/square7.gif" border=0>  Level 8.
					</td>
		</tr>
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
</body>
</td></tr></table></td></tr></table>
</digi:form>