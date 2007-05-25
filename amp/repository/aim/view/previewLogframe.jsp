<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>	
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<digi:instance property="aimEditActivityForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

<!--

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function backClicked() {
	document.aimEditActivityForm.step.value = "8";
	<digi:context name="backStep" property="context/module/moduleinstance/addActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= backStep %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

function disable() {
	document.aimEditActivityForm.submitButton.disabled = true;
	document.aimEditActivityForm.backButton.disabled = true;
	var appstatus = document.aimEditActivityForm.approvalStatus.value;
	var wTLFlag   = document.aimEditActivityForm.workingTeamLeadFlag.value;
	if (appstatus == "started") {
		if (wTLFlag == "yes") {
			//if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
		else if (confirm("Do you want to submit this activity for approval ?"))
				document.aimEditActivityForm.approvalStatus.value = "created";
	}
	if (appstatus == "approved") {
		if (wTLFlag != "yes")
			document.aimEditActivityForm.approvalStatus.value = "edited";
	}
	else if (wTLFlag == "yes") {
		if (appstatus == "created" || appstatus == "edited") {
			if (confirm("Do you want to approve this activity ?"))
				document.aimEditActivityForm.approvalStatus.value = "approved";
		}
	}
	document.aimEditActivityForm.submit();
	return true;
}
-->

</script>

<digi:form action="/saveActivity.do" method="post">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden property="approvalStatus" />
<html:hidden property="workingTeamLeadFlag" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td valign="top" width="100%" vAlign="top" align="left">

 <bean:define id="defaultCurrency" name="currentMember" property="appSettings.currencyId" type="java.lang.Long" scope="session" toScope="page"/> 

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
	<tr>
		<td valign="top" class=r-dotted-lg width="10" align="left" vAlign="top">&nbsp;</td>
		<td valign="top" class=r-dotted-lg align=left vAlign=top>
			<table width="100%" cellSpacing="3" cellPadding="1" vAlign="top" align="left" border=0>
				<tr><td valign="top">
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td valign="top" width="100%" vAlign="top">
						<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
							<tr>
								<td valign="top" width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td valign="top" width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td valign="top" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="aim:logframePlanningMatrix">
													Logframe Planning Matrix
												</digi:trn>
												
											</td>
											<td valign="top" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<% java.util.Date now = new java.util.Date();
											session.setAttribute("logframepr","false");
												java.text.DateFormat df =  java.text.DateFormat.getDateInstance();%>
											      <%= df.format(now)%></td>
											<td valign="top" width="13" height="20" background="module/aim/images/right-side.gif">
												
											</td>
										</tr>
									 </table>
								</td>
							</tr>
							<tr><td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
										<tr><td valign="top" colspan="2" bgcolor="#ffffff" height="20">&nbsp;</td></tr>
										<tr><td valign="top" align="left" vAlign="top" bgcolor="#ffffff" width="30%">
											<digi:trn key="aim:programId">Program ID:</digi:trn>
										</td>
										<td valign="top" align="left" vAlign="top" bgcolor="#ffffff"> 
											<logic:notEmpty name="aimEditActivityForm" property="ampId">
												<bean:write name="aimEditActivityForm" property="ampId"/>
											</logic:notEmpty>
										</td>
										</tr>
										
										<tr><td valign="top" align="left" vAlign="top" bgcolor="#ffffff" width="30%">
											<digi:trn key="aim:programName">Program Name:</digi:trn>
										</td>
										<td valign="top" align="left" vAlign="top" bgcolor="#ffffff"> 
											<logic:notEmpty name="aimEditActivityForm" property="title">
											<bean:write name="aimEditActivityForm" property="title"/>
											</logic:notEmpty>
										</td>
										</tr>
										
										<tr><td valign="top" align="left" vAlign="top" bgcolor="#ffffff" width="30%">
											
											<digi:trn key="aim:contractPeriodExpiration">Contract Period Expiration:</digi:trn>
										</td>
										<td valign="top" align="left" vAlign="top" bgcolor="#ffffff"> 
											<c:out default="" value="${aimEditActivityForm.currentCompDate}"/>
										</td>
										</tr>
									</table>
								</td></tr>
								<tr>
								<td valign="top" width="70%" bgcolor="#f4f4f2">
									<table width="75%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:overallObjective">Overall Objective</digi:trn>
										</td>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:indicatorsPreview">Indicators</digi:trn>
										</td>
										<td valign="top"  width="33%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										
									</tr>
									<tr>
										<td valign="top"  width="33%"  bgcolor="#ffffff" style="border:1px solid #CCC;">
										<digi:edit key="${aimEditActivityForm.objectives}"/>
										</td>
										<td valign="top"  width="33%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										<logic:notEmpty name="aimEditActivityForm" property="indicatorsME">
												<logic:iterate name="aimEditActivityForm" property="indicatorsME" id="indicator" 
													type="org.digijava.module.aim.helper.ActivityIndicator">
													<logic:notEmpty name="indicator" property="indicatorsCategory"><logic:notEmpty name="indicator" property="indicatorsCategory.value">
													<logic:equal name="indicator" property="indicatorsCategory.value" value="Objective">
													<bean:write name="indicator" property="indicatorName"/>
													<br/>
													</logic:equal>
													</logic:notEmpty></logic:notEmpty>
												</logic:iterate>
											</logic:notEmpty>
										</td>
											
										<td valign="top"  width="33%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										<logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
                                        	<logic:equal name="comments" property="key" value="Objective Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>

									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top" width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:projectPurpose">Project Purpose</digi:trn>
										</td>
										<td valign="top" width="25%"  vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:indicatorsPreview">Indicators</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										
									</tr>
									<tr>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
												<digi:edit key="${aimEditActivityForm.purpose}"/>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">&nbsp;
											<logic:notEmpty name="aimEditActivityForm" property="indicatorsME">
												<logic:iterate name="aimEditActivityForm" property="indicatorsME" id="indicator" 
													type="org.digijava.module.aim.helper.ActivityIndicator">
													<logic:notEmpty name="indicator" property="indicatorsCategory"><logic:notEmpty name="indicator" property="indicatorsCategory.value">
													<logic:equal name="indicator" property="indicatorsCategory.value" value="Purpose">
													<bean:write name="indicator" property="indicatorName"/>
													<br/>
													</logic:equal>
													</logic:notEmpty></logic:notEmpty>
												</logic:iterate>
											</logic:notEmpty>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										<logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
                                        	<logic:equal name="comments" property="key" value="Purpose Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										<logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Purpose Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										</td>
									</tr>
								    </table>
							   </td>
							</tr>
							
							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:results">Results</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:indicatorsPreview">Indicators</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:verification">Verification</digi:trn>
										</td>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										
									</tr>
									<tr>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
												<digi:edit key="${aimEditActivityForm.results}"/>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
											<logic:notEmpty name="aimEditActivityForm" property="indicatorsME">
												<logic:iterate name="aimEditActivityForm" property="indicatorsME" id="indicator" 
													type="org.digijava.module.aim.helper.ActivityIndicator">
													<logic:notEmpty name="indicator" property="indicatorsCategory"><logic:notEmpty name="indicator" property="indicatorsCategory.value">
													<logic:equal name="indicator" property="indicatorsCategory.value" value="Results">
													<bean:write name="indicator" property="indicatorName"/>
													<br/>
													</logic:equal>
													</logic:notEmpty></logic:notEmpty>
												</logic:iterate>
											</logic:notEmpty>
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
											<logic:equal name="comments" property="key" value="Results Verification">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
										<td valign="top"  width="25%" bgcolor="#ffffff" style="border:1px solid #CCC;">
										 <logic:iterate name="aimEditActivityForm" id="comments" property="allComments">
										 	<logic:equal name="comments" property="key" value="Results Assumption">
												<logic:iterate name="comments" id="comment" property="value" 
													type="org.digijava.module.aim.dbentity.AmpComments">
													<bean:write name="comment" property="comment"/><br/>
                                        		</logic:iterate>
                                        	</logic:equal>
										</logic:iterate>
										
										</td>
									</tr>
								    </table>
							   </td>
							</tr>

							<tr>
								<td valign="top" width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699" >
									<tr>
										<td valign="top"  width="25%" vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:activities">Activities</digi:trn>
										</td>
										<td valign="top" vAlign="center"  width="25%" align ="center" class="textalb" height="20" bgcolor="#006699" >
											<digi:trn key="aim:contributions">Contributions</digi:trn>
										</td>
										<td valign="top" vAlign="center"  width="25%" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:costs">Costs</digi:trn>
										</td>
										<td valign="top" vAlign="center"  width="25%" align ="center" class="textalb" height="20" bgcolor="#006699">
											<digi:trn key="aim:assumptions">Assumptions</digi:trn>
										</td>
										
									</tr>
									<logic:notEmpty name="aimEditActivityForm" property="costs">
									<logic:iterate name="aimEditActivityForm" property="costs" id="euActivity"
												type="org.digijava.module.aim.dbentity.EUActivity">	
									<c:set target="${euActivity}" property="desktopCurrencyId" value="${defaultCurrency}"/>
									<tr>
										<td valign="top" bgcolor="#ffffff" width="25%"  style="border:1px solid #CCC;">
												<c:out default="" value="${euActivity.name}"/>
										</td>
										<td valign="top" bgcolor="#ffffff" width="25%"  style="border:1px solid #CCC;">
												<bean:write name="euActivity" property="totalContributionsConverted" format="###,###,###"/>

										</td>
										<td valign="top" bgcolor="#ffffff"  width="25%" style="border:1px solid #CCC;">
												<bean:write name="euActivity" property="totalCostConverted" format="###,###,###"/>												
										</td>
										<td valign="top" bgcolor="#ffffff" width="25%"  style="border:1px solid #CCC;">
												<c:out default="" value="${euActivity.assumptions}"/>
										</td>
									</tr>
									</logic:iterate>
									</logic:notEmpty>
								    </table>
							   </td>
							</tr>
							
						</table>
						</td></tr>
					</table>
				</td></tr>
			</table>
		</td></tr>
	</table>
</td></tr>
<tr>
	<td valign="top" bgcolor="#ffffff" align="center" width="50%" >
		<html:button styleClass="buton" value="Close" onclick="return window.close()" property="closeButton"/>
		&nbsp;&nbsp;&nbsp;
		<html:button styleClass="buton" value="Print" onclick="return window.print()" property="printButton"/>&nbsp;&nbsp;
	</td>
</tr>
</table>
</digi:form>
