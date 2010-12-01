
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
	
		 var openedMessageId = -1;
		 var loadedMessageIds = new Array();
		 
     function loadSelectedMessage(id){
     		
     		if (openedMessageId == id) return;
     		//Close previously opened message
     		$("#msg_body_" + openedMessageId + " > .message").hide("slow");
     	
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
					$("#msg_body_" + id).append(div);
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
					var ajaxLoader = $("#msg_body_" + openedMessageId + " > #message_loader_indicator");
					var msgContainer = $("#msg_body_" + openedMessageId);
					ajaxLoader.remove();
					msgContainer.append(responseText);
					msgContainer.find(".message").hide();
					msgContainer.find(".message").show("slow");
				} else {
					$("#msg_body_" + openedMessageId + " > .message").show("slow")
				}
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
	
	function getMessages(){
		lastTimeStamp = new Date().getTime();
		var url=addActionToURL('messageActions.do?actionType=viewAllMessages&page='+currentPage+'&timeStamp='+lastTimeStamp);			
		var async=new Asynchronous();
		async.complete=buildMessagesList;
		async.call(url);
	}
	
	
	function buildMessagesList (status, statusText, responseText, responseXML){			
		
		console.log (responseText);
		
		var tbl = document.getElementById('msgsList');

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
                                
                  
					//var tblBody=tbl.getElementsByTagName('tbody')[0];
					//while (tblBody.childNodes.length>0){
					//	tblBody.removeChild(tblBody.childNodes[0]);
					//}
					
					
					
					if(myArray!=null && myArray.length>0){
						var whereToInsertRow=1;						
						for(var i=0;i<messages.length;i++){
							var msgId=messages[i].getAttribute('msgid');
							
							
                                                       
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
						var messageListMarkup = new Array();
						//create header
						messageListMarkup.push('<tr><td width=20 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center><input name="" type="checkbox" value="" /></td>');
						messageListMarkup.push('<td width=620 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside><b class="ins_title">Message Title</b></td>');
						messageListMarkup.push('<td width=100 background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center><b class="ins_title">Actions</b></td></tr>');
						
    
    
    
						
						for(var i=0;i<messages.length;i++){
							
							var msgId=messages[i].getAttribute('msgId');
							myArray[i]=msgId;							
							//creating tr
            	
            	messageListMarkup.push('<tr><td class=inside valign="top"><input name="" type="checkbox" value="" /></td>');
            	
            	
							messageListMarkup.push('<td id="msg_body_');
          		messageListMarkup.push(msgId);            	
          		messageListMarkup.push('"');

            	if (messages[i].getAttribute('read') == "false") {
	            	messageListMarkup.push(' class=inside valign="top"><img src="/TEMPLATE/ampTemplate/img_2/ico_unread.gif" align=left style="margin-right:5px;">');
	            	messageListMarkup.push('<a href=');
            		messageListMarkup.push('javascript:loadSelectedMessage(');
            		messageListMarkup.push(msgId);
            		messageListMarkup.push(')');
            		messageListMarkup.push(' class=l_sm>');
	            	messageListMarkup.push(messages[i].getAttribute('name'));
	            	messageListMarkup.push('</b>');
            	} else {
            		messageListMarkup.push('" class=inside><img src="/TEMPLATE/ampTemplate/img_2/ico_read.gif" align=left style="margin-right:5px;">');
            		messageListMarkup.push('<a href=');
            		messageListMarkup.push('javascript:loadSelectedMessage(');
            		messageListMarkup.push(msgId);
            		messageListMarkup.push(')');
            		messageListMarkup.push(' class=l_sm>');
            		 
	            	messageListMarkup.push(messages[i].getAttribute('name'));
            	}
            	messageListMarkup.push('</a>');
            	
            	
            	/*
            	messageListMarkup.push('<div class="message"><div class="message_cont">');
							messageListMarkup.push('<div style="float:right;">Priority: <b>');
							messageListMarkup.push('Medium');
							messageListMarkup.push('</b><br/>');
							messageListMarkup.push('Date: <b>');
							messageListMarkup.push('13/05/2010');
							messageListMarkup.push('</b></div>');
							messageListMarkup.push('From:'); 
							messageListMarkup.push('<b>ATL ATL | atl@amp.org');
							messageListMarkup.push('</b><br/>');
							messageListMarkup.push('To: <b>');
							messageListMarkup.push('Carl Sherson Clermont | csclermont@yahoo.com');
							messageListMarkup.push('</b> (<a href=#>view all</a>)<br /><a href=#>Click here to view Object</a></div>');
							messageListMarkup.push('<div class="message_body">');
							messageListMarkup.push('Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.');
							messageListMarkup.push('Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.</div>');
							messageListMarkup.push('</div>');
            	*/
            	
            	messageListMarkup.push('</td>');
            	
            	

							messageListMarkup.push('<td class=inside align=center valign="top"><img src="/TEMPLATE/ampTemplate/img_2/ico_reply.gif" width="16" height="14" style="margin-right:10px;"><img src="/TEMPLATE/ampTemplate/img_2/ico_forward.gif" width="16" height="14" style="margin-right:10px;"><img src="/TEMPLATE/ampTemplate/img_2/ico_trash.gif" width="14" height="14"></td>');
							messageListMarkup.push('</tr>');
							
							
							            	                           
							var isMsgRead=messages[i].getAttribute('read');	
							
						           																				
						}//end of for loop
						
						tbl.innerHTML = messageListMarkup.join("");
						
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
		while(paginationTR.firstChild != null){
			paginationTR.removeChild(paginationTR.firstChild);
		}
		
		var paginationParams=paginationTag.childNodes[0];
		if(paginationParams!=null){
			var paginationTD=document.createElement('TD');
			var paginationTDContent=pagesTrn+':';
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
						if(i<=allPages && i==page) {paginationTDContent+='<font color="red">'+i+'</font>|&nbsp;';}
						if(i<=allPages && i!=page) {paginationTDContent+='<a href="javascript:goToPage('+i+')" title="'+nextPage+'">'+i+'</a>|&nbsp;'; }
					}
				}
				if(page<allPages){
					var nextPg=page+1;									
					paginationTDContent+='<a href="javascript:goToPage('+nextPg+')" title="'+nextPage+'">&gt;</a>';
					paginationTDContent+='<a href="javascript:goToPage('+allPages+')" title="'+lastPg+'">&gt;&gt;</a>|';
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
               
		imgTD.innerHTML='<img id="'+msgId+'_plus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view//TEMPLATE/ampTemplate/img_2/unread.gif" title="<digi:trn key="message:ClickExpandMessage">Click on this icon to expand message&nbsp;</digi:trn>"/>'+
			'<img id="'+msgId+'_minus"  onclick="toggleGroup(\''+msgId+'\')" src="/repository/message/view//TEMPLATE/ampTemplate/img_2/read.gif" style="display : none" <digi:trn key="message:ClickCollapseMessage"> Click on this icon to collapse message&nbsp;</digi:trn>/>';
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
		var receivedTD2=document.createElement('TD');
		var received=message.getAttribute('received');
		receivedTD2.innerHTML=received;
		receivedTR.appendChild(receivedTD2);
		divTblBody.appendChild(receivedTR);						
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
      
        if(fwdOrEditDel){
        
        	if(isDraft!='true'){
        		replyTD=document.createElement('TD');
            	replyTD.width='10%';
            	replyTD.align='center';
            	replyTD.vAlign="top";
            	replyTD.innerHTML='<digi:link href="/messageActions.do?actionType=replyOrForwardMessage&reply=fillForm&editingMessage=true&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+replyClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view//TEMPLATE/ampTemplate/img_2/reply.gif" border=0 hspace="2" /></digi:link>';
            	msgTr.appendChild(replyTD);
        	}    
        	        
			// forward or edit link
			fwdOrEditTD=document.createElement('TD');
			fwdOrEditTD.width='10%';
			fwdOrEditTD.align='center';
			fwdOrEditTD.vAlign="top";
			
			if(isDraft=='true'){
	            fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=fillTypesAndLevels&editingMessage=true&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+editClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view//TEMPLATE/ampTemplate/img_2/edit.gif" border=0 hspace="2" /></digi:link>';									
			}else{
				fwdOrEditTD.innerHTML='<digi:link href="/messageActions.do?actionType=replyOrForwardMessage&fwd=fillForm&msgStateId='+sateId+'" style="cursor:pointer; text-decoration:underline; color: blue" title="'+forwardClick+'" onclick="return unCheckMessages()"><img  src="/repository/message/view//TEMPLATE/ampTemplate/img_2/finalForward.gif" border=0  hspace="2" /></digi:link>';
			}
			msgTr.appendChild(fwdOrEditTD);	
						
			//delete link
			var deleteTD=document.createElement('TD');
			deleteTD.width='10%';
			deleteTD.align='center';
	        deleteTD.vAlign="top";
			//deleteTD.innerHTML='<digi:link href="/messageActions.do?editingMessage=false&actionType=removeSelectedMessage&msgStateId='+msgId+'">'+deleteBtn+'</digi:link>';
			deleteTD.innerHTML='<a href="javascript:deleteMessage(\''+msgId+'\')" style="cursor:pointer; text-decoration:underline; color: blue" title="'+deleteClick+'" ><img  src="/repository/message/view//TEMPLATE/ampTemplate/img_2/trash_12.gif" border=0 hspace="2"/></a>';
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
    
function addActionToURL(actionName){
	var fullURL=document.URL;
    var lastSlash=fullURL.lastIndexOf("/");
    var partialURL=fullURL.substring(0,lastSlash);
    return partialURL+"/"+actionName;
}    
        
