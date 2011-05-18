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

<%@ taglib uri="/taglib/aim" prefix="aim" %>

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

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=3&surveyFlag=true&activityId=" + id;
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

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
	  var urlToGo = "<%=addUrl%>~pageId=2~activityId=" + id;
	    document.location.href = urlToGo;
}

</script>


<digi:errors/>

<digi:instance property="aimYearlyComparisonsForm" />

<digi:context name="digiContext" property="context"/>



<logic:equal name="aimYearlyComparisonsForm" property="sessionExpired" value="true">

	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp"  />

</logic:equal>



<logic:equal name="aimYearlyComparisonsForm" property="sessionExpired" value="false">



<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">

	<bean:write name="aimYearlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">

	<bean:write name="aimYearlyComparisonsForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlSubTabs}" property="tabIndex" >
	<bean:write name="aimYearlyComparisonsForm" property="tabIndex"/>
</c:set>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<c:set target="${urlSubTabs}" property="currency"  >
	<bean:write name="aimYearlyComparisonsForm" property="currency"/>
</c:set>



<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlFinancialOverview}" property="ampActivityId">

	<bean:write name="aimYearlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="ampFundingId">

	<bean:write name="aimYearlyComparisonsForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">

	<bean:write name="aimYearlyComparisonsForm" property="tabIndex"/>

</c:set>



<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">

	<bean:write name="aimYearlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex"  >
	<bean:write name="aimYearlyComparisonsForm" property="tabIndex"/>
</c:set>


<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>



<digi:form action="/yearlyComparisonsFilter.do" name="aimYearlyComparisonsForm"

type="org.digijava.module.aim.form.YearlyComparisonsForm" method="get" styleId="myForm">

<html:hidden property="ampActivityId" />

<html:hidden property="ampFundingId" />

<html:hidden property="transactionType" />

<html:hidden property="tabIndex" />


<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">

<TR>

	<TD vAlign="top" align="center">

		<!-- contents -->



			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">

			<TR><TD bgcolor="#f4f4f4">



			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>

				<TR><TD>

                <div id="subtabsFinancial">

					<c:set var="translation">

						<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>

					</c:set>

					<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" title="${translation}" >

              		<digi:trn key="aim:overview">OVERVIEW</digi:trn>

					</digi:link>|

<feature:display module="Funding" name="Commitments">
<c:set var="translation">

	<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

</c:set>

              	<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  			<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  		</digi:link>|
</feature:display>
                                        <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                        <c:set target="${urlSubTabs}" property="transactionType" value="4"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewDisbursementOrderss">Click here to view Disbursement Orders</digi:trn>

		</c:set>

		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

		  				<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>

		  			</digi:link>|
                                        	</field:display>
<feature:display module="Funding" name="Disbursement">
		  		<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>

		</c:set>

		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

		  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

		  			</digi:link>|
</feature:display>

		  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

		</c:set>
		<feature:display module="Funding" name="Expenditures">
					  			
		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

		  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

		  			</digi:link>|
		</feature:display>

              	<span>

              		<digi:trn key="aim:all">ALL</digi:trn>

              	</span>
