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

@media print {
    .noPrint{
        display: none;
    }
}
-->
</style>

<script language="javascript">

function deleteEvent(){
	var err = '<digi:trn jsFriendly="true">Are You sure?</digi:trn>';
	if(confirm(err))
	{
		document.getElementById('hdnMethod').value = "delete";
		return true;
	}
	return false;
}


</script>

<digi:form action="/showCalendarEvent.do">

  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
  <html:hidden styleId="id" name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>

  <table width="100%">
      <tr>
          <td colspan="2">
              <div class="noPrint">
                  <a target="_blank"  title="Printing" onclick="window.print();" style="cursor: pointer">
                      <img width="20" vspace="2" hspace="2" height="30" border="0" alt="Printer Friendly" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif"/>
                  </a>
                  <a target="_blank"   title="Printing" onclick="window.close();" style="cursor: pointer;" >
                      <digi:img src="module/aim/images/close.gif" border="0" alt="Close" height="30"/>
                  </a>
              </div>
          </td>
      </tr>
    <tr>
        <td colspan="2" align="center" vAlign="middle" style="font-family: Arial;font-size: 16px; font-weight:bold;">
			<digi:trn key="calendar:previewEvent">Preview  Event</digi:trn>
		</td>
	</tr>
	<tr>				
        <td align="center" nowrap="nowrap" valign="top">
        	<table  width="100%" cellpadding="0" cellspacing="0">

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
				          <table align="center" cellpadding="2" cellspacing="5">
<tr>
<td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:evntTitle">Creator Member</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
				                 ${calendarEventForm.eventCreator}
				              </td>
</tr>
				            <tr>
				            
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:evntTitle">Event title</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
				                ${calendarEventForm.eventTitle}
				             
				              </td>
				            </tr>
				           <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Description">Description</digi:trn>
				              </td>
				              <td>
				              	${calendarEventForm.description}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:cType">Calendar type</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
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
					              <td style="font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="calendar:eventsType">Event type</digi:trn>
					              </td>
					              <td style="font-family: Arial;font-size: 12px;">
					                <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
					                <digi:trn>${calendarEventForm.selectedEventTypeName}</digi:trn>
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <feature:display name="Donors" module="Calendar">
				            	<tr>
					              <td style="font-family: Arial;font-size: 12px; font-weight:bold;"nowrap="nowrap">
					                <digi:trn key="cal:organizations">Organizations</digi:trn>
					              </td>
					              <td style="font-family: Arial;font-size: 12px; ">
                                      <c:if test="${not empty calendarEventForm.organizations}">
                                          <ul>
                                              <c:forEach var="organization" items="${calendarEventForm.organizations}">
                                                  <li>${organization.acronymAndName}</li>
                                              </c:forEach>
                                          </ul>
                                      </c:if>
					              </td>
					            </tr>
					            <tr height="3px"><td colspan="2"></td></tr>
				            </feature:display>			            
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:StDate">Start date</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
				                ${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;"nowrap="nowrap">
				                <digi:trn key="calendar:EndDate">End Date</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
				                <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
				                ${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:Attendee">Attendee</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size:11px;">
                                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                                      <c:forEach var="attendee" items="${calendarEventForm.selectedAttsCol}">
                                                          <c:if test="${fn:startsWith(attendee.value, 'g:')==false}">
                                                              ${attendee.label}<br/>
                                                          </c:if>
                                                      </c:forEach>      
				                  </c:if>
				              </td>
				            </tr>
                                            <tr>
				              <td style="text-align: right;font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn>Additional Receivers</digi:trn>:
				              </td>
				              <td style="font-family: Arial;font-size:11px;"> 
				                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                                      <c:forEach var="attendee" items="${calendarEventForm.selectedAttsCol}">
                                                          <c:if test="${fn:startsWith(attendee.value, 'g:')}">
                                                              ${attendee.label}<br/>
                                                          </c:if>
                                                      </c:forEach>      
				                  </c:if>
				              </td>
				            </tr>
				            <tr height="3px"><td colspan="2"></td></tr>
				            <tr>
				              <td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
				                <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>
				              </td>
				              <td style="font-family: Arial;font-size: 12px;">
				                <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
				                <c:if test="${calendarEventForm.privateEvent}"><digi:trn key="calendar:no">No</digi:trn></c:if>
				                <c:if test="${!calendarEventForm.privateEvent}"><digi:trn key="calendar:yes">Yes</digi:trn></c:if>
				              </td>
				            </tr>
				            <c:if test="${calendarEventForm.typeofOccurrence != null}">
					            <tr height="5px"><td colspan="2"></td></tr>
					            <tr>
					              <td style="font-family: Arial;font-size: 12px;font-weight:bold;" nowrap="nowrap">
					                <digi:trn>Recurring Event</digi:trn>
					              </td>
					              <td style="font-family: Arial;font-size: 12px;">
					                ${calendarEventForm.typeofOccurrence}
					                 ${calendarEventForm.weekDays}
					                 <c:if test="${calendarEventForm.selectedStartMonth != 0}"> ${calendarEventForm.selectedStartMonth}</c:if>
					                  <c:if test="${calendarEventForm.selectedStartYear != 0}"> ${calendarEventForm.selectedStartYear}</c:if>
					                 <c:if test="${calendarEventForm.recurrPeriod != 0}">${calendarEventForm.recurrPeriod}</c:if>
					                 <c:if test="${calendarEventForm.recurrStartDate != 0}">${calendarEventForm.recurrStartDate}</c:if>
					                 <c:if test="${calendarEventForm.recurrEndDate != 0}">${calendarEventForm.recurrStartDate}</c:if>
					                  
					              </td>
					            </tr>
				            </c:if>
				            <tr height="5px"><td colspan="2">&nbsp;</td></tr>
				            <tr>
				              <td>
				              </td>
				              <td>
				              
				              </td>
				            </tr>
				          </table>
				      </td>
				    </tr>
				</logic:notEmpty>
             </table>
       		</td>
    	</tr>
    	
    </table>
</digi:form>