/**
 * AMP-bootstrap scripts
 * @author Dolghier Constantin
 */

function fix_aim_button(elem)
{
	elem.addClass("input-sm").css("float", "right");
}

$(document).on('change', '.aim-button-to-fix input', function() // change the "Implementation Level" select
{
	fix_aim_button($(this));
});

$(document).ready(function()
{
	$('.aim-button-to-fix input').each(function(){fix_aim_button($(this));});
});


function looksLikeEmail(email) {
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (!filter.test(email)) {
		return false;
	}
	return true;
}

function looksLikePhoneNumber(nr)
{
	nr = nr.replace('+', '').replace('-', '').replace(' ', '');
	var isnum = /^\d+$/.test(nr);
	return isnum && nr.length < 20;
}