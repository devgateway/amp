/**
 * AMP-bootstrap scripts
 * @author Dolghier Constantin
 */

//Remove disable enable function that was added to jquery since its being overwritten by amp-boilerplate

function disableAreaOnPledges(area) { // disable a div in its entirety
	$(area).addClass("disabled-div");
	$(area).find("input,textarea,button,a").addClass("disabled-by-me").attr("disabled", "disabled");
	return true;
};

function enableAreaOnPledges(area) { // redo the actions of "disable" from above
	$(area).removeClass("disabled-div");
	$(area).find("input.disabled-by-me, textarea.disabled-by-me, button.disabled-by-me, a.disabled-by-me").removeAttr("disabled").removeClass("disabled-by-me");
	return true;
};

if(!Array.prototype.last) {
    Array.prototype.last = function() {
        return this[this.length - 1];
    };
}

if (typeof String.prototype.trim != 'function') {
	  String.prototype.trim = function (){
	    return this.replace(/^\s+|\s+$/gm,'');
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
	$('.fields_group_title').click(function()
	{
		$(this).siblings('.fields_group_contents').slideToggle();
		return false;
	});
});


function looksLikeEmail(email) {
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (filter.test(email)) {
		return true;
	} else {
		return false;
	}
}

function looksLikePhoneNumber(input)
{
	var nr = input.value.replace('+', '').replace('-', '').replace(' ', '');
	//var isnum = /^\d*$/.test(nr);
	var isnum = true;//AMP-18125: accepting all characters
	return isnum && nr.length < 20;
}

function looksLikeNumber(nr){
	if (nr.length == 0)
		return true;
	
	if (isNaN(nr))
		return false;
	
	var number = parseInt(nr);
	return number > 0;
}

function looksLikeAmount(nr){
	if (nr.length == 0)
		return true;
	
	nr = nr.replace(/\s/g, '').replace(/\./g, '').replace(/,/g, '');
			
	if (isNaN(nr))
		return false;
	
	var number = parseInt(nr);
	return number > 0;
}

function isYearValidator(input){
	var nr = input;
	return looksLikeNumber(nr) && (parseInt(nr) > 1900) && (parseInt(nr) < 2100);
}


/**
 * should be called on every new ajax addition, or all functionality dependent on $(document).ready() will not work
 * TODO: maybe change everything to $(document).on(...);
 */
function init_custom_looks(divId)
{
	$(divId + ' select').addClass('text-left');
	$(divId + ' select').selectpicker({
		style: 'btn-primary btn-xs',
		'data-style': 'btn-primary btn-xs',
		//size: 5
	});
	$(divId + ' select.live-search').attr('data-live-search', 'true'); // Struts is stupid and does not allow to inject custom attributes
	
    $(divId + ' .date-range-start').each(function(){
    	$(this).datetimepicker({pickTime: false});
    	$(this).on("dp.change", function(e) {
    		$(divId).parent().find('.date-range-end').data("DateTimePicker").setMinDate(e.date);
    	});
    });
    
    $(divId + ' .date-range-end').each(function(){
    	$(this).datetimepicker({pickTime: false});
    	$(this).on("dp.change", function(e) {
    		$(divId).parent().find('.date-range-start').data("DateTimePicker").setMaxDate(e.date);
    	});
    });

}

function init_ajax_upload(divId){
	$(function () {
		$(divId + ' .fileupload').each(function(){
			var item = this;
			var url = $(item).closest('.file-upload-container').attr('data-post-url');
			$(item).fileupload({
				'url': url,
				dataType: 'json',
				done: function (e, data) {
//					$.each(data.result, function (index, file) {
//						alert(index);
//						alert(file.name);
//					});
					$.each(data.result, function (index, file) {

                        $('<p/>').attr("id", file.url.substring(file.url.lastIndexOf('?') + 1, file.url.length))
                            .text(file.name).appendTo(divId + ' .files-list');
                    });
				},
				progressall: function (e, data) {
					var progress = parseInt(data.loaded / data.total * 100, 10);
					$(divId + ' .progress .progress-bar').css(
							'width',
							progress + '%'
					);
				}
			}).prop('disabled', !$.support.fileInput)
	        	.parent().addClass($.support.fileInput ? undefined : 'disabled');
		});
		});
}

function init_amp_magic(divIdInput){
	$(document).ready(function(){
		init_amp_magic_immediately(divIdInput);
	});
}

function init_amp_magic_immediately(divIdInput){
	var divId = divIdInput;
	if (divId.charAt(0) != '#')
		divId = '#' + divId;
	init_custom_looks(divId);
	init_validation(divId);
	init_ajax_upload(divId);
}
