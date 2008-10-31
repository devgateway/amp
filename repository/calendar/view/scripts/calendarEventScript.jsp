<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>

<script type="text/javascript">
		YAHOOAmp.namespace("YAHOOAmp.amptab");
		
		YAHOOAmp.amptab.handleCloseAbout = function() {
			if(navigator.appName == 'Microsoft Internet Explorer'){
			}
		}
		
		YAHOOAmp.amptab.handleClose = function() {
			var filter			= document.getElementById('myFilter');
			if (filter.parent != null)
			filter.parent.removeChild(filter);
			
		};
	
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
	
		    
	function initScripts() {
	
	    var msg='\n<digi:trn key="cal:calendar:eventsetup">Recurring Event Setup</digi:trn>';
		myPanel1.setHeader(msg);
		myPanel1.setBody("");
		myPanel1.render(document.body);
						
	}
	
	function showRecEvent() {
		YAHOOAmp.amptab.init();
		var element = document.getElementById("myEvent");
		element.style.display = "inline";
		
		myPanel1.setBody(element);
		myPanel1.center();
		myPanel1.show();

		initCalendar();
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

	window.onload=initScripts;
	
function submit() {
		var eventForm = document.getElementsByName("calendarEventForm")[0];
		eventForm.action = "/calendar/showCalendarEvent.do" 
		eventForm.submit();
	}
</script>




















