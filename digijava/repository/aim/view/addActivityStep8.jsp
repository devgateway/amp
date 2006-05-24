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
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot()" title="Click here to view MyDesktop ">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>				
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
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 2" >						
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 3">						
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;							
									<digi:link href="/addActivity.do?step=4&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 4">						
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<digi:link href="/addActivity.do?step=5&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 5">						
									<digi:trn key="aim:addActivityStep5">
									Step 5
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=6&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 6">						
									<digi:trn key="aim:addActivityStep6">
									Step 6
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/addActivity.do?step=7&edit=true" styleClass="comment" 
									title="Click here to goto Add Activity Step 7">						
									<digi:trn key="aim:addActivityStep7">
									Step 7
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;									
									<digi:trn key="aim:addActivityStep8" >
									Step 8
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
												<digi:trn key="aim:step8of8ContactInformation">
													Step 8 of 8: Contact Information
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
										<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
										<b><digi:trn key="aim:contactInformation">Contact Information</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
											<tr>
												<td width="32%">
													&nbsp;
												</td>
												<td width="18%">
													<b>
													<digi:trn key="aim:firstName">
														First Name
													</digi:trn></b>
												</td>
												<td width="18%">
													<b>
													<digi:trn key="aim:lastName">
														Last Name
													</digi:trn></b>
												</td>
												<td width="18%">
													<b>
													<digi:trn key="aim:email">
														Email
													</digi:trn></b>
												</td>												
											</tr>
											<tr>
												<td align="right">
													<digi:trn key="aim:donorFundingContactInformation">
													Donor funding contact information</digi:trn>	
												</td>
												<td>
													<html:text property="dnrCntFirstName" styleClass="inp-text"/>
												</td>
												<td>
													<html:text property="dnrCntLastName" styleClass="inp-text"/>												
												</td>
												<td>
													<html:text property="dnrCntEmail" styleClass="inp-text"/>												
												</td>												
											</tr>
											<tr>
												<td align="right">
													<digi:trn key="aim:mofedContactInformation">
													MOFED contact information</digi:trn>	
												</td>
												<td>
													<html:text property="mfdCntFirstName" styleClass="inp-text"/>
												</td>
												<td>
													<html:text property="mfdCntLastName" styleClass="inp-text"/>												
												</td>
												<td>
													<html:text property="mfdCntEmail" styleClass="inp-text"/>												
												</td>												
											</tr>											
										</table>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<input type="button" value=" << Back " class="dr-menu" onclick="gotoStep(7)">
												</td>
												<logic:notEmpty name="ME" scope="application">												 										
													<td>
														<input type="submit" value="Next >> " class="dr-menu" onclick="gotoStep(10)">
													</td>
												</logic:notEmpty>
												<logic:empty name="ME" scope="application">												 										
													<td>
													<input type="button" value="Preview" class="dr-menu" onclick="previewClicked()">
													</td>
												</logic:empty>												
												<td>
													<input type="reset" value="Reset" class="dr-menu" onclick="return resetAll()">
												</td>
											</tr>
										</table>
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
