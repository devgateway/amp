<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<digi:instance property="calendarViewForm"/>

<script type="text/javascript">
function addEvent () {	
	<digi:context name="refreshUrl" property="context/module/moduleinstance/showCalendarEvent.do~selectedCalendarTypeId=0~method=new" />
    document.calendarViewForm.action= "<%= refreshUrl %>";
    document.calendarViewForm.submit();
}
function submitFilterForm(view, timestamp) {
	changeState();	
  	var form = document.getElementById('filterForm');
  	if (form != null) {
    	form.view.value = view;
    	form.timestamp.value = timestamp;
    	form.submit();
  	}
}

function changeState() {
	var publicFilter = document.getElementById('publicFilter').value;
	if (publicFilter!=null) {
	  document.calendarViewForm.showPublicEvents.value = publicFilter;
	}
	document.getElementById('filterForm').submit();
} 
function getElementsByName_iefix(tag, name) {
    var elem = document.getElementsByTagName(tag);
    var arr = new Array();
    for(i = 0,iarr = 0; i < elem.length; i++) {
         att = elem[i].getAttribute("name");
         if(att == name) {
              arr[iarr] = elem[i];
              iarr++;
         }
    }
    return arr;
}
function changeDonorsAndEventTypesState(){
	//changeDonorsState();
	changeEventTypesState();
	var form = document.getElementById('filterForm');
	
	var yearlyView = getElementsByName_iefix('div', 'year_tab')[0];
	var monthlyView = getElementsByName_iefix('div', 'month_tab')[0];
	var weeklyView = getElementsByName_iefix('div', 'week_tab')[0];
	var daylyView = getElementsByName_iefix('div', 'day_tab')[0];
	var view='';
	if (yearlyView.className.indexOf('active')!=-1){
		view='yearly';
	}else if (monthlyView.className.indexOf('active')!=-1){
		view='monthly';
	}else if (weeklyView.className.indexOf('active')!=-1){
		view='weekly';
	}else if (daylyView.className.indexOf('active')!=-1){
		view='daily';
	}
	form.view.value = view;
	form.submit();
}

function openPrinter(){
var view = document.getElementById("printView").value;
var date = document.getElementById("printDate").value;
var myDate = new Date(date)

if(view!= null)
     {
 		window.open('/calendar/showCalendarView.do~filterInUse=false~view='+view+'~date='+myDate.valueOf()+'~print=true','mywindow','toolbar=no,location=no, width="10"10,height=600", directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
	}
}

function changeDonorsState(){	
	var donors = new Array();
	var inputs = document.getElementsByTagName("input");
	var j = 0;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].id.indexOf("donors_") == 0){
			donors[j++]=inputs[i];
		}
	}
	var resetDonors=true;
	for(var i=0;i<donors.length;i++){
		if(donors[i].checked){
			resetDonors=false;
			break;
		}
	}
	if(resetDonors==true){
		document.calendarViewForm.resetDonors.value=true;
	}else{
		document.calendarViewForm.resetDonors.value=false;
	}
}

function changeEventTypesState(){
	var evntTypes = new Array();
	var inputs = document.getElementsByTagName("input");
	var j = 0;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].id.indexOf("evType_") == 0){
			evntTypes[j++]=inputs[i];
		}
	}
	var resetEventTypes=true;
	for(var i=0;i<evntTypes.length;i++){
		if(evntTypes[i].checked){
			resetEventTypes=false;
			break;
		}
	}
	if(resetEventTypes==true){
		document.calendarViewForm.resetEventTypes.value=true;
	}else{
		document.calendarViewForm.resetEventTypes.value=false;
	}	
}

</script>


<html:hidden name="calendarViewForm" property="view" value="${calendarViewForm.view}"/>
<html:hidden name="calendarViewForm" property="timestamp" value="${calendarViewForm.timestamp}"/>
<html:hidden name="calendarViewForm" property="filterInUse" value="true"/>
<html:hidden name="calendarViewForm" property="showPublicEvents" value="${calendarViewForm.showPublicEvents}"/>
<html:hidden name="calendarViewForm" property="resetDonors" value="${calendarViewForm.resetDonors}"/>
<html:hidden name="calendarViewForm" property="resetEventTypes" value="${calendarViewForm.resetEventTypes}"/>
<html:hidden name="calendarViewForm" property="filter.showPublicEvents" value="${filter.showPublicEvents}"/>

