<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

function validate(field) {
	if (field == 1) { // validate location
		if (document.aimEditActivityForm.selLocs.checked != null) {
			if (document.aimEditActivityForm.selLocs.checked == false) {
				alert("Please choose a location to remove");
				return false;
			}				  
		} else {
			var length = document.aimEditActivityForm.selLocs.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selLocs[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a location to remove");
				return false;					  
			}				  
		}
		return true;
	} else { // validate sector
		if (document.aimEditActivityForm.selActivitySectors.checked != null) {
			if (document.aimEditActivityForm.selActivitySectors.checked == false) {
				alert("Please choose a sector to remove");
				return false;
			}				  
		} else {
			var length = document.aimEditActivityForm.selActivitySectors.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selActivitySectors[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a sector to remove");
				return false;					  
			}				  
		}
		return true;			  
	}
}

function selectLocation() {
		openNewWindow(600, 500);
		<digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selectLoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
} 	

function addSectors() {
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/module/moduleinstance/selectSectors.do?edit=true" />
	  	document.aimEditActivityForm.action = "<%= addSector %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
} 	

function removeSelSectors() {
		  var flag = validate(2);
		  if (flag == false) return false;
		   <digi:context name="remSec" property="context/module/moduleinstance/removeSelSectors.do?edit=true" />
			document.aimEditActivityForm.action = "<%= remSec %>";
		  	document.aimEditActivityForm.target = "_self"
			document.aimEditActivityForm.submit();
			return true;
} 	

function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function removeSelLocations() {
		  var flag = validate(1);
		  if (flag == false) return false;
		   <digi:context name="remLocs" property="context/module/moduleinstance/removeSelLocations.do?edit=true" />
			document.aimEditActivityForm.action = "<%= remLocs %>";
		  	document.aimEditActivityForm.target = "_self"
			document.aimEditActivityForm.submit();
			return true;
} 	

function validateForm() {
	if (document.aimEditActivityForm.selActivitySectors == null) {
		alert("Please add sectors");
		document.aimEditActivityForm.addSec.focus();
		return false;
	}
	document.aimEditActivityForm.step.value="3";
	return true;
}

function popupwin()
{
	var wndWidth = window.screen.availWidth/2.5;
	var wndHeight = window.screen.availHeight/2.5;
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;
	winpopup=window.open('',"popup","height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,status=no,toolbar=no");
	winpopup.document.write('<html>\n<head>\n');
	winpopup.document.write('<title>About : Sector</title>\n');
	winpopup.document.write('</head>\n');
	winpopup.document.write('<body bgcolor="#f4f4f2">\n');
	winpopup.document.write('<font face="verdana" size=1>\n');
	winpopup.document.write('The OECD/DAC Creditor Reporting System(CRS) codes are used by all 23 OECD/DAC members when they report on their aid activities to the DAC Secretariat. The complete list of CRS codes and definitions and principles can be found in Annex 3.<ul><li>In the CRS,data on the sector of destination are recorded using 5-digit purpose codes. The first three digits of the code refer to the main sector or category (i.e.112 for Basic education, or 210 for Transpost and storage). The last two digits of the CRS purpose code allow providing more detailed classification(i.e. 11240 for Early childhood education, or 21020 for Rail transport).</li><li>For the purpose of AMP, if the 5-digits codificaton is too detailed and not relevant, only 3-digits codes may be used.</li><li>One and only one purpose code should be applied to each project. In case of multi-sector projects, use the CRS codes 400xx.</li><li>Non-sector activities (i.e. general budget support, debt, emergency aid, NGOs) are covered by the CRS, under codes 500xx to 900xx.</li></ul>\n');
	winpopup.document.write('</font>\n');
	winpopup.document.write('</body>\n</html>\n');
	winpopup.document.close();	
}

