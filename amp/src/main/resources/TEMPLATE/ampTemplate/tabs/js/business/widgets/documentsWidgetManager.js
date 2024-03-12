define([ 'marionette', 'models/document', 'collections/documents',
		'text!views/html/desktopResourcesContainerTemplate.html', 'translationManager',
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
		var dynamicContentView = new DynamicContentView();
		app.TabsApp.documentsWidgetRegion.show(dynamicContentView);

        var docItemTagName, docItemClassName, docItemTemplate;
        if (documents.length == 0) {
            var fakeDoc = new Document();
            documents.add(fakeDoc);
			var message = TranslationManager.getTranslated("No results");
            docItemTagName = 'p';
            docItemClassName = 'right_menu_empty';
            docItemTemplate = _.template(message);
        } else {
            docItemTagName = 'li';
            docItemClassName = 'document-item tri tri-desktop';
            docItemTemplate = _.template(documentTemplate);
        }
        var DocumentItemView = Marionette.ItemView.extend({
            tagName : docItemTagName,
            className : docItemClassName,
            template : docItemTemplate
        });
        var DocumentsItemsView = Marionette.CollectionView.extend({
            tagName : 'ul',
            childViewContainer : 'ul',
            childView : DocumentItemView
        });
        var documentsItemsView = new DocumentsItemsView({
            collection : documents
        });
        dynamicContentView.content.show(documentsItemsView);
	};

	return DocumentsWidgetManager;
});