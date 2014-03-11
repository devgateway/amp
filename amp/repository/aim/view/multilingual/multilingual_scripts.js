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
	for(var i = 0; i < allInputs.length; i++)
	{
		var elem = allInputs[i];
		if (rs.length > 0)
			rs = rs + "&";
		rs = rs + elem.name + "=" + encodeURIComponent(elem.value);
	};
	return rs;

}
