<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants" %>
<%@ page import="org.digijava.module.aim.util.DynLocationManagerUtil" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@page import="org.digijava.module.aim.helper.Constants"%>


<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script type="text/javascript">

function fnEditProject1(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimChannelOverviewForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~debugFM=true~activityId=" + id + "~actId=" + id;
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
<c:set var="translation">
	<digi:trn key="aim:deleteRecord">Are you sure you want to delete the record</digi:trn>
</c:set>
	var name=confirm("${translation}")
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

function commentWin(val, commentId) {
	openNewWindow(600, 400);
	<digi:context name="commurl" property="context/module/moduleinstance/viewComment.do" />
	url = "<%=commurl %>~comment=view" + commentId + "~previus=vco~actId=" + val;
	document.aimChannelOverviewForm.action = url;
	document.aimChannelOverviewForm.currUrl1.value = "<%=commurl%>";
	document.aimChannelOverviewForm.target = popupPointer.name;
	document.aimChannelOverviewForm.submit();
}

</script>


<digi:instance property="aimChannelOverviewForm" />
<digi:form action="/viewChannelOverview.do"
	name="aimChannelOverviewForm"
	type="org.digijava.module.aim.form.ChannelOverviewForm" method="post">


	<html:hidden property="id" styleId="actId" />
	<input type="hidden" name="currUrl1">

	<logic:equal name="aimChannelOverviewForm" property="validLogin"
		value="false">
		<h3 align="center">Invalid Login. Please Login Again.</h3>
		<p align="center"><html:submit styleClass="dr-menu" value="Log In"
			onclick="login()" /></p>
	</logic:equal>
	<logic:equal name="aimChannelOverviewForm" property="validLogin"
		value="true">
        <c:if test="${not empty aimChannelOverviewForm.activity}">
		<TABLE cellspacing="0" cellpadding="0" align="left" vAlign="top" border="0"
			width="100%">
			<TR>
				<TD vAlign="top" align="center"><!-- contents -->

				<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top"
					align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4"><c:set var="activity"
							value="${aimChannelOverviewForm.activity}" />

						<TABLE width="100%" cellspacing="1" cellPadding=3 vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
								<TD align=left>
								<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
									vAlign="top">
									<TR bgColor=#ccdbff>
										<TD align="left">
										<SPAN><strong>
										<feature:display module="Project ID and Planning" name="Identification">
											<field:display name="Project Comments" feature="Identification">
											&nbsp;&nbsp;<div id="gen" title='<digi:trn>Click here to View Project Comments</digi:trn>'>
											<a href="#" onclick="javascript:previewOverviewframe('ProjCom','<c:out value="${activity.projectComments}"/>', '<digi:trn jsFriendly="true">Project Comments</digi:trn>'); return false;" >
											<digi:trn>Project Comments</digi:trn></a>&nbsp;|</div>&nbsp;
											</field:display>
											<field:display name="Description" feature="Identification">
											<div id="gen" title='<digi:trn>Click here to View Project Description</digi:trn>'>
											<a href="#" onclick="javascript:previewOverviewframe('Desc','<c:out value="${activity.description}"/>', '<digi:trn jsFriendly="true">Description</digi:trn>'); return false;" >
											<digi:trn>Description</digi:trn></a>&nbsp;|</div>&nbsp;
											</field:display>
											<field:display name="Objective" feature="Identification">
												<div id="gen" title='<digi:trn>Click here to View Project Objectives</digi:trn>'>
												<a href="#" onclick="javascript:previewOverviewframe('Obj','<c:out value="${activity.objective}"/>', '<digi:trn jsFriendly="true">Objectives</digi:trn>'); return false;" >
												<digi:trn>Objectives</digi:trn></a>&nbsp;|</div>&nbsp;
											</field:display>
											<field:display name="Purpose" feature="Identification">
											<div id="gen" title='<digi:trn>Click here to View Project Purpose</digi:trn>'>
											<a href="#" onclick="javascript:previewOverviewframe('Purp','<c:out value="${activity.purpose}"/>', '<digi:trn jsFriendly="true">Purpose</digi:trn>'); return false;" >
												<digi:trn>Purpose</digi:trn></a>&nbsp;|</div>&nbsp;
											</field:display>
											<field:display name="Results" feature="Identification">
											<div id="gen" title='<digi:trn>Click here to View Project Results</digi:trn>'>
											<a href="#" onclick="javascript:previewOverviewframe('Res','<c:out value="${activity.results}"/>', '<digi:trn jsFriendly="true">Results</digi:trn>'); return false;" >
												<digi:trn>Results</digi:trn></a></div>&nbsp;
											</field:display>
										</feature:display>

											<%--	<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Logframe" module="Previews">
														<field:display name="Logframe Preview Button" feature="Logframe">
															<div id="gen" title='<digi:trn key="logframeBtn:previewLogframe">Preview Logframe</digi:trn>'>
																<a href="#" onclick="javascript:previewLogframe(${aimChannelOverviewForm.id}); return false;">
																	<digi:trn key="logframeBtn:previewLogframe">Preview Logframe</digi:trn></a>&nbsp;|</div>&nbsp;
														</field:display>
													</feature:display>
												</module:display>

												<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Project Fiche" module="Previews">
														<field:display name="Project Fiche Button" feature="Project Fiche">
															<div id="gen" title='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>'>
																<a href="#" onclick='javascript:projectFiche(${aimChannelOverviewForm.id}); return false;'>
															<digi:trn key="aim:projectFiche">Project Fiche</digi:trn></a></div>
														</field:display>
													</feature:display>
												</module:display>--%>
											
											</strong>
										</SPAN>									
										</TD>
										
										<%--<TD align="right">
										<table>
											<tr>
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Preview Activity" module="Previews">
														<field:display feature="Preview Activity" name="Preview Button">
															<td> <a href="" target="_blank" onclick="javascript:preview(${aimChannelOverviewForm.id}); return false;" title="<digi:trn key='btn:preview'>Preview</digi:trn>"> 
																<img src="/repository/aim/images/magnifier.png" border="0"></a></td>
														</field:display>
													</feature:display>
												</module:display>
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Edit Activity" module="Previews">
														<field:display feature="Edit Activity" name="Edit Activity Button">
															 <td>              
																				<!-- 
                                                                               <c:if test="${aimChannelOverviewForm.buttonText != 'validate'}">              
                                                                               <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}"> 
                                                                                 -->  
                                                                                       <a href="" target="_blank" onclick="javascript:fnEditProject(${activity.ampActivityId}); return false;" title="<digi:trn key='btn:edit'>Edit</digi:trn>"> 
																							<img src="/repository/aim/images/application_edit.png" border="0"></a>

                                                                               <!--       
                                                                               </c:if>                                                                 
                                                                                     
                                                                               </c:if>
                                                                                --> &nbsp;                                                                
                                                                       </td>      
														</field:display>
													</feature:display>
												</module:display>
												
												<module:display name="Previews"
													parentModule="PROJECT MANAGEMENT">
													<feature:display name="Edit Activity" module="Previews">
														<field:display feature="Edit Activity" name="Validate Activity Button">
    														<c:if
																test="${aimChannelOverviewForm.buttonText == 'validate'}">
																 <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}"> 
																
																<td><html:button styleClass="dr-menu"
																	onclick="fnEditProject(${aimChannelOverviewForm.id})"
																	property="validateBtn">
																	<digi:trn key="aim:validate">Validate</digi:trn>
																</html:button></td>
 															</c:if>	

															</c:if>
														</field:display>
													</feature:display>
												</module:display>

											</tr>
										</table>
										</TD>--%>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<tr>
								<td>&nbsp;</td>
							</tr>

							<TR bgColor=#f4f4f2 >
								<TD vAlign="top" align="center" width="100%">
								<font color="#FF0000">
								<!--  FFerreyra: This was added to handle errors between different forms while keeping translation tags possible -->
								<logic:present name="<%=org.apache.struts.Globals.ERROR_KEY%>">
									<bean:define id="errors" name="<%=org.apache.struts.Globals.ERROR_KEY%>" type="org.apache.struts.action.ActionMessages"/>
									<ul>
										<logic:iterate collection="<%=errors.get()%>" id="error" type="org.apache.struts.action.ActionMessage">
										<li>
											<digi:trn key="${error.key}"><bean:message key="<%=error.getKey()%>"/></digi:trn>
										</li>
										</logic:iterate>
									</ul>
								</logic:present>
								
                            		<logic:iterate id="element" name="aimChannelOverviewForm" property="errors">
                               			<digi:trn key="${element.key}">
                                   			<bean:write name="element" property="value"/>                                   			
                               			</digi:trn>
                           			</logic:iterate>
                       				<logic:iterate id="element" name="aimChannelOverviewForm" property="messages">
                               			<digi:trn key="${element.key}">
                                   			<bean:write name="element" property="value"/>
                               		</digi:trn>
                           			</logic:iterate>
                   				</font>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="left" width="100%">
								<TABLE width="100%" cellPadding=2 cellSpacing=2 vAlign="top"
									align="left" bgColor=#f4f4f2>
									<TR>
										<TD width="100%"><IMG height=10
											src="../ampTemplate/images/arrow-014E86.gif" width=15>
                                            <c:set var="status">
                                            	${aimChannelOverviewForm.status}
                                            </c:set>
                                            
										<c:if test="${status == 'Planned'}">
											<b><digi:trn key="aim:plannedCommitment">
										Planned Commitment</digi:trn> : </b>
										</c:if> 
                                        <c:if test="${status != 'Planned'}">
											<b><digi:trn key="aim:totalCommitmentsActual">
										Total Commitments(Actual)</digi:trn> : </b>
										</c:if> ${aimChannelOverviewForm.grandTotal}
										${aimChannelOverviewForm.currCode} 

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
										<FONT color="blue">
										( <digi:trn key="aim:enteredInThousands">Entered in thousands 000</digi:trn>)
										</FONT>
