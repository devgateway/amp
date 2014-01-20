<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.helper.*" %>
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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript">
	function load() {
		window.print();
	}
	function unload() {}
</script>

<%
	Long actId = (Long)request.getAttribute("actId");
	//Long actId = (Long)request.getAttribute("ampActivityId");


	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=6";

	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(
						 actId,session,new PrintWriter(out),370,450,url,true,request);

	String actPerfChartUrl = null;
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}


	String actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(
						 actId,session,new PrintWriter(out),370,350,url);

	String actRiskChartUrl = null;

	if (actRiskChartFileName != null)  {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}
%>
<link href="/TEMPLATE/ampTemplate/css_2/amp.css" rel="stylesheet" type="text/css"></link>
<style>
html {background:none;}
body {background:none;}
.prnt_tbl td {border-collapse:collapse; border-color:#CCCCCC;}
</style>
<digi:instance property="aimEditActivityForm" />
<c:if test="${aimEditActivityForm!=null}">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="650" vAlign="top" align="left" border="0">
	<tr>
		<td align=left valign="top">
			<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" border="0">
          <tr>
            <td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
                <tr>
                  <td width="100%" vAlign="top">
						<table width="100%" cellspacing="1" cellPadding="2" vAlign="top" align="left" border="0">

                      <tr>
                        <td width="100%">
								<table width="98%" cellspacing="1" cellPadding="2">
								<field:display feature="Identification" name="Project Title">
									<tr>
										<td class="head2-name" width="100%" align="center" bgcolor="#ffffff">
                                			<c:if test="${aimEditActivityForm.identification.title!=null}">${aimEditActivityForm.identification.title}</c:if>
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
								<table width="98%" cellspacing="0" class="prnt_tbl" cellpadding=4 style="border-collapse: collapse; border-color:#CCCCCC;" border="1">
									<tr bgcolor="#f4f4f2">
										<td  align="center" colspan="2" bgcolor=#C7D4DB>
											<b><digi:trn key="aim:activityDetail">Activity Details</digi:trn></b>										
										</td>
									</tr>
								<feature:display name="Identification" module="Project ID and Planning">   
									<field:display name="AMP ID" feature="Identification">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
												<digi:trn key="aim:ampId">AMP ID</digi:trn>		
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.ampId!=null}">
													${aimEditActivityForm.identification.ampId}
											</c:if>
										</td>
									</tr>
                                    </field:display>
                                    </feature:display>
                                 <feature:display name="Identification" module="Project ID and Planning">
										
										<field:display name="Status" feature="Identification">							
                                            <tr>
                                            	  <td align="right" valign="top" nowrap="nowrap" >
                                                  	<b>
                                                  	<digi:trn key="aim:status"> Status</digi:trn>
                                                  	</b>                      	      
                                                  	</td>
													<td bgcolor="#FFFFFF">
												   	  <category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}"/><br><br>
												   	</td>
											</tr>
											<c:if test="${not empty aimEditActivityForm.identification.statusReason}">
	                                            <tr>
	                                            	  <td align="right" valign="top" nowrap="nowrap" >
	                                                  	<b>
	                                                  	<digi:trn>Status Reason</digi:trn>
	                                                  	</b>                      	      
	                                                  	</td>
														<td bgcolor="#FFFFFF">${aimEditActivityForm.identification.statusReason}
													   	</td>
												</tr>
											</c:if>                                                       			
										</field:display>									
                                        </feature:display>   								
								<feature:display name="Identification" module="Project ID and Planning">

									<field:display feature="Identification" name="Project Comments">
                                    	
                                    <tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:projectcomments">
										    Project Comments</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
	                                          <c:if test="${aimEditActivityForm.identification.projectComments!=null}">
												<c:set var="objKey" value="${aimEditActivityForm.identification.projectComments}" />
												<digi:edit key="${objKey}"></digi:edit>
	                                         </c:if>										
                                         </td>
									</tr>    
									</field:display>
									
									<field:display feature="Identification" name="Objective">
                                    	
                                    <tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:objectives">
										    Objectives</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.identification.objectives!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.identification.objectives}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										
                                         </td>
									</tr> 
									   	<field:display name="Objective Comments" feature="Identification">
											<logic:present name="currentMember" scope="session">
											<tr>
												<td width="27%" align="right" valign="top" nowrap="nowrap" >
													<b><digi:trn key="aim:objectiveComments">Objective Comments</digi:trn></b>	
												</td>
												<td bgcolor="#ffffff">
												 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
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
												</logic:iterate>										
												</td>
											</tr>
											</logic:present>
										</field:display>
									</field:display>
									 </feature:display>
                                       <feature:display name="Identification" module="Project ID and Planning">   
									<field:display name="Contract Number" feature="Planning">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
												<digi:trn key="aim:convenionumcont">Contract Number</digi:trn>		
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        	<c:out value="${aimEditActivityForm.identification.convenioNumcont}"/>										
                                        </td>
									</tr>
                                    </field:display>                                    
                                                                      
									<field:display feature="Identification" name="Description">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:description">
										    Description</digi:trn>		
									  </b>								</td>
									  <td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.description!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.description}" />
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
                                          <c:if test="${aimEditActivityForm.identification.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.identification.purpose}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<module:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:purposeComments">
										    Purpose Comments</digi:trn>		
									  </b>								</td>
				       <td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
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
									
									</module:display>
									</field:display>

									
									
									<field:display name="Project Impact" feature="Identification">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
												<digi:trn key="aim:Project Impact">Project Impact</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.projectImpact!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.projectImpact}" />
											<digi:edit key="${descKey}"></digi:edit>
                                        </c:if>										
                                        </td>
									</tr>
									</field:display>
									<module:display name="/Activity Form/Identification/Activity Summary" parentModule="/Activity Form/Identification">
										<field:display name="Activity Summary" feature="Identification">
										<tr>
											<td width="27%" align="right" valign="top" nowrap="nowrap" >
												<b>
													<digi:trn>Activity Summary:</digi:trn>
										  		</b>								
										  	</td>
										  	<td bgcolor="#ffffff">
	                                        <c:if test="${aimEditActivityForm.identification.activitySummary!=null}">
												<c:set var="descKey" value="${aimEditActivityForm.identification.activitySummary}" />
												<digi:edit key="${descKey}"></digi:edit>
	                                        </c:if>										
	                                        </td>
										</tr>
										</field:display> 
									</module:display>
									<field:display name="Contracting Arrangements" feature="Identification">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
												<digi:trn>Contracting Arrangements</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.contractingArrangements!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.contractingArrangements}" />
											<digi:edit key="${descKey}"></digi:edit>
                                        </c:if>										
                                        </td>
									</tr>
									</field:display>

									<field:display name="Conditionalities" feature="Identification">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
												<digi:trn>Conditionalities</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.conditionality!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.conditionality}" />
											<digi:edit key="${descKey}"></digi:edit>
                                        </c:if>										
                                        </td>
									</tr>
									</field:display> 
									
									<field:display feature="Identification" name="Results">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:results">
										    Results</digi:trn>	
									  </b>									</td>
		    <td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.identification.results!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.identification.results}" />
											<digi:edit key="${objKey}"></digi:edit>
                                         </c:if>										</td>
									</tr>
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:resultsComments">
										    Results Comments</digi:trn>		
									  </b>								</td>
				       <td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
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
											<c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									
									<field:display name="Project Implementing Unit" feature="Identification">
										<tr>
											<td width="30%" align="right" valign="top" nowrap="nowrap">
												<b><digi:trn>Project Implementing Unit</digi:trn></b>
											</td>
											<td bgcolor="#ffffff">
												<c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">
													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/>
												</c:if>&nbsp;
											</td>
										</tr>
									</field:display>
									
									<field:display name="A.C. Chapter" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" ><b>
									  <digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn>	</b>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="Cris Number" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" ><b>
									  <digi:trn key="aim:crisNumber">Cris Number</digi:trn>	</b>										</td>
										<td bgcolor="#ffffff">
										<c:out value="${aimEditActivityForm.identification.crisNumber}"/>
