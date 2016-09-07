<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="calendarEventForm"/>

<style  type="text/css">
<!--

.contentbox_border {
    border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}
-->
</style>

<script language="javascript">

function deleteEvent(){
	var err = '<digi:trn jsFriendly="true">Are You sure?</digi:trn>';
	if(confirm(err))
	{
		document.getElementById('hdnMethod').value = "delete";
		document.calendarEventForm.submit();
		return true;
	} else {
		return false;
	}
	
}


function openPrinter(){
	var id = document.getElementById('id').value;
	var href="/calendar/showCalendarEvent.do~method=print~resetForm=true~calendarId="+id;
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){
		var popupName = "popup"+new Date().getTime();
	 	var popupWindow =  window.open('',popupName,'toolbar=no,location=no, width=540,height=500, directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');    
	 	var referLink = document.createElement('a');
	 	referLink.href = href;
	 	referLink.target = popupName;
	 	document.body.appendChild(referLink);
	 	referLink.click();
	 } else {
		 window.open(href,'mywindow','toolbar=no,location=no, width=540,height=500, directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
	 }
}

function getWeekdays(){
   var weekDays = document.getElementById('weekDays').value;
   var myDays = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
   var result = "";
   var days = weekDays.split("",weekDays.length);
    for(i=0; i<weekDays.length; i++){
    
    	var wday = myDays[days[i]];
    	result +=wday+",";  	
        }
    return result;
 }

function valid(value){
	var err = '<digi:trn jsFriendly="true">Are You sure?</digi:trn>';
 	if(confirm(err)){
 		if (value){
 	 		document.getElementById('hdnValid').value = 1;
 	 	} else {
 	 	 	document.getElementById('hdnValid').value = -1;
 	 	}
 	 	document.getElementById('hdnMethod').value = "valid";
 	 	document.getElementById('eventForm').submit();
 	 	return true;
 	 }
 	 return false;
}
function editEvent(){
         document.getElementById('hdnMethod').value = '';
         var allOptions = document.getElementById('selreceivers').options;
         for(var i=0;i<allOptions.length;i++){
             allOptions[i].selected=true;
         }
 	 return true;
}

</script>

