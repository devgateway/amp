<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

	function validateForm() {
		return true;
	}

function addOrgs(value) {
		openNewWindow(600, 400);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do~orgSelReset=true" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>~item="+value+"~edit=true";
		document.aimEditActivityForm.prevOrg.value = value;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function removeSelOrgs(value) {
	document.aimEditActivityForm.item.value = value;
	<digi:context name="remOrgs" property="context/module/moduleinstance/removeSelRelOrgs.do?edit=true" />
	document.aimEditActivityForm.action = "<%= remOrgs %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

-->
</script>

<digi:form action="/addActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="item" />
<input type="hidden" name="prevOrg">

<html:hidden property="editAct" />
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
 </c:set>


<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">

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
									<c:set var="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</c:set>
									<digi:link href="/admin.do" styleClass="comment" title="${translation}">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
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
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="${translation}">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								          
                           <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                               
                               <c:set property="translation" var="trans">
                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                       Click here to goto Add Activity Step ${step.stepActualNumber}
                                   </digi:trn>
                               </c:set>
                               
                               
                               
                               <c:if test="${!index.last}">
                                   
                                   <c:if test="${index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           
                                           
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
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
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
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:step${stepNm}of10">
													Step ${stepNm} of 10: </digi:trn>
                                                                                                  <digi:trn key="aim:activity:RelatedOrganizations">
                                                                                                         Related Organizations
												</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<table width="95%" bgcolor="#f4f4f2">
							<feature:display name="Executing Agency" module="Organizations">
								<jsp:include page="addActivityStep7ExecutingAgency.jsp"/>
							</feature:display>

									<tr><td>
										&nbsp;
									</td></tr>
									<feature:display name="Implementing Agency" module="Organizations">
										<jsp:include page="addActivityStep7ImplementingAgency.jsp"/>
									</feature:display>
									<tr><td>
										&nbsp;
									</td></tr>


									<!-- Beneficiary Agency  -->
									<feature:display name="Beneficiary Agency" module="Organizations">
										<jsp:include page="addActivityStep7BeneficiaryAgency.jsp"/>
									</feature:display>

									<!-- /Benegiciary Agency -->


									<!-- Contracting Agency  -->
									<feature:display name="Contracting Agency" module="Organizations">
										<jsp:include page="addActivityStep7ContractingAgency.jsp"/>

									</feature:display>

									<!-- /Contracting Agency -->

									<!-- Regional Group  -->
									<feature:display name="Regional Group" module="Organizations">
										<jsp:include page="addActivityStep7RegionalGroup.jsp"/>
									</feature:display>

									<!-- /Regional Group -->
									
									<!-- Sector Group  -->
									<feature:display name="Sector Group" module="Organizations">
										<jsp:include page="addActivityStep7SectorGroup.jsp"/>
									</feature:display>
									<!-- /Sector Group  -->


<!--

									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:ContractAgency">The third party outside of the implementing agency</digi:trn>">
										<b><digi:trn key="aim:contractor">Contractor</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										<a title="<digi:trn key="aim:ContractAgency">The third party outside of the implementing agency</digi:trn>">
										<html:text property="contractors" size="60" styleClass="inp-text"/>
										</a>
									</td></tr>
									<tr><td bgColor=#f4f4f2 align="center">&nbsp;
									</td></tr>
 -->

<!--
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<html:button  styleClass="dr-menu" property="submitButton" onclick="gotoStep(6)">
															<< <digi:trn key="btn:back">Back</digi:trn>
													</html:button>


												</td>
												<td>
														<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(8)">
																<digi:trn key="btn:next">Next</digi:trn> >>
														</html:submit>
												</td>
												<td>
														<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">
																<digi:trn key="btn:reset">Reset</digi:trn>
														</html:reset>

												</td>
											</tr>
										</table>
									</td></tr>
 -->
							</td></tr>
								</table>

								<!-- end contents -->
							</td></tr>
							</table>
							</td></tr>
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
</td></tr>
</table>
</digi:form>