&nbsp;										</td>
									</tr>
									</field:display>
									<feature:display name="Budget" module="Project ID and Planning">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
										<b>
											<digi:trn key="aim:actBudget">Budget</digi:trn>
										</b>										
										</td>
										<td bgcolor="#ffffff">
										<field:display name="On/Off/Treasury Budget" feature="Budget">
											<c:choose>
												<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
												<digi:trn>Activity is On Budget</digi:trn>
												</c:when>
												<c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOff}">
												<digi:trn>Activity is Off Budget</digi:trn>
												</c:when>
												<c:when test="${aimEditActivityForm.identification.budgetCV==0}">
												<digi:trn>Budget Unallocated</digi:trn>
												</c:when>
												<c:otherwise>
													<digi:trn>Activity is On</digi:trn>  
													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.budgetCV}" />
												</c:otherwise>
											</c:choose>	
											<br>
										</field:display>
										
										<module:display name="/Activity Form/Identification/Budget Extras" parentModule="/Activity Form/Identification">
										<c:if test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">										
											<module:display name="/Activity Form/Identification/Budget Extras/FY" parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>FY</digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.FY"/></b>
												<br />
											</module:display>
											<module:display name="/Activity Form/Identification/Budget Extras/Ministry Code"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Ministry Code</digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.ministryCode"/></b>
												<br />
											</module:display>
											<module:display name="/Activity Form/Identification/Budget Extras/Project Code"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Project Code</digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.projectCode"/></b>
												<br />
											</module:display>
											<module:display name="/Activity Form/Identification/Budget Extras/Sub-Program"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Sub-Program</digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.subProgram"/></b>
												<br />
											</module:display>
											<module:display name="/Activity Form/Identification/Budget Extras/Sub-Vote"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Sub-Vote </digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.subVote"/></b>
												<br />
											</module:display>
											<module:display name="/Activity Form/Identification/Budget Extras/Vote"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Vote</digi:trn>:&nbsp;
												<b><bean:write name="aimEditActivityForm" property="identification.vote"/></b>
												<br />
											</module:display>
										</c:if>
									</module:display>
										<c:if test="${aimEditActivityForm.identification.budgetCV == aimEditActivityForm.identification.budgetCVOn}">
										<p/>
										<field:display name="Project Code" feature="Budget">
											<digi:trn key="aim:actProjectCode">Project Code</digi:trn>: <bean:write name="aimEditActivityForm" property="identification.projectCode"/> 
										</field:display>										
										</c:if>				
										</td>
									</tr>
									</feature:display>
 
									<field:display name="Project Category" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b>
									  <digi:trn key="aim:ProjectCategory">Project Category</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
		</feature:display>
									<field:display name="Government Agreement Number" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b>
									  <digi:trn key="aim:step1:GovernmentAgreementNumTitle">Government Agreement Number</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/>&nbsp;
										</td>
									</tr>
									</field:display>
									

                                     

										<field:display name="Humanitarian Aid" feature="Identification">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap" ><b>
													<digi:trn key="aim:humanitarianaid">
													 Humanitarian Aid</digi:trn></b></td>
												<td bgcolor="#ffffff">
													<c:if test="${!aimEditActivityForm.identification.humanitarianAid==true}">
														<digi:trn key="aim:no">No</digi:trn>
													</c:if>
													<c:if test="${aimEditActivityForm.identification.humanitarianAid==true}">
														<digi:trn key="aim:yes">Yes</digi:trn>
													</c:if>
	&nbsp;										</td>
											</tr>
										</field:display>
									<field:display name="Procurement System" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b><digi:trn key="aim:ProcurementSystem">Procurement System</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="Reporting System" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b><digi:trn key="aim:ReportingSystem">Reporting System</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="Audit System" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b><digi:trn key="aim:AuditSystem">Audit System</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									<field:display name="Institutions" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap"><b><digi:trn key="aim:Institutions">Institutions</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.institutions > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/>
											</c:if>
&nbsp;										</td>
									</tr>
									</field:display>
									
									
								
									<field:display feature="Identification" name="Organizations and Project ID">
											<tr>
												<td align="right" valign="top" nowrap="nowrap">
													<b>
													<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn>
												  </b>												</td>
										  <td width="73%" bgcolor="#ffffff">
