<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-calendar.js"/>"></script>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/calendar/assets/skins/sam/calendar.css">
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/calendar/calendar-min.js"></script> 

<!-- code for rendering that nice calendar -->
<bean:define id="langBean" name="org.digijava.kernel.navigation_language" scope="request" type="org.digijava.kernel.entity.Locale" toScope="page" />
<bean:define id="lang" name="langBean" property="code" scope="page" toScope="page" />

<style type="text/css">
    /* Clear calendar's float, using dialog inbuilt form element */
	.yui-dialog {
		position: fixed;
	}
   .yui-dialog .bd form {
        clear:left;
    }

    /* Have calendar squeeze upto bd bounding box */
    .yui-dialog .bd {
        padding:0;
    }

   .yui-dialog .hd {
        text-align:left;
    }

    /* Center buttons in the footer */
    .yui-dialog .ft .button-group {
        text-align:center;
    }

    /* Prevent border-collapse:collapse from bleeding through in IE6, IE7 */
    .yui-overlay-hidden table {
        *display:none;
    }

    /* Remove calendar's border and set padding in ems instead of px, so we can specify an width in ems for the container */
    .cal-div-wrapper {
        border:none;
        padding:1em;
        height: 170px;
    }

    /* Datefield look/feel */
    .datefield {
        position:relative;
        top:10px;
        left:10px;
        white-space:nowrap;
        border:1px solid black;
        background-color:#eee;
        width:25em;
        padding:5px;
    }

    .datefield input,
    .datefield button,
    .datefield label  {
        vertical-align:middle;
    }

    .datefield label  {
        font-weight:bold;
    }

    .datefield input  {
        width:15em;
    }

    .datefield button  {
        padding:0 5px 0 5px;
        margin-left:2px;
    }

    .datefield button img {
        padding:0;
        margin:0;
        vertical-align:middle;
    }

    /* Example box */
    .box {
        position:relative;
        height:30em;
    }
</style>



