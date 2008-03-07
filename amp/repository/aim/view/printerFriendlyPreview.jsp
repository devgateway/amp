<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<script language="JavaScript">
	function load() {
		window.print();
	}
	function unload() {}
</script>

<%
	Long actId = (Long) request.getAttribute("actId");

	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=6";

	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),370,450,url,true,request);

	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}


	String actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(
						 actId,session,new PrintWriter(out),370,350,url,request);

	String actRiskChartUrl = null;

	if (actRiskChartFileName != null)  {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}
%>

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
								<field:display feature="Identification" name="Project Title">
									<tr>
										<td class="head2-name" width="100%" align="left" bgcolor="#ffffff">
                                			<c:if test="${aimEditActivityForm.title!=null}">${aimEditActivityForm.title}</c:if>
										</td>
									</tr>	
								</field:display>
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
											<b><digi:trn key="aim:activityDetail">Activity Details</digi:trn></b>										
										</td>
									</tr>								
<feature:display name="Identification" module="Project ID and Planning">

									<field:display feature="Identification" name="Objective">
                                    	
                                    <tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:objectives">
										    Objectives</digi:trn>
											</b>								</td>
<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.objectives!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.objectives}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>    
										<logic:present name="currentMember" scope="session">
										<tr>
											<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>	
<digi:trn key="aim:objectiveComments">
											    Objective Comments</digi:trn>
	</b>									</td>
<td bgcolor="#ffffff">
											 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
											 	<logic:equal name="comments" property="key" value="Objective Assumption">
													<logic:iterate name="comments" id="comment" property="value"
														type="org.digijava.module.aim.dbentity.AmpComments"><b>
														<digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
	                                        	<logic:equal name="comments" property="key" value="Objective Verification">
													<logic:iterate name="comments" id="comment" property="value"
														type="org.digijava.module.aim.dbentity.AmpComments"><b>
														<digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
	                                        	<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
													<logic:iterate name="comments" id="comment" property="value"
														type="org.digijava.module.aim.dbentity.AmpComments"><b>
														<digi:trn key="aim:objectivelyVerificationIndicators">Objective Objectively Verifiable Indicators</digi:trn>:</b>
														<bean:write name="comment" property="comment"/><br/>
	                                        		</logic:iterate>
	                                        	</logic:equal>
											</logic:iterate>										</td>
										</tr>
										</logic:present>
									</field:display>
									 </feature:display>
                                       <feature:display name="Identification" module="Project ID and Planning">                                     
									<field:display feature="Identification" name="Description">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:description">
										    Description</digi:trn>		
									  </b>								</td>
									  <td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.description!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.description}" />
											<digi:edit key="${descKey}"></digi:edit>
                                        </c:if>										</td>
									</tr>
									</field:display>
									<field:display feature="Identification" name="Purpose">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:purpose">
										    Purpose</digi:trn> 
									  </b>										</td>
		    <td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.purpose}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:purposeComments">
										    Purpose Comments</digi:trn>		
									  </b>								</td>
				       <td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>										</td>
									</tr>
									</logic:present>
									</field:display>

									<field:display feature="Identification" name="Results">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:results">
										    Results</digi:trn>	
									  </b>									</td>
		    <td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.results!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.results}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="allComments">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:resultsComments">
										    Results Comments</digi:trn>		
									  </b>								</td>
				       <td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.module.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:</b>
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>										</td>
									</tr>
									</logic:present>
									</field:display>
									
<field:display name="Accession Instrument" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b>
									  <digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.accessionInstrument > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.accessionInstrument}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="A.C. Chapter" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" ><b>
									  <digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn>	</b>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.acChapter > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.acChapter}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>

		        </feature:display>
<feature:display name="Identification" module="Project ID and Planning">
                                        
                                        
										<field:display name="Activity Budget" feature="Identification">
										<tr>
											<td width="27%" align="right" valign="top" nowrap="nowrap">
												<b>
											  <digi:trn key="aim:actBudget"> Budget</digi:trn>
											  	</b>
											</td>
										  <td bgcolor="#ffffff">
	
											<logic:equal name="aimEditActivityForm" property="budget" value="true">
											<digi:trn key="aim:actBudgeton">
													Activity is On Budget										</digi:trn>
											</logic:equal>
	
											<logic:equal name="aimEditActivityForm" property="budget" value="false">
											<digi:trn key="aim:actBudgetoff">
													Activity is Off Budget										</digi:trn>
											</logic:equal>
	
											<logic:equal name="aimEditActivityForm" property="budget" value="">
											<digi:trn key="aim:actBudgetoff">
													Activity is Off Budget										</digi:trn>
											</logic:equal>										</td>
										</tr>
										</field:display>
									</feature:display>
									<field:display feature="Identification" name="Organizations and Project ID">
											<tr>
												<td align="right" valign="top" nowrap="nowrap">
													<b>
													<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn>
												  </b>												</td>
										  <td width="73%" bgcolor="#ffffff">
<c:if test="${!empty aimEditActivityForm.selectedOrganizations}">
														<table cellSpacing=2 cellPadding=2 border=0>
															<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.selectedOrganizations}" >
																<c:if test="${not empty selectedOrganizations}">
																	<tr><td>
																		<c:out value="${selectedOrganizations.name}"/>:
																		<c:out value="${selectedOrganizations.projectId}"/>																		
																	</td></tr>
																</c:if>
															</c:forEach>
														</table>
													</c:if>										</td>
										  </tr>
										</field:display>
                                        
                                        <feature:display module="Project ID and Planning" name="Planning">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
					            <digi:trn key="aim:planning">Planning</digi:trn>		</b>
											</td>
