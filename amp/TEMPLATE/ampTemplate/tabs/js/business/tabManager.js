define([ 'marionette', 'text!views/html/saveTabDialogTemplate.html', 'models/tab',
		'translationManager', 'business/filter/filterManager', 'jquery', 'jqueryui' ], function(
		Marionette, saveTabDialogTemplate, Tab, TranslationManager, FilterManager, jQuery) {

	"use strict";

	function TabManager() {
		if (!(this instanceof TabManager)) {
			throw new TypeError("TabManager constructor cannot be called as a function.");
		}
	}

	TabManager.prototype = {
		constructor : TabManager
	};  
  
	TabManager.openSaveTabDialog = function(id, name) {
		TranslationManager.getAvailableLanguages().done (function (languages){
			var trnTitle = TranslationManager.getTranslated("Please enter a title for this tab:");
			var translatedNames = app.TabsApp.currentTab.get('translatedNames');

			var renderOptions = {
		    	languages: languages,
		    	translatedNames:translatedNames
		    };

			var SaveTabDialogView = Marionette.ItemView.extend({
				template : _.template(saveTabDialogTemplate,renderOptions),
				events: {
				        'click .tab': 'openTab'
				    },
			openTab: function (e) {
				$('.tab').removeClass('focused');
				var el = $(e.currentTarget);
				el.addClass('focused');
				var language = el.attr('data-label');
				$('.save-tab').hide();
				$('.save-tab.'+language).show();
				e.preventDefault();
			}
			});
			var auxTab = app.TabsApp.currentTab;
			var dialogView = new SaveTabDialogView({
				model : auxTab
			});
			dialogView.render();
			jQuery(dialogView.el).dialog({
				modal : true,
				title : trnTitle,
				width : 400,
				close: function(event, ui) 
		        { 
					jQuery(this).remove();
		        },
		        position: { my: "center bottom", at: "center center", of: window }
			});
			jQuery(".buttonify").button();
			TranslationManager.searchAndTranslate();

			jQuery(".ui-dialog-content #saveTabsBtn").on("click", function() {
				FilterManager.saveTab(dialogView);
			});			
		});

	};
	return TabManager;
});