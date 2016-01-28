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
.prnt_tbl td {
    border-collapse:collapse; 
    border-color:#CCCCCC;
}

.prnt_tbl td.field_name {
    text-align: right;
    vertical-align: top;
    max-width: 150px;
    word-wrap: break-word;
}

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
								<module:display name="/Activity Form/Identification/Project Title" parentModule="/Activity Form/Identification">
									<tr>
										<td class="head2-name" width="100%" align="center" bgcolor="#ffffff">
                                			<c:if test="${aimEditActivityForm.identification.title!=null}">${aimEditActivityForm.identification.title}</c:if>
										</td>
									</tr>	
								</module:display>
						</table>							
                        </td>
                      </tr>
                      <tr>
                        <td width="100%">
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
                            <tr>
                              <td align="center" vAlign="top">
								<table width="98%" cellspacing="0" class="prnt_tbl" cellpadding=4 style="border-collapse: collapse; border-color:#CCCCCC;" border="1">
									<!-- here starts identification -->
									<tr bgcolor="#f4f4f2">
										<td  align="center" colspan="2" bgcolor=#C7D4DB>
											<b><digi:trn key="aim:activityDetail">Activity Details</digi:trn></b>										
										</td>
									</tr>
								<feature:display name="Identification" module="Project ID and Planning">   
									<field:display name="AMP ID" feature="Identification">
									<tr>
										<td class="field_name" >
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
                                 
										
                                    <module:display name="/Activity Form/Identification/Activity Status" parentModule="/Activity Form/Identification">
                                        <tr>
                                              <td class="field_name" >
                                                  <b><digi:trn key="aim:status">Status</digi:trn></b>
                                              </td>
                                              <td bgcolor="#FFFFFF">
                                                  <category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}" />
                                              </td>
                                        </tr>
                                    </module:display>

                                    <module:display name="/Activity Form/Identification/Status Reason" parentModule="/Activity Form/Identification">
                                        <tr>
                                              <td class="field_name" >
                                                  <b><digi:trn key="aim:statusReason">Status Reason</digi:trn></b>
                                              </td>
                                              <td bgcolor="#FFFFFF">
                                                  <c:out value="${aimEditActivityForm.identification.statusReason}" escapeXml="false" />
                                              </td>
                                        </tr>
                                    </module:display>

								<module:display name="/Activity Form/Funding/Overview Section/Type of Cooperation" parentModule="/Activity Form/Funding/Overview Section">
                                    <tr>
                                        <td class="field_name">
                                            <b>
                                                <digi:trn>Type of Cooperation</digi:trn>
                                            </b></td>
                                        <td bgcolor="#ffffff">
                                            <c:out value="${aimEditActivityForm.identification.ssc_typeOfCooperation}"/>
                                        </td>
                                    </tr>
								</module:display>

								<module:display name="/Activity Form/Funding/Overview Section/Type of Implementation" parentModule="/Activity Form/Funding/Overview Section">
									<tr>
									<td class="field_name">
										<b><digi:trn>Type of Implementation</digi:trn></b>
									</td>
									<td bgcolor="#ffffff">
									<c:out value="${aimEditActivityForm.identification.ssc_typeOfImplementation}"/>
									</tr>
								</module:display>
								<module:display name="/Activity Form/Funding/Modalities" parentModule="/Activity Form/Funding">
								<tr>
									<td class="field_name">
									<b><digi:trn>Modalities</digi:trn></b>
								</td>
								<td bgcolor="#ffffff">
										<c:if test="${not empty aimEditActivityForm.identification.ssc_modalities}">				
										<b>
										<c:forEach var="modality" items="${aimEditActivityForm.identification.ssc_modalities}">
											${modality}<br/>
										</c:forEach>
										</b>
										</c:if>
									</td>
								</tr>
								</module:display>
						


							
                                 <module:display name="/Activity Form/Identification/Objective" parentModule="/Activity Form/Identification">   	
                                    <tr>
										<td class="field_name">
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
									</module:display>
									   	<module:display name="/Activity Form/Identification/Objective Comments" parentModule="/Activity Form/Identification">
											<logic:present name="currentMember" scope="session">
											<tr>
												<td class="field_name" >
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
										</module:display>
													
									<module:display name="/Activity Form/Identification/Description" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
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
									</module:display>

								<module:display name="/Activity Form/Identification/Project Comments" parentModule="/Activity Form/Identification">    	
                                    <tr>
										<td class="field_name">
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
									</module:display>
									<module:display name="/Activity Form/Identification/Lessons Learned" parentModule="/Activity Form/Identification">
                                    <tr>
										<td class="field_name">
											<b><digi:trn>Lessons Learned</digi:trn></b>
											</td>
											<td bgcolor="#ffffff">
												<c:if test="${not empty aimEditActivityForm.identification.lessonsLearned}">
													<bean:define id="lessonsLearnedKey">
														<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
													</bean:define>
													<b><digi:edit key="${lessonsLearnedKey}"></digi:edit></b>
												</c:if>
											</td>
										</tr>
									</module:display>
									
									<module:display name="/Activity Form/Identification/Project Impact" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
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
									</module:display>						
									<module:display name="/Activity Form/Identification/Activity Summary" parentModule="/Activity Form/Identification">
										<tr>
											<td class="field_name" >
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
									</module:display>
									<module:display name="/Activity Form/Identification/Conditionalities" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
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
									</module:display>

									<module:display name="/Activity Form/Identification/Project Management" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b>
												<digi:trn>Project Management</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.projectManagement!=null}">
											<c:set var="projectManagement" value="${aimEditActivityForm.identification.projectManagement}" />
											<digi:edit key="${projectManagement}"></digi:edit>
                                        </c:if>										
                                        </td>
									</tr>
									</module:display>									 
                                       <feature:display name="Identification" module="Project ID and Planning">   
									<module:display name="/Activity Form/Identification/Purpose" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
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
									</module:display>
									
									
									<module:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:purposeComments">Purpose Comments</digi:trn></b>
									  </td>
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

									<module:display name="/Activity Form/Identification/Results" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
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
									</module:display>
									
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<module:display name="/Activity Form/Identification/Results Comments" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name">
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
									</module:display>
									</logic:present>
									
									<module:display name="/Activity Form/Identification/Accession Instrument" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">

												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>
									<module:display name="/Activity Form/Identification/Project Implementing Unit" parentModule="/Activity Form/Identification">
										<c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">
										<tr>
											<td class="field_name" >
												<b><digi:trn>Project Implementing Unit</digi:trn></b>
											</td>
											<td bgcolor="#ffffff">

													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/>
												&nbsp;
											</td>
										</tr>
										</c:if>
									</module:display>

									<module:display name="/Activity Form/Identification/A.C. Chapter" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn>	</b>										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>
											</c:if>&nbsp;</td>
									</tr>
									</c:if>
									</module:display>

									<module:display name="/Activity Form/Identification/Cris Number" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:crisNumber">Cris Number</digi:trn>	</b></td>
										<td bgcolor="#ffffff">
										<c:out value="${aimEditActivityForm.identification.crisNumber}"/>&nbsp;</td>
									</tr>
									</module:display>

									<module:display name="/Activity Form/Identification/Procurement System" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:ProcurementSystem">Procurement System</digi:trn></b></td>
										<td bgcolor="#ffffff">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>
										<module:display name="/Activity Form/Identification/Reporting System" parentModule="/Activity Form/Identification">
										<c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:ReportingSystem">Reporting System</digi:trn></b></td>
										<td bgcolor="#ffffff">

												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>

									<module:display name="/Activity Form/Identification/Audit System" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:AuditSystem">Audit System</digi:trn></b></td>
										<td bgcolor="#ffffff">

												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>

									<module:display name="/Activity Form/Identification/Institutions" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.institutions > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:Institutions">Institutions</digi:trn></b></td>
										<td bgcolor="#ffffff">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>

									<module:display name="/Activity Form/Identification/Project Category" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:ProjectCategory">Project Category</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">

												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</module:display>
									<module:display name="/Activity Form/Identification/Government Agreement Number" parentModule="/Activity Form/Identification">
									<c:if test="${not empty aimEditActivityForm.identification.govAgreementNumber}">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:step1:GovernmentAgreementNumTitle">Government Agreement Number</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/>&nbsp;
										</td>
									</tr>
									</c:if>
									</module:display>
