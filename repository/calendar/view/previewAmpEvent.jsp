<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
	if(confirm('Are You sure?'))
	{
		document.getElementById('hdnMethod').value = "delete";
		return true;
	}
	return false;
}
</script>

<digi:form action="/showCalendarEvent.do">

  <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
  <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>

  <table>
  	 <tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
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
		<td height=16 vAlign=center width=571>
			<span class=subtitle-blue>	<digi:trn key="calendar:previewEvent">Preview  Event</digi:trn> </span>
		</td>
	</tr>
	<tr>				
        <td align="center" nowrap="nowrap" valign="top">
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
			      <td style="font-family: Tahoma; font-size: 12px;">        
			        <div style="padding: 20px; background-color: #F5F5F5;">
			          <table>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;" nowrap="nowrap">
			                <digi:trn key="calendar:evntTitle">Event title</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
			                <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
			                ${calendarEventForm.eventTitle}
			              </td>
			            </tr>
			           <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;" nowrap="nowrap">
			                <digi:trn key="calendar:Description">Description</digi:trn>
			              </td>
			              <td>
			              	<html:textarea name="calendarEventForm" property="description" style="width: 220px;" readonly="true"/>                
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;"nowrap="nowrap">
			                <digi:trn key="calendar:cType">Calendar type</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
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
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;"nowrap="nowrap">
			                <digi:trn key="calendar:eventsType">Event type</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
			                <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
			                ${calendarEventForm.selectedEventTypeName}
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;"nowrap="nowrap">
			                <digi:trn key="calendar:orgs">Organizations</digi:trn>
			              </td>
			              <td>
			                <html:select name="calendarEventForm" property="selOrganizations" multiple="multiple" size="5" styleId="organizationList" style="width: 220px; height: 70px;">
			                    <html:optionsCollection name="calendarEventForm" property="organizations" value="ampOrgId" label="acronymAndName" style="font-family: Tahoma;font-size:11px;"/>
			                </html:select>
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;"nowrap="nowrap">
			                <digi:trn key="calendar:StDate">Start date</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
			                <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
			                <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
			                ${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;"nowrap="nowrap">
			                <digi:trn key="calendar:EndDate">End Date</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
			                <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
			                <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
			                ${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;" nowrap="nowrap">
			                <digi:trn key="calendar:Attendee">Attendee</digi:trn>
			              </td>
			              <td>
			                <html:select multiple="multiple" styleId="selreceivers" name="calendarEventForm" property="selectedAtts" size="11" styleClass="inp-text" style="width: 220px; height: 70px;">
			                  <c:if test="${!empty calendarEventForm.selectedAttsCol}">
			                    <html:optionsCollection name="calendarEventForm" property="selectedAttsCol" value="value" label="label" style="font-family: Tahoma;font-size:11px;"/>
			                  </c:if>
			                </html:select>
			              </td>
			            </tr>
			            <tr height="3px"><td colspan="2"></td></tr>
			            <tr>
			              <td style="text-align: right;font-family: Tahoma;font-size: 12px;" nowrap="nowrap">
			                <digi:trn key="calendar:PublicEvent">Public Event</digi:trn>
			              </td>
			              <td style="font-family: Tahoma;font-size: 12px;">
			                <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
			                <c:if test="${calendarEventForm.privateEvent}"><digi:trn key="calendar:no">No</digi:trn></c:if>
			                <c:if test="${!calendarEventForm.privateEvent}"><digi:trn key="calendar:yes">Yes</digi:trn></c:if>
			              </td>
			            </tr>
			            <tr height="5px"><td colspan="2">&nbsp;</td></tr>
			            <tr>
			              <td>
			              </td>
			              <td>
			                <input type="submit" style="width: 100px;" value="<digi:trn key="calendar:savebutton">Save</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'save'">
			                &nbsp;
			                <input type="submit" style="width: 100px;" value="<digi:trn key="calendar:editbutton">Edit</digi:trn>" onclick="document.getElementById('hdnMethod').value = ''">
			                &nbsp;
			                <input type="submit" value="<digi:trn key="calendar:deletebutton">Delete</digi:trn>" style="width: 100px;" onclick="deleteEvent();" />
			              </td>
			            </tr>
			          </table>
			        </div>
			      </td>
			    </tr>
             </table>
       		</td>
    	</tr>
    </table>
	</td>
</tr>
</table>
</digi:form>

