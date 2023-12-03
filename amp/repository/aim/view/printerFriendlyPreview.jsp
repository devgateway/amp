<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.ampModule.aim.helper.*" %>
<%@ page import = "org.digijava.ampModule.aim.helper.ChartGenerator" %>
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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
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
<% if(org.digijava.kernel.util.SiteUtils.isEffectiveLangRTL() == true) {%>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css_2/amp-rtl.css">
<% } %>
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
<div class="content-dir">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="left" border="0">
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
								<ampModule:display name="/Activity Form/Identification/Project Title" parentModule="/Activity Form/Identification">
									<tr>
										<td class="head2-name" width="100%" align="center" bgcolor="#ffffff">
                                			<c:if test="${not empty aimEditActivityForm.identification.title}">
                                				<span class="word_break bold">${aimEditActivityForm.identification.title}</span>
                                			</c:if>
										</td>
									</tr>	
								</ampModule:display>
						</table>							
                        </td>
                      </tr>
                      <tr>
                        <td width="100%">
							<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
                            <tr>
                              <div align="center" vAlign="top">
								<table width="98%" cellspacing="0" class="prnt_tbl fixed-layout" cellpadding=4 style="border-collapse: collapse; border-color:#CCCCCC;" border="1" >

								    <!-- columns widths -->
								    <tr><td width="30%"></td><td width="70%"></td></tr>

									<!-- here starts identification -->
									<tr bgcolor="#f4f4f2">
										<td  align="center" colspan="2" bgcolor=#C7D4DB>
											<b><digi:trn key="aim:activityDetail">Activity Details</digi:trn></b>
										</td>
									</tr>
								<feature:display name="Identification" ampModule="Project ID and Planning">
									<field:display name="AMP ID" feature="Identification">
									<tr>
										<td class="field_name">
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
                                 
										
                                    <ampModule:display name="/Activity Form/Identification/Activity Status" parentModule="/Activity Form/Identification">
                                        <tr>
                                              <td class="field_name">
                                                  <b><digi:trn key="aim:status">Status</digi:trn></b>
                                              </td>
                                              <td bgcolor="#FFFFFF">
	                                              <span class="word_break">
	                                                  <category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}" />
                                                  </span>
                                              </td>
                                        </tr>
                                    </ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Status Other Info"
													parentModule="/Activity Form/Identification">
										<c:if test="${not empty aimEditActivityForm.identification.statusOtherInfo}">
										<tr>
											<td class="field_name" >
												<b>
													<digi:trn>Status Other Info</digi:trn>
												</b>
											</td>
											<td bgcolor="#ffffff">
													<span class="word_break bold">
															${aimEditActivityForm.identification.statusOtherInfo}
													</span>
											</td>
										</tr>
										</c:if>
									</ampModule:display>

                                    <ampModule:display name="/Activity Form/Identification/Status Reason" parentModule="/Activity Form/Identification">
                                        <tr>
                                              <td class="field_name" >
                                                  <b><digi:trn key="aim:statusReason">Status Reason</digi:trn></b>
                                              </td>
                                              <td bgcolor="#FFFFFF">
                                                <span class="word_break">
                                                	<c:set var="objStatusReason" value="${aimEditActivityForm.identification.statusReason}" />
													<span class="word_break"><digi:edit key="${objStatusReason}"></digi:edit></span>
												</span>
                                              </td>
                                        </tr>
                                    </ampModule:display>

								<ampModule:display name="/Activity Form/Funding/Overview Section/Type of Cooperation" parentModule="/Activity Form/Funding/Overview Section">
                                    <tr>
                                        <td class="field_name">
                                            <b>
                                                <digi:trn>Type of Cooperation</digi:trn>
                                            </b></td>
                                        <td bgcolor="#ffffff">
                                        <span class="word_break">
                                            <c:out value="${aimEditActivityForm.identification.ssc_typeOfCooperation}"/>
                                        </span>
                                        </td>
                                    </tr>
								</ampModule:display>

								<ampModule:display name="/Activity Form/Funding/Overview Section/Type of Implementation" parentModule="/Activity Form/Funding/Overview Section">
									<tr>
									<td class="field_name">
										<b><digi:trn>Type of Implementation</digi:trn></b>
									</td>
									<td bgcolor="#ffffff">
										<span class="word_break">
										<c:out value="${aimEditActivityForm.identification.ssc_typeOfImplementation}"/>
										</span>
									</tr>
								</ampModule:display>

								<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities"/>
								<c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
									<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities"/>
								</c:if>

								<ampModule:display name="${modalitiesPath}" parentModule="/Activity Form/Funding/Overview Section">
								<tr>
									<td class="field_name">
									<b><digi:trn>Modalities</digi:trn></b>
								</td>
								<td bgcolor="#ffffff">
										<c:if test="${not empty aimEditActivityForm.identification.ssc_modalities}">
										<b>
										<c:forEach var="modality" items="${aimEditActivityForm.identification.ssc_modalities}">
											<span class="word_break">
												${modality}
											</span>
											<br/>
										</c:forEach>
										</b>
										</c:if>
									</td>
								</tr>
								</ampModule:display>

									<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities Other Info"/>
									<c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
										<c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities Other Info"/>
									</c:if>
									<ampModule:display name="${modalitiesPath}"
													parentModule="/Activity Form/Funding/Overview Section">
										<c:if test="${not empty aimEditActivityForm.identification.modalitiesOtherInfo}">
											<tr>
												<td class="field_name" >
													<b>
														<digi:trn>Modalities Other Info</digi:trn>
													</b>
												</td>
												<td bgcolor="#ffffff">
													<span class="word_break bold">
															${aimEditActivityForm.identification.modalitiesOtherInfo}
													</span>
												</td>
											</tr>
										</c:if>
									</ampModule:display>

                                 <ampModule:display name="/Activity Form/Identification/Objective" parentModule="/Activity Form/Identification">
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
											<span class="word_break">
												<digi:edit key="${objKey}"></digi:edit>
											</span>
                                         </c:if>
                                         </td>
									</tr> 
									</ampModule:display>
									   	<ampModule:display name="/Activity Form/Identification/Objective Comments" parentModule="/Activity Form/Identification">
											<logic:present name="currentMember" scope="session">
											<tr>
												<td class="field_name" >
													<b><digi:trn key="aim:objectiveComments">Objective Comments</digi:trn></b>	
												</td>
												<td bgcolor="#ffffff">
												 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
												 	<logic:equal name="comments" property="key" value="Objective Assumption">
														<logic:iterate name="comments" id="comment" property="value"
															type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
															<digi:trn key="aim:objectiveAssumption">Objective Assumption</digi:trn>:</b>
															<span class="word_break">
																<bean:write name="comment" property="comment"/><br/>
															</span>
		                                        		</logic:iterate>
		                                        	</logic:equal>
		                                        	<logic:equal name="comments" property="key" value="Objective Verification">
														<logic:iterate name="comments" id="comment" property="value"
															type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
															<digi:trn key="aim:objectiveVerification">Objective Verification</digi:trn>:</b>
															<span class="word_break">
																<bean:write name="comment" property="comment"/>
															</span>
															<br/>
		                                        		</logic:iterate>
		                                        	</logic:equal>
		                                        	<logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
														<logic:iterate name="comments" id="comment" property="value"
															type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
															<digi:trn key="aim:objectivelyVerificationIndicators">Objective Objectively Verifiable Indicators</digi:trn>:</b>
															<span class="word_break">
																<bean:write name="comment" property="comment"/>
															</span>
															<br/>
		                                        		</logic:iterate>
		                                        	</logic:equal>
												</logic:iterate>
												</td>
											</tr>
											</logic:present>
										</ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Description" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<span class="bold">
												<digi:trn key="aim:description">Description</digi:trn>
											</span>
										</td>
										<td bgcolor="#ffffff">
	                                        <c:if test="${aimEditActivityForm.identification.description!=null}">
												<c:set var="descKey" value="${aimEditActivityForm.identification.description}" />
												<span class="word_break"><digi:edit key="${descKey}"></digi:edit></span>
	                                        </c:if>
										</td>
									</tr>
									</ampModule:display>

								<ampModule:display name="/Activity Form/Identification/Project Comments" parentModule="/Activity Form/Identification">
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
												<span class="word_break"><digi:edit key="${objKey}"></digi:edit></span>
	                                         </c:if>
                                         </td>
									</tr>
									</ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Lessons Learned" parentModule="/Activity Form/Identification">
                                    <tr>
										<td class="field_name">
											<b><digi:trn>Lessons Learned</digi:trn></b>
											</td>
											<td bgcolor="#ffffff">
												<c:if test="${not empty aimEditActivityForm.identification.lessonsLearned}">
													<bean:define id="lessonsLearnedKey">
														<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
													</bean:define>
													<span class="word_break bold">
														<digi:edit key="${lessonsLearnedKey}"></digi:edit>
													</span>
												</c:if>
											</td>
										</tr>
									</ampModule:display>
									
									<ampModule:display name="/Activity Form/Identification/Project Impact" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b>
												<digi:trn key="aim:Project Impact">Project Impact</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.projectImpact!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.projectImpact}" />
											<span class="word_break">
												<digi:edit key="${descKey}"></digi:edit>
											</span>
                                        </c:if>
                                        </td>
									</tr>
									</ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Activity Summary" parentModule="/Activity Form/Identification">
										<tr>
											<td class="field_name" >
												<b>
													<digi:trn>Activity Summary:</digi:trn>
										  		</b>
										  	</td>
										  	<td bgcolor="#ffffff">
	                                        <c:if test="${aimEditActivityForm.identification.activitySummary!=null}">
												<c:set var="descKey" value="${aimEditActivityForm.identification.activitySummary}" />
												<span class="word_break">
													<digi:edit key="${descKey}"></digi:edit>
												</span>
	                                        </c:if>
	                                        </td>
										</tr>
									</ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Conditionalities" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b>
												<digi:trn>Conditionalities</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.conditionality!=null}">
											<c:set var="descKey" value="${aimEditActivityForm.identification.conditionality}" />
											<span class="word_break">
											<digi:edit key="${descKey}"></digi:edit>
											</span>
                                        </c:if>
                                        </td>
									</tr>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Project Management" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b>
												<digi:trn>Project Management</digi:trn>
									  		</b>								
									  	</td>
									  	<td bgcolor="#ffffff">
                                        <c:if test="${aimEditActivityForm.identification.projectManagement!=null}">
											<c:set var="projectManagement" value="${aimEditActivityForm.identification.projectManagement}" />
											<span class="word_break">
												<digi:edit key="${projectManagement}"></digi:edit>
											</span>
                                        </c:if>
                                        </td>
									</tr>
									</ampModule:display>
                                       <feature:display name="Identification" ampModule="Project ID and Planning">
									<ampModule:display name="/Activity Form/Identification/Purpose" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:purpose">
										    Purpose</digi:trn> 
									  </b>										</td>
		    							<td bgcolor="#ffffff">
                                          <c:if test="${aimEditActivityForm.identification.purpose!=null}">
											<c:set var="objKey" value="${aimEditActivityForm.identification.purpose}" />
											<span class="word_break">
												<digi:edit key="${objKey}"></digi:edit>
											</span>
                                         </c:if>
                                         </td>
									</tr>
									</ampModule:display>
									
									
									<ampModule:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:purposeComments">Purpose Comments</digi:trn></b>
									  </td>
				       					<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:</b>
													<span class="word_break">
													<bean:write name="comment" property="comment"/><br/>
													</span>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:</b>
													<span class="word_break">
													<bean:write name="comment" property="comment"/>
													</span>
													<br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:</b>
													<span class="word_break">
														<bean:write name="comment" property="comment"/>
													</span>
													<br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										</td>
									</tr>
									</logic:present>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Results" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name">
											<span class="bold"><digi:trn key="aim:results">Results</digi:trn></span>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.results!=null}">
												<c:set var="objKey" value="${aimEditActivityForm.identification.results}" />
												<span class="word_break">
													<digi:edit key="${objKey}"></digi:edit>
												</span>
											</c:if>
										</td>
									</tr>
									</ampModule:display>
									
									<logic:present name="aimEditActivityForm" property="comments.allComments">
									<ampModule:display name="/Activity Form/Identification/Results Comments" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name">
										<b><digi:trn key="aim:resultsComments">Results Comments</digi:trn></b>
									</td>
									<td bgcolor="#ffffff">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:</b>
													<span class="word_break">
														<bean:write name="comment" property="comment"/><br/>
													</span>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:</b>
													<span class="word_break">
														<bean:write name="comment" property="comment"/>
													</span>
													<br/>
                                        		</logic:iterate>
                                        	</logic:equal>
                                        	<logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
												<logic:iterate name="comments" id="comment" property="value"
													type="org.digijava.ampModule.aim.dbentity.AmpComments"><b>
													<digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:</b>
													<span class="word_break">
													<bean:write name="comment" property="comment"/>
													</span><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										</td>
									</tr>
									</ampModule:display>
									</logic:present>
									
									<ampModule:display name="/Activity Form/Identification/Accession Instrument" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
									<tr>
										<td class="field_name" >
											<b>
												<digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>
									<ampModule:display name="/Activity Form/Identification/Project Implementing Unit" parentModule="/Activity Form/Identification">
										<c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">
										<tr>
											<td class="field_name" >
												<b><digi:trn>Project Implementing Unit</digi:trn></b>
											</td>
											<td bgcolor="#ffffff">
												<span class="word_break">
													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/>
												</span>
												&nbsp;
											</td>
										</tr>
										</c:if>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/A.C. Chapter" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:acChapter"> A.C. Chapter</digi:trn></b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.acChapter > 0}">
												<span class="word_break">
													<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>
												</span>
											</c:if>&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Cris Number" parentModule="/Activity Form/Identification">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:crisNumber">Cris Number</digi:trn></b>
										</td>
										<td bgcolor="#ffffff">
										<c:out value="${aimEditActivityForm.identification.crisNumber}"/>&nbsp;</td>
									</tr>
									</ampModule:display>

								    <ampModule:display name="/Activity Form/Identification/IATI Identifier" parentModule="/Activity Form/Identification">
									    <tr>
										   <td class="field_name" >
											   <b><digi:trn key="aim:iatiIdentifier">IATI Identifier</digi:trn></b>
										   </td>
										   <td bgcolor="#ffffff">
											   <c:out value="${aimEditActivityForm.identification.iatiIdentifier}"/>&nbsp;</td>
									    </tr>
								    </ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Procurement System" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:ProcurementSystem">Procurement System</digi:trn></b></td>
										<td bgcolor="#ffffff">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/>
											&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>
										<ampModule:display name="/Activity Form/Identification/Reporting System" parentModule="/Activity Form/Identification">
										<c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:ReportingSystem">Reporting System</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/>
											</span>
											&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Audit System" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:AuditSystem">Audit System</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/>
											</span>
											&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Institutions" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.institutions > 0}">
									<tr>
										<td class="field_name" ><b><digi:trn key="aim:Institutions">Institutions</digi:trn></b></td>
										<td bgcolor="#ffffff">
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/>
											</span>
											&nbsp;</td>
									</tr>
									</c:if>
									</ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Project Category" parentModule="/Activity Form/Identification">
									<c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:ProjectCategory">Project Category</digi:trn></b>
										</td>
										<td bgcolor="#ffffff">
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/>
											</span>
											&nbsp;
										</td>
									</tr>
									</c:if>
									</ampModule:display>


										   <ampModule:display name="/Activity Form/Identification/Project Category Other Info"
														   parentModule="/Activity Form/Identification">
											   <c:if test="${not empty aimEditActivityForm.identification.projectCategoryOtherInfo}">
												   <tr>
													   <td class="field_name" >
														   <b>
															   <digi:trn>Project Category Other Info</digi:trn>
														   </b>
													   </td>
													   <td bgcolor="#ffffff">
													<span class="word_break bold">
															${aimEditActivityForm.identification.projectCategoryOtherInfo}
													</span>
													   </td>
												   </tr>
											   </c:if>
										   </ampModule:display>

									<ampModule:display name="/Activity Form/Identification/Government Agreement Number" parentModule="/Activity Form/Identification">
									<c:if test="${not empty aimEditActivityForm.identification.govAgreementNumber}">
									<tr>
										<td class="field_name" ><b>
									  <digi:trn key="aim:step1:GovernmentAgreementNumTitle">Government Agreement Number</digi:trn>		</b>								</td>
										<td bgcolor="#ffffff">
										<span class="word_break">
											<c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/>&nbsp;
										</span>
										</td>
									</tr>
									</c:if>
									</ampModule:display>
