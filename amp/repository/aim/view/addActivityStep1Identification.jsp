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

<script language="JavaScript">
	function budgetCheckboxClick()
	{
		if (document.aimEditActivityForm.budget != null) {
			if((document.aimEditActivityForm.budget.checked==false))
			{
				try {
				 	document.getElementById("hbudget").value="false";
				 	document.getElementById("FY").style.display='none';
				 	document.getElementById("Vote").style.display='none';
				 	document.getElementById("Sub-Vote").style.display='none';
				 	document.getElementById("Sub-Program").style.display='none';
				 	document.getElementById("ProjectCode").style.display='none';
				 	document.getElementById("financial").style.display='none';
				}
				catch(e)
				{
					//This silent try/catch was added because some of the fields can be deactivated from FM
				}
			 }
			else if(document.aimEditActivityForm.budget.checked==true)
			{
				try {
				 	document.getElementById("hbudget").value="true";
				 	document.getElementById("FY").style.display='';
				 	document.getElementById("Vote").style.display='';
				 	document.getElementById("Sub-Vote").style.display='';
				 	document.getElementById("Sub-Program").style.display='';
				 	document.getElementById("ProjectCode").style.display='';
				 	document.getElementById("financial").style.display='';
				}
				catch(e)
				{
					//This silent try/catch was added because some of the fields can be deactivated from FM
				}
			}
		}
	}

function InitBud(){
	if(document.getElementById("hbudget").value=="true"){
	 	try {
			document.aimEditActivityForm.budget.checked=true;
		 	document.getElementById("FY").style.display='';
		 	document.getElementById("Vote").style.display='';
		 	document.getElementById("Sub-Vote").style.display='';
		 	document.getElementById("Sub-Program").style.display='';
		 	document.getElementById("ProjectCode").style.display='';
		 	document.getElementById("financial").style.display='';
		}
		catch(e)
		{
			//This silent try/catch was added because some of the fields can be deactivated from FM
		}
	}
	else{
	 	try {
			document.aimEditActivityForm.budget.checked=false;
		 	document.getElementById("FY").style.display='none';
		 	document.getElementById("Vote").style.display='none';
		 	document.getElementById("Sub-Vote").style.display='none';
		 	document.getElementById("Sub-Program").style.display='none';
		 	document.getElementById("ProjectCode").style.display='none';
		 	document.getElementById("financial").style.display='none';
		}
		catch(e)
		{
			//This silent try/catch was added because some of the fields can be deactivated from FM
		}
	}
}

function disableSelection(element) {

//alert("asd");
    element.onselectstart = function() {
      return false;
    };
    element.unselectable = "on";
    element.style.MozUserSelect = "none";
    element.style.cursor = "default";
}


function disableSelection1(target){

	for (i=0;i<target.childNodes.length;i++){
			//alert("ad "+i);
			nownode=target.childNodes[i];
			alert("ad "+i+" ::"+nownode);
			if (typeof nownode.onselectstart!="undefined") //IE route
				nownode.onselectstart=function(){return false}
			else if (typeof nownode.style.MozUserSelect!="undefined") //Firefox route
					nownode.style.MozUserSelect="none"
				else //All other route (ie: Opera)
			nownode.onmousedown=function(){return false}
			
	}

if (typeof target.onselectstart!="undefined") //IE route
	target.onselectstart=function(){return false}
else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
	target.style.MozUserSelect="none"
else //All other route (ie: Opera)
	target.onmousedown=function(){return false}
	//alert("Ad");
target.style.cursor = "default"
}

</script>


