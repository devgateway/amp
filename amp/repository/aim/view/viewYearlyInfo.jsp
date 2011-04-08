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

<c:set target="${urlSubTabs}" property="tabIndex" >
	<bean:write name="aimYearlyInfoForm" property="tabIndex"/>
</c:set>

<c:set target="${urlSubTabs}" property="transactionType" >
	<%=new String("0")%>
</c:set>

<c:set target="${urlSubTabs}" property="currency"  >
	<bean:write name="aimYearlyInfoForm" property="currency"/>
</c:set>



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
<c:set target="${urlAll}" property="currency"  >
	<bean:write name="aimYearlyInfoForm" property="currency"/>
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


<digi:form action="/yearlyInfoFilter.do" name="aimYearlyInfoForm" type="org.digijava.module.aim.form.YearlyInfoForm" method="get" styleId="myForm">



<html:hidden property="ampActivityId" />

<html:hidden property="ampFundingId" />

<html:hidden property="transactionType" />

<html:hidden property="tabIndex" />


<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">

<TR>

	<TD vAlign="top" align="center">

		<!-- contents -->



			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">

			<TR><TD bgcolor="#f4f4f4">



			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border="0">

				<TR height="20"><TD height="20">

				<div id="subtabsFinancial">

					<c:set var="translation">

						<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>

					</c:set>

					<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" title="${translation}" >

              		<digi:trn key="aim:overview">OVERVIEW</digi:trn>

					</digi:link>|

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="0">

<c:set var="translation">

	<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

</c:set>

              		<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  			</digi:link>

					</logic:notEqual>

					<logic:equal name="aimYearlyInfoForm" property="transactionType" value="0">

	            	<span >

	              		<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

	              	</span>

              	</logic:equal> |
                 <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">
                	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="4">

              		<c:set target="${urlSubTabs}" property="transactionType" >
              			<%=new String("4")%>
              		</c:set>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewDisbursementOrderss">Click here view Disbursement Orders</digi:trn>

						</c:set>

			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>

			  			</digi:link>

              	</logic:notEqual>

              	<logic:equal name="aimYearlyInfoForm" property="transactionType" value="4">

	            	<span>

	              		<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>

	              	</span>

              	</logic:equal>|
                </field:display>

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="1">

              		<c:set target="${urlSubTabs}" property="transactionType">
              			<%=new String("1")%>
					</c:set>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewDisbursements">Click here view Disbursements</digi:trn>

						</c:set>

			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

			  			</digi:link>

              	</logic:notEqual>

              	<logic:equal name="aimYearlyInfoForm" property="transactionType" value="1">

	            	<span>

	              		<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>

	              	</span>

              	</logic:equal>|

              	<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="2">

               	<c:set target="${urlSubTabs}" property="transactionType" >
               		<%=new String("2")%>
               	</c:set>

			<c:set var="translation">

				<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>

			</c:set>
<feature:display module="Funding" name="Expenditures">
			  			<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

			  			</digi:link>|
