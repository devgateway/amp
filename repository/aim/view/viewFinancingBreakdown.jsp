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

<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<script type="text/javascript">

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimFinancingBreakdownForm.action = "<%=addUrl%>?pageId=1&action=edit&step=3&surveyFlag=true&activityId=" + id;
	document.aimFinancingBreakdownForm.target = "_self";
   document.aimFinancingBreakdownForm.submit();
}

function previewLogframe(id)
{
    <digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);
}


function preview(id)
{

	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimFinancingBreakdownForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimFinancingBreakdownForm.target = "_self";
   document.aimFinancingBreakdownForm.submit();
}

</script>


<digi:errors/>

<digi:instance property="aimFinancingBreakdownForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancingBreakdownForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimFinancingBreakdownForm" property="sessionExpired" value="false">

<digi:form action="/viewFinancingBreakdownFilter.do" name="aimFinancingBreakdownForm"
type="org.digijava.module.aim.form.FinancingBreakdownForm" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="ampFundingId" />
<html:hidden property="tabIndex" />


<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      			<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>
													<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
														<bean:write name="aimFinancingBreakdownForm" property="ampActivityId"/>
													</c:set>
													<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="2"/>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
													</c:set>
													<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown"
													styleClass="comment" title="${translation}" >
													<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:actOverview">Overview</digi:trn>
												</SPAN>
											</TD>
											<TD align="right">&nbsp;
												
											</TD>


											<TD align="right">
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Preview Activity" module="Previews">
													<field:display feature="Preview Activity" name="Preview Button">
														<input type="button" value="<digi:trn key='btn:preview'>Preview</digi:trn>" class="dr-menu"
															onclick="preview(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Edit Activity" module="Previews">
													<field:display feature="Edit Activity" name="Edit Activity Button">
														<c:if test="${aimChannelOverviewForm.buttonText != 'validate'}">              
	                                                        <c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}">    
	                                                                <input type="button" value="<digi:trn key='btn:edit'>Edit</digi:trn>" class="dr-menu"
															onclick="fnEditProject(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
													&nbsp;      
	                                                        </c:if>                                                                       
                                                        </c:if>														
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Logframe" module="Previews">
													<field:display name="Logframe Preview Button" feature="Logframe" >
														<input type="button" value="Preview Logframe" class="dr-menu"	onclick="previewLogframe(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Project Fiche" module="Previews">
													<field:display name="Project Fiche Button" feature="Project Fiche" >
														<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
															onclick='projectFiche(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)'>
													</field:display>
												</feature:display>
											</module:display>

											</TD>


										</TR>
									</TABLE>
								</TD>
							</TR>
							<feature:display name="Calendar" module="Calendar">
							<logic:equal name="aimFinancingBreakdownForm" property="goButtonPresent" value="true">
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="750">
										<TABLE cellPadding=2 cellSpacing=0 vAlign="top" align="left" border=0>
											<TR>
												<logic:equal name="aimFinancingBreakdownForm" property="currencyPresent" value="true">
												<TD align="center">
													<STRONG>
														<digi:trn key="aim:currency">Currency</digi:trn>
													</STRONG>
												</TD>
												</logic:equal>
												<logic:equal name="aimFinancingBreakdownForm" property="calendarPresent" value="true">
												<TD align="center">
													<STRONG>
														<digi:trn key="aim:calendarType">Calendar Type</digi:trn>
													</STRONG>
												</TD>
												</logic:equal>
			               				<logic:equal name="aimFinancingBreakdownForm" property="yearRangePresent" value="true">
													<TD align="center">
														<STRONG>
             											<digi:trn key="aim:year">Year</digi:trn>
														</STRONG>
													</TD>
												</logic:equal>
												<TD width="5">&nbsp;
													
												</TD>
											</TR>
											<TR>
			               			<logic:equal name="aimFinancingBreakdownForm" property="currencyPresent" value="true">
												<TD>
                         					<html:select property="currency" styleClass="dr-menu">
                       							<html:optionsCollection name="aimFinancingBreakdownForm" property="currencies" value="currencyCode"
														label="currencyName"/>
													</html:select>
												</TD>
											</logic:equal>
											<logic:equal name="aimFinancingBreakdownForm" property="calendarPresent" value="true">
												<TD>
													<html:select property="fiscalCalId" styleClass="dr-menu">
														<html:optionsCollection name="aimFinancingBreakdownForm"
														property="fiscalYears" value="ampFiscalCalId" label="name"/>
													</html:select>
												</TD>
											</logic:equal>
			              	 			<logic:equal name="aimFinancingBreakdownForm" property="yearRangePresent" value="true">
												<TD>
   	                 						<STRONG><digi:trn key="aim:financeprg:from">From</digi:trn></STRONG>&nbsp;
								      				<html:select property="fromYear" styleClass="dr-menu">
								      					<html:optionsCollection name="aimFinancingBreakdownForm"
															property="years" value="year" label="year"/>
														</html:select>&nbsp;&nbsp;
								      			<STRONG><digi:trn key="aim:financeprg:to">To</digi:trn></STRONG>&nbsp;
														<html:select property="toYear" styleClass="dr-menu">
															<html:optionsCollection name="aimFinancingBreakdownForm"
															property="years" value="year" label="year"/>
														</html:select>
												</TD>
											</logic:equal>
												<TD width="5">
													<input type="submit" value="<digi:trn key="aim:go">GO</digi:trn>" styleClass="dr-menu"/>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</logic:equal>
							</feature:display>
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="100%">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" height="17">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                        	<TR bgcolor="#F4F4F2" height="17">
                          	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
															<digi:trn key="aim:financingOverviewOfActivity">Financing Overview of Activity</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif"
											height=17 width=17>
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD width="100%" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" >
 
		    	                    	<field:display name="Funding Organization Id" feature="Funding Information">
		    	                    		<TD><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>
		    	                    	</field:display>
		    	                    	<field:display name="Funding Organization" feature="Funding Information">
						                    <TD width="20"><digi:trn key="aim:organization">Organization</digi:trn></TD>
						                </field:display>

						                <feature:display module="Funding" name="MTEF Projections">
											<field:display feature="MTEF Projections" name="MTEFProjections">
											<td><digi:trn key="aim:financialProgress:totalProjections_projection">Total Projections</digi:trn></td>
											</field:display>
										</feature:display>

						                <field:display name="Total Committed" feature="Funding Information">
											<TD><digi:trn key="aim:totalCommitmentsActual">Total Commitments (Actual)</digi:trn></TD>
										</field:display>
                                                                              <field:display name="Total Ordered" feature="Disbursement Orders">
			                	         	<TD><digi:trn key="aim:totalOrdered">Total Ordered</digi:trn></TD>
			                	        </field:display>
										<field:display name="Total Disbursed" feature="Funding Information">
			                	         	<TD><digi:trn key="aim:totalDisbursementsActual">Total Disbursements (Actual)</digi:trn></TD>
			                	        </field:display>
			                	        <field:display name="Undisbursed Funds" feature="Funding Information">
											<TD><digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn></TD>
										</field:display>

										<feature:display module="Funding" name="Expenditures">
                                   
                                            <field:display name="Total Expended" feature="Funding Information">
                                                <TD><digi:trn key="aim:totalExpendituresActual">Total Expenditures (Actual)</digi:trn></TD>
                                            </field:display>
                                   
                                        </feature:display>

	    	                    	 	<feature:display module="Funding" name="Expenditures">
		    	                    	 	<field:display name="Unexpended Funds" feature="Funding Information">
												<TD><digi:trn key="aim:unExpendedFunds">Unexpended a Funds</digi:trn></TD>
											</field:display>
										</feature:display>

									</TR>
													<logic:empty name="aimFinancingBreakdownForm" property="financingBreakdown">
			                    	<TR valign="top">
															<TD align="center" colspan="7"><span class="note"> <digi:trn key="aim:noRecords">No records !! </digi:trn></span></TD>
			                     </TR>
			                    </logic:empty>
			                    <logic:notEmpty name="aimFinancingBreakdownForm" property="financingBreakdown">
								<logic:iterate name="aimFinancingBreakdownForm" property="financingBreakdown" id="breakdown"
			  	                   type="org.digijava.module.aim.helper.FinancingBreakdown">
															<TR valign="top" bgcolor="#f4f4f2">
												<field:display name="Funding Organization Id" feature="Funding Information">
					    	           			<TD>
						               				<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlFinancialOverview}" property="ampActivityId">

																		<bean:write name="aimFinancingBreakdownForm" property="ampActivityId"/>
																	</c:set>
																	<c:set target="${urlFinancialOverview}" property="ampFundingId">
																		<bean:write name="breakdown" property="ampFundingId"/>
																	</c:set>
																	<c:set target="${urlFinancialOverview}" property="tabIndex">
																		<bean:write name="aimFinancingBreakdownForm" property="tabIndex"/>
																	</c:set>
																	<c:set var="translation">
																		<digi:trn key="aim:clickToViewFinancialOverview">
																		Click here to view Financial Overview</digi:trn>
																	</c:set>
						                  							<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview"
																		title="${translation}" >
																		<c:if test="${!empty breakdown.financingId}">
																			<bean:write name="breakdown" property="financingId" />
																		</c:if>
																		<c:if test="${empty breakdown.financingId}">
																			<c:set var="translation">
																				<digi:trn key="im:unspecified">Unspecified</digi:trn>
																			</c:set>
																			<c:out value="${translation}"/>
																		</c:if>
																	</digi:link>
																</TD>
												</field:display>
											<bean:define id="breakdown" name="breakdown" type="org.digijava.module.aim.helper.FinancingBreakdown" toScope="request" />

											<field:display name="Funding Organization" feature="Funding Information">
						                  		<TD><jsp:include page="previewFinancingOrganizationPopup.jsp"/></TD>
						                  	</field:display>
