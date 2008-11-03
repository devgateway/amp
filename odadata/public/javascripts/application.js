/* (c) 2007-2008 ODAdata S.A. */

/*
 * General methods used throught the entire JS code of this page
 * Might be worth to create an ODAjslib later.
 */
document.getElementsByClassName = function(tn, cl) {
	var retnode = [];
	var myclass = new RegExp('\\b'+cl+'\\b');
	var elem = this.getElementsByTagName(tn);
	for (var i = 0; i < elem.length; i++) {
		var classes = elem[i].className;
		if (myclass.test(classes)) 
			retnode.push(elem[i]);
	}
	return retnode;
};

function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	return [curleft,curtop];
}

/* 
 * More specific stuff 
 * TODO: Move into separate behavior files
 */
function cofundings_toggle(checked) {
	rows = $('tr.cofundings_row');
	if (checked) {
		rows.show();
	} else {
		rows.hide();
	}
	$('#cofundings_toggle_cb').attr('checked', checked);
};

/* 
 * Page initialization
 */
$(document).ready(function() {
  $('div.expander').expander({
		expandText: '(read more)',
		slicePoint: 200,
		userCollapseText: '(show less)'
	});
	$('div.short_expander').expander({
		expandText: '(more)',
		slicePoint: 20,
		userCollapseText: '(show less)'
	});
});

/*
 * Open ODAdata style reports popup
 */
function report_window(url) {
    report = window.open(url, "_blank", "width=800,height=600,scrollbars=yes,resizable=yes");
    report.focus();
}
