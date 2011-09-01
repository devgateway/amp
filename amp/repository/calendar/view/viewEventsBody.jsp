<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@page import="java.util.*"%>
<%@page import="org.digijava.module.calendar.form.CalendarViewForm" %>
<%@page import="org.digijava.module.calendar.util.CalendarUtil"%>

<script type="text/javascript" charset="utf-8">
    var trn_today_button 	='<digi:trn jsFriendly="true">Today</digi:trn>';
	var trn_day_tab 		='<digi:trn jsFriendly="true">Day</digi:trn>';
	var trn_week_tab		='<digi:trn jsFriendly="true">Week</digi:trn>';
	var trn_month_tab		='<digi:trn jsFriendly="true">Month</digi:trn>';
	var trn_year_tab		='<digi:trn jsFriendly="true">Year</digi:trn>';
	
//Translations for calendar locale_sv.js file 
//month full names
	
	var trn_month_january 		='<digi:trn jsFriendly="true">January</digi:trn>';
	var trn_month_february 		='<digi:trn jsFriendly="true">February</digi:trn>';
	var trn_month_march 		='<digi:trn jsFriendly="true">March</digi:trn>';
	var trn_month_april 		='<digi:trn jsFriendly="true">April</digi:trn>';
	var trn_month_may 			='<digi:trn jsFriendly="true">May</digi:trn>';
	var trn_month_june 			='<digi:trn jsFriendly="true">June</digi:trn>';
	var trn_month_july 			='<digi:trn jsFriendly="true">July</digi:trn>';
	var trn_month_august 		='<digi:trn jsFriendly="true">August</digi:trn>';
	var trn_month_september 	='<digi:trn jsFriendly="true">September</digi:trn>';
	var trn_month_october 		='<digi:trn jsFriendly="true">October</digi:trn>';
	var trn_month_november 		='<digi:trn jsFriendly="true">November</digi:trn>';
	var trn_month_december 		='<digi:trn jsFriendly="true">December</digi:trn>';
	
//month short names
	var trn_month_jan='<digi:trn jsFriendly="true">Jan</digi:trn>';
	var trn_month_feb='<digi:trn jsFriendly="true">Feb</digi:trn>';
	var trn_month_mar='<digi:trn jsFriendly="true">Mar</digi:trn>';
	var trn_month_apr='<digi:trn jsFriendly="true">Apr</digi:trn>';
	var trn_month_jun='<digi:trn jsFriendly="true">Jun</digi:trn>';
	var trn_month_jul='<digi:trn jsFriendly="true">Jul</digi:trn>';
	var trn_month_aug='<digi:trn jsFriendly="true">Aug</digi:trn>';
	var trn_month_sep='<digi:trn jsFriendly="true">Sep</digi:trn>';
	var trn_month_oct='<digi:trn jsFriendly="true">Oct</digi:trn>';
	var trn_month_nov='<digi:trn jsFriendly="true">Nov</digi:trn>';
	var trn_month_dec='<digi:trn jsFriendly="true">Dec</digi:trn>';

//day full names 
	
	var trn_day_sunday		='<digi:trn jsFriendly="true">Sunday</digi:trn>';
	var trn_day_monday		='<digi:trn jsFriendly="true">Monday</digi:trn>';
	var trn_day_tuesday		='<digi:trn jsFriendly="true">Tuesday</digi:trn>';
	var trn_day_wednesday	='<digi:trn jsFriendly="true">Wednesday</digi:trn>';
	var trn_day_thursday	='<digi:trn jsFriendly="true">Thursday</digi:trn>';
	var trn_day_friday		='<digi:trn jsFriendly="true">Friday</digi:trn>';
	var trn_day_saturday	='<digi:trn jsFriendly="true">Saturday</digi:trn>';