<feature:display module="Funding" name="MTEF Projections">
											<field:display feature="MTEF Projections" name="MTEFProjections">
												<TD align="right"><bean:write name="breakdown" property="totalProjection"/></TD>
											</field:display>
</feature:display>
						                  	<field:display name="Total Committed" feature="Funding Information">
							                  <TD align="right"><bean:write name="breakdown" property="totalCommitted"/></TD>
							                </field:display>
                                                                          <field:display name="Total Ordered" feature="Disbursement Orders">
							                  <TD align="right"><bean:write name="breakdown" property="totalDisbOrdered"/></TD>
							                </field:display>
							                <field:display name="Total Disbursed" feature="Funding Information">
							                  <TD align="right"><bean:write name="breakdown" property="totalDisbursed"/></TD>
							                </field:display>
							                <field:display name="Undisbursed Funds" feature="Funding Information">
								                <TD align="right"><bean:write name="breakdown" property="unDisbursed"/></TD>
								            </field:display>
                                            
                                            <feature:display module="Funding" name="Expenditures">
                                                <field:display name="Total Expended" feature="Funding Information">
                                                  <TD align="right"><bean:write name="breakdown" property="totalExpended"/></TD>
                                                </field:display>
					      			        </feature:display>
                                            
					      			        <feature:display module="Funding" name="Expenditures">
							      			        <field:display name="Unexpended Funds" feature="Funding Information">
							            		      <TD align="right"><bean:write name="breakdown" property="unExpended"/></TD>
							            		    </field:display>
					            		    </feature:display>
											
															</TR>
														</logic:iterate>
													</logic:notEmpty>
				                  <TR valign="top" class="note">
														<TD><digi:trn key="aim:total">Total</digi:trn></TD>
				                    <TD></TD>
				                    				
                                                    <feature:display module="Funding" name="MTEF Projections">
                                                    	<field:display feature="MTEF Projections" name="MTEFProjections"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalProjections"/></TD>
				                    					</field:display>
                                                        </feature:display>
														<field:display name="Total Committed" feature="Funding Information"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalCommitted"/></TD>
														</field:display>
                                                                                                                <field:display name="Total Ordered" feature="Disbursement Orders"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalDisbOrdered"/></TD>
                                                                                                                </field:display>
														<field:display name="Total Disbursed" feature="Funding Information"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalDisbursed"/></TD>
														</field:display>
														<field:display name="Undisbursed Funds" feature="Funding Information"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalUnDisbursed"/></TD>
                                                        
                                                        </field:display>
												
                                                <feature:display module="Funding" name="Expenditures">
                                              
                                                		<field:display name="Total Expended" feature="Funding Information"><TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalExpended"/></TD>
                                                		</field:display>
                                                        
                                                        </feature:display>
														
<feature:display module="Funding" name="Expenditures">
    <field:display name="Unexpended Funds" feature="Funding Information">
        <TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalUnExpended"/></TD>
    </field:display>
</feature:display>
</TR>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD>
												<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">
													All the amounts are in thousands (000)</digi:trn>
													<bean:write name="aimFinancingBreakdownForm" property="selectedCurrency"/>
												</FONT><br>
												<FONT color=blue>*
													<digi:trn key="aim:UndisbursedAndUnexpectedBalancesAre">
														Undisbursed Balance = Actual Commitments - Actual Disbursements. Unexpended Balance = Actual Disbursements - Actual Expenditures.
													</digi:trn>													
												</FONT>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>
</digi:form>
</logic:equal>



