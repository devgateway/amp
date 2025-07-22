<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>

<script type="text/javascript">
		YAHOOAmp.namespace("YAHOO.amptab");
		
		YAHOOAmp.amptab.handleCloseAbout = function() {
			if(navigator.appName == 'Microsoft Internet Explorer'){
			}
		}
		
		YAHOOAmp.amptab.handleClose = function() {
			var filter			= document.getElementById('myFilter');
			if (filter.parent != null)
			filter.parent.removeChild(filter);
			
		};
	
		var myPanel1Init = false;
		var myPanel1 = new YAHOOAmp.widget.Panel("new", {
			width:"700px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true} );
		    
		
	myPanel1.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	
		    
	function initScriptsRecurr() {
	
	    var msg='\n<digi:trn jsFriendly="true" key="cal:calendar:eventsetup">Recurring Event Setup</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("");
		myPanel1.render(document.body);
						
	}
	
	function showRecEvent() {
		YAHOOAmp.amptab.init();
		var element = document.getElementById("myEvent");
		element.style.display = "inline";
        var recSelEndDate=document.getElementById("recurrSelectedEndDate");
        var recSelEndTime=document.getElementById("recurrSelectedEndTime");

        if(recSelEndDate != null && recSelEndDate.value==''){
            var endDate=document.getElementById("selectedEndDate");
            recSelEndDate.value=endDate.value;
        }
        if(recSelEndTime != null && recSelEndTime.value==''){
            var endDateTime=document.getElementById("selectedEndTime");
            recSelEndTime.value=endDateTime.value;           
        }

        var recEndTimeHour = document.getElementById("recSelectedEndHour");
        recEndTimeHour.selectedIndex=parseInt(recSelEndTime.value.substring(0,2));

        var recStartMinute = document.getElementById("recSelectedEndMinute");
        recStartMinute.selectedIndex=parseInt(recSelEndTime.value.substring(3,5));
       

		if (!myPanel1Init){
	        myPanel1.setBody(element);
	        myPanel1Init = true;
		}
		myPanel1.center();
		myPanel1.show();
//		initCalendar();
		disableInputs();
	}
	
	function addTitleSelect(selectName){
		var categoryOptions = document.getElementsByName(selectName);
		if(categoryOptions[0])
		{
			for(idx = 0; idx < categoryOptions[0].options.length;idx++)
			{
				categoryOptions[0].options[idx].title = categoryOptions[0].options[idx].text;
			}
		}
	}

	addLoadEvent(initScriptsRecurr);
	
function submit() {

	 var list = document.getElementById('selreceivers');  
	 if(list!=null){
	  	for(var i = 0; i < list.length; i++) {
	  		list.options[i].selected = true;
	  	}
	}
		var eventForm = document.getElementById("showAmpEventFormID");
		eventForm.action = "/calendar/showCalendarEvent.do?method=recurr"; 
		eventForm.target = "_self";
		eventForm.submit();
}
</script>




















