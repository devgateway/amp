// various scripts for the multilingual field entry jsp

function check_multilingual_value_entered(html_prefix)
{
	var divId = html_prefix + "_holder";
	var valueExists = false;
	var allInputs = $('#' + divId).find('.multilingual_input_element');
	for(var i = 0; i < allInputs.length; i++){
		valueExists |= (allInputs[i].value.trim().length > 0);
	};
	return valueExists;
}

function multilingual_serialize(html_prefix)
{
	var divId = html_prefix + "_holder";
	var allInputs = $('#' + divId).find('.multilingual_input_element');
	var rs = "";
	var seen_valid_values = false;
	for(var i = 0; i < allInputs.length; i++)
	{
		var elem = allInputs[i];
		if (rs.length > 0)
			rs = rs + "&";
		rs = rs + elem.name + "=" + encodeURIComponent(elem.value);
		seen_valid_values |= (elem.value.trim().length > 0);
	};
	if (!seen_valid_values){
//		var errmsg = 'please enter a value in at least one language';
	//	throw errmsg;
		return null;
	}		
	return rs;

}

/**
 * initializes YahooTabs and anything else needed given a multilingualFieldEntry dta.prefix
 * @param holderId - the parameter dta.prefix passed to multilingualFieldEntry
 */
function initMultilingualInput(holderId){
	(function(id){
		var selector = holderId + "_holder";
		var yahooTabs = new YAHOO.widget.TabView(selector);
		yahooTabs.set('activeIndex', 0); // select first tab
	})(holderId);
}


function focusOnTitle (language) {
	var name = "AmpReports_name_"+language;
	
	//leave a few milliseconds for the component to render
	setTimeout (function(){$( "[name='"+name+"']" ).focus();}, 150);
}


