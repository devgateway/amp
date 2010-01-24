var field;

function y2k(number) {
	return (number < 1000) ? number + 1900 : number;
}

var today = new Date();
var curr_day   = today.getDate();
var curr_month = today.getMonth();
var curr_year  = y2k(today.getYear());

var names = new makeArray0('January','February','March','April','May','June','July','August','September','October','November','December');
var days  = new makeArray0(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var dow   = new makeArray0('Sun','Mon','Tue','Wed','Thu','Fri','Sat');

function makeArray0() {
    for (i = 0; i<makeArray0.arguments.length; i++)
        this[i] = makeArray0.arguments[i];
}

function reload() {
	openCalendar(curr_month,curr_year);
	calWindow.focus();
}


function openCalendar(month,year) {
	Month = month;
	Year = year;

	var t = ((screen.width)-350)/2;
	var l = ((screen.height)-270)/2;

	calWindow = window.open('','Calendar','resizable=no,width=350,height=270,top='+l+',left='+t);

	str  = '<HTML><HEAD><TITLE>Calendar</TITLE>';
	str += '<SCRIPT LANGUAGE="JavaScript"> ';
	str += ' function changeDay(day) { ';
	str += ' 	opener.curr_day = day + \'\'; ';
	str += '		opener.restart(); } ';

	str += ' function changeMonth() {';
   str += '		opener.curr_month = document.Cal.Month.options[document.Cal.Month.selectedIndex].value + \'\';';
	str += '		opener.reload(); }';

	str += ' function changeYear() {';
   str += ' 	opener.curr_year = document.Cal.Year.options[document.Cal.Year.selectedIndex].value + \'\';';
	str += '		opener.reload(); }';

	str += ' function makeArray0() {';
	str += ' for (i = 0; i<makeArray0.arguments.length; i++) this[i] = makeArray0.arguments[i]; }';

	str += ' var names = new makeArray0(\'January\',\'February\',\'March\',\'April\',\'May\',\'June\',\'July\',\'August\',\'September\',\'October\',\'November\',\'December\'); ';
	str += ' var days = new makeArray0(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); ';
	str += ' var dow = new makeArray0(\'Sun\',\'Mon\',\'Tue\',\'Wed\',\'Thu\',\'Fri\',\'Sat\'); ';
	str += '</SCRIPT>	';

	str += '</HEAD><BODY BGCOLOR="#FFFFFF"><CENTER>';
	str += '<FORM NAME="Cal"><TABLE BGCOLOR="#FFFFFF" BORDER=0><TR><TD ALIGN=LEFT WIDTH=100%>';
	str += '<FONT COLOR="#0000BB" FACE="Arial" SIZE=+1>' + names[Month] + ' ' + Year + '<\/FONT><\/TD><TD WIDTH=50% ALIGN=RIGHT>';
	str += '<SELECT NAME="Month" onChange="changeMonth();">';

	for (t_month=0;t_month<12; t_month++) {
		if (t_month == Month)
			str += '<OPTION VALUE="' + t_month + '" SELECTED>' + names[t_month] + '<\/OPTION>';
		else
			str += '<OPTION VALUE="' + t_month + '">'          + names[t_month] + '<\/OPTION>';
	}

	str += '<\/SELECT><SELECT NAME="Year" onChange="changeYear();">';

	for (t_year=1950; t_year<2049; t_year++) {
		if (t_year == Year)
			str += '<OPTION VALUE="' + t_year + '" SELECTED>' + t_year + '<\/OPTION>';
		else
			str += '<OPTION VALUE="' + t_year + '">'          + t_year + '<\/OPTION>';
	}

	str += '<\/SELECT><\/TD><\/TR><TR><TD ALIGN=CENTER COLSPAN=2>';

	firstDay = new Date(Year,Month,1);
	startDay = firstDay.getDay();

	if (((Year % 4 == 0) && (Year % 100 != 0)) || (Year % 400 == 0))
		days[1] = 29;
	else
		days[1] = 28;

	str += '<TABLE CALLSPACING=0 CELLPADDING=0 BORDER=1 BORDERCOLORDARK="#FFFFFF" BORDERCOLORLIGHT="#C0C0C0"><TR>';

	for (i=0; i<7; i++)
		str += '<TD WIDTH=50 ALIGN=CENTER VALIGN=MIDDLE><FONT SIZE=-1 COLOR="#000000" FACE="ARIAL"><B>' + dow[i] +'<\/B><\/FONT><\/TD>';

	str += '<\/TR><TR ALIGN=CENTER VALIGN=MIDDLE>';

	var column = 0;
	var lastMonth = Month - 1;
	if (lastMonth == -1) lastMonth = 11;

	for (i=0; i<startDay; i++, column++)
		str += '<TD WIDTH=50 HEIGHT=30><FONT SIZE=-1 COLOR="#808080" FACE="ARIAL">' + (days[lastMonth]-startDay+i+1) + '<\/FONT><\/TD>';

	for (i=1; i<=days[Month]; i++, column++) {
		str += '<TD WIDTH=50 HEIGHT=30>' + '<A HREF="javascript:changeDay(' + i + ')"><FONT SIZE=-1 FACE="ARIAL" COLOR="#0000FF">' + i + '<\/FONT><\/A>' +'<\/TD>';

		if (column == 6) {
			str += '<\/TR><TR ALIGN=CENTER VALIGN=MIDDLE>';
			column = -1;
		}
	}

	if (column > 0) {
		for (i=1; column<7; i++, column++)
			str +=  '<TD WIDTH=50 HEIGHT=30><FONT SIZE=-1 COLOR="#808080" FACE="ARIAL">' + i + '<\/FONT><\/TD>';
	}

	str += '<\/TR><\/TABLE><\/FORM><\/TD><\/TR><\/TABLE>';
	str += "<\/CENTER><\/BODY><\/HTML>";

    self.calWindow.document.open();
	self.calWindow.document.write(str);
	self.calWindow.document.close();
}

function sameAsDateClicked(value) {
	if (value == 1) {
		if (document.aimEditActivityForm.actAppDateCheckBox.checked == true) {
			document.aimEditActivityForm.actAppDate.value = document.aimEditActivityForm.propAppDate.value;
		} else {
		}
	}
	else if (value == 2) {
		if (document.aimEditActivityForm.actStartDateCheckBox.checked == true) {
			document.aimEditActivityForm.actStartDate.value = document.aimEditActivityForm.propStartDate.value;
		} else {
		}
	}
	else if (value == 3) {
		if (document.aimEditActivityForm.actCloseDateCheckBox.checked == true) {
			document.aimEditActivityForm.actCloseDate.value = document.aimEditActivityForm.propCloseDate.value;
		} else {
		}
	}
}

function padout(number) { return (number < 10) ? '0' + number : number; }

function restart() {
	document.getElementById(field).value = '' + padout(curr_day) + '/' + padout(curr_month - 0 + 1) + '/' + curr_year;
	calWindow.close();
}

function calendar(f) {
	field = f;
	reload(curr_month,curr_year);
}
