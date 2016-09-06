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
  <digi:form action="/messageActions.do" style="margin-bottom:0;">
    <html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
    <c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
    <c:set var="teamType">${sessionScope.currentMember.teamAccessType}</c:set>


    <script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
    <script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
    <script type="text/javascript">
    var inboxFull='<digi:trn key="message:fullMailBox" jsFriendly="true">Your MailBox Is Full</digi:trn>';
    var deleteData='<digi:trn key="message:plzDeleteData" jsFriendly="true">Please delete messages or you will not get newer ones</digi:trn>';
    var newmessages=' <digi:trn key="message:newmessages" jsFriendly="true">New Messages</digi:trn>';
    var newalerts=' <digi:trn key="message:newalerts" jsFriendly="true">New Alerts</digi:trn>';
    var newapprovals=' <digi:trn key="message:newaprovals" jsFriendly="true">New Approvals</digi:trn>'
    var newcalevents=' <digi:trn key="message:newcalevents" jsFriendly="true">New Calendar Events</digi:trn>'
    var moremessages='<digi:trn key="message:moremessages" jsFriendly="true">More messages</digi:trn>'
    var newCount=0;
    var prevCount=0;
    var isInboxFull='false';
    var firstEntry=0;

    function initMsgDiv(){

      var mdv=document.getElementById("msgDiv");
      var myWidth = 0, myHeight = 0;
      try{
        if( typeof( window.innerWidth ) == 'number' ) {
          //Non-IE
          myWidth = window.innerWidth;
          myHeight = window.innerHeight;
        } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
          //IE 6+ in 'standards compliant mode'
          myWidth = document.documentElement.clientWidth;
          myHeight = document.documentElement.clientHeight;
        } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
          //IE 4 compatible
          myWidth = document.body.clientWidth;
          myHeight = document.body.clientHeight;
        }

      }catch(ex){
      }
      myWidth-=270;
      myHeight-=150;
      //mdv.setAttribute("style","left:"+myWidth+";top:"+myHeight+";")
      mdv.style.left=myWidth;
      mdv.style.top=myHeight;
      mdv.style.zIndex=1;
    }

    function getInterval(){
      var interval=60000*document.getElementsByName('msgRefreshTimeCurr')[0].value;
      if(interval<=0){
        return 60000;
      }
      return interval;
    }

    function showMessage(fullInbox){   
    	var titleTD=document.getElementById('titleTD');
    	var textTD=document.getElementById('textTD');
    	if(fullInbox==true){    	 
    		titleTD.innerHTML=inboxFull; 
    		textTD.innerHTML=deleteData; 
    	}else{    	 
    		titleTD.innerHTML='<digi:trn jsFriendly="true">New Message</digi:trn>'; 
    		textTD.innerHTML='<digi:trn jsFriendly="true">You have a new Message</digi:trn>'; 
    	}
      $('#msgDiv').show("slow");
      window.setTimeout("hideMessage()",4000);
    }

    function hideMessage(){
		$('#msgDiv').hide("slow");
    	id=window.setTimeout("checkForNewMessages()",getInterval());
    }

