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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm" %>

<!-- Source File -->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
        
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>

<!-- Individual YUI CSS files -->
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/assets/skins/sam/autocomplete.css"> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script>

<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp"  />

<style>
<!--
.ui-autocomplete {
	font-size:12px;
	border: 1px solid silver;
	max-height: 150px;
	overflow-y: scroll;
	background: white;
}

-->
</style>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#statesAutoComplete ul,
{
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#contactsAutocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesAutoComplete div{
	padding: 0px;
	margin: 0px; 
}

#contactsAutocomplete div {
	padding: 0px;
	margin: 0px; 
}

#statesAutoComplete,
#contactsAutocomplete {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesAutoComplete,contactsAutocomplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
    font-size: 12px;
}

#statesInput,
#contactInput {
    font-size: 12px;
}
.charcounter {
    display: block;
    font-size: 11px;
}

#statesAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

span.extContactDropdownEmail {
	color:grey;
}

-->
</style>


<div id="popin" class="invisible-item">
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
	
	// don't remove or change this line!!
	document.getElementsByTagName('body')[0].className='yui-skin-sam';
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
	
	div.charcounter-progress-container {
	width:inherit; 
	height:3px;
	max-height:3px;
	border: 1px solid gray; 
	filter:alpha(opacity=20); 
	opacity:0.2;
	}
	
	div.charcounter-progress-bar {
		height:3px; 
		max-height:3px;
		font-size:3px;
		background-color:#5E8AD1;
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
		'<digi:trn jsFriendly="true">Loading...</digi:trn><br/><br/></div>';
		showContent();
	}

	-->

</script>


<digi:instance property="calendarEventForm"/>

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
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
  <jsp:include page="../../aim/view/scripts/calendar.js.jsp"  />
  
	
