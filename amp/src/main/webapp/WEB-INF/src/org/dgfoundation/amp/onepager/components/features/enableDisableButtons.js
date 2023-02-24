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