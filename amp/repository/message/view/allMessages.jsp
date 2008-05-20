<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>


<digi:instance property="messageForm"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script langauage="JavaScript">
	function viewMessage(id) {
		openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,550,400);
	}
	
	function goToPage(page){	
		 <digi:context name="searchMsg" property="context/module/moduleinstance/messageActions.do"/>
		 url = "<%= searchMsg %>?actionType=viewAllMessages&page="+page;
		 document.forms[0].action =url;
		 document.forms[0].submit();		 
		 return true;
	}
	
	function toggleGroup(group_id){
		var strId='#'+group_id;
		$(strId+'_minus').toggle();
		$(strId+'_plus').toggle();		
		$('#msg_'+group_id).toggle('fast');		
		
		var partialUrl=addActionToURL('messageActions.do');
		var url=getUrl(partialUrl,group_id);
		var async=new Asynchronous();
		async.complete=makeRead;
		async.call(url);
	}
	
	
	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}
	
	function getUrl(url,group_id){				
		var result=url;
		result+='~actionType=makeMsgRead';
		result+='~msgStateId='+group_id;
		return result;
	}
	
	function makeRead (status, statusText, responseText, responseXML){
		activityXML=responseXML;
		var root=responseXML.getElementsByTagName('Messaging')[0].childNodes[0];
		var stateId=root.getAttribute('id');
		var isRead=root.getAttribute('read');
		if(isRead){
			var myid='#'+stateId+'_unreadLink';		
			$(myid).css("color","");		
		}
	}
	
	function deleteMsg(){
		return confirm("Are You Sure You Want To Remove This Message ?");
	}
	
	
	