<feature:display name="Filter" module="Calendar">
	<field:display name="Event Type Filter" feature="Filter">
		<div class="right_menu">
			<div class="right_menu_header_big">
				<div class="right_menu_header_cont">
					<digi:trn>Event Types</digi:trn>
				</div>
			</div>
			<div class="right_menu_box_big" style="max-height:175px; overflow: auto;">
	    		<c:if test="${!empty calendarViewForm.filter.eventTypes}">
	      			<table cellpadding="0" cellspacing="0">
	        			<c:forEach var="eventType" items="${calendarViewForm.filter.eventTypes}" varStatus="stat">
	         				 <tr>
	         				 	<td>
	              					<html:multibox name="calendarViewForm" property="filter.selectedEventTypes" value="${eventType.id}" styleId="evType_${stat.index}"/>
	            				</td>
	            				<td style="width:29px;padding-left:10px;text-align:center;">
	              					<div style="height: 15px; width: 24px; background-color: ${eventType.color}; border: solid 1px Black;">
	              					</div>
	            				</td>
	            				<td style="padding:5px;text-align:left;font-size:11px; width: 80px;" nowrap="nowrap">
		             				<div style="white-space: nowrap;"><digi:trn><c:out value="${eventType.name}"></c:out></digi:trn></div> 
	            				</td>
	          				</tr>
	        			</c:forEach>
	      			</table>
	    		</c:if>
			</div>
		</div>
	</field:display>
	<div style="height:5px;font-family:Tahoma;">
	&nbsp;
	</div>
<!--	<field:display name="Donor Filter" feature="Filter">-->
<!--		<div class="right_menu">-->
<!--			<div class="right_menu_header_big">-->
<!--				<div class="right_menu_header_cont"><digi:trn>Donors</digi:trn></div>-->
<!--		  	</div>-->
<!--		  	<div class="right_menu_box_big" style="height:175px; overflow: auto;">-->
<!--			    <c:if test="${!empty calendarViewForm.filter.donors}">-->
<!--			    	<table cellpadding="0" cellspacing="0">-->
<!--						<tr>-->
<!--			        		<td>-->
<!--			            	<html:multibox name="calendarViewForm" property="filter.selectedDonors" value="None" styleId="donors_none"/>-->
<!--			            	<digi:trn>None</digi:trn>-->
<!--			            	</td>-->
<!--			            </tr>-->
<!--			        	<c:forEach var="donor" items="${calendarViewForm.filter.donors}" varStatus="stat">-->
<!--							<tr>-->
<!--								<td style="overflow: auto;">-->
<!--		              			<html:multibox name="calendarViewForm" property="filter.selectedDonors" value="${donor.value}" styleId="donors_${stat.index}"/>-->
<!--		              			${donor.label} -->
<!--								</td>-->
<!--							</tr>-->
<!--			        	</c:forEach>-->
<!--			      	</table>-->
<!--				</c:if>-->
<!--			</div>-->
<!--		</div>-->
<!--	</field:display>-->
	<c:if test="${not empty sessionScope.currentMember}">
		<div style="padding:5px;height:28px;">
	 	<select id="publicFilter" style="width:210px;" class="inp-text" onchange="changeState()" name="filter.showPublicEvents" >
			<c:if test="${calendarViewForm.showPublicEvents == 0}">
				<option selected="selected" value="0">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 0}">
				<option value="0">
			</c:if>
				<digi:trn>Public and private events</digi:trn>
			</option>

	  		<c:if test="${calendarViewForm.showPublicEvents == 1}">
				<option selected="selected" value="1">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 1}">
				<option value="1">
			</c:if>
				<digi:trn>Only private events</digi:trn>
			</option>
	  		
			<c:if test="${calendarViewForm.showPublicEvents == 2}">
				<option selected="selected" value="2">	
			</c:if>
			<c:if test="${calendarViewForm.showPublicEvents != 2}">
				<option value="2">
			</c:if>
				<digi:trn>Only public events</digi:trn>
			</option>
		</select>
	</div>
	</c:if>
</feature:display>


