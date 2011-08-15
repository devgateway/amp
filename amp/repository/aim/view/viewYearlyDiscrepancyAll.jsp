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

<script language="Javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=3&surveyFlag=true&activityId=" + id;
}
function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
	  var urlToGo = "<%=addUrl%>~pageId=2~activityId=" + id;
	    document.location.href = urlToGo;
}
</script>


<digi:errors/>
<digi:instance property="aimYearlyDiscrepancyAllForm" />
<digi:context name="digiContext" property="context"/>

<logic:equal name="aimYearlyDiscrepancyAllForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp"  />
</logic:equal>

<logic:equal name="aimYearlyDiscrepancyAllForm" property="sessionExpired" value="false">
<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="tabIndex"/>
</c:set>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlFinancialOverview}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="ampFundingId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="tabIndex">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimYearlyDiscrepancyAllForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex" >
	<bean:write name="aimYearlyDiscrepancyAllForm" property="tabIndex"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>

<digi:form action="/viewYearlyDiscrepancyAllFilter.do" name="aimYearlyDiscrepancyAllForm" type="org.digijava.module.aim.form.YearlyDiscrepancyAllForm" method="post">
				<tr> 
			        <td width="100%" nowrap >
			        	<table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="box-border-nopadding">
							<tr bgcolor="#222E5D"> 
						  		<td height="20" style="color: #C9C9C7">&nbsp; &nbsp; &nbsp;
					  <c:set var="translation">
							<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>
						</c:set>
						  			<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" styleClass="sub-nav2" title="${translation}" >
						  				<digi:trn key="aim:overview">OVERVIEW</digi:trn> 
						  			</digi:link> | 
						<c:set var="translation">
							<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
						</c:set>
						  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
						  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
						  			</digi:link> | 
						  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
						</c:set>
						  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
						  					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
						  			</digi:link> |
						  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</c:set>
					  				
					  			<feature:display module="Funding" name="Expenditures">
					  				<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
					  					<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
					  				</digi:link> | 
								</feature:display>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewAllYearlyComparisons">Click here to view All Yearly Comparisons</digi:trn>
					</c:set>
					  				<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="${translation}" >
										<digi:trn key="aim:all">ALL</digi:trn>
									</digi:link>|
									<c:set var="translation">
										<digi:trn key="aim:clickToViewYearlyDiscrepancy">Click here to view Yearly Discrepancy</digi:trn>
									</c:set>
									<digi:link href="/viewYearlyDiscrepancy.do" name="urlDiscrepancy" styleClass="sub-nav2" title="${translation}" >
										<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>
									</digi:link>| 
									<span class="sub-nav2-selected">
					  					<digi:trn key="aim:discrepancyAll">DISCREPANCY ALL</digi:trn>
					  				</span>
								</td>
		    			  </tr>
			    		</table>
			    	</td>		
				</tr>		
				<tr bgcolor="#F4F4F2" > 
					<td>
					<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
					<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
					<bean:write name="aimYearlyDiscrepancyAllForm" property="ampActivityId"/>
					</c:set>
					<c:set target="${urlFinancingBreakdown}" property="tabIndex" >
						<bean:write name="aimYearlyDiscrepancyAllForm" property="tabIndex"/>
					</c:set>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
					</c:set>
					<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >
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
				        			
				        			<table  border="0" cellspacing="0" cellpadding="0">
				            			<tr> 
				              				<td>&nbsp;</td>
				              				<td height="30">
				              					
				              				</td>
				              				<td>&nbsp;</td>
				              				<td> 
				              					
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
				              				<td width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">
				            			</tr>
				          			</table>
				          		</td>
				      		</tr>
				      		<tr> 
				        		<td bgcolor="#FFFFFF" class="box-border"> 
				          			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				            			<tr> 
				              				<td valign="middle"> 
				              					<logic:equal name="aimYearlyDiscrepancyAllForm" property="currencyPresent" value="true">
			                           			<strong><digi:trn key="aim:currency">Currency</digi:trn>:</strong>
			                           			</logic:equal>
				                			</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyAllForm" property="currencyPresent" value="true">
				                      			<html:select property="currency" styleClass="dr-menu">
				                      				<html:optionsCollection name="aimYearlyDiscrepancyAllForm" property="currencies" value="currencyCode" label="currencyName"/>
												</html:select>
												</logic:equal>
				               				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyAllForm" property="calendarPresent" value="true">
														<strong>
															<digi:trn key="aim:calendarType">Calendar Type</digi:trn> :
														</strong>
												</logic:equal>
				              				</td>
				              				<td> 
								               <logic:equal name="aimYearlyDiscrepancyAllForm" property="calendarPresent" value="true">
				                      			<html:select property="fiscalCalId" styleClass="dr-menu">
													<html:option value="4">Gregorian</html:option>
													<html:option value="1">Eth. Calendar</html:option>
													<html:option value="-1">Eth. Fiscal Year</html:option>
												</html:select>
												</logic:equal>
				              				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyAllForm" property="yearRangePresent" value="true">
	                              					<strong>
	                              						<digi:trn key="aim:year">Year</digi:trn>&nbsp;
	                              					</strong>
	                              				</logic:equal>
				              				</td>
				              				<td>
				              					<logic:equal name="aimYearlyDiscrepancyAllForm" property="yearRangePresent" value="true">
							                      			<strong><digi:trn key="aim:from">From</digi:trn>:</strong>
							                     </logic:equal>
				              				</td>
							              	<td> 
							                	<logic:equal name="aimYearlyDiscrepancyAllForm" property="yearRangePresent" value="true">
					                      			<b>
					                      			<html:select property="fromYear" styleClass="dr-menu">
					                      				<html:optionsCollection name="aimYearlyDiscrepancyAllForm" property="years" value="year" label="year"/>
													</html:select>
													</b>
												</logic:equal>
							              	</td>
							              	<td>
							              		<logic:equal name="aimYearlyDiscrepancyAllForm" property="yearRangePresent" value="true">
					                      			<strong><digi:trn key="aim:to">To</digi:trn>:</strong>
					                      		</logic:equal>
							              	</td>
							              	<td>
							              		<logic:equal name="aimYearlyDiscrepancyAllForm" property="yearRangePresent" value="true">
					                      			<b> 
					                        		<html:select property="toYear" styleClass="dr-menu">
														<html:optionsCollection name="aimYearlyDiscrepancyAllForm" property="years" value="year" label="year"/>
													</html:select>
					                        		</b>
					                        	</logic:equal> 
							              	</td>
							              	<td> 
							              	<html:hidden property="ampActivityId" />
											<html:hidden property="tabIndex" />
							              	<logic:equal name="aimYearlyDiscrepancyAllForm" property="goButtonPresent" value="true">
												<html:submit styleClass="dr-menu"><digi:trn key="btn:go">Go</digi:trn></html:submit>
											</logic:equal> 
							                </td>
							              	<td>&nbsp;</td>
				            			</tr>
				          			</table>
				          			<table width="100%"  border="0" cellpadding="3" cellspacing="1" class="box-border">
				            			<tr bgcolor="#DDDDDB" > 
				              				<td height="30" bgcolor="#DDDDDB">
				              					<div align="center">
				              					<digi:trn key="aim:year">Year</digi:trn>
				               					</div>
				                  			</td>
							              	<td bgcolor="#DDDDDB" colspan="3">
							              		<div align="center"> 
							                  		<p>
							                  		<digi:trn key="aim:discrepancyCommitments">Commitments</digi:trn>
							                  		</p>
							                	</div>
							                </td>
							              	
                                            <td bgcolor="#DDDDDB" colspan="3">
							              		<div align="center">
							              			<digi:trn key="aim:discrepancyDisbursements">Disbursements</digi:trn>
							              		</div>
							              	</td>
                                          
                                            <feature:display module="Funding" name="Expenditures">
							              	<td bgcolor="#DDDDDB" colspan="3">
							              		<div align="center">
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
							                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>
							                  		</p>
							                	</div>
							                </td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:implAgencyActuals">Impl. Agency Actuals</digi:trn>
							              		</div>
							              	</td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>
							              		</div>
							              	</td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center"> 
							                  		<p>
							                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>
							                  		</p>
							                	</div>
							                </td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:implAgencyActuals">Impl. Agency Actuals</digi:trn>
							              		</div>
							              	</td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>
							              		</div>
							              	</td>
							              	<feature:display module="Funding" name="Expenditures">
                                            <td bgcolor="#DDDDDB">
							              		<div align="center"> 
							                  		<p>
							                  		<digi:trn key="aim:donorActuals">Donor Actuals</digi:trn>
							                  		</p>
							                	</div>
							                </td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:implAgencyActuals">Impl. Agency Actuals</digi:trn>
							              		</div>
							              	</td>
							              	<td bgcolor="#DDDDDB">
							              		<div align="center">
							              			<digi:trn key="aim:mofedActuals">MOFED Actuals</digi:trn>
							              		</div>
							              	</td>
                                            </feature:display>
				            			</tr>
				            			<logic:empty name="aimYearlyDiscrepancyAllForm" property="yearlyDiscrepanciesAll" >
			                        		<tr valign="top"> 
			                          			<td colspan="8" align="center">
			                          			<span class="note"><digi:trn key="aim:noRecords">No records!</digi:trn></span>
			                          			</td>
			                          		</tr>
			                        	</logic:empty>
				            			<logic:notEmpty name="aimYearlyDiscrepancyAllForm" property="yearlyDiscrepanciesAll">
										<logic:iterate name="aimYearlyDiscrepancyAllForm" property="yearlyDiscrepanciesAll" id="discrepancy" type="org.digijava.module.aim.helper.YearlyDiscrepancyAll">
							            <tr valign="top"> 
							              <td height="30">
							              <logic:equal name="discrepancy" property="fiscalYear" value="0">
				                          		NA
				                          	</logic:equal>
				                          	<logic:notEqual  name="discrepancy" property="fiscalYear" value="0">
													<bean:write name="discrepancy" property="fiscalYear"/>
											</logic:notEqual>
								              	
							              </td>
							              <td>
							              	<div align="right">
							              		<bean:write name="discrepancy" property="commitmentDonorActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="center">
							              		<bean:write name="discrepancy" property="commitmentImplAgencyActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="right"> 
							                  <bean:write name="discrepancy" property="commitmentMofedActual"/>
							                </div></td>
							              <td>
							              	<div align="right">
							              		<bean:write name="discrepancy" property="disbursementDonorActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="center">
							              		<bean:write name="discrepancy" property="disbursementImplAgencyActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="right"> 
							                  <bean:write name="discrepancy" property="disbursementMofedActual"/>
							                </div>
							              </td>
							              	<feature:display module="Funding" name="Expenditures">
                                           <td>
							              	<div align="right">
							              		<bean:write name="discrepancy" property="expenditureDonorActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="center">
							              		<bean:write name="discrepancy" property="expenditureImplAgencyActual"/>
							              	</div>
							              </td>
							              <td>
							              	<div align="right"> 
							                  <bean:write name="discrepancy" property="expenditureMofedActual"/>
							                </div>
							              </td>
                                          </feature:display>
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
		        						<bean:write name='aimYearlyDiscrepancyAllForm' property='transactionType'/>
		        					</c:set>
		<c:set var="translation">
			<digi:trn key="aim:clickToViewAllQuarterlyDiscrepancy">Click here to view All Quarterly Discrepancy</digi:trn>
		</c:set>
		        					<digi:link href="/viewQuarterlyDiscrepancyAll.do" name="urlSubTabs" title="${translation}" >
		        						<strong>
										<digi:trn key="aim:showQuarterly">Show Quarterly </digi:trn>
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



