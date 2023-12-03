<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<script language="JavaScript">

function load()
{
	window.print();
}

</script>

<digi:instance property="aimEditSurveyForm" />
<digi:form action="/viewSurvey.do" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="tabIndex" />

<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%" bgcolor="#ffffff">
	<!--<TR>
		<TD vAlign="top" align="center">
			<!-- contents
			<TABLE width="65%" cellspacing="0" cellpadding="0" vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border="3">
							<TR bgColor=#f4f4f2>
      	      					<TD align=left>

								</TD>
							</TR>-->
							<TR bgcolor="#ffffff" width="95%">
								<TD vAlign="top" align="center" width="750">
									<TABLE width="95%" cellpadding="0" cellspacing="0" vAlign="top" align="left" bgColor=#ffffff>
										<TR>
											<TD width="750" bgcolor="#ffffff" height="17">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#ffffff" height="17">
                        							<TR bgcolor="#ffffff" height="17">
                          	  							<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
															<digi:trn key="aim:aidEffectIndicators">Aid Effectiveness Indicators</digi:trn>
							  							</TD>
	                          							<TD background="ampModule/aim/images/corner-r.gif" height=17 width=17>
                                                        	&nbsp;
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#ffffff" align="center" class="box-border-nopadding">
                                              <table width="100%" cellPadding=3>
                                                <tr>
                                                  <td width="100%">
                                                    <b><digi:trn key="aim:pointOfDeliveryDonor">Point of delivery donor</digi:trn></b>
                                                  </td>
                                                </tr>

                                                <TR>
                                                  <TD colspan="2" width="90%">
                                                    <TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
                                                      <tr>
                                                        <td>
                                                        ${aimEditSurveyForm.deliveryDonor}
                                                        </td>
                                                      </tr>
                                                    </TABLE>
                                                  </td>
                                                </tr>
                                              </table>
												<logic:notEmpty name="aimEditSurveyForm" property="indicators">
													<table width="100%" cellPadding=3>
													<%--<bean:define id="start" name="aimEditSurveyForm" property="offset" />--%>
														<nested:iterate name="aimEditSurveyForm" property="indicators"
																	     >
															<tr>
																<td bgcolor=#ffffff width="5%">
																	<b><nested:write property="indicatorCode" /></b>
																</td>
																<td bgcolor=#ffffff width="95%"><b>
																	<nested:write property="name" />
																</td>
															</tr>
															<nested:iterate property="question">
															<TR bgcolor="#ffffff">
																<TD colspan="2" width="90%">
																<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
																	<TR>
																		<TD width="80%">
																			<nested:write property="questionText" /><br>
																			<nested:equal property="questionType" value="calculated">

																					<digi:trn key="aim:autoCalculated">(This is calculated automatically)</digi:trn>

																			</nested:equal>
																		</TD>
																		<TD width="20%">
																			<nested:equal property="response" value="">
																				<c:out value="-" />
																				<%--<nested:text property="response" value="NA" size="2" readonly="true" />--%>
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
																	<%--
																	<nested:equal property="questionType" value="yes-no">
																		<TD width="3%">
																			<nested:radio property="response" disabled="false" value="yes" />
																		</TD>
																		<TD width="7%">yes</TD>
																		<TD width="3%">
																			<nested:radio property="response" disabled="false" value="no" />
																		</TD>
																		<TD width="7%">no</TD>
																	</nested:equal>
																	<nested:equal property="questionType" value="calculated">
																		<TD width="20%">
																			<nested:text name="question" property="response" indexed="true" size="5" readonly="true" />
																		</TD>
																	</nested:equal> --%>
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
									</TABLE>
						<!--		</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>-->
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>
</digi:form>
