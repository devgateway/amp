define([ 'marionette', 'text!views/html/tabContentTemplate.html' ], function(Marionette, tabContent) {

	// Content item.
	var TabContentView = Marionette.ItemView.extend({
		tagName : 'div',
		template : _.template(tabContent)
	});

	return TabContentView;

});