<!-- end identification in the same order as previewActivity -->
									<feature:display name="Budget" module="Project ID and Planning">
									<tr>
										<td class="field_name" >
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
										<c:if test="${aimEditActivityForm.identification.budgetCV == aimEditActivityForm.identification.budgetCVOn}">
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
										</td>
									</tr>
									</feature:display>

		</feature:display>
		<module:display name="/Activity Form/Identification/Humanitarian Aid" parentModule="/Activity Form/Identification">
								<tr>
									<td class="field_name" >
										<b><digi:trn key="aim:humanitarianaid">Humanitarian Aid</digi:trn></b>
									</td>
									<td bgcolor="#ffffff">
										<c:if test="${!aimEditActivityForm.identification.humanitarianAid==true}">
											<digi:trn key="aim:no">No</digi:trn>
										</c:if>
										<c:if test="${aimEditActivityForm.identification.humanitarianAid==true}">
											<digi:trn key="aim:yes">Yes</digi:trn>
										</c:if>
										&nbsp;
									</td>
								</tr>
		</module:display>

									<field:display feature="Identification" name="Organizations and Project ID">
											<tr>
												<td class="field_name">
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
										<td class="field_name">
											<b>
					            <digi:trn key="aim:planning">Planning</digi:trn>		</b>
											</td>
