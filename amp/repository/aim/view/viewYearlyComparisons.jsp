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




<digi:errors/>

<digi:instance property="aimYearlyComparisonsForm" />

<digi:context name="digiContext" property="context"/>



<logic:equal name="aimYearlyComparisonsForm" property="sessionExpired" value="true">

	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />

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



<digi:form action="/viewYearlyComparisonsFilter.do" name="aimYearlyComparisonsForm"

type="org.digijava.module.aim.form.YearlyComparisonsForm" method="post">



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

				<TR bgColor=#222e5d><TD style="COLOR: #c9c9c7">

					&nbsp;&nbsp;&nbsp;

					<c:set var="translation">

						<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>

					</c:set>

					<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" styleClass="sub-nav2" title="${translation}" >

              		<digi:trn key="aim:overview">OVERVIEW</digi:trn>

					</digi:link>|

<c:set var="translation">

	<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

</c:set>

              	<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  			<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  		</digi:link>|
                                        <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">

                                        <c:set target="${urlSubTabs}" property="transactionType" value="4"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewDisbursementOrderss">Click here to view Disbursement Orders</digi:trn>

		</c:set>

		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

		  				<digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>

		  			</digi:link>|
                                        	</field:display>

		  		<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>

		</c:set>

		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

		  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

		  			</digi:link>|

		  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

		<c:set var="translation">

			<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

		</c:set>
		<feature:display module="Funding" name="Expenditures">
					  			
		  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

		  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

		  			</digi:link>|
		</feature:display>
					 <digi:link href="/viewYearlyDiscrepancy.do" name="urlDiscrepancy" styleClass="sub-nav2" title="${translation}" >

									<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>

								</digi:link>		|

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
<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">

							&gt;

							<digi:trn key="aim:${aimYearlyComparisonsForm.perpsectiveName}">
                                                                <bean:write name="aimYearlyComparisonsForm" property="perpsectiveName"/></digi:trn>&nbsp;
                                                            <digi:trn key="aim:perspective">Perspective</digi:trn>
</logic:equal>

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

															<digi:trn key="aim:yearlyAllAmounts">Yearly All Amounts</digi:trn>

                  			          		</TD>

                          						<TD width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">

			                          			</TD>

         			               		</TR>

                  			    		</TABLE>

											</TD>

											<TD vAlign="top" align="right">

												<logic:equal name="aimYearlyComparisonsForm" property="perspectivePresent" value="true">

												<TABLE cellSpacing="2" cellPadding="0" vAlign="top" bgColor=#f4f4f2>

													<TR>

														<TD>

						                         	<STRONG>Perspective:</STRONG>

														</TD>

														<TD>

															<html:select property="perspective" styleClass="dr-menu">

																<html:optionsCollection name="aimYearlyComparisonsForm"

																property="perspectives" value="code" label="name"/>

															</html:select>

														</TD>

													</TR>

												</TABLE>

												</logic:equal>

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

													<html:submit value="GO" styleClass="dr-menu"/>

			                        	</TD>

											</TR>

											</TABLE>

											</logic:equal>

										</TD></TR>

										<TR><TD>

						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>

												  <logic:notEqual name="aimYearlyComparisonsForm" property="perspective" value="DI">

				                     			<table width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">

            			                			<tr bgcolor="#DDDDDB" >

                     		         				<td width="6%" bgcolor="#DDDDDB">

                           		   					<div align="center">

                              								<digi:trn key="aim:year">Year</digi:trn>

                           							  </div>

		                              				</td>

					                              	<td width="13%" bgcolor="#DDDDDB">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualCommitments">Actual Commitments</digi:trn>

																	</div>

					                             	  </td>

                                                                <feature:display name="Disbursement Orders" module="Funding">
                                                                                <td width="13%" bgcolor="#DDDDDB">


																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:disbursementsOrders">Disbursement Orders</digi:trn>

																	</div>
                                                                                                                                      

						                            	</td>


    </feature:display>
						                             	<td width="13%" bgcolor="#DDDDDB">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>

																	</div>

						                            	</td>

					                              	<td width="13%" bgcolor="#DDDDDB">

																	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualDisbursements">Actual Disbursements</digi:trn>

																	</div>

			      		                        	</td>

			            		                  
			            		                  <feature:display module="Funding" name="Expenditures">
			            		                  	<td width="24%" bgcolor="#DDDDDB">

			                  			              	<div align="center">

																		<FONT color="blue">*</FONT>

																		<digi:trn key="aim:actualExpenditures">Actual Expenditures</digi:trn>

													  </div>

			                     		         	</td>
			                     		         	
												</feature:display>
                            						</tr>

			                            			<logic:empty name="aimYearlyComparisonsForm" property="yearlyComparisons">

         			                   			<tr valign="top">

			         			                   	<td align="center">

			                  						  		<span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!!</span>						                            	</td>
         			                   			</tr>

			                            			</logic:empty>

			                            			<logic:notEmpty name="aimYearlyComparisonsForm" property="yearlyComparisons">

                                                                        <logic:iterate name="aimYearlyComparisonsForm" property="yearlyComparisons"

                                                                        id="yearlyComparisons" type="org.digijava.module.aim.helper.YearlyComparison">

                                                                        <tr valign="top">

						                            	<td bgcolor="#F8F8F5">

						                            		<logic:equal name="yearlyComparisons" property="fiscalYear" value="0">

				      			                    			NA

				               			           		</logic:equal>

				                        			  		<logic:notEqual  name="yearlyComparisons" property="fiscalYear" value="0">

																		<bean:write name="yearlyComparisons" property="fiscalYear" />

														  </logic:notEqual>

			                            				</td>

				   	                           	<td bgcolor="#F8F8F5">

																	<div align="right">

																		<bean:write name="yearlyComparisons" property="actualCommitment" />

																	</div>

																</td>



                                                                                                                                                                                                                                                        <feature:display name="Disbursement Orders" module="Funding"> <td bgcolor="#F8F8F5">


																	<div align="right">

																		<bean:write name="yearlyComparisons" property="disbOrders" />

																	</div>
                                                                                                                                  

																</td>
