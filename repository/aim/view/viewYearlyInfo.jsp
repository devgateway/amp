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



<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<digi:instance property="aimYearlyInfoForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimYearlyInfoForm" property="sessionExpired" value="true">

	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />

</logic:equal>



<logic:equal name="aimYearlyInfoForm" property="sessionExpired" value="false">



<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">

	<bean:write name="aimYearlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">

	<bean:write name="aimYearlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlSubTabs}" property="tabIndex">
	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
</c:set>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>



<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlFinancialOverview}" property="ampActivityId">

	<bean:write name="aimYearlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="ampFundingId">

	<bean:write name="aimYearlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">

	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>

</c:set>

<c:set target="${urlFinancialOverview}" property="tabIndex">
	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
</c:set>



<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlAll}" property="ampActivityId">

	<bean:write name="aimYearlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlAll}" property="ampFundingId">

	<bean:write name="aimYearlyInfoForm" property="ampFundingId"/>

</c:set>

<c:set target="${urlAll}" property="tabIndex">
	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
</c:set>



<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">

	<bean:write name="aimYearlyInfoForm" property="ampActivityId"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="transactionType">

	<bean:write name="aimYearlyInfoForm" property="transactionType"/>

</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex">
	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
</c:set>


<digi:form action="/viewYearlyInfoFilter.do" name="aimYearlyInfoForm" type="org.digijava.module.aim.form.YearlyInfoForm" method="post">



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

					</digi:link>|

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="0">

<c:set var="translation">

	<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

</c:set>

              		<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  			</digi:link>

					</logic:notEqual>

					<logic:equal name="aimYearlyInfoForm" property="transactionType" value="0">

	            	<span class="sub-nav2-selected">

	              		<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

	              	</span>

              	</logic:equal> |
                 <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">
                	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="4">

              		<c:set target="${urlSubTabs}" property="transactionType" value="4"/>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewDisbursementOrderss">Click here view Disbursement Orders</digi:trn>

						</c:set>

			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  				<digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>

			  			</digi:link>

              	</logic:notEqual>

              	<logic:equal name="aimYearlyInfoForm" property="transactionType" value="4">

	            	<span class="sub-nav2-selected">

	              		<digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>

	              	</span>

              	</logic:equal>|
                </field:display>

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="1">

              		<c:set target="${urlSubTabs}" property="transactionType" value="1"/>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewDisbursements">Click here view Disbursements</digi:trn>

						</c:set>

			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			  			</digi:link>

              	</logic:notEqual>

              	<logic:equal name="aimYearlyInfoForm" property="transactionType" value="1">

	            	<span class="sub-nav2-selected">

	              		<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

	              	</span>

              	</logic:equal>|

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="2">

               	<c:set target="${urlSubTabs}" property="transactionType" value="2"/>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

			</c:set>
<feature:display module="Funding" name="Expenditures">
			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >

			  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

			  			</digi:link>|
</feature:display>
               </logic:notEqual>

               <logic:equal name="aimYearlyInfoForm" property="transactionType" value="2">
				
				<feature:display module="Funding" name="Expenditures">
	            	<span class="sub-nav2-selected">

	              		<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

	              	</span>|
				</feature:display>
              	</logic:equal>	

								<digi:link href="/viewYearlyDiscrepancy.do" name="urlDiscrepancy" styleClass="sub-nav2" title="${translation}" >

									<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>

								</digi:link>		|

			<c:set var="translation">

				<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>

			</c:set>

              	<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="${translation}" >

						<digi:trn key="aim:all">ALL</digi:trn>

					</digi:link>

				</TD></TR>

				<TR bgColor=#f4f4f2>

            	<TD align=left>

						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">

							<TR>

								<TD align="left">

						<SPAN class=crumb>

              			<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>

							<c:set target="${urlFinancingBreakdown}" property="ampActivityId">

								<bean:write name="aimYearlyInfoForm" property="ampActivityId"/>

							</c:set>

							<c:set target="${urlFinancingBreakdown}" property="tabIndex">
								<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
							</c:set>

							<c:set var="translation">

								<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>

							</c:set>

							<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="<%translation%>" >

								<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>

							</digi:link>

              			 &gt;

							<logic:equal name="aimYearlyInfoForm" property="transactionType" value="0">

                     	<digi:trn key="aim:yearlyCommitments">Yearly Commitments</digi:trn>

							</logic:equal>
                            <logic:equal name="aimYearlyInfoForm" property="transactionType" value="4">

                  	  	<digi:trn key="aim:yearlyDisbursementsOrders">Yearly Disbursement Orders</digi:trn>

							</logic:equal>


							<logic:equal name="aimYearlyInfoForm" property="transactionType" value="1">

                  	  	<digi:trn key="aim:yearlyDisbursements">Yearly Disbursements</digi:trn>

							</logic:equal>

							<logic:equal name="aimYearlyInfoForm" property="transactionType" value="2">

                     	<digi:trn key="aim:yearlyExpenditures">Yearly Expenditures</digi:trn>

							</logic:equal>