<!-- end identification in the same order as previewActivity -->


										   <!-- PROJECT INTERNAL IDS SECTION -->
										   <ampModule:display name="/Activity Form/Activity Internal IDs" parentModule="/Activity Form">
											   <tr>
												   <td class="field_name" >
													   <b>
														   <digi:trn>Agency Internal IDs</digi:trn>
													   </b>										</td>
												   <td bgcolor="#ffffff">
													   <c:if test="${!empty aimEditActivityForm.internalIds}">
														   <table width="100%" cellSpacing="2" cellPadding="1">
															   <c:forEach var="internalObj" items="${aimEditActivityForm.internalIds}">
																   <tr>
																	   <td>
																			<span class="word_break bold">
																				[${internalObj.organisation.name}]
																			</span>
																	   </td>
																	   <td align="right">
																		   <ampModule:display name="/Activity Form/Activity Internal IDs/Internal IDs/internalId" parentModule="/Activity Form">
																				<c:out value="${internalObj.internalId}"/>
																		   </ampModule:display>
																	   </td>
																   </tr>
															   </c:forEach>
														   </table>
													   </c:if>
												   </td>
											   </tr>
										   </ampModule:display>
										   <!-- END PROJECT INTERNAL IDS SECTION -->


									<feature:display name="Budget" ampModule="Project ID and Planning">
									<tr>
										<td class="field_name" >
										<b>
											<digi:trn key="aim:actBudget">Budget</digi:trn>
										</b>
										</td>
										<td bgcolor="#ffffff">
										<field:display name="Activity Budget" feature="Budget">
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
													<span class="word_break">
														<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.budgetCV}" />
													</span>
												</c:otherwise>
											</c:choose>
											<br>
										</field:display>

										<c:if test="${aimEditActivityForm.identification.budgetCV == aimEditActivityForm.identification.budgetCVOn}">
											<ampModule:display name="/Activity Form/Identification/Budget Extras/FY" parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>FY</digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.FY"/>
												</span>
												<br />
											</ampModule:display>
											<ampModule:display name="/Activity Form/Identification/Budget Extras/Ministry Code"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Ministry Code</digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.ministryCode"/>
												</span>
												<br />
											</ampModule:display>
											<ampModule:display name="/Activity Form/Identification/Budget Extras/Project Code"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Project Code</digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.projectCode"/>
												</span>
												<br />
											</ampModule:display>
											<ampModule:display name="/Activity Form/Identification/Budget Extras/Sub-Program"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Sub-Program</digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.subProgram"/>
												</span>
												<br />
											</ampModule:display>
											<ampModule:display name="/Activity Form/Identification/Budget Extras/Sub-Vote"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Sub-Vote </digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.subVote"/>
												</span>
												<br />
											</ampModule:display>
											<ampModule:display name="/Activity Form/Identification/Budget Extras/Vote"  parentModule="/Activity Form/Identification/Budget Extras">
												<digi:trn>Vote</digi:trn>:&nbsp;
												<span class="word_break bold">
													<bean:write name="aimEditActivityForm" property="identification.vote"/>
												</span>
												<br />
											</ampModule:display>
										</c:if>
										</td>
									</tr>
									</feature:display>

		</feature:display>
		<ampModule:display name="/Activity Form/Identification/Humanitarian Aid" parentModule="/Activity Form/Identification">
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
		</ampModule:display>

									<field:display feature="Identification" name="Organizations and Project ID">
										<tr>
											<td class="field_name">
												<b><digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn></b>
											</td>
											<td width="73%" bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.identification.selectedOrganizations}">
														<table cellSpacing="2" cellPadding="2" border="0">
															<c:forEach var="selectedOrganizations" items="${aimEditActivityForm.identification.selectedOrganizations}" >
																<c:if test="${not empty selectedOrganizations}">
																	<tr><td>
																	<span class="word_break">
																		<c:out value="${selectedOrganizations.organisation.name}"/>:
																	</span>
																	<span class="word_break">
																		<c:out value="${selectedOrganizations.projectId}"/>
																	</span>
																	</td></tr>
																</c:if>
															</c:forEach>
														</table>
													</c:if>
												</td>
										  </tr>
										</field:display>

                                        <feature:display ampModule="Project ID and Planning" name="Planning">
									<tr>
										<td class="field_name">
											<b> <digi:trn key="aim:planning">Planning</digi:trn> </b>
											</td>
											<td bgcolor="#ffffff">
												<table width="100%" cellSpacing="2" cellPadding="1">
												<ampModule:display name="/Activity Form/Planning/Line Ministry Rank" parentModule="/Activity Form/Planning">
												<field:display feature="Planning" name="Line Ministry Rank">
												<tr>
													<td width="32%"><digi:trn key="aim:lineMinRank">
													Line Ministry Rank</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														<c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
															<span class="word_break">${aimEditActivityForm.planning.lineMinRank}</span>
														</c:if>
													</td>
												</tr>
												</field:display>
												</ampModule:display>
												<ampModule:display name="/Activity Form/Planning/Proposed Approval Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
														<digi:trn key="aim:proposedApprovalDate">Proposed Approval Date</digi:trn>
													</td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.originalAppDate}
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Actual Approval Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:actualapprovaldate">Actual Approval Date</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.revisedAppDate}
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Proposed Start Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
														<digi:trn>Proposed Start Date</digi:trn>
													</td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.originalStartDate}
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Actual Start Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn>Actual Start Date</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.revisedStartDate}
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Original Completion Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn>Original Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.originalCompDate}
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Proposed Completion Date" parentModule="/Activity Form/Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn key="aim:proposedCompletionDate">
													Proposed Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														${aimEditActivityForm.planning.proposedCompDate}
													</td>
												</tr>
												</c:if>
												</ampModule:display>
												<ampModule:display name="/Activity Form/Planning/Actual Completion Date" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
													<digi:trn>Actual Completion Date</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														<c:out value="${aimEditActivityForm.planning.currentCompDate}"/>
													</td>
												</tr>
												</ampModule:display>
												
												<ampModule:display name="/Activity Form/Planning/Project Implementation Delay" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
													<digi:trn>Project Implementation Delay</digi:trn></td>
													<td width="1">:</td>
													<td class="preview-align-inverse">
														<c:out value="${aimEditActivityForm.planning.projectImplementationDelay}"/>
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Planning" ampModule="Project ID and Planning">
														<field:display name="Current Completion Date Comments" feature="Planning">
															<tr> <td>
																<digi:trn>Current Completion Date comments</digi:trn>:&nbsp;
																<ul>
																	<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
																			<logic:equal name="comments" property="key" value="current completion date">
																				<logic:iterate name="comments" id="comment" property="value" type="org.digijava.ampModule.aim.dbentity.AmpComments">
																					<li>
																					<span class="word_break bold">
																						<b><bean:write name="comment" property="comment" />
																					</span>
																						<br />
																					</li>
																				</logic:iterate>
																			</logic:equal>
																	</logic:iterate>
																</ul>
															</td> </tr>
														</field:display>
													</feature:display>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Final Date for Contracting" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%">
													<digi:trn key="aim:ContractingDateofProject1">Final Date for Contracting</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.planning.contractingDate}"/>
													</td>
												</tr>
												</ampModule:display>

												<ampModule:display name="/Activity Form/Planning/Final Date for Disbursements" parentModule="/Activity Form/Planning">
												<tr>
													<td width="32%"><digi:trn key="aim:DisbursementsDateofProject1">Final Date for Disbursements</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
														<c:out value="${aimEditActivityForm.planning.disbursementsDate}"/>
													</td>
												</tr>
												</ampModule:display>
												<ampModule:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
													<feature:display name="Planning" ampModule="Project ID and Planning">
														<field:display name="Final Date for Disbursements Comments" feature="Planning">
															<tr>
																<td>
																	<digi:trn>Final Date for Disbursements comments</digi:trn>:&nbsp;
																	<ul>
																		<logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
																				<logic:equal name="comments" property="key" value="Final Date for Disbursements">
																					<logic:iterate name="comments" id="comment" property="value" type="org.digijava.ampModule.aim.dbentity.AmpComments">
																						<li>
																							<span class="word_break bold">
																								<bean:write name="comment" property="comment" />
																							</span>
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
												</ampModule:display>
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
																	<c:out value="${closeDate}"/>
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
												</c:if>

													<ampModule:display name="/Activity Form/Planning/Proposed Project Life" parentModule="/Activity Form/Planning">
														<tr>
															<td width="32%"><digi:trn>Proposed Project Life</digi:trn></td>
															<td width="1">:</td>
															<td align="left">
																	${aimEditActivityForm.planning.proposedProjectLife}
															</td>
														</tr>
													</ampModule:display>

												<field:display name="Duration of Project" feature="Planning">
												<c:if test="${!aimEditActivityForm.editAct}">
												<tr>
													<td width="32%"><digi:trn>
													Duration of project</digi:trn></td>
													<td width="1">:</td>
													<td align="left">
													<c:if test="${not empty aimEditActivityForm.planning.projectPeriod}">
													   <b>${aimEditActivityForm.planning.projectPeriod}&nbsp; </b><digi:trn>Months</digi:trn>
													</c:if>
													</td>
												</tr>
												</c:if>
												</field:display>

											</table>
										</td>
									</tr>
									</feature:display>
                                        <ampModule:display name="References" parentModule="PROJECT MANAGEMENT">
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
													<c:if test="${not empty refDoc.comment}">
														<span class="word_break bold">${refDoc.categoryValue}</span>
													</c:if>
												</td>
											</tr>
										</table>
									</c:forEach>
									</td>
									</tr>
									</ampModule:display>


                                 <ampModule:display name="/Activity Form/Location" parentModule="/Activity Form">
                               		<ampModule:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
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
													<td width="85%">
														<c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
														<span class="word_break bold">
															[${ancestorLoc}]
														</span>
	                                                    </c:forEach>
													</td>
													<td width="15%">
														<!-- <c:out value="${locations.percent}"/>% -->
														<field:display name="Regional Percentage" feature="Location">
														<c:if test="${selectedLocs.showPercent}">
															<c:out value="${selectedLocs.percent}"/> %
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
									</ampModule:display>
                                    <ampModule:display name="/Activity Form/Location/Implementation Level" parentModule="/Activity Form/Location">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:level">Implementation Level</digi:trn>
										  </b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.location.levelId>0}" >
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.levelId}"/>
											</span>
											</c:if>
										</td>
									</tr>
									</ampModule:display>
								  <ampModule:display name="/Activity Form/Location/Implementation Location" parentModule="/Activity Form/Location">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:implementationLocation">
										    Implementation Location</digi:trn>
										  </b>
										</td>