<c:if test="${!empty aimEditActivityForm.identification.selectedOrganizations}">
														<table cellSpacing="2" cellPadding="2" border="0">
															<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.identification.selectedOrganizations}" >
																<c:if test="${not empty selectedOrganizations}">
																	<tr><td>
																		<c:out value="${selectedOrganizations.organisation.name}"/>:
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
												<table width="100%" cellSpacing="2" cellPadding="1">
												<field:display feature="Planning" name="Line Ministry Rank">
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planning.lineMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
													${aimEditActivityForm.planning.lineMinRank}													</c:if>													</td>
												</tr>
												</field:display>
												<field:display name="Ministry of Planning Rank" feature="Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:planMinRank">
													Ministry of Planning Rank</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${aimEditActivityForm.planning.planMinRank == -1}">													</c:if>
													<c:if test="${aimEditActivityForm.planning.planMinRank != -1}">
													${aimEditActivityForm.planning.planMinRank}													</c:if>													</td>
												</tr>
												</field:display>
												
												<module:display name="/Activity Form/Planning/Proposed Approval Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
														<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
													</td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.originalAppDate}													</td>
												</tr>
												</module:display>
												
												<module:display name="/Activity Form/Planning/Actual Approval Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:actualapprovaldate">Actual Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.revisedAppDate}													</td>
												</tr>
												</module:display>												

												<module:display name="/Activity Form/Planning/Proposed Start Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
														<digi:trn>Proposed Start Date</digi:trn>
													</td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.originalStartDate}													</td>
												</tr>
												</module:display>

												<module:display name="/Activity Form/Planning/Actual Start Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn>Actual Start Date</digi:trn></td>													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.revisedStartDate}													</td>
												</tr>
												</module:display>

												<module:display name="/Activity Form/Planning/Original Completion Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn>Original Completion Date</digi:trn></td>													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.originalCompDate}													</td>
												</tr>
												</module:display>

												<module:display name="/Activity Form/Planning/Proposed Completion Date" parentModule="/Activity Form/Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.proposedCompDate}													</td>
												</tr>
												</c:if>
												</module:display>

												<module:display name="/Activity Form/Planning/Actual Completion Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
													<digi:trn>Actual Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.planning.currentCompDate}"/>													</td>
												</tr>
												</module:display>
																																				
												<module:display name="/Activity Form/Planning/Final Date for Contracting" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">													
													<digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.planning.contractingDate}"/>													</td>
												</tr>
												</module:display>
														
												<module:display name="/Activity Form/Planning/Final Date for Disbursements" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.planning.disbursementsDate}"/>													</td>
												</tr>
												</module:display>

												<c:if test="${aimEditActivityForm.editAct}">
												<c:if test="${!empty aimEditActivityForm.planning.activityCloseDates}">
												<tr>
													<td width="32%" valign="top"><digi:trn key="aim:proposedCompletionDates">
													Proposed Completion Dates</digi:trn></td>
													<td width="1" valign="top">:</td>
													<td align="left" valign="top">
														<table cellpadding="0" cellspacing="0">
															<c:forEach var="closeDate" items="${aimEditActivityForm.planning.activityCloseDates}">
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
												
												<field:display name="Duration of Project" feature="Planning"> 
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn>
													Duration of project</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														${aimEditActivityForm.planning.projectPeriod}													</td>
												</tr>
												</c:if>
												</field:display>
												
											</table>
											</div>										</td>
									</tr>
									</feature:display>
                                        <module:display name="References" parentModule="PROJECT MANAGEMENT">
									<tr>
									<td width="27%" align="right" valign="top" nowrap="nowrap">
									<b>
									<digi:trn key="aim:printPreview:references"> References</digi:trn>
									</b>
									</td>
									<td bgcolor="#ffffff">
									<c:forEach items="${aimEditActivityForm.documents.referenceDocs}" var="refDoc" varStatus="loopstatus">
										<table border="0">
											<tr>
												<td>
													<c:if test="${!empty refDoc.comment}">
													${refDoc.categoryValue}													</c:if>												</td>
											</tr>
										</table>
									</c:forEach>									</td>
									</tr>
									</module:display>
								
                               
                                 <feature:display name="Location" module="Project ID and Planning">
                               		<field:display name="Implementation Location" feature="Location">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:location">
										    Location</digi:trn>
									  </b>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.location.selectedLocs}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
													<tr>
													<td>
														<c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
	                                                                    	[${ancestorLoc}] 
	                                                    </c:forEach>
													<!--  
													<c:if test="${!empty locations.country}">
														[<c:out value="${locations.country}"/>]													
													</c:if>
													<c:if test="${!empty locations.region}">
														[<c:out value="${locations.region}"/>]													
														</c:if>
													<c:if test="${!empty locations.zone}">
														[<c:out value="${locations.zone}"/>]													
													</c:if>
													<c:if test="${!empty locations.woreda}">
														[<c:out value="${locations.woreda}"/>]													
													</c:if>
													-->													
													</td>
													<td align="right">
														<!-- <c:out value="${locations.percent}"/>% -->
														<field:display name="Regional Percentage" feature="Location">
														<c:if test="${selectedLocs.showPercent}">
																	<c:out value="${selectedLocs.percent}"/>%
														</c:if>												
														</field:display>
													</td>
													</tr>
												</c:forEach>
												<tr>
													<td colspan="2">
														<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
                            	<bean:define id="selLocIds">
                              	<c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}"><bean:write name="selectedLocs" property="locId" />|</c:forEach>
                            	</bean:define>
                          	</logic:notEmpty>
														<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
															<img border="0" src="/gis/getActivityMap.do?action=paintMap&width=500&height=500&mapLevel=2&mapCode=TZA&selRegIDs=<bean:write name="selLocIds"/>">
														</logic:notEmpty>
													</td>
												</tr>
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
										    Implementation Level</digi:trn>
										  </b>
										</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.location.levelId>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/>
											</c:if>										</td>
									</tr>
									</field:display>
									
								  <field:display name="Implementation Location" feature="Location">	  
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:implementationLocation">
										    Implementation Location</digi:trn>
										  </b>
										</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.location.implemLocationLevel>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/>
											</c:if>										</td>
									</tr>
									</field:display>
									
                            </feature:display>   
							<module:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
								<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b><digi:trn>National Plan</digi:trn></b>
										</td>
										<td>
											<c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
												<c:set var="program" value="${nationalPlanObjectivePrograms.program}"/>
													<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;" border="0">
														<tr>
															<td width=85%><b>${nationalPlanObjectivePrograms.hierarchyNames}</b></td>
															<td width=15% align=right valign=top><b>${nationalPlanObjectivePrograms.programPercentage}%</b></td>
														</tr>
													</table>
											</c:forEach>
										</td>
									</tr>
								</c:if>
							</module:display>                            
                            <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
								
                                	<feature:display name="NPD Programs" module="National Planning Dashboard">
									<field:display name="National Planning Objectives" feature="NPD Programs">
									<TR>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
												<b>
								      <digi:trn key="aim:national Plan Objective"> National Plan Objective</digi:trn>
												</b></TD>

<TD bgcolor="#ffffff">
											<c:forEach var="program" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
                                                 <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                             </c:forEach>
                                      </TD>
									</TR>
                                      </field:display> 
                                     <field:display name="Primary Program" feature="NPD Programs">
                                     <c:if test="${aimEditActivityForm.programs.primaryPrograms != null} }">
                                           <TR>
												<td width="27%" align="right" valign="top" nowrap="nowrap">
																												<b>
										     <digi:trn key="aim:primary Programs"> Primary Programs</digi:trn>
																												</b></TD>

						  <TD bgcolor="#ffffff">
								<c:forEach var="program" items="${aimEditActivityForm.programs.primaryPrograms}">
                                	<c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                </c:forEach>
                   		</TD>
						</TR>
						</c:if>
										</field:display>
										<field:display name="Secondary Program" feature="NPD Programs">
											<c:if test="${aimEditActivityForm.programs.secondaryPrograms != null} }">
                                         	<TR>
												<td width="27%" align="right" valign="top" nowrap="nowrap" >
													<b>	
											  <digi:trn key="aim:secondary Programs"> Secondary Programs</digi:trn>
													</b></TD>
<TD bgcolor="#ffffff">
                                                      <c:forEach var="program" items="${aimEditActivityForm.programs.secondaryPrograms}">
	                                                      <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                      </c:forEach>
                                        		</TD>
											</TR>
											</c:if>
										</field:display>
									</feature:display>
                                   </module:display>
                            
							<%-- <feature:display name="Program" module="Program">
                            <field:display name="National Planning Objectives" feature="NPD Programs">                       
                          	<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
                                                  <digi:trn key="national Plan Objective">National Plan Objective</digi:trn>
                        					</b>
                        				</td>
							<td bgcolor="#ffffff">
							<table width="100%" cellSpacing="2" cellPadding="1">
                                                        <c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
                                                          <c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
                                                          <c:set var="program" value="${nationalPlanObjectivePrograms.program}"/>
                                                          <tr><td>
                                                                                 ${nationalPlanObjectivePrograms.hierarchyNames}
                                                          <td align="right">
                                                                    ${nationalPlanObjectivePrograms.programPercentage}%
                                                          </td></tr>                                                          
                                                          </c:forEach>

                                                        </c:if>
                            </table>
							</td>
							</tr>
						</field:display>
                            </feature:display> --%>   
                                    <feature:display name="Sectors" module="Project ID and Planning">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" class="t-name">
											<b>
									  			<digi:trn key="aim:sector">Sector</digi:trn>
									  		</b>
										</td>
										<td bgcolor="#ffffff">
			                            <c:forEach var="config" items="${aimEditActivityForm.sectors.classificationConfigs}" varStatus="ind">
			                               <field:display name="${config.name} Sector" feature="Sectors">
											<c:set var="hasSectors">
												false
											</c:set>

											<c:forEach var="actSect" items="${aimEditActivityForm.sectors.activitySectors}">
												<c:if test="${actSect.configId==config.id}">
													<c:set var="hasSectors">
														true
													</c:set>
												</c:if>
											</c:forEach>
											<c:if test="${hasSectors}">
			                                <strong>
				                               	<digi:trn key="aim:addactivitysectors:${config.name} Sector">
				                                <c:out value="${config.name} Sector"/>
				                                </digi:trn>
				                                </strong><br/>
			                                </c:if>
	                                        <c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<c:forEach var="sectors" items="${aimEditActivityForm.sectors.activitySectors}">
                                                 	<c:if test="${sectors.configId==config.id}">
														<tr><td>
														<c:if test="${!empty sectors.sectorName}">
															<c:out value="${sectors.sectorName}" />
														</c:if>&nbsp;&nbsp;
														<field:display name="${config.name} Sector Sub-Sector" feature="Sectors"> 
														<c:if test="${!empty sectors.subsectorLevel1Name}">
															[<c:out value="${sectors.subsectorLevel1Name}"/>]
														</c:if>
														</field:display>
														<field:display name="${config.name} Sector Sub-Sub-Sector" feature="Sectors">
														<c:if test="${!empty sectors.subsectorLevel2Name}">
															[<c:out value="${sectors.subsectorLevel2Name}"/>]
														</c:if>
														</field:display>
														<c:if test="${sector.sectorPercentage!=''}">
															<c:if test="${sector.sectorPercentage!='0'}">
																(<c:out value="${sectors.sectorPercentage}" />)%
															</c:if>
														</c:if>
														</td></tr>
													</c:if>
												</c:forEach>
												</table>
											</c:if>
		                                    </field:display>
										</c:forEach>
										</td>
									</tr>
									</feature:display>
                                          
                                  <c:if test="${not empty aimEditActivityForm.components.activityComponentes}">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
									  	<b><digi:trn key="aim:preview:component_Sector">Components</digi:trn></b>
									  	</td>
									  <td bgcolor="#ffffff">
												<table>
													<c:forEach var="compo" items="${aimEditActivityForm.components.activityComponentes}">
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
									
                                    
                                  
                                   
                                  <logic:present name="currentMember" scope="session">
									<module:display name="Funding" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
										  <digi:trn key="aim:funding"> Funding</digi:trn>
