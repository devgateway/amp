define([ 'marionette', 'views/tabBodyView' ], function(Marionette, TabBodyView) {

	// Content item container.
	var TabBodysView = Marionette.CollectionView.extend({
		tagName : 'div',
		childViewContainer : 'div',
		childView : TabBodyView
	});

	return TabBodysView;

});
