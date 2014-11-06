var AMPSettings = Backbone.View.extend({
			events : {
				'click .edit_amp_settings' : 'add_amp_settings'
			},

			initialize : function(args) {
		    	if(!Settings.AMP_REPORT_API_BRIDGE) return; 

				this.workspace = args.workspace;

				this.id = _.uniqueId("amp_settings_");
				$(this.el).attr({
					id : this.id
				});

				_.bindAll(this, "render", "show", "add_amp_settings");

				this.add_button();
				this.workspace.toolbar.amp_settings = this.show;

				$(this.workspace.el).find('.workspace_results').prepend($(this.el).hide());
				
				//Find container for settings/show it/hide it/apply changes
			},

			add_button : function() {

				var $settings_button = $(
						'<a href="#amp_settings" class="amp_settings button i18n" title="AMP Settings">Settings</a>')
						.css(
								{
									'width' : "44px"
								});

				var $settings_li = $('<li></li>').append(
						$settings_button);
				$(this.workspace.toolbar.el).find("ul").prepend($settings_li);
			},

			show : function(event, ui) {
				var self = this;
				$(this.el).toggle();
				$(event.target).toggleClass('on');

				if ($(event.target).hasClass('on')) {
					alert("To be implemented");
				} else {
					// Hide it
				}

			},

			render : function() {
				$(this.el).empty();
			},

			add_amp_settings : function(event) {
				var self = this;
				alert("To be implemented");
			},

			save_amp_settings : function() {
				var self = this;
				alert("To be implemented");
			},

		});

Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		if (typeof args.workspace.amp_settings == "undefined") {
			args.workspace.amp_settings = new AMPSettings({
				workspace : args.workspace
			});
		}
	}

	function clear_workspace(args) {
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
	}
	;

	Saiku.session.bind("workspace:new", new_workspace);
	Saiku.session.bind("workspace:clear", clear_workspace);
});
