<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title width=110>
								<A title="<digi:trn key="aim:TeamMemberListNActivities">
									List of Team Members associated with Activities</digi:trn>">
									<digi:trn key="aim:teamMembers">Team Members</digi:trn>
								</A>
							</TD>
                    	<TD background="module/aim/images/corner-r.gif" 
							height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<logic:notEmpty name="myTeamMembers" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
					<logic:iterate name="myTeamMembers" id="tm" scope="session" 
					type="org.digijava.module.aim.helper.TeamMember"> 
						<TR><TD>
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewMemberDetails">Click here to View Member Details</digi:trn>
							</bean:define>
							<A href="javascript:showUserProfile(<c:out value="${tm.memberId}"/>)" title="<%=translation%>">	
								<bean:write name="tm" property="memberName" />
							</A>											
							<logic:equal name="tm" property="teamHead" value="true">*</logic:equal>
						</TD></TR>
					</logic:iterate>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>
			</TABLE>	
		</TD>
	</TR>
</TABLE>
