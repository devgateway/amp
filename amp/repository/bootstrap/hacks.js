/**
 * hacks related to loading bootstrap within an iframe
 * include this file in the INCLUDING page (old AMP) if not using bootstrap layout only
 * @param elem
 * @returns
 */
	function pageY(elem) {
	    return elem.offsetParent ? (elem.offsetTop + pageY(elem.offsetParent)) : elem.offsetTop;
	}

	function get_number(nr)
	{
		if (typeof(nr) == 'undefined')
			return 0;
		if (nr == null)
			return 0;
		if (isNaN(nr))
			return 0;
		return parseInt(nr);
	}
	
	function window_resized() {
		var height = $('#bootstrap_iframe').contents().find('html').height();
		$('#bootstrap_iframe').height(height);
	}
	
	$(document).ready(function(){
		window_resized();
	});

	window.onresize = function(){
 	 	window_resized();
	};
	