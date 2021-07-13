<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
   
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
	<nested:root name="myForm">
				<div class="main_side_cont yui-tab-content" id="type_step_div" style="${topBottomPadding}">
							<c:set var="stepNum" value="0" scope="request" />
							<div id="toolbarMarkerDiv"></div>
							<jsp:include page="toolbar.jsp" />
							<br />
						<c:set var="tableVisibility"></c:set>
						<c:if test="${myForm.onePager }">
							<c:set var="tableVisibility">display: none;</c:set>
							<div style="clear:both;">&nbsp;</div>
							<fieldset style="background-color: #F6F6F6; width:97%;">
								<legend onclick="toggleMoreSettings()" id="moreSettingsLegend" style="cursor: pointer;">+ <digi:trn>${typeAndDescription}</digi:trn></legend>
								<div id="moreSettingsInfoDiv" onclick="toggleMoreSettings()" style="cursor: pointer;">
									<digi:trn>Click '${typeAndDescription}' to expand</digi:trn>....
								</div>	
						</c:if>
							<table style="${tableVisibility}" width="99%" border="0" cellspacing="0" cellpadding="0" id="moreSettingsTable">
								<tr>
									<td width="340px" valign="top">
										<fieldset class="main_side_cont">
										
										<c:if test="${myForm.reportBeingEdited == true }">
										<c:set var="disableFundingType">true</c:set>
										</c:if>
											<legend><span class="legend_label"><digi:trn>Funding Grouping</digi:trn></span></legend>
											<div id="reportGroupDiv" class="inputx text-align">
										<c:set var="pledges_type_const"><%=ArConstants.PLEDGES_TYPE%></c:set>
										<c:set var="donor_type_const"><%=ArConstants.DONOR_TYPE%></c:set>
										<c:choose>
										<c:when test="${param.type==null || param.type!=pledges_type_const}">								
													<feature:display name="Donor Report" module="Report Types">                                      	
				                                    	<nested:radio property="reportType" disabled="${disableFundingType}" value="donor"  onclick="repManager.checkSteps();repManager.callbackRepType('donor');">
				                                           	${donorFunding}
														</nested:radio>
														<br />
													</feature:display>
				                                    <feature:display name="Regional Report" module="Report Types">										
				                                    	<nested:radio property="reportType" disabled="${disableFundingType}" value="regional"  onclick="repManager.checkSteps();repManager.callbackRepType('regional');">
				                                        	${regionalFunding}
				                                      	</nested:radio>
				                                        <br />
													</feature:display>
				                                    <feature:display name="Component Report" module="Report Types">
				                                       	<nested:radio property="reportType" disabled="${disableFundingType}" value="component"  onclick="repManager.checkSteps();repManager.callbackRepType('component');">
					                                		${componentFunding}
				                                        </nested:radio>
				                                        <br />
				                                   	</feature:display>
												</c:when>
                           						<c:otherwise>
                                              <c:set var="disablePledgeType">true</c:set>

                                                		<nested:radio property="reportType" disabled="${disablePledgeType}" value="pledge" onclick="repManager.checkSteps()">
                                                   			${PledgesFunding}
                                                     	</nested:radio>

                                           </c:otherwise>
                                         </c:choose>
                                         </div>

                                         
										</fieldset>
										
									</td>
									<td>&nbsp;</td>
									<td width="340px" valign="top">
										<fieldset class="main_side_cont">
											<nested:hidden property="reportDescription" />
											<legend><span class="legend_label">${descriptionName}</span></legend>
											<textarea name="reportDescriptionClone"  rows="5" class="inputx" style="width:423px; height:67px;"></textarea>
										</fieldset>
									</td>
								</tr>
								<tr>
									<c:if test="${!myForm.desktopTab}">
										<td valign="top">
									    	<fieldset class="main_side_cont">
												<legend><span class="legend_label"><digi:trn>Totals Grouping</digi:trn></span></legend>
												<div id="totalsGroupingDiv" class="inputx text-align">
													
													<nested:checkbox property="hideActivities" value="true" onchange="repManager.checkSteps();createPreview();">
														${summary}
													</nested:checkbox>
													<br />&rlm;
													<nested:radio property="reportPeriod" value="A" onchange="createPreview();">
														<digi:trn key="aim:AnnualReport">Annual Report</digi:trn>
													</nested:radio>
													<br />&rlm;
													<c:if test="${param.type==null || param.type!=pledges_type_const}">
													<nested:radio property="reportPeriod" value="Q" onchange="createPreview();">
														<digi:trn key="aim:QuarterlyReport">Quarterly Report</digi:trn>
													</nested:radio>
													<br />&rlm;
													<nested:radio property="reportPeriod" value="M" onchange="createPreview();">
														<digi:trn key="aim:MonthlyReport">Monthly Report</digi:trn>
													</nested:radio>
													</c:if>
													<br />&rlm;
													<nested:radio property="reportPeriod" value="N" onchange="createPreview();">
														<digi:trn>Totals Only</digi:trn>
													</nested:radio>
												</div>											
											</fieldset>									
										</td>
										<td valign="top">&nbsp;</td>
									</c:if>
									<td valign="top">
										<c:set var="spanUseFiltersVisibility">visibility: hidden</c:set>
										<c:set var="spanUseFiltersHeight">height:90px</c:set>
										<c:set var="spanUseFiltersDisabled">disabled</c:set>
										<c:if test="${myForm.useFilters!=null && myForm.useFilters}">
											<c:set var="spanUseFiltersVisibility">visibility: visible</c:set>
											<c:set var="spanUseFiltersDisabled">enabled</c:set>
										</c:if>
										<fieldset class="main_side_cont text-align">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:subtitle:selectedFilters">Selected Filters</digi:trn></span></legend>
											<div id="listFiltersDiv" class="inputx wizard-filters-div">
												<c:choose>
													<c:when test="${myForm.useFilters!=null && myForm.useFilters}">
														<jsp:include page="showSelectedFilters.jsp" />				
													</c:when>
												</c:choose>
											</div>
											<span id="spanUseFilters">
												<nested:checkbox property="useFilters" styleId="useFiltersCheckbox" onclick="repManager.decideStrikeFilters()" disabled="${spanUseFiltersDisabled}"/> 
												<digi:trn key="rep:wizard:useAboveFilters">Use above filters</digi:trn>
											</span>
										</fieldset>
									</td>
								</tr>
					  			<c:if test="${ (!myForm.desktopTab) || (member.teamHead==true && member.teamAccessType=='Management')}">
									<tr>
										<td valign="top" colspan="3">
									    	<fieldset class="main_side_cont text-align">
												<legend><span class="legend_label"><digi:trn>Options</digi:trn></span></legend>
												<div id="optionsDiv" class="inputx">
													<feature:display  name="Public View Checkbox" module="Report and Tab Options">
														<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
															<nested:checkbox property="publicReport" styleId="publicReportChkBox"/>
															<digi:trn key="aim:makePublic">Make public</digi:trn> <br />
															<c:set var="workspaceLinkedWarningTitle">
																<digi:trn>WARNING! leaving unchecked means that this report will render ALL activities from ALL management workspaces in the database. Putting the checkbox ON will only render the activities from the creating workspace </digi:trn>
															</c:set>
															<div style="margin-left: 30px; display: none;" id="workspaceLinkedHolder" title="${workspaceLinkedWarningTitle}">
																<nested:checkbox property="workspaceLinked" /><digi:trn>Link public view with creating workspace</digi:trn>
															</div>															
				                                    	</c:if>
			                                    	</feature:display>
													<feature:display  name="Also show pledges checkbox" module="Report and Tab Options">
														<c:if test="${(!myForm.desktopTab) and (param.type==null || param.type==donor_type_const)}">
															<div>
																<c:set var="alsoShowPledgesExplanation">
																	<digi:trn>Checking this box will lead to pledges being included in this report, with their commitment gap being displayed as Actual Commitments</digi:trn>
																</c:set>
																<nested:checkbox property="alsoShowPledges" title="${alsoShowPledgesExplanation}" onclick="javascript:if (this.checked) {selectMeasure('Actual Commitments');}; repManager.checkSteps();" />
																<digi:trn key="aim:alsoShowPledges">Also show pledges</digi:trn> <br />
															</div>
														</c:if>
			                                    	</feature:display>
			                                    	<c:if test="${!myForm.desktopTab}">
														<div>
															<nested:checkbox property="allowEmptyFundingColumns"/>
															<digi:trn key="rep:wizard:allowEmptyFundingCols">Allow empty funding columns for year, quarter and month</digi:trn>
			                                    		<br />
														</div>
			                                    	</c:if>
													<div id="splitByFundingDiv">
														<nested:checkbox property="splitByFunding"/>
														<digi:trn>Split by funding</digi:trn>
													</div>
													<div id="showOriginalCurrencyDiv" >
													    <c:set var="showOriginalCurrencyExplanation">
                                                            <digi:trn>This feature will show each transaction in the currency originally reported in the AMP.</digi:trn>
                                                        </c:set>
                                                        <nested:checkbox property="showOriginalCurrency" title="${showOriginalCurrencyExplanation}" onchange="createPreview();"/>
                                                        <digi:trn>Show Original reporting currencies</digi:trn>
                                                    </div>
			                                    </div>
											</fieldset>
										</td>
				  					</tr>
								</c:if>
							</table>
						<c:if test="${myForm.onePager }">
						</fieldset>
						</c:if>
							<br />
						</div>
	</nested:root>