-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">

	<html:hidden property="step" />
	<html:hidden property="reset" />
	<html:hidden property="country" />
	<html:hidden property="editAct" />

	<input type="hidden" name="edit" value="true">

	<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top"
		align="left">
		<tr>
			<td width="100%" vAlign="top" align="left"><!--  AMP Admin Logo --> <jsp:include
				page="teamPagesHeader.jsp" flush="true" /> <!-- End of Logo --></td>
		</tr>
		<tr>
			<td width="100%" vAlign="top" align="left">
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%"
				vAlign="top" align="center" border=0>
				<tr>
					<td class=r-dotted-lg width="10">&nbsp;</td>
					<td align=left vAlign=top class=r-dotted-lg>
					<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top"
						align="left">
						<tr>
							<td>
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
								<tr>
									<td><span class=crumb> <c:if
										test="${aimEditActivityForm.pageId == 0}">
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
										</bean:define>
										<digi:link href="/admin.do" styleClass="comment"
											title="<%=translation%>">
											<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp;
								</c:if> <c:if test="${aimEditActivityForm.pageId == 1}">
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
										</bean:define>
										<digi:link href="/viewMyDesktop.do" styleClass="comment"
											onclick="return quitRnot()" title="<%=translation%>">
											<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp;								
								</c:if> <bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
									</bean:define> <digi:link
										href="/addActivity.do?step=1&edit=true" styleClass="comment"
										title="<%=translation%>">
										<c:if test="${aimEditActivityForm.editAct == true}">
											<digi:trn key="aim:editActivityStep1">
										Edit Activity - Step 1
									</digi:trn>
										</c:if>
										<c:if test="${aimEditActivityForm.editAct == false}">
											<digi:trn key="aim:addActivityStep1">
										Add Activity - Step 1
									</digi:trn>
										</c:if>
									</digi:link>&nbsp;&gt;&nbsp; 
									<digi:trn key="aim:addActivityStep2">
									Step 2
								</digi:trn> </span></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td>
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
								<tr>
									<td height=16 vAlign=center width="100%"><span
										class=subtitle-blue> <c:if
										test="${aimEditActivityForm.editAct == false}">
										<digi:trn key="aim:addNewActivity">
										Add New Activity
									</digi:trn>
									</c:if> <c:if test="${aimEditActivityForm.editAct == true}">
										<digi:trn key="aim:editActivity">
										Edit Activity 
									</digi:trn>:
									<bean:write name="aimEditActivityForm" property="title"/>
									</c:if></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp; <digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT
									color=red><B><BIG>*</BIG> </B></FONT> are required.</digi:trn>
							</td>
						</tr>
						<tr>
							<td><digi:errors /></td>
						</tr>

						<tr>
							<td>
							<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
								<tr>
									<td width="75%" vAlign="top">
									<table cellPadding=0 cellSpacing=0 width="100%">
										<tr>
											<td width="100%">
											<table cellPadding=0 cellSpacing=0 width="100%" border=0>
												<tr>
													<td width="13" height="20"
														background="module/aim/images/left-side.gif"></td>
													<td vAlign="center" align="center" class="textalb"
														height="20" bgcolor="#006699">
														<digi:trn key="aim:step2of9LocationAndSectors">
													Step 2 of 9: Location | Sectors
												</digi:trn></td>
													<td width="13" height="20"
														background="module/aim/images/right-side.gif"></td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td width="100%" bgcolor="#f4f4f2">
											<table width="100%" cellSpacing="1" cellPadding="3"
												vAlign="top" align="left" bgcolor="#006699">
												<tr>
													<td bgColor=#f4f4f2 align="center" vAlign="top"><!-- contents -->

													<table width="95%" bgcolor="#f4f4f2">

														<tr>
															<td><IMG alt=Link height=10
																src="../ampTemplate/images/arrow-014E86.gif" width=15> <b>
																<digi:trn key="aim:location">Location</digi:trn></b></td>
														</tr>
														<tr>
															<td><digi:trn key="aim:chooseLocation">
										Choose the area covered by the project.</digi:trn></td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td vAlign="center">

															<table border=0>
																<tr>
																	<td><a
																		title="<digi:trn key="aim:impleLevel">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
																	<digi:trn key="aim:implementLevel">Implementation Level</digi:trn></a>&nbsp;
																	</td>
																	<td>
																		<html:select property="level" styleClass="inp-text">
																			<html:option value="-1">Select Level</html:option>
																			<logic:iterate name="aimEditActivityForm" property="levelCollection" id="ampLevelId" >
																				<option value="<%=ampLevelId%>"> 
																		 		 <bean:define id="ampLevelName" name="ampLevelId" property="name" />
																				 <digi:trn key="<%= "aim:" + ampLevelName%>">
																					<bean:write name="ampLevelId" property="name"/>
																				 </digi:trn>
																				</option>
																			</logic:iterate>
																			
																	</html:select></td>
																</tr>
																<tr>
																	<td vAlign="center" colspan=5><b></b> Select the
																	appropriate Region, Zone or Woreda as needed.</td>
																</tr>
																<tr>
																	<td vAlign="center"><a
																		title="<digi:trn key="aim:impLocation">The regions, zones and woredas in which the project is implemented</digi:trn>">
																	<digi:trn key="aim:implementationLoc">Implementation Location </digi:trn></a>&nbsp;
																	</td>
																	<td vAlign="center"><br>
																	<html:select property="implementationLevel"
																		styleClass="inp-text">
																		<html:option value="country"><digi:trn key="aim:CountryStep2">Country</digi:trn></html:option>
																		<html:option value="region"><digi:trn key="aim:RegionStep2">Region</digi:trn></html:option>
																		<html:option value="zone"><digi:trn key="aim:ZoneStep2">Zone</digi:trn></html:option>
																		<html:option value="woreda"><digi:trn key="aim:DistrictStep2">District </digi:trn></html:option>
																	</html:select></td>
																</tr>
															</table>
															</td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td>

															<table cellPadding=5 cellSpacing=1 border=0 width="100%"
																bgcolor="#d7eafd">
																<tr>
																	<td align="left"><b><digi:trn key="aim:location">Location</digi:trn></b>
																	</td>
																</tr>
																<tr>
																	<td bgcolor="#ffffff" width="100%">
																	<table cellPadding=1 cellSpacing=1 border=0
																		bgcolor="#ffffff" width="100%">
																		<logic:empty name="aimEditActivityForm"
																			property="selectedLocs">
																			<tr>
																				<td bgcolor="#ffffff">
																					<html:button  styleClass="buton" property="submitButton" onclick="selectLocation()">
																						<digi:trn key="btn:addLocation">Add Location</digi:trn> 
																					</html:button>		
																				</td>
																			</tr>
																		</logic:empty>
																		<logic:notEmpty name="aimEditActivityForm"
																			property="selectedLocs">
																			<tr>
																				<td>
																				<table cellSpacing=0 cellPadding=0 border=0
																					bgcolor="#ffffff" width="100%">
																					<logic:iterate name="aimEditActivityForm"
																						property="selectedLocs" id="selectedLocs"
																						type="org.digijava.module.aim.helper.Location">
																						<tr>
																							<td width="100%">
																							<table width="100%" cellSpacing=1 cellPadding=1
																								vAlign="top" align="left">
																								<tr>
																									<td width="3" vAlign="center"><html:multibox
																										property="selLocs">
																										<bean:write name="selectedLocs"
																											property="locId" />
																									</html:multibox></td>
																									<td vAlign="center" align="left"><c:if
																										test="${!empty selectedLocs.country}">
																						[<bean:write name="selectedLocs"
																											property="country" />]
																					</c:if> <c:if test="${!empty selectedLocs.region}">
																						[<bean:write name="selectedLocs" property="region" />]
																					</c:if> <c:if test="${!empty selectedLocs.zone}">
																						[<bean:write name="selectedLocs" property="zone" />]
																					</c:if> <c:if test="${!empty selectedLocs.woreda}">
																						[<bean:write name="selectedLocs" property="woreda" />]
																					</c:if></td>
																								</tr>
																							</table>
																							</td>
																						</tr>
																					</logic:iterate>
																					<tr>
																						<td>
																						<table cellSpacing=2 cellPadding=2>
																							<tr>
																								<td>
																									<html:button  styleClass="buton" property="submitButton" onclick="selectLocation()">
																										<digi:trn key="btn:addLocation">Add Location</digi:trn> 
																									</html:button>
																								</td>
																								<td>
																									<html:button  styleClass="buton" property="submitButton" onclick="return removeSelLocations()">
																										<digi:trn key="btn:removeLocation">Remove Location</digi:trn> 
																									</html:button>
																								</td>
																							</tr>
																						</table>
																						</td>
																					</tr>
																				</table>
																				</td>
																			</tr>
																		</logic:notEmpty>
																	</table>
																	</td>
																</tr>
															</table>

															<!-- Add Location --></td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td><IMG alt=Link height=10
																src="../ampTemplate/images/arrow-014E86.gif" width=15> <b><FONT
																color=red>*</FONT> <digi:trn key="aim:sector">Sector</digi:trn></b>
															<a href="javascript:popupwin()"> <img
																src="../ampTemplate/images/help.gif"
																alt="Click to get help on Status" width=10 height=10
																border=0></a></td>
														</tr>
														<tr>
															<td><FONT color=red>*</FONT> 
															<digi:trn key="aim:chooseSector">
															Choose the sector</digi:trn></td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td>


															<table cellPadding=5 cellSpacing=1 border=0 width="100%"
																bgcolor="#d7eafd">
																<tr>
																	<td align="left"><b><digi:trn key="aim:sector">Sector</digi:trn></b>
																	</td>
																</tr>
																<tr>
																	<td bgcolor="#ffffff" width="100%">
																	<table cellPadding=1 cellSpacing=1 border=0
																		bgcolor="#ffffff" width="100%">
																		<logic:empty name="aimEditActivityForm"
																			property="activitySectors">
																			<tr>
																				<td bgcolor="#ffffff">
																					<html:button  styleClass="dr-menu" property="addSec" onclick="addSectors()">
																						<digi:trn key="btn:addSector">Add Sector</digi:trn> 
																					</html:button>
																				</td>
																			</tr>
																		</logic:empty>
																		<logic:notEmpty name="aimEditActivityForm"
																			property="activitySectors">
																			<tr>
																				<td>
																				<table cellSpacing=0 cellPadding=0 border=0
																					bgcolor="#ffffff" width="100%">
																					<logic:iterate name="aimEditActivityForm"
																						property="activitySectors" id="actSect"
																						type="org.digijava.module.aim.helper.ActivitySector">
																						<tr>
																							<td>
																							<table width="100%" cellSpacing=1 cellPadding=1
																								vAlign="top" align="left">
																								<tr>
																									<td width="3" vAlign="center"><html:multibox
																										property="selActivitySectors">
																										<bean:write name="actSect" property="id" />
																									</html:multibox></td>
																									<td vAlign="center" align="left"><c:if
																										test="${!empty actSect.sectorName}">
																							[<bean:write name="actSect" property="sectorName" />]
																						</c:if> <c:if
																										test="${!empty actSect.subsectorLevel1Name}">
																							[<bean:write name="actSect"
																											property="subsectorLevel1Name" />]
																						</c:if> <c:if
																										test="${!empty actSect.subsectorLevel2Name}">
																							[<bean:write name="actSect"
																											property="subsectorLevel2Name" />]
																						</c:if></td>
																								</tr>
																							</table>
																							</td>
																						</tr>
																					</logic:iterate>
																					<tr>
																						<td>
																						<table cellSpacing=2 cellPadding=2>
																							<tr>
																								<%--
																			<td>
																				<input type="button" value="Add Sectors" class="buton" 
																				onclick="addSectors()">
																			</td>
																			--%>
																								<td>
																									<html:button  styleClass="buton" property="submitButton" onclick="return removeSelSectors()">
																										<digi:trn key="btn:removeSector">Remove Sector</digi:trn> 
																									</html:button>
																								</td>
																							</tr>
																						</table>
																						</td>
																					</tr>
																				</table>
																				</td>
																			</tr>
																		</logic:notEmpty>
																	</table>
																	</td>
																</tr>
															</table>

															<!-- Add Sectors --></td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td><IMG alt=Link height=10
																src="../ampTemplate/images/arrow-014E86.gif" width=15> <a
																title="<digi:trn key="aim:ProgramImp">Set of policies, projects and strategies grouped by area</digi:trn>">
															<b><digi:trn key="aim:program">Program</digi:trn></b> </a>
															</td>
														</tr>
														<tr>
															<td><digi:trn key="aim:selectProgram">
										Select the program from the list.</digi:trn></td>
														</tr>
														<tr>
															<td><html:select property="program" styleClass="inp-text">
																<html:option value="-1">--- Select program ---</html:option>
																<html:optionsCollection name="aimEditActivityForm"
																	property="programCollection" value="ampThemeId"
																	label="name" />
															</html:select></td>
														</tr>
														<tr>
															<td><a
																title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
															Description </a></td>
														</tr>
														<tr>
															<td><a
																title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
															<html:textarea property="programDescription" rows="3"
																cols="75" styleClass="inp-text" /> </a></td>
														</tr>
														<tr>
															<td>&nbsp;</td>
														</tr>
														<tr>
															<td bgColor=#f4f4f2 align="center">
															<table cellPadding=3>
																<tr>
																	<td>
																	<html:submit  styleClass="dr-menu" property="submitButton" onclick="return gotoStep(1)">
																		<digi:trn key="btn:back">Back</digi:trn> >> 
																	</html:submit>	
																	</td>
																	<td>
																		<html:submit  styleClass="dr-menu" property="submitButton" onclick="return validateForm()">
																			<digi:trn key="btn:next">Next</digi:trn> >>
																		</html:submit>
																	</td>
																	<td>
																		<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">
																			<digi:trn key="btn:reset">Reset</digi:trn> 
																		</html:reset>
																	</td>
																</tr>
															</table>
															</td>
														</tr>
													</table>

													<!-- end contents --></td>
												</tr>
											</table>
											</td>
										</tr>
									</table>
									</td>
									<td width="25%" vAlign="top" align="right"><!-- edit activity form menu -->
									<jsp:include page="editActivityMenu.jsp" flush="true" /> <!-- end of activity form menu -->
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
					<td width="10">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</digi:form>