</b>
									  </td>
										<td bgcolor="#ffffff">
										    <table width="100%" cellspacing="1" cellpadding="0" border="0" align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing=8 cellpadding="0" border="0" width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">

                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellspacing="1" cellpadding="0" border="0" width="100%" class="box-border-nopadding">
                                                                  <tr>
                                                                    <td>
                                                                      <table cellspacing="1" cellpadding="0" border="0" width="100%">
                                                                        <tr>
                                                                          <td>
                                                                            <table width="100%" border="0" cellpadding="1" bgcolor="#dddddd" cellspacing="1">
                                                                            <field:display name="Funding Organization Id" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">																																
                                                                                  <digi:trn key="aim:fundingOrgId">
                                                                                    Funding Organization Id</digi:trn></a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td width="454" align="left">
                                                                                <bean:write name="funding"	property="orgFundingId"/>                                                                                </td>
                                                                              </tr>
                                                                             </field:display>
                                                                             <field:display name="Funding Organization Name" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">

                                                                                  <a title="<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>">
                                                                               	  <digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
                                                                                  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                  ${fundingOrganization.orgName}                                                                                </td>
                                                                              </tr>
                                                                             </field:display>
																		<logic:present name="funding" property="sourceRole">
																			<tr>
																				<td align="left" width="150">
																					<a title='<digi:trn key="aim:orgRole">Organization Role</digi:trn>'>
																						<digi:trn key="aim:OrgRole">Organization Role</digi:trn>
																					</a>
																				</td>
																				<td width="1">:</td>
																				<td align="left">
																					<b><digi:trn><bean:write name="funding" property="sourceRole"/></digi:trn></b>
																				</td>
																			</tr>
																		</logic:present>
                                                                              <!-- type of assistance -->
                                                                              <field:display name="Type Of Assistance" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																				  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                  <logic:notEmpty name="funding" property="typeOfAssistance">
                                                                                    <digi:trn>
                                                                                    	<bean:write name="funding" property="typeOfAssistance.value"/>
                                                                                    </digi:trn>
                                                                                  </logic:notEmpty>                                                                                </td>
                                                                              </tr>
																			</field:display>
																			<field:display name="Financing Instrument" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                               	  <digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																				  </a></td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                  <logic:notEmpty name="funding" property="financingInstrument">
                                                                                    <digi:trn>
                                                                                    	<bean:write name="funding"	property="financingInstrument.value"/>
                                                                                    </digi:trn>
                                                                                  </logic:notEmpty>                                                                                </td>
                                                                              </tr>
                                                                           <field:display name="Credit/Donation" feature="Planning">
                                                                           <tr>
                                                                                <td align="left" width="339">
                                                                                  <a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                                 	 <digi:trn key="aim:credit_donation">Credit/Donation</digi:trn>
																				  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                    <c:if test="${not empty funding.financingInstrument}">
                                                                                        <c:set var="creditTypeId">
                                                                                            <bean:write name="funding"	property="financingInstrument.value"/>
                                                                                        </c:set>
                                                                                        <c:if test="${creditTypeId == 'Comercial' || creditTypeId == 'Concesional'}">
                                                                                            <digi:trn key="aim:preview_credit">Credito</digi:trn>	                                        		 
                                                                                        </c:if>
                                                                                        <c:if test="${creditTypeId == 'Donación'}">
                                                                                            <digi:trn key="aim:preview_donation">Donación</digi:trn>	                                        		
                                                                                        </c:if>
                                                                                            
                                                                                    </c:if>
                                                                                       
										                                        	                                                                               </td>
                                                                              </tr>
                                                                              </field:display>
                                                                              
																			</field:display>
																			  <field:display name="Funding Status" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a>
                                                                                  	<digi:trn >Funding Status </digi:trn>
																				  </a>                                                                                
																				 </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                  <logic:notEmpty name="funding" property="fundingStatus">
                                                                                    <digi:trn>
                                                                                    	<bean:write name="funding" property="fundingStatus.value"/>
                                                                                    </digi:trn>
                                                                                  </logic:notEmpty>                                                                                
                                                                                  </td>
                                                                              </tr>
																			</field:display>
																			<field:display name="Mode of Payment" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a>
                                                                                  	<digi:trn>Mode of Payment</digi:trn>
																				  </a>                                                                                
																				 </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                  <logic:notEmpty name="funding" property="modeOfPayment">
                                                                                    <digi:trn>
                                                                                    	<bean:write name="funding" property="modeOfPayment.value"/>
                                                                                    </digi:trn>
                                                                                  </logic:notEmpty>                                                                                
                                                                                  </td>
                                                                              </tr>
																			</field:display>
																			<!-- here it goes Donor Objective  and Conditions ISSUE AMP-16421-->
																			<field:display name="Conditions" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">
                                                                                  <a>
                                                                                  	<digi:trn>Conditions</digi:trn>
																				  </a>                                                                                
																				 </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left">
                                                                                	<logic:notEmpty name="funding" property="modeOfPayment">
                                                                                    	<digi:trn>
	                                                                                    	<bean:write name="funding" property="conditions"/>
    	                                                                                </digi:trn>
                                                                                    </logic:notEmpty>
                                                                                  </td>
                                                                              </tr>
																			</field:display>																			
																			
																			
				                                                            </table>
                                                                           </td>
                                                                        </tr>
                                                                      </table>                                    </td>
                                                                  </tr>
                                                                  <tr>
                                                                  	<td>
                                                                    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                                                                        <bean:define id="funding" name="funding" scope="page"
                                                                            toScope="request"
                                                                            type="org.digijava.module.aim.helper.Funding"></bean:define>
                                                                        <jsp:include page="previewActivityFundingCommitments.jsp" />
                                                                        
                                                                        <feature:display module="Funding" name="Disbursement">
                                                                        	<jsp:include page="previewActivityFundingDisbursement.jsp" />
                                                                        </feature:display>
                                                                        
                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>
                                                                        
																		<jsp:include page="previewMtefProjections.jsp" />