<td bgcolor="#ffffff">
												<table width="100%" cellSpacing=2 cellPadding=1>
												<field:display feature="Planning" name="Line Ministry Rank">
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.lineMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.lineMinRank != -1}">
													${aimEditActivityForm.lineMinRank}													</c:if>													</td>
												</tr>
												</field:display>
												<field:display name="Ministry of Planning Rank" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:planMinRank">
													Ministry of Planning Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.planMinRank != -1}">
													${aimEditActivityForm.planMinRank}													</c:if>													</td>
												</tr>
												</field:display>

												<field:display name="Proposed Approval Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:originalApprovalDate">
													Original Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalAppDate}													</td>
												</tr>
												</field:display>
												<field:display name="Actual Approval Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:revisedApprovalDate">Revised Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedAppDate}													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:originalStartDate">Original Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.originalStartDate}													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Contracting" feature="Planning">
												<tr>
													<td width="32%">													
													<digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.contractingDate}"/>													</td>
												</tr>
												</field:display>
												<field:display name="Final Date for Disbursements" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.disbursementsDate}"/>													</td>
												</tr>
												</field:display>
												<field:display name="Actual Start Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:revisedStartDate">Revised Start Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.revisedStartDate}													</td>
												</tr>
												</field:display>
												<field:display name="Proposed Completion Date" feature="Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.proposedCompDate}													</td>
												</tr>
												</c:if>
												</field:display>
												<field:display name="Actual Completion Date" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:currentCompletionDate">
													Current Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.currentCompDate}"/>													</td>
												</tr>
												</field:display>
												<c:if test="${aimEditActivityForm.editAct}">
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
																	<c:out value="${closeDate}"/>																</td>
															</tr>
															</c:forEach>
														</table>													</td>
												</tr>
												</c:if>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												</c:if>
											</table>
											</div>										</td>
									</tr>
									</feature:display>
                                    <feature:display name="Identification" module="Project ID and Planning">
										
										<field:display name="Status" feature="Planning">							
                                            <tr>
                                            	  <td align="right" valign="top" nowrap="nowrap" >
                                                  	<b>
                                                  	<digi:trn key="aim:status"> Status</digi:trn>
                                                  	</b>                      	      
                                                  	</td>
													<td bgcolor="#FFFFFF">
												   		<category:getoptionvalue categoryValueId="${aimEditActivityForm.statusId}"/><br><br>
                                                        <c:out value="${aimEditActivityForm.statusReason}"/>
                                                   </td>									
                                         	 </tr>
										</field:display>
										<field:display name="Credit Type" feature="Planning">							
                                            <tr>
                                            	  <td align="right" valign="top" nowrap="nowrap" >
                                                  	<b>
                                                  		<digi:trn key="aim:type_of_credit">Type of Credit</digi:trn>                                       	      
                                                  	</b>                      	      
                                                  	</td>
													<td bgcolor="#FFFFFF">
												   		<category:getoptionvalue categoryValueId="${aimEditActivityForm.creditTypeId}"/>
                                                   </td>									
                                         	 </tr>
										</field:display>
										
	                                    <field:display name="Credit/Donation" feature="Planning">
	                                        <tr>
		                                        <td align="right" valign="top" nowrap="nowrap" >
		                                        	<b>
														<digi:trn key="aim:credit_donation">Credit/Donation</digi:trn> 
													</b>                                       	      
												</td>
		                                        <td bgcolor="#FFFFFF">
		                                        	<c:set var="creditTypeId">
		                                        		<category:getoptionvalue categoryValueId="${aimEditActivityForm.creditTypeId}"/>
		                                        	</c:set>
		                                        	<c:if test="${creditTypeId == 'Comercial' || creditTypeId == 'Concesional'}">
		                                        		<digi:trn key="aim:preview_credit">CREDITO</digi:trn>	                                        		 
		                                        	</c:if>
													<c:if test="${creditTypeId == 'Donación'}">
														<digi:trn key="aim:preview_donation">DONACIÓN</digi:trn>	                                        		
		                                        	</c:if>
		                                        </td>                                        
	                                        </tr>
	                                    </field:display> 
                                    										
                                        </feature:display>
                                        <feature:display name="References" module="References">
									<tr>
									<td width="27%" align="right" valign="top" nowrap="nowrap">
									<b>
									<digi:trn key="aim:printPreview:references"> References</digi:trn>
									</b>
									</td>
									<td bgcolor="#ffffff">
									<c:forEach items="${aimEditActivityForm.referenceDocs}" var="refDoc" varStatus="loopstatus">
										<table border="0">
											<tr>
												<td>
													<c:if test="${!empty refDoc.comment}">
													${refDoc.categoryValue}													</c:if>												</td>
											</tr>
										</table>
									</c:forEach>									</td>
									</tr>
									</feature:display>
								
                               
                                 <feature:display name="Location" module="Project ID and Planning">
                               		<field:display name="Implementation Location" feature="Location">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:location">
										    Location</digi:trn>
									  </b>										</td>
<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="locations" items="${aimEditActivityForm.selectedLocs}">
													<tr>
													<td>
													<c:if test="${!empty locations.country}">
														[<c:out value="${locations.country}"/>]													</c:if>
													<c:if test="${!empty locations.region}">
														[<c:out value="${locations.region}"/>]													</c:if>
													<c:if test="${!empty locations.zone}">
														[<c:out value="${locations.zone}"/>]													</c:if>
													<c:if test="${!empty locations.woreda}">
														[<c:out value="${locations.woreda}"/>]													</c:if>													</td>
													<td align="right">
														${locations.percent}%													</td>
													</tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</field:display>
                                    
                                    <field:display name="Implementation Level" feature="Location">	  
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:level">
										    Level</digi:trn>
										  </b>
										</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.levelId>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.levelId}"/>
											</c:if>										</td>
									</tr>
									</field:display>
                            </feature:display>   
                                    <feature:display name="Sectors" module="Project ID and Planning">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" class="t-name">
											<b>
									  <digi:trn key="aim:sector">	 Sector</digi:trn>
									  	</b>									</td>
