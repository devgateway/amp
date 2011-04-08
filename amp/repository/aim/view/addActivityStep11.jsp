<%@ page pageEncoding="UTF-8" %>
<%@page import="org.digijava.module.aim.util.CurrencyUtil" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--

	function addEUActivity() {
			openNewWindow(800, 460);
			<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do?new" />
			document.aimEditActivityForm.action = "<%= addEUActivity %>";
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function editEUActivity(indexId) {
			openNewWindow(700, 420);
			<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?editEU&indexId=" />
			document.aimEditActivityForm.action = "<%=editEUActivity%>"+indexId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function deleteEUActivity(indexId) {
		if (confirm("<digi:trn jsFriendly='true'>Are you sure you want to delete the activity?</digi:trn>")) {
			popupPointer=window.open('about:blank','popper','width="10",height=10,top=5000,left=5000,scrollbars=no,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=no');
			<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?deleteEU&indexId=" />
			document.aimEditActivityForm.action = "<%=editEUActivity%>"+indexId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
		}
	}


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
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
 </c:set>


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
														<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
													</c:set>
													<c:set var="message">
														<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
														</c:set>
														<c:set var="quote">'</c:set>
														<c:set var="escapedQuote">\'</c:set>
														<c:set var="msg">
														${fn:replace(message,quote,escapedQuote)}
														</c:set>
													<digi:link href="/viewMyDesktop.do" styleClass="comment"
													onclick="return quitRnot1('${msg}')" title="${translation}" >
														<digi:trn key="aim:portfolio">
															Portfolio
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
												
                                                                                                 <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                                                                                                     
                                                                                                     <c:set property="translation" var="trans">
                                                                                                         <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                                                                                             Click here to goto Add Activity Step ${step.stepActualNumber}
                                                                                                         </digi:trn>
                                                                                                     </c:set>
                                                                                                     <c:set var="link">
                                                                                                         <c:if test="${step.stepNumber==9}">
                                                                                                             /editSurveyList.do?edit=true
                                                                                                             
                                                                                                         </c:if>
                                                                                                         
                                                                                                         <c:if test="${step.stepNumber!=9}">
                                                                                                         
                                                                                                             /addActivity.do?step=${step.stepNumber}&edit=true
                                                                                                             
                                                                                                         </c:if>
                                                                                                     </c:set>
                                                                                                     
                                                                                                     
                                                                                                     
                                                                                                     <c:if test="${!index.last}">
                                                                                                         
                                                                                                         <c:if test="${index.first}">
                                                                                                             <digi:link href="${link}" styleClass="comment" title="${trans}">
                                                                                                                 
                                                                                                                 
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
                                                                                                                 <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn> 
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
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>:
													<bean:write name="aimEditActivityForm" property="identification.title"/>
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
										<table cellpadding="0" cellspacing="0" width="100%" vAlign="top" >
											<tr>
												<td width="100%">
													<table cellpadding="0" cellspacing="0" width="100%">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"></td>
															<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
                                                                                                                           <digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                                                                            ${fn:length(aimEditActivityForm.steps)}:
																<digi:trn key="aim:stepCosting">EU Project Costing</digi:trn>
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"></td>
														</tr>
													</table>
												</td>
											</tr>
											
											<feature:display name="Costing" module="Activity Costing">
											<tr valign="top">
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left"
													bgcolor="#006699">
													<tr>
															<td bgColor=#f4f4f2 align="center" vAlign="top">
																<table width="95%">
																	<tr>
																		<td>
																			<IMG alt=Link height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<a title="<digi:trn key="aim:costing">Costing</digi:trn>">
																				<b><digi:trn key="aim:costing">Costing</digi:trn></b>
																			</a>
																		</td>
																	</tr>
																	<tr><td>&nbsp;</td></tr>

																	<tr><td valign="top">
																		<c:if test="${!empty aimEditActivityForm.costing.costs}">
																		<bean:define id="costs" name="aimEditActivityForm" property="costing.costs" toScope="request"/>
																		<bean:define id="mode" value="form" type="java.lang.String" toScope="request"/>
																		<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
		                                                                  <digi:trn key="aim:amountsinthousands">Amounts in thousands (000) -</digi:trn>
		                                                                </gs:test>
		                                                                  <c:out value="${aimEditActivityForm.currCode}"/>
																		<jsp:include page="viewCostsSummary.jsp"/>
																		</c:if>
																	</td></tr>


																	<tr><td>
																	&nbsp;
																	</td></tr>
																	<tr><td>
																	
																	<field:display name="Add Costing Button" feature="Costing">
																	<html:button  styleClass="dr-menu" property="submitButton" onclick="addEUActivity()">
																		<digi:trn key="btn:addActivity">Add Activity</digi:trn>
																	</html:button>
																	</field:display>
																	
																	</td></tr>
																	
																	
																	<tr>
																		<td align="left">
																			<table width="100%" cellSpacing=5 cellpadding="0" border="0"
																			class="box-border-nopadding">


																	<!--tr><td bgColor=#f4f4f2 align="center">
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
																	</td></tr-->
	
	
																</table>
															</td>
														</tr>
														
													</table>
												</td>
											</tr>
											</table>
											</td>
											</tr>											
											</feature:display>											
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
