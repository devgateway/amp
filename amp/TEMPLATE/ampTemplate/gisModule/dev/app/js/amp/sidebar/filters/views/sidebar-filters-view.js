var fs = require('fs');
var _ = require('underscore');

var FiltersWidget = require('../../../filters/main');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/filters-sidebar-template.html', 'utf8');

require('jquery-ui/draggable');

module.exports = BaseControlView.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',

  events:{
    'click .accordion-heading': 'newlaunchFilter'
  },


  template: _.template(Template),

  initialize:function(options) {
    this.app = options.app;
    BaseControlView.prototype.initialize.apply(this, arguments);

  },


  render:function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    // Doesn't do anything for now, used to show filter titles / counts in sidebar.
    // _.each(this.filtersWidget.filtersWidgetsInstances, function(filtersWidget) {
    //   self.$('.filter-list').append(filtersWidget.renderTitle().titleEl);
    // });

    this.filtersWidget = new FiltersWidget({el:this.$('#filter-popup'), draggable: true });

    this.app.state.register(this, 'filters', {
      get: function() { return this.filtersWidget.serialize(); },
      set: function(state) { return this.filtersWidget.deserialize(state);},
      empty: null
    });

    return this;
  },


  newlaunchFilter:function() {
    var self = this;
    this.$('#filter-popup').show();

    // could do better, but must close accordion or get weird states from bootstrap and manual filter showing....
    this.filtersWidget.on('cancel', function() {
      self.$('.accordion-body').collapse('hide');
    });

    this.filtersWidget.on('apply', function() {
      //TODO: ...trigger something wider....or atttach fitler widget to app.data....
      self.$('.accordion-body').collapse('hide');
    });
  }
});

