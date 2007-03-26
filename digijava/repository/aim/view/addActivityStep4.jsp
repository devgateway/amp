<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
		
<script language="JavaScript">

	<!--

	function addRegionalFunding() {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=show" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} 		

	function editFunding(id) {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=showEdit" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>&fundId="+id;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} 		

	function removeRegFundings() {
		<digi:context name="rem" property="context/module/moduleinstance/removeRegionalFunding.do?edit=true" />
		document.aimEditActivityForm.action = "<%= rem %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();	
	}

	function validateForm() {
		return true;
	}

	-->
	
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step"/>


<html:hidden property="editAct" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
	<tr>
		<td width="100%" vAlign="top" align="left">
		<!--  AMP Admin Logo -->
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
		<!-- End of Logo -->
		</td>
	</tr>
	<tr>
		<td width="100%" vAlign="top" align="left">
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
				<tr>
					<td class=r-dotted-lg width="10">&nbsp;</td>
					<td align=left vAlign=top class=r-dotted-lg>
						<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
							<tr><td>
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
									<tr>
										<td>
											<span class=crumb>
												<c:if test="${aimEditActivityForm.pageId == 0}">
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
													</bean:define>
													<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
														<digi:trn key="aim:AmpAdminHome">
															Admin Home
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
												</c:if>
												<c:if test="${aimEditActivityForm.pageId == 1}">								
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
													</bean:define>
													<digi:link href="/viewMyDesktop.do" styleClass="comment" 
													onclick="return quitRnot()" title="<%=translation%>" >
														<digi:trn key="aim:portfolio">
															Portfolio
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;								
												</c:if>																	
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAddActivityStep1">
														Click here to go to Add Activity Step 1</digi:trn>
												</bean:define>
												<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" 
												title="<%=translation%>" >
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
												
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAddActivityStep2">
														Click here to goto Add Activity Step 2</digi:trn>
												</bean:define>
												<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" 
												title="<%=translation%>" >	
													<digi:trn key="aim:addActivityStep2">
														Step 2
													</digi:trn>
												</digi:link>&nbsp;&gt;&nbsp;						
												
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAddActivityStep3">
														Click here to goto Add Activity Step 3</digi:trn>
												</bean:define>
												<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" 
												title="<%=translation%>" >	
													<digi:trn key="aim:addActivityStep3">
														Step 3
													</digi:trn>
												</digi:link>&nbsp;&gt;&nbsp;						
												<digi:trn key="aim:addActivityStep4">
													Step 4
												</digi:trn>
											</span>
										</td>
									</tr>
								</table>
							</td></tr>
							<tr><td>
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
									<tr>
										<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
											<c:if test="${aimEditActivityForm.editAct == false}">
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>			
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>:
													<bean:write name="aimEditActivityForm" property="title"/>
											</c:if>										
										</td>
									</tr>	
								</table>
							</td></tr>
							<tr> <td>
								<digi:errors/>
							</td></tr>				
							<tr><td>
								<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">  
									<tr><td width="75%" vAlign="top">
										<table cellPadding=0 cellSpacing=0 width="100%" vAlign="top">
											<tr>
												<td width="100%">
													<table cellPadding=0 cellSpacing=0 width="100%" border=0>
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"></td>
															<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
																<digi:trn key="aim:step4of9RegionalFunding">
																	Step 4 of 9: Regional Funding
															</digi:trn>
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"></td>
														</tr>
													</table>
												</td>								
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" 
													bgcolor="#006699">
														<tr>
															<td bgColor=#f4f4f2 align="center" vAlign="top">
																<table width="95%" bgcolor="#f4f4f2">
																	<tr>
																		<td>
																			<IMG alt=Link height=10 
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<a title="<digi:trn key="aim:regionalFunding">Regional funding</digi:trn>">
																				<b><digi:trn key="aim:regionalFunding">Regional funding</digi:trn></b>
																			</a>
																		</td>
																	</tr>
																	<tr><td>&nbsp;</td></tr>
																	<tr>
																		<td align="left">
																			<table width="100%" cellSpacing=5 cellPadding=0 border=0
																			class="box-border-nopadding">
																			<logic:notEmpty name="aimEditActivityForm" property="regionalFundings">
																					<tr><td>
																							<b>
																							<digi:trn key="aim:donorcommitments">Donor Commitments</digi:trn> - (
																							<digi:trn key="aim:totalActualAllocation">Total actual 
																							allocation</digi:trn> = 
																							<c:out value="${aimEditActivityForm.totalCommitments}"/> 
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</b>
																						</td></tr>
																						<tr><td>
																							<b>
																							<digi:trn key="aim:donordisbursements">Donor Disbursements</digi:trn> - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> = 
																							<c:out value="${aimEditActivityForm.totalDisbursements}"/> 
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</b>
																						</td></tr>
																		<tr><td><b>
																		<font
																		<c:if test="${aimEditActivityForm.totalCommitments < aimEditActivityForm.regionTotalDisb }">
																		 color="RED"
																		</c:if>
																		>
																		<digi:trn key="aim:totalRegionalActualDisbursement">Regional Grand Total Actual Disbursements</digi:trn>=
																		<c:out value="${aimEditActivityForm.regionTotalDisb}"/>
																		<c:out value="${aimEditActivityForm.currCode}"/>																			
																		</font></b>
																		</td></tr>
																				
																				
																				<logic:iterate name="aimEditActivityForm" property="regionalFundings" 
																				id="regionalFunding" 
																				type="org.digijava.module.aim.helper.RegionalFunding"> <!-- L1 START-->
																				<tr><td>
																					<!-- Region name -->
																					<html:multibox property="selRegFundings">
																						<bean:write name="regionalFunding" property="regionId"/>
																					</html:multibox>
																					<bean:write name="regionalFunding" property="regionName"/>
																					&nbsp;
																					<a href="javascript:editFunding('
																						<bean:write name="regionalFunding" property="regionId"/> 
																					')">Edit this funding</a>
																				</td></tr>
																				<tr><td>
																					<!-- Regional funding details -->
																					<table width="100%" cellSpacing=1 cellPadding=3 border=0
																					bgcolor="#d7eafd">
																					<logic:notEmpty name="regionalFunding" property="commitments">
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<td>Actual/Planned</td>
																									<td>Total Amount</td>
																									<td>Currency</td>
																									<td>Date</td>
																									<td>Perspective</td>
																								</tr>
																								<logic:iterate name="regionalFunding" 
																								property="commitments" id="commitment" 
																								type="org.digijava.module.aim.helper.FundingDetail"> 
																								<!-- L2 START-->
																								<tr bgcolor="#ffffff">
																									<td><c:out value="${commitment.adjustmentTypeName}"/></td>
																									<td align="right">
																									<FONT color=blue>*</FONT>
																									<c:out value="${commitment.transactionAmount}"/></td>
																									<td><c:out value="${commitment.currencyCode}"/></td>
																									<td><c:out value="${commitment.transactionDate}"/></td>
																									<td>
																										<digi:trn key='<%="aim:"+commitment.getPerspectiveNameTrimmed() %>'>
																											<bean:write name="commitment" property="perspectiveName"/>
																										</digi:trn>
																									
																									</td>
																								</tr>																			
																								</logic:iterate>	<!-- L2 END-->
																							</table>	
																						</td></tr>
																					</logic:notEmpty>
																					<logic:notEmpty name="regionalFunding" property="disbursements">
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<td>Actual/Planned</td>
																									<td>Total Amount</td>
																									<td>Currency</td>
																									<td>Date</td>
																									<td>Perspective</td>
																								</tr>
																								<logic:iterate name="regionalFunding" 
																								property="disbursements" id="disbursement" 
																								type="org.digijava.module.aim.helper.FundingDetail">
																								<!-- L3 START-->
																								<tr bgcolor="#ffffff">
																									<td><c:out value="${disbursement.adjustmentTypeName}"/>
																									</td>
																									<td align="right">
																									<FONT color=blue>*</FONT>
																									<c:out value="${disbursement.transactionAmount}"/>
																									</td>
																									<td><c:out value="${disbursement.currencyCode}"/></td>
																									<td><c:out value="${disbursement.transactionDate}"/></td>
																									<td>
																										<digi:trn key='<%="aim:"+disbursement.getPerspectiveNameTrimmed() %>'>
																											<bean:write name="disbursement" property="perspectiveName"/>
																										</digi:trn>
																									</td>
																								</tr>																			
																								</logic:iterate>	<!-- L3 END-->
																							</table>																		
																						</td></tr>															
																					</logic:notEmpty>
																					<logic:notEmpty name="regionalFunding" property="expenditures">
																						<tr><td>
																							<b>
																							<digi:trn key="aim:expenditures">Expenditures</digi:trn> - (
																							<digi:trn key="aim:totalActualToDate">Total actual to date
																							</digi:trn> = 
																							<c:out value="${aimEditActivityForm.totalExpenditures}"/> 
																							<c:out value="${aimEditActivityForm.currCode}"/>)
																							</b>
																						</td></tr>
																						<tr><td bgcolor=#ffffff>
																							<table width="100%" cellSpacing=1 cellPadding=3 border=0
																							bgcolor="#eeeeee">
																								<tr>
																									<td>Actual/Planned</td>
																									<td>Total Amount</td>
																									<td>Currency</td>
																									<td>Date</td>
																									<td>Perspective</td>
																								</tr>
																								<logic:iterate name="regionalFunding" 
																								property="expenditures" id="expenditure" 
																								type="org.digijava.module.aim.helper.FundingDetail">
																								<!-- L4 START-->
																								<tr bgcolor="#ffffff">
																									<td><c:out value="${expenditure.adjustmentTypeName}"/>
																									</td>
																									<td align="right">
																									<FONT color=blue>*</FONT>
																									<c:out value="${expenditure.transactionAmount}"/>
																									</td>
																									<td><c:out value="${expenditure.currencyCode}"/></td>
																									<td><c:out value="${expenditure.transactionDate}"/></td>
																									<td>
																										<digi:trn key='<%="aim:"+expenditure.getPerspectiveNameTrimmed() %>'>
																											<bean:write name="expenditure" property="perspectiveName"/>
																										</digi:trn>
																									</td>
																								</tr>
																								</logic:iterate>	<!-- L4 END-->
																							</table>
																						</td></tr>
																					</logic:notEmpty>
																					</table>
																				</td></tr>
																				</logic:iterate>	<!-- L1 END-->
																				<TR><TD>
																					<FONT color=blue>*
																						<digi:trn key="aim:allTheAmountsInThousands">	
																							All the amounts are in thousands (000)
				  																		</digi:trn>
																					</FONT>								
																				</TD></TR>
																			</logic:notEmpty>
																			<logic:notEmpty name="aimEditActivityForm" property="fundingRegions">
																			<logic:empty name="aimEditActivityForm" property="regionalFundings">
																				<!-- No fundings -->
																				<tr><td>
																					<input type="button" value="Add Fundings" class="buton" 
																					onclick="addRegionalFunding()">
																				</td></tr>														
																			</logic:empty>
																			<logic:notEmpty name="aimEditActivityForm" property="regionalFundings">
																				<tr><td bgcolor=#ffffff>
																					<table cellSpacing=2 cellPadding=2>
																						<tr>
																							<td>
																								<input type="button" value="Add Fundings" 
																								class="buton" onclick="addRegionalFunding()">
																							</td>
																							<td>
																								<input type="button" value="Remove Fundings"
																								class="buton" onclick="removeRegFundings()">
																							</td>
																						</tr>
																					</table>
																				</td></tr>															
																			</logic:notEmpty>
																			</logic:notEmpty>
																			<logic:empty name="aimEditActivityForm" property="fundingRegions">
																				<tr><td align="center" class="red-log">
																					<digi:trn key="aim:noRegionsSelected">No regions selected
																					</digi:trn>
																				</td></tr>
																			</logic:empty>
																			</table>	
																		</td>
																	</tr>
																	

																	<tr><td bgColor=#f4f4f2 align="center">
																		<table cellPadding=3>
																			<tr>
																				<td>
																					<input type="submit" value=" << Back " class="dr-menu" 
																					onclick="gotoStep(3)">
																				</td>
																				<td>
																					<input type="submit" value="Next >> " class="dr-menu" 
																					onclick="gotoStep(5)">
																				</td>
																				<td>
																					<input type="reset" value="Reset" class="dr-menu" 
																					onclick="return resetAll()">
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
									<td width="25%" vAlign="top" align="right">
										<!-- edit activity form menu -->
											<jsp:include page="editActivityMenu.jsp" flush="true" />
										<!-- end of activity form menu -->							
									</td></tr>									
								</table>
							</td></tr>
							<tr><td>
								&nbsp;
							</td></tr>
						</table>
					</td>
					<td width="10">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>	
</digi:form>
