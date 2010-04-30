<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>





<script language="Javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	document.aimEditSurveyForm.action = "<%=addUrl%>~pageId=1~step=17~action=edit~surveyFlag=true~activityId=" + id;
	document.aimEditSurveyForm.target = "_self";
	document.aimEditSurveyForm.submit();
}

<!--

	function getSurvey() {

		if (document.aimEditSurveyForm.surveyId.value != "-1")
		{

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

													<c:set var="clickToViewAESurveys">
														<digi:trn key="aim:clickToViewAESurveys">Click here to view Aid Effectiveness Surveys</digi:trn>
													</c:set>

													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />

													<c:set target="${urlParams}" property="ampActivityId" value="${aimEditSurveyForm.ampActivityId}" />

													<c:set target="${urlParams}" property="tabIndex" value="${aimEditSurveyForm.tabIndex}" />

													<digi:link href="/viewSurveyList.do" name="urlParams" styleClass="comment" title="${clickToViewAESurveys}" >

													<digi:trn key="aim:viewAESurveys">Aid Effectiveness Surveys</digi:trn>

													</digi:link>&nbsp;&gt;&nbsp;

													<c:out value="${aimEditSurveyForm.fundingOrg}" />

												</SPAN>

											</TD>

											<td>

						<img src="../ampTemplate/images/print_icon.gif">

				<digi:link href="/ParisIndicatorPrintSurvey.do" target="_blank">

					<digi:trn key="btn:print">Print</digi:trn>

				</digi:link>

				</td>

											<TD align="right">&nbsp;

												

											</TD>

										</TR>

									</TABLE>

								</TD>

							</TR>

							<TR bgColor=#f4f4f2>

								<TD vAlign="top" align="center" width="750">

									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 border="0">

										<TR>

											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
                                <table width="100%" cellPadding=3>
                                  <tr>

                                    <td bgcolor=#ECF3FD width="100%">

                                      <b><digi:trn key="aim:pointOfDeliveryDonor">Point of delivery donor</digi:trn></b>

                                    </td>
                                  </tr>

                                  <TR bgcolor="#f4f4f2">
                                    <TD colspan="2" width="90%">
                                      <TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
                                        <tr>
                                          <td>
                                          ${aimEditSurveyForm.deliveryDonor}
                                          </td>
                                          <td align="right">

                                          </td>
                                        </tr>
                                      </TABLE>
                                     </td>
                                   </tr>
                                </table>

												<logic:notEmpty name="aimEditSurveyForm" property="indicators">

													<table width="100%" cellPadding=3 border="0">

													<bean:define id="start" name="aimEditSurveyForm" property="offset" />

														<nested:iterate name="aimEditSurveyForm" property="indicators" offset="start" length="5">

															<tr>

																<td bgcolor=#ECF3FD width="5%">

																	<b><nested:write property="indicatorCode" /></b>

																</td>

																<td bgcolor=#ECF3FD width="95%"><b>


																	<bean:define id ="piIndcCode" scope="page" >aim:parisIndc<nested:write property="indicatorCode" /></bean:define>

																	<digi:trn key="${piIndcCode}"><nested:write property="name"/></digi:trn>

																</td>

															</tr>

															<nested:iterate property="question">

															<TR bgcolor="#f4f4f2">

																<TD colspan="2" width="90%">

																<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">

																	<TR>

																		<c:choose>

																			<c:when test="${indicators.indicatorCode == '7' }">

																				<TD width="100%">

																					<digi:trn key="aim:noQuestionPI7">

																					No question here. This indicator is calculated by the system based on

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



																					<c:set var="piKey" scope="page">
																						aim:parisIndc${piIndcCode}Ques<nested:write property="questionId" />
																					</c:set>

																					<digi:trn key="${piKey}">

																						<nested:write property="questionText" />

																					</digi:trn>

																					<br>

																			</c:otherwise>

																		</c:choose>

																		</TD>

																		<c:if test="${indicators.indicatorCode != '7' && indicators.indicatorCode != '10a' && indicators.indicatorCode != '10b' }">

																			<TD width="20%">

																				<nested:equal property="response" value="">

																					<c:out value="-" />

																				</nested:equal>

																				<nested:notEqual property="response" value="">

																					<nested:notEqual property="questionType" value="calculated">

																						<nested:write property="response" />

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

																		</c:if>

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

																	<c:set var="clickToViewNextPage">
																	<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																	</c:set>
																	<digi:link href="/viewSurvey.do" name="urlParams" title="${clickToViewNextPage}" >

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
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>