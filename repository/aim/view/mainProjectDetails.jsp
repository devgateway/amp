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
<script language="JavaScript">
<!--

	function viewProjectDetails(type,key) {
		openNewWindow(600, 400);
		<digi:context name="viewProjDetails" property="context/module/moduleinstance/viewProjectDetails.do"/>
		document.aimMainProjectDetailsForm.action = "<%= viewProjDetails %>";
		document.aimMainProjectDetailsForm.type.value = type;
		document.aimMainProjectDetailsForm.description.value = key;
		document.aimMainProjectDetailsForm.objectives.value = key;
		document.aimMainProjectDetailsForm.target = popupPointer.name;
		document.aimMainProjectDetailsForm.submit();					  
	}

-->
</script>

<%--
<digi:errors/>
--%>

<digi:instance property="aimMainProjectDetailsForm" />

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="true">
</logic:equal>

<logic:equal name="aimMainProjectDetailsForm" property="sessionExpired" value="false">
<jsp:useBean id="urlTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlTabs}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlTabs}" property="tabIndex" value="0"/>
<jsp:useBean id="urlDescription" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDescription}" property="ampActivityId">
	<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
</c:set>
	
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="left">
   <TR>
		<TD>
			<digi:form action="/viewProjectDetails.do">
			<html:hidden property="type" />
			<html:hidden property="description" />
			<html:hidden property="objectives" />					
			<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top">
				<TR>
					<TD>
						<SPAN class=crumb>
							<div id="gen" title='<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>'>
							<digi:link href="/viewMyDesktop.do" styleClass="comment">
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link></div>&nbsp;&gt;&nbsp;
							<div id="gen" title='<digi:trn key="aim:clickToViewActivity">Click here to view Activity</digi:trn>'>
		            	<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="comment">
        						<digi:trn key="aim:activity">Activity</digi:trn>
							</digi:link></div>&nbsp;&gt;&nbsp;
        					<digi:trn key="aim:activityDetails">Details</digi:trn>
						</SPAN>
					</TD>
				</TR>
			</TABLE>
			</digi:form>
		</TD>
	</TR>

   <TR>
		<TD>

			<TABLE width="100%" cellSpacing="0" cellPadding="0" vAlign="top">
				<TR>
					<TD>
						<TABLE cellpadding=0 cellspacing=0 valign=top align=left width="100%"> 
							<TR>
							<TD vAlign=center><span class=subtitle-blue>
								<bean:write name="aimMainProjectDetailsForm" property="name"/></span>
							</TD>
							<feature:display module="Project ID and Planning" name="Identification">
							<TD align="right">
								<SPAN class=crumb>
									<field:display name="Description" feature="Identification">
									<div id="gen" title='<digi:trn key="aim:clickToViewProjectDescription">Click here to View Project Description</digi:trn>'>
									<a href="javascript:viewProjectDetails('Desc','<c:out value="${aimMainProjectDetailsForm.description}"/>')" 
									class="comment">
									<digi:trn key="aim:viewDescription">View Description</digi:trn></a></div>
									&nbsp;&nbsp;
									</field:display>
									<field:display name="Objectives" feature="Identification">
										<field:display name="Objective" feature="Identification">
										<div id="gen" title='<digi:trn key="aim:clickToViewProjectObjectives">Click here to View Project Objectives</digi:trn>'>
										<a href="javascript:viewProjectDetails('Obj','<c:out value="${aimMainProjectDetailsForm.objectives}"/>')" 
										class="comment">
										<digi:trn key="aim:viewObjectives">View Objectives</digi:trn></a></div>
										&nbsp;&nbsp;
										</field:display>
									</field:display>
									<field:display name="Purpose" feature="Identification">
									<div id="gen" title='<digi:trn key="aim:clickToViewProjectPurpose">Click here to View Project Purpose</digi:trn>'>
									<a href="javascript:viewProjectDetails('Purp','<c:out value="${aimMainProjectDetailsForm.purpose}"/>')" 
									class="comment">
										<digi:trn key="aim:viewPurpose">View Purpose</digi:trn></a></div>
									&nbsp;&nbsp;
									</field:display>
									<field:display name="Results" feature="Identification">
									<div id="gen" title='<digi:trn key="aim:clickToViewProjectResults">Click here to View Project Results</digi:trn>'>
									<a href="javascript:viewProjectDetails('Res','<c:out value="${aimMainProjectDetailsForm.results}"/>')" 
									class="comment">
										<digi:trn key="aim:viewResults">View Results</digi:trn></a></div>
									</field:display>
								</SPAN>									
							</TD>
							</feature:display>
							</TR>
						</TABLE>
					</TD>
				</TR>					
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD width="100%" nowrap align="left" vAlign="bottom" height="20">
			<TABLE width="100%" cellspacing="1" cellpadding=0 border="0">
			  	<TR><TD bgcolor="#3754a1">
				<DIV id="navlinks">
					<UL>
						<feature:display name="Channel Overview Tab" module="Channel Overview">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="0">
							   <LI class="selected">
									:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="0">
								<LI>
								<div id="gen" title='<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>'>
								<digi:link href="/viewChannelOverview.do" name="urlTabs">
									:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
								</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>
						
						<feature:display name="References Tab" module="References">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="1">
							   <LI class="selected">
									:: <digi:trn key="aim:references">References</digi:trn>
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="1">
								<c:set target="${urlTabs}" property="tabIndex" value="1"/>
								<LI>
								<div id="gen" title='<digi:trn key="aim:clickToViewReferences">Click here to view References</digi:trn>'>
								<digi:link href="/viewReferences.do" name="urlTabs">
									:: <digi:trn key="aim:references">references</digi:trn>
								</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>
						
						<module:display name="Financial Progress" parentModule="PROJECT MANAGEMENT"></module:display>
						<feature:display name="Financial Progress Tab" module="Financial Progress">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="2">
							   <LI class="selected">
									:: <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>								
								</LI>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="2">
		     					<c:set target="${urlTabs}" property="tabIndex" value="2"/>
								<LI>
								<div id="gen" title='<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>'>
		              			<digi:link href="/viewFinancingBreakdown.do" name="urlTabs">
									:: <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
								</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>
						
							<feature:display name="Funding Organizations" module="Funding"></feature:display>
							<feature:display name="Funding Organizations Tab" module="Funding">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="3">
								   <LI class="selected">
										:: <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>  
									</LI>
									</logic:equal>
									<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="3">
			                  <c:set target="${urlTabs}" property="tabIndex" value="3"/>
									<LI>
									<div id="gen" title='<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>'>
			                 	<digi:link href="/viewPhysicalProgress.do" name="urlTabs">
			                 		:: <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
			                 	</digi:link></div>
									</LI>
								</logic:notEqual>
							</feature:display>
							
						<module:display name="Document" parentModule="Project Management">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="4">
							   <LI class="selected">
									:: <digi:trn key="aim:documents">Documents</digi:trn>
							    </LI>
						  	</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="4">
	     					<c:set target="${urlTabs}" property="tabIndex" value="4"/>
							<LI>
							<div id="gen" title='<digi:trn key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>'>
	           				<digi:link href="/viewKnowledge.do" name="urlTabs">
								:: <digi:trn key="aim:documents">Documents</digi:trn>	
							</digi:link></div>
							</LI>
							</logic:notEqual>
						</module:display>
					</UL>
				</DIV>
				</TD></TR>
			  	<TR><TD bgcolor="#3754a1" >
				<DIV id="navlinks">
					<UL>
							<feature:display name="Regional Funding" module="Funding"></feature:display>
							<feature:display name="Regional Funding Tab" module="Funding">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="5">
								   <LI class="selected">
										:: <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
									</LI>
									</logic:equal>
									<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="5">
									<c:set target="${urlTabs}" property="tabIndex" value="5"/>
									<c:set target="${urlTabs}" property="regionId" value="-1"/>
									<LI>
									<div id="gen" title='<digi:trn key="aim:clickToViewRegionalFundings">Click here to view regional funding</digi:trn>'>
									<digi:link href="/viewRegionalFundingBreakdown.do" name="urlTabs">
										:: <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 	
									</digi:link></div>
									</LI>
								</logic:notEqual>
						</feature:display>
						<feature:display name="Paris Indicators" module="Paris Indicators"></feature:display>
						<field:display name="Paris Survey" feature="Paris Indicators">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="6">
								   <LI class="selected">						
									:: <digi:trn key="aim:parisIndicators">Paris Indicators</digi:trn>
									</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="6">
		           			<jsp:useBean id="survey" type="java.util.Map" class="java.util.HashMap" />
								<c:set target="${survey}" property="ampActivityId">
									<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
								</c:set>
								<c:set target="${survey}" property="tabIndex" value="6"/>
								<LI>	
								<div id="gen" 
								title='<digi:trn key="aim:clickToViewAidEffectIndicators">Click here to view Aid Effectiveness Indicators</digi:trn>'>
								<digi:link href="/viewSurveyList.do" name="survey">
									:: <digi:trn key="aim:parisIndicators">Paris Indicators</digi:trn> 	
								</digi:link></div>
								</LI>
							</logic:notEqual>
						</field:display>
						<feature:display name="Dashboard" module="M & E">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="7">
									<LI class="selected">
									:: <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
									</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="7">
								<c:set target="${urlTabs}" property="tabIndex" value="7"/>
								<LI>
								<div id="gen" title='<digi:trn key="aim:clickToViewActivityDashboard">Click here to view activity dashboard</digi:trn>'>
								<digi:link href="/viewActivityDashboard.do" name="urlTabs">
									:: <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
								</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>						
						
						<feature:display name="Costing" module="Activity Costing"></feature:display>
						<feature:display name="Costing Tab" module="Activity Costing">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="8">
							   <LI class="selected">
									:: <digi:trn key="aim:projectCosting">Costing</digi:trn>								
								</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="8">
									
										<c:set target="${urlTabs}" property="tabIndex" value="8"/>
										<LI>
										<div id="gen" title='<digi:trn key="aim:clickToViewCosting">Click here to view Costing</digi:trn>'>
				              		<digi:link href="/viewProjectCostsBreakdown.do" name="urlTabs">
											:: <digi:trn key="aim:projectCosting">Costing</digi:trn>
										</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>
                                                 <feature:display name="Contracting Tab" module="Contracting"></feature:display>
						<feature:display name="Contracting Tab" module="Contracting">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="9">
							   <LI class="selected">
									:: <digi:trn key="aim:projectContracting"> Contracting </digi:trn>								
								</LI>
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="9">
									
										<c:set target="${urlTabs}" property="tabIndex" value="9"/>
										<LI>
										<div id="gen" title='<digi:trn key="aim:clickToViewContracting">Click here to view Contracting</digi:trn>'>
				              		<digi:link href="/viewIPAContracting.do" name="urlTabs">
											:: <digi:trn key="aim:projectContracting">Contracting</digi:trn>
										</digi:link></div>
								</LI>
							</logic:notEqual>
						</feature:display>
					</UL>		
				</DIV>	
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</logic:equal>