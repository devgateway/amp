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
	if (document.aimEditActivityForm.currUrl.value == "") {
		openNewWindow(600, 230);
		document.aimEditActivityForm.docFileOrLink.value = "file";
		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selDoc %>";
		document.aimEditActivityForm.currUrl.value = "<%= selDoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();	  
	}
}

function addLinks() {
	if (document.aimEditActivityForm.currUrl.value == "") {
		openNewWindow(600, 225);
		document.aimEditActivityForm.docFileOrLink.value = "link";
		<digi:context name="selDoc" property="context/module/moduleinstance/selectDocument.do?edit=true" />
		document.aimEditActivityForm.action = "<%= selDoc %>";
		document.aimEditActivityForm.currUrl.value = "<%= selDoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();	  
	}
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

<input type="hidden" name="currUrl">

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
									</digi:trn>
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
												<digi:trn key="aim:step6of8RelatedDocumentsandLinks">								
													Step 6 of 8: Related Documents and Links
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

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:DocumentsRelated">Document related to the project</digi:trn>">
										<b><digi:trn key="aim:relatedDocuments">Related Documents</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="documentList">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="documentList"
												id="selDocuments" type="org.digijava.module.cms.dbentity.CMSContentItem">
												<tr>
													<td>
														<html:multibox property="selDocs">
															<bean:write name="selDocuments" property="id" />
														</html:multibox>
														<bean:write name="selDocuments" property="title" /> - 

														<c:if test="${!empty selDocuments.fileName}">
							   							<bean:define id="fileName" name="selDocuments" 
															property="fileName" />
														    <%
															int index2;
															String extension = null;
															index2 = ((String)fileName).lastIndexOf(".");	
															if( index2 >= 0 ) {
															   extension = "module/cms/images/extensions/" + 
																((String)fileName).substring(
																index2 + 1,((String)fileName).length()) + ".gif";
															}
														    %>
														    <digi:img skipBody="true" src="<%=extension%>" border="0" 
															 align="absmiddle"/>	
														</c:if>
															<i>
														<bean:write name="selDocuments" property="fileName" /></i>	
													</td>
												</tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<td>
																<input type="button" value="Add Documents" class="buton" 
																onclick="addDocuments()" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Documents" class="buton" 
																onclick="return removeSelDocuments()" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="documentList">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Documents" class="buton" 
														onclick="addDocuments()" class="buton">
													</td>
												</tr>
											</table>
										</logic:empty>										
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">										  			  <b><digi:trn key="aim:webResource">Web resources</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="linksList">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="linksList"
												id="selWebLinks" type="org.digijava.module.cms.dbentity.CMSContentItem">
												<tr>
													<td>
														<html:multibox property="selLinks">
															<bean:write name="selWebLinks" property="id" />
														</html:multibox>
														<bean:write name="selWebLinks" property="title" /> - 
														<a href="<bean:write name="selWebLinks" property="url" />" target="_blank">
														<bean:write name="selWebLinks" property="url" />
													</td>
												</tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<td>
																<input type="button" value="Add Web Resources" class="buton" 
																onclick="addLinks()" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Web Resources" class="buton" 
																onclick="return removeSelLinks()" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="linksList">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Web Resources" class="buton" 
														onclick="addLinks()" class="buton">
													</td>
												</tr>
											</table>
										</logic:empty>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<input type="button" value=" << Back " class="dr-menu" onclick="gotoStep(5)">
												</td>
												<td>
													<input type="submit" value="Next >> " class="dr-menu" onclick="gotoStep(7)">
												</td>
												<td>
													<input type="reset" value="Reset" class="dr-menu" onclick="return resetAll()">
												</td>
											</tr>
										</table>
									</td></tr>
									
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