</feature:display>


			                           		   	<td bgcolor="#F8F8F5">

																	<div align="right">

																		<bean:write name="yearlyComparisons" property="plannedDisbursement" />

																	</div>

																</td>

			      	                        		<td bgcolor="#F8F8F5">

																	<div align="right">

																		<bean:write name="yearlyComparisons" property="actualDisbursement" />

																	</div>

																</td>
  <feature:display module="Funding" name="Expenditures">
				                  	            	<td bgcolor="#F8F8F5">

																	<div align="right">

																		<bean:write name="yearlyComparisons" property="actualExpenditure" />

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

																			<bean:write name="aimYearlyComparisonsForm" property="totalActualCommitment" />

																		</span>

																	</div>

															  </td>

   <feature:display name="Disbursement Orders" module="Funding"> 	<td>
                                                                                                                                       

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimYearlyComparisonsForm" property="totalDisbOrder" />

																		</span>

																	</div>
                                                                                                                                       

																</td>

   </feature:display>


			      		                        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimYearlyComparisonsForm" property="totalPlannedDisbursement" />

																		</span>

																	</div>

															  </td>

			                     			        	<td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimYearlyComparisonsForm" property="totalActualDisbursement" />

																		</span>

																	</div>

															  </td>


			      		                        	<feature:display module="Funding" name="Expenditures">
                                                    <td>

																	<div align="right">

																		<span class="note">

																			<bean:write name="aimYearlyComparisonsForm" property="totalActualExpenditure" />

																		</span>

																	</div>

													  </td>
</feature:display>
			      			                      </tr>

			               			             </logic:notEmpty>

                   		         		   </table>

	                   		                    <p>&nbsp;</p>
												  </logic:notEqual>



												</TD></TR>

												<TR><TD>

                   		            	<logic:equal name="aimYearlyComparisonsForm" property="perspective" value="DI">

		                   		            <table width="100%"  border="0" cellpadding="0" cellspacing="1" class="box-border">

								            			<tr bgcolor="#DDDDDB" >

				      				        				<td height="30" bgcolor="#DDDDDB">

				              									<div align="center">

									              					<digi:trn key="aim:year">Year</digi:trn>

				   			            					</div>

				            			      			</td>

							            				  	<td bgcolor="#DDDDDB" colspan="3">

											              		<div align="center">

									                  		<p>

																		<FONT color="blue">*</FONT>

										                  		<digi:trn key="aim:discrepancyCommitments">Commitments</digi:trn>

							         			        		</p>

							                					</div>

											                </td>

											              	<td bgcolor="#DDDDDB" colspan="3">

							   				           		<div align="center">

																		<FONT color="blue">*</FONT>

							              							<digi:trn key="aim:discrepancyDisbursements">Disbursements</digi:trn>

											              		</div>

											              	</td>

	<feature:display module="Funding" name="Expenditures">
							   				           	<td bgcolor="#DDDDDB" colspan="3">

							              						<div align="center">

																		<FONT color="blue">*</FONT>

											              			<digi:trn key="aim:discrepancyExpenditures">Expenditures</digi:trn>

											              		</div>

							   					        	</td>
