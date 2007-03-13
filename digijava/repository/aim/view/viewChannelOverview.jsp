<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>	
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<script type="text/javascript">
	
function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~activityId=" + id;
	document.aimChannelOverviewForm.target = "_self";    
   document.aimChannelOverviewForm.submit();
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimChannelOverviewForm.target = "_self";
   document.aimChannelOverviewForm.submit();
}

function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimChannelOverviewForm.action = "<%=addUrl%>";
    document.aimChannelOverviewForm.submit();
}

function fnDeleteProject()
{
	var name=confirm("Are you sure you want to delete the record")
	if (name==true)
	{
		<digi:context name="addUrl" property="context/module/moduleinstance/deleteAmpActivity.do" />
	    document.aimChannelOverviewForm.action = "<%=addUrl%>";
		document.aimChannelOverviewForm.submit();
	}
	else
	{
		<digi:context name="addUrl" property="context/module/moduleinstance/viewChannelOverview.do" />
	    document.aimChannelOverviewForm.action = "<%=addUrl%>";
		document.aimChannelOverviewForm.submit();

	}
}

function commentWin(val) {
	if (document.aimChannelOverviewForm.currUrl1.value == "") { 		  
		openNewWindow(600, 400);
		<digi:context name="commurl" property="context/module/moduleinstance/viewComment.do" />
		url = "<%=commurl %>?comment=" + "viewccd" + "&actId=" + val;
		document.aimChannelOverviewForm.action = url;
		document.aimChannelOverviewForm.currUrl1.value = "<%=commurl %>";
		document.aimChannelOverviewForm.target = popupPointer.name;
		document.aimChannelOverviewForm.submit();
	} else
		popupPointer.focus();
}

</script>



<digi:form action="/viewChannelOverview.do"
	name="aimChannelOverviewForm"
	type="org.digijava.module.aim.form.ChannelOverviewForm" method="post">
	<digi:instance property="aimChannelOverviewForm" />

	<html:hidden property="id" />
	<input type="hidden" name="currUrl1">

	<logic:equal name="aimChannelOverviewForm" property="validLogin"
		value="false">
		<h3 align="center">Invalid Login. Please Login Again.</h3>
		<p align="center"><html:submit styleClass="dr-menu" value="Log In"
			onclick="login()" /></p>
	</logic:equal>

