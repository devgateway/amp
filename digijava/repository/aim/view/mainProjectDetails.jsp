<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimMainProjectDetailsForm" />
<digi:context name="digiContext" property="context"/>

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
	
<TABLE width="100%"  border="0" cellpadding="0" cellspacing="0" vAlign="top" align="center">
   <TR>
		<TD>
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
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewProjectDescription">Click here to View Project Description</digi:trn>
						</bean:define>
						(<digi:link href="/viewChannelOverviewDescription.do" name="urlDescription" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:viewDescription">View Description</digi:trn>
						</digi:link>)			
						<A class=comment>
							<bean:write name="aimMainProjectDetailsForm" property="description" /> 
						</A>
					</TD>
				</TR>	
			</TABLE>
		</TD>
	</TR>
	<TR>
      <TD width="100%" nowrap align="center" vAlign="bottom" height="20">
        	<TABLE width="765"  border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" class="box-border-nopadding" > 
            <TR bgColor=#f4f4f2>
              	<TD height="20">
						<TABLE border="0" cellpadding="0" cellspacing="1" bgcolor="#F4F4F2" height="20">
                 		<TR bgColor=#f4f4f2 height="20">
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="0">
	                     <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="165">
									:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>									
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="0">
	                     <TD vAlign=center bgColor=#3754a1 noWrap width="165">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewChannelOverview">Click here to view Channel Overview</digi:trn>
									</bean:define>
									<digi:link href="/viewChannelOverview.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
										:: <digi:trn key="aim:channelOverview">Channel Overview</digi:trn>
									</digi:link>									
								</logic:notEqual>
								</TD>
									
								<logic:equal name="aimMainProjectDetailsForm" property="tabIndex" value="1">
	                     <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="175">
									:: <digi:trn key="aim:financialProgress">Financial Progress</digi:trn>								
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="1">
                    		<TD vAlign=center bgColor=#3754a1 noWrap width="175">
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
	                      <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap width="170">
									:: <digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>  
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="2">
                    		<TD vAlign=center bgColor=#3754a1 noWrap width="170">
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
	                     <TD vAlign=center bgColor=#222e5d class="sub-nav-selected" noWrap>
									:: <digi:trn key="aim:documents">Documents</digi:trn> 									
								</logic:equal>
								<logic:notEqual name="aimMainProjectDetailsForm" property="tabIndex" value="3">
	                    	<TD vAlign=center bgColor=#3754a1 noWrap width="117">
                   			<c:set target="${urlTabs}" property="tabIndex" value="3"/>
<bean:define id="translation">
	<digi:trn key="aim:clickToViewDocuments">Click here to view Documents</digi:trn>
</bean:define>
                    			<digi:link href="/viewKnowledge.do" name="urlTabs" styleClass="sub-nav" title="<%=translation%>" >
									 :: <digi:trn key="aim:documents">Documents</digi:trn>	
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
								

