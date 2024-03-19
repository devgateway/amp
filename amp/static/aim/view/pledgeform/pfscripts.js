/**
 * pledges-form specific scripts
 * @author Dolghier Constantin
 */


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
});


$(document).ready(function() // enable javascript-backed ajax forms
{
	if ($('#pledge_form_sectors').size() > 0)
	{
		window.sectorsController = new InteractiveFormArea('#pledge_form_sectors', '/selectPledgeProgram.do', 'selected_sector', 'pledge_sector', // using the programs AJAX page for this too
		[
		 	{id: 'sector_id_select', action: 'rootSectorChanged', attr: 'rootSectorId'},
		 	{id: 'sector_item_select', action: 'sectorSelected', attr: 'sectorId'}
		]);
	};

	if ($('#pledge_form_locations').size() > 0)
	{
		window.locationsController = new InteractiveFormArea('#pledge_form_locations', '/selectPledgeLocation.do', 'selected_loc', 'add_locations', 
		[
		 	{id: 'location_impl_level_select', action: 'implLevelChanged', attr: 'implLevelId'},
		 	{id: 'location_impl_location_select', action: 'implLocationChanged', attr: 'implLocationId'},
		 	{id: 'location_location_select', action: 'locationSelected', attr: 'locationId'}
		]);
	};
	
	if ($('#pledge_form_programs').size() > 0)
	{
		window.programsController = new InteractiveFormArea('#pledge_form_programs', '/selectPledgeProgram.do', 'selected_program', 'pledge_program', 
		[
		 	{id: 'program_id_select', action: 'rootThemeChanged', attr: 'rootThemeId'},
		 	{id: 'program_item_select', action: 'themeSelected', attr: 'themeId'}
		]);
	};
	
	if ($('#pledge_form_funding').size() > 0)
	{
		window.fundingsController = new InteractiveFormArea('#pledge_form_funding', '/selectPledgeProgram.do', 'selected_funding', 'pledge_funding',
			[] // no select's - that area is empty
		);
	}

	if ($('#pledge_form_documents').size() > 0)
	{
		window.documentsController = new InteractiveFormArea('#pledge_form_documents', '/selectPledgeProgram.do', 'selected_document', 'pledge_document',
			[] // no select's - that area is empty
		);
	}

});

function pledge_form_do_validation(bigDivSelector){
	return amp_bootstrap_form_validate(bigDivSelector);
}

/**
 * returns true IFF all elements have "validation ok" after onblur being fired on them all
 * @param bigDivSelector
 * @returns {Boolean}
 */
function pledge_form_validate(bigDivSelector){
	var validation_ok = pledge_form_do_validation(bigDivSelector);
	if (validation_ok)
		show_error_message("Validation Result", "Everything ok!", "success");
/*	else
		show_error_message("Validation Result", "Found errors. Please check areas in red");*/ // no need for supplemental message - each validation error shows ones anyway
	return validation_ok;
}

function pledge_form_submit(bigDivSelector){
	var validated = pledge_form_do_validation(bigDivSelector);
	if (!validated)
		return;
	var data = getFormData('#pledge_form_big_div');
	formatDatesToISO(data);
	$.post("/savePledge.do",
			data,
			function(data){
				if (data.trim() != 'ok')
				{
					var errs = $.parseJSON(data.trim()); //org.dgfoundation.amp.forms.ValidationError instances
					for(var i = 0; i < errs.length; i++)
						show_error_message('Error Submitting', 'Error submitting form: ' + errs[i].errMsg);
				}
				else
					go_to_pledge_list();
		});
}


function formatDatesToISO(arrayData){
	var expectedDateFormat = 'YYYY-MM-DD';
	var dateRangeStartItem = $('.validate-date-range-start');
	var dateFormat = dateRangeStartItem.data('date-format') ? dateRangeStartItem.data('date-format') : 'YYYY-MM-DD';
	for (index = 0; index < arrayData.length; ++index) {
	    if (arrayData[index].name.indexOf('Date') != -1) {	        
	    	arrayData[index].value = moment(arrayData[index].value, dateFormat).format(expectedDateFormat);        
	    }
	}
	return arrayData;
}



function go_to_pledge_list(){
	parent.window.location = '/viewPledgesList.do';
}

/**
 * send a POST requesting for the PledgeForm lock to be released and then go to the pledge-list-page
 * @param bigDivSelector
 */
function pledge_form_cancel(bigDivSelector){
	//BootstrapDialog.alert("I want banana!");
	$.post("/selectPledgeProgram.do",
			{extraAction: 'cancel'},
			function(data){
				if (data.trim() == 'ok')
					go_to_pledge_list();
			});
}

init_amp_magic('pledge_form_big_div');

function register_heart_beat(){
	setInterval(do_heart_beat, 1000);
}

function do_heart_beat(){
	$.post("/addPledge.do?heartBeat=true", {}, function(data) {/* do nothing*/});
}