</feature:display>
               </logic:notEqual>

               <logic:equal name="aimYearlyInfoForm" property="transactionType" value="2">
				
				<feature:display module="Funding" name="Expenditures">
	            	<span>

	              		<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>

	              	</span>|
				</feature:display>
              	</logic:equal>	


			<c:set var="translation">

				<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>

			</c:set>

              	<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="${translation}" >

						<digi:trn key="aim:all">ALL</digi:trn>

					</digi:link>

				</TD></TR>

				<TR bgColor=#f4f4f2>

            	<TD align=left>
            		
					</TD>

				</TR>



				<TR bgColor=#f4f4f2>

					<TD vAlign="top" align="center" width="100%" bgColor="#f4f4f2">
						

						<TABLE cellspacing="0" cellpadding="0" width="100%" align="center" bgColor="#f4f4f2" border="0">

							<TR bgcolor="#ffffff">

								<TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">

             					<TABLE cellSpacing=2 cellpadding="0" border="0" bgColor="#ffffff" width="100%" vAlign="top" align="left"

									bgColor="#ffffff">

										<TR><TD bgColor="#ffffff">

											<logic:equal name="aimYearlyInfoForm" property="goButtonPresent" value="true">

											<TABLE cellSpacing=2 cellpadding="0" border="0" bgColor=#ffffff vAlign="top" align="left">

   		              				<TR>

												<logic:equal name="aimYearlyInfoForm" property="currencyPresent" value="true">

      	    	       					<TD>

													<TABLE cellSpacing=2 cellpadding="0">

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

													<TABLE cellSpacing=2 cellpadding="0">

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

													<TABLE cellSpacing=2 cellpadding="0">

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

													<input type="button" styleClass="dr-menu" onclick="javascript:go();" value="<digi:trn>Go</digi:trn>"/>
													

			                        	</TD>

											</TR>

											</TABLE>

											</logic:equal>



										</TD></TR>

										<TR><TD>


											<TABLE cellspacing="0" cellpadding="0" border="0" bgColor=#ffffff width="100%" vAlign="top" align="left">

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


										</TD></TR>

										<TR><TD>

						            	<TABLE cellspacing="0" cellpadding="0" border="0" bgColor=#ffffff width="100%" vAlign="top" align="left">

												<TR><TD>

                      							<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding" id="dataTable">

					                        		<tr bgcolor="#999999" >

               					           			<td bgcolor="#999999">

					                          				<div align="center" style="font-weight:bold;color:black;">

																	<digi:trn key="aim:year">Year</digi:trn>

																	</div>

   					                       			</td>
									                    <logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="4">
									                    	<td bgcolor="#999999">
									                    		<div align="center" style="font-weight:bold;color:black;">
																	<digi:trn key="aim:plannedAmount">Planned Amount</digi:trn>
																</div>
															</td>
														</logic:notEqual>	
									                 <td bgcolor="#999999" align="center" style="font-weight:bold;">
															<font color="blue">*</font>
														  <span style="color:black;">
																<digi:trn key="aim:actualAmount">Actual Amount</digi:trn>
														 </span>
													</td>

									              		</tr>

				   					           		<logic:notEmpty name="aimYearlyInfoForm" property="yearlyInfo">
														<logic:iterate name="aimYearlyInfoForm" property="yearlyInfo" id="yearlyInfo" type="org.digijava.module.aim.helper.YearlyInfo">
																<tr valign="top">
																	<td>
																	<logic:equal name="yearlyInfo" property="fiscalYear" value="0">NA</logic:equal>
																	<logic:notEqual  name="yearlyInfo" property="fiscalYear" value="0">
																		<bean:write name="yearlyInfo" property="fiscalYear" />
																	</logic:notEqual>
																</td>
																<logic:notEqual  name="aimYearlyInfoForm" property="transactionType" value="4">
																<td>
		         					                 				<div align="right">
		         					                 					<logic:present name="debug">
		         					                 						<%=yearlyInfo.getWrapedPlanned()%>
		         					                 					</logic:present>
		         					                 					<logic:notPresent name="debug">
		         					                 						<%=FormatHelper.formatNumber(yearlyInfo.getPlannedAmount())%>
		         					                 					</logic:notPresent>
																	</div>
																</td>
																</logic:notEqual>
																<td>
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
													<tr valign="top" >
														<td>
                                                        	<span class="note">
															<digi:trn key="aim:total">Total</digi:trn>
                                                            </span>
														</td>
														<logic:notEqual name="aimYearlyInfoForm" property="transactionType" value="4">
														<td>
															<div align="right" class="note">
																<bean:write name="aimYearlyInfoForm" property="totalPlanned" />
															</div>
														</td>
														</logic:notEqual>
														<td>
															<div align="right" class="note">
																<bean:write name="aimYearlyInfoForm" property="totalActual" />
															</div>
														</td>
													</tr>
												</TABLE>


												</TD></TR>

												<TR><TD>


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
	document.getElementById("myForm").action = "/aim/viewYearlyInfo.do";
	document.getElementById("myForm").submit();
}
</script>