<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
&gt;

								<digi:trn key="aim:${aimYearlyInfoForm.perpsectiveName}">
                                                                <bean:write name="aimYearlyInfoForm" property="perpsectiveName"/></digi:trn>&nbsp;
                                                                <digi:trn key="aim:perspective">Perspective</digi:trn>
                                                                </logic:equal>

						</SPAN>

								</TD>

								<TD align="right">

									&nbsp;

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

															<logic:equal name="aimYearlyInfoForm" property="transactionType" value="0">

                            							<digi:trn key="aim:yearlyCommitments">Yearly Commitments</digi:trn>

															</logic:equal>

                                                                                                                        	<logic:equal name="aimYearlyInfoForm" property="transactionType" value="4">

                  			          				<digi:trn key="aim:yearlyDisbursementOrders">Yearly Disbursement Orders</digi:trn>

															</logic:equal>



															<logic:equal name="aimYearlyInfoForm" property="transactionType" value="1">

                  			          				<digi:trn key="aim:yearlyDisbursements">Yearly Disbursements</digi:trn>

															</logic:equal>

															<logic:equal name="aimYearlyInfoForm" property="transactionType" value="2">

			                            				<digi:trn key="aim:yearlyExpenditures">Yearly Expenditures</digi:trn>

															</logic:equal>

                  			          		</TD>

                          						<TD width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">

			                          			</TD>

         			               		</TR>

                  			    		</TABLE>

											</TD>

											<TD vAlign="top" align="right">

												<logic:equal name="aimYearlyInfoForm" property="perspectivePresent" value="true">

												<TABLE cellSpacing="2" cellPadding="0" vAlign="top" bgColor=#f4f4f2>

													<TR>

														<TD>

						                         	<STRONG><digi:trn key="aim:perspective">Perspective</digi:trn>:</STRONG>

														</TD>

														<TD>

															<html:select property="perspective" styleClass="dr-menu">
																<logic:iterate name="aimYearlyInfoForm" property="perspectives" id="perspectiveId">
																<bean:define name="perspectiveId" property="name" id="perspectiveName"/>
																	<html:option value="${perspectiveId.code}">
																		<digi:trn key='<%="aim:"+perspectiveName %>' >
																			<bean:write name="perspectiveId" property="name"/></digi:trn>
																	</html:option>
																</logic:iterate>
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

											<logic:equal name="aimYearlyInfoForm" property="goButtonPresent" value="true">

											<TABLE cellSpacing=2 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimYearlyInfoForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG><digi:trn key="aim:currency">Currency</digi:trn>:</STRONG></TD>

															<TD>

		                           	 				<html:select property="currency" styleClass="dr-menu">
		                           	 					<html:optionsCollection name="aimYearlyInfoForm" property="currencies" value="currencyCode" label="currencyName"/>
	   			                        	 		</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimYearlyInfoForm" property="calendarPresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG><digi:trn key="aim:calendarType">Calendar Type</digi:trn>:</STRONG></TD>

															<TD>

																<html:select property="fiscalCalId" styleClass="dr-menu">

																	<html:optionsCollection name="aimYearlyInfoForm" property="fiscalYears"

																	value="ampFiscalCalId" label="name"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

         	                 			<logic:equal name="aimYearlyInfoForm" property="yearRangePresent" value="true">

      	                    			<TD>

													<TABLE cellSpacing=2 cellPadding=0>

														<TR>

															<TD><STRONG><digi:trn key="aim:year">Year</digi:trn>&nbsp;&nbsp;</STRONG></TD>

															<TD><STRONG><digi:trn key="aim:from">From</digi:trn>:</STRONG></TD>

															<TD>

							                      		<html:select property="fromYear" styleClass="dr-menu">

									                      	<html:optionsCollection name="aimYearlyInfoForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

															<TD><STRONG><digi:trn key="aim:to">To</digi:trn></STRONG></TD>

															<TD>

						   	                     	<html:select property="toYear" styleClass="dr-menu">

																	<html:optionsCollection name="aimYearlyInfoForm" property="years"

																	value="year" label="year"/>

																</html:select>

															</TD>

														</TR>

													</TABLE>

                  	        			</TD>

               	           			</logic:equal>

												<TD>

													<html:submit  styleClass="dr-menu"><digi:trn key="btn:go">Go</digi:trn></html:submit>

			                        	</TD>

											</TR>

											</TABLE>

											</logic:equal>



										</TD></TR>

										<TR><TD>



   	                       		<logic:notEqual name="aimYearlyInfoForm" property="perspective" value="DI">

											<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

	   	                     		<TR>

         	      	           			<TD width="100%" valign="middle">

            	      		        			<SPAN class="note">

               	         	  					<logic:equal name="aimYearlyInfoForm" property="transactionType" value="1">
													<digi:trn key="aim:totaldisbursed">Total Disbursed</digi:trn>:
													<bean:write name="aimYearlyInfoForm" property="totalDisbursed"/>
													<bean:write name="aimYearlyInfoForm" property="currency"/>
												</logic:equal>

                                                <logic:equal name="aimYearlyInfoForm" property="transactionType" value="4">
													<digi:trn key="aim:totalDisbOrdered">Total Ordered</digi:trn>:
													<bean:write name="aimYearlyInfoForm" property="totalDisbOrdered"/>
													<bean:write name="aimYearlyInfoForm" property="currency"/>
												</logic:equal>

												<logic:equal name="aimYearlyInfoForm" property="transactionType" value="2">
													<digi:trn key="aim:totalexpended">Total Expended</digi:trn>:
													<bean:write name="aimYearlyInfoForm" property="totalExpended"/>
													<bean:write name="aimYearlyInfoForm" property="currency"/>
												</logic:equal>

         			                 			</SPAN>

                  		        			</TD>

												</TR>

											</TABLE>

	                          		</logic:notEqual>

										</TD></TR>

										<TR><TD>

						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>

						                     <logic:notEqual name="aimYearlyInfoForm" property="perspective" value="DI">

                      							<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">

					                        		<tr bgcolor="#DDDDDB" >

               					           			<td bgcolor="#DDDDDB">

					                          				<div align="center">

																	<digi:trn key="aim:year">Year</digi:trn>

																	</div>

   					                       			</td>
															<!-- Commented by mouhamad for burkina -->
									                    	<!--td bgcolor="#DDDDDB">

				   					                     <div align="center">

															  		<digi:trn key="aim:plannedAmount">Planned Amount</digi:trn>

																	</div>

				   					              		</td-->

									                 <td bgcolor="#DDDDDB">
														  <div align="center">
															<font color="blue">*</font>
																<digi:trn key="aim:actualAmount">Actual Amount</digi:trn>
														 </div>
													</td>

									              		</tr>

				   					           		<logic:notEmpty name="aimYearlyInfoForm" property="yearlyInfo">
														<logic:iterate name="aimYearlyInfoForm" property="yearlyInfo" id="yearlyInfo" type="org.digijava.module.aim.helper.YearlyInfo">
																<tr valign="top" bgcolor="#F8F8F5">
																	<td bgcolor="#F8F8F5">
																	<logic:equal name="yearlyInfo" property="fiscalYear" value="0">NA</logic:equal>
																	<logic:notEqual  name="yearlyInfo" property="fiscalYear" value="0">
																		<bean:write name="yearlyInfo" property="fiscalYear" />
																	</logic:notEqual>
																</td>
																<!-- Commented by mouhamad for burkina -->
																<!--td bgcolor="#F8F8F5">
		         					                 				<div align="right">
		         					                 					<logic:present name="debug">
		         					                 						<%//=yearlyInfo.getWrapedPlanned()%>
		         					                 					</logic:present>
		         					                 					<logic:notPresent name="debug">
		         					                 						<%//=FormatHelper.formatNumber(yearlyInfo.getPlannedAmount())%>
		         					                 					</logic:notPresent>
																	</div>
																</td-->
																<td bgcolor="#F8F8F5">
																	<div align="right">
																		<logic:present name="debug">
																			<%=yearlyInfo.getWrapedActual()%>
																		</logic:present>
																		<logic:notPresent name="debug">
																			<%=FormatHelper.formatNumber(yearlyInfo.getActualAmount())%>
																		</logic:notPresent>
																	</div>
																</td>
															</tr>
														</logic:iterate>
													</logic:notEmpty>
													<logic:empty name="aimYearlyInfoForm" property="yearlyInfo">
														<tr valign="top" bgcolor="#F8F8F5">
															<td bgcolor="#F8F8F5" colspan="3">
																<div align="center" class="note">
																	<digi:trn key="aim:noRecords">No records</digi:trn> !
																</div>
															</td>
														</tr>
													</logic:empty>
													<tr valign="top" class="note">
														<td>
															<digi:trn key="aim:total">Total</digi:trn>
														</td>
														<!--td>
															<div align="right">
																<bean:write name="aimYearlyInfoForm" property="totalPlanned" />
															</div>
														</td-->
														<td>
															<div align="right">
																<bean:write name="aimYearlyInfoForm" property="totalActual" />
															</div>
														</td>
													</tr>
												</TABLE>

													</logic:notEqual>

												</TD></TR>

												<TR><TD>

									<logic:equal name="aimYearlyInfoForm" property="perspective" value="DI">

									<table width="100%"  border="0" cellpadding="3" cellspacing="1" class="box-border">

										<tr bgcolor="#DDDDDB" >

											<td height="30" bgcolor="#DDDDDB">

												<div align="center">

				              					Year

				              				</div>

				                  	</td>

							            <td bgcolor="#DDDDDB">

												<div align="center">

							               	<p>Donor Planned</p>

							               </div>

											</td>

							            <td bgcolor="#DDDDDB">

							            	<div align="center">Impl. Agency Planned</div>

							            </td>

							            <td bgcolor="#DDDDDB">

							            	<div align="center">

							              		<digi:trn key="aim:MOFED">Mofed</digi:trn> Planned

							             	</div>

							            </td>

							            <td bgcolor="#DDDDDB">

							            	<div align="center">

							              		Donor Actuals

							              	</div>

							            </td>

							            <td bgcolor="#DDDDDB">

							            	<div align="center">Impl. Agency Actuals</div>

							            </td>

							            <td bgcolor="#DDDDDB">

							            	<div align="center">

							              		<digi:trn key="aim:MOFED">Mofed</digi:trn> Actuals

							            	</div>

							            </td>

				            		</tr>

				            		<logic:empty name="aimYearlyInfoForm" property="discrepancies">

                        		<tr valign="top">

                         			<td colspan="8" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>

	                          	</tr>

			                     </logic:empty>

				            		<logic:notEmpty name="aimYearlyInfoForm" property="discrepancies">

										<logic:iterate name="aimYearlyInfoForm" property="discrepancies" id="discrepancy"

										type="org.digijava.module.aim.helper.YearlyDiscrepancy">

							         <tr valign="top">

							         	<td height="30">

							            	<logic:equal name="discrepancy" property="fiscalYear" value="0">NA</logic:equal>

				                        <logic:notEqual  name="discrepancy" property="fiscalYear" value="0">

													<bean:write name="discrepancy" property="fiscalYear" />

												</logic:notEqual>

											</td>

							            <td>

							            	<div align="right">

							              		<bean:write name="discrepancy" property="donorPlanned"/>

							              	</div>

							            </td>

							            <td>

							              	<div align="center">

							              		<bean:write name="discrepancy" property="implAgencyPlanned"/>

							              	</div>

							            </td>

							            <td>

							            	<div align="right">

							                  <bean:write name="discrepancy" property="mofedPlanned"/>

							                </div></td>

							            <td>

							              	<div align="right">

							              		<bean:write name="discrepancy" property="donorActual"/>

							              	</div>

							            </td>

							            <td>

							              	<div align="center">

							              		<bean:write name="discrepancy" property="implAgencyActual"/>

							              	</div>

							            </td>

							            <td>

							              	<div align="right">

							                  <bean:write name="discrepancy" property="mofedActual"/>

							                </div>

							            </td>

										</tr>

							         </logic:iterate>

							         </logic:notEmpty>

				       			</table>

                     	</logic:equal>



												</TD></TR>

												<TR><TD align="right">

													<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="0">

													<TABLE cellspacing="0" cellpadding="0" vAlign="top">
                                                                                                             <c:set target="${urlSubTabs}" property="transactionType">

							   		     						<bean:write name='aimYearlyInfoForm' property='transactionType'/>

				      			  						    </c:set>


														<TR>

															<TD width="15">

																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

															</TD>

															<TD>

																
			<c:set var="translation">

				<digi:trn key="aim:clickToViewQuarterly">Click here to view Quarterly</digi:trn>

			</c:set>

			   	     										<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" title="${translation}" >

			      	  											<STRONG>

																	<digi:trn key="aim:showQuartery">Show Quarterly </digi:trn>

																	</STRONG>

						   		     						</digi:link>
                                                                                                             	</TD>

														</TR>
                                                                                                                 <tr>
                                                                                                                     <TD width="15">

																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>

															</TD>

                                                                                                                     <td>
                                                                                                                     <c:set var="translation">

                                                                                                                        <digi:trn key="aim:clickToViewMonthly">Click here to view Monthly</digi:trn>

                                                                                                                 </c:set>

                                                                                                                 <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" title="${translation}" >

			      	  											<STRONG>

																	<digi:trn key="aim:showMonthly">Show Monthly </digi:trn>

																	</STRONG>

                                                                                                                      </digi:link >
                                                                                                                  </td>
                                                                                                              </tr>

														

													</TABLE>

													</logic:notEqual>

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