<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.location.implemLocationLevel>0}" >
											<span class="word_break">
												<category:getoptionvalue categoryValueId="${aimEditActivityForm.location.implemLocationLevel}"/>
												</span>
											</c:if>										</td>
									</tr>
									</ampModule:display>

                            </ampModule:display>
                            <ampModule:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">

                                	<feature:display name="NPD Programs" ampModule="National Planning Dashboard">
									<field:display name="National Planning Objectives" feature="NPD Programs">
									<TR>
										<td class="field_name">
												<b>
								      <digi:trn key="aim:national Plan Objective"> National Plan Objective</digi:trn>
												</b></TD>
<TD bgcolor="#ffffff">
											<c:forEach var="program" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
											<span class="word_break">
                                                 <c:out value="${program.hierarchyNames}" />&nbsp; <c:out
													value="${program.programPercentage}"/> %<br/>
                                            </span>
                                             </c:forEach>
		                                   </TD>
									      </TR>
                                        </field:display>
									  </feature:display>
                                     </ampModule:display>
                                    
                                    <!-- PROGRAMS SECTION -->
                                    <ampModule:display name="/Activity Form/Program" parentModule="/Activity Form">
                                        <tr>
                                            <td class="field_name" class="t-name">
                                                <b>
                                                    <digi:trn>Program</digi:trn>
                                                </b>
                                            </td>
                                            <td bgcolor="#ffffff">
                                            <table width="100%" cellSpacing="2" cellPadding="1">
					                         <ampModule:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
					                                <c:if test="${not empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
					                                    <tr>
					                                        <td>
					                                            <b><digi:trn>National Plan Objective</digi:trn></b>
					                                            <c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
					                                                    <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;" border="0">
					                                                        <tr>
					                                                            <td width="85%">
					                                                                <span class="word_break">${nationalPlanObjectivePrograms.hierarchyNames}</span>
					                                                            </td>
					                                                            <td width="15%" valign="top">
					                                                                <span class="word_break">${nationalPlanObjectivePrograms.programPercentage} %</span>
					                                                            </td>
					                                                        </tr>
					                                                    </table>
					                                            </c:forEach>
					                                        </td>
					                                    </tr>
					                                </c:if>
				                             </ampModule:display>
				                             <ampModule:display name="/Activity Form/Program/Primary Programs" parentModule="/Activity Form/Program">
                                                    <c:if test="${not empty aimEditActivityForm.programs.primaryPrograms}">
                                                        <tr>
                                                            <td>
                                                                <b><digi:trn>Primary Programs</digi:trn></b>
                                                                <c:forEach var="program" items="${aimEditActivityForm.programs.primaryPrograms}">
                                                                        <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;" border="0">
                                                                            <tr>
                                                                                <td width="85%">
                                                                                    <span class="word_break">${program.hierarchyNames}</span>
                                                                                </td>
                                                                                <td width="15%" valign="top">
                                                                                    <span class="word_break">${program.programPercentage} %</span>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                </c:forEach>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                             </ampModule:display>
                                             <ampModule:display name="/Activity Form/Program/Secondary Programs" parentModule="/Activity Form/Program">
                                                    <c:if test="${not empty aimEditActivityForm.programs.secondaryPrograms}">
                                                        <tr>
                                                            <td>
                                                                <b><digi:trn>Secondary Programs</digi:trn></b>
                                                                <c:forEach var="program" items="${aimEditActivityForm.programs.secondaryPrograms}">
                                                                        <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;" border="0">
                                                                            <tr>
                                                                                <td width="85%">
                                                                                    <span class="word_break">${program.hierarchyNames}</span>
                                                                                </td>
                                                                                <td width="15%" valign="top">
                                                                                    <span class="word_break">${program.programPercentage} %</span>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                </c:forEach>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                             </ampModule:display>
		                                     <ampModule:display name="/Activity Form/Program/Tertiary Programs" parentModule="/Activity Form/Program">
                                                    <c:if test="${not empty aimEditActivityForm.programs.tertiaryPrograms}">
                                                        <tr>
                                                            <td>
                                                                <b><digi:trn>Tertiary Programs</digi:trn></b>
                                                                <c:forEach var="program" items="${aimEditActivityForm.programs.tertiaryPrograms}">
                                                                        <table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;" border="0">
                                                                            <tr>
                                                                                <td width="85%">
                                                                                    <span class="word_break">${program.hierarchyNames}</span>
                                                                                </td>
                                                                                <td width="15%" valign="top">
                                                                                    <span class="word_break">${program.programPercentage} %</span>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                </c:forEach>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                             </ampModule:display>
                                             <ampModule:display name="/Activity Form/Program/Program Description" parentModule="/Activity Form/Program">
                                                    <c:if test="${not empty aimEditActivityForm.programs.programDescription}">
                                                        <tr>
                                                            <td>
                                                                <b><digi:trn>Program Description</digi:trn></b>
                                                                <c:set var="programDescription" value="${aimEditActivityForm.programs.programDescription}"/>
                                                                </br>
                                                                <span class="word_break">${programDescription}</span>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                             </ampModule:display>
											</table>
										  </td>
										</tr>
									</ampModule:display>

                                    <!-- SECTORS SECTION -->
                                    <ampModule:display name="/Activity Form/Sectors" parentModule="/Activity Form">
                                        <tr>
                                            <td class="field_name" class="t-name">
                                                <b>
                                                    <digi:trn key="aim:sector">Sector</digi:trn>
                                                </b>
                                            </td>
                                            <td bgcolor="#ffffff">
                                                <c:forEach var="config"
                                                           items="${aimEditActivityForm.sectors.classificationConfigs}"
                                                           varStatus="ind">
                                                    <ampModule:display name="/Activity Form/Sectors/${config.name} Sectors"
                                                                    parentModule="/Activity Form/Sectors">
                                                        <c:set var="hasSectors">false</c:set>
                                                        <c:forEach var="actSect"
                                                                   items="${aimEditActivityForm.sectors.activitySectors}">
                                                            <c:if test="${actSect.configId==config.id}">
                                                                <c:set var="hasSectors">true</c:set>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:if test="${hasSectors}">
                                                            <strong>
                                                                <digi:trn
                                                                        key="aim:addactivitysectors:${config.name} Sector">
                                                                    <c:out value="${config.name} Sector"/>
                                                                </digi:trn>
                                                            </strong><br/>
                                                        </c:if>
                                                        <c:if test="${!empty aimEditActivityForm.sectors.activitySectors}">
                                                            <table width="100%" cellSpacing="2" cellPadding="1">
                                                                <c:forEach var="sectors"
                                                                           items="${aimEditActivityForm.sectors.activitySectors}">
                                                                    <c:if test="${sectors.configId==config.id}">
                                                                        <ampModule:display name="/Activity Form/Sectors"
                                                                                        parentModule="/Activity Form">
                                                                            <tr>
                                                                                <td width="85%">
                                                                                    <c:if test="${!empty sectors.sectorName}">
                                                                                        <span class="word_break">
                                                                                            <c:out value="${sectors.sectorName}"/>
                                                                                        </span>
                                                                                    </c:if>&nbsp;&nbsp;
                                                                                    <c:if test="${!empty sectors.subsectorLevel1Name}">
                                                                                        <span class="word_break">
                                                                                            [<c:out
                                                                                                value="${sectors.subsectorLevel1Name}"/>]
                                                                                        </span>
                                                                                    </c:if>
                                                                                    <c:if test="${!empty sectors.subsectorLevel2Name}">
                                                                                        <span class="word_break">
                                                                                            [<c:out
                                                                                                value="${sectors.subsectorLevel2Name}"/>]
                                                                                        </span>
                                                                                    </c:if>
																				</td>
																				<td width="15%">
                                                                                    <c:if test="${sectors.sectorPercentage!=''}">
                                                                                        <c:if test="${sectors.sectorPercentage!='0'}">
                                                                                            (<c:out
                                                                                                value="${sectors.sectorPercentage}"/>)%
                                                                                        </c:if>
                                                                                    </c:if>
                                                                                </td>
                                                                            </tr>
                                                                        </ampModule:display>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </table>
                                                        </c:if>
                                                    </ampModule:display>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                    </ampModule:display>
                                    <!-- END SECTORS SECTION -->

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
														<span class="word_break">
															${compo.sectorName}
														</span>
														</td>
														<td align="right">
															${compo.sectorPercentage} %
														</td>
													</tr>
													</c:forEach>
												</table>
									  </td>
									</tr>
								  </c:if>
                                  <logic:present name="currentMember" scope="session">
									<ampModule:display name="Funding" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td class="field_name" >
											<b><digi:trn key="aim:funding"> Funding</digi:trn></b>
									  </td>
										<td bgcolor="#ffffff">
										    <table width="100%" cellspacing="1" cellpadding="0" border="0" align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing=8 cellpadding="0" border="0" width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.ampModule.aim.helper.FundingOrganization">
                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.ampModule.aim.helper.Funding">
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
                                                                                		<td width="339">
                                                                                  			<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
                                                                                  				<digi:trn key="aim:fundingOrgId">Funding Organization Id</digi:trn>
                                                                                    		</a>
                                                                                    	</td>
                                                                                		<td width="10">:</td>
                                                                                		<td width="454">
                                                                                			<bean:write name="funding"	property="orgFundingId"/>
                                                                                		</td>
                                                                              		</tr>
                                                                             	</field:display>
                                                                             </logic:notEmpty>
                                                                             <field:display name="Funding Organization Name" feature="Funding Information">
                                                                              <tr>
                                                                                <td width="339">

                                                                                  <a title="<digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>">
                                                                               	  <digi:trn key="aim:fundOrgName">Funding Organization Name</digi:trn>
                                                                                  </a>
                                                                                  </td>
                                                                                <td width="10">:</td>
                                                                                <td width="454">
                                                                                <span class="word_break">
                                                                                  ${fundingOrganization.orgName}
                                                                                  </span>
                                                                                  </td>
                                                                              </tr>
                                                                             </field:display>
																		<logic:present name="funding" property="sourceRole">
																			<tr>
																				<td width="150">
																					<a title='<digi:trn jsFriendly="true" key="aim:orgRole">Organization Role</digi:trn>'>
																						<digi:trn key="aim:OrgRole">Organization Role</digi:trn>
																					</a>
																				</td>
																				<td width="1">:</td>
																				<td>
																					<b><digi:trn><bean:write name="funding" property="sourceRole"/></digi:trn></b>
																				</td>
																			</tr>
																		</logic:present>
																		<!-- type of assistance -->
																		<field:display name="Type Of Assistance" feature="Funding Information">
																			<logic:notEmpty name="funding" property="typeOfAssistance">
																				<tr>
                                                                                	<td width="339">
                                                                                  		<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  			<digi:trn key="aim:typeOfAssist">Type of Assistance </digi:trn>
																				  		</a>
																					</td>
                                                                                	<td width="10">:</td>
                                                                                	<td>
                                                                                		<digi:trn><bean:write name="funding" property="typeOfAssistance.value"/></digi:trn>
																					</td>
                                                                              	</tr>
                                                                              </logic:notEmpty>
																		</field:display>
																		<field:display name="Financing Instrument" feature="Funding Information">
																			<logic:notEmpty name="funding" property="financingInstrument">
																				<tr>
                                                                                	<td width="339">
                                                                                  		<a title="<digi:trn key="aim:financialInst">Financing Instrument</digi:trn>">
                                                                               	  		<digi:trn key="aim:financialInst">Financing Instrument</digi:trn>
																				  		</a>
																				  	</td>
                                                                                	<td width="10">:</td>
                                                                                	<td>
                                                                                		<digi:trn><bean:write name="funding" property="financingInstrument.value"/></digi:trn>
                                                                                	</td>
                                                                              	</tr>
                                                                              </logic:notEmpty>
                                                                            <logic:notEmpty name="funding" property="ratificationDate">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Ratification Date</digi:trn>'>
																			<digi:trn>Ratification Date</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="ratificationDate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="maturity">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Maturity</digi:trn>'>
																			<digi:trn>Maturity</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="maturity"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="interestRate">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Interest Rate</digi:trn>'>
																			<digi:trn>Interest Rate</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="interestRate"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
																<logic:notEmpty name="funding" property="gracePeriod">
																<tr>
																	<td width="150">
																		<a title='<digi:trn jsFriendly="true">Grace Period</digi:trn>'>
																			<digi:trn>Grace Period</digi:trn>
																		</a>
																	</td>
																	<td width="1">:</td>
																	<td>
																			<b><bean:write name="funding" property="gracePeriod"/></b>
																	</td>
																</tr>
																</logic:notEmpty>
                                                                           <field:display name="Credit/Donation" feature="Planning">
																				<logic:notEmpty name="funding" property="financingInstrument">
																					<tr>
                                                                                		<td width="339">
                                                                                  			<a title="<digi:trn key="aim:financialInst">Financing Instrument</digi:trn>">
                                                                                 	 			<digi:trn key="aim:credit_donation">Credit/Donation</digi:trn>
																				  			</a>
																				  		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
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
                                                                                <td>
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
                                                                                		<td width="339">
                                                                                  			<a><digi:trn>Mode of Payment</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
                                                                                    		<digi:trn><bean:write name="funding" property="modeOfPayment.value"/></digi:trn>
																						</td>
																					</tr>
                                                                                  </logic:notEmpty>
																			</field:display>
																			<field:display name="Concessionality Level" feature="Funding Information">
																				<logic:notEmpty name="funding" property="concessionalityLevel">
                                                                              		<tr>
                                                                                		<td width="339">
                                                                                  			<a><digi:trn>Concessionality Level</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
                                                                                    		<digi:trn><bean:write name="funding" property="concessionalityLevel.value"/></digi:trn>
																						</td>
																					</tr>
                                                                                  </logic:notEmpty>
																			</field:display>
																				<logic:notEmpty name="funding" property="fundingClassificationDate">
                                                                              		<tr>
                                                                                		<td width="339">
                                                                                  			<a><digi:trn>Funding Classification Date</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
                                                                                    		<digi:trn><bean:write name="funding" property="fundingClassificationDate"/></digi:trn>
																						</td>
																					</tr>
                                                                                  </logic:notEmpty>
	            																	<logic:notEmpty name="funding" property="effectiveFundingDate">
																						<tr>
																							<td width="339">
																								<a><digi:trn>Effective Funding Date</digi:trn></a>
																							</td>
																							<td width="10">:</td>
																							<td>
																								<bean:write name="funding" property="effectiveFundingDate"/>
																							</td>
																						</tr>
																					</logic:notEmpty>
																					<logic:notEmpty name="funding" property="fundingClosingDate">
																						<tr>
																							<td width="339">
																								<a><digi:trn>Funding Closing Date</digi:trn></a>
																							</td>
																							<td width="10">:</td>
																							<td>
																								<bean:write name="funding" property="fundingClosingDate"/>
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
                                                                                		<td>
                                                                                		<span class="word_break">
                                                                                			<digi:trn><bean:write name="funding" property="conditions"/></digi:trn>
                                                                                		</span>
                                                                                  		</td>
                                                                              		</tr>
																				</logic:notEmpty>
																			</field:display>

																			<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement"
																					parentModule="/Activity Form/Funding/Funding Group/Funding Item/Funding Classification">
																				<logic:notEmpty name="funding" property="title">
                                                                              		<tr>
                                                                                		<td width="339">
                                                                                  			<a><digi:trn>Agreement Title</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
                                                                                		<span class="word_break">
                                                                                			<digi:trn><bean:write name="funding" property="title"/></digi:trn>
                                                                                		</span>
                                                                                  		</td>
                                                                              		</tr>
