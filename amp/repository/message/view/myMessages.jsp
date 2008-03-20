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
	openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&messageId='+id,550,400);	
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
				
				<c:if test="${empty messageForm.allMessages}">
					<TR><TD bgColor=#ffffff class=box-border align=left> 
						<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
							<c:set var="trn">
								<digi:trn key="message:noMessagesToShow">No Messages to View</digi:trn>
							</c:set>
							<TR><TD title="${trn}">								
								<digi:trn key="message:noMessagesToShow">No Messages to View</digi:trn>
							</TD></TR>					
						</TABLE>
					</TD></TR>					
				</c:if>		
				
						
				<logic:notEmpty name="messageForm" property="allMessages">
				<TR><TD bgColor=#ffffff class=box-border align=left> 
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
						<logic:iterate name="messageForm" property="allMessages" id="all" length="3"> 
							<TR><TD title="${trn}">
								<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
								<A href="javascript:viewMessage(${all.id});">
									<c:if test="${all.read=='false'}"><font color="red"><digi:trn key="${all.name}">${all.name}</digi:trn></font></c:if> 
									<c:if test="${all.read=='true'}"><digi:trn key="${all.name}">${all.name}</digi:trn></c:if>
								</A>							
							</TD></TR>						
						</logic:iterate>
						<TR><TD title='<digi:trn key="message:clickToViewMoreMessages">Click here to view More Messages</digi:trn>'>
								<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages">
									<digi:trn key="message:more">..more</digi:trn>
								</a>
							</TD></TR>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>				
			</TABLE>				
		</TD>
	</TR>
</TABLE>
</module:display>