//day shotr names 
	

	var trn_day_sun='<digi:trn jsFriendly="true">Sun</digi:trn>';
	var trn_day_mon='<digi:trn jsFriendly="true">Mon</digi:trn>';
	var trn_day_tue='<digi:trn jsFriendly="true">Tue</digi:trn>';
	var trn_day_wed='<digi:trn jsFriendly="true">Wed</digi:trn>';
	var trn_day_thu='<digi:trn jsFriendly="true">Thu</digi:trn>';
	var trn_day_fri='<digi:trn jsFriendly="true">Fri</digi:trn>';
	var trn_day_sat='<digi:trn jsFriendly="true">Sat</digi:trn>';

</script>

 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxcommon.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/scheduler.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/config.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/base.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/locale_sv.js"/>" language="JavaScript" type="text/javascript" ></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/event.js"/>"language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/load.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/lightbox.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxdataprocessor.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/property.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/recurring.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxscheduler_year_view.js"/>" language="JavaScript" type="text/javascript"></script>
 <script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

 <link rel="stylesheet" href="<digi:file src="module/calendar/css/layout.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/note.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/recurring.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/lightbox.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/dhtmlxscheduler_ext.css"/>"> 

<c:set var="printButon"><%=request.getSession().getAttribute("print")%></c:set>
 <c:if test="${printButon}">
 	<style type="text/css">
		.dhx_cal_container {
		font-size: 10pt;
		}
		.dhx_cal_container input {
		display: none; /* hide buttons in print preview */ 
		}
		.dhx_cal_event_line {
		 height: auto;
		}
		.dhx_cal_event_clear{
		 height: auto;
	
		}
		
	</style>
    <style type="text/css" media="print">
        .noPrint{
            display: none;
        }

    </style>
	<script type="text/javascript" charset="utf-8">
		  scheduler._click.dhx_cal_tab=function(){
		   var mode = this.getAttribute("name").split("_")[0];
		   scheduler.setCurrentView(scheduler._date,mode);
		    if(mode=="week"||mode=="day")
		      scheduler._els["dhx_cal_data"][0].style.height="1100px";
		 }
	
	</script>  
    <table width="200px" height="40px" class="noPrint">
	  	<tr>
	  		<td>	
	 	
		 	<a target="_blank" title="Printing" onClick="window.print();" style="cursor: pointer">
		 		<img width="20" vspace="2" hspace="2" height="30" border="0" alt="Printer Friendly" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif"/>
		 	</a>
	 		<input type="button" value="close" onClick="window.close()" width="20" height="30"/>
	 		</td>
	 	</tr>
	  </table>
 </c:if>

<style>
	<%=CalendarUtil.getEventTypesCss()%>
