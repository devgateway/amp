<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ page import="org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm" %>

<!-- Dependencies -->        
<script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>        
<script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
<!-- Source File -->
<script type="text/javascript" src="<digi:file src="script/yui/menu-min.js"/>"></script>
<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script>       
<script type="text/javascript" src="<digi:file src="script/yui/element-beta-min.js"/>"></script> 
<script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script>        
        
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>

<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp" flush="true" />

<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOO.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
	var panelStart=0;
	var checkAndClose=false;
	function initOrganizationsScript() {
		var msg='\n<digi:trn key="aim:selectOrg">Select Organization</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
	}
	//this is called from editActivityMenu.jsp
	addLoadEvent(initOrganizationsScript);
-->	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#popin .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	.bd a:hover {
  		background-color:#ecf3fd;
		font-size: 10px; 
		color: #0e69b3; 
		text-decoration: none	  
	}
	.bd a {
	  	color:black;
	  	font-size:10px;
	}
		
</style>
<script language="JavaScript">
    <!--
   
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
    }
    
    
    var responseSuccess = function(o){
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText; 
		var content = document.getElementById("popinContent");
	    //response = response.split("<!")[0];
		content.innerHTML = response;
	    //content.style.visibility = "visible";
		
		showContent();
	}
 
	var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		//alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};

	function showContent(){
		var element = document.getElementById("popin");
		element.style.display = "inline";
		if (panelStart < 1){
			myPanel.setBody(element);
		}
		if (panelStart < 2){
			document.getElementById("popin").scrollTop=0;
			myPanel.show();
			panelStart = 2;
		}
		checkErrorAndClose();
	}
	function checkErrorAndClose(){
		if(checkAndClose==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose();
				refreshPage();
			}
			checkAndClose=false;			
		}
	}
	function refreshPage(){
		submitForm();
	}

	function myclose(){
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		showContent();
	}

	-->

</script>


<digi:instance property="calendarEventForm"/>
<style  type="text/css">
<!--

