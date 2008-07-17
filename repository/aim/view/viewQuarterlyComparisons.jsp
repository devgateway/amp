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

<digi:context name="digiContext" property="context" />



<digi:errors/>

<digi:instance property="aimQuarterlyComparisonsForm" />



<logic:equal name="aimQuarterlyComparisonsForm" property="sessionExpired" value="true">

	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />

</logic:equal>



<logic:equal name="aimQuarterlyComparisonsForm" property="sessionExpired" value="false">

<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">

	<bean:write name="aimQuarterlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">

	<bean:write name="aimQuarterlyComparisonsForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>



<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlFinancialOverview}" property="ampActivityId">

	<bean:write name="aimQuarterlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="ampFundingId">

	<bean:write name="aimQuarterlyComparisonsForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">

	<bean:write name="aimQuarterlyComparisonsForm" property="tabIndex"/>

</c:set>



<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">

	<bean:write name="aimQuarterlyComparisonsForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>

<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>





<digi:form action="/viewQuarterlyComparisonsFilter.do" name="aimQuarterlyComparisonsForm"

type="org.digijava.module.aim.form.QuarterlyComparisonsForm" method="post">



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

			  			</digi:link> |

			<c:set var="translation">

				<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

			</c:set>

			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  			</digi:link> |
                                                    <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                                <c:set target="${urlSubTabs}" property="transactionType" value="4"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursementsOrders">Click here to view Disbursement Orders</digi:trn>

			</c:set>

			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

			  					<digi:trn key="aim:disbursementOrders">DISBURSEMENTS ORDERS</digi:trn>

			  			</digi:link> |
                                                </field:display>

			  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>

			</c:set>

			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

			  					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			  			</digi:link> |

			  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

		</c:set>
					<feature:display module="Funding" name="Expenditures">
		  				<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

		  					<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

		  				</digi:link> |
					</feature:display>

								<span>

		  					<digi:trn key="aim:all">ALL</digi:trn>

		  				</span>
