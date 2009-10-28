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
 * Open ODAdata style reports popup
 */
function report_window(url) {
    report = window.open(url, "_blank", "width=800,height=600,scrollbars=yes,resizable=yes");
    report.focus();
}

/*
 * Glossary Tooltips
 */
function initialize_tooltips() {
	$('img.info-icon')
	  .mouseover(function() {
	    var info = $('#' + $(this).attr('id').replace(/_icon$/, ''));
	    var offset = $(this).offset();
	    
	    info.css({ left: 0, top: 0, width: 'auto' });
	    var width = info.width();
	    width = width - parseInt((width - info.height()) / 1.5);
	    
	    var difference = $(document.body).width() - width - 16;
	    if (offset.left > difference) offset.left = difference;
	    
	    this.timer = setTimeout(function() {
	      info
	        .css({ left: offset.left - 16, top: offset.top - 16, width: width })
	        .fadeIn();
	    }, 250);
	  })
	  .mouseout(function() {
	    clearTimeout(this.timer);
	  });
	
	$('div.info').mouseout(function() {
	  $(this).fadeOut();
	});
}

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
	
	initialize_tooltips();
});