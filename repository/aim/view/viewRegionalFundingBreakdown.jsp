<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="text/javascript">
	
function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimRegionalFundingForm.action = "<%=addUrl%>~pageId=1~step=4~action=edit~surveyFlag=true~activityId=" + id;
	document.aimRegionalFundingForm.target = "_self";    
   document.aimRegionalFundingForm.submit();
}

function preview(id)
{

	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimRegionalFundingForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimRegionalFundingForm.target = "_self";
   document.aimRegionalFundingForm.submit();
}

</script>

<html:errors/>

<digi:instance property="aimRegionalFundingForm" />
<digi:form action="/viewRegionalFundingBreakdown.do" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="regionId" />
<html:hidden property="tabIndex" />

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      	<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>					
													<jsp:useBean id="url" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${url}" property="ampActivityId">
														<bean:write name="aimRegionalFundingForm" property="ampActivityId"/>
													</c:set>
													<c:set target="${url}" property="tabIndex" value="4"/>
													<bean:define id="translation">
															<digi:trn key="aim:clickToViewRegionalFunding">Click here to view regional funding</digi:trn>
													</bean:define>
													<digi:link href="/viewRegionalFundingBreakdown.do" name="url" 
													styleClass="comment" title="<%=translation%>" >
													<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:actOverview">Overview</digi:trn>&nbsp;&gt;&nbsp;
													<bean:write name="aimRegionalFundingForm" property="perspective"/>
													<bean:define id="perspectiveNameTrimedLocal" name="aimRegionalFundingForm" property="perspectiveNameLocaly" type="java.lang.String"/>
													<digi:trn key='<%="aim:"+ perspectiveNameTrimedLocal %>'>	
														<bean:write name="aimRegionalFundingForm" property="perspective"/></digi:trn>
												</SPAN>											
											</TD>
											<TD align="right">
												&nbsp;
											</TD>
											<TD align=right>
												<input type="button" value="<digi:trn key="aim:physical:Preview">Preview</digi:trn>" class="dr-menu"
												onclick="preview(<c:out value="${aimRegionalFundingForm.ampActivityId}"/>)">
												<input type="button" value="<digi:trn key="aim:physical:edit">Edit</digi:trn>" class="dr-menu"
												onclick="fnEditProject(<c:out value="${aimRegionalFundingForm.ampActivityId}"/>)">
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>

							<c:if test="${aimRegionalFundingForm.goButton == true}">
							<TR bgColor=#f4f4f2>
      	      	<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<c:if test="${aimRegionalFundingForm.currFilter == true}">
												Currency :
												<html:select property="currFilterValue" styleClass="dr-menu">
  		       		        	<html:optionsCollection name="aimRegionalFundingForm" 
													property="currencies" value="currencyCode" label="currencyName"/>
												</html:select>&nbsp;&nbsp;&nbsp;
												</c:if>
												<c:if test="${aimRegionalFundingForm.calFilter == true}">
												Calendar :
												<html:select property="calFilterValue" styleClass="dr-menu">
  		       		        	<html:optionsCollection name="aimRegionalFundingForm" 
													property="fiscalCalendars" value="ampFiscalCalId" label="name"/>
												</html:select>												
												</c:if>
												<html:submit value="GO!" styleClass="dr-menu"/>
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>							
							</c:if>
							
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
                        	<TR bgcolor="#F4F4F2"> 
                          	<TD bgcolor="#C9C9C7" class="box-title" height="17">&nbsp;&nbsp;
															<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif" 
														height=17 width=17></TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" > 
	                        	<TD><digi:trn key="aim:region">Region</digi:trn></TD>
														<TD><digi:trn key="aim:totalCommitted">Total Committed</digi:trn></TD>
	                         	<TD><digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn></TD>
														<TD><digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn></TD>
	                         	<TD><digi:trn key="aim:totalExpended">Total Expended</digi:trn></TD>
														<TD><digi:trn key="aim:unExpendedFunds">Unexpended Funds</digi:trn></TD>
													</TR>
			                    <logic:notEmpty name="aimRegionalFundingForm" property="regionalFundings">
														<logic:iterate name="aimRegionalFundingForm" property="regionalFundings" id="fd" 
			  	                   type="org.digijava.module.aim.helper.RegionalFunding">
															<TR valign="top" bgcolor="#f4f4f2"> 
					    	           			<TD>
																	<c:set target="${url}" property="regionId">
																		<bean:write name="fd" property="regionId"/>
																	</c:set>
																	<digi:link href="/viewRegFundDetails.do" name="url">
																	<bean:write name="fd" property="regionName"/></digi:link>
																</TD>
							                  <TD align="right"><bean:write name="fd" property="totCommitments"/></TD>
							                  <TD align="right"><bean:write name="fd" property="totDisbursements"/></TD>
					      			          <TD align="right"><bean:write name="fd" property="totUnDisbursed"/></TD>
								                <TD align="right"><bean:write name="fd" property="totExpenditures"/></TD>
					            		      <TD align="right"><bean:write name="fd" property="totUnExpended"/></TD>
															</TR>
														</logic:iterate>
													</logic:notEmpty>
				                  <TR valign="top" class="note"> 
														<TD><digi:trn key="aim:total">Total</digi:trn></TD>
														<TD align="right"><bean:write name="aimRegionalFundingForm" property="totCommitments"/></TD>
														<TD align="right"><bean:write name="aimRegionalFundingForm" property="totDisbursements"/></TD>
														<TD align="right"><bean:write name="aimRegionalFundingForm" property="totUnDisbursed"/></TD>
														<TD align="right"><bean:write name="aimRegionalFundingForm" property="totExpenditures"/></TD>
														<TD align="right"><bean:write name="aimRegionalFundingForm" property="totUnExpended"/></TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD>
												<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">	
													All the amounts are in thousands (000)</digi:trn>
												</FONT>								
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
	<TR><TD>&nbsp;</TD></TR>	
</TABLE>
</digi:form>