</div>
				</TD></TR>

				<TR bgColor=#f4f4f2>

            	<TD align=left>
						<!-- 
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">

							<TR>

								<TD align="left">

						<SPAN class=crumb>

							<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>

							<c:set target="${urlFinancingBreakdown}" property="ampActivityId">

								<bean:write name="aimYearlyComparisonsForm" property="ampActivityId"/>

							</c:set>

							<c:set target="${urlFinancingBreakdown}" property="tabIndex"  >
									<bean:write name="aimYearlyComparisonsForm" property="tabIndex"/>
							</c:set>


							<c:set var="translation">

								<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>

							</c:set>

							<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >

								<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>

							</digi:link>

                  	&gt; <digi:trn key="aim:yearlyAll">Yearly All</digi:trn>

						</SPAN>

								</TD>

								<TD align="right">&nbsp;

									

								</TD>

							</TR>

						</TABLE>
						-->
					</TD>

				</TR>

				<TR bgColor=#f4f4f2>

					<TD vAlign="top" align="center" width="100%" bgColor="#f4f4f2">



						<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center bgColor="#f4f4f2" border=0>

							<TR bgcolor="#ffffff">

								<TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">

             					<TABLE cellSpacing=2 cellPadding=0 border=0 bgColor="#ffffff" width="100%" vAlign="top" align="left"

									bgColor="#ffffff">

										<TR><TD bgColor="#ffffff">

			                        <logic:equal name="aimYearlyComparisonsForm" property="goButtonPresent" value="true">



											<TABLE cellSpacing=1 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimYearlyComparisonsForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Currency:</STRONG></TD>

															<TD>

		                           	 				<html:select property="currency" styleClass="dr-menu">

	   			                        	 			<html:optionsCollection name="aimYearlyComparisonsForm" property="currencies"

																	value="currencyCode" label="currencyName"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimYearlyComparisonsForm" property="calendarPresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Calendar Type:</STRONG></TD>

															<TD>

																<html:select property="fiscalCalId" styleClass="dr-menu">

																	<html:optionsCollection name="aimYearlyComparisonsForm" property="fiscalYears"

																	value="ampFiscalCalId" label="name"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimYearlyComparisonsForm" property="yearRangePresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Year&nbsp;&nbsp;</STRONG></TD>

															<TD><STRONG>From:</STRONG></TD>

															<TD>

							                      		<html:select property="fromYear" styleClass="dr-menu">

									                      	<html:optionsCollection name="aimYearlyComparisonsForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

															<TD><STRONG>To:</STRONG></TD>

															<TD>

						   	                     	<html:select property="toYear" styleClass="dr-menu">

																	<html:optionsCollection name="aimYearlyComparisonsForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

												<TD>

													<input type="button" styleClass="dr-menu" onclick="javascript:go();" value="<digi:trn>Go</digi:trn>"/>

			                        	</TD>

											</TR>

											</TABLE>

											</logic:equal>

										</TD></TR>

										<TR><TD>

						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>



				                     			<table width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding" id="dataTable">

            			                			<tr bgcolor="#999999" >

                     		         				<td width="6%" bgcolor="#999999" style="color:black;font-weight:bold;">

                           		   					<div align="center">

                              								<digi:trn key="aim:year">Year</digi:trn>

                           							  </div>

		                              				</td>

					                              	<td width="13%" bgcolor="#999999" style="color:black;font-weight:bold;">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualCommitments">Actual Commitments</digi:trn>

																	</div>

					                             	  </td>

                                                                <feature:display name="Disbursement Orders" module="Funding">
                                                                                <td width="13%" bgcolor="#999999" style="color:black;font-weight:bold;">


																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:disbursementsOrders">Disbursement Orders</digi:trn>

																	</div>
                                                                                                                                      

						                            	</td>


    </feature:display>
						                             	<td width="13%" bgcolor="#999999" style="color:black;font-weight:bold;">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>

																	</div>

						                            	</td>

					                              	<td width="13%" bgcolor="#999999" style="color:black;font-weight:bold;">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualDisbursements">Actual Disbursements</digi:trn>

																	</div>

			      		                        	</td>

			            		                  
			            		                  <feature:display module="Funding" name="Expenditures">
			            		                  	<td width="24%" bgcolor="#999999" style="color:black;font-weight:bold;">

			                  			              	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualExpenditures">Actual Expenditures</digi:trn>

													  </div>

			                     		         	</td>
			                     		         	
												</feature:display>
                            						</tr>

			                            			<logic:empty name="aimYearlyComparisonsForm" property="yearlyComparisons">

         			                   			<tr valign="top">

			         			                   	<td align="center" colspan="6">

			                  						  		<span class="note">
                  						  		  <digi:trn key="aim:noRecords">No records</digi:trn>!!</span>						                            	</td></tr>

			                            			</logic:empty>

			                            			<logic:notEmpty name="aimYearlyComparisonsForm" property="yearlyComparisons">

                                                                        <logic:iterate name="aimYearlyComparisonsForm" property="yearlyComparisons"

                                                                        id="yearlyComparisons" type="org.digijava.module.aim.helper.YearlyComparison">

                                                                        <tr valign="top">

						                            	<td>

						                            		<logic:equal name="yearlyComparisons" property="fiscalYear" value="0">

				      			                    			NA

				               			           		</logic:equal>

				                        			  		<logic:notEqual  name="yearlyComparisons" property="fiscalYear" value="0">

																		<bean:write name="yearlyComparisons" property="fiscalYear" />

														  </logic:notEqual>

			                            				</td>

				   	                           	<td>

																	<div align="right">

																		<aim:formatNumber  value="${yearlyComparisons.actualCommitment}" />

																	</div>

																</td>



                                                                                                                                                                                                                                                        <feature:display name="Disbursement Orders" module="Funding"> <td>


																	<div align="right">

																		<aim:formatNumber  value="${yearlyComparisons.disbOrders}" />

																	</div>
                                                                                                                                  

																</td>
