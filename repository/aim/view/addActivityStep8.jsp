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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--
	function validateForm() {	
		return true;
	}

function checkNumber(phoneOrFaxId){
	var phoneOrFax=document.getElementById(phoneOrFaxId);
	var number=phoneOrFax.value;
	var validChars= "0123456789()+ ";
	for (var i = 0;  i < number.length;  i++) {
		var ch = number.charAt(i);
		if (validChars.indexOf(ch)==-1){
			alert('enter correct number');
			phoneOrFax.value=number.substring(0,i);
			return false;
			break;
		}
	}
	return true;
}

function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}
-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
 </c:set>
 <digi:errors/>

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
									<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home ">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
                                    <c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="Click here to view MyDesktop ">
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
										<bean:write name="aimEditActivityForm" property="identification.title"/>
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
												<digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>${fn:length(aimEditActivityForm.steps)}:
                                                <digi:trn key="aim:activity:ContactInformation">Contact Information</digi:trn>
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
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
										<b><digi:trn key="aim:contactInformation">Contact Information</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
											<tr>
												<td width="50%" valign="top">
													<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
														<tr>
															<td width="20%"></td>
															<td width="15%" align="right"></td>
															<td width="65%"></td>
														</tr>														
														<feature:display name="Donor Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:donorFundingContactInformation">
																Donor funding contact information</digi:trn>
															</td>
															<field:display name="Donor First Name" feature="Donor Contact Information">
															<td align="right">
																<b>
																<digi:trn key="aim:firstName">
																	First Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntFirstName" styleClass="inp-text"/>
															</td>
															</field:display>
														</tr>
														<field:display name="Donor Last Name" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:lastName">
																	Last Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Email" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:email">
																	Email
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Title" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:title">
																	Title
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Organization" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:organization">
																	Organization
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Phone Number" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:phoneNumber">
																	Phone Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntPhoneNumber" styleClass="inp-text" styleId="donorPhone" onkeyup="checkNumber('donorPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Fax Number" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:faxNumber">
																	Fax Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntFaxNumber" styleClass="inp-text" styleId="dnrCntFax" onkeyup="checkNumber('dnrCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
														<tr>
															<td>&nbsp;</td><td></td><td></td>
														</tr>
														<feature:display name="Government Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:mofedContactInformation">
																MOFED contact information</digi:trn>
															</td>
															<field:display name="Government First Name" feature="Government Contact Information">
															<td align="right">
																<b>
																<digi:trn key="aim:firstName">
																	First Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntFirstName" styleClass="inp-text"/>
															</td>
															</field:display>
														</tr>
														<field:display name="Government Last Name" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:lastName">
																	Last Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Email" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:email">
																	Email
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Title" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:title">
																	Title
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Organization" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:organization">
																	Organization
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Phone Number" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:phoneNumber">
																	Phone Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntPhoneNumber" styleClass="inp-text" styleId="mofedPhone" onkeyup="checkNumber('mofedPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Fax Number" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:faxNumber">
																	Fax Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntFaxNumber" styleClass="inp-text" styleId="mfdCntFax" onkeyup="checkNumber('mfdCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
													</table>
												</td>
												<td width="50%" valign="top">
													<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
														<tr>
															<td width="20%"></td>
															<td width="15%" align="right"></td>
															<td width="65%"></td>
														</tr>
														<feature:display name="Project Coordinator Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:projectCoordinator">Project Coordinator Contact Information</digi:trn>
															</td>
															<field:display name="Project Coordinator First Name" feature="Project Coordinator Contact Information">
																<td align="right">
																	<b><digi:trn key="aim:projectCoordinator:firstName">First Name</digi:trn></b>
																</td>
																<td>
																	<html:text property="contactInfo.prjCoFirstName" styleClass="inp-text"/>
																</td>
															</field:display>
														</tr>
														<field:display name="Project Coordinator Last Name" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:lastName">Last Name</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Email" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:email">Email</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Title" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:title">Title</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Organization" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:organization">Organization</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Phone Number" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:phoneNumber">Phone Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoPhoneNumber" styleClass="inp-text" styleId="prjCoPhone" onkeyup="checkNumber('prjCoPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Fax Number" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:faxNumber">Fax Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoFaxNumber" styleClass="inp-text" styleId="prjCoFax" onkeyup="checkNumber('prjCoFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
														<tr>
															<td>&nbsp;</td><td></td><td></td>
														</tr>
														<feature:display name="Sector Ministry Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:sectorMinistryCnt">Sector Ministry Contact Information</digi:trn>
															</td>
															<field:display name="Sector Ministry Contact First Name" feature="Sector Ministry Contact Information">
																<td align="right">
																	<b><digi:trn key="aim:sectorMinistryCnt:firstName">First Name</digi:trn></b>
																</td>
																<td>
																	<html:text property="contactInfo.secMiCntFirstName" styleClass="inp-text"/>
																</td>
															</field:display>
														</tr>
														<field:display name="Sector Ministry Contact Last Name" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:lastName">Last Name</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Email" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:email">Email</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Title" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:title">Title</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Organization" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:organization">Organization</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Phone Number" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:phoneNumber">Phone Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntPhoneNumber" styleClass="inp-text" styleId="MiCntPhone" onkeyup="checkNumber('MiCntPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Fax Number" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:faxNumber">Fax Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntFaxNumber" styleClass="inp-text" styleId="MiCntFax" onkeyup="checkNumber('MiCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
													</table>
												</td>
											</tr>											
										</table>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
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
