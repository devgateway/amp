<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	<!--
		function editPrgIndicator(id)
		{
			openNewWindow(650, 500);
			<digi:context name="indAssign" property="context/module/moduleinstance/addThemeIndicator.do"/>
			document.aimAllIndicatorForm.action = "<%= indAssign %>?themeId=" + id;
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
		function deletePrgIndicator()
	  	{
			return confirm("Do you want to delete the Indicator ? Please check whether the indicator is being used by some Program.");
		}
		function deleteProjIndicator()
	  	{
			return confirm("Do you want to delete the Indicator ? Please check whether the indicator is being used by some Activity.");
		}
		function load(){}
		function unload(){}
	-->
</script>

<digi:errors/>
<digi:instance property="aimAllIndicatorForm" />
<digi:form action="/overallIndicatorManager.do" method="post">
<digi:context name="digiContext" property="context" />
<input type="hidden" name="indicator">
<%--  AMP Admin Logo --%>
<jsp:include page="teamPagesHeader.jsp" flush="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr><%-- Start Navigation --%>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:indicatorManager">
							Indicator Manager
						</digi:trn>
					</td>
				</tr><%-- End navigation --%>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:indicatorManager">
							Indicator Manager
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
																<bean:define id="translation">
																	<digi:trn key="aim:viewIndicators">Click here to View Indicators</digi:trn>
																</bean:define>
																<digi:link href="/overallIndicatorManager.do?view=indicators"  styleClass="sub-navGov" title="<%=translation%>" ><font color="ffffff">
																<digi:trn key="aim:programIndicatorList">
																		Program Indicator List
																</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;</font>
																</digi:link>
												 			</td>
															<td noWrap height=17>
																<bean:define id="translation">
																	<digi:trn key="aim:viewMultiProgramIndicators" >Click here to view Multi Program Indicators</digi:trn>
																</bean:define>
																<digi:link href="/overallIndicatorManager.do?view=multiprogram"  styleClass="sub-navGovSelected" title="<%=translation%>" ><font color="ffffff">
															<digi:trn key="aim:multiProgramManager">
																Multi Program Manager
															</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
																</digi:link>
															</td>
															<td noWrap height=17>
																<bean:define id="translation">
																	<digi:trn key="aim:viewM&EProjectIndicators" >Click here to view M & E Project Indicators</digi:trn>
																</bean:define>
																<digi:link href="/overallIndicatorManager.do?view=meindicators"  styleClass="sub-navGovSelected" title="<%=translation%>" ><font color="ffffff">
																<digi:trn key="aim:projectIndicatorList">
																		Project Indicator List
																</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
																</digi:link>
															</td>		
														</tr>
													</table>
												</td>
												</tr>
										  </table>
											<%-- end table title --%>										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#ffffff">
													<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
														<digi:trn key="aim:programIndicatorList">
															Program Indicator List
														</digi:trn>
													</td></tr>
													<logic:notEmpty name="aimAllIndicatorForm" property="prgIndicators">
													<logic:iterate name="aimAllIndicatorForm" property="prgIndicators" id="prgIndicators" type="org.digijava.module.aim.helper.AllThemes">
														<tr><td align="left">
														<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#ffffff">
															<tr bgcolor="#ffffff">
															<td align="left">
																<img src= "../ampTemplate/images/arrow-014E86.gif" border=0>&nbsp;<b>
																<bean:write name="prgIndicators" property="programName"/></b>
															</td>
															<td align="right">
																<input class="buton" type="button" name="editIndicator" value="Edit Indicators" onclick="editPrgIndicator('<bean:write name="prgIndicators" property="programId"/>')"/>
															</td>
															</tr>
														</table>
														</td></tr>
														<logic:notEmpty name="prgIndicators" property="allPrgIndicators">
														<tr><td>
															<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																<logic:iterate name="prgIndicators" property="allPrgIndicators" id="allPrgIndicators" type="org.digijava.module.aim.helper.AllPrgIndicators">	
																	<tr bgcolor="#ffffff">
																	<td width="9">
																		<img src= "../ampTemplate/images/link_out_bot.gif" border=0>
																	</td>																	
																	<td>
																		<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams1}" property="indicatorId">
																			<bean:write name="allPrgIndicators" property="indicatorId" />
																		</c:set>
																		<bean:write name="allPrgIndicators" property="name"/>
																	</td>
																	<td width="50">
																		<bean:write name="allPrgIndicators" property="code"/>
																	</td>
																	<td width="40">
																		<%--<input class="buton" type="button" name="editIndicator" value="Edit" onclick="editPrgIndicator('<bean:write name="prgIndicators" property="programId"/>')"/>--%>
																	</td>																
																	<td align="left" width="12">
																		<bean:define id="translation">
																			<digi:trn key="aim:clickToDeleteIndicator">
																				Click here to Delete Indicator
																			</digi:trn>
																		</bean:define>
																		<c:set target="${urlParams1}" property="indicator" value="deletePrg"/>
																		<digi:link href="/editAllIndicator.do" name="urlParams1" title="<%=translation%>" onclick="return deletePrgIndicator()">
																			<img src= "../ampTemplate/images/trash_12.gif" border=0>
																		</digi:link>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>
														</logic:notEmpty>
														<logic:empty name="prgIndicators" property="allPrgIndicators">
															<tr><td>
																<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																	<tr bgcolor="#ffffff" align="center"><td>
																		No Indicators assigned to this Program
																	</td></tr>
																</table>
															</td></tr>
														</logic:empty>
													</logic:iterate>
													</logic:notEmpty>
													<logic:empty name="aimAllIndicatorForm" property="prgIndicators">
														<tr align="center" bgcolor="#ffffff"><td><b>
															<digi:trn key="aim:noProgramIndicatorsPresent">
																No Program indicators present
															</digi:trn></b></td>
														</tr>
													</logic:empty>
													<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
														<digi:trn key="aim:projectIndicatorList">
															Project Indicator List
														</digi:trn>
													</td></tr>
													<logic:notEmpty name="aimAllIndicatorForm" property="projIndicators">
													<logic:iterate name="aimAllIndicatorForm" property="projIndicators" id="projIndicators" type="org.digijava.module.aim.helper.AllActivities">
														<tr><td align="left">
															<img src= "../ampTemplate/images/arrow-014E86.gif" border=0>&nbsp;<b>
															<bean:write name="projIndicators" property="activityName"/></b>																			  </td></tr>
														<logic:notEmpty name="projIndicators" property="allMEIndicators">
														<tr><td>
															<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																<logic:iterate name="projIndicators" property="allMEIndicators" id="allMEIndicators" type="org.digijava.module.aim.helper.AllMEIndicators">	
																	<tr bgcolor="#ffffff">
																	<td width="9">
																		<c:if test="${allMEIndicators.defaultInd == true}">
																			<img src= "../ampTemplate/images/bullet_red.gif" border=0>
																		</c:if>
																		<c:if test="${allMEIndicators.defaultInd == false}">
																			<img src= "../ampTemplate/images/bullet_grey.gif" border=0>
																		</c:if>
																	</td>																	
																	<td>
																		<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams2}" property="indicatorId">
																			<bean:write name="allMEIndicators" property="ampMEIndId" />
																		</c:set>
																		<bean:write name="allMEIndicators" property="name"/>
																	</td>
																	<td width="50">
																		<bean:write name="allMEIndicators" property="code"/>
																	</td>
																	<td width="40">
																		<input class="buton" type="button" name="editIndicator" value="Edit" onclick="editProjIndicator('<bean:write name="allMEIndicators" property="ampMEIndId"/>')">
																	</td>																	
																	<td align="left" width="12">
																		<bean:define id="translation">
																			<digi:trn key="aim:clickToDeleteIndicator">
																				Click here to Delete Indicator
																			</digi:trn>
																		</bean:define>
																		<c:set target="${urlParams2}" property="indicator" value="deleteProj"/>
																		<digi:link href="/editAllIndicator.do" name="urlParams2" title="<%=translation%>" onclick="return deleteProjIndicator()">
																			<img src= "../ampTemplate/images/trash_12.gif" border=0>
																		</digi:link>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>
														</logic:notEmpty>
														<logic:empty name="projIndicators" property="allMEIndicators">
															<tr><td>
																<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																	<tr bgcolor="#ffffff" align="center"><td>
																		No Indicators assigned to this Project
																	</td></tr>
																</table>
															</td></tr>
														</logic:empty>
													</logic:iterate>
													</logic:notEmpty>
													<logic:empty name="aimAllIndicatorForm" property="projIndicators">
														<tr align="center" bgcolor="#ffffff"><td><b>
															<digi:trn key="aim:noProjectIndicatorsPresent">
																No Project indicators present
															</digi:trn></b></td>
														</tr>
													</logic:empty>	
											</table>
										</td></tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td>
					<table width="100%" cellspacing=1 cellpadding=3 bgcolor="ffffff" border=0>
							<tr bgcolor="#ffffff">
									<td width="9">
											<img src= "../ampTemplate/images/bullet_red.gif" border=0>
									</td>
									<td width="100">
											<digi:trn key="aim:globalIndicator">Global Indicator</digi:trn>
									</td>
									<td width="9">
											<img src= "../ampTemplate/images/bullet_grey.gif" border=0>
									</td>
									<td>
											<digi:trn key="aim:activitySpecificIndicator">Activity Specific Indicator</digi:trn>
									</td>
							</tr>																
					</table>
				</td></tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
