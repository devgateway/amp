<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--
	function searchIndicatorkey()
	{
		<digi:context name="searchInd" property="context/module/moduleinstance/searchIndicators.do" />
		document.aimIndicatorForm.action = "<%=searchInd%>";
		document.aimIndicatorForm.target = "_self";
		document.aimIndicatorForm.submit();
	}
	function addIndicatorTL()
	{
		<digi:context name="addInd" property="context/module/moduleinstance/addIndicatorsTL.do"/>
		document.aimIndicatorForm.action = "<%=addInd%>";
		document.aimIndicatorForm.target = "_self";
		document.aimIndicatorForm.submit();
	}


	function addNewIndicatorTL()
	{
		var valid = validateForm();
		if (valid == true) {
			<digi:context name="addNewInd" property="context/module/moduleinstance/addNewIndicatorTL.do"/>
			document.aimIndicatorForm.action = "<%=addNewInd%>";
			document.aimIndicatorForm.target = "_self";
			document.aimIndicatorForm.submit();				  
		}
		return valid;

	}
	function unload(){}

	function validateForm() {
		if (trim(document.aimIndicatorForm.indicatorName.value).length == 0) {
			alert("Please enter indicator name");
			document.aimIndicatorForm.indicatorName.focus();
			return false;
		}

		if (trim(document.aimIndicatorForm.indicatorCode.value).length == 0) {
			alert("Please enter indicator code");
			document.aimIndicatorForm.indicatorCode.focus();
			return false;
		}
		return true;
	}	

-->
</script>