<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.activitySectors}">
													<tr><td>
													<c:if test="${!empty sectors.sectorName}">
																				<c:out value="${sectors.sectorName}" />
																			</c:if>&nbsp;&nbsp; <c:if
																				test="${sector.sectorPercentage!=''}">
																				<c:if test="${sector.sectorPercentage!='0'}">
																			(<c:out value="${sectors.sectorPercentage}" />)%																			</c:if>
																			</c:if> <c:if test="${!empty sectors.subsectorLevel1Name}">
														[<c:out value="${sectors.subsectorLevel1Name}"/>]
													</c:if>
													<c:if test="${!empty sectors.subsectorLevel2Name}">
														[<c:out value="${sectors.subsectorLevel2Name}"/>]													</c:if>
													</td>
													</tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
                                          
                                  <c:if test="${not empty aimEditActivityForm.activityComponentes}">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
									  	<b><digi:trn key="aim:preview:component_Sector">Components</digi:trn></b>
									  	</td>
									  <td bgcolor="#ffffff">
												<table>
													<c:forEach var="compo" items="${aimEditActivityForm.activityComponentes}">
													<tr>
														<td width="100%">
															${compo.sectorName}														</td>
														<td align="right">
															${compo.sectorPercentage}%														</td>
													</tr>
													</c:forEach>
												</table>
									  </td>
									</tr>
								  </c:if>
									
                                    
                                  <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
								
                                	<feature:display name="NPD Programs" module="National Planning Dashboard">
									<field:display name="National Plan Objective" feature="NPD Programs">
									<TR>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
												<b>
								      <digi:trn key="aim:national Plan Objective"> National Plan Objective</digi:trn>
												</b></TD>

<TD bgcolor="#ffffff">
											<c:forEach var="program" items="${aimEditActivityForm.nationalPlanObjectivePrograms}">
                                                 <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                             </c:forEach>
                                      </TD>
									</TR>
                                      </field:display> 
                                     <field:display name="Primary Program" feature="NPD Programs">
                                           <TR>
												<td width="27%" align="right" valign="top" nowrap="nowrap">
																												<b>
										     <digi:trn key="aim:primary Programs"> Primary Programs</digi:trn>
																												</b></TD>

						  <TD bgcolor="#ffffff">
								<c:forEach var="program" items="${aimEditActivityForm.primaryPrograms}">
                                	<c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                </c:forEach>
                   		</TD>
						</TR>
										</field:display>
										<field:display name="Secondary Program" feature="NPD Programs">
                                         	<TR>
												<td width="27%" align="right" valign="top" nowrap="nowrap" >
													<b>	
											  <digi:trn key="aim:secondary Programs"> Secondary Programs</digi:trn>
													</b></TD>
<TD bgcolor="#ffffff">
                                                      <c:forEach var="program" items="${aimEditActivityForm.secondaryPrograms}">
	                                                      <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                      </c:forEach>
                                        		</TD>
											</TR>
										</field:display>
									</feature:display>
                                   </module:display>
                                   
                                  <logic:present name="currentMember" scope="session">
									<module:display name="Funding" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
										  <digi:trn key="aim:funding"> Funding</digi:trn>
