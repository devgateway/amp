define([ 'marionette', 'text!views/html/tabBodyTemplate.html' ], function(Marionette, tabBody) {

	// Content item.
	var TabBodyView = Marionette.ItemView.extend({
		tagName : 'div',
		template : _.template(tabBody)
	});

	return TabBodyView;

});
