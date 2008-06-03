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
<digi:form action="/messageActions.do">
<html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script langauage="JavaScript">
	
	var clickToViewMsg='<digi:trn key="message:clickToEditAlert">Click here to view Message</digi:trn>';
	var moreMsgs='<digi:trn key="message:clickToViewMoreMessages">Click here to view More Messages</digi:trn>';
	
	//setting timer to check for new messages after specified time	
	if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}

	 function checkForNewMessages(){
		var url=addActionToURL('message/messageActions.do?actionType=checkForNewMessage');			
		var async=new Asynchronous();
		async.complete=getNewMessagesAmount;
		async.call(url);
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}

	function viewMessage(id) {
		openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,550,400);	
	}
	
	function getNewMessagesAmount(status, statusText, responseText, responseXML){
		var root=responseXML.getElementsByTagName('Messaging')[0].childNodes[0];
		var msgsAmount=root.getAttribute('messages');
		var alertsAmount=root.getAttribute('alerts');
		var approvalsAmount=root.getAttribute('approvals');
		var calEventsAmount=root.getAttribute('calEvents');
		
		//creating table
		var tbl=document.getElementById('msgLinks');	
		if(tbl.childNodes!=null && tbl.childNodes.length>0){
			while (tbl.childNodes.length>0){
				tbl.removeChild(tbl.childNodes[0]);
			}
		}		
			var tr1=document.createElement('TR');
				var td1=document.createElement('TD');
				td1.title=clickToViewMsg;
				td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
								'<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">'+msgsAmount+'New Messages</a>';
			tr1.appendChild(td1);
		tbl.appendChild(tr1);		
			var tr1=document.createElement('TR');
				var td1=document.createElement('TD');
				td1.title=clickToViewMsg;
				td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
								'<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2">'+alertsAmount+'New Alerts</a>';
			tr1.appendChild(td1);
		tbl.appendChild(tr1);	
			var tr1=document.createElement('TR');
				var td1=document.createElement('TD');
				td1.title=clickToViewMsg;
				td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
								'<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">'+approvalsAmount+'New Approvals</a>';
			tr1.appendChild(td1);
		tbl.appendChild(tr1);	
			var tr1=document.createElement('TR');
				var td1=document.createElement('TD');
				td1.title=clickToViewMsg;
				td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
								'<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">'+calEventsAmount+'New Calendar Events</a>';
			tr1.appendChild(td1);
		tbl.appendChild(tr1);
			var tr1=document.createElement('TR');
				var td1=document.createElement('TD');
				td1.title=moreMsgs;
				td1.innerHTML='<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">...more</a>';
			tr1.appendChild(td1);
		tbl.appendChild(tr1);				
		
	}
	
	function addActionToURL(actionName){		
		var fullURL=document.URL;
		var urlPath=location.pathname;
		var contextPart=fullURL.length-urlPath.length;			
		var partialURL=fullURL.substring(0,contextPart);
		return partialURL+"/"+actionName;
	}
	
	//window.onload=checkForNewMessages;
	
	$(document).ready(function(){
		checkForNewMessages();
	});
	
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
				
				<TR><TD bgColor="#ffffff" class="box-border" align="left"> 
					<TABLE id="msgLinks" border="0" cellPadding="1" cellSpacing="1" width="100%" >				 
							
						</TABLE>
					</TD></TR>								
				</TABLE>				
			</TD>
		</TR>
	</TABLE>
</digi:form>
</module:display>
