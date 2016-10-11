
CKEDITOR.editorConfig = function( config ) {
	config.extraPlugins = 'removeformat';
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
		config.toolbar = [
	['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','PasteFromWord', 'RemoveFormat','FontSize']
	];
		//config.skin = 'kama';
	
		config.width = 340;
};
