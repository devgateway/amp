<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


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
		openNewWindow(600, 270);
		document.aimEditActivityForm.docFileOrLink.value = "file";
		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selDoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function addManagedDocuments() {
		openNewWindow(600, 230);
		document.aimEditActivityForm.docFileOrLink.value = "document";
		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selDoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function addLinks() {
		openNewWindow(600, 225);
		document.aimEditActivityForm.docFileOrLink.value = "link";
		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selDoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function removeSelDocuments() {
	var flag = validate(1);
	if (flag == false) return false;
	document.aimEditActivityForm.docFileOrLink.value = "file";
	<digi:context name="remDoc" property="context/module/moduleinstance/removeSelDocuments.do?edit=true" />
	document.aimEditActivityForm.action = "<%= remDoc %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

function removeSelManagedDocuments() {
	//var flag = validate(1);
	//if (flag == false) return false;
	document.aimEditActivityForm.docFileOrLink.value = "document";
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
	document.aimEditActivityForm.docFileOrLink.value = "link";
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
<html:hidden property="docFileOrLink" />


<html:hidden property="editAct" />

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
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</bean:define>
									<digi:link href="/admin.do" styleClass="comment" title="<%translation%>">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">								
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</bean:define>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot()" title="<%=translation%>">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>				
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to go to Add Activity Step 1</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="<%=translation%>">
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
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="<%=translation%>">						
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" title="<%=translation%>">
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to goto Add Activity Step 4</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" title="<%=translation%>">
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep5">Click here to goto Add Activity Step 5</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" title="<%=translation%>">
									<digi:trn key="aim:addActivityStep5">
									Step 5
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;												
									<digi:trn key="aim:addActivityStep6">
									Step 6
									</digi:trn>						
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
												<digi:trn key="aim:step6of9RelatedDocumentsandLinks">								
													Step 6 of 9: Related Documents and Links
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
								<!-- contents -->
							<jsp:include page="addActivityStep6RelatedDocuments.jsp"/>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
							<jsp:include page="addActivityStep6WebResources.jsp"/>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<!-- Managed Documents -->
										<jsp:include page="addActivityStep6ManagedDocuments.jsp"/>
									<tr><td>
										&nbsp;
									</td></tr>
<!-- 
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<html:button  styleClass="dr-menu" property="submitButton" onclick="gotoStep(5)">
															<< <digi:trn key="btn:back">Back</digi:trn>
													</html:button>

												</td>
												<td>
													<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(7)">
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
								</table>

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
