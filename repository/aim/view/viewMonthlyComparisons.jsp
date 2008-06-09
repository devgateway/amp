<%-- 
    Document   : viewMonthlyComparisons
    Created on : Mar 27, 2008, 2:01:08 PM
--%>

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




<digi:context name="digiContext" property="context" />



<digi:errors/>

<digi:instance property="aimMonthlyInfoForm" />




<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">

	<bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">

	<bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>



<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlFinancialOverview}" property="ampActivityId">

	<bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="ampFundingId">

	<bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">

	<bean:write name="aimMonthlyInfoForm" property="tabIndex"/>

</c:set>



<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">

	<bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>

<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>





<digi:form action="/viewMonthlyComparisons.do" name="aimMonthlyInfoForm"

type="org.digijava.module.aim.form.MonthlyInfoForm" method="post">



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

				<TR bgColor=#222e5d height="20"><TD style="COLOR: #c9c9c7" height="20">

				&nbsp;&nbsp;&nbsp;

			<c:set var="translation">

				<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>

			</c:set>

			  			<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" styleClass="sub-nav2" title="${translation}" >

			  				<digi:trn key="aim:overview">OVERVIEW</digi:trn>

			  			</digi:link> |

			
                                                    <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                                <c:set target="${urlSubTabs}" property="transactionType" value="4"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursementsOrders">Click here to view Disbursement Orders</digi:trn>

			</c:set>

			  			<digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  					<digi:trn key="aim:disbursementOrders">DISBURSEMENTS ORDERS</digi:trn>

			  			</digi:link> |
                                                </field:display>

			  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>

			</c:set>

			  			<digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			  			</digi:link> |

			  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

		</c:set>
					<feature:display module="Funding" name="Expenditures">
		  				<digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

		  					<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

		  				</digi:link> |
					</feature:display>
		  				
								<span class="sub-nav2-selected">

		  					<digi:trn key="aim:all">ALL</digi:trn>

		  				</span>

				</TD></TR>

				<TR bgColor=#f4f4f2>

            	<TD align=left>

						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">

							<TR>

								<TD align="left">

						<SPAN class=crumb>

		              	<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>

							<c:set target="${urlFinancingBreakdown}" property="ampActivityId">

								<bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>

							</c:set>

							<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>

								<c:set var="translation">

									<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>

								</c:set>

								<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >

									<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>

								</digi:link> &gt;

								<digi:trn key="aim:monthlyAll">Monthly All</digi:trn>



						</SPAN>

								</TD>

								<TD align="right">&nbsp;

									

								</TD>

							</TR>

						</TABLE>

					</TD>

				</TR>



				<TR bgColor=#f4f4f2>

					<TD vAlign="top" align="center" width="100%" bgColor="#f4f4f2">

						<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center bgColor="#f4f4f2" border=0>

							<TR>

								<TD height="30">

									<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center bgColor="#f4f4f2" border=0 height="30">

										<TR>

											<TD vAlign="bottom" align="left">

												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">

         			               		<TR bgcolor="#F4F4F2">

                  			        			<TD nowrap bgcolor="#C9C9C7" class="box-title">&nbsp;

 							              				<digi:trn key="aim:monthlyAllAmounts">Monthly All Amounts</digi:trn>

                  			          		</TD>

                          						<TD width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">

			                          			</TD>

         			               		</TR>

                  			    		</TABLE>

											</TD>

											<TD vAlign="top" align="right">

												<TABLE cellSpacing="2" cellPadding="0" vAlign="top" bgColor=#f4f4f2>

													<TR>

														<TD>


														</TD>

														<TD>

														</TD>

													</TR>

												</TABLE>


											</TD>

										</TR>

									</TABLE>

								</TD>

							</TR>

							<TR bgcolor="#ffffff">

								<TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">

             					<TABLE cellSpacing=2 cellPadding=0 border=0 bgColor="#ffffff" width="100%" vAlign="top" align="left"

									bgColor="#ffffff">

										<TR><TD bgColor="#ffffff">

											<logic:equal name="aimMonthlyInfoForm" property="goButtonPresent" value="true">



											<TABLE cellSpacing=1 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimMonthlyInfoForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Currency:</STRONG></TD>

															<TD>

		                           	 				<html:select property="currency" styleClass="dr-menu">

	   			                        	 			<html:optionsCollection name="aimMonthlyInfoForm" property="currencies"

																	value="currencyCode" label="currencyName"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			

         	                 			<logic:equal name="aimMonthlyInfoForm" property="yearRangePresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG>Year&nbsp;&nbsp;</STRONG></TD>

															<TD><STRONG>From:</STRONG></TD>

															<TD>

							                      		<html:select property="fromYear" styleClass="dr-menu">

									                      	<html:optionsCollection name="aimMonthlyInfoForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

															<TD><STRONG>To:</STRONG></TD>

															<TD>

						   	                     	<html:select property="toYear" styleClass="dr-menu">

																	<html:optionsCollection name="aimMonthlyInfoForm" property="years"

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

				                      				<logic:equal name="aimMonthlyInfoForm" property="transactionType" value="1">

                     									<digi:trn key="aim:totalCommitted">Total Committed</digi:trn>:

								     								<bean:write name="aimMonthlyInfoForm" property="totalCommitted"/> USD

							     								<digi:trn key="aim:totalRemaining">Total Remaining</digi:trn>:

				     											<bean:write name="aimMonthlyInfoForm" property="totalRemaining"/> USD

				     										</logic:equal>
                                                                                                                	     							<logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">

         		          								<digi:trn key="aim:totalDisbOrdered">Total Ordered</digi:trn>:

				   				  								<bean:write name="aimMonthlyInfoForm" property="totalDisbOrdered"/> USD




							     							</logic:equal>

							     							<logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">

         		          								<digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn>:

				   				  								<bean:write name="aimMonthlyInfoForm" property="totalDisbursed"/> USD

				     											<digi:trn key="aim:totalUnExpended">Total Un-Expended</digi:trn>:

				     											<bean:write name="aimMonthlyInfoForm" property="totalUnExpended"/> USD

							     							</logic:equal>

         			                 			</SPAN>

                  		        			</TD>

												</TR>

											</TABLE>

	                          		

										</TD></TR>

										<TR><TD>

						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>


				                          		<table width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">

            				                		<tr bgcolor="#DDDDDB">

			                              			<td bgcolor="#DDDDDB">

         			                     				<div align="center">

                  			            					<digi:trn key="aim:year">Year</digi:trn>

                           			   				</div>

			                              			</td>

         			                     			<td bgcolor="#DDDDDB">

                  			            				<div align="center">

                           									<digi:trn key="aim:MonthlyComparision:Month">Month</digi:trn>

                              							</div>

			                              			</td>

         			                     			<td bgcolor="#DDDDDB">

                  			            				<div align="center">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualCommitments">Actual Commitments</digi:trn>

                               							</div>

			                              			</td>
                                                    
                                                     <feature:display module="Funding" name="Disbursement Orders">
                                                    
                                                                            <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                                                        <td bgcolor="#DDDDDB">

                  			            				<div align="center">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualDisbOrdres">Actual Disbursements Orders</digi:trn>

                               							</div>

			                              				</td>
                                                  	</field:display>

												 </feature:display>
                  			            			<td bgcolor="#DDDDDB">

         			         		        				<div align="center">

																		<FONT color="blue">*</FONT>

                              							<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>

                              							</div>

			                              			</td>

         			                     			<td bgcolor="#DDDDDB">

                  			            				<div align="center">

																		<FONT color="blue">*</FONT>

                           		   						<digi:trn key="aim:actualDisbursements">Actual Disbursements</digi:trn>

                              							</div>

			                              			</td>
										<feature:display module="Funding" name="Expenditures">
         			                     			<td bgcolor="#DDDDDB">

                  			            				<div align="center">

																		<FONT color="blue">*</FONT>

                           			   					<digi:trn key="aim:actualExpenditures">Actual Expenditures</digi:trn>

                               							</div>

                              						</td>
											</feature:display>
				                            		</tr>

            				                		<logic:empty name="aimMonthlyInfoForm" property="monthlyInfoList" >

			               			         		<tr valign="top">

			                        			  			<td colspan="6" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>

			                          					</tr>

							                        </logic:empty>

			   				                     <logic:notEmpty name="aimMonthlyInfoForm" property="monthlyInfoList" >

                        				    			<logic:iterate name="aimMonthlyInfoForm" property="monthlyInfoList"

				                            			id="monthlyComparison" type="org.digijava.module.aim.helper.MonthlyComparison">

																	<tr valign="top">

			                  					      		<td bgcolor="#F8F8F5">

								      		                 		<logic:equal name="monthlyComparison" property="fiscalYear" value="0">

				   					      	                 		NA

				                          							</logic:equal>

										                          	<logic:notEqual  name="monthlyComparison" property="fiscalYear" value="0">

																				<bean:write name="monthlyComparison" property="fiscalYear" />

																			</logic:notEqual>

																		</td>

									                           <td bgcolor="#F8F8F5">

			      	   		                   				
				            	

																				<bean:write name="monthlyComparison" property="month" />

																			

																		</td>

																		<td bgcolor="#F8F8F5">

																			<div align="right">

																				<bean:write name="monthlyComparison" property="actualCommitment" format="#.##" />
																			</div>

																		</td>
                                                                                                                                              
                                                                                                                                              <feature:display module="Funding" name="Disbursement Orders">
                                                                                                                                                <td bgcolor="#F8F8F5">
                                                                                                                                                <field:display name="Actual Disbursement Orders" feature="Disbursement Orders">

																			<div align="right">

																				<bean:write name="monthlyComparison" property="disbOrders" format="#.##"/>
																			</div>
                                                                                                                                                            </field:display >


																		</td>
