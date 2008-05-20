<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarEventForm"/>
<script language="JavaScript" type="text/javascript">
                  <jsp:include page="../../aim/view/scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />

<link rel="stylesheet" href="<digi:file src="module/calendar/css/main.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script language="JavaScript" type="text/javascript">

function makePublic(){

var showPrivateEvents = document.getElementById('PrivateEvents');

    if (showPrivateEvents.checked==true) {
    document.getElementsByName('privateEvent')[0].value=true;
    }
    if (showPrivateEvents.checked==false){
    document.getElementsByName('privateEvent')[0].value=false;      
        
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

function preSubmit(){
  if(!check()){
    return false;
  }
  document.getElementsByName('ispreview')[0].value=1;
  selectUsers();
}

function check(){
    var et=document.getElementById("eventTitle");
    if(et.value==''){
      alert("Please enter event title");
      return false;
    }else{
      selectAllOrgs();
      return true;
    }
}

function addUser(){
    var uslist = document.getElementById('usersList');
    var selUsList=document.getElementById('selectedUsersSel');

    if (uslist == null) {
        return false;
    }

    var index = uslist.selectedIndex;
    if (index != -1) {
        for(var i = 0; i < uslist.length; i++) {
            if (uslist.options[i].selected){
              if(selUsList.length!=0){
                var flag=false;
                for(var j=0; j<selUsList.length;j++){
                  if(selUsList.options[j].value=='u:'+uslist.options[i].value &&
                     selUsList.options[j].text==uslist.options[i].text){
                    flag=true;
                  }
                }
                if(!flag){
                  addOnption(selUsList,uslist.options[i].text,'u:'+uslist.options[i].value);
                }
              }else{
                addOnption(selUsList,uslist.options[i].text,'u:'+uslist.options[i].value);
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

function addGuest(guest) {
    var list = document.getElementById('selectedUsersSel');
    if (list == null || guest == null || guest.value == null || guest.value == "") {
        return;
    }

    var flag=false;
    for(var i=0; i<list.length;i++){
      if(list.options[i].value=='g:'+guest.value &&
         list.options[i].text==guest.value){
        flag=true;
        break;
      }
    }
    if(flag){
      return false;
    }

    var option = document.createElement("OPTION");
    option.value = 'g:'+guest.value;
    option.text = guest.value;
    list.options.add(option);
    guest.value = "";
}

function removeUser() {
    var list = document.getElementById('selectedUsersSel');
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

function selectUsers() {
    var list = document.getElementById('selectedUsersSel');
    if (list == null) {
        return;
    }
    for(var i = 0; i < list.length; i++) {
        list.options[i].selected = true;
    }
}

function edit(key) {
	document.calendarEditActivityForm.step.value = "1.1";
	document.calendarEditActivityForm.editKey.value = key;
	document.calendarEditActivityForm.target = "_self";
	document.calendarEditActivityForm.submit();
}

function removeSelOrganisations() {
    var list = document.getElementById('organizationList');
    if (list == null) {
        return false;
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
    return false;
}

function selectAllOrgs() {
    var lsItem;
    var orglist = document.getElementById('organizationList');
    for(var ind=0; ind<orglist.options.length; ind++){
      lsItem=orglist.options[ind];
      lsItem.selected=true;
    }
    //document.getElementById('organizationList').option.selectAll;
    return true;
}

function delSubmit(){
  if(confirm('Are You sure?')){
    return true
  }else{
    return false
  }
}
</script>



<table border="0" width="100%" cellPadding=0 cellSpacing=0>
        <tr>
          <td colspan="3"><jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true"/><td>
       </tr>

       <table class="r-dotted-lg">
       <tr>
       <td class="r-dotted-lg">&nbsp;</td>
         <td valign="top" width="230">
            <table border="0" style="border:1px solid; border-color: #484846;">
                <tr>
                    <td nowrap="nowrap">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td nowrap="nowrap"><digi:trn key="calendar:CalendarType">&nbsp;Calendar Type&nbsp;&nbsp;</digi:trn></td>
                                <digi:form action="/showCalendarEvent.do">
                                    <td>
                                        <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
                                        <html:select name="calendarEventForm" property="selectedCalendarTypeId" onchange="this.form.submit()">
                                            <logic:notEmpty name="calendarEventForm" property="calendarTypes">
                                                <bean:define id="types" name="calendarEventForm" property="calendarTypes" type="java.util.List"/>
                                                <html:options collection="types" property="value" labelProperty="label"/>
                                            </logic:notEmpty>
                                        </html:select>
                                    </td>
                                </digi:form>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </td>
        <td align="right">
             <c:if test="${calendarEventForm.ampCalendarId != null && calendarEventForm.ampCalendarId != 0}">
                <digi:form action="/DeleteEvent.do~ampCalendarId=${calendarEventForm.ampCalendarId}">
                  <input type="submit" onclick="return delSubmit();" value="<digi:trn key="calendar:deleteBtn">Delete</digi:trn>">
                </digi:form>
             </c:if>
        </td>
    </tr>
    <tr>
      <td class="r-dotted-lg">&nbsp;</td>
        <td colspan="3">
            <digi:errors/>
            <digi:form action="/previewCalendarEvent.do" onsubmit="selectGuests()">
            <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
            <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
            <table border="0" style="border:1px solid; border-color: #484846;" width="756px">
                <tr>
                    <td colspan="3" nowrap="nowrap"><digi:trn key="calendar:details">&nbsp;Details&nbsp;&nbsp;</digi:trn></td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span><digi:trn key="calendar:eventTitle">Event Title&nbsp;&nbsp</digi:trn></td>
                    <td>
                        <html:text name="calendarEventForm" styleId="eventTitle" property="eventTitle"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span><digi:trn key="calendar:eventType">Event Type&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <html:select name="calendarEventForm" property="selectedEventTypeId">
                            <logic:notEmpty name="calendarEventForm" property="eventTypesList">
                                <bean:define id="types" name="calendarEventForm" property="eventTypesList" type="java.util.List"/>
                                <html:options collection="types" property="id" labelProperty="name"/>
                            </logic:notEmpty>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><br /><digi:trn key="calendar:organizations">&nbsp;Organizations&nbsp;&nbsp;</digi:trn></td>
                    <td>

                      <a title="Facilitates tracking activities in donors' internal databases">
                       <br />
                       <digi:link href="/selectOrganization.do?orgSelReset=true&edit=true" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;">
                        <digi:trn key="calendar:addOrganizations">Add Organizations</digi:trn>
                       </digi:link>
                       &nbsp
                      <a href="#" onclick="return removeSelOrganisations();">
                      <digi:trn key="calendar:deleteOrganisation">Delete Organization</digi:trn>

                      </a>
                        </a>
                        <br /><br />
                        <html:select name="calendarEventForm" property="selectedDonors" multiple="multiple" size="5" styleId="organizationList" style="width: 61%;">
                            <logic:notEmpty name="calendarEventForm" property="donors">
                                <bean:define id="donors" name="calendarEventForm" property="donors" type="java.util.List"/>
                                <html:options collection="donors" property="value" labelProperty="label"/>
                            </logic:notEmpty>
                        </html:select>

                    </td>

                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span><digi:trn key="calendar:from">From</digi:trn></td>
                    <html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
                    <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">

                        <td nowrap="nowrap">
                            <table cellpadding="0" cellspacing="0">
                                <td nowrap="nowrap">
                                    <html:text styleId="selectedStartDate" readonly="true" name="calendarEventForm" property="selectedStartDate" style="width:80px"/>
                                </td>
                                <td>&nbsp;</td>
                                <td>
                	       			<a id="clear1" href="javascript:clearDate(document.getElementById('selectedStartDate'), 'clear1')">
									 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
									</a>
                       				 <a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("selectedStartDate"),"clear1")'>
										<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
									</a>
                                </td>
                                <td>&nbsp;&nbsp;</td>
                                <td>
                                    <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
                                        <c:forEach var="hour" begin="0" end="23">
                                            <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                            <option value="${hour}">${hour}</option>
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
                                            <option value="${minute}">${minute}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
                                    </script>
                                </td>
                            </table>
                        </td>
                    </c:if>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
                        <html:hidden styleId="selectedStartDate" name="calendarEventForm" property="selectedStartDate"/>
                        <td nowrap="nowrap">
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
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>
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
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>${i}</option>
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
                                                <option value="${hour}">${hour}</option>
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
                                                <option value="${minute}">${minute}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
                                        </script>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span><digi:trn key="calendar:to">To</digi:trn></td>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">

                        <td nowrap="nowrap">
                            <table cellpadding="0" cellspacing="0">
                                <td nowrap="nowrap">
                                    <html:text styleId="selectedEndDate" readonly="true" name="calendarEventForm" property="selectedEndDate" style="width:80px"/>
                                </td>
                                <td>&nbsp;</td>
                                <td>
                                <a id="clear2" href="javascript:clearDate(document.getElementById('selectedEndDate'),'clear2')">
									 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
									</a>
                                </td>
                                <td>&nbsp;</td>
                                <td>
                      				<a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("selectedEndDate"),"clear2")'>
										<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
									</a>
                                </td>
                                <td>&nbsp;&nbsp;</td>
                                <td>
                                    <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
                                        <c:forEach var="hour" begin="0" end="23">
                                            <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                            <option value="${hour}">${hour}</option>
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
                                            <option value="${minute}">${minute}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
                                    </script>
                                </td>
                            </table>
                        </td>
                    </c:if>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
                        <html:hidden styleId="selectedEndDate" name="calendarEventForm" property="selectedEndDate"/>
                        <td nowrap="nowrap">
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
                                                <option value="${i}"/>
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
                                                <option value="${i}"/>${i}</option>
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
                                                <option value="${hour}">${hour}</option>
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
                                                <option value="${minute}">${minute}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
                                        </script>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><digi:trn key="calendar:attendees">&nbsp;Attendees&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <table border="0" style="border:1px solid; border-color: #484846;">
                            <tr>
                                <td valign="top">
                                    <table border="0" width="100%">
                                        <tr>
                                            <td nowrap="nowrap"><digi:trn key="calendar:users">&nbsp;Users</digi:trn></td>
                                        </tr>
                                        <tr>
                                            <td>
                                              <select multiple="multiple" size="7" id="usersList">
                                                <c:forEach var="usr" items="${calendarEventForm.attendeeUsers}">
                                                  <option value="${usr.value}">${usr.label}</option>
                                                </c:forEach>
                                               </select>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                  <input type="button" onclick="addUser();" style="font-family:tahoma;font-size:11px;" value="<digi:trn key="calendar:addUser">Add Users >></digi:trn>">
                                </td>
                                <td valign="top">
                                    <table border="0" width="100%" cellpadding="0">
                                        <tr>
                                            <td nowrap="nowrap"><digi:trn key="calendar:guests">&nbsp;Guests</digi:trn></td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap" valign="top">
                                                <table border="0" width="100%">
                                                    <tr>
                                                        <td valign="top">
                                                            <input id="guest" type="text" style="width:100%">
                                                        </td>
                                                        <td valign="top">
                                                            <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="addGuest(document.getElementById('guest'))" value="<digi:trn key="calendar:addGuestBtn">Add Guest</digi:trn>">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">

                                                            <html:select styleId="selectedUsersSel" name="calendarEventForm" property="selectedUsers" multiple="multiple" size="5" style="width:200px">
                                                              <c:if test="${!empty calendarEventForm.selectedUsersList}">
                                                              	<html:optionsCollection name="calendarEventForm" property="selectedUsersList" value="value" label="label" />
                                                              </c:if>
                                                            </html:select>
                                                        </td>
                                                        <td valign="top">
                                                            <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="removeUser()" value="<digi:trn key="calendar:removeBtn">Remove</digi:trn>" >
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top">&nbsp;</td>
                                        
                    <td> 
                    	<html:hidden name="calendarEventForm" property="privateEvent"/>                   	
                        <html:checkbox styleId="PrivateEvents" name="calendarEventForm" property="privateEvent"  onchange="javascript:makePublic();" /><digi:trn key="calendar:eventIsPrivate">&nbsp;Event is private</digi:trn>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>   
                    	<html:hidden name="calendarEventForm" property="ispreview"/>                 	
                        <input type="submit" onclick="return preSubmit();" value="<digi:trn key="calendar:previewBtn">Preview</digi:trn>" />
                    </td>
                </tr>
            </table>
            </digi:form>

