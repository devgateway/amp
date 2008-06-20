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
    <script type="text/javascript">

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
    }

    function showMessage(){
      $('#msgDiv').show("slow");
      window.setTimeout("hideMessage()",4000,"JavaScript");
    }

    function hideMessage(){
      $('#msgDiv').hide("slow");
      id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
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
      td1.innerHTML='<div  style="margin-left:12px;margin-top:5px; margin-bottom:7px"><a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">More messages ...</a></div>';
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
    </script>
    <br />
    <div id="content" class="yui-skin-sam" style="width:100%;">
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
    <div id="msgDiv" name="msgDiv" style="display : none; position: absolute;width: 250px; background-color: #317082;">
      <table style="width: 250px;">
        <tr>
          <td style="font-family: Tahoma; font-size: 12px; color: White; font-weight: bold;background-color: #317082; padding: 5px 2px 2px 2px;">
          New message
          </td>
          <!--
          <td style="font-size: 11px; color: Red; text-align: right; font-weight: bold;" onclick="hideMessage();">
          [Close]
          </td>-->
        </tr>
        <tr>
          <td colspan="2" style="font-family: Verdana; font-size: 10px; font-weight:bold;color: Black; background-color: #DBF0E6; margin-top: 2px; padding: 35px 35px 35px 35px;">
          You have new message
          </td>
        </tr>
      </table>
    </div>
  </digi:form>
  <script type="text/javascript">
    $(document).ready(function(){
      initMsgDiv();
      checkForNewMessages();
    });
  </script>
</module:display>