</script>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:messagesAssosiatedWithTeam">List of Messages associated with Team</digi:trn>'>
							<c:if test="${messageForm.tabIndex==1}">
								<digi:trn key="message:Messages">Messages</digi:trn>							
							</c:if>
							<c:if test="${messageForm.tabIndex!=1}">
								<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&tabIndex=1&page=1">
                 					<digi:trn key="message:Messages">Messages</digi:trn>
                 				</a>
							</c:if>							
						</TD>
                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:alertsAssosiatedWithTeam">List of Alerts associated with Team</digi:trn>'>
							<c:if test="${messageForm.tabIndex==2}">
								<digi:trn key="message:Alerts">Alerts</digi:trn>							
							</c:if>
							<c:if test="${messageForm.tabIndex!=2}">
								<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&tabIndex=2&page=1">
									<digi:trn key="message:Alerts">Alerts</digi:trn>
								</a>							
							</c:if>
						</TD>
                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:approvalsAssosiatedWithTeam">List of Approvals associated with Team</digi:trn>'>
							<c:if test="${messageForm.tabIndex==3}">
								<digi:trn key="message:approvals">Approvals</digi:trn>
							</c:if>
							<c:if test="${messageForm.tabIndex!=3}">
								<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&tabIndex=3&page=1">
									<digi:trn key="message:approvals">Approvals</digi:trn>
								</a>
							</c:if>
						</TD>
                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
                    	<TD bgColor=#c9c9c7 class=box-title	title='<digi:trn key="message:eventsAssosiatedWithTeam">List of Events associated with Team</digi:trn>'>
							<c:if test="${messageForm.tabIndex==4}">
								<digi:trn key="message:ebents">Calendar Events</digi:trn>
							</c:if>
							<c:if test="${messageForm.tabIndex!=4}">
								<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&tabIndex=4&page=1">
									<digi:trn key="message:ebents">Calendar Events</digi:trn>
								</a>
							</c:if>							
						</TD>
                    	<TD background="module/aim/images/corner-r.gif"	height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>				
				<TR>
					<TD width="100%">
						<table align="center" width="30%" cellpadding="5" cellspacing="5">
							<tr>
								<td width="6%"></td>
								<td width="38%" align="right">
									<c:if test="${messageForm.childTab=='inbox'}">
										<digi:trn key="message:inbox">Inbox</digi:trn>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='inbox'}">
										<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&childTab=inbox&tabIndex=${messageForm.tabIndex}&page=1">
											<digi:trn key="message:inbox">Inbox</digi:trn>
										</a>
									</c:if>
								</td>
								<td width="2%"> |</td>
								<td width="14%" align="center">
									<c:if test="${messageForm.childTab=='sent'}">
										<digi:trn key="message:sent">Sent</digi:trn>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='sent'}">
										<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&childTab=sent&tabIndex=${messageForm.tabIndex}&page=1">
											<digi:trn key="message:sent">Sent</digi:trn>
										</a>
									</c:if>
								</td>
								<td width="2%"> |</td>
								<td width="38%" align="left">
									<c:if test="${messageForm.childTab=='draft'}">
										<digi:trn key="message:draft">Draft</digi:trn>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='draft'}">
										<a href="${contextPath}/message/messageActions.do?actionType=viewAllMessages&childTab=draft&tabIndex=${messageForm.tabIndex}&page=1">
											<digi:trn key="message:draft">Draft</digi:trn>
										</a>
									</c:if>
								</td>
							</tr>
						</table>
					</TD>					
				</TR>		
				<logic:notEmpty name="messageForm" property="pagedMessagesForTm">
					<TR><TD bgColor="#ffffff" class="box-border" align="left"> 
						<TABLE border="0" cellPadding="1" cellSpacing="1" width="100%" >
						<c:forEach items="${messageForm.pagedMessagesForTm}" var="all" varStatus="status">
								<c:choose>
									<c:when test="${(status.index)%2 eq 0}">
										<c:set var="backGround">#ffffff</c:set>
									</c:when>
									<c:when test="${(status.index)%2 eq 1}">
										<c:set var="backGround">#eeeeee</c:set>
									</c:when>
								</c:choose>
								<tr height="20px" style="background-color: ${backGround};" >
									<td valign="top">
										<img id="${all.id}_plus"  onclick="toggleGroup('${all.id}')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
										<img id="${all.id}_minus" onclick="toggleGroup('${all.id}')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display : none"/>
									</td>
									<td width="60%">
										<div id="${all.id}_dots" >
											<c:if test="${all.read=='false'}">
												<span>													
														<A id="${all.id}_unreadLink" href="javascript:viewMessage(${all.id});" style="color:red">								
															<digi:trn key="${all.message.name}">${all.message.name}</digi:trn>
														</A>
												</span>
											</c:if>
											<c:if test="${empty all.read || all.read=='true'}">
												<A href="javascript:viewMessage(${all.id});">
													<digi:trn key="${all.message.name}">${all.message.name}</digi:trn>									 							
												</A>
											</c:if>
										</div>
										<div id="msg_${all.id}" style="display: none;">
											<table width="100%">
												<tr>
													<td><digi:trn key="message:from">From</digi:trn></td>
													<td>&nbsp;${all.sender}</td>
												</tr>
												<tr>
													<td><digi:trn key="message:receiver">Received</digi:trn></td>
													<td>&nbsp;${all.message.creationDate}</td>
												</tr>
												<tr>
													<td><digi:trn key="message:priority">priority</digi:trn> </td>
													<td>&nbsp;
														<c:if test="${all.message.priorityLevel==1}">Low</c:if>
														<c:if test="${all.message.priorityLevel==2}">Medium</c:if>
														<c:if test="${all.message.priorityLevel==3}">Critical</c:if>
														<c:if test="${all.message.priorityLevel==-1}">None</c:if>
													</td>
												</tr>
												<tr>
													<td><digi:trn key="message:msgDetails">Message Details</digi:trn> </td>
													<td>&nbsp;${all.message.description}</td>
												</tr>
											</table>
										</div>
									</td>	
									<td width="20%" align="right">
										<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
										 <c:set target="${urlParams}" property="msgStateId" value="${all.id}" />
										 <c:if test="${messageForm.childTab=='draft'}">
											<digi:link href="/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true" name="urlParams">
										 		<digi:trn key="message:Edit">Edit</digi:trn>
										 	</digi:link>		 
										 </c:if>
										 <c:if test="${messageForm.childTab!='draft'}">
											<digi:link href="/messageActions.do?actionType=forwardMessage&fwd=fillForm" name="urlParams">
										 		<digi:trn key="message:Forward">Forward</digi:trn>
										 	</digi:link>		 
										 </c:if> 
									</td>															
									<td width="20%" align="center">
										<digi:link href="/messageActions.do?editingMessage=false&actionType=removeSelectedMessage" name="urlParams" onclick="return deleteMsg()">
										 	<digi:trn key="message:delete">Delete</digi:trn>
										 </digi:link>										
									</td>
								</tr>
							</c:forEach>
						</TABLE>
					</TD></TR>
					<TR><TD> 
						<digi:trn key="message:pages">Pages</digi:trn>:
						<c:if test="${messageForm.page>1}">
							<c:set var="trn">
								<digi:trn key="message:firstPage">click here to go to first page</digi:trn>
							</c:set>
							<a href="javascript:goToPage('1')" title="${trn}" >&lt;&lt;</a>
							
							<c:set var="trn">
								<digi:trn key="message:previousPage">click here to go to previous page</digi:trn>
							</c:set>
							<a href="javascript:goToPage(${messageForm.page-1})" title="${trn}" > &lt; </a>
							
						</c:if>
						&nbsp;
						<c:if test="${not empty messageForm.allPages}">
							
							<c:set var="length" value="${messageForm.pagesToShow}"></c:set>
							<c:set var="start" value="${messageForm.offset}"/>								
							
							<logic:iterate id="pg" name="messageForm" property="allPages" offset="${start}" length="${length}">
								<c:if test="${pg==messageForm.page}"><font color="red">${pg}</font></c:if>
								<c:if test="${pg!=messageForm.page}">
									<c:set var="translation">
										<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
									</c:set>
									<a href="javascript:goToPage(${pg})" title="${translation}" >${pg}</a> 
								</c:if> |&nbsp;
							</logic:iterate>
						</c:if>
						<c:if test="${messageForm.page<messageForm.lastPage}">
							<c:set var="trn">
								<digi:trn key="message:previousPage">click here to go to next page</digi:trn>
							</c:set>
							<a href="javascript:goToPage(${messageForm.page+1})" title="${trn}" > &gt; </a>
							
							<c:set var="trn">
								<digi:trn key="message:firstPage">click here to go to last page</digi:trn>
							</c:set>
							<a href="javascript:goToPage(${messageForm.lastPage})" title="${trn}" >&gt;&gt;</a>							
						</c:if>
						&nbsp; ${messageForm.page}of ${messageForm.lastPage}
					</TD></TR>
				</logic:notEmpty>			
			</TABLE>				
		</TD>
	</TR>
</TABLE>