</feature:display>


																		<td bgcolor="#F8F8F5">

																			<div align="right">

																				<bean:write name="monthlyComparison" property="plannedDisbursement" format="#.##"/>
																			</div>

																		</td>

																		<td bgcolor="#F8F8F5">

																			<div align="right">

																				<bean:write name="monthlyComparison" property="actualDisbursement" format="#.##" />
																			</div>

																		</td>
	<feature:display module="Funding" name="Expenditures">
																		<td bgcolor="#F8F8F5">

																			<div align="right">

																				<bean:write name="monthlyComparison" property="actualExpenditure" format="#.##"/>
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

																			<bean:write name="aimMonthlyInfoForm" property="totalActualCommitment" format="#.##"/>

																		</span>

																	</div>

															  </td>

   <feature:display name="Disbursement Orders" module="Funding"> 	<td>
                                                                                                                                       

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimMonthlyInfoForm" property="totalDisbOrder" format="#.##" />

																		</span>

																	</div>
                                                                                                                                       

																</td>

   </feature:display>


			      		                        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimMonthlyInfoForm" property="totalPlannedDisbursement" format="#.##" />

																		</span>

																	</div>

															  </td>

			                     			        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimMonthlyInfoForm" property="totalActualDisbursement" format="#.##" />

																		</span>

																	</div>

															  </td>


			      		                        	<feature:display module="Funding" name="Expenditures">
                                                    <td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimMonthlyInfoForm" property="totalActualExpenditure" format="#.##"/>

																		</span>

																	</div>

													  </td></tr>
                                                                                                   </feature:display>

															</logic:notEmpty>

		                       					</table>


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

																	<bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>

																</c:set>

																<c:set target="${urlShowQuarterly}" property="ampFundingId">

																	<bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>

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

                                                                                                    <digi:trn key="aim:clickToViewQuarterlyComparisons">Click here to view Quarterly Comparisons</digi:trn>

                                                                                            </c:set>

							                        	<digi:link href="/viewQuarterlyComparisons.do" name="urlShowQuarterly" title="${translation}" >

				      	  										<STRONG>
																	<digi:trn key="aim:showQuarterly">Show Quarterly</digi:trn>
																	</STRONG>

						   		     						</digi:link>

															</TD>

									

													</TABLE>

												</TD></TR>

												<TR><TD>

													<FONT color=blue>*

													<digi:trn key="aim:allTheAmountsInThousands">

													All the amounts are in thousands (000)</digi:trn>
													</FONT>

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



</digi:form>





