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

function validatePhyProg() {
	if (document.aimEditActivityForm.selPhyProg.checked != null) { 
		if (document.aimEditActivityForm.selPhyProg.checked == false) {
			alert("Please choose a physical progress to remove");
			return false;
		}
	} else {
		var length = document.aimEditActivityForm.selPhyProg.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimEditActivityForm.selPhyProg[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("Please choose a physical progress to remove");
			return false;					  
		}
	}
	return true;		  
}

function validateComponents() {
	if (document.aimEditActivityForm.selComp.checked != null) { 
		if (document.aimEditActivityForm.selComp.checked == false) {
			alert("Please choose a component to remove");
			return false;
		}
	} else { 
		var length = document.aimEditActivityForm.selComp.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimEditActivityForm.selComp[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("Please choose a component to remove");
			return false;					  
		}
	}
	return true;		  
}

function addPhyProgess(id,comp) {
	if (document.aimEditActivityForm.currUrl.value == "" ||
						 document.aimEditActivityForm.prevId.value != id) {
		openNewWindow(610, 255);
		<digi:context name="addPhyProg" property="context/module/moduleinstance/showAddPhyProg.do" />
		if (id == -1) {
			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp;
		} else {
			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp + "~id=" + id;	
		}
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.currUrl.value = "<%=addPhyProg%>";
		document.aimEditActivityForm.prevId.value = id;
		document.aimEditActivityForm.submit();
	} else {
		popupPointer.focus();
	}
}