</style>
<script type="text/javascript" charset="utf-8">
<!--

	function init() {
		 
		var sections=[
		  			{key:1, label:'Section A'},
		  			{key:2, label:'Section B'},
		  			{key:3, label:'Section C'},
		  			{key:4, label:'Section D'},
		  			{key:5, label:'Section E'},
		  			{key:6, label:'Section F'},
		  			{key:7, label:'Section G'},
		  			{key:8, label:'Section H'},
		  			{key:9, label:'Section I'},
		  			{key:10, label:'Section G'},
		  			{key:11, label:'Section K'},
		  			{key:12, label:'Section L'}
		  			
		  		];		
		
		scheduler.config.xml_date="%Y-%m-%d %H:%i";
		scheduler.config.multi_day = true;
	
		var eth = getEthiopianCalendarDate(<%=request.getSession().getAttribute("year")%>,<%=request.getSession().getAttribute("month")%>,<%=request.getSession().getAttribute("day")%>);
		var myDate = new Date(eth);
		var ehtMonth = <%=request.getSession().getAttribute("month")%>;
		var type = calType(<%=request.getSession().getAttribute("type")%>);
		var printView = <%=request.getSession().getAttribute("view")%>;
		var printDate = <%=request.getSession().getAttribute("date")%>;


		if(printDate !=null){
		 var date = <%=request.getSession().getAttribute("date")%>;
		}else{
			date = myDate;
			}
		
		var defoultView = "year";
		if(printView!=null){
			if(printView == 1){
				defoultView = "day"
			}else if(printView == 2){
				defoultView = "week"
			}else if(printView == 4){
				defoultView = "year"
			}else if(printView == 3){
				defoultView = "month"
			}
		}
		
		
		scheduler.init('scheduler_here',date,defoultView,type, ehtMonth);
		scheduler.templates.event_text=function(start_date,end_date,ev){
			return '<digi:trn jsFriendly="true">Text:</digi:trn><b> '+ev.text+'</b><br>'+'<digi:trn jsFriendly="true">Descr.</digi:trn>'+ev.details;
		}
         var lastTimeStamp=new Date().getTime();
		scheduler.load("/calendar/showEvents.do?lastTimeStamp="+lastTimeStamp);
		scheduler.templates.event_class=function(start_date,end_date,ev){
			if(ev.type != 0){
						return "event_"+ev.type;
				}
		  }
 
				 
		scheduler.attachEvent("onViewChange", function (mode , date){
			   document.getElementById("printView").value = mode;
			   document.getElementById("printDate").value = date;
			   //show corresponding view breadcrumb
			   var whichView=document.getElementById("viewSpan");
			   var monthly='<digi:trn jsFriendly="true">Monthly View</digi:trn>';
			   var weekly='<digi:trn jsFriendly="true">Weekly View</digi:trn>';
			   var daily='<digi:trn jsFriendly="true">Daily View</digi:trn>';
			   var yearly='<digi:trn jsFriendly="true">Yearly View</digi:trn>';
			   if(mode=='month'){
				   whichView.innerHTML=monthly;
			   }else if(mode=='week'){
				   whichView.innerHTML=weekly;
			   }else if(mode=='day'){
				   whichView.innerHTML=daily;
			   }else if(mode=='year'){
				   whichView.innerHTML=yearly;
			   }			   
			});
		//scheduler.attachEvent("onMouseOver",showToolTip);
		scheduler.attachEvent("onClick",function(id){
		    var ev = scheduler.getEvent(id);
			var eventId = ev.id;
			if(eventId.indexOf("#") != "-1"){
			var	eventId = eventId.slice(0,eventId.indexOf("#"));
			}
			<digi:context name="previewEvent" property="context/module/moduleinstance/showCalendarEvent.do" />
		        document.forms[0].action = "<%= previewEvent%>~ampCalendarId="+eventId+"~method=preview~resetForm=true";
		       document.forms[0].submit();
		    return true;
		});
		scheduler.createUnitsView("unit","section_id",sections);
		var printView = <%=request.getSession().getAttribute("print")%>
		
	if(!printView){
		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
			var app = "";
			if (ev.approve==0) {
				app = "*";
			} else if (ev.approve==2){
				app = "**";
			}

			var evName = app+ev.text;	
			var dotted= "..."; 
			if (evName.length >18) {
				var shortText = evName.substr(0,15)+dotted;
			} else {
				var shortText = evName;
			}
		    var text = evName;
	        var img = '<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/magnifier.png" height="12" width="12" align="left"/>';
	        var trnLbelTitle 		= '<digi:trn jsFriendly="true">Title:</digi:trn>';
	        var trnLabelStartDate 	= '<digi:trn jsFriendly="true">StartDate:</digi:trn>';
	        var trnLabelEndDate 	= '<digi:trn jsFriendly="true">EndDate:</digi:trn>';
	        return "<span  title='"+trnLbelTitle+" "+text+"  "+trnLabelStartDate+" "+start_date+" "+trnLabelEndDate+" "+end_date+"'>"+img+""+shortText+"</span>";
		 }
	 }else{

		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
		        var text = ev.text.substr(0,20);
		        return "<span style='font-size: 10pt;'>"+text+"</span>";
			 }
		 

		 }
		scheduler.config.dblclick_create = false;
		scheduler.config.multi_days = true;
	/*
		scheduler.attachEvent("onViewChange",function(mode,date){

			if(mode == "month"){
						submitFilterForm('monthly',date.getTime()/1000); 
						
						return true;
					}
			});
		
	*/



	} 

