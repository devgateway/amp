<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="calendarEventForm"/>
<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />

<link rel="stylesheet" href="<digi:file src="module/calendar/css/main.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/script/messages.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<script language="JavaScript" type="text/javascript">


function eventType(){
    
        var Dailly = document.getElementById("Dailly").checked;
		var Weekly = document.getElementById("Weekly").checked;
		var Monthly = document.getElementById("Monthly").checked;
		var Yearly = document.getElementById("Yearly").checked;
        

    if(!Dailly && !Weekly && !Monthly && !Yearly){
		alert("please choose");
		return false;
	}

    if(Yearly){
    	var rec = document.getElementById("recurrYearly").value;
		
		document.getElementById("type").value = 'Yearly';
        document.getElementById("hiddenMonth").value = month;
		document.getElementById("hidden").value = rec;
	}
	
	if(Monthly){
   		var rec = document.getElementById("recurrMonthly").value;
		
		document.getElementById("type").value = 'Monthly';
        document.getElementById("hiddenMonth").value = month;
		document.getElementById("hidden").value = rec;
	}

	if(Dailly){
        var rec = document.getElementById("recurrDailly").value;
        

        document.getElementById("hidden").value = rec;
        document.getElementById("type").value = 'Dailly';
    }

	if(Weekly){
        var rec = document.getElementById("recurrWeekly").value;

        document.getElementById("type").value = 'Weekly';
        document.getElementById("hidden").value = rec;
	}
   submit();
}

</script>
<digi:form action="/showCalendarEvent.do">
<table border="0" cellPadding=2 cellSpacing=0 width="100%" >

<tr>
	<td style="font-family: Tahoma;">
	                <div style="padding: 1px;">
	                    <div style="padding:7px;text-align:center;background-color: #336699; font-size: 18px;color:white; font-weight: bold;">
	                        <digi:trn key="calendar:recurrEvent">Reccuring Event Setup</digi:trn>
	                    </div>
	                </div>
	</td>