</feature:display>


			                           		   	<td>

																	<div align="right">

																		<aim:formatNumber  value="${yearlyComparisons.plannedDisbursement}" />

																	</div>

																</td>

			      	                        		<td>

																	<div align="right">
																		<aim:formatNumber  value="${yearlyComparisons.actualDisbursement}" />
																	</div>

																</td>
  <feature:display module="Funding" name="Expenditures">
				                  	            	<td>

																	<div align="right">
																		<aim:formatNumber  value="${yearlyComparisons.actualExpenditure}" />
													</div>

											  </td>

  </feature:display>
			               	           			</tr>

															</logic:iterate>

															<tr valign="top">

						                            	<td>

						                            		<span class="note">

																		<FONT color="blue">*</FONT>

			         				                   		<digi:trn key="aim:total">Total</digi:trn>

			                     			       		</span>

			                            				</td>

					                              	<td>

																	<div align="right">

																		<span class="note">

																		<aim:formatNumber  value="${aimYearlyComparisonsForm.totalActualCommitment}" />

																		</span>

																	</div>

															  </td>

   <feature:display name="Disbursement Orders" module="Funding"> 	<td>
                                                                                                                                       

																	<div align="right">

																		<span class="note">

																			<aim:formatNumber  value="${aimYearlyComparisonsForm.totalDisbOrder}" />

																		</span>

																	</div>
                                                                                                                                       

																</td>

   </feature:display>


			      		                        	<td>

																	<div align="right">

																		<span class="note">

																			<aim:formatNumber  value="${aimYearlyComparisonsForm.totalPlannedDisbursement}" />

																		</span>

																	</div>

															  </td>

			                     			        	<td>

																	<div align="right">

																		<span class="note">
																		

																		<aim:formatNumber  value="${aimYearlyComparisonsForm.totalActualDisbursement}" />

																		</span>

																	</div>

															  </td>


			      		                        	<feature:display module="Funding" name="Expenditures">
                                                    <td>

																	<div align="right">

																		<span class="note">

																			<aim:formatNumber  value="${aimYearlyComparisonsForm.totalActualExpenditure}" />

																		</span>

																	</div>

													  </td>
</feature:display>
			      			                      </tr>

			               			             </logic:notEmpty>

                   		         		   </table>

	                   		                    <p>&nbsp;</p>



												</TD></TR>

												<TR><TD>

												</TD></TR>

												<TR><TD align="right">

													<TABLE cellspacing="0" cellpadding="0" vAlign="top">

														<TR>

															<TD width="15">

																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

															</TD>

															<TD>

																<jsp:useBean id="urlShowQuarterly" type="java.util.Map" class="java.util.HashMap"/>

																<c:set target="${urlShowQuarterly}" property="ampActivityId">

																	<bean:write name="aimYearlyComparisonsForm" property="ampActivityId"/>

																</c:set>

																<c:set target="${urlShowQuarterly}" property="ampFundingId">

																	<bean:write name="aimYearlyComparisonsForm" property="ampFundingId"/>

																</c:set>

																<c:set target="${urlShowQuarterly}" property="tabIndex"  >
																	<bean:write name="aimYearlyComparisonsForm" property="tabIndex"/>
																</c:set>
																<c:set target="${urlShowQuarterly}" property="currency"  >
																	<bean:write name="aimYearlyComparisonsForm" property="currency"/>
																</c:set>



							<c:set var="translation">

								<digi:trn key="aim:clickToViewQuarterlyComparisons">Click here to view Quarterly Comparisons</digi:trn>

							</c:set>

							                        	<digi:link href="/viewQuarterlyComparisons.do" name="urlShowQuarterly" title="${translation}" >

			      	  											<STRONG>
																	<digi:trn key="aim:showQuartery">Show Quarterly</digi:trn>
																	</STRONG>

						   		     						</digi:link>

															</TD>

														</TR>
                                                                                                                 
                                                                                                                 
                                                                                                                 <TR>

															<TD width="15">

																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

															</TD>

															<TD>

																<c:set var="translation">

								<digi:trn key="aim:clickToViewMonthlyComparisons">Click here to view Monthly Comparisons</digi:trn>

							</c:set>

							                        	<digi:link href="/viewMonthlyComparisons.do" name="urlShowQuarterly" title="${translation}" >

			      	  											<STRONG>
																	<digi:trn key="aim:showMonthly">Show Monthly</digi:trn>
																	</STRONG>

						   		     						</digi:link>

															</TD>

														</TR>


													</TABLE>

												</TD></TR>

												<TR><TD>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
													<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">

													All the amounts are in thousands (000)</digi:trn>
													</FONT>
</gs:test>
												</TD></TR>

											</TABLE>

										</TD></TR>

									</TABLE>

								</TD>

							</TR>

						</TABLE>

					</TD>

				</TR>

			</TABLE>



			</TD></TR>



			</TABLE>

		<!-- end -->

	</TD>

</TR>

<TR><TD>&nbsp;</TD></TR>

</TABLE>
<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>



</digi:form>

</logic:equal>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}

function go() {
	document.getElementById("myForm").action = "/aim/viewYearlyComparisons.do";
	document.getElementById("myForm").submit();
}
</script>



