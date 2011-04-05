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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>


<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm" %>

<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>

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
<!--AMP-9995 
<html:hidden property="currency" />
 -->


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
											<!-- 
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
												 -->
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
											</TR>
											<TR>
			               			<logic:equal name="aimFinancingBreakdownForm" property="currencyPresent" value="true">
												<TD>
												<logic:notEmpty name="aimFinancingBreakdownForm" property="currencies">
                         						<html:select property="currency" styleClass="dr-menu">
                         							<html:optionsCollection name="aimFinancingBreakdownForm" property="currencies" value="currencyCode"
														label="currencyName"/>
													</html:select>
												</logic:notEmpty>
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
<!--
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
-->
										<TR>
											<TD width="100%" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="100%"  style="border-collapse:collapse;" bordercolor='silver'  border="1" cellpadding="4" cellspacing="1" id="dataTable">
               					  <TR bgcolor="#999999" >
    									<TD bgcolor="#999999" style="color:black;font-weight:bold;">&nbsp;</TD>
		    	                    	<field:display name="Funding Organization Id" feature="Funding Information">
		    	                    		<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>
		    	                    	</field:display>
		    	                    	<field:display name="Funding Organization" feature="Funding Information">
						                    <TD width="20" bgcolor="#999999" style="color:black"><digi:trn key="aim:organization">Organization</digi:trn></TD>
						                </field:display>

						                <feature:display module="Funding" name="MTEF Projections">
											<field:display feature="MTEF Projections" name="MTEFProjections">
											<td bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:financialProgress:totalProjections_projection">Total Projections</digi:trn></td>
											</field:display>
										</feature:display>

						                <field:display name="Total Committed" feature="Commitments">
											<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:totalCommitmentsActual">Total Commitments (Actual)</digi:trn></TD>
										</field:display>
                                        <field:display name="Total Ordered" feature="Disbursement Orders">
			                	         	<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:totalOrdered">Total Ordered</digi:trn></TD>
			                	        </field:display>
										<field:display name="Total Disbursed" feature="Disbursement">
			                	         	<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:totalDisbursementsActual">Total Disbursements (Actual)</digi:trn></TD>
			                	        </field:display>
			                	        <field:display name="Undisbursed Funds" feature="Funding Information">
											<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn></TD>
										</field:display>

										<feature:display module="Funding" name="Expenditures">
                                   
                                            <field:display name="Total Expended" feature="Expenditures">
                                                <TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:totalExpendituresActual">Total Expenditures (Actual)</digi:trn></TD>
                                            </field:display>
                                   
                                        </feature:display>

	    	                    	 	<feature:display module="Funding" name="Expenditures">
		    	                    	 	<field:display name="Unexpended Funds" feature="Funding Information">
												<TD bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:unExpendedFunds">Unexpended a Funds</digi:trn></TD>
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
															<TD>&nbsp;
																		<field:display name="Financial Progress More Info Link"  feature="Funding Information">
																		<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview"
																		title="${translation}" >
																			<digi:trn>More info...</digi:trn>
																		</digi:link>
																		</field:display>
															</TD>
												<field:display name="Funding Organization Id" feature="Funding Information">
					    	           			<TD>
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
												<TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="totalProjection"/></TD>
									  </field:display>
</feature:display>
						                  	<field:display name="Total Committed" feature="Commitments">
							                  <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="totalCommitted"/></TD>
							                </field:display>
                                                                          <field:display name="Total Ordered" feature="Disbursement Orders">
							                  <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="totalDisbOrdered"/></TD>
							                </field:display>
							                <field:display name="Total Disbursed" feature="Disbursement">
							                  <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="totalDisbursed"/></TD>
							                </field:display>
							                <field:display name="Undisbursed Funds" feature="Funding Information">
								                <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="unDisbursed"/></TD>
								            </field:display>
                                            
                                            <feature:display module="Funding" name="Expenditures">
                                                <field:display name="Total Expended" feature="Expenditures">
                                                  <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="totalExpended"/></TD>
                                              </field:display>
					      			        </feature:display>
                                            
					      			        <feature:display module="Funding" name="Expenditures">
							      			        <field:display name="Unexpended Funds" feature="Funding Information">
							            		      <TD align="center" nowrap="nowrap"><bean:write name="breakdown" property="unExpended"/></TD>
					            		      </field:display>
					            		    </feature:display>
											
															</TR>
														</logic:iterate>
													</logic:notEmpty>
				                  <TR valign="top">
														<TD class="note" style="background-color:#FFFFFF;"><digi:trn key="aim:total">Total</digi:trn></TD>
				                     	<field:display name="Funding Organization Id" feature="Funding Information">
		    	                    <TD class="note" style="background-color:#FFFFFF;"></TD>
		    	                    	</field:display>
		    	                    	<field:display name="Funding Organization" feature="Funding Information">
						                <TD class="note" style="background-color:#FFFFFF;"></TD>
						                </field:display>
				                
				                                        <feature:display module="Funding" name="MTEF Projections">
                                                    	<field:display feature="MTEF Projections" name="MTEFProjections"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalProjections"/></TD>
				                    					</field:display>
                                                        </feature:display>
														<field:display name="Total Committed" feature="Commitments"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalCommitted"/></TD>
														</field:display>
                                                                                                                <field:display name="Total Ordered" feature="Disbursement Orders"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalDisbOrdered"/></TD>
                                                                                                                </field:display>
														<field:display name="Total Disbursed" feature="Disbursement"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalDisbursed"/></TD>
														</field:display>
														<field:display name="Undisbursed Funds" feature="Funding Information"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalUnDisbursed"/></TD>
                                                        
                                                        </field:display>
												
                                                <feature:display module="Funding" name="Expenditures">
                                              
                                                		<field:display name="Total Expended" feature="Expenditures"><TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalExpended"/></TD>
                                                		</field:display>
                                                        
                                                        </feature:display>
														
<feature:display module="Funding" name="Expenditures">
    <field:display name="Unexpended Funds" feature="Funding Information">
        <TD align="center" class="note" style="background-color:#FFFFFF;"><bean:write name="aimFinancingBreakdownForm" property="totalUnExpended"/></TD>
    </field:display>
</feature:display>
</TR>
												</TABLE>
										  </TD>
										</TR>
										<TR>
											<TD>
												<FONT color=blue>*
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
													<digi:trn key="aim:allTheAmountsInThousands">
													All the amounts are in thousands (000)</digi:trn>
</gs:test>
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



<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>
