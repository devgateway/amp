<%-- 
    Document   : viewMonthlyInfo
    Created on : Mar 25, 2008, 7:12:21 PM
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

<digi:instance property="aimMonthlyInfoForm" />

<digi:context name="digiContext" property="context"/>





<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlSubTabs}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlSubTabs}" property="ampFundingId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>
    
</c:set>

<c:set target="${urlSubTabs}" property="tabIndex" value="2"/>

<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<c:set target="${urlSubTabs}" property="currency"  >
	<bean:write name="aimMonthlyInfoForm" property="currency"/>
</c:set>


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



<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlAll}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlAll}" property="ampFundingId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>
    
</c:set>

<c:set target="${urlAll}" property="tabIndex" value="2"/>
<c:set target="${urlAll}" property="currency"  >
	<bean:write name="aimMonthlyInfoForm" property="currency"/>
</c:set>



<jsp:useBean id="urlMonthlyGrouping" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlMonthlyGrouping}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlMonthlyGrouping}" property="ampFundingId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>
    
</c:set>

<c:set target="${urlMonthlyGrouping}" property="tabIndex" value="2"/>

<c:set target="${urlMonthlyGrouping}" property="transactionType">
    
    <bean:write name="aimMonthlyInfoForm" property="transactionType"/>
    
</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex" value="2"/>

<c:set target="${urlDiscrepancy}" property="transactionType">
    
    <bean:write name="aimMonthlyInfoForm" property="transactionType"/>
    
</c:set>


<digi:form action="/monthlyInfo.do" name="aimMonthlyInfoForm" type="org.digijava.module.aim.form.MonthlyInfoForm" method="get" styleId="myForm">
    
    
    
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
                            
                            
                            
                            <TABLE width="100%" border=0 align="center" cellPadding=3 cellSpacing=3 bgcolor="#f4f4f4" vAlign="top">
                                
                              <TR height="20"><TD height="20">
				<div id="subtabsFinancial">
                                        
                                        <c:set var="translation">
                                            
                                            <digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>
                                            
                                        </c:set>
                                        
                                        <digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" title="${translation}" >
                                            
                                            <digi:trn key="aim:overview">OVERVIEW</digi:trn>
                                            
                                        </digi:link> |
                                        
                                      
			  		<logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="0">

						<c:set var="translation">

							<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>

						</c:set>

						<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >

			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			  			</digi:link>

					</logic:notEqual>

					<logic:equal name="aimMonthlyInfoForm" property="transactionType" value="0">

			      	<span>

			      		<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>

			      	</span>

			  		</logic:equal> |                                       
                                     
                                        <field:display name="Disbursement Orders Tab" feature="Disbursement Orders">
                                            <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="4">
                                                
                                                <c:set target="${urlSubTabs}" property="transactionType" value="4"/>
                                                
                                                <c:set var="translation">
                                                    
                                                    <digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursement Orders</digi:trn>
                                                    
                                                </c:set>
                                                
                                                <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" title="${translation}" >
                                                    
                                                    <digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>
                                                    
                                                </digi:link>
                                                
                                            </logic:notEqual>
                                            
                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="4">
                                            
                                                <span>
                                                    
                                                    <digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>
                                                    
                                                </span>
                                                
                                            </logic:equal>|
                                            
                                        </field:display>
                                        
                                        <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="1">
                                            
                                            <c:set target="${urlSubTabs}" property="transactionType" value="1"/>
                                            
                                            <c:set var="translation">
                                                
                                                <digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
                                                
                                            </c:set>
                                            
                                            <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" title="${translation}" >
                                                
                                                <digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
                                                
                                            </digi:link>
                                            
                                        </logic:notEqual>
                                        
                                        <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="1">
                                        
                                            <span>
                                                
                                                <digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
                                                
                                            </span>
                                            
                                        </logic:equal>|
                                        
                                        
                                        <feature:display module="Funding" name="Expenditures">
                                            <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="2">
                                                
                                                <c:set target="${urlSubTabs}" property="transactionType" value="2"/>
                                                
                                                <c:set var="translation">
                                                    
                                                    <digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
                                                    
                                                </c:set>
                                                
                                                
                                                
                                                <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" title="${translation}" >
                                                    
                                                    <digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
                                                    
                                                </digi:link>
                                                
                                                
                                            </logic:notEqual>
                                            
                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">
                                            
                                                <span>
                                                    
                                                    <digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
                                                    
                                                </span>
                                                
                                            </logic:equal>	|
                                        </feature:display>
                                        <c:set var="translation">
                                            
                                            <digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
                                            
                                        </c:set>
                                        
                                        <digi:link href="/viewMonthlyComparisons.do" name="urlAll" title="${translation}" >
                                            
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
                                                            
                                                            <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
                                                            
                                                        </c:set>
                                                        
                                                        <c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>
                                                        
                                                        <c:set var="translation">
                                                            
                                                            <digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
                                                            
                                                        </c:set>
                                                        
                                                        <digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="${translation}" >
                                                            
                                                            <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
                                                            
                                                        </digi:link>
                                                        
                                                        
                                                        
                                                        
                                                        
                                                        <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="4">
                                                            &gt;
                                                            
                                                            <digi:trn key="aim:MonthlyDisbursementOrders">Monthly Disbursement Orders</digi:trn>
                                                            
                                                        </logic:equal>
                                                        
                                                        
                                                        
                                                        <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="1">
                                                            &gt;
                                                            
                                                            <digi:trn key="aim:MonthlyDisbursements">Monthly Disbursements</digi:trn>
                                                            
                                                        </logic:equal>
                                                        
                                                        <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">
                                                             &gt;
                                                            
                                                            <digi:trn key="aim:MonthlyExpenditures">Monthly Expenditures</digi:trn>
                                                            
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
                                    
                                  <TD vAlign="top" align="center" bgColor="#f4f4f2">
                                        
                              <TABLE width="100%" border=0 align=center cellPadding=0 cellSpacing=0 bgColor="#f4f4f2">
                                            
                  <TR bgcolor="#ffffff">
                                                
                                                <TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">
                                                    
                                                    <TABLE cellSpacing=2 cellPadding=0 border=0 bgColor="#ffffff" width="100%" vAlign="top" align="left"
                                                           
                                                         >
                                                        
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
                                                                                
                                                                                <input type="button" styleClass="dr-menu" onclick="javascript:go();" value="<digi:trn>Go</digi:trn>"/>
                                                                                
                                                                            </TD>
                                                                            
                                                                        </TR>
                                                                        
                                                                    </TABLE>
                                                                    
                                                                </logic:equal>
                                                                
                                                        </TD></TR>
                                                        
                                                        <TR>