.contentbox_border {
    border: 1px solid #cccccc;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}
.myStyleClass{	
	min-width: 110px;
}
-->
</style>

<script language="JavaScript" type="text/javascript">
  <jsp:include page="../../aim/view/scripts/calendar.js.jsp" flush="true" />
  

function removeSelOrgs() {
	setMethod("removeOrg");
	selectAtts();
	var eventForm = document.getElementById("showAmpEventFormID");
	eventForm.target = "_self";
	eventForm.submit();	
//	document.calendarEventForm.submit();
}
function submitForm() {
	setMethod("");
	selectAtts();
	var eventForm = document.getElementById("showAmpEventFormID");
	eventForm.target = "_self";
	eventForm.submit();	
//	document.calendarEventForm.submit();
}



</script>

<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />
<jsp:include page="/repository/calendar/view/scripts/calendarEventScript.jsp"/>
<link rel="stylesheet" href="<digi:file src="module/calendar/css/main.css"/>">

<script language="JavaScript" type="text/javascript">

	var calendarHelp="<digi:trn>Calendar</digi:trn>"
	var separateEmails="<digi:trn>Please separate email addresses by semicolons</digi:trn>"
	var validEmailmsg="<digi:trn>Invalid e-mail address:</digi:trn>"
	var alreadyAdded="<digi:trn >E-mail address already added: </digi:trn>"
    var guestText='---<digi:trn jsFriendly="true">Guest</digi:trn>---';
		
function makePublic(){

  var showPrivateEvents = document.getElementsByName('privateEventCheckbox')[0];
  if (showPrivateEvents.checked==true) {
    document.getElementsByName('privateEvent')[0].value=false;
  }
  if (showPrivateEvents.checked==false){
    document.getElementsByName('privateEvent')[0].value=true;

  }
}

function addOrganisation(orgId, orgName){
  var list = document.getElementById('organizationList');
  if (list == null || orgId == "" || orgName == "") {
    return;
    }
    var option = document.createElement("OPTION");
    option.value = orgId;
    option.text = orgName;
    list.options.add(option);
  }


  function MyremoveUserOrTeam(){
  	var MyGuests=new Array();
  	var orphands=new Array();
    var list = document.getElementById('selreceivers');

	var index = 0;
	var orpIndex = 0;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
         orphands[orpIndex]=list.options[i];
         orpIndex++;
      }
      if(list.options[i].value.indexOf('g:')==0){
         if(list.options[i].selected){
            list.options[i]=null;
         }
         else{
      	   MyGuests[index]=list.options[i];
      	   index++;
         }
      }
      else{
         if(list.options[i].value.indexOf('guest')==0&&list.options[i].selected){
              $("#selreceivers > option[value^='g']").remove();
         }
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }
    removeUserOrTeam();
    if(index != 0){
       addOption(list,guestText,"guest");
	   for(var j=0; j<index; j++){
	      list.options.add(MyGuests[j]);
	   }
    }
  }
  function MyaddUserOrTeam(){
  	var MyGuests=new Array();
    var list = document.getElementById('selreceivers');
	var orphands=new Array();
	var index = 0;
	var orpIndex = 0;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value.indexOf('m')==0 && list.options[i].id.indexOf('t')!=0){
         orphands[orpIndex]=list.options[i];
         orpIndex++;
      }

      if(list.options[i].value.indexOf('g:')==0){
      	MyGuests[index]=list.options[i];
      	index++;
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }

	//add teams and members
  	addUserOrTeam();//fills out the list with teams and members

  	//add the guests to the list
  	if(index != 0){
       addOption(list,guestText,"guest");
	   for(var j=0; j<index; j++){
	      list.options.add(MyGuests[j]);
	   }
    }
  }

  function addAtt(){
    var reclist = document.getElementById('whoIsReceiver');
    var selrec=document.getElementById('selreceivers');

    if (reclist == null) {
      return false;
    }

    var index = reclist.selectedIndex;
    if (index != -1) {
      for(var i = 0; i < reclist.length; i++) {
        if (reclist.options[i].selected){
          if(selrec.length!=0){
            var flag=false;
            for(var j=0; j<selrec.length;j++){
              if(selrec.options[j].value==reclist.options[i].value &&
              selrec.options[j].text==reclist.options[i].text){
                flag=true;
              }
            }
            if(!flag){
              addOption(selrec,reclist.options[i].text,reclist.options[i].value);
            }
          }else{
            addOption(selrec,reclist.options[i].text,reclist.options[i].value);
          }
        }

      }
    }
    return false;
  }

  function addOption(list, text, value){
    if (list == null) {
      return;
    }
    var option = document.createElement("OPTION");
    option.value = value;
    option.text = text;
    list.options.add(option);
    return false;
  }

    function addGuest(guest) {
	    var list = document.getElementById('selreceivers');
	    if (list == null || guest == null || guest.value == null || guest.value == "") {
            return;
	    }

        var guestVal=guest.value;
		
        while(guestVal.indexOf(";")!=-1){
            var optionValue=guestVal.substring(0,guestVal.indexOf(";"));
            if (!checkEmail(optionValue)){
                alert(validEmailmsg+' '+optionValue);
                return false;
            }
            if (isGuestAllreadyAdded(optionValue)){
                alert(alreadyAdded+' '+optionValue);
                return false;
            }
            guestVal=guestVal.substring(guestVal.indexOf(";")+1);
	    }
        var guestAmount=$("#selreceivers > option[value^='g:']").length;
		var guestVal=guest.value;
		while(guestVal.indexOf(";")!=-1){
			var optionValue=guestVal.substring(0,guestVal.indexOf(";"));
             if(guestAmount==0){
                addOption(list,guestText,"guest");
            }
			addOption(list,optionValue,'g:'+optionValue);				
		    guestVal=guestVal.substring(guestVal.indexOf(";")+1);		
		}
		if(guestVal.length>0){
            if (!checkEmail(guestVal)){
                alert(validEmailmsg+' '+guestVal);
                return false;
            }

            if (isGuestAllreadyAdded(guestVal)){
                alert(alreadyAdded+' '+guestVal);
                return false;
            }
            if(guestAmount==0){
                addOption(list,guestText,"guest");
            }
			addOption(list,guestVal,'g:'+guestVal);
		}	
	    guest.value = "";
	} 

    function isGuestAllreadyAdded(guest){
	  var selreceivers=document.getElementById('selreceivers');
	  for(var j=0; j<selreceivers.length;j++){
	    if(selreceivers.options[j].value=='g:'+guest){
	      return true;
	    }
	  }
	  return false
	}
  
    function checkEmail(email){	
        var pattern=/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
        var expression = new RegExp(pattern)
        if(expression.test(email)){         
    		return true;   
        }else{   
        	return false; 
        }
    }
   

  function removeAtt() {
    var list = document.getElementById('selreceivers');
    if (list == null) {
      return;
    }
    var index = list.selectedIndex;
    if (index != -1) {
      for(var i = list.length - 1; i >= 0; i--) {
        if (list.options[i].selected) {
          list.options[i] = null;
        }
      }
      if (list.length > 0) {
        list.selectedIndex = index == 0 ? 0 : index - 1;
      }
    }
  }

  function selectAtts() {
    var list = document.getElementById("selreceivers");
    if (list != null) {
      for(var i = 0; i < list.length; i++){
        list.options[i].selected = true;
      }
    }
  }

  function edit(key) {
    document.calendarEditActivityForm.step.value = "1.1";
    document.calendarEditActivityForm.editKey.value = key;
    document.calendarEditActivityForm.target = "_self";
    document.calendarEditActivityForm.submit();
  }

  function removeSelectedElements(selId){
    var sel = document.getElementById(selId);
    if (sel != null) {
      if (sel.selectedIndex > -1) {
        for(var i=0; i<sel.options.length; i++) {
          if (sel.options[i].selected) {
            sel.remove(i);
          }
        }
      }
    }
  }

  function removeSelOrganisations() {
    var orglist = document.getElementById('organizationList');
    if (orglist != null) {
      if (orglist.selectedIndex > -1) {
        for(var i=0; i<orglist.options.length; i++) {
          if (orglist.options[i].selected) {
            orglist.remove(i);
          }
        }
      }
    }
  }

  function selectAllOrgs() {
    var orglist = document.getElementById('organizationList');
    for(var i=0; i<orglist.options.length; i++){
      orglist.options[i].selected=true;
    }
    //document.getElementById('organizationList').option.selectAll;
    return true;
  }

  function deleteEvent(){
    if(confirm('<digi:trn key="calendar:deleteEventComfirmMsg">Are You sure?</digi:trn>')){
      setMethod("delete");
      return true;
    }
    return false;
  }


  function previewEvent(){
      setMethod("preview");
      selectAtts();
      selectAllOrgs();
      return true;
  }

  function reccuringEvent(){
	   	<digi:context name="rev" property="context/module/moduleinstance/recurringEvent.do" />
		openURLinWindow("<%=rev%>",832,624);
  }

  function sendEvent(){
		 var list = document.getElementById('selreceivers');  
		 if(list!=null){
		  	for(var i = 0; i < list.length; i++) {
		  		list.options[i].selected = true;
		  	}
		}
			  
		document.getElementById('hdnMethod').value = 'save';
		<digi:context name="sendEvent" property="context/module/moduleinstance/showCalendarEvent.do?method=save"/>
//		document.calendarEventForm.action = "<%=sendEvent %>";
//		document.calendarEventForm.target = "_self";
//		document.calendarEventForm.submit();
		
		var eventForm = document.getElementById("showAmpEventFormID");
		eventForm.action = "<%=sendEvent %>"; 
		eventForm.target = "_self";
		eventForm.submit();	
		
  }	  
	
  function setMethod(mth){
    var h=document.getElementById("hdnMethod");
    if(h!=null){
      h.value=mth;
    }
  }

