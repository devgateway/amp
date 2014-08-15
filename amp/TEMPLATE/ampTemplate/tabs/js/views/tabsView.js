var app = app || {};

(function() {
	'use strict';

	// Tab item.
	app.TabItemView = Marionette.ItemView.extend({
		tagName : 'li',
		template : '#tab-template'
	});

	// Tab item container.
	app.TabItemsView = Marionette.CollectionView.extend({
		tagName : 'ul',
		childViewContainer : 'ul',
		childView : app.TabItemView
	});

	// Content item.
	app.TabContentView = Marionette.ItemView.extend({
		tagName : 'div',
		template : '#tab-content-template'
	});

	// Content item container.
	app.TabContentsView = Marionette.CollectionView.extend({
		tagName : 'div',
		childViewContainer : 'div',
		childView : app.TabContentView
	});

})();