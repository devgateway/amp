if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Calendar model
*
*	Created:						January, 20th, 2007
*	@class Purpose of class:		Handle language parameters for a calendar
*			
* 	Update log:
*
************************************************************************************************************/
/**
* @constructor
* @class Purpose of class:	Store language specific data for calendar.
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.calendarLanguageModel = function(languageCode){
	
	var monthArray;							// An array of months.
	var monthArrayShort;					// An array of the months, short version
	var dayArray;							// An array of the days in a week
	var weekString;							// String representation of the string "Week"
	var todayString;						// String representatinon of the string "Today"
	var todayIsString;						// String representation of the string "Today is"
	var timeString;							// String representation of the string "Time"
	this.monthArray = new Array();
	this.monthArrayShort = new Array();
	this.dayArray = new Array();
	
	if(!languageCode)languageCode = 'en';
	this.languageCode = languageCode;
	this.__setCalendarProperties();	
}

DHTMLSuite.calendarLanguageModel.prototype = {
	// {{{ __setCalendarProperties()
    /**
     *	Fill object with string values according to chosen language
     *
     * @private	
     */			
	__setCalendarProperties : function()
	{
		switch(this.languageCode){
			case "ge":	/* German */
				this.monthArray = ['Januar','Februar','Marz','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'];
				this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'];
				this.dayArray = ['Mon','Die','Mit','Don','Fre','Sam','Son'];	
				this.weekString = 'Woche';
				this.todayIsString = 'Heute';		
				this.todayString = 'Heute';		
				this.timeString = '';
				break;
			case "no":	/* Norwegian */
				this.monthArray = ['Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'];
				this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Des'];
				this.dayArray = ['Man','Tir','Ons','Tor','Fre','L&oslash;r','S&oslash;n'];	
				this.weekString = 'Uke';
				this.todayIsString = 'Dagen i dag er';
				this.todayString = 'I dag';
				this.timeString = 'Tid';
				break;	
			case "nl":	/* Dutch */
				this.monthArray = ['Januari','Februari','Maart','April','Mei','Juni','Juli','Augustus','September','Oktober','November','December'];
				this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Aug','Sep','Okt','Nov','Dec'];
				this.dayArray = ['Ma','Di','Wo','Do','Vr','Za','Zo'];
				this.weekString = 'Week';
				this.todayIsString = 'Vandaag';
				this.todayString = 'Vandaag';
				this.timeString = '';
				break;	
			case "es": /* Spanish */
				this.monthArray = ['Enero','Febrero','Marzo','April','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
				this.monthArrayShort =['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
				this.dayArray = ['Lun','Mar','Mie','Jue','Vie','Sab','Dom'];
				this.weekString = 'Semana';
				this.todayIsString = 'Hoy es';
				this.todayString = 'Hoy';
				this.timeString = 'Hora';
				break; 	
			case "pt-br":  /* Brazilian portuguese (pt-br) */
				this.monthArray = ['Janeiro','Fevereiro','Mar&ccedil;o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'];
				this.monthArrayShort = ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'];
				this.dayArray = ['Seg','Ter','Qua','Qui','Sex','S&aacute;b','Dom'];
				this.weekString = 'Sem.';
				this.todayIsString = 'Hoje &eacute;';
				this.todayString = 'Hoje';
				this.timeString = '';				
				break;
			case "fr":      /* French */
				this.monthArray = ['Janvier','F&eacute;vrier','Mars','Avril','Mai','Juin','Juillet','Ao&ucirc;t','Septembre','Octobre','Novembre','D&eacute;cembre'];		
				this.monthArrayShort = ['Jan','Fev','Mar','Avr','Mai','Jun','Jul','Aou','Sep','Oct','Nov','Dec'];
				this.dayArray = ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'];
				this.weekString = 'Semaine';
				this.todayIsString = "Aujourd'hui";
				this.todayString = 'Aujourd';
				this.timeString = 'Heure';
				break; 	
			case "da": /*Danish*/
				this.monthArray = ['januar','februar','marts','april','maj','juni','juli','august','september','oktober','november','december'];
				this.monthArrayShort = ['jan','feb','mar','apr','maj','jun','jul','aug','sep','okt','nov','dec'];
				this.dayArray = ['man','tirs','ons','tors','fre','l&oslash;r','s&oslash;n'];
				this.weekString = 'Uge';
				this.todayIsString = 'I dag er den';
				this.todayString = 'I dag';
				this.timeString = 'Tid';
				break;	
			case "it":	/* Italian*/
				this.monthArray = ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno','Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'];
				this.monthArrayShort = ['Gen','Feb','Mar','Apr','Mag','Giu','Lugl','Ago','Set','Ott','Nov','Dic'];
				this.dayArray = ['Lun',';Mar','Mer','Gio','Ven','Sab','Dom'];
				this.weekString = 'Settimana';
				this.todayIsString = 'Oggi &egrave; il';
				this.todayString = 'Oggi &egrave; il';
				this.timeString = '';
				break;		
			case "sv":	/* Swedish */
				this.monthArray = ['Januari','Februari','Mars','April','Maj','Juni','Juli','Augusti','September','Oktober','November','December'];
				this.monthArrayShort = ['Jan','Feb','Mar','Apr','Maj','Jun','Jul','Aug','Sep','Okt','Nov','Dec'];
				this.dayArray = ['M&aring;n','Tis','Ons','Tor','Fre','L&ouml;r','S&ouml;n'];
				this.weekString = 'Vecka';
				this.todayIsString = 'Idag &auml;r det den';
				this.todayString = 'Idag &auml;r det den';
				this.timeString = '';
				break;
			default:	/* English */
				this.monthArray = ['January','February','March','April','May','June','July','August','September','October','November','December'];
				this.monthArrayShort = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
				this.dayArray = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
				this.weekString = 'Week';
				this.todayIsString = '';
				this.todayString = 'Today';
				this.timeString = 'Time';
				break;
		}
	}
}


/************************************************************************************************************
*	Calendar model
*
*	Created:						January, 19th, 2007
*	@class Purpose of class:		Deal with dates and other calendar stuff
*			
*
* 	Update log:
*
************************************************************************************************************/
/**
* @constructor
* @class Purpose of class:	Data source for a calendar.
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.calendarModel = function(inputArray){
	var initialDay;							// Initial day (i.e. day in month)
	var initialMonth;						// Initial month(1-12)
	var initialYear;						// Initial Year(4 digits)
	var initialHour;						// Initial Hour(0-23)
	var initialMinute;						// Initial Minute
	
	var displayedDay;						// Currently displayed day
	var displayedMonth;						// Currently displayed month
	var displayedYear;						// Current displayed year	
	var displayedMinute;					// Currently displayed Minute
	var displayedHour;						// Current displayed Hour
	var languageCode;						// Current language code
	var languageModel;						// Reference to object of class DHTMLSuite.calendarLanguageModel
	var invalidDateRange;					// Array of invalid date ranges
	var weekStartsOnMonday;					// Should the week start on monday?	
	this.weekStartsOnMonday = true;	// Default - start week on monday	
	this.languageCode = 'en';
	this.invalidDateRange = new Array();
	
	this.__createDefaultModel(inputArray);
	
	
}

DHTMLSuite.calendarModel.prototype = 
{
	// {{{ setCallbackFunctionOnMonthChange()
    /**
     *	Automatically set start date from input field
     *	
     *	@param String functionName - Name of function to call when someone clicks on a date in the calendar. Argument to this function will be an array containing year,month,day,hour and minute
     *								 
     *
     *
     * @public	
     */		
	setCallbackFunctionOnMonthChange : function(functionName)
	{
		this.callbackFunctionOnMonthChange = functionName;
	}
	// }}}
	,
	// {{{ setInitialDateFromInput()
    /**
     *	Automatically set start date from input field
     *	
     *	@param Object inputReference - Reference to form input, i.e. id of form element, or a reference to the form element it's self
     *	@param String dateFormat - Format of value(examples: dd.mm.yyyy, yyyy.mm.dd, yyyy-mm-dd HH:
     *
     * @public	
     */		
	addInvalidDateRange : function(fromDateAsArray,toDateAsArray)
	{
		var index = this.invalidDateRange.length;
		this.invalidDateRange[index] = new Array();
		
		if(fromDateAsArray){
			fromDateAsArray.day = fromDateAsArray.day + '';
			fromDateAsArray.month = fromDateAsArray.month + '';
			fromDateAsArray.year = fromDateAsArray.year + '';
			if(!fromDateAsArray.month)fromDateAsArray.month = fromDateAsArray.month='1';
			if(!fromDateAsArray.day)fromDateAsArray.day = fromDateAsArray.day='1';
			if(fromDateAsArray.day.length==1)fromDateAsArray.day = '0' + fromDateAsArray.day;
			if(fromDateAsArray.month.length==1)fromDateAsArray.month = '0' + fromDateAsArray.month;
			this.invalidDateRange[index].fromDate = fromDateAsArray.year + fromDateAsArray.month + fromDateAsArray.day;
			
			
		}else{
			this.invalidDateRange[index].fromDate = false;
		}
		
		if(toDateAsArray){
			toDateAsArray.day = toDateAsArray.day + '';
			toDateAsArray.month = toDateAsArray.month + '';
			toDateAsArray.year = toDateAsArray.year + '';			
			if(!toDateAsArray.month)toDateAsArray.month = toDateAsArray.month='1';
			if(!toDateAsArray.day)toDateAsArray.day = toDateAsArray.day='1';
			if(toDateAsArray.day.length==1)toDateAsArray.day = '0' + toDateAsArray.day;
			if(toDateAsArray.month.length==1)toDateAsArray.month = '0' + toDateAsArray.month;
			this.invalidDateRange[index].toDate = toDateAsArray.year + toDateAsArray.month + toDateAsArray.day;
		}else{
			this.invalidDateRange[index].toDate = false;
		}
		
	}
	// }}}
	,
	// {{{ isDateWithinValidRange()
    /**
     *	Return true if given date is within valid date range.
     *
     *	@param Array inputDate - Associative array representing a date, keys in array : year,month and day
     *	@return Boolean dateWithinRange
     * @public	
     */		
	isDateWithinValidRange : function(inputDate)
	{
		if(this.invalidDateRange.length==0)return true;
		var month = inputDate.month+'';
		if(month.length==1)month = '0' + month;
		var day = inputDate.day+'';
		if(day.length==1)day = '0' + day;		
		var dateToCheck = inputDate.year + month + day;
		for(var no=0;no<this.invalidDateRange.length;no++){
			if(!this.invalidDateRange[no].fromDate && this.invalidDateRange[no].toDate>=dateToCheck)return false;
			if(!this.invalidDateRange[no].toDate && this.invalidDateRange[no].fromDate<=dateToCheck)return false;
			if(this.invalidDateRange[no].fromDate<=dateToCheck && this.invalidDateRange[no].toDate>=dateToCheck)return false;
		}	
		return true;
		
	}
	,	
	// {{{ setInitialDateFromInput()
    /**
     *	Automatically set start date from input field
     *	
     *	@param Object inputReference - Reference to form input, i.e. id of form element, or a reference to the form element it's self
     *	@param String dateFormat - Format of value(examples: dd.mm.yyyy, yyyy.mm.dd, yyyy-mm-dd HH:
     *
     * @public	
     */		
	setInitialDateFromInput : function(inputReference,format)
	{		
		var tmpDay;
		if(inputReference.value.length>0){			
			if (format.toLowerCase().indexOf('mmm') != -1){
				var monthPos = format.toLowerCase().indexOf('mmm');
				var monthName = inputReference.value.substr(monthPos,3);
				
				var pos = -1;
				var k;
				for (k = 0; k < 12; k++)
					if (this.languageModel.monthArrayShort[k].toLowerCase() == monthName.toLowerCase()){
						pos = k + 1;
						break;
					}
					
				var yearPos = format.indexOf('yyyy');
				this.initialYear = inputReference.value.substr(yearPos,4);		
				var dayPos = format.indexOf('dd');
				tmpDay = inputReference.value.substr(dayPos,2);		
				this.initialDay = tmpDay;

				if (pos == -1){
					monthName = inputReference.value.substr(monthPos,2);
					pos = monthName / 1;
					
					this.initialYear = inputReference.value.substr(yearPos-1,4);		
					tmpDay = inputReference.value.substr(dayPos-1,2);		
					this.initialDay = tmpDay;
				}

				this.initialMonth = pos;
				
				
				var empty = "blank";
				empty = "nt";
			}
			else		
			if(!format.match(/^[0-9]*?$/gi)){
				var items = inputReference.value.split(/[^0-9]/gi);
				var positionArray = new Array();
				//AMP-2480 path to make it compatible with java date formats 
				positionArray['m'] = (format.indexOf('mm')==-1)?format.indexOf('MM'):format.indexOf('mm');
				if(positionArray['m']==-1)positionArray['m'] = format.indexOf('m');
				positionArray['d'] = format.indexOf('dd');
				if(positionArray['d']==-1)positionArray['d'] = format.indexOf('d');
				positionArray['y'] = format.indexOf('yyyy');
				positionArray['h'] = format.indexOf('hh');
				positionArray['i'] = format.indexOf('ii');
				
				var positionArrayNumeric = Array();
				positionArrayNumeric[0] = positionArray['m'];
				positionArrayNumeric[1] = positionArray['d'];
				positionArrayNumeric[2] = positionArray['y'];
				positionArrayNumeric[3] = positionArray['h'];
				positionArrayNumeric[4] = positionArray['i'];
				
				
				positionArrayNumeric = positionArrayNumeric.sort(this.__calendarSortItems);
				var itemIndex = -1;
				this.initialHour = '00';
				this.initialMinute = '00';
				for(var no=0;no<positionArrayNumeric.length;no++){
					if(positionArrayNumeric[no]==-1)continue;
					itemIndex++;
					if(positionArrayNumeric[no]==positionArray['m']){
						this.initialMonth = items[itemIndex];
						continue;
					}
					if(positionArrayNumeric[no]==positionArray['y']){
						this.initialYear = items[itemIndex];
						continue;
					}	
					if(positionArrayNumeric[no]==positionArray['d']){
						tmpDay = items[itemIndex];
						continue;
					}	
					if(positionArrayNumeric[no]==positionArray['h']){
						this.initialHour = items[itemIndex];
						continue;
					}	
					if(positionArrayNumeric[no]==positionArray['i']){
						this.initialMinute = items[itemIndex];
						continue;
					}	
				}
	
				this.initialMonth = this.initialMonth / 1;
				tmpDay = tmpDay / 1;
				this.initialDay = tmpDay;
			}else{		
				//AMP-2480 path to make it compatible with java date formats 
				var monthPos = (format.indexOf('mm')==-1)?format.indexOf('MM'):format.indexOf('mm');
				//format.indexOf('mm');
				this.initialMonth = inputReference.value.substr(monthPos,2)/1;	
				var yearPos = format.indexOf('yyyy');
				this.initialYear = inputReference.value.substr(yearPos,4);		
				var dayPos = format.indexOf('dd');
				tmpDay = inputReference.value.substr(dayPos,2);		
				this.initialDay = tmpDay;
				var hourPos = format.indexOf('hh');
				if(hourPos>=0){
					tmpHour = inputReference.value.substr(hourPos,2);	
					this.initialHour = tmpHour;
				}else{
					this.initialHour = '00';
				}
				var minutePos = format.indexOf('ii');
				if(minutePos>=0){
					tmpMinute = inputReference.value.substr(minutePos,2);	
					this.initialMinute = tmpMinute;
				}else{
					this.initialMinute = '00';
				}	
			}
		}	
		this.__setDisplayedDateToInitialData();
	}
	// }}}
	,
	// {{{ __setDisplayedDateToInitialData()
    /**
     *	Set displayed date equal to initial data.
     *	
     * @private	
     */			
	__setDisplayedDateToInitialData : function()
	{
		this.displayedYear = this.initialYear;
		this.displayedMonth = this.initialMonth;
		this.displayedDay = this.initialDay;
		this.displayedHour = this.initialHour;
		this.displayedMinute = this.initialMinute;	
	}
	// }}}
	,
	// {{{ __calendarSortItems()
    /**
     *	Sort calendar items.
     *	
     * @private	
     */		
	__calendarSortItems : function(a,b)
	{
		return a/1 - b/1;
	}
	// }}}
	,
	// {{{ setLanguageCode()
    /**
     *	Set language code.
     *	
     * @public	
     */		
	setWeekStartsOnMonday : function(weekStartsOnMonday)
	{
		this.weekStartsOnMonday = weekStartsOnMonday;
	}
	// }}}
	,
	// {{{ setLanguageCode()
    /**
     *	Set language code.
     *	
     * @public	
     */		
	setLanguageCode : function(languageCode)
	{
		this.languageModel = new DHTMLSuite.calendarLanguageModel(languageCode);	// Default english language model
	}
	// }}}
	,
	// {{{ __isLeapYear()
    /**
     *	Check for leap years.
     *	
     * @private	
     */			
	__isLeapYear : function(inputYear)
	{
		if(inputYear%400==0||(inputYear%4==0&&inputYear%100!=0)) return true;
		return false;		
	}
	// }}}
	,
	// {{{ getWeekStartsOnMonday()
    /**
     *	Return true if week starts on monday
     *	
     * @private	
     */		
	getWeekStartsOnMonday : function()
	{
		return this.weekStartsOnMonday;
	}
	// }}}
	,
	// {{{ __createDefaultModel()
    /**
     *	Create default calendar model
     *	
     * @private	
     */	
	__createDefaultModel : function(inputArray)
	{
		var d = new Date();
		this.initialYear = d.getFullYear();
		this.initialMonth = d.getMonth() + 1;
		this.initialDay = d.getDate();
		this.initialHour = d.getHours();

		if(inputArray){	/* Initial date data sent to the constructor ? */
			if(inputArray.initialYear)this.initialYear = inputArray.initialYear;
			if(inputArray.initialMonth)this.initialMonth = inputArray.initialMonth;
			if(inputArray.initialDay)this.initialDay = inputArray.initialDay;
			if(inputArray.initialHour)this.initialHour = inputArray.initialHour;
			if(inputArray.initialMinute)this.initialMinute = inputArray.initialMinute;
			if(inputArray.languageCode)this.languageCode = inputArray.languageCode;
		}		
		this.displayedYear = this.initialYear;
		this.displayedMonth = this.initialMonth;
		this.displayedDay = this.initialDay;
		this.displayedHour = this.initialHour;
		this.displayedMinute = this.initialMinute;
		
		this.languageModel = new DHTMLSuite.calendarLanguageModel();	// Default english language model
	}	
	// }}}
	,
	// {{{ __getDisplayedYear()
    /**
     *	Return current displayed day
     *	
     * @private	
     */		
	__getDisplayedDay : function()
	{
		return this.displayedDay;
	}
	,
	// {{{ __getDisplayedHourWithLeadingZeros()
    /**
     *	Return current displayed day
	 *
     * @private	
     */		
	__getDisplayedHourWithLeadingZeros : function()
	{
		var retVal = this.__getDisplayedHour()+'';
		if(retVal.length==1)retVal = '0' + retVal;
		return retVal;
	}
	// }}}
	,
	// {{{ __getDisplayedMinuteWithLeadingZeros()
    /**
     *	Return current displayed day
     *	
     * @private	
     */		
	__getDisplayedMinuteWithLeadingZeros : function()
	{
		var retVal = this.__getDisplayedMinute()+'';
		if(retVal.length==1)retVal = '0' + retVal;
		return retVal;
	}
	// }}}
	,
	// {{{ __getDisplayedDayWithLeadingZeros()
    /**
     *	Return current displayed day
     *	
     * @private	
     */		
	__getDisplayedDayWithLeadingZeros : function()
	{
		var retVal = this.__getDisplayedDay()+'';
		if(retVal.length==1)retVal = '0' + retVal;
		return retVal;
	}
	// }}}
	,
	// {{{ __getDisplayedMonthNumberWithLeadingZeros()
    /**
     *	Return current displayed day
     *
     * @private	
     */		
	__getDisplayedMonthNumberWithLeadingZeros : function()
	{
		var retVal = this.__getDisplayedMonthNumber()+'';
		if(retVal.length==1)retVal = '0' + retVal;
		return retVal;
	}
	// }}}
	,	
	// {{{ __getDisplayedYear()
    /**
     *	Return current displayed year
     *	
     * @private	
     */		
	__getDisplayedYear : function()
	{
		return this.displayedYear;
	}
	// }}}
	,
	// {{{ __getDisplayedHour()
    /**
     *	Return current displayed hour
     *	
     * @private	
     */		
	__getDisplayedHour : function()
	{
		if(!this.displayedHour)this.displayedHour = 0;
		return this.displayedHour;
	}
	// }}}
	,
	// {{{ __getDisplayedMinute()
    /**
     *	Return current displayed minute
     *	
     * @private	
     */		
	__getDisplayedMinute : function()
	{
		if(!this.displayedMinute)this.displayedMinute = 0;
		return this.displayedMinute;
	}
	// }}}
	,
	// {{{ __getDisplayedMonthNumber()
    /**
     *	Return month number (1-12)
     *	
     * @private	
     */		
	__getDisplayedMonthNumber : function()
	{
		return this.displayedMonth;
	}	,
	// {{{ __getInitialYear()
    /**
     *	Return current initial day
     *	
     * @private	
     */		
	__getInitialDay : function()
	{
		return this.initialDay;
	}
	// }}}
	,	
	// {{{ __getInitialYear()
    /**
     *	Return current initial year
	 *
     * @private	
     */		
	__getInitialYear : function()
	{
		return this.initialYear;
	}
	// }}}
	,
	// {{{ __getInitialMonthNumber()
    /**
     *	Return month number (1-12)
     *	
     * @private	
     */		
	__getInitialMonthNumber : function()
	{
		return this.initialMonth;
	}
	,
	// {{{ __getMonthNameByMonthNumber()
    /**
     *	Return month name from month number(1-12)
     *	
     *	@param Integer monthNumber - Month from 1 to 12.
     *
     * @private	
     */		
	__getMonthNameByMonthNumber : function(monthNumber)
	{
		return this.languageModel.monthArray[monthNumber-1];	
	}
	// }}}
	,
	// {{{ __moveOneYearBack()
    /**
     *	Set currently displayed year one year back.
     *
     * @private	
     */		
	__moveOneYearBack : function()
	{
		this.displayedYear--;		
	}
	// }}}
	,
	// {{{ __moveOneYearForward()
    /**
     *	Move the display one year ahead in time.
     *
     * @private	
     */		
	__moveOneYearForward : function()
	{
		this.displayedYear++;
	}
	// }}}
	,	
	// {{{ __moveOneMonthBack()
    /**
     *	Set currently displayed month one back.
     *
     * @private	
     */	
	__moveOneMonthBack : function()
	{
		this.displayedMonth--;
		if(this.displayedMonth<1){
			this.displayedMonth = 12;
			this.displayedYear--;
		}
	}
	// }}}
	,
	// {{{ __moveOneMonthForward()
    /**
     *	Set currently displayed month one month ahead.
     *	
     * @private	
     */		
	__moveOneMonthForward : function()
	{
		this.displayedMonth++;
		if(this.displayedMonth>12){
			this.displayedMonth=1;
			this.displayedYear++;
		}	
	}	
	// }}}
	,
	// {{{ __setDisplayedYear()
    /**
     *	Set new year
     *	
     *	@param Integer year (4 digits)
     *
	 *	@return Boolean success - return true if year have actually changed, false otherwise
     * @private	
     */		
	__setDisplayedYear : function(year)
	{
		var success = year!=this.displayedYear;
		this.displayedYear = year;
		return success
	}
	// }}}
	,
	// {{{ __setDisplayedMonth()
    /**
     *	Set new month
     *	
     *	@param Integer month ( 1 - 12)
     *
     *	@return Boolean success - return true if month have actually changed, false otherwise
     * @private	
     */		
	__setDisplayedMonth : function(month)
	{
		var success = month!=this.displayedMonth;
		this.displayedMonth = month;
		return success;
	}
	,
	// {{{ __setDisplayedDay()
    /**
     *	Set new displayed day
     *	
     *	@param day in month
     *     
     * @private	
     */		
	__setDisplayedDay : function(day)
	{
		this.displayedDay = day;
	}
	// }}}
	,
	// {{{ __setDisplayedHour()
    /**
     *	Set new displayed hour
     *	
     *	@param hour (0-23)
     *     
     * @private	
     */		
	__setDisplayedHour : function(hour)
	{
		this.displayedHour = hour/1;
	}
	// }}}
	,
	// {{{ __setDisplayedMinute()
    /**
     *	Set new displayed minute
     *	
     *	@param minute (0-59)
     *     
     * @private	
     */			
	__setDisplayedMinute : function(minute)
	{
		this.displayedMinute = minute/1;
	}
	// }}}
	,
	// {{{ __getPreviousYearAndMonthAsArray()
    /**
     *	Return previous month as an array(year and month)
     *	
     *	@return Array year and month(numeric)
     *
     * @private	
     */		
	__getPreviousYearAndMonthAsArray : function()
	{
		var month = this.displayedMonth-1;
		var year = this.displayedYear;
		if(month==0){
			month = 12;
			year = year-1;
		}	
		var retArray = [year,month];
		return retArray;
		
	}
	// }}}
	,
	// {{{ __getNumberOfDaysInCurrentDisplayedMonth()
    /**
     *	Return number of days in currently displayed month.
     *	
     *	@param Integer monthNumber - Month from 1 to 12.
     *
     * @private	
     */	
	__getNumberOfDaysInCurrentDisplayedMonth : function()
	{
		return this.__getNumberOfDaysInAMonthByMonthAndYear(this.displayedYear,this.displayedMonth);
	}
	// }}}
	,
	// {{{ __getNumberOfDaysInAMonthByMonthAndYear()
    /**
     *	Return number of days in given month.
     *	
     *	@param Integer year - Year(4 digits) 
     *	@param Integer month - Month(1-12)
     *
     * @private	
     */		
	__getNumberOfDaysInAMonthByMonthAndYear : function(year,month)
	{		
		var daysInMonthArray = [31,28,31,30,31,30,31,31,30,31,30,31];
		var daysInMonth = daysInMonthArray[month-1];
		if(daysInMonth==28){
			if(this.__isLeapYear(year))daysInMonth=29;
		}
		return daysInMonth/1;				
	}
	// }}}
	,
	// {{{ __getStringWeek()
    /**
     *	Return the string "Week"
     *	
     * @private	
     */		
	__getStringWeek : function()
	{	
		return this.languageModel.weekString;
	}	
	// }}}
	,
	// {{{ __getDaysMondayToSunday()
    /**
     *	Return an array of days from monday to sunday
     *	
     * @private	
     */		
	__getDaysMondayToSunday : function()
	{
		return this.languageModel.dayArray;	
	}
	// }}}
	,
	// {{{ __getDaysSundayToSaturday()
    /**
     *	Return an array of days from sunday to saturday
     *	
     * @private	
     */		
	__getDaysSundayToSaturday : function()
	{
		var retArray = this.languageModel.dayArray.concat();
		var lastDay = new Array(retArray[retArray.length-1]);
		retArray.pop();		
		return lastDay.concat(retArray);		
	}
	// }}}
	,
	// {{{ __getWeekNumberFromDayMonthAndYear()
    /**
     *	Return week in year from year,month and day
     *	
     * @private	
     */		
	__getWeekNumberFromDayMonthAndYear : function(year,month,day)
	{
		day = day/1;
		year = year /1;
	    month = month/1;
	    var a = Math.floor((14-(month))/12);
	    var y = year+4800-a;
	    var m = (month)+(12*a)-3;
	    var jd = day + Math.floor(((153*m)+2)/5) + 
	                 (365*y) + Math.floor(y/4) - Math.floor(y/100) + 
	                 Math.floor(y/400) - 32045;      // (gregorian calendar)
	    var d4 = (jd+31741-(jd%7))%146097%36524%1461;
	    var L = Math.floor(d4/1460);
	    var d1 = ((d4-L)%365)+L;
	    NumberOfWeek = Math.floor(d1/7) + 1;
	    return NumberOfWeek;    		
	}
	// }}}
	,
	// {{{ __getRemainingDaysInPreviousMonthAsArray()
    /**
     *	Return number of days remaining in previous month, i.e. in the view before first day of current month starts some day in the week.
     *	
     * @private	
     */		
	__getRemainingDaysInPreviousMonthAsArray : function()
	{
		// Figure out when this month starts
		var d = new Date();
		d.setFullYear(this.displayedYear);		
		d.setDate(1);		
		d.setMonth(this.displayedMonth-1);
			
		var dayStartOfMonth = d.getDay();
		if(this.weekStartsOnMonday){
			if(dayStartOfMonth==0)dayStartOfMonth=7;
			dayStartOfMonth--;
		}
		
		var previousMonthArray = this.__getPreviousYearAndMonthAsArray();

		var daysInPreviousMonth = this.__getNumberOfDaysInAMonthByMonthAndYear(previousMonthArray[0],previousMonthArray[1]);
		var returnArray = new Array();
		for(var no=0;no<dayStartOfMonth;no++){
			returnArray[returnArray.length] = daysInPreviousMonth-dayStartOfMonth+no+1;
		}
		return returnArray;
		
		
	}
	// }}}
	,
	// {{{ __getMonthNames()
    /**
     *	Return an array of month names
     *	
     * @private	
     */		
	__getMonthNames : function()
	{
		return this.languageModel.monthArray;			
	}
	// }}}
	,
	// {{{ __getTodayAsString()
    /**
     *	Return the string "Today" in the specified language
     *
     * @private	
     */		
	__getTodayAsString : function()
	{
		return this.languageModel.todayString;
	}
	// }}}
	,
	// {{{ __getTimeAsString()
    /**
     *	Return the string "Time" in the specified language
     *
     * @private	
     */		
	__getTimeAsString : function()
	{
		return this.languageModel.timeString;
	}
}



