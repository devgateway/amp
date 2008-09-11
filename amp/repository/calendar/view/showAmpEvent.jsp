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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
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

  function setMethod(mth){
    var h=document.getElementById("hdnMethod");
    if(h!=null){
      h.value=mth;
    }
  }

  </script>


<digi:form action="/showCalendarEvent.do">
  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>

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
                      <td>
                        <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
                        <html:select name="calendarEventForm" property="selectedCalendarTypeId" onchange="this.form.submit()">
                          <c:if test="${!empty calendarEventForm.calendarTypes}">
                            <c:set var="types" value="${calendarEventForm.calendarTypes}"/>
                            <html:options collection="types" property="value" labelProperty="label"/>
                          </c:if>
                        </html:select>
                      </td>
                  </tr>
                  </table>
              </td>
            </tr>
          </table>
        </td>
        <td align="right">
          <c:if test="${calendarEventForm.ampCalendarId != null && calendarEventForm.ampCalendarId != 0}">
              <input type="submit" onclick="return deleteEvent();" value="<digi:trn key="calendar:deleteBtn">Delete</digi:trn>">
          </c:if>
        </td>
      </tr>
      <tr>
        <td class="r-dotted-lg">&nbsp;</td>
        <td colspan="3">
          <digi:errors/>
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
                    <c:if test="${!empty calendarEventForm.eventTypesList}">
                      <html:optionsCollection name="calendarEventForm" property="eventTypesList" value="id" label="name" />
                    </c:if>
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
                    <a href="javascript:removeSelectedElements('organizationList');">
                      <digi:trn key="calendar:deleteOrganisation">Delete Organization</digi:trn>
                    </a>
                  </a>
                  <br /><br />
                  <html:select name="calendarEventForm" property="selectedEventOrganisations" multiple="multiple" size="5" styleId="organizationList" style="width: 61%;">
                    <c:if test="${!empty calendarEventForm.selectedEventOrganisationsCol}">
                      <html:optionsCollection name="calendarEventForm" property="selectedEventOrganisationsCol" value="value" label="label" />
                    </c:if>
                  </html:select>
                </td>
              </tr>
              <tr>
                <td nowrap="nowrap">&nbsp;<span class="redbold">*</span><digi:trn key="calendar:from">From</digi:trn></td>
                <html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
                <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
                <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
                  <td nowrap="nowrap">
                    <table cellpadding="0" cellspacing="0">
                      <tr>
                        <td nowrap="nowrap">
                          <html:text styleId="selectedStartDate" readonly="true" name="calendarEventForm" property="selectedStartDate" style="width:80px"/>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                          <a id="clear1" href="javascript:clearDate(document.getElementById('selectedStartDate'), 'clear1')">
                            <digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
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
                      </tr>
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
                      <td>
                        <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
                          <c:forEach var="hour" begin="0" end="23">
                            <c:if test="${hour < 10}">
                              <c:set var="hour" value="0${hour}"/>
                            </c:if>
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
                              <select multiple="multiple" size="13" id="whoIsReceiver" class="inp-text" style="width:200px">
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
                          </tr>
                        </table>
                      </td>
                      <td>
                        <input type="button" onclick="MyaddUserOrTeam();" style="font-family:tahoma;font-size:11px;" value="<digi:trn key="calendar:addUser">Add Users >></digi:trn>">
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
                                    <html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width:200px">
                                      <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                        <html:optionsCollection name="calendarEventForm" property="selectedAttsCol" value="value" label="label" />
                                      </c:if>
                                    </html:select>
                                  </td>
                                  <td valign="top">
                                    <input type="button" style="width:80px;font-family:tahoma;font-size:11px;" onclick="MyremoveUserOrTeam()" value="<digi:trn key="calendar:removeBtn">Remove</digi:trn>" >
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
                  <input type="submit" onclick="return previewEvent();" value="<digi:trn key="calendar:previewBtn">Preview</digi:trn>" />
                </td>
              </tr>
            </table>
          </digi:form>