function cancel() { 
	setMethod("");
	selectAtts();
	var eventForm = document.getElementById("showAmpEventFormID");
	eventForm.action="${contextPath}/calendar/showCalendarView.do?filterInUse=false";
	eventForm.target = "_self";
	eventForm.submit();	
}
function removeSelOrgs() {
	var orgList = document.getElementsByName("selOrganizations");
	var confirmMsg = '<digi:trn jsFriendly="true">Are you sure to remove selected organizations?</digi:trn>';
	var alertMsg = '<digi:trn jsFriendly="true">You should select at least one organization.</digi:trn>';
	if (orgList[0].selectedIndex == -1){
		alert (alertMsg);
	} else {
		if (confirm(confirmMsg)){
			setMethod("removeOrg");
			selectAtts();
			var eventForm = document.getElementById("showAmpEventFormID");
			eventForm.target = "_self";
			eventForm.submit();	
		}
	}
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

<jsp:include page="../../aim/view/scripts/newCalendar.jsp"  />
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
	  var selreceivers = document.getElementById('selreceivers');
		if (selreceivers != null) {
			for (var j = 0; j < selreceivers.length; j++) {
				if (selreceivers.options[j].value == 'g:' + guest) {
					return true;
				}
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
	  if (orglist != null) {
		  for (var i = 0; i < orglist.options.length; i++) {
			  orglist.options[i].selected = true;
		  }
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
			

		if (validateText() && validateDates()){ 
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
  }	  

function validateDates(){
	var startDate = document.getElementById("selectedStartDate").value;
   	var endDate = document.getElementById("selectedEndDate").value; 
   	var startHour = 1*(document.getElementById("selectedStartHour").value);
   	var endHour = 1*(document.getElementById("selectedEndHour").value); 
   	var startMin = 1*(document.getElementById("selectedStartMinute").value);
   	var endMin = 1*(document.getElementById("selectedEndMinute").value); 
   	//alert ("startDate: " + startDate + " -- "+"startHour: " + startHour + " -- "+"startMin: " + startMin + " -- "+"endDate: " + endDate + " -- "+"endHour: " + endHour + " -- "+"endMin: " + endMin + " -- ");
	if (compareDates(startDate, endDate, false)==1){
		alert ('<digi:trn jsFriendly="true">End Date Should Be Greater Than Start Date</digi:trn>');
        return false;
	}
	if (compareDates(startDate, endDate, false)==0){
		if (endHour < startHour){
			alert ('<digi:trn jsFriendly="true">End Date Should Be Greater Than Start Date</digi:trn>');
	        return false;
		}
		if (endHour == startHour){
			if ((endMin < startMin) || (endMin == startMin)){
				alert ('<digi:trn jsFriendly="true">End Date Should Be Greater Than Start Date</digi:trn>');
		        return false;
			}
		}
	}
	return true;
}

function validateText(){
	var title = "" + document.getElementById("titleMax").value;
   	var decription = "" + document.getElementById("descMax").value; 
   	var regexp = new RegExp("[a-zA-Z0-9 \r\n,._ÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéëìíïñòóõöùúü%&' ()а-яА-ЯşŞţŢîÎăĂâÂ]+");
   	
   	if (title==""){
		alert ("<digi:trn jsFriendly='true'>Title can't be empty!</digi:trn>");
        return false;
	}
	if (regexp.exec(title)!=title){
		alert ("<digi:trn jsFriendly='true'>Please, for title use only letters, digits, '_', () and space.</digi:trn>");
        return false;
	}
	if (decription != "" && regexp.exec(decription)!=decription){
		alert ("<digi:trn jsFriendly='true'>Please, for description use only letters, digits, '_', () and space.</digi:trn>");
        return false;
	}
	return true;
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

function compareDates(date1, date2, diffDate)
{
    var dt1  = parseInt(date1.substring(0,2),10);
    var mon1 = parseInt(date1.substring(3,5),10);
    var yr1  = parseInt(date1.substring(6,10),10);
    var dt2  = parseInt(date2.substring(0,2),10);
    var mon2 = parseInt(date2.substring(3,5),10);
    var yr2  = parseInt(date2.substring(6,10),10);
    var date1 = new Date(yr1, mon1, dt1);
    var date2 = new Date(yr2, mon2, dt2);
    if (diffDate){
		return Math.abs(date1-date2);
    } else {
    	if(date2-0 < date1-0)
	    {
	        return 1;
	    } else if (date2-0 > date1-0) {
	        return -1;
	    } else if (date2-0 == date1-0) {
	        return 0;
	    }
	    return null;
    }
}

</script>

<script language="JavaScript" type="text/javascript">

var addedGuests = new Array();

function validateGuestEmail (email) {
	var successVal = true;
	if(email.indexOf("<")!=-1){
		email=email.substr(email.indexOf("<")+1, email.indexOf(">")-email.indexOf("<")-1); //cut email from "some text <email>"
	}
	
	var pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	var expression = new RegExp(pattern)
	if(expression.test(email)!=true){
	    var trn='<digi:trn jsFriendly="true">Please provide correct email</digi:trn>';
			alert(trn);
	    successVal = false; 
	}
	return successVal;
}

function addContact(contact){
	var guestVal=contact.value;
	if (validateGuestEmail(guestVal)) {
		if(guestVal.length>0 && $.inArray("g:" + guestVal, addedGuests) < 0){
			addedGuests.push("g:" + guestVal);
			var filteredGusetId = guestVal.replace("<", "&lt;").replace(">", "&gt;");

			var guestListItemMarkup = new Array();
			guestListItemMarkup.push('<div class="msg_added_cont">');
			guestListItemMarkup.push('<div style="float:right;position: relative"><span style="cursor:pointer;" onClick="removeGuest(this)">[x] remove</span></div>');
			guestListItemMarkup.push(filteredGusetId);
			guestListItemMarkup.push('<input name="selectedAtts" class="guest_contact_hidden" type="hidden" value="g:');
			guestListItemMarkup.push(filteredGusetId);
			guestListItemMarkup.push('">');
			guestListItemMarkup.push('</div>');
			var html=guestListItemMarkup.join('');
			$('#guest_user_container').append(html);
		}	
		contact.value = "";
	}
}

function fillExternalContact() {
	if (selContacts != null && selContacts.length > 0) {
		var guestListItemMarkup = new Array();
		for (var contIdx = 0; contIdx < selContacts.length; contIdx ++) {
			var curContact = selContacts[contIdx];
			
			if (curContact.substring(0,2) == "g:") {
				guestListItemMarkup.push('<div class="msg_added_cont">');
				guestListItemMarkup.push('<div style="float:right;"><span style="cursor:pointer;" onClick="removeGuest(this)">[x] remove</span></div>');
				guestListItemMarkup.push(curContact.substring(2));

				guestListItemMarkup.push('<input name="selectedAtts" class="guest_contact_hidden" type="hidden" value="');								

				guestListItemMarkup.push(curContact);
				guestListItemMarkup.push('"/>');

				guestListItemMarkup.push('</div></div>');
			}
		}
		$('#guest_user_container').append(guestListItemMarkup.join(''));
	}
}

function removeGuest(obj) {
	var delControl = $(obj);
	delControl.parent().parent().remove();
	
	var addedGuestIdx;
	for (addedGuestIdx = 0; addedGuestIdx < addedGuests.length; addedGuestIdx ++) {
		if (addedGuests[addedGuestIdx] == delControl.parent().parent().find("input").attr("value")) {
			addedGuests.splice(addedGuestIdx, 1);
			break;
		}
	}
	return null;
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
	<h1 class="admintitle">&nbsp;<digi:trn key="calendar:CreateAnEvent">Create An Event</digi:trn></h1>
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
			<td noWrap vAlign="top"> 
            	<table class="contentbox_border" width="100%" cellpadding="0" cellspacing="0">
                	<tr>	
                		<td align="center" style="padding: 0px 3px 0px 3px;">
			           		
			           </td>	
                    </tr>
             		<tr>
			            <td style="font-family: Arial;font-size: 12px;">                
			                <div style="background-color: #ffffff; padding: 20px; background-color:#F8F8F8;">
			                	<span style="font-family: Arial;font-size: 11px;"><digi:errors/></span>			                  
			                  <html:hidden name="calendarEventForm" property="calendarTypeId" styleId="CalendatTypeid"/>
			                  <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                    <table border="0" align="center" cellpadding="3" cellspacing="3" class="t_mid">
			                    	<tr>
			                    		<td nowrap="nowrap" style="vertical-align: text-top" width=48%>			                    			
			                    			<div style="margin-bottom:5px;"><font color="red" size="3px">*</font><b><digi:trn>Title</digi:trn></b>
			                    				<span style="font-size:11px;" id="titleCharCounter"></span>
			                    			</div>			                    			
										    <div class="charcounter-progress-container" style="width: 220px;">
										    	<div id="titleProgressBar" class="charcounter-progress-bar" style="width:0%;"></div>
										    </div>
										    <div style="height:5px;width:220px;">&nbsp; </div>
										    
			                    			<html:text name="calendarEventForm" styleId="titleMax" property="eventTitle" style="width: 220px" styleClass="inp-text"/>
			                    			<br /><br />
			                    			<digi:trn key="calendar:cType"><div style="margin-bottom:5px;"><b><digi:trn>Calendar type</digi:trn></b></div></digi:trn>
			                    			<html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                                 <html:select name="calendarEventForm" property="selectedCalendarTypeId" styleId="selectedCalendarTypeId" style="width: 220px;" onchange="submitForm(this.form)" styleClass="inp-text">
			                                     <c:if test="${!empty calendarEventForm.calendarTypes}">
			                                     	<c:forEach var="type" items="${calendarEventForm.calendarTypes}">
				                                        	<html:option value="${type.value}">${type.label}</html:option>
		                                           </c:forEach>
			                                     </c:if>
                                                         </html:select><br /><br /><div style="margin-bottom:5px;"><b><digi:trn key="calendar:eventsType">Event type</digi:trn></b></div>
											 <html:select name="calendarEventForm" style="width: 220px;" property="selectedEventTypeId" styleClass="inp-text">
				                                    <c:if test="${!empty calendarEventForm.eventTypesList}">
				                                    	<c:forEach var="evType" items="${calendarEventForm.eventTypesList}">
				                                        	<html:option value="${evType.id}" style="color:${evType.color};font-weight:Bold;"><digi:trn>${evType.name}</digi:trn></html:option>
				                                        </c:forEach>
				                                 	</c:if>
				                                 </html:select>
											<br /><br />
                                                         <div style="margin-bottom:5px;"><b><digi:trn>Start date</digi:trn></b></div>
											<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
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
			                                                <td  style="font-family: Arial;font-size: 11px">
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
			                                                <td  style="font-family: Arial;font-size: 11px">
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
			                                          </c:if>
													  <br />
                                                                                                          <div style="margin-bottom:5px;"><b><digi:trn>End Date</digi:trn></b></div>											  <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
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
			                                                <td  style="font-family: Arial;font-size: 11px">
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
			                                                <td  style="font-family: Arial;font-size: 11px">
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
			                                          </c:if>
													  <br />
													  <html:hidden name="calendarEventForm" property="privateEvent"/>
			                                          <input type="checkbox" name="privateEventCheckbox" onchange="javascript:makePublic();" />
				                                          <c:if test="${!calendarEventForm.privateEvent }">
				                                          	<script language="Javascript">
				                                          		document.getElementsByName("privateEventCheckbox")[0].checked = true
				                                          	</script>
				                                          </c:if>                                          
			                                          <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>											</td>
			                    		<td width="2%">&nbsp;</td>
			                    		<feature:display name="Donors" module="Calendar">			                    			
			                    			<td colspan="4" valign="top" width=48%>
                                                                <digi:trn key="cal:organizations"><b><digi:trn>Organizations</digi:trn></b></digi:trn>	
											<br />
											<div style="margin-top:7px;">
												<table border="0" cellpadding="0" cellspacing="3">
													<tr>
														<td>
															<html:select multiple="multiple" property="selOrganizations" size="4" style="width: 300px;">
							                	<logic:notEmpty name="calendarEventForm" property="organizations">
																	<logic:iterate name="calendarEventForm" property="organizations" id="organization" type="org.digijava.module.aim.dbentity.AmpOrganisation">
																		<html:option value="${organization.ampOrgId}" style="font-family: Arial;font-size:11px;">${organization.name}</html:option>
																	</logic:iterate>
																</logic:notEmpty>
							                </html:select>
							              </td><td valign="top">
							                <div style="float:right;">
						                  	<table border="0" cellPadding="1" cellSpacing="1">
						                    	<field:display name="Add Donor Button" feature="Donors">
																		<tr>
																			<td>
																				<aim:addOrganizationButton refreshParentDocument="false" collection="organizations" form="${calendarEventForm}"  callBackFunction="submitForm();" styleClass="buttonx"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
																			</td>
																		</tr>
						                    	</field:display>
																	<field:display name="Remove Donor Button" feature="Donors">
																		<c:if test="${not empty calendarEventForm.organizations}">
																			<tr>
																				<td>
																					<html:button  property="submitButton" onclick="return removeSelOrgs()" styleClass="buttonx" style="width:110px">
																						<digi:trn key="btn:remove">Remove</digi:trn>
																					</html:button>
																				</td>
																			</tr>
																		</c:if>
																	</field:display>
																</table>
															</div>
														</td>
													</tr>
												</table>
											</div>
												
											<br/><br/>


												<b><digi:trn key="calendar:Description">Description</digi:trn></b><span style="font-size:11px;" id="descCharCounter"></span>
											    <div class="charcounter-progress-container" style="width: 100%">
											    	<div id="descProgressBar" class="charcounter-progress-bar" style="width:0%;"></div>
											    </div>
											    <div style="height:5px;width:220px;">&nbsp; </div>
												<div style="margin-top:7px;"><html:textarea name="calendarEventForm" styleId="descMax" property="description" style="width: 100%" rows="4"/></div>													                    						                    		</td>
			                    		</feature:display>			                    		
			                    	</tr>


									
			                    	<tr height="25px;">
			                    		<td colspan="8">
										
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign=top style="font-size:12px;">
	<hr />
	<font color="red" size="3px">*</font>
		<b style="font-size:12px;"><digi:trn key="calendar:Attendee">Attendee</digi:trn></b><br />
	<div class="msg_receivers">
		<logic:empty name="calendarEventForm" property="teamMapValues">
			<div class="msg_lbl">No receivers</div>
		</logic:empty>
		<logic:notEmpty name="calendarEventForm"  property="teamMapValues" >
			<c:forEach var="team" items="${calendarEventForm.teamMapValues}">
				<logic:notEmpty name="team" property="members">
					<div class="rec_group_container">
						<div class="msg_grp_name">
							
							<html:multibox property="selectedAtts" value="t:${team.id}" style="float:left;" styleClass="group_checkbox"/>
							<div class="msg_lbl">---<c:out value="${team.name}"/>---</div>
						</div>
						<div class="msg_grp_mem_name">
							<c:forEach var="tm" items="${team.members}">
								<html:multibox property="selectedAtts" styleId="t:${team.id}" value="m:${tm.memberId}" />${tm.memberName}<br/>
							</c:forEach>
						</div>
					</div>
				</logic:notEmpty>											                                                		
			</c:forEach>		
		</logic:notEmpty>
	
	</div>
	<br />
				<input type="checkbox" name="sendToAll" value="checkbox"/><digi:trn>Send to All</digi:trn><br/><br/>
	<b><digi:trn>Additional Receivers</digi:trn>: </b><digi:trn>Type first letter of contact to view suggestions or enter e-mail to send message to</digi:trn><br />
			<div class="msg_add">
				
				<input type="text" id="contactInput" class="inputx" style="width:470px; Font-size: 10pt; height:22px;">
				<div id="extContactAutocom"></div>
				<input type="button" value="<digi:trn>Add</digi:trn>" class="buttonx_sm" onClick="addContact(document.getElementById('contactInput'))">
				<br>
				<div id="contactsContainer" style="width:470px;"></div>
				<div id="guest_user_container">
				<c:if test="${!empty calendarEventForm.selectedAttsCol}">
                    <c:forEach var="attendee" items="${calendarEventForm.selectedAttsCol}">
                        <c:if test="${fn:startsWith(attendee.value, 'g:')}">
                       		<div class="msg_added_cont">	
								<div style="float:right;position: relative"><span style="cursor:pointer;" onClick="removeGuest(this)">[x] remove</span></div>
								${attendee.label}
								<input name="selectedAtts" class="guest_contact_hidden" type="hidden" value="g:${attendee.value}">
							</div>
                        </c:if>
                    </c:forEach>
                    
                  </c:if>
				</div>
			</td>
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
					                                	<input type="button" class="buttonx" onclick="MyaddUserOrTeam();" style="width:110px;font-family:Arial;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
					                                  	<br><br>
					                       			  	<input type="button" class="buttonx" style="width:110px;font-family:Arial;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >											
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
									  <hr />
			                          	<feature:display name="Preview Event button" module="Calendar">
			                          		<input type="submit" class="buttonx" style="width: 110px" onclick="return previewEvent();" value="<digi:trn key="calendar:previewBtn">Preview</digi:trn>" />
&nbsp;			                          	</feature:display>                           
			                            <feature:display name="Save and Send button" module="Calendar">
			                            	<input type="button" class="buttonx" style="min-width: 110px" onclick="return sendEvent();" value="<digi:trn key="calendar:sendSaveBtn">Save and Send</digi:trn>" />
&nbsp;			                            </feature:display>
			                            <feature:display name="Recurring Event Button" module="Calendar">
			                            	<input type="button" class="buttonx" style="min-width: 110px" onclick="showRecEvent();" value="<digi:trn key="calendar:recurrinEventBtn">Recurring Event</digi:trn>"/>
&nbsp;			                          	</feature:display>
			                            <input type="button" class="buttonx" style="min-width: 110px" onclick="cancel();" value="<digi:trn>Cancel</digi:trn>"/>
			                          </td>
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

<script type="text/javascript">
	
	//Char counters	
	var titleLength = 50;
	var titleCounter = $("#titleCharCounter");
	var titleProgressBar = $("#titleProgressBar");
	initTitleCharCounter();
	
	function initTitleCharCounter() {
		var titleCounterTxt = ["(", titleLength - $("#titleMax").val().length, " <digi:trn>characters remaining</digi:trn>", ")"];
		titleCounter.html(titleCounterTxt.join(""));
		titleProgressBar.css("width", $("#titleMax").val().length/titleLength*100 + "%");
	}
	$("#titleMax").bind("keyup", function (event) {
		if (this.value.length > titleLength) {
			this.value = this.value.substring(0, titleLength);
		}
		var titleCounterTxt = ["(", titleLength - this.value.length, " <digi:trn>characters remaining</digi:trn>", ")"];
		titleCounter.html(titleCounterTxt.join(""));
		titleProgressBar.css("width", this.value.length/titleLength*100 + "%");
	});
	
	var descLength = 500;
	var descCounter = $("#descCharCounter");
	var descProgressBar = $("#descProgressBar");
	
	initDescCharCounter();
	
	function initDescCharCounter() {
		var descCounterTxt = ["(", descLength - $("#descMax").val().length, " <digi:trn>characters remaining</digi:trn>", ")"];
		descCounter.html(descCounterTxt.join(""));
		descProgressBar.css("width", $("#descMax").val().length/descLength*100 + "%");
	}
	$("#descMax").bind("keyup", function (event) {
		if (this.value.length > descLength) {
			this.value = this.value.substring(0, descLength);
		}
		var descCounterTxt = ["(", descLength - this.value.length, " <digi:trn>characters remaining</digi:trn>", ")"];
		descCounter.html(descCounterTxt.join(""));
		descProgressBar.css("width", this.value.length/descLength*100 + "%");
	});
	//End of char counters	
	
	//Team select checkbox handlers
	$(".group_checkbox").bind("change", function (e) {
		var srcObj = $(this);
		srcObj.parents("div.rec_group_container").children("div.msg_grp_mem_name").children("input[type='checkbox']").attr("checked", srcObj.attr("checked"));
				
	});
	
	//select all handler
	$("input[type='checkbox'][name='sendToAll']").bind("change", function (e) {
        var checked = $(this).attr('checked');
        var childBoxes = $("div.msg_receivers").find("input[type='checkbox']");
        if (checked || 'checked' == checked) {
            childBoxes.attr("checked", "checked");
        } else {
            childBoxes.attr("checked", false);
        }
	});
	
	//External contact autocomplite
	var extContactDataSource = new YAHOO.widget.DS_XHR("/message/messageActions.do", ["\n", ";"]);
	extContactDataSource.scriptQueryAppend = "actionType=searchExternalContacts";
	//extContactDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	extContactDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	extContactDataSource.queryMatchContains = true;
  extContactDataSource.scriptQueryParam  = "srchStr";
	var extContactAutoComp = new YAHOO.widget.AutoComplete("contactInput","extContactAutocom", extContactDataSource);
	
	extContactAutoComp.formatResult = function( oResultData , sQuery , sResultMatch ) {
		var retVal;
		//Hilight email separately
		
		if (oResultData[0].indexOf('<') > -1 && oResultData[0].indexOf('>') > -1) {
			var contactEmail = oResultData[0].substring (oResultData[0].indexOf('<') + 1, oResultData[0].indexOf('>'));
			var contactName = oResultData[0].substring (0, oResultData[0].indexOf('<'));
			var markup = [contactName, '<span class="extContactDropdownEmail">(', contactEmail, ')<span>'];
			retVal = markup.join("");
		} else {
			retVal = oResultData;
		}
		
		return retVal;
	}
	
	extContactAutoComp.queryDelay = 0.5;
	$("#contactInput").css("position", "static");
	
</script>


