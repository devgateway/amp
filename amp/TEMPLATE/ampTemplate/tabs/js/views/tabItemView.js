define([ 'marionette', 'text!views/html/tabTemplate.html' ], function(Marionette, tabTemplate) {

	// Tab item.
	var TabItemView = Marionette.ItemView.extend({
		tagName : 'li',
		className : 'tab-item',
		template : _.template(tabTemplate)
	});

	return TabItemView;

});