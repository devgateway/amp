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
function myclearDate(editBox, clearLink, checkboxId){
	clearDate(editBox, clearLink);
	document.getElementById(checkboxId).checked=false;
}
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
										<table width="100%" bgcolor="#ffffff" cellspacing="1" cellPadding=5>
										<field:display name="Line Ministry Rank" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>">&nbsp;
													<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:lineMinistryRank">Line Ministry Rank</digi:trn>">
																	<html:select name="aimEditActivityForm" property="planning.lineMinRank" styleClass="inp-text">
																		<html:option value="-1"><digi:trn key="aim:selectRank">-Select Rank-</digi:trn></html:option>
																		<c:forEach var="lmr" items="${aimEditActivityForm.planning.actRankCollection}" >
																			<c:choose>
																				<c:when test="${lmr == aimEditActivityForm.planning.lineMinRank}">
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
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>">&nbsp;
													<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:planMinistryRank">Ministry of Planning Rank</digi:trn>">
																	<html:select property="planning.planMinRank" styleClass="inp-text">
																		<html:option value="-1"><digi:trn key="aim:selectRank">-Select Rank-</digi:trn></html:option>
							<c:forEach var="mpr"
								items="${aimEditActivityForm.planning.actRankCollection}">
								<c:choose>
									<c:when
										test="${mpr == aimEditActivityForm.planning.planMinRank}">
										<option value='<c:out value="${mpr}" />' selected><c:out
											value="${mpr}" /></option>
									</c:when>
									<c:otherwise>
										<option value='<c:out value="${mpr}" />'><c:out
											value="${mpr}" /></option>
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
												<td width="20"0 bgcolor="#ffffff">
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
												<td width="20"0 bgcolor="#ffffff">
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
											
											
											<field:display name="Proposed Start Date" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn>Date (dd/mm/yy) when the project is expected to commence</digi:trn>">&nbsp;
   														<digi:trn>Proposed Start Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
													<table cellPadding="0" cellSpacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ProposedDateProjectStart">Date (dd/mm/yy) when the project is expected to commence</digi:trn>">
																<html:text name="aimEditActivityForm" property="planning.originalStartDate" size="10"
																styleId="originalStartDate" styleClass="inp-text" />
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear3" href='javascript:clearDate(document.getElementById("originalStartDate"), "clear3")'>
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date3" href='javascript:pickDateWithSameAs("date3",document.getElementById("originalStartDate"), "clear3",document.getElementById("revisedStartDate"),"sameAs2","clear4")'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Actual Start Date" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn>Date (dd/mm/yy) when the project commenced (effective start date) </digi:trn>">&nbsp;
														<digi:trn>Actual Start Date </digi:trn>
												</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualDateofProjectStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
																<html:text name="aimEditActivityForm" property="planning.revisedStartDate" size="10"
																styleId="revisedStartDate" styleClass="inp-text" />
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear4" href="javascript:myclearDate(document.aimEditActivityForm.revisedStartDate, 'clear4', 'sameAs2')">
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date4" href='javascript:pickDateWithClear("date4",document.aimEditActivityForm.revisedStartDate,"clear4")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<field:display name="Same as Proposed Start Date" feature="Planning">
																	<input type="checkbox" name="sameAs2" onclick="sameAsfunction(2)" id="sameAs2">
																	<digi:trn key="aim:sameAsProposedStartDate">Same as Proposed Start Date</digi:trn>
																</field:display>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											<field:display name="Proposed Approval Date" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency</digi:trn>">&nbsp;
														<digi:trn>Proposed Approval Date</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project is expected to be approved by the donor agency </digi:trn>">
																<html:text name="aimEditActivityForm" property="planning.originalAppDate" size="10"
																styleId="originalAppDate" styleClass="inp-text"  />
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear1" href='javascript:clearDate(document.getElementById("originalAppDate"), "clear1")'>
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date1" href='javascript:pickDateWithSameAs("date1",document.getElementById("originalAppDate"),"clear1",document.getElementById("revisedAppDate"),"sameAs1","clear2")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Actual Approval Date" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">&nbsp;
														<digi:trn>Actual Approval Date </digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ActualApprovalDatebyDonorAgency">Date (dd/mm/yy) when the project was approved by the donor agency</digi:trn>">
																	<html:text name="aimEditActivityForm" property="planning.revisedAppDate" size="10"
																	styleId="revisedAppDate" styleClass="inp-text" />
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear2" href='javascript:myclearDate(document.getElementById("revisedAppDate"), "clear2", "sameAs1")'>
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("revisedAppDate"),"clear2")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<field:display name="Same as Proposed Approval Date" feature="Planning">
																	<input type="checkbox" name="sameAs1" onclick="sameAsfunction(1)" id="sameAs1">
																	<digi:trn key="aim:sameAsProposedApprovalDate">Same as Proposed Approval Date</digi:trn>
																</field:display>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Final Date for Contracting" feature="Planning">
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ContractingDateofProject">Final Date (dd/mm/yy) for Contracting</digi:trn>">&nbsp;
													<digi:trn key="aim:contractiongDate">Final Date for Contracting</digi:trn>
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ContractingDateofProject">Final Date (dd/mm/yy) for Contracting</digi:trn>">
																<html:text name="aimEditActivityForm" property="planning.contractingDate" size="10"
																styleId="contractingDate" styleClass="inp-text" />
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear5" href="javascript:clearDate(document.aimEditActivityForm.contractingDate, 'clear5')">
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date5" href='javascript:pickDateWithClear("date5",document.aimEditActivityForm.contractingDate,"clear5")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Final Date for Disbursements" feature="Planning">
												<c:set var="trn">
													<digi:trn>Final Date for Disbursements</digi:trn>
												</c:set>
												<tr>
													<td width="20"0 bgcolor="#ffffff">
														<a title="<digi:trn key="aim:DisbursementsDateofProject">Final Date (dd/mm/yy) for Disbursements</digi:trn>">&nbsp;
															${trn}
														</a>
													</td>
													<td bgcolor="#ffffff">
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td>
																	<a title="<digi:trn key="aim:DisbursementsDateofProject">Final Date (dd/mm/yy) for Disbursements</digi:trn>">
																	<html:text name="aimEditActivityForm" property="planning.disbursementsDate" size="10"
																	styleId="disbursementsDate" styleClass="inp-text" />
																	</a>
																</td>
																<td align="left" vAlign="center">&nbsp;
																	<a id="clear6" href="javascript:clearDate(document.aimEditActivityForm.disbursementsDate, 'clear6')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																	</a>
																	<a id="date6" href='javascript:pickDateWithClear("date6",document.aimEditActivityForm.disbursementsDate,"clear6")'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																	</a>
																</td>
																<td>&nbsp;
																	<input type="button" class="dr-menu" value="<digi:trn key="btn:Comment">Comment</digi:trn>" onclick="commentWin('Add Comments For ${trn}','fdd')">
																</td>
															</tr>
														</table>
													</td>
												</tr>	
											</field:display>
											
											<field:display name="Proposed Completion Date" feature="Planning">
											 <c:set var="trn">
													<digi:trn>Proposed Completion Date</digi:trn>
                                              </c:set>
											<tr>
												<td width="20"0 bgcolor="#ffffff">
													<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed </digi:trn>">&nbsp;
													${trn}
													</a>
												</td>
												<td bgcolor="#ffffff">
													<table cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<a title="<digi:trn key="aim:ExpectedCompletionDateofProject">Date (dd/mm/yy) when the project is expected to be completed</digi:trn>">
																<html:text name="aimEditActivityForm" property="planning.proposedCompDate" size="10"
																styleId="proposedCompDate" styleClass="inp-text"/>
																</a>
															</td>
															<td align="left" vAlign="center">&nbsp;
																<a id="clear7" href="javascript:clearDate(document.aimEditActivityForm.proposedCompDate, 'clear7')">
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																</a>
																<a id="date7" href='javascript:pickDateWithClear("date7",document.aimEditActivityForm.proposedCompDate,"clear7")'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																</a>
															</td>
															 <td>&nbsp;
																	<input type="button" class="dr-menu" value="<digi:trn key="btn:Comment">Comment</digi:trn>" onclick="commentWin('Add Comments For ${trn}','pcd')">
															</td>
														</tr>
													</table>
												</td>
											</tr>
											</field:display>
											
											<field:display name="Current Completion Date" feature="Planning">
												<c:set var="trn">
													<digi:trn>Current Completion Date</digi:trn>
												</c:set>
												<tr>
													<td width="20"0 bgcolor="#ffffff">&nbsp;
													<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
														${trn}
													</a>
													</td>
													<td bgcolor="#ffffff">
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td>
																	<a title="<digi:trn key="aim:CompletionDateofProject">Date (dd/mm/yy) when the project is expected to end or ended. AMP users have the possibility to update this field; AMP will keep track of the history of proposed completion dates </digi:trn>">
																	<html:text name="aimEditActivityForm" property="planning.currentCompDate" size="10"
																	styleId="currentCompDate" styleClass="inp-text" />
																	</a>
																</td>
																<td align="left" vAlign="center">&nbsp;
																	<a id="clear8" href="javascript:clearDate(document.aimEditActivityForm.currentCompDate, 'clear8')">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="<digi:trn>Delete this transaction</digi:trn>"/>
																	</a>
																	<a id="date8" href='javascript:pickDateWithClear("date8",document.aimEditActivityForm.currentCompDate,"clear8")'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="<digi:trn>Click to View Calendar</digi:trn>" border="0">
																	</a>
																</td>
																<td>&nbsp;
																	<input type="button" class="dr-menu" value="<digi:trn key="btn:Comment">Comment</digi:trn>" onclick="commentWin('Add Comments For ${trn}','ccd')">
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</field:display>
											
											
										</table>
									</td></tr>




<script language="JavaScript">
	clearDisplay(document.aimEditActivityForm.originalAppDate, "clear1");
	clearDisplay(document.aimEditActivityForm.revisedAppDate, "clear2");
	clearDisplay(document.aimEditActivityForm.originalStartDate, "clear3");
	clearDisplay(document.aimEditActivityForm.revisedStartDate, "clear4");
	clearDisplay(document.aimEditActivityForm.contractingDate, "clear5");
	clearDisplay(document.aimEditActivityForm.disbursementsDate, "clear6");
	clearDisplay(document.aimEditActivityForm.proposedCompDate, "clear7");
	clearDisplay(document.aimEditActivityForm.currentCompDate, "clear8");
	
</script>
									