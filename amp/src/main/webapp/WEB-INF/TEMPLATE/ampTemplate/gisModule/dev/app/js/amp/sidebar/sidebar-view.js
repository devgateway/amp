var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var ProjectsView = require('./layers/views/projects-view');
var StatisticalDataView = require('./layers/views/statistical-data-view');
var FiltersView = require('./filters/views/sidebar-filters-view');
//var LayerManager = require('./layers-manager/views/layers-manager-view');
//var SearchView = require('./search/views/search-view');
var ToolsView = require('./tools/views/tools-view');
var SettingsView = require('./settings/views/settings-view');


var controlViews = [
  {view: ProjectsView, name: 'projectsView'},
  {view: StatisticalDataView, name: 'statisticalDataView'},
  //LayerManager,
  {view: FiltersView, name: 'filtersView'},
  //SearchView, //disabled for 2.10
  {view: ToolsView, name: 'toolsView'},
  {view: SettingsView, name: 'settingsView'},  
];

module.exports = Backbone.View.extend({

  events: {
    'hide.bs.collapse .sidebar-tool': 'hideAllPopovers',
    'show.bs.popover .layer-info': 'exclusiveShowPopover'
  },

  initialize: function(options) {
    this.app = options.app;
    this.defaultOpenCollapsable = '#collapse-projectdata';
  },

  // Render entire geocoding view.
  render: function() {
	self = this;
    this.$el.append(_.map(controlViews, function(ControlView) {
      var view = new ControlView.view({app: this.app});
      // We save the reference of each child-view for future use.
      self[ControlView.name] = view;
      return view.render().el;
    }));

    // sidebar popover init

    //TODO(tdk): consider switching back to this.$()
    this.popovers = $('.layer-info');
    this.popovers.popover();

    // Temp dirty popover fix until 'exclusiveShowPopover' is fixed in next sprint.
    $('body').on('click', function(e) {
      if ($(e.target).data('toggle') !== 'popover' &&
          $(e.target).parents('[data-toggle="popover"]').length === 0 &&
          $(e.target).parents('.popover.in').length === 0) {
        $('[data-toggle=popover]').popover('hide');
      }
    });

    // Open default:
    //TODO(tdk): consider switching back to this.$()
    $(this.defaultOpenCollapsable).collapse('show');

    return this;
  },

  hideAllPopovers: function() {
    this.popovers.popover('hide');
  },

//TODO: doesn't work, opening is a Backbone 'view', triggerer is a DOM element
  exclusiveShowPopover: function() {
    /*
    var opening = this;
    this.popovers.each(function(i, triggerer) {
      if (triggerer !== opening) {
        $(triggerer).popover('hide');
      }
    });*/
  }

});