<TD>
                                                                
                                                                <TABLE width="100%" border=0 align="left" cellPadding=0 cellSpacing=0 bgColor=#ffffff vAlign="top">
                                                                    
                                                                  <TR>
                                                                    <TD>
                                                                        
                                                                        
                                                                        
                                                                     
                                                                        
                                                          <TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding" id="dataTable">
                                                                            
      <tr bgcolor="#999999" >
                                                                                
                                                                                <td width="21%" bgcolor="#999999"  >
                                                                                    
                                                                                    <div align="center" style="font-weight:bold;color:black;">
                                                                                        
                                                                                        <digi:trn key="aim:year">Year</digi:trn>
                                                                                    </div>                                                                              </td>
                                                                                
                                                                                <td width="25%" bgcolor="#999999">
                                                                                    
                                                                                    <div align="center" style="font-weight:bold;color:black;">
                                                                                        
                                                                                        <digi:trn key="aim:dateDisbursed">Date Disbursed</digi:trn>
                                                                                    </div>                                                                              </td>
                                                                                <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                
                                                                                <td width="24%" bgcolor="#999999">
                                                                                    
                                                                                    <div align="center" style="font-weight:bold;color:black;">
                                                                                        
                                                                                        <FONT color="blue">*</FONT>
                                                                                        
                                                                                        <digi:trn key="aim:plannedAmount">Planned Amount</digi:trn>
                                                                                    </div>                                                                                </td>
                                                                            </c:if>
                                                                                
                                                                                <td width="30%" bgcolor="#999999">
                                                                                    
                                                                                    <div align="center" style="font-weight:bold;color:black;">
                                                                                        
                                                                                        <FONT color="blue">*</FONT>
                                                                                        
                                                                                        <digi:trn key="aim:actualAmount">Actual Amount</digi:trn>
                                                                                    </div>                                                                              </td>
                                                            </tr>
                                                                            
                                                                            <logic:empty name="aimMonthlyInfoForm" property="monthlyInfoList">
                                                                            
                                                                                <tr valign="top">
                                                                                    
                                                                                    <td colspan="5" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>
                                                                                </tr>
                                                                            </logic:empty>
                                                                            
                                                                            <logic:notEmpty name="aimMonthlyInfoForm" property="monthlyInfoList">
                                                                                
                                                                                <logic:iterate name="aimMonthlyInfoForm" property="monthlyInfoList"
                         
                         id="month" type="org.digijava.module.aim.helper.MonthlyInfo">
                                                                                    <td align="left" valign="baseline">
                                                                                        
                                                                                        ${month.year}                                                                                    </td>
                                                                                    
                                                                                    
                                                                                    
                                                                                    <td valign="baseline"  align="right">
                                                                                      <digi:trn key='aim:monthly:${month.month}'>${month.month}</digi:trn>                                                                                     </td>
                                                                                    
                                                                                     <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                    <td align="center" valign="baseline">
                                                                                       <aim:formatNumber  value="${month.plannedAmount}"/>
                                                                                    </td>
                                                                                    </c:if>
                                                                                    
                                                                                    <td valign="baseline" align="right" style="padding-right:5px">
                                                                                        
                                                                                      <aim:formatNumber  value="${month.actualAmount}"/>                                                                                    </td>
                                                                                    
                                                                                    
                                                                                </tr>    
                                                                            </logic:iterate>
                                                                            <tr>
                                                                            	<td align="left" valign="baseline">
                                                                            		<span class="note"> <font color="blue">*</font>
                              															<digi:trn key="aim:total">Total</digi:trn>
                  																	</span>
                  																</td>
                                                                                <td valign="baseline">&nbsp;</td>
                                                                                <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                	<td align="center" class="note" valign="baseline"><aim:formatNumber  value="${aimMonthlyInfoForm.totalPlanned}"/></td>
                                                                                </c:if>
                                                                                <td valign="baseline" class="note" align="right" style="padding-right:5px">
																					<aim:formatNumber  value="${aimMonthlyInfoForm.totalActual}"/>
																				</td>
																			</tr>
                                                                       	</logic:notEmpty>
                                                                    </TABLE>
                                                                    
                                                               
                                                                
                                                        </TD></TR>
                                                        
                                                        
                                                        
                                                        <TR>
                                                          <TD align="right">                                                            <br />
                                                     <TABLE cellspacing="0" cellpadding="0" vAlign="top">
                                                                     <c:set target="${urlSubTabs}" property="transactionType">
                                                                                
                                                                                <bean:write name='aimMonthlyInfoForm' property='transactionType'/>
                                                                                
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
<script>
function go() {
	document.getElementById("myForm").action = "/aim/viewMonthlyInfo.do";
	document.getElementById("myForm").submit();
}

if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>
    
    
    
    