<digi:instance property="aimEditActivityForm" />

										<table width="100%" bgcolor="#cccccc" cellPadding=5 cellSpacing=1>
											<bean:define id="contentDisabled">false</bean:define>
											<c:set var="contentDisabled"><field:display name="Project Title" feature="Identification">false</field:display>
											</c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<field:display name="Project Title" feature="Identification"></field:display>
											<tr bgcolor="#ffffff">											
												<td valign="top" align="left">
													<FONT color=red>*</FONT>
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">Title used in donors or MoFED internal systems</digi:trn>">
													<digi:trn key="aim:projectTitle">Project Title</digi:trn>
													</a>
												
												</td>
												<td valign="top" align="left">
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">
													Title used in donors or MoFED internal systems
													</digi:trn>">
													<html:textarea name="aimEditActivityForm" property="identification.title" cols="60" rows="2" styleClass="inp-text"  disabled="${contentDisabled}"/>
													</a>
												</td>											
											</tr>
																						
												<field:display name="Objective" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ObjectivesAndComponentsofProject">The key objectives and main components of the project</digi:trn>">
												<digi:trn key="aim:objective">Objective</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="objKey">
																<c:out value="${aimEditActivityForm.identification.objectives}"/>
															</bean:define>
															<digi:edit key="<%=objKey%>"/>
												
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=objKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
															
																<a href="javascript:edit('<%=objKey%>')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>	
																&nbsp;
															
														<field:display name="Objectively Verifiable Indicators" feature="Identification">
															<a href="javascript:commentWin('objObjVerIndicators')" id="CommentObjObjVerIndicators"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Assumptions" feature="Identification">
															<a href="javascript:commentWin('objAssumption')" id="CommentObjAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Verifications" feature="Identification">
															<a href="javascript:commentWin('objVerification')" id="CommentObjVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
														</field:display>		
														</td>
													</tr>
												</table>												
											</td></tr>
											</field:display>
											
											<field:display name="Description" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
												<digi:trn key="aim:description">
												Description
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="descKey">
																<c:out value="${aimEditActivityForm.identification.description}"/>
															</bean:define>
			
															<digi:edit key="<%=descKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
																<a href="javascript:edit('<%=descKey%>')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>
															
															
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>

		
											<!-- Purpose -->
											<field:display name="Purpose" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:PurposeofProject">Purpose of the project</digi:trn>">
												<digi:trn key="aim:purpose">
												Purpose
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="purpKey">
																<c:out value="${aimEditActivityForm.identification.purpose}"/>
															</bean:define>
			
															<digi:edit key="<%=purpKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
			
															<a href="javascript:edit('<%=purpKey%>')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<a href="javascript:commentWin('purpObjVerIndicators')" id="CommentPurpObjVerInd"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
														
															&nbsp;
															<a href="javascript:commentWin('purpAssumption')" id="CommentPurpAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
															<a href="javascript:commentWin('purpVerification')" id="CommentPurpVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>

											<field:display name="Results" feature="Identification">
											<!-- Results -->
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ResultsofProject">Results of the project</digi:trn>">
												<digi:trn key="aim:results">
												Results
												</digi:trn>
												</a>
											</td>

											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="resKey">
																<c:out value="${aimEditActivityForm.identification.results}"/>
															</bean:define>
			
															<digi:edit key="<%=resKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
			
															<a href="javascript:edit('<%=resKey%>')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<a href="javascript:commentWin('resObjVerIndicators')" id="CommentResObjVerInd"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															
															&nbsp;
															<a href="javascript:commentWin('resAssumption')" id="CommentResAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
															<a href="javascript:commentWin('resVerification')" id="CommentResVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
											
											<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
										
											<bean:define id="largeTextLabel" value="Lessons Learned" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
			
			
			
											<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.projectImpact}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>

											<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.activitySummary}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.contractingArrangements}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.condSeq}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.linkedActivities}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.conditionality}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.projectManagement}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
											
											
											
																		
											
											<field:display name="Accession Instrument" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfAccessionInstrument">Accession Instrument of the project</digi:trn>">
												<digi:trn key="aim:AccessionInstrument">
												Accession Instrument
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.accessionInstrument" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCESSION_INSTRUMENT_NAME %>" styleClass="inp-text" />
											</td></tr>	
											</field:display>

											<field:display name="Project Category" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfProjectCategory">Project Category</digi:trn>">
												<digi:trn key="aim:ProjectCategory">
												Project Category
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.projectCategory" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROJECT_CATEGORY_NAME %>" styleClass="inp-text" />													
											</td></tr>	
											</field:display>


											<field:display name="Government Agreement Number" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:step1:GovernmentAgreementNumTooltip">Government Agreement Number</digi:trn>">
												<digi:trn key="aim:step1:GovernmentAgreementNumTitle">
												Government Agreement Number
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<html:text name="aimEditActivityForm" property="identification.govAgreementNumber"/>
											</td></tr>	
											</field:display>
											
											<field:display name="A.C. Chapter" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofACChapter">A.C. Chapter of the project</digi:trn>">
												<digi:trn key="aim:acChapter">
												A.C. Chapter
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAcChapterFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="acChapter" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCHAPTER_NAME %>" styleClass="inp-text" />
											</td></tr>											
											</field:display>
											
											<feature:display name="Budget" module="Project ID and Planning">
											
											<field:display name="On/Off Budget" feature="Budget">	
											<tr bgcolor="#ffffff">
												<td valign="top" align="left">

													<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
														<digi:trn key="aim:actBudget">Activity Budget</digi:trn>
														<br/>
													</a>
													<html:checkbox styleId="budget" property="budget"  onclick="budgetCheckboxClick();">
													<digi:trn key="aim:actBudgeton">Activity is On Budget</digi:trn>
													</html:checkbox>
													<html:hidden property="identification.budgetCheckbox" styleId="hbudget"/>
												</td>
											<td>
											<table>
											 <tr>
											
											<field:display name="FY" feature="Budget">
											<td valign="top" align="left" id="FY" align="right" style="display:none">
												<a title="<digi:trn key="aim:FY">FY</digi:trn>">
												<digi:trn key="aim:actFY">
												FY
												</digi:trn>
												</a>
														<br/>
														<html:text property="FY" size="12"/>
											</td>
										</field:display>
											
										<field:display name="Vote" feature="Budget">
											<td valign="top" align="left" id="Vote" align="right">
												<a title="<digi:trn key="aim:Vote">Vote</digi:trn>">
												<digi:trn key="aim:actVote">
												Vote
												</digi:trn>
												</a>
											<br/>
													<html:text property="vote" size="12"/>
										</td>	
											</field:display>
											
											<field:display name="Sub-Vote" feature="Budget">
											<td valign="top" align="left" id="Sub-Vote" align="right">
												<a title="<digi:trn key="aim:Sub-Vote">Sub-Vote</digi:trn>">
												<digi:trn key="aim:actSub-Vote">
												Sub-Vote
												</digi:trn>
												</a>
											<br/>
										<html:text property="subVote" size="12"/>
										</td>
											</field:display>
											
	
											
								<field:display name="Sub-Program" feature="Budget">
										<td valign="top" align="left" id="Sub-Program" align="right">
											<a title="<digi:trn key="aim:Sub_Program">Sub-Program</digi:trn>">
											<digi:trn key="aim:actSubProgram">
												Sub-Program
												</digi:trn>
												</a>
											<br/>
											<html:text property="subProgram" size="12"/>
								
									</td>
								</field:display>
											
								<field:display name="Project Code" feature="Budget">
										<td valign="top" align="left" id="ProjectCode" align="right">
											<a title="<digi:trn key="aim:ProjectCode">Project Code</digi:trn>">
											<digi:trn key="aim:actProjectCode">
												Project Code
												</digi:trn>
												</a>
										<br/><html:text property="projectCode" size="12"/>
								
									</td>
								</field:display>	
									
									</tr>
								</table>
								</field:display>
								</td></tr>		
								
								
								</feature:display>
								
								<field:display name="Financial Instrument" feature="Budget">
										<tr bgcolor="#ffffff" id="financial"><td valign="top" align="left" >
											<a title="<digi:trn key="aim:GBS">Financial Instrument</digi:trn>">
											<digi:trn key="aim:actGBS">
												Financial Instrument
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left" >
											<category:showoptions listView="false" name="aimEditActivityForm" property="gbsSbs" categoryName="<%=org.digijava.module.categorymanager.util.CategoryConstants.FINANCIAL_INSTRUMENT_NAME %>" styleClass="inp-text" />
											<!-- 	<html:radio property="gbsSbs" value="1"/>GBS<br/>
												<html:radio property="gbsSbs" value="2"/>SBS<br/>								
												<html:radio property="gbsSbs" value="3"/>Basket<br/>
												<html:radio property="gbsSbs" value="4"/>DPS on Budget -->
										</td>
									</tr>
								</field:display>	
								<field:display name="Government Approval Procedures" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:governmentApprovalProcedures">Government Approval Procedures </digi:trn>">
											<digi:trn key="aim:actGovernmentApprovalProcedures">
												Government Approval Procedures 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn>
												<html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn>
												<html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="false"/>
									</td></tr>
								</field:display>	
								
								<field:display name="Joint Criteria" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:jointCriteria">Joint Criteria</digi:trn>">
											<digi:trn key="aim:actJointCriteria">
												Joint Criteria 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.jointCriteria" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.jointCriteria" value="false"/>
									</td></tr>
								</field:display>
								
								<field:display name="Humanitarian Aid" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:humanitarianAid">Humanitarian Aid</digi:trn>">
												<digi:trn key="aim:humanitarianAid">
													Humanitarian Aid 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.humanitarianAid" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.humanitarianAid" value="false"/>
										</td>
									</tr>
								</field:display>	

								<field:display name="Cris Number" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:crisNumber">Cris Number</digi:trn>">
												<digi:trn key="aim:crisNumber">
													Cris Number 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
										    <html:text name="aimEditActivityForm" property="identification.crisNumber" size="12"/>
										</td>
									</tr>
								</field:display>	

								</table>
									<script>
										InitBud();
									</script>