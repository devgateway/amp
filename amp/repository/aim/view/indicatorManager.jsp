<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	<!--
		function editIndicator(id) {
			openNewWindow(500, 300);
			<digi:context name="addIndicator" property="context/module/moduleinstance/addIndicator.do?event=edit" />
			document.aimIndicatorForm.action = "<%= addIndicator %>&indId=" + id;
			document.aimIndicatorForm.target = popupPointer.name;
			document.aimIndicatorForm.submit();
		}

		function addingIndicators()
		{
			openNewWindow(500, 300);
			<digi:context name="addIndicator" property="context/module/moduleinstance/addIndicator.do?event=add" />
			document.aimIndicatorForm.action = "<%= addIndicator %>";
			document.aimIndicatorForm.target = popupPointer.name;
			document.aimIndicatorForm.submit();
			return true;
		}
		function deleteIndicator(id)
		{
			openNewWindow(600, 300);
			<digi:context name="deleteInd" property="context/module/moduleinstance/deleteIndicator.do?action=delete"/>
			document.aimIndicatorForm.action = "<%= deleteInd %>&id=" +id;
			document.aimIndicatorForm.target = popupPointer.name;
			document.aimIndicatorForm.submit();
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
<digi:instance property="aimIndicatorForm" />
<digi:form action="/indicatorManager.do" method="post">

<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
				<tr>
						<td class=r-dotted-lg width=14>&nbsp;</td>
						<td align=left class=r-dotted-lg valign="top" width=750>
								<table cellPadding=5 cellspacing="0" width="100%" border="0">
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
														<digi:trn key="aim:projectIndicatorManager">
																Project Indicator Manager
														</digi:trn>
												</td>
										</tr><%-- End navigation --%>
										<tr>
												<td height=16 valign="center" width=571><span class=subtitle-blue>
														<digi:trn key="aim:projectIndicatorManager">
																Project Indicator Manager
														</digi:trn></span>
												</td>
										</tr>
										<tr><td height=16 valign="center" width=571><digi:errors /></td></tr>
										<tr>
												<td noWrap width="100%" vAlign="top">
														<table width="100%" cellspacing="0" cellspacing="0" border="0">
																<tr><td noWrap width=600 vAlign="top">
																		<table bgColor=#d7eafd cellpadding="0" cellspacing="0" width="100%" valign="top">
																				<tr bgColor=#ffffff><td vAlign="top" width="100%">
																						<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left>
																								<tr><td>
																										<table cellspacing="0" cellpadding="0">
																												<tr>
																														<td noWrap height=17>
																														<c:set var="iewIndicators">
																														<digi:trn key="aim:viewIndicators">Click here to View Indicators</digi:trn>
																														</c:set>
																																<digi:link href="/indicatorManager.do?view=indicators"  styleClass="sub-navGovSelected" title="${iewIndicators}" onmouseover="setOverImg(1)" onmouseout="setOutImg(1)"><font color="ffffff">
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
																																<digi:link href="/indicatorManager.do?view=multiprogram"  styleClass="sub-navGovSelected" title="${MultiProgramIndicators}" onmouseover="setOverImg(2)" onmouseout="setOutImg(2)"><font color="ffffff">
																																		<digi:trn key="aim:multiProgramManager">
																																				Multi Program Manager
																																		</digi:trn></font>
																																</digi:link>
																														</td>
                                                                                                                        <td>
                                                                                                                          <img id="img2" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-rightselected1.gif" width="20" height="19" />
                                                                                                                        </td>
																														<td noWrap height=17>
																														<c:set var="EProjectIndicators">
																															<digi:trn key="aim:viewM&EProjectIndicators" >Click here to view M & E Project Indicators</digi:trn>
																														</c:set>

																																<digi:link href="/indicatorManager.do?view=meindicators"  styleClass="sub-navGov" title="${EProjectIndicators}"><font color="ffffff">
																																		<digi:trn key="aim:projectIndicatorList">
																																				Project Indicator List
																																		</digi:trn></font>
																																</digi:link>
																														</td>
                                                                                                                        <td>
                                                                                                                          <img id="img3" alt="" src="/TEMPLATE/ampTemplate/module/aim/images/tab-right1.gif" width="20" height="19" />
                                                                                                                        </td>
																												</tr>
																										</table>
																								</tr>
																								<tr>
																										<td noWrap width="100%" vAlign="top">
																												<table width="100%" cellspacing="1" cellspacing="1" border="0">
																														<tr><td noWrap width=600 vAlign="top">
																																<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
																																		<tr bgColor=#ffffff>
																																				<td vAlign="top" width="100%">
																																						<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>
																																								<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
																																									<digi:trn key="aim:projectIndicatorList">
												Project Indicator List
											</digi:trn>
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#ffffff">

													<logic:notEmpty name="aimIndicatorForm" property="indicators">
														<tr><td>
															<table width="100%" cellspacing="1" cellpadding=3 bgcolor="#d7eafd">

																<logic:iterate name="aimIndicatorForm" property="indicators" id="indicators"
																type="org.digijava.module.aim.helper.AmpMEIndicatorList">

																	<tr bgcolor="#ffffff">
																	<td width="9">
																		<c:if test="${indicators.defaultInd == true}">
																			<img src= "../ampTemplate/images/bullet_red.gif" border="0">
																		</c:if>
																		<c:if test="${indicators.defaultInd == false}">
																			<img src= "../ampTemplate/images/bullet_grey.gif" border="0">
																		</c:if>
																	</td>
																	<td>
																		<a href="javascript:editIndicator('<bean:write name="indicators" property="ampMEIndId" />')">

																			<bean:define id="indName">
																				<bean:write name="indicators" property="name"/>
																			</bean:define>
																			<digi:trn key='<%="aim:" + indName%>'>
																			<bean:write name="indicators" property="name"/></digi:trn>

																		</a>
																	</td>
																	<td width="100">
																		<bean:write name="indicators" property="code"/>
																	</td>
																	<td align="left" width="12">
																		<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="id">
																			<bean:write name="indicators" property="ampMEIndId" />
																		</c:set>
																		<c:set target="${urlParams}" property="action" value="delete"/>
																		<c:set var="translation">
																			<digi:trn key="aim:clickToDeleteIndicator">Click here to Delete Indicator</digi:trn>
																		</c:set>
																		<a href="javascript:deleteIndicator('<bean:write name="indicators" property="ampMEIndId" />')">
																			<img src= "../ampTemplate/images/trash_12.gif" border="0">
																		</a>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>

													</logic:notEmpty>
													<logic:empty name="aimIndicatorForm" property="indicators">
														<tr align="center" bgcolor="#ffffff"><td><b>
															<digi:trn key="aim:noProjectIndicatorsPresent">No Project indicators present</digi:trn>
															</b></td>
														</tr>
													</logic:empty>

													<tr bgcolor="#ffffff">
														<td height="20" align="center"><B>
															<html:button  styleClass="dr-menu" property="submitButton"  onclick="addingIndicators()">
																<digi:trn key="btn:addNewAIndicator">Add a New Indicator</digi:trn>
															</html:button>
														</td>
													</tr>
											</table>
										</td></tr>
									</table>

								</td>
							</tr>

						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td>
		<table width="100%" cellspacing="1" cellpadding=3 bgcolor="ffffff" border="0">
			<tr bgcolor="#ffffff">
				<td width="9">
					<img src= "../ampTemplate/images/bullet_red.gif" border="0">
				</td>
				<td width="100">
					<digi:trn key="aim:globalIndicator">Global Indicator</digi:trn>
				</td>
				<td width="9">
					<img src= "../ampTemplate/images/bullet_grey.gif" border="0">
				</td>
				<td>
					<digi:trn key="aim:activitySpecificIndicator">Activity Specific Indicator</digi:trn>
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>
