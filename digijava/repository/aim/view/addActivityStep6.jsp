<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

function addOrgs(value) {
	if (document.aimEditActivityForm.currUrl.value == "" ||
						 document.aimEditActivityForm.prevOrg.value != value) { 		  			  			  		  
		openNewWindow(600, 400);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do~orgSelReset=true" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>~item="+value;
		document.aimEditActivityForm.currUrl.value = "<%= selectOrganization %>";
		document.aimEditActivityForm.prevOrg.value = value;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();
	}
} 	
function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function removeSelOrgs(value) {
	document.aimEditActivityForm.item.value = value;
	<digi:context name="remOrgs" property="context/module/moduleinstance/removeSelRelOrgs.do" />
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
<input type="hidden" name="currUrl">
<input type="hidden" name="prevOrg">
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
									<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>">
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
								<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams}" property="step" value="1" />
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to go to Add Activity Step 1</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do" name="urlParams" styleClass="comment" title="<%=translation%>" >
								
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivityStep1">
										Edit Activity - Step 1
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addActivityStep1">
										Add Activity - Step 1
									</digi:trn>
								</c:if>																
								</digi:link>&nbsp;&gt;&nbsp;						
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep2">Click here to go to Add Activity Step 2</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do?step=2" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep2">
									Step 2
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep3">Click here to go to Add Activity Step 3</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=3" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep3">
									Step 3
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;							
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep4">Click here to go to Add Activity Step 4</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=4" styleClass="comment" title="<%=translation%>" >						
									<digi:trn key="aim:addActivityStep4">
									Step 4
									</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep5">Click here to go to Add Activity Step 5</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=5" styleClass="comment" title="<%=translation%>" >						
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
								<c:if test="${aimEditActivityForm.edit == false}">
									<digi:trn key="aim:addNewActivity">
										Add New Activity
									</digi:trn>
								</c:if>			
								<c:if test="${aimEditActivityForm.edit == true}">
									<digi:trn key="aim:editActivity">
										Edit Activity
									</digi:trn>
								</c:if>				
							</td>
						</tr>	
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="top">
						<tr><td width="75%" vAlign="top">	
						<table cellPadding=2 cellSpacing=1 width="100%" bgcolor="#006699">
							<tr>
								<td vAlign="center" width="100%" align ="center" class="textalb" 
								height="20">
								<digi:trn key="aim:step6RelatedOrganizations">
								Step 6 of 7: Related Organizations
								</digi:trn>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top" align="left" bgcolor="#f4f4f2">							
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">

								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:AgencyExecuting">The organization that receives the funds from the funding country/agency, and coordinates the project</digi:trn>">
										<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="executingAgencies">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="executingAgencies"
												id="exAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#ffffff">
														<tr>
															<td width="3">
																<html:multibox property="selExAgencies">
																	<bean:write name="exAgency" property="ampOrgId" />
																</html:multibox>															
															</td>
															<td align="left">
																<bean:write name="exAgency" property="name" />
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<input type="button" value="Add Organizations" class="buton" 
																onclick="addOrgs(1)" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Selected Organizations" class="buton" 
																onclick="return removeSelOrgs(1)" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="executingAgencies">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Organizations" class="buton" 
														onclick="addOrgs(1)">
													</td>
												</tr>
											</table>
										</logic:empty>										
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
								</table>

								<!-- end contents -->
							</td></tr>
							
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:AgencyImplementing">The organization that directly implements the activity</digi:trn>">
										<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="impAgencies"
												id="impAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="3">
																<html:multibox property="selImpAgencies">
																	<bean:write name="impAgency" property="ampOrgId" />
																</html:multibox>															
															</td>
															<td align="left">
																<bean:write name="impAgency" property="name" />
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<input type="button" value="Add Organizations" class="buton" 
																onclick="addOrgs(2)" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Selected Organizations" class="buton" 
																onclick="return removeSelOrgs(2)" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="impAgencies">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Organizations" class="buton" 
														onclick="addOrgs(2)">
													</td>
												</tr>
											</table>
										</logic:empty>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
								</table>

								<!-- end contents -->
							</td></tr>
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->



								<table width="95%" bgcolor="#f4f4f2">
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
								</table>


								
								<%--

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:contractor">Contractor</digi:trn></b>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="contractors">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="contractors"
												id="cont" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="3">
																<html:multibox property="selContractors">
																	<bean:write name="cont" property="ampOrgId" />
																</html:multibox>															
															</td>
															<td align="left">
																<bean:write name="cont" property="name" />
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<input type="button" value="Add Organizations" class="buton" 
																onclick="addOrgs(3)" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Selected Organizations" class="buton" 
																onclick="return removeSelOrgs(3)" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="contractors">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Organizations" class="buton" 
														onclick="addOrgs(3)">
													</td>
												</tr>
											</table>
										</logic:empty>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
								</table>
								--%>
								<!-- end contents -->
							</td></tr>					
							
							<%--
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:reportingAgency">Reporting Agency</digi:trn></b>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="reportingOrgs">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="reportingOrgs"
												id="repOrgs" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#ffffff">
														<tr>
															<td width="3">
																<html:multibox property="selReportingOrgs">
																	<bean:write name="repOrgs" property="ampOrgId" />
																</html:multibox>															
															</td>
															<td align="left">
																<bean:write name="repOrgs" property="name" />
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<input type="button" value="Add Organizations" class="buton" 
																onclick="addOrgs(4)" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Selected Organizations" class="buton" 
																onclick="return removeSelOrgs(4)" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										
										<logic:empty name="aimEditActivityForm" property="reportingOrgs">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<input type="button" value="Add Organizations" class="buton" 
														onclick="addOrgs(4)">
													</td>
												</tr>
											</table>
										</logic:empty>										
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
								</table>
								<!-- end contents -->							
							</td></tr>							
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>														
							--%>
							
							
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
