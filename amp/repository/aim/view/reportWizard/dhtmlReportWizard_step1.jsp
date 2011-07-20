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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
   
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

						<div class="main_side_cont yui-tab-content" id="type_step_div">
							<c:set var="stepNum" value="0" scope="request" />
							<div id="toolbarMarkerDiv"></div>
							<jsp:include page="toolbar.jsp" />
							<br />
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="365" valign="top">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn>Funding Grouping</digi:trn></span></legend>
											<div id="reportGroupDiv" class="inputx">
										<c:set var="pledges_type_const"><%=ArConstants.PLEDGES_TYPE%></c:set>
										<c:choose>
										<c:when test="${param.type==null || param.type!=pledges_type_const}">								
													<feature:display name="Donor Report" module="Report Types">                                      	
				                                    	<html:radio property="reportType" disabled="${disableFundingType}" value="donor"  onclick="repManager.checkSteps()">
				                                           	${donorFunding}
														</html:radio>
														<br />
													</feature:display>
				                                    <feature:display name="Regional Report" module="Report Types">										
				                                    	<html:radio property="reportType" disabled="${disableFundingType}" value="regional"  onclick="repManager.checkSteps()">
				                                        	${regionalFunding}
				                                      	</html:radio>
				                                        <br />
													</feature:display>
				                                    <feature:display name="Component Report" module="Report Types">
				                                       	<html:radio property="reportType" disabled="${disableFundingType}" value="component"  onclick="repManager.checkSteps()">
					                                		${componentFunding}
				                                        </html:radio>
				                                        <br />
				                                   	</feature:display>
				                                    <feature:display name="Contribution Report" module="Report Types">
														<html:radio property="reportType" disabled="${disableFundingType}" value="contribution"  onclick="repManager.checkSteps()">
				                                        	${activityContributions}
				                                      	</html:radio>
				                                        <br />   	
				                                   	</feature:display>
												</c:when>
                           						<c:otherwise>
                                              <c:set var="disablePledgeType">true</c:set>
                                              	<tr>
                                              		<td>
                                                		<html:radio property="reportType" disabled="${disablePledgeType}" value="pledge" onclick="repManager.checkSteps()">
                                                   			${PledgesFunding}
                                                     	</html:radio>
                                                   </td>
                                                </tr>
                                           </c:otherwise>
                                         </c:choose>
                                         </div>
										</fieldset>
									</td>
									<td width="16">&nbsp;</td>
									<td width="365" valign="top">
										<fieldset class="main_side_cont">
											<html:hidden property="reportDescription" />
											<legend><span class="legend_label">${descriptionName}</span></legend>
											<textarea name="reportDescriptionClone"  rows="5" class="inputx" style="width:340px; height:90px;"></textarea>
										</fieldset>
									</td>
								</tr>
								<tr>
									<c:if test="${!myForm.desktopTab}">
										<td valign="top">
									    	<fieldset class="main_side_cont">
												<legend><span class="legend_label"><digi:trn>Totals Grouping</digi:trn></span></legend>
												<div id="totalsGroupingDiv" class="inputx">
													<html:checkbox property="hideActivities" value="true" onchange="createPreview();">
														${summary}
													</html:checkbox>
													<br />
													<html:radio property="reportPeriod" value="A" onchange="createPreview();">
														<digi:trn key="aim:AnnualReport">Annual Report</digi:trn>
													</html:radio>
													<br />
													<c:if test="${param.type==null || param.type!=pledges_type_const}">
													<html:radio property="reportPeriod" value="Q" onchange="createPreview();">
														<digi:trn key="aim:QuarterlyReport">Quarterly Report</digi:trn>
													</html:radio>
													<br />
													<html:radio property="reportPeriod" value="M" onchange="createPreview();">
														<digi:trn key="aim:MonthlyReport">Monthly Report</digi:trn>
													</html:radio>
													</c:if>
													<br />
													<html:radio property="reportPeriod" value="N" onchange="createPreview();">
														<digi:trn>Totals Only</digi:trn>
													</html:radio>
												</div>											
											</fieldset>									
										</td>
										<td valign="top">&nbsp;</td>
									</c:if>
									<td valign="top">
										<c:set var="spanUseFiltersVisibility">visibility: hidden</c:set>
										<c:set var="spanUseFiltersHeight">height:90px</c:set>
										<c:set var="spanUseFiltersDisabled">disabled</c:set>
										<c:if test="${aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters}">
											<c:set var="spanUseFiltersVisibility">visibility: visible</c:set>
											<c:set var="spanUseFiltersDisabled">enabled</c:set>
										</c:if>
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:subtitle:selectedFilters">Selected Filters</digi:trn></span></legend>
											<div id="listFiltersDiv" style="height:85px; overflow-y:auto; overflow-x:hidden; margin-bottom: 5px;" class="inputx">
												<c:choose>
													<c:when test="${aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters}">
														<jsp:include page="showSelectedFilters.jsp" />				
													</c:when>
												</c:choose>
											</div>
											<span id="spanUseFilters">
												<html:checkbox property="useFilters" styleId="useFiltersCheckbox" onclick="repManager.decideStrikeFilters()" disabled="${spanUseFiltersDisabled}"/> 
												<digi:trn key="rep:wizard:useAboveFilters">Use above filters</digi:trn>
											</span>
										</fieldset>
									</td>
								</tr>
					  			<c:if test="${ (!myForm.desktopTab) || (member.teamHead==true && member.teamAccessType=='Management')}">
									<tr>
										<td valign="top" colspan="3">
									    	<fieldset class="main_side_cont">
												<legend><span class="legend_label"><digi:trn>Options</digi:trn></span></legend>
												<div id="optionsDiv" class="inputx">
													<feature:display  name="Public View Checkbox" module="Report and Tab Options">
														<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
															<html:checkbox property="publicReport"/><digi:trn key="aim:makePublic">Make public</digi:trn>
															<br />
				                                    	</c:if>
			                                    	</feature:display>
			                                    	<c:if test="${!myForm.desktopTab}">
			                                    		<html:checkbox property="allowEmptyFundingColumns"/>
					                                    <digi:trn key="rep:wizard:allowEmptyFundingCols">Allow empty funding columns for year, quarter and month</digi:trn>
			                                    		<br />
			                                    	</c:if>
			                                    </div>
											</fieldset>
										</td>
				  					</tr>
								</c:if>
							</table>
							<br />
						</div>