function calType(type){
	switch (type){
	case 0:
		return "GRE";
		break;
	case 1:
		return "ETH";
		break;	

		}
}	
	
function getEthiopianCalendarDate(yyyy,mm,dd)
{
	
   var months = new Array(13);
   months[1]  = "jan";
   months[2]  = "feb";
   months[3]  = "mar";
   months[4]  = "apr";
   months[5]  = "may";
   months[6]  = "jun";
   months[7]  = "jul";
   months[8]  = "aug";
   months[9]  = "sep";
   months[10] = "oct";
   months[11] = "nov";
   months[12] = "dec";
   months[13] = "dec";
   var now = new Date(yyyy,mm,dd);

   if(now.getMonth()==0 || mm == 13){
   		var monthnumber = now.getMonth()+12;
   }else{
	   var monthnumber = now.getMonth();
	   }
   var monthname   = months[monthnumber];
   var monthday    = now.getDate();
 
   var dateString = monthname +
                    ' ' + monthday +
                	' ' + yyyy;

  return dateString;
}	
-->
</script>	
<body>
<script type="text/javascript">
window.onload=init;
</script>
<input type="hidden" value=""  id="printView"/>
<input type="hidden" value=""  id="printDate"/>
	<div id="scheduler_here" class="dhx_cal_container"  style='padding:2% 0% 2% 0%; width:100%; height:93%;max-height:570;  position:relative'>
		<div class="dhx_cal_navline" style="z-index:10">
<!--			<div class="dhx_cal_prev_button" style="">&nbsp;</div>-->
<!--			<div class="dhx_cal_date" style=""></div>-->
<!--			<div class="dhx_cal_next_button" style="">&nbsp;</div>-->
<!--			<div class="dhx_cal_today_button" style=""></div>-->	
	 		<div class="dhx_cal_tab" name="year_tab" style="left:0px;"></div>  	 
			<div class="dhx_cal_tab" name="month_tab" style="left:60px;"></div>
			<div class="dhx_cal_tab" name="week_tab" style="left:136px;"></div>
			<div class="dhx_cal_tab" name="day_tab" style="left:203px;"></div>
			
		</div>
		<div id="newContainer" style="width: 100%; height: 110%; position: relative; top: 15px; border: 1px #CCCCCC solid;">
			<div style="z-index: 1">
				<table cellpadding="5" cellspacing="5">
					<tr style="margin-bottom: 10px;">
						<td>
							<div class="dhx_cal_prev_button" style="">&nbsp;</div>
						</td>
						<td>
							<div class="dhx_cal_date" style=""></div>
						</td>
						<td>
							<div class="dhx_cal_next_button" style="">&nbsp;</div>
						</td>
						<td align="right">
							<div class="dhx_cal_today_button" style=""></div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="dhx_cal_header" style=""></div>
							<div class="dhx_cal_data"></div>
						</td>
					</tr>
				</table>
			</div>
		</div>		
		<feature:display name="Filter" module="Calendar">
			<div style="padding-top:30px;width:250px;height:28px;white-space: nowrap;">
				<field:display name="Run Filter Button" feature="Filter">
					<input class="buttonx" type="submit" value="<digi:trn>Run Filter</digi:trn>" onclick="changeDonorsAndEventTypesState();"/>
				</field:display>
		    	&nbsp;
		    	<field:display name="Reset Filter Button" feature="Filter">
		    		<input class="buttonx" type="reset" value="<digi:trn>Reset</digi:trn>" />
		    	</field:display>
		    	&nbsp;
		      	<input type="button" class="buttonx" value="<digi:trn key="calendar:print">Print</digi:trn>"  onclick="openPrinter();" />
		      	&nbsp;
		      	<input type="button" class="buttonx" value="<digi:trn>Add New Event</digi:trn>"  onclick="addEvent();" />
			</div>
		</feature:display>
	</div>
</body>
