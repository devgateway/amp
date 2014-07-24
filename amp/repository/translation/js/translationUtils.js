/**
 * Call an action that performs a translation (like digi:trn tag) but through Ajax.
 * Can be used to perform translations inside js files (where <% %> doesnt work) and avoid the use of hidden fields in jsp.
 * @param text
 * @param callback
 */
function ajaxTranslate(text, callback) {
	$.ajax({
		url: "/translation/ajaxTranslator.do",
		type: "POST",
		async: false,
		data: {"originalText": text},
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		success: function(data) {
			callback(data.text);			
		}
	});
}