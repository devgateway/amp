var AMPFilters = Backbone.View.extend({
			events : {
				'click .edit_amp_filter' : 'add_amp_filter'
			},

			initialize : function(args) {
		    	if(!Settings.AMP_REPORT_API_BRIDGE) return; 

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

				window.currentFilter = new ampFilter({el:$('#filters-container'), draggable:true})
				window.currentFilter.showFilters();
				$('#filters-container').hide();

				Saiku.events.listenTo(window.currentFilter, 'cancel', function() {
					self.filters_button.toggleClass('on');
					$('#filters-container').hide();
				});
				Saiku.events.listenTo(window.currentFilter, 'close', function() {
					self.filters_button.toggleClass('on');
					$('#filters-container').hide();
				});
				Saiku.events.listenTo(window.currentFilter, 'apply', function(data) {
					if(data.columnFilters || data.otherFilters) {
						self.workspace.query.run_query(window.currentFilter.serialize(), null);
						//self.filters_button.removeClass('on');
					}
					$('#filters-container').hide();
				});
			},

			add_button : function() {

				this.filters_button = $(
						'<a href="#amp_filters" class="amp_filters button i18n" title="AMP Filters">Filters</a>')
						.css(
								{
									'width' : "30px"
								});

				var $filters_li = $('<li class="seperator"></li>').append(
						this.filters_button);
				$(this.workspace.toolbar.el).find("ul").prepend($filters_li);
			},

			show : function(event, ui) {
				var self = this;
				$(this.el).toggle();
				$(event.target).toggleClass('on');

				if ($(event.target).hasClass('on')) {
					// AMP-18921: workaround to the filters until they will be properly initialized, 
					// that should be done as part of filters widget improvement as a whole
					if (!window.currentFilter.converted) {
						var blob = FilterUtils.convertJavaFiltersToJS(this.workspace.query.get('filters'));
						window.currentFilter.deserialize(blob, {
							silent : true
						});
						window.currentFilter.converted = true;
					}
					$('#filters-container').show();
				} else {
					$('#filters-container').hide();
				}

			},

			render_menu : function() {
				//alert("RENDERING FILTER");
			},

			render : function() {
				$(this.el).empty();
			},

			add_amp_filter : function(event) {
				var self = this;
			},

			save_amp_filter : function() {
				var self = this;
				alert("To be implemented")
			},

		});

Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
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
	}
	;

	Saiku.session.bind("workspace:new", new_workspace);
	Saiku.session.bind("workspace:clear", clear_workspace);
});


var FilterUtils = {};

FilterUtils.mapping = {
		"Contracting Agency": "Contracting Agency Id",
		"Executing Agency": "Executing Agency Id",
		"Implementing Agency": "Implementing Agency Id",
		"Beneficiary Agency": "Beneficiary Agency Id",
		"Status": "ActivityStatusList",
		"National Planning Objectives": "National Planning Objectives Level 1 Id",
		"Primary Program": "Primary",
		"Secondary Program": "Secondary",
		"On/Off/Treasury Budget": "ActivityBudgetList",
		"Donor Agency": "Donor Id",
		"Primary Sector": "Primary Sector Id"
}

FilterUtils.getFilterName = function(name) {
	var returnName = FilterUtils.mapping[name] || name;
	return returnName;
}

FilterUtils.getFilterValues = function(items) {
	if(items.length <= 0) return;
	var item = items[0];
	switch(item.filterType) {
		case "RANGE":
			return [item.min, item.max];
			break;
		case "SINGLE_VALUE":
			return parseInt(item.value);
			break;
		case "VALUES":
			return _.map(item.values, function(item){return parseInt(item)});
			break;
	}
	return;
}

FilterUtils.convertJavaFiltersToJS = function(data) {
	// Define structure for the blob object literal
	var blob = {
		otherFilters : {
			date : {
			}
		},
		columnFilters : {
		}
	};
	
	var filterRules = data.columnFilterRules;
	_.each(filterRules, function(item, i) {
		var filterName = FilterUtils.getFilterName(i);
		var values = FilterUtils.getFilterValues(item);
		blob.columnFilters[filterName] =  values;
	});
	return blob;
};

