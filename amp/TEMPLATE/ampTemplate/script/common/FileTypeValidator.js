/*
Initial author: Viorel Chihai

Utility class for validation file types by extensions
*/

FileTypeValidator = {
	rootURL : '/rest/mimetypes/allowed',
	enabled : false,
	errorMessage: 'The file does not have a valid extension',
	extensions : [], 
	
	init: function() {
		$.ajax({
		type: 'GET',
		url: FileTypeValidator.rootURL,
		dataType: "json", // data type of response
		success: function(data) {
			FileTypeValidator.extensions = data == null ? [] : [].concat.apply([], data.map(function(a) {
					return a.extensions
				}));
			}
		})
	},
	
	isValid: function(fileName) {
		return (new RegExp('(' + this.extensions.join('|').replace(/\./g, '\\.') + ')$')).test(fileName);
	}
};

FileTypeValidator.init();

$.getScript("/TEMPLATE/ampTemplate/script/common/TranslationManager.js")
	.done(function() {
		FileTypeValidator.errorMessage =  TranslationManager.getTranslated(FileTypeValidator.errorMessage);
	});
