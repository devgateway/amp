<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import = "org.digijava.module.aim.helper.TreeItem" %>
<%@ page import = "org.digijava.module.aim.util.ProgramUtil" %>
<%@ page import = "java.util.Collection" %>
<%@page import="java.util.Iterator"%>
<digi:instance property="aimThemeForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="themeManagerPopin.jsp" flush="true"/>
<script language="JavaScript">

	<!--


		
	
		function assignIndicators(id)
		{

			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do"/>
			document.aimThemeForm.action = "<%= indAssign %>?resetIndicatorId=true&themeId=" + id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();

		}
		function deleteProgram()
		{
			return confirm("Are you sure you want to delete this Program and its Sub-Program(s)?");
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
 
 	function expandProgram(progId){
		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}       
	
	function collapseProgram(progId){
		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}       
       
	function expandOrCollapseAll(){     
        $("img[@id^='img_']").toggle();
		$("img[@id^='imgh_']").toggle();	
		$("div[@id^='div_theme_']").toggle();
    }
     
      
        
	-->
</script>

<style type="text/css">
<!--
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
	font-size:8pt;
	padding:2px;
}
-->
</style>
<digi:errors/>

<digi:form action="/themeManager.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">

<%--  AMP Admin Logo--%>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<%-- End of Logo--%>

	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772 border="0">
	<tr>
		<td align=left vAlign=top width=750 border="0">
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
						<digi:trn key="aim:multiprogramanmanager">
							Multi Program Manager
						</digi:trn>
					</td>
				</tr><%-- End navigation --%>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:multiprogramanmanager">
							Multi Program Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
			
				<tr>
					<td noWrap width=100% vAlign="top">
					
					<table width="100%" cellspacing=0 cellSpacing=0 border="0">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left>
										<!-- AMP-1655 -->
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border="0">
					<tr><td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
										<table align=left valign=top cellPadding=1 cellSpacing=1 width="100%">
												<tr><td>
													<digi:errors/>
												</td></tr>
												<tr><td bgColor=#cccccc class=box-title height="20" align="center">
														<digi:trn key="aim:listofPrograms">
																List of Programs
														</digi:trn>
												</td></tr>
												<c:if test="${aimThemeForm.flag == 'activityReferences'}">
																	<tr>
																		<td colspan="2" align="center">
																			<font color="red"><b><digi:trn key="aim:cannotDeleteThemeMsg1">
																			Cannot delete the theme since some
																			activities references it or its sub program: 
																			</digi:trn>
																				${aimThemeForm.activitiesUsingTheme }
																				</b>
																			</font>
																		</td>
																	</tr>
												</c:if>
												<c:if test="${aimThemeForm.flag == 'settingUsedInActivity'}">
																	<tr>
																		<td colspan="2" align="center">
																			<font color="red"><b><digi:trn key="aim:cannotDeleteThemeDefHierarchy">
																			Cannot delete the theme since it is used as a default hierarchy in: 
																			</digi:trn>
																				${aimThemeForm.settingsUsedByTheme }
																				</b>
																			</font>
																		</td>
																	</tr>
												</c:if>
												<c:if test="${aimThemeForm.flag == 'indicatorsNotEmpty'}">

																	<tr>

																		<td colspan="2" align="center">

																			<font color="red"><b><digi:trn key="aim:cannotDeleteThemeMsg2">

																			Cannot delete this program, one or more indicators are attached to it or its sub program.

																			Delete the indicator(s) before deleting the program.

																			</digi:trn></b></font>

																		</td>
																	</tr>
												</c:if>
															<c:if test="${aimThemeForm.flag == 'error'}">
																<tr>
																		<td colspan="2" align="center">
																<font color="red"><b><digi:trn key="aim:cannotadd">
																		program with this name already exists
																</digi:trn></b></font>
															</c:if>
														</td>
													</tr>
											<tr>
												<td>
												<div  style="overflow:auto;width:100%;height:220px;max-height:220px;"  >
												<!-- AMP-2204 -->
														<bean:define id="firstLevel" name="aimThemeForm" property="themes" type="java.util.Collection"/>
														<%= ProgramUtil.renderLevel(firstLevel,0,request) %>
												</div>
												</td>											
											
											<tr align="center" bgcolor="#ffffff">
												<td>
													<input class="button" type="button" name="addBtn" value="<digi:trn key="aim:addProgramMPM">Add New Program</digi:trn>" onclick="addProgram()" style="font-family:verdana;font-size:11px;">
													<input class="button" type="button" name="expandBtn" value="<digi:trn>Expand/Collapse All</digi:trn>" onclick="expandOrCollapseAll()" style="font-family:verdana;font-size:11px;">
												</td>
											</tr>
											<tr>
											
												<td  width="20%" nowrap="nowrap"> <digi:trn key="aim:subprogramleves">Sub Program leves</digi:trn> :
												<img src= "../ampTemplate/images/arrow_right.gif" border=0><digi:trn key="aim:subproglevel_1">  Level 1</digi:trn>,
												<img src= "../ampTemplate/images/square1.gif" border=0><digi:trn key="aim:subproglevel_2">  Level 2</digi:trn>,
												<img src= "../ampTemplate/images/square2.gif" border=0><digi:trn key="aim:subproglevel_3">  Level 3</digi:trn>,
												<img src= "../ampTemplate/images/square3.gif" border=0><digi:trn key="aim:subproglevel_4">  Level 4</digi:trn>,
												<img src= "../ampTemplate/images/square4.gif" border=0><digi:trn key="aim:subproglevel_5">  Level 5</digi:trn>,
												<img src= "../ampTemplate/images/square5.gif" border=0><digi:trn key="aim:subproglevel_6">  Level 6</digi:trn>,
												<img src= "../ampTemplate/images/square6.gif" border=0><digi:trn key="aim:subproglevel_7">  Level 7</digi:trn>,
												<img src= "../ampTemplate/images/square7.gif" border=0><digi:trn key="aim:subproglevel_8">  Level 8</digi:trn>.
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