</b>
									  </td>
										<td bgcolor="#ffffff">
										    <table width="100%" cellSpacing=1 cellPadding=0 border=0 align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing=8 cellPadding=0 border=0 width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">

                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellSpacing=1 cellPadding=0 border="0" width="100%" class="box-border-nopadding">
                                                                  <tr>
                                                                    <td>
                                                                      <table cellSpacing=1 cellPadding=0 border="0" width="100%">
                                                                        <tr>
                                                                          <td>
                                                                            <table width="100%" border="0" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
                                                                            <field:display name="Funding Organization Id" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">																																
                                                                                  <digi:trn key="aim:fundingOrgId">
                                                                                    Funding Organization Id</digi:trn></a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td width="454" align="left" bgcolor="#FFFFFF">
                                                                                <bean:write name="funding"	property="orgFundingId"/>                                                                                </td>
                                                                              </tr>
                                                                             </field:display>
                                                                             <field:display name="Funding Organization Name" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="339">

                                                                                  <a title="<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>">
                                                                               	  <digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
                                                                                  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  ${fundingOrganization.orgName}                                                                                </td>
                                                                              </tr>
                                                                             </field:display>

                                                                              <!-- type of assistance -->
                                                                              <field:display name="Type Of Assistance" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																				  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="typeOfAssistance">
                                                                                    <bean:write name="funding"	property="typeOfAssistance.value"/>
                                                                                  </logic:notEmpty>                                                                                </td>
                                                                              </tr>
																			</field:display>
																			<field:display name="Type Of Assistance" feature="Funding Organizations">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                               	  <digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																				  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="financingInstrument">
                                                                                    <bean:write name="funding"	property="financingInstrument.value"/>
                                                                                  </logic:notEmpty>                                                                                </td>
                                                                              </tr>
																			</field:display>
				                                                            </table>                                          </td>
                                                                        </tr>
                                                                      </table>                                    </td>
                                                                  </tr>
                                                                  <tr>
                                                                    <td>
                                                                      <table width="98%" border="0" cellpadding="1"   bgcolor="#ffffff" cellspacing="1">
                                                                        <tr>
                                                                          <td bgcolor="#FFFFFF" align="right" colspan="2">
                                                                            <table width="100%" border="0" cellSpacing="1" cellPadding="1" bgcolor="#dddddd">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="4"><b>
                                                                                  <a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>">
                                                                                  <digi:trn key="aim:commitments">Commitments </digi:trn> 
                                                                                  </a>
                                                                                  </b>                                                                                </td>
                                                                                <td width="25%"><b>
                                                                                <c:if test="${aimEditActivityForm.fixerate == true}">
                                                                                	<digi:trn key="aim:exchange">Exchange Rate</digi:trn></b>
                                                                                </c:if>	                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="0">

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#FFFF00">                                                                                    </c:if>
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">                                                                                    </c:if>

                                                                                    <td width="50">
	                                                                                    <field:display name="Adjustment Type Commitment" feature="Funding Organizations">																						
    	                                                                                	<digi:trn key='<%="aim:commitments:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</digi:trn>
																						</field:display>                                                                                    </td>


                                                                                    <td width="120" align="right">
                                                                                      <field:display name="Amount Commitment" feature="Funding Organizations">
                                                                                      	<FONT color=blue>*</FONT>
                                                                                      	<bean:write name="fundingDetail" property="transactionAmount" format="###,###,###,###,###"/>&nbsp;                                                                                      </field:display>                                                                                    </td>

                                                                                    <td width="150">
	                                                                                    <field:display name="Currency Commitment" feature="Funding Organizations">
    	                                                                                  <bean:write name="fundingDetail" property="currencyCode"/>
       	                                                                              </field:display>                                                                                    </td>
                                                                                    <td width="70">
                                                                                    	<field:display name="Date Commitment" feature="Funding Organizations">
		                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
	                                                                                    </field:display>                                                                                    </td>
                                                                                    <td>
                                                                                    	<field:display name="Exchange Rate" feature="Funding Organizations">
   																							 <bean:write name="fundingDetail" property="formattedRate" format="###.##"/>
																					  </field:display>                                                                                    </td>
                                                                                      </tr>
                                                                                  </c:if>

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == false}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Funding Organizations">
                                                                                          <digi:trn key='<%="aim:commitments:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																						  </digi:trn>
																						  </field:display>                                                                                        </td>
                                                                                        <td width="120" align="right">
                                                                                        <field:display name="Amount Commitment" feature="Funding Organizations">
                                                                                          <FONT color=blue>*</FONT>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;                                                                                          </field:display>                                                                                        </td>
                                                                                        <td width="150">
                                                                                        <field:display name="Currency Commitment" feature="Funding Organizations">
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                          </field:display>                                                                                        </td>
                                                                                        <td width="70">
                                                                                       	 	<field:display name="Date Commitment" feature="Funding Organizations">
                                                                                          		<bean:write name="fundingDetail" property="transactionDate"/>
                                                                                          	</field:display>                                                                                        </td>
                                                                                        <td>
	                                                                                        <field:display name="Exchange Rate" feature="Funding Organizations">
   																									<bean:write name="fundingDetail" property="formattedRate" format="###.##"/>
																							</field:display>                                                                                        </td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                     <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Funding Organizations">
                                                                                          <digi:trn key='<%="aim:commitments:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																						  </digi:trn>
																						</field:display>                                                                                        </td>
                                                                                        <td width="120" align="right">
	                                                                                        <field:display name="Amount Commitment" feature="Funding Organizations">
    	                                                                                      <FONT color=blue>*</FONT>
        	                                                                                  <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;        	                                                                                 </field:display>                                                                                        </td>
                                                                                        <td width="150">
	                                                                                        <field:display name="Currency Commitment" feature="Funding Organizations">
    	                                                                                      <bean:write name="fundingDetail" property="currencyCode"/>
    	                                                                                    </field:display>                                                                                        </td>
                                                                                        <td width="70">
	                                                                                        <field:display name="Date Commitment" feature="Funding Organizations">
    	                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
    	                                                                                    </field:display>                                                                                        </td>
                                                                                        <td>
   																							<field:display name="Exchange Rate" feature="Funding Organizations">
   																									<bean:write name="fundingDetail" property="formattedRate"/>
																							</field:display>																						</td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                  </c:if>
                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                                <tr>
                                                                                <td><digi:trn key='aim:totalcommittment'>
                                                                                TOTAL:
                                                                                </digi:trn></td>
                                                                                      <TD  colspan="4" align="right"><bean:write name="aimEditActivityForm" property="totalCommitted"/>&nbsp;USD</TD>
                                                                                </tr>
                                                                              </c:if>
                                                                           
                                                                                    <!-- Disbursement orders-->
                                                                              <feature:display module="Funding" name="Disbursement orders">

                                                                                <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>"><b> <digi:trn key="aim:disbursementOrders">Disbursements Orders</digi:trn></b>
																				</a>
                                                                                </td>
                                                                              </tr>

                                                                                <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails"
                                                                              id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                              <logic:equal name="fundingDetail" property="transactionType" value="4">

                                                                                 <tr bgcolor="#ffffff">

                                                                                 <td width="50">
                                                                                 <field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders">
                                                                                 <digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
                                                                                 <bean:write name="fundingDetail" property="adjustmentTypeName"/>
                                                                                 </digi:trn>
                                                                                 </field:display>
                                                                                 </td>
                                                                                 <td width="120" align="right">
                                                                                 <field:display name="Amount of Disbursement Order" feature="Disbursement Orders">
                                                                                 <FONT color=blue>*</FONT>
                                                                                 <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                 </field:display>
                                                                                 </td>
                                                                                 <td width="150">
                                                                                 <field:display name="Currency of Disbursement Order" feature="Disbursement Orders">
                                                                                 <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                 </field:display>
                                                                                 </td>
                                                                                 <td width="70">
                                                                                 <field:display name="Date of Disbursement Order" feature="Disbursement Orders">
                                                                                 <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                 </field:display>
                                                                                </td>
                                                                                <td>&nbsp;
                                                                                
                                                                                </td>

                                                                                </tr>
                                                                                </logic:equal>
                                                                                </logic:iterate>
                                                                                </c:if>
                                                                                 <tr>
                                                                                <td><digi:trn key='aim:totalDisbursementOrder'>
                                                                                TOTAL:
                                                                                </digi:trn></td>
                                                                                      <TD  colspan="4" align="right"><bean:write name="aimEditActivityForm" property="totalDisbOrder"/>&nbsp;USD</TD>
                                                                                </tr>
                                                                              </feature:display>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>">
                                                                                   <b><digi:trn key="aim:disbursements">Disbursements </digi:trn></b>																				  </a>                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails"
                                                                              id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                              <logic:equal name="fundingDetail" property="transactionType" value="1">
                                                                              <logic:equal name="fundingDetail" property="adjustmentType" value="0">


                                                                                <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                  <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                    <tr bgcolor="#FFFF00">                                                                                  </c:if>
                                                                                  <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">																						</c:if>
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>

																						</tr>
																				</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						</c:if>
																						</c:if>
																				</logic:equal>
																				</logic:equal>
																				</logic:iterate>
																				
																				<tr>
	                                                                                <td>
	                                                                                	<digi:trn key='aim:totalplanneddisbursement'>
	                                                                                		TOTAL PLANNED DISBURSEMENT:
	                                                                                	</digi:trn>
	                                                                                </td>
	                                                                                <TD  colspan="4" align="right">	                                                                                	
	                                                                                	<c:out value="${aimEditActivityForm.totalPlannedDisbursements}"/>&nbsp;<bean:write name="aimEditActivityForm" property="currCode"/>
	                                                                                </TD>
	                                                                           </tr>
	                                                                           	
	                                                                           <tr bgcolor="#ffffff">
	                                                                                <td>&nbsp;</td>
	                                                                                <TD colspan="4" align="right">&nbsp;</TD>
	                                                                           </tr>																			

																			  <!-- Start Actual Disbursements -->
                                                                              <logic:iterate name="funding" property="fundingDetails"
                                                                              id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                               <logic:equal name="fundingDetail" property="transactionType" value="1">
                                                                               <logic:equal name="fundingDetail" property="adjustmentType" value="1">


                                                                                <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                  <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                    <tr bgcolor="#FFFF00">                                                                                  </c:if>
                                                                                  <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">																						</c:if>
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>

																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<field:display name="Adjustment Type Disbursement" feature="Funding Organizations">
																									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Disbursement" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Disbursement" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						</c:if>
																						</c:if>
																						</logic:equal>
																					    </logic:equal>
																					  </logic:iterate>
																					  <!-- End Actual Disbursements -->																				
																				
			                                                                                <tr>
			                                                                                <td><digi:trn key='aim:totaldisbursement'>
			                                                                                TOTAL:
			                                                                                </digi:trn></td>
			                                                                                      <TD  colspan="4" align="right"><bean:write name="aimEditActivityForm" property="totalDisbursed"/>&nbsp;USD</TD>
			                                                                                </tr>																						
                                                                              </c:if>
																						<tr bgcolor="#ffffff">
																							<td colspan="5">&nbsp;</td>
																						</tr>
																						<!-- expenditures -->
	<feature:display module="Funding" name="Expenditures">
                                                                                        <tr bgcolor="#ffffff">
																							<td colspan="5">
																							<a title='<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>'>	<b><digi:trn key="aim:expenditures"> Expenditures </digi:trn></b>																							</a>																							</td>
																						</tr>
                                                                                        <c:if test="${!empty funding.fundingDetails}">
																						<logic:iterate name="funding" property="fundingDetails"
																						id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
																						<logic:equal name="fundingDetail" property="transactionType" value="2">


																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#FFFF00">																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">																						</c:if>
																							<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>

																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>																							</td>
																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																							<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																								<tr bgcolor="#ffffff">
																									<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70" colspan="2">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>																							</td>
																						</tr>
																							</c:if>
																							<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																								<tr bgcolor="#ffffff">
																								<td width="50">
																								<field:display name="Adjustment Type Expenditure" feature="Funding Organizations">
																									<digi:trn key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</digi:trn>
																								</field:display>																							</td>
																							<td width="120" align="right">
																								<field:display name="Amount Expenditure" feature="Funding Organizations">
																									<FONT color=blue>*</FONT>
																									<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;																								</field:display>																							</td>
																							<td width="150">
																								<field:display name="Currency Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="currencyCode"/>
																								</field:display>																							</td>
																							<td width="70">
																								<field:display name="Date Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="transactionDate"/>
																								</field:display>																							</td>
																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<field:display name="Classification Expenditure" feature="Funding Organizations">
																									<bean:write name="fundingDetail" property="classification"/>
																								</field:display>																							</td>
																						</tr>
																							</c:if>
																						</c:if>
																						</logic:equal>
																						</logic:iterate>
                                                                                        </c:if>
                                                                                        </feature:display>
																			</table>																				</td>
																		</tr>
																	  </table>																	</td></tr>
																	<tr><td bgcolor="#ffffff">
																		<FONT color=blue>*
																			<digi:trn key="aim:theAmountEnteredAreInThousands">
																				The amount entered are in thousands (000)		  																	</digi:trn>
																		</FONT>
																	</td></tr>
																</table>																	</td></tr>
														  </logic:iterate>
														</logic:notEmpty>
													  </logic:iterate>
																<tr><td>&nbsp;</td></tr>
													</logic:notEmpty>
												  </table>														</td>
											  </tr>
										  </table>
									  </td>
									</tr>
									</module:display>
								  </logic:present>  
                                    <feature:display name="Regional Funding" module="Funding">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:regionalFunding">
										   Regional Funding</digi:trn>		
										  </b>
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.regionalFundings}">
													<tr><td bgcolor="#ffffff">
														<table width="100%" cellSpacing="1" cellPadding="1">
															<tr><td bgcolor="#ffffff"><b>
																<c:out value="${regFunds.regionName}"/></b>
															</td></tr>
															<c:if test="${!empty regFunds.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName}"/>	
																								</digi:trn>
																																															</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.perspectiveNameTrimmed}">
																										<c:out value="${fd.perspectiveName}"/>	
																							</digi:trn>
																																														</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																									<digi:trn key="aim:disbursements:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName}"/>	
																								</digi:trn>																						</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:disbursements:${fd.perspectiveNameTrimmed}">
																										<c:out value="${fd.perspectiveName}"/>	
																							</digi:trn>																						</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty regFunds.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:expenditures:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName}"/>	
																								</digi:trn>
																							</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																									<digi:trn key="aim:expenditures:${fd.perspectiveNameTrimmed}">
																										<c:out value="${fd.perspectiveName}"/>	
																									</digi:trn>
																							</td>																							</td>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
														</table>
													</td></tr>
												</c:forEach>
												<tr><td bgcolor="#ffffff">
													<FONT color=blue>*
													<digi:trn key="aim:theAmountEnteredAreInThousands">
													The amount entered are in thousands (000)</digi:trn></FONT>
												</td></tr>
												</table>
											</c:if>										</td>
									</tr>
									</feature:display>
									
									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
                                    <module:display name="Components" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:components">
										    Components</digi:trn>
										  </b>								
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td><b>
																<c:out value="${comp.title}"/></b>
															</td></tr>

															<tr><td>
																<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<c:out value="${comp.description}"/>
															</td></tr>

															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.commitments}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:commitments">
																				Commitments</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<field:display name="Components Actual/Planned Commitments" feature="Components">
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.adjustmentTypeNameTrimmed}">
																									<c:out value="${fd.adjustmentTypeName}"/>	
																								</digi:trn>
																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Commitments" feature="Components">
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color="blue">*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Commitments" feature="Components">
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Commitments" feature="Components">
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																							<field:display name="Components Perspective Commitments" feature="Components">
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.perspectiveNameTrimmed}">
																									<c:out value="${fd.perspectiveName}"/>	
																								</digi:trn>
																							</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.disbursements}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:disbursements">
																				Disbursements</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.disbursements}">
																						<tr>
																							<field:display name="Components Actual/Planned Disbursements" feature="Components">
																							<td width="50" bgcolor="#ffffff">
																							<digi:trn key="aim:disbursements:${fd.adjustmentTypeNameTrimmed}">
																								<c:out value="${fd.adjustmentTypeName}"/>	
																							</digi:trn>								
																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Disbursements" feature="Components">
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color="blue">*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Disbursements" feature="Components">
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Disbursements" feature="Components">
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																							<field:display name="Components Perspective Disbursements" feature="Components">
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:disbursements:${fd.perspectiveNameTrimmed}">
																									<c:out value="${fd.perspectiveName}"/>	
																								</digi:trn>	
																																													</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<c:if test="${!empty comp.expenditures}">
																<tr><td bgcolor="#ffffff">
																	<table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
																		<tr>
																			<td valign="top" width="100" bgcolor="#ffffff">
																				<digi:trn key="aim:expenditures">
																				Expenditures</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.expenditures}">
																						<tr bgcolor="#ffffff">
																							<field:display name="Components Actual/Planned Expenditures" feature="Components">
																							<td width="50">
																								<digi:trn key="aim:expenditures:${fd.adjustmentTypeNameTrimmed}">
																									<c:out value="${fd.adjustmentTypeName}"/>	
																								</digi:trn>																							</td>
																							</field:display>
																							<field:display name="Components Total Amount Expenditures" feature="Components">
																							<td align="right">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Expenditures" feature="Components">
																							<td>
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Expenditures" feature="Components">
																							<td width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																							<field:display name="Components Perspective Expenditures" feature="Components">
																							<td>
																								<digi:trn key="aim:expenditures:${fd.perspectiveNameTrimmed}">
																									<c:out value="${fd.perspectiveName}"/>	
																								</digi:trn>																						</td>
																							</field:display>
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<tr><td bgcolor="#ffffff">
																<FONT color="blue">*
																	<digi:trn key="aim:theAmountEnteredAreInThousands">
																		The amount entered are in thousands (000)		  															</digi:trn>
																</FONT>
															</td></tr>
															<field:display name="Components Physical Progress" feature="Components">
															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:physicalProgressOfTheComponent">
																Physical progress of the component</digi:trn></b>
															</td></tr>
															<c:if test="${!empty comp.phyProgress}">
																<c:forEach var="phyProg" items="${comp.phyProgress}">
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<b>
																			<c:out value="${phyProg.title}"/></b> -
																			<c:out value="${phyProg.reportingDate}"/>
																	</td></tr>
																	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<i>
																		<digi:trn key="aim:description">Description</digi:trn> :</i>
																		<c:out value="${phyProg.description}"/>
																	</td></tr>
																</c:forEach>
															</c:if>
															</field:display>
														</table>
													</td></tr>
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>
								  </module:display>
									</logic:equal>
									
									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">									
									<module:display name="Components_Resume" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:components">
										    Components</digi:trn>
										  </b>								
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td><b>
																<c:out value="${comp.title}"/></b>
															</td></tr>

															<tr><td>
																<i>
																<digi:trn key="aim:component_code">Component code</digi:trn> :</i>
																<c:out value="${comp.code}"/>
															</td></tr>															

															<tr><td>
															 <c:out value="${comp.url}"/><c:out value="${comp.code}"/>																
															</td></tr>

															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>

															<tr>
																	<td bgcolor="#ffffff">
																		<table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
																		<c:forEach var="financeByYearInfo" items="${comp.financeByYearInfo}">
																		<tr>
																			<td width="20%" valign="top" bgcolor="#ffffff">
																				<c:out value="${financeByYearInfo.key}"/>
																			</td>
																			<c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
																			<td bgcolor="#ffffff" align="left">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																				<fmt:timeZone value="US/Eastern">
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>																							
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																								
																								<fmt:formatNumber type="number" pattern="0.00" value="${financeByYearInfoMap['MontoProgramado']}" />																						
																							</td>																																													
																						</tr>
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>																																															
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																																															
																								<fmt:formatNumber type="number" pattern="0.00" value="${financeByYearInfoMap['MontoReprogramado']}" />																																														
																							</td>																						
																						</tr>																																																																		
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>																																															
																							</td>																							
																							<td align="right" width="100" bgcolor="#ffffff">																																															
																								<fmt:formatNumber type="number" pattern="0.00" value="${financeByYearInfoMap['MontoEjecutado']}" />																							
																							</td>																						
																						</tr>
																				</fmt:timeZone>
																				</table>																			
																			</td>
																		</tr>
																		<tr>
																			<td>&nbsp;
																				
																			</td>
																			<td>&nbsp;
																				
																			</td>																			
																		</tr>																	
																		</c:forEach>
																	</table>																	
																	</td>
																</tr>
														</table>
													</td></tr>
													<tr>
														<td>&nbsp;</td>
													</tr>													
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>

									</module:display>									
                                    </logic:equal>
								  
								  
                               	  <module:display name="Issues" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:issues">
										    Issues</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border="0">
												<c:forEach var="issue" items="${aimEditActivityForm.issues}">
													<tr><td valign="top">
														<li class="level1"><b><c:out value="${issue.name}"/></b></li>
													</td></tr>
													<c:if test="${!empty issue.measures}">
														<c:forEach var="measure" items="${issue.measures}">
															<tr><td>
																<li class="level2"><i><c:out value="${measure.name}"/></i></li>
															</td></tr>
															<c:if test="${!empty measure.actors}">
																<c:forEach var="actor" items="${measure.actors}">
																	<tr><td>
																		<li class="level3"><c:out value="${actor.name}"/></li>
																	</td></tr>
																</c:forEach>
															</c:if>
														</c:forEach>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</module:display>
                             <module:display name="Document" parentModule="PROJECT MANAGEMENT">       
                                   	<feature:display name="Related Documents" module="Document">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:relatedDocuments">
										    Related Documents</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">											
											<c:if test="${!empty aimEditActivityForm.documentList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents"
													id="docs" type="org.digijava.module.aim.helper.Documents">
													<c:if test="${docs.isFile == true}">
													<tr><td>
													 <table width="100%" class="box-border-nopadding">
													 	<tr bgcolor="#ffffff">
															<td vAlign="center" align="left">
																&nbsp;<b><c:out value="${docs.title}"/></b> -
																&nbsp;&nbsp;&nbsp;<i><c:out value="${docs.fileName}"/></i>
																<logic:notEqual name="docs" property="docDescription" value=" ">
																	<br />&nbsp;
																	<b>Description:</b>&nbsp;<bean:write name="docs" property="docDescription" />
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b>Date:</b>&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b>Document Type:</b>&nbsp;
																	<bean:write name="docs" property="docType"/>
																</logic:notEmpty>															</td>
														</tr>
													 </table>
													</td></tr>
													</c:if>
													</logic:iterate>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docList" items="${aimEditActivityForm.linksList}">
					   							<bean:define id="links" name="docList" property="relLink" />
													<tr><td>
														<table width="100%" class="box-border-nopadding">
															<tr>
																<td width="2">
																	<digi:img src="module/aim/images/web-page.gif"/>																</td>
																<td align="left" vAlign="center">&nbsp;
																	<b><c:out value="${links.title}"/></b> -
																	&nbsp;&nbsp;&nbsp;<i><a href="<c:out value="${links.url}"/>">
																	<c:out value="${links.url}"/></a></i>
																	<br>&nbsp;
																	<b>Desc:</b>&nbsp;<c:out value="${links.description}"/>																</td>
															</tr>
														</table>
													</td></tr>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</feature:display>
                                 </module:display>   
                                     
								<module:display name="Organizations" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:relatedOrganizations">
										    Related Organizations</digi:trn>
									  </b>									</td>

