var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LayersView = require('./layers/views/layers-view');
var FiltersView = require('./filters/views/filters-view');
var SearchView = require('./search/views/search-view');
var ToolsView = require('./tools/views/tools-view');
var SettingsView = require('./settings/views/settings-view');


var controlViews = [
  LayersView,
  SearchView,
  FiltersView,
  ToolsView,
  SettingsView
];

module.exports = Backbone.View.extend({

  events: {
    'hide.bs.collapse .sidebar-tool': 'hideAllPopovers',
    'show.bs.popover .layer-info': 'exclusiveShowPopover'
  },

  // Render entire geocoding view.
  render: function () {
    this.$el.append(_.map(controlViews, function(ControlView) {
      var view = new ControlView();
      return view.render().el;
    }));

    // sidebar popover init
    this.popovers = this.$('.layer-info');
    this.popovers.popover();

    // Temp dirty popover fix until 'exclusiveShowPopover' is fixed in next sprint.
    $('body').on('click', function (e) {
                            if ($(e.target).data('toggle') !== 'popover'
                                && $(e.target).parents('[data-toggle="popover"]').length === 0
                                && $(e.target).parents('.popover.in').length === 0) {
                                $('[data-toggle=popover]').popover('hide');
                            }
                        });

    return this;
  },

  hideAllPopovers: function() {
    console.log('hide all');
    this.popovers.popover('hide');
  },

//TODO: doesn't work, opening is a Backbone 'view', triggerer is a DOM element
  exclusiveShowPopover: function() {
    /*
    var opening = this;
    this.popovers.each(function(i, triggerer) {      
      console.log('open ',opening);
      console.log('trgigo ', triggerer);
      if (triggerer !== opening) {
        console.log('hide', i);
        $(triggerer).popover('hide');
      }
    });*/
  }

});