function removeSelPhyProgress() {
	var flag = validatePhyProg();
	if (flag == false) return false;
	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeSelPhyProg.do" />
	document.aimEditActivityForm.action = "<%= remPhyProg %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

function addComponents(id) {

	 if (document.aimEditActivityForm.currUrl.value == "") { 
		<digi:context name="addComponents" property="context/module/moduleinstance/showAddComponent.do" />
		if (id == -1) {
			document.aimEditActivityForm.action = "<%= addComponents %>";
		} else {
			document.aimEditActivityForm.action = "<%= addComponents %>~id=" + id;	
		}
		openNewWindow(610, 280);
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();			  
		document.aimEditActivityForm.currUrl.value = "<%=addComponents%>";
	} else {
		popupPointer.focus();
	}

}

function removeSelComponents() {
	var flag = validateComponents();
	if (flag == false) return false;
	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeSelPhyProg.do" />
	document.aimEditActivityForm.action = "<%= remPhyProg %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

-->
</script>

<digi:form action="/addActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="componentId" />

<input type="hidden" name="currUrl">
<input type="hidden" name="prevId">


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
										<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
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
									<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>																	
								<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams}" property="step" value="1" />
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
								</bean:define>
								<digi:link href="/addActivity.do" name="urlParams" styleClass="comment" title="<%=translation%>">
								
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
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=2" styleClass="comment" title="<%=translation%>">						
										<digi:trn key="aim:addActivityStep2">
											Step 2
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;			
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep3">Click here to goto Add Activity Step 3</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=3" styleClass="comment" title="<%=translation%>">						
										<digi:trn key="aim:addActivityStep3">
											Step 3
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;							
									<digi:trn key="aim:addActivityStep4">
										Step 4
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
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">	
						<table bgcolor="#006699" cellPadding=2 cellSpacing=1 width="100%">
							<tr>
								<td vAlign="center" width="100%" align ="center" class="textalb" 
								height="20">
								<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">								
								<digi:trn key="aim:step4Components">Step 4 of 7: Components</digi:trn></a>
								</td>
							</tr>
							<tr><td width="100%" bgcolor="#f4f4f2">
							<table width="100%" cellSpacing="2" cellPadding="2" vAlign="top" align="left" bgcolor="#f4f4f2">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">										
										<b><digi:trn key="aim:components">Components</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="selectedComponents">
											<table width="100%" cellSpacing=1 cellPadding=4 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="selectedComponents"
												id="selComponents" type="org.digijava.module.aim.helper.Components">
												<tr><td align="center">
													<table width="98%" cellSpacing=1 cellPadding=4 vAlign="top" align="center" border=0
													class="box-border-nopadding">
														<tr>
															<td vAlign="center" align="left" width="95%">
																<a title="<digi:trn key="aim:TitleComponent">Title of the project component specified</digi:trn>">																
																<digi:trn key="aim:TitleofComponent">Component Title</digi:trn></a> :
																<a href="javascript:addComponents(<c:out value="${selComponents.componentId}"/>)">
																<bean:write name="selComponents" property="title" /></a><br>
																<a title="<digi:trn key="aim:ComponentAmt">Budgeted amount for the component based on total project award amount (Grant, Load and In-Kind)</digi:trn>">
																<digi:trn key="aim:componentAmount">Amount</digi:trn></a> :
																<c:if test="${selComponents.amount == '0.00'}">
																	<bean:write name="selComponents" property="amount" />
																</c:if>
																<c:if test="${selComponents.amount != '0.00'}">
																	<bean:write name="selComponents" property="amount" />&nbsp;
																	<bean:write name="selComponents" property="currencyCode" /> 
																</c:if><br>
																<c:if test="${!empty selComponents.reportingDate}">
																<a title="<digi:trn key="aim:DateofReporting">Date the activity was initiated</digi:trn>">
																<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></a> : 
																	<bean:write name="selComponents" property="reportingDate" />
																</c:if>
															</td>
															<td align="right">
																<bean:define id="compId" property="componentId" name="selComponents"/>
																<% String url = "/deleteComponent.do~id=" + compId;%>
																<digi:link href="<%=url%>">
															 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this component"/>
																</digi:link>
															</td>															
														</tr>	
														<tr>
															<td colspan="2">

															
																<table width="100%" cellPadding=1 cellSpacing=1 vAlign="top" border=0 
																class="box-border-nopadding">
																	<tr><td width="100%" bgcolor="#dddddd" height="15">
																	<a title="<digi:trn key="aim:PhysicalProgress">Measurable task done on a project to achieve component objectives</digi:trn>">
																		<digi:trn key="aim:physicalProgres">Physical Progress</digi:trn></a>
																	</td></tr>														
														
																	<c:if test="${empty selComponents.phyProgress}">
																	<tr>
																		<td colspan="2"><b>
																			<a href="javascript:
																			addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																			Add Physical Progress</a></b>
																		</td>
																	</tr>															
																	</c:if>
																	<c:if test="${!empty selComponents.phyProgress}">
																	<c:forEach var="phyProg" items="${selComponents.phyProgress}">
																	<tr>
																		<td>
																			<table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
																				<tr>
																					<td vAlign="center" align="left" width="95%">
																						<a href="javascript:addPhyProgess(<bean:write name="phyProg" 
																						property="pid" />,<c:out value="${selComponents.componentId}"/>)">
																						<bean:write name="phyProg" property="title" /> - 
																						<bean:write name="phyProg" property="reportingDate" />
																						</a>
																					</td>
																					<td align="right">
																						<bean:define id="id" property="pid" name="phyProg"/>
																						<% String url1 = "/removeSelPhyProg.do~pid="+id+"~cid="+compId;%>
																						<digi:link href="<%=url1%>">
																					 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" 
																						alt="Delete this physical progress"/>
																						</digi:link>
																					</td>																							
																				</tr>	
																			</table>	
																		</td>
																	</tr>
																	</c:forEach>
																	<tr>
																		<td>
																			<table cellSpacing=2 cellPadding=2>
																				<tr><td>
																					<b>
																					<a href="javascript:
																					addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																					Add Physical Progress</a></b>
																				</td></tr>
																			</table>																
																		</td>
																	</tr>
																	</c:if>
																</table>													
															</td>
														</tr>
													</table>	
												</td></tr>	
												</logic:iterate>	
												<tr><td align="center">
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<td>
																<input type="button" value="Add Components" class="buton" name="addComponent" 
																onclick="addComponents(-1)" class="buton">
															</td>
														</tr>
													</table>
												</td></tr>
											</table>												
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="selectedComponents">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<tr><td>
													<input type="button" value="Add Components" class="buton" name="addComponent" 
													onclick="addComponents(-1)" class="buton">
												</td></tr>
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
							<tr><td bgColor=#f4f4f2 align="center">
								<table cellPadding=3>
									<tr>
										<td>
											<input type="submit" value=" << Back " class="dr-menu" onclick="gotoStep(3)">
										</td>
										<td>
											<input type="submit" value="Next >> " class="dr-menu" onclick="gotoStep(5)">
										</td>
										<td>
											<input type="reset" value="Reset" class="dr-menu">
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