<tr>
                                                                                		<td width="339">
                                                                                  			<a><digi:trn>Agreement Code</digi:trn></a>
																				 		</td>
                                                                                		<td width="10">:</td>
                                                                                		<td>
                                                                                		<span class="word_break">
                                                                                			<digi:trn><bean:write name="funding" property="code"/></digi:trn>
																						</span>
                                                                                  		</td>
                                                                              		</tr>
																				</logic:notEmpty>
																			</ampModule:display>
																			
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
                                                                            type="org.digijava.ampModule.aim.helper.Funding"></bean:define>
                                                                        <jsp:include page="activitypreview/previewActivityFundingCommitments.jsp" />

                                                                        <feature:display ampModule="Funding" name="Disbursement">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingDisbursement.jsp" />
                                                                        </feature:display>

                                                                        <feature:display ampModule="Funding" name="Expenditures">
                                                                        	<jsp:include page="activitypreview/previewActivityFundingExpenditures.jsp" />
                                                                        </feature:display>

																		<jsp:include page="activitypreview/previewMtefProjections.jsp" />

																		<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Arrears"
																		parentModule="/Activity Form/Funding/Funding Group/Funding Item">
                                                                       		<jsp:include page="activitypreview/previewActivityFundingArrears.jsp" />
                                                                        </ampModule:display>

                                                                        <feature:display ampModule="Funding" name="Undisbursed Balance">
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
                        <feature:display name="Planned Commitments" ampModule="Measures">
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedCommitments">
	                        	<tr>
	                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase;">
	                            		<digi:trn key='aim:totalplannedcommittment'> TOTAL PLANNED COMMITMENTS</digi:trn>:
	                            	</td>
	                            	<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
										<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedCommitments" /></span>
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
                            	<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
                            		<b><span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalMtefProjections" /></span>
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
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalCommitments" /></span>
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
	                              		&nbsp;
	                         		</td>
                        		</tr>
                        	</logic:notEmpty>
						<c:if test="${aimEditActivityForm.funding.showPipeline}">
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalPipelineCommitments">
								<tr>
	                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
										<digi:trn key='aim:totalpipelinecommittment'> TOTAL PIPELINE COMMITMENTS </digi:trn>:
									</td>
	                            	<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
										<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPipelineCommitments" /></span>
										<bean:write name="aimEditActivityForm" property="currCode" />
										&nbsp;
									</td>
	                        	</tr>
	                        </logic:notEmpty>
						</c:if>
                        <feature:display ampModule="Funding" name="Disbursement">
							<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedDisbursements">
                       			<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key='aim:totalplanneddisbursement'>TOTAL PLANNED DISBURSEMENT</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedDisbursements" /></span>
		                                <bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
                            		</td>
                        		</tr>
                        	</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalDisbursements">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000"><digi:trn key='aim:totalActualdisbursement'>TOTAL ACTUAL DISBURSEMENT </digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalDisbursements" /></span>
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
                           			</td>
                        		</tr>
                        	</logic:notEmpty>
                        </feature:display>
                        <feature:display ampModule="Funding" name="Expenditures">
							<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedExpenditures">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalActualExpenditures">TOTAL PLANNED EXPENDITURES</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedExpenditures" /></span>
		                                <bean:write name="aimEditActivityForm" property="currCode" />
			                      		&nbsp;
                            		</td>
                        		</tr>
                        	</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalExpenditures">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalplannedExpenditures">TOTAL ACTUAL EXPENDITURES</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
	                                	<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalExpenditures" /></span>
	                                	<bean:write name="aimEditActivityForm" property="currCode" />
		                          		&nbsp;
		                    		</td>
                        		</tr>
                        	</logic:notEmpty>
                        </feature:display>
                        <feature:display ampModule="Funding" name="Disbursement Orders">
                        <tr>
                            <td bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase;">
                        <digi:trn key='aim:totalActualDisbursementOrder'>
                                    <a title='<digi:trn jsFriendly="true" key="aim:FundRelease"> Release of funds to,
                                or the purchase of goods or services for a recipient; by
                                extension, the amount thus spent. Disbursements record the actual
                                international transfer of financial resources, or of goods or
                                services valued at the cost to the donor</digi:trn>'>TOTAL ACTUAL DISBURSMENT ORDERS </a></digi:trn>:
                        </td>
                          <td nowrap="nowrap" bgcolor="#eeeeee"
                                style="border-top: 1px solid #000000; text-transform: uppercase;">
                                 <c:if test="${not empty aimEditActivityForm.funding.totalActualDisbursementsOrders}">
	                                <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalActualDisbursementsOrders" /></span>	
	                                <bean:write name="aimEditActivityForm" property="currCode" />
	                             </c:if>&nbsp;
	                       </td>
                      	</tr>
                        </feature:display>
                        <ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Arrears"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
							<logic:notEmpty name="aimEditActivityForm" property="funding.totalPlannedArrears">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalActualArrears">TOTAL PLANNED ARREARS</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000">
		                                <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalPlannedArrears" /></span>
		                                <span class="word_break bold">
		                                	<bean:write name="aimEditActivityForm" property="currName" />
		                                </span>
			                      		&nbsp;
                            		</td>
                        		</tr>
                        	</logic:notEmpty>
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.totalArrears">
                        		<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:totalplannedArrears">TOTAL ACTUAL ARREARS</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000">
	                                	<span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.totalArrears" /></span>
	                                	<span class="word_break bold">
	                                		<bean:write name="aimEditActivityForm" property="currName" />
	                                	</span>
		                          		&nbsp;
		                    		</td>
                        		</tr>
                        	</logic:notEmpty>
                        </ampModule:display>
                        
                        <feature:display ampModule="Funding" name="Undisbursed Balance">
                        	<logic:notEmpty name="aimEditActivityForm" property="funding.unDisbursementsBalance">
                      			<tr>
                            		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn key="aim:undisbursedBalance">UNDISBURSED BALANCE</digi:trn>:</td>
                            		<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
		                                <span dir="ltr"><bean:write name="aimEditActivityForm" property="funding.unDisbursementsBalance" /></span>
		                                <bean:write name="aimEditActivityForm" property="currCode" />
		                         		&nbsp;
		                  			</td>
                        		</tr>
                        	</logic:notEmpty>
                        </feature:display>

                        <logic:notEmpty name="aimEditActivityForm" property="funding.deliveryRate">
							<tr>
                            	<td bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn>Delivery rate</digi:trn>: </td>
								<td nowrap="nowrap" bgcolor="#eeeeee" style="border-top: 1px solid #000000; font-weight: bold;">
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
									</ampModule:display>
								  </logic:present>

                                    <ampModule:display name="/Activity Form/Aid Effectivenes" parentModule="/Activity Form">
                                        <logic:notEmpty name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                                            <tr>
                                                <td vAlign="top"><b><digi:trn>Aid Effectivenes</digi:trn></b></td>
                                                <td>
                                                    <logic:iterate id="option" name="aimEditActivityForm" property="selectedEffectivenessIndicatorOptions">
                                                        <ampModule:display name="/Activity Form/Aid Effectivenes/${option.indicator.ampIndicatorName}"
                                                            parentModule="/Activity Form/Aid Effectivenes">
                                                            <span class="word_break bold">${fn:escapeXml(option.indicator.ampIndicatorName)}</span> -
                                                            <span class="word_break">${fn:escapeXml(option.ampIndicatorOptionName)}</span>
                                                            <br/>
                                                        </ampModule:display>
                                                    </logic:iterate>
                                                </td>
                                            </tr>
                                        </logic:notEmpty>
                                    </ampModule:display>

                                    <ampModule:display name="/Activity Form/Regional Funding" parentModule="/Activity Form">
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
																				<digi:trn key="aim:commitments">Commitments</digi:trn>																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.commitments}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:commitments:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName.value}"/>
																								</digi:trn>
																																															</td>
																							<td width="100" bgcolor="#ffffff">

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
																				<digi:trn key="aim:disbursements">Disbursements</digi:trn>
																				</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.disbursements}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																									<digi:trn key="aim:disbursements:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName.value}"/>
																								</digi:trn>																						</td>
																							<td width="100" bgcolor="#ffffff">

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
																				<digi:trn key="aim:expenditures">Expenditures</digi:trn>
																			</td>
																			<td bgcolor="#ffffff">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																					<c:forEach var="fd" items="${regFunds.expenditures}">
																						<tr>
																							<td width="50" bgcolor="#ffffff">
																								<digi:trn key="aim:expenditures:${fd.adjustmentTypeNameTrimmed}">
																										<c:out value="${fd.adjustmentTypeName.value}"/>
																								</digi:trn>
																							</td>
																							<td width="100" bgcolor="#ffffff">

																								<c:out value="${fd.transactionAmount}"/>																							</td>
																							<td bgcolor="#ffffff">
																								<c:out value="${fd.currencyCode}"/>																							</td>
																							<td bgcolor="#ffffff" width="70">
																								<c:out value="${fd.transactionDate}"/>																							</td>
																							<td bgcolor="#ffffff">
																							</td>																							</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</td>
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
									</ampModule:display>

									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
                                    <ampModule:display name="/Activity Form/Components" parentModule="/Activity Form">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:components">Components</digi:trn>
										  </b>
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td><b>
															<span class="word_break bold">
																<c:out value="${comp.title}"/>
															</span>
															</b></td></tr>

															<tr><td>
																<i>
																<digi:trn key="aim:description">Description</digi:trn> :</i>
																<span class="word_break">
																	<c:out value="${comp.description}"/>
																</span>
															</td></tr>

															<tr><td bgcolor="#f4f4f2">
																<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
															</td></tr>
															<ampModule:display
																	name="/Activity Form/Components/Component/Components Commitments"
																	parentModule="/Activity Form/Components/Component">
																<c:if test="${!empty comp.commitments}">
																	<tr>
																		<td bgcolor="#ffffff">
																			<table width="100%" cellSpacing="1"
																				   cellPadding="0"
																				   class="box-border-nopadding">
																				<tr>
																					<td valign="top" width="100"
																						bgcolor="#ffffff">
																						<digi:trn key="aim:commitments">
																							Commitments</digi:trn>
																					</td>
																					<td bgcolor="#ffffff">
																						<table width="100%"
																							   cellSpacing="1"
																							   cellPadding="1"
																							   bgcolor="#eeeeee">
																							<c:forEach var="fd"
																									   items="${comp.commitments}">
																								<tr>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Commitments"
																											parentModule="/Activity Form/Components/Component">
																										<td width="50"
																											bgcolor="#ffffff">
																											<digi:trn
																													key="aim:commitments:${fd.adjustmentTypeNameTrimmed}">
																												<c:out value="${fd.adjustmentTypeName.value}"/>
																											</digi:trn>
																										</td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount"
																											parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																										<td
																											width="100"
																											bgcolor="#ffffff">
																											<c:out value="${fd.transactionAmount}"/>
																										</td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency"
																											parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																										<td bgcolor="#ffffff">
																											<c:out value="${fd.currencyCode}"/>
																										</td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date"
																											parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																										<td bgcolor="#ffffff"
																											width="70">
																											<c:out value="${fd.transactionDate}"/>
																										</td>
																									</ampModule:display>
																								</tr>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"
																										parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Organisation</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentOrganisation"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentOrganisation.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Second Responsible Organization"
																										parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Component Second Responsible Organization</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentSecondResponsibleOrganization"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Description"
																										parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
																									<tr>
																										<td width="50"
																											bgcolor="#ffffff">
																											<b><digi:trn>Description</digi:trn></b>
																										</td>
																										<td colspan="3"
																											bgcolor="white">
																								<span class="word_break">
																									<c:out value="${fd.componentTransactionDescription}"/>
																								</span>
																										</td>
																									</tr>
																								</ampModule:display>
																							</c:forEach>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</c:if>
															</ampModule:display>
															<ampModule:display
																	name="/Activity Form/Components/Component/Components Disbursements"
																	parentModule="/Activity Form/Components/Component">
																<c:if test="${!empty comp.disbursements}">
																	<tr>
																		<td bgcolor="#ffffff">
																			<table width="100%" cellSpacing="1"
																				   cellPadding="1"
																				   class="box-border-nopadding">
																				<tr>
																					<td valign="top" width="100"
																						bgcolor="#ffffff">
																						<digi:trn
																								key="aim:disbursements">
																							Disbursements</digi:trn></td>
																					<td bgcolor="#ffffff">
																						<table width="100%"
																							   cellSpacing="1"
																							   cellPadding="1"
																							   bgcolor="#eeeeee">
																							<c:forEach var="fd"
																									   items="${comp.disbursements}">
																								<tr>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Disbursements"
																											parentModule="/Activity Form/Components/Component">
																										<td width="50"
																											bgcolor="#ffffff">
																											<digi:trn
																													key="aim:disbursements:${fd.adjustmentTypeNameTrimmed}">
																												<c:out value="${fd.adjustmentTypeName.value}"/>
																											</digi:trn>
																										</td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount"
																											parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																										<td
																											width="100"
																											bgcolor="#ffffff">
																											<c:out value="${fd.transactionAmount}"/></td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency"
																											parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																										<td bgcolor="#ffffff">
																											<c:out value="${fd.currencyCode}"/></td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date"
																											parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																										<td bgcolor="#ffffff"
																											width="70">
																											<c:out value="${fd.transactionDate}"/></td>
																									</ampModule:display>
																								</tr>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Organization"
																										parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Organisation</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentOrganisation"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentOrganisation.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Second Responsible Organization"
																										parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Component Second Responsible Organization</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentSecondResponsibleOrganization"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Description"
																										parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
																									<tr>
																										<td width="50"
																											bgcolor="#ffffff">
																											<b><digi:trn>Description</digi:trn></b>
																										</td>
																										<td colspan="3"
																											bgcolor="white">
																								<span class="word_break">
																									<c:out value="${fd.componentTransactionDescription}"/>
																								</span>
																										</td>
																									</tr>
																								</ampModule:display>
																							</c:forEach>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</c:if>
															</ampModule:display>
															<ampModule:display
																	name="/Activity Form/Components/Component/Components Expenditures"
																	parentModule="/Activity Form/Components/Component">
																<c:if test="${!empty comp.expenditures}">
																	<tr>
																		<td bgcolor="#ffffff">
																			<table width="100%" cellSpacing="1"
																				   cellPadding="1"
																				   class="box-border-nopadding">
																				<tr>
																					<td valign="top" width="100"
																						bgcolor="#ffffff">
																						<digi:trn
																								key="aim:expenditures">
																							Expenditures</digi:trn>
																					</td>
																					<td bgcolor="#ffffff">
																						<table width="100%"
																							   cellSpacing="1"
																							   cellPadding="1"
																							   bgcolor="#eeeeee">
																							<c:forEach var="fd"
																									   items="${comp.expenditures}">
																								<tr bgcolor="#ffffff">
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Expeditures"
																											parentModule="/Activity Form/Components/Component">
																										<td width="50">
																											<digi:trn
																													key="aim:expenditures:${fd.adjustmentTypeNameTrimmed}">
																												<c:out value="${fd.adjustmentTypeName.value}"/>
																											</digi:trn></td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount"
																											parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																										<td>

																											<c:out value="${fd.transactionAmount}"/></td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency"
																											parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																										<td>
																											<c:out value="${fd.currencyCode}"/></td>
																									</ampModule:display>
																									<ampModule:display
																											name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date"
																											parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																										<td width="70">
																											<c:out value="${fd.transactionDate}"/></td>
																									</ampModule:display>
																								</tr>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Organization"
																										parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Organisation</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentOrganisation"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentOrganisation.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Second Responsible Organization"
																										parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																									<tr bgcolor="#ffffff">
																										<td>
																											<b><digi:trn>Component Second Responsible Organization</digi:trn></b>
																										</td>
																										<td colspan="3">
																											<logic:notEmpty
																													property="componentSecondResponsibleOrganization"
																													name="fd">
																									<span class="word_break">
																										<c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
																									</span>
																											</logic:notEmpty>
																										</td>
																									</tr>
																								</ampModule:display>
																								<ampModule:display
																										name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Description"
																										parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
																									<tr>
																										<td width="50"
																											bgcolor="#ffffff">
																											<b><digi:trn>Description</digi:trn></b>
																										</td>
																										<td colspan="3"
																											bgcolor="white">
																								<span class="word_break">
																									<c:out value="${fd.componentTransactionDescription}"/>
																								</span>
																										</td>
																									</tr>
																								</ampModule:display>
																							</c:forEach>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</c:if>
															</ampModule:display>
															<tr><td bgcolor="#ffffff">&nbsp;</td>
															</tr>
														</table>
													</td></tr>
													</table>
												</c:forEach>
											</c:if>
										</td>
									</tr>
								  </ampModule:display>
								  </logic:equal>

									<logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">
									<ampModule:display name="Components Resume" parentModule="PROJECT MANAGEMENT">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:components">Components</digi:trn>
										  </b>
									  </td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
												<c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
													<table width="100%" cellSpacing="1" cellPadding="1">
													<tr><td>
														<table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">

															<tr><td>
																<span class="word_break bold">
																<c:out value="${comp.title}"/>
																</span>
															</td></tr>

															<tr><td>
																<span class="word_break italic">
																<digi:trn key="aim:component_code">Component code</digi:trn> :
																</span>
																<span class="word_break">
																	<c:out value="${comp.code}"/>
																</span>
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
																			<span class="word_break">
																				<c:out value="${financeByYearInfo.key}"/>
																			</span>
																			</td>
																			<c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
																			<td bgcolor="#ffffff" align="left">
																				<table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
																				<fmt:timeZone value="US/Eastern">
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>
																							</td>
																							<td width="100" bgcolor="#ffffff">
																								<fmt:formatNumber type="number" pattern="0.00" value="${financeByYearInfoMap['MontoProgramado']}" />
																							</td>
																						</tr>
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>
																							</td>
																							<td width="100" bgcolor="#ffffff">
																								<fmt:formatNumber type="number" pattern="0.00" value="${financeByYearInfoMap['MontoReprogramado']}" />
																							</td>
																						</tr>
																						<tr>
																							<td bgcolor="#ffffff">
																								<digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>
																							</td>
																							<td width="100" bgcolor="#ffffff">
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
									</ampModule:display>
                                    </logic:equal>
								<ampModule:display name="/Activity Form/Issues Section" parentModule="/Activity Form">
								<ampModule:display name="/Activity Form/Issues Section/Issue" parentModule="/Activity Form/Issues Section">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:issues">Issues</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${!empty aimEditActivityForm.issues.issues}">
												<table width="100%" cellSpacing="2" cellPadding="2" border="0">
												<c:forEach var="issue" items="${aimEditActivityForm.issues.issues}">
													<tr><td valign="top"  colspan="3">
														<li class="level1">
														<span class="word_break bold">
															<c:out value="${issue.name}"/> 
															<ampModule:display name="/Activity Form/Issues Section/Issue/Date" parentModule="/Activity Form/Issues Section/Issue">
																<c:out value="${issue.issueDate}"/> 
															</ampModule:display>
														</span>
													</li>
													</td></tr>
														<ampModule:display name="/Activity Form/Issues Section/Issue/Measure" parentModule="/Activity Form/Issues Section/Issue">
														<c:if test="${!empty issue.measures}">
															<c:forEach var="measure" items="${issue.measures}">
																<tr><td></td><td colspan="2">
																	<li class="level2">
																	<span class="word_break italic">
																		<c:out value="${measure.name}"/>
																		<ampModule:display name="/Activity Form/Issues Section/Issue/Measure/Date" parentModule="/Activity Form/Issues Section/Issue/Measure">
									  										<c:out value="${measure.measureDate}"/>
									  									</ampModule:display></span>
									  								</li>
																</td></tr>
																	<ampModule:display name="/Activity Form/Issues Section/Issue/Measure/Actor" parentModule="/Activity Form/Issues Section/Issue/Measure">
																	<c:if test="${!empty measure.actors}">
																		<c:forEach var="actor" items="${measure.actors}">
																			<tr>
																			<td colspan="2"></td>
																			<td>
																				<li class="level3"><span class="word_break"><c:out value="${actor.name}"/></span></li>
																			</td></tr>
																		</c:forEach>
																	</c:if>
																	</ampModule:display>
															</c:forEach>
														</c:if>
														</ampModule:display>
												</c:forEach>
												</table>
											</c:if>
										</td>
									</tr>
									</ampModule:display>
									</ampModule:display>
                             <ampModule:display name="/Activity Form/Related Documents" parentModule="/Activity Form">
									<tr>
										<td class="field_name">
											<b>
											<digi:trn key="aim:relatedDocuments">
										    Related Documents</digi:trn>
											</b>									</td>
