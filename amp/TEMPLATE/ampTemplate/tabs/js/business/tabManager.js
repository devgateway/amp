define([ 'marionette', 'text!views/html/saveTabDialogTemplate.html', 'models/tab', 'business/translations/translationManager', 'jquery',
		'jqueryui' ], function(Marionette, saveTabDialogTemplate, Tab, TranslationManager, jQuery) {

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
		var trnTitle = TranslationManager.getTranslated("Please enter a title for this tab:");

		var SaveTabDialogView = Marionette.ItemView.extend({
			template : _.template(saveTabDialogTemplate)
		});
		var auxTab = app.TabsApp.currentTab;
		var dialogView = new SaveTabDialogView({
			model : auxTab
		});
		dialogView.render();
		jQuery(dialogView.el).dialog({
			modal : true,
			title : trnTitle,
			width : 400
		});
		jQuery(".buttonify").button();
		TranslationManager.searchAndTranslate();

		jQuery(".settings-container #saveTabsBtn").on("click", function() {
			// TODO: Implement saving code here.
		});
	};

	return TabManager;
});