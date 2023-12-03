define([ 'marionette', 'views/tabItemView' ], function(Marionette, TabItemView) {

	// Tab item container.
	var TabItemsView = Marionette.CollectionView.extend({
		tagName : 'ul',
		childViewContainer : 'ul',
		childView : TabItemView
	});

	return TabItemsView;

});
