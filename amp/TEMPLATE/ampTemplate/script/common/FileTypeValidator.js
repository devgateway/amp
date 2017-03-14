/*
Initial author: Viorel Chihai

Utility class for validation file types by extensions
*/

FileTypeValidator = {
	rootURL : '/rest/resourcemanager/file-types/allowed',
	settingsURL: '/rest/settings-definitions/resource-manager',
	errorMessage: 'The uploaded file does not have an allowed extension',
	exceptionMessage : 'The file type validation has not been initialized',
	extensions : [], 
	
	init: function() {
		$.ajax({
			type: 'GET',
			url: FileTypeValidator.settingsURL,
			dataType: "json", // data type of response
			success: function(data) {
				FileTypeValidator.validationEnabled = data == null 
					? false 
					: (data.filter(function(a) {
							return a.id=='limit-file-to-upload'}
						)[0].value == 'true');
				}
		});
		
		$.ajax({
		type: 'GET',
		url: FileTypeValidator.rootURL,
		dataType: "json", // data type of response
		success: function(data) {
			FileTypeValidator.extensions = data == null ? [] : [].concat.apply([], data.map(function(a) {
					return a.extensions;
				}));
			}
		});
	},
	
	isValid: function(fileName) {
		if (typeof this.validationEnabled == 'undefined' ) {
			throw this.exceptionMessage;
		} else if (this.validationEnabled) {
			return (new RegExp('(' + this.extensions.join('|').replace(/\./g, '\\.') + ')$')).test(fileName);
		}
		
		return true;
	}
};

FileTypeValidator.init();

$.getScript("/TEMPLATE/ampTemplate/script/common/TranslationManager.js")
	.done(function() {
		FileTypeValidator.errorMessage = TranslationManager.getTranslated(FileTypeValidator.errorMessage);
		FileTypeValidator.exceptionMessage = TranslationManager.getTranslated(FileTypeValidator.exceptionMessage);
	});
