define([ 'marionette', 'views/tabContentView' ], function(Marionette, TabContentView) {

	// Content item container.
	var TabContentsView = Marionette.CollectionView.extend({
		tagName : 'div',
		childViewContainer : 'div',
		childView : TabContentView
	});

	return TabContentsView;

});
