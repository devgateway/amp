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

<script language="JavaScript">
	<!--
		function editPrgIndicator(id)
		{
			openNewWindow(650, 500);
			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do?event=overall"/>
			document.aimAllIndicatorForm.action = "<%= indAssign %>&themeId=" + id;
			document.aimAllIndicatorForm.target = popupPointer.name;
			document.aimAllIndicatorForm.submit();
			return true;
		}
		function editProjIndicator(projId)
		{
			openNewWindow(500, 300);
			<digi:context name="editIndicator" property="context/module/moduleinstance/editAllIndicator.do?indicator=project"/>
			document.aimAllIndicatorForm.action = "<%= editIndicator %>&indicatorId=" +projId;
			document.aimAllIndicatorForm.target = popupPointer.name;
			document.aimAllIndicatorForm.submit();
		}
		function assignIndicatorTo(indId)
		{
			openNewWindow(850, 150);
			<digi:context name="assignIndicator" property="context/module/moduleinstance/editAllIndicator.do?indicator=assign"/>
			document.aimAllIndicatorForm.action = "<%= assignIndicator %>&indicatorId=" +indId;
			document.aimAllIndicatorForm.target = popupPointer.name;
			document.aimAllIndicatorForm.submit();
		}
		function deletePrgIndicator()
	  	{  
			<c:set var="translation"> 
				<digi:trn key="aim:doYouWantToDelIndicatorCheckProgramFirst">Do you want to delete the Indicator ? Please check whether the indicator is being used by some Program.</digi:trn>
			</c:set>
			return confirm("${translation}");
		}
		function deleteProjIndicator()
	  	{
			<c:set var="translation">
				<digi:trn key="aim:doYouWantToDelIndicatorCheckActivityFirst">Do you want to delete the Indicator?</digi:trn>
			</c:set>
			return confirm("${translation}");
		}
		function load(){}
		function unload(){}
		
		function showIndicators(prgId){
			<digi:context name="showIndicator" property="context/module/moduleinstance/overallIndicatorManager.do?indicatorFlag=true"/>
			document.aimAllIndicatorForm.action = "<%= showIndicator%>&flagShow=true"+"&prgId="+prgId;
			document.aimAllIndicatorForm.submit();
		}
		
		function hideIndicators(){
			<digi:context name="hideIndicator" property="context/module/moduleinstance/overallIndicatorManager.do?indicatorFlag=false"/>
			document.aimAllIndicatorForm.action = "<%= hideIndicator%>";
			document.aimAllIndicatorForm.submit();
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
<digi:instance property="aimAllIndicatorForm" />
<digi:form action="/overallIndicatorManager.do" method="post">
<digi:context name="digiContext" property="context" />
<input type="hidden" name="indicator">
<%--  AMP Admin Logo --%>
<jsp:include page="teamPagesHeader.jsp"  />

	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg valign="top" width=750>
				<table cellPadding=5 cellspacing="0" width="100%" border="0">
					<tr><%-- Start Navigation --%>
						<td height=33><span class=crumb>
						<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
								 <digi:link href="/admin.do" styleClass="comment"	title="${clickToViewAdmin}">
									<digi:trn key="aim:AmpAdminHome">
										Admin Home
									</digi:trn>
										</digi:link>&nbsp;&gt;&nbsp; 
										<digi:trn key="aim:ProgramIndicatorManager">
                  							Program Indicator Manager
                 					 </digi:trn>
						</td>
				</tr><%-- End navigation --%>
					<tr>
						<td height=16 valign="center" width=571>
							<span class=subtitle-blue> 
						<digi:trn key="aim:ProgramIndicatorManager">
                  					Program Indicator Manager
                  				</digi:trn> 
                  			</span>
						</td>
					</tr>
					<tr>
						<td height=16 valign="center" width=571>
							<digi:errors />
						</td>
					</tr>
					<tr>
						<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="0" cellspacing="0" border="0">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellpadding="0" cellspacing="0" width="100%" valign="top">
											<tr bgColor=#ffffff>
												<td vAlign="top" width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" valign="top"	align=left>
														<tr><td>
																<table cellspacing="0" cellpadding="0">
																	<tr>
																		<td noWrap height=17>
																		<c:set var="viewIndicators">
																		<digi:trn key="aim:viewIndicators">Click here to View Indicators</digi:trn>
																		</c:set>
																		<digi:link
																				href="/overallIndicatorManager.do?view=indicators"
																				styleClass="sub-navGov" title="${viewIndicators}">
																				<font color="ffffff"> <digi:trn
																						key="aim:programIndicatorList">
																		Program Indicator List
																</digi:trn> </font>
																			</digi:link>
																		</td>
																		<td>
																			<img id="img1" alt=""
																				src="/TEMPLATE/ampTemplate/module/aim/images/tab-right1.gif"
																				width="20" height="19" />
																		</td>
																		<feature:display name="NPD Dashboard" module="National Planning Dashboard">
																		<td noWrap height=17>
																		<c:set var="ProgramIndicators">
																				<digi:trn key="aim:viewMultiProgramIndicators">Click here to view Multi Program Indicators</digi:trn>
																	
																	     </c:set>
																			<digi:link
																				href="/overallIndicatorManager.do?view=multiprogram"
																				styleClass="sub-navGovSelected"
																				title="${ProgramIndicators}" onmouseover="setOverImg(2)"
																				onmouseout="setOutImg(2)">
																				<font color="ffffff"> <digi:trn key="aim:multiProgramManager">
																					Multi Program Manager
																				</digi:trn> </font>
																			</digi:link>
																		</td>
																		</feature:display>
																		<td>
																			<img id="img2" alt=""
																				src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
																				width="20" height="19" />
																		</td>
																		<td noWrap height=17>
																		
																		<c:set var="ProjectIndicators">
																		<digi:trn key="aim:viewM&EProjectIndicators">Click here to view M & E Project Indicators</digi:trn>
																		</c:set>
																			<digi:link
																				href="/overallIndicatorManager.do?view=meindicators"
																				styleClass="sub-navGovSelected"
																				title="${ProjectIndicators}" onmouseover="setOverImg(3)"
																				onmouseout="setOutImg(3)">
																				<font color="ffffff">
																				<digi:trn	key="aim:projectIndicatorList">
																							Project Indicator List
																						</digi:trn>
																 				</font>
																			</digi:link>
																		</td>
																		<td>
																			<img id="img3" alt=""
																				src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif"
																				width="20" height="19" />
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
													<%-- end table title --%>
												</td>
											</tr>
											<tr>
												<td>
													<table width="100%" cellspacing="1" cellpadding=4 valign="top"
														align=left bgcolor="#ffffff" border="0" bordercolor="BLACK">
														<tr>
															<td bgColor=#d7eafd class=box-title height="20"	align="center">
																<digi:trn key="aim:programIndicatorList">
																	Program Indicator List
																</digi:trn>
															</td>
														</tr>
														<tr>
															<td >
															<table  cellPadding=3 cellspacing="1">
			<tr>
			<td><img src= "../ampTemplate/images/arrow_right.gif" border="0">  Level 1, </td>
			<td>	<img src= "../ampTemplate/images/square1.gif" border="0">  Level 2, </td>
			<td>	<img src= "../ampTemplate/images/square2.gif" border="0">  Level 3, </td>
			<td>	<img src= "../ampTemplate/images/square3.gif" border="0">  Level 4, </td>
			<td>	<img src= "../ampTemplate/images/square4.gif" border="0">  Level 5, </td>
			<td>	<img src= "../ampTemplate/images/square5.gif" border="0">  Level 6, </td>
			<td>	<img src= "../ampTemplate/images/square6.gif" border="0">  Level 7, </td>
			<td>	<img src= "../ampTemplate/images/square7.gif" border="0">  Level 8. </td>
			</tr>
			</table>	
															</td>
														</tr>
														<logic:notEmpty name="aimAllIndicatorForm" property="prgIndicators">
															<logic:iterate name="aimAllIndicatorForm"	property="prgIndicators" id="prgIndicators"	type="org.digijava.module.aim.helper.AllThemes">
														<tr>
															<td align="left">
																		<table width="100%" align="right" cellspacing="1" cellpadding="0" bgcolor="#d7eafd" border="0" >
																											
																			<tr bgcolor="#FFFFFF">
																				<td align="left" width=85%>
																					<img src= "../ampTemplate/images/arrow-014E86.gif" border="0">&nbsp;<b>
																						<bean:write name="prgIndicators" property="programName"/></b>
																				</td>
																						<c:if test="${aimAllIndicatorForm.programId!=prgIndicators.programId or aimAllIndicatorForm.indicatorFlag==false}">
																						
																				<td align="right">
																					<input class="dr-menu" type="button"	name="showIndicator" value="<digi:trn key="aim:shoindicator">Show Indicators</digi:trn>" 
																							onclick="showIndicators('<bean:write name="prgIndicators" property="programId"/>')" />
																				</td>
																						</c:if>
																						<c:if test="${aimAllIndicatorForm.programId==prgIndicators.programId and aimAllIndicatorForm.indicatorFlag==true}">
																						
																				<td align="right">
																					<input class="dr-menu" type="button"	name="hideIndicator" value="<digi:trn key="aim:hideind">Hide Indicators</digi:trn>" onclick="hideIndicators()"/>
																				</td>
																						</c:if>
																			</tr>
																					<c:if test="${aimAllIndicatorForm.indicatorFlag == true}">
																					<c:if test="${aimAllIndicatorForm.programId==prgIndicators.programId}">
																					<logic:notEmpty name="prgIndicators" property="allPrgIndicators">
																			<tr>
																				<td colspan=3>
																						<table align="right" width="99%" cellspacing="1" cellpadding="0" bgcolor="#00FFFF" border="0">
																							<logic:iterate name="prgIndicators"	property="allPrgIndicators" id="ampPrgIndicator" type="org.digijava.module.aim.helper.AllPrgIndicators">
																							<tr bgcolor="#ffffff">
																								<td width="9">
																									<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																								</td>
																								<td width=160>
																									<jsp:useBean id="urlParams11" type="java.util.Map"	class="java.util.HashMap" />
																									<c:set target="${urlParams11}" property="indicatorId">
																										<bean:write name="ampPrgIndicator" property="indicatorId" />
																									</c:set>
																									<bean:write name="ampPrgIndicator" property="name" />
																								</td>
																								<td width="270">
																											<bean:write name="ampPrgIndicator"
																													property="code" />
																								</td>
																								<td align="center" width="60">
																									<input class="dr-menu" type="button"
																											name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																													onclick="assignIndicatorTo('<bean:write name="ampPrgIndicator" property="indicatorId"/>')" />
																								</td>
																								<td align="left" width="12">
																								<c:set var="clickToDeleteIndicator">
																								<digi:trn key="aim:clickToDeleteIndicator">
																											Click here to Delete Indicator
																									</digi:trn>
																								</c:set>
																									<c:set target="${urlParams11}"
																										property="indicator" value="deletePrg" />
																											<digi:link href="/editAllIndicator.do"
																													name="urlParams11" title="${clickToDeleteIndicator}"
																																onclick="return deletePrgIndicator()">
																													<img src="../ampTemplate/images/trash_12.gif" border="0">
																											</digi:link>
																								</td>
																							</tr>
																								
																							</logic:iterate>
																						</table>
																				</td>
																			</tr>
																					</logic:notEmpty>
																					<logic:empty name="prgIndicators"	property="allPrgIndicators">
																			<tr>
																				<td colspan=3>
																					<table align="right" width="97%" cellspacing="1" cellpadding=3	bgcolor="#d7eafd" border="0" bordercolor="FF0000">
																						<tr bgcolor="#FFFFFF" align="left" bordercolor="FF0000">
																							<td>
																								<font color=red><digi:trn  key="aim:Noind">No Indicator present in the Program</digi:trn></font>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																				</logic:empty>
																				</c:if>
																				</c:if>
																			<tr>
																				<td colspan=3>
																					<logic:notEmpty name="aimAllIndicatorForm" property="map">
																						<logic:iterate name="aimAllIndicatorForm" property="map" id="maps">
																						<c:if test="${maps.key==prgIndicators.programId}">
																						<c:if test="${prgIndicators.programId==aimAllIndicatorForm.programId}">
 																							<table bgColor=#FFFFFF cellpadding="1" cellspacing="1" width="100%" valign="top" border="0">
																								<logic:iterate name="maps" property="value" id="subPrograms">
																								<%------- level 1 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="1">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="right" bgColor="#d7eafd" cellpadding="1" cellspacing="1" border="0">
																											<tr>
																												<td width=2%>
																													<img
																														src="../ampTemplate/images/arrow_right.gif"
																														border="0">
																												</td>
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="97%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																													<jsp:useBean id="urlParams1" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams1}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																												<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams1}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams1" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="97%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 1 ends ------------%>
																		<%------- level 2 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="2">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="3%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square1.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="95%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams2" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams2}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																															<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																															</digi:trn>
																												</c:set>
																												<c:set target="${urlParams2}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams2" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="95%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 2 ends ------------%>
																		<%------- level 3 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="3">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="4%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square2.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="93%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams3" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams3}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																												<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams3}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams3" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="93%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 3 ends ------------%>
																		<%------- level 4 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="4">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="5%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square3.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="91%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams4" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams4}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																														<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams4}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams4" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="91%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 4 ends ------------%>
																		<%------- level 5 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="5">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="6%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square4.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="89%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams5" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams5}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																														<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams5}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams5" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="89%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 5 ends ------------%>
																		<%------- level 6 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="6">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="7%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square5.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="87%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams6" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams6}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																														<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams6}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams6" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="87%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 6 ends ------------%>
																		<%------- level 7 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="7">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="8%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square6.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="85%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams7" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams7}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																												<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams7}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams7" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="85%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 7 ends ------------%>
																		<%------- level 8 starts ------------%>
																		<logic:equal name="subPrograms" property="indlevel" value="8">
																				<tr bgcolor="#ffffff">
																					<td height="15" colspan=2>
																						<table width="100%" align="rigth" bgColor="#d7eafd" cellpadding="1" cellspacing="1">
																											<tr>
																												<td width="9%" align="right" height="15" bgcolor="#f4f4f2">
																													<img src= "../ampTemplate/images/square7.gif" border="0">
																												</td>	
																												<td  width=90% bgcolor="#ffcccc">
																													<bean:write name="subPrograms"
																														property="name" />
																												</td>
																											</tr>
																											<logic:notEmpty name="aimAllIndicatorForm" property = "themeIndi">	
																												<logic:iterate name="aimAllIndicatorForm" property="themeIndi" id="themeIndiId">
																													<c:if test="${themeIndiId.key==subPrograms.ampThemeId}">
																														<logic:notEmpty name="themeIndiId" property="value">
																																		<logic:iterate name="themeIndiId" property="value" id="indi">
																								<tr>	
																									<td colspan=2>
																										<table width="83%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#00FFF" border="0">
																											<tr bgcolor="#ffffff" >
																												<td width="9">
																													<img src="../ampTemplate/images/link_out_bot.gif" border="0">
																												</td>
																												<td>
																													<bean:write name="indi" property="name" />
																												</td>
																												<td align=right>
																													<bean:write name="indi"	property="code" />
																												</td>
																												<jsp:useBean id="urlParams8" type="java.util.Map"	class="java.util.HashMap" />
																														<c:set target="${urlParams8}" property="indicatorId">
																															<bean:write name="indi" property="indicatorId" />
																														</c:set>
																												<td align="center" width="105">
																													<input class="dr-menu" type="button"
																														name="assignIndicator" value="<digi:trn key="aim:Assign">Assign to</digi:trn>"
																																onclick="assignIndicatorTo('<bean:write name="indi" property="indicatorId"/>')" />
																												</td>
																												<td align="left" width="12">
																												<c:set var="clickToDeleteIndicator">
																												<digi:trn key="aim:clickToDeleteIndicator">
																															Click here to Delete Indicator
																														</digi:trn>
																												</c:set>
																														<c:set target="${urlParams8}"
																															property="indicator" value="deletePrg" />
																															<digi:link href="/editAllIndicator.do"
																																name="urlParams8" title="${clickToDeleteIndicator}"
																																	onclick="return deletePrgIndicator()">
																															<img src="../ampTemplate/images/trash_12.gif" border="0">
																															</digi:link>
																												</td>
																											</tr>
																									</table>
																									</td>
																								</tr>
																																		</logic:iterate>
																														</logic:notEmpty>
																														<logic:empty name="themeIndiId" property="value">
																								<tr>
																									<td colspan=2>
																										<table width="83%" align="right" cellspacing="1" cellpadding="0"	bgcolor="#FF0000" border="0">
																								
																											<tr bgcolor="ffffff">
																												<td>
																													<font color=red><digi:trn  key="aim:Noindpr">No indicator present in the Program</digi:trn></font>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																														</logic:empty>
																													</c:if>
																												</logic:iterate>
																											</logic:notEmpty>
																							</table>
																					</td>
																				</tr>
																		</logic:equal>
																		<%------- level 8 ends ------------%>
																								</logic:iterate>
																							</table>
																						</c:if>
																						</c:if>
																						</logic:iterate>
																					</logic:notEmpty>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</logic:iterate>
														</logic:notEmpty>
														<logic:empty name="aimAllIndicatorForm"
															property="prgIndicators">
															<tr align="center" bgcolor="#ffffff">
																<td>
																	<b> <digi:trn key="aim:noProgramIndicatorsPresent">
																No Program indicators present
															</digi:trn> </b>
																</td>
															</tr>
														</logic:empty>
														<tr>
															<td bgColor=#d7eafd class=box-title height="20"
																align="center">
																<digi:trn key="aim:projectIndicatorList">
																	Project Indicator List
																</digi:trn>
															</td>
														</tr>
														<logic:notEmpty name="aimAllIndicatorForm"
															property="projIndicators">
															<logic:iterate name="aimAllIndicatorForm"
																property="projIndicators" id="projIndicators"
																type="org.digijava.module.aim.helper.AllActivities">
																<tr>
																	<td align="left">
																		<img src="../ampTemplate/images/arrow-014E86.gif"
																			border="0">
																		&nbsp;
																		<b> <bean:write name="projIndicators"
																				property="activityName" /> </b>
																	</td>
																</tr>
																<logic:notEmpty name="projIndicators"
																	property="allMEIndicators">
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding=3
																				bgcolor="#d7eafd" border="0">
																				<logic:iterate name="projIndicators"
																					property="allMEIndicators" id="allMEIndicators"
																					type="org.digijava.module.aim.helper.AllMEIndicators">
																					<tr bgcolor="#ffffff">
																						<td width="9">
																							<c:if
																								test="${allMEIndicators.defaultInd == true}">
																								<img src="../ampTemplate/images/bullet_red.gif"
																									border="0">
																							</c:if>
																							<c:if
																								test="${allMEIndicators.defaultInd == false}">
																								<img src="../ampTemplate/images/bullet_grey.gif"
																									border="0">
																							</c:if>
																						</td>
																						<td>
																							<jsp:useBean id="urlParams22" type="java.util.Map"
																								class="java.util.HashMap" />
																							<c:set target="${urlParams22}"
																								property="indicatorId">
																								<bean:write name="allMEIndicators"
																									property="ampMEIndId" />
																							</c:set>
																							<bean:write name="allMEIndicators"
																								property="name" />
																						</td>
																						<td width="50">
																							<bean:write name="allMEIndicators"
																								property="code" />
																						</td>
																						<td width="40">
																							<input class="dr-menu" type="button"
																								name="editIndicator" value="<digi:trn key="aim:edit">Edit</digi:trn>"
																								onclick="editProjIndicator('<bean:write name="allMEIndicators" property="ampMEIndId"/>')">
																						</td>
																						<td align="left" width="12">
																						<c:set var="clickToDeleteIndicator">
																						<digi:trn key="aim:clickToDeleteIndicator">
																							Click here to Delete Indicator
																						</digi:trn>
																						</c:set>
																						
																							<c:set target="${urlParams22}"
																								property="indicator" value="deleteProj" />
																							<digi:link href="/editAllIndicator.do"
																								name="urlParams22" title="${clickToDeleteIndicator}"
																								onclick="return deleteProjIndicator()">
																								<img src="../ampTemplate/images/trash_12.gif"
																									border="0">
																							</digi:link>
																						</td>
																					</tr>
																				</logic:iterate>
																			</table>
																		</td>
																	</tr>
																</logic:notEmpty>
																<logic:empty name="projIndicators"
																	property="allMEIndicators">
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding=3
																				bgcolor="#d7eafd" border="0">
																				<tr bgcolor="#ffffff" align="center">
																					<td>
																						No Indicators assigned to this Project
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</logic:empty>
															</logic:iterate>
														</logic:notEmpty>
														<logic:empty name="aimAllIndicatorForm"
															property="projIndicators">
															<tr align="center" bgcolor="#ffffff">
																<td>
																	<b> <digi:trn key="aim:noProjectIndicatorsPresent">
																No Project indicators present
															</digi:trn> </b>
																</td>
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
					<tr>
						<td>
							<table width="100%" cellspacing="1" cellpadding=3 bgcolor="ffffff"
								border="1">
								<tr bgcolor="#ffffff">
									<td width="9">
										<img src="../ampTemplate/images/bullet_red.gif" border="0">
									</td>
									<td width="100">
										<digi:trn key="aim:globalIndicator">Global Indicator</digi:trn>
									</td>
									<td width="9">
										<img src="../ampTemplate/images/bullet_grey.gif" border="0">
									</td>
									<td>
										<digi:trn key="aim:activitySpecificIndicator">Activity Specific Indicator</digi:trn>
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
