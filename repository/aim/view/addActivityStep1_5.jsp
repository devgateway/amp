<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>


<script language="JavaScript">
<!--


function goNext() {
    if(!validateForm()){
      return false;
    }
    <digi:context name="nextSetp" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextSetp %>";
    document.aimEditActivityForm.target = "_self"
    document.aimEditActivityForm.submit();
    return true;
}

function resetAll(){
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function validateForm() {

	return true;
}

function goNextStep(){
  if(validateForm()){
    <digi:context name="nextStepUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextStepUrl %>";
    document.aimEditActivityForm.target = "_self";
    document.aimEditActivityForm.submit();
  }
}

function popupwin()
{
	var wndWidth = window.screen.availWidth/3.5;
	var wndHeight = window.screen.availHeight/3.5;
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;
	winpopup=window.open('',"popup","height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,status=no,toolbar=no");
	winpopup.document.write('<html>\n<head>\n');
	winpopup.document.write('<title>About : Status</title>\n');
	winpopup.document.write('</head>\n');
	winpopup.document.write('<body bgcolor="#f4f4f2">\n');
	winpopup.document.write('<font face="verdana" size=1>\n');
	winpopup.document.write('<ul><li><b>Planned:</b> from the conceptual stage to just prior to official commitment.</li><li><b>On-going:</b> the project is committed, is active but not yet completed.</li><li><b>Completed:</b> the project is finished, with all approved assistance provided.</li><li><b>Cancelled:</b> the project was committed but was terminated prior to planned completion.</li><li><b>Suspended:</b> the project has been suspended.</li></ul>\n');
	winpopup.document.write('</font>\n');
	winpopup.document.write('</font>\n</body>\n</html>\n');
	winpopup.document.close();
}

function toggleDiv(num){
	var textBoxName='#refComment'+num;
	var checkBoxName='#refCheck'+num;
	var hiddenName='documents.referenceDocs['+num+'].checked';//'input[@name=referenceDoc['+num+'].checked]';
	var hidInput=document.getElementsByName(hiddenName)[0];
	$(textBoxName).toggle("fast");
	hidInput.value=$(checkBoxName).get(0).checked;
	
}

-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">

    <c:set var="stepNm">
        ${aimEditActivityForm.stepNumberOnPage}
    </c:set>

<html:hidden property="step"/>
<html:hidden property="editKey"/>
<html:hidden property="editAct"/>

<input type="hidden" name="edit" value="true">

<input type="hidden" name="selectedDate" value="">
<html:hidden property="reset" />
<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
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
							<td><jsp:include page="t.jsp"/>
								<span class=crumb>
									<c:set var="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
									</c:set>
									<c:set var="message">
									<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${msg}')" title="${translation}">

										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								
								<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment"
								title="Click here to goto Add Activity Step 1">
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
								</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:addActivityStep2" >
									Step 2
									</digi:trn>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height="16" vAlign="center" width="100%"><span class="subtitle-blue">
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
:
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
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												             <digi:trn>Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn> ${fn:length(aimEditActivityForm.steps)}:
                                                                                                         <digi:trn key="aim:activity:References">
                                                                                                         References
                                                                                                         </digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2" border=0>
									<feature:display name="Identification" module="Project ID and Planning">
									&nbsp;
									</feature:display>
									<tr>
										<td>
											<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
											<b><digi:trn key="aim:editActivity:references">References</digi:trn></b>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr>
										<td>
										<c:if test="${aimEditActivityForm.documents.referenceDocs == null }">
										<digi:trn>There are not documents referenced</digi:trn>
										</c:if>	
										<c:if test="${aimEditActivityForm.documents.referenceDocs != null }">
										<table width="100%" bgcolor="#cccccc" cellPadding="5" cellSpacing="1">
											<tr bgcolor="#ffffff">
												<td>
													<table border="0" width="100%">
														<c:forEach items="${aimEditActivityForm.documents.referenceDocs}" var="refDoc" varStatus="loopstatus">
															<tr valign="top">
																<td>
																	<html:hidden name="aimEditActivityForm" property="documents.referenceDocs[${loopstatus.index}].checked" value="${refDoc.checked}"/>
																	<html:multibox onclick="toggleDiv(${loopstatus.index})" styleId="refCheck${loopstatus.index}" name="aimEditActivityForm" property="documents.allReferenceDocNameIds" value="${refDoc.categoryValueId}"/>
																</td>
																<td>
																	${refDoc.categoryValue}
																</td>
																<td>&nbsp;&nbsp;</td>
																<td width="100%">
																	<c:if test="${refDoc.checked}">
																		<div Id="refComment${loopstatus.index}" >
																			<html:textarea rows="4" cols="80" name="aimEditActivityForm" property="documents.referenceDocs[${loopstatus.index}].comment" />
																		</div>
																	</c:if>
																	<c:if test="${! refDoc.checked}">
																		<div Id="refComment${loopstatus.index}" style="display: none;" >
																			<html:textarea rows="4" cols="80" name="aimEditActivityForm" property="documents.referenceDocs[${loopstatus.index}].comment" />
																		</div>
																	</c:if>

																</td>
															</tr>
														</c:forEach>
													</table>
												</td>
											</tr>
										</table>
										</c:if>
										</td>
									</tr>

									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>


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
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