<td bgcolor="#ffffff">						<c:if test="${ (!empty aimEditActivityForm.documents.documents) || (!empty aimEditActivityForm.documents.crDocuments)}">
												<table width="100%" cellSpacing="0" cellPadding="0">
												 <logic:iterate name="aimEditActivityForm"  property="documents.documents"
													id="docs" type="org.digijava.ampModule.aim.helper.Documents">
													<c:if test="${docs.isFile == true}">
													<tr><td>
													 <table width="100%" class="box-border-nopadding">
													 	<tr bgcolor="#ffffff">
															<td vAlign="center">
																&nbsp;<b><c:out value="${docs.title}"/></b> -
																&nbsp;&nbsp;&nbsp;<span class="word_break italic"><c:out value="${docs.fileName}"/></span>

																<logic:notEqual name="docs" property="docDescription" value=" ">
																	<br />&nbsp;
																	<b><digi:trn key="aim:description">Description</digi:trn>:</b>
																	<span class="word_break">
																	&nbsp;<bean:write name="docs" property="docDescription" />
																	</span>
																</logic:notEqual>
																<logic:notEmpty name="docs" property="date">
																	<br />&nbsp;
																	<b><digi:trn key="aim:date">Date</digi:trn>:</b>
																	&nbsp;<c:out value="${docs.date}"/>
																</logic:notEmpty>
																<logic:notEmpty name="docs" property="docType">
																	<br />&nbsp;
																	<b><digi:trn key="aim:documentType">Document Type</digi:trn>:</b>&nbsp;
																	<span class="word_break">
																	<bean:write name="docs" property="docType"/>
																	</span>
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
																	<td vAlign="center">
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
																			<img src="/repository/contentrepository/view/images/check_out.gif" border="0"></a>
																		<logic:notEmpty name="crDoc" property="description">
																			<br />&nbsp;
																			<b><digi:trn key="aim:description">Description</digi:trn>:</b>&nbsp;
																			<span class="word_break">
																			<bean:write name="crDoc" property="description" />
																			</span>
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
                                 </ampModule:display>

								<ampModule:display name="/Activity Form/Regional Observations" parentModule="/Activity Form">
									<tr>
										<td class="field_name">
											<b><digi:trn>Regional Observations</digi:trn></b>
										</td>
										<td>
											<c:if test="${not empty aimEditActivityForm.regionalObservations.issues}">
												<logic:iterate name="aimEditActivityForm" id="regionalObs" property="regionalObservations.issues">
													<table style="width: 98%;">
														<ampModule:display name="/Activity Form/Regional Observations/Observation" parentModule="/Activity Form/Regional Observations">
															<tr >
																<td width="27%;">
																	<b><digi:trn>Observation</digi:trn>:</b>
																</td>
																<td>
															<span class="word_break">
																<b><c:out value="${regionalObs.name}"/></b>
															</span>
																</td>
															</tr>
														</ampModule:display>
														<ampModule:display name="/Activity Form/Regional Observations/Observation/Date" parentModule="/Activity Form/Regional Observations/Observation">
															<tr >
																<td>
																	<digi:trn>Observation Date</digi:trn>:
																</td>
																<td>
																	<c:out value="${regionalObs.issueDate}"/>
																</td>
															</tr>
														</ampModule:display>
														<logic:iterate name="regionalObs" id="measure" property="measures">
															<tr>
																<td>
																	<digi:trn>Measure</digi:trn>:
																</td>
																<td>
																<span class="word_break">
																	<c:out value="${measure.name}"/>
																</span>
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
																					<span class="word_break">
																						<c:out value="${actor.name}"/>
																					</span>
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
								</ampModule:display>

                                 <ampModule:display name="/Activity Form/Line Ministry Observations" parentModule="/Activity Form">
                                 	<tr>
										<td class="field_name">
											<b><digi:trn>Line Ministry Observations</digi:trn></b>
                                     	</td>
                                     	<td>
											<c:if test="${not empty aimEditActivityForm.lineMinistryObservations.issues}">
												<logic:iterate name="aimEditActivityForm" id="lineMinistryObs" property="lineMinistryObservations.issues">
													<table style="width: 98%;">
														<ampModule:display name="/Activity Form/Line Ministry Observations/Observation" parentModule="/Activity Form/Line Ministry Observations">
															<tr >
																<td width="27%;">
																	<b><digi:trn>Observation</digi:trn>:</b>
																</td>
																<td>
																<span class="word_break">
																	<b><c:out value="${lineMinistryObs.name}"/></b>
																</span>
																</td>
															</tr>
														</ampModule:display>
														<ampModule:display name="/Activity Form/Line Ministry Observations/Observation/Date" parentModule="/Activity Form/Line Ministry Observations/Observation">
															<tr >
																<td>
																	<digi:trn>Observation Date</digi:trn>:
																</td>
																<td>
																	<c:out value="${lineMinistryObs.issueDate}"/>
																</td>
															</tr>
														</ampModule:display>
														<logic:iterate name="lineMinistryObs" id="measure" property="measures">
															<tr>
																<td>
																	<digi:trn>Measure</digi:trn>:
																</td>
																<td>
																	<span class="word_break">
																		<c:out value="${measure.name}"/>
																	</span>
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
																						<span class="word_break">
																							<c:out value="${actor.name}"/>
																						</span>
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
								</ampModule:display>

								<ampModule:display name="/Activity Form/Organizations" parentModule="/Activity Form">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:relatedOrganizations">Related Organizations</digi:trn></b>
											</td>

										<td bgcolor="#ffffff">
										<ampModule:display name="/Activity Form/Organizations/Donor Organization" parentModule="/Activity Form/Organizations">
											<b><digi:trn key="aim:donororganisation">Donor Organization</digi:trn></b>
											<br/>
											<logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
												<div id="act_donor_organisation" style="display: block;">
													<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
														<tr>
															<td>
																<logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.ampModule.aim.helper.FundingOrganization">
																<ul>
																	<li>
																		<span class="word_break">
																			<bean:write name="fundingOrganization" property="orgName"/>
																		</span>
																	</li>
																</ul>
																</logic:iterate>
															</td>
														</tr>
													</table>
												</div>
											</logic:notEmpty>
										</ampModule:display>
										<ampModule:display name="/Activity Form/Organizations/Responsible Organization" parentModule="/Activity Form/Organizations">
											<b><digi:trn key="aim:responsibleOrganisation">Responsible Organization</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations"	id="respOrg" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
															<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="respOrg" property="name" />
																			<c:set var="tempOrgId" scope="page">${respOrg.ampOrgId}</c:set>
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgToInfo(${tempOrgId})" >
																			<span class="word_break">
																				(  <c:out value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}" /> )
																			</span>
																			</logic:notEmpty>
																		</li></ul>
																	</td>
																	<td width="15%">
																		<ampModule:display name="/Activity Form/Organizations/Responsible Organization/percentage" parentModule="/Activity Form/Organizations/Responsible Organization">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty>
																		</ampModule:display>
																	</td>
																</tr>
															</table>

													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty>
											<br/>
											</ampModule:display>

                                           <ampModule:display name="/Activity Form/Organizations/Executing Agency" parentModule="/Activity Form/Organizations">
											<b><digi:trn key="aim:executingAgency">Executing Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.executingAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.executingAgencies" id="execAgencies" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="execAgencies" property="name" />
																			<c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set>
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgToInfo(${tempOrgId})" >
																				( <span class="word_break">
																				 <c:out value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}" /> 
																				 </span>)
																			</logic:notEmpty>
																		</li></ul>
																	</td>
																	<td width="15%">
																	<ampModule:display name="/Activity Form/Organizations/Executing Agency/percentage" parentModule="/Activity Form/Organizations/Executing Agency">
																		<logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
																		  <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
																		</logic:notEmpty>
																	</ampModule:display>
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty>
											<br/>
											</ampModule:display>

											<ampModule:display name="/Activity Form/Organizations/Implementing Agency" parentModule="/Activity Form/Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies">
											<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b><br/>
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.impAgencies"
													id="impAgencies" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="impAgencies" property="name" />
																			<c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set>
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgToInfo(${tempOrgId})" >
																				( <span class="word_break">
																				 <c:out value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}" /> 
																				 </span>)
																			</logic:notEmpty>
																		</li></ul>
																	</td>
																	<td width="15%">
																		<ampModule:display name="/Activity Form/Organizations/Implementing Agency/percentage" parentModule="/Activity Form/Organizations/Implementing Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty>
																		</ampModule:display>
																	</td>
																</tr>
															</table>

													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</ampModule:display>

											<ampModule:display name="/Activity Form/Organizations/Beneficiary Agency" parentModule="/Activity Form/Organizations">
											<b><digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn></b><br/>

											<logic:notEmpty name="aimEditActivityForm" property="agencies.benAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
														<tr><td>
														<logic:iterate name="aimEditActivityForm" property="agencies.benAgencies"
														id="benAgency" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
															<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="benAgency" property="name" />
																		<c:set var="tempOrgId">${benAgency.ampOrgId}</c:set>
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgToInfo(${tempOrgId})" >
																				(  <span class="word_break">
																				<c:out value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}" />
																				</span> )
																			</logic:notEmpty>
																			</li></ul>
																	</td>
																	<td width="15%">
																		<ampModule:display name="/Activity Form/Organizations/Beneficiary Agency/percentage" parentModule="/Activity Form/Organizations/Beneficiary Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty>
																		</ampModule:display>
																	</td>
																</tr>
															</table>
														</logic:iterate>
														</td></tr>
													</table>
												</logic:notEmpty><br/>
											</ampModule:display>

											<ampModule:display name="/Activity Form/Organizations/Contracting Agency" parentModule="/Activity Form/Organizations">
											<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b><br/>
											<logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
												<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.conAgencies"
													id="conAgencies" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="conAgencies" property="name" />
																			<c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set>
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgToInfo(${tempOrgId})" >
																				(  <span class="word_break">
																				<c:out value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}" />
																				</span> )
																			</logic:notEmpty>
																		</li></ul>
																	</td>
																	<td width="15%">
																		<ampModule:display name="/Activity Form/Organizations/Contracting Agency/percentage" parentModule="/Activity Form/Organizations/Contracting Agency">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty>
																		</ampModule:display>
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>

											</logic:notEmpty><br/>
											</ampModule:display>
											<ampModule:display name="/Activity Form/Organizations/Sector Group" parentModule="/Activity Form/Organizations">
												<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
													<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b><br/>
														<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
																<tr><td>
																<logic:iterate name="aimEditActivityForm" property="agencies.sectGroups"
																id="sectGroup" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
																	<table width="100%">
																		<tr>
																			<td width="85%">
																				<ul><li>
																				<span class="word_break">
																					<bean:write name="sectGroup" property="name" />
																				</span>
																					<c:set var="tempOrgId">${sectGroup.ampOrgId}</c:set>
																					<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgToInfo(${tempOrgId})" >
																						(  <span class="word_break">
																						<c:out value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}" />
																						</span> )
																					</logic:notEmpty>
																				</li></ul>
																			</td>
																			<td width="15%">
																				<ampModule:display name="/Activity Form/Organizations/Sector Group/percentage" parentModule="/Activity Form/Organizations/Sector Group">
																					<logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
																					  <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
																					</logic:notEmpty>
																				</ampModule:display>
																			</td>
																		</tr>
																	</table>
																</logic:iterate>
																</td></tr>
															</table>
													<br/>
												</logic:notEmpty>
											</ampModule:display>

											<ampModule:display name="/Activity Form/Organizations/Regional Group" parentModule="/Activity Form/Organizations">
											<logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
											<b><digi:trn key="aim:regionalGroup">Regional Group</digi:trn></b><br/>
											<table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
													<tr><td>
													<logic:iterate name="aimEditActivityForm" property="agencies.regGroups"
													id="regGroup" type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
														<table width="100%">
																<tr>
																	<td width="85%">
																		<ul><li>
																			<bean:write name="regGroup" property="name" />
																			<c:set var="tempOrgId">${regGroup.ampOrgId}</c:set>
																			<logic:notEmpty property="agencies.regOrgToInfo(${tempOrgId})"  name="aimEditActivityForm">
																				(  <span class="word_break">
																				<c:out value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}" />
																				</span> )
																			</logic:notEmpty>
																		</li></ul>
																	</td>
																	<td width="15%">
																		<ampModule:display name="/Activity Form/Organizations/Regional Group/percentage" parentModule="/Activity Form/Organizations/Regional Group">
																			<logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
																			  <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
																			</logic:notEmpty>
																		</ampModule:display>
																	</td>
																</tr>
															</table>
													</logic:iterate>
													</td></tr>
												</table>
											</logic:notEmpty><br/>
											</ampModule:display>
                                           </td>
									</tr>
									</ampModule:display>

                                    <ampModule:display name="/Activity Form/Contacts" parentModule="/Activity Form">
									<ampModule:display name="/Activity Form/Contacts/Donor Contact Information" parentModule="/Activity Form/Contacts">
											<tr>
												<td width="30%" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Donor funding contact information</digi:trn>
												</td>
												<td>
													<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.donorContacts}" scope="request" />
													<jsp:include page="activitypreview/contactInformation.jsp"/>
												</td>
											</tr>
											</ampModule:display>
											<ampModule:display name="/Activity Form/Contacts/Mofed Contact Information" parentModule="/Activity Form/Contacts">
											<tr>
												<td width="30%" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>MOFED contact information</digi:trn>
												</td>
												<td>
													<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.mofedContacts}" scope="request" />
													<jsp:include page="activitypreview/contactInformation.jsp"/>
												</td>
											</tr>
											</ampModule:display>
											<ampModule:display name="/Activity Form/Contacts/Project Coordinator Contact Information" parentModule="/Activity Form/Contacts">
											<tr>
												<td width="30%" valign="top" nowrap="nowrap"class="t-name">
													<digi:trn>Project Coordinator Contact Information</digi:trn>
												</td>
												<td>
													<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.projCoordinatorContacts}" scope="request" />
													<jsp:include page="activitypreview/contactInformation.jsp"/>
												</td>
											</tr>
											</ampModule:display>
											<ampModule:display name="/Activity Form/Contacts/Sector Ministry Contact Information" parentModule="/Activity Form/Contacts">
											<tr>
												<td width="30%" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Sector Ministry Contact Information</digi:trn>
												</td>
												<td>
													<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.sectorMinistryContacts}" scope="request" />
													<jsp:include page="activitypreview/contactInformation.jsp"/>
												</td>
											</tr>
										</ampModule:display>
										<ampModule:display name="/Activity Form/Contacts/Implementing Executing Agency Contact Information" parentModule="/Activity Form/Contacts">
											<tr>
												<td width="30%" valign="top" nowrap="nowrap" class="t-name">
													<digi:trn>Implementing/Executing Agency Contact Information</digi:trn>
												</td>
												<td>
													<c:set var="contactInformation" value="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}" scope="request" />
													<jsp:include page="activitypreview/contactInformation.jsp"/>
												</td>
											</tr>
										</ampModule:display>
									</ampModule:display>
									<!-- M & E  SECTION -->
									<ampModule:display name="M & E" parentModule="MONITORING AND EVALUATING">
										<tr>
											<td class="field_name" >
												<b><digi:trn>M&E</digi:trn></b>
											</td>
											<td>
                                                <bean:define id="aimEditActivityForm" name="aimEditActivityForm" scope="page" toScope="request"/>
                                                <jsp:include page="previewIndicatosList.jsp"/>
											</td>
										</tr>
									</ampModule:display>
									<!-- END M & E  SECTION -->
							 		<field:display name="Activity Performance"  feature="Activity Dashboard">
									<tr>
										<td class="field_name">
											<b>
												<digi:trn key="aim:meActivityPerformance">Activity - Performance</digi:trn></b>
											</td>
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



                              <ampModule:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost" parentModule="/Activity Form/Funding/Overview Section">
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
                                                       <ampModule:display name="/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost" parentModule="/Activity Form/Funding/Overview Section/Proposed Project Cost">
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
																			${annualBudget.currencyCode}</td>
																		<td>${annualBudget.funDate}</td>
																	</tr>
																</c:forEach>
															</table>
													      </td>
                                                       </tr>
                                                    	</c:if>
                                                    	</ampModule:display>
                                              		</table>
                            				</c:if>
                            			</td>
									</tr>
								  </ampModule:display>
								  <ampModule:display name="/Activity Form/Funding/Overview Section/Total Number of Funding Sources"
												  parentModule="/Activity Form/Funding/Overview Section">
									<tr bgcolor="#ffffff">
										<td>
											<digi:trn>Total Number of Funding Sources</digi:trn>
										</td>
										<td bgcolor="#FFFFFF" align="left" width="150">
											<c:if test="${aimEditActivityForm.identification.fundingSourcesNumber!=null}">
												<span class="word_break">${aimEditActivityForm.identification.fundingSourcesNumber}</span>
											</c:if>
										</td>
									</tr>
								  </ampModule:display>
								  <ampModule:display name="/Activity Form/Funding/Overview Section/Revised Project Cost" parentModule="/Activity Form/Funding/Overview Section">
								  	<tr>
										<td class="field_name">
											<b><digi:trn key="aim:revisedPrjectCost">Revised Project Cost</digi:trn></b>
									  	</td>
									  	<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.funding.revProjCost != null}">
												<table cellspacing="1" cellPadding="3" bgcolor="#aaaaaa" width="100%">
													<tr bgcolor="#ffffff">
														<td><digi:trn key="aim:cost">Cost</digi:trn></td>
                                                        <td bgcolor="#FFFFFF" align="left" >
                                                          <c:if test="${aimEditActivityForm.funding.revProjCost.funAmount != null}">
																 	<FONT color=blue>*</FONT> ${aimEditActivityForm.funding.revProjCost.funAmount}
														  </c:if>&nbsp;
														  <c:if test="${aimEditActivityForm.funding.revProjCost.currencyCode != null}">
																${aimEditActivityForm.funding.revProjCost.currencyCode}
														  </c:if>
														</td>
												    </tr>
												    <tr bgcolor="#ffffff">
														<td><digi:trn key="aim:proposedcompletiondate">Date</digi:trn></td>
                                                        <td bgcolor="#FFFFFF" align="left" width="150">
                                                        	<c:if test="${aimEditActivityForm.funding.revProjCost.funDate!=null}">
                                                        		${aimEditActivityForm.funding.revProjCost.funDate}
                                                         	</c:if>
                                                        </td>
                                                    </tr>
												</table>
											</c:if>
								  </ampModule:display>

								  <ampModule:display name="/Activity Form/Budget Structure/Budget Structure" parentModule="/Activity Form/Budget Structure">

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
																	<span class="word_break bold">${budgetStructure.budgetStructureName}</span>
																</td>
																<td bgcolor="#FFFFFF" align="left" width="150">
																	<span class="word_break
																	bold">${budgetStructure.budgetStructurePercentage} %</span>
																</td>
															</tr>
														</c:forEach>
                                              		</table>
                            				</c:if>										</td>
									</tr>
								  </ampModule:display>

									<!-- GPI -->
									<ampModule:display name="/Activity Form/GPI" parentModule="/Activity Form">
										<bean:define id="gpiSurvey" name="gpiSurveys" scope="request" toScope="page"
													 type="java.util.Collection"/>
										<c:if test="${not empty gpiSurvey}">
											<tr>
												<td vAlign="top" class="field_name">
													<b><digi:trn>GPI</digi:trn></b>
												</td>
												<td vAlign="top">
													<c:set var="currentIndicatorName" value=""/>
													<logic:iterate name="gpiSurveys" id="gpiSurvey"
																   type="java.util.Collection" indexId="gpiId">
														<logic:iterate name="gpiSurvey" id="gpiresponse"
																	   type="org.digijava.ampModule.aim.dbentity.AmpGPISurveyResponse">
															<table width="100%" cellSpacing="2" cellPadding="1"
																   style="font-size:11px;" border="0">
																<c:if test="${!currentIndicatorName.equals(gpiresponse.ampQuestionId.ampIndicatorId.name)}">
																	<c:set var="currentIndicatorName"
																		   value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
																	<tr>
																		<td bgcolor="#eeeeee"
																			style="text-transform: uppercase;">
																			<c:set var="indicatorName"
																				   value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
																			<span class="word_break bold"><digi:trn>${indicatorName}</digi:trn></span>
																		</td>
																	</tr>
																</c:if>
																<tr>
																	<td width=85%>
																		<c:set var="questionText"
																			   value="${gpiresponse.ampQuestionId.questionText}"/>
																		<c:set var="ampTypeName"
																			   value="${gpiresponse.ampQuestionId.ampTypeId.name}"/>
																		<span class="word_break bold"><digi:trn>${questionText}</digi:trn></span>
																		<c:set var="responseText"
																			   value="${gpiresponse.response}"/>
																		<lu>
																			<li>
																				<c:if test='${"yes-no".equals(ampTypeName) &&
																						!"".equals(responseText) && responseText != null}'>
																					<span class="word_break bold"><digi:trn>${responseText}</digi:trn></span>
																				</c:if>
																				<c:if test='${!"yes-no".equals(ampTypeName)}'>
																					<span class="word_break bold">${responseText}</span>
																				</c:if>
																			</li>
																		</lu>
																	</td>
																</tr>
															</table>
														</logic:iterate>
													</logic:iterate>
												</td>
											</tr>
										</c:if>
									</ampModule:display>
									<!-- END GPI -->


									<field:display name="Activity Created By" feature="Identification">
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:activityCreatedBy">
										    Activity created by</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
											<c:out value="${aimEditActivityForm.identification.actAthFirstName}"/>
											<c:out value="${aimEditActivityForm.identification.actAthLastName}"/>
										</td>
									</tr>
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

										<ampModule:display name="/Activity Form/Identification/Activity Last Updated by" parentModule="/Activity Form/Identification">
											<logic:notEmpty name="aimEditActivityForm" property="identification.modifiedBy">
												<tr>
													<td class="field_name" >
														<b>
															<digi:trn key="aim:activityLastUpdatedBy">Activity Last Updated by</digi:trn>
														</b>
													</td>
													<td bgcolor="#ffffff">
														<c:out value="${aimEditActivityForm.identification.modifiedBy.user.firstNames}"/>
														<c:out value="${aimEditActivityForm.identification.modifiedBy.user.lastName}"/>
													</td>
												</tr>
											</logic:notEmpty>
										</ampModule:display>

										<ampModule:display name="/Activity Form/Identification/Activity Updated On" parentModule="/Activity Form/Identification">
											<logic:notEmpty name="aimEditActivityForm" property="identification.updatedDate">
												<tr>
													<td class="field_name" >
														<b>
															<digi:trn key="aim:activityUpdatedOn">Activity updated on</digi:trn>
														</b>
													</td>
													<td bgcolor="#ffffff">
														<c:out value="${aimEditActivityForm.identification.updatedDate}"/>
													</td>
												</tr>
											</logic:notEmpty>
										</ampModule:display>
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:createdInWorkspace">Created in workspace</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.team !=null}">
												<c:out value="${aimEditActivityForm.identification.team.name}"/> -
												<digi:trn>
													<c:out value="${aimEditActivityForm.identification.team.accessType}"/>
												</digi:trn>
											</c:if>
										</td>
									</tr>
										<logic:notEmpty name="aimEditActivityForm" property="identification.team">
											<field:display name="Data Team Leader" feature="Identification">
												<tr>
													<td class="field_name" >
														<b>
															<digi:trn key="aim:workspaceManager">
																Workspace manager</digi:trn>
														</b>
													</td>

													<td bgcolor="#ffffff">
											<span class="word_break">
												<c:out value="${aimEditActivityForm.identification.team.teamLead.user.firstNames}"/>
											</span>
														<span class="word_break">
												<c:out value="${aimEditActivityForm.identification.team.teamLead.user.lastName}"/> -
											</span>
														<span class="word_break">
												<c:out value="${aimEditActivityForm.identification.team.teamLead.user.email}"/>
											</span>
													</td>
												</tr>
											</field:display>
										</logic:notEmpty>
									<tr>
										<td class="field_name" >
											<b>
											<digi:trn key="aim:computation">Computation</digi:trn>
											</b>
										</td>
										<td bgcolor="#ffffff">
											<c:if test="${aimEditActivityForm.identification.team.computation == 'true'}">
												<digi:trn key="aim:yes">Yes</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.identification.team.computation == 'false'}">
												<digi:trn key="aim:no">No</digi:trn>
											</c:if>
										</td>
									</tr>
									</field:display>

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
						<span class="word_break bold">
							${structure.title}
						</span>
					</td>
				</tr>
				
				<ampModule:display
					name="/Activity Form/Structures/Structure Type"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td width="15%"><digi:trn key="trn:type">Type</digi:trn></td>
						<td align="left"><span class="word_break bold"> ${structure.type.name} </span]></td>
					</tr>
				</ampModule:display>
				<ampModule:display
					name="/Activity Form/Structures/Structure Title"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td><digi:trn key="trn:title">Title</digi:trn></td>
						<td align="left"> <span class="word_break bold">${structure.title} </span></td>
					</tr>
				</ampModule:display>
				<ampModule:display
					name="/Activity Form/Structures/Structure Description"
					parentModule="/Activity Form/Structures">
					<tr bgcolor="#f0f0f0">
						<td><digi:trn key="trn:description">Description</digi:trn></td>
						<td align="left"><span class="word_break bold">${structure.description} </span></td>
					</tr>
				</ampModule:display>
				<ampModule:display
					name="/Activity Form/Structures/Structure Latitude"
					parentModule="/Activity Form/Structures">
					<c:if test="${not empty structure.latitude}">
						<tr bgcolor="#f0f0f0">
							<td><digi:trn key="trn:latitude">Latitude</digi:trn></td>
							<td align="left"> <b> ${structure.latitude} </b></td>
						</tr>
					</c:if>
				</ampModule:display>
				<ampModule:display
					name="/Activity Form/Structures/Structure Longitude"
					parentModule="/Activity Form/Structures">
					<c:if test="${not empty structure.longitude}">
						<tr bgcolor="#f0f0f0">
							<td><digi:trn key="trn:longitude">Longitude</digi:trn></td>
							<td align="left"><b> ${structure.longitude} </b></td>
						</tr>
					</c:if>
				</ampModule:display>
				<c:if test="${not empty structure.coordinates}">
					<tr>
						<td bgcolor="#f0f0f0" valign="top"><digi:trn>Coordinates</digi:trn></td>
						<td bgcolor="#f0f0f0">
							<table>
								<logic:iterate id="coordinate" name="structure" property="coordinates"
											   type="org.digijava.ampModule.aim.dbentity.AmpStructureCoordinate">
									<tr>
										<td><b> ${coordinate.latitude}</b></td>
										<td><b> ${coordinate.longitude}</b></td>
									</tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
				</c:if>
			
	</table>
	<br />
	<hr>
	</logic:iterate>
	</td> </tr> </logic:notEmpty> </table>
</div>
</c:if>
<c:if test="${aimEditActivityForm==null}">
		Invalid activity id
</c:if>