<td bgcolor="#ffffff">
                                           <feature:display name="Executing Agency" module="Organizations">
											<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="executingAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="executingAgencies"
													id="execAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="execAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty>
											<br/>
											</feature:display>

											<feature:display name="Implementing Agency" module="Organizations">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="impAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="impAgencies"
													id="impAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="impAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/></feature:display>

											<feature:display name="Beneficiary Agency" module="Organizations">
																			
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>
									
											<logic:notEmpty name="aimEditActivityForm" property="benAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="benAgencies"
														id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
																<ul><li> <bean:write name="benAgency" property="name" /></li></ul>
														</logic:iterate>
														</td></tr>
													</table>
												</logic:notEmpty><br/>
											</feature:display>

											<feature:display name="Contracting Agency" module="Organizations">
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="conAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="conAgencies"
													id="conAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="conAgencies" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											
											</logic:notEmpty><br/>
											</feature:display>


											<feature:display name="Sector Group" module="Organizations"></feature:display>
											<field:display name="Sector Group" feature="Sector Group">
											<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="sectGroups">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="sectGroups"
														id="sectGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<ul><li> <bean:write name="sectGroup" property="name" /></li></ul>
														</logic:iterate>
														</td></tr>
													</table>											
											</logic:notEmpty><br/>
											</field:display>
		
        									<feature:display name="Regional Group" module="Organizations">
											<field:display name="Regional Group" feature="Regional Group">
											<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="regGroups">
											<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="regGroups"
													id="regGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<ul><li> <bean:write name="regGroup" property="name" /></li></ul>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</field:display>
                                  	</feature:display>
                                           </td>
									</tr>
								
									</module:display>
									

									
									 
									
                                    <module:display name="Contact Information" parentModule="PROJECT MANAGEMENT">
									<feature:display name="Donor Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:donorFundingContactInformation">
										    Donor funding contact information</digi:trn>	
									  </b>									</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.dnrCntFirstName}"/>
											<c:out value="${aimEditActivityForm.dnrCntLastName}"/> -
											<c:out value="${aimEditActivityForm.dnrCntEmail}"/>										</td>
									</tr>
									</feature:display>
									<feature:display name="Government Contact Information" module="Contact Information">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
										<b>	<digi:trn key="aim:mofedContactInformation">
										    MOFED contact information</digi:trn>	
									  </b>									</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.mfdCntFirstName}"/>
											<c:out value="${aimEditActivityForm.mfdCntLastName}"/> -
											<c:out value="${aimEditActivityForm.mfdCntEmail}"/>										</td>
									</tr>
									</feature:display>
									</module:display>
							
									

								  

									
									
								  
                                
                                
                               	  <field:display name="Project Performance"  feature="Dashboard">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:meActivityPerformance">
										    Activity - Performance</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">
											<% if (actPerfChartUrl != null) { %>
												<img src="<%= actPerfChartUrl %>" width="370" height="450" border="0" usemap="#<%= actPerfChartFileName %>"><br><br>
<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
											    <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>
											    </span><br><br>
											<% } %>										</td>
									</tr>
									</field:display>
									<field:display name="Project Risk" feature="Dashboard">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:meActivityRisk">
										    Activity - Risk</digi:trn>
											</b>							</td>
