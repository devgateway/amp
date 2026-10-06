/**
 * various AMP-Bootstrap utils
 * @author Dolghier Constantin
 * 
 */ 

var global_disable_cleaning_error_messages = null;

$(document).ready(function()
{
	$.pnotify.defaults.styling = "bootstrap3";
	$.pnotify.defaults.history = false;
});

//var stack_bottomleft = {"dir1": "right", "dir2": "up", "push": "top"}; // where the validation errors are shown

/**
 * removes all validation-statuses from elements given by a jQuery entity and adds the given one
 * @param selector
 * @param val_status
 */
function setValidationStatus(selector, val_status)
{
	selector.parent().removeClass('has-success').removeClass('has-warning').removeClass('has-error').addClass(val_status);
//	selector.removeClass('has-success').removeClass('has-warning').removeClass('has-error').addClass(val_status);
}

function hasValidationError(selector)
{
	return selector.parent().hasClass('has-warning') || selector.parent().hasClass('has-error');
}

function clean_all_error_messages()
{
	if (!global_disable_cleaning_error_messages) // you normally only set this ON during a global validation run. TRY / FINALLY !!!
		$.pnotify_remove_all();
}


/**
 * shows a pnotify error msg
 * @param title
 * @param text
 * @param the_type: one of 'error', 'warning', 'success': http://sciactive.github.io/pnotify/#demos-simple
 */
function show_error_message(title, text, the_type, the_delay)
{
	if (!the_type)
		the_type = 'error';
	
	if (!the_delay)
		the_delay = 2500;
//	var the_stack = null;
//	var the_before_open = function(pnotify) {
//        					// Position this notice relative to the mouse cursor
//        					pnotify.css(build_notification_position(pnotify));
//    					};    					
//	if (forced_pnotify_stack)
//	{
//		the_stack = forced_pnotify_stack;
//		the_before_open = undefined;
//	}
//	else
//		the_stack = {"dir1": "down", "dir2": "left"}; // detach from other notices - if we are in iframe
	$.pnotify({
	    'title': title,
	    'text': text,
	    type: the_type,
	    //type: 'error',
	    /*addclass: "stack-bottomleft",
	    stack: stack_bottomleft,*/
	    'delay': the_delay,
	    maxonscreen: 5,
	    mouse_reset: false,
	    width: '250px'//,
//	    stack: the_stack,
//	    before_open: the_before_open
	});
}

/**
 * builds a notification position based on current mouse pointer, whether we are in an iframe or not, moon phase etc
 */
function build_notification_position(pnotify)
{
	var lft = currentMousePos.x + 20; // by default go to the left, 20 spacing
	if (lft + pnotify.width() > $(window).width()) // oops, we got out of the window
		lft = currentMousePos.x - 20 - pnotify.width();
	
	var tp = currentMousePos.y - 20 - pnotify.height(); // by default we go upper
	if (tp < 0)
		tp = currentMousePos.y + 20;
//	tp = currentMousePos.y;
//	console.log(tp);
	return {
        "top": tp, //($(window).height() / 2) - (pnotify.height() / 2),
        "left": lft //($(window).width() / 2) - (pnotify.width() / 2)
    };
}

var currentMousePos = { x: -1, y: -1 }; // this will always store the current mouse position
$(document).mousemove(function(event) {
    currentMousePos.x = event.pageX;
    currentMousePos.y = event.pageY;
});
