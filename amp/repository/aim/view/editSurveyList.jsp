<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>



<script language="JavaScript">

<!--

	function move(cntr) {

		var sid = "survey[" + cntr + "].surveyId";

		var val = (document.aimEditActivityForm.elements)[sid].value;

		if (val != "-1") {

			document.aimEditActivityForm.surveyId.value = val;

			document.aimEditActivityForm.target = "_self" ;

			document.aimEditActivityForm.submit();

		}

		else

			return false;

	}

-->

</script>



<digi:instance property="aimEditActivityForm" />

<digi:form action="/editSurvey.do" method="post">



<%-- <input type="hidden" name="surveyId" value=""> --%>



<html:hidden property="step" />
  <c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
  </c:set>



<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border="0">

<tr><td width="100%" vAlign="top" align="left">

<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<!-- End of Logo -->

</td></tr>

<tr><td width="100%" vAlign="top" align="left">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" vAlign="top" align="center" border="0">

	<tr>

		<td class=r-dotted-lg width="10">&nbsp;</td>

		<td align=left valign="top" class=r-dotted-lg>

			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">

				<tr><td>

					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">

						<tr>

							<td>

								<span class=crumb>

								<c:set var="translation">

									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>

								</c:set>

								<c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>

								<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${message}')"  title="${translation}">

									<digi:trn key="aim:portfolio">Portfolio</digi:trn>

								</digi:link>&nbsp;&gt;&nbsp;
                                                                 <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">

                                                                     <c:set property="translation" var="trans">
                                                                         <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                                                             Click here to goto Add Activity Step ${step.stepActualNumber}
                                                                         </digi:trn>
                                                                     </c:set>

                                                                      <c:set var="link">
                                                                              /addActivity.do?step=${step.stepNumber}&edit=true
                                                                      </c:set>






                                                                     <c:if test="${!index.last}">

                                                                         <c:if test="${index.first}">

                                                                             <digi:link href=" ${link}" styleClass="comment" title="${trans}">


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

                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                         <c:if test="${!index.first}">
                                                                             <digi:link href="${link}" styleClass="comment" title="${trans}">
                                                                                 <digi:trn key="aim:addActivityStep${step.stepActualNumber}">
                                                                                 Step ${step.stepActualNumber}
                                                                             </digi:trn>
                                                                             </digi:link>
                                                                             &nbsp;&gt;&nbsp;
                                                                         </c:if>
                                                                     </c:if>



                                                                     <c:if test="${index.last}">

                                                                         <c:if test="${index.first}">



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
                                                                         </c:if>


                                                                         <c:if test="${!index.first}">
                                                                             <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
                                                                         </c:if>



                                                                     </c:if>







                                                                 </c:forEach>
								</span>

							</td>

						</tr>

					</table>

				</td></tr>

				<tr><td>

					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">

						<tr>

							<td height=16 valign="center" width="100%"><span class=subtitle-blue>

								<c:if test="${aimEditActivityForm.editAct == false}">

									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>

								</c:if>

								<c:if test="${aimEditActivityForm.editAct == true}">

									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>

								</c:if>

							</td>

						</tr>

					</table>

				</td></tr>

				<tr><td>

					<digi:errors/>

				</td></tr>

				<tr><td style="vertical-align:top;">

					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border="0">

						<tr><td width="75%" vAlign="top">

						<table cellpadding="0" cellspacing="0" width="100%" border="0">

							<tr>

								<td width="100%">

									<table cellpadding="0" cellspacing="0" width="100%" border="0">

										<tr>

											<td width="13" height="20" background="module/aim/images/left-side.gif">

                                            &nbsp;

											</td>
                                                                                        <td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
													<digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                                                 ${fn:length(aimEditActivityForm.steps)}:
                                                                                                <digi:trn key="aim:aidEffectiveIndicators">

													Aid Effectiveness Indicators
                                                                                                  </digi:trn>
											</td>

											

											<td width="13" height="20" background="module/aim/images/right-side.gif">

                                            &nbsp;

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr><td bgcolor="#f4f4f2" width="100%">

							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">

							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">

								<!-- contents -->


								<table width="95%" bgcolor="#f4f4f2" border="0">

									<tr><td>&nbsp;</td></tr>

									<tr><td>

						<field:display name="Paris Survey" feature="Paris Indicators">

								<!-- Indicator Table starts here -->


									<TABLE width="656"  align="center" cellpadding="4" cellspacing="1" class="box-border-nopadding">

                 					<TR bgcolor="#DDDDDB" >

                 					<%--

	                        			<TD width="212"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>

				                    	<TD width="204"><digi:trn key="aim:organization">Organization</digi:trn></TD>

									    <TD width="114"><digi:trn key="aim:termAssist">Term Assist</digi:trn></TD>

	                         	   		<TD width="143"><digi:trn key="aim:surveyYear">Survey Year</digi:trn></TD>

	                         	   	--%>

	                         	   		<TD width="172"><digi:trn key="aim:aeSurvey">Aid Effectiveness Survey</digi:trn></TD>
				                    	<TD width="210"><digi:trn key="aim:donorOrganization">Donor Organization</digi:trn></TD>
                                        <TD width="200"><digi:trn key="aim:pointOfDeliveryDonor">Point of delivery donor</digi:trn></TD>
									</TR>

									<nested:empty name="aimEditActivityForm" property="surveyFundings">

			                    		<TR valign="top">

											<TD align="center" colspan="7" width="742"><span class="note"> 
												<digi:trn key="aim:noRecordsFound">No records found</digi:trn> </span></TD>

			                     		</TR>

			                    	</nested:empty>

			                    	<nested:notEmpty name="aimEditActivityForm" property="surveyFundings">

										<nested:iterate name="aimEditActivityForm" property="surveyFundings" id="surveyFund" indexId="cntr"

			  	                   					    type="org.digijava.module.aim.helper.SurveyFunding">

											<TR valign="top" bgcolor="#f4f4f2">

												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />

												<c:set target="${urlParams}" property="surveyId" value="${surveyFund.surveyId}" />

												<c:set target="${urlParams}" property="edit" value="true" />

                                                <c:set target="${urlParams}" property="index" value="${cntr}" />

												<c:set var="translation">

													<digi:trn key="aim:clickToViewAESurvey">Click here to view Aid Effectiveness Survey</digi:trn>

												</c:set>

												<TD width="172">

													<digi:link href="/editSurvey.do" name="urlParams" styleClass="comment" title="${translation}" >

														<nested:write name="surveyFund" property="acronim" /></digi:link>

												</TD>

						               			<TD width="210"><nested:write name="surveyFund" property="fundingOrgName" /></TD>
                                                <TD width="200"><nested:write name="surveyFund" property="deliveryDonorName" /></TD>
											</TR>

										</nested:iterate>

									</nested:notEmpty>

									</TABLE>
									</td></tr>

									<tr><td>&nbsp;</td></tr>

								</table>

								<!-- Indicator Table ends here -->

					</field:display> 

									</td></tr>

								</table>



								<!-- end contents -->

							</td></tr>

							</table>

							</td></tr>

						</table>

						</td>

						<td width="25%" vAlign="top" align="right">
							 
							  <jsp:include page="editActivityMenu.jsp" flush="true" />
  	

						</td></tr>

					</table>

				</td></tr>

				<tr><td>&nbsp;</td></tr>

			</table>

		</td>

		<td width="10">&nbsp;</td>

	</tr>

</table>

<%--

</td></tr>

</table> --%>

</digi:form>




