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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>

var field;

function y2k(number) { 
	return (number < 1000) ? number + 1900 : number; 
}	

var today = new Date();
var curr_day   = today.getDate();
var curr_month = today.getMonth();
var curr_year  = y2k(today.getYear());


var names = new makeArray0('<digi:trn jsFriendly="true" key='calendar:January'>January</digi:trn>','<digi:trn jsFriendly="true" key='calendar:February'>February</digi:trn>','<digi:trn jsFriendly="true" key='calendar:March'>March</digi:trn>','<digi:trn jsFriendly="true" key='calendar:April'>April</digi:trn>','<digi:trn jsFriendly="true" key='calendar:May'>May</digi:trn>','<digi:trn jsFriendly="true" key='calendar:June'>June</digi:trn>','<digi:trn jsFriendly="true" key='calendar:July'>July</digi:trn>','<digi:trn jsFriendly="true" key='calendar:August'>August</digi:trn>','<digi:trn jsFriendly="true" key='calendar:September'>September</digi:trn>','<digi:trn jsFriendly="true" key='calendar:October'>October</digi:trn>','<digi:trn jsFriendly="true" key='calendar:November'>November</digi:trn>','<digi:trn jsFriendly="true" key='calendar:December'>December</digi:trn>');
var days  = new makeArray0(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var dow   = new makeArray0('<digi:trn jsFriendly="true" key='calendar:Sun'>Sun</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Mon'>Mon</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Tue'>Tue</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Wed'>Wed</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Thu'>Thu</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Fri'>Fri</digi:trn>','<digi:trn jsFriendly="true" key='calendar:Sat'>Sat</digi:trn>');

function makeArray0() {
    for (i = 0; i < makeArray0.arguments.length; i++)
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

	str  = '<HTML><HEAD><TITLE>Calendar<\/TITLE>\n';
	str += '<SCRIPT LANGUAGE="JavaScript"> \n';
	str += ' function changeDay(day) { \n';
	str += ' 	opener.curr_day = day + \'\'; \n';
	str += '		opener.restart(); } ';

	str += ' function changeMonth() {\n';
   str += '		opener.curr_month = document.Cal.Month.options[document.Cal.Month.selectedIndex].value + \'\';\n';
	str += '		opener.reload(); }\n';

	str += ' function changeYear() {\n';
   str += ' 	opener.curr_year = document.Cal.Year.options[document.Cal.Year.selectedIndex].value + \'\';\n';
	str += '		opener.reload(); }\n';	

	str += ' function makeArray0() {\n';
	str += ' for (i = 0; i<makeArray0.arguments.length; i++) this[i] = makeArray0.arguments[i]; }\n';

	str += ' var names = new makeArray0(\'' + names[0] + '\',\'' + names[1] + '\',\'' + names[2] + '\',\'' + names[3] + '\',\'' + names[4] + '\',\'' + names[5] + '\',\'' + names[6] + '\',\'' + names[7] + '\',\'' + names[8] + '\',\'' + names[9] + '\',\'' + names[10] + '\',\'' + names[11] + '\'); \n';
	str += ' var days = new makeArray0(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); \n';
	str += ' var dow = new makeArray0(\'' + dow[0] + '\',\'' + dow[1] + '\',\'' + dow[2] + '\',\'' + dow[3] + '\',\'' + dow[4] + '\',\'' + dow[5] + '\',\'' + dow[6] + '\'); \n';
	str += ' <\/SCRIPT>	\n';
	
	str += '</HEAD><BODY BGCOLOR="#FFFFFF"><CENTER>\n';
	str += '<FORM NAME="Cal"><TABLE BGCOLOR="#FFFFFF" border="0"><TR><TD ALIGN=LEFT width="100%">\n';

	str += '<FONT COLOR="#0000BB" FACE="Arial" SIZE=+1>' + names[Month] + ' ' + Year + '<\/FONT><\/TD><TD WIDTH=50% ALIGN=RIGHT>\n';
	str += '<SELECT NAME="Month" onChange="changeMonth();">';

	for (t_month=0;t_month<12; t_month++) {
		if (t_month == Month) 
			str += '<OPTION VALUE="' + t_month + '" SELECTED>' + names[t_month]+ '<\/OPTION>\n';
		else 
			str += '<OPTION VALUE="' + t_month + '">' + names[t_month] + '<\/OPTION>\n';
	}

	str += '<\/SELECT><SELECT NAME="Year" onChange="changeYear();">\n';

	for (t_year=1950; t_year<2049; t_year++) {
		if (t_year == Year) 
			str += '<OPTION VALUE="' + t_year + '" SELECTED>' + t_year + '<\/OPTION>\n';
		else
			str += '<OPTION VALUE="' + t_year + '">'          + t_year + '<\/OPTION>\n';
	}

	str += '<\/SELECT><\/TD><\/TR><TR><TD align="center" COLSPAN=2>\n';
	
	firstDay = new Date(Year,Month,1);
	startDay = firstDay.getDay();

	if (((Year % 4 == 0) && (Year % 100 != 0)) || (Year % 400 == 0))
		days[1] = 29; 
	else
		days[1] = 28;

	str += '<TABLE CALLSPACING=0 cellpadding="0" border="1" BORDERCOLORDARK="#FFFFFF" BORDERCOLORLIGHT="#C0C0C0"><TR>\n';

	for (i=0; i<7; i++)
		str += '<TD WIDTH=50 align="center" VALIGN=MIDDLE><FONT SIZE=-1 COLOR="#000000" FACE="ARIAL"><B> ' + dow[i]+ '<\/B><\/FONT><\/TD>\n';

	str += '<\/TR><TR align="center" VALIGN=MIDDLE>';

	var column = 0;
	var lastMonth = Month - 1;
	if (lastMonth == -1) lastMonth = 11;

	for (i=0; i < startDay; i++, column++)
		str += '<TD WIDTH=50 HEIGHT=30><FONT SIZE=-1 COLOR="#808080" FACE="ARIAL">' + (days[lastMonth]-startDay+i+1) + '<\/FONT><\/TD>\n';

	for (i=1; i<=days[Month]; i++, column++) {
		str += '<TD WIDTH=50 HEIGHT=30>' + '<A HREF="javascript:changeDay(' + i + ')"><FONT SIZE=-1 FACE="ARIAL" COLOR="#0000FF">' + i + '<\/FONT><\/A>' +'<\/TD>\n';
						 
		if (column == 6) {
			str += '<\/TR><TR align="center" VALIGN=MIDDLE>\n';
			column = -1;
		}
	}

	if (column > 0) {
		for (i=1; column<7; i++, column++)
			str +=  '<TD WIDTH=50 HEIGHT=30><FONT SIZE=-1 COLOR="#808080" FACE="ARIAL">' + i + '<\/FONT><\/TD>\n';
	}

	str += '<\/TR><\/TABLE><\/FORM><\/TD><\/TR><\/TABLE>\n';
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
