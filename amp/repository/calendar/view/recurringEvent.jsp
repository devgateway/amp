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
	
	if(Yearly == true){
		var month = document.getElementById("selectedStartYearlyMonth").value;
		var rec = document.getElementById("recurrYearly").value;
		
		document.getElementById("hiddenMonth").value = month;
		document.getElementById("hidden").value = rec;
	}
	
	if(Monthly == true){
		var month = document.getElementById("selectedStartMonth").value;
		var rec = document.getElementById("recurrMonthly").value;
		
		document.getElementById("hiddenMonth").value = month;
		document.getElementById("hidden").value = rec;
	}

	if(Dailly == true){
	  	var rec = document.getElementById("recurrDailly").value;
	 	document.getElementById("hidden").value = rec;
	}

	if(Weekly == true){
		var rec = document.getElementById("recurrWeekly").value;
		document.getElementById("hidden").value = rec;
	}
	window.close();
}

function expandProgram(progId){
 	var imgId='#img_'+progId;
 	var Id='img_'+progId;
	var display  = document.getElementById(Id).style.display;

		if(display == "none"){
				$(imgId).show();
			}else{
				$(imgId).hide();
			}
		
}       
	
	

</script>
<digi:form action="/showCalendarEvent.do">
<input type="hidden" name="selectedStartMonth" id="hiddenMonth"/>
<input type="hidden" name="recurrPeriod" id="hidden"/>
<table border="0" cellPadding=4 cellSpacing=3 width="100%">
<tr>
	<td style="font-family: Tahoma;">
	                <div style="padding-bottom: 20px;">
	                    <div style="padding:7px;text-align:center;background-color: #27415F; font-size: 18px;color:white; font-weight: bold;">
	                        <digi:trn key="calendar:recurrEvent">Reccuring Event Setup</digi:trn>
	                    </div>
	                </div>
	</td>
	
	 <tr onclick="expandProgram('1');">
	 	<td style="font-family: Tahoma;font-family: Tahoma;font-size: 10px;color:white; font-weight: bold; border: 1px;border-bottom-style:solid" bgcolor="#336699">Dailly</td>
	 </tr>
	 <tr>
	 	<td style="display:none;" id="img_1">
			 <div style="padding-bottom: 20px; background-color:#F5F5F5; width:400px;border-color;blue; border-style:solid;border-width:1px" >
			 		<table border="0" cellPadding=2 cellSpacing=2>
				 		<tr><td><input type="radio" name="typeofOccurrence" value="Dailly" id="Dailly"/>Dailly</td></tr>
					 		<tr>
					 	 		<td>Every</td>
					 	 		<td><input type="text" name="recurrPeriod" id="recurrDailly" value=""/></td>
					 	 		<td>Day(s)</td>
					 		</tr>
			 		</table>
			 	</div>	
		 		</td>
	 </tr>
	 <tr onclick="expandProgram('2');">
	 	<td style="font-family: Tahoma;font-size: 10px;color:white; font-weight: bold; border: 1px;border-bottom-style:solid;" bgcolor="#336699">Weekly</td>
	 </tr>
	 <tr>
	 		<td  bgcolor="white" style="display:none;" id="img_2">
	 			 <div style="padding-bottom: 20px; background-color:#F5F5F5; width:400px;border-color;blue; border-style:solid;border-width:1px" >
				 		<table border="0" cellPadding=2 cellSpacing=2>
						 		<tr><td><input type="radio" name="typeofOccurrence" value="Weekly" id="Weekly"/>Weekly</td></tr>
						 		<tr>
						 	 		<td>Recurr every</td>
						 	 		<td><input type="text" name="recurrPeriod" id="recurrWeekly" value=""/></td>
						 	 		<td>Week (s)</td>
						 		</tr>
						 		<tr> 
						 			<td><input type="checkbox" name="occurrWeekDays" value="Sun"/>Sun</td>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Wednesday"/>Wednesday</td>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Saturday"/>Saturday</td>
						 		</tr>
						 		<tr>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Monday"/>Monday</td>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Thur"/>Thur</td>
						 		</tr>
						 		<tr>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Tuesday"/>Tuesday</td>
						 			<td><input type="checkbox" name="occurrWeekDays" value="Friday"/>Friday</td>
						 		</tr>
				 		</table>
			 		</div>
			 		</td>
	 </tr>
	 <tr onclick="expandProgram('3');">
	 	<td style="font-family: Tahoma;font-family: Tahoma;font-size: 10px;color:white; font-weight: bold; border: 1px;border-bottom-style:solid;" bgcolor="#336699" >Monthly</td>
	 </tr>
	 <tr>
	 	<td bgcolor="white" style="display:none;" id="img_3">
	 		<div style="padding-bottom: 20px; background-color:#F5F5F5; width:400px;border-color;blue; border-style:solid;border-width:1px" >
		 			<table border="0" cellPadding=2 cellSpacing=2>
				 		<tr><input type="radio" name="typeofOccurrence" value="Monthly" id="Monthly"/>Monthly</tr>
				 		<tr>
				 	 		<td>Recover Every</td>
				 	 		<td><input type="text" name="recurrPeriod" id="recurrMonthly" value=""/></td>
				 	 	</tr>
				 		<tr>
				 	 		<td>Off Every</td>
				 	 		<td>
					 	 		<select id="selectedStartMonth" name="selectedStartMonth">
									    <c:forEach var="hour" begin="0" end="12">
                                                     	<option value="${hour}">${hour}</option>
                                        </c:forEach>		
								</select>
							</td>
				 	 		<td>Month(s)</td>
				 		</tr>
			 		 </table>
			 		 </div>
			 	 </td>
	 </tr>	
	 <tr onclick="expandProgram('4');">
	 	<td style="font-family: Tahoma;font-family: Tahoma;font-size: 10px;color:white; font-weight: bold; border: 1px;border-bottom-style:solid;" bgcolor="#336699">Yearly</td>
	 </tr>
	 <tr>
	 	<td bgcolor="white" style="display:none;" id="img_4">
			 <div style="padding-bottom: 20px; background-color:#F5F5F5; width:400px;border-color;blue; border-style:solid;border-width:1px" >
			 	    <table border="0" cellPadding=2 cellSpacing=2>
					 		<tr><td><input type="radio" name="typeofOccurrence" value="Yearly" id="Yearly"/>Yearly</td></tr>
					 		<tr>
					 	 		<td>Every</td>
					 	 		<td>
					 	 		
					 	 		<select id="selectedStartYearlyMonth" name="selectedStartMonth">
									<logic:iterate id="month" property="months" name="calendarEventForm">
											<option value="${month}">${month}</option>
									</logic:iterate>
										
								</select>
				 	 		    </td>
					 	 		<td><input type="text" name="recurrPeriod" id="recurrYearly" value=""/></td>
					 	 		<td>Day(s)</td>
					 		</tr>
			 		</table>
 	 		 </div>
	 	</td>
	 </tr>
<!--
	 <tr>
		<td>
			<table border="0" cellPadding=2 cellSpacing=2>
				 		<tr>Time</tr>
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
			 		</table>
		</td>	
	</tr>
-->
	<tr>
		<td align="center">
			<html:submit  property="button" value="Save An CLose" onclick="return eventType();"/>
		</td>
	</tr>	
</table>
</digi:form>