<td bgcolor="#ffffff">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<module:display name="/Activity Form/Planning/Line Ministry Rank" parentModule="/Activity Form/Planning">
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
												</module:display>
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

												<module:display name="/Activity Form/Planning/Proposed Project Life" parentModule="/Activity Form/Planning">
                                                    <tr>
                                                        <td width="32%"><digi:trn>Proposed Project Life</digi:trn></td>
                                                        <td width="1">:</td>
                                                        <td align="left">
                                                            ${aimEditActivityForm.planning.proposedProjectLife}
                                                        </td>
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

												<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Planning" module="Project ID and Planning">
														<field:display name="Current Completion Date Comments" feature="Planning">
															<tr>
																<td>
																	<digi:trn>Current Completion Date comments</digi:trn>:&nbsp;
																	<ul>
																		<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
																				<logic:equal name="comments" property="key" value="current completion date">
																					<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
																						<li>
																							<b><bean:write name="comment" property="comment" /></b>
																							<br />
																						</li>
																					</logic:iterate>
																				</logic:equal>
																		</logic:iterate>
																	</ul>
																</td>
															</tr>
														</field:display>
													</feature:display>
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
												<module:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Planning" module="Project ID and Planning">
														<field:display name="Final Date for Disbursements Comments" feature="Planning">
															<tr>
																<td>
																	<digi:trn>Final Date for Disbursements comments</digi:trn>:&nbsp;
																	<ul>
																		<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
																				<logic:equal name="comments" property="key" value="Final Date for Disbursements">
																					<logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
																						<li>
																							<b><bean:write name="comment" property="comment" /></b>
																							<br />
																						</li>
																					</logic:iterate>
																				</logic:equal>
																		</logic:iterate>
																	</ul>
																</td>
															</tr>
														</field:display>
													</feature:display>
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
									<td class="field_name">
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


                                 <module:display name="/Activity Form/Location" parentModule="/Activity Form">
                               		<module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
									<tr>
										<td class="field_name" >
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
						</td>
												</tr>
												</table>
											</c:if>
										</td>
									</tr>
									</module:display>
                                    <module:display name="/Activity Form/Location/Implementation Level" parentModule="/Activity Form/Location">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:level">Implementation Level</digi:trn>
										  </b>
										</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.location.levelId>0}" >
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/>
											</c:if>										</td>
									</tr>
									</module:display>
								  <module:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
									<tr>
										<td class="field_name">
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
									</module:display>

                            </module:display>
							<module:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
								<c:if test="${not empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
									<tr>
										<td class="field_name">
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
										<td class="field_name">
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
									</feature:display>
                                   </module:display>

                                     <module:display name="/Activity Form/Program/Primary Programs" parentModule="/Activity Form/Program">
                                     <c:if test="${not empty aimEditActivityForm.programs.primaryPrograms }">
                                           <TR>
												<td class="field_name">
								<b>
										     <digi:trn key="aim:primary Programs">Primary Programs</digi:trn>
																												</b></TD>

						  <TD bgcolor="#ffffff">
								<c:forEach var="program" items="${aimEditActivityForm.programs.primaryPrograms}">
                                	<c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%s<br/>
                                </c:forEach>
                   		</TD>
						</TR>
						</c:if>
										</module:display>
										<module:display name="/Activity Form/Program/Secondary Programs" parentModule="/Activity Form/Program">
											<c:if test="${not empty  aimEditActivityForm.programs.secondaryPrograms  }">
                                         	<TR>
												<td class="field_name" >
													<b>
											  <digi:trn key="aim:secondary Programs">Secondary Programs</digi:trn>
													</b></TD>
