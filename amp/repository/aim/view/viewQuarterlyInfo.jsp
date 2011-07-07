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


<digi:errors/>

<digi:instance property="aimQuarterlyInfoForm" />

<digi:context name="digiContext" property="context"/>



<logic:equal name="aimQuarterlyInfoForm" property="sessionExpired" value="true">

	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp"  />

</logic:equal>



<logic:equal name="aimQuarterlyInfoForm" property="sessionExpired" value="false">



<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">

	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">

	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlSubTabs}" property="tabIndex" value="2"/>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<c:set target="${urlSubTabs}" property="currency"  >
	<bean:write name="aimQuarterlyInfoForm" property="currency"/>
</c:set>


<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlFinancialOverview}" property="ampActivityId">

	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="ampFundingId">

	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">

	<bean:write name="aimQuarterlyInfoForm" property="tabIndex"/>

</c:set>



<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlAll}" property="ampActivityId">

	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlAll}" property="ampFundingId">

	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlAll}" property="tabIndex" value="2"/>
<c:set target="${urlAll}" property="currency"  >
	<bean:write name="aimQuarterlyInfoForm" property="currency"/>
</c:set>


<jsp:useBean id="urlQuarterlyGrouping" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlQuarterlyGrouping}" property="ampActivityId">

	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlQuarterlyGrouping}" property="ampFundingId">

	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlQuarterlyGrouping}" property="tabIndex" value="2"/>

<c:set target="${urlQuarterlyGrouping}" property="transactionType">

	<bean:write name="aimQuarterlyInfoForm" property="transactionType"/>

</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">

	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex" value="2"/>

<c:set target="${urlDiscrepancy}" property="transactionType">

	<bean:write name="aimQuarterlyInfoForm" property="transactionType"/>

</c:set>



<digi:form action="/quarterlyInfoFilter.do" name="aimQuarterlyInfoForm"