</gs:test>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%">
								<TABLE width="98%" cellpadding="0" cellspacing="0" vAlign="top"
									align="center" bgColor=#f4f4f2>

									<TR>
										<TD width="100%" bgcolor="#F4F4F2" align="center">
										<TABLE width="100%" cellPadding="2" cellSpacing="2"
											vAlign="top" align="center" bgColor=#f4f4f2
											class="box-border-nopadding">
											<TR>
												<TD width="49%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="3" cellSpacing="1"
													vAlign="top" align="left">
													
													<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
														<feature:display name="Planning" module="Project ID and Planning">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellspacing="1"
																	vAlign="top" align="center" bgcolor="#FFFFFF">
																	<field:display name="AMP ID" feature="Identification">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18" colspan="2">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<b>
																	  		<digi:trn key="aim:ampId">AMP ID</digi:trn></b>
																	  	</td>
																	  </TR>
																	<TR>
																		<TD bgcolor="#ffffff" colspan="2">&nbsp;&nbsp;&nbsp;
																	  		<c:out value="${activity.ampId}" />
																		</td>
																	</TR>
																	</field:display> 
																	<field:display name="Organizations and Project ID" feature="Identification">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18" colspan="2">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b>
																		<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn></b>
																	  </td></TR>
																		<c:forEach items="${aimChannelOverviewForm.selectedOrganizations}" var="selectedOrganizations">
																		<tr>
																			<c:if test="${!empty selectedOrganizations.id}">
																				<td width="75%">
																					<li><i><c:out value="${selectedOrganizations.organisation.name}"/></i></li>
																			  </td>
																	
																				<td width="25%" align="center">
																					<i>
																				  <c:out value="${selectedOrganizations.projectId}"/>
																					</i></td>
																			</c:if>
																		</tr>
																	</c:forEach>
																	</field:display>
																</TABLE>
																</TD>
															</TR>

															<field:display name="Status" feature="Identification">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="top" bgcolor="#FFFFFF">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:status">Status</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><c:out
																				value="${status}" /></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><i><digi:trn
																				key="aim:reason">Reason</digi:trn></i>: <c:out
																				value="${activity.statusReason}" /></TD>
																		</TR>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
														<feature:display name="Budget" module="Project ID and Planning">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:actBudget">Budget</digi:trn></b>
																			</TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
																				<table>
																				<c:if test="${aimChannelOverviewForm.budgetCV != null && 
																						aimChannelOverviewForm.budgetCV.value=='On'}">
																					<field:display name="On/Off/Treasury Budget" feature="Budget">
																						<tr>
																							<td>
																								<digi:trn key="aim:actBudgeton">Activity is On Budget</digi:trn>
																							</td>
																						</tr>

																						<logic:present name="activity" property="chapter">
																						<tr>
																							<td>&nbsp;</td>
																						</tr>
																						<tr>
																							<td><digi:trn>Code Chapitre</digi:trn>: ${activity.chapter.code} - ${activity.chapter.description}</td>
																						</tr>
																						<tr>
																							<td>
																								<digi:trn>Imputations</digi:trn>:
																							</td>
																						</tr>
																						<tr>																						
																							<td>
																							<logic:iterate name="activity" property="chapter.imputations" id="imp">
																								${activity.chapter.year} - ${imp.code} - ${imp.description}
																							<br/> 
																							</logic:iterate>
																						</td>
																						</tr>
																								<tr>
																							<td>&nbsp;</td>
																						</tr>
																						</logic:present>
																						<field:display name="FY" feature="Budget">
																							<tr>
																								<td>
																									<digi:trn key="aim:actFY">FY</digi:trn>: ${activity.FY}
																								</td>
																							</tr>
																						</field:display>
																						<field:display name="Vote" feature="Budget">
																							<tr>
																								<td>
																									<digi:trn key="aim:actVote">Vote</digi:trn>: ${activity.vote}
																								</td>
																							</tr>
																						</field:display>
																						<field:display name="Sub-Vote" feature="Budget">
																							<tr>
																								<td>
																									<digi:trn key="aim:actSub-Vote">Sub-Vote</digi:trn>: ${activity.subVote}
																								</td>
																							</tr>
																						</field:display>
																						<field:display name="Sub-Program" feature="Budget">
																							<tr>
																								<td>
																									<digi:trn key="aim:actSub-Program">Sub-Program</digi:trn>: ${activity.subProgram}
																								</td>
																							</tr>
																						</field:display>
																						<field:display name="Project Code" feature="Budget">
																							<tr>
																								<td>
																									<digi:trn key="aim:actProjectCode">Project Code</digi:trn>: ${activity.projectCode}
																								</td>
																							</tr>
																						</field:display>
																					</field:display>
																					
																					<field:display name="Financial Instrument" feature="Budget">
																						<tr>
																							<td>
																								<digi:trn key="aim:actGBS">Financial Instrument</digi:trn>: ${aimChannelOverviewForm.financialInstrument}
                                                                                            </td>
																						</tr>
																					</field:display>
																					<field:display name="Government Approval Procedures" feature="Budget">
																						<tr>
																							<td>
																								<digi:trn key="aim:actGovernmentApprovalProcedures">Government Approval Procedures </digi:trn>: 
																								<c:if test="${activity.governmentApprovalProcedures==true}">
																									Yes
																								</c:if>
																								<c:if test="${activity.governmentApprovalProcedures==false || activity.governmentApprovalProcedures==''}">
																									No
																								</c:if>
																							</td>
																						</tr>
																					</field:display>
																					<field:display name="Joint Criteria" feature="Budget">
																						<tr>
																							<td>
																								<digi:trn key="aim:actJointCriteria">Joint Criteria</digi:trn>:
																							 	<c:if test="${activity.jointCriteria==true}">
																									<c:out value="Yes" />
																								</c:if> 
																								<c:if test="${activity.jointCriteria==false || activity.jointCriteria==''}">
																									<c:out value="No" />
																								</c:if>
																							</td>
																						</tr>
																					</field:display>
																					<field:display name="Humanitarian Aid" feature="Identification">
																						<tr><td><digi:trn key="aim:humanitarianaid">Humanitarian Aid</digi:trn>: 
																								<c:if test="${!activity.humanitarianAid==true}">
																									<digi:trn key="aim:no">No</digi:trn>
																								</c:if>
																								<c:if test="${activity.humanitarianAid==true}">
																									<digi:trn key="aim:yes">Yes</digi:trn>
																								</c:if></td></tr>
																					</field:display>
																					</c:if> 
																					<field:display name="Budget Classification" feature="Budget">
																						<tr>
																							<td style="margin-top: 10px;">
																								<strong>
																									<digi:trn>Budget Classification</digi:trn>
																								</strong>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<logic:notEmpty property="budgetsector" name="aimChannelOverviewForm">
																									<li style="margin-left: 10px"> ${aimChannelOverviewForm.budgetsector}</li>
																								</logic:notEmpty>
																								<logic:notEmpty property="budgetorganization" name="aimChannelOverviewForm">
																									<IMG src="../ampTemplate/images/link_out_bot.gif" style="margin-left: 20px"/>
																									${aimChannelOverviewForm.budgetorganization}<br>
																								</logic:notEmpty>
																								<logic:notEmpty property="budgetdepartment" name="aimChannelOverviewForm">
																									<IMG src="../ampTemplate/images/link_out_bot.gif" style="margin-left: 30px"/>
																									${aimChannelOverviewForm.budgetdepartment}<br>
																								</logic:notEmpty>
																								<logic:notEmpty property="budgetprogram" name="aimChannelOverviewForm">
																									<IMG src="../ampTemplate/images/link_out_bot.gif" style="margin-left: 40px"/>
																									${aimChannelOverviewForm.budgetprogram}<br>
																								</logic:notEmpty>
																							</td>
																						</tr>
																					</field:display>
																				</table>
																			<field:display name="On/Off/Treasury Budget" feature="Budget">
																			<c:choose>
																				<c:when test="${aimChannelOverviewForm.budgetCV!=null && aimChannelOverviewForm.budgetCV.value=='Off'}">
																					<digi:trn>Activity is Off Budget</digi:trn>
																				</c:when>
																				<c:when test="${aimChannelOverviewForm.budgetCV!=null && aimChannelOverviewForm.budgetCV.value=='On'}">
																				</c:when>
																				<c:when test="${aimChannelOverviewForm.budgetCV==null}">
																					<digi:trn>Budget Unallocated</digi:trn>
																				</c:when>
	                                                                            <c:otherwise>
																					<digi:trn>${aimChannelOverviewForm.budgetCV.value}</digi:trn>
																				</c:otherwise>
																			</c:choose>
																			</field:display>
																			</TD>
																		</TR>

																	</TABLE>
																	</TD>
																</TR>
														</feature:display>
														
														<feature:display name="Identification" module="Project ID and Planning">
																		<field:display name="Description" feature="Identification">
                                                                        <TR>
                                                                            <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                <IMG
                                                                                        height=10
                                                                                        src="../ampTemplate/images/arrow-014E86.gif"
                                                                                        width=15>
                                                                                        <b><digi:trn key="aim:programDescription">Description</digi:trn></b>
                                                                            </TD>
                                                                        </TR>
																		<TR>
																			<TD bgcolor="#ffffff">
																			<c:if test='${!empty activity.description}'>
																				<digi:edit key="${activity.description}" />
																			</c:if>
																			<br />
                                                                            </TD>
                                                                        </TR>
																		</field:display>
																		<field:display feature="Identification" name="Objectives">
                                                                        <TR>
                                                                            <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                <IMG
                                                                                        height=10
                                                                                        src="../ampTemplate/images/arrow-014E86.gif"
                                                                                        width=15>
                                                                                        <b><digi:trn key="aim:programObjective">
                                                                                        Objective
                                                                                    </digi:trn></b>
                                                                            </TD>
                                                                        </TR>
                                                                        <TR>
                                                                            <TD bgcolor="#ffffff">
																			<c:if test='${!empty activity.objective}'>
																				<digi:edit key="${activity.objective}" />
																			</c:if>
																		
																			<ul>
																				<c:forEach var="comments"
																					items="${aimChannelOverviewForm.allComments}">
																					<field:display feature="Identification" name="Objective Assumption">
																						<c:if test='${comments.key=="Objective Assumption"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																					<field:display feature="Identification" name="Objective Verification">
																						<c:if test='${comments.key=="Objective Verification"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn
																								key="aim:objectiveVerification">Objective Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																					<field:display feature="Identification" name="Objective Objectively Verifiable Indicators">
																						<c:if test='${comments.key=="Objective Objectively Verifiable Indicators"}'>
																							<c:forEach var="comment" items="${comments.value}">
																								<li><i><digi:trn key="aim:objectivelyindicatorspreview">
																								Objectively Verifiable Indicators
																								</digi:trn>:
																								</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																					</field:display>
																				</c:forEach>
																			</ul>
                                                                            </TD>
                                                                        </TR>
																		</field:display>

																		<field:display name="Lessons Learned" feature="Identification">
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn key="aim:Lessons Learned">Lessons Learned</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
																					<c:if test="${not empty activity.lessonsLearned && fn:trim(activity.lessonsLearned) ne ''}">
																						<bean:define id="lessonsLearnedKey">
																							<c:out value="${activity.lessonsLearned}"/>
																						</bean:define>
																						<digi:edit key="<%=lessonsLearnedKey%>"/>
																					 </c:if>
																				</TD>
																			</TR>
																		</field:display>
																		
                                                                        <bean:define id="largeTextFeature" value="Identification" toScope="request"/>
                                                                        <field:display name="Project Impact" feature="Identification">																		
                                                                            <c:if test="${not empty activity.projectImpact}">			
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Project Impact</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
                                                                                        <bean:define id="largeTextKey" toScope="request" name="activity" property="projectImpact"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
                                                                            </c:if>
                                                                        </field:display>
                                                                        <field:display name="Activity Summary" feature="Identification">
																		<c:if test="${not empty activity.activitySummary}">			
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Activity Summary</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
                                                                                        <bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
                                                                                        <bean:define id="largeTextKey" toScope="request" name="activity" property="activitySummary"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>
																		
																		<field:display name="Contracting Arrangements" feature="Identification">
                                                                        <c:if test="${not empty activity.contractingArrangements}">						
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Contracting Arrangements</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
				                                                                        <bean:define id="largeTextKey" toScope="request" name="activity" property="contractingArrangements"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>
																		<field:display name="Conditionality and Sequencing" feature="Identification">
																		<c:if test="${not empty activity.condSeq}">									
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Conditionality and Sequencing</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
																						<bean:define id="largeTextKey" toScope="request" name="activity" property="condSeq"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>
																		
																		<field:display name="Linked Activities" feature="Identification">
																		<c:if test="${not empty activity.linkedActivities}">												
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Linked Activities</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
                                                                                        <bean:define id="largeTextKey" toScope="request" name="activity" property="linkedActivities"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>
																		
																		<field:display name="Conditionalities" feature="Identification">
																		<c:if test="${not empty activity.conditionality}">									
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Conditionalities</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
																						<bean:define id="largeTextKey" toScope="request" name="activity" property="conditionality"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>
																		
																		<field:display name="Project Management" feature="Identification">
																		<c:if test="${not empty activity.projectManagement}">												
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15 />
                                                                                            <b><digi:trn>Project Management</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
																						<bean:define id="largeTextKey" toScope="request" name="activity" property="projectManagement"/>
                                                                                        <digi:edit key="${largeTextKey}"/>
																				</TD>
																			</TR>
																		</c:if>
																		</field:display>

																		<field:display feature="Identification"
																			name="Purpose">
																			<c:if test="${!empty activity.purpose}">
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15>
                                                                                            <b><digi:trn key="aim:programPurpose">Purpose</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
																				<digi:edit key="${activity.purpose}" />
																				<ul>
																					<c:forEach var="comments"
																						items="${aimChannelOverviewForm.allComments}">
																						<c:if test='${comments.key=="Purpose Assumption"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																						<c:if
																							test='${comments.key=="Purpose Verification"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:purposeVerification">Purpose Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
                                                                                        <c:if test='${comments.key=="Purpose Objectively Verifiable Indicators"}'>
                                                                                            <c:forEach var="comment" items="${comments.value}">
                                                                                                <li><i><digi:trn>
                                                                                                Purpose Objectively Verifiable Indicators
                                                                                                </digi:trn>:
                                                                                                </i>
                                                                                                ${comment.comment}</li>
                                                                                            </c:forEach>
                                                                                        </c:if>
																					</c:forEach>
																				</ul>
																				</TD>
																			</TR>
																			</c:if>
																		</field:display> 
                                                                        <field:display feature="Identification"
																			name="Results">

																			<c:if test="${!empty activity.results}">
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp;
                                                                                    <IMG
                                                                                            height=10
                                                                                            src="../ampTemplate/images/arrow-014E86.gif"
                                                                                            width=15>
                                                                                            <b><digi:trn key="aim:programResults">Results</digi:trn></b>
                                                                                </TD>
                                                                            </TR>
																			<TR>
																				<TD bgcolor="#ffffff">
	                                                                            <digi:edit key="${activity.results}" />
																				<ul>
																					<c:forEach var="comments"
																						items="${aimChannelOverviewForm.allComments}">
																						<c:if test='${comments.key=="Results Assumption"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:resultsAssumption">Results Assumption</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
																						<c:if
																							test='${comments.key=="Purpose Verification"}'>
																							<c:forEach var="comment"
																								items="${comments.value}">
																								<li><i><digi:trn
																									key="aim:resultsVerification">Results Verification</digi:trn>:</i>
																								${comment.comment}</li>
																							</c:forEach>
																						</c:if>
                                                                                        <c:if test='${comments.key=="Results Objectively Verifiable Indicators"}'>
                                                                                            <c:forEach var="comment" items="${comments.value}">
                                                                                                <li><i><digi:trn>
                                                                                                Results Objectively Verifiable Indicators
                                                                                                </digi:trn>:
                                                                                                </i>
                                                                                                ${comment.comment}</li>
                                                                                            </c:forEach>
                                                                                        </c:if>
																					</c:forEach>
																				</ul>
																				</TD>
																			</TR>
																			</c:if>
																		</field:display>
																</feature:display>
														
														
														
														<feature:display module="Project ID and Planning" name="Sectors">
															<field:display feature="Sectors" name="Level 1 Sectors List">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn key="aim:sector">Sector</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
												                            <c:forEach var="config" items="${aimChannelOverviewForm.classificationConfigs}" varStatus="ind">
																				<bean:define id="emptySector" value="Sector"></bean:define>
																				<field:display name="${config.name} Sector" feature="Sectors">
																				<c:set var="hasSectors">
																					false
																				</c:set>

																				<c:forEach var="actSect" items="${aimChannelOverviewForm.activitySectors}">
																					<c:if test="${actSect.configId==config.id}">
																						<c:set var="hasSectors">
																							true
																						</c:set>
																					</c:if>
																				</c:forEach>
																				<c:if test="${hasSectors}">
												                                <strong>
													                               	<digi:trn key="aim:addactivitysectors:${config.name} Sector">
													                                <c:out value="${config.name} Sector"/>
													                                </digi:trn>
													                                </strong>
												                                </c:if>
										                                        <c:if test="${!empty aimChannelOverviewForm.activitySectors}">
																					<ul>
																						<c:forEach var="actSect" items="${aimChannelOverviewForm.activitySectors}">
						                                                            		<c:if test="${actSect.configId==config.id}">
																								<li>
																									<field:display name="Sector Scheme Name" feature="Sectors">
																										<c:out value="${actSect.sectorScheme}" />
																										<br/>&nbsp;
																										<IMG src="../ampTemplate/images/link_out_bot.gif"/>
																									</field:display>
																									<field:display name="${config.name} Sector" feature="Sectors">
																										<c:out value="${actSect.sectorName}" />
																									</field:display>
																									<c:if test="${!empty actSect.subsectorLevel1Name}">
																										<field:display name="${config.name} Sector Sub-Sector" feature="Sectors">
																										<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG
																											src="../ampTemplate/images/link_out_bot.gif"/>
																										<c:out value="${actSect.subsectorLevel1Name}" />
																										
																											<c:if test="${!empty actSect.subsectorLevel2Name}">
																											<field:display name="${config.name} Sector Sub-Sub-Sector" feature="Sectors">
																												 <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG
																													src="../ampTemplate/images/link_out_bot.gif"/>
																												<c:out value="${actSect.subsectorLevel2Name}" />
																												&nbsp;
																											</field:display>
																											</c:if>
																										</field:display>
																									</c:if>
																									<field:display name="Percentage" feature="Sectors">
				                                                                                    <logic:present name="actSect" property="sectorPercentage">
																										<c:if test="${actSect.sectorPercentage!=0}">
																											(<c:out value="${actSect.sectorPercentage}" />%)
																										</c:if>
																									</logic:present>
																									</field:display>

																								</li>
																								</c:if>
																						</c:forEach>
																					</ul>
										                                        </c:if>
											                                    </field:display>
																			</c:forEach>
																			
																			</TD>
																		</TR>
																	</TABLE>
																	</TD>
																</TR>

															</field:display>
														</feature:display>

														<feature:display module="Project ID and Planning"
															name="Location">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellspacing="1"
																	vAlign="top" align="left" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
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
																					<field:display name="Implementation Level"
																						feature="Location">
																						<TR>
																							<TD width="100%" colspan="${aimChannelOverviewForm.numImplLocationLevels+1}" align="left" bgcolor="#ffffff">
																								<i><digi:trn key="aim:impLevel">Implementation Level</digi:trn></i>
																								 :
                                                                                                 [<category:getoptionvalue categoryValueId="${aimChannelOverviewForm.implemLocationLevel}" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LEVEL_KEY %>"  />]

																							</TD>
																						</TR>
																					</field:display>
																					<field:display name="Implementation Location" feature="Location">
																					<TD width="100%" colspan="${aimChannelOverviewForm.numImplLocationLevels+1}" align="left" bgcolor="#ffffff">
																						<i>
																							<digi:trn key="aim:impLocations">Implementation Location</digi:trn>
																							:&nbsp;
																						</i>
[<category:getoptionvalue categoryValueId="${aimChannelOverviewForm.impLocation}" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />]

																					</TD>
																					</field:display>

																					<c:if test="${!empty activity.locations}">
                                                                                       
                                                                                         <tr>
                                                                                        	 <c:forEach var="indexLayer" begin="${aimChannelOverviewForm.countryIndex}" end="${aimChannelOverviewForm.numImplLocationLevels-1}">
                                                                                        	 	<c:if test="${aimChannelOverviewForm.numOfLocationsPerLayer[indexLayer]>0}">
	                                                                                        	 	<td align="center" bgcolor="#ffffff">
	                                                                                         		<i>
	                                                                                         			<category:getoptionvalue categoryIndex="${indexLayer}" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
	                                                                                         		</i>
	                                                                                         		</td>
                                                                                         		</c:if>
                                                                                        	 </c:forEach>
                                                                                        	 <td  align="center" bgcolor="#ffffff">
                                                                                              <i> <digi:trn key="aim:percent">Percent</digi:trn></i>
																							</td>
                                                                                         </tr>
																						<%--
																						<TR>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 1}" >
																									<i>
																									<category:getoptionvalue categoryIndex="1" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 2}" >
																									<i>
																									<category:getoptionvalue categoryIndex="2" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
																							<TD width="30%" align="center" bgcolor="#ffffff">
																								<c:if test="${aimChannelOverviewForm.numImplLocationLevels > 3}" >
																									<i>
																									<category:getoptionvalue categoryIndex="3" categoryKey="<%=CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>"  />
																									</i>
																								</c:if>
																								&nbsp;
																							</TD>
                                                                                              <TD  align="center" bgcolor="#ffffff">
                                                                                              <i> <digi:trn key="aim:percent">Percent</digi:trn></i>
																							</TD>
																							</TR>
																						
																						<c:forEach varStatus="varSt" var="loc" items="${activity.locations}">

																							
                                                                                            
																							
																							<c:if test="${not empty loc.location.ampRegion || not empty loc.location.ampZone ||not empty loc.location.ampWoreda}">
																							
																							<c:set var="mapParam">${mapParam}${loc.location.region}/${loc.location.zone}/${loc.locationPercentage}</c:set>
																							<c:if test="${not varSt.last}">
																								<c:set var="mapParam">${mapParam}|</c:set>
																							</c:if>
																							
																							<TR>
																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.location.region}" />
																								</TD>

																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.location.zone}" />
																								</TD>
																								<TD width="30%" align="center" bgcolor="#ffffff">
																									<c:out value="${loc.location.woreda}" />
																								</TD>
                                                                                                  <TD  align="center" bgcolor="#ffffff">
                                                                                                     <c:if test='${loc.locationPercentage > 0}'>

																										<fmt:formatNumber type="number" value="${loc.locationPercentage}" />

                                                                                                     %
																									
                                                                                                     </c:if>
																								</TD>
																							</TR>
                                                                                            </c:if>
																						</c:forEach> --%>
                                                                                                                                                                                <c:forEach  var="loc" items="${aimChannelOverviewForm.sortedLocations}">
                                                                                                                                                                                    <c:set var="locSize" >
                                                                                                                                                                                        ${fn:length(loc.ancestorLocationNames)}
                                                                                                                                                                                    </c:set>

                                                                                                                                                                                    <tr>
                                                                                                                                                                                        <c:forEach var="ancestor" items="${loc.ancestorLocationNames}">
                                                                                                                                                                                            <td align="center" bgcolor="#ffffff">
                                                                                                                                                                                                ${ancestor}
                                                                                                                                                                                            </td>

                                                                                                                                                                                        </c:forEach>

                                                                                                                                                                                     <c:forEach begin="${locSize}" end="${aimChannelOverviewForm.numImplLocationLevels-1}" step="1">
                                                                                                                                                                                            <td align="center" bgcolor="#ffffff">
                                                                                                                                                                                                &nbsp;
                                                                                                                                                                                            </td>

                                                                                                                                                                                        </c:forEach>

                                                                                                                                                                                        <td align="center" bgcolor="#ffffff">
                                                                                                                                                                                            <field:display name="Regional Percentage" feature="Location">&nbsp;
                                                                                                                                                                                                <c:if test="${loc.percent!=0}">
                                                                                                                                                                                                     ${loc.percent}%
                                                                                                                                                                                                </c:if>

                                                                                                                                                                                                   
                                                                                                                                                                                            </field:display>
                                                                                                                                                                                        </td>
                                                                                                                                                                                    </tr>
                                                                                                                                                                                </c:forEach>

                                                                                                                                                                </c:if>
                                                                                                                                                                <!--commented by Sebastian Dimunzio when working on UI issues: This code Is not showing the image and can't hidde it by FM
                                                                                                                                                                                                <tr>
                                                                                                                                                                                                        <td colspan="4">
                                                                                                                                                                                                                <img width="500" height="500" src="../../gis/getActivityMap.do?action=paintMap&mapCode=TZA&segmentData=${mapParam}">
                                                                                                                                                                                                        </td>
                                                                                                                                                                                                </tr>
                                                                                                                                                                                                 -->
                                                                                                                                                        </TABLE>
                                                                                                                                        </TD>
                                                                                                                                        </TR>
                                                                                                                                        </TABLE>
                                                                                                                                        </TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellspacing="1"
															vAlign="top" align="left" bgcolor="#aaaaaa">

															<module:display name="National Planning Dashboard"
																parentModule="NATIONAL PLAN DASHBOARD">
																<feature:display name="NPD Programs"
																	module="National Planning Dashboard">
																	<field:display name="National Plan Objective"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:national Plan Objective">National Plan Objective</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
																			<c:forEach var="program"
																				items="${aimChannelOverviewForm.nationalPlanObjectivePrograms}">
																				<c:out value="${program.hierarchyNames}" />&nbsp;
																				<c:out value="${program.programPercentage}"/>%<br/>
																			</c:forEach></TD>
																		</TR>
																	</field:display>
																	<field:display name="Primary Program"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:primary Programs">Primary Programs</digi:trn></b></TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff"><c:forEach var="program"
																				items="${aimChannelOverviewForm.primaryPrograms}">
																				<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
																					value="${program.programPercentage}" />%<br />
																			</c:forEach></TD>
																		</TR>
																	</field:display>

																	<field:display name="Secondary Program"
																		feature="NPD Programs">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:secondary Programs">Secondary Programs</digi:trn></b></TD>
																		</TR>
																	</field:display>
																	<TR>
																		<TD bgcolor="#ffffff"><c:forEach var="program"
																			items="${aimChannelOverviewForm.secondaryPrograms}">
																			<c:out value="${program.hierarchyNames}" />&nbsp; <c:out
																				value="${program.programPercentage}" />%<br />
																		</c:forEach></TD>
																	</TR>
																</feature:display>
															</module:display>
                                                            <module:display name="Cross Cutting Issues" parentModule="PROJECT MANAGEMENT">
                                                                <feature:display name="Cross Cutting Issues" module="Cross Cutting Issues">
                                                                            <TR>
                                                                                <TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
                                                                                    height=10
                                                                                    src="../ampTemplate/images/arrow-014E86.gif"
                                                                                    width=15> <b><digi:trn>Cross Cutting Issues</digi:trn></b></TD>
                                                                            </TR>
                                                                            <TR>
                                                                                <TD bgcolor="#ffffff" height="5"></TD>
                                                                            </TR>
                                                                          <field:display name="Equal Opportunity" feature="Cross Cutting Issues">
                                                                            <c:if test="${not empty aimChannelOverviewForm.equalOpportunity && fn:trim(aimChannelOverviewForm.equalOpportunity) ne ''}" >
                                                                            <tr>
                                                                                <td bgcolor="#ffffff">
	                                                                                <i><digi:trn>Equal Opportunity</digi:trn>:</i><br />
																					<digi:edit key="${aimChannelOverviewForm.equalOpportunity}"/>
                                                                                </td>
                                                                            </tr>
                                                                            </c:if>
                                                                          </field:display>
                                                                          <field:display name="Environment" feature="Cross Cutting Issues">
                                                                            <c:if test="${!empty aimChannelOverviewForm.environment && fn:trim(aimChannelOverviewForm.environment) ne ''}" >
                                                                            <tr>
                                                                                <td bgcolor="#ffffff">
                                                                                    <i><digi:trn>Environment</digi:trn>:</i><br />
                                                                                    <digi:edit key="${aimChannelOverviewForm.environment}"/>
                                                                                </td>
                                                                            </tr>
                                                                            </c:if>
                                                                          </field:display>
                                                                          <field:display name="Minorities" feature="Cross Cutting Issues">
                                                                            <c:if test="${!empty aimChannelOverviewForm.minorities && fn:trim(aimChannelOverviewForm.minorities) ne ''}" >
                                                                            <tr>
                                                                                <td bgcolor="#ffffff">
                                                                                  <i><digi:trn key="aim:monitories">Minorities</digi:trn>:</i><br />
                                                                                   <digi:edit key="${aimChannelOverviewForm.minorities}"/>
                                                                                </td>
                                                                            </tr>
                                                                            </c:if>
                                                                          </field:display>
                                                                </feature:display>
                                                            </module:display>

														</TABLE>
														</TD>
													</TR>




													
													
													<module:display name="Contact Information"	parentModule="PROJECT MANAGEMENT">
														<feature:display name="Donor Contact Information" module="Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; 
																			<IMG height="10"src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15"/>
																			<b><digi:trn >Donor funding Contact Information</digi:trn></b>
																		</TD>
																	</TR>
																	<field:display feature="Donor Contact Information"	name="Donor First Name">
																		<TR>
																			<TD bgcolor="#ffffff"><i>
																			<digi:trn>Name</digi:trn></i>:
																			<c:if test="${not empty aimChannelOverviewForm.primaryDonorContact}">
																				<c:out value="${aimChannelOverviewForm.primaryDonorContact.contact.name}" />&nbsp; 
																				<c:out value="${aimChannelOverviewForm.primaryDonorContact.contact.lastname}" /></TD>
																			</c:if> 																			
																		</TR>
																	</field:display>
																	<field:display feature="Donor Contact Information"	name="Donor Email">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn>Email</digi:trn></i>:
																				<c:if test="${not empty aimChannelOverviewForm.primaryDonorContact}">
																					<c:forEach var="property" items="${aimChannelOverviewForm.primaryDonorContact.contact.properties}">
																						<c:if test="${property.name=='contact email'}">
																							<c:set	var="mailTo" value="mailto:${property.value}" /> 
																							<a	href="${mailTo}">${property.value}</a> ; &nbsp;	
																						</c:if>
																					</c:forEach>																					
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display module="Contact Information" name="Government Contact Information">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding="3" cellSpacing="1"	vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; 
																				<IMG height="10" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15">
																				<b><digi:trn>MOFED Contact Information</digi:trn></b>
																			</TD>
																		</TR>
																		<field:display feature="Government Contact Information"	name="Government First Name">
																			<TR>
																				<TD bgcolor="#ffffff">
																					<i><digi:trn>Name</digi:trn></i>: 
																					<c:if test="${not empty aimChannelOverviewForm.primaryMofedContact}">
																						<c:out value="${aimChannelOverviewForm.primaryMofedContact.contact.name}" />&nbsp; 
																						<c:out value="${aimChannelOverviewForm.primaryMofedContact.contact.lastname}" />
																					</c:if>
																				</TD>
																			</TR>
																		</field:display>
																		<field:display feature="Government Contact Information"	name="Government Email">
																			<TR>
																				<TD bgcolor="#ffffff">
																					<i><digi:trn key="aim:contactPersonEmail">Email</digi:trn></i>: 
																					<c:if test="${not empty aimChannelOverviewForm.primaryMofedContact}">
																						<c:forEach var="property" items="${aimChannelOverviewForm.primaryMofedContact.contact.properties}">
																							<c:if test="${property.name=='contact email'}">
																								<c:set	var="mailTo" value="mailto:${property.value}" /> 
																								<a	href="${mailTo}">${property.value}</a> ; &nbsp;	
																							</c:if>
																						</c:forEach>																					
																					</c:if>
																				</TD>
																			</TR>
																		</field:display>
																	</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display name="Project Coordinator Contact Information"	module="Contact Information">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding="3" cellSpacing="1"	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; 
																			<IMG height="10" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15">
																			<b><digi:trn>Project Coordinator Contact Information</digi:trn></b>
																		</TD>
																	</TR>
																	<field:display feature="Project Coordinator Contact Information" name="Project Coordinator First Name">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn>Name</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primaryprojCoordinatorContact}">
																					<c:out value="${aimChannelOverviewForm.primaryprojCoordinatorContact.contact.name}" />&nbsp; 
																					<c:out value="${aimChannelOverviewForm.primaryprojCoordinatorContact.contact.lastname}" />
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																	<field:display feature="Project Coordinator Contact Information" name="Project Coordinator Email">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn key="aim:contactPersonEmail">Email</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primaryprojCoordinatorContact}">
																					<c:forEach var="property" items="${aimChannelOverviewForm.primaryprojCoordinatorContact.contact.properties}">
																						<c:if test="${property.name=='contact email'}">
																							<c:set	var="mailTo" value="mailto:${property.value}" /> 
																							<a	href="${mailTo}">${property.value}</a>;
																						</c:if>
																					</c:forEach>																					
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display name="Sector Ministry Contact Information"	module="Contact Information">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding="3" cellSpacing="1"	vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; 
																				<IMG height="10" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15">
																				<b><digi:trn>Sector Ministry Contact Information</digi:trn></b>
																			</TD>
																	</TR>
																	<field:display feature="Sector Ministry Contact Information" name="Sector Ministry Contact First Name">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn>Name</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primarySectorMinistryContact}">
																					<c:out value="${aimChannelOverviewForm.primarySectorMinistryContact.contact.name}" />&nbsp; 
																					<c:out value="${aimChannelOverviewForm.primarySectorMinistryContact.contact.lastname}" />
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																	<field:display feature="Sector Ministry Contact Information" name="Sector Ministry Contact Email">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn key="aim:contactPersonEmail">Email</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primarySectorMinistryContact}">
																					<c:forEach var="property" items="${aimChannelOverviewForm.primarySectorMinistryContact.contact.properties}">
																						<c:if test="${property.name=='contact email'}">
																							<c:set	var="mailTo" value="mailto:${property.value}" /> 
																							<a	href="${mailTo}">${property.value}</a> ; &nbsp;	
																						</c:if>
																					</c:forEach>																					
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
														<feature:display name="Implementing/Executing Agency Contact Information"	module="Contact Information">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding="3" cellSpacing="1"	vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; 
																				<IMG height="10" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15">
																				<b><digi:trn>Implementing/Executing Agency Contact Information</digi:trn></b>
																			</TD>
																	</TR>
																	<field:display feature="Implementing/Executing Agency Contact Information" name="Implementing/Executing Agency Contact First Name">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn>Name</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primaryImplExecutingAgencyContact}">
																					<c:out value="${aimChannelOverviewForm.primaryImplExecutingAgencyContact.contact.name}" />&nbsp; 
																					<c:out value="${aimChannelOverviewForm.primaryImplExecutingAgencyContact.contact.lastname}" />
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																	<field:display feature="Implementing/Executing Agency Contact Information" name="Implementing/Executing Agency Contact Email">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<i><digi:trn>Email</digi:trn></i>: 
																				<c:if test="${not empty aimChannelOverviewForm.primaryImplExecutingAgencyContact}">
																					<c:forEach var="property" items="${aimChannelOverviewForm.primaryImplExecutingAgencyContact.contact.properties}">
																						<c:if test="${property.name=='contact email'}">
																							<c:set	var="mailTo" value="mailto:${property.value}" /> 
																							<a	href="${mailTo}">${property.value}</a> ; &nbsp;	
																						</c:if>
																					</c:forEach>																					
																				</c:if>
																			</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>
													
													
													<field:display name="Accession Instrument" feature="Identification">
                                                    <c:set var="accessionInstrument">
														${aimChannelOverviewForm.accessionInstrument}
                                                    </c:set>
													<c:if test="${!empty accessionInstrument}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:AccessionInstrument">
																	Accession Instrument</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${accessionInstrument}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>
													
													<field:display name="Project Implementing Unit" feature="Identification">
	                                                    <c:set var="projectImplUnit">
															${aimChannelOverviewForm.projectImplUnit}
	                                                    </c:set>
														<c:if test="${!empty projectImplUnit}">
															<TR>
																<TD>
																	<TABLE width="100%" cellPadding="3" cellSpacing="1"	vAlign="top" align="top" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp;
																				<IMG height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
																				<b><digi:trn>Project Implementing Unit</digi:trn></b>
																			</TD>
																		</TR>
																		<TR>
																			<TD bgcolor="#ffffff">
																				${projectImplUnit}
																			</TD>
																		</TR>
																	</TABLE>
																</TD>
															</TR>
														</c:if>
													</field:display>
													
													<field:display name="A.C. Chapter" feature="Identification">
                                                    <c:set var="acChapter">
	                                                    ${aimChannelOverviewForm.acChapter}
                                                    </c:set>
													<c:if test="${!empty acChapter}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn key="aim:acChapter">
                                                              A.C. Chapter</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">${acChapter}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>												
													
													
													<field:display name="Cris Number" feature="Identification">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b>
																		<digi:trn key="aim:crisNumber">Cris Number</digi:trn>
																		</b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">${activity.crisNumber}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</field:display>
													<field:display name="Procurement System" feature="Identification">
                                                    <c:set var="procurementSystem">
														${aimChannelOverviewForm.procurementSystem}
                                                    </c:set>
													<c:if test="${!empty procurementSystem}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:ProcurementSystem">
																	Procurement System</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${procurementSystem}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>

													<field:display name="Reporting System" feature="Identification">
                                                    <c:set var="reportingSystem">
														${aimChannelOverviewForm.reportingSystem}
                                                    </c:set>
													<c:if test="${!empty reportingSystem}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:ReportingSystem">
																	Reporting System</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${reportingSystem}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>

													<field:display name="Audit System" feature="Identification">
                                                    <c:set var="auditSystem">
														${aimChannelOverviewForm.auditSystem}
                                                    </c:set>
													<c:if test="${!empty auditSystem}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:AuditSystem">
																	Audit System</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${auditSystem}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>

													<field:display name="Institutions" feature="Identification">
                                                    <c:set var="institutions">
														${aimChannelOverviewForm.institutions}
                                                    </c:set>
													<c:if test="${!empty institutions}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:Institutions">
																	Institutions</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${institutions}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>

													<field:display name="Project Category" feature="Identification">
                                          
													<c:if test="${!empty aimChannelOverviewForm.projectCategory}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn
																		key="aim:ProjectCategory">
																	Project Category</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${aimChannelOverviewForm.projectCategory}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>
													
													<field:display name="Government Agreement Number" feature="Identification">
                                          
													<c:if test="${!empty aimChannelOverviewForm.govAgreementNumber}">
														<TR>
															<TD>
															<TABLE width="100%" cellPadding=3 cellspacing="1"
																vAlign="top" align="top" bgcolor="#aaaaaa">
																<TR>
																	<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																		height=10 src="../ampTemplate/images/arrow-014E86.gif"
																		width=15> <b><digi:trn key="aim:step1:GovernmentAgreementNumTitle">
																		Government Agreement Number
																		</digi:trn></b></TD>
																</TR>
																<TR>
																	<TD bgcolor="#ffffff">
																	${aimChannelOverviewForm.govAgreementNumber}</TD>
																</TR>
															</TABLE>
															</TD>
														</TR>
													</c:if>
													</field:display>
												</TABLE>
												</TD>
												<TD width="2%" vAlign="top" align="left">&nbsp;
													
												</TD>
												<TD width="49%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="2" cellSpacing="2"
													vAlign="top" align="left">
													<TR>
														<TD>
														<TABLE width="100%" cellpadding="0" cellspacing="1"
															vAlign="top" align="left" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																	height=10 src="../ampTemplate/images/arrow-014E86.gif"
																	width=15> <b><digi:trn
																	key="aim:relatedOrganizations">Related Organizations</digi:trn></b>
																</TD>
															</TR>




															<TR>
																<TD bgcolor="#fffff">
																<TABLE width="100%" cellSpacing="2" cellPadding="2"
																	vAlign="top" align="center" bgcolor="#ffffff">
																	<module:display name="Funding"
																		parentModule="PROJECT MANAGEMENT">
																		<feature:display module="Funding"
																			name="Funding Information">
																			<TR>
																				<TD>
																				<TABLE cellSpacing="1" cellPadding="2" vAlign="top"
																					align="left" bgcolor="#ffffff">
																					<TR>
																						<TD bgcolor="#ffffff" colspan="2"><b><digi:trn
																							key="aim:fundingCountryAgency">
																								Funding Country/Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD>
																							<%-- 
																							<c:if test="${!empty aimChannelOverviewForm.financingBreakdown}">
																								<logic:iterate name="aimChannelOverviewForm" property="financingBreakdown" id="breakdown"
					  	                   															type="org.digijava.module.aim.helper.FinancingBreakdown">
																										<bean:define id="breakdown" name="breakdown" type="org.digijava.module.aim.helper.FinancingBreakdown" toScope="request" />
																										<ul><li><i><jsp:include page="previewFinancingOrganizationPopup.jsp"/></i></li></ul>
																								</logic:iterate>
																							</c:if>
																							--%>
																							
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'DN'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <li><c:out value="${relOrg.orgName}" /></li><br>
																									<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</TD>
																						<td bgcolor="#ffffff">&nbsp;</td>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																	</module:display>
																	<module:display name="Organizations" parentModule="PROJECT MANAGEMENT">
																	
																		<feature:display module="Organizations" name="Responsible Organization">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:responsibleOrganisation">
																							Responsible Organization</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'RO'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <li><c:out value="${relOrg.orgName}" /></li><br>
																									<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Executing Agency">
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
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'EA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <li><c:out value="${relOrg.orgName}" /></li><br>
																									<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Implementing Agency">
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
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'IA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%-- <bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Beneficiary Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:beneficiary2Agency">
																								Beneficiary Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'BA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
																		<feature:display module="Organizations"
																			name="Contracting Agency">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:contracting2Agency">
																								Contracting Agency</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'CA'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>

																		<feature:display module="Organizations"
																			name="Regional Group">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:regionalGroup">Regional Group</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'RG'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>


																		<feature:display module="Organizations" name="Sector Group">
																			<TR>
																				<TD>
																				<TABLE width="100%" cellSpacing="1" cellPadding="2"
																					vAlign="top" align="left" bgcolor="#dddddd">
																					<TR>
																						<TD bgcolor="#ffffff"><b><digi:trn
																							key="aim:sectorGroup">Sector Group</digi:trn></b></TD>
																					</TR>
																					<TR>
																						<TD bgcolor="#ffffff"><c:if
																							test="${!empty aimChannelOverviewForm.relOrgs}">
																							<c:forEach var="relOrg"
																								items="${aimChannelOverviewForm.relOrgs}">
																								<c:if test="${relOrg.role == 'SG'}">
																									<c:set var="currentOrg" value="${relOrg}"
																										target="request" scope="request" />
																									<%--<bean:define id="currentOrg" name="relOrg"
																											type="org.digijava.module.aim.helper.RelOrganization"
																											toScope="request" />--%>
																									<jsp:include page="organizationPopup.jsp" />
																								</c:if>
																							</c:forEach>
																						</c:if></TD>
																					</TR>
																				</TABLE>
																				</TD>
																			</TR>
																		</feature:display>
													</module:display>
																</TABLE>
																</TD>
															</TR>
														</TABLE>
														</TD>
													</TR>
													<module:display name="Project ID and Planning"
														parentModule="PROJECT MANAGEMENT">
														<feature:display module="Project ID and Planning"
															name="Planning">
															<TR>
																<TD>
																<TABLE width="100%" cellPadding=2 cellspacing="1"
																	vAlign="top" align="top" bgcolor="#aaaaaa">
																	<TR>
																		<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																			height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																		<b><digi:trn key="aim:keyActivityDates">Key Activity Dates</digi:trn></b>
																		</TD>
																	</TR>
																	
																	<field:display name="Proposed Start Date"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<digi:trn>Proposed Start Date</digi:trn> : <aim:formatDate value="${activity.proposedStartDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Actual Start Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<digi:trn>Actual Start Date</digi:trn> : 
																				<aim:formatDate value="${activity.actualStartDate}" />
																			</TD>
																		</TR>
																	</field:display>
																																		
																	<field:display name="Proposed Approval Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																			<digi:trn>Proposed Approval Date</digi:trn> : 
                                                                              <aim:formatDate value="${activity.proposedApprovalDate}" />
																			</TD>
																		</TR>
																	</field:display>
																	<field:display name="Actual Approval Date" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:actualapprovaldate">Actual Approval Date</digi:trn> : <aim:formatDate value="${activity.actualApprovalDate}" /></TD>
																		</TR>
																	</field:display>
																	
																	
																	<field:display name="Final Date for Contracting"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:FinalDateForContracting">
																			Final Date for Contracting</digi:trn> : <aim:formatDate value="${activity.contractingDate}" /></TD>
																		</TR>
																	</field:display>
																	<field:display name="Final Date for Disbursements"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:FinalDateForDisbursments">
																			Final Date for Disbursments</digi:trn> : <aim:formatDate value="${activity.disbursmentsDate}" />&nbsp; <a
																				href="javascript:commentWin('<c:out value="${activity.ampActivityId}" />', 'fdd')">
																			<digi:trn key="aim:comment">Comment</digi:trn></a></TD>
																		</TR>
																	</field:display>
																	<field:display name="Proposed Completion Date" feature="Planning">
																		<tr>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:proposedCompletionDate">
																			Proposed Completion Date</digi:trn> : <aim:formatDate value="${activity.proposedCompletionDate}" /><a
																				href="javascript:commentWin('<c:out value="${activity.ampActivityId}" />', 'pcd')">
																			<digi:trn key="aim:comment">Comment</digi:trn></a></TD>
																		</tr>
																	</field:display>
																		
																	<field:display name="Current Completion Date"
																		feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff"><digi:trn
																				key="aim:currentCompletionDate">
																			Current Completion Date</digi:trn> : <aim:formatDate value="${activity.actualCompletionDate}" /> &nbsp; <a
																				href="javascript:commentWin('<c:out value="${activity.ampActivityId}" />', 'ccd')">
																			<digi:trn key="aim:comment">Comment</digi:trn></a></TD>
																		</TR>
																	</field:display>
																	
																		<field:display name="Closing Dates" feature="Planning">
																		<TR>
																			<TD bgcolor="#ffffff">
																				<TABLE width="100%" cellspacing="0" cellpadding="0" valign="top" align=left>
																					<TR>
																						<TD width="170" valign="top"><digi:trn
																							key="aim:proposedClosingDates">Closing Dates </digi:trn> :</TD>
																						<TD>
																				<TABLE cellpadding="0" cellspacing="0">
																					<c:forEach var="closeDate" items="${aimChannelOverviewForm.closingDates}">
																					<TR>
																						<TD><aim:formatDate value="${closeDate}" /></TD>
																					</TR>
																					</c:forEach>
																				</TABLE>
																					</TD>
																				 </TR>
																			</TABLE>
																		</TD>
																		</TR>
																	</field:display>
																</TABLE>
																</TD>
															</TR>
														</feature:display>
													</module:display>

													<module:display name="Funding"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Funding Information"
															module="Funding">
															<field:display feature="Funding Information"
																name="Type Of Assistance">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:typeOfAssistance">
																				Type Of Assistance</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty aimChannelOverviewForm.typesOfAssistance}">
																			<c:forEach var="asstType"
																				items="${aimChannelOverviewForm.typesOfAssistance}">
																				<TR>
																					<TD bgcolor="#ffffff">
																						<category:getoptionvalue categoryValueId="${asstType.id}"/>
																					</TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
													</module:display>
													<module:display name="Funding"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Funding Information"
															module="Funding">
															<field:display name="Financing Instrument" feature="Funding Information">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn
																				key="aim:financingInstruments">
																				Financing Instruments</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty aimChannelOverviewForm.uniqueModalities}">

																			<c:forEach var="modal"
																				items="${aimChannelOverviewForm.uniqueModalities}">
																				<TR>
																					<TD bgcolor="#ffffff">
																						<category:getoptionvalue categoryValueId="${modal.id}"/>
																					</TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
													</module:display>

													<module:display name="Funding"
														parentModule="PROJECT MANAGEMENT">
														<feature:display name="Funding Information"
															module="Funding">
															<field:display name="Funding Status" feature="Funding Information">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn>
																				Funding Status</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty aimChannelOverviewForm.fundingStatuses}">

																			<c:forEach var="fundingStatus"
																				items="${aimChannelOverviewForm.fundingStatuses}">
																				<TR>
																					<TD bgcolor="#ffffff">
																						<category:getoptionvalue categoryValueId="${fundingStatus.id}"/>
																					</TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
															<field:display name="Mode of Payment" feature="Funding Information">
																<TR>
																	<TD>
																	<TABLE width="100%" cellPadding=2 cellspacing="1"
																		vAlign="top" align="left" bgcolor="#aaaaaa">
																		<TR>
																			<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																				height=10
																				src="../ampTemplate/images/arrow-014E86.gif"
																				width=15> <b><digi:trn>
																				Mode of Payment</digi:trn></b></TD>
																		</TR>
																		<c:if test="${!empty aimChannelOverviewForm.modesOfPayment}">

																			<c:forEach var="modesOfPayment"
																				items="${aimChannelOverviewForm.modesOfPayment}">
																				<TR>
																					<TD bgcolor="#ffffff">
																						<category:getoptionvalue categoryValueId="${modesOfPayment.id}"/>
																					</TD>
																				</TR>
																			</c:forEach>
																		</c:if>
																	</TABLE>
																	</TD>
																</TR>
															</field:display>
														</feature:display>
													</module:display>

													<TR>
														<TD>
														<TABLE width="100%" cellPadding=3 cellspacing="1"
															vAlign="top" align="top" bgcolor="#aaaaaa">
															<TR>
																<TD bgcolor="#eeeeee" height="18">&nbsp; <IMG
																	height=10 src="../ampTemplate/images/arrow-014E86.gif"
																	width=15> <b><digi:trn
																	key="aim:activityCreationDetails">
																	Activity creation details</digi:trn></b></TD>
															</TR>
															<field:display name="Activity Created By" feature="Identification">
																<TR>
																	<TD bgcolor="#ffffff"><i><digi:trn
																		key="aim:createdBy">Created By</digi:trn></i>: <c:out
																		value="${activity.activityCreator.user.firstNames}" /> <c:out
																		value="${activity.activityCreator.user.lastName}" /> - <c:out
																		value="${activity.activityCreator.user.email}" />
																	</TD>
																</TR>
															</field:display>
															<TR>
																<TD bgcolor="#ffffff"><i><digi:trn
																	key="aim:email">Email</digi:trn></i>: <bean:define
																	id="mailTo">
																		mailto:<c:out value="${activity.activityCreator.user.email}" />
																</bean:define> <a href="<%=mailTo%>"> <c:out
																	value="${activity.activityCreator.user.email}" /></a></TD>
															</TR>
															<field:display name="Activity Created On" feature="Identification">
																<TR>
																	<TD bgcolor="#ffffff"><i><digi:trn
																		key="aim:createdDate">Created date</digi:trn></i>: 
																		<aim:formatDate value="${activity.createdDate}" />&nbsp;
																	</TD>
																</TR>
															</field:display>
															<field:display name="Activity Approved By" feature="Identification">
																<c:if test="${!empty activity.approvedBy}">
																	<TR>
																		<TD bgcolor="#ffffff"><i> 
																		<digi:trn key="aim:activityApprovedBy">Activity approved by</digi:trn></i>: 
																		<c:out value="${activity.approvedBy.user.firstNames}"/> 
																		<c:out value="${activity.approvedBy.user.lastName}" /> - 
																		<c:out value="${activity.approvedBy.user.email}" />
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Activity Approved On" feature="Identification">
																<c:if test="${!empty activity.approvalDate}">
																	<TR>
																		<TD bgcolor="#ffffff">
																			<i>
																				<digi:trn key="aim:activityApprovedOn">Activity approved on</digi:trn>
																			</i>: 
																			<c:out value="${activity.approvalDate}" /> &nbsp;
																		</TD>
																	</TR>
																</c:if>
															</field:display>
															
															<field:display name="Activity Updated By" feature="Identification">
																<c:if test="${!empty activity.modifiedBy}">
																	<TR>
																		<TD bgcolor="#ffffff"><i> <digi:trn
																			key="aim:activityUpdatedBy">
																				Activity updated by</digi:trn></i>: <c:out
																			value="${activity.modifiedBy.user.firstNames}" /> <c:out
																			value="${activity.modifiedBy.user.lastName}" /> - <c:out
																			value="${activity.modifiedBy.user.email}" />
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Workspace of Creator" feature="Identification">
															<TR>
																		<TD bgcolor="#ffffff">
																			<i>
																				<digi:trn key="aim:workspaceOfCreator">Worskpace of creator</digi:trn>
																			</i>:
																			<c:out value="${activity.activityCreator.ampTeam.name}" /> - <c:out value="${activity.activityCreator.ampTeam.accessType}" />
																			<br/>
																			<i>
																				<digi:trn key="aim:computation">Computation</digi:trn>
																			</i>:
																				<c:if test="${activity.activityCreator.ampTeam.computation == 'true'}">
																				  <digi:trn key="aim:yes">Yes</digi:trn>
																				</c:if>
																				<c:if test="${activity.activityCreator.ampTeam.computation == 'false'}">
																				  <digi:trn key="aim:no">No</digi:trn>
																				</c:if>
																			<br/>
																		</TD>
															</TR>
															</field:display>
															<field:display name="Activity Updated On" feature="Identification">
																<c:if test="${!empty activity.updatedDate}">
																	<TR>
																		<TD bgcolor="#ffffff"><i><digi:trn
																			key="aim:activityUpdatedOn">
																			Activity updated on</digi:trn></i>: <c:out
																			value="${activity.updatedDate}" /> &nbsp;
																		</TD>
																	</TR>
																</c:if>
															</field:display>
															<field:display name="Data Source"
																feature="Identification">
																<c:if test="${!empty activity.activityCreator.user.organizationName}">
																	<TR>
																		<TD bgcolor="#ffffff"><i><digi:trn
																			key="aim:dataSource">
																Data Source</digi:trn></i>: <c:out
																			value="${activity.activityCreator.user.organizationName}" /> &nbsp;</TD>
																	</TR>
																</c:if>
															</field:display>
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
      </c:if>
	</logic:equal>
</digi:form>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>