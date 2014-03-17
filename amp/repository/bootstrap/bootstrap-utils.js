// various AMP-Bootstrap utils
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
	    addclass: "stack-bottomleft",
	    stack: stack_bottomleft,
	    'delay': 2000,
	    maxonscreen: 2,
	    mouse_reset: false,
	    width: '250px'
	});
}