function recurEvent(){
 	<digi:context name="rev" property="context/module/moduleinstance/recurringEvent.do" />
	openURLinWindow("<%=rev%>",832,624);
}

function is_mail(m) {
	var p = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	//alert(p.test(m));
	return p.test(m);		  
}

function submitForm(thisform){
	var typeid = document.getElementById('selectedCalendarTypeId').value;
	document.getElementById('CalendatTypeid').value = typeid;
	setMethod("");
	selectAtts();
	
	var eventForm = document.getElementById("showAmpEventFormID");
	eventForm.target = "_self";
	eventForm.submit();	
//	document.calendarEventForm.submit();
}


</script>


<digi:form action="/showCalendarEvent.do" styleId="showAmpEventFormID">
    <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
    <html:hidden name="calendarEventForm" property="selectedStartMonth" styleId="hiddenMonth"/>
    <html:hidden name="calendarEventForm" property="selectedStartYear" styleId="hiddenYearMonth"/>
    <html:hidden name="calendarEventForm" property="recurrPeriod" styleId="hidden"/>
	<html:hidden name="calendarEventForm" property="typeofOccurrence" styleId="type"/>
    <html:hidden name="calendarEventForm" property="recurrStartDate" styleId="recurrStrDate"/>
    <html:hidden name="calendarEventForm" property="recurrEndDate" styleId="recurrEndDate"/>
    <html:hidden name="calendarEventForm" property="weekDays" styleId="weekDays"/>
    <html:hidden name="calendarEventForm" property="recurrSelectedEndTime" styleId="recurrSelectedEndTime"/>
    <html:hidden name="calendarEventForm" property="recurrSelectedStartTime" styleId="recurrSelectedStartTime"/>
	
    <table width="1000" align="center">
    	 <tr>
			<td align=left valign="top" width=750>
				<table cellPadding=5 cellspacing="0" width="100%">
					<tr>
			<td height=33>
				<span class=crumb> &nbsp;
					<c:set var="translation">
						<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
					</c:set>
					<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">Portfolio</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:link href="/../calendar/showCalendarView.do" styleClass="comment" title="${translation}">
						<digi:trn key="calendar:Calendar">Calendar</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:trn key="aim:createNewEvent">Create New Event</digi:trn>
				</span>
			</td>
		</tr>	
		<tr>
			<td height="16" vAlign="middle" width="571">
				<span class=subtitle-blue>&nbsp;<digi:trn key="calendar:CreateAnEvent">Create An Event</digi:trn> </span>
			</td>
		</tr>
		<tr>			
			<td noWrap vAlign="top"> 
            	<table class="contentbox_border" width="100%" cellpadding="0" cellspacing="0">
                	<tr>	
                		<td align="center" style="padding: 0px 3px 0px 3px;">
			           		
			           </td>	
                    </tr>
             		<tr>
			            <td style="font-family: Tahoma;font-size: 12px;">                
			                <div style="background-color: #ffffff; padding: 20px; background-color:#F8F8F8;">
			                	<span style="font-family: Tahoma;font-size: 11px;"><digi:errors/></span>			                  
			                  <html:hidden name="calendarEventForm" property="calendarTypeId" styleId="CalendatTypeid"/>
			                  <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                    <table border="2" align="center" cellpadding="3" cellspacing="3" class="t_mid">
			                    	<tr>
			                    		<td nowrap="nowrap" style="vertical-align: text-top" width=50%>
			                    			<font color="red" size="3px">*</font>
			                    			<digi:trn key="calendar:evntTitle"><b>Event title</b></digi:trn><br /><html:text name="calendarEventForm" styleId="eventTitle" property="eventTitle" style="width: 220px" styleClass="inp-text"/><br /><br /><digi:trn key="calendar:cType"><b>Calendar type</b></digi:trn><br /><html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                                 <html:select name="calendarEventForm" property="selectedCalendarTypeId" styleId="selectedCalendarTypeId" style="width: 220px;" onchange="submitForm(this.form)" styleClass="inp-text">
			                                     <c:if test="${!empty calendarEventForm.calendarTypes}">
			                                     	<c:forEach var="type" items="${calendarEventForm.calendarTypes}">
				                                        	<html:option value="${type.value}">${type.label}</html:option>
		                                           </c:forEach>
			                                     </c:if>
			                                 </html:select><br /><br /><digi:trn key="calendar:eventsType"><b>Event type</b></digi:trn><br />
											 <html:select name="calendarEventForm" style="width: 220px;" property="selectedEventTypeId" styleClass="inp-text">
				                                    <c:if test="${!empty calendarEventForm.eventTypesList}">
				                                    	<c:forEach var="evType" items="${calendarEventForm.eventTypesList}">
				                                        	<html:option value="${evType.id}" style="color:${evType.color};font-weight:Bold;"><digi:trn>${evType.name}</digi:trn></html:option>
				                                        </c:forEach>
				                                 	</c:if>
				                                 </html:select>
											
											</td>
			                    		<td width="2">&nbsp;</td>
			                    		<td width="20" align="left">&nbsp;</td>
			                    		<td>&nbsp;</td>
			                    		<feature:display name="Donors" module="Calendar">			                    			
			                    			<td colspan="4" valign="top" width=50%>
			                    			<digi:trn key="cal:organizations"><b>Organizations</b></digi:trn>	
											<br />
											<div style="margin-top:7px;"><html:select multiple="multiple" property="selOrganizations" size="4" style="width: 220px;">
				                                   	<logic:notEmpty name="calendarEventForm" property="organizations">
														<logic:iterate name="calendarEventForm" property="organizations" id="organization" type="org.digijava.module.aim.dbentity.AmpOrganisation">
															<html:option value="${organization.ampOrgId}" style="font-family: Tahoma;font-size:11px;">${organization.name}</html:option>
														</logic:iterate>
													</logic:notEmpty>
				                                 </html:select><div style="float:right;"><table cellSpacing="1" cellPadding="1">
				                    				<field:display name="Add Donor Button" feature="Donors">
				                    					<tr>
															<td>
																<aim:addOrganizationButton refreshParentDocument="false" collection="organizations" form="${calendarEventForm}"  callBackFunction="submitForm();" styleClass="buttonx"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>															</td>
														</tr>
				                    				</field:display>
												
													<field:display name="Remove Donor Button" feature="Donors">
														<tr>
															<td>
																<html:button  property="submitButton" onclick="return removeSelOrgs()" styleClass="buttonx" style="width:110px">
																	<digi:trn key="btn:remove">Remove</digi:trn>
																</html:button>															</td> 
														</tr>
													</field:display>													
												</table>	</div></div>														                    						                    		</td>
			                    		</feature:display>			                    		
			                    	</tr>
									<tr>
									<td colspan="6">&nbsp;</td>
									</tr>
			                    	<tr>
			                    		<td nowrap="nowrap">&nbsp;</td>
			                    		<td width="2">&nbsp;</td>
			                    		<td align="left">&nbsp;</td>	
										<td>&nbsp;</td>	
										<td>&nbsp;</td>
										<td>&nbsp;</td> 
									</tr>
									<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									</tr>
			                    	
			                    	<tr style="height:25px">
			                    		<feature:display name="Event Type" module="Calendar">
			                    			<td valign="top" nowrap="nowrap" rowspan="1">&nbsp;</td>
				                    		<td width="2">&nbsp;</td>
				                    		<td align="left" valign="top">&nbsp;</td>
			                    		</feature:display>
			                    		<td>&nbsp;</td>			                    		
			                    		<td  nowrap="nowrap">
			                    			<digi:trn key="calendar:Description"><b>Description</b></digi:trn><br />
											<div style="margin-top:7px;"><html:textarea name="calendarEventForm" styleId="descMax" property="description" style="width: 100%" rows="4"/></div></td>
			                    		<td width="2">&nbsp;</td>
		                    		</tr>	
									<tr>
									<td> &nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									</tr>		                    	
			                    	<tr style="height: 25px;">
			                    		<td nowrap="nowrap" style="vertical-align: top;" >
			                    			<digi:trn key="calendar:StDate"><b>Start date</b></digi:trn>			                    		</td>
			                    		<td width="2" valign="top">&nbsp;</td>
			                    		<td align="left" style="width: 220px;vertical-align: top;">&nbsp;			                    		</td>			                    		
			                    		<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>	
									<tr>
									<td><c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
			                                          	<html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
			                                            <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td nowrap="nowrap">
			                                                  <html:text styleId="selectedStartDate" readonly="true" name="calendarEventForm" property="selectedStartDate" style="width:80px" styleClass="inp-text"/>			                                                </td>
			                                                <td>&nbsp;			                                                </td>
			                                                <!-- <td>
			                                                  <a id="clear1" href="javascript:clearDate(document.getElementById('selectedStartDate'), 'clear1')">
			                                                    <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this transaction"/>
			                                                  </a>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td> -->
			                                                <td>
			                                                  <a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("selectedStartDate"),"clear1")'>
			                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">			                                                  </a>			                                                </td>
			                                                <td>&nbsp;&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option value="${hour}" class="inp-text">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>			                                                 
			                                                  	<script type="text/javascript">
			                                                  	if(document.getElementById('selectedStartTime') != null)
				                                                  selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
				                                                </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option value="${minute}" class="inp-text">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartTime')!= null)
			                                                  selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
			                                                  </script>			                                                </td>
			                                              </tr>
			                                            </table>
                                          </c:if>
			                                          <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
			                                            <html:hidden styleId="selectedStartDate" name="calendarEventForm" property="selectedStartDate"/>
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td>
			                                                 <select id="selectedStartYear" onchange="updateDate(document.getElementById('selectedStartDate'), 'year', this.value)"></select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartDate') != null)
			                                                  createYearCombo(document.getElementById('selectedStartYear'), document.getElementById('selectedStartDate').value);
			                                                  </script>			                                                </td>
			                                                <td>
			                                                  <select id="selectedStartMonth" onchange="updateDate(document.getElementById('selectedStartDate'), 'month', this.value)">
			                                                    <c:forEach var="i" begin="1" end="13">
			                                                      <bean:define id="index" value="${i - 1}"/>
			                                                      <bean:define id="type" value="${calendarEventForm.selectedCalendarTypeId}"/>
			                                                      <c:if test="${i < 10}">
			                                                        <c:set var="i" value="0${i}"/>
			                                                      </c:if>
			                                                      <option class="inp-text" value="${i}"/>
			                                                      <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
			                                                      </option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartDate') != null){
			                                                  selectOptionByValue(document.getElementById('selectedStartMonth'), get('month', document.getElementById('selectedStartDate').value));
			                                                  }
			                                                  </script>			                                                </td>
			                                                <td>
			                                                   <select id="selectedStartDay" onchange="updateDate(document.getElementById('selectedStartDate'), 'day', this.value)">
			                                                    <c:forEach var="i" begin="1" end="30">
			                                                      <c:if test="${i < 10}">
			                                                        <c:set var="i" value="0${i}"/>
			                                                      </c:if>
			                                                      <option class="inp-text" value="${i}"/>${i}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartDate') != null)
			                                                  selectOptionByValue(document.getElementById('selectedStartDay'), get('day', document.getElementById('selectedStartDate').value));
			                                                  </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option class="inp-text" value="${hour}">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartTime') != null)
			                                                  selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
			                                                  </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedStartTime') != null)
			                                                  selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
			                                                  </script>			                                                </td>
			                                              </tr>
			                                            </table>
			                                          </c:if></td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									</tr>		                    	
			                    	<tr height="25px;">
			                    		<td  nowrap="nowrap">
			                    			<digi:trn key="calendar:EndDate"><b>End Date</b></digi:trn>			                    		</td>
			                    		<td width="2" valign="top">&nbsp;</td>
			                    		<td>&nbsp;			                    		</td>			                    		
			                    		<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
			                    	<tr>
									<td><c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td nowrap="nowrap">
			                                                  <html:text styleId="selectedEndDate" readonly="true" name="calendarEventForm" property="selectedEndDate" style="width:80px" styleClass="inp-text"/>			                                                </td>
			                                                <td>&nbsp;			                                                </td>
			                                                <!-- <td>
			                                                  <a id="clear2" href="javascript:clearDate(document.getElementById('selectedEndDate'),'clear2')">
			                                                    <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this transaction"/>
			                                                  </a>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td>-->
			                                                <td>
			                                                  <a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("selectedEndDate"),"clear2")'>
			                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">			                                                  </a>			                                                </td>
			                                                <td>
