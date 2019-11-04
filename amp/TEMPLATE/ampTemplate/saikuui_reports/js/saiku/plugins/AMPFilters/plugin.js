var reportType = Saiku.originalReportMetadata.reportMetadata.reportSpec.reportType;
var reportTypeCode = 'D';
if (reportType === 5) {
    reportTypeCode = 'P';
}
window.currentFilter = new ampFilter({
    el: $('#filter-popup'),
    draggable: true,
    caller: 'REPORTS',
    reportType: reportTypeCode
});

var AMPFilters = Backbone.View.extend({
			events : {
				'click .edit_amp_filter' : 'add_amp_filter'
			},

			initialize : function(args) {
				Saiku.logger.log("AMPFilters.initialize");
				var self = this;
				this.workspace = args.workspace;
				this.initialized = false;

				this.id = _.uniqueId("amp_filters_");
				$(this.el).attr({
					id : this.id
				});

				_.bindAll(this, "render", "show", "add_amp_filter");

				this.add_button();
				this.workspace.toolbar.amp_filters = this.show;

				$(this.workspace.el).find('.workspace_results').prepend($(this.el).hide());				
				window.currentFilter.showFilters();
				window.currentFilter.converted = false;
				$('#filter-popup').hide();

				Saiku.events.listenTo(window.currentFilter, 'cancel', function() {
					self.filters_button.toggleClass('on');
					$('#filter-popup').hide();
				});
				Saiku.events.listenTo(window.currentFilter, 'close', function() {
					self.filters_button.toggleClass('on');
					$('#filter-popup').hide();
				});
				Saiku.events.listenTo(window.currentFilter, 'apply', function(data) {
					var filterObject = window.currentFilter.serialize();
					self.workspace.query.run_query(filterObject || {}, null);
					self.filters_button.removeClass('on');
					$('#filter-popup').hide();
				});
				
				this.parseSavedFilters(this);
			},
			
			parseSavedFilters: function(obj) {
				Saiku.logger.log("AMPFilters.parseSavedFilters");
		        if (window.currentFilter !== undefined) {
		            window.currentFilter.loaded.done(function() {
			            var auxFilters = {filters: obj.workspace.query.get('filters')};;
			            auxFilters.includeLocationChildren = obj.workspace.query.get('includeLocationChildren');
						auxFilters['include-location-children'] = obj.workspace.query.get('includeLocationChildren');
			            window.currentFilter.deserialize(auxFilters, {
			            	silent : true
			            });
		            });		            
		            
		        }
			},
			
			deferredInitialization: function() {			
				Saiku.logger.log("AMPFilters.deferredInitialization");
				if (typeof args.workspace.amp_filters == "undefined") {
					args.workspace.amp_filters = new AMPFilters({
						workspace : args.workspace
					});
		            args.workspace.bind('query:result', function(args) {
		            	args.workspace.toolbar.$el.find(".amp_settings").removeClass("disabled_toolbar");
		            	args.workspace.toolbar.$el.find(".amp_filters").removeClass("disabled_toolbar")
		        	});
				}
			},

			add_button : function() {
				Saiku.logger.log("AMPFilters.add_button");
				this.filters_button = $(
						'<a href="#amp_filters" class="amp_filters button i18n" title="Filters">Filters</a>')
						.css(
								{
									'width' : "auto"
								});

				var $filters_li = $('<li class="seperator"></li>').append(
						this.filters_button);
				$(this.workspace.toolbar.el).find("ul").prepend($filters_li);
			},

			show : function(event, ui) {
				Saiku.logger.log("AMPFilters.show");
				var self = this;
				$(this.el).toggle();
				$(event.target).toggleClass('on');

				if ($(event.target).hasClass('on')) {
					// AMP-18921: workaround to the filters until they will be properly initialized, 
					// that should be done as part of filters widget improvement as a whole
					//this.workspace.query.initFilters();
					window.currentFilter.setStash();
					$('#filter-popup').show();
				} else {
					$('#filter-popup').hide();
				}
			},

			render_menu : function() {
				Saiku.logger.log("AMPFilters.render_menu");
				//alert("RENDERING FILTER");
			},

			render : function() {
				Saiku.logger.log("AMPFilters.render");
				$(this.el).empty();
			},

			add_amp_filter : function(event) {
				Saiku.logger.log("AMPFilters.add_amp_filter");
				var self = this;
			},

			save_amp_filter : function() {
				Saiku.logger.log("AMPFilters.save_amp_filter");
				var self = this;
				alert("To be implemented")
			},

		});

Saiku.events.bind('render:end', function(session) {
	function new_workspace(args) {
		Saiku.logger.log("AMPFilters.new_workspace");
		if (typeof args.workspace.amp_filters == "undefined") {
			args.workspace.amp_filters = new AMPFilters({
				workspace : args.workspace
			});
            args.workspace.bind('query:result', function(args) {
            	args.workspace.toolbar.$el.find(".amp_settings").removeClass("disabled_toolbar");
            	args.workspace.toolbar.$el.find(".amp_filters").removeClass("disabled_toolbar")
        	});
		}
	}

	function clear_workspace(args) {
		Saiku.logger.log("AMPFilters.clear_workspace");
		if (typeof args.workspace.amp_filters != "undefined") {
			$(args.workspace.amp_filters.el).hide();
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