<digi:form action="/selectCreateIndicators.do">
<digi:instance property="aimIndicatorForm" />
<html:hidden property="activityId" />
<html:hidden property="addswitch" />

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">
	<tr><td width="100%" valign="top" align="left">
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td></tr>
	<tr><td width="100%" valign="top" align="left">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
			<tr>
				<td class=r-dotted-lg width=14>&nbsp;</td>
				<td align=left class=r-dotted-lg vAlign=top width=750>

				<table cellPadding=5 cellSpacing=0 width="100%">
					<tr>
						<td height=33><span class=crumb>
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
							</bean:define>
							<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>
							&nbsp;&gt;&nbsp;
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>
							</bean:define>
							<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
							</digi:link>
							&nbsp;&gt;&nbsp;						
							<digi:trn key="aim:activityList">Activity List</digi:trn>
						</td>
					</tr>
					<tr>
						<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span></td>
					</tr>
					<tr>
						<td noWrap width=571 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#3754a1>
									<td vAlign="top" width="100%">
										<jsp:include page="teamSetupMenu.jsp" flush="true" />								
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td>&nbsp;</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	
											<tr><td>
												<digi:errors />
											</td></tr>
											<tr bgColor=#f4f4f2>
												<td bgColor=#f4f4f2>
													<table border="0" cellPadding=0 cellSpacing=0 width=237>
														<tr bgColor=#f4f4f2>
															<td bgColor=#c9c9c7 class=box-title width=220>
																<digi:trn key="aim:monitoringAndEvaluation">&nbsp;
																	Monitoring & Evaluation	
																</digi:trn>
															</td>
															<td background="module/aim/images/corner-r.gif" height="17" width=17>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												
												<td bgColor=#ffffff class=box-border valign="top">
													<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
														<tr>
															<td align="left" width="100%" valign="center">
																<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
																bgcolor="#ffffff">
																	<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">
																		<b><digi:trn key="aim:PickfrmList">1.   Pick from the List</digi:trn></b>
																	</td></tr>
																</table>
															</td>
														</tr>
														<tr>
															<td align="center" bgcolor="#f4f4f2">
																<table cellSpacing=2 cellPadding=3 vAlign="top" align="center"
																bgcolor="#f4f4f2">
																	<tr>
																		<td bgcolor="#f4f4f2" align="right" valign="top">
																			Indicator Name
																		</td>
																		<td bgcolor="#f4f4f2" align="left">
																			<html:select property="selectedIndicators" styleClass="inp-text" size="5" multiple="true">
																				<logic:notEmpty name="aimIndicatorForm" property="nondefaultindicators">
																					<html:optionsCollection name="aimIndicatorForm" 
																					property="nondefaultindicators" value="ampMEIndId" label="name"/>
																				</logic:notEmpty>
																			</html:select>
																		</td>														
																	</tr>
																	<tr>
																		<td bgcolor="#f4f4f2" align="center" colspan="2">
																			<input class="buton" type="button" name="addFromList"
																			value=" Add " onclick="addIndicatorTL()">
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											
											<tr>
												<td bgColor=#ffffff class=box-border valign="top">
													<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
														<tr>
															<td align="left" width="100%" valign="center">
																<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
																bgcolor="#ffffff">
																	<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">
																		<b><digi:trn key="aim:SearchList">2.   Search for Indicators</digi:trn></b>
																	</td></tr>
																</table>
															</td>
														</tr>
														<tr>
															<td align="center" bgcolor="#f4f4f2">
																<table cellSpacing=2 cellPadding=3 vAlign="top" align="center"
																bgcolor="#f4f4f2">
																	<tr>
																		<td bgcolor="#f4f4f2" align="right">
																			Give a Keyword
																		</td>
																		<td bgcolor="#f4f4f2" align="left">
																			<html:text property="searchkey" size="20" styleClass="inp-text"/>&nbsp;&nbsp;
																			<input class="buton" type="button" name="searchIndicatorkeyword" value=" Go " 
																			onclick="searchIndicatorkey()">
																		</td>														
																	</tr>
																	<logic:notEmpty name="aimIndicatorForm" property="searchReturn">
																		<tr><td bgcolor="#f4f4f2" align="center" colspan="2">
																		<table width="100%" cellSpacing=0 cellPadding=1 vAlign="top" align="center" bgcolor="#ffffff">
																			<logic:iterate name="aimIndicatorForm" property="searchReturn" 
																			id="searchValues" type="org.digijava.module.aim.dbentity.AmpMEIndicators">
																				<tr bgcolor="#ffffAA">
																					<td align="right" width="3">
																						<html:multibox property="selIndicators">
																							<bean:write name="searchValues" property="ampMEIndId"/>
																						</html:multibox>
																					</td>
																					<td align="left">&nbsp;
																						<bean:write name="searchValues" property="name"/>
																					</td>
																				</tr>
																			</logic:iterate>
																		</table>
																		</td></tr>
																	</logic:notEmpty>
																	<tr>
																		<td bgcolor="#f4f4f2" align="center" colspan="3">
																			<input class="buton" type="button" name="addFromSearchList" value=" Add " 
																			onclick="addIndicatorTL()">
																		</td>
																	</tr>
																</table>														
															</td>
														</tr>
													</table>
												</td>
											</tr>													
											<tr>
												<td bgColor=#ffffff class=box-border valign="top">
													<table width="100%" border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
														<tr>
															<td align="left" width="100%" valign="center">
																<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="center"
																bgcolor="#ffffff">
																	<tr><td valign="center" align="center" bgcolor="#dddddd" height="20">
																		<b><digi:trn key="aim:NewIndicatorCreation">3.   Create a New Indicator</digi:trn></b>
																	</td></tr>
																</table>
															</td>
														</tr>
														<tr>
															<td align="center" bgcolor="#f4f4f2">
																<table cellSpacing=2 cellPadding=3 vAlign="top" align="center"
																bgcolor="#f4f4f2" border=0>
																	<tr>
																		<td bgcolor="#f4f4f2" align="right">
																			Indicator Name<FONT color=red>*</FONT>
																		</td>
																		<td bgcolor="#f4f4f2" align="left">
																			<html:text property="indicatorName" size="20" styleClass="inp-text"/>
																		</td>																	
																	</tr>
																	<tr>
																		<td bgcolor="#f4f4f2" align="right">
																			Description
																		</td>
																		<td bgcolor="#f4f4f2" align="left">
																			<html:textarea property="indicatorDesc" cols="35" rows="2" styleClass="inp-text"/>
																		</td>																	
																	</tr>
																	<tr>
																		<td bgcolor="#f4f4f2" align="right">
																			Indicator Code<FONT color=red>*</FONT>
																		</td>
																		<td bgcolor="#f4f4f2" align="left">
																			<html:text property="indicatorCode" size="20" styleClass="inp-text"/>
																		</td>																	
																	</tr>
																	<tr>
																		<td bgcolor="#f4f4f2" align="center" colspan="2">
																			<input class="buton" type="button" name="addnewIndicator" 
																			value=" Add " onclick="return addNewIndicatorTL()">
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
								<tr bgcolor="#f4f4f2">
									<td>&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>
