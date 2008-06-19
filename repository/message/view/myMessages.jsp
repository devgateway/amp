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
    <style type="text/css">
    body{
    font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
    margin:0px;

    }

    .dhtmlgoodies_question{
    position:absolute;
    color:#FFF;
    font-size:14px;
    background-color:#317082;
    width:250px;
    margin-bottom:2px;
    margin-top:2px;
    padding-left:2px;
    background-image:url('images/bg_answer.gif');
    background-repeat:no-repeat;
    background-position:top right;
    height:20px;
    overflow:hidden;
    cursor:pointer;
    font-weight:bold;
    }

    .dhtmlgoodies_answer{
    position:absolute;
    border:1px solid #317082;
    background-color:#E2EBED;
    width:250px;
    font-weight:bold;
    visibility:hidden;
    height:0px;
    font-size:11px;
    overflow:hidden;
    }

    .dhtmlgoodies_answer_content{
    padding:1px;
    font-size:11px;
    font-weight:bold;
    }

    </style>
    <script type="text/javascript">

    var dhtmlgoodies_slideSpeed = 10;
    var dhtmlgoodies_timer = 10;

    var objectIdToSlideDown = false;
    var dhtmlgoodies_activeId = false;
    var dhtmlgoodies_slideInProgress = false;

    function showHideContent(e,inputId)
    {
      if(dhtmlgoodies_slideInProgress)return;
      dhtmlgoodies_slideInProgress = true;
      if(!inputId)inputId = this.id;
      inputId = inputId + '';
      var numericId = inputId.replace(/[^0-9]/g,'');
      var answerDiv = document.getElementById('dhtmlgoodies_a' + numericId);

      objectIdToSlideDown = false;

      if(!answerDiv.style.display || answerDiv.style.display=='none'){
        if(dhtmlgoodies_activeId &&  dhtmlgoodies_activeId!=numericId){
          objectIdToSlideDown = numericId;
          slideContent(dhtmlgoodies_activeId,(dhtmlgoodies_slideSpeed*-1));
        }else{

          answerDiv.style.display='block';
          answerDiv.style.visibility = 'visible';

          slideContent(numericId,dhtmlgoodies_slideSpeed);
        }
      }else{
        slideContent(numericId,(dhtmlgoodies_slideSpeed*-1));
        dhtmlgoodies_activeId = false;
      }
    }

    function slideContent(inputId,direction)
    {

      var obj =document.getElementById('dhtmlgoodies_a' + inputId);
      var contentObj = document.getElementById('dhtmlgoodies_ac' + inputId);
      height = obj.clientHeight;
      if(height==0)height = obj.offsetHeight;
      height = height + direction;
      rerunFunction = true;
      if(height>contentObj.offsetHeight){
        height = contentObj.offsetHeight;
        rerunFunction = false;
      }
      if(height<=1){
        height = 1;
        rerunFunction = false;
      }

      obj.style.height = height + 'px';
      var topPos = height - contentObj.offsetHeight;
      if(topPos>0)topPos=0;
      contentObj.style.top = topPos + 'px';
      if(rerunFunction){
        setTimeout('slideContent(' + inputId + ',' + direction + ')',dhtmlgoodies_timer);
      }else{
        if(height<=1){
          obj.style.display='none';
          if(objectIdToSlideDown && objectIdToSlideDown!=inputId){
            document.getElementById('dhtmlgoodies_a' + objectIdToSlideDown).style.display='block';
            document.getElementById('dhtmlgoodies_a' + objectIdToSlideDown).style.visibility='visible';
            slideContent(objectIdToSlideDown,dhtmlgoodies_slideSpeed);
          }else{
            dhtmlgoodies_slideInProgress = false;
          }
        }else{
          dhtmlgoodies_activeId = inputId;
          dhtmlgoodies_slideInProgress = false;
        }
      }
    }

    function initShowHideDivs()
    {
      var divs = document.getElementsByTagName('DIV');
      var divCounter = 1;
      for(var no=0;no<divs.length;no++){
        if(divs[no].className=='dhtmlgoodies_question'){
          divs[no].onclick = showHideContent;
          divs[no].id = 'dhtmlgoodies_q'+divCounter;
          var answer = divs[no].nextSibling;
          while(answer && answer.tagName!='DIV'){
            answer = answer.nextSibling;
          }
          answer.id = 'dhtmlgoodies_a'+divCounter;
          contentDiv = answer.getElementsByTagName('DIV')[0];
          contentDiv.style.top = 0 - contentDiv.offsetHeight + 'px';
          contentDiv.className='dhtmlgoodies_answer_content';
          contentDiv.id = 'dhtmlgoodies_ac' + divCounter;
          answer.style.display='none';
          answer.style.height='1px';
          answer.className="dhtmlgoodies_answer";
          divCounter++;
        }
      }
    }

    function showMessage(){
      var qdv=document.getElementById("dhtmlgoodies_q1");
      var adv=document.getElementById("dhtmlgoodies_a1");

      var myWidth = 0, myHeight = 0;
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
      qdv.style.left=myWidth-260;
      qdv.style.top=myHeight-97;

      adv.style.left=myWidth-260;
      adv.style.top=myHeight-75;

      qdv.style.visibility="visible";
      showHideContent(null,"dhtmlgoodies_q1");
      window.setTimeout("hideMessage()",3000,"JavaScript");
    }

    function hideMessage(){
      showHideContent(null,"dhtmlgoodies_q1");
      var qdv=document.getElementById("dhtmlgoodies_q1");
      qdv.style.visibility="hidden";
      var adv=document.getElementById("dhtmlgoodies_a1");
      adv.style.visibility="hidden";
      id=window.setTimeout("checkForNewMessages()",20000,"JavaScript");
    }

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
      //60000*document.getElementsByName('msgRefreshTimeCurr')[0].value
      showMessage();
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
      var body=document.createElement('TBODY');
      var tr1=document.createElement('TR');
      var td1=document.createElement('TD');
      td1.title=clickToViewMsg;
      td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
      '<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">'+msgsAmount+'New Messages</a>';
      tr1.appendChild(td1);
      body.appendChild(tr1);

      var tr1=document.createElement('TR');
      var td1=document.createElement('TD');
      td1.title=clickToViewMsg;
      td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
      '<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2">'+alertsAmount+'New Alerts</a>';
      tr1.appendChild(td1);
      body.appendChild(tr1);
      var tr1=document.createElement('TR');
      var td1=document.createElement('TD');
      td1.title=clickToViewMsg;
      td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
      '<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">'+approvalsAmount+'New Approvals</a>';
      tr1.appendChild(td1);
      body.appendChild(tr1);
      var tr1=document.createElement('TR');
      var td1=document.createElement('TD');
      td1.title=clickToViewMsg;
      td1.innerHTML='<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10> &nbsp'+
      '<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">'+calEventsAmount+'New Calendar Events</a>';
      tr1.appendChild(td1);
      body.appendChild(tr1);
      var tr1=document.createElement('TR');
      var td1=document.createElement('TD');
      td1.title=moreMsgs;
      td1.innerHTML='<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">...more</a>';
      tr1.appendChild(td1);
      body.appendChild(tr1);
      tbl.appendChild(body);
    }

    function addActionToURL(actionName){
      var fullURL=document.URL;
      var urlPath=location.pathname;
      var contextPart=fullURL.length-urlPath.length;
      var partialURL=fullURL.substring(0,contextPart);
      return partialURL+"/"+actionName;
    }

    $(document).ready(function(){
      initShowHideDivs();checkForNewMessages();
    });

    </script>
    <br />
    <div id="content"  class="yui-skin-sam" style="width:100%;">
      <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
        <ul class="yui-nav">
          <li class="selected">
            <a title='<digi:trn key="message:alertsAssosiatedWithTeam">List of Alerts associated with Team</digi:trn>'>
            <div>
              <digi:trn key="message:myMessages">My Messages</digi:trn>
            </div>
</a>
          </li>
        </ul>
        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
          <TABLE id="msgLinks" border="0" cellPadding="0" cellSpacing="0" width="100%" >

          </TABLE>
        </div>
      </div>
    </div>
    <div class="dhtmlgoodies_question" id="dhtmlgoodies_q1">New message</div>
    <div class="dhtmlgoodies_answer" id="dhtmlgoodies_a1">
      <div>
        <table>
          <tr>
            <td>
            New message
            </td>
          </tr>
          <tr>
            <td>
            You have new message
            </td>
          </tr>
        </table>
      </div>
    </div>
  </digi:form>
</module:display>
