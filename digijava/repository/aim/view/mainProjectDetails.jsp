<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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

<digi:errors/>

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
	
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" vAlign="top" align="center">
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
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
							</bean:define>
							<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewActivity">Click here to view Activity</digi:trn>
							</bean:define>
		            	<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="comment" title="<%=translation%>" >
        						<digi:trn key="aim:activity">Activity</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
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

			<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top">
				<TR>
					<TD height=16 vAlign=center width="100%"><span class=subtitle-blue>
						<bean:write name="aimMainProjectDetailsForm" property="ampId"/></span>
					</TD>
				</TR>				
				<TR>
					<TD height=16 vAlign=center width="100%"><span class=subtitle-blue>
						<bean:write name="aimMainProjectDetailsForm" property="name"/></span>
					</TD>
				</TR>	
				<TR>
					<TD>
						<TABLE cellpadding=0 cellspacing=0 valign=top align=left>
							<TR>
								<TD>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewProjectDescription">Click here to View Project Description</digi:trn>
									</bean:define>	
									<a href="javascript:viewProjectDetails('Desc','<c:out value="${aimMainProjectDetailsForm.description}"/>')" class="comment" title="<%=translation%>">
										<digi:trn key="aim:viewDescription">View Description</digi:trn></a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;									
								</TD>
								<TD>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewProjectObjectives">Click here to View Project Objectives</digi:trn>
									</bean:define>					
									<a href="javascript:viewProjectDetails('Obj','<c:out value="${aimMainProjectDetailsForm.objectives}"/>')" class="comment" title="<%=translation%>">
										<digi:trn key="aim:viewObjectives">View Objectives</digi:trn></a>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>					
			</TABLE>
		</TD>
	</TR>
	<TR>
      <TD width="100%" nowrap align="center" vAlign="bottom" height="20">
        	<TABLE width="810"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="box-border-nopadding" > 
            <TR bgColor=#f4f4f2>
              	<TD height="20">
						<TABLE border="0" cellpadding="0" cellspacing="1" bgcolor="#F4F4F2" height="20">
                 		<TR bgColor=#f4f4f2 height="20">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="0">
	                     <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="149">
									:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>									
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="0">
	                     <TD vAlign=center bgColor=#3754a1 noWrap width="149">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>
									</bean:define>
									<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
										:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>									
								</logic:notEqual>
								</TD>
									
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="1">
	                     <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="159">
									:: <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>								
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="1">
                    		<TD vAlign=center bgColor=#3754a1 noWrap width="159">
              					<c:set target="${urlTabs}" property="tabIndex" value="1"/>
<bean:define id="translation">
	<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
</bean:define>
              					<digi:link href="/viewFinancingBreakdown.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
										:: <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
									</digi:link>								
								</logic:notEqual>
								</TD>
									
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="2">
	                      <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="154">
									:: <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>  
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="2">
                    		<TD vAlign=center bgColor=#3754a1 noWrap width="154">
                   			<c:set target="${urlTabs}" property="tabIndex" value="2"/>
<bean:define id="translation">
	<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
</bean:define>
                    			<digi:link href="/viewPhysicalProgress.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
                    				:: <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn> 
                    			</digi:link>								
								</logic:notEqual>
								</TD>
									
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="3">
	                     		<TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="102">
									:: <digi:trn key="aim:documents">Documents</digi:trn> 									
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="3">
	                    		<TD vAlign=center bgColor=#3754a1 noWrap width="102">
                   					<c:set target="${urlTabs}" property="tabIndex" value="3"/>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>
									</bean:define>
                    			<digi:link href="/viewKnowledge.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
									 :: <digi:trn key="aim:documents">Documents</digi:trn>	
									</digi:link>									
								</logic:notEqual>
								</TD>
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="4">
	                     		<TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="153">
									:: <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 									
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="4">
	                    		<TD vAlign=center bgColor=#3754a1 noWrap width="153">
                   					<c:set target="${urlTabs}" property="tabIndex" value="4"/>
                   					<c:set target="${urlTabs}" property="regionId" value="-1"/>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewRegionalFundings">Click here to view regional funding</digi:trn>
									</bean:define>
                    				<digi:link href="/viewRegionalFundingBreakdown.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
									 	:: <digi:trn key="aim:regionalFunding">Regional Funding</digi:trn> 	
									</digi:link>									
								</logic:notEqual>
								</TD>
								<TD vAlign=middle width="100%" bgColor=#3754A1 >&nbsp;</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
      <TD width="100%" nowrap align="left" vAlign="bottom" height="20">
        	<TABLE width="810"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="box-border-nopadding" > 
            <TR bgColor=#f4f4f2>
              	<TD height="20">
						<TABLE border="0" cellpadding="0" cellspacing="1" bgcolor="#F4F4F2" height="20">
                 		<TR bgColor=#f4f4f2 height="20">
                 			<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="5">
	                     		<TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="123">
									:: <digi:trn key="aim:indicators">INDICATORS</digi:trn> 									
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="5">
	                    		<TD vAlign=center bgColor=#3754a1 noWrap width="123">
	                    			<jsp:useBean id="survey" type="java.util.Map" class="java.util.HashMap" />
	                    			<c:set target="${survey}" property="ampActivityId">
										<bean:write name="aimMainProjectDetailsForm" property="ampActivityId"/>
									</c:set>
                   					<c:set target="${survey}" property="tabIndex" value="5"/>
                   					<bean:define id="translation">
										<digi:trn key="aim:clickToViewAidEffectIndicators">Click here to view Aid Effectiveness Indicators</digi:trn>
									</bean:define>
                    				<digi:link href="/viewSurveyList.do" name="survey" styleClass="sub-nav" title="<%=translation%>" >
									 	:: <digi:trn key="aim:indicators">Indicators</digi:trn> 	
									</digi:link>									
							</logic:notEqual>
								</TD>
							<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="5">
	                     		<TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="103">
									:: <digi:trn key="aim:activityDashboard">Dashboard</digi:trn> 									
							</logic:equal>
							<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="5">
	                    		<TD vAlign=center bgColor=#3754a1 noWrap width="103">
                   				<c:set target="${urlTabs}" property="tabIndex" value="5"/>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewActivityDashboard">Click here to view activity dashboard</digi:trn>
									</bean:define>
                    			<digi:link href="/viewActivityDashboard.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
									 :: <digi:trn key="aim:activityDashboard">Dashboard</digi:trn>
									</digi:link>									
							</logic:notEqual>
								</TD>	
								<TD vAlign=middle width="100%" bgColor=#3754A1 >&nbsp;</TD>
						</TR>
						</TABLE>
				</TD>
			</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</logic:equal>								
