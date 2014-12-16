define([ 'marionette', 'models/document', 'collections/documents',
		'text!views/html/desktopResourcesContainerTemplate.html', 'business/translations/translationManager',
		'text!views/html/documentTemplate.html', 'jquery', 'jqueryui' ], function(Marionette, Document, Documents,
		desktopResourcesContainerTemplate, TranslationManager, documentTemplate, jQuery) {

	"use strict";

	function DocumentsWidgetManager() {
		if (!(this instanceof DocumentsWidgetManager)) {
			throw new TypeError("DocumentsWidgetManager constructor cannot be called as a function.");
		}
	}

	DocumentsWidgetManager.prototype = {
		constructor : DocumentsWidgetManager
	};

	DocumentsWidgetManager.showDocumentsWidget = function() {
		var documents = new Documents();
		documents.fetchData();

		var DynamicContentView = Marionette.LayoutView.extend({
			template : _.template(desktopResourcesContainerTemplate),
			regions : {
				content : '#documents-container-region'
			}
		});

		var DocumentItemView = Marionette.ItemView.extend({
			tagName : 'li',
			className : 'document-item',
			template : _.template(documentTemplate)
		});
		var DocumentsItemsView = Marionette.CollectionView.extend({
			tagName : 'ul',
			childViewContainer : 'ul',
			childView : DocumentItemView
		});
		var documentsItemsView = new DocumentsItemsView({
			collection : documents
		});

		var dynamicContentView = new DynamicContentView();
		app.TabsApp.documentsWidgetRegion.show(dynamicContentView);
		dynamicContentView.content.show(documentsItemsView);
	};

	return DocumentsWidgetManager;
});