<%--                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>

                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>
     --%>                                                                   
                                                                        <feature:display module="Funding" name="Disbursement Orders">
                                                                        	<jsp:include page="previewActivityFundingDisbursementOrders.jsp" />
                                                                        </feature:display>

                                                                        <feature:display module="Funding" name="Undisbursed Balance">
                                                                        	<jsp:include page="previewActivityFundingUndisbursedBalance.jsp" />
                                                                        </feature:display>
                                                                    </table> 
                                                                    </td>
                                                                  </tr>
                                                                  <tr>
                                                                    <td>&nbsp;</td>
                                                                  </tr>
																	<tr><td bgcolor="#ffffff">
																		<FONT color='blue'>
																			<jsp:include page="utils/amountUnitsUnformatted.jsp">
																				<jsp:param value="* " name="amount_prefix"/>
																			</jsp:include>	
																		</FONT>
																	</td></tr>
																</table>
																<br><br>
															</td></tr>
														  </logic:iterate>
														</logic:notEmpty>
													  </logic:iterate>
																<tr><td>
                                                               

                        <table cellspacing="1" cellpadding="0" border="0" bordercolor="#FF0000" width="100%">
                        <feature:display name="Planned Commitments" module="Measures">
	                        <tr>
	                            <td bgcolor="#eeeeee"
	                                style="border-top: 1px solid #000000; text-transform: uppercase;"><digi:trn
	                                key='aim:totalplannedcommittment'> TOTAL PLANNED COMMITMENTS </digi:trn>:
	                            </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
	                                style="border-top: 1px solid #000000">
	                                 <c:if test="${not empty aimEditActivityForm.funding.totalPlannedCommitments}">
		                                <bean:write
		                                name="aimEditActivityForm" property="funding.totalPlannedCommitments" /> <bean:write
		                                name="aimEditActivityForm" property="currCode" />
		                             </c:if>&nbsp;
	                        </td>
	                        </tr>
                        </feature:display>
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
                                key='aim:totalactualcommittment'> TOTAL ACTUAL COMMITMENTS </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalCommitments}">
	                                <bean:write
	                                name="aimEditActivityForm" property="funding.totalCommitments" /> <bean:write
	                                name="aimEditActivityForm" property="currCode" />
	                              </c:if>&nbsp;
	                         </td>
                        </tr>
                        <field:display name="Pipeline" feature="Commitments">
							<tr>
	                            <td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn> TOTAL PIPELINE COMMITMENTS: </digi:trn>
	                            </td>
	                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000">
	                                <c:if test="${not empty aimEditActivityForm.funding.totalPipelineCommitments}">
										<bean:write name="aimEditActivityForm" property="funding.totalPipelineCommitments" /> <bean:write name="aimEditActivityForm" property="currCode" />
									</c:if>&nbsp;
								</td>
	                        </tr>
                        </field:display>
                        <feature:display module="Funding"
                            name="Disbursement">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
                                key='aim:totalplanneddisbursement'>
                                TOTAL PLANNED DISBURSEMENT	
                                </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalPlannedDisbursements}">
		                                <bean:write
		                                name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /> <bean:write
		                                name="aimEditActivityForm" property="currCode" />
		                          </c:if>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000"><digi:trn
                                key='aim:totalActualdisbursement'>
                                                                                                        TOTAL ACTUAL DISBURSEMENT </digi:trn>:
                            </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalDisbursements}">
		                                <bean:write
		                                name="aimEditActivityForm" property="funding.totalDisbursements" /> <bean:write
		                                name="aimEditActivityForm" property="currCode" />
		                          </c:if>&nbsp;
                           </td>
                        </tr>
                        </feature:display>
                        <feature:display module="Funding" name="Expenditures">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
                                key="aim:totalActualExpenditures">
                                  TOTAL PLANNED EXPENDITURES             </digi:trn>:</td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalPlannedExpenditures}">
			                                <bean:write
			                                name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /> <bean:write
			                                name="aimEditActivityForm" property="currCode" />
			                      </c:if>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
                                key="aim:totalplannedExpenditures">
                                    TOTAL ACTUAL EXPENDITURES       	 	 </digi:trn>:</td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalExpenditures}">
		                                <bean:write
		                                name="aimEditActivityForm" property="funding.totalExpenditures" /> <bean:write
		                                name="aimEditActivityForm" property="currCode" />
		                          </c:if>&nbsp;
		                    </td>
                        </tr>
                        </feature:display>
                        <feature:display module="Funding" name="Disbursement Orders">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase;">
                        <digi:trn
                                key='aim:totalActualDisbursementOrder'>
                                    <a
                                title='<digi:trn key="aim:FundRelease"> Release of funds to,
                                or the purchase of goods or services for a recipient; by
                                extension, the amount thus spent. Disbursements record the actual
                                international transfer of financial resources, or of goods or
                                services valued at the cost to the donor</digi:trn>'>
                          TOTAL ACTUAL DISBURSMENT ORDERS </a></digi:trn>:
                        </td>
                          <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase;">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalActualDisbursementsOrders}">
	                                <bean:write
	                                name="aimEditActivityForm" property="funding.totalActualDisbursementsOrders" />	<bean:write
	                                name="aimEditActivityForm" property="currCode" />
	                             </c:if>&nbsp;
	                       </td>
                      	</tr>
                        </feature:display>
                        <feature:display module="Funding" name="Undisbursed Balance">
                      	<tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
                                key="aim:undisbursedBalance">
                                  UNDISBURSED BALANCE 	             </digi:trn>:</td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                 <c:if test="${not empty aimEditActivityForm.funding.unDisbursementsBalance}">
		                                <bean:write
		                                name="aimEditActivityForm" property="funding.unDisbursementsBalance" /> <bean:write
		                                name="aimEditActivityForm" property="currCode" />
		                         </c:if>&nbsp;
		                  </td>
                        </tr>
                        </feature:display>
                        
                        
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase">
                                <digi:trn key="aim:undisbursedBalance"> Consumption Rate</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                            	 <c:if test="${not empty aimEditActivityForm.funding.consumptionRate}">
                                	<b>${aimEditActivityForm.funding.consumptionRate}</b>
                                </c:if>
                                &nbsp;
                            </td>
                        </tr>
                         <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase">
                                <digi:trn>Delivery Rate</digi:trn>: </td>
                            <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000">
                                <c:if test="${not empty aimEditActivityForm.funding.deliveryRate}">
                                <b>${aimEditActivityForm.funding.deliveryRate}</b>
                                </c:if>
                                &nbsp;
                            </td>
                        </tr>
                        </table>
                                                                </td></tr>
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
											<c:if test="${!empty aimEditActivityForm.funding.regionalFundings}">
												<table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
												<c:forEach var="regFunds" items="${aimEditActivityForm.funding.regionalFundings}">
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
																										<c:out value="${fd.adjustmentTypeName.value}"/>	
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
																										<c:out value="${fd.adjustmentTypeName.value}"/>	
																								</digi:trn>																						</td>
																							<td align="right" width="100" bgcolor="#ffffff">
																							<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																																														</td>
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
																										<c:out value="${fd.adjustmentTypeName.value}"/>	
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
												<tr><td bgcolor="#ffffff">&nbsp;</td>
												</tr>
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
											<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
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
																				Commitments</digi:trn>																			
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${comp.commitments}">
																						<tr>
																							<field:display name="Components Actual/Planned Commitments" feature="Activity - Component Step">
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.adjustmentTypeNameTrimmed}">
																									<c:out value="${fd.adjustmentTypeName.value}"/>	
																								</digi:trn>
																							</td>
																							</field:display>
																							<field:display name="Components Amount Commitments" feature="Activity - Component Step">
																								<td align="right" width="100" bgcolor="#ffffff">
																									<FONT color="blue">*</FONT>
																									<c:out value="${fd.transactionAmount}"/>
																								</td>
																							</field:display>
																							<field:display name="Components Currency Commitments" feature="Activity - Component Step">
																								<td bgcolor="#ffffff">
																									<c:out value="${fd.currencyCode}"/>
																								</td>
																							</field:display>																						
																							<field:display name="Components Date Commitments" feature="Activity - Component Step">
																								<td bgcolor="#ffffff" width="70">
																									<c:out value="${fd.transactionDate}"/>
																								</td>
																							</field:display>																							
																						</tr>
																						<field:display name="Component Funding Organization" feature="Activity - Component Step">
																							<tr>
																								<td bgcolor="#ffffff">
																									<b><digi:trn>Organisation</digi:trn></b>
																								</td>
																								<td colspan="3">
																									<logic:notEmpty property="componentOrganisation" name="fd">
																										<c:out value="${fd.componentOrganisation.name}" />
																									</logic:notEmpty>
																								</td>
																							</tr>
																						</field:display>
																						
																						<field:display name="Description of Component Funding" feature="Activity - Component Step">
																							<tr>
																								<td width="50" bgcolor="#ffffff">
																									<b><digi:trn>Description</digi:trn></b>
																								</td>
																								<td colspan="3" bgcolor="white">
																									<c:out value="${fd.componentTransactionDescription}" />
																								</td>
																							</tr>																							
																						</field:display>
																					</c:forEach>
																				</table>																			
																			</td>
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
																							<field:display name="Components Actual/Planned Disbursements" feature="Activity - Component Step">
																							<td width="50" bgcolor="#ffffff">
																							<digi:trn key="aim:disbursements:${fd.adjustmentTypeNameTrimmed}">
																								<c:out value="${fd.adjustmentTypeName.value}"/>	
																							</digi:trn>								
																							</td>
																							</field:display>
																							<field:display name="Components Amount Disbursements" feature="Activity - Component Step">
																							<td align="right" width="100" bgcolor="#ffffff">
																								<FONT color="blue">*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Disbursements" feature="Activity - Component Step">
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Disbursements" feature="Activity - Component Step">
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
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
																							<field:display name="Components Actual/Planned Expenditures" feature="Activity - Component Step">
																							<td width="50">
																								<digi:trn key="aim:expenditures:${fd.adjustmentTypeNameTrimmed}">
																									<c:out value="${fd.adjustmentTypeName.value}"/>	
																								</digi:trn>																							</td>
																							</field:display>
																							<field:display name="Components Amount Expenditures" feature="Activity - Component Step">
																							<td align="right">
																								<FONT color=blue>*</FONT>
																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							</field:display>
																							<field:display name="Components Currency Expenditures" feature="Activity - Component Step">
																							<td>
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							</field:display>
																							<field:display name="Components Date Expenditures" feature="Activity - Component Step">
																							<td width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							</field:display>
																							
																						</tr>
																					</c:forEach>
																				</table>																			</td>
																		</tr>
																	</table>
																</td></tr>
															</c:if>
															<tr><td bgcolor="#ffffff">&nbsp;</td>
															</tr>
															<field:display name="Components Physical Progress" feature="Activity - Component Step">
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
									<module:display name="Components Resume" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:components">
										    Components</digi:trn>
										  </b>								
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
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
								  <feature:display name="Issues" module="Issues">
								  <field:display name="Issues" feature="Issues">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:issues">
										    Issues</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border="0">
												<c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
													<tr><td valign="top">
														<li class="level1"><b><c:out value="${issue.name}"/> <field:display feature="Issues" name="Issue Date"><c:out value="${issue.issueDate}"/> </field:display> </b></li>
													</td></tr>
													<field:display name="Measures Taken" feature="Issues">
														<c:if test="${!empty issue.measures}">
															<c:forEach var="measure" items="${issue.measures}">
																<tr><td>
																	<li class="level2"><i><c:out value="${measure.name}"/></i></li>
																</td></tr>
																<field:display name="Actors" feature="Issues">
																	<c:if test="${!empty measure.actors}">
																		<c:forEach var="actor" items="${measure.actors}">
																			<tr><td>
																				<li class="level3"><c:out value="${actor.name}"/></li>
																			</td></tr>
																		</c:forEach>
																	</c:if>
																</field:display>
															</c:forEach>
														</c:if>
													</field:display>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</field:display>
									</feature:display>
									</module:display>
                             <module:display name="Document" parentModule="PROJECT MANAGEMENT">       
                                   	<feature:display name="Related Documents" module="Document">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:relatedDocuments">
										    Related Documents</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">						<c:if test="${ (!empty aimEditActivityForm.documents.documents) || (!empty aimEditActivityForm.documents.crDocuments)}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents.documents"
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
																	<b><digi:trn key="aim:description">Description</digi:trn>:</b>
																	&nbsp;<bean:write name="docs" property="docDescription" />
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																	&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b><digi:trn key="aim:documentType">Document Type</digi:trn>:</b>&nbsp;
																	<bean:write name="docs" property="docType"/>
																</logic:notEmpty>															
															</td>
														</tr>
													 </table>
													</td></tr>
													</c:if>
													</logic:iterate>
													<logic:notEmpty name="aimEditActivityForm" property="documents.crDocuments">
														<tr>
														<td>
														<logic:iterate name="aimEditActivityForm" property="documents.crDocuments" id="crDoc">
															<table width="100%" class="box-border-nopadding">
															 	<tr bgcolor="#ffffff">
																	<td vAlign="center" align="left">
																		&nbsp;<b><c:out value="${crDoc.title}"/></b> -
																		&nbsp;&nbsp;&nbsp;<i><c:out value="${crDoc.name}"/></i>
																		<c:set var="translation">
																			<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
																		</c:set>																		
																		<%-- <a style="cursor: pointer; text-decoration: underline; color: blue;" id="<c:out value="${crDoc.uuid}"/>" 
																			onclick="window.location='/contentrepository/downloadFile.do?uuid=<c:out value="${crDoc.uuid}"/>'" title="${translation}">
																			<img src="/repository/contentrepository/view/images/check_out.gif" border="0">
																		</a> --%>
																		<a id="<c:out value="${crDoc.uuid}"/>" target="_blank" href="${crDoc.generalLink}" title="${translation}">
																			<img src="/repository/contentrepository/view/images/check_out.gif" border="0">
																		</a>									
																		<logic:notEmpty name="crDoc" property="description">
																			<br />&nbsp;
																			<b><digi:trn key="aim:description">Description</digi:trn>:</b>&nbsp;
																			<bean:write name="crDoc" property="description" />
																		</logic:notEmpty>
																		<logic:notEmpty name="crDoc" property="calendar">
																			<br />&nbsp;
																			<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																			&nbsp;<c:out value="${crDoc.calendar}"/>
																		</logic:notEmpty>
																	</td>
																</tr>
															 </table>
														</logic:iterate>
														</td>
														</tr>
													</logic:notEmpty>
												</table>
											</c:if>
											<c:if test="${!empty aimEditActivityForm.documents.linksList}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												<c:forEach var="docList" items="${aimEditActivityForm.documents.linksList}">
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
                                     
								<module:display name="/Activity Form/Related Organizations" parentModule="/Activity Form">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:relatedOrganizations">
										    Related Organizations</digi:trn>
									  </b>									</td>

										<td bgcolor="#ffffff">
										<module:display name="/Activity Form/Related Organizations/Donor Organization" parentModule="/Activity Form/Related Organizations">
											<b><digi:trn key="aim:donororganisation">Donor Organization</digi:trn></b>
											<br/>
											<logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
												<div id="act_donor_organisation" style="display: block;">
													<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
														<tr>
															<td>
																<logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">
																<ul>
																	<li>
																		<bean:write name="fundingOrganization" property="orgName"/> 
																	</li>
																</ul>
																</logic:iterate>
															</td>
														</tr>
													</table>	
												</div>
											</logic:notEmpty>				
										</module:display>					
										<module:display name="/Activity Form/Related Organizations/Responsible Organization" parentModule="/Activity Form/Related Organizations">
											<b><digi:trn key="aim:responsibleOrganisation">Responsible Organization</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations"	id="respOrg" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="respOrg" property="name" />
																			<c:set var="tempOrgId" scope="page">${respOrg.ampOrgId}</c:set>
																			<field:display name="Responsible Organization Additional Info"  feature="Responsible Organization">
																				<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgToInfo(${tempOrgId})" >
																				(  <c:out value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}" /> ) 
																				</logic:notEmpty>
																			</field:display>
																<field:display name="Responsible Organization Percentage"  feature="Responsible Organization">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Responsible Organization Percentage"  feature="Responsible Organization">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty> 
																		</field:display>	
																	</td>
																</tr>
															</table>
															
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty>
											<br/>
											</module:display>
										
                                           <module:display name="/Activity Form/Related Organizations/Executing Agency" parentModule="/Activity Form/Related Organizations">
											<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.executingAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.executingAgencies" id="execAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="execAgencies" property="name" />
																<c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set>
																<field:display name="Executing Agency Additional Info"  feature="Executing Agency">
																	<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgToInfo(${tempOrgId})" >
																	(  <c:out value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}" /> )
																	</logic:notEmpty> 
																</field:display>
																<field:display name="Executing Agency Percentage"  feature="Executing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Executing Agency Percentage"  feature="Executing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																</field:display>	
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty>
											<br/>
											</module:display>

											<module:display name="/Activity Form/Related Organizations/Implementing Agency" parentModule="/Activity Form/Related Organizations">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.impAgencies"
													id="impAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="impAgencies" property="name" />
																	<c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set>
																	<field:display name="Implementing Agency Additional Info"  feature="Implementing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgToInfo(${tempOrgId})" >
																		(  <c:out value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}" /> )
																		</logic:notEmpty> 
																	</field:display>
																	<field:display name="Implementing Agency Percentage"  feature="Implementing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Implementing Agency Percentage"  feature="Implementing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																	
																	</td>
																</tr>
															</table>
													
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</module:display>

											<module:display name="/Activity Form/Related Organizations/Beneficiary Agency" parentModule="/Activity Form/Related Organizations">
																			
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>
									
											<logic:notEmpty name="aimEditActivityForm" property="agencies.benAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="agencies.benAgencies"
														id="benAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="benAgency" property="name" />
																		<c:set var="tempOrgId">${benAgency.ampOrgId}</c:set>
																		<field:display name="Beneficiary Agency  Additional Info"  feature="Beneficiary Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgToInfo(${tempOrgId})" >
																			(  <c:out value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}" /> ) 
																			</logic:notEmpty>
																		</field:display>
																		<field:display name="Beneficiary Agency  Percentage"  feature="Beneficiary Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty> 
																		</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Beneficiary Agency  Percentage"  feature="Beneficiary Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty> 
																		</field:display>
																	
																	</td>
																</tr>
															</table>
														</logic:iterate>
														</td></tr>
													</table>
												</logic:notEmpty><br/>
											</module:display>

											<module:display name="/Activity Form/Related Organizations/Contracting Agency" parentModule="/Activity Form/Related Organizations">
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.conAgencies"
													id="conAgencies" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="conAgencies" property="name" />
																<c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set>
																	<field:display name="Contracting Agency Additional Info"  feature="Contracting Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgToInfo(${tempOrgId})" >
																		(  <c:out value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}" /> )
																		</logic:notEmpty> 
																	</field:display>
																	<field:display name="Contracting Agency Percentage"  feature="Contracting Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Contracting Agency Percentage"  feature="Contracting Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																	
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>
											
											</logic:notEmpty><br/>
											</module:display>


											<module:display name="/Activity Form/Related Organizations/Sector Group" parentModule="/Activity Form/Related Organizations">
											<field:display name="Sector Group" feature="Sector Group">
											<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="agencies.sectGroups"
														id="sectGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="sectGroup" property="name" />
																			<c:set var="tempOrgId">${sectGroup.ampOrgId}</c:set>
																			<field:display name="Sector Group Additional Info"  feature="Sector Group">
																				<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgToInfo(${tempOrgId})" >
																				(  <c:out value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}" /> ) 
																				</logic:notEmpty>
																			</field:display>
																	<field:display name="Sector Group Percentage"  feature="Sector Group">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty> 
																	</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Sector Group Percentage"  feature="Sector Group">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty> 
																		</field:display>																	
																	</td>
																</tr>
															</table>
														</logic:iterate>
														</td></tr>
													</table>											
											</logic:notEmpty><br/>
											</field:display>
		
        									<module:display name="/Activity Form/Related Organizations/Regional Group" parentModule="/Activity Form/Related Organizations">
											<field:display name="Regional Group" feature="Regional Group">
											<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
											<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.regGroups"
													id="regGroup" type="org.digijava.module.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>  
																			<bean:write name="regGroup" property="name" />
																<c:set var="tempOrgId">${regGroup.ampOrgId}</c:set>
																<field:display name="Regional Group Additional Info"  feature="Regional Group">
																	<logic:notEmpty property="agencies.regOrgToInfo(${tempOrgId})"  name="aimEditActivityForm">
																		(  <c:out value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}" /> )
																	</logic:notEmpty> 
																</field:display>
																<field:display name="Regional Group Percentage"  feature="Regional Group">
																	<logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
																	  <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
																	</logic:notEmpty> 
																</field:display>
																			</li></ul>
																	</td>
																	<td width="15%" align="right">
																		<field:display name="Regional Group Percentage"  feature="Regional Group">
																	<logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
																	  <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
																	</logic:notEmpty> 
																</field:display>																	
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</field:display>
                                  	</module:display>
                                           </td>
									</tr>								
									</module:display>									 
									
                                    <module:display name="Contact Information" parentModule="PROJECT MANAGEMENT">
									<feature:display name="Donor Contact Information" module="Contact Information">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Donor funding contact information</digi:trn>
												</td>
												<td>
													<c:if test="${not empty aimEditActivityForm.contactInformation.donorContacts}">
														<c:forEach var="donorContact" items="${aimEditActivityForm.contactInformation.donorContacts}">
															<div>
																<c:out value="${donorContact.contact.name}"/>
																<c:out value="${donorContact.contact.lastname}"/> -
																<c:forEach var="property" items="${donorContact.contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<c:out value="${property.value}"/> ;
																	</c:if>
																</c:forEach>			
															</div>
														</c:forEach>
													</c:if>
												</td>
											</tr>
											</feature:display>
											<feature:display name="Government Contact Information" module="Contact Information">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>MOFED contact information</digi:trn>
												</td>
												<td>
													<c:if test="${not empty aimEditActivityForm.contactInformation.mofedContacts}">
														<c:forEach var="mofedContact" items="${aimEditActivityForm.contactInformation.mofedContacts}">
															<div>
																<c:out value="${mofedContact.contact.name}"/>
																<c:out value="${mofedContact.contact.lastname}"/> -
																<c:forEach var="property" items="${mofedContact.contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<c:out value="${property.value}"/> ;
																	</c:if>
																</c:forEach>			
															</div>
														</c:forEach>
													</c:if>
												</td>
											</tr>
											</feature:display>
											<feature:display name="Project Coordinator Contact Information" module="Contact Information">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap"class="t-name">
													<digi:trn>Project Coordinator Contact Information</digi:trn>
												</td>
												<td>
													<c:if test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}">
														<c:forEach var="projCoordinatorContact" items="${aimEditActivityForm.contactInformation.projCoordinatorContacts}">
															<div>
																<c:out value="${projCoordinatorContact.contact.name}"/>
																<c:out value="${projCoordinatorContact.contact.lastname}"/> -
																<c:forEach var="property" items="${projCoordinatorContact.contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<c:out value="${property.value}"/> ;
																	</c:if>
																</c:forEach>			
															</div>
														</c:forEach>
													</c:if>
												</td>
											</tr>
											</feature:display>
											<feature:display name="Sector Ministry Contact Information" module="Contact Information">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Sector Ministry Contact Information</digi:trn>
												</td>
												<td>
													<c:if test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}">
														<c:forEach var="sectorMinistryContact" items="${aimEditActivityForm.contactInformation.sectorMinistryContacts}">
															<div>
																<c:out value="${sectorMinistryContact.contact.name}"/>
																<c:out value="${sectorMinistryContact.contact.lastname}"/> -
																<c:forEach var="property" items="${sectorMinistryContact.contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<c:out value="${property.value}"/>;
																	</c:if>
																</c:forEach>			
															</div>
														</c:forEach>
													</c:if>
												</td>
											</tr>
										</feature:display>
										<feature:display name="Implementing/Executing Agency Contact Information" module="Contact Information">
											<tr>
												<td width="30%" align="right" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>
												</td>
												<td>
													<c:if test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
														<c:forEach var="implExecAgencyContact" items="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
															<div>
																<c:out value="${implExecAgencyContact.contact.name}"/>
																<c:out value="${implExecAgencyContact.contact.lastname}"/> -
																<c:forEach var="property" items="${implExecAgencyContact.contact.properties}">
																	<c:if test="${property.name=='contact email'}">
																		<c:out value="${property.value}"/> ;
																	</c:if>
																</c:forEach>			
															</div>
														</c:forEach>
													</c:if>
												</td>
											</tr>
										</feature:display>
									</module:display>
									
							 		<field:display name="Activity Performance"  feature="Activity Dashboard">
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
									<field:display name="Project Risk" feature="Activity Dashboard">
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
								
                                    
                                    
                              <module:display name="/Activity Form/Funding/Proposed Project Cost" parentModule="/Activity Form/Funding">
									<tr>
										<td width="27%" align="right" valign="top" nowrap="nowrap">
											<b>
									  <digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn></b>
									  </td>
									<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.funding.proProjCost!=null}">
                                                  <table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      <tr bgcolor="#ffffff">
															<td>
															<digi:trn key="aim:cost">Cost</digi:trn> 
															</td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.funding.proProjCost.funAmount!=null}">
																 	<FONT color=blue>*</FONT> ${aimEditActivityForm.funding.proProjCost.funAmount}                                                          
														  </c:if>&nbsp;
														  <c:if test="${aimEditActivityForm.funding.proProjCost.currencyCode!=null}"> 
																${aimEditActivityForm.funding.proProjCost.currencyCode} </c:if>                                                        
														</td>
												      </tr>
													  <tr bgcolor="#ffffff">
														<td>
															<digi:trn key="aim:proposedcompletiondate">Proposed Completion Date</digi:trn>  
														</td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.funding.proProjCost.funDate!=null}">
                                                             ${aimEditActivityForm.funding.proProjCost.funDate}                                                          
                                                         </c:if>                                                        
                                                         </td>
                                                       </tr>
                                              		</table>
                            				</c:if>										</td>
									</tr>
								  </module:display>
								
                                
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
											<c:out value="${aimEditActivityForm.identification.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.identification.actAthLastName}"/> -
											<c:out value="${aimEditActivityForm.identification.actAthEmail}"/>										</td>
									</tr>
									</field:display>
									<field:display name="Activity Updated On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="identification.updatedDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:activityUpdatedOn">
										    Activity updated on</digi:trn>
											</b>
										</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.updatedDate}"/>
										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Updated By" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="identification.modifiedBy">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:activityUpdatedBy">
										    Activity updated by</digi:trn>
											</b>				
											</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.modifiedBy.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.identification.modifiedBy.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.identification.modifiedBy.user.email}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<field:display name="Activity Created On" feature="Identification">
									<logic:notEmpty name="aimEditActivityForm" property="identification.createdDate">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap">
											<b>
											<digi:trn key="aim:activityCreatedOn">
										    Activity created on</digi:trn>
											</b>								</td>
