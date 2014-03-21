/**
 * pledges-form specific scripts
 * @author Dolghier Constantin
 */

function pledges_form_check_percentage(inputItem, itemsClass, error_message_numeric, error_message_sum_100)
{
	if (isNaN(inputItem.value))
	{
		show_error_message(error_message_numeric);
		setValidationStatus($(inputItem), 'has-error');
		$(inputItem).focus();
		return false;
	}
	setValidationStatus($('.' + itemsClass), 'has-success'); // ok
	clean_all_error_messages();
	var totalValue = 0;
	$('.' + itemsClass).each(function(i, obj) {
		totalValue += parseFloat(obj.value);
	});
	if (totalValue > 100) {
		show_error_message(error_message_sum_100);
		setValidationStatus($('.' + itemsClass), 'has-warning'); // ok
		$(inputItem).focus();
		return false;  
	}
	return true;
}

function pledges_form_update_area(url, action, replaceId)
{
	$.post(url,
			{extraAction: action},
			function(data)
			{
				$('#' + replaceId).html(data);
			});
}

/**
 * refreshes the table holding the currently-selected locations and their percentages
 */
function pledge_locations_refresh_table()
{
	pledges_form_update_area('/selectPledgeLocation.do', 'render_locations_list', 'pledge_locations_table');
}

/**
 * refreshes the area holding the "implementation level / implementation location / region" area
 */
function pledge_locations_refresh_add_area()
{
	pledges_form_update_area('/selectPledgeLocation.do', 'render_locations_add', 'pledge_add_location_area');
}

//function pledge_programs_refresh_table()
//{
//	pledges_form_update_area('/selectPledgeProgram.do', 'render_programs_list', 'pledge_programs_list');
//}
//
//function pledge_programs_refresh_add_area()
//{
//	pledges_form_update_area('/selectPledgeProgram.do', 'render_programs_add', 'pledge_programs_list');
//}

function pledges_form_delete_location(nr)
{
	$.post('/removePledgeLocation.do', 
			{ deleteLocs: nr },
			function(data)
			{
				pledge_locations_refresh_table();
			});
}

function copy_from_contact_1_to_contact_2(input_elem)
{
	var elem1 = $(input_elem);
	if (!elem1.hasClass('do_not_copy')){
		var elem2selector = '#pledge_contact_2 input[name="' + elem1.attr('name').replace("contact1", "contact2") + '"]'; 
		$(elem2selector).val(elem1.val());
	}
}

$(document).ready(function()
{
	$('#pledge_add_location_area').hide(); // only on init load - the ones loaded through ajax should stay the same
	$('#add_location_button').click(function() // click on "Add Location"
	{
		$('#add_location_button').hide();
		$('#pledge_add_location_area').show();
		//$('#myModal').modal('show');
		//$('#pledge_add_location_area').modal('show');
	});
	//$('#pledge_add_program_area').hide();
//	$('#add_program_button').click(function() //click on "Add Program"
//	{
//		$(this).hide();
//		$('#pledge_programs_list').disable();
//		$('#pledge_add_program_area').show();
//	});
	
	$('#sameContactCheckBox').change(function(){
		var is_checked = $(this).is(":checked");
		if (is_checked){ 
			// checkbox just checked: copy all values from contact1 to contact2
			$('#pledge_contact_1 input[name]').each(function(){copy_from_contact_1_to_contact_2(this);});
		} else{ // checkbox just unchecked: clear everything in contact2 area
			$('#pledge_contact_2 input[name]').each(function(){
				var elem = $(this);
				if (!elem.hasClass('do_not_copy')) elem.val(''); // clear all elements
			});
		}});
	
	$('#pledge_contact_1 input[name]').keyup(function(){
		if ($('#sameContactCheckBox').is(":checked")) // copy values from contact1 to contact2 as they are typed, IFF the corresponding checkbox is checked
			copy_from_contact_1_to_contact_2(this);
		});
	
	$('.phone-number').blur(function(){
		return pledges_form_check_phone_number(this, please_enter_phone_number_message);
	});
	
	$('.email-address').blur(function(){
		return pledges_form_check_email(this, please_enter_email_message);
	});
});
	
$(document).on('change', '#impl_level_select', function() // change the "Implementation Level" select
{
	var elem = this;
	$.post("/selectPledgeLocation.do",
		{
			levelId: $(elem).val(),
			edit: true
		},
		function(data){
			pledge_locations_refresh_add_area();
		});
});
			
$(document).on('change', '#impl_location_select', function() // change the "Implementation Location" select
{
	var elem = this;
	$.post("/selectPledgeLocation.do",
		{
			implemLocationLevel: $(elem).val(),
			edit: true
		},
		function (data) {
			pledge_locations_refresh_add_area();
		});
});
			
function pledges_hide_add_location()
{
	$('#pledge_add_location_area').hide();
	$('#add_location_button').show();
}

function pledges_submit_add_locations()
{
	var location_ids = [];
	$('#location_id_select option:selected').each(function ()
		{
			if (($(this).attr('value') != '0') && ($(this).attr('value') != '-1'))
				location_ids.push($(this).attr('value'));
		});
	if (location_ids.length == 0)
	{
		// nothing selected -> just hide the area
		pledges_hide_add_location();
		return;
	}
	$.post("/selectPledgeLocation.do",{
		locs: location_ids.join(','),
		extraAction: "add_locations"
	},
	function(data) {
		if (data.trim() == 'ok'){
			pledges_hide_add_location();
			pledge_locations_refresh_table();
		} else {
			show_error_message('Error adding locations', data);
		}
	});
}

//$(document).on('change', '#program_id_select', function() // change the "Implementation Location" select
//		{
//			var elem = this;
//			$.post("/selectPledgeProgram.do",
//				{
//					extraAction: 'add_program',
//					rootThemeId: $(elem).val()
//				},
//				function (data) {
//					pledge_programs_refresh_table();
//				});
//		});

/**
 * validates an input with a generic function and highlights any found error
 * @param inputItem
 * @param validation_function
 * @param error_message
 * @returns {Boolean}
 */
function pledges_form_simple_validation(inputItem, validation_function, error_message)
{
	if ((inputItem.value.length > 0) && (!validation_function(inputItem.value)))
	{
		show_error_message(error_message);
		setValidationStatus($(inputItem), 'has-error');
		$(inputItem).focus();
		return false;
	}
	setValidationStatus($(inputItem), 'has-success'); // ok
	clean_all_error_messages();
	return true;
}

function pledges_form_check_email(inputItem, error_message)
{
	return pledges_form_simple_validation(inputItem, looksLikeEmail, error_message);
}

function pledges_form_check_phone_number(inputItem, error_message)
{
	return pledges_form_simple_validation(inputItem, looksLikePhoneNumber, error_message);
}

/**
 * should be called on every new ajax addition
 */
function on_element_loaded()
{
	$(document).ready(function()
	{
		//alert('called');
		$('select').addClass('text-left');
		$('select').selectpicker({
			style: 'btn-primary btn-xs',
			'data-style': 'btn-primary',
			//size: 5
		});
	});
}

on_element_loaded();
bootstrap_iframe(); // init iframe hacks

