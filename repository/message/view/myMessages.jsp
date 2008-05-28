<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<module:display name="Messaging System">

<digi:instance property="messageForm"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script langauage="JavaScript">
function viewMessage(id) {
	openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,550,400);	
}
</script>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
	              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
	              		<TR bgColor=#f4f4f2>
	                 		<TD bgColor=#c9c9c7 class=box-title
								title='<digi:trn key="message:alertsAssosiatedWithTeam">List of Alerts associated with Team</digi:trn>'>
									<digi:trn key="message:myMessages">My Messages</digi:trn>
								</TD>
	                    	<TD background="module/aim/images/corner-r.gif" 
								height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<c:set var="trn">
						<digi:trn key="message:clickToEditAlert">Click here to view Message</digi:trn>
				</c:set>

				<TR><TD bgColor=#ffffff class=box-border align=left> 
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >				 
							<TR>
								<TD title="${trn}">
									<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>									
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
										${messageForm.msgType}New Messages
									</a>									 
								</TD>
							</TR>
							<TR>
								<TD title="${trn}">
								<IMG alt="Link" height="10" src="../ampTemplate/images/arrow-gr.gif" width="10">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2">
										${messageForm.alertType} New Alerts
									</a>
								</TD>
							</TR>	
							<TR>
								<TD title="${trn}">
								<IMG alt="Link" height="10" src="../ampTemplate/images/arrow-gr.gif" width="10">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">
										${messageForm.approvalType}New Approvals
									</a>									 
								</TD>
							</TR>	
							<TR>
								<TD title="${trn}">
								<IMG alt="Link" height="10" src="../ampTemplate/images/arrow-gr.gif" width="10">
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">
										${messageForm.calendarEventType} New Events
									</a>									 
								</TD>
							</TR>						
							<TR><TD title='<digi:trn key="message:clickToViewMoreMessages">Click here to view More Messages</digi:trn>'>
									<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
										<digi:trn key="message:more">..more</digi:trn>
									</a>
							</TD></TR>
						</TABLE>
					</TD></TR>								
				</TABLE>				
			</TD>
		</TR>
	</TABLE>
</module:display>