<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>


<script language="Javascript">
<!--
	function getSurvey() {
		if (document.aimEditSurveyForm.surveyId.value != "-1") {
			document.aimEditSurveyForm.submit();
		}
		else
			return false;
	}
-->
</script>

<digi:instance property="aimEditSurveyForm" />
<digi:form action="/viewSurvey.do" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="tabIndex" />

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      			<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewAESurveys">Click here to view Aid Effectiveness Surveys</digi:trn>
													</bean:define>
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />
													<c:set target="${urlParams}" property="ampActivityId" value="${aimEditSurveyForm.ampActivityId}" />
													<c:set target="${urlParams}" property="tabIndex" value="${aimEditSurveyForm.tabIndex}" />
													<digi:link href="/viewSurveyList.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
													<digi:trn key="aim:viewAESurveys">Aid Effectiveness Surveys</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<c:out value="${aimEditSurveyForm.fundingOrg}" />
												</SPAN>				
											</TD>
											<td>
						<img src="../ampTemplate/images/print_icon.gif">
				<digi:link href="/ParisIndicatorPrintSurvey.do" target="_blank">
					Print
				</digi:link>
				</td>
											<TD align="right">
												&nbsp;
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" height="17">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                        							<TR bgcolor="#F4F4F2" height="17"> 
                          	  							<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
															<digi:trn key="aim:aidEffectIndicators">Aid Effectiveness Indicators</digi:trn>
							  							</TD>
	                          							<TD background="module/aim/images/corner-r.gif" height=17 width=17>
                                                        	&nbsp;
														</TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<logic:notEmpty name="aimEditSurveyForm" property="indicators">
													<table width="100%" cellPadding=3>
													<bean:define id="start" name="aimEditSurveyForm" property="offset" />
														<nested:iterate name="aimEditSurveyForm" property="indicators" 
																	    offset="start" length="5">
															<tr>
																<td bgcolor=#ECF3FD width="5%">
																	<b><nested:write property="indicatorCode" /></b>
																</td>
																<td bgcolor=#ECF3FD width="95%"><b>
																	<nested:write property="name" />
																</td>
															</tr>
															<nested:iterate property="question">
															<TR bgcolor="#f4f4f2">
																<TD colspan="2" width="90%">
																<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
																	<TR>
																		<c:choose>
																			<c:when test="${indicators.indicatorCode == '7' }">
																		<TD width="80%">
																					<digi:trn key="aim:noQuestioPI7">
																					No question here. This indicator is calculated by the system based on <br>
																					information entered for disbursements for this project/programme
																					</digi:trn>
																			</c:when>
																			<c:when test="${indicators.indicatorCode == '10a' }">
																		<TD width="100%">
																					<digi:trn key="aim:noQuestionPI10a">
																					No question at the activity level; this indicator is calculated using the Calendar Module
																					</digi:trn>
																			</c:when>
																			<c:when test="${indicators.indicatorCode == '10b' }">
																		<TD width="100%">
																					 <digi:trn key="aim:noQuestionPI10b">
																					No question at the activity level; this indicator is calculated using the Document Management Module
																					</digi:trn>
																			</c:when>
																			<c:otherwise>
																		<TD width="80%">
																					<nested:write property="questionText" /><br>
																			</c:otherwise>
																		</c:choose>
																		</TD>
																		<TD width="20%">
																			<nested:equal property="response" value="">
																				<c:out value="-" />
																			</nested:equal>
																			<nested:notEqual property="response" value="">
																				<nested:notEqual property="questionType" value="calculated">
																					<c:if test="${indicators.indicatorCode != '10a' && indicators.indicatorCode != '10b' }">
																						<nested:write property="response" />
																					</c:if>
																					<nested:equal property="questionType" value="input">%</nested:equal>
																				</nested:notEqual>
																				<nested:equal property="questionType" value="calculated">
																					<nested:equal property="response" value="nil">
																						<digi:trn key="aim:noPlannedDisbursement">No planned disbursement</digi:trn>
																					</nested:equal>
																					<nested:notEqual property="response" value="nil">
																						<nested:write property="response" />
																					</nested:notEqual>
																				</nested:equal>
																			</nested:notEqual>
																		</TD>
																	</TR>
																</TABLE>
																</TD>
															</TR>
															</nested:iterate>
														</nested:iterate>
													</table>
												</logic:notEmpty>
											</TD>
										</TR>
										<tr><td>&nbsp;</td></tr>
										
										<!-- pagination starts here-->
										
											<logic:notEmpty name="aimEditSurveyForm" property="pages">
												<tr>
													<td colspan="2">
														<digi:trn key="aim:surveyPages">Pages :</digi:trn>
															<logic:iterate name="aimEditSurveyForm" property="pages" id="pages" type="java.lang.Integer">
																<c:set target="${urlParams}" property="page"><%=pages%></c:set>
																<c:if test="${aimEditSurveyForm.currentPage == pages}">
																	<font color="#FF0000"><%=pages%></font>
																</c:if>
																<c:if test="${aimEditSurveyForm.currentPage != pages}">
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																	</bean:define>
																	<digi:link href="/viewSurvey.do" name="urlParams" title="<%=translation%>" >
																		<%=pages%>
																	</digi:link>					
																</c:if>
																|&nbsp; 
															</logic:iterate>
													</td>
												</tr>
											</logic:notEmpty>
										
										<!-- pagination ends here-->
										
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>	
</TABLE>
</digi:form>