type="org.digijava.module.aim.form.QuarterlyInfoForm" method="get" styleId="myForm">



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

				<TR height="20"><TD height="20">

				<div id="subtabsFinancial">
					<c:set var="translation">

						<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>

					</c:set>

					<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" title="${translation}" >

			  			<digi:trn key="aim:overview">OVERVIEW</digi:trn>

			  		</digi:link> |

			  		<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="0">

						<c:set var="translation">

							<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

						</c:set>

						<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  			</digi:link>

					</logic:notEqual>

					<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="0">

			      	<span>

			      		<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			      	</span>

			  		</logic:equal> |
                                        <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">
                                        <logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="4">

			  			<c:set target="${urlSubTabs}" property="transactionType" value="4"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursement Orders</digi:trn>

			</c:set>

			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>

			  			</digi:link>

			  		</logic:notEqual>

			  		<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="4">

			      	<span>

			      		<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>

			      	</span>

			  		</logic:equal>|

                                        </field:display>

			  		<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="1">

			  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>

			</c:set>

			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			  			</digi:link>

			  		</logic:notEqual>

			  		<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="1">

			      	<span>

			      		<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			      	</span>

			  		</logic:equal>|

			  	
			  	<feature:display module="Funding" name="Expenditures">
			  	<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="2">

			  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

					<c:set var="translation">
		
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
		
					</c:set>

					  	
							  			
					  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >
		
					  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
		
					  			</digi:link>
					
				
			    	</logic:notEqual>

			    	<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="2">

			      	<span>

			      		<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

			      	</span>

			  		</logic:equal>	|
			</feature:display>
			<c:set var="translation">

				<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>

			</c:set>

			  		<digi:link href="/viewQuarterlyComparisons.do" name="urlAll" title="${translation}" >

						<digi:trn key="aim:all">ALL</digi:trn>

					</digi:link>
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

								<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>

							</c:set>

							<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>

							<c:set var="translation">

								<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>

							</c:set>

							<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >

								<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>

							</digi:link>



							&gt;

							<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="4">

            	        	<digi:trn key="aim:quarterlyDisbursementOrders">Quarterly Disbursement Orders</digi:trn>

							</logic:equal>



							<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="1">
                                                         &gt;

            	        	<digi:trn key="aim:quarterlyDisbursements">Quarterly Disbursements</digi:trn>

							</logic:equal>

							<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="2">

                     	<digi:trn key="aim:quarterlyExpenditures">Quarterly Expenditures</digi:trn>

							</logic:equal>

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

											<logic:equal name="aimQuarterlyInfoForm" property="goButtonPresent" value="true">



											<TABLE cellSpacing=1 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimQuarterlyInfoForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Currency:</STRONG></TD>

															<TD>

		                           	 				<html:select property="currency" styleClass="dr-menu">

	   			                        	 			<html:optionsCollection name="aimQuarterlyInfoForm" property="currencies"

																	value="currencyCode" label="currencyName"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimQuarterlyInfoForm" property="calendarPresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Calendar Type:</STRONG></TD>

															<TD>

																<html:select property="fiscalCalId" styleClass="dr-menu">

																	<html:optionsCollection name="aimQuarterlyInfoForm" property="fiscalYears"

																	value="ampFiscalCalId" label="name"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimQuarterlyInfoForm" property="yearRangePresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Year&nbsp;&nbsp;</STRONG></TD>

															<TD><STRONG>From:</STRONG></TD>

															<TD>

							                      		<html:select property="fromYear" styleClass="dr-menu">

									                      	<html:optionsCollection name="aimQuarterlyInfoForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

															<TD><STRONG>To:</STRONG></TD>

															<TD>

						   	                     	<html:select property="toYear" styleClass="dr-menu">

																	<html:optionsCollection name="aimQuarterlyInfoForm" property="years"

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




					                      	<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding" id="dataTable">

			                     		   	<tr bgcolor="#999999" >

			                          				<td bgcolor="#999999"  >

			   	                   					<div align="center" style="font-weight:bold;color:black;">

					                          				<digi:trn key="aim:year">Year</digi:trn>

				   	                       			</div>

			         		                 		</td>

			               		           		<td bgcolor="#999999">

			                     		     			<div align="center" style="font-weight:bold;color:black;">

			                          						<digi:trn key="aim:dateDisbursed">Date Disbursed</digi:trn>

			                           				</div>

					                          		</td>
													<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="4">
					                          		<td bgcolor="#999999">
														<div align="center" style="font-weight:bold;color:black;">
															<FONT color="blue">*</FONT>
																<digi:trn key="aim:plannedAmount">Planned Amount</digi:trn>
														</div>
													</td>
													</logic:notEqual>
			                          				<td bgcolor="#999999">

					                          			<div align="center" style="font-weight:bold;color:black;">

																	<FONT color="blue">*</FONT>

					                          				<digi:trn key="aim:actualAmount">Actual Amount</digi:trn>

			   		                       			</div>

			         			              		</td>

			                  				  	</tr>

					                        	<logic:empty name="aimQuarterlyInfoForm" property="quarterlyInfo">

					                        		<tr valign="top">

			   		                       			<td colspan="5" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>

			         		                 		</tr>

			               		         	</logic:empty>

			                        			<logic:notEmpty name="aimQuarterlyInfoForm" property="quarterlyInfo">

														<logic:iterate name="aimQuarterlyInfoForm" property="quarterlyInfo"

														id="qtr" type="org.digijava.module.aim.helper.QuarterlyInfo">

					                           	<logic:equal name="qtr" property="display" value="true">

						                          	<tr valign="top">

			      					              		<td valign="baseline">

				                     		     			<logic:equal name="qtr" property="aggregate" value="0">

			   	                       						<bean:write name="qtr" property="fiscalYear" />

			      	                    					</logic:equal>

						                          		</td>

			          			                		<td valign="baseline">

			                  			              	<logic:equal name="qtr" property="aggregate" value="1">

			                          							&nbsp;&nbsp;&nbsp;&nbsp;

				                          						<bean:write name="qtr" property="dateDisbursed" />

						                          			</logic:equal>

			   			                       			<logic:equal name="qtr" property="aggregate" value="0">

			            			              				<c:set target="${urlQuarterlyGrouping}" property="fiscalYrGrp">

																			<bean:write name="qtr" property="fiscalYear"/>

																		</c:set>

																		<c:set target="${urlQuarterlyGrouping}" property="fiscalQtrGrp">

																			<bean:write name="qtr" property="fiscalQuarter"/>

																		</c:set>

			         			                 				<logic:equal name="qtr" property="plus" value="true">

			                  		        						<c:set target="${urlQuarterlyGrouping}" property="clicked" value="plus"/>

							            						 		<digi:link href="/viewQuarterlyGrouping.do" name="urlQuarterlyGrouping"

																			style="TEXT-DECORATION: NONE">+</digi:link>

						                          			 	</logic:equal>

				              		            			 	<logic:equal name="qtr" property="plus" value="false">

			   	               		        			 		<c:set target="${urlQuarterlyGrouping}" property="clicked" value="minus"/>

				                        		  			 		<digi:link href="/viewQuarterlyGrouping.do" name="urlQuarterlyGrouping"

																			style="TEXT-DECORATION: NONE">-</digi:link>

						                          			 	</logic:equal>

						                          			 	<logic:equal name="qtr" property="fiscalQuarter" value="0">

			   			                       			 		NA

			      	   		                 			 	</logic:equal>

			         	      		           			 	<logic:equal name="qtr" property="fiscalQuarter" value="1">
			         	      		           			 	<digi:trn key="aim:1st_quarter">1st quarter</digi:trn>
			            	         		     			 </logic:equal>

			                  	         				 	<logic:equal name="qtr" property="fiscalQuarter" value="2">
																<digi:trn key="aim:2nd_quarter">2nd quarter</digi:trn>
			                     	     			 		</logic:equal>
															<logic:equal name="qtr" property="fiscalQuarter" value="3">
														
																<digi:trn key="aim:3rd_quarter">3rd quarter</digi:trn>
			   		                     	  			 	</logic:equal>
															<logic:equal name="qtr" property="fiscalQuarter" value="4">
																<digi:trn key="aim:4th_quarter">4th quarter</digi:trn>
															</logic:equal>

							                          		</logic:equal>

						                          		</td>
													<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="4">
			                   			       			<td valign="baseline" align="right">
													         <aim:formatNumber value="${qtr.plannedAmount}"/>
														</td>
													</logic:notEqual>				
			                          					<td valign="baseline" align="right">

			                          					 <aim:formatNumber value="${qtr.actualAmount}" />

						                          		</td>

					                           	</tr>

			                           			</logic:equal>

														</logic:iterate>
                                                      <tr valign="top">
														<td colspan="2">
                                                        	 <span class="note">
															<digi:trn key="aim:total">Total</digi:trn>
                                                           	</span>
														</td>
														<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="4">
														<td>
															<div align="right" class="note">
																<bean:write name="aimQuarterlyInfoForm" property="totalPlanned" />
															</div>
														</td>
														</logic:notEqual>
														<td>
															<div align="right" class="note">
																<bean:write name="aimQuarterlyInfoForm" property="totalActual" />
															</div>
														</td>
													</tr>

														</logic:notEmpty>

					                        </TABLE>

												</TD></TR>

												<TR><TD>


												</TD></TR>

												<TR><TD align="right">

													<TABLE cellspacing="0" cellpadding="0" vAlign="top">
                                                                                                             <c:set target="${urlSubTabs}" property="transactionType">

			        												<bean:write name='aimQuarterlyInfoForm' property='transactionType'/>

									        					</c:set>


														<TR>

															<TD width="15">

																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

															</TD>

															<TD>

									        					
			<c:set var="translation">

				<digi:trn key="aim:clickToViewYearlyInfo">Click here to view Yearly Info</digi:trn>

			</c:set>

			        											<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

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

                                                                                                                            <digi:trn key="aim:clickToViewMonthlyInfo">Click here to view Monthly Info</digi:trn>

                                                                                                                    </c:set>

			        											<digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" title="${translation}" >

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
function go() {
	document.getElementById("myForm").action = "/aim/viewQuarterlyInfo.do";
	document.getElementById("myForm").submit();
}

if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>
