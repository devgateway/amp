var AMPSettings = Backbone.View.extend({
			events : {
				'click .edit_amp_settings' : 'add_amp_settings'
			},			
			SETTINGS : {
				"currency": "1",
				"calendar": "2",
				"calendarCurrencies": "calendarCurrencies"
			},

			initialize : function(args) {
		    	if(!Settings.AMP_REPORT_API_BRIDGE) return; 
                                
				var self = this;
				this.workspace = args.workspace;

				this.id = _.uniqueId("amp_settings_");
				$(this.el).attr({
					id : this.id
				});

				_.bindAll(this, "render", "show", "add_amp_settings", "hideContainer", "applySettings", "updateCurrenciesForCalendar");

				this.add_button();
				this.workspace.toolbar.amp_settings = this.show;
				this.settings_data = this.workspace.query.get('settings_data');

				$(this.workspace.el).find('.workspace_results').prepend($(this.el).hide());
				//TODO: Move this to use the this.el correctly instead of a set html
				//$("#settings-container").find(".panel-heading .close").on("click", this.hideContainer);
				$("#settings-container").find(".cancel").on("click", this.hideContainer);
				$("#settings-container").find(".apply").on("click", this.applySettings);
				$("#settings-container").find("#amp_calendar").on("change", this.updateCurrenciesForCalendar);

				var raw_settings = this.workspace.query.get('raw_settings');
				settings = {
						"1": raw_settings.currencyCode,
						"2": raw_settings.calendar.ampFiscalCalId,
						"3": raw_settings.unitsMultiplier						
					};
				
				
				if(raw_settings.columnFilterRules.YEAR !== null && raw_settings.columnFilterRules.YEAR !== undefined ){
					settings.yearRange = {};
					settings.yearRange.yearFrom = raw_settings.columnFilterRules.YEAR[0].min;
					settings.yearRange.yearTo = raw_settings.columnFilterRules.YEAR[0].max;
				}				
				window.currentSettings = settings;

				$.ajax({
					url : '/rest/amp/settings',
					async : false
				}).done(function(data) {
					self.populate_dropdowns(data);
					self.currencies = data[0].options;
					if (window.currentSettings["1"] === null) {
						var foundCurrency = _.find(data, function(item) {return item.id === '1'});
						window.currentSettings["1"] = _.find(foundCurrency.options, function(item) { return item.id === foundCurrency.defaultId}).id;
					}
					
					// Keep link between calendars and currencies.
					var calendarCurrencies = _.findWhere(data, {"id": self.SETTINGS["calendarCurrencies"]});
					if (calendarCurrencies !== undefined && calendarCurrencies !== null) {
						window.settings.calendarCurrencies = calendarCurrencies.options;
					}
					
					// Keep all currencies for later.
					window.settings.allCurrencies = _.find(data, function(item) {return item.id === '1'}).options;
				});
			},
			
			hideContainer: function() {
				this.settings_button.removeClass('on');
				$("#settings-container").hide();
			},
			
			applySettings: function() {
				$('#settings-invalid-year-range').hide();
				$('#settings-missing-values-error').hide();
				
				if($('#amp_currency').val() === null || $('#amp_calendar').val() === null) {
					$('#settings-missing-values-error').show();
					return;
				}
				if(parseInt($('#amp_year_range_from').val()) > parseInt($('#amp_year_range_to').val())){
					$('#settings-invalid-year-range').show();
					return;
					
				}
				var settings = {
					"1": $('#amp_currency').val(),
					"2": $('#amp_calendar').val(),
					"3": (window.currentSettings !== undefined ? window.currentSettings["3"] : ''),
					"yearRange":{
						 "yearFrom":$('#amp_year_range_from').val(),
						 "yearTo": $('#amp_year_range_to').val()
						}
				};
				this.workspace.query.set('settings', settings);
				window.currentSettings = settings;
				
				this.workspace.query.run_query(null, settings);
				this.settings_button.removeClass('on');
				$("#settings-container").hide();
			},
			
			populate_dropdowns: function(data) {				
				var selectedCalendar = $('#amp_calendar').val();
				if (selectedCalendar !== null) {
					var currencyValues = _.findWhere(data, {"id": this.SETTINGS["currency"]});
					var calendarCurrencies = _.find(data, function(item) {return item.id === 'calendarCurrencies'}).options;
					var availableCurrenciesForCalendar = _.uniq(_.findWhere(calendarCurrencies, {id: selectedCalendar}).value.split(","));				
					this.populateCurrencyPopup(availableCurrenciesForCalendar, currencyValues.options);										
				} else {
					var calendarValues = _.findWhere(data, {"id": this.SETTINGS["calendar"]});
					$.each(calendarValues.options, function(index, object) { 
					     $('#amp_calendar')
					         .append($("<option></option>")
					         .attr("value", object.value)
					         .text(object.name)); 
					});
					$('#amp_calendar').val(null);
				}
				
				var yearRangeSettings = _.findWhere(this.settings_data, {id:'yearRange'});
				if(yearRangeSettings){
					var fromOptions = yearRangeSettings.value ? _.findWhere(yearRangeSettings.value, {id:'yearFrom'}).value.options : [];
					var toOptions = yearRangeSettings.value ? _.findWhere(yearRangeSettings.value, {id:'yearTo'}).value.options : [];
					
					_.each(fromOptions,function(option ){
						$('#amp_year_range_from')
				         .append($("<option></option>")
				         .attr("value", option.value)
				         .text(option.name));
					});
					_.each(toOptions,function(option ){
						$('#amp_year_range_to')
				         .append($("<option></option>")
				         .attr("value", option.value)
				         .text(option.name));
					});
				}				
			},	
			
			add_button : function() {
				this.settings_button = $(
						'<a href="#amp_settings" class="amp_settings button i18n" title="Settings">Settings</a>')
						.css(
								{
									'width' : "auto"
								});

				var $settings_li = $('<li></li>').append(
						this.settings_button);
				$(this.workspace.toolbar.el).find("ul").prepend($settings_li);
			},

			show : function(event, ui) {				
				$('#settings-invalid-year-range').hide();
				$('#settings-missing-values-error').hide();
				var self = this;
				$(this.el).toggle();
				var settings;
				settings = window.currentSettings;
				$('#amp_calendar').val(settings["2"]);
				$('#amp_calendar').trigger("change");
				$('#amp_currency').val(settings["1"]);
				if(settings.yearRange){
					$('#amp_year_range_from').val(settings.yearRange.yearFrom)
					$('#amp_year_range_to').val(settings.yearRange.yearTo)
				}		
				

				$(event.target).toggleClass('on');

				if ($(event.target).hasClass('on')) {
					$('#settings-container').show();
					$('#settings-missing-values-error').hide();
					$('#settings-container').css('width', 'auto');
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
			
			updateCurrenciesForCalendar: function() {
				var selectedCalendar = $('#amp_calendar').val();				
				//Note: uniq is used because the list has duplicated currencies.
				var availableCurrencies = _.uniq(_.findWhere(window.settings.calendarCurrencies, {id: selectedCalendar}).value.split(","));
				$("#amp_currency").empty();				
				this.populateCurrencyPopup(window.settings.allCurrencies, availableCurrencies);
				$('#amp_currency').val($("#amp_currency option:first").val());
			},
			
			populateCurrencyPopup: function(data, calendarCurrencies) {
				$.each(calendarCurrencies, function(index, object) {   
				     $('#amp_currency')
				         .append($("<option></option>")
				         .attr("value", _.find(data, function(item) {return item.id === object}).id)
				         .text(_.find(data, function(item) {return item.id === object}).name)); 
				});				
			}

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