</tr>
	<tr>
		<td>
		 <table border="0" cellpadding="10" width="100%" style="border-style:solid;border-color:#1C5180;border-width: 1px" >
		 	<tr>
		 		<td rowspan="2" >
			 		<table  border="0" width="100%">
					 		<tr bgcolor="white">
					 			<td colspan="3">
					 				<input type="radio" name="typeofOccurrence" value="Dailly" id="Dailly"/>Dailly
					 			</td>
					 		</tr>
					 		<tr>
					 			<td>
					 			 <table bgcolor="#F5F5F5" align="center" width="320px" cellpadding="7"  style="border-style:solid;border-color:#1C5180;border-width: 1px">
					 			 	<tr>
							 		 	<td width="12px">Every</td>
							 	 		<td width="12px">
							 	 			<input type="text"  size="7px" name="recurrPeriod" id="recurrDailly" value=""/>
							 	 		</td>
							 	 		<td align="">Day(s)</td>
					 			 	</tr>
					 			 </table>
					 			</td>
					 		</tr>
					 		<tr><td height="20px">&nbsp;</td></tr>
					 		<tr>
							 	<td bgcolor="white">
							 			<table border="0" cellPadding=1 cellSpacing=1>
									 		<tr bgcolor="white">
										 		<td colspan="5">
										 			<input type="radio"  name="typeofOccurrence" value="Monthly" id="Monthly"/>Monthly
										 		</td>
									 		</tr>
							 		 </table>
								 </td>
							   </tr>
					 		<tr>
				 			<td>
				 				<table bgcolor="#F5F5F5" cellpadding="3" align="center" width="320px" style="border-style:solid;border-color:#1C5180;border-width: 1px">
				 					
				 					<tr>
							 	 		<td width="95px">Recover Every Day</td>
							 	 		<td><input type="text" size="9px" name="recurrPeriod" id="recurrMonthly" value=""/></td>
							 	 	</tr>
							 	 	<tr>	
							 	 		<td width="95px">Off Every</td>
							 	 		<td width="12px">
								 	 		<select id="selectedStartMonth" name="selectedStartMonth">
												    <c:forEach var="hour" begin="1" end="12">
			                                                     	<option value="${hour}">${hour}</option>
			                                        </c:forEach>		
											</select>
										</td>
										<td align="left">Month(s)</td>
					 	 			</tr>
					 	 			
					 	 		</table>
					 	 	</td>	
				 		</tr>
				 		<tr><td height="40px">&nbsp;</td></tr>
			 		</table>
		 		</td>
			 		<td>
				 		<table  bgcolor="#F5F5F5">
						 		<tr bgcolor="white">
						 			<td><input type="radio" name="typeofOccurrence" value="Weekly" id="Weekly"/>Weekly</td>
						 			<td></td>
						 			<td></td>
						 		</tr>
								<tr>
									<td>
									  <table cellpadding="2" width="240px" style="border-style:solid;border-color:#1C5180;border-width: 1px">		
									 		<tr>
									 	 		<td>Recurr every</td>
									 	 		<td><input type="text"  size="7px" name="recurrPeriod" id="recurrWeekly" value=""/></td>
									 	 		<td>Week (s)</td>
									 		</tr>
									 		<tr> 
									 			<td><input type="checkbox" name="occurrWeekDays" value="Sun"/>Sun</td>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Wednesday"/>Wed</td>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Saturday"/>Saturday</td>
									 		</tr>
									 		<tr>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Monday"/>Monday</td>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Thur"/>Thur</td>
									 			<td>
									 		</tr>
									 		<tr>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Tuesday"/>Tuesday</td>
									 			<td><input type="checkbox" name="occurrWeekDays" value="Friday"/>Friday</td>
									 		</tr>
								 		</table>
						 			</td>
						 		</tr>
				 		</table>
			 		 </td>
		 		   <td>
		 		 </td>
		 	</tr>
		 	
		 	    <td>
			 	    <table border="0" bgcolor="#F5F5F5" >
					 		<tr bgcolor="white">
					 			<td colspan="4">
					 				<input type="radio" name="typeofOccurrence" value="Yearly" id="Yearly"/>Yearly
					 			</td>
					 		</tr>
					 		<tr>
					 			<td>
					 				<table cellpadding="6" width="240px" style="border-style:solid;border-color:#1C5180;border-width: 1px">
					 				 	<tr>
					 						<td>Every</td>
							 	 			<td>
								 	 			<select id="selectedStartYearlyMonth" name="selectedStartMonth">
														<logic:iterate id="month" property="months" name="calendarEventForm">
																<option value="${month}">${month}</option>
														</logic:iterate>		
												</select>
						 	 		    	</td>
							 	 			<td><input type="text"  size="7px" name="recurrPeriod" id="recurrYearly" value=""/></td>
							 	 			<td> Day(s) </td>
							 	 	    </tr>
							  	   </table>
							  </td>
				 		  </tr>
			 		</table>
			 	</td>
		 
		 </table>
		</td>
	</tr>

<!--
<tr>
		<td>
			<table bgcolor="#F5F5F5" border="0" cellPadding=2 cellSpacing=2 width="340px" style="border-style:solid;border-color:#1C5180;border-width: 1px">
				 		<tr><td>Time</td></tr>
				 		<tr>
				 	 		<td>Start TIme</td>
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
				 	 		
				 	 		<td>End TIme</td>
				 	 		<td>
				 	 			<select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
                                                    <c:forEach var="hour" begin="0" end="23">
	                                                      <c:if test="${hour < 10}">
	                                                      	<c:set var="hour" value="0${hour}"/>
	                                                      </c:if>
	                                                      	<option value="${hour}">${hour}</option>
                                                    </c:forEach>
                                                  </select>
                                <script type="text/javascript">
                                   selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
                                </script>
							</td>
				 	 	</tr>
				 	 	<tr>
				 	 		<td>
				 	 			<digi:trn key="calendar:StartDate">Start date:</digi:trn>
				 	 		</td>
				 	 		 <td>
				 		 	 	<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
		                                  <html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
		                                  <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
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
		                                          </tr>
		                                      </table>
		                        	</c:if>
		 	                 	</td>
						 	 </tr>
				 	 	
				 	 	<tr>
				 	 		<td><digi:trn key="calendar:EndDate">End Date</digi:trn></td>
				 	 		<td>
			 	 				<c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
                                     <table cellpadding="0" cellspacing="0">
                                           <tr>
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
                                      </table>
                                 </c:if>
				 	 		</td>
				 	 		
				 	 	</tr>
			 	</table>
		</td>	
	</tr>  -->
	<tr>
		<td align="center">
			<input type="button" onclick="eventType();" value="Save An CLose"/>
		</td>
	</tr>
</table>
</digi:form>