<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript">
	function load() {
		window.print();
	}
	function unload() {}
</script>


<digi:instance property="aimEditActivityForm" />
<c:if test="${aimEditActivityForm!=null}">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="650" vAlign="top" align="left" border=0>
	<tr>
		<td align=left vAlign=top>
			<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" border=0>
          <tr>
            <td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                <tr>
                  <td width="100%" vAlign="top">
						<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left" border=0>
                      <tr>
                        <td width="100%">
								<table width="98%" cellSpacing=1 cellpadding=0>
									<tr>
										<td class="head1-name" width="100%" align="left" bgcolor="#ffffff">
                                <c:if test="${aimEditActivityForm.ampId!=null}">
                                ${aimEditActivityForm.ampId}
                                </c:if>
										</td>
									</tr>		
								</table>														
                        </td>
                      </tr>
                      <tr>
                        <td width="100%">
								<table width="98%" cellSpacing=1 cellpadding=2>
									<tr>
										<td class="head2-name" width="100%" align="left" bgcolor="#ffffff">
                                <c:if test="${aimEditActivityForm.title!=null}">
                                ${aimEditActivityForm.title}
                                </c:if>
										</td>
									</tr>	
									<tr>
										<td class="head3-name" width="100%" align="left" bgcolor="#ffffff">
											<b><digi:trn key="aim:objectives">Objectives</digi:trn></b>
										</td>
									</tr>	
									<tr>
										<td width="100%" align="left" bgcolor="#ffffff">
                                <c:if test="${aimEditActivityForm.objectives!=null}">
                                  <c:set var="objKey" value="${aimEditActivityForm.objectives}" />
                                  <digi:edit key="${objKey}"/>
                                </c:if>
										</td>
									</tr>										

									<tr>
										<td class="head3-name" width="100%" align="left" bgcolor="#ffffff">
											<b><digi:trn key="aim:description">Description</digi:trn></b>
										</td>
									</tr>	
									<tr>
										<td width="100%" align="left" bgcolor="#ffffff">
                                <c:if test="${aimEditActivityForm.description!=null}">
                                  <c:set var="descKey" value="${aimEditActivityForm.description}" />
                                  <digi:edit key="${descKey}"/>
                                </c:if>
										</td>
									</tr>									
								</table>							
                        </td>
                      </tr>
                      <tr>
                        <td width="100%">
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
                            <tr>
                              <td align="center" vAlign="top">
								<table width="98%" cellSpacing=0 cellpadding=4 style="border-collapse: collapse" border=1>
									<tr bgcolor="#f4f4f2">
										<td  align="center" colspan="2">
											<b>
											<digi:trn key="aim:activityDetail">
												Activity Details	
											</digi:trn></b>
										</td>
									</tr>								
									<tr>
										<td width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:orgsAndProjectIds">
											Organizations and Project IDs
											</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.selectedOrganizations}">
												<table cellSpacing=2 cellPadding=2 border=0>
													<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
                                            <tr>
                                              <td>
                                              ${selectedOrganizations.name} :
                                              ${selectedOrganizations.projectId}
                                              </td>
                                            </tr>
													</c:forEach>
												</table>
											</c:if>
											<c:if test="${empty aimEditActivityForm.selectedOrganizations}">
												&nbsp;
											</c:if>
										</td>
									</tr>
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:planning">
											Planning</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<table width="100%" cellSpacing=2 cellPadding=1>
												<tr>
													<td width="32%"><digi:trn key="aim:originalApprovalDate">
													Original Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
                                            ${aimEditActivityForm.originalAppDate}
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedApprovalDate">Revised Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
                                          ${aimEditActivityForm.revisedAppDate}
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
                                          ${aimEditActivityForm.originalStartDate}
													</td>
												</tr>
												<tr>
													<td width="32%"><digi:trn key="aim:revisedStartDate">Revised Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
                                          ${aimEditActivityForm.revisedStartDate}
													</td>
												</tr>
												<field:display name="Current Completion Date" feature="Planning">												
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
                                            ${aimEditActivityForm.currentCompDate}
													</td>
												</tr>	
												</field:display>														
												<c:if test="${!empty aimEditActivityForm.activityCloseDates}">
												<tr>
													<td width="32%" valign=top><digi:trn key="aim:proposedCompletionDates">
													Proposed Completion Dates</digi:trn></td>
													<td width="1" valign=top>:</td>
													<td align="left" valign=top>
														<table cellPadding=0 cellSpacing=0>
															<c:forEach var="closeDate" items="${aimEditActivityForm.activityCloseDates}">
															<tr>
																<td>
                                                      ${closeDate}
																</td>
															</tr>
															</c:forEach>
														</table>
													</td>
												</tr>
												</c:if>												
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td colspan="3"><digi:trn key="aim:status">Status</digi:trn> :
														<c:if test="${!empty aimEditActivityForm.status}">
															${aimEditActivityForm.status}
														</c:if>
													</td>
												</tr>																								
												<tr>
                                          <td colspan="3">${aimEditActivityForm.statusReason}</td>
												</tr>
											</table>
										</td>
									</tr>		
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:level">
											Level</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.levelCollection}">
												<c:forEach var="tempLevel" items="${aimEditActivityForm.levelCollection}">
													<c:if test="${tempLevel.ampLevelId == aimEditActivityForm.levelId}">												
                                          ${tempLevel.name}
													</c:if>
												</c:forEach>
												&nbsp;
											</c:if>										
											<c:if test="${empty aimEditActivityForm.levelCollection}">
												&nbsp;
											</c:if>										
										</td>
									</tr>																											
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:location">
											Location</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="locations" items="${aimEditActivityForm.selectedLocs}">
                                            <tr>
                                              <td>
													<c:if test="${!empty locations.country}">
                                                [${locations.country}]
													</c:if>
													<c:if test="${!empty locations.region}">
                                                [${locations.region}]
													</c:if>
													<c:if test="${!empty locations.zone}">
                                                [${locations.zone}]
													</c:if>
													<c:if test="${!empty locations.woreda}">
                                                [${locations.woreda}]
													</c:if>
                                              </td>
                                            </tr>
												</c:forEach>
												</table>
											</c:if>
											<c:if test="${empty aimEditActivityForm.selectedLocs}">
												&nbsp;
											</c:if>
										</td>
									</tr>		
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:sector">
											Sector</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
                                            <tr>
                                              <td>
													<c:if test="${!empty sectors.sectorName}">
                                                [${sectors.sectorName}]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel1Name}">
                                                [${sectors.subsectorLevel1Name}]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel2Name}">
                                                [${sectors.subsectorLevel2Name}]
													</c:if>
                                              </td>
                                            </tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>		
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b><digi:trn key="aim:program">Program</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.programCollection}">
												<c:forEach var="tempPgm" items="${aimEditActivityForm.programCollection}">
													<c:if test="${tempPgm.ampThemeId == aimEditActivityForm.program}">
                                          ${tempPgm.name}
													</c:if>
												</c:forEach>
											</c:if>
											<c:if test="${empty aimEditActivityForm.programCollection}">											
												&nbsp;
											</c:if>											
										</td>
									</tr>		
									
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:funding">
											Funding</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.fundingOrganizations}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#dddddd">
												<c:forEach var="fundOrgs" items="${aimEditActivityForm.fundingOrganizations}">
                                            <tr>
                                              <td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
                                                  <tr>
                                                    <td bgcolor="#ffffff"><b>
                                                      ${fundOrgs.orgName}</b>
                                                    </td>
                                                  </tr>
															<c:if test="${!empty fundOrgs.fundings}">
																<c:forEach var="fund" items="${fundOrgs.fundings}">
                                                      <tr>
                                                        <td bgcolor="#ffffff">
																		<table width="100%" cellSpacing="1" cellPadding="1" class="box-border">
																			<tr>
																				<td width="35%">
																					<digi:trn key="aim:fundingOrgId">
																					Funding Organization Id</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
                                                              ${fund.orgFundingId}
																				</td>
																			</tr>																		
																			<tr>
																				<td width="35%">
																					<digi:trn key="aim:typeOfAssistance">
																					Type of Assistance</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
                                                              ${fund.typeOfAssistance}
																				</td>
																			</tr>
																				<%-- <tr>
																				<td width="35%">
																					<digi:trn key="aim:financingInstrument">
																					Financing Instrument</digi:trn>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<c:if test="${!empty aimEditActivityForm.modalityCollection}">
																						<c:forEach var="tempModality" 
																						items="${aimEditActivityForm.modalityCollection}">
																						<c:if test="${tempModality.ampModalityId == fund.modality.ampModalityId}">
                                                                  ${tempModality.name}
																						</c:if>
																						</c:forEach>
																					</c:if>
																				</td>
																			</tr>	 --%>
																			<c:if test="${!empty fund.fundingDetails}">
                                                              <tr>
                                                                <td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bordercolor="#dddddd"
																			border=1  style="border-collapse: collapse">
                                                                  <tr>
                                                                    <td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="aim:commitments">
																					Commitments</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
                                                                      <c:if test="${!empty fund.fundingDetails}">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="2">
																						<c:if test="${fundDet.transactionType == 0}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                                ${fundDet.adjustmentTypeName}
																							</td>
																							<td align="right" width="140" bgcolor="#ffffff">
                                                                                * ${fundDet.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                                ${fundDet.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.perspectiveName}
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
                                                                      </c:if>
                                                                    </td>
                                                                  </tr>
																			</table>
                                                                </td>
                                                              </tr>
                                                              <tr>
                                                                <td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bordercolor="#dddddd"
																			border=1  style="border-collapse: collapse">
                                                                  <tr>
                                                                    <td valign="top" width="100" bgcolor="#ffffff">
																					<digi:trn key="aim:disbursements">
																					Disbursements</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
                                                                      <c:if test="${!empty fund.fundingDetails}">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="2">
																						<c:if test="${fundDet.transactionType == 1}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                                ${fundDet.adjustmentTypeName}
																							</td>
																							<td align="right" width="140" bgcolor="#ffffff">
                                                                                * ${fundDet.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                                ${fundDet.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.perspectiveName}
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
                                                                      </c:if>
                                                                    </td>
                                                                  </tr>
																			</table>
                                                                </td>
                                                              </tr>
                                                              <tr>
                                                                <td colspan="3">
																			<table width="100%" cellSpacing=1 cellPadding=1 bordercolor="#dddddd"
																			border=1  style="border-collapse: collapse">
                                                                  <tr>
                                                                    <td valign="top" width="100" bgcolor="#ffffff">
                                                                      <digi:trn key="aim:expenditures">
																					Expenditures</digi:trn>
																				</td>
																				<td bgcolor="#ffffff">
                                                                      <c:if test="${!empty fund.fundingDetails}">
																					<c:forEach var="fundDet" items="${fund.fundingDetails}">
																					<table width="100%" cellSpacing="1" cellPadding="2">
																						<c:if test="${fundDet.transactionType == 2}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                                ${fundDet.adjustmentTypeName}
																							</td>
																							<td align="right" width="140" bgcolor="#ffffff">
                                                                                * ${fundDet.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                                ${fundDet.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                                ${fundDet.perspectiveName}
																							</td>
																						</tr>
																						</c:if>																						
																					</table>
																				   </c:forEach>
                                                                      </c:if>
                                                                    </td>
                                                                  </tr>
																			</table>
                                                                </td>
                                                              </tr>
																			</c:if>
																		</table>
</td>
                                                      </tr>
																</c:forEach>
															</c:if>
														</table>
</td>
                                            </tr>
												</c:forEach>
                                          <tr>
                                            <td bgcolor="#ffffff"> *
													<digi:trn key="aim:theAmountEnteredAreInThousands">	
													The amount entered are in thousands (000)</digi:trn>
                                            </td>
                                          </tr>
												</table>
											</c:if>
											&nbsp;
										</td>
									</tr>		

									<tr>
										<td width="30%" align="right" bgcolor="#ffffff">
											<b><digi:trn key="aim:regionalFunding">
											Regional Funding</digi:trn></b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.regionalFundings}">
                                            <tr>
                                              <td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
                                                  <tr>
                                                    <td bgcolor="#ffffff"><b>
                                                      ${regFunds.regionName}</b>
                                                    </td>
                                                  </tr>
															<c:if test="${!empty regFunds.commitments}">
                                                    <tr>
                                                      <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" bordercolor="#dddddd"
																	border=1 style="border-collapse: collapse">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#ffffff">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                  ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
                                                                  * ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                  ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
                                                      </td>
                                                    </tr>
															</c:if>
															<c:if test="${!empty regFunds.disbursements}">
                                                    <tr>
                                                      <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" bordercolor="#dddddd"
																	border=1 style="border-collapse: collapse">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#ffffff">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                  ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
                                                                  * ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                  ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
                                                      </td>
                                                    </tr>
															</c:if>
															<c:if test="${!empty regFunds.expenditures}">
                                                    <tr>
                                                      <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" bordercolor="#dddddd"
																	border=1 style="border-collapse: collapse">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#ffffff">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                                  ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
                                                                  * ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                                  ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                                  ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
                                                      </td>
                                                    </tr>
															</c:if>															
														</table>
                                              </td>
                                            </tr>
												</c:forEach>
                                          <tr>
                                            <td bgcolor="#ffffff">
													* <digi:trn key="aim:theAmountEnteredAreInThousands">	
													The amount entered are in thousands (000)</digi:trn>
                                            </td>
                                          </tr>
												</table>
											</c:if>
										</td>
									</tr>										
									
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:components">
											Components</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${empty aimEditActivityForm.selectedComponents}">
												&nbsp;
											</c:if>

											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1" bordercolor="#dddddd"
														border=1 style="border-collapse: collapse">
                                          <tr>
                                            <td>
														<table width="100%" cellSpacing="2" cellPadding="1" >

                                                <tr>
                                                  <td>
                                                    <b>
                                                      ${comp.title}</b>
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <td>
																<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
                                                      ${comp.description}
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <td bgcolor="#ffffff">
																&nbsp;
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <td bgcolor="#ffffff">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
                                                  </td>
                                                </tr>
															<c:if test="${!empty comp.commitments}">
                                                  <tr>
                                                    <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" bordercolor="#dddddd"
																	style="border-collapse: collapse" border=1>
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" 
																				bordercolor="#dddddd" border=1  style="border-collapse: collapse">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                              ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
                                                                ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                              ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
                                                    </td>
                                                  </tr>
															</c:if>
															<c:if test="${!empty comp.disbursements}">
                                                  <tr>
                                                    <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1"  bordercolor="#dddddd"
																	style="border-collapse: collapse" border=1>
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" 
																				bordercolor="#dddddd" border=1  style="border-collapse: collapse">
																					<c:forEach var="fd" items="${comp.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                              ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
                                                                ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                              ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
																		</tr>
																	</table>
                                                    </td>
                                                  </tr>
															</c:if>
															<c:if test="${!empty comp.expenditures}">
                                                  <tr>
                                                    <td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1"  bordercolor="#dddddd"
																	style="border-collapse: collapse" border=1>
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" 
																				bordercolor="#dddddd" border=1  style="border-collapse: collapse">
																					<c:forEach var="fd" items="${comp.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
                                                              ${fd.adjustmentTypeName}
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color=blue>*</FONT>
                                                                ${fd.transactionAmount}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.currencyCode}
																							</td>
																							<td bgcolor="#ffffff" width="70">
                                                              ${fd.transactionDate}
																							</td>
																							<td bgcolor="#ffffff">
                                                              ${fd.perspectiveName}
																							</td>
																						</tr>
																					</c:forEach>
																				</table>	
																			</td>
																		</tr>
																	</table>
                                                    </td>
                                                  </tr>
															</c:if>
                                                <tr>
                                                  <td bgcolor="#ffffff">
																&nbsp;
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <td bgcolor="#ffffff">
																<b><digi:trn key="aim:physicalProgressOfTheComponent">
																Physical progress of the component</digi:trn></b>
                                                  </td>
                                                </tr>
															<c:if test="${!empty comp.phyProgress}">
																<c:forEach var="phyProg" items="${comp.phyProgress}">
                                                    <tr>
                                                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<b>
                                                          ${phyProg.title}</b> -
                                                          ${phyProg.reportingDate}
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<i>
																		<digi:trn key="aim:description">Description</digi:trn> :</i>
                                                          ${phyProg.description}
                                                      </td>
                                                    </tr>
																</c:forEach>
															</c:if>
														</table>
                                            </td>
                                          </tr>
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>	
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:issues">
											Issues</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${empty aimEditActivityForm.issues}">
												&nbsp;
											</c:if>
											<c:if test="${!empty aimEditActivityForm.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border=0>
												<c:forEach var="issue" items="${aimEditActivityForm.issues}">
                                            <tr>
                                              <td valign="top">
                                                <li class="level1"><b>${issue.name}</b></li>
                                              </td>
                                            </tr>
													<c:if test="${!empty issue.measures}">
														<c:forEach var="measure" items="${issue.measures}">
                                                <tr>
                                                  <td>
                                                    <li class="level2"><i>${measure.name}</i></li>
                                                  </td>
                                                </tr>
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
                                                    <tr>
                                                      <td>
                                                        <li class="level3">${actor.name}</li>
                                                      </td>
                                                    </tr>
																</c:forEach>
															</c:if>															
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>									
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:relatedDocuments">
											Related Documents</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${empty aimEditActivityForm.documentList}">
												&nbsp;
											</c:if>										
											<c:if test="${!empty aimEditActivityForm.documentList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="relLinks" items="${aimEditActivityForm.documentList}">
					   							<bean:define id="docs" name="relLinks" 
													property="relLink" />
                                            <tr>
                                              <td>
                                                <bean:define id="fileName" name="docs" property="fileName" />
													    <%
														int index2;
														String extension = "";
														index2 = ((String)fileName).lastIndexOf(".");	
														if( index2 >= 0 ) {
														   extension = "module/cms/images/extensions/" + 
															((String)fileName).substring(
															index2 + 1,((String)fileName).length()) + ".gif";
														}
													    %>
														 
													 <table width="100%" cellPadding=0 cellSpacing=1>
													 	<tr>
															<td width="2">
																<digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>
															</td>
															<td vAlign="center" align="left">
                                                    ${docs.title} -
                                                    ${docs.fileName}
																<br>
                                                    <b>Desc:</b>${docs.description}
															</td>
														</tr>
													 </table>
                                              </td>
                                            </tr>
												</c:forEach>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="relLinks" items="${aimEditActivityForm.linksList}">
					   							<bean:define id="links" name="relLinks" property="relLink" />
                                            <tr>
                                              <td>
														<table width="100%" cellPadding=1 cellSpacing=1>
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>
																</td>
																<td align="left" vAlign="center">
                                                    ${links.title} -
                                                    <a href="${links.url}">
                                                      ${links.url}</a>
																	<br>
                                                      <b>Desc:</b>${links.description}
																</td>
															</tr>
														</table>
                                              </td>
                                            </tr>
												</c:forEach>
												</table>
											</c:if>											
										</td>
									</tr>
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:executingAgencies">
                                          Executing Agencies
                                        </digi:trn>
                                      </b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.executingAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="exAgency" items="${aimEditActivityForm.executingAgencies}"> 
                                            <tr>
                                              <td>
                                              ${exAgency.name}
                                              </td>
                                            </tr>
													</c:forEach>
												</table>
											</c:if>
											<c:if test="${empty aimEditActivityForm.executingAgencies}">
												&nbsp;
											</c:if>
										</td>
									</tr>
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:implementingAgencies">
                                          Implementing Agencies
                                        </digi:trn>
                                      </b>
										</td>
										<td bgcolor="#ffffff" >
											<c:if test="${!empty aimEditActivityForm.impAgencies}">
												<table width="100%" cellpadding="2" cellspacing="2" valign="top" align="left">
													<c:forEach var="impAgency" items="${aimEditActivityForm.impAgencies}"> 
                                            <tr>
                                              <td>
                                              ${impAgency.name}
                                              </td>
                                            </tr>
													</c:forEach>
												</table>
											</c:if>
											<c:if test="${empty aimEditActivityForm.impAgencies}">											
												&nbsp;
											</c:if>											
										</td>
									</tr>
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:contractors">
											Contractors</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
                                    ${aimEditActivityForm.contractors}
											&nbsp;
										</td>
									</tr>	

									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:donorFundingContactInformation">
											Donor funding contact information</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff" >
                                    ${aimEditActivityForm.dnrCntFirstName}
                                    ${aimEditActivityForm.dnrCntLastName} -
                                    ${aimEditActivityForm.dnrCntEmail}
											&nbsp;	
										</td>
									</tr>	
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:mofedContactInformation">
											MOFED contact information</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff" >
                                    ${aimEditActivityForm.mfdCntFirstName}
                                    ${aimEditActivityForm.mfdCntLastName} -
                                    ${aimEditActivityForm.mfdCntEmail}
											&nbsp;	
										</td>
									</tr>
									<field:display name="Activity Created By" feature="Identification">										
									<tr>
										<td  width="140" align="right" bgcolor="#ffffff">
											<b>
											<digi:trn key="aim:activityCreatedBy">
											Activity created by</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff" >
                                    ${aimEditActivityForm.actAthFirstName}
                                    ${aimEditActivityForm.actAthLastName} -
                                    ${aimEditActivityForm.actAthEmail}
											&nbsp;	
										</td>
									</tr>		
									</field:display>
									<field:display name="Activity Created On" feature="Identification">
                                  <c:if test="${!empty aimEditActivityForm.createdDate}">
									<tr>
										<td  width="30%" align="right" bgcolor="#ffffff"><b>
											<digi:trn key="aim:activityCreatedOn">
											Activity created on</digi:trn></b>
										</td>
										<td bgcolor="#ffffff" >
                                      ${aimEditActivityForm.createdDate}
										</td>
									</tr>									
                                  </c:if>
                                  </field:display>
								</table>
</td>
                            </tr>
							</table>
</td>
                      </tr>
						</table>
</td>
                </tr>
					</table>
</td>
          </tr>
			</table>
		</td>
	</tr>
</table>
</c:if>
<c:if test="${aimEditActivityForm==null}">
Invalid activity id
</c:if>
