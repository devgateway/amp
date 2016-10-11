/**
 * this file should NOT be used starting with AMP 2.11
 * left here in case it is needed.
 */
/**
 * hacks related to loading bootstrap within an iframe
 * include this file in the INCLUDING page (old AMP) if not using bootstrap layout only
 * @param elem
 * @returns
 *  
 *  	DO NOT CHANGE THIS FILE IF YOU DO NOT UNDERSTAND WHAT YOU ARE DOING!
 *  	IT IS VERY FRAGILE SINCE WE HAVE A RESIZEABLE IFRAME WHICH USES PNOTIFY!
 *  
 *  COUNTER: number of times pledges module has been broken: <!== 3 ==!>
 * 
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
	
	function window_resized(){
		var buffer = 50;
	    var height = Math.max(get_number($(window).height()), 
	    		get_number(window.innerHeight)); //document.documentElement.clientHeight; // was: clientHeight
	    height -= pageY(document.getElementById('bootstrap_iframe'))+ buffer ;
	    height = (height < 0) ? 0 : height;
	    //outerDocument.getElementById('bootstrap_iframe').style.height = (height - 0) + 'px';

		$('#bootstrap_iframe').height(height);
	}
	
	$(document).ready(function(){
		window_resized();
	});
	
	window.onresize = function(){
		window_resized();
	};
	