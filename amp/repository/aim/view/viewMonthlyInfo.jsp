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



<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlAll}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlAll}" property="ampFundingId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>
    
</c:set>

<c:set target="${urlAll}" property="tabIndex" value="1"/>



<jsp:useBean id="urlMonthlyGrouping" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlMonthlyGrouping}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlMonthlyGrouping}" property="ampFundingId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampFundingId"/>
    
</c:set>

<c:set target="${urlMonthlyGrouping}" property="tabIndex" value="1"/>

<c:set target="${urlMonthlyGrouping}" property="transactionType">
    
    <bean:write name="aimMonthlyInfoForm" property="transactionType"/>
    
</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${urlDiscrepancy}" property="ampActivityId">
    
    <bean:write name="aimMonthlyInfoForm" property="ampActivityId"/>
    
</c:set>

<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>

<c:set target="${urlDiscrepancy}" property="transactionType">
    
    <bean:write name="aimMonthlyInfoForm" property="transactionType"/>
    
</c:set>



<digi:form action="/viewMonthlyInfo.do" name="aimMonthlyInfoForm" type="org.digijava.module.aim.form.MonthlyInfoForm" method="post">
    
    
    
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
                                            <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="4">
                                                
                                                <c:set target="${urlSubTabs}" property="transactionType" value="4"/>
                                                
                                                <c:set var="translation">
                                                    
                                                    <digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursement Orders</digi:trn>
                                                    
                                                </c:set>
                                                
                                                <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
                                                    
                                                    <digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>
                                                    
                                                </digi:link>
                                                
                                            </logic:notEqual>
                                            
                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="4">
                                            
                                                <span class="sub-nav2-selected">
                                                    
                                                    <digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>
                                                    
                                                </span>
                                                
                                            </logic:equal>|
                                            
                                        </field:display>
                                        
                                        <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="1">
                                            
                                            <c:set target="${urlSubTabs}" property="transactionType" value="1"/>
                                            
                                            <c:set var="translation">
                                                
                                                <digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
                                                
                                            </c:set>
                                            
                                            <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
                                                
                                                <digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
                                                
                                            </digi:link>
                                            
                                        </logic:notEqual>
                                        
                                        <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="1">
                                        
                                            <span class="sub-nav2-selected">
                                                
                                                <digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
                                                
                                            </span>
                                            
                                        </logic:equal>|
                                        
                                        
                                        <feature:display module="Funding" name="Expenditures">
                                            <logic:notEqual name="aimMonthlyInfoForm" property="transactionType" value="2">
                                                
                                                <c:set target="${urlSubTabs}" property="transactionType" value="2"/>
                                                
                                                <c:set var="translation">
                                                    
                                                    <digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
                                                    
                                                </c:set>
                                                
                                                
                                                
                                                <digi:link href="/viewMonthlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
                                                    
                                                    <digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
                                                    
                                                </digi:link>
                                                
                                                
                                            </logic:notEqual>
                                            
                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">
                                            
                                                <span class="sub-nav2-selected">
                                                    
                                                    <digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
                                                    
                                                </span>
                                                
                                            </logic:equal>	|
                                        </feature:display>
                                        <c:set var="translation">
                                            
                                            <digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
                                            
                                        </c:set>
                                        
                                        <digi:link href="/viewMonthlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="${translation}" >
                                            
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
                                                        <logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
                                                        
                                                            &gt;
                                                            <digi:trn key="aim:${aimMonthlyInfoForm.perpsectiveName}">
                                                            <bean:write name="aimMonthlyInfoForm" property="perpsectiveName"/></digi:trn>&nbsp;
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
                                                                            
                                                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="4">
                                                                                
                                                                                <digi:trn key="aim:MonthlyDisbursementOrders">Monthly Disbursement Orders</digi:trn>
                                                                                
                                                                            </logic:equal>
                                                                            
                                                                            
                                                                            
                                                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="1">
                                                                                
                                                                                <digi:trn key="aim:MonthlyDisbursements">Monthly Disbursements</digi:trn>
                                                                                
                                                                            </logic:equal>
                                                                            
                                                                            <logic:equal name="aimMonthlyInfoForm" property="transactionType" value="2">
                                                                                
                                                                                <digi:trn key="aim:MonthlyExpenditures">Monthly Expenditures</digi:trn>
                                                                                
                                                                            </logic:equal>
                                                                            
                                                                        </TD>
                                                                        
                                                                        <TD width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">
                                                                            
                                                                        </TD>
                                                                        
                                                                    </TR>
                                                                    
                                                                </TABLE>
                                                                
                                                            </TD>
                                                            
                                                            <TD vAlign="top" align="right">
                                                                
                                                                <logic:equal name="aimMonthlyInfoForm" property="perspectivePresent" value="true">
                                                                
                                                                    <TABLE cellSpacing="2" cellPadding="0" vAlign="top" bgColor=#f4f4f2>
                                                                        
                                                                        <TR>
                                                                            
                                                                            <TD>
                                                                                
                                                                                <STRONG>Perspective:</STRONG>
                                                                                
                                                                            </TD>
                                                                            
                                                                            <TD>
                                                                                
                                                                                <html:select property="perspective" styleClass="dr-menu">
                                                                                    
                                                                                    <html:optionsCollection name="aimMonthlyInfoForm"

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
                                                                    
                                                                    <TR><TD>
                                                                        
                                                                        
                                                                        
                                                                     
                                                                        
                                                                            <TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">
                                                                            
                                                                            <tr bgcolor="#DDDDDB" >
                                                                                
                                                                                <td bgcolor="#DDDDDB"  >
                                                                                    
                                                                                    <div align="center">
                                                                                        
                                                                                        <digi:trn key="aim:year">Year</digi:trn>
                                                                                        
                                                                                    </div>
                                                                                    
                                                                                </td>
                                                                                
                                                                                <td bgcolor="#DDDDDB">
                                                                                    
                                                                                    <div align="center">
                                                                                        
                                                                                        <digi:trn key="aim:dateDisbursed">Date Disbursed</digi:trn>
                                                                                        
                                                                                    </div>
                                                                                    
                                                                                </td>
                                                                                <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                
                                                                                <td bgcolor="#DDDDDB">
                                                                                    
                                                                                    <div align="center">
                                                                                        
                                                                                        <FONT color="blue">*</FONT>
                                                                                        
                                                                                        <digi:trn key="aim:plannedAmount">Planned Amount</digi:trn>
                                                                                        
                                                                                    </div>
                                                                                    
                                                                                </td>
                                                                            </c:if>
                                                                                
                                                                                <td bgcolor="#DDDDDB">
                                                                                    
                                                                                    <div align="center">
                                                                                        
                                                                                        <FONT color="blue">*</FONT>
                                                                                        
                                                                                        <digi:trn key="aim:actualAmount">Actual Amount</digi:trn>
                                                                                        
                                                                                    </div>
                                                                                    
                                                                                </td>
                                                                                
                                                                            </tr>
                                                                            
                                                                            <logic:empty name="aimMonthlyInfoForm" property="monthlyInfoList">
                                                                            
                                                                                <tr valign="top">
                                                                                    
                                                                                    <td colspan="5" align="center"><span class="note"><digi:trn key="aim:noRecords">No records</digi:trn>!</td>
                                                                                    
                                                                                </tr>
                                                                                
                                                                            </logic:empty>
                                                                            
                                                                            <logic:notEmpty name="aimMonthlyInfoForm" property="monthlyInfoList">
                                                                                
                                                                                <logic:iterate name="aimMonthlyInfoForm" property="monthlyInfoList"
                         
                         id="month" type="org.digijava.module.aim.helper.MonthlyInfo">
                                                                                    <td valign="baseline" bgcolor="#F8F8F5">
                                                                                        
                                                                                        ${month.year}

                                                                                    </td>
                                                                                    
                                                                                    
                                                                                    
                                                                                    <td valign="baseline" bgcolor="#F8F8F5">
                                                                                        
                                                                                        <div align="right"><digi:trn key='aim:monthly:${month.month}'>${month.month}</digi:trn> </div>
                                                                                        
                                                                                    </td>
                                                                                    
                                                                                     <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                    <td valign="baseline" bgcolor="#F8F8F5">
                                                                                        
                                                                                        <div align="right"><bean:write name="month" property="plannedAmount" format="#.##"/></div>
                                                                                        
                                                                                    </td>
                                                                                    </c:if>
                                                                                    
                                                                                    <td valign="baseline" bgcolor="#F8F8F5">
                                                                                        
                                                                                        <div align="right"><bean:write name="month" property="actualAmount" format="#.##"/></div>
                                                                                        
                                                                                    </td>
                                                                                    
                                                                                </tr>
                                                                                
                                                                                
                                                                                
                                                                            </logic:iterate>
                                                                            <tr valign="top" class="note">
                                                                                <c:if test="${aimMonthlyInfoForm.transactionType!=4}">
                                                                                    <td colspan="2">
                                                                                </c:if>
                                                                                 <c:if test="${aimMonthlyInfoForm.transactionType==4}">
                                                                                    <td>
                                                                                </c:if>
														
															<digi:trn key="aim:total">Total</digi:trn>
														</td>
														<td>
															<div align="right">
																<bean:write name="aimMonthlyInfoForm" property="totalPlanned" format="#.##"/>
															</div>
														</td>
														<td>
															<div align="right">
																<bean:write name="aimMonthlyInfoForm" property="totalActual" format="#.##"/>
															</div>
														</td>
									</tr>
                                                                            
                                                                        </logic:notEmpty>
                                                                        
                                                                    </TABLE>
                                                                    
                                                               
                                                                
                                                        </TD></TR>
                                                        
                                                        
                                                        
                                                        <TR><TD align="right">
                                                                
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
    
    
    
    
    
    
    