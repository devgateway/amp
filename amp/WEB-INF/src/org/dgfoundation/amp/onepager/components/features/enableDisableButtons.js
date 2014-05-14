function disableButton(){
	$('input[type=button][data-is_submit=true]').attr('disabled', 'disabled');
	//$('input[type=button][data-is_submit=true]').css('background-color','red');
	}
function enableButtons(){
	$('input[type=button][data-is_submit=true]').removeAttr('disabled');
	//$('input[type=button][data-is_submit=true]').css('background-color','#5E8AD1');
}
