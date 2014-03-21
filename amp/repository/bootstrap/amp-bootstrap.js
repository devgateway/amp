/**
 * AMP-bootstrap scripts
 * @author Dolghier Constantin
 */

$.fn.disable = function() { // disable a div in its entirety
	$(this).addClass("disabled-div");
	$(this).find("input,textarea,button,a").addClass("disabled-by-me").attr("disabled", "disabled");
    return true;
};

$.fn.enable = function() { // redo the actions of "disable" from above
	$(this).removeClass("disabled-div");
	$(this).find("input.disabled-by-me, textarea.disabled-by-me, button.disabled-by-me, a.disabled-by-me").removeAttr("disabled").removeClass("disabled-by-me");
	return true;
};

if(!Array.prototype.last) {
    Array.prototype.last = function() {
        return this[this.length - 1];
    };
}

function fix_aim_button(elem)
{
	elem.addClass("input-sm").css("float", "right");
}

//$(document).on('change', '.aim-button-to-fix input', function() // change the "Implementation Level" select
//{
//	fix_aim_button($(this));
//});

$(document).ready(function()
{
	$('.aim-button-to-fix input').each(function(){fix_aim_button($(this));});
	$('.auto-placeholder input[id]').each(function() // automatically set the "placeholder" attribute of inputs contained in this
			{
				var id = $(this).attr('id');
				var label = $('label[for="' + id + '"]');
				$(this).attr('placeholder', label.html()); //copy the insides of the label to the "placeholder" attribute of the input
			});

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