<td bgcolor="#ffffff">
											<% if (actRiskChartUrl != null) { %>
												<bean:define id="riskColor" name="riskColor" scope="request" toScope="page" type="java.lang.String"/>
												<bean:define id="riskName" name="overallRisk" scope="request" toScope="page" type="java.lang.String"/>
												<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>:
												<font color="${riskColor}"/>

												<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>

												<img src="<%= actRiskChartUrl %>" width="370" height="350" border="0" usemap="#<%= actRiskChartFileName %>">
												<br><br>
											<% } else { %>
												<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
										  	    <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>
											    </span><br><br>
											<% } %>										</td>
									</tr>
									</field:display>
								
                                    
                                    
                              <feature:display name="Proposed Project Cost" module="Funding">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
									  <digi:trn key="aim:proposedPrjectCost"> Proposed Project Cost</digi:trn>	
									  </b>									</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                  <table cellSpacing=1 cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      <tr bgcolor="#ffffff">
															<td>Cost</td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.proProjCost.funAmount!=null}">
																 	<FONT color=blue>*</FONT> ${aimEditActivityForm.proProjCost.funAmount}                                                          </c:if>&nbsp;
																<c:if test="${aimEditActivityForm.proProjCost.currencyCode!=null}"> ${aimEditActivityForm.proProjCost.currencyCode} </c:if>                                                        </td>
												    </tr>
																		  <tr bgcolor="#ffffff">
															<td>Proposed Completion Date  </td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.proProjCost.funDate!=null}">
                                                             ${aimEditActivityForm.proProjCost.funDate}                                                          </c:if>                                                        </td>
                                                      </tr>
                                              </table>
                            </c:if>										</td>
									</tr>
								  </feature:display>
								
                                
                                 <logic:present name="currentMember" scope="session">
									<feature:display name="Costing" module="Activity Costing">
										<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
												<b>
												<digi:trn key="aim:costing"> Costing</digi:trn>
										  </b>
										</td>
											<td bgcolor="#ffffff"><table width="100%">
                                              <tr>
                                                <td>
                                                	<bean:define id="mode" value="preview" type="java.lang.String" toScope="request" />
                                                    <jsp:include page="viewCostsSummary.jsp" flush="" />                                              
                                                </td>
                                              </tr>
                                            </table> 
                                            </td>										
                                      </tr>
									</feature:display>
								  </logic:present>
									        
                           		
													

