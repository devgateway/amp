<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html:errors/>
<digi:instance property="aimYearlyDiscrepancyForm" />
<digi:context name="digiContext" property="context"/>

<logic:equal name="aimYearlyDiscrepancyForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimYearlyDiscrepancyForm" property="sessionExpired" value="false">
<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlFinancialOverview}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="ampFundingId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="tabIndex">
	<bean:write name="aimYearlyDiscrepancyForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex" value="1"/>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>
<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>
				<tr> 
			        <td width="100%" nowrap >
			        	<table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="box-border-nopadding">
							<tr bgcolor="#222E5D"> 
						  		<td style="color: #C9C9C7">&nbsp; &nbsp; &nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>
						</bean:define>
						  			<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" styleClass="sub-nav2" title="<%=translation%>" >
						  				<digi:trn key="aim:overview">OVERVIEW</digi:trn> 
						  			</digi:link> | 
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
						</bean:define>
						  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
						  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
						  			</digi:link> | 
						  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
						</bean:define>
						  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
						  					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
						  			</digi:link> |
						  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</bean:define>
					  				<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
					  					<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
					  				</digi:link> | 
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewYearlyComparisons">Click here to view Yearly Comparisons</digi:trn>
					</bean:define>
					  				<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="<%=translation%>" >
										<digi:trn key="aim:all">ALL</digi:trn>
									</digi:link>|
									<span class="sub-nav2-selected">
					  					<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>
					  				</span>|
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewAllYearlyDiscrepancy">Click here to view All Yearly Discrepancy</digi:trn>
					</bean:define>
					  				<digi:link href="/viewYearlyDiscrepancyAll.do" name="urlDiscrepancy" styleClass="sub-nav2" title="<%=translation%>" >
										<digi:trn key="aim:discrepancyAll">DISCREPANCY ALL</digi:trn>
									</digi:link>| 
			    				</td>
			    			</tr>
			    		</table>
			    	</td>		
				</tr>		
				<tr bgcolor="#F4F4F2" > 
					<td>
					<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
					<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
					<bean:write name="aimYearlyDiscrepancyForm" property="ampActivityId"/>
					</c:set>
					<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
					</bean:define>
					<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
					</digi:link> &gt; 
					<digi:trn key="aim:discrepancyPerspective">Discrepancy Perspective</digi:trn>  
				   	</td>
				</tr>
				<tr bgcolor="#F4F4F2" > 
					<td align="right" >
						<table width="90%"  border="0" align="right" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
				      		<tr bgcolor="#F4F4F2"> 
				        		<td  bgcolor="#F4F4F2">&nbsp; </td>
				        		<td align="right" bgcolor="#F4F4F2">
				        			<digi:form action="/viewYearlyDiscrepancyFilter.do" name="aimYearlyDiscrepancyForm" type="org.digijava.module.aim.form.QuarterlyDiscrepancyForm" method="post">
				        			<table  border="0" cellspacing="0" cellpadding="0">
				            			<tr> 
				              				<td>&nbsp;</td>
				              				<td height="30">
				              					<strong>
				              					<digi:trn key="aim:transactionType">Transaction Type</digi:trn>
				              					:</strong>
				              				</td>
				              				<td>&nbsp;</td>
				              				<td> 
				              					<html:select property="transactionType" styleClass="dr-menu" onchange="formSubmit()">
												<html:option value="0">Commitments</html:option>
												<html:option value="1">Disbursements</html:option>
												<html:option value="2">Expenditures</html:option>
												</html:select>
												<html:hidden property="ampActivityId" />
												<html:hidden property="tabIndex" />
				                			</td>
				              				<td>&nbsp;</td>
				            			</tr>
				          			</table>
				          			
				          		</td>
				      		</tr>
				        </table>
				        <div align="right"></div>
				    </td>
				</tr>
				<tr bgcolor="#F4F4F2" > 
					<td valign="top" >
						<table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
				      		<tr bgcolor="#F4F4F2"> 
				        		<td bgcolor="#F4F4F2">
				        			<table border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
				            			<tr bgcolor="#F4F4F2"> 
				              				<td height="17" nowrap bgcolor="#C9C9C7" class="box-title">
				              					&nbsp;<digi:trn key="aim:yearlyAmounts">Yearly Amounts</digi:trn>
				              				</td>
				              				<td width="17" height="17" 
											background="<%= digiContext %>/repository/aim/images/corner-r.gif">
				            			</tr>
				          			</table>
				          		</td>
				      		</tr>
				      		<tr> 
				        		<td bgcolor="#FFFFFF" class="box-border"> 
				          			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				            			<tr> 
				              				<td valign="middle"> 
				              					<logic:equal name="aimYearlyDiscrepancyForm" property="currencyPresent" value="true">
			                           			<strong><digi:trn key="aim:currency">Currency</digi:trn>:</strong>
			                           			</logic:equal>
				                			</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyForm" property="currencyPresent" value="true">
				                      			<html:select property="currency" styleClass="dr-menu">
				                      				<html:optionsCollection name="aimYearlyDiscrepancyForm" property="currencies" value="currencyCode" label="currencyName"/>
												</html:select>
												</logic:equal>
				               				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyForm" property="calendarPresent" value="true">
														<strong>
															<digi:trn key="aim:calendarType">Calendar Type</digi:trn> :
														</strong>
												</logic:equal>
				              				</td>
				              				<td> 
								               <logic:equal name="aimYearlyDiscrepancyForm" property="calendarPresent" value="true">
				                      			<html:select property="fiscalCalId" styleClass="dr-menu">
													<html:option value="4">Gregorian</html:option>
													<html:option value="1">Eth. Calendar</html:option>
													<html:option value="-1">Eth. Fiscal Year</html:option>
												</html:select>
												</logic:equal>
				              				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyForm" property="yearRangePresent" value="true">
	                              					<strong>
	                              						<digi:trn key="aim:year">Year</digi:trn>&nbsp;
	                              					</strong>
	                              				</logic:equal>
				              				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyForm" property="yearRangePresent" value="true">
							                      			<strong>From:</strong>
							                     </logic:equal>
				              				</td>
							              	<td> 
							                	<logic:equal name="aimYearlyDiscrepancyForm" property="yearRangePresent" value="true">
					                      			<b>
					                      			<html:select property="fromYear" styleClass="dr-menu">
					                      				<html:optionsCollection name="aimYearlyDiscrepancyForm" property="years" value="year" label="year"/>
													</html:select>
													</b>
												</logic:equal>
							              	</td>
							              	<td>
							              		<logic:equal name="aimYearlyDiscrepancyForm" property="yearRangePresent" value="true">
					                      			<strong>To:</strong>
					                      		</logic:equal>
							              	</td>
							              	<td>
							              		<logic:equal name="aimYearlyDiscrepancyForm" property="yearRangePresent" value="true">
					                      			<b> 
					                        		<html:select property="toYear" styleClass="dr-menu">
														<html:optionsCollection name="aimYearlyDiscrepancyForm" property="years" value="year" label="year"/>
													</html:select>
					                        		</b>
					                        	</logic:equal> 
							              	</td>
							              	<td> 
							              	<logic:equal name="aimYearlyDiscrepancyForm" property="goButtonPresent" value="true">
												<html:submit value="GO" styleClass="dr-menu"/>
											</logic:equal> 
							                </td>
							              	<td>&nbsp;</td>
				            			</tr>
				          			</table>
				          			<table width="100%"  border="0" cellpadding="3" cellspacing="1" class="box-border">
				            			<tr bgcolor="#DDDDDB" > 
				              				<td height="30" bgcolor="#DDDDDB">
				              					<div align="center">
				              						Fiscal Year 
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
							              			MOFED Planned 
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
							              			MOFED Actuals 
							              		</div>
							              	</td>
				            			</tr>
				            			<logic:empty name="aimYearlyDiscrepancyForm" property="discrepancies" >
			                        		<tr valign="top"> 
			                          			<td colspan="8" align="center"><span class="note">No records!</td>
			                          		</tr>
			                        	</logic:empty>
				            			<logic:notEmpty name="aimYearlyDiscrepancyForm" property="discrepancies">
										<logic:iterate name="aimYearlyDiscrepancyForm" property="discrepancies" id="discrepancy" type="org.digijava.module.aim.helper.YearlyDiscrepancy">
							            <tr valign="top"> 
							              <td height="30">
							              	<logic:equal name="discrepancy" property="fiscalYear" value="0">
				                          		NA
				                          	</logic:equal>
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
				       			</td>
				      		</tr>
				      		
				    	</table>
				    </td>
				</tr>
				<tr bgcolor="#F4F4F2" > 
                	<td valign="top">
						<table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
                     		<tr bgcolor="#F4F4F2"> 
                        		<td width="43%" bgcolor="#F4F4F2">
								</td>
                        		<td width="57%" align="right" bgcolor="#F4F4F2">
									<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
									<c:set target="${urlSubTabs}" property="transactionType">
		        						<bean:write name='aimYearlyDiscrepancyForm' property='transactionType'/>
		        					</c:set>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewQuarterlyDiscrepancy">Click here to view Quarterly Discrepancy</digi:trn>
									</bean:define>
		        					<digi:link href="/viewQuarterlyDiscrepancy.do" name="urlSubTabs" title="<%=translation%>" >
		        						<strong>
											Show Quarterly 
										</strong>
		        					</digi:link>
								</td>
                      		</tr>
                      		<tr bgcolor="#F4F4F2"> 
                        		<td  bgcolor="#F4F4F2">&nbsp;</td>
                        		<td align="right" bgcolor="#F4F4F2">&nbsp;</td>
                      		</tr>
                    	</table>
					</td>
                </tr>
            </digi:form>
</logic:equal>
