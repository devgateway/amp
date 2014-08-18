define([ 'marionette' ], function(Marionette) {

	// Content item.
	var TabContentView = Marionette.ItemView.extend({
		tagName : 'div',
		template : '#tab-content-template'
	});

	return TabContentView;

});
