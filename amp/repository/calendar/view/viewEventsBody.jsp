<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@page import="java.util.*"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@page import="org.digijava.module.calendar.form.CalendarViewForm" %>
<%@page import="org.digijava.module.calendar.util.CalendarUtil"%>



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

 <link rel="stylesheet" href="<digi:file src="module/calendar/css/layout.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/note.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/lightbox.css"/>"> 
 
<div style="display: none"><jsp:include page="viewEventsFilter.jsp" flush="true"/></div>
 
<style>
<%=CalendarUtil.getEventTypesCss()%>
</style>

<script type="text/javascript" charset="utf-8">

	function init() {
		 
		var sections=[
		  			{key:1, label:"Section A"},
		  			{key:2, label:"Section B"},
		  			{key:3, label:"Section C"},
		  			{key:4, label:"Section A"},
		  			{key:5, label:"Section B"},
		  			{key:6, label:"Section C"},
		  			{key:7, label:"Section D"},
		  			{key:8, label:"Section A"},
		  			{key:9, label:"Section B"},
		  			{key:10, label:"Section C"},
		  			{key:11, label:"Section A"},
		  			{key:12, label:"Section B"}
		  			
		  		];		
		
		scheduler.config.xml_date="%Y-%m-%d %H:%i";
		scheduler.config.multi_day = true;
		var myDate=new Date();
		var currentDate = myDate.setFullYear(<%=request.getSession().getAttribute("year")%>,<%=request.getSession().getAttribute("month")%>,<%=request.getSession().getAttribute("day")%>);
		scheduler.init('scheduler_here',currentDate,'month');
		scheduler.templates.event_text=function(start_date,end_date,ev){
			return "Text:<b> "+ev.text+"</b><br>"+"Descr."+ev.details;
		}
		scheduler.load("/calendar/showEvents.do");
		scheduler.templates.event_class=function(start_date,end_date,ev){
			if(ev.type != 0){
						return "event_"+ev.type;
				}
		  }
		scheduler.attachEvent("onClick",function(id){
		    var ev = scheduler.getEvent(id);
			var eventId = ev.id;
		    <digi:context name="previewEvent" property="context/module/moduleinstance/showCalendarEvent.do" />
		      document.forms[0].action = "<%= previewEvent%>~ampCalendarId="+eventId+"~method=preview~resetForm=true";
		      document.forms[0].submit();
		    return true;
		})
		scheduler.createUnitsView("unit","section_id",sections);
		scheduler.templates.event_bar_text=function(start_date,end_date,ev){
	        var text = ev.text.substr(0,20);
	        var img = '<digi:img src="module/calendar/images/magnifier.png" height="13" width="13" align="left"/>';
	        return "<span title='"+"Title:"+text+" "+"StartDate:"+start_date+"EndDate:"+end_date+"'>"+img+""+text+"</span>";
		}
		scheduler.config.dblclick_create = false;
		scheduler.config.multi_days = true;
		
	//		scheduler.attachEvent("onViewChange",function(mode,date){
			//alert(mode);
		//		if(mode == "month"){
			//		submitFilterForm('monthly',date); return(false);
				//}else{
					//submitFilterForm('yearly',date); return(false);
			//	}
		//})

	
	} 
	var trn_today_button ='<digi:trn>Today</digi:trn>';
	var trn_day_tab ='<digi:trn>Day</digi:trn>';
	var trn_week_tab='<digi:trn>Week</digi:trn>';
	var trn_month_tab='<digi:trn>Month</digi:trn>';
	
// Translations for calendar locale_sv.js file 
// month full names
	
	var trn_month_january='<digi:trn>January</digi:trn>';
	var trn_month_february='<digi:trn>February</digi:trn>';
	var trn_month_march='<digi:trn>March</digi:trn>';
	var trn_month_april='<digi:trn>April</digi:trn>';
	var trn_month_may='<digi:trn>May</digi:trn>';
	var trn_month_june='<digi:trn>June</digi:trn>';
	var trn_month_july='<digi:trn>July</digi:trn>';
	var trn_month_august='<digi:trn>August</digi:trn>';
	var trn_month_september='<digi:trn>September</digi:trn>';
	var trn_month_october='<digi:trn>October</digi:trn>';
	var trn_month_november='<digi:trn>November</digi:trn>';
	var trn_month_december='<digi:trn>December</digi:trn>';
	
//month short names
	var trn_month_jan='<digi:trn>Jan</digi:trn>';
	var trn_month_feb='<digi:trn>Feb</digi:trn>';
	var trn_month_mar='<digi:trn>Mar</digi:trn>';
	var trn_month_apr='<digi:trn>Apr</digi:trn>';
	var trn_month_jun='<digi:trn>Jun</digi:trn>';
	var trn_month_jul='<digi:trn>Jul</digi:trn>';
	var trn_month_aug='<digi:trn>Aug</digi:trn>';
	var trn_month_sep='<digi:trn>Sep</digi:trn>';
	var trn_month_oct='<digi:trn>Oct</digi:trn>';
	var trn_month_nov='<digi:trn>Nov</digi:trn>';
	var trn_month_dec='<digi:trn>Dec</digi:trn>';

// day full names 
	
	var trn_day_sunday='<digi:trn>Sunday</digi:trn>';
	var trn_day_monday='<digi:trn>Monday</digi:trn>';
	var trn_day_tuesday='<digi:trn>Tuesday</digi:trn>';
	var trn_day_wednesday='<digi:trn>Wednesday</digi:trn>';
	var trn_day_thursday='<digi:trn>Thursday</digi:trn>';
	var trn_day_friday='<digi:trn>Friday</digi:trn>';
	var trn_day_saturday='<digi:trn>Saturday</digi:trn>';

// day shotr names 
	

	var trn_day_sun='<digi:trn>Sun</digi:trn>';
	var trn_day_mon='<digi:trn>Mon</digi:trn>';
	var trn_day_tue='<digi:trn>Tue</digi:trn>';
	var trn_day_wed='<digi:trn>Wed</digi:trn>';
	var trn_day_thu='<digi:trn>Thu</digi:trn>';
	var trn_day_fri='<digi:trn>Fri</digi:trn>';
	var trn_day_sat='<digi:trn>Sat</digi:trn>';

</script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/locale_sv.js"/>" language="JavaScript" type="text/javascript" ></script>
 <script type="text/javascript" charset="utf-8">
 	
 </script>
<div id="css" style="display:block;"></div>
<body onload="init();">
	<div id="scheduler_here" class="dhx_cal_container"  style='padding:1% 0% 1% 0%; width:100%; height:100%; position:relative'>
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button"></div>
			<div class="dhx_cal_date"></div>
			<div  class="dhx_cal_tab" name="unit_tab" style="right:270px; display: none"></div>
			<div class="dhx_cal_tab" name="month_tab" style="right:205px;"></div>
			<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
			<div class="dhx_cal_tab" name="day_tab" style="right:76px;"></div>
			
		</div>
		<div class="dhx_cal_header">
		</div>
		<div class="dhx_cal_data">
		</div>		
	</div>
</body>