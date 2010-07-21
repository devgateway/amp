<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script type="text/javascript">


function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimMainProjectDetailsForm.action = "<%=addUrl%>";
    document.aimMainProjectDetailsForm.submit();
}

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimMainProjectDetailsForm.action = "<%=addUrl%>~pageId=1~step=5~action=edit~surveyFlag=true~activityId=" + id;
	document.aimMainProjectDetailsForm.target = "_self";
   document.aimMainProjectDetailsForm.submit();
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
    document.aimMainProjectDetailsForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;
	document.aimMainProjectDetailsForm.target = "_self";
    document.aimMainProjectDetailsForm.submit();
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}


</script>
<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<digi:context name="digiContext" property="context"/>
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="false">
<digi:form action="/viewPhysicalProgress.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm" 
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="true">

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR><TD vAlign="top" align="center">
		<!-- contents -->
		<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" 
		class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
					<TR bgColor=#f4f4f2><TD align=left>
						<SPAN class=crumb>					
							<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlPhysicalProgress}" property="ampActivityId">
								<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
							</c:set>
							<c:set target="${urlPhysicalProgress}" property="tabIndex" value="2"/>
							<c:set var="translation">
								<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
							</c:set>
							<digi:link href="/viewPhysicalProgress.do" name="urlPhysicalProgress" styleClass="comment" 
							title="${translation}" >
								<digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
							</digi:link>
							&gt; Details &gt; 
							<!-- bean:write name="aimPhysicalProgressForm" property="perspective"/-->
							Perspective
						</SPAN>
					</TD></TR>
					<TR bgColor=#f4f4f2><TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
                                 <TABLE cellSpacing=0 cellPadding=0 bgColor=#f4f4f2 border=0>
                                    <TR bgColor=#f4f4f2>
                                        <TD width="7" height="17">
                                        <img src="/TEMPLATE/ampTemplate/css/boxleftcorner.gif" border="0"  /><br />
                                        </TD>
                                        <TD bgColor=#ccdbff height="17" style="padding:4px 4px 4px 4px;">
                                        <strong>
                                            <digi:trn>Component Details</digi:trn>
                                        </strong>
                                        </TD>
                                        <TD width="7" height="17">
                                        <img src="/TEMPLATE/ampTemplate/css/boxrightcorner.gif" border="0"  /><br />
                                        </TD>
                                    </TR>
                                </TABLE>
							</TD></TR>
							<TR><TD width="100%" bgcolor="#F4F4F2" align="center">
                                <TABLE width="100%" cellSpacing=2 cellPadding=5 vAlign="top" align="left" style="border:1px solid #ccdbff;">
									<TR><TD width="100%" vAlign="top" align="left">
										<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left" 
										bgcolor="#ffffff">
											<bean:define name="aimPhysicalProgressForm" property="component"
											id="component" />
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><bean:write name="component" property="title"/></b>
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													&nbsp;&nbsp;&nbsp;<bean:write name="component" property="description"/>
												</TD>
											</TR>
											<TR bgcolor="#ffffff">
												<TD>&nbsp;
													
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
												</TD>
											</TR>											
											<TR bgcolor="#f4f4f2">
												<TD>
													<TABLE width="100%" cellpadding=2 cellspacing=1>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb"> 
																			<digi:trn key="aim:commitments">Commitments</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="commitments" id="comm">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<digi:trn key="aim:fundingOfTheComponent:${comm.adjustmentTypeName}"><bean:write name="comm" property="adjustmentTypeName"/></digi:trn>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="comm" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<font color="blue">*</font>
																			<bean:write name="comm" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="comm" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																			<!-- bean:write name="comm" property="perspectiveName"/-->
																		</TD>
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb">
																			<digi:trn key="aim:disbursements">Disbursements</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="disbursements" id="disb">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<digi:trn key="aim:fundingOfTheComponent:${disb.adjustmentTypeName}"><bean:write name="disb" property="adjustmentTypeName"/></digi:trn>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="disb" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<font color="blue">*</font>
																			<bean:write name="disb" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="disb" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																			<!-- bean:write name="disb" property="perspectiveName"/-->
																		</TD>
																		
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb">
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="expenditures" id="exp">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<digi:trn key="aim:fundingOfTheComponent:${exp.adjustmentTypeName}"><bean:write name="exp" property="adjustmentTypeName"/></digi:trn>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="exp" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<font color="blue">*</font>
																			<bean:write name="exp" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="exp" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																		<!-- bean:write name="exp" property="perspectiveName"/-->
																		</TD>
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>														
														<TR><TD>
															<FONT color=blue>
																<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																	*<digi:trn key="aim:allTheAmountsInThousands">All the amounts are in thousands (000)</digi:trn>
																</gs:test>
															</FONT>								
														</TD></TR>
													</TABLE>
												</TD>
											</TR>																						
											<TR bgcolor="#ffffff">
												<TD>&nbsp;
																									
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><digi:trn key="aim:physicalProgressOfTheComponent">
													Physical progress of the component</digi:trn></b>
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>	
													<TABLE width="100%" cellpadding=2 cellspacing=1 bgcolor="#f4f4f2">
														<logic:iterate name="component" id="pp" property="phyProgress">
														<TR>
															<TD bgcolor="#fffffc">
																<bean:write name="pp" property="title"/> - 
																<bean:write name="pp" property="reportingDate"/><br>
																&nbsp;&nbsp;&nbsp;<i><bean:write name="pp" property="description"/></i>
															</TD>
														</TR>
														</logic:iterate>
													</TABLE>
												</TD>
											</TR>																						
										</TABLE>
									</TD></TR>
								</TABLE>
							</TD></TR>
						</TABLE>
					</TD></TR>				
				</TABLE>
				</TD></TR>
			</TABLE>
		<!-- end -->
	</TD></TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>

</logic:equal>	
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>