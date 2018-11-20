var AMPSettingsView = Backbone.View.extend({			
			initialize : function(args) {
				Saiku.logger.log("AMPSettings.initialize");
				var self = this;
				this.workspace = args.workspace;
				this.settings_data = this.workspace.query.get('settings_data');
				this.original_currency = this.workspace.query.get('original_currency');
				this.id = _.uniqueId("amp_settings_");
				$(this.el).attr({
					id : this.id
				});

				_.bindAll(this, "render", "show", "hide_container", "apply_settings", "init_settings_widget","add_button");
				
				this.add_button();
				this.init_settings_widget();
				this.workspace.toolbar.amp_settings = this.show;
				$(this.workspace.el).find('.workspace_results').prepend($(this.el).hide());					
			},
			init_settings_widget: function(){
				var self  = this;
			    this.settingsWidget =  new AMPSettings.SettingsWidget({
					el:$('#settings-popup'),
					draggable : true,
					caller : 'REPORTS',
					isPopup: true,
					definitionUrl: '/rest/settings-definitions/reports'
				});	
				
			    window.settingsWidget = this.settingsWidget;
			    this.settingsWidget.definitions.loaded.done(function() {
			    	self.settingsWidget.restoreFromSaved(self.settings_data);	
			    });
			    
				Saiku.events.listenTo(this.settingsWidget, 'cancel', function() {
					self.hide_container();
				});
				Saiku.events.listenTo(this.settingsWidget, 'close', function() {
					self.hide_container();
				});
				Saiku.events.listenTo(this.settingsWidget, 'applySettings', function(data) {
					self.apply_settings(self.settingsWidget.toAPIFormat());
				});				
				
				window.generalSettings = new AMPSettings.GeneralSettings();		
				this.workspace.generalSettings = window.generalSettings;
				generalSettings.load();
			},
			hide_container: function() {
				Saiku.logger.log("AMPSettings.hideContainer");
				this.settings_button.removeClass('on');
				$("#settings-popup").hide();
			},
			
			apply_settings: function(settings) {
				Saiku.logger.log("AMPSettings.applySettings");				
				this.workspace.query.set('settings', settings);								
				this.workspace.query.run_query(null, settings);
				this.settings_button.removeClass('on');
				$("#settings-popup").hide(); //hide settings dialog
			},			
			add_button : function() {
				Saiku.logger.log("AMPSettings.add_button");
				this.settings_button = $('<a href="#amp_settings" class="amp_settings button i18n" title="Settings">Settings</a>').css(
								{
									'width' : "auto"
								});

				var $settings_li = $('<li></li>').append(this.settings_button);
				$(this.workspace.toolbar.el).find("ul").prepend($settings_li);
			},

			show : function(event, ui) {
				Saiku.logger.log("AMPSettings.show");				
				var self = this;
				$(this.el).toggle();					
				$(event.target).toggleClass('on');
				if ($(event.target).hasClass('on')) {					
					this.settingsWidget.show();
					$('#settings-popup').show();
				} else {
					$('#settings-popup').hide();
				}
			},
			
			render : function() {
				Saiku.logger.log("AMPSettings.render");
				$(this.el).empty();
			}

		});
Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		Saiku.logger.log("AMPSettings.new_workspace");
		
		if (typeof args.workspace.amp_settings == "undefined") {
			args.workspace.amp_settings = new AMPSettingsView({
				workspace : args.workspace
			});			
			
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