/**
* @constructor
* @class Calendar widget (<a href="../../demos/demo-calendar-1.html" target="_blank">demo</a>).
* @version				1.0
* @version 1.0
* 
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/



DHTMLSuite.calendar = function(propertyArray)
{
	var id;								// Unique identifier - optional
	var divElement;
	var divElementContent;	// Div element for the content inside the calendar
	var divElementHeading;
	var divElementNavigationBar;
	var divElementMonthView;			// Div for the main view - weeks, days, months etc.
	var divElementMonthNameInHeading;
	var divElementYearInHeading;
	var divElementBtnPreviousYear;		// Button - previous year
	var divElementBtnNextYear;			// Button - next year
	var divElementBtnPreviousMonth;		// Button - previous Month
	var divElementBtnNextMonth;			// Button - next Month
	var divElementYearDropdown;			// Dropdown box - years
	var divElementYearDropDownParentYears;	// Inner div inside divElementYearDropdown which is parent to all the small year divs.
	var divElementHourDropDownParentHours;	// Inner div inside divElementYearDropdown which is parent to all the small year hours.
	var divElementHourDropDown;			// Drop down hours
	var divElementMinuteDropDownParentMinutes;	// Inner div inside divElementYearDropdown which is parent to all the small year Minutes.
	var divElementMinuteDropDown;			// Drop down Minutes
	var divElementTodayInNavigationBar;	// Today in navigation bar.
	var divElementHourInTimeBar;		// Div for hour in timer bar
	var divElementMinuteInTimeBar;		// Div for minute in timer bar
	var divElementTimeStringInTimeBar;		// Div for "Time" string in timer bar
	
	var iframeElement;
	var iframeElementDropDowns;	
	var calendarModelReference;			// Reference to object of class calendarModel
	var objectIndex;
	var targetReference;				// Where to insert the calendar.
	var layoutCSS;
	var isDragable;						// Is the calendar dragable - default = false
	var referenceToDragDropObject;		// Reference to object of class DHTMLSuite.dragDropSimple
	var scrollInYearDropDownActive;		// true when mouse is over up and down arrows in year dropdown
	var scrollInHourDropDownActive;		// true when mouse is over up and down arrows in hour dropdown
	var scrollInMinuteDropDownActive;		// true when mouse is over up and down arrows in minute dropdown
	var yearDropDownOffsetInYear;		// Offset in year relative to current displayed year.
	var hourDropDownOffsetInHour;		// Offset in hours relative to current displayed hour.
	var minuteDropDownOffsetInHour;		// Offset in minute relative to current displayed minute.
	
	var displayCloseButton;					// Display close button at the top right corner
	var displayNavigationBar;				// Display the navigation bar ? ( default = true)
	var displayTodaysDateInNavigationBar;	// Display the string "Today" in the navigation bar(default = true)
	var displayTimeBar;					// Display timer bar - default = false;
	
	var positioningReferenceToHtmlElement;	// reference to html element to position the calendar at
	var positioningOffsetXInPixels;			// Offset in positioning when positioning calendar at a element
	var positioningOffsetYInPixels;			// Offset in positioning when positioning calendar at a element
	var htmlElementReferences;
	var minuteDropDownInterval;				// Minute drop down interval(interval between each row in the minute drop down list)
	
	var numberOfRowsInMinuteDropDown;		// Number of rows in minute drop down. (default = 10)
	var numberOfRowsInHourDropDown;			// Number of rows in hour drop down. (default = 10)
	var numberOfRowsInYearDropDown;			// Number of rows in year drop down. (default = 10)
	
	this.displayTimeBar = false;
	this.minuteDropDownInterval = 5;
	this.htmlElementReferences = new Array();
	this.positioningReferenceToHtmlElement = false;
	this.displayCloseButton = true;		// Default value - close button visible at the top right corner.
	this.displayNavigationBar = true;
	this.displayTodaysDateInNavigationBar = true;
	this.yearDropDownOffsetInYear = 0;
	this.hourDropDownOffsetInHour = 0;
	this.minuteDropDownOffsetInHour = 0;
	this.minuteDropDownOffsetInMinute = 0;
	this.layoutCSS = 'calendar.css';
	this.isDragable = false;
	this.scrollInYearDropDownActive = false;
	this.scrollInHourDropDownActive = false;
	this.scrollInMinuteDropDownActive = false;
	
	this.numberOfRowsInMinuteDropDown = 10;
	this.numberOfRowsInHourDropDown = 10;
	this.numberOfRowsInYearDropDown = 10;
	
	var callbackFunctionOnDayClick;			// Name of call back function to call when you click on a day
	var callbackFunctionOnClose;			// Name of call back function to call when the calendar is closed.
	var callbackFunctionOnMonthChange;		// Name of call back function to call when the month is changed in the view
		
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
		
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;	
	
	if(propertyArray)this.__setInitialData(propertyArray);
	
	
}

DHTMLSuite.calendar.prototype = 
{
	// {{{ callbackFunctionOnDayClick()
    /**
     *	Specify call back function - click on days in calendar
     *	
     *	@param String functionName - Name of function to call when someone clicks on a date in the calendar. Argument to this function will be an array containing year,month,day,hour and minute
     *								 
     * @public	
     */		
	setCallbackFunctionOnDayClick : function(functionName)
	{
		this.callbackFunctionOnDayClick = functionName;	
	}
	// }}}
	,
	// {{{ setCallbackFunctionOnMonthChange()
    /**
     *	Automatically set start date from input field
     *	
     *	@param String functionName - Name of function to call when someone clicks on a date in the calendar. Argument to this function will be an array containing year,month,day,hour and minute
     *								 
     * @public	
     */		
	setCallbackFunctionOnMonthChange : function(functionName)
	{
		if(!this.calendarModelReference){
			this.calendarModelReference = new DHTMLSuite.calendarModel();
		}
		this.callbackFunctionOnMonthChange = functionName;
	}
	// }}}
	,
	// {{{ setCallbackFunctionOnClose()
    /**
     *	Specify call back function - calendar close
     *	
     *	@param String functionName - Function name to call when the calendar is closed. This function will receive one argument which is an associative array of the properties year,month,day,hour,minute and calendarRef. calendarRef is a reference to the DHTMLSuite.calendar object.
     *
     * @public	
     */		
	setCallbackFunctionOnClose : function(functionName)
	{
		this.callbackFunctionOnClose = functionName;
	}
	,
	// {{{ setCalendarModelReference()
    /**
     *	Automatically set start date from input field
     *	
     *	@param Object calendarModelReference - Reference to an object of class DHTMLSuite.calendarModel
     *
     * @public	
     */		
	setCalendarModelReference : function(calendarModelReference)
	{
		this.calendarModelReference = calendarModelReference;
	}
	// }}}
	,
	// {{{ setCalendarPositionByHTMLElement()
    /**
     *	Make the calendar absolute positoned and positioning it next by a HTML element
     *	
     *	@param Object referenceToHtmlElement - Reference to html element
     *	@param Integer offsetXInPixels - X offset in pixels
     *	@param Integer offsetYInPixels - Y offset in pixels.
     *
     *
     * @public	
     */		
	setCalendarPositionByHTMLElement : function(referenceToHtmlElement,offsetXInPixels,offsetYInPixels)
	{
		if(typeof referenceToHtmlElement == 'string'){
			referenceToHtmlElement = document.getElementById(referenceToHtmlElement);
		}			
		this.positioningReferenceToHtmlElement = referenceToHtmlElement;
		if(!offsetXInPixels)offsetXInPixels = 0;
		if(!offsetYInPixels)offsetYInPixels = 0;
		this.positioningOffsetXInPixels = offsetXInPixels;
		this.positioningOffsetYInPixels = offsetYInPixels;
	}
	// }}}
	,
	// {{{ addHtmlElementReference()
    /**
     *	Add a reference to form field element - a reference to this object will be sent back in the call back function.
     *	
     *	@param String key - Key in the array for this element - To make it easier for you to pick it up later.
     *	@param Object referenceToHtmlElement - Reference to html element
     *
     * @public	
     */		
	addHtmlElementReference : function(key,referenceToHtmlElement)
	{
		if(typeof referenceToHtmlElement == 'string'){
			referenceToHtmlElement = document.getElementById(referenceToHtmlElement);
		}			
		if(key){
			this.htmlElementReferences[key] = referenceToHtmlElement;
		}
	}
	// }}}
	,	
	// {{{ setDisplayCloseButton()
    /**
     *	Specify close button visibility
     *	
     *	@param Boolean displayCloseButton - Display close button.
     *
     *
     * @public	
     */		
	getHtmlElementReferences : function()
	{
		return this.htmlElementReferences;	
	}
	// }}}
	,
	// {{{ setDisplayCloseButton()
    /**
     *	Specify close button visibility
     *	
     *	@param Boolean displayCloseButton - Display close button.
     *
     * @public	
     */		
	setDisplayCloseButton : function(displayCloseButton)
	{
		this.displayCloseButton = displayCloseButton;
	}
	// }}}
	,
	// {{{ setTargetReference()
    /**
     *	Automatically set start date from input field
     *	
     *	@param Object targetReference - Id or direct reference to an element on your web page. The calender will be inserted as child of this element
     *
     * @public	
     */		
	setTargetReference : function(targetReference)
	{
		if(typeof targetReference == 'string'){
			targetReference = document.getElementById(targetReference);
		}		
		this.targetReference = targetReference;		
	}
	// }}}
	,
	// {{{ setIsDragable()
    /**
     *	Automatically set start date from input field
     *	
     *	@param Boolean isDragable - Should the calendar be dragable?
     *
     * @public	
     */		
	setIsDragable : function(isDragable){
		this.isDragable = isDragable;
	}
	// }}}
	,
	// {{{ resetViewDisplayedMonth()
    /**
     *	Reset current display, i.e. display data for the inital set month.
     *	
     * @public	
     */		
	resetViewDisplayedMonth : function()
	{
		if(!this.divElement)return;
		if(!this.calendarModelReference){
			this.calendarModelReference = new DHTMLSuite.calendarModel();
		}
		this.calendarModelReference.__setDisplayedDateToInitialData();
		this.__populateCalendarHeading();	// Populate heading with data
		this.__populateMonthViewWithMonthData();	// Populate month view with month data
	}
	// }}}
	,
	// {{{ setLayoutCss()
    /**
     *	Specify new name of css file
     *	
     *	@param String nameOfCssFile - Name of css file
     *
     * @public	
     */		
	setLayoutCss : function(nameOfCssFile)
	{
		this.layoutCSS = nameOfCssFile;		
	}
	// }}}
	,
	// {{{ __init()
    /**
     *	Initializes the widget
     *	
     * @private	
     */			
	__init : function()
	{
		if(!this.divElement){
			DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css
			if(!this.calendarModelReference){
				this.calendarModelReference = new DHTMLSuite.calendarModel();
			}
			this.__createPrimaryHtmlElements();	// Create main html elements for the calendar
			this.__createCalendarHeadingElements();	// Create html elements for the heading
			this.__createNavigationBar();	// Create the navigation bar below the heading.
			this.__populateNavigationBar();	// Fill navigation bar with todays date.
			this.__populateCalendarHeading();	// Populate heading with data
			this.__createCalendarMonthView();	// Create div element for the main view, i.e. days, weeks months, etc.
			this.__populateMonthViewWithMonthData();	// Populate month view with month data
			this.__createTimeBar();				// Create div elements for the timer bar.
			this.__populateTimeBar();			// Populate the timer bar
			this.__createDropDownYears();
			this.__populateDropDownYears();
			this.__positionDropDownYears();
			this.__createDropDownMonth();
			this.__populateDropDownMonths();
			this.__positionDropDownMonths();
			
			this.__createDropDownHours();
			this.__populateDropDownHours();
			this.__positionDropDownHours();
			
			this.__createDropDownMinutes();
			this.__populateDropDownMinutes();
			this.__positionDropDownMinutes();
			
			this.__addEvents();	
		}else{
			this.divElement.style.display = 'block';
			this.__populateCalendarHeading();	// Populate heading with data	
			this.__populateMonthViewWithMonthData();	// Populate month view with month data
		}	
		this.__resizePrimaryIframeElement();
		
	}
	// }}}
	,
	// {{{ display()
    /**
     *	Displays the calendar
     *	
     * @public	
     */		
	display : function()
	{
		if(!this.divElement)this.__init();
		this.__positionCalendar();
		this.divElement.style.display='block';
		
	}
	// }}}
	,
	// {{{ hide()
    /**
     *	Closes the calendar
     *	
     * @public	
     */		
	hide : function()
	{
		this.divElement.style.display='none';
		this.divElementYearDropdown.style.display='none';
		this.divElementMonthDropdown.style.display='none';
	}
	// }}}
	,
	// {{{ isVisible()
    /**
     *	Is the calendar visible
     *	
     * @private	
     */		
	isVisible : function()
	{
		if(!this.divElement)return false;
		return this.divElement.style.display=='block'?true:false;	
	}
	// }}}
	,
	// {{{ setInitialDateFromInput()
    /**
     *	Set intial date from form input
     *	
     *
     * @private	
     */		
	setInitialDateFromInput : function(inputReference,format)
	{
		if(!this.calendarModelReference){
			this.calendarModelReference = new DHTMLSuite.calendarModel();
		}
		this.calendarModelReference.setInitialDateFromInput(inputReference,format);
	}
	// }}}
	,
	// {{{ setDisplayedYear()
    /**
     *	Set a new displayed year
     *
     *
     * @public	
     */		
	setDisplayedYear : function(year)
	{
		var success = this.calendarModelReference.__setDisplayedYear(year);	// Year has actually changed		
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();			
		if(success)this.__handleCalendarCallBack('monthChange');
	}
	// }}}
	,
	// {{{ setDisplayedMonth()
    /**
     *	Set a new displayed month
     *
     *
     * @public	
     */		
	setDisplayedMonth : function(month)
	{
		var success = this.calendarModelReference.__setDisplayedMonth(month);	// Month have actually changed		
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();				
		if(success)this.__handleCalendarCallBack('monthChange');
	}
	// }}}
	,
	// {{{ setDisplayedHour()
    /**
     *	Set new displayed hour.
     *
     * @private	
     */			
	setDisplayedHour : function(hour)
	{
		this.calendarModelReference.__setDisplayedHour(hour);	// Month have actually changed	
		this.__populateTimeBar();
		
	}
	// }}}
	,
	// {{{ setDisplayedMinute()
    /**
     *	Set new displayed minute.
     *
     * @private	
     */			
	setDisplayedMinute : function(minute)
	{
		this.calendarModelReference.__setDisplayedMinute(minute);	// Month have actually changed	
		this.__populateTimeBar();
		
	}
	// }}}
	,
	// {{{ __createDropDownMonth()
    /**
     *	Create main div elements for the month drop down.
     *	
     * @private	
     */		
	__createDropDownMonth : function()
	{
		this.divElementMonthDropdown = document.createElement('DIV');
		this.divElementMonthDropdown.style.display='none';
		this.divElementMonthDropdown.className = 'DHTMLSuite_calendar_monthDropDown';
		document.body.appendChild(this.divElementMonthDropdown);			
	}
	// }}}
	,
	// {{{ __populateDropDownMonths()
    /**
     *	Populate month drop down.
     *	
     * @private	
     */		
	__populateDropDownMonths : function()
	{
		this.divElementMonthDropdown.innerHTML = '';	// Initially clearing drop down.
		var ind = this.objectIndex;	// Get a reference to this object in the global object array.
		var months = this.calendarModelReference.__getMonthNames();	// Get an array of month name according to current language settings
		for(var no=0;no<months.length;no++){	// Loop through names
			var div = document.createElement('DIV');	// Create div element
			div.className = 'DHTMLSuite_calendar_dropDownAMonth';
			if((no+1)==this.calendarModelReference.__getDisplayedMonthNumber())div.className = 'DHTMLSuite_calendar_yearDropDownCurrentMonth';	// Highlight current month.
			div.innerHTML = months[no];	// Set text of div
			div.id = 'DHTMLSuite_calendarMonthPicker' + (no+1);	// Set id of div. this is used inside the __setMonthFromDropdown in order to pick up the date.
			div.onmouseover = this.__mouseoverMonthInDropDown;
			div.onmouseout = this.__mouseoutMonthInDropDown;
			div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setMonthFromDropdown(e); } 
			this.divElementMonthDropdown.appendChild(div);	
			DHTMLSuite.commonObj.__addEventElement(div);						
		}
		
	}
	// }}}
	,
	// {{{ __createDropDownYears()
    /**
     *	Create drop down box for years
     *	
     * @private	
     */		
	__createDropDownYears : function()
	{
		this.divElementYearDropdown = document.createElement('DIV');
		this.divElementYearDropdown.style.display='none';
		this.divElementYearDropdown.className = 'DHTMLSuite_calendar_yearDropDown';
		document.body.appendChild(this.divElementYearDropdown);
		
	}
	,
	// {{{ __createDropDownHours()
    /**
     *	Create drop down box for years
     *	
     * @private	
     */		
	__createDropDownHours : function()
	{
		this.divElementHourDropdown = document.createElement('DIV');
		this.divElementHourDropdown.style.display='none';
		this.divElementHourDropdown.className = 'DHTMLSuite_calendar_hourDropDown';
		document.body.appendChild(this.divElementHourDropdown);
		
	}
	// }}}
	,
	// {{{ __createDropDownMinutes()
    /**
     *	Create minute drop down box.
     *	
     * @private	
     */		
	__createDropDownMinutes : function()
	{
		this.divElementMinuteDropdown = document.createElement('DIV');
		this.divElementMinuteDropdown.style.display='none';
		this.divElementMinuteDropdown.className = 'DHTMLSuite_calendar_minuteDropDown';
		document.body.appendChild(this.divElementMinuteDropdown);		
		
	}
	// }}}
	,
	// {{{ __populateDropDownMinutes()
    /**
     *	Populate - minute dropdown.
     *	
     * @private	
     */		
	__populateDropDownMinutes : function()
	{
		var ind = this.objectIndex;		
		this.divElementMinuteDropdown.innerHTML = '';	
		
		// Previous minute
		var divPrevious = document.createElement('DIV');
		divPrevious.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
		divPrevious.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownMinutes(e); } ;
		divPrevious.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownMinutes(e); } ;	
		this.divElementMinuteDropdown.appendChild(divPrevious);	
		DHTMLSuite.commonObj.__addEventElement(divPrevious);			

		this.divElementMinuteDropDownParentMinutes = document.createElement('DIV');
		this.divElementMinuteDropdown.appendChild(this.divElementMinuteDropDownParentMinutes);
		this.__populateMinutesInsideDropDownMinutes(this.divElementMinuteDropDownParentMinutes);
		
		// Next Minute	
		var divNext = document.createElement('DIV');
		divNext.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
		divNext.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownMinutes(e); } ;
		divNext.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownMinutes(e); } ;
		DHTMLSuite.commonObj.__addEventElement(divNext);
		this.divElementMinuteDropdown.appendChild(divNext);		
		
		if(60 / this.minuteDropDownInterval	< this.numberOfRowsInMinuteDropDown){
			divPrevious.style.display='none';
			divNext.style.display='none';
		}				
	}
	// }}}
	,
	// {{{ __populateMinutesInsideDropDownMinutes()
    /**
     *	Populate - minutes inside minute drop down
     *	
     * @private	
     */		
	__populateMinutesInsideDropDownMinutes : function()
	{
		var ind = this.objectIndex;
		this.divElementMinuteDropDownParentMinutes.innerHTML = '';	
		
		
		if(60 / this.minuteDropDownInterval	< this.numberOfRowsInMinuteDropDown){
			startMinute=0;
		}else{
			var startMinute = Math.max(0,(this.calendarModelReference.__getDisplayedMinute()-Math.round(this.numberOfRowsInMinuteDropDown/2)));
			startMinute+=(this.minuteDropDownOffsetInMinute*this.minuteDropDownInterval)
			if(startMinute<0){	/* Start minute negative - adjust it and change offset value */						
				startMinute+=this.minuteDropDownInterval;
				this.minuteDropDownOffsetInMinute++;
			}
			if(startMinute + (this.numberOfRowsInMinuteDropDown * this.minuteDropDownInterval) >60){	/* start minute in drop down + number of records shown * interval larger than 60 -> adjust it */		
				startMinute-=this.minuteDropDownInterval;
				this.minuteDropDownOffsetInMinute--;
			}
		}
		for(var no=startMinute;no<Math.min(60,startMinute+this.numberOfRowsInMinuteDropDown*(this.minuteDropDownInterval));no+=this.minuteDropDownInterval){
			var div = document.createElement('DIV');
			div.className = 'DHTMLSuite_calendar_dropDownAMinute';
			if(no==this.calendarModelReference.__getDisplayedMinute())div.className = 'DHTMLSuite_calendar_minuteDropDownCurrentMinute';
			var prefix = "";
			if(no<10)prefix = "0";
			div.innerHTML = prefix + no;
			
			div.onmouseover = this.__mouseoverMinuteInDropDown;
			div.onmouseout = this.__mouseoutMinuteInDropDown;
			div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setMinuteFromDropdown(e); } 
			this.divElementMinuteDropDownParentMinutes.appendChild(div);	
			DHTMLSuite.commonObj.__addEventElement(div);			
		}		
		
	}
	// }}}	
	,
	// {{{ __populateDropDownHours()
    /**
     *	Populate - hour dropdown.
     *	
     * @private	
     */		
	__populateDropDownHours : function()
	{
		var ind = this.objectIndex;		
		this.divElementHourDropdown.innerHTML = '';	
		
		// Previous hour
		var div = document.createElement('DIV');
		div.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
		div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownHours(e); } ;
		div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownHours(e); } ;	
		this.divElementHourDropdown.appendChild(div);	
		DHTMLSuite.commonObj.__addEventElement(div);			

		this.divElementHourDropDownParentHours = document.createElement('DIV');
		this.divElementHourDropdown.appendChild(this.divElementHourDropDownParentHours);
		this.__populateHoursInsideDropDownHours(this.divElementHourDropDownParentHours);
		
		// Next Hour	
		var div = document.createElement('DIV');
		div.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
		div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownHours(e); } ;
		div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownHours(e); } ;
		DHTMLSuite.commonObj.__addEventElement(div);
		this.divElementHourDropdown.appendChild(div);			
				
	}
	// }}}
	,
	// {{{ __populateHoursInsideDropDownHours()
    /**
     *	Populate - hours inside hour drop down
     *	
     * @private	
     */		
	__populateHoursInsideDropDownHours : function()
	{
		
		var ind = this.objectIndex;
		this.divElementHourDropDownParentHours.innerHTML = '';
		var startHour = Math.max(0,(this.calendarModelReference.__getDisplayedHour()-Math.round(this.numberOfRowsInHourDropDown/2)));
		startHour = Math.min(14,startHour);
		if((startHour + this.hourDropDownOffsetInHour + this.numberOfRowsInHourDropDown)>24){
			this.hourDropDownOffsetInHour = (24-startHour-this.numberOfRowsInHourDropDown);			
		}
		if((startHour + this.hourDropDownOffsetInHour)<0){
			this.hourDropDownOffsetInHour = startHour*-1;			
		}
				
		startHour+=this.hourDropDownOffsetInHour;
		if(startHour<0)startHour = 0;
		if(startHour>(24-this.numberOfRowsInHourDropDown))startHour = (24-this.numberOfRowsInHourDropDown);
		for(var no=startHour;no<startHour+this.numberOfRowsInHourDropDown;no++){
			var div = document.createElement('DIV');
			div.className = 'DHTMLSuite_calendar_dropDownAHour';
			if(no==this.calendarModelReference.__getDisplayedHour())div.className = 'DHTMLSuite_calendar_hourDropDownCurrentHour';
			var prefix = "";
			if(no<10)prefix = "0";
			div.innerHTML = prefix + no;
			
			div.onmouseover = this.__mouseoverHourInDropDown;
			div.onmouseout = this.__mouseoutHourInDropDown;
			div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setHourFromDropdown(e); } 
			this.divElementHourDropDownParentHours.appendChild(div);	
			DHTMLSuite.commonObj.__addEventElement(div);			
		}		
		
	}
	// }}}
	,
	// {{{ __populateDropDownYears()
    /**
     *	Populate - year dropdown.
     *	
     * @private	
     */		
	__populateDropDownYears : function()
	{
		var ind = this.objectIndex;		
		this.divElementYearDropdown.innerHTML = '';		
		
		// Previous year 
		var div = document.createElement('DIV');
		div.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
		div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownYears(e); } ;
		div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownYears(e); } ;	
		this.divElementYearDropdown.appendChild(div);	
		DHTMLSuite.commonObj.__addEventElement(div);	

		
		this.divElementYearDropDownParentYears = document.createElement('DIV');
		this.divElementYearDropdown.appendChild(this.divElementYearDropDownParentYears);
		this.__populateYearsInsideDropDownYears(this.divElementYearDropDownParentYears);
				

		
		// Next year	
		var div = document.createElement('DIV');
		div.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
		div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownYears(e); } ;
		div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownYears(e); } ;
		DHTMLSuite.commonObj.__addEventElement(div);
		this.divElementYearDropdown.appendChild(div);			
	}
	// }}}
	,
	// {{{ __populateYearsInsideDropDownYears()
    /**
     *	Populate inner div inside the year drop down with months.
     *	
     * @private	
     */		
	__populateYearsInsideDropDownYears : function(divElementToPopulate)
	{
		var ind = this.objectIndex;
		this.divElementYearDropDownParentYears.innerHTML = '';
		var startYear = this.calendarModelReference.__getDisplayedYear()-5 + this.yearDropDownOffsetInYear;
		for(var no=startYear;no<startYear+this.numberOfRowsInYearDropDown;no++){
			var div = document.createElement('DIV');
			div.className = 'DHTMLSuite_calendar_dropDownAYear';
			if(no==this.calendarModelReference.__getDisplayedYear())div.className = 'DHTMLSuite_calendar_yearDropDownCurrentYear';
			div.innerHTML = no;
			
			div.onmouseover = this.__mouseoverYearInDropDown;
			div.onmouseout = this.__mouseoutYearInDropDown;
			div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setYearFromDropdown(e); } 
			this.divElementYearDropDownParentYears.appendChild(div);	
			DHTMLSuite.commonObj.__addEventElement(div);			
		}			
	}
	// }}}
	,
	// {{{ __positionDropDownMonths()
    /**
     *	Position the year dropdown below the year in the heading.
     *	
     * @private	
     */		
	__positionDropDownMonths : function()
	{
		this.divElementMonthDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementMonthNameInHeading) + 'px';
		this.divElementMonthDropdown.style.top = (DHTMLSuite.commonObj.getTopPosCalendar(this.divElementMonthNameInHeading) + this.divElementMonthNameInHeading.offsetHeight) + 'px';
		
		if(this.iframeElementDropDowns){
			this.iframeElementDropDowns.style.left = this.divElementMonthDropdown.style.left;
			this.iframeElementDropDowns.style.top = this.divElementMonthDropdown.style.top;
			this.iframeElementDropDowns.style.width = (this.divElementMonthDropdown.clientWidth) + 'px';
			this.iframeElementDropDowns.style.height = this.divElementMonthDropdown.clientHeight + 'px';
			this.iframeElementDropDowns.style.display = this.divElementMonthDropdown.style.display;
		}
		
	}
	// }}}
	,
	// {{{ __positionDropDownYears()
    /**
     *	Position the month dropdown below the month in the heading.
     *	
     * @private	
     */		
	__positionDropDownYears : function()
	{
		this.divElementYearDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementYearInHeading) + 'px';
		this.divElementYearDropdown.style.top = (DHTMLSuite.commonObj.getTopPosCalendar(this.divElementYearInHeading) + this.divElementYearInHeading.offsetHeight) + 'px';
		if(this.iframeElementDropDowns){
			this.iframeElementDropDowns.style.left = this.divElementYearDropdown.style.left;
			this.iframeElementDropDowns.style.top = this.divElementYearDropdown.style.top;
			this.iframeElementDropDowns.style.width = (this.divElementYearDropdown.clientWidth) + 'px';
			this.iframeElementDropDowns.style.height = this.divElementYearDropdown.clientHeight + 'px';
			this.iframeElementDropDowns.style.display = this.divElementYearDropdown.style.display;
		}		
		
	}
	// }}}
	,
	// {{{ __positionDropDownHours()
    /**
     *	Position the month dropdown below the month in the heading.
     *	
     * @private	
     */		
	__positionDropDownHours : function()
	{
		this.divElementHourDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementHourInTimeBar) + 'px';
		this.divElementHourDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementHourInTimeBar) + this.divElementHourInTimeBar.offsetHeight) + 'px';
		if(this.iframeElementDropDowns){
			this.iframeElementDropDowns.style.left = this.divElementHourDropdown.style.left;
			this.iframeElementDropDowns.style.top = this.divElementHourDropdown.style.top;
			this.iframeElementDropDowns.style.width = (this.divElementHourDropdown.clientWidth) + 'px';
			this.iframeElementDropDowns.style.height = this.divElementHourDropdown.clientHeight + 'px';
			this.iframeElementDropDowns.style.display = this.divElementHourDropdown.style.display;
		}		
		
	}
	// }}}
	,
	// {{{ __positionDropDownMinutes()
    /**
     *	Position the month dropdown below the month in the heading.
     *	
     * @private	
     */		
	__positionDropDownMinutes : function()
	{
		this.divElementMinuteDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementMinuteInTimeBar) + 'px';
		this.divElementMinuteDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementMinuteInTimeBar) + this.divElementMinuteInTimeBar.offsetHeight) + 'px';
		if(this.iframeElementDropDowns){
			this.iframeElementDropDowns.style.left = this.divElementMinuteDropdown.style.left;
			this.iframeElementDropDowns.style.top = this.divElementMinuteDropdown.style.top;
			this.iframeElementDropDowns.style.width = (this.divElementMinuteDropdown.clientWidth) + 'px';
			this.iframeElementDropDowns.style.height = this.divElementMinuteDropdown.clientHeight + 'px';
			this.iframeElementDropDowns.style.display = this.divElementMinuteDropdown.style.display;
		}		
		
	}
	// }}}
	,
	// {{{ __setMonthFromDropdown()
    /**
     *	Select new month from drop down box. The id is fetched by looking at the id of the element triggering this event, i.e. a month div in the dropdown.
     *	
     * @private	
     */		
	__setMonthFromDropdown : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);		
		this.__showHideDropDownBoxMonth();	
		this.setDisplayedMonth(src.id.replace(/[^0-9]/gi,''));
		
	}
	// }}}
	,
	// {{{ __setYearFromDropdown()
    /**
     *	Select new year from drop down box. The id is fetched by looking at the innerHTML of the element triggering this event, i.e. a year div in the dropdown.
     *	
     * @private	
     */		
	__setYearFromDropdown : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);		
		this.__showHideDropDownBoxYear();	
		this.setDisplayedYear(src.innerHTML);
		
	}
	// }}}
	,
	// {{{ __setHourFromDropdown()
    /**
     *	Set displayed hour from drop down box
     *	
     * @private	
     */			
	__setHourFromDropdown : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);		
		this.__showHideDropDownBoxHour();	
		this.setDisplayedHour(src.innerHTML);		
		
	}
	,
	// {{{ __setMinuteFromDropdown()
    /**
     *	Set displayed hour from drop down box
     *
     * @private	
     */			
	__setMinuteFromDropdown : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);		
		this.__showHideDropDownBoxMinute();	
		this.setDisplayedMinute(src.innerHTML);		
		
	}
	
	// }}}
	,
	// {{{ __autoHideDropDownBoxes()
    /**
     *	Automatically hide drop down boxes when users click someplace on the page except in the headings triggering these dropdowns.
     *	
     * @private	
     */		
	__autoHideDropDownBoxes : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		if(src.className.indexOf('MonthAndYear')>=0 || src.className.indexOf('HourAndMinute')>=0){	// class name of element same as element triggering the dropdowns ?
			if(DHTMLSuite.commonObj.isObjectClicked(this.divElement,e))return;	// if element clicked is a sub element of main calendar div - return
	
		}
		this.__showHideDropDownBoxMonth('none');
		this.__showHideDropDownBoxYear('none');
		this.__showHideDropDownBoxHour('none');
		this.__showHideDropDownBoxMinute('none');
		
	}
	// }}}
	,
	// {{{ __showHideDropDownBoxMonth()
    /**
     *	Show or hide month drop down box
     *	
     * @private	
     */		
	__showHideDropDownBoxMonth : function(forcedDisplayAttribute)
	{
		if(!forcedDisplayAttribute){
			this.__showHideDropDownBoxYear('none');	// Hide year drop down.
			this.__showHideDropDownBoxHour('none');	// Hide year drop down.			
		}
		if(forcedDisplayAttribute){
			this.divElementMonthDropdown.style.display = forcedDisplayAttribute;
		}else{
			this.divElementMonthDropdown.style.display=(this.divElementMonthDropdown.style.display=='block'?'none':'block');
		}
		this.__populateDropDownMonths();
		this.__positionDropDownMonths();	

		
	}
	// }}}
	,
	// {{{ __createPrimaryHtmlElements()
    /**
     *	Create main div elements for the calendar
     *	
     * @private	
     */		
	__showHideDropDownBoxYear : function(forcedDisplayAttribute)
	{
		if(!forcedDisplayAttribute){
			this.__showHideDropDownBoxMonth('none');	// Hide year drop down.
			this.__showHideDropDownBoxHour('none');	// Hide year drop down.
			this.__showHideDropDownBoxMinute('none');	// Hide year drop down.
		}
		if(forcedDisplayAttribute){
			this.divElementYearDropdown.style.display = forcedDisplayAttribute;
		}else{
			this.divElementYearDropdown.style.display=(this.divElementYearDropdown.style.display=='block'?'none':'block');
		}
		if(this.divElementYearDropdown.style.display=='none' ){
			this.yearDropDownOffsetInYear = 0;
		}else{
			this.__populateDropDownYears();
		}
		this.__positionDropDownYears();
		
	}	
	// }}}
	,	
	// {{{ __createPrimaryHtmlElements()
    /**
     *	Create main div elements for the calendar
     *	
     * @private	
     */		
	__showHideDropDownBoxHour : function(forcedDisplayAttribute)
	{
		if(!forcedDisplayAttribute){
			this.__showHideDropDownBoxYear('none');	// Hide Hour drop down.
			this.__showHideDropDownBoxMonth('none');	// Hide Hour drop down.
			this.__showHideDropDownBoxMinute('none');	// Hide Hour drop down.
			
		}
		if(forcedDisplayAttribute){
			this.divElementHourDropdown.style.display = forcedDisplayAttribute;
		}else{
			this.divElementHourDropdown.style.display=(this.divElementHourDropdown.style.display=='block'?'none':'block');
		}
		if(this.divElementHourDropdown.style.display=='none' ){
			this.hourDropDownOffsetInHour = 0;
		}else{
			this.__populateDropDownHours();
		}
		this.__positionDropDownHours();
		
	}
	// }}}
	,	
	// {{{ __createPrimaryHtmlElements()
    /**
     *	Create main div elements for the calendar
     *	
     * @private	
     */		
	__showHideDropDownBoxMinute : function(forcedDisplayAttribute)
	{
		if(!forcedDisplayAttribute){
			this.__showHideDropDownBoxYear('none');	// Hide Minute drop down.
			this.__showHideDropDownBoxMonth('none');	// Hide Minute drop down.
			this.__showHideDropDownBoxHour('none');	// Hide Minute drop down.
			
		}
		if(forcedDisplayAttribute){
			this.divElementMinuteDropdown.style.display = forcedDisplayAttribute;
		}else{
			this.divElementMinuteDropdown.style.display=(this.divElementMinuteDropdown.style.display=='block'?'none':'block');
		}
		if(this.divElementMinuteDropdown.style.display=='none' ){
			this.minuteDropDownOffsetInMinute = 0;
		}else{
			this.__populateDropDownMinutes();
		}
		this.__positionDropDownMinutes();
		
	}	
	// }}}
	,	
	// {{{ __createPrimaryHtmlElements()
    /**
     *	Create main div elements for the calendar
     *
     * @private	
     */		
	__createPrimaryHtmlElements : function()
	{
		this.divElement = document.createElement('DIV');
		this.divElement.className = 'DHTMLSuite_calendar';
		this.divElementContent = document.createElement('DIV');
		this.divElement.appendChild(this.divElementContent);
		this.divElementContent.className = 'DHTMLSuite_calendarContent';
		if(this.targetReference)this.targetReference.appendChild(this.divElement);else document.body.appendChild(this.divElement);
		if(this.isDragable){
			try{
				this.referenceToDragDropObject = new DHTMLSuite.dragDropSimple(this.divElement,false,0,0);	
			}catch(e){
				alert('You need to include DHTMLSuite-dragDropSimple.js for the drag feature');
			}
		}
		
		if(DHTMLSuite.clientInfoObj.isMSIE && DHTMLSuite.clientInfoObj.navigatorVersion<8){
			this.iframeElement = document.createElement('<iframe src="about:blank" frameborder="0">');
			this.iframeElement.className = 'DHTMLSuite_calendar_iframe';
			this.divElement.appendChild(this.iframeElement);
			
			this.iframeElementDropDowns = document.createElement('<iframe src="about:blank" frameborder="0">');
			this.iframeElementDropDowns.className = 'DHTMLSuite_calendar_iframe';
			this.iframeElementDropDowns.style.display='none';
			document.body.appendChild(this.iframeElementDropDowns);
			
		}
	}
	// }}}
	,
	// {{{ __createCalendarHeadingElements()
    /**
     *	Create main div elements for the calendar
     *
     * @private	
     */		
	__createCalendarHeadingElements : function()
	{
		this.divElementHeading = document.createElement('DIV');	
		
		if(this.isDragable){	/* Calendar is dragable */
			this.referenceToDragDropObject.addDragHandle(this.divElementHeading);
			this.divElementHeading.style.cursor = 'move';
		}		
		
		this.divElementHeading.className='DHTMLSuite_calendarHeading';
		this.divElementContent.appendChild(this.divElementHeading);
		this.divElementHeading.style.position = 'relative';
		
		this.divElementClose = document.createElement('DIV');
		this.divElementClose.className = 'DHTMLSuite_calendarCloseButton';
		this.divElementHeading.appendChild(this.divElementClose);
		if(!this.displayCloseButton)this.divElementClose.style.display='none';
		
		this.divElementHeadingTxt = document.createElement('DIV');
		this.divElementHeadingTxt.className = 'DHTMLSuite_calendarHeadingTxt';
		
		if(DHTMLSuite.clientInfoObj.isMSIE){
			var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
		}else{
			var table = document.createElement('TABLE');
			table.setAttribute('cellpadding',0);
			table.setAttribute('cellspacing',0);
			table.setAttribute('border',0);
		}
		table.style.margin = '0 auto';
		this.divElementHeadingTxt.appendChild(table);
		
		var row = table.insertRow(0);
		
		var cell = row.insertCell(-1);		
		this.divElementMonthNameInHeading = document.createElement('DIV');
		this.divElementMonthNameInHeading.className = 'DHTMLSuite_calendarHeaderMonthAndYear';
		
		cell.appendChild(this.divElementMonthNameInHeading);		
		
		var cell = row.insertCell(-1);	
		var span = document.createElement('SPAN');
		span.innerHTML = ', ';
		cell.appendChild(span);
		
		var cell = row.insertCell(-1);	
		this.divElementYearInHeading = document.createElement('DIV');
		
		this.divElementYearInHeading.className = 'DHTMLSuite_calendarHeaderMonthAndYear';
		cell.appendChild(this.divElementYearInHeading);
		
		this.divElementHeading.appendChild(this.divElementHeadingTxt);

	}
	// }}}
	,
	// {{{ __createNavigationBar()
    /**
     *	Create navigation bar elements below the heading.
     * @private	
     */		
	__createNavigationBar : function()
	{
		this.divElementNavigationBar = document.createElement('DIV');
		this.divElementNavigationBar.className='DHTMLSuite_calendar_navigationBar';
		this.divElementContent.appendChild(this.divElementNavigationBar);	
		
		this.divElementBtnPreviousYear = document.createElement('DIV');	
		this.divElementBtnPreviousYear.className='DHTMLSuite_calendar_btnPreviousYear';
		this.divElementNavigationBar.appendChild(this.divElementBtnPreviousYear);
		
		
		this.divElementBtnNextYear = document.createElement('DIV');	
		this.divElementBtnNextYear.className='DHTMLSuite_calendar_btnNextYear';
		this.divElementNavigationBar.appendChild(this.divElementBtnNextYear);
		
		this.divElementBtnPreviousMonth = document.createElement('DIV');	
		this.divElementBtnPreviousMonth.className='DHTMLSuite_calendar_btnPreviousMonth';
		this.divElementNavigationBar.appendChild(this.divElementBtnPreviousMonth);
		
		
		this.divElementBtnNextMonth = document.createElement('DIV');	
		this.divElementBtnNextMonth.className='DHTMLSuite_calendar_btnNextMonth';
		this.divElementNavigationBar.appendChild(this.divElementBtnNextMonth);
		
		this.divElementTodayInNavigationBar = document.createElement('DIV');
		this.divElementTodayInNavigationBar.className='DHTMLSuite_calendar_navigationBarToday';
		this.divElementNavigationBar.appendChild(this.divElementTodayInNavigationBar);
		
		if(!this.displayNavigationBar)this.divElementNavigationBar.style.display='none';
		if(!this.displayTodaysDateInNavigationBar)this.divElementTodayInNavigationBar.style.display='none';
		
	}
	// }}}
	,
	// {{{ __populateNavigationBar()
    /**
     *	Populate navigation bar, i.e. display the "Today" string and add an onclick event on this span tag. this onclick events makes the calendar display current month.
     *	
     * @private	
     */		
	__populateNavigationBar : function()
	{
		var ind = this.objectIndex;
		this.divElementTodayInNavigationBar.innerHTML = '';
		var span = document.createElement('SPAN');
		span.innerHTML = this.calendarModelReference.__getTodayAsString();
		span.onclick = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__displayMonthOfToday(); }
		this.divElementTodayInNavigationBar.appendChild(span);
	}
	// }}}
	,
	// {{{ __createCalendarMonthView()
    /**
     *	Create div element for the month view
     *	
     * @private	
     */		
	__createCalendarMonthView : function()
	{
		this.divElementMonthView = document.createElement('DIV');
		this.divElementMonthView.className = 'DHTMLSuite_calendar_monthView';
		this.divElementContent.appendChild(this.divElementMonthView);
	}
	// }}}
	,
	// {{{ __populateMonthViewWithMonthData()
    /**
     *	Populate month view with month data, i.e. heading weeks, days, months.
     *	
     * @private	
     */	
	__populateMonthViewWithMonthData : function()
	{
		var ind = this.objectIndex;
		
		var dateOfToday = new Date();
		
		this.divElementMonthView.innerHTML = '';
		var modelRef = this.calendarModelReference;
			
		if(DHTMLSuite.clientInfoObj.isMSIE){
			var table = document.createElement('<TABLE cellpadding="1" cellspacing="0" border="0" width="100%">');
		}else{
			var table = document.createElement('TABLE');
			table.setAttribute('cellpadding',1);
			table.setAttribute('cellspacing',0);
			table.setAttribute('border',0);
			table.width = '100%';
		}
		
		this.divElementMonthView.appendChild(table);
		
		var row = table.insertRow(-1);	// Insert a new row at the end of the table
		row.className = 'DHTMLSuite_calendar_monthView_headerRow';
		
		var cell = row.insertCell(-1);
		cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
		cell.innerHTML = modelRef.__getStringWeek();
		
		if(modelRef.getWeekStartsOnMonday()){
			var days = modelRef.__getDaysMondayToSunday();
		}else{
			var days = modelRef.__getDaysSundayToSaturday();
		}
		
		/* Outputs days in the week */
		for(var no=0;no<days.length;no++){
			var cell = row.insertCell(-1);
			cell.innerHTML = days[no];
			cell.className = 'DHTMLSuite_calendar_monthView_headerCell';
			if(modelRef.getWeekStartsOnMonday() && no==6){
				cell.className = 'DHTMLSuite_calendar_monthView_headerSunday';
			}
			if(!modelRef.getWeekStartsOnMonday() && no==0){
				cell.className = 'DHTMLSuite_calendar_monthView_headerSunday';
			}			
		}		
		// First row of days
		var row = table.insertRow(-1);
		var cell = row.insertCell(-1);
		cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
		var week = modelRef.__getWeekNumberFromDayMonthAndYear(modelRef.__getDisplayedYear(),modelRef.__getDisplayedMonthNumber(),1);
		
		cell.innerHTML = week;
		
		var daysRemainingInPreviousMonth = modelRef.__getRemainingDaysInPreviousMonthAsArray();
		for(var no=0;no<daysRemainingInPreviousMonth.length;no++){
			var cell = row.insertCell(-1);
			cell.innerHTML = daysRemainingInPreviousMonth[no];	
			cell.className = 'DHTMLSuite_calendar_monthView_daysInOtherMonths';		
		}
		
		var daysInCurrentMonth = modelRef.__getNumberOfDaysInCurrentDisplayedMonth();
		var cellCounter = daysRemainingInPreviousMonth.length+1;
		/* Loop through days in this month */
		for(var no=1;no<=daysInCurrentMonth;no++){			
			var cell = row.insertCell(-1);
			cell.innerHTML = no;
			cell.className = 'DHTMLSuite_calendar_monthView_daysInThisMonth';

			DHTMLSuite.commonObj.__addEventElement(cell);	
			if(cellCounter%7==0 && modelRef.getWeekStartsOnMonday()){
				cell.className = 'DHTMLSuite_calendar_monthView_sundayInThisMonth';
			}
			if(cellCounter%7==1 && !modelRef.getWeekStartsOnMonday()){
				cell.className = 'DHTMLSuite_calendar_monthView_sundayInThisMonth';
			}
			// Day displayed the same as inital date ?
			if(no==modelRef.__getInitialDay() && modelRef.__getDisplayedYear() == modelRef.__getInitialYear() && modelRef.__getDisplayedMonthNumber()==modelRef.__getInitialMonthNumber()){
				cell.className = 'DHTMLSuite_calendar_monthView_initialDate';	
			}
			if(!modelRef.isDateWithinValidRange({year:modelRef.__getDisplayedYear(),month:modelRef.__getDisplayedMonthNumber(),day:no})){
				cell.className = 'DHTMLSuite_calendar_monthView_invalidDate';	
			}else{
				cell.onmousedown = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mousedownOnDayInCalendar(e); } 
				cell.onmouseover = this.__mouseoverCalendarDay;
				cell.onmouseout = this.__mouseoutCalendarDay;				
				
			}
			// Day displayed the same as date of today ?
			if(no==dateOfToday.getDate() && modelRef.__getDisplayedYear() == dateOfToday.getFullYear() && modelRef.__getDisplayedMonthNumber()==(dateOfToday.getMonth()+1)){
				cell.className = 'DHTMLSuite_calendar_monthView_currentDate';	
			}			
			if(cellCounter%7==0 && no<daysInCurrentMonth){
				var row = table.insertRow(-1);	
				var cell = row.insertCell(-1)
				cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
				var week = modelRef.__getWeekNumberFromDayMonthAndYear(modelRef.__getDisplayedYear(),modelRef.__getDisplayedMonthNumber(),(no+1));
				cell.innerHTML = week;				
			}
			cellCounter++;
		}
		
		// Adding the first days of the next month to the view		
		if((cellCounter-1)%7>0){
			var dayCounter = 1;
			for(var no=(cellCounter-1)%7;no<7;no++){
				var cell = row.insertCell(-1);
				cell.innerHTML = dayCounter;
				cell.className = 'DHTMLSuite_calendar_monthView_daysInOtherMonths';	
				dayCounter++;
			}
		}
		
	}
	// }}}
	,
	// {{{ __createTimeBar()
    /**
     *	Create bar where users can select hours and minutes.
     *	
     * @private	
     */		
	__createTimeBar : function()
	{
		this.divElementTimeBar = document.createElement('DIV');
		this.divElementTimeBar.className = 'DHTMLSuite_calendar_timeBar';
		this.divElementContent.appendChild(this.divElementTimeBar);	
		
		
		if(DHTMLSuite.clientInfoObj.isMSIE){
			var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
		}else{
			var table = document.createElement('TABLE');
			table.setAttribute('cellpadding',0);
			table.setAttribute('cellspacing',0);
			table.setAttribute('border',0);
		}
		table.style.margin = '0 auto';
		this.divElementTimeBar.appendChild(table);
		
		var row = table.insertRow(0);
		
		var cell = row.insertCell(-1);		
		this.divElementHourInTimeBar = document.createElement('DIV');
		this.divElementHourInTimeBar.className = 'DHTMLSuite_calendar_timeBarHourAndMinute';		
		cell.appendChild(this.divElementHourInTimeBar);		

		var cell = row.insertCell(-1);	
		var span = document.createElement('SPAN');
		span.innerHTML = ' : ';
		cell.appendChild(span);
				
		var cell = row.insertCell(-1);	
		this.divElementMinuteInTimeBar = document.createElement('DIV');		
		this.divElementMinuteInTimeBar.className = 'DHTMLSuite_calendar_timeBarHourAndMinute';
		cell.appendChild(this.divElementMinuteInTimeBar);
		
		this.divElementTimeStringInTimeBar = document.createElement('DIV');
		this.divElementTimeStringInTimeBar.className = 'DHTMLSuite_calendarTimeBarTimeString'; 
		this.divElementTimeBar.appendChild(this.divElementTimeStringInTimeBar);
		
		if(!this.displayTimeBar)this.divElementTimeBar.style.display='none';
			
	}
	// }}}
	,
	// {{{ __populateTimeBar()
    /**
     *	Populate time time bar with hour and minutes.
     *	
     * @private	
     */		
	__populateTimeBar : function()
	{
		this.divElementHourInTimeBar.innerHTML = this.calendarModelReference.__getDisplayedHourWithLeadingZeros();
		this.divElementMinuteInTimeBar.innerHTML = this.calendarModelReference.__getDisplayedMinuteWithLeadingZeros();	
		this.divElementTimeStringInTimeBar.innerHTML = this.calendarModelReference.__getTimeAsString() + ':';
		
	}
	// }}}
	,
	// {{{ __populateCalendarHeading()
    /**
     *	Populate heading of calendar
     *	
     * @private	
     */		
	__populateCalendarHeading : function()
	{		
		this.divElementMonthNameInHeading.innerHTML = this.calendarModelReference.__getMonthNameByMonthNumber(this.calendarModelReference.__getDisplayedMonthNumber());
		this.divElementYearInHeading.innerHTML = this.calendarModelReference.__getDisplayedYear();
	}
	// }}}
	,
	// {{{ __mousedownOnDayInCalendar()
    /**
     *	Mouse down day inside the calendar view. Set current displayed date to the clicked date and check for call back functions.
     *	
     * @private	
     */		
	__mousedownOnDayInCalendar : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		this.calendarModelReference.__setDisplayedDay(src.innerHTML);
		this.__handleCalendarCallBack('dayClick');
	}
	// }}}
	,
	// {{{ __handleCalendarCallBack()
    /**
     *	This method handles all call backs from the calendar
     *	
     * @private	
     */		
	__handleCalendarCallBack : function(action)
	{
		var callbackString = '';
		switch(action){
			case 'dayClick':
				if(this.callbackFunctionOnDayClick)callbackString = this.callbackFunctionOnDayClick;
				break;	
			case "monthChange":
				if(this.callbackFunctionOnMonthChange)callbackString = this.callbackFunctionOnMonthChange;
				break;									
		}	
		
		if(callbackString){
			callbackString = callbackString + 
				'({'
				+ ' year:' + this.calendarModelReference.__getDisplayedYear() 
				+ ',month:"' + this.calendarModelReference.__getDisplayedMonthNumberWithLeadingZeros() + '"'
				+ ',monthName:"' + this.calendarModelReference.languageModel.monthArrayShort[this.calendarModelReference.__getDisplayedMonthNumber() - 1] + '"'
				+ ',day:"' + this.calendarModelReference.__getDisplayedDayWithLeadingZeros() + '"'
				+ ',hour:"' + this.calendarModelReference.__getDisplayedHourWithLeadingZeros() + '"'
				+ ',minute:"' + this.calendarModelReference.__getDisplayedMinuteWithLeadingZeros() + '"'
				+ ',calendarRef:this'
				
			callbackString = callbackString + '})';
		}
		
		if(callbackString)this.__evaluateCallBackString(callbackString);	
	}
	// }}}
	,
	// {{{ __evaluateCallBackString()
    /**
     *	Evaluate call back string.
     *	
     *
     *
     * @private	
     */		
	__evaluateCallBackString : function(callbackString)
	{
		try{
			eval(callbackString);
		}catch(e){
			alert('Could not excute call back function ' + callbackString + '\n' + e.message);
		}		
	}
	// }}}
	,
	// {{{ __displayMonthOfToday()
    /**
     *	Show calendar data for present day
     *	
     * @private	
     */		
	__displayMonthOfToday : function()
	{
		var d = new Date();
		var month = d.getMonth()+1;
		var year = d.getFullYear();
		this.setDisplayedYear(year);
		this.setDisplayedMonth(month);	
		
	}
	// }}}
	,
	// {{{ __moveOneYearBack()
    /**
     *	Show calendar data for the same month in previous year
     *	
     * @private	
     */		
	__moveOneYearBack : function()
	{
		this.calendarModelReference.__moveOneYearBack();
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();
		this.__handleCalendarCallBack('monthChange');
		
	}
	// }}}
	,
	// {{{ __moveOneYearForward()
    /**
     *	Show calendar data for the same month in next year
     *	
     *
     *
     * @private	
     */		
	__moveOneYearForward : function()
	{
		this.calendarModelReference.__moveOneYearForward();
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();
		this.__handleCalendarCallBack('monthChange');		
	}
	// }}}
	,
	// {{{ __moveOneMonthBack()
    /**
     *	Show calendar data for previous month
     *
     *
     * @private	
     */		
	__moveOneMonthBack : function()
	{
		this.calendarModelReference.__moveOneMonthBack();
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();
		this.__handleCalendarCallBack('monthChange');
	}
	// }}}
	,
	// {{{ __moveOneMonthForward()
    /**
     *	Move one month forward
     *
     *
     * @private	
     */		
	__moveOneMonthForward : function()
	{
		this.calendarModelReference.__moveOneMonthForward();
		this.__populateCalendarHeading();
		this.__populateMonthViewWithMonthData();
		this.__handleCalendarCallBack('monthChange');		
	}
	// }}}
	,
	// {{{ __addEvents()
    /**
     *	Add events to calendar elements.
     *	
     *
     *
     * @private	
     */		
	__addEvents : function()
	{
		var ind = this.objectIndex;
		this.divElementClose.onmouseover = this.__mouseoverCalendarButton;
		this.divElementClose.onmouseout = this.__mouseoutCalendarButton;
		this.divElementClose.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].hide(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementClose);	
		
		// Button - previous year
		this.divElementBtnPreviousYear.onmouseover = this.__mouseoverCalendarButton;
		this.divElementBtnPreviousYear.onmouseout = this.__mouseoutCalendarButton;
		this.divElementBtnPreviousYear.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneYearBack(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementBtnPreviousYear);	
		
		// Button - next year
		this.divElementBtnNextYear.onmouseover = this.__mouseoverCalendarButton;
		this.divElementBtnNextYear.onmouseout = this.__mouseoutCalendarButton;
		this.divElementBtnNextYear.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneYearForward(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementBtnNextYear);	
		
		// Button previous month
		this.divElementBtnPreviousMonth.onmouseover = this.__mouseoverCalendarButton;
		this.divElementBtnPreviousMonth.onmouseout = this.__mouseoutCalendarButton;
		this.divElementBtnPreviousMonth.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneMonthBack(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementBtnPreviousMonth);	
		
		// Button next month
		this.divElementBtnNextMonth.onmouseover = this.__mouseoverCalendarButton;
		this.divElementBtnNextMonth.onmouseout = this.__mouseoutCalendarButton;
		this.divElementBtnNextMonth.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneMonthForward(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementBtnNextMonth);			
		
		// Year in the heading
		this.divElementYearInHeading.onmouseover = this.__mouseoverMonthAndYear;
		this.divElementYearInHeading.onmouseout = this.__mouseoutMonthAndYear;
		this.divElementYearInHeading.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxYear(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementYearInHeading);	

		// Month in the heading	
		this.divElementMonthNameInHeading.onmouseover = this.__mouseoverMonthAndYear;
		this.divElementMonthNameInHeading.onmouseout = this.__mouseoutMonthAndYear;
		this.divElementMonthNameInHeading.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxMonth(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementMonthNameInHeading);		
		
		// Hour in timer bar
		this.divElementHourInTimeBar.onmouseover = this.__mouseoverHourAndMinute;
		this.divElementHourInTimeBar.onmouseout = this.__mouseoutHourAndMinute;
		this.divElementHourInTimeBar.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxHour(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementHourInTimeBar);	

		// Minute in timer bar	
		this.divElementMinuteInTimeBar.onmouseover = this.__mouseoverHourAndMinute;
		this.divElementMinuteInTimeBar.onmouseout = this.__mouseoutHourAndMinute;
		this.divElementMinuteInTimeBar.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxMinute(); }
		DHTMLSuite.commonObj.__addEventElement(this.divElementMinuteInTimeBar);	
		
		// Disable text selection in the heading
		this.divElementHeading.onselectstart = DHTMLSuite.commonObj.cancelEvent;
		DHTMLSuite.commonObj.__addEventElement(this.divElementHeading);
		
		DHTMLSuite.commonObj.addEvent(document.documentElement,'click',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__autoHideDropDownBoxes(e); },ind+'');
		
	}
	// }}}
	,
	// {{{ __resizePrimaryIframeElement()
    /**
     *	Resize primary iframe element.
     *	
     *
     *
     * @private	
     */		
	__resizePrimaryIframeElement : function()
	{
		if(!this.iframeElement)return;
		this.iframeElement.style.width = this.divElement.clientWidth + 'px';
		this.iframeElement.style.height = this.divElement.clientHeight + 'px';
		
	}
	// }}}
	,
	// {{{ __scrollInYearDropDown()
    /**
     *	Scroll the year drop down as long as the scrollInYearDropDownActive is true
     *	
     *
     *
     * @private	
     */		
	__scrollInYearDropDown : function(scrollDirection)
	{
		if(!this.scrollInYearDropDownActive)return;
		var ind = this.objectIndex;	
		this.yearDropDownOffsetInYear+=scrollDirection;
		this.__populateYearsInsideDropDownYears();
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInYearDropDown(' + scrollDirection + ')',150);
	}
	
	// }}}
	,
	// {{{ __mouseoverUpAndDownArrowsInDropDownYears()
    /**
     *	Mouse over year drop down arrow
     *	
     * @private	
     */		
	__mouseoverUpAndDownArrowsInDropDownYears : function(e)
	{
		var ind = this.objectIndex;
		
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
		src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';	
		this.scrollInYearDropDownActive = true;	
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInYearDropDown(' + scrollDirection + ')',100);
	}
	// }}}
	,
	// {{{ __mouseoutUpAndDownArrowsInDropDownYears()
    /**
     *	Mouse away from year drop down arrow
     *	
     * @private	
     */		
	__mouseoutUpAndDownArrowsInDropDownYears : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');	
		this.scrollInYearDropDownActive = false;		
	}
	// }}}
	,
	// {{{ __scrollInYearDropDown()
    /**
     *	Scroll the year drop down as long as the scrollInYearDropDownActive is true
     *	
     *
     *
     * @private	
     */		
	__scrollInHourDropDown : function(scrollDirection)
	{
		if(!this.scrollInHourDropDownActive)return;
		var ind = this.objectIndex;	
		this.hourDropDownOffsetInHour+=scrollDirection;
		this.__populateHoursInsideDropDownHours();
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInHourDropDown(' + scrollDirection + ')',150);
	}
	
	// }}}
	,	
	// {{{ __mouseoverUpAndDownArrowsInDropDownHours()
    /**
     *	Mouse over arrows inside drop down (hour)
     *	
     *
     *
     * @private	
     */		
	__mouseoverUpAndDownArrowsInDropDownHours : function(e)
	{
		var ind = this.objectIndex;		
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
		src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';	
		this.scrollInHourDropDownActive = true;	
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInHourDropDown(' + scrollDirection + ')',100);
	}
	// }}}
	,
	// {{{ __mouseoutUpAndDownArrowsInDropDownHours()
    /**
     *	Mouse out from arrow inside hour drop down.
     *	
     *
     *
     * @private	
     */		
	__mouseoutUpAndDownArrowsInDropDownHours : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');	
		this.scrollInHourDropDownActive = false;		
	}
	// }}}	
	,
	// {{{ __scrollInYearDropDown()
    /**
     *	Scroll the year drop down as long as the scrollInYearDropDownActive is true
     *	
     *
     *
     * @private	
     */		
	__scrollInMinuteDropDown : function(scrollDirection)
	{
		if(!this.scrollInMinuteDropDownActive)return;
		var ind = this.objectIndex;	
		this.minuteDropDownOffsetInMinute+=scrollDirection;
		this.__populateMinutesInsideDropDownMinutes();
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInMinuteDropDown(' + scrollDirection + ')',150);
	}
	
	// }}}
	,	
	// {{{ __mouseoverUpAndDownArrowsInDropDownMinutes()
    /**
     *	Mouse over arrows inside drop down (minute)
     *	
     * @private	
     */		
	__mouseoverUpAndDownArrowsInDropDownMinutes : function(e)
	{
		var ind = this.objectIndex;		
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
		src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';	
		this.scrollInMinuteDropDownActive = true;	
		setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInMinuteDropDown(' + scrollDirection + ')',100);
	}
	// }}}
	,
	// {{{ __mouseoutUpAndDownArrowsInDropDownMinutes()
    /**
     *	Mouse out from arrow inside minute drop down.
     *	
     * @private	
     */		
	__mouseoutUpAndDownArrowsInDropDownMinutes : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');	
		this.scrollInMinuteDropDownActive = false;		
	}
	// }}}
	,
	// {{{ __mouseoverYearInDropDown()
    /**
     *	Mouse over - year in year drop down.
     *	
     * @private	
     */		
	__mouseoverYearInDropDown : function()
	{
		this.className = this.className + ' DHTMLSuite_calendar_dropdownAYearOver';	
		
	}
	// }}}
	,
	// {{{ __mouseoutYearInDropDown()
    /**
     *	Mouse over - year in year drop down.
     *	
     * @private	
     */		
	__mouseoutYearInDropDown : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAYearOver','');
		
	}
	// }}}
	,
	// {{{ __mouseoverHourInDropDown()
    /**
     *	Mouse over - hour in hour drop down.
     *	
     * @private	
     */		
	__mouseoverHourInDropDown : function()
	{
		this.className = this.className + ' DHTMLSuite_calendar_dropdownAnHourOver';	
		
	}
	// }}}
	,
	// {{{ __mouseoutHourInDropDown()
    /**
     *	Mouse out - hour in hour drop down.
     *	
     * @private	
     */		
	__mouseoutHourInDropDown : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAnHourOver','');
		
	}
	// }}}
	,
	// {{{ __mouseoverMinuteInDropDown()
    /**
     *	Mouse over effect - minute in minute drop down.
     *	
     * @private	
     */		
	__mouseoverMinuteInDropDown : function()
	{
		this.className = this.className + ' DHTMLSuite_calendar_dropdownAMinuteOver';	
		
	}
	// }}}	
	,
	// {{{ __mouseoutMinuteInDropDown()
    /**
     *	Mouse over effect - month in minute drop down.
     *	
     * @private	
     */		
	__mouseoutMinuteInDropDown : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAMinuteOver','');
		
	}
	// }}}
	,
	// {{{ __mouseoverMonthInDropDown()
    /**
     *	Mouse over effect - minute in month drop down.
     *	
     * @private	
     */		
	__mouseoverMonthInDropDown : function()
	{
		this.className = this.className + ' DHTMLSuite_calendar_dropdownAMonthOver';	
		
	}
	// }}}
	,
	// {{{ __mouseoutMonthInDropDown()
    /**
     *	Mouse out effect - month in month drop down.
     *	
     * @private	
     */		
	__mouseoutMonthInDropDown : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAMonthOver','');
		
	}
	// }}}
	,
	// {{{ __mouseoverCalendarDay()
    /**
     *	Mouse over effect - a day in the calendar view
     *	
     * @private	
     */		
	__mouseoverCalendarDay : function()
	{
		this.className = this.className + ' DHTMLSuite_calendarDayOver';		
	}
	// }}}
	,
	// {{{ __mouseoutCalendarDay()
    /**
     *	Mouse out effect - a day in the calendar view
     *	
     * @private	
     */		
	__mouseoutCalendarDay : function()
	{		
		this.className = this.className.replace(' DHTMLSuite_calendarDayOver','');
	}
	// }}}
	,
	// {{{ __mouseoverCalendarButton()
    /**
     *	Mouse over effect - close button
     *	
     * @private	
     */		
	__mouseoverCalendarButton : function()
	{
		this.className = this.className + ' DHTMLSuite_calendarButtonOver';
	}
	// }}}
	,
	// {{{ __mouseoutCalendarButton()
    /**
     *	Remove mouse over effect from close button
     *	
     * @private	
     */		
	__mouseoutCalendarButton : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendarButtonOver','');
	}	
	// }}}
	,
	// {{{ __mouseoverMonthAndYear()
    /**
     *	Mouse over effect - month and year in the heading
     *	
     * @private	
     */		
	__mouseoverMonthAndYear : function()
	{
		this.className = this.className + ' DHTMLSuite_calendarHeaderMonthAndYearOver';	
			
	}
	// }}}
	,
	// {{{ __mouseoutMonthAndYear()
    /**
     *	Remove mouse over effect - month and year in the heading
     *	
     * @private	
     */			
	__mouseoutMonthAndYear : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendarHeaderMonthAndYearOver','');
	}	,
	// {{{ __mouseoverHourAndMinute()
    /**
     *	Mouse over effect - Hour and minute in timer bar
     *	
     * @private	
     */		
	__mouseoverHourAndMinute : function()
	{
		this.className = this.className + ' DHTMLSuite_calendarTimeBarHourAndMinuteOver';	
			
	}
	// }}}
	,
	// {{{ __mouseoutHourAndMinute()
    /**
     *	Remove mouse over effect - Hour and minute in timer bar
     *	
     * @private	
     */			
	__mouseoutHourAndMinute : function()
	{
		this.className = this.className.replace(' DHTMLSuite_calendarTimeBarHourAndMinuteOver','');
	}
	,
	// {{{ __positionCalendar()
    /**
     *	Position the calendar
     *
     * @private	
     */		
	__positionCalendar : function()
	{
		if(!this.positioningReferenceToHtmlElement)return;
		this.divElement.style.position='absolute';
		this.divElement.style.left = (DHTMLSuite.commonObj.getLeftPos(this.positioningReferenceToHtmlElement) + this.positioningOffsetXInPixels) + 'px';
		this.divElement.style.top = (DHTMLSuite.commonObj.getTopPos(this.positioningReferenceToHtmlElement) + this.positioningOffsetYInPixels) + 'px';
		
	}
	// }}}	
	,
	// {{{ __setInitialData()
    /**
     *	Set initial calendar properties sent to the constructor in an associative array
     *	
     *	@param Array propertyArray - Array of calendar properties
     *								 
     * @private	
     */		
	__setInitialData : function(propertyArray)
	{
		
		if(propertyArray.id)this.id = propertyArray.id;	
		if(propertyArray.targetReference)this.targetReference = propertyArray.targetReference;	
		if(propertyArray.calendarModelReference)this.calendarModelReference = propertyArray.calendarModelReference;	
		if(propertyArray.callbackFunctionOnDayClick)this.callbackFunctionOnDayClick = propertyArray.callbackFunctionOnDayClick;	
		if(propertyArray.callbackFunctionOnMonthChange)this.callbackFunctionOnMonthChange = propertyArray.callbackFunctionOnMonthChange;	
		if(propertyArray.callbackFunctionOnClose)this.callbackFunctionOnClose = propertyArray.callbackFunctionOnClose;
		if(propertyArray.displayCloseButton || propertyArray.displayCloseButton===false)this.displayCloseButton = propertyArray.displayCloseButton;
		if(propertyArray.displayNavigationBar || propertyArray.displayNavigationBar===false)this.displayNavigationBar = propertyArray.displayNavigationBar;
		if(propertyArray.displayTodaysDateInNavigationBar || propertyArray.displayTodaysDateInNavigationBar===false)this.displayTodaysDateInNavigationBar = propertyArray.displayTodaysDateInNavigationBar;
		if(propertyArray.minuteDropDownInterval)this.minuteDropDownInterval = propertyArray.minuteDropDownInterval;
		if(propertyArray.numberOfRowsInHourDropDown)this.numberOfRowsInHourDropDown = propertyArray.numberOfRowsInHourDropDown;
		if(propertyArray.numberOfRowsInMinuteDropDown)this.numberOfRowsInHourDropDown = propertyArray.numberOfRowsInMinuteDropDown;
		if(propertyArray.numberOfRowsInYearDropDown)this.numberOfRowsInYearDropDown = propertyArray.numberOfRowsInYearDropDown;
		if(propertyArray.isDragable || propertyArray.isDragable===false)this.isDragable = propertyArray.isDragable;
		if(propertyArray.displayTimeBar || propertyArray.displayTimeBar===false)this.displayTimeBar = propertyArray.displayTimeBar;
				
	}
	// }}}	
	
}