var clickToViewMsg='<digi:trn key="message:clickToEditAlert" jsFriendly="true">Click here to view Message</digi:trn>';
    var moreMsgs='<digi:trn key="message:clickToViewMoreMessages" jsFriendly="true">Click here to view More Messages</digi:trn>';


    //setting timer to check for new messages after specified time
    if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
      id=window.setTimeout("checkForNewMessages()",getInterval());
    }

    function checkForNewMessages(){
      var url=addActionToURL('message/messageActions.do?actionType=checkForNewMessage');
      var async=new Asynchronous();
      async.complete=getNewMessagesAmount;
      async.call(url);
      if(newCount!=prevCount){
        showMessage(false);
        prevCount=newCount;
      }else{
        id=window.setTimeout("checkForNewMessages()",getInterval());
      }
    }

    function viewMessage(id) {
      window.open('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,'','channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
      //openURLinWindow('${contextPath}/message/messageActions.do?actionType=viewSelectedMessage&msgStateId='+id,550,400);
    }

    function getNewMessagesAmount(status, statusText, responseText, responseXML){
      var root=responseXML.getElementsByTagName('Messaging')[0].childNodes[0];

      var msgsAmount=root.getAttribute('messages');
      var alertsAmount=root.getAttribute('alerts');
      var approvalsAmount=root.getAttribute('approvals');
      var calEventsAmount=root.getAttribute('calEvents');
      
      if(isInboxFull=='false'){
      	  isInboxFull=root.getAttribute('inboxFull');
	      if(isInboxFull=='true' && firstEntry==0){
	      	showMessage(true);
	      	firstEntry=1;
	      }
      }
      

      newCount=msgsAmount+alertsAmount+approvalsAmount+calEventsAmount;
		
      $('#msgLinks li').each(function(index) {
    	  var n = jQuery(this)// <- This works
    	  n.remove();
  	  });
      
      $('#msgLinks a').each(function(index) {
    	  var n = jQuery(this)// <- This works
    	  n.remove();
  	  });
      //creating table
      var div=$('#msgLinks');
    
        var li= $('<li></li>');
      li.title=clickToViewMsg;
      li.append('<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1&childTab=inbox">'+msgsAmount+''+newmessages+'</a>');
      li.addClass('tri');
	  <feature:display name="Message tab" module="Messages">
      	div.append(li);
      </feature:display>
      
      var li= $('<li></li>');
      li.title=clickToViewMsg;
      li.append('<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&childTab=inbox">'+alertsAmount+''+newalerts+'</a>');
      li.addClass('tri');
      <feature:display name="Alert tab" module="Messages">
      	div.append(li);
      </feature:display>
      
      var li= $('<li></li>');
      li.title=clickToViewMsg;
      li.append('<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">'+approvalsAmount+''+newapprovals+'</a>');
      li.addClass('tri');
      <feature:display name="Approval Tab" module="Messages">
      	div.append(li);
      </feature:display>
	 
      var li= $('<li></li>');
      li.title=clickToViewMsg;
      li.append('<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">'+calEventsAmount+''+newcalevents+'</a>');
      li.addClass('tri');
      <feature:display name="Event Tab" module="Messages">
      	div.append(li);
      </feature:display>
      
      div.append('<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1" style="padding-top:10px;margin-left:12px;margin-top:5px; margin-bottom: 7px; display:block;">'+moremessages+'</a>');
      
      
    }

    function addActionToURL(actionName){
      var timestmp = new Date().getTime();
      var fullURL=document.URL;
      var urlPath=location.pathname;
      var contextPart=fullURL.length-urlPath.length;
      var partialURL=fullURL.substring(0,contextPart);
      return partialURL+"/"+actionName+"&lastTimestamp="+timestmp;
    }
    </script>
    
    <feature:display name="My Messages" module="Messages">
    <div class="right_menu" style="margin-top:20px;">
		<div class="right_menu_header">
			<div class="right_menu_header_cont"><digi:trn>My Messages</digi:trn></div>
		</div>
		<div class="right_menu_box">
			<div id="msgLinks" class="right_menu_cont">
				<!-- LINKS HERE -->
			</div>
		</div>
	</div>
    <div id="msgDiv" name="msgDiv" style="display : none; position: absolute;width: 250px; background-color: #317082;">
      <table style="width: 250px;">
        <tr>
          <td id="titleTD" style="font-family: Tahoma; font-size: 12px; color: White; font-weight: bold;background-color: #317082; padding: 5px 2px 2px 2px;">
       			  
          </td>
        </tr>
        <tr>
          <td id="textTD" colspan="2" style="font-family: Verdana; font-size: 10px; font-weight:bold;color: Black; background-color: #DBF0E6; margin-top: 2px; padding: 35px 35px 35px 35px;">
          
          </td>
        </tr>
      </table>
    </div>
    </feature:display>
  </digi:form>
  <script type="text/javascript">
    //jQuery(document).ready(function(){
      initMsgDiv();
      checkForNewMessages();
    //});
  </script>
</module:display>