<TD bgcolor="#ffffff">
                                                      <c:forEach var="program" items="${aimEditActivityForm.programs.secondaryPrograms}">
	                                                      <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                      </c:forEach>
                                        		</TD>
											</TR>
											</c:if>
										</module:display>
										<module:display name="/Activity Form/Program/Tertiary Programs" parentModule="/Activity Form/Program">
											<c:if test="${not empty aimEditActivityForm.programs.tertiaryPrograms  }">
                                         	<TR>
												<td class="field_name" >
													<b>
											  <digi:trn key="aim:secondary Programs">Tertiary Programs</digi:trn>
													</b></TD>
<TD bgcolor="#ffffff">
                                                      <c:forEach var="program" items="${aimEditActivityForm.programs.tertiaryPrograms}">
	                                                      <c:out value="${program.hierarchyNames}" />&nbsp; <c:out value="${program.programPercentage}"/>%<br/>
                                                      </c:forEach>
                                        		</TD>
											</TR>
											</c:if>
										</module:display>
										<module:display name="/Activity Form/Program/Program Description" parentModule="/Activity Form/Program">
											<c:if test="${not empty aimEditActivityForm.programs.programDescription}">
                                         	<TR>
												<td class="field_name" >
													<b>
											  <digi:trn key="aim:secondary Programs">Program Description</digi:trn>
													</b></TD>
												<TD bgcolor="#ffffff">
					  							<c:set var="programDescription" value="${aimEditActivityForm.programs.programDescription}"/>
														<digi:edit key="${programDescription}"/>
                                        		</TD>
											</TR>
											</c:if>
										</module:display>

                                    <feature:display name="Sectors" module="Project ID and Planning">
									<tr>
										<td class="field_name" class="t-name">
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
										<td class="field_name">
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
										<td class="field_name" >
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
                                                                            <logic:notEmpty name="funding" property="orgFundingId">
                                                                            	<field:display name="Funding Organization Id" feature="Funding Information">
                                                                              		<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
                                                                                  				<digi:trn key="aim:fundingOrgId">Funding Organization Id</digi:trn>
                                                                                    		</a>
                                                                                    	</td>
                                                                                		<td width="10">:</td>
                                                                                		<td width="454" align="left">
                                                                                			<bean:write name="funding"	property="orgFundingId"/>
                                                                                		</td>
                                                                              		</tr>
                                                                             	</field:display>
                                                                             </logic:notEmpty>
                                                                             <field:display name="Funding Organization Name" feature="Funding Information">
                                                                              <tr>
                                                                                <td align="left" width="339">

                                                                                  <a title="<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>">
                                                                               	  <digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
                                                                                  </a>                                                                                </td>
                                                                                <td width="10">:</td>
                                                                                <td align="left" width="454">
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
																			<logic:notEmpty name="funding" property="typeOfAssistance">
																				<tr>
                                                                                	<td align="left" width="339">
                                                                                  		<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  			<digi:trn key="aim:typeOfAssist">Type of Assistance </digi:trn>
																				  		</a>
																					</td>
                                                                                	<td width="10">:</td>
                                                                                	<td align="left">
                                                                                		<digi:trn><bean:write name="funding" property="typeOfAssistance.value"/></digi:trn>
																					</td>
                                                                              	</tr>
                                                                              </logic:notEmpty>
																		</field:display>
																		<field:display name="Financing Instrument" feature="Funding Information">
																			<logic:notEmpty name="funding" property="financingInstrument">
																				<tr>
                                                                                	<td align="left" width="339">
                                                                                  		<a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                               	  		<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>
																				  		</a>
																				  	</td>
                                                                                	<td width="10">:</td>
                                                                                	<td align="left">
                                                                                		<digi:trn><bean:write name="funding" property="financingInstrument.value"/></digi:trn>
                                                                                	</td>
                                                                              	</tr>
                                                                              </logic:notEmpty>
                                                                           <field:display name="Credit/Donation" feature="Planning">
																				<logic:notEmpty name="funding" property="financingInstrument">
																					<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a title="<digi:trn key="aim:financialInst">Financial Instrument</digi:trn>">
                                                                                 	 			<digi:trn key="aim:credit_donation">Credit/Donation</digi:trn>
																				  			</a>
																				  		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                       		<bean:write name="funding"	property="financingInstrument.value"/>
                                                                                		</td>
                                                                              		</tr>
                                                                              	</logic:notEmpty>
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
																				<logic:notEmpty name="funding" property="modeOfPayment">
                                                                              		<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a><digi:trn>Mode of Payment</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                    		<digi:trn><bean:write name="funding" property="modeOfPayment.value"/></digi:trn>
																						</td>
																					</tr>
                                                                                  </logic:notEmpty>
																			</field:display>
																				<logic:notEmpty name="funding" property="fundingClassificationDate">
                                                                              		<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a><digi:trn>Funding Classification Date</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                    		<digi:trn><bean:write name="funding" property="fundingClassificationDate"/></digi:trn>
																						</td>
																					</tr>
                                                                                  </logic:notEmpty>
																			<!-- here it goes Donor Objective  and Conditions ISSUE AMP-16421-->
																			<field:display name="Conditions" feature="Funding Information">
																				<logic:notEmpty name="funding" property="conditions">
                                                                              		<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a><digi:trn>Conditions</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                			<digi:trn><bean:write name="funding" property="conditions"/></digi:trn>
                                                                                  		</td>
                                                                              		</tr>
																				</logic:notEmpty>
																			</field:display>

																			<field:display name="Agreement" feature="Funding Information">
																				<logic:notEmpty name="funding" property="title">
                                                                              		<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a><digi:trn>Agreement Title</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                			<digi:trn><bean:write name="funding" property="title"/></digi:trn>
                                                                                  		</td>
                                                                              		</tr>
