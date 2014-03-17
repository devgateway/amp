// Pledges Form Scripts

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

function pledges_form_delete_location(nr)
{
	$.post('/removePledgeLocation.do', 
			{ deleteLocs: nr },
			function(data)
			{
				pledge_locations_refresh_table();
			});
}


$(document).ready(function()
{
	$('#pledge_add_location_area').hide(); // only on init load - the ones loaded through ajax should stay the same
	$('#add_location_button').click(function() // click on "Add Location"
	{
		$('#add_location_button').hide();
		$('#pledge_add_location_area').show();
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
});
	
function pledges_hide_add_location()
{
	$('#pledge_add_location_area').hide();
	$('#add_location_button').show();
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
			style: 'btn-primary btn-sm',
			'data-style': 'btn-primary',
			//size: 5
		});
	});
}

on_element_loaded();
bootstrap_iframe(); // init iframe hacks

