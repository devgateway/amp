/**
 * various AMP-Bootstrap utils
 * @author Dolghier Constantin
 * 
 */ 
$(document).ready(function()
{
	$.pnotify.defaults.styling = "bootstrap3";
	$.pnotify.defaults.history = false;
});

var stack_bottomleft = {"dir1": "right", "dir2": "up", "push": "top"}; // where the validation errors are shown

/**
 * removes all validation-statuses from elements given by a jQuery entity and adds the given one
 * @param selector
 * @param val_status
 */
function setValidationStatus(selector, val_status)
{
	selector.parent().removeClass('has-success').removeClass('has-warning').removeClass('has-error').addClass(val_status);
}

function clean_all_error_messages()
{
	$.pnotify_remove_all();
}

function show_error_message(title, text)
{
	$.pnotify({
	    'title': title,
	    'text': text,
	    //type: 'error',
	    /*addclass: "stack-bottomleft",
	    stack: stack_bottomleft,*/
	    'delay': 2000,
	    maxonscreen: 2,
	    mouse_reset: false,
	    width: '250px',
	    stack: {"dir1": "down", "dir2": "left"}, // detach from other notices - if we are in iframe
	    before_open: function(pnotify) {
            // Position this notice relative to the mouse cursor
            pnotify.css(build_notification_position(pnotify));
        },
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
