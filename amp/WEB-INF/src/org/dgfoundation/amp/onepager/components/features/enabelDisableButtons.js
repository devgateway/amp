function disableButton(){
	$('input[type=button][data-is_submit=true]').attr('disabled', 'disabled');
	}
function enableButtons(){
	$('input[type=button][data-is_submit=true]').removeAttr('disabled');
}