<digi:form styleId="eventForm" action="/showCalendarEvent.do">

  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
  <html:hidden styleId="hdnValid" name="calendarEventForm" property="approve"/>
  <html:hidden styleId="id" name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>

  <table width="530">
  	 <tr>
		<td width=14>&nbsp;</td>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
		<td height=33>
			<span class=crumb>&nbsp;
				<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
				</c:set>
				<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
					<digi:trn key="aim:portfolio">Portfolio</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<digi:link href="/../calendar/showCalendarView.do" styleClass="comment" title="${translation}">
					<digi:trn key="calendar:Calendar">Calendar</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<digi:trn key="calendar:previewEvent">Preview Event</digi:trn>
			</span>
		</td>
	</tr>	
	<tr>
		<td height="16" vAlign="middle" width="520">
			<span class=subtitle-blue>	<digi:trn key="calendar:previewEvent">Preview  Event</digi:trn> </span>
		</td>
	</tr>
	<tr>				
        <td align="center" nowrap="nowrap" valign="top">
        	<table style="border: 1px solid #CCCCCC;background:#fff;" width="100%" cellpadding="1" cellspacing="1">
                <tr>
                	<td align="center" style="padding: 0px 3px 0px 3px;">
                		<table width="100%">
			              	<tr>
			                   	<td  style="height: 5px;"/>
			                 </tr>
			                 <tr>
								<td style="background-color: #CCDBFF;height: 18px;">
				               	 	<feature:display name="Event Approve" module="Calendar">
                                                        <c:if test="${calendarEventForm.approve==0||calendarEventForm.approve==2}"><digi:trn>Awaiting Validation</digi:trn></c:if>
                                                        <c:if test="${calendarEventForm.approve==-1}"><digi:trn>Event not Approved</digi:trn></c:if>
	 	 	 	 				    </feature:display>
 	 	 	 		            </td>
			                 </tr>
			            </table>
                	</td>                	
                </tr>
                <!-- calendar event always has a title -->
                <logic:empty name="calendarEventForm" property="eventTitle">
                	<tr style="height: 50px;">
                		<td style="text-align: center;font-family: Arial;font-size: 15px;font-weight:bold;color: grey;background-color: #F5F5F5;">
                			<digi:trn>The Event Doesn't Exist Any More </digi:trn>
                		</td>
                	</tr>
                </logic:empty>
				<logic:notEmpty name="calendarEventForm" property="eventTitle">
					<tr>
				      <td style="font-family: Arial; font-size: 12px;">        
				        <div style="padding: 20px;">
				          <table>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:evntTitle">Event title</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size: 11px;">
				                <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
				                ${calendarEventForm.eventTitle}
				              </td>
				            </tr>
				           <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Description">Description</digi:trn>:
				              </td>
				              <td>
				              	<html:textarea name="calendarEventForm" property="description" style="width: 220px;" readonly="true"/>                
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:cType">Calendar type</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size: 11px;">
				                <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
				                        Gregorian                     
				                  </c:if>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 1}">
				                        Ethiopian                     
				                  </c:if>
				                  <c:if test="${calendarEventForm.selectedCalendarTypeId == 2}">
				                        Ethiopian_FY                     
				                  </c:if>
				              </td>
				             </tr>
				             <tr height="3px"><td colspan="2"></td></tr>
				            <feature:display name="Event Type" module="Calendar">
				            	<tr>
					              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="calendar:eventsType">Event type</digi:trn>:
					              </td>
					              <td style="font-family: Arial;font-size: 11px;">
					                <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
					                 <digi:trn>${calendarEventForm.selectedEventTypeName}</digi:trn> 
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <feature:display name="Donors" module="Calendar">
				            	<tr>
					              <td style="text-align: right;font-family: Arial;font-size: 12px; font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="cal:organizations">Organizations</digi:trn>:
					              </td>
					              <td>
					                <html:select name="calendarEventForm" property="selOrganizations" multiple="multiple" size="5" styleId="organizationList" style="width: 220px; height: 70px;">
					                    <html:optionsCollection name="calendarEventForm" property="organizations" value="ampOrgId" label="acronymAndName" style="font-family: Arial;font-size:11px;"/>
					                </html:select>
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:StDate">Start date</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size: 11px;">
				                <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
				                ${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:EndDate">End Date</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size: 11px;">
				                <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
				                ${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Attendee">Attendee</digi:trn>:
				              </td>
				              <td>
				                <html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width: 220px; height: 70px;">
				                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                                      <c:forEach var="attendee" items="${calendarEventForm.selectedAttsCol}">
                                                          <c:if test="${fn:startsWith(attendee.value, 'g:')==false}">
                                                          <html:option value="${attendee.value}">${attendee.label}</html:option>
                                                          </c:if>
                                                      </c:forEach>
				                    
				                  </c:if>
				                </html:select>
				              </td>
				            </tr>
                                             <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn>Additional Receivers</digi:trn>:
				              </td>
				              <td> 
				                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                                      <div class="msg_added_cont">
                                                      <c:forEach var="attendee" items="${calendarEventForm.selectedAttsCol}">
                                                          <c:if test="${fn:startsWith(attendee.value, 'g:')}">${attendee.label}
                                                            <input name="selectedAtts" class="guest_contact_hidden" type="hidden" value="g:${attendee.value}"><br/>
                                                          </c:if>
                                                      </c:forEach>
                                                      </div>
				                    
				                  </c:if>
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size: 11px;">
				                <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
				                <c:if test="${calendarEventForm.privateEvent}"><digi:trn key="calendar:no">No</digi:trn></c:if>
				                <c:if test="${!calendarEventForm.privateEvent}"><digi:trn key="calendar:yes">Yes</digi:trn></c:if>
				              </td>
				            </tr>
				            <c:if test="${not empty calendarEventForm.typeofOccurrence}">
					            <tr height="3px"><td colspan="2"></td></tr>
					            <tr>
					              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
					                <digi:trn>Recurring Event</digi:trn>:
					              </td>
					              <td style="font-family: Arial;font-size: 12px;">
					         		<html:hidden name="calendarEventForm" property="typeofOccurrence" value="${calendarEventForm.typeofOccurrence}"/>
									<html:hidden name="calendarEventForm" property="recurrPeriod" value="${calendarEventForm.recurrPeriod}"/>
									<html:hidden name="calendarEventForm" property="occurrWeekDays" value="${calendarEventForm.occurrWeekDays}"/>
				                	<html:hidden name="calendarEventForm" property="selectedStartMonth" value="${calendarEventForm.selectedStartMonth}"/>
									<html:hidden name="calendarEventForm" property="selectedStartYear" value="${calendarEventForm.selectedStartYear}"/>
									<html:hidden name="calendarEventForm" property="recurrStartDate" value="${calendarEventForm.recurrStartDate}"/>
				                each 
					               ${calendarEventForm.recurrPeriod} 
					               ${calendarEventForm.typeofOccurrence}
					               <input type="hidden" value="${calendarEventForm.occurrWeekDays}" id="weekDays"/>
					               <script language="javascript">
					               document.write(getWeekdays());
						             </script>
					                 <!--<c:if test="${calendarEventForm.selectedStartMonth != 0}"> ${calendarEventForm.selectedStartMonth}</c:if>
					                  <c:if test="${calendarEventForm.selectedStartYear != 0}"> ${calendarEventForm.selectedStartYear}</c:if>
					                 <c:if test="${calendarEventForm.recurrStartDate != 0}">${calendarEventForm.recurrStartDate}</c:if>
					                  
					              --></td>
					            </tr>
								<tr height="3px"><td colspan="2"></td></tr>
					            <tr>
					            <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
					                <digi:trn>Recurring Event End Date</digi:trn>:
					              </td>
					               <td style="font-family: Arial;font-size: 12px;">
				                		<html:hidden name="calendarEventForm" property="recurrEndDate" value="${calendarEventForm.recurrEndDate}"/>
				                		<html:hidden name="calendarEventForm" property="recurrSelectedEndTime" value="${calendarEventForm.recurrSelectedEndTime}"/>
				                		<c:if test="${not empty calendarEventForm.recurrEndDate}">${calendarEventForm.recurrEndDate}</c:if>
					               </td>
					            </tr>
				            </c:if>
				            <tr height="5px"><td colspan="2">&nbsp;</td></tr>
				            <c:if test="${calendarEventForm.actionButtonsVisible!=false}">
								<tr>
					              	<td>
					              	</td>
					              	<td>
					                	<input class="buttonx" type="submit" style="width: 110px;" value="<digi:trn>Save</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'save'">
					                	&nbsp;
					                	<input class="buttonx" type="submit" style="width: 110px;" value="<digi:trn>Edit</digi:trn>" onclick="editEvent()">
									</td>
								</tr>
								<tr height="3px"><td colspan="2"></td></tr>
                                <c:if test="${not empty calendarEventForm.ampCalendarId&&calendarEventForm.ampCalendarId!=0}" >
								<tr>
					              	<td>
					              	</td>
									<td>
					                	<input class="buttonx" type="button" value="<digi:trn>Delete</digi:trn>" style="width: 110px;" onclick="deleteEvent();" />
										&nbsp;
										<input class="buttonx" type="button" value="<digi:trn>Print</digi:trn>" style="width: 110px;" onclick="openPrinter();" />
									</td>
								</tr>
                                </c:if>
				            </c:if>
							<c:if test="${sessionScope.currentMember.teamHead==true && (calendarEventForm.approve==0||calendarEventForm.approve==2)&&(not empty calendarEventForm.ampCalendarId&&calendarEventForm.ampCalendarId!=0)}">
 	 	 	 					<tr>
					              	<td>
					              	</td>
									<td>
					               		<feature:display name="Event Approve" module="Calendar">
		 	 	 	 						<input type="button" value="<digi:trn>Approved</digi:trn>" class="buttonx" style="width: 110px;" onclick="valid(true);" />
		 	 	 	 						&nbsp;
											<input type="button" value="<digi:trn>Not Approved</digi:trn>" class="buttonx" style="width: 110px;" onclick="valid(false);" />
 	 	 	 							</feature:display>
 	 	 	 						</td>
								</tr>
				            </c:if>
				           	<c:if test="${calendarEventForm.actionButtonsVisible==false}">
				                <tr>
					              	<td>
					              	</td>
					              	<td>
				                		<input class="buttonx" type="submit" style="width: 110px;" value="<digi:trn>OK</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'OK'">
				                		&nbsp;
										<input class="buttonx" type="button" value="<digi:trn>Print</digi:trn>" style="width: 110px;" onclick="openPrinter();" />
									</td>
								</tr>
				            </c:if>
				          </table>
				        </div>
				      </td>
				    </tr>
				</logic:notEmpty>
             </table>
       		</td>
    	</tr>
    </table>
	</td>
</tr>
</table>
</digi:form>