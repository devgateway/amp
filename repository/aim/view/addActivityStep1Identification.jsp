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
	 if(document.aimEditActivityForm.budget.checked==false)
	 	document.aimEditActivityForm.budgetCheckbox.value="unchecked";
	 else if(document.aimEditActivityForm.budget.checked==true)
	 	document.aimEditActivityForm.budgetCheckbox.value="checked";
	}


</script>


<digi:instance property="aimEditActivityForm" />
										<table width="100%" bgcolor="#cccccc" cellPadding=5 cellSpacing=1>
											
											<field:display name="Project Title" feature="Identification">
											&nbsp;
											</field:display>
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<FONT color=red>*</FONT>
												<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">Title used in donors or MoFED internal systems</digi:trn>">
												<digi:trn key="aim:projectTitle">Project Title</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">
												Title used in donors or MoFED internal systems
												</digi:trn>">
												<html:textarea property="title" cols="60" rows="2" styleClass="inp-text"/>
												</a>
											</td></tr>
											
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
																<c:out value="${aimEditActivityForm.objectives}"/>
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
															<a href="javascript:commentWin('objAssumption')" id="CommentObjAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
															<a href="javascript:commentWin('objVerification')" id="CommentObjVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>		
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
																<c:out value="${aimEditActivityForm.description}"/>
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
																<c:out value="${aimEditActivityForm.purpose}"/>
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
																<c:out value="${aimEditActivityForm.results}"/>
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
															<a href="javascript:commentWin('resAssumption')" id="CommentResAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
															<a href="javascript:commentWin('resVerification')" id="CommentResVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
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
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="accessionInstrument" categoryName="<%= org.digijava.module.aim.helper.CategoryConstants.ACCESSION_INSTRUMENT_NAME %>" styleClass="inp-text" />
											</td></tr>	
											</field:display>
											
											<field:display name="AC Chapter" feature="Identification">
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
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="acChapter" categoryName="<%= org.digijava.module.aim.helper.CategoryConstants.ACCHAPTER_NAME %>" styleClass="inp-text" />
											</td></tr>											
											</field:display>
											
												
											<tr bgcolor="#ffffff"><td colspan="2" valign="top" align="left">
											<table>
											 <tr>
												<field:display name="Activity Budget" feature="Identification">
												<td valign="top" align="left">
											
												<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
												<digi:trn key="aim:actBudget">
												Activity Budget
												</digi:trn>
												</a>
												</td>
												
												<td valign="top" align="left">
													<html:checkbox property="budget"  onclick="budgetCheckboxClick();">
													<digi:trn key="aim:actBudgeton">
												Activity is On Budget
												</digi:trn>
													</html:checkbox>
													<input type="hidden" name="budgetCheckbox">
												</td>
											</field:display>
											
											<c:if test="${aimEditActivityForm.budget==true}">
											
											<field:display name="FY" feature="Identification">
											<td valign="top" align="left">
												<a title="<digi:trn key="aim:FY">FY</digi:trn>">
												<digi:trn key="aim:actFY">
												FY
												</digi:trn>
												</a>
														<br/>
														<html:text property="FY" size="5"/>
											</td>
										</field:display>
											
										<field:display name="Vote" feature="Identification">
											<td valign="top" align="left">
												<a title="<digi:trn key="aim:Vote">Vote</digi:trn>">
												<digi:trn key="aim:actVote">
												Vote
												</digi:trn>
												</a>
											<br/>
													<html:text property="vote" size="3"/>
										</td>	
											</field:display>
											
											<field:display name="Sub-Vote" feature="Identification">
											<td valign="top" align="left">
												<a title="<digi:trn key="aim:Sub-Vote">Sub-Vote</digi:trn>">
												<digi:trn key="aim:actSub-Vote">
												Sub-Vote
												</digi:trn>
												</a>
											<br/>
										<html:text property="subVote" size="3"/>
										</td>
											</field:display>
											
	
											
								<field:display name="Sub-Program" feature="Identification">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:Sub_Program">Sub-Program</digi:trn>">
											<digi:trn key="aim:actSubProgram">
												Sub-Program
												</digi:trn>
												</a>
											<br/>
											<html:text property="subProgram" size="3"/>
								
									</td>
								</field:display>
											
								<field:display name="Project Code" feature="Identification">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:ProjectCode">Project Code</digi:trn>">
											<digi:trn key="aim:actProjectCode">
												Project Code
												</digi:trn>
												</a>
										<br/><html:text property="projectCode" size="7"/>
								
									</td>
								</field:display>			
									</tr>
								</table>
								</td></tr>		
								</c:if>
								<c:if test="${aimEditActivityForm.budget==true}">
								<field:display name="GBS/SBS" feature="Identification">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:GBS">GBS</digi:trn>">
											<digi:trn key="aim:actGBS">
												GBS/SBS ??
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<html:radio property="gbsSbs" value="1"/>GBS<br/>
												<html:radio property="gbsSbs" value="2"/>SBS<br/>								
												<html:radio property="gbsSbs" value="3"/>Basket<br/>
												<html:radio property="gbsSbs" value="4"/>DPS on Budget
									</td>
								</field:display>	
								</c:if>
												
								
										
								<field:display name="Government Approval Procedures" feature="Identification">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:governmentApprovalProcedures">Government Approval Procedures </digi:trn>">
											<digi:trn key="aim:actGovernmentApprovalProcedures">
												Government Approval Procedures 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												Yes<html:radio property="governmentApprovalProcedures" value="true"/> &nbsp;&nbsp;No<html:radio property="governmentApprovalProcedures" value="false"/>
									</td></tr>
								</field:display>	
								
								<field:display name="Joint Criteria" feature="Identification">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:jointCriteria">Joint Criteria</digi:trn>">
											<digi:trn key="aim:actJointCriteria">
												Joint Criteria 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												Yes<html:radio property="jointCriteria" value="true"/> &nbsp;&nbsp;No<html:radio property="jointCriteria" value="false"/>
									</td></tr>
								</field:display>
								
								
										</table>

