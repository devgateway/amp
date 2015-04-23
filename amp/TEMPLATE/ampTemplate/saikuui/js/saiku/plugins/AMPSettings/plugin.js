var AMPSettings = Backbone.View.extend({
			events : {
				'click .edit_amp_settings' : 'add_amp_settings'
			},
			
			SETTINGS : {
				"currency": "1",
				"calendar": "2"
			},

			initialize : function(args) {
		    	if(!Settings.AMP_REPORT_API_BRIDGE) return; 

				var self = this;
				this.workspace = args.workspace;

				this.id = _.uniqueId("amp_settings_");
				$(this.el).attr({
					id : this.id
				});

				_.bindAll(this, "render", "show", "add_amp_settings", "hideContainer", "applySettings");

				this.add_button();
				this.workspace.toolbar.amp_settings = this.show;

				$(this.workspace.el).find('.workspace_results').prepend($(this.el).hide());
				//TODO: Move this to use the this.el correctly instead of a set html
				//$("#settings-container").find(".panel-heading .close").on("click", this.hideContainer);
				$("#settings-container").find(".cancel").on("click", this.hideContainer);
				$("#settings-container").find(".apply").on("click", this.applySettings);

				var raw_settings = this.workspace.query.get('raw_settings');
				settings = {
						"1": raw_settings.currencyCode,
						"2": raw_settings.calendar.ampFiscalCalId,
						"3": raw_settings.unitsMultiplier
					};
				window.currentSettings = settings;

				$.ajax({
					url : '/rest/amp/settings',
					async : false
				}).done(function(data) {
					self.populate_dropdowns(data);
				});
			},
			
			hideContainer: function() {
				this.settings_button.removeClass('on');
				$("#settings-container").hide();
			},
			
			applySettings: function() {
				var settings = {
					"1": $('#amp_currency').val(),
					"2": $('#amp_calendar').val()
				};
				this.workspace.query.set('settings', settings);
				window.currentSettings = settings;
				
				this.workspace.query.run_query(null, settings);
				this.settings_button.removeClass('on');
				$("#settings-container").hide();
			},
			
			populate_dropdowns: function(data) {
				var currencyValues = _.findWhere(data, {"id": this.SETTINGS["currency"]});
				$.each(currencyValues.options, function(index, object) {   
				     $('#amp_currency')
				         .append($("<option></option>")
				         .attr("value", object.value)
				         .text(object.name)); 
				});
				var calendarValues = _.findWhere(data, {"id": this.SETTINGS["calendar"]});
				$.each(calendarValues.options, function(index, object) { 
				     $('#amp_calendar')
				         .append($("<option></option>")
				         .attr("value", object.value)
				         .text(object.name)); 
				});
			},
			add_button : function() {

				this.settings_button = $(
						'<a href="#amp_settings" class="amp_settings button i18n" title="AMP Settings">Settings</a>')
						.css(
								{
									'width' : "44px"
								});

				var $settings_li = $('<li></li>').append(
						this.settings_button);
				$(this.workspace.toolbar.el).find("ul").prepend($settings_li);
			},

			show : function(event, ui) {
				var self = this;
				$(this.el).toggle();
				var settings;
				settings = window.currentSettings;
				$('#amp_currency').val(settings["1"]);
				$('#amp_calendar').val(settings["2"]);

				$(event.target).toggleClass('on');

				if ($(event.target).hasClass('on')) {
					$('#settings-container').show();
				} else {
					$('#settings-container').hide();
				}

			},

			render : function() {
				$(this.el).empty();
			},

			add_amp_settings : function(event) {
				var self = this;
				alert("add_amp_settings: To be implemented");
			},

			save_amp_settings : function() {
				var self = this;
				alert("save_amp_settings: To be implemented");
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