</div>
				</TD></TR>

				<TR bgColor=#f4f4f2>

            	<TD align=left>

						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">

							<TR>

								<TD align="left">

						<SPAN class=crumb>

		              	<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>

							<c:set target="${urlFinancingBreakdown}" property="ampActivityId">

								<bean:write name="aimQuarterlyComparisonsForm" property="ampActivityId"/>

							</c:set>

							<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>

								<c:set var="translation">

									<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>

								</c:set>

								<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >

									<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>

								</digi:link> &gt;

								<digi:trn key="aim:quarterlyAll">Quarterly All</digi:trn>


						</SPAN>

								</TD>

								<TD align="right">&nbsp;

									

								</TD>

							</TR>

						</TABLE>

					</TD>

				</TR>



				<TR bgColor=#f4f4f2>


							<TR bgcolor="#ffffff">

								<TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">

             					<TABLE cellSpacing=2 cellPadding=0 border=0 bgColor="#ffffff" width="100%" vAlign="top" align="left"

									bgColor="#ffffff">

										<TR><TD bgColor="#ffffff">

											<logic:equal name="aimQuarterlyComparisonsForm" property="goButtonPresent" value="true">



											<TABLE cellSpacing=1 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimQuarterlyComparisonsForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Currency:</STRONG></TD>

															<TD>

		                           	 				<html:select property="currency" styleClass="dr-menu">

	   			                        	 			<html:optionsCollection name="aimQuarterlyComparisonsForm" property="currencies"

																	value="currencyCode" label="currencyName"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimQuarterlyComparisonsForm" property="calendarPresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Calendar Type:</STRONG></TD>

															<TD>

																<html:select property="fiscalCalId" styleClass="dr-menu">

																	<html:optionsCollection name="aimQuarterlyComparisonsForm" property="fiscalYears"

																	value="ampFiscalCalId" label="name"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimQuarterlyComparisonsForm" property="yearRangePresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Year&nbsp;&nbsp;</STRONG></TD>

															<TD><STRONG>From:</STRONG></TD>

															<TD>

							                      		<html:select property="fromYear" styleClass="dr-menu">

									                      	<html:optionsCollection name="aimQuarterlyComparisonsForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

															<TD><STRONG>To:</STRONG></TD>

															<TD>

						   	                     	<html:select property="toYear" styleClass="dr-menu">

																	<html:optionsCollection name="aimQuarterlyComparisonsForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

												<TD>

													<html:submit value="GO" styleClass="dr-menu"/>

			                        	</TD>

											</TR>

											</TABLE>

											</logic:equal>

										</TD></TR>

										<TR><TD>




											<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

	   	                     		<TR>

         	      	           			<TD width="100%" valign="middle">

            	      		        			<SPAN class="note">

				                      				<logic:equal name="aimQuarterlyComparisonsForm" property="transactionType" value="1">

                     									<digi:trn key="aim:totalCommitted">Total Committed</digi:trn>:

								     								<bean:write name="aimQuarterlyComparisonsForm" property="totalCommitted"/> USD

							     								<digi:trn key="aim:totalRemaining">Total Remaining</digi:trn>:

				     											<bean:write name="aimQuarterlyComparisonsForm" property="totalRemaining"/> USD

				     										</logic:equal>
                                                                                                                	     							<logic:equal name="aimQuarterlyComparisonsForm" property="transactionType" value="2">

         		          								<digi:trn key="aim:totalDisbOrdered">Total Ordered</digi:trn>:

				   				  								<bean:write name="aimQuarterlyComparisonsForm" property="totalDisbOrdered"/> USD




							     							</logic:equal>

							     							<logic:equal name="aimQuarterlyComparisonsForm" property="transactionType" value="2">

         		          								<digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn>:

				   				  								<bean:write name="aimQuarterlyComparisonsForm" property="totalDisbursed"/> USD

				     											<digi:trn key="aim:totalUnExpended">Total Un-Expended</digi:trn>:

				     											<bean:write name="aimQuarterlyComparisonsForm" property="totalUnExpended"/> USD

							     							</logic:equal>

         			                 			</SPAN>

                  		        			</TD>

												</TR>

											</TABLE>


										</TD></TR>

										<TR><TD>

						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>




				                          		<table width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding" id="dataTable">

            				                		<tr bgcolor="#999999">

			                              			<td bgcolor="#999999">

         			                     				<div align="center" style="font-weight:bold;color:black;">

                  			            					<digi:trn key="aim:year">Year</digi:trn>

                           			   				</div>

			                              			</td>

         			                     			<td bgcolor="#999999">

                  			            				<div align="center" style="font-weight:bold;color:black;">

                           									<digi:trn key="aim:quarter">Quarter</digi:trn>

                              							</div>

			                              			</td>

         			                     			<td bgcolor="#999999">

                  			            				<div align="center" style="font-weight:bold;color:black;">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualCommitments">Actual Commitments</digi:trn>

                               							</div>

			                              			</td>
                                                    
                                                     <feature:display module="Funding" name="Disbursement Orders">
                                                    
                                                                            <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                                                        <td bgcolor="#999999">

                  			            				<div align="center" style="font-weight:bold;color:black;">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualDisbOrdres">Actual Disbursements Orders</digi:trn>

                               							</div>

			                              				</td>
                                                  	</field:display>

												 </feature:display>
                  			            			<td bgcolor="#999999">

         			         		        				<div align="center" style="font-weight:bold;color:black;">

																		<FONT color="blue">*</FONT>

                              							<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>

                              							</div>

			                              			</td>

         			                     			<td bgcolor="#999999">

                  			            				<div align="center" style="font-weight:bold;color:black;">

																		<FONT color="blue">*</FONT>

                           		   						<digi:trn key="aim:actualDisbursements">Actual Disbursements</digi:trn>

                              							</div>

			                              			</td>
										<feature:display module="Funding" name="Expenditures">
         			                     			<td bgcolor="#999999">

                  			            				<div align="center" style="font-weight:bold;color:black;">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualExpenditures">Actual Expenditures</digi:trn>

                               							</div>

                              						</td>
											</feature:display>
				                            		</tr>

            				                		<logic:empty name="aimQuarterlyComparisonsForm" property="quarterlyComparisons" >

			               			         		<tr valign="top">

			                        			  			<td colspan="6" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>

			                          					</tr>

							                        </logic:empty>

			   				                     <logic:notEmpty name="aimQuarterlyComparisonsForm" property="quarterlyComparisons" >

                        				    			<logic:iterate name="aimQuarterlyComparisonsForm" property="quarterlyComparisons"

				                            			id="quarterlyComparison" type="org.digijava.module.aim.helper.QuarterlyComparison">

																	<tr valign="top">

			                  					      		<td>

								      		                 		<logic:equal name="quarterlyComparison" property="fiscalYear" value="0">

				   					      	                 		NA

				                          							</logic:equal>

										                          	<logic:notEqual  name="quarterlyComparison" property="fiscalYear" value="0">

																				<bean:write name="quarterlyComparison" property="fiscalYear" />

																			</logic:notEqual>

																		</td>

									                           <td>

			      	   		                   				<logic:equal name="quarterlyComparison" property="fiscalQuarter" value="0">

				      	                    							NA

										                          	</logic:equal>

				            						              	<logic:notEqual  name="quarterlyComparison" property="fiscalQuarter" value="0">

																				<bean:write name="quarterlyComparison" property="fiscalQuarter" />

																			</logic:notEqual>

																		</td>

																		<td>

																			<div align="right">

																				<bean:write name="quarterlyComparison" property="actualCommitment" />
																			</div>

																		</td>
                                                                                                                                              
                                                                                                                                              <feature:display module="Funding" name="Disbursement Orders">
                                                                                                                                                <td>
                                                                                                                                                <field:display name="Actual Disbursement Orders" feature="Disbursement Orders">

																			<div align="right">

																				<bean:write name="quarterlyComparison" property="actualDisbOrder" />
																			</div>
                                                                                                                                                            </field:display >


																		</td>