</feature:display>
								            			</tr>

								            			<tr bgcolor="#DDDDDB" >

				            				  				<td height="30" bgcolor="#DDDDDB"></td>

							              					<td bgcolor="#DDDDDB">

										              			<div align="center">

									                  		<p>

																		<FONT color="blue">*</FONT>

									                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>

									                  		</p>

								      			          	</div>

							               				 </td>

											              	<td bgcolor="#DDDDDB">

							            				  		<div align="center">

																		<FONT color="blue">*</FONT>

											              			Impl. Agency Actuals

											              		</div>

							   					        	</td>

											              	<td bgcolor="#DDDDDB">

											              		<div align="center">

																		<FONT color="blue">*</FONT>

							   				           			<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>

							              						</div>

											              	</td>

											              	<td bgcolor="#DDDDDB">

							   				           		<div align="center">

							               			   		<p>

																		<FONT color="blue">*</FONT>

										                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>

										                  		</p>

											                	</div>

							         				       </td>

											              	<td bgcolor="#DDDDDB">

											              		<div align="center">

																		<FONT color="blue">*</FONT>

							      				        			Impl. Agency Actuals

							              						</div>

											              	</td>

											              	<td bgcolor="#DDDDDB">

							   				           		<div align="center">

																		<FONT color="blue">*</FONT>

							              							<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>

											              		</div>

											              	</td>

							   				         <feature:display module="Funding" name="Expenditures">
                                                       	<td bgcolor="#DDDDDB">

							              						<div align="center">

										                  		<p>

																		<FONT color="blue">*</FONT>

										                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>

										                  		</p>

							      				          	</div>

											                </td>

											              	<td bgcolor="#DDDDDB">

							   				           		<div align="center">

																		<FONT color="blue">*</FONT>

											              			Impl. Agency Actuals

											              		</div>

							   				           	</td>

											              	<td bgcolor="#DDDDDB">

											              		<div align="center">

																		<FONT color="blue">*</FONT>

							   				           			<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>

											              		</div>

											              	</td>
</feature:display>
				            							</tr>

								            			<logic:empty name="aimYearlyComparisonsForm" property="yearlyDiscrepanciesAll" >

			   			                    		<tr valign="top">

			            				        			<td colspan="10" align="center">

			                        			  			<span class="note">No records!</span>			                          					</td>
			   			                    		</tr>

						                        	</logic:empty>

				   				         			<logic:notEmpty name="aimYearlyComparisonsForm" property="yearlyDiscrepanciesAll">

															<logic:iterate name="aimYearlyComparisonsForm" property="yearlyDiscrepanciesAll"

															id="discrepancy" type="org.digijava.module.aim.helper.YearlyDiscrepancyAll">

											            <tr valign="top">

											              <td height="30" bgcolor="#F8F8F5">

							   					           <logic:equal name="discrepancy" property="fiscalYear" value="0">

				                          					NA

								                          	</logic:equal>

								                          	<logic:notEqual  name="discrepancy" property="fiscalYear" value="0">

																		<bean:write name="discrepancy" property="fiscalYear"/>

																	</logic:notEqual>

							              					</td>

											              	<td bgcolor="#F8F8F5">

																	<div align="right">

											              			<bean:write name="discrepancy" property="commitmentDonorActual"/>

												              	</div>

											              	</td>

							      				        	<td bgcolor="#F8F8F5">

																	<div align="center">

												              		<bean:write name="discrepancy" property="commitmentImplAgencyActual"/>

												              	</div>

																</td>

											             	<td bgcolor="#F8F8F5">

							              						<div align="right">

												                  <bean:write name="discrepancy" property="commitmentMofedActual"/>

											               	</div>

																</td>

							              					<td bgcolor="#F8F8F5">

												              	<div align="right">

												              		<bean:write name="discrepancy" property="disbursementDonorActual"/>

							         					     	</div>

											              	</td>

											              	<td bgcolor="#F8F8F5">

												              	<div align="center">

							      					        		<bean:write name="discrepancy" property="disbursementImplAgencyActual"/>

												              	</div>

											              	</td>

							      				        	<td bgcolor="#F8F8F5">

												              	<div align="right">

												                  <bean:write name="discrepancy" property="disbursementMofedActual"/>

							         					      </div>

							              					</td>
   <feature:display module="Funding" name="Expenditures">
							               				<td bgcolor="#F8F8F5">

							              						<div align="right">

							              							<bean:write name="discrepancy" property="expenditureDonorActual"/>

							              						</div>

							              					</td>

							              					<td bgcolor="#F8F8F5">

							              						<div align="center">

							              							<bean:write name="discrepancy" property="expenditureImplAgencyActual"/>

							              						</div>

							              					</td>

							              					<td bgcolor="#F8F8F5">

							              						<div align="right">

							                  					<bean:write name="discrepancy" property="expenditureMofedActual"/>

							                					</div>

							              					</td>
   </feature:display>
							            				</tr>

							            				</logic:iterate>

											            </logic:notEmpty>

								       				</table>

													</logic:equal>

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

</logic:equal>




