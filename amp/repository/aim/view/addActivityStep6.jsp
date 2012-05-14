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



function validate(field) {

	if (field == 1) { // validate location

		if (document.aimEditActivityForm.selDocs.checked != null) {

			if (document.aimEditActivityForm.selDocs.checked == false) {

				alert("Please choose a document to remove");

				return false;

			}

		} else {

			var length = document.aimEditActivityForm.selDocs.length;

			var flag = 0;

			for (i = 0;i < length;i ++) {

				if (document.aimEditActivityForm.selDocs[i].checked == true) {

					flag = 1;

					break;

				}

			}



			if (flag == 0) {

				alert("Please choose a document to remove");

				return false;

			}

		}

		return true;

	} else { // validate sector

		if (document.aimEditActivityForm.selLinks.checked != null) {

			if (document.aimEditActivityForm.selLinks.checked == false) {

				alert("Please choose a link to remove");

				return false;

			}

		} else {

			var length = document.aimEditActivityForm.selLinks.length;

			var flag = 0;

			for (i = 0;i < length;i ++) {

				if (document.aimEditActivityForm.selLinks[i].checked == true) {

					flag = 1;

					break;

				}

			}



			if (flag == 0) {

				alert("Please choose a link to remove");

				return false;

			}

		}

		return true;

	}

}



function addDocuments() {

		openNewWindow(740, 400);
		
		document.getElementById('docFileOrLink').value = "file";

		//<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />

		document.aimEditActivityForm.action = "/contentrepository/addTemporaryDocument.do?webResource=false";

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.submit();

}

function addDocumentsDM(documentsType) {
		openNewWindow(900, 300);
		document.getElementById('docFileOrLink').value = "file";
		document.aimEditActivityForm.action = "/contentrepository/selectDocumentDM.do?documentsType="+documentsType;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function addManagedDocuments() {

		openNewWindow(600, 230);

		document.getElementById('docFileOrLink').value = "document";

		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />

		document.aimEditActivityForm.action = "<%= selDoc %>";

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.submit();

}



function addLinks() {

		openNewWindow(600, 350);

		document.getElementById('docFileOrLink').value = "link";

		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />

		document.aimEditActivityForm.action = "/contentrepository/addTemporaryDocument.do?webResource=true";

		document.aimEditActivityForm.target = popupPointer.name;

		document.aimEditActivityForm.submit();

}



function removeSelDocuments() {

	var flag = validate(1);

	if (flag == false) return false;

	document.getElementById('docFileOrLink').value = "file";

	<digi:context name="remDoc" property="context/module/moduleinstance/removeSelDocuments.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remDoc %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}



function removeSelManagedDocuments() {

	//var flag = validate(1);

	//if (flag == false) return false;

	document.getElementById('docFileOrLink').value = "document";

	<digi:context name="remDoc" property="context/module/moduleinstance/removeSelDocuments.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remDoc %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

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



function removeSelLinks() {

	var flag = validate(2);

	if (flag == false) return false;

	document.getElementById('docFileOrLink').value = "link";

	<digi:context name="remDoc" property="context/module/moduleinstance/removeSelDocuments.do?edit=true" />

	document.aimEditActivityForm.action = "<%= remDoc %>";

	document.aimEditActivityForm.target = "_self"

	document.aimEditActivityForm.submit();

	return true;

}

-->

</script>



<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">
  


<html:hidden property="step" />

<html:hidden property="documents.docFileOrLink"  styleId="docFileOrLink"/>





<html:hidden property="editAct" />
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
 </c:set>




<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">

<tr><td width="100%" vAlign="top" align="left">

<!--  AMP Admin Logo -->

<jsp:include page="teamPagesHeader.jsp"  />

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
										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;

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

				<tr><td>

					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">

						<tr><td width="75%" vAlign="top">

						<table cellpadding="0" cellspacing="0" width="100%">

							<tr>

								<td width="100%">

									<table cellpadding="0" cellspacing="0" width="100%" border="0">

										<tr>

											<td width="13" height="20" background="module/aim/images/left-side.gif">

											</td>

											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">

												<digi:trn>Step</digi:trn> ${stepNm} <digi:trn> of </digi:trn>  ${fn:length(aimEditActivityForm.steps)}:
                                                                                                         <digi:trn key="aim:activity:RelatedDocumentsAndLinks">
                                                                                                         Related Documents and Links

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
							<feature:display name="Related Documents" module="Document">
								<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
									<!-- contents -->									
									<jsp:include page="addActivityStep6RelatedDocuments.jsp"/>
								</td></tr>
							</feature:display>

							
							 
								<tr><td>&nbsp;</td></tr>
								<feature:display name="Web Resources" module="Document">
									<jsp:include page="addActivityStep6WebResources.jsp"/>
								</feature:display>
								<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
							
							
							<!-- Managed Documents -->
										<jsp:include page="addActivityStep6ManagedDocuments.jsp"/>
									<tr><td>

										&nbsp;

									</td></tr>
									<module:display name="Document" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td>
										<table width="100%" cellspacing="1" cellPadding=5 border="0" bgcolor="white">
										<tr>
											<td>
												<bean:define toScope="request" id="showRemoveButton" value="true" />
												<bean:define toScope="request" id="documentsType" value="<%=org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS %>" />
												<bean:define toScope="request" id="versioningRights" value="false" />
												<bean:define toScope="request" id="viewAllRights" value="true" />
												<bean:define toScope="request" id="makePublicRights" value="false" />
												<bean:define toScope="request" id="showVersionsRights" value="false" />
												<bean:define toScope="request" id="deleteRights" value="false" />
												<bean:define toScope="request" id="crRights" value="true" />
												<bean:define toScope="request" id="checkBoxToHide" value="false" />
												<jsp:include page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp"/>
											</td>
										</tr>
										</table>
										</td>
									</tr>
									</module:display>
								</table>
							</td></tr>

							</table>

							</td></tr>

						</table>

						</td>

						<td width="25%" vAlign="top" align="right">

						<!-- edit activity form menu -->

							<jsp:include page="editActivityMenu.jsp"  />

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

