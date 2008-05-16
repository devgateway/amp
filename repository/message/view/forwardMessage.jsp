<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script langauage="JavaScript">

	function validate(){
		if(document.messageForm.messageName.value.length==0){
			alert('Please Enter Name');
			return false;
		}
		return true;
	}

	function save (event){
		if(!validate()){
			return false;
		}
		if(selectUsers(event)){
			messageForm.action="${contextPath}/message/messageActions.do?actionType=addMessage&toDo="+event;
  			messageForm.target = "_self";
  			messageForm.submit();		
		}	 		
	}
	
	function cancel() {
		messageForm.action="${contextPath}/message/messageActions.do?actionType=cancelMessage";
  		messageForm.target = "_self";
  		messageForm.submit();	
	}
	
	function selectUsers(event) {
    	var list = document.getElementById('selreceivers');
    	if (event=='send') {
    		if (list == null || list.length==0) {
        		alert('Please add receivers');
        		return false ;
    		}
    	}    	
    	if(list!=null){
    		for(var i = 0; i < list.length; i++) {
        		list.options[i].selected = true;
    		}
    	}
    	
    return true;
}
	
	function addUserOrTeam(){
		
		var reslist = document.getElementById('whoIsReceiver');
	    var selreceivers=document.getElementById('selreceivers');
	
	    if (reslist == null) {
	        return false;
	    }
	
	    var index = reslist.selectedIndex;
	    if (index != -1) {
	        for(var i = 0; i < reslist.length; i++) {
	            if (reslist.options[i].selected){
	              if(selreceivers.length!=0){
	                var flag=false;
	                for(var j=0; j<selreceivers.length;j++){
	                  if(selreceivers.options[j].value==reslist.options[i].value && selreceivers.options[j].text==reslist.options[i].text){
	                    flag=true;
	                  }
	                }
	                if(!flag){
	                  addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
	                }
	              }else{
	                addOnption(selreceivers,reslist.options[i].text,reslist.options[i].value);
	              }
	            }	
	        }
	    }
	    return false;
		
	}
	
	function addOnption(list, text, value){
    if (list == null) {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    list.options.add(option);
    return false;
}
	
	function removeUserOrTeam() {
		var tobeRemoved=document.getElementById('selreceivers');
		if(tobeRemoved==null){
			return;
		}		
		
		for(var i=tobeRemoved.length-1; i>=0; i--){
			if(tobeRemoved.options[i].selected){
				tobeRemoved.options[i]=null;
			}			
		}			
	}

</script>

<digi:form action="/messageActions.do">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td>
						<jsp:include page="addMessages.jsp"></jsp:include>
					<td/>
				</tr>
			</table>
</digi:form>