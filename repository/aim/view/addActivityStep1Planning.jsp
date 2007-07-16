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

</script>


<digi:instance property="aimEditActivityForm" />
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:planning">Planning</digi:trn></b>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
										<field:display name="Line Ministry Rank" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>">&nbsp;
													<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>">
																	<html:select name="aimEditActivityForm" property="lineMinRank" styleClass="inp-text">
																		<html:option value="-1"><digi:trn key="aim:selectRank">-Select Rank-</digi:trn></html:option>
																		<c:forEach var="lmr" items="${aimEditActivityForm.actRankCollection}" >
																			<c:choose>
																				<c:when test="${lmr == aimEditActivityForm.lineMinRank}">
																					<option value='<c:out value="${lmr}" />' selected><c:out value="${lmr}"/></option>
																				</c:when>
																				<c:otherwise>
																					<option value='<c:out value="${lmr}" />'><c:out value="${lmr}"/></option>
																				</c:otherwise>
																			</c:choose>
																		</c:forEach>
																	</html:select>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</field:display>
										<field:display name="Ministry of Planning Rank" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>">&nbsp;
													<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>">
																	<html:select property="planMinRank" styleClass="inp-text">
																		<html:option value="-1"><digi:trn key="aim:selectRank">-Select Rank-</digi:trn></html:option>
																		<c:forEach var="mpr" items="${aimEditActivityForm.actRankCollection}" >
																			<c:choose>
																				<c:when test="${mpr == aimEditActivityForm.planMinRank}">
																					<option value='<c:out value="${mpr}" />' selected><c:out value="${mpr}"/></option>
																				</c:when>
																				<c:otherwise>
																					<option value='<c:out value="${mpr}" />'><c:out value="${mpr}"/></option>
																				</c:otherwise>
																			</c:choose>
																		</c:forEach>
																	</html:select>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											<field:display name="Overall Cost" feature="Planning">
											<logic:present name="aimEditActivityForm" property="overallCost">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:OverallCost">Overall Cost</digi:trn>">&nbsp;
													<digi:trn key="aim:OverallCost2">Overall Cost</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
										  		    <a title="<digi:trn key="aim:OverallCost">Overall Cost</digi:trn>">
													<bean:write  name="aimEditActivityForm" property="overallCost" format="###,###,###"/>
													</a>
												</td>
											</tr>
											</logic:present>
											</field:display>
											
											<field:display name="Overall Contribution" feature="Planning">
											<logic:present name="aimEditActivityForm" property="overallContribution">											
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:OverallContribution">Overall Contribution</digi:trn>">&nbsp;
													<digi:trn key="aim:OverallContribution2">Overall Contribution</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
										  		    <a title="<digi:trn key="aim:OverallContribution">Overall Contribution</digi:trn>">
													<bean:write  name="aimEditActivityForm" property="overallContribution" format="###,###,###"/>
													</a>
												</td>
											</tr>
											</logic:present>
											</field:display>
											
											<field:display name="Proposed Approval Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency</digi:trn>">&nbsp;
													<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency </digi:trn>">
																<html:text name="aimEditActivityForm" property="originalAppDate" size="10"
																styleId="originalAppDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
															<a href="javascript:calendar('originalAppDate')">
																<img src= "../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Actual Approval Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">&nbsp;
												<digi:trn key="aim:actualApprovalDate">Actual Approval Date </digi:trn></a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">
																<html:text name="aimEditActivityForm" property="revisedAppDate" size="10"
																styleId="revisedAppDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('revisedAppDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<input type="checkbox" name="sameAs1" onclick="sameAsfunction(1)">
																Same as <digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>

											<field:display name="Proposed Start Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ProposedDateProjectStart">Date (dd/mm/yy) when the project is expected to commence</digi:trn>">&nbsp;
   												<digi:trn key="aim:proposedStartDate">Proposed Start Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ProposedDateProjectStart">Date (dd/mm/yy) when the project is expected to commence</digi:trn>">
																<html:text name="aimEditActivityForm" property="originalStartDate" size="10"
																styleId="originalStartDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('originalStartDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Actual Start Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ActualDateofProjectStart">Date (dd/mm/yy) when the project commenced (effective start date) </digi:trn>">&nbsp;
													<digi:trn key="aim:actualStartDate">Actual Start Date </digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualDateofProjectStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
																<html:text name="aimEditActivityForm" property="revisedStartDate" size="10"
																styleId="revisedStartDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('revisedStartDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<input type="checkbox" name="sameAs2" onclick="sameAsfunction(2)">
																Same as <digi:trn key="aim:proposedStartDate">Proposed Start Date</digi:trn>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Final Date for Contracting" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ContractingDateofProject">Final Date (dd/mm/yy) for Contracting</digi:trn>">&nbsp;
													<digi:trn key="aim:contractiongDate">Final Date for Contracting</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ContractingDateofProject">Final Date (dd/mm/yy) for Contracting</digi:trn>">
																<html:text name="aimEditActivityForm" property="contractingDate" size="10"
																styleId="contractingDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('contractingDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to view the Calendar" border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Final Date for Disbursements" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:DisbursementsDateofProject">Final Date (dd/mm/yy) for Disbursements</digi:trn>">&nbsp;
													<digi:trn key="aim:disbursementsDate">Final Date for Disbursements</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:DisbursementsDateofProject">Final Date (dd/mm/yy) for Disbursements</digi:trn>">
																<html:text name="aimEditActivityForm" property="disbursementsDate" size="10"
																styleId="disbursementsDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('disbursementsDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to view the Calendar" border=0>
																</a>
															</td>
															<td>&nbsp;
																<input type="button" class="buton" value="<digi:trn key="btn:Comment">Comment</digi:trn>" onclick="commentWin()">
															</td>
														</tr>
													</table>
												</td>
											</tr>	
											</field:display>
											
											<field:display name="Proposed Completion Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed </digi:trn>">&nbsp;
													<digi:trn key="aim:proposedCompletionDate">Proposed Completion Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed</digi:trn>">
																<html:text name="aimEditActivityForm" property="proposedCompDate" size="10"
																styleId="proposedCompDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('proposedCompDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to view the Calendar" border=0>
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Current Completion Date" feature="Planning">
											<tr>
												<td width=200 bgcolor="#ffffff">&nbsp;
												<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
												<digi:trn key="aim:currentCompletionDate">Current Completion Date</digi:trn>
												</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellPadding=0 cellSpacing=0>
														<tr>
															<td>
																<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
																<html:text name="aimEditActivityForm" property="currentCompDate" size="10"
																styleId="currentCompDate" styleClass="inp-text" readonly="true"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a href="javascript:calendar('currentCompDate')">
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</td>
															<td>&nbsp;
																<input type="button" class="buton" value="<digi:trn key="btn:Comment">Comment</digi:trn>" onclick="commentWin('ccd')">
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Status" feature="Planning">
											<tr>
												<td bgcolor="#ffffff">
												<FONT color=red>*</FONT>&nbsp;
												<digi:trn key="aim:status">Status</digi:trn>												  
												<a href="javascript:popupwin()">
												<img src="../ampTemplate/images/help.gif" alt="Click to get help on Status" width=10 height=10 border=0></a>
												</td>
												<td bgcolor="#ffffff">
	                                                <category:showoptions name="aimEditActivityForm" property="statusId"  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY %>" styleClass="inp-text" />
                                                    <br/><br/>
													<digi:trn key="aim:reasonsToChangeStatus">If there have been some changes in the status, explain below the reasons</digi:trn> :
													<a title="<digi:trn key="aim:ReasonforStatusofProject">Use this space to provide explanations as to why that status was selected. Used primarily in the case of cancelled and suspended projects</digi:trn>">
                                                    <br/>
													<html:textarea property="statusReason" cols="50" rows="3" styleClass="inp-text" />
													</a>
												</td>
											</tr>
											</field:display>
										</table>
									</td></tr>
