<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>	
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<script type="text/javascript">

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}
	
function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimFinancingBreakdownForm.action = "<%=addUrl%>?pageId=1&action=edit&step=3&surveyFlag=true&activityId=" + id;
	document.aimFinancingBreakdownForm.target = "_self";    
   document.aimFinancingBreakdownForm.submit();
}

function previewLogframe(id)
{
	openResisableWindow(700, 650);
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   	document.aimFinancingBreakdownForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	document.aimFinancingBreakdownForm.target = popupPointer.name;    
    document.aimFinancingBreakdownForm.submit();
}


function preview(id)
{

	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.aimFinancingBreakdownForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimFinancingBreakdownForm.target = "_self";
   document.aimFinancingBreakdownForm.submit();
}

</script>


<html:errors/>

<digi:instance property="aimFinancingBreakdownForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancingBreakdownForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimFinancingBreakdownForm" property="sessionExpired" value="false">

<digi:form action="/viewFinancingBreakdownFilter.do" name="aimFinancingBreakdownForm" 
type="org.digijava.module.aim.form.FinancingBreakdownForm" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="ampFundingId" />
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
													<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
														<bean:write name="aimFinancingBreakdownForm" property="ampActivityId"/>
													</c:set>
													<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
													</bean:define>
													<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" 
													styleClass="comment" title="<%=translation%>" >
													<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:actOverview">Overview</digi:trn>&nbsp;&gt;&nbsp;
													<bean:define id="perspectiveNameTrimedLocal" name="aimFinancingBreakdownForm" property="perpsectiveNameTrimmed" type="java.lang.String"/>
													<digi:trn key='<%="aim:"+ perspectiveNameTrimedLocal %>'>	
														<bean:write name="aimFinancingBreakdownForm" property="perpsectiveName"/></digi:trn>
												</SPAN>
											</TD>
											<TD align="right">
												&nbsp;
											</TD>


											<TD align="right">
												<input type="button" value="Preview" class="dr-menu"
												onclick="preview(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
												<input type="button" value="Edit" class="dr-menu"
														onclick="fnEditProject(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
													&nbsp;
											<logic:empty name="SA" scope="application">
												<input type="button" value="Preview Logframe" class="dr-menu"	onclick="previewLogframe(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)">
											</logic:empty>
											<logic:empty name="SA" scope="application">
											<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
												onclick='projectFiche(<c:out value="${aimFinancingBreakdownForm.ampActivityId}"/>)'>
											</logic:empty>
												
											</TD>


										</TR>
									</TABLE>										
								</TD>
							</TR>
							
							<logic:equal name="aimFinancingBreakdownForm" property="goButtonPresent" value="true">
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="750">
										<TABLE cellPadding=2 cellSpacing=0 vAlign="top" align="left" border=0>
											<TR>
												<logic:equal name="aimFinancingBreakdownForm" property="perspectivePresent" value="true">
												<TD align="center">
													<STRONG>	
														<digi:trn key="aim:perspective">Perspective</digi:trn>
													</STRONG>
												</TD>
												</logic:equal>
												<logic:equal name="aimFinancingBreakdownForm" property="currencyPresent" value="true">
												<TD align="center">
													<STRONG>
														<digi:trn key="aim:currency">Currency</digi:trn>
													</STRONG>
												</TD>
												</logic:equal>
												<logic:equal name="aimFinancingBreakdownForm" property="calendarPresent" value="true">
												<TD align="center">
													<STRONG>
														<digi:trn key="aim:calendarType">Calendar Type</digi:trn>
													</STRONG>					
												</TD>
												</logic:equal>
			               				<logic:equal name="aimFinancingBreakdownForm" property="yearRangePresent" value="true">
													<TD align="center">
														<STRONG>
             											<digi:trn key="aim:year">Year</digi:trn>
														</STRONG>								
													</TD>
												</logic:equal>
												<TD width="5">
													&nbsp;
												</TD>
											</TR>
											<TR>
		                     		<logic:equal name="aimFinancingBreakdownForm" property="perspectivePresent" value="true">
												<TD>
													<html:select property="perspective" styleClass="dr-menu">
														<html:optionsCollection name="aimFinancingBreakdownForm" 
														property="perspectives" value="code" label="name"/>
													</html:select>
												</TD>
											</logic:equal>
			               			<logic:equal name="aimFinancingBreakdownForm" property="currencyPresent" value="true">
												<TD>
                         					<html:select property="currency" styleClass="dr-menu">
                       							<html:optionsCollection name="aimFinancingBreakdownForm" property="currencies" value="currencyCode" 
														label="currencyName"/>
													</html:select>
												</TD>
											</logic:equal>
											<logic:equal name="aimFinancingBreakdownForm" property="calendarPresent" value="true">
												<TD>
													<html:select property="fiscalCalId" styleClass="dr-menu">
														<html:optionsCollection name="aimFinancingBreakdownForm" 
														property="fiscalYears" value="ampFiscalCalId" label="name"/> 
													</html:select>
												</TD>
											</logic:equal>
			              	 			<logic:equal name="aimFinancingBreakdownForm" property="yearRangePresent" value="true">
												<TD>
   	                 						<STRONG>From</STRONG>&nbsp;
								      				<html:select property="fromYear" styleClass="dr-menu">
								      					<html:optionsCollection name="aimFinancingBreakdownForm" 
															property="years" value="year" label="year"/>
														</html:select>&nbsp;&nbsp;
								      			<STRONG>To</STRONG>&nbsp;
														<html:select property="toYear" styleClass="dr-menu">
															<html:optionsCollection name="aimFinancingBreakdownForm" 
															property="years" value="year" label="year"/>
														</html:select>
												</TD>
											</logic:equal>
											<TD width="5">
												<html:submit value="GO" styleClass="dr-menu"/>
											</TD>
											</TR>							
										</TABLE>
									</TD>
								</TR>
							</logic:equal>

							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" height="17">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                        	<TR bgcolor="#F4F4F2" height="17"> 
                          	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
															<digi:trn key="aim:financingOverviewOfActivity">Financing Overview of Activity</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif" 
											height=17 width=17>
														</TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" > 
	                        	<TD><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn>
														</TD>
				                    <TD width="20"><digi:trn key="aim:organization">Organization</digi:trn></TD>
														<TD><digi:trn key="aim:totalCommitted">Total Committed</digi:trn></TD>
	                         	<TD><digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn></TD>
														<TD><digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn></TD>
	                         	<TD><digi:trn key="aim:totalExpended">Total Expended</digi:trn></TD>
														<TD><digi:trn key="aim:unExpendedFunds">Unexpended Funds</digi:trn></TD>
													</TR>
													<logic:empty name="aimFinancingBreakdownForm" property="financingBreakdown"> 
			                    	<TR valign="top"> 
															<TD align="center" colspan="7"><span class="note"> No records !! </span></TD>
			                     </TR>
			                    </logic:empty>
			                    <logic:notEmpty name="aimFinancingBreakdownForm" property="financingBreakdown">
														<logic:iterate name="aimFinancingBreakdownForm" property="financingBreakdown" id="breakdown" 
			  	                   type="org.digijava.module.aim.helper.FinancingBreakdown">
															<TR valign="top" bgcolor="#f4f4f2"> 
					    	           			<TD>
						               				<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlFinancialOverview}" property="ampActivityId">
																		<bean:write name="aimFinancingBreakdownForm" property="ampActivityId"/>
																	</c:set>
																	<c:set target="${urlFinancialOverview}" property="ampFundingId">
																		<bean:write name="breakdown" property="ampFundingId"/>
																	</c:set>
																	<c:set target="${urlFinancialOverview}" property="tabIndex">
																		<bean:write name="aimFinancingBreakdownForm" property="tabIndex"/>
																	</c:set>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToViewFinancialOverview">
																		Click here to view Financial Overview</digi:trn>
																	</bean:define>
						                  		<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" 
																	title="<%=translation%>" >
																		<bean:write name="breakdown" property="financingId" /> 
																	</digi:link>
																</TD>
															<bean:define id="breakdown" name="breakdown"
															type="org.digijava.module.aim.helper.FinancingBreakdown"
															toScope="request" />
																
					                  		<TD><jsp:include page="previewFinancingOrganizationPopup.jsp"/></TD>
							                  <TD align="right"><bean:write name="breakdown" property="totalCommitted"/></TD>
							                  <TD align="right"><bean:write name="breakdown" property="totalDisbursed"/></TD>
								                <TD align="right"><bean:write name="breakdown" property="unDisbursed"/></TD>
					      			          <TD align="right"><bean:write name="breakdown" property="totalExpended"/></TD>
					            		      <TD align="right"><bean:write name="breakdown" property="unExpended"/></TD>
															</TR>
														</logic:iterate>
													</logic:notEmpty>
				                  <TR valign="top" class="note"> 
														<TD><digi:trn key="aim:total">Total</digi:trn></TD>
				                    <TD>&nbsp;</TD>
														<TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalCommitted"/></TD>
														<TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalDisbursed"/></TD>
														<TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalUnDisbursed"/></TD>
														<TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalExpended"/></TD>
														<TD align="right"><bean:write name="aimFinancingBreakdownForm" property="totalUnExpended"/></TD>
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
</logic:equal>
