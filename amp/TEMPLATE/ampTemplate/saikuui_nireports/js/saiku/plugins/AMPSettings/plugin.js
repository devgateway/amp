Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		Saiku.logger.log("AMPSettings.new_workspace");
		if (typeof args.workspace.amp_settings == "undefined") {
			args.workspace.amp_settings = window.AMPSettings.SettingsWidget({
				draggable : true,
				caller : 'REPORTS',
				isPopup: true,
				definitionUrl: '/rest/settings-definitions/reports'
			});	
			args.workspace.generalSettings = window.AMPSettings.GeneralSettings();
			args.workspace.generalSettings.load();
		}
	}

	function clear_workspace(args) {
		Saiku.logger.log("AMPSettings.clear_workspace");
		if (typeof args.workspace.amp_settings != "undefined") {
			$(args.workspace.amp_settings.el).hide();
		}
	}

	for (var i = 0, len = Saiku.tabs._tabs.length; i < len; i++) {
		var tab = Saiku.tabs._tabs[i];
		if (tab.caption != "Home") {
			new_workspace({
				workspace : tab.content
			});
		}
	};

	Saiku.session.bind("workspace:new", new_workspace);
	Saiku.session.bind("workspace:clear", clear_workspace);
});
