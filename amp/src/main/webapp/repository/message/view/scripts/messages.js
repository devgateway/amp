

	 function checkForNewMessages(){
	 	lastTimeStamp = new Date().getTime(); 
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage+'&timeStamp='+lastTimeStamp);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
		id=window.setTimeout("checkForNewMessages()",60000*document.getElementsByName('msgRefreshTimeCurr')[0].value,"JavaScript");
	}
	
		 var openedMessageId = -1;
		 var innerMessageId = -1;
		 var loadedMessageIds = new Array();
		 var messageIdStateMap = new Array();
		 
		 function loadInnerMessage(id){
		 		var msgContainer = $("td#msg_body_" + openedMessageId).find("div#msg_body_" + id);
		 		var collapseContainer = msgContainer.children(".message");
		 		if (collapseContainer[0] == null) {
	     		innerMessageId = id;
	        var div=document.createElement('DIV');
	        div.id="message_loader_indicator";
	        div.className="message";
	        div.align="center";
					div.innerHTML='<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/>';
					msgContainer.append(div);
	        var url;
	        url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgId=' + id);
	        var async=new Asynchronous();
	        async.complete=viewInnerMsg;
	        async.call(url);
      	} else {
      		//collapseContainer.css("display", collapseContainer.css("display")=="block"?"none":"block");
      		if (collapseContainer.css("display")=="block") {
      			collapseContainer.hide("slow");
      		} else {
      			collapseContainer.show("slow");
      		}
      	}
    	}
	    function viewInnerMsg(status, statusText, responseText, responseXML) {
	    		if (status =! null && statusText != null && responseText != null) {
						var ajaxLoader = $("td#msg_body_" + openedMessageId).find("div#msg_body_" + innerMessageId + " > #message_loader_indicator");
						var msgContainer = $("td#msg_body_" + openedMessageId).find("div#msg_body_" + innerMessageId);
						ajaxLoader.remove();
						msgContainer.append(responseText);
						msgContainer.find(".message").hide();
						msgContainer.find(".message").show("slow");
						$(".view_all_recipients").unbind("click");
						$(".view_all_recipients").bind("click", toggleAllRecipientVisibility);
						
						//Mark loaded message as read					
						for (var msgStIdIdx = 0; msgStIdIdx < messageIdStateMap.length; msgStIdIdx ++) {
							if (parseInt(innerMessageId) == parseInt(messageIdStateMap[msgStIdIdx][1])) {
								markMsgeAsRead(messageIdStateMap[msgStIdIdx][0]);
								break;
							}
						}
						
					} else {
						$("td#msg_body_" + openedMessageId).find("div#msg_body_" + innerMessageId + " > .message").show("slow")
					}
			}
		 
     function loadSelectedMessage(id){
     		
     		if (openedMessageId == id) {
     			$("td#msg_body_" + openedMessageId + " > .message").hide("slow");
     			openedMessageId = null;
     			return;
     		}
     		
     		//Close previously opened message
     		if (openedMessageId != null) {
	     		$("td#msg_body_" + openedMessageId + " > .message").hide("slow");
  	   	}

     		openedMessageId = id;
     		
     		var alreadyLoaded = false;
     		var loadedMsgIdx;
     		for (loadedMsgIdx = 0; loadedMsgIdx < loadedMessageIds.length; loadedMsgIdx ++) {
     			if (loadedMessageIds[loadedMsgIdx] == id) {
     				alreadyLoaded = true;
     				break;
     			}
     		}
     		
     		
     		if (!alreadyLoaded) {
	     		loadedMessageIds.push(id);
	        var div=document.createElement('DIV');
	        div.id="message_loader_indicator";
	        div.className="message";
	        div.align="center";
					div.innerHTML='<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/>';
					$("td#msg_body_" + id).append(div);
	        var url;
	        url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgId=' + id);
	            /*
	            url=addActionToURL('messageActions.do?actionType=viewSelectedMessage&msgStateId='+id);
	            markMsgeAsRead(id);*/
	        var async=new Asynchronous();
	        async.complete=viewMsg;
	        async.call(url);
      	} else {
      		viewMsg (null, null, null, null);
      	}
    }
    	
		function openObjectURL(url){
            window.open(url,'','channelmode=no,directories=no,menubar=yes,resizable=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes');
            //openURLinWindow(url,600,550);
		}
	
    function viewMsg(status, statusText, responseText, responseXML) {
    		if (status =! null && statusText != null && responseText != null) {
					var ajaxLoader = $("td#msg_body_" + openedMessageId + " > #message_loader_indicator");
					var msgContainer = $("td#msg_body_" + openedMessageId);
					ajaxLoader.remove();
					msgContainer.append(responseText);
					msgContainer.find(".message").hide();
					msgContainer.find(".message").show("slow");
					$(".view_all_recipients").unbind("click");
					$(".view_all_recipients").bind("click", toggleAllRecipientVisibility);
					
					//Mark loaded message as read					
					for (var msgStIdIdx = 0; msgStIdIdx < messageIdStateMap.length; msgStIdIdx ++) {
						if (parseInt(openedMessageId) == parseInt(messageIdStateMap[msgStIdIdx][1])) {
							markMsgeAsRead(messageIdStateMap[msgStIdIdx][0]);
							break;
						}
					}
					
				} else {
					$("td#msg_body_" + openedMessageId + " > .message").show("slow")
				}
		}
		
		
        
    function unCheckMessages() {
         var chk=document.messageForm.getElementsByTagName('input');
         for(var i=0;i<chk.length;i++){
             if(chk[i].type == 'checkbox'&&chk[i].checked){
            	 alert(alertForEmptySelection.selectAlert);
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
	
	function deleteMessage(msgId) {
            var flag=false;
             if(msgId!=null&&deleteMsg()){
                flag=true;
            }
            if(msgId==null){
                msgId=getSelectedMessagesIds();
                if(msgId.length==0){
                	alert(alertForEmptySelection.selectAlert);
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
				
				//Get msg id from state id				
				var idStateIdx;
				var messageId;
				for (idStateIdx = 0; idStateIdx < messageIdStateMap.length; idStateIdx ++) {
					if (messageIdStateMap[idStateIdx][0] == idsArray[i]) {
						messageId = messageIdStateMap[idStateIdx][1];
						break;
					}
				}
				
				$("#msg_body_" + messageId).parent().remove();
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
				alert(alertForEmptySelection.selectAlert);
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
				
				var msgId=messages[i].getAttribute('msgId');
				var isRead=messages[i].getAttribute('read');
				if(isRead){
					var msgTd = $("#msg_body_" + msgId);
					if (msgTd.find("B.show_msg_link")[0] != null) {
						msgTd.find("IMG.show_msg_link").attr("src", "/TEMPLATE/ampTemplate/img_2/ico_read.gif");
						msgTd.find("A.show_msg_link").text(msgTd.find("B.show_msg_link").text());
						msgTd.find("B.show_msg_link").remove();
					}
				}
			}
			deselectAllCheckboxes();
		}
	}
	
	var tabIndex = 1;
	var childTab = "inbox";
	
	function getMsgParams() {
		var requestURL = "../../message/messageActions.do?actionType=getMsgParams&tabIndex=" + tabIndex + "&childTab=" + childTab + "&unique=" + new Date().getTime();
		$.get(requestURL, getMsgParamsComplited, "xml");
	}
	
	function getMsgParamsComplited(data, textStatus) {
		var msgCount = data.getElementsByTagName('message-info')[0].attributes.getNamedItem("total").value;
		var hiddenMsgCount = data.getElementsByTagName('message-info')[0].attributes.getNamedItem("totalHidden").value;
		$("#totalMsgCountContainer").html(msgCount);
		$("#totalHiddenMsgCountContainer").html(hiddenMsgCount);
	}
	
	function getMessages(){
		lastTimeStamp = new Date().getTime();
		
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+ currentPage + '&childTab=' + childTab + '&tabIndex=' + tabIndex +'&timeStamp='+lastTimeStamp);			
		$("#msgsList").html("<tr><td><div align='center'><img src='/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif'/></div></td></tr>");
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}
	
	
	function buildMessagesList (status, statusText, responseText, responseXML){			
		
		openedMessageId = -1;
		loadedMessageIds = new Array();
		messageIdStateMap = new Array();
		
		
		var tbl = $('#msgsList');
		
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
                       
					messages=root.childNodes;

					
						var messageListMarkup = new Array();
						//create header
						
						
    
    				if (messages.length != 0) {
		    
		    			messageListMarkup.push('<tbody><tr><td width=20 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center><input name="" id="select_all_msg_checkbox" type="checkbox" value="" /></td>');
		    			messageListMarkup.push('<td width=620 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title">'+msgHeaders.msgTitle+'</b></td>');
						messageListMarkup.push('<td width=100 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center><b class="ins_title">'+msgHeaders.msgActions+'</b></td></tr>');
					
						for(var i=0;i<messages.length;i++){
							var msgId=messages[i].getAttribute('msgId');
							myArray[i]=msgId;				
								
							messageIdStateMap.push (new Array (messages[i].getAttribute('id'), msgId));
											
	            	
	            	messageListMarkup.push('<tr><td class=inside valign="top"><input ');
	            	messageListMarkup.push('id="delChkbox_');
	            	messageListMarkup.push(messages[i].getAttribute('id'));
	            	messageListMarkup.push('" type="checkbox" value="');
	            	messageListMarkup.push(messages[i].getAttribute('id'));
	            	messageListMarkup.push('"/></td>');
					messageListMarkup.push('<td id="msg_body_');
	          		messageListMarkup.push(msgId);
	          		messageListMarkup.push('"');
	          		var name=messages[i].getAttribute('name').replace(/</g,'&lt;').replace(/>/g, "&gt;");
	            	if (messages[i].getAttribute('read') == "false") {
		            	messageListMarkup.push(' class=inside valign="top">');
		            	messageListMarkup.push('<a href=');
	            		messageListMarkup.push('javascript:loadSelectedMessage(');
	            		messageListMarkup.push(msgId);
	            		messageListMarkup.push(')>');
		            	messageListMarkup.push('<img class="show_msg_link" src="/TEMPLATE/ampTemplate/img_2/ico_unread.gif" border="0" align=left style="margin-right:5px;">');
	            		messageListMarkup.push('</a><a href=');
	            		messageListMarkup.push('javascript:loadSelectedMessage(');
	            		messageListMarkup.push(msgId);
	            		messageListMarkup.push(')');
	            		messageListMarkup.push(' class="l_sm show_msg_link">');
	            		messageListMarkup.push('<b class="show_msg_link">');
	            		
		            	messageListMarkup.push(name);
		            	messageListMarkup.push('</b>');
	            	} else {
	            		messageListMarkup.push('" class=inside>');
	            		messageListMarkup.push('<a href=');
	            		messageListMarkup.push('javascript:loadSelectedMessage(');
	            		messageListMarkup.push(msgId);
	            		messageListMarkup.push(')>');
	            		messageListMarkup.push('<img class="show_msg_link" src="/TEMPLATE/ampTemplate/img_2/ico_read.gif" border="0" align=left style="margin-right:5px;">');
	            		messageListMarkup.push('</a><a href=');
	            		messageListMarkup.push('javascript:loadSelectedMessage(');
	            		messageListMarkup.push(msgId);
	            		messageListMarkup.push(')');
	            		messageListMarkup.push(' class="l_sm show_msg_link">');
	            		 
		            	messageListMarkup.push(name);
	            	}
	            	messageListMarkup.push('</a>');
	
	            	
	            	messageListMarkup.push('</td>');
	            	
	            	
	
								messageListMarkup.push('<td class=inside align=center valign="top">');
								
								
								if (childTab != "draft") {
									messageListMarkup.push('<a href="/message/messageActions.do~actionType=replyOrForwardMessage~reply=fillForm~editingMessage=true~msgStateId=');
									messageListMarkup.push(messages[i].getAttribute('id'));
									messageListMarkup.push('">');
									messageListMarkup.push('<img src="/TEMPLATE/ampTemplate/img_2/ico_reply.gif" border="0" width="16" height="14" style="margin-right:10px;">');
									messageListMarkup.push('</a>');
									
									messageListMarkup.push('<a href="/message/messageActions.do~actionType=replyOrForwardMessage~fwd=fillForm~msgStateId=');
									messageListMarkup.push(messages[i].getAttribute('id'));
									messageListMarkup.push('">');
									messageListMarkup.push('<img src="/TEMPLATE/ampTemplate/img_2/ico_forward.gif" border="0" width="16" height="14" style="margin-right:10px;">');
									messageListMarkup.push('</a>');
								} else {
									messageListMarkup.push('<a href="/message/messageActions.do~actionType=fillTypesAndLevels~editingMessage=true~msgStateId=');
									messageListMarkup.push(messages[i].getAttribute('id'));
									messageListMarkup.push('">');
									messageListMarkup.push('<img src="/TEMPLATE/ampTemplate/img_2/ico_edit.gif" border="0" width="16" height="14" style="margin-right:10px;">');
									messageListMarkup.push('</a>');
								}
								
								messageListMarkup.push('<a href="javascript:deleteMessage(\'');
								messageListMarkup.push(messages[i].getAttribute('id'));
								messageListMarkup.push('\')">');
								messageListMarkup.push('<img src="/TEMPLATE/ampTemplate/img_2/ico_trash.gif" border="0" width="14" height="14"></td>');
								messageListMarkup.push('</a>');
								
								messageListMarkup.push('</tr>');
								
								
							           																				
							}//end of for loop
						} else {
							
							var noItemMsg = null;
							switch (tabIndex){
		          case 2:
		              noItemMsg = noAlerts;
		              break;
		          case 3:
		              noItemMsg = noApprovals;
		              break;
		          case 4:
		              noItemMsg = noEvents;
		              break;
		  
		          default : noItemMsg = noMsgs;
		          }
		          
							messageListMarkup.push('<tr><td class=inside colspan="3" align="center">');
							messageListMarkup.push(noItemMsg);
							messageListMarkup.push('</td></tr></tbody>');
						}
						
						tbl.html(messageListMarkup.join(""));
						
						//Set "select all" checkbox handler						
						$("#select_all_msg_checkbox").bind("change", function () {
							$("input[id^='delChkbox_']").attr("checked", this.checked);
						});
						
					}			
				//messages end
				
				//pagination start
				if(paginationTag!=null){
					var paginationParams=paginationTag.childNodes[0];
					var doMsgsExist=paginationParams.getAttribute('messagesExist');	
					if(doMsgsExist=='true'){
						var page=paginationParams.getAttribute('page');
						var allPages=paginationParams.getAttribute('allPages');
						var lastPage=paginationParams.getAttribute('lastPage');
						setupPagionation(paginationTag,parseInt(page),parseInt(allPages));
					}else{
						var paginationTR=document.getElementById('paginationPlace');
						while(paginationTR.firstChild != null){
							paginationTR.removeChild(paginationTR.firstChild);
						}
					}				
				}
				//pagination end
			}
		}
		
		
		getMsgParams();
	}
       	
	function setupPagionation (paginationTag,page,allPages){
		currentPage=page;
		var paginationTR=document.getElementById('paginationPlace');
		while(paginationTR.firstChild != null){
			paginationTR.removeChild(paginationTR.firstChild);
		}
		
		var paginationParams=paginationTag.childNodes[0];
		if(paginationParams!=null){
			var paginationTD=document.createElement('TD');
			paginationTD.className="paging"
			var paginationTDContent=pagesTrn+':';
				if(currentPage>1){
					var prPage=currentPage-1;
					paginationTDContent+=':<a class="l_sm" href="javascript:goToPage(1)" title="'+firstPage+'">&lt;&lt; </a> ';
					paginationTDContent+='&nbsp;|&nbsp;';
					paginationTDContent+='<a class="l_sm" href="javascript:goToPage('+prPage+')" title="'+prevPage+'" > Prev </a>';								
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
						if(i<=allPages && i==page) {paginationTDContent+='<b class="paging_sel">'+i+'</b>';}
						if(i<=allPages && i!=page) {paginationTDContent+='<a class="l_sm" href="javascript:goToPage('+i+')" title="'+nextPage+'">'+i+'</a>'; }
						paginationTDContent+='&nbsp;|&nbsp;';
					}
				}
				if(page<allPages){
					var nextPg=page+1;									
					paginationTDContent+='<a class="l_sm" href="javascript:goToPage('+nextPg+')" title="'+nextPage+'">Next</a>';
					paginationTDContent+='&nbsp;|&nbsp;';
					paginationTDContent+='<a class="l_sm" href="javascript:goToPage('+allPages+')" title="'+lastPg+'">&gt;&gt;</a>|';
				}	
				paginationTDContent+='&nbsp;'+page+ ofTrn +allPages;
			paginationTD.innerHTML=	paginationTDContent;						
			paginationTR.appendChild(paginationTD);						
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
	   	
	   	tabIndex = $("input[name=tabIndex]").val();
			childTab = $("input[name=childTab]").val();
			
			
			
			getMessages();
	   	
});
    
function addActionToURL(actionName){
	var fullURL=document.URL;
    var lastSlash=fullURL.lastIndexOf("/");
    var partialURL=fullURL.substring(0,lastSlash);
    return partialURL+"/"+actionName;
}    
        
function toggleAllRecipientVisibility() {
	
	var toggleLink = $(this);
	var recipientContainerDiv = toggleLink.parent().children(".msg_all");
	if (recipientContainerDiv.css("display") == "none") {
		recipientContainerDiv.show("slow");
		toggleLink.text(viewOrHideAllLabel.hAll);
	} else {
		recipientContainerDiv.hide("slow");
		toggleLink.text(viewOrHideAllLabel.vAll);
	}
	
	return false;
}