<tr>
                                                                                		<td align="left" width="339">
                                                                                  			<a><digi:trn>Agreement Code</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td align="left">
                                                                                			<digi:trn><bean:write name="funding" property="code"/></digi:trn>
                                                                                  		</td>
                                                                              		</tr>
																				</logic:notEmpty>
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
                                                                        <jsp:include page="activitypreview/previewActivityFundingCommitments.jsp" />

                                                                        <feature:display module="Funding" name="Disbursement">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingDisbursement.jsp" />
                                                                        </feature:display>

                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>

																		<jsp:include page="activitypreview/previewMtefProjections.jsp" />

<%--                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>

                                                                        <feature:display module="Funding" name="Expenditures">
                                                                        	<jsp:include page="previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>
     --%>
                                                                        <feature:display module="Funding" name="Disbursement Orders">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingDisbursementOrders.jsp" />
                                                                        </feature:display>


                                                                        <feature:display module="Funding" name="Undisbursed Balance">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingUndisbursedBalance.jsp" />
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
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedCommitments">
	                        	<tr>
	                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase;">
	                            		<digi:trn key='aim:totalplannedcommittment'> TOTAL PLANNED COMMITMENTS</digi:trn>:
	                            	</td>
	                            	<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
										<bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" />
										<bean:write name="aimEditActivityForm" property="currCode" />
		                            	 &nbsp;
	                        		</td>
	                        	</tr>
							</logic:notEmpty>
                        </feature:display>
                       	<logic:notEmpty name="aimEditActivityForm" property="funding.totalMtefProjections">
                        	<tr>
                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase;">
                            		<digi:trn key='aim:totalmtefprojections'> TOTAL MTEF PROJECTIONS</digi:trn>:
                            	</td>
                            	<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
                            		<b><bean:write name="aimEditActivityForm" property="funding.totalMtefProjections" />
									<bean:write name="aimEditActivityForm" property="currCode" /></b>
	                            	 &nbsp;
                        		</td>
                        	</tr>
						</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalCommitments">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
                            			<digi:trn key='aim:totalactualcommittment'> TOTAL ACTUAL COMMITMENTS </digi:trn>:
                            		</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<bean:write name="aimEditActivityForm" property="funding.totalCommitments" />
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
	                              		&nbsp;
	                         		</td>
                        		</tr>
                        	</logic:notEmpty>
                        <field:display name="Pipeline" feature="Commitments">
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalPipelineCommitments">
								<tr>
	                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn> TOTAL PIPELINE COMMITMENTS: </digi:trn></td>
	                            	<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
										<bean:write name="aimEditActivityForm" property="funding.totalPipelineCommitments" />
										<bean:write name="aimEditActivityForm" property="currCode" />
										&nbsp;
									</td>
	                        	</tr>
	                        </logic:notEmpty>
                        </field:display>
                        <feature:display module="Funding" name="Disbursement">
							<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedDisbursements">
                       			<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key='aim:totalplanneddisbursement'>TOTAL PLANNED DISBURSEMENT</digi:trn>:</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" />
		                                <bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
                            		</td>
                        		</tr>
                        	</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalDisbursements">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000"><digi:trn key='aim:totalActualdisbursement'>TOTAL ACTUAL DISBURSEMENT </digi:trn>:</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<bean:write name="aimEditActivityForm" property="funding.totalDisbursements" />
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
                           			</td>
                        		</tr>
                        	</logic:notEmpty>
                        </feature:display>
                        <feature:display module="Funding" name="Expenditures">
							<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedExpenditures">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalActualExpenditures">TOTAL PLANNED EXPENDITURES</digi:trn>:</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" />
		                                <bean:write name="aimEditActivityForm" property="currCode" />
			                      		&nbsp;
                            		</td>
                        		</tr>
                        	</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalExpenditures">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalplannedExpenditures">TOTAL ACTUAL EXPENDITURES</digi:trn>:</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<bean:write name="aimEditActivityForm" property="funding.totalExpenditures" />
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
		                    		</td>
                        		</tr>
                        	</logic:notEmpty>
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
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.unDisbursementsBalance">
                      			<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:undisbursedBalance">UNDISBURSED BALANCE</digi:trn>:</td>
                            		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <bean:write name="aimEditActivityForm" property="funding.unDisbursementsBalance" />
		                                <bean:write name="aimEditActivityForm" property="currCode" />
		                         		&nbsp;
		                  			</td>
                        		</tr>
                        	</logic:notEmpty>
                        </feature:display>

                        <logic:notEmpty name="aimEditActivityForm" property="funding.consumptionRate">
                        	<tr>
                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:undisbursedBalance"> Consumption Rate</digi:trn>: </td>
                            	<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
									<b>${aimEditActivityForm.funding.consumptionRate}</b>
                                	&nbsp;
                            	</td>
                        	</tr>
                        </logic:notEmpty>
                        <logic:notEmpty name="aimEditActivityForm" property="funding.deliveryRate">
							<tr>
                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn>Delivery Rate</digi:trn>: </td>
								<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
                                	<b>${aimEditActivityForm.funding.deliveryRate}</b>
                                	&nbsp;
                            	</td>
                        	</tr>
                        </logic:notEmpty>
					</table>
				</td></tr>
		</logic:notEmpty>
	</table>
	</td>
											  </tr>
										  </table>
									  </td>
									</tr>
									</module:display>
								  </logic:present>

                                    <module:display name="/Activity Form/Aid Effectivenes" parentModule="/Activity Form">
                                        <logic:notEmpty name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                                            <tr>
                                                <td align="right" vAlign="top"><b><digi:trn>Aid Effectivenes</digi:trn></b></td>
                                                <td>
                                                    <logic:iterate id="option" name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                                                        <module:display name="/Activity Form/Aid Effectivenes/${option.indicator.ampIndicatorName}"
                                                            parentModule="/Activity Form/Aid Effectivenes">
                                                            <b>${option.indicator.ampIndicatorName}</b> -
                                                            ${option.ampIndicatorOptionName}
                                                            </br>
                                                        </module:display>
                                                    </logic:iterate>
                                                </td>
                                            </tr>
                                        </logic:notEmpty>
                                    </module:display>

                                    <module:display name="/Activity Form/Regional Funding" parentModule="/Activity Form">
									<tr>
										<td class="field_name" >
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
									</module:display>

									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
                                    <module:display name="Components" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td class="field_name" >
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
										<td class="field_name" >
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


								<module:display name="/Activity Form/Issues Section" parentModule="/Activity Form">
								<module:display name="/Activity Form/Issues Section/Issue" parentModule="/Activity Form/Issues Section">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:issues">
										    Issues</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border="0">
												<c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
													<tr><td valign="top"  colspan="3">
														<li class="level1"><b><c:out value="${issue.name}"/> <module:display name="/Activity Form/Issues Section/Issue/Date" parentModule="/Activity Form/Issues Section/Issue">
													<c:out value="${issue.issueDate}"/> </module:display></b></li>
													</td></tr>
														<module:display name="/Activity Form/Issues Section/Issue/Measure" parentModule="/Activity Form/Issues Section/Issue">
														<c:if test="${!empty issue.measures}">
															<c:forEach var="measure" items="${issue.measures}">
																<tr><td></td><td colspan="2">
																	<li class="level2"><i><c:out value="${measure.name}"/><module:display name="/Activity Form/Issues Section/Issue/Measure/Date" parentModule="/Activity Form/Issues Section/Issue/Measure">
									  							<c:out value="${measure.measureDate}"/></module:display></i></li>
																</td></tr>
																	<module:display name="/Activity Form/Issues Section/Issue/Measure/Actor" parentModule="/Activity Form/Issues Section/Issue/Measure">
																	<c:if test="${!empty measure.actors}">
																		<c:forEach var="actor" items="${measure.actors}">
																			<tr>
																			<td colspan="2"></td>
																			<td>
																				<li class="level3"><c:out value="${actor.name}"/></li>
																			</td></tr>
																		</c:forEach>
																	</c:if>
																	</module:display>
															</c:forEach>
														</c:if>
														</module:display>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</module:display>
									</module:display>
                             <module:display name="Document" parentModule="PROJECT MANAGEMENT">
                                   	<feature:display name="Related Documents" module="Document">
									<tr>
										<td class="field_name">
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
										</td>
									</tr>
									</feature:display>
                                 </module:display>


                                 <module:display name="/Activity Form/Line Ministry Observations" parentModule="/Activity Form">
                                 	<tr>
										<td class="field_name">
											<b><digi:trn>Line Ministry Observations</digi:trn></b>
                                     	</td>
                                     	<td>
											<c:if test="${not empty aimEditActivityForm.lineMinistryObservations.issues}">
												<logic:iterate name="aimEditActivityForm" id="lineMinistryObs" property="lineMinistryObservations.issues">
													<table style="width: 98%;">
														<module:display name="/Activity Form/Line Ministry Observations/Observation" parentModule="/Activity Form/Line Ministry Observations">
															<tr >
																<td width="27%;">
																	<b><digi:trn>Observation</digi:trn>:</b>
																</td>
																<td>
																	<b><c:out value="${lineMinistryObs.name}"/></b>
																</td>
															</tr>
														</module:display>
														<module:display name="/Activity Form/Line Ministry Observations/Observation/Date" parentModule="/Activity Form/Line Ministry Observations/Observation">
															<tr >
																<td>
																	<digi:trn>Observation Date</digi:trn>:
																</td>
																<td>
																	<c:out value="${lineMinistryObs.issueDate}"/>
																</td>
															</tr>
														</module:display>
														<logic:iterate name="lineMinistryObs" id="measure" property="measures">
															<tr>
																<td>
																	<digi:trn>Measure</digi:trn>:
																</td>
																<td>
																	<c:out value="${measure.name}"/>
																</td>
															</tr>
															<c:if test="${not empty measure.actors }">
																<tr>
																	<td>
																		<digi:trn>Actors</digi:trn>
																	</td>
																	<td>
																		<table style="border-collapse:collapse" cellpadding="0" cellspacing="0" widht="100%">
																			<logic:iterate name="measure" id="actor" property="actors">
																				<tr>
																					<td>
																						<c:out value="${actor.name}"/>
																					</td>
																				</tr>
																			</logic:iterate>
																		</table>
																	</td>
																</tr>
															</c:if>
														</logic:iterate>
													</table>
												</logic:iterate>
											</c:if>
										</td>
									</tr>
								</module:display>

								<module:display name="/Activity Form/Organizations" parentModule="/Activity Form">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:relatedOrganizations">
										    Related Organizations</digi:trn>
									  </b>									</td>

										<td bgcolor="#ffffff">
										<module:display name="/Activity Form/Organizations/Donor Organization" parentModule="/Activity Form/Organizations">
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
										<module:display name="/Activity Form/Organizations/Responsible Organization" parentModule="/Activity Form/Organizations">
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

                                           <module:display name="/Activity Form/Organizations/Executing Agency" parentModule="/Activity Form/Organizations">
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

											<module:display name="/Activity Form/Organizations/Implementing Agency" parentModule="/Activity Form/Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
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

											<module:display name="/Activity Form/Organizations/Beneficiary Agency" parentModule="/Activity Form/Organizations">
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

											<module:display name="/Activity Form/Organizations/Contracting Agency" parentModule="/Activity Form/Organizations">
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
											<module:display name="/Activity Form/Organizations/Sector Group" parentModule="/Activity Form/Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
											<field:display name="Sector Group" feature="Sector Group">
											<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b><br/>
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
											<br/>
											</field:display>
											</logic:notEmpty>
											</module:display>

											<module:display name="/Activity Form/Organizations/Regional Group" parentModule="/Activity Form/Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
											<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b><br/>
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
											</module:display>
                                           </td>
									</tr>
									</module:display>





                                    <module:display name="/Activity Form/Contacts" parentModule="/Activity Form">
									<module:display name="/Activity Form/Contacts/Donor Contact Information" parentModule="/Activity Form/Contacts">
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
											</module:display>
											<module:display name="/Activity Form/Contacts/Mofed Contact Information" parentModule="/Activity Form/Contacts">
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
											</module:display>
											<module:display name="/Activity Form/Contacts/Project Coordinator Contact Information" parentModule="/Activity Form/Contacts">
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
											</module:display>
											<module:display name="/Activity Form/Contacts/Sector Ministry Contact Information" parentModule="/Activity Form/Contacts">
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
										</module:display>
										<module:display name="/Activity Form/Contacts/Implementing Executing Agency Contact Information" parentModule="/Activity Form/Contacts">
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
										</module:display>
									</module:display>

							 		<field:display name="Activity Performance"  feature="Activity Dashboard">
									<tr>
										<td class="field_name">
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
										<td class="field_name">
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



                              <module:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost" parentModule="/Activity Form/Funding/Overview Section">
									<tr>
										<td class="field_name">
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
                                                       <module:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost" parentModule="/Activity Form/Funding/Overview Section/Proposed Project Cost">
	                                                       	<c:if
																test="${aimEditActivityForm.funding.proposedAnnualBudgets!=null
																&& aimEditActivityForm.funding.proposedAnnualBudgets.size()>0}">
													   <tr>
                                                       <td bgcolor="#FFFFFF" align="left" colspan="2">
                                                         	<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa"
																width="100%">
																<tr bgcolor="#f0f0f0">
																	<td><b><digi:trn key="aim:cost">Cost</digi:trn></b></td>
																	<td><b><digi:trn key="aim:cost">Year</digi:trn></b></td>
																</tr>
																<c:forEach var="annualBudget"
																	items="${aimEditActivityForm.funding.proposedAnnualBudgets}">
																	<tr bgcolor="#f0f0f0">
																		<td>${annualBudget.funAmount}
																			${annualBudget.currencCode}</td>
																		<td>${annualBudget.funDate}</td>
																	</tr>
																</c:forEach>
															</table>
													      </td>
                                                       </tr>
                                                    	</c:if>
                                                    	</module:display>
                                                        <module:display name="/Activity Form/Funding/Total Number of Funding Sources" parentModule="/Activity Form/Funding">
														 <tr bgcolor="#ffffff">
														<td>
															<digi:trn>Total Number of Funding Sources</digi:trn>
														</td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                          <c:if test="${aimEditActivityForm.identification.fundingSourcesNumber!=null}">
                                                             ${aimEditActivityForm.identification.fundingSourcesNumber}
                                                         </c:if>
                                                         </td>
                                                       </tr>
														</module:display>
                                              		</table>
                            				</c:if>										</td>
									</tr>
								  </module:display>

								  <module:display name="/Activity Form/Budget Structure/Budget Structure" parentModule="/Activity Form/Budget Structure">

									<tr>
										<td class="field_name">
											<b>
									  <digi:trn key="aim:proposedPrjectCost">Budget Structure</digi:trn></b>
									  </td>
									<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.budgetStructure != null}">
                                                  <table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%">
                                                      	<tr bgcolor="#ffffff">
															<td>
															<digi:trn>Name</digi:trn>
															</td>
															<td>
															<digi:trn>Percentage</digi:trn>
															</td>
														</tr>
														<c:forEach var="budgetStructure" items="${aimEditActivityForm.budgetStructure}" >
															<tr bgcolor="#ffffff">
																<td bgcolor="#FFFFFF" align="left" width="150">
																			<b>${budgetStructure.budgetStructureName}</b>
																</td>
																<td bgcolor="#FFFFFF" align="left" width="150">
																			<b>${budgetStructure.budgetStructurePercentage}%</b>
																</td>
															</tr>
														</c:forEach>
                                              		</table>
                            				</c:if>										</td>
									</tr>
								  </module:display>


                                 <logic:present name="currentMember" scope="session">
									<feature:display name="Costing" module="Activity Costing">
										<tr>
										<td class="field_name" >
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
										<td class="field_name" >
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
										<td class="field_name" >
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
										<td class="field_name" >
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
										<td class="field_name" >
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
										<td class="field_name" >
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

									<tr>
										<td class="field_name" >
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
									</logic:notEmpty>
    	</table>
        
</c:if>
<c:if test="${aimEditActivityForm==null}">
		Invalid activity id
</c:if>

