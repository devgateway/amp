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
	<digi:form action="/viewProjectDetails.do">
			<html:hidden property="type" />
			<html:hidden property="description" />
			<html:hidden property="objectives" />		
		<TD width="100%" nowrap align="center" vAlign="bottom" height="20"  bgcolor="#f7f9e3">
			<TABLE width="98%" cellspacing="0" cellpadding=0 border=0  bgcolor="#f7f9e3">
			  	<TR><TD bgcolor="#f7f9e3" >
				<DIV id="leftNav">
				
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="0">
					  	<span id="leftNavSelected">
						 <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
					   </span>
						 </logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="0">
						
						<div id="gen" title='<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>' >
						
						<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="leftNavItem" >
							 <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
						</digi:link></div>
						
						</logic:notEqual>
						
					</td></tr>		
					<TR><TD bgcolor="#f7f9e3" >
					<DIV id="leftNav">
					
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="1">
					  					  	<span id="leftNavSelected">
						 <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>								
						</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="1">
     					<c:set target="${urlTabs}" property="tabIndex" value="1"/>
					
					
						<div id="gen" title='<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>' >
              		<digi:link href="/viewFinancingBreakdown.do" name="urlTabs" styleClass="leftNavItem">
						
							 <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
						</digi:link></div>


						</logic:notEqual>
								
					</td></tr>		
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">

					
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="2">
											  	<span id="leftNavSelected">
							 <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>  
							</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="2">
                  <c:set target="${urlTabs}" property="tabIndex" value="2"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>'>
                 	<digi:link href="/viewPhysicalProgress.do" name="urlTabs" styleClass="leftNavItem">
                 		 <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
                 	</digi:link></div>
					
						</logic:notEqual>

					</td></tr>		
					<feature:display name="Documents Tab" module="Document">
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">

						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="3">
										  	<span id="leftNavSelected">
							 <digi:trn key="aim:documents">Documents</digi:trn>
							 </span>
						</logic:equal>
					
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="3">
     					<c:set target="${urlTabs}" property="tabIndex" value="3"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>'>
           			<digi:link href="/viewKnowledge.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:documents">Documents</digi:trn>	
						</digi:link></div>
					
						</logic:notEqual>
					</td></tr>
					</feature:display>		

					<feature:display name="Regional Funding" module="Funding">
					<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="4">
   					  	<span id="leftNavSelected">
   					    	<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
						</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="4">
						<c:set target="${urlTabs}" property="tabIndex" value="4"/>
						<c:set target="${urlTabs}" property="regionId" value="-1"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewRegionalFundings">Click here to view regional funding</digi:trn>'>
						<digi:link href="/viewRegionalFundingBreakdown.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 	
						</digi:link></div>
					
						</logic:notEqual>
					</DIV>
					</TD></TR>
					</feature:display>
					<field:display name="Paris Survey" feature="Paris Indicators">
				  	<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="5">
									  	<span id="leftNavSelected">
							 <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn>
								</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="5">
           			<jsp:useBean id="survey" type="java.util.Map" class="java.util.HashMap" />
						<c:set target="${survey}" property="ampActivityId">
							<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
						</c:set>
						<c:set target="${survey}" property="tabIndex" value="5"/>
				
						<div id="gen" 
						title='<digi:trn key="aim:clickToViewAidEffectIndicators">Click here to view Aid Effectiveness Indicators</digi:trn>'>
						<digi:link href="/viewSurveyList.do" name="survey" styleClass="leftNavItem">
							 <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> 	
						</digi:link></div>
				
						</logic:notEqual>
						</DIV>
					</TD></TR>
					</field:display>
					
					<feature:display name="NPD Dashboard" module="National Planning Dashboard">
			  		<TR><TD bgcolor="#f7f9e3">
					<DIV id="leftNav">
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="6">
										  	<span id="leftNavSelected">
							 <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
							</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="6">
						<c:set target="${urlTabs}" property="tabIndex" value="6"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewActivityDashboard">Click here to view activity dashboard</digi:trn>'>
						<digi:link href="/viewActivityDashboard.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
						</digi:link></div>
					
						</logic:notEqual>
					</DIV>
					</TD></TR>
					</feature:display>
			  	<TR><TD bgcolor="#f7f9e3">
				<DIV id="leftNav">
					
						
						<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="7">
					  					  	<span id="leftNavSelected">
							 <digi:trn key="aim:projectCosting">Costing</digi:trn>								
							</span>
						</logic:equal>
						<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="7">
     					<c:set target="${urlTabs}" property="tabIndex" value="7"/>
					
						<div id="gen" title='<digi:trn key="aim:clickToViewCosting">Click here to view Costing</digi:trn>'>
              		<digi:link href="/viewProjectCostsBreakdown.do" name="urlTabs" styleClass="leftNavItem">
							 <digi:trn key="aim:projectCosting">Costing</digi:trn>
						</digi:link></div>
					
						</logic:notEqual>
						
					
				</DIV>	
				</TD></TR>
				<TR>
					<TD bgcolor="#f7f9e3">
						<DIV id="leftNav">
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="8">
					  			<span id="leftNavSelected">
							 		<digi:trn>Regional Observations</digi:trn>								
								</span>
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="8">
     							<c:set target="${urlTabs}" property="tabIndex" value="8"/>
								<div id="gen" title='<digi:trn>Click here to view Regional Observations</digi:trn>'>
              						<digi:link href="/viewProjectCostsBreakdown.do" name="urlTabs" styleClass="leftNavItem">
							 			<digi:trn>Regional Observations</digi:trn>
									</digi:link>
								</div>
							</logic:notEqual>
						</DIV>	
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR></digi:form>
</TABLE>
</logic:equal>								