<script type="text/javascript">

	YUI_MONTH_NAMES_LONG = ["<digi:trn>January</digi:trn>", 
	   					 "<digi:trn>February</digi:trn>", 
						 "<digi:trn>March</digi:trn>", 
						 "<digi:trn>April</digi:trn>", 
						 "<digi:trn>May</digi:trn>", 
						 "<digi:trn>June</digi:trn>", 
						 "<digi:trn>July</digi:trn>", 
						 "<digi:trn>August</digi:trn>", 
						 "<digi:trn>September</digi:trn>", 
						 "<digi:trn>October</digi:trn>", 
						 "<digi:trn>November</digi:trn>", 
						 "<digi:trn>December</digi:trn>"];
	
	YUI_MONTH_NAMES_MEDIUM = ["<digi:trn>Jan</digi:trn>", 
		   					 "<digi:trn>Feb</digi:trn>", 
							 "<digi:trn>Mar</digi:trn>", 
							 "<digi:trn>Apr</digi:trn>", 
							 "<digi:trn>May</digi:trn>", 
							 "<digi:trn>Jun</digi:trn>", 
							 "<digi:trn>Jul</digi:trn>", 
							 "<digi:trn>Aug</digi:trn>", 
							 "<digi:trn>Sep</digi:trn>", 
							 "<digi:trn>Oct</digi:trn>", 
							 "<digi:trn>Nov</digi:trn>", 
							 "<digi:trn>Dec</digi:trn>"];
	
	YUI_DAY_NAMES_SHORT	= ["<digi:trn>Su</digi:trn>", "<digi:trn>Mo</digi:trn>", "<digi:trn>Tu</digi:trn>", "<digi:trn>We</digi:trn>", 
                           "<digi:trn>Th</digi:trn>", "<digi:trn>Fr</digi:trn>", "<digi:trn>Sa</digi:trn>"];
	
	YUI_DAY_NAMES_MEDIUM = ["<digi:trn>Sun</digi:trn>", "<digi:trn>Mon</digi:trn>", "<digi:trn>Tue</digi:trn>", "<digi:trn>Wed</digi:trn>", 
                               "<digi:trn>Thu</digi:trn>", "<digi:trn>Fri</digi:trn>", "<digi:trn>Sat</digi:trn>"];

	var dateFormat = '<%=org.digijava.module.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT) %>';
	if (dateFormat == 'null')
		dateFormat = 'dd/mm/yyyy';
	
	
	function getDateFromCalendar(inputArray)
	{
		var format = dateFormat;
		var monthPos, dayPos, yearPos;
		var month = inputArray.month;
		if (format.toLowerCase().indexOf('mmm') != -1){
			monthPos = format.toLowerCase().indexOf('mmm');
			yearPos = format.indexOf('yyyy');
			dayPos = format.indexOf('dd');
			month = YUI_MONTH_NAMES_MEDIUM[inputArray.month - 1];		
		}
		else{
			monthPos = format.toLowerCase().indexOf('mm');
			yearPos = format.toLowerCase().indexOf('yyyy');
			dayPos = format.toLowerCase().indexOf('dd');
		}	
		var result = '';
		if (dayPos < monthPos){
			if (monthPos < yearPos)
				result = inputArray.day + '/' + month + '/' + inputArray.year;
			else{
				if (dayPos < yearPos)
					result = inputArray.day + '/' + inputArray.year + '/' + month;
				else
					result = inputArray.year + '/' + inputArray.day + '/' + month;
			}
		}	
		else{ //month < dayPos
			if (yearPos < monthPos)
				result = inputArray.year + '/' + month + '/' + inputArray.day;
			else{
				if (dayPos < yearPos)
					result = month + '/' + inputArray.day + '/' + inputArray.year;
				else
					result = month + '/' + inputArray.year + '/' + inputArray.day;
			}
		}	
		
		
		return result;
		
	}	
	
	function dateStringToObject (dateStr, format ) {
		
		var monthPos, dayPos, yearPos;
		var monthLen, dayLen=2, yearLen=4;
		if (format.toLowerCase().indexOf('mmm') != -1){
			monthPos = format.toLowerCase().indexOf('mmm');
			yearPos = format.indexOf('yyyy');
			dayPos = format.indexOf('dd');
			monthLen = 3;
		}
		else{
			monthPos = format.toLowerCase().indexOf('mm');
			yearPos = format.toLowerCase().indexOf('yyyy');
			dayPos = format.toLowerCase().indexOf('dd');
			monthLen = 2;
		}
		
		var month	= dateStr.substr(monthPos, monthLen);
		var day		= dateStr.substr(dayPos, dayLen);
		var year	= dateStr.substr(yearPos, yearLen);
		
		if ( monthLen == 3 ) {
			for (var i=0; i<YUI_MONTH_NAMES_MEDIUM.length; i++) {
				if ( month.toLowerCase() == YUI_MONTH_NAMES_MEDIUM[i].toLowerCase() ) {
					month	= i + 1;
					break;
				}
			}
		}
		return {
			month: month,
			day:day,
			year:year
		};
		
	}

	function clearDate(objectId){
		var textboxEl= document.getElementById(objectId);
		if (textboxEl != null) {
			textboxEl.value = "";
		}
	}
	
	function positionHelper (buttonId) {
		var retVal = true;
		if (buttonId != null) {
			var buttonEl	= document.getElementById(buttonId);
			var parent		= buttonEl.parentNode;
			while ( parent != null && !YAHOO.util.Dom.hasClass(parent, 'yui-panel-container') ) {
				parent	= parent.parentNode;
			}
			if ( parent != null ) {
				var parentRegion 	= YAHOO.util.Dom.getRegion(parent);
				var parentTop		= parentRegion.top;
				var parentBottom	= parentRegion.bottom;
				var buttonY			= YAHOO.util.Dom.getY(buttonEl);
				
				if ( parentTop < buttonY && buttonY < parentBottom ) {
					if ( buttonY - parentTop > parentBottom - buttonY ) 
						retVal = true;
					else 
						retVal = false;
				}
			}
		}
		
		return retVal;
	}

	function pickDateById(buttonId,objectId){
		pickDateById2(buttonId, objectId, true);
	}
	
	function pickDateById2(buttonId,objectId,calendarUp,overrideObjectCorner)
	{

		var localCalendarUp	= true;
		var calendarCorner = "bl";
		var objectCorner = "tr";
		if (calendarUp != null)  {
			if ( calendarUp instanceof Function ) {
				localCalendarUp	= calendarUp(buttonId);
			} 
			else 
				localCalendarUp = calendarUp;
		}
		
		textboxEl= document.getElementById(objectId);
		
		if ( typeof(calendarWrapperDialogs) == 'undefined' || calendarWrapperDialogs == null )
			calendarWrapperDialogs	= new Object();
		
		var dialogId		= objectId + '_dialog';
		var calDivWrapper	= objectId + '_calDivWrapper';
		var dialog		= calendarWrapperDialogs[dialogId];

		//if ( dialog == null ) {
			var renderDiv	= document.getElementById(buttonId).parentNode;
			while ( renderDiv != null ) {
				var yuiEl 	= new YAHOO.util.Element(renderDiv);
				if ( renderDiv.tagName != null && renderDiv.tagName.toLowerCase() == "div" && yuiEl.hasClass("yui-overlay") )
					break;
				renderDiv	= renderDiv.parentNode;
			}
			if ( renderDiv == null )
				renderDiv	= document.body;
			if(localCalendarUp == true){
				calendarCorner = "bl";
				objectCorner = "tr";
			}else{
				calendarCorner = "tl";
				objectCorner = "br";
			}
			if (overrideObjectCorner) {
				objectCorner = overrideObjectCorner;
			}
			dialog		= new YAHOO.widget.Dialog(dialogId, {
		        visible:false,
		        
		        context:[buttonId,  calendarCorner, objectCorner],
		        draggable:true,
		        close:true
		        
		    });
			dialog.setHeader('<digi:trn jsFriendly="true">Pick A Date</digi:trn>');
            dialog.setBody('<div class="yui-skin-sam"><div class="cal-div-wrapper" id="'+calDivWrapper+'"></div></div>');
            dialog.render( renderDiv );
            dialog.showEvent.subscribe(function() {
                if (YAHOO.env.ua.ie) {
                    // Since we're hiding the table using yui-overlay-hidden, we 
                    // want to let the dialog know that the content size has changed, when
                    // shown
                    dialog.fireEvent("changeContent");
                }
            });
			calendarWrapperDialogs[dialogId] 	= dialog;
			
			var calendar 	= new YAHOO.widget.Calendar(calDivWrapper, {
				navigator: true,
                iframe:false,          // Turn iframe off, since container has iframe support.
                hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
            });
			calendar.cfg.setProperty("MONTHS_LONG", YUI_MONTH_NAMES_LONG );
			
			calendar.cfg.setProperty("WEEKDAYS_SHORT", YUI_DAY_NAMES_SHORT);
			calendar.cfg.setProperty("WEEKDAYS_MEDIUM",YUI_DAY_NAMES_MEDIUM);
			if ( textboxEl.value != null && textboxEl.value.length > 0 ) {
				var dateObj			= dateStringToObject(textboxEl.value,dateFormat);
				calendar.cfg.setProperty("selected", dateObj.month + "/" + dateObj.day + "/" + dateObj.year );
				calendar.cfg.setProperty("pagedate", dateObj.month + "/" + dateObj.year );
			}
			
            calendar.render();
            
            calendar.selectEvent.subscribe(function() {
                if (calendar.getSelectedDates().length > 0) {

                    var selDate = calendar.getSelectedDates()[0];

                    // Pretty Date Output, using Calendar's Locale values: Friday, 8 February 2008
                   // var wStr = calendar.cfg.getProperty("WEEKDAYS_LONG")[selDate.getDay()];
                    var dStr = selDate.getDate() + "";
                   // var mStr = calendar.cfg.getProperty("MONTHS_LONG")[selDate.getMonth()];
                    var yStr = selDate.getFullYear() + "";
    				var mStr = (selDate.getMonth()+1) + "";
    				if ( mStr.length == 1 )
                    	mStr	= "0" + mStr;
    				if ( dStr.length == 1 )
    					dStr	= "0" + dStr;
    				
                    var parameter = {
                    	year: yStr,
                    	month: mStr,
                    	day: dStr,
                    	monthName: [selDate.getMonth()]
                    };
                    var dateString	= getDateFromCalendar(parameter);
                    textboxEl.value = dateString;
                } 
                dialog.hide();
            });
            
		//}
		dialog.show();
		//dialog.align(calendarCorner, objectCorner);
	}
	
	/*Just for compatibility reasons with functions that were used with the old DHTMLSuite calendar widget*/
	function pickDateWithClear(buttonId,inputObject,clearId) {
		if ( inputObject.id == null )
			inputObject.id = "tb" + new Date().getTime();
		pickDateById(buttonId, inputObject.id);
	}


	
</script>		
