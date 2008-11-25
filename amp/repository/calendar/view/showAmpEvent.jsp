<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm" %>
<jsp:include page="/repository/calendar/view/scripts/calendarEventScript.jsp"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>
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

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<digi:instance property="calendarEventForm"/>
<style  type="text/css">
<!--

.contentbox_border {
    border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}
.myStyleClass{	
	width: 80px;
}
-->
</style>

<script language="JavaScript" type="text/javascript">
  <jsp:include page="../../aim/view/scripts/calendar.js.jsp" flush="true" />
  

function removeSelOrgs() {
	setMethod("removeOrg");
	selectAtts();
	document.calendarEventForm.submit();
}
function submitForm()
{
	setMethod("");
	selectAtts();
	document.calendarEventForm.submit();
}



</script>

<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />

<link rel="stylesheet" href="<digi:file src="module/calendar/css/main.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script language="JavaScript" type="text/javascript">

	var calendarHelp="<digi:trn key='calendar:calendarHelp'>Calendar</digi:trn>"
	var separateEmails="<digi:trn key='calendar:separateEmails'>Please separate email addresses by semicolons</digi:trn>"
		
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

  function check(){
    var et=document.getElementById("eventTitle");
    var list=document.getElementById("selreceivers"); 
    if(et.value==''){
      alert("Please enter event title");
      return false;
    }if(list==null || list.length==0){
    	alert("Please enter Attendees");
        return false;
    }else{
      return true;
    }
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
      if(list.options[i].value.indexOf('g')==0){
         if(list.options[i].selected){
            list.options[i]=null;
         }
         else{
      	   MyGuests[index]=list.options[i];
      	   index++;
         }
      }
    }
    if(orpIndex!=0){
       registerOrphanMember(orphands);
    }
    removeUserOrTeam();
    if(index != 0){
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

      if(list.options[i].value.indexOf('g')==0){
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

    var flag=false;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value=='g:'+guest.value &&list.options[i].text==guest.value){
        flag=true;
        break;
      }
    }
    if(flag){
      return false;
    }

	var guestVal=guest.value;
	while(guestVal.indexOf(";")!=-1){		
		var optionValue=guestVal.substring(0,guestVal.indexOf(";"));		
		addOption(list,optionValue,'g:'+optionValue);				
	    guestVal=guestVal.substring(guestVal.indexOf(";")+1);		
	}
	if(guestVal.length>0){
		addOption(list,guestVal,'g:'+guestVal);
	}	

    guest.value = "";
  }

    function isGuestAllreadyAdded(guest){
	  var selreceivers=document.getElementById('selreceivers');
	  for(var j=0; j<selreceivers.length;j++){
	    if(selreceivers.options[j].value==guest){
	      return true;
	    }
	  }
	  return false
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
    if(check()){
      setMethod("preview");
      selectAtts();
      selectAllOrgs();
      return true;
    }
    return false;
  }

  function reccuringEvent(){
	   	<digi:context name="rev" property="context/module/moduleinstance/recurringEvent.do" />
		openURLinWindow("<%=rev%>",832,624);
  }

  function sendEvent(){
	  if(check()){
		 var list = document.getElementById('selreceivers');  
		 if(list!=null){
		  	for(var i = 0; i < list.length; i++) {
		  		list.options[i].selected = true;
		  	}
		}
			  
		document.getElementById('hdnMethod').value = 'save';
		<digi:context name="sendEvent" property="context/module/moduleinstance/showCalendarEvent.do?method=save"/>
		document.calendarEventForm.action = "<%=sendEvent %>";
		document.calendarEventForm.target = "_self";
		document.calendarEventForm.submit();
	  }	else{
		  return false;
	  }  
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
  </script>


<digi:form action="/showCalendarEvent.do">
    <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
    <html:hidden name="calendarEventForm" property="selectedStartMonth" styleId="hiddenMonth"/>
    <html:hidden name="calendarEventForm" property="recurrPeriod" styleId="hidden"/>
	<html:hidden name="calendarEventForm" property="typeofOccurrence" styleId="type"/>
   
   
    <table>
    	 <tr>
			<td width=14>&nbsp;</td>
			<td align=left vAlign=top width=750>
				<table cellPadding=5 cellSpacing=0 width="100%">
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
			           		<table width="100%">
			                	<tr>
			                    	<td  style="height: 5px;"/>
			                    </tr>
			                    <tr>
			                   	 	<td style="background-color: #CCDBFF;height: 18px;"/>
			                    </tr>
			                </table>
			           </td>	
                    </tr>
             		<tr>
			            <td style="font-family: Tahoma;font-size: 12px;">                
			                <div style="background-color: #F5F5F5; padding: 20px">
			                  <digi:errors/>
			                  <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
			                  <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                    <table>
			                    	<tr>
			                    		<td nowrap="nowrap" align="right" style="vertical-align: text-top">
			                    			<font color="red" size="3px">*</font>
			                    			<digi:trn key="calendar:evntTitle">Event title</digi:trn>			                                
			                    		</td>
			                    		<td align="left" style="width: 220px;vertical-align: top;">
			                    			<html:text name="calendarEventForm" styleId="eventTitle" property="eventTitle" style="width: 220px" styleClass="inp-text"/>
			                    		</td>
			                    		<td rowspan="2" style="text-align: center;">
			                    			<digi:trn key="calendar:orgs">Organizations</digi:trn>
			                    		</td>
			                    		<td rowspan="2" align="left" valign="top">
			                    			 <html:select multiple="multiple" property="selOrganizations" size="4" style="width: 220px;">
			                                        	<logic:notEmpty name="calendarEventForm" property="organizations">
															<logic:iterate name="calendarEventForm" property="organizations" id="organization" type="org.digijava.module.aim.dbentity.AmpOrganisation">
																<html:option value="${organization.ampOrgId}">${organization.name}</html:option>
															</logic:iterate>
														</logic:notEmpty>
			                                          </html:select>
			                    		</td>
			                    		<td rowspan="2" valign="top">
			                    			<table cellSpacing="1" cellPadding="1">
												<tr>
													<td>
													<aim:addOrganizationButton refreshParentDocument="false" collection="organizations" form="${calendarEventForm}"  callBackFunction="submitForm();" styleClass="dr-menu"><digi:trn key="btn:addOrganizations" >Add Organizations</digi:trn></aim:addOrganizationButton> 	 	                                                                                                                    
													</td>
												</tr>
												<tr>
													<td>
														<html:button  property="submitButton" onclick="return removeSelOrgs()" styleClass="myStyleClass">
															<digi:trn key="btn:remove">Remove</digi:trn>
														</html:button>
													</td> 
												</tr>
											</table>	
			                    		</td>
			                    	</tr>
			                    	
			                    	<tr>
			                    		<td align="right" nowrap="nowrap" style="text-align: right;">
			                    			<digi:trn key="calendar:cType">Calendar type</digi:trn>
			                    		</td>
			                    		<td align="left">
			                    			 <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
			                                          <html:select name="calendarEventForm" property="selectedCalendarTypeId" style="width: 220px;" onchange="this.form.submit()" styleClass="inp-text">
			                                            <c:if test="${!empty calendarEventForm.calendarTypes}">
			                                              <c:set var="types" value="${calendarEventForm.calendarTypes}"/>
			                                              <html:options collection="types" property="value" labelProperty="label"/>
			                                            </c:if>
			                                          </html:select>
			                    		</td>			                    		
			                    	</tr>
			                    	<tr style="height:25px">
			                    		<td valign="top" align="right" nowrap="nowrap" rowspan="1">
			                    			<digi:trn key="calendar:eventsType">Event type</digi:trn>
			                    		</td>
			                    		<td align="left" valign="top" rowspan="1">
			                    			 <html:select name="calendarEventForm" style="width: 220px;" property="selectedEventTypeId" styleClass="inp-text">
			                                            <c:if test="${!empty calendarEventForm.eventTypesList}">
			                                              <c:forEach var="evType" items="${calendarEventForm.eventTypesList}">
			                                                <html:option value="${evType.id}" style="color:${evType.color};font-weight:Bold;">${evType.name}</html:option>
			                                              </c:forEach>
			                                            </c:if>
			                                          </html:select>
			                    		</td>
			                    		<td rowspan="4" style="text-align: center" align="right" nowrap="nowrap">
			                    			<digi:trn key="calendar:Description">Description</digi:trn>
			                    		</td>
			                    		<td style="width: 220px" align="left" rowspan="4">
			                    			<html:textarea name="calendarEventForm" styleId="descMax" property="description" style="width: 100%" rows="4"/>
			                    		</td>
			                    		<td>&nbsp;</td>			                    		
			                    	</tr>
			                    	<tr style="height: 25px;">
			                    		<td nowrap="nowrap" align="right" style="text-align: right;vertical-align: top;" >
			                    			<digi:trn key="calendar:StDate">Start date</digi:trn>
			                    		</td>
			                    		<td align="left" style="width: 220px;vertical-align: top;">
			                    			<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
			                                          	<html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
			                                            <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td nowrap="nowrap">
			                                                  <html:text styleId="selectedStartDate" readonly="true" name="calendarEventForm" property="selectedStartDate" style="width:80px" styleClass="inp-text"/>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td>
			                                                <td>
			                                                  <a id="clear1" href="javascript:clearDate(document.getElementById('selectedStartDate'), 'clear1')">
			                                                    <digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
			                                                  </a>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td>
			                                                <td>
			                                                  <a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("selectedStartDate"),"clear1")'>
			                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
			                                                  </a>
			                                                </td>
			                                                <td>&nbsp;&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option value="${hour}" class="inp-text">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>			                                                 
			                                                  	<script type="text/javascript">
				                                                  selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
				                                                </script>	                                                  
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option value="${minute}" class="inp-text">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
			                                                  </script>
			                                                </td>
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
			                                                  createYearCombo(document.getElementById('selectedStartYear'), document.getElementById('selectedStartDate').value);
			                                                  </script>
			                                                </td>
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
			                                                  selectOptionByValue(document.getElementById('selectedStartMonth'), get('month', document.getElementById('selectedStartDate').value));
			                                                  </script>
			                                                </td>
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
			                                                  selectOptionByValue(document.getElementById('selectedStartDay'), get('day', document.getElementById('selectedStartDate').value));
			                                                  </script>
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option class="inp-text" value="${hour}">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
			                                                  </script>
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
			                                                  </script>
			                                                </td>
			                                              </tr>
			                                            </table>
			                                          </c:if>
			                    		</td>			                    		
			                    		<td>&nbsp;</td>
			                    	</tr>
			                    	<tr height="25px;">
			                    		<td  nowrap="nowrap" align="right" style="text-align: right">
			                    			<digi:trn key="calendar:EndDate">End Date</digi:trn>
			                    		</td>
			                    		<td>
			                    			<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
			                                            <table cellpadding="0" cellspacing="0">
			                                              <tr>
			                                                <td nowrap="nowrap">
			                                                  <html:text styleId="selectedEndDate" readonly="true" name="calendarEventForm" property="selectedEndDate" style="width:80px" styleClass="inp-text"/>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td>
			                                                <td>
			                                                  <a id="clear2" href="javascript:clearDate(document.getElementById('selectedEndDate'),'clear2')">
			                                                    <digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
			                                                  </a>
			                                                </td>
			                                                <td>
			                                                &nbsp;
			                                                </td>
			                                                <td>
			                                                  <a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("selectedEndDate"),"clear2")'>
			                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
			                                                  </a>
			                                                </td>
			                                                <td>
			                                                &nbsp;&nbsp;
			                                                </td>
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
			                                                  selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
			                                                  </script>
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td  style="font-family: Tahoma;font-size: 11px">
			                                                  <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
			                                                  </script>
			                                                </td>
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
			                                                  createYearCombo(document.getElementById('selectedEndYear'), document.getElementById('selectedEndDate').value);
			                                                  </script>
			                                                </td>
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
			                                                  selectOptionByValue(document.getElementById('selectedEndMonth'), get('month', document.getElementById('selectedEndDate').value));
			                                                  </script>
			                                                </td>
			                                                <td>
			                                                  <select id="selectedEndDay" onchange="updateDate(document.getElementById('selectedEndDate'), 'day', this.value)">
			                                                    <c:forEach var="i" begin="1" end="30">
			                                                      <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
			                                                      <option class="inp-text" value="${i}"/>${i}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedEndDay'), get('day', document.getElementById('selectedEndDate').value));
			                                                  </script>
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
			                                                    <c:forEach var="hour" begin="0" end="23">
			                                                      <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
			                                                      <option class="inp-text" value="${hour}">${hour}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
			                                                  </script>
			                                                </td>
			                                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
			                                                <td>
			                                                  <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
			                                                    <c:forEach var="minute" begin="0" end="59">
			                                                      <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
			                                                      <option class="inp-text" value="${minute}">${minute}</option>
			                                                    </c:forEach>
			                                                  </select>
			                                                  <script type="text/javascript">
			                                                  selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
			                                                  </script>
			                                                </td>
			                                              </tr>
			                                            </table>
			                                          </c:if>
			                    		</td>			                    		
			                    		<td>&nbsp;</td>
			                    	</tr>
			                    	<tr height="25px;">
			                    		<td>&nbsp;</td>
			                    		<td style="vertical-align: top">
			                    			<html:hidden name="calendarEventForm" property="privateEvent"/>
			                                          <input type="checkbox" name="privateEventCheckbox" onchange="javascript:makePublic();" 
				                                          <c:if test="${!calendarEventForm.privateEvent }">
				                                          	CHECKED
				                                          </c:if>                                          
			                                          />
			                                          <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>
			                    		</td>			                    		
			                    		<td>&nbsp;</td>			                    		
			                    	</tr>
			                    	<tr height="25px;">
			                    		<td colspan="5"> &nbsp;</td>
			                    	</tr>
			                    	<tr>
			                    		<td>&nbsp;</td>
			                    		<td>
			                    			<font color="red" size="3px">*</font>
			                    			<digi:trn key="calendar:Attendee">Attendee</digi:trn>			                                            
			                    		</td>
			                    		<td> &nbsp;</td>
			                    		<td>&nbsp;</td>
			                    		<td>&nbsp;</td>
			                    	</tr>
			                    	<tr>
			                    		<td>&nbsp;</td>
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
			                                        <td>
					                                	<input type="button" onclick="MyaddUserOrTeam();" style="width:80px;font-family:tahoma;font-size:11px;" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
					                                  	<br><br>
					                       			  	<input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >											
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
			                                        <td style="vertical-align: top;">
			                                        	<input type="button" style="width:65px;" onclick="addGuest(document.getElementById('guest'))" value="<digi:trn key="calendar:btnAddGuest">Add</digi:trn>">
			                                            <img src="../ampTemplate/images/help.gif" onmouseover="stm([calendarHelp,separateEmails],Style[15])" onmouseout="htm()"/>
			                                        </td>
			                    	</tr>
			                    	<tr>
			                          <td colspan="5" style="text-align:center;">                            
			                            <input type="submit" style="width: 105px" onclick="return previewEvent();" value="<digi:trn key="calendar:previewBtn">Preview</digi:trn>" />
			                            &nbsp;
			                            <input type="submit" style="width: 105px" onclick="return sendEvent();" value="<digi:trn key="calendar:sendSaveBtn">Save and Send</digi:trn>" />
			                            &nbsp;                            
			                            <input type="button" style="width: 105px" onclick="showRecEvent();" value="<digi:trn key="calendar:recurrinEventBtn">Recurring Event</digi:trn>">
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
	//attach character counters
	$("#eventTitle").charCounter(50,{
									format: " (%1"+ " <digi:trn key="calendar:charactersRemaining">characters remaining</digi:trn>)",
									pulse: false});
	$("#descMax").charCounter(300,{
									format: " (%1"+ " <digi:trn key="calendar:charactersRemaining">characters remaining</digi:trn>)",
									pulse: false});
</script>
