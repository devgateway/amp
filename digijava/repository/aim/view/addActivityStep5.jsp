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

function checkallIssues() {
	var selectbox = document.aimEditActivityForm.checkAllIssues;
	var items = document.aimEditActivityForm.selIssues;
	if (document.aimEditActivityForm.selIssues.checked == true || 
						 document.aimEditActivityForm.selIssues.checked == false) {
			  document.aimEditActivityForm.selIssues.checked = selectbox.checked;
	} else {
		for(i=0; i<items.length; i++){
			document.aimEditActivityForm.selIssues[i].checked = selectbox.checked;
		}
	}
}

function addIssues() {
	openNewWindow(610, 160);
	<digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addIssue%>&issueId=-1";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function updateIssues(id) {
	openNewWindow(610, 160);
	<digi:context name="addIssue" property="context/module/moduleinstance/showUpdateIssue.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addIssue%>&issueId="+id;
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function removeIssues() {
	<digi:context name="addIssue" property="context/module/moduleinstance/removeIssue.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addIssue%>";				  
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();		  
}

function addMeasures(issueId) {
	openNewWindow(610, 160);
	<digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addMeasure%>&issueId="+issueId+"&measureId=-1";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function updateMeasures(issueId,measureId) {
	openNewWindow(610, 160);
	<digi:context name="addMeasure" property="context/module/moduleinstance/showUpdateMeasure.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addMeasure%>&issueId="+issueId+"&measureId="+measureId;
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function removeMeasure(issueId) {
	<digi:context name="removeMeasure" property="context/module/moduleinstance/removeMeasure.do?edit=true" />
	document.aimEditActivityForm.action = "<%=removeMeasure%>&issueId="+issueId;
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();		  
}

function addActors(issueId,measureId) {
	openNewWindow(610, 160);
	<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addActors%>&issueId="+issueId+"&measureId="+measureId+"&actorId=-1";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function updateActor(issueId,measureId,actorId) {
	openNewWindow(610, 160);
	<digi:context name="addActors" property="context/module/moduleinstance/showUpdateActors.do?edit=true" />
	document.aimEditActivityForm.action = "<%=addActors%>&issueId="+issueId+"&measureId="+measureId+"&actorId="+actorId;
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function removeActors(issueId,measureId) {
	<digi:context name="removeActors" property="context/module/moduleinstance/removeActors.do?edit=true" />
	document.aimEditActivityForm.action = "<%=removeActors%>&issueId="+issueId+"&measureId="+measureId;
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();		  
}

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
		openNewWindow(610, 255);
		<digi:context name="addPhyProg" property="context/module/moduleinstance/showAddPhyProg.do~edit=true" />
		if (id == -1) {
			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp;
		} else {
			document.aimEditActivityForm.action = "<%= addPhyProg %>~comp=" + comp + "~id=" + id;	
		}
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.prevId.value = id;
		document.aimEditActivityForm.submit();
}

function removeSelPhyProgress() {
	var flag = validatePhyProg();
	if (flag == false) return false;
	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />
	document.aimEditActivityForm.action = "<%= remPhyProg %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}


/*
function addComponents(id) {

		<digi:context name="addComponents" property="context/module/moduleinstance/showAddComponent.do~edit=true" />
		if (id == -1) {
			document.aimEditActivityForm.action = "<%= addComponents %>";
		} else {
			document.aimEditActivityForm.action = "<%= addComponents %>~id=" + id;	
		}
		openNewWindow(610, 280);
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();			  
}
*/

function addComponents() 
{
	openNewWindow(650,500 );
	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=show" />
	document.aimEditActivityForm.action = "<%= addComp %>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
} 		

function editFunding(id) 
{
	openNewWindow(650,500 );
	<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=showEdit" />
	document.aimEditActivityForm.action = "<%= addComp %>&fundId="+id;
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
} 		

/*
function removeSelComponents() 
{
	<digi:context name="rem" property="context/module/moduleinstance/removeSelPhyProg.do?edit=true" />
	document.aimEditActivityForm.action = "<%= rem %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();	
}
*/

function resetAll()
{
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}


function removeSelComponents() {
	var flag = validateComponents();
	if (flag == false) return false;
	<digi:context name="remPhyProg" property="context/module/moduleinstance/removeComponent.do?edit=true" />
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

<input type="hidden" name="prevId">

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
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot()" title="<%=translation%>">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;								
								</c:if>																	
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to goto Add Activity Step 1</digi:trn>
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
									
									<digi:trn key="aim:addActivityStep5">
										Step 5
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
					<digi:errors/>
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
												<digi:trn key="aim:step5of9Components">Step 5 of 9: Components</digi:trn>
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
										<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">										
										<b><digi:trn key="aim:components">Components</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
								
									<tr><td align="left">
										<table width="100%" cellSpacing=5 cellPadding=0 border=0 class="box-border-nopadding">

										<logic:notEmpty name="aimEditActivityForm" property="selectedComponents">
											<logic:iterate name="aimEditActivityForm" property="selectedComponents"
											id="selComponents" type="org.digijava.module.aim.helper.Components">
										
												<tr><td align="center">
													<table width="98%" cellSpacing=1 cellPadding=4 vAlign="top" align="center" border=0
													class="box-border-nopadding">
														<tr bgcolor="#fffffc">
															<td vAlign="center" align="left" width="95%">
																<html:multibox property="selComp">
																	<c:out value="${selComponents.componentId}"/>
																</html:multibox>
																<a title="<digi:trn key="aim:TitleofComponent">Title of the project component specified</digi:trn>">											<b>					
																<digi:trn key="aim:TitleofComponent">Component Title</digi:trn></a> :
																<c:out value="${selComponents.title}"/></b>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<a href="javascript:editFunding('<bean:write name="selComponents" 
																property="componentId"/>')">Edit</a><br>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${selComponents.description}"/>
																</a><br>
															</td>
														</tr>		

																<tr><td>
																<!-- Component funding details -->
																	<table width="100%" cellSpacing=1 cellPadding=3 border=0 
																	bgcolor="#d7eafd">
																		<logic:notEmpty name="selComponents" property="commitments">
																		<tr><td><b>
																			<digi:trn key="aim:commitments">Commitments</digi:trn> - (
																			<digi:trn key="aim:totalActualAllocation">Total actual 
																			allocation</digi:trn> = 
																			<c:out value="${aimEditActivityForm.totalCommitments}"/> 
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																		</b></td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0 
																			bgcolor="#eeeeee">
																				<tr>
																					<td>Actual/Planned</td>
																					<td>Total Amount</td>
																					<td>Currency</td>
																					<td>Date</td>
																					<td>Perspective</td>
																				</tr>
																				<logic:iterate name="selComponents" property="commitments" 
																				id="commitment" 
																				type="org.digijava.module.aim.helper.FundingDetail"> 																						           <!-- L2 START-->
																					<tr bgcolor="#ffffff">
																						<td>
																							<c:out value="${commitment.adjustmentTypeName}"/></td>
																						<td align="right">
																							<FONT color=blue>*</FONT>
																							<c:out value="${commitment.transactionAmount}"/>
																						</td>
																						<td>
																							<c:out value="${commitment.currencyCode}"/>
																						</td>
																						<td>
																							<c:out value="${commitment.transactionDate}"/>
																						</td>
																						<td>
																							<c:out value="${commitment.perspectiveName}"/>
																						</td>
																					</tr>																			
																				</logic:iterate>	<!-- L2 END-->
																			</table>	
																		</td></tr>
																	</logic:notEmpty>
																	<logic:notEmpty name="selComponents" property="disbursements">
																		<tr><td><b>
																			<digi:trn key="aim:disbursements">Disbursements</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> = 
																			<c:out value="${aimEditActivityForm.totalDisbursements}"/> 
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																	</b>		
																		</td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0 
																			bgcolor="#eeeeee">
																				<tr>
																					<td>Actual/Planned</td>
																					<td>Total Amount</td>
																					<td>Currency</td>
																					<td>Date</td>
																					<td>Perspective</td>
																				</tr>
																				<logic:iterate name="selComponents" property="disbursements" 
																				id="disbursement" 
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L3 START-->
																					<tr bgcolor="#ffffff">
																						<td>
																							<c:out value="${disbursement.adjustmentTypeName}"/>
																						</td>
																						<td align="right">
																							<FONT color=blue>*</FONT>
																							<c:out value="${disbursement.transactionAmount}"/>
																						</td>
																						<td>
																							<c:out value="${disbursement.currencyCode}"/>
																						</td>
																						<td>
																							<c:out value="${disbursement.transactionDate}"/>
																						</td>
																						<td>
																							<c:out value="${disbursement.perspectiveName}"/>
																						</td>
																					</tr>																			
																				</logic:iterate>	
																				<!-- L3 END-->
																			</table>																		
																		</td></tr>				

																	</logic:notEmpty>
																	<logic:notEmpty name="selComponents" property="expenditures">
																		<tr><td><b>
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn> - (
																			<digi:trn key="aim:totalActualToDate">Total actual to date
																			</digi:trn> = 
																			<c:out value="${aimEditActivityForm.totalExpenditures}"/> 
																			<c:out value="${aimEditActivityForm.currCode}"/>)
																	</b>
																		</td></tr>
																		<tr><td bgcolor=#ffffff>
																			<table width="100%" cellSpacing=1 cellPadding=3 border=0 
																			bgcolor="#eeeeee">
																				<tr>
																					<td>Actual/Planned</td>
																					<td>Total Amount</td>
																					<td>Currency</td>
																					<td>Date</td>
																					<td>Perspective</td>
																				</tr>
																				<logic:iterate name="selComponents" property="expenditures" 
																				id="expenditure" 
																				type="org.digijava.module.aim.helper.FundingDetail">
																				<!-- L4 START-->
																				<tr bgcolor="#ffffff">
																					<td>
																						<c:out value="${expenditure.adjustmentTypeName}"/>
																					</td>
																					<td align="right">
																						<FONT color=blue>*</FONT>
																						<c:out value="${expenditure.transactionAmount}"/>
																					</td>
																					<td>
																						<c:out value="${expenditure.currencyCode}"/>
																					</td>
																					<td>
																						<c:out value="${expenditure.transactionDate}"/>
																					</td>
																					<td>
																						<c:out value="${expenditure.perspectiveName}"/>
																					</td>
																				</tr>
																			</logic:iterate>	
																			<!-- L4 END-->
																		</table>
																	</td></tr>
																	
																</logic:notEmpty>
																
															</table>
															<logic:notEmpty name="selComponents" property="commitments">
															<TR><TD>
																<FONT color=blue>*
																	<digi:trn key="aim:allTheAmountsInThousands">	
																		All the amounts are in thousands (000)
				  													</digi:trn>
																</FONT>								
															</TD></TR>															
															</logic:notEmpty>
														</td></tr>
													</tr>	
													<tr>
														<td colspan="2">
															<table width="100%" cellPadding=1 cellSpacing=1 vAlign="top" border=0 
															class="box-border-nopadding">
															<tr><td width="100%" bgcolor="#dddddd" height="15">
																<a title="<digi:trn key="aim:physicalProgressTitleDesc">Measurable task done on a 
																project to achieve component objectives</digi:trn>">
																	<digi:trn key="aim:physicalProgres">Physical Progress</digi:trn>
																</a>
															</td></tr>														
															<c:if test="${empty selComponents.phyProgress}">
																<tr><td colspan="2"><b>
																	<a href="javascript:
																	addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																	Add Physical Progress</a>
																	</b>
																</td></tr>															
															</c:if>
															<c:if test="${!empty selComponents.phyProgress}">
															<c:forEach var="phyProg" items="${selComponents.phyProgress}">
																<tr><td>
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
																			<bean:define id="compId" property="componentId" name="selComponents"/>
																			<% String url1 = 
																			"/removeSelPhyProg.do~edit=true~pid="+id+"~cid="+compId;%>
																			<digi:link href="<%=url1%>">
																				<digi:img src="module/cms/images/deleteIcon.gif" border="0" 
																				alt="Delete this physical progress"/>
																			</digi:link>
																		</td>																																								  </tr>	
																	</table>	
																</td></tr>
															</c:forEach>
																<tr><td>
																	<table cellSpacing=2 cellPadding=2>
																		<tr><td>
																			<b>
																				<a href="javascript:
																				addPhyProgess(-1,<c:out value="${selComponents.componentId}"/>)">
																				Add Physical Progress</a>
																			</b>
																		</td></tr>
																	</table>																
																</td></tr>
															</c:if>
															</table>													
														</td></tr>
													</table>	
												</td></tr>	
											</logic:iterate>	
											<tr><td align="center">
												<table cellSpacing=2 cellPadding=2>
													<tr><td>
														<input type="button" value="Add Components" class="buton" name="addComponent" 
														onclick="addComponents()" class="buton"> &nbsp;&nbsp;&nbsp;
														<input type="button" value="Remove Components" class="buton" name="removeComponent" 
														onclick="removeSelComponents()" class="buton">														
													</td></tr>
												</table>
											</td></tr>
										</table>												
									</logic:notEmpty>
									<logic:empty name="aimEditActivityForm" property="selectedComponents">
										<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
											<tr><td>
												<input type="button" value="Add Components" class="buton" name="addComponent" 
												onclick="addComponents()" class="buton">
											</td></tr>
										</table>
									</logic:empty>
								</td></tr>

								
								<!-- Issues , Measures and Actions -->
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:issuesForTheActivity">The issues for the activity</digi:trn>">
										<b><digi:trn key="aim:issues">Issues</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="issues">
											<table width="100%" cellSpacing=1 cellPadding=4 class="box-border-nopadding">
												<tr><td align="center">
													<table width="98%" cellSpacing=1 cellPadding=2 vAlign="top" align="center" bgcolor="#dddddd">
														<%--<tr bgcolor="#d7eafd">--%>
														<tr bgcolor="#ffd5d5">
															<td vAlign="center" align="left" width="3">
																<input type="checkbox" name="checkAllIssues" onclick="checkallIssues()">
															</td>														
															<td vAlign="center" align="left">
																<b><digi:trn key="aim:issues">Issues</digi:trn></b>
															</td>
														</tr>
														<% int i = 0; 
															String rowClass = "";
														%>
														
														<logic:iterate name="aimEditActivityForm" property="issues"
														id="issues" type="org.digijava.module.aim.helper.Issues">
														<% if ((i % 2) != 0) { 
															rowClass = "rowAlternate";
															} else {
															rowClass = "rowNormal";
															} 
															i++; 
														%>
														
														<tr class="<%=rowClass%>">
															<td vAlign="center" align="left" width="3">
																<html:multibox property="selIssues">
																	<c:out value="${issues.id}"/>
																</html:multibox>
															</td>														
															<td vAlign="center" align="left">
																<a href="javascript:updateIssues('<c:out value="${issues.id}"/>')">
																<c:out value="${issues.name}"/></a>
															</td>
														</tr>
														<tr class="<%=rowClass%>">
															<td vAlign="center" align="left" width="3">
															</td>														
															<td vAlign="center" align="left">
																<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0 
																bgcolor="#dddddd">
																	<tr class="<%=rowClass%>">
																		<td align="left" colspan="2">
																			<b><digi:trn key="aim:measures">Measures</digi:trn></b>&nbsp;&nbsp;
																			<a href="javascript:addMeasures('<c:out value="${issues.id}"/>')">
																			Add Measures</a>
																		</td>																	
																	</tr>																
																	<logic:notEmpty name="issues" property="measures">
																	<logic:iterate name="issues" property="measures" id="measure"
																	 type="org.digijava.module.aim.helper.Measures">
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																			<html:multibox property="selMeasures">
																				<c:out value="${measure.id}"/>
																			</html:multibox>
																		</td>														
																		<td vAlign="center" align="left">
																			<a href="javascript:updateMeasures('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')">
																			<c:out value="${measure.name}"/>
																		</td>																		
																	</tr>
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																		</td>														
																		<td vAlign="center" align="left">
																			<table width="100%" cellPadding=4 cellSpacing=1 vAlign="top" border=0 
																			bgcolor="#dddddd">
																				<tr class="<%=rowClass%>">
																					<td align="left" colspan="2">
																						<b><digi:trn key="aim:actors">Actors</digi:trn></b>&nbsp;&nbsp;
																						<a href="javascript:addActors('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')">Add Actors</a>
																					</td>																	
																				</tr>																
																				<logic:notEmpty name="measure" property="actors">
																				<logic:iterate name="measure" property="actors" id="actor"
																				 type="org.digijava.module.aim.dbentity.AmpActor">
																				<tr class="<%=rowClass%>">
																					<td vAlign="center" align="left" width="3">
																						<html:multibox property="selActors">
																							<c:out value="${actor.ampActorId}"/>
																						</html:multibox>
																					</td>														
																					<td vAlign="center" align="left">
																						<a href="javascript:updateActor('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>','<c:out value="${actor.ampActorId}"/>')">
																							<c:out value="${actor.name}"/>
																					</td>																		
																				</tr>
																				</logic:iterate>		
																				<tr class="<%=rowClass%>">
																					<td vAlign="center" align="left" width="3">
																					</td>														
																					<td vAlign="center" align="left">
																						<input type="button" value="Remove Actors" class="buton"
																						onclick="removeActors('<c:out value="${issues.id}"/>','<c:out value="${measure.id}"/>')" 
																						class="buton">
																					</td>
																				</tr>
																				</logic:notEmpty>
																			</table>
																		</td>														
																	</tr>
																	</logic:iterate>		
																	<tr class="<%=rowClass%>">
																		<td vAlign="center" align="left" width="3">
																		</td>														
																		<td vAlign="center" align="left">
																			<input type="button" value="Remove Measures" class="buton"
																			onclick="removeMeasure('<c:out value="${issues.id}"/>')" class="buton">
																		</td>
																	</tr>
																	</logic:notEmpty>
																</table>
															</td>
														</tr>														
														</logic:iterate>	
													</table>	
												</td></tr>	
												<tr><td align="center">
													<table cellSpacing=2 cellPadding=2>
														<tr>
															<td>
																<input type="button" value="Add Issues" class="buton"
																onclick="addIssues()" class="buton">
															</td>
															<td>
																<input type="button" value="Remove Issues" class="buton"
																onclick="removeIssues()" class="buton">
															</td>															
														</tr>
													</table>
												</td></tr>
											</table>												
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="issues">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<tr><td>
													<input type="button" value="Add Issues" class="buton"
													onclick="addIssues()" class="buton">
												</td></tr>
											</table>
										</logic:empty>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<input type="submit" value=" << Back " class="dr-menu" onclick="gotoStep(4)">
												</td>
												<td>
													<input type="submit" value="Next >> " class="dr-menu" onclick="gotoStep(6)">
												</td>
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
