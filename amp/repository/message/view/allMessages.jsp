<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<!-- Yahoo Panel --> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>"></script>

<style>
<!--
.settings{
    font-size:11px;font-family:Arial,Helvetica,sans-serif;
}
#selectedMessagePanel a:link{
     color: #05528B;
     text-decoration:underline;
    
}
#selectedMessagePanel a:hover{
      color: #05528B;
      background-color:#FFFFFF;
  
}

#selectedMessagePanel .bd { 
    
    /* Apply scrollbars for all browsers. */ 
    overflow: auto; 
} 
.yui-panel-container.hide-scrollbars #selectedMessagePanel .bd { 
	    /* Hide scrollbars by default for Gecko on OS X */ 
	    overflow: hidden; 
	} 
	
	.yui-panel-container.show-scrollbars #selectedMessagePanel .bd { 
	    /* Show scrollbars for Gecko on OS X when the Panel is visible  */ 
	    overflow: auto; 
} 
	


.my-border-style {
      border-top: 1px solid  #f4f4f2;
}
.trOdd {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.trEven{
	background-color:#FFFFFF;
	font-size:8pt !important;
	padding:2px;
}

.trHeading {
	
	font-size:11pt;
	padding:2px;
}

.trPagination {
	font-size:11pt;
	padding:2px;
}

.contentbox_border{
        border: 1px solid black;
	border-width: 0px 1px 1px 1px; 
	background-color: #f4f4f2;
}

.Hovered {
	background-color:#a5bcf2;
}

.userMsg{
background-color:yellow;
}
.scrollable {
    height: 400px; overflow: auto; width:100%;
}

-->
</style>

<digi:instance property="messageForm"/>
<digi:form action="/messageActions.do">
<html:hidden name="messageForm" property="msgRefreshTimeCurr"/>
<html:hidden name="messageForm" property="tabIndex"/>
<html:hidden name="messageForm" property="childTab"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script langauage="JavaScript">

    var  selectedMessagePanel;
    var noMsgs="<digi:trn>No Messages Present</digi:trn>";
    var noAlerts="<digi:trn>No Alerts Present</digi:trn>";
    var noApprovals="<digi:trn>No Pending Approvals</digi:trn>";
    var noEvents="<digi:trn>No Upcoming Events</digi:trn>";
   	var from='<digi:trn>From</digi:trn>';
    var to='<digi:trn>To</digi:trn>';
	var date='<digi:trn>Date</digi:trn>';
	var prLevel='<digi:trn>Priority</digi:trn>';
	var desc='<digi:trn>Message Details</digi:trn>';
	var editBtn='<digi:trn>Edit</digi:trn>';
	var fwdBtn='<digi:trn>Forward</digi:trn>';
	var deleteBtn='<digi:trn>Delete</digi:trn>';
	var pagesTrn='<digi:trn>Pages</digi:trn>';
	var ofTrn='<digi:trn>of</digi:trn>';
	var firstPage='<digi:trn>click here to go to first page</digi:trn>';
	var prevPage='<digi:trn>click here to go to previous page</digi:trn>';
	var nextPage='<digi:trn>Click here to go to next page</digi:trn>';
	var lastPg='<digi:trn>click here to go to last page</digi:trn>';
	var referenceURL='<digi:trn>Reference URL</digi:trn>';
    var forwardClick='<digi:trn> Click on this icon to forward message&nbsp;</digi:trn>';
    var editClick='<digi:trn> Click on this icon to edit message&nbsp;</digi:trn>';
    var replyClick='<digi:trn> Click on this icon to reply message&nbsp;</digi:trn>';
    var deleteClick='<digi:trn> Click on this icon to delete message&nbsp;</digi:trn>';
    var viewMessage='<digi:trn> Click here to view the message</digi:trn>';
    var viewDetails='<digi:trn>Click here to view details</digi:trn>';
	//used to define whether we just entered page from desktop
	var firstEntry=0;
	var currentPage=1;
    var messages;
	var slMsgId;
	var lastTimeStamp;
	//used to hold already rendered messages
	var myArray=new Array();

	//for sorting
	var sortByName='name';
	var sortByDate='creationDate';
	var sortedBy='creationDate';
	
	window.onload=getMessages;
		
	//setting timer to check for new messages after specified time	
	if(document.getElementsByName('msgRefreshTimeCurr')[0].value>0){
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}

    function hoverTr(id, obj){
    	
    	if(slMsgId!=id){
         obj.className='Hovered';
        }   
    }
    
    function closeWindow() {	
       selectedMessagePanel.destroy();
    }
    
    /*code below doesn't look good... but still
     *  it attachs events to rows: mouse over(makes row color darker) 
     *  and mouse out(returns to row it basic color)
     */

    function paintTr(msgTR,i){
        var className='';
        if(i!=1&&i%2==0){
            msgTR.className = 'trEven';
            className="this.className='trEven'";                                                            
        }
        else{
            msgTR.className = 'trOdd';
            className="this.className='trOdd'";
        }        
        
        var setBGColor = new Function(className);
       
        /*msgTR.onmouseover=hoverTr;*/
        msgTR.onmouseout=setBGColor;
      
        return msgTR;
    }   
    


	 function checkForNewMessages(){
		lastTimeStamp = new Date().getTime(); 
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage+'&timeStamp='+lastTimeStamp);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}
	
     function loadSelectedMessage(id){
        /*
         * some messages need long time to load,
         * that is why we create blank panel here, so user will see blank panel
         * before function call is completed
         */

        //create div to hold selected message
        var div=document.createElement('DIV');
        div.id="selectedMessagePanel";
        document.body.appendChild(div);

        // create body div to hold selected message
        var divBody=document.createElement('DIV');
        divBody.className="bd";
        divBody.id="msg_bd";
        divBody.className='scrollable'
        divBody.innerHTML='<digi:img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/><digi:trn>Loading, please wait ...</digi:trn>';
        div.appendChild(divBody);
        selectedMessagePanel=new YAHOO.widget.Panel("selectedMessagePanel",{
            width: 600,
            constraintoviewport: true,
            fixedcenter: true,
            Underlay:"shadow",
            modal: true,
            close:true,
            visible:true,
            draggable:true} );
        selectedMessagePanel.beforeHideEvent.subscribe(closeWindow);
        selectedMessagePanel.render();
        var url;
        var ind=id.indexOf('_fId');
        if(ind!=-1){
            var msgId=id.substring(0,ind);
            url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgId='+msgId);
        }
        else{
            url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgStateId='+id);
            markMsgeAsRead(id);
        }
        var async=new Asynchronous();
        async.complete=viewMsg;
        async.call(url);

    }
    	
	function openObjectURL(url){
            window.open(url,'','channelmode=no,directories=no,menubar=yes,resizable=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes');
            //openURLinWindow(url,600,550);
	
        }
	
    function viewMsg(status, statusText, responseText, responseXML) {
        var divBody=document.getElementById("msg_bd");
        divBody.setAttribute("visibility","visible");
        divBody.innerHTML=responseText;
	}
        
    function unCheckMessages() {
         var chk=document.messageForm.getElementsByTagName('input');
         for(var i=0;i<chk.length;i++){
             if(chk[i].type == 'checkbox'&&chk[i].checked){
                 alert("Please uncheck or delete selected message(s)");
                 return false;
             }
         }
         return true;
     }

     function deleteMessageIds() {
         return getSelectedMessagesIds();
     }     

	function getSelectedMessagesIds() {
		var msgIds='';
		$("input[id^='delChkbox_']:checked").each(function(){
	        msgIds+=this.value+',';
	    })            
		if(msgIds.length>0){
	       msgIds=msgIds.substring(0,msgIds.length-1);
		}
	    return msgIds;
	}  

	function getAllMessagesIds() {
		var msgIds='';
		$("input[id^='delChkbox_']").each(function(){
	        msgIds+=this.value+',';
	    })            
		if(msgIds.length>0){
	       msgIds=msgIds.substring(0,msgIds.length-1);
		}
	    return msgIds;
	}   
	
	function deleteMessage(msgId) {
            var flag=false;
             if(msgId!=null&&deleteMsg()){
                flag=true;
            }
            if(msgId==null){
                msgId=getSelectedMessagesIds();
                if(msgId.length==0){
                    alert("Please select messages");
                     flag=false;  
                }
                else{
                    if(deleteMsgs()){                                                         
                         flag=true;  
                   }
                }
                
            }
                    
            
           
		if(flag){
			//remove current element from array
			var idsArray=msgId.split(',');
			var tbl=document.getElementById('msgsList');
			for(var i=0;i<idsArray.length;i++){
				var index=getIndexOfElement(idsArray[i]);
				if(index!=-1){
					myArray.splice(index,1);	
					//removing TR from rendered messages list
							
					var img=document.getElementById(idsArray[i]+'_plus');
					var imgTD=img.parentNode;
					var msgTR=imgTD.parentNode;
					tbl.tBodies[0].removeChild(msgTR);
				}
			}
                
                                
			 /* 
			  * after we delete row we need to repaint remain rows
			  *  its also reattachs mouse out event to rows: 
			  * (returns to row it basic (new) color)
			  */
			var trs;
			if(tbl.tBodies.length>0){
			    trs=tbl.tBodies[0].rows;
			}
                                
			if(trs!=null&&trs.length>0){
			
				var startIndex=0;
				if(trs[0].id=='blankRow'){
			   		startIndex=1;
				}
				for(var i=startIndex;i<trs.length;i++){
				    if(startIndex==1){
				        var className='';
				        if(i!=1&&i%2==0){
				            trs[i].className = 'trOdd';
				            className="this.className='trOdd'";
				
				        }
				        else{
				            trs[i].className='trEven';
				            className="this.className='trEven'";
				        }
				        var setBGColor = new Function(className);
				        trs[i].onmouseout=setBGColor;
				    }
				    else{
				        trs[i]=paintTr(trs[i],i);
				    }
				    
				
				}
			}
			//removing record from db
			var url=addActionToURL('messageActions.do');	
			url+='~actionType=removeSelectedMessage';
			url+='~editingMessage=false';
			url+='~removeStateIds='+msgId;
			url+='~page='+currentPage;
			lastTimeStamp = new Date().getTime();
			url+='~timeStamp='+lastTimeStamp;
			var async=new Asynchronous();
			async.complete=buildMessagesList;
			async.call(url);		
				
		}
	}
	
	function deleteMsg(){
		return confirm("Are You Sure You Want To Remove This Message ?");
	}
        
    function deleteMsgs(){
		return confirm("Are You Sure You Want To Remove Selected Messages ?");
	}
        
	function getIndexOfElement(elemId){
		var index=-1;
		for(var i=0;i<myArray.length;i++){
			if(myArray[i]==elemId){
				index=i;
				return index;
			}
		}
	}
	
	
	function goToPage(page){	
		if(myArray!=null && myArray.length>0){
			myArray.splice(0,myArray.length);
		}
		//creating table
		var tbl=document.getElementById('msgsList');
		var tblBody=tbl.getElementsByTagName('tbody')[0];	
		if(tblBody.childNodes!=null && tblBody.childNodes.length>0){
			while (tblBody.childNodes.length>0){
				tblBody.removeChild(tblBody.childNodes[0]);
			}
		}				
		currentPage=page;
		getMessages();		
	}
        
    function toggleGr(id){
        $('#'+id+'_minus'+':visible').slideUp('fast');
        $('#'+id+'_plus'+':hidden').slideDown('fast');
        $('#msg_'+id+':visible').slideUp('fast');
    }
        
   
        
    function toggleGroup(group_id){
	    if(group_id==slMsgId){
	    	slMsgId='';    	
	    }else{
	    	slMsgId=group_id;
	    	 
	    	var ind=group_id.indexOf('_fId');
	        var stateId;
	    	
	    	if(ind!=-1){
	    		stateId = group_id.slice(ind+4);
	    		slMsgId = stateId;    			
	    	}
	    }
	    
	    var strId='#'+group_id;
	    $(strId+'_minus').toggle();
	    $(strId+'_plus').toggle();
	    var ind=group_id.indexOf('_fId');
	    var stateId;
	       
	    if(ind!=-1){
	    	ind=group_id.indexOf('_fId');
	        stateId=group_id.slice(ind+4);   
	    }else{
	     	stateId=group_id;
	        markMsgeAsRead( stateId);          
	    }
	       
	    for(var j=0;j<messages.length;j++){ 
	    	var stId= messages[j].getAttribute('id');
	        if(stateId!=stId){
	        	toggleGr(stId);
	        }  
	        for(var i=0;i<messages[j].childNodes.length;i++){
	        	var forwardedMsg=messages[j].childNodes[i];
	            var parStId=forwardedMsg.getAttribute('parentStateId');
	            if(stateId!=parStId){
	            	var lastMsgId=forwardedMsg.getAttribute('msgId');
	                var fId=lastMsgId+'_fId'+parStId;
	                toggleGr(fId);
	            }
	                
	        }            
	    }
	    $('#msg_'+group_id).toggle('fast');                
	}
               
	//this is new function
	function markMessageAsRead(group_id){
		var selMsgsIds;
		if(group_id!=null){
			selMsgsIds=group_id;
		}else{
			selMsgsIds=getSelectedMessagesIds();
			if(selMsgsIds.length==0){
                alert("Please select messages");
                return false;  
            }
		}
		var partialUrl=addActionToURL('messageActions.do');
		var url=getUrl(partialUrl,selMsgsIds);
		var async=new Asynchronous();
		async.complete=makeRead;
		async.call(url);
	}
	                  
	function markMsgeAsRead(group_id){
        var partialUrl=addActionToURL('messageActions.do');
		var url=getUrl(partialUrl,group_id);
		var async=new Asynchronous();
		async.complete=makeRead;
		async.call(url);                
   }
	
	function getUrl(url,group_id){
		lastTimeStamp = new Date().getTime();
		var result=url;
		result+='~actionType=makeMsgRead';
		result+='~msgStateId='+group_id;
		result+='~timeStamp='+lastTimeStamp;
		return result;
	}
	
	function makeRead (status, statusText, responseText, responseXML){		
		var messages=responseXML.getElementsByTagName('Messaging')[0].childNodes;
		if(messages!=null){
			for (var i=0;i<messages.length;i++){
				var stateId=messages[i].getAttribute('id');
				var isRead=messages[i].getAttribute('read');
				if(isRead){
					var myid='#'+stateId+'_unreadLink';
					$(myid).css("color","");		
				}
			}
			deselectAllCheckboxes();
		}
	}


	function sortBy(sort){
		if(myArray!=null && myArray.length>0){
			myArray.splice(0,myArray.length);
		}
		//creating table
		var tbl=document.getElementById('msgsList');
		var tblBody=tbl.getElementsByTagName('tbody')[0];	
		if(tblBody.childNodes!=null && tblBody.childNodes.length>0){
			while (tblBody.childNodes.length>0){
				tblBody.removeChild(tblBody.childNodes[0]);
			}
		}				
		currentPage=0;
		sortedBy = sort;
		
		lastTimeStamp = new Date().getTime();
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&sortBy='+sort+'&timeStamp='+lastTimeStamp);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}

	
	function getMessages(){
		lastTimeStamp = new Date().getTime();
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage+'&timeStamp='+lastTimeStamp);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}
	
	
	function buildMessagesList (status, statusText, responseText, responseXML){			
		var tbl=document.getElementById('msgsList');
		tbl.border='0';
		tbl.cellPadding="1";
		tbl.cellSpacing="1";
		tbl.width="100%";
		var browser=navigator.appName;
		var mainTag=responseXML.getElementsByTagName('Messaging')[0];
		if(mainTag!=null){
			var paginationTag=mainTag.getElementsByTagName('Pagination')[0];
			var informationTag=mainTag.getElementsByTagName('Information')[0];
			var totalNumber=document.getElementById('totalNumber');
			var totalHidden=document.getElementById('totalHidden');
			if(totalNumber!=null&&totalHidden!=null){
				totalNumber.innerHTML=informationTag.getAttribute("total");
				totalHidden.innerHTML=informationTag.getAttribute("totalHidden");
			}
                       
			//messages start
			var root=mainTag.getElementsByTagName('MessagesList')[0];
			if(root!=null){
				if(!root.hasChildNodes()&& firstEntry==0){
					var newTR=document.createElement('TR');
					var newTD=document.createElement('TD');
                    switch (document.messageForm.tabIndex.value){
                    case "2":
                        newTD.innerHTML=noAlerts;
                        break;
                    case "3":
                        newTD.innerHTML=noApprovals;
                        break;
                    case "4":
                        newTD.innerHTML=noEvents;
                        break;
            
                    default :newTD.innerHTML=noMsgs;
                    }
                                
					newTD.colSpan=4;
					newTR.appendChild(newTD);
					newTR.id="noMsg";
					var tableBody= tbl.getElementsByTagName("tbody");
					tableBody[0].appendChild(newTR);
					firstEntry++;
					return;
                               
				} else {
					
					var tablBody= tbl.getElementsByTagName("tbody");   
					messages=root.childNodes;
					
					// create a header row here
					if(document.getElementById("headingTr")==null){
						var headingTR = document.createElement('TR');
	                    headingTR.id="headingTr";
	                    headingTR.className = "trHeading";
	                    	var mshTitleTD = document.createElement('TD');
	                    	mshTitleTD.align='center';
	                    	mshTitleTD.setAttribute("colSpan","2");
	                    	var sp=document.createElement('SPAN');
	                    	if(sortedBy=='name'){
	                    		sp.innerHTML='<strong>Message Name</strong>';	
		                    }else {
		                    	sp.innerHTML='<A href="javascript:sortBy(\''+sortByName+'\')"; title="something">Message Name</A>';
			                }
	                    	
	                    	mshTitleTD.appendChild(sp);
	                    	headingTR.appendChild(mshTitleTD);

	                    	var msgDateTD = document.createElement('TD');
	                    	msgDateTD.align='center';
	                    	msgDateTD.setAttribute("nowrap","nowrap");
	                    	sp=document.createElement('SPAN');
	                    	if(sortedBy=='creationDate'){
	                    		sp.innerHTML='<strong>Received</strong>';
		                    }else {
		                    	sp.innerHTML='<A href="javascript:sortBy(\''+sortByDate+'\')"; title="something">Received</A>';
			                }
	                    	
	                    	msgDateTD.appendChild(sp);
	                    	headingTR.appendChild(msgDateTD);
	                    	
	                    	
	                    tablBody[0].appendChild(headingTR);
					}
                              
                  
					//var tblBody=tbl.getElementsByTagName('tbody')[0];
					//while (tblBody.childNodes.length>0){
					//	tblBody.removeChild(tblBody.childNodes[0]);
					//}
					if(myArray!=null && myArray.length>0){
						var whereToInsertRow=1;						
						for(var i=0;i<messages.length;i++){
							var msgId=messages[i].getAttribute('id');
                                                       
							for(var j=0;j<myArray.length;j++){
								if(msgId==myArray[j]){
									break;
								}else{
									if(j==myArray.length-1){
										if(paginationTag!=null){
											var pagParams=paginationTag.childNodes[0];
											var wasDelteActionCalled=pagParams.getAttribute('deleteWasCalled');
											if(wasDelteActionCalled=='true'){
                                                var msgTR;
												
												if(browser=="Microsoft Internet Explorer"){
										   			msgTR=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');
										   			
												}else{
                                                    msgTR=document.createElement('TR');      	
                                                    msgTR.setAttribute('onmouseover','hoverTr('+msgId+',this)');
                                               	}                                                 
                                               	var color=++j;                                    
                                                msgTR=paintTr(msgTR,color);                                                                                                
												tbl.tBodies[0].appendChild(createTableRow(tbl,msgTR,messages[i],true));
												myArray[myArray.length]=msgId;										
											}else{
												tbl.tBodies[0].insertRow(whereToInsertRow);
												var msgTr=tbl.tBodies[0].rows[whereToInsertRow];
												if(browser=="Microsoft Internet Explorer"){
									   				msgTr=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');	
												}else{
                                                                                                        
                                                    msgTr.setAttribute('onmouseover','hoverTr('+msgId+',this)');
												}
												msgTr=paintTr(msgTr,i);
												createTableRow(tbl,msgTR,messages[i],true);
												myArray[myArray.length]=msgId;
												whereToInsertRow++;										
												tbl.tBodies[0].removeChild(tbl.tBodies[0].lastChild);
											}
										}												
									}							
								}
							}
						}		
					} else {
						for(var i=0;i<messages.length;i++){				
							var msgId=messages[i].getAttribute('id');
							myArray[i]=msgId;							
							//creating tr
                                       
							var isMsgRead=messages[i].getAttribute('read');					
							if(browser=="Microsoft Internet Explorer"){
								var msgTr=document.createElement('<tr onmouseover="hoverTr('+msgId+',this);" onmouseout="paintTr(this,'+i+');"></tr>');
							}else{
                               	var msgTr=document.createElement('TR');	
 		 						msgTr.setAttribute('onmouseover','hoverTr('+msgId+',this)');
                        	}
                            msgTr=paintTr(msgTr,i);
							var myTR=createTableRow(tbl,msgTr,messages[i],true);													
        	                // var tablBody= tbl.getElementsByTagName("tbody"); test commenting and moving up !
                            tablBody[0].appendChild(myTR);            																				
						}//end of for loop
					}			
				}//messages end
				
				
				//pagination start			
				if(paginationTag!=null){
					var paginationParams=paginationTag.childNodes[0];
					var doMsgsExist=paginationParams.getAttribute('messagesExist');	
					if(doMsgsExist=='true'){
						var page=paginationParams.getAttribute('page');
						var allPages=paginationParams.getAttribute('allPages');
						var lastPage=paginationParams.getAttribute('lastPage');
						setupPagionation(paginationTag,parseInt(page),parseInt(allPages));
					}				
				}
				//pagination end
			}
		}
	}
       	
	function setupPagionation (paginationTag,page,allPages){
		currentPage=page;
		var paginationTR=document.getElementById('paginationPlace');
		paginationTR.className = "trPagination"
		while(paginationTR.firstChild != null){
			paginationTR.removeChild(paginationTR.firstChild);
		}
		
		var paginationParams=paginationTag.childNodes[0];
		if(paginationParams!=null){
			var paginationTD=document.createElement('TD');
			var paginationTDContent='<strong>'+pagesTrn+'</strong> :';
				if(currentPage>1){
					var prPage=currentPage-1;
					paginationTDContent+=':<a href="javascript:goToPage(1)" title="'+firstPage+'">&lt;&lt; </a> ';
					paginationTDContent+='<a href="javascript:goToPage('+prPage+')" title="'+prevPage+'" > &lt; </a>';								
				}
				paginationTDContent+='&nbsp';
				if(allPages!=null){
					var fromIndex=1;
					if((currentPage-2)<1){
						fromIndex=1;
					}else{
						fromIndex=currentPage-2;
					}
					var toIndex;
					if(currentPage+2>allPages){
						toIndex=allPages;
					}else{
						toIndex=currentPage+2;
					}
					for(var i=fromIndex;i<=toIndex;i++){
						if(i<=allPages && i==page) {paginationTDContent+='<font color="red">'+i+'</font>&nbsp;&nbsp;|&nbsp;&nbsp;';}
						if(i<=allPages && i!=page) {paginationTDContent+='<a href="javascript:goToPage('+i+')" title="'+nextPage+'">'+i+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;'; }
					}
				}
				if(page<allPages){
					var nextPg=page+1;									
					paginationTDContent+='<a href="javascript:goToPage('+nextPg+')" title="'+nextPage+'">&gt;</a>&nbsp;&nbsp;';
					paginationTDContent+='<a href="javascript:goToPage('+allPages+')" title="'+lastPg+'">&gt;&gt;</a> &nbsp;&nbsp;|&nbsp;';
				}	
				paginationTDContent+='&nbsp;'+page+ ofTrn +allPages;
			paginationTD.innerHTML=	paginationTDContent;						
			paginationTR.appendChild(paginationTD);						
		}
	}				
	
	
	//creates table rows with message information
	function createTableRow(tbl,msgTr,message,fwdOrEditDel){
	    
		var msgId=message.getAttribute('id');	//message state id
        var messageId=message.getAttribute('msgId');
        var isDraft=message.getAttribute('isDraft');
        var sateId;
        if(msgId==null){
            msgId=messageId;
        }
        else{
            sateId=msgId;
        }
        var newMsgId=message.getAttribute('parentStateId');// id of the hierarchy end for forwarding messages
		//create image's td
		var imgTD=document.createElement('TD');
               
        if(newMsgId!=null){
            msgId+='_fId'+newMsgId; // create id for forwarded message

        }
               
		imgTD.vAlign='top';	
               
		imgTD.innerHTML='<img id="'+msgId+'_plus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view/images/unread.gif" title="<digi:trn key="message:ClickExpandMessage">Click on this icon to expand message&nbsp;</digi:trn>"/>'+
			'<img id="'+msgId+'_minus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view/images/read.gif" style="display : none" <digi:trn key="message:ClickCollapseMessage"> Click on this icon to collapse message&nbsp;</digi:trn>/>';
		msgTr.appendChild(imgTD);
                
               
					
		//message name and description
		var nameTD=document.createElement('TD');				
		var msgName; 
        if(message.childNodes!=null&&message.childNodes.length>0){
            msgName="FW: "+message.getAttribute('name');
        }
        else{
             msgName=message.getAttribute('name');
        }
        if(fwdOrEditDel){
            nameTD.width='70%';}
        else{
            nameTD.width='95%';
        }
                   
		//creating visible div for message name
		var nameDiv=document.createElement('DIV');
        var visId;
        if(!fwdOrEditDel){
            visId=newMsgId+'_'+msgId+'_dots'
        }
        else{
           visId=+msgId+'_dots' 
        }
                
		nameDiv.setAttribute("id",visId);	
		var sp=document.createElement('SPAN');
		var isMsgRead=message.getAttribute('read');
		if(isMsgRead=='false'){
			sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:loadSelectedMessage(\''+msgId+'\')"; style="color:red;" title="'+viewMessage+'">'+msgName+'</A>';   
		}else {
            if(isDraft==null||isDraft=='false'){
                sp.innerHTML='<A id="'+msgId+'_unreadLink" href="javascript:loadSelectedMessage(\''+msgId+'\')"; title="'+viewMessage+'">'+msgName+'</A>';
            }
            else{
                sp.innerHTML=msgName;
            }
			
		}
		nameDiv.appendChild(sp);
		nameTD.appendChild(nameDiv);


		//getting received date
		var receivedTD=document.createElement('TD');
		receivedTD.vAlign="top";
		var received=message.getAttribute('received');
		receivedTD.innerHTML=received; //appending happens below, so date appears after message Name
		
					
		//creating hidden div for message description.It'll become visible after user clicks on twistie
		var descDiv=document.createElement('DIV');
		var invId='msg_'+msgId;
		descDiv.setAttribute("id",invId);	
		descDiv.style.display='none';
		//creating table inside hidden div
		var divTable=document.createElement('TABLE');
		var divTblBody=document.createElement('TBODY');
		divTable.appendChild(divTblBody);
		divTable.width='100%';
		var fromTR=document.createElement('TR');
		var fromTD1=document.createElement('TD');
		fromTD1.innerHTML='<strong>'+from+'</strong>';
		fromTR.appendChild(fromTD1);
		//getting sender
		var fromTD2=document.createElement('TD');
		var msgSender=message.getAttribute('from');
		if(fromTD2.textContent==undefined){
			var mytool=msgSender;
			var temp_array=mytool.split(";");
			fromTD2.innerText = temp_array[0];
		} else{
			var mytool=msgSender;
			var temp_array=mytool.split(";");
			fromTD2.textContent = temp_array[0];
		}
		fromTR.appendChild(fromTD2);
		divTblBody.appendChild(fromTR);
                        
		var toTR=document.createElement('TR');
		var toTD1=document.createElement('TD');
		toTD1.innerHTML='<strong>'+to+'</strong>';
		toTR.appendChild(toTD1);
		//getting receives
		var toTD2=document.createElement('TD');
		var msgReceiver=message.getAttribute('to');
		var receiver_array=msgReceiver.split(";");
		if(receiver_array.length>5){
 			msgReceiver="";
 			for(var j=0;j<5;j++){
   				if(receiver_array[j].indexOf("@") != -1){
    				msgReceiver+=receiver_array[j];
    			}
    			if(j!=4){
        			msgReceiver+="";
    			}else{
         			msgReceiver+="......";
    			}
 			}
		}

		if(toTD2.textContent==undefined){
			var mytool=msgReceiver;
			var temp_array=mytool.split(";");
			toTD2.innerText = temp_array[0];
			//toTD2.innerText=msgReceiver;
		} else{
			var mytool=msgReceiver;
			var temp_array=mytool.split(";");
			toTD2.textContent = temp_array[0];
			//toTD2.textContent=msgReceiver;
		}
                       
                    
		toTR.appendChild(toTD2);
		divTblBody.appendChild(toTR);
                       
		var receivedTR=document.createElement('TR');
		var receivedTD1=document.createElement('TD');
		receivedTD1.innerHTML='<strong>'+date+'</strong>';							
		receivedTR.appendChild(receivedTD1);
							
		//getting received date
		//var receivedTD2=document.createElement('TD');
		//var received=message.getAttribute('received');
		//receivedTD2.innerHTML=received;
		//receivedTR.appendChild(receivedTD2);
		//divTblBody.appendChild(receivedTR);		
						
		var priorityTR=document.createElement('TR');
		var priorityTD1=document.createElement('TD');
		priorityTD1.innerHTML='<strong>'+prLevel+'</strong>';
		priorityTR.appendChild(priorityTD1);
								
		var priorityTD2=document.createElement('TD');
		//getting priority level
		var priority=message.getAttribute('priority');
			if(priority==1){priorityTD2.innerHTML='low';}
			else if(priority==2){priorityTD2.innerHTML='Medium';}
			else if(priority==3){priorityTD2.innerHTML='Critical';}
			else if(priority==0){priorityTD2.innerHTML='None';}
		
		priorityTR.appendChild(priorityTD2);
		divTblBody.appendChild(priorityTR);	
				
		//getting URL
		var objectURL=message.getAttribute('objURL');
       	if(objectURL!='null'){
			var objURLTR=document.createElement('TR');
			var objURLTD1=document.createElement('TD');
			objURLTD1.innerHTML='<strong>'+referenceURL+'</strong>';
			objURLTR.appendChild(objURLTD1);
			var objURLTD2=document.createElement('TD');
			objURLTD2.innerHTML='<A href="javascript:openObjectURL(\''+objectURL+'\')";> '+viewDetails+'</A>';

			objURLTR.appendChild(objURLTD2);
			divTblBody.appendChild(objURLTR);
		}	
				
		var detailsTR=document.createElement('TR');
		var detailsTD1=document.createElement('TD');
              detailsTD1.width='30%';
		detailsTD1.innerHTML='<strong>'+desc+'</strong>';
		detailsTR.appendChild(detailsTD1);
			
		var detailsTD2=document.createElement('TD');
		//getting description
		var description=message.getAttribute('msgDetails');
              if(description!='null'){
              	description=description.substring(0,34);
                  detailsTD2.innerHTML=description+" .........";
              }else{
               	detailsTD2.innerHTML="&nbsp";
              }					
		detailsTR.appendChild(detailsTD2);
		divTblBody.appendChild(detailsTR);
                      
      		// create forwarded messages
        if(message.childNodes!=null&&message.childNodes.length>0){
            var forwardedTb=document.createElement('TABLE');
            forwardedTb.width="100%";
            forwardedTb.className="my-border-style";
            var forwardedTbody=document.createElement('TBODY');
            for(var i=0;i<message.childNodes.length;i++){
                var forwardedTR=document.createElement('TR');
                createTableRow(tbl,forwardedTR,message.childNodes[i],false);
                forwardedTbody.appendChild(forwardedTR);
            }
            forwardedTb.appendChild(forwardedTbody);
            var forwardTR=document.createElement('TR');
            var forwardTD=document.createElement('TD');
            forwardTD.setAttribute("colSpan","3");
            
            forwardTD.appendChild(forwardedTb);
            forwardTR.appendChild(forwardTD);
            divTblBody.appendChild(forwardTR);
        }
        descDiv.appendChild(divTable);	
        nameTD.appendChild(descDiv);
        msgTr.appendChild(nameTD);
        msgTr.appendChild(receivedTD);
      
        if(fwdOrEditDel){
        
        	if(isDraft!='true'){
        		replyTD=document.createElement('TD');
            	replyTD.width='10%';
            	replyTD.align='center';
            	replyTD.vAlign="top";
            	replyTD.innerHTML='<digi:link href="/messageActions.do?actionType=replyOrForwardMessage&reply=fillForm&editingMessage=true&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+replyClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view/images/reply.gif" border=0 hspace="2" /></digi:link>';
            	msgTr.appendChild(replyTD);
        	}    
        	        
			// forward or edit link
			fwdOrEditTD=document.createElement('TD');
			fwdOrEditTD.width='10%';
			fwdOrEditTD.align='center';
			fwdOrEditTD.vAlign="top";
			
			if(isDraft=='true'){
	            fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+editClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view/images/edit.gif" border=0 hspace="2" /></digi:link>';									
			}else{
				fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=replyOrForwardMessage&fwd=fillForm&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+forwardClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view/images/finalForward.gif" border=0  hspace="2" /></digi:link>';
			}
			msgTr.appendChild(fwdOrEditTD);	
						
			//delete link
			var deleteTD=document.createElement('TD');
			deleteTD.width='10%';
			deleteTD.align='center';
	        deleteTD.vAlign="top";
			//deleteTD.innerHTML='<digi:link href="/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId='+msgId+'">'+deleteBtn+'</digi:link>';
			deleteTD.innerHTML='<a href="javascript:deleteMessage(\''+msgId+'\')" style="cursor:pointer; text-decoration:underline; color: blue" title="'+deleteClick+'" ><img  src="/repository/message/view/images/trash_12.gif" border=0 hspace="2"/></a>';
			msgTr.appendChild(deleteTD);
			
	        //delete link
			var deleteTDCheckbox=document.createElement('TD');
			deleteTDCheckbox.width='10%';
			deleteTDCheckbox.align='center';
	        deleteTDCheckbox.vAlign="top";
	        var deleteCheckbox=document.createElement('input');
	        deleteCheckbox.type='checkbox';
	        deleteCheckbox.id='delChkbox_'+msgId;
			deleteCheckbox.value=msgId;
	        deleteTDCheckbox.appendChild(deleteCheckbox);
			msgTr.appendChild(deleteTDCheckbox);
        }
					
		return msgTr;			
	}
       
	function selectAllCheckboxes(){
		var allChkboxes=$("input[id^='delChkbox_']");
		if(allChkboxes!=null && allChkboxes.length>0){
			for(var i=0;i<allChkboxes.length;i++){
				allChkboxes[i].checked=true;
			}
		}
	}
	function deselectAllCheckboxes(){
		var allChkboxes=$("input[id^='delChkbox_']");
		if(allChkboxes!=null && allChkboxes.length>0){
			for(var i=0;i<allChkboxes.length;i++){
				allChkboxes[i].checked=false;
			}
		}
	}

	function createMessage(){
		messageForm.action="${contextPath}/message/messageActions.do?editingMessage=false&actionType=fillTypesAndLevels";
		messageForm.target = "_self";
		messageForm.submit();
	}        
 
$(document).ready(function(){
	   $("#displaySettingsButton").toggle(function(){
	     	$("#currentDisplaySettings").show('fast');
	     	$("#show").hide('fast');
	     	$("#hidde").css("background", "#CCDBFF" );
	     	$("#hidde").show('fast');
	   },function(){
	     	$("#currentDisplaySettings").hide('fast');
	     	$("#hidde").hide('fast');
	     	$("#show").css("background", "#CCDBFF" );
	     	$("#show").show('fast');
	   	});
});
        
</script>
<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr>
<td width="100%">
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />
</td>
</tr>
<tr>
<td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780 border="0">
    <tr>
   <td width=20>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border="0">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" onclick="return unCheckMessages()">
						<digi:trn>Portfolio</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>Message Module</digi:trn>&nbsp;&gt;&nbsp;
                        <c:choose>
	                    	<c:when test="${messageForm.tabIndex==1}">
	                        	<digi:trn>Messages</digi:trn>
	                        </c:when>
	                        <c:when test="${messageForm.tabIndex==2}">
	                        	<digi:trn>Alerts</digi:trn>
	                        </c:when>
	                        <c:when test="${messageForm.tabIndex==3}">
	                        	<digi:trn>Approvals</digi:trn>
	                        </c:when>
	                        <c:otherwise>
	                        	<digi:trn>Calendar Events</digi:trn>
	                        </c:otherwise>
                        </c:choose>
                        <c:if test="${messageForm.tabIndex!=3 && messageForm.tabIndex!=4}">
	                   		&nbsp;&gt;&nbsp;
	                        <c:choose>
		                        <c:when test="${messageForm.childTab=='inbox'}">
		                        	<digi:trn>Inbox</digi:trn>
		                        </c:when>
		                        <c:when test="${messageForm.childTab=='sent'}">
		                        	<digi:trn>Sent</digi:trn>
		                        </c:when>
		                        <c:otherwise>
		                        	<digi:trn>Draft</digi:trn>
		                    	</c:otherwise>
	                        </c:choose>
                        </c:if>
                       </span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
						<span class="subtitle-blue">
							<digi:trn>Message Module</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
                	<td noWrap vAlign="top">
						<TABLE cellPadding=0 cellSpacing=0 width="100%"	valign="top" align="left" border="0" >
	        				<TR>
	        					<TD STYLE="width:750">
                                	<DIV id="tabs">
                                     	<UL>
                                     	<feature:display name="Message tab" module="Messages">
                                			<c:if test="${messageForm.tabIndex==1}">
                                         		<LI>
                                            		<a name="node">
                                      					<div>
															<digi:trn>Messages</digi:trn>							
                                               			</div>
                                              		</a>
                                                    </LI>
												</c:if> 
												<c:if test="${messageForm.tabIndex!=1}">
                                                	<LI>
                                                    	<span>
                                    						<a onclick="return unCheckMessages()" href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=1">
                                                        		<div title='<digi:trn>List of Messages associated with Team</digi:trn>'>
	                 												<digi:trn>Messages</digi:trn>
                                                            	</div>
	                 										</a>
                                                        </span>
                                                    </LI>
											</c:if>							
                                		</feature:display>                  
                                        <feature:display name="Alert tab" module="Messages">           
                                        	<c:if test="${messageForm.tabIndex==2}">
                                            	<LI>
                                                 	<a name="node">
                                                    	<div>
															<digi:trn>Alerts</digi:trn>							
                                                         </div>
                                                      </a>
                                                 </LI>
										</c:if>
										<c:if test="${messageForm.tabIndex!=2}">
											<LI>
                                            	<span>
													<a onclick="return unCheckMessages()" href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2">
                                                    	<div title='<digi:trn>List of Alerts associated with Team</digi:trn>'>
															<digi:trn>Alerts</digi:trn>
                                                        </div>
													</a>							
                                                </span>
                                            </LI>
										</c:if>
                                        </feature:display>
                                        <feature:display name="Approval Tab" module="Messages">          
                                        	
												<c:if test="${messageForm.tabIndex==3}">
                                                    <LI>
                                                        <a name="node">
                                                            <div>
																<digi:trn>Approvals</digi:trn>
                                                        	</div>
                                                        </a>
                                                    </LI>
												</c:if>
												<c:if test="${messageForm.tabIndex!=3}">
                                                    <LI>
                                                        <span>
															<a onclick="return unCheckMessages()" href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=3">
                                                            	<div title='<digi:trn>List of Approvals associated with Team</digi:trn>'>
																	<digi:trn>Approvals</digi:trn>
                                                            	</div>
															</a>
                                                        </span>
                                                    </LI>
												</c:if>
                                          
                                        </feature:display>          
                                        <feature:display name="Event Tab" module="Messages">     
                                			<c:if test="${messageForm.tabIndex==4}">
                                            	<LI>
                                                	<a name="node">
                                                    	<div>
															<digi:trn>Calendar Events</digi:trn>
                                                        </div>
                                                      </a>
                                                 </LI>
											</c:if>
												<c:if test="${messageForm.tabIndex!=4}">
                                            		<LI>
                                                		<span>
															<a onclick="return unCheckMessages()" href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=4">
                                                        		<div title='<digi:trn>List of Events associated with Team</digi:trn>'>
																	<digi:trn>Calendar Events</digi:trn>
                                                        		 </div>
															</a>
                                                     	</span>
                                                  	</LI>
												</c:if>	
											</feature:display>						
											</UL>						
										</DIV>
                    	<div id="main">
						<DIV id="subtabs" style="width: 99%; background-color: #ccdbff; padding: 2px 2px 2px 2px; min-height: 17px; Font-size: 8pt; font-family: Arial, Helvetica, sans-serif;">
	                        <div style="pa">
	                        	<UL>
	                        	<feature:display name="Message tab" module="Messages">
	                        	<c:if test="${messageForm.tabIndex==1}">    
	                        	<field:display name="Inbox Message" feature="Message tab">                                                                
									<c:if test="${messageForm.childTab=='inbox'}">
                                    	<LI>
                                        	<span>
                                        		<digi:trn key="message:inbox">Inbox</digi:trn>&nbsp;&nbsp;|					
                                             </span>
                                         </LI>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='inbox'}">
                                    	<LI>
                                        	<div>
                                            	<span>
                                                	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                    	<digi:trn key="message:inbox">Inbox</digi:trn>
                                                     </a>&nbsp;&nbsp;|							
                                                 </span>
                                             </div>	
                                        </LI>
                                    </c:if>
								</field:display>
								<field:display name="Sent Message" feature="Message tab">		
								<c:if test="${messageForm.childTab=='sent'}">
                             		<LI>
                                		<span>
                                			<digi:trn key="message:sent">Sent</digi:trn>&nbsp;&nbsp;|					
                                		</span>
                                	</LI>
								</c:if>
								<c:if test="${empty messageForm.childTab || messageForm.childTab!='sent'}">
                                	<LI>
                                    	<div>
                                         	<span>
                                            	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                 	<digi:trn key="message:sent">Sent</digi:trn>
                                                 </a>&nbsp;&nbsp;|							
                                             </span>
                                        </div>	
                                    </LI>
								</c:if>
								</field:display>		 
								<field:display name="Draft Message" feature="Message tab">		
									<c:if test="${messageForm.childTab=='draft'}">
                                    	<LI>
                                        	<span>
                                            	<digi:trn key="message:draft">Draft</digi:trn>					
                                            </span>
                                        </LI>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='draft'}">
                                    	<LI>
                                        	<div>
                                            	<span>
                                                	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                    	<digi:trn key="message:draft">Draft</digi:trn>
                                                     </a>							
                                                 </span>
                                              </div>	
                                        </LI>
									</c:if>
								</field:display>
								</c:if>
								</feature:display>
								<feature:display name="Alert tab" module="Messages">
	                        	<c:if test="${messageForm.tabIndex==2}">    
	                        	<field:display name="Inbox Alert" feature="Alert tab">                                                                
									<c:if test="${messageForm.childTab=='inbox'}">
                                    	<LI>
                                        	<span>
                                        		<digi:trn key="message:inbox">Inbox</digi:trn>&nbsp;&nbsp;|					
                                             </span>
                                         </LI>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='inbox'}">
                                    	<LI>
                                        	<div>
                                            	<span>
                                                	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=inbox&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                    	<digi:trn key="message:inbox">Inbox</digi:trn>
                                                     </a>&nbsp;&nbsp;|							
                                                 </span>
                                             </div>	
                                        </LI>
                                    </c:if>
								</field:display>
								<field:display name="Sent Alert" feature="Alert tab">		
								<c:if test="${messageForm.childTab=='sent'}">
                             		<LI>
                                		<span>
                                			<digi:trn key="message:sent">Sent</digi:trn>&nbsp;&nbsp;|					
                                		</span>
                                	</LI>
								</c:if>
								<c:if test="${empty messageForm.childTab || messageForm.childTab!='sent'}">
                                	<LI>
                                    	<div>
                                         	<span>
                                            	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=sent&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                 	<digi:trn key="message:sent">Sent</digi:trn>
                                                 </a>&nbsp;&nbsp;|							
                                             </span>
                                        </div>	
                                    </LI>
								</c:if>
								</field:display>		 
								<field:display name="Draft Alert" feature="Alert tab">		
									<c:if test="${messageForm.childTab=='draft'}">
                                    	<LI>
                                        	<span>
                                            	<digi:trn key="message:draft">Draft</digi:trn>					
                                            </span>
                                        </LI>
									</c:if>
									<c:if test="${empty messageForm.childTab || messageForm.childTab!='draft'}">
                                    	<LI>
                                        	<div>
                                            	<span>
                                                	<a href="${contextPath}/message/messageActions.do?actionType=gotoMessagesPage&childTab=draft&tabIndex=${messageForm.tabIndex}" onclick="return unCheckMessages()">
                                                    	<digi:trn key="message:draft">Draft</digi:trn>
                                                     </a>							
                                                 </span>
                                              </div>	
                                        </LI>
									</c:if>
								</field:display>
								</c:if>
								</feature:display>
												 <LI style="float: right;">
									               <div>
													<span id="displaySettingsButton"  style="cursor: pointer;float: right; font-style: italic;">
														<div id="show"  style="display:block; float: right; margin:0 3px 0 0;"><digi:trn key="message:show">Show more information</digi:trn> &gt;&gt;</div>
														<div id="hidde" style="display:none;float: right; margin:0 3px 0 0;"><digi:trn key="message:hide">Hide more information</digi:trn> &lt;&lt; </div>
                                                     </span>
												   </div>	
                                                   </LI>
                                                </UL>
											</div>
									            <div id="currentDisplaySettings" style="clear:both;padding: 2px; display:none; background-color: rgb(255, 255, 204);">
			                                        <table  cellpadding="1" cellspacing="1" style="clear:both;">
														<tr>
															<td colspan="4" class="settings" nowrap>
																<strong><digi:trn key="message:totalNum">Total Number</digi:trn></strong> :		
															</td>
															<td colspan="4" class="settings" id="totalNumber">
																	${messageForm.allmsg}
															</td>
														</tr>
                                                        <tr>
															<td colspan="4" class="settings" nowrap>
																<strong><digi:trn key="message:numofhidden">Total Number Of Hidden </digi:trn></strong>:		
															</td>
															<td colspan="4"  class="settings" id="totalHidden">
																
																	${messageForm.hiddenMsgCount}
																
															</td>
														</tr>
														<tr>
															<td colspan="4" class="settings" nowrap>
																<strong><digi:trn key="message:adminSetings">Admin Settings</digi:trn></strong>:
														
															</td>
															<td colspan="4" class="settings" id="adimSettings">
																	<i><digi:trn key="message:refreshtime">Message Refresh Time(minutes)</digi:trn>:</i>  ${messageForm.msgRefreshTimeCurr} |
																	<i><digi:trn key="message:storepermess">Message Storage Per Message Type</digi:trn>:</i>  ${messageForm.msgStoragePerMsgTypeCurr} |
																	<i><digi:trn key="message:alertwarnings">Days of Advance Alert Warnings</digi:trn>:</i>  ${messageForm.daysForAdvanceAlertsWarningsCurr} |																	
																	<i><digi:trn key="message:emailalerts">Email Alerts</digi:trn>:</i>
																	
																	<c:if test="${empty messageForm.emailMsgsCurrent ||messageForm.emailMsgsCurrent==0}">
																		<digi:trn key="message:No">No</digi:trn>
																	</c:if>
																	<c:if test="${messageForm.emailMsgsCurrent==1}">
																		<digi:trn key="message:yes">Yes</digi:trn>
																	</c:if>
																	<br>
																	
															</td>
															
														</tr>
									
						
													</table>
											   </div>   
                                             </DIV>
					
                                        </div>
                                        	</TD>					
						</TR>
                                              <TR>
                                              
                        
							<TD bgColor="#ffffff" class="contentbox_border" align="left">
								<TABLE id="msgsList" border="1">
									<TR class="usersg" id="blankRow">
										<TD colspan="4">
										
										</TD>
									</TR>			
								</TABLE>
							</TD>
						</TR>
						<tr height="3px"><td colspan="7">&nbsp;</td></tr>
                                                <TR>
                                                      
                                               
							<TD bgColor="#ffffff"  align="left">
								<TABLE >
									<TR id="paginationPlace"><TD colspan="4"></TD></TR>			
								</TABLE>
							</TD>
                                                        </TR>
                                                          <TR>
                                                              <TD ALIGN="RIGHT">
								                            	<input type="button" onclick="createMessage()" value="<digi:trn>Create Message</digi:trn>" class="dr-menu" />
								                                <input type="button" onclick="selectAllCheckboxes()" value="<digi:trn>Select All</digi:trn>" class="dr-menu" />
								                                <input type="button" onclick="deselectAllCheckboxes()" value="<digi:trn>Deselect All</digi:trn>" class="dr-menu" />
                                                              	<input type="button" onclick="deleteMessage()" value="<digi:trn key='message:deleteSelMsgs'>Delete Selected Messages</digi:trn>" class="dr-menu" />
								                                <input type="button" onclick="markMessageAsRead()" value="<digi:trn>Mark As Read</digi:trn>" class="dr-menu" />
                                                              </TD>
                                                        </TR>
                                                        <TR >
                                                            <TD>&nbsp;</TD>
                                                        </TR>
                                                        <TR>
                                                        <TD>
                                                            <TABLE width="750px">
                                                            <TR>
                                                                <TD COLSPAN="2">
                                                                <strong><digi:trn key="message:IconReference">Icons Reference</digi:trn></strong>
                                                            </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/unread.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickExpandMessage"> Click on this icon to expand message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                             <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/read.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickCollapseMessage">Click on this icon to collapse message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
						                                     <TR>
							                                     <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/reply.gif" vspace="2" border="0" align="absmiddle" />
								                                     <digi:trn>Click on this icon to reply to a message</digi:trn>
							                                     </TD>
						                                     </TR>                                                            
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/finalForward.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickForwardMessage">Click on this icon to forward message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickEditMessage">Click on this icon to edit message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                             <TR>
                                                                <TD nowrap="nowrap" bgcolor="#E9E9E9"><img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
                                                                    <digi:trn key="message:ClickDeleteMessage">Click on this icon to delete message&nbsp;</digi:trn>
                                                                    <br />
                                                            </TD>
                                                            </TR>
                                                        </TABLE>
                                                        </TD>
                                                    </TR>
						
                                              
                                             </TABLE>				
                                                 
                                         </td>
                                     </tr>
                                 </table>
                             </td>
                         </tr>
                     </table>
                 </td>
             </tr>
         </table>

</digi:form>