</feature:display>


																		<td>

																			<div align="right">

																				<bean:write name="quarterlyComparison" property="plannedDisbursement" />
																			</div>

																		</td>

																		<td>

																			<div align="right">

																				<bean:write name="quarterlyComparison" property="actualDisbursement" />
																			</div>

																		</td>
	<feature:display module="Funding" name="Expenditures">
																		<td>

																			<div align="right">

																				<bean:write name="quarterlyComparison" property="actualExpenditure" />
																			</div>

																		</td>
	</feature:display>
																	</tr>
                                                                                                                                      											</logic:iterate>
                                                                                                                                                                                                                            <tr valign="top">

						                            	<td colspan="2">

						                            		<span class="note">

																		<FONT color="blue">*</FONT>

			         				                   		<digi:trn key="aim:total">Total</digi:trn>

			                     			       		</span>

			                            				</td>

					                              	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimQuarterlyComparisonsForm" property="totalActualCommitment" />

																		</span>

																	</div>

															  </td>

   <feature:display name="Disbursement Orders" module="Funding"> 	<td>
                                                                                                                                       

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimQuarterlyComparisonsForm" property="totalDisbOrder" />

																		</span>

																	</div>
                                                                                                                                       

																</td>

   </feature:display>


			      		                        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimQuarterlyComparisonsForm" property="totalPlannedDisbursement" />

																		</span>

																	</div>

															  </td>

			                     			        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimQuarterlyComparisonsForm" property="totalActualDisbursement" />

																		</span>

																	</div>

															  </td>


			      		                        	<feature:display module="Funding" name="Expenditures">
                                                    <td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimQuarterlyComparisonsForm" property="totalActualExpenditure" />

																		</span>

																	</div>

													  </td></tr>
</feature:display>
			      			                      

			               			    

					

															</logic:notEmpty>

		                       					</table>


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

																	<bean:write name="aimQuarterlyComparisonsForm" property="ampActivityId"/>

																</c:set>

																<c:set target="${urlShowQuarterly}" property="ampFundingId">

																	<bean:write name="aimQuarterlyComparisonsForm" property="ampFundingId"/>

																</c:set>

																<c:set target="${urlShowQuarterly}" property="tabIndex" value="1"/>

							<c:set var="translation">

								<digi:trn key="aim:clickToViewYearlyComparisons">Click here to view Yearly Comparisons</digi:trn>

							</c:set>

							                        	<digi:link href="/viewYearlyComparisons.do" name="urlShowQuarterly" title="${translation}" >

				      	  										<STRONG>
																	<digi:trn key="aim:showYearly">Show Yearly</digi:trn>
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




