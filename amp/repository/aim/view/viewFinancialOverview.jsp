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
<digi:instance property="aimFinancialOverviewForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="false">

<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex"  >
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex">
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex">
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>

<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR height="20"><TD height="20">
				<div id="subtabsFinancial">
				<!-- logic:notEqual name="aimFinancialOverviewForm" property="ampActivityId" value="0"-->
	            	<span>
	              		<digi:trn key="aim:overview">OVERVIEW</digi:trn>
	              	</span>
	              	<!-- /logic:notEqual--> 
					<c:set var="translation">
						<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
					</digi:link>
                                        <field:display feature="Disbursement Orders" name="Disbursement Orders Tab">
                                        <c:set target="${urlSubTabs}" property="transactionType" value="4"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewDisbursementOrders">Click here to view Disbursement Orders</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:disbursementOrdersTab">DISBURSEMENT ORDERS</digi:trn>
					</digi:link>
                                        </field:display>
					<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
					</digi:link>
					<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</c:set>
					
                    <feature:display module="Funding" name="Expenditures">
                    	<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
							<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
						</digi:link>
					</feature:display>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyComparisons.do" name="urlAll" title="${translation}" >
					<digi:trn key="aim:all">ALL</digi:trn>
					</digi:link>
                    
                    </div>
				</TD></TR>
				<TR bgColor=#f4f4f2>
            	<TD align=left><html:hidden property="tabIndex" />
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
							<TR>
								<TD align="left">
									<SPAN class=crumb>
								  		<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
											<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
										</c:set>
										<c:set target="${urlFinancingBreakdown}" property="tabIndex" >
											<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
										</c:set>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
										</c:set>
										<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment"
										title="${translation}" >
										<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
										</digi:link> &gt; 
										<digi:trn key="aim:overview">Overview</digi:trn>
									</SPAN>
								</TD>
								<TD align="right">&nbsp;
									
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="750">
						<TABLE width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR>
								<TD width="100%">
        							<TABLE cellSpacing=0 cellPadding=0 width="99%" align=center bgColor=#f4f4f2 border=0>
                					<TR bgColor=#f4f4f2>
					            		<TD width=370 bgColor=#f4f4f2>
					                    	<TABLE cellSpacing=0 cellPadding=0 bgColor=#f4f4f2 border=0>
			      	                		<TR bgColor=#f4f4f2>
			                        			<TD class=box-title noWrap bgColor=#c9c9c7 height="17">&nbsp;
																			<digi:trn key="aim:referenceInformation">Reference Information</digi:trn>
			                          		</TD>
			                        			<TD background="module/aim/images/corner-r.gif" height="17" width="17">
			                          		</TD>
			                          		</TR>
			                  			</TABLE>
			                  		</TD>
          							</TR>
		                			<TR>
											<TD>
												<TABLE width="100%" cellSpacing=2 cellPadding=5 vAlign="top" align="left"
												class="box-border-nopadding">
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:fundingOrganization">
															Funding Organization</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="donor" />
														</TD>
													</TR>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:fundingOrgId">
															Funding Organization Id</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="donorFundingId" />
														</TD>
													</TR>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:typeOfAssistance">
															Type of Assistance</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="termsOfAssistance" />
														</TD>
													</TR>
													<%--
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:signatureDate">
															Signature Date</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="signatureDate" />
														</TD>
													</TR>
													--%>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:conditions">
															Conditions</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="conditions" />
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
			</div>
			</TD></TR>

			</TABLE>
		<!-- end -->
	</TD>
</TR>
<TR><TD>&nbsp;</TD></TR>
</TABLE>
</logic:equal>