<logic:equal name="aimChannelOverviewForm" property="validLogin" value="true">
................................
<TABLE cellSpacing=0 cellPadding=0 align="left" vAlign="top" border=0 width=770>
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

				<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top"
					align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4"><bean:define id="activity"
							property="activity" name="aimChannelOverviewForm" />

						<TABLE width="100%" cellSpacing=1 cellPadding=3 vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
								<TD align=left>
								<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
									vAlign="top">
									<TR>
										<TD align="left"><SPAN class=crumb> <jsp:useBean
											id="urlChannelOverview" type="java.util.Map"
											class="java.util.HashMap" /> <c:set
											target="${urlChannelOverview}" property="ampActivityId">
											<bean:write name="aimChannelOverviewForm"
												property="ampActivityId" />
										</c:set> <c:set target="${urlChannelOverview}"
											property="tabIndex" value="0" /> <bean:define
											id="translation">
											<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>
										</bean:define> <digi:link href="/viewChannelOverview.do"
											name="urlChannelOverview" styleClass="comment"
											title="<%=translation%>">
											<digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
										</digi:link> &nbsp;&gt;&nbsp; Overview&nbsp;&gt;&nbsp;<bean:write
											name="aimChannelOverviewForm" property="perspective" />&nbsp;
										Perspective </SPAN></TD>
										<TD align="right"><input type="button" value="Preview"
											class="dr-menu"
											onclick="preview(<c:out value="${activity.activityId}"/>)"> <c:if
											test="${aimChannelOverviewForm.buttonText == 'edit'}">
											<input type="button" value="Edit" class="dr-menu"
												onclick="fnEditProject(<c:out value="${activity.activityId}"/>)">
										</c:if> <c:if
											test="${aimChannelOverviewForm.buttonText == 'validate'}">
											<input type="button" value="Validate" class="dr-menu"
												onclick="fnEditProject(<c:out value="${activity.activityId}"/>)">
										</c:if> <c:if
											test="${aimChannelOverviewForm.buttonText == 'approvalAwaited'}">
											<input type="button" value="Approval Awaited" class="dr-menu"
												disabled>
										</c:if></TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<%--
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="left" width="100%">
						<TABLE width="100%" cellPadding=2 cellSpacing=2 vAlign="top" align="left" bgColor=#f4f4f2>
							<TR>
								<TD width="100%">
									<IMG  height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
									<b><digi:trn key="aim:objectives">Objectives</digi:trn></b>
								</TD>
							</TR>
							<TR>
								<TD width="100%">
									<c:out value="${activity.objective}" />
									<c:if test="${activity.objMore == true}"> 
										<bean:define id="translation">
											<digi:trn key="aim:clickToViewMore">Click here to view more</digi:trn>
										</bean:define>
										<digi:link href="/viewChannelOverviewObjective.do" paramName="aimChannelOverviewForm" 
										paramId="ampActivityId" paramProperty="ampActivityId" title="<%=translation%>" >
											<digi:trn key="aim:more">more...</digi:trn>
										</digi:link>									
									</c:if>
								</TD>
							</TR>							
						</TABLE>
					</TD>
				</TR>				
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="left" width="100%">
						<TABLE width="100%" cellPadding=2 cellSpacing=2 vAlign="top" align="left" bgColor=#f4f4f2>
							<TR>
								<TD width="100%">
									<IMG  height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>								
									<b>
									<digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn>:</b>&nbsp;
									<c:out value="${activity.modality}"/>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				--%>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%"><html:errors /></TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="left" width="100%">
								<TABLE width="100%" cellPadding=2 cellSpacing=2 vAlign="top"
									align="left" bgColor=#f4f4f2>
									<TR>
										<TD width="100%"><IMG height=10
											src="../ampTemplate/images/arrow-014E86.gif" width=15> <c:if
											test="${activity.status == 'Planned'}">
											<b><digi:trn key="aim:plannedCommitment">
										Planned Commitment</digi:trn> : </b>
										</c:if> <c:if test="${activity.status != 'Planned'}">
											<b><digi:trn key="aim:totalCostOfActivity">
										Total Cost Of Activity</digi:trn> : </b>
										</c:if> <bean:write name="aimChannelOverviewForm"
											property="grandTotal" /> <bean:write
											name="aimChannelOverviewForm" property="currCode" /> <FONT
											color="blue">( <digi:trn key="aim:enteredInThousands">Entered in thousands 000</digi:trn>)</FONT>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%">
								<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top"
									align="center" bgColor=#f4f4f2>
									<TR>
										<TD width="100%" bgcolor="#F4F4F2" height="17">
										<TABLE border="0" cellpadding="0" cellspacing="0"
											bgcolor="#F4F4F2" height="17">
											<TR bgcolor="#F4F4F2" height="17">
												<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;<digi:trn
													key="aim:details">Details</digi:trn></TD>
												<TD><IMG src="../ampTemplate/images/corner-r.gif" width="17"
													height="17"></TD>
											</TR>
										</TABLE>
										</TD>
									</TR>
									<TR>
										<TD width="100%" bgcolor="#F4F4F2" align="center">
										<TABLE width="100%" cellPadding="2" cellSpacing="2"
											vAlign="top" align="center" bgColor=#f4f4f2
											class="box-border-nopadding">
											<TR>
												<TD width="50%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="3" cellSpacing="1"
													vAlign="top" align="left">
													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:projectIds">Project Ids</digi:trn></b>
																</TD>
															</TR>
															<c:if test="${empty activity.projectIds}">
																<TR>
																	<TD bgcolor="#ffffff">&nbsp;</TD>
																</TR>
															</c:if>
															<c:if test="${!empty activity.projectIds}">
																<c:forEach var="pId" items="${activity.projectIds}">
																	<TR>
																		<TD bgcolor="#ffffff"><c:out value="${pId.internalId}" />&nbsp;
																		</TD>
																	</TR>
																</c:forEach>
															</c:if>
														</TABLE>
														</TD>
													</TR>
													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:status">Status</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><c:out value="${activity.status}" />
																</TD>
															</TR>															
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn key="aim:reason">Reason</digi:trn></i>:
																<c:out value="${activity.statusReason}" /></TD>
															</TR>
														</TABLE>
														</TD>
													</TR>
													<TR>
														<TD>
															<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR><TD bgcolor="#eeeeee" height="18">&nbsp;
																	<IMG  height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
																	<b><digi:trn key="aim:actBudget">Budget</digi:trn></b>
																</TD></TR>
																<TR><TD bgcolor="#ffffff">
										<logic:equal name="activity" property="budget" value="true">
										<digi:trn key="aim:actBudgeton">
												Activity is On Budget
										</digi:trn>
										</logic:equal>
										<logic:equal name="activity" property="budget" value="false">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>		
										<logic:equal name="activity" property="budget" value="">
										<digi:trn key="aim:actBudgetoff">
												Activity is Off Budget
										</digi:trn>
										</logic:equal>
																</TD></TR>
																														
															</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:sector">Sector</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff">
																<ul>
																	<c:forEach var="actSect" items="${activity.sectors}">
																		<li><c:out value="${actSect.sectorName}" />&nbsp;&nbsp;
																			<logic:present name="actSect" property="sectorPercentage">
																			<c:if test="${actSect.sectorPercentage!=0}">
																				(<c:out value="${actSect.sectorPercentage}" />%)
																			</c:if>
																		</logic:present>
																		</li>
																		<c:if test="${!empty actSect.subsectorLevel1Name}">
																			<li><IMG src="../ampTemplate/images/link_out_bot.gif">
																			<c:out value="${actSect.subsectorLevel1Name}" />&nbsp;
																			</li>
																			<c:if test="${!empty actSect.subsectorLevel2Name}">
																				<li>&nbsp;&nbsp;&nbsp;&nbsp; <IMG
																					src="../ampTemplate/images/link_out_bot.gif"> <c:out
																					value="${actSect.subsectorLevel2Name}" /> &nbsp;</li>
																			</c:if>
																		</c:if>

																	</c:forEach>
																</ul>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:location">Location</digi:trn></b>
																</TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff">
																<TABLE width="100%" cellSpacing="0" cellPadding="0"
																	vAlign="top" align="left" bgcolor="#ffffff">
																	<TR>
																		<TD>
																		<TABLE width="100%" cellSpacing="1" cellPadding="2"
																			vAlign="top" align="left" bgcolor="#dddddd">
																			<TR>
																				<TD width="100%" colspan="3" align="left"
																					bgcolor="#ffffff"><i><digi:trn key="aim:impLevel">
																						Implementation Level</digi:trn></i>: &nbsp; <c:out
																					value="${activity.impLevel}" /></TD>
																			</TR>
																			<c:if test="${!empty activity.locations}">
																				<TR>
																					<TD width="33%" align="center" bgcolor="#ffffff"><digi:trn
																						key="aim:region">
																						Region</digi:trn></TD>
																					<TD width="33%" align="center" bgcolor="#ffffff"><digi:trn
																						key="aim:zone">
																							Zone
																						</digi:trn></TD>
																					<TD width="33%" align="center" bgcolor="#ffffff"><digi:trn
																						key="aim:woreda">
																							Woreda
																						</digi:trn></TD>
																				</TR>
																				<c:forEach var="loc" items="${activity.locations}">
																					<TR>
																						<TD width="33%" align="left" bgcolor="#ffffff"><c:out
																							value="${loc.region}" /></TD>
																						<TD width="33%" align="left" bgcolor="#ffffff"><c:out
																							value="${loc.zone}" /></TD>
																						<TD width="33%" align="left" bgcolor="#ffffff"><c:out
																							value="${loc.woreda}" /></TD>
																					</TR>
																				</c:forEach>
																			</c:if>
																		</TABLE>
																		</TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>
													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:program">Program</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><c:out value="${activity.program}" />
																</TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:programDescription">Description</digi:trn></i>:
																<c:out value="${activity.programDescription}" /></TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:donorFundingContactInformation">
																		Donor funding Contact Information
																	</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																	value="${activity.contFirstName}" />&nbsp; <c:out
																	value="${activity.contLastName}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:contactPersonEmail">Email</digi:trn></i>: <bean:define
																	id="mailTo">
																		mailto:<c:out value="${activity.email}" />
																</bean:define> <a href="<%=mailTo%>"> <c:out
																	value="${activity.email}" /></a></TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:mofedContactInformation">
																	MOFED Contact Information</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:contactPersonName">Name</digi:trn></i>: <c:out
																	value="${activity.mfdContFirstName}" />&nbsp; <c:out
																	value="${activity.mfdContLastName}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:contactPersonEmail">Email</digi:trn></i>: <bean:define
																	id="mailTo">
																		mailto:<c:out value="${activity.mfdContEmail}" />
																</bean:define> <a href="<%=mailTo%>"> <c:out
																	value="${activity.mfdContEmail}" /></a></TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

												</TABLE>
												</TD>
												<TD width="50%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="2" cellSpacing="2"
													vAlign="top" align="left">
													<TR>
														<TD>
														<TABLE width="100%" cellPadding=0 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:relatedOrganizations">Related Organizations</digi:trn></b>
																</TD>
															</TR>
															<TR>
																<TD bgcolor="#fffff">
																<TABLE width="100%" cellSpacing="2" cellPadding="2"
																	vAlign="top" align="center" bgcolor="#ffffff">
																	<TR>
																		<TD>
																		<TABLE width="100%" cellSpacing="1" cellPadding="2"
																			vAlign="top" align="left" bgcolor="#dddddd">
																			<TR>
																				<TD bgcolor="#ffffff"><b><digi:trn
																					key="aim:executingAgencies">
																					Executing Agencies</digi:trn></b></TD>
																			</TR>
																			<TR>
																				<TD bgcolor="#ffffff"><c:if
																					test="${!empty activity.relOrgs}">
																					<c:forEach var="relOrgs"
																						items="${activity.relOrgs}">
																						<c:if test="${relOrgs.role == 'EA'}">
																							<li><c:out value="${relOrgs.orgName}" /><br>
																						</c:if>
																					</c:forEach>
																				</c:if></TD>
																			</TR>
																		</TABLE>
																		</TD>
																	</TR>
																	<TR>
																		<TD>
																		<TABLE  cellSpacing="1" cellPadding="2"
																			vAlign="top" align="left" bgcolor="#dddddd">
																			<TR>
																				<TD bgcolor="#ffffff" colspan="2"><b><digi:trn
																					key="aim:fundingCountryAgency">
																						Funding Country/Agency</digi:trn></b></TD>
																			</TR>
																			<TR>
																				<TD bgcolor="#ffffff"><c:if
																					test="${!empty activity.relOrgs}">
																					<c:forEach var="relOrg"
																						items="${activity.relOrgs}">
																						<c:if test="${relOrg.role == 'DN'}">
																							<bean:define id="currentOrg" name="relOrg"
																									type="org.digijava.module.aim.helper.RelOrganization" 
																									toScope="request" />
																									<jsp:include page="organizationPopup.jsp"/>
																						</c:if>
																					</c:forEach>
																				</c:if></TD>
																				<td bgcolor="#ffffff">&nbsp;</td>
																			</TR>
																		</TABLE>
																		</TD>
																	</TR>
																	<TR>
																		<TD>
																		<TABLE width="100%" cellSpacing="1" cellPadding="2"
																			vAlign="top" align="left" bgcolor="#dddddd">
																			<TR>
																				<TD bgcolor="#ffffff"><b><digi:trn
																					key="aim:implementingAgency">
																						Implementing Agency</digi:trn></b></TD>
																			</TR>
																			<TR>
																				<TD bgcolor="#ffffff"><c:if
																					test="${!empty activity.relOrgs}">
																					<c:forEach var="relOrg"
																						items="${activity.relOrgs}">
																						<c:if test="${relOrg.role == 'IA'}">
																							<li><bean:define id="currentOrg" name="relOrg"
																									type="org.digijava.module.aim.helper.RelOrganization" 
																									toScope="request" />
																									<jsp:include page="organizationPopup.jsp"/>
																						</c:if>
																					</c:forEach>
																				</c:if></TD>
																			</TR>
																		</TABLE>
																		</TD>
																	</TR>
																	<TR>
																		<TD>
																		<TABLE width="100%" cellSpacing="1" cellPadding="2"
																			vAlign="top" align="left" bgcolor="#dddddd">
																			<TR>
																				<TD bgcolor="#ffffff"><b><digi:trn
																					key="aim:contractors">
																						Contractors</digi:trn></b></TD>
																			</TR>
																			<TR>
																				<TD bgcolor="#ffffff"><c:if
																					test="${!empty activity.contractors}">
																					<li><c:out value="${activity.contractors}" />
																				</c:if></TD>
																			</TR>
																		</TABLE>
																		</TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:keyActivityDates">Key Activity Dates</digi:trn></b>
																</TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><digi:trn
																	key="aim:originalApprovalDate">
																	Original Approval Date</digi:trn> : <c:out
																	value="${activity.origAppDate}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><digi:trn
																	key="aim:revisedApprovalDate">
																	Revised Approval Date</digi:trn> : <c:out
																	value="${activity.revAppDate}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><digi:trn
																	key="aim:originalStartDate">
																	Original Start Date</digi:trn> : <c:out
																	value="${activity.origStartDate}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><digi:trn
																	key="aim:revisedStartDate">
																	Revised Start Date</digi:trn> : <c:out
																	value="${activity.revStartDate}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><digi:trn
																	key="aim:currentCompletionDate">
																	Current Completion Date</digi:trn> : <c:out
																	value="${activity.currCompDate}" /> &nbsp; <a
																	href="javascript:commentWin('<c:out value="${activity.activityId}" />')">
																<digi:trn key="aim:comment">Comment</digi:trn></a></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff">
																<TABLE width="100%" cellspacing=0 cellpadding=0
																	valign=top align=left>
																	<TR>
																		<TD width="170" valign=top><digi:trn
																			key="aim:proposedCompletionDates">Proposed Completion Dates
																			</digi:trn> :</TD>
																		<TD>
																		<TABLE cellPadding=0 cellSpacing=0>
																			<c:forEach var="closeDate"
																				items="${activity.revCompDates}">
																				<TR>
																					<TD><c:out value="${closeDate}" /></TD>
																				</TR>
																			</c:forEach>
																		</TABLE>
																		</TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:typeOfAssistance">
																	Type Of Assistance</digi:trn></b></TD>
															</TR>
															<c:if test="${!empty activity.assistanceType}">
																<c:forEach var="asstType"
																	items="${activity.assistanceType}">
																	<TR>
																		<TD bgcolor="#ffffff"><c:out value="${asstType}" /></TD>
																	</TR>
																</c:forEach>
															</c:if>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=2 cellSpacing=1
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:financingInstruments">
																	Financing Instruments</digi:trn></b></TD>
															</TR>
															<c:if test="${!empty activity.uniqueModalities}">

																<c:forEach var="modal" items="${activity.uniqueModalities}">
																	<TR>
																		<TD bgcolor="#ffffff"><c:out value="${modal.name}" />
																		</TD>
																	</TR>
																</c:forEach>
															</c:if>
														</TABLE>
														</TD>
													</TR>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellSpacing=1
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG height=10
																	src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<b><digi:trn key="aim:activityCreationDetails">
																	Activity creation details</digi:trn></b></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn key="aim:createdBy">Created By</digi:trn></i>:
																<c:out value="${activity.actAthFirstName}" />&nbsp; <c:out
																	value="${activity.actAthLastName}" /></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn key="aim:email">Email</digi:trn></i>:
																<bean:define id="mailTo">
																		mailto:<c:out value="${activity.actAthEmail}" />
																</bean:define> <a href="<%=mailTo%>"> <c:out
																	value="${activity.actAthEmail}" /></a></TD>
															</TR>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn key="aim:createdDate">Created date</digi:trn></i>:
																<c:out value="${activity.createdDate}" />&nbsp;</TD>
															</TR>
															<c:if test="${!empty activity.updatedBy}">
															<TR>
																<TD bgcolor="#ffffff">
																<i>
																<digi:trn key="aim:activityUpdatedBy">
																	Activity updated by</digi:trn></i>: 
																	<c:out value="${activity.updatedBy.user.firstNames}"/>										
																	<c:out value="${activity.updatedBy.user.lastName}"/>	-
																	<c:out value="${activity.updatedBy.user.email}"/>
																	
															</TR>
															</c:if>
															<c:if test="${!empty activity.updatedDate}">
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn key="aim:activityUpdatedOn">
																Activity updated on</digi:trn></i>:
																<c:out value="${activity.updatedDate}"/>
															&nbsp;</TD>
															</TR>
															</c:if>
														</TABLE>
														</TD>
													</TR>

												</TABLE>
												</TD>
											</TR>
										</TABLE>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>&nbsp;</TD>
							</TR>

						</TABLE>

						</TD>
					</TR>

				</TABLE>
				<!-- end --></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>

	</logic:equal>
</digi:form>