<field:display name="Activity Created By" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:activityCreatedBy">
										    Activity created by</digi:trn>
											</b>							</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.actAthEmail}"/>										</td>
									</tr>
									</field:display>
									<field:display feature="Identification" name="Data Source">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:dataSource">
										    Data Source</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.actAthAgencySource}"/>										</td>
									</tr>
									</field:display>
									<field:display name="Activity Updated On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="updatedDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:activityUpdatedOn">
										    Activity updated on</digi:trn>
											</b>
										</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedDate}"/>
										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Updated By" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="updatedBy">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:activityUpdatedBy">
										    Activity updated by</digi:trn>
											</b>				
											</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.updatedBy.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.updatedBy.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.updatedBy.user.email}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Created On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="createdDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:activityCreatedOn">
										    Activity created on</digi:trn>
											</b>								</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.createdDate}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<logic:notEmpty name="aimEditActivityForm" property="team">
									<field:display name="Date Team Leader" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:activityTeamLeader">
										    Data Team Leader</digi:trn>
											</b>
										</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.team.teamLead.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.team.teamLead.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.team.teamLead.user.email}"/>										</td>
									</tr>
									</field:display>
									</logic:notEmpty>
    	</table>
        
</c:if>
<c:if test="${aimEditActivityForm==null}">
		Invalid activity id
</c:if>

