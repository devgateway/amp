function disableButton(){
	$('input[type=button][data-is_submit=true]').attr('disabled', 'disabled');
	//$('input[type=button][data-is_submit=true]').css('background-color','red');
	}

var enableButtons2 = function(){
	$('input[type=button][data-is_submit=true]').removeAttr('disabled');
//$('input[type=button][data-is_submit=true]').css('background-color','#5E8AD1');
}

$( document).ready( function () {
	    enableButtons2();
	});

function enableButtons(){}

// If the session expires (e.g. logged out from another browser tab) while editing an
// activity, a Wicket AJAX save/save-as-draft comes back as a bare 401/403 instead of the
// <ajax-response> XML the client expects. Wicket then never runs the response's
// JavaScript, so code that hides #save_overlay and re-enables the buttons never executes,
// leaving a black/gray full-screen overlay with no feedback. Subscribe to Wicket's global
// ajax failure topic to clean that up and send the user back to the login page.
var ampSessionExpiredHandled = false;

function ampHandleAjaxSessionExpired() {
	if (ampSessionExpiredHandled) {
		return;
	}
	ampSessionExpiredHandled = true;
	$('#save_overlay').hide();
	enableButtons2();
	alert('Your session has expired. Please log in again. Any unsaved changes were not saved.');
	window.onbeforeunload = null;
	window.location.href = '/showLayout.do/?layout=login';
}

(function subscribeToAjaxFailure(retriesLeft) {
	if (!window.Wicket || !Wicket.Event || !Wicket.Event.subscribe || !Wicket.Event.Topic) {
		if (retriesLeft > 0) {
			window.setTimeout(function () {
				subscribeToAjaxFailure(retriesLeft - 1);
			}, 50);
		}
		return;
	}

	Wicket.Event.subscribe(Wicket.Event.Topic.AJAX_CALL_FAILURE, function (jqEvent, attrs, jqXHR) {
		if (jqXHR && (jqXHR.status === 401 || jqXHR.status === 403)) {
			ampHandleAjaxSessionExpired();
		}
	});
})(40);