&nbsp;&nbsp;			                                                </td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}">
			                                                        <c:set var="hour" value="0${hour}"/>
			                                                      </c:if>
			                                                      <option class="inp-text" value="${hour}">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndTime') != null){
			                                                  	selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
			                                                  }
			                                                  </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndTime') != null){
			                                                  	selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
			                                                  }
			                                                  </script>			                                                </td>
			                                              </tr>
			                                            </table>
                                          </c:if>
			                                          <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
			                                            <html:hidden styleId="selectedEndDate" name="calendarEventForm" property="selectedEndDate"/>
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td>
			                                                  <select id="selectedEndYear" onchange="updateDate(document.getElementById('selectedEndDate'), 'year', this.value)"></select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndDate') != null)
			                                                  createYearCombo(document.getElementById('selectedEndYear'), document.getElementById('selectedEndDate').value);
			                                                  </script>			                                                </td>
			                                                <td>
			                                                  <select id="selectedEndMonth" onchange="updateDate(document.getElementById('selectedEndDate'), 'month', this.value)">
			                                                    <c:forEach var="i" begin="1" end="13">
			                                                      <bean:define id="index" value="${i - 1}"/>
			                                                      <bean:define id="type" value="${calendarEventForm.selectedCalendarTypeId}"/>
			                                                      <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
			                                                      <option class="inp-text" value="${i}"/>
			                                                      <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
			                                                      </option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndDate') != null)
			                                                  selectOptionByValue(document.getElementById('selectedEndMonth'), get('month', document.getElementById('selectedEndDate').value));
			                                                  </script>			                                                </td>
			                                                <td>
			                                                  <select id="selectedEndDay" onchange="updateDate(document.getElementById('selectedEndDate'), 'day', this.value)">
			                                                    <c:forEach var="i" begin="1" end="30">
			                                                      <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
			                                                      <option class="inp-text" value="${i}"/>${i}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndDate') != null)
			                                                  selectOptionByValue(document.getElementById('selectedEndDay'), get('day', document.getElementById('selectedEndDate').value));
			                                                  </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option class="inp-text" value="${hour}">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndTime') != null)
			                                                  selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
			                                                  </script>			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  if(document.getElementById('selectedEndTime') != null)
			                                                  selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
			                                                  </script>			                                                </td>
			                                              </tr>
			                                            </table>
			                                          </c:if></td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									</tr>
			                    	<tr height="25px;">
			                    		<td><html:hidden name="calendarEventForm" property="privateEvent"/>
			                                          <input type="checkbox" name="privateEventCheckbox" onchange="javascript:makePublic();" /> 
				                                          <c:if test="${!calendarEventForm.privateEvent }">
				                                          	CHECKED				                                          </c:if>                                          
			                                          />
			                                          <digi:trn key="calendar:PublicEvent">Public Event</digi:trn></td>
										<td>&nbsp;</td>
			                    		<td>&nbsp;</td>			                    		
			                    		<td>&nbsp;</td>	
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
			                    	<tr height="25px;">
			                    		<td colspan="8">
										
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="103" valign=top><font color="red" size="3px">*</font>
    <digi:trn key="calendar:Attendee"><b style="font-size:12px;">Attendee</b></digi:trn></td>
    <td style="font-size:11px;">
	<div class="msg_receivers">
	<div class="rec_group_container">
	
	<div class="msg_grp_name" style="font-size:11px">
	<input type="checkbox" style="float: left;" value="t:10" class="group_checkbox">
	<div class="msg_lbl">---Team Name---</div>
	</div>
	
	<div class="msg_grp_mem_name" style="font-size:11px;">
	<input type="checkbox" value="m:82" checkbox="" id="t:10 type=" name="receiversIds">UATtl UATtl@amp.org<br>
	</div>
	<div class="msg_grp_mem_name" style="font-size:11px;">
	<input type="checkbox" value="m:82" checkbox="" id="t:10 type=" name="receiversIds">UATtl UATtl@amp.org<br>
	</div><div class="msg_grp_mem_name" style="font-size:11px;">
	<input type="checkbox" value="m:82" checkbox="" id="t:10 type=" name="receiversIds">UATtl UATtl@amp.org<br>
	</div><div class="msg_grp_mem_name" style="font-size:11px;">
	<input type="checkbox" value="m:82" checkbox="" id="t:10 type=" name="receiversIds">UATtl UATtl@amp.org<br>
	</div><div class="msg_grp_mem_name" style="font-size:11px;">
	<input type="checkbox" value="m:82" checkbox="" id="t:10 type=" name="receiversIds">UATtl UATtl@amp.org<br>
	</div>
	</div>
	</div>
	<br />
				<input type="checkbox" name="sendToAll" value="checkbox"/><digi:trn>Send to All</digi:trn><br/><br/>
	<b>Additional Receivers: </b>Type first letter of contact to view suggestions or enter e-mail to send message to<br />
			<div class="msg_add">
				
				<input type="text" id="contactInput" class="inputx" style="width:470px; Font-size: 10pt; height:22px;">
				<input type="button" value="Add" class="buttonx_sm" onClick="addContact(document.getElementById('contactInput'))">	</td>
  </tr>
