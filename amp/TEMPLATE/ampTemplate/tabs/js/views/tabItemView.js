define([ 'marionette' ], function(Marionette) {

	// Tab item.
	var TabItemView = Marionette.ItemView.extend({
		tagName : 'li',
		template : '#tab-template'
	});

	return TabItemView;

});