<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.createdDate}"/>										</td>
									</tr>
									</logic:notEmpty>
									</field:display>
									<logic:notEmpty name="aimEditActivityForm" property="identification.team">
									<field:display name="Data Team Leader" feature="Identification">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn key="aim:activityTeamLeader">
										    Data Team Leader</digi:trn>
											</b>
										</td>

										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.team.teamLead.user.firstNames}"/>
											<c:out value="${aimEditActivityForm.identification.team.teamLead.user.lastName}"/>	-
											<c:out value="${aimEditActivityForm.identification.team.teamLead.user.email}"/>											
										</td>
									</tr>
									</field:display>
									</logic:notEmpty>
					
									<logic:notEmpty name="aimEditActivityForm" property="structures">
									<field:display name="Structures Column" feature="Structures">
									<tr>
										<td width="30%" align="right" valign="top" nowrap="nowrap" >
											<b>
											<digi:trn>Structures</digi:trn>
											</b>
										</td>

										<td bgcolor="#ffffff">
											<logic:iterate id="structure" name="aimEditActivityForm"
			property="structures">
			<table style="cellspacing:1; cellPadding:3; bgcolor:#aaaaaa; width:100%;">
				<tr bgcolor="#f0f0f0">
					<td colspan="2" align="center">
						<b>
							${structure.title}
						</b>
					</td>
				</tr>
				
				<module:display
					name="/Activity Form/Structures/Structure Type"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td align="right" width="15%"><digi:trn key="trn:type">Type</digi:trn></td>
						<td align="left"><b> ${structure.type.name} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Title"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td align="right"><digi:trn key="trn:title">Title</digi:trn></td>
						<td align="left"> <b> ${structure.title} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Description"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td align="right"><digi:trn key="trn:description">Description</digi:trn></td>
						<td align="left"><b> ${structure.description} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Latitude"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td align="right"><digi:trn key="trn:latitude">Latitude</digi:trn></td>
						<td align="left"> <b> ${structure.latitude} </b></td>
					</tr>
				</module:display>
				<module:display
					name="/Activity Form/Structures/Structure Longitude"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td align="right"><digi:trn key="trn:longitude">Longitude</digi:trn></td>
						<td align="left"><b> ${structure.longitude} </b></td>
					</tr>
				</module:display>
			
	</table>
	<br />
	<hr>
	</logic:iterate>										
										</td>
									</tr>
									</field:display>
									</logic:notEmpty>
									

									<logic:iterate name="aimEditActivityForm" property="customFields" id="customField" indexId="index">
									<field:display name="${customField.FM_field}" feature="Step${customField.step.step}">												
										<tr>
											<td width="30%" align="right" valign="top" nowrap="nowrap" >
												<b>													
													<digi:trn key="aim:customfield:${customField.name}">${customField.name}</digi:trn>
												</b>														
											</td>
											<td bgcolor="#ffffff">
												<c:choose>
													<c:when test="<%=customField instanceof ComboBoxCustomField%>">
														<c:set var="idx" value="${customField.value}"/>	
														<c:out value="${customField.options[idx]}"/>									
													</c:when>
													<c:when test="<%=customField instanceof CategoryCustomField%>">
														<c:if test="${customField.value > 0}" >
															<category:getoptionvalue categoryValueId="${customField.value}"/>
														</c:if>								
													</c:when>
													<c:when test="<%=customField instanceof DateCustomField%>">																
														<c:out value="${customField.strDate}"/>	
													</c:when>
													<c:when test="<%=customField instanceof RadioOptionCustomField%>">
														<logic:iterate name="customField" property="options" id="option" >
															<logic:equal name="option" property="key"  value="${customField.value}">
																<c:out value="${option.value}"/> 
															</logic:equal>
														</logic:iterate>
													</c:when>
													<c:when test="<%=customField instanceof CheckCustomField%>">
														<c:if test="${customField.value == true}" >
															<c:out value="${customField.labelTrue}"/>
														</c:if>
														<c:if test="${customField.value == false}" >
															<c:out value="${customField.labelFalse}"/>
														</c:if>
													</c:when>
													<c:otherwise>		
														<c:out value="${customField.value}"/>																												
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</field:display>
									</logic:iterate>


    	</table>
        
</c:if>
<c:if test="${aimEditActivityForm==null}">
		Invalid activity id
</c:if>

