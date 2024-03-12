;(function($){
/**
 * jqGrid English Translation
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/
$.jgrid = $.jgrid || {};
$.extend($.jgrid,{
	defaults : {
		recordtext: "<span data-i18n='tabs.common:view'>View</span> {0} - {1} <span data-i18n='tabs.common:of'>of</span> {2}",
		emptyrecords: "<span data-i18n='tabs.common:noRecordsToView'>No records to view</span>",
		loadtext: "<span data-i18n='tabs.common:loading'>Loading...</span>",
		pgtext : "<span data-i18n='tabs.common:page'>Page</span> {0} <span data-i18n='tabs.common:of'>of</span> {1}"
	},
	search : {
		caption: "<span data-i18n='tabs.common:search'>Search...</span>",
		Find: "<span data-i18n='tabs.common:find'>Find</span>",
		Reset: "<span data-i18n='tabs.common:reset'>Reset</span>",
		odata: [{ oper:'eq', text:'equal'},{ oper:'ne', text:'not equal'},{ oper:'lt', text:'less'},{ oper:'le', text:'less or equal'},{ oper:'gt', text:'greater'},{ oper:'ge', text:'greater or equal'},{ oper:'bw', text:'begins with'},{ oper:'bn', text:'does not begin with'},{ oper:'in', text:'is in'},{ oper:'ni', text:'is not in'},{ oper:'ew', text:'ends with'},{ oper:'en', text:'does not end with'},{ oper:'cn', text:'contains'},{ oper:'nc', text:'does not contain'},{ oper:'nu', text:'is null'},{ oper:'nn', text:'is not null'}],
		groupOps: [{ op: "AND", text: "all" },{ op: "OR",  text: "any" }],
		operandTitle : "<span data-i18n='tabs.common:searchOp'>Click to select search operation.</span>",
		resetTitle : "<span data-i18n='tabs.common:resetSearchValue'>Reset Search Value</span>"
	},
	edit : {
		addCaption: "<span data-i18n='tabs.common:addRecord'>Add Record</span>",
		editCaption: "<span data-i18n='tabs.common:editRecord'>Edit Record</span>",
		bSubmit: "<span data-i18n='tabs.common:submit'>Submit</span>",
		bCancel: "<span data-i18n='tabs.common:cancel'>Cancel</span>",
		bClose: "<span data-i18n='tabs.common:close'>Close</span>",
		saveData: "<span data-i18n='tabs.common:saveChangesQuestion'>Data has been changed! Save changes?</span>",
		bYes : "<span data-i18n='tabs.common:yes'>Yes</span>",
		bNo : "<span data-i18n='tabs.common:no'>No</span>",
		bExit : "<span data-i18n='tabs.common:cancel'>Cancel</span>",
		msg: {
			required:"<span data-i18n='tabs.common:fieldRequired'>Field is required</span>",
			number:"<span data-i18n='tabs.common:invalidNumber'>Please, enter valid number</span>",
			minValue:"<span data-i18n='tabs.common:valGE'>value must be greater than or equal to </span>",
			maxValue:"<span data-i18n='tabs.common:valLE'>value must be less than or equal to</span>",
			email: "<span data-i18n='tabs.common:invalidEmail'>is not a valid e-mail</span>",
			integer: "<span data-i18n='tabs.common:invalidInteger'>Please, enter valid integer value</span>",
			date: "<span data-i18n='tabs.common:invalidDate'>Please, enter valid date value</span>",
			url: "<span data-i18n='tabs.common:invalidURL'>is not a valid URL. Prefix required ('http://' or 'https://')</span>",
			nodefined : " <span data-i18n='tabs.common:notDefined'>is not defined!</span>",
			novalue : " <span data-i18n='tabs.common:requireReturnValue'>return value is required!</span>",
			customarray : "<span data-i18n='tabs.common:notArray'>Custom function should return array!</span>",
			customfcheck : "<span data-i18n='tabs.common:customCheck'>Custom function should be present in case of custom checking!</span>"
			
		}
	},
	view : {
		caption: "<span data-i18n='tabs.common:viewRecord'>View Record</span>",
		bClose: "<span data-i18n='tabs.common:close'>Close</span>"
	},
	del : {
		caption: "<span data-i18n='tabs.common:delete'>Delete</span>",
		msg: "<span data-i18n='tabs.common:sureToDelete'>Delete selected record(s)?</span>",
		bSubmit: "<span data-i18n='tabs.common:delete'>Delete</span>",
		bCancel: "<span data-i18n='tabs.common:cancel'>Cancel</span>"
	},
	nav : {
		edittext: "",
		edittitle: "<span data-i18n='tabs.common:edit'>Edit selected row</span>",
		addtext:"",
		addtitle: "<span data-i18n='tabs.common:addRow'>Add new row</span>",
		deltext: "",
		deltitle: "<span data-i18n='tabs.common:deleteRow'>Delete selected row</span>",
		searchtext: "",
		searchtitle: "<span data-i18n='tabs.common:findRecord'>Find records</span>",
		refreshtext: "",
		refreshtitle: "<span data-i18n='tabs.common:reloadGrid'>Reload Grid</span>",
		alertcap: "<span data-i18n='tabs.common:warning'>Warning</span>",
		alerttext: "<span data-i18n='tabs.common:selectRow'>Please, select row</span>",
		viewtext: "",
		viewtitle: "<span data-i18n='tabs.common:viewSelectedRow'>View selected row</span>"
	},
	col : {
		caption: "<span data-i18n='tabs.common:selectColumns'>Select columns</span>",
		bSubmit: "<span data-i18n='tabs.common:ok'>Ok</span>",
		bCancel: "<span data-i18n='tabs.common:cancel'>Cancel</span>"
	},
	errors : {
		errcap : "<span data-i18n='tabs.common:error'>Error</span>",
		nourl : "<span data-i18n='tabs.common:noURL'>No url is set</span>",
		norecords: "<span data-i18n='tabs.common:noRecords'>No records to process</span>",
		model : "<span data-i18n='tabs.common:invalidColModel'>Length of colNames <> colModel!</span>"
	},
	formatter : {
		integer : {thousandsSeparator: ",", defaultValue: '0'},
		number : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: '0.00'},
		currency : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
		date : {
			dayNames:   [
				"<span data-i18n='tabs.common:sun'>Sun", "<span data-i18n='tabs.common:mon'>Mon</span>", "<span data-i18n='tabs.common:tue'>Tue</span>", "<span data-i18n='tabs.common:wed'>Wed</span>", "<span data-i18n='tabs.common:thr'>Thr</span>", "<span data-i18n='tabs.common:fri'>Fri</span>", "<span data-i18n='tabs.common:sat'>Sat</span>",
				"<span data-i18n='tabs.common:sunday'>Sunday</span>", "<span data-i18n='tabs.common:monday'>Monday</span>", "<span data-i18n='tabs.common:tuesday'>Tuesday</span>", "<span data-i18n='tabs.common:wednesday'>Wednesday</span>", "<span data-i18n='tabs.common:thursday'>Thursday</span>", "<span data-i18n='tabs.common:friday'>Friday</span>", "<span data-i18n='tabs.common:saturday'>Saturday</span>"
			],
			monthNames: [
				"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
				"<span data-i18n='tabs.common:january'>January</span>", "<span data-i18n='tabs.common:february'>February</span>", "<span data-i18n='tabs.common:march'>March</span>", "<span data-i18n='tabs.common:april'>April</span>", "<span data-i18n='tabs.common:may'>May</span>", "<span data-i18n='tabs.common:june'>June</span>", "<span data-i18n='tabs.common:july'>July</span>", "<span data-i18n='tabs.common:august'>August</span>", "<span data-i18n='tabs.common:september'>September</span>", "<span data-i18n='tabs.common:october'>October</span>", "<span data-i18n='tabs.common:november'>November</span>", "<span data-i18n='tabs.common:december'>December</span>"
			],
			AmPm : ["am","pm","AM","PM"],
			S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th';},
			srcformat: 'Y-m-d',
			newformat: 'n/j/Y',
			parseRe : /[#%\\\/:_;.,\t\s-]/,
			masks : {
				// see http://php.net/manual/en/function.date.php for PHP format used in jqGrid
				// and see http://docs.jquery.com/UI/Datepicker/formatDate
				// and https://github.com/jquery/globalize#dates for alternative formats used frequently
				// one can find on https://github.com/jquery/globalize/tree/master/lib/cultures many
				// information about date, time, numbers and currency formats used in different countries
				// one should just convert the information in PHP format
				ISO8601Long:"Y-m-d H:i:s",
				ISO8601Short:"Y-m-d",
				// short date:
				//    n - Numeric representation of a month, without leading zeros
				//    j - Day of the month without leading zeros
				//    Y - A full numeric representation of a year, 4 digits
				// example: 3/1/2012 which means 1 March 2012
				ShortDate: "n/j/Y", // in jQuery UI Datepicker: "M/d/yyyy"
				// long date:
				//    l - A full textual representation of the day of the week
				//    F - A full textual representation of a month
				//    d - Day of the month, 2 digits with leading zeros
				//    Y - A full numeric representation of a year, 4 digits
				LongDate: "l, F d, Y", // in jQuery UI Datepicker: "dddd, MMMM dd, yyyy"
				// long date with long time:
				//    l - A full textual representation of the day of the week
				//    F - A full textual representation of a month
				//    d - Day of the month, 2 digits with leading zeros
				//    Y - A full numeric representation of a year, 4 digits
				//    g - 12-hour format of an hour without leading zeros
				//    i - Minutes with leading zeros
				//    s - Seconds, with leading zeros
				//    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
				FullDateTime: "l, F d, Y g:i:s A", // in jQuery UI Datepicker: "dddd, MMMM dd, yyyy h:mm:ss tt"
				// month day:
				//    F - A full textual representation of a month
				//    d - Day of the month, 2 digits with leading zeros
				MonthDay: "F d", // in jQuery UI Datepicker: "MMMM dd"
				// short time (without seconds)
				//    g - 12-hour format of an hour without leading zeros
				//    i - Minutes with leading zeros
				//    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
				ShortTime: "g:i A", // in jQuery UI Datepicker: "h:mm tt"
				// long time (with seconds)
				//    g - 12-hour format of an hour without leading zeros
				//    i - Minutes with leading zeros
				//    s - Seconds, with leading zeros
				//    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
				LongTime: "g:i:s A", // in jQuery UI Datepicker: "h:mm:ss tt"
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				// month with year
				//    Y - A full numeric representation of a year, 4 digits
				//    F - A full textual representation of a month
				YearMonth: "F, Y" // in jQuery UI Datepicker: "MMMM, yyyy"
			},
			reformatAfterEdit : false
		},
		baseLinkUrl: '',
		showAction: '',
		target: '',
		checkbox : {disabled:true},
		idName : 'id'
	}
});
})(jQuery);