</table></td>
			                    	</tr>
			                    	<!--<tr>
			                    		<td colspan="2">&nbsp;</td>
			                    		<td>
			                    			<font color="red" size="3px">*</font>
			                    			<digi:trn key="calendar:Attendee">Attendee</digi:trn>			                                            
			                    		</td>
			                    		<td colspan="3">&nbsp; </td>
			                    	</tr>			                    	
			                    	<tr>
			                    		<td colspan="2">&nbsp;</td>
			                    		 <td style="width: 220px">
			                                          <select multiple="multiple" size="13" id="whoIsReceiver" class="inp-text" style="width: 220px; height: 150px;">
			                                            <c:if test="${empty calendarEventForm.teamMapValues}">
			                                              <option value="-1">No receivers</option>
			                                            </c:if>
			                                            <c:if test="${!empty calendarEventForm.teamMapValues}">
			                                              <option value="all" ><digi:trn key="message:AllTeams">All</digi:trn></option>
			                                              <c:forEach var="team" items="${calendarEventForm.teamMapValues}">
			                                                <c:if test="${!empty team.members}">
			                                                  <option value="t:${team.id}" style="font-weight: bold;background:#CCDBFF;font-size:11px;">---${team.name}---</option>
			                                                  <c:forEach var="tm" items="${team.members}">
			                                                    <option value="m:${tm.memberId}" id="t:${team.id}" styleId="t:${team.id}" style="font:italic;font-size:11px;">${tm.memberName}</option>
			                                                  </c:forEach>
			                                                </c:if>
			                                              </c:forEach>
			                                            </c:if>
			                                          </select>
			                                        </td>
			                                        <td colspan="3" style="text-align: center;">
					                                	<input type="button" class="buttonx" onclick="MyaddUserOrTeam();" style="width:110px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
					                                  	<br><br>
					                       			  	<input type="button" class="buttonx" style="width:110px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >											
			                                        </td>
			                                        <td>
			                                        	<table width="100%" height="100%">
			                                        		<tr height="25px">
			                                        			<td style="width: 100%;height: 100%">
			                                        				<input id="guest" type="text" style="width:220px;height: 25px">
			                                        			</td>
			                                        		</tr>
			                                        		<tr height="125px">
			                                        			<td style="width: 100%;height: 100%">
			                                        				<html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width: 220px; height: 125px;">
						                                                <c:if test="${!empty calendarEventForm.selectedAttsCol}">
						                                                  <html:optionsCollection name="calendarEventForm" property="selectedAttsCol" value="value" label="label" />
						                                                </c:if>
						                                              </html:select>
			                                        			</td>
			                                        		</tr>
			                                        	</table>
			                                        </td>			                                        
			                                        <td style="vertical-align: top;" nowrap="nowrap">
			                                        	<input type="button" class="buttonx" style="width:110px;" onclick="addGuest(document.getElementById('guest'))" value="<digi:trn key="calendar:btnAddGuest">Add</digi:trn>">
			                                            <img src="../ampTemplate/images/help.gif" onmouseover="stm([calendarHelp,separateEmails],Style[15])" onmouseout="htm()"/>
			                                        </td>
			                    	</tr>	
			                    	<tr height="5px"><td colspan="8">&nbsp;</td></tr>-->			                    	
			                    	<tr>
			                          <td colspan="8" style="text-align:center;">
			                          	<feature:display name="Preview Event button" module="Calendar">
			                          		<input type="submit" class="buttonx" style="width: 110px" onclick="return previewEvent();" value="<digi:trn key="calendar:previewBtn">Preview</digi:trn>" />
&nbsp;			                          	</feature:display>                           
			                            <feature:display name="Save and Send button" module="Calendar">
			                            	<input type="submit" class="buttonx" style="min-width: 110px" onclick="return sendEvent();" value="<digi:trn key="calendar:sendSaveBtn">Save and Send</digi:trn>" />
&nbsp;			                            </feature:display>
			                            <feature:display name="Recurring Event Button" module="Calendar">
			                            	<input type="button" class="buttonx" style="min-width: 110px" onclick="showRecEvent();" value="<digi:trn key="calendar:recurrinEventBtn">Recurring Event</digi:trn>"/>			                            </feature:display>			                          </td>
			                        </tr>
		                      </table>
			                </div>
			            </td>
        			</tr>       
				</table>
			</td>    	
        	</table>
    	</td>
	</tr>
</table>

<div id="myEvent" style="display:none;" >
		<jsp:include page="/calendar/recurringEvent.do" />
	</div>
</digi:form>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>
<script type="text/javascript">
	//attach character counters 
	$("#eventTitle").charCounter(50,{
									format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
									pulse: false});
	$("#descMax").charCounter(300,{
									format: " (%1"+ " <digi:trn>characters remaining</digi:trn>)